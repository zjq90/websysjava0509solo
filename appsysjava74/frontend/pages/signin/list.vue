<template>
    <view class="container">
        <view class="stats-card card">
            <view class="stat-item">
                <text class="stat-num text-primary">{{ statistics?.registeredCount || 0 }}</text>
                <text class="stat-label">报名人数</text>
            </view>
            <view class="stat-item">
                <text class="stat-num text-success">{{ statistics?.signedCount || 0 }}</text>
                <text class="stat-label">已签到</text>
            </view>
            <view class="stat-item">
                <text class="stat-num text-warning">{{ statistics?.absentCount || 0 }}</text>
                <text class="stat-label">未签到</text>
            </view>
            <view class="stat-item">
                <text class="stat-num text-info">{{ signRate }}%</text>
                <text class="stat-label">签到率</text>
            </view>
        </view>
        
        <view class="filter-bar">
            <u--input 
                v-model="searchKey" 
                placeholder="搜索姓名/学号" 
                clearable
                @confirm="loadData"
            ></u--input>
        </view>
        
        <view class="action-bar">
            <u-button type="primary" size="small" @click="goToQrcode">签到二维码</u-button>
            <u-button type="warning" size="small" @click="goToMakeup">手动补签</u-button>
            <u-button type="success" size="small" @click="exportList">导出签到表</u-button>
        </view>
        
        <view class="signin-list">
            <view 
                class="signin-item" 
                v-for="item in list" 
                :key="item.id"
            >
                <view class="signin-avatar">
                    <u-avatar 
                        :text="item.realName" 
                        :bgColor="getAvatarColor(item.realName)"
                        size="60"
                    ></u-avatar>
                </view>
                
                <view class="signin-info">
                    <view class="signin-header">
                        <text class="signin-name">{{ item.realName }}</text>
                        <u-tag 
                            :type="getStatusType(item.status)" 
                            :text="getStatusText(item.status)"
                            size="mini"
                        ></u-tag>
                    </view>
                    <view class="signin-meta">
                        <text>{{ item.studentNo }}</text>
                        <text>{{ item.department }}</text>
                    </view>
                    <view class="signin-time" v-if="item.signInTime">
                        <u-icon name="clock" size="24" color="#999"></u-icon>
                        <text>{{ formatTime(item.signInTime) }}</text>
                        <text class="method">{{ item.signInMethod === 'QR_CODE' ? '扫码签到' : '手动补签' }}</text>
                    </view>
                </view>
                
                <view class="signin-action" v-if="item.status === 'ABSENT'">
                    <u-button type="warning" size="mini" @click="makeUp(item)">补签</u-button>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="list" text="暂无签到记录"></u-empty>
        </view>
    </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { signInApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const activityId = ref(route.query.activityId)
const searchKey = ref('')
const loading = ref(false)
const list = ref([])
const statistics = ref(null)

const signRate = computed(() => {
    if (!statistics.value || statistics.value.registeredCount === 0) return 0
    return Math.round((statistics.value.signedCount / statistics.value.registeredCount) * 100)
})

const statusMap = {
    SIGNED: { text: '已签到', type: 'success' },
    LATE: { text: '迟到', type: 'warning' },
    MAKE_UP: { text: '已补签', type: 'primary' },
    ABSENT: { text: '未签到', type: 'error' }
}

const getStatusText = (status) => statusMap[status]?.text || status
const getStatusType = (status) => statusMap[status]?.type || 'info'

const formatTime = (date) => dayjs(date).format('MM-DD HH:mm:ss')

const getAvatarColor = (name) => {
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#909399', '#722ed1']
    const index = name.charCodeAt(0) % colors.length
    return colors[index]
}

const loadData = async () => {
    loading.value = true
    try {
        const [signList, stats] = await Promise.all([
            signInApi.getList(activityId.value),
            signInApi.getStatistics(activityId.value)
        ])
        
        list.value = signList
        statistics.value = stats
        
        if (searchKey.value) {
            const key = searchKey.value.toLowerCase()
            list.value = list.value.filter(item => 
                item.realName.includes(key) || 
                item.studentNo.toLowerCase().includes(key) ||
                item.department.includes(key)
            )
        }
    } catch (e) {
        console.error('加载签到数据失败:', e)
    } finally {
        loading.value = false
    }
}

const makeUp = async (item) => {
    uni.showModal({
        title: '手动补签',
        content: `确定为 ${item.realName} 进行补签？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    await signInApi.makeUp({
                        activityId: activityId.value,
                        userId: item.userId,
                        reason: '手动补签'
                    })
                    uni.showToast({ title: '补签成功', icon: 'success' })
                    loadData()
                } catch (e) {
                    console.error('补签失败:', e)
                }
            }
        }
    })
}

const goToQrcode = () => {
    uni.navigateTo({
        url: `/pages/signin/qrcode?activityId=${activityId.value}`
    })
}

const goToMakeup = () => {
    uni.navigateTo({
        url: `/pages/signin/makeup?activityId=${activityId.value}`
    })
}

const exportList = async () => {
    try {
        const filePath = await signInApi.exportList(activityId.value, {
            keyword: searchKey.value
        })
        uni.openDocument({
            filePath,
            showMenu: true,
            success: () => {
                uni.showToast({ title: '导出成功', icon: 'success' })
            }
        })
    } catch (e) {
        console.error('导出失败:', e)
    }
}

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding: 20rpx;
}

.card {
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
}

.stats-card {
    display: flex;
    justify-content: space-around;
    
    .stat-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .stat-num {
            font-size: 40rpx;
            font-weight: bold;
        }
        
        .stat-label {
            font-size: 24rpx;
            color: #999;
            margin-top: 4rpx;
        }
    }
}

.filter-bar {
    margin-bottom: 20rpx;
}

.action-bar {
    display: flex;
    gap: 16rpx;
    margin-bottom: 20rpx;
    
    button {
        flex: 1;
    }
}

.signin-list {
    .signin-item {
        display: flex;
        align-items: center;
        background: #fff;
        border-radius: 16rpx;
        padding: 24rpx;
        margin-bottom: 16rpx;
        
        .signin-avatar {
            margin-right: 16rpx;
        }
        
        .signin-info {
            flex: 1;
            
            .signin-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 8rpx;
                
                .signin-name {
                    font-size: 30rpx;
                    font-weight: bold;
                    color: #333;
                }
            }
            
            .signin-meta {
                display: flex;
                gap: 20rpx;
                font-size: 24rpx;
                color: #666;
                margin-bottom: 4rpx;
            }
            
            .signin-time {
                display: flex;
                align-items: center;
                gap: 8rpx;
                font-size: 24rpx;
                color: #999;
                
                .method {
                    margin-left: 16rpx;
                    padding: 2rpx 12rpx;
                    background: #f0f0f0;
                    border-radius: 8rpx;
                }
            }
        }
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
