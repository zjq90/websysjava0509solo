<template>
    <view class="container">
        <view class="header" v-if="activity">
            <view class="activity-name">{{ activity.name }}</view>
            <view class="activity-time">{{ formatDateTime(activity.startTime) }}</view>
        </view>
        
        <view class="stats-bar">
            <view class="stat-item">
                <text class="stat-label">报名人数</text>
                <text class="stat-value primary">{{ statistics?.registeredCount || 0 }}</text>
            </view>
            <view class="stat-item">
                <text class="stat-label">已签到</text>
                <text class="stat-value success">{{ statistics?.signedInCount || 0 }}</text>
            </view>
            <view class="stat-item">
                <text class="stat-label">未签到</text>
                <text class="stat-value warning">{{ statistics?.absentCount || 0 }}</text>
            </view>
        </view>
        
        <view class="filter-section">
            <u-tabs 
                :list="filterTabs" 
                :current="currentFilter"
                @change="handleFilterChange"
                lineWidth="30"
                activeColor="#3c9cff"
            ></u-tabs>
        </view>
        
        <view class="search-section">
            <view class="search-bar">
                <u-icon name="search" size="28" color="#999"></u-icon>
                <input 
                    type="text" 
                    v-model="keyword" 
                    placeholder="搜索姓名/学号"
                    @confirm="handleSearch"
                />
            </view>
        </view>
        
        <view class="registration-list">
            <view 
                class="reg-item" 
                v-for="item in list" 
                :key="item.id"
            >
                <view class="user-info">
                    <u-avatar 
                        :text="item.studentName?.charAt(0)" 
                        size="80"
                        :bgColor="getAvatarColor(item.id)"
                    ></u-avatar>
                    <view class="user-detail">
                        <view class="user-name">{{ item.studentName }}</view>
                        <view class="user-meta">
                            <text>{{ item.department }}</text>
                            <text class="divider">|</text>
                            <text>{{ item.studentNo }}</text>
                        </view>
                    </view>
                </view>
                
                <view class="status-info">
                    <u-tag 
                        :type="item.signedIn ? 'success' : 'warning'" 
                        :text="item.signedIn ? '已签到' : '未签到'"
                        size="mini"
                    ></u-tag>
                    <view class="signin-time" v-if="item.signInTime">
                        {{ formatTime(item.signInTime) }}
                    </view>
                </view>
                
                <view class="action-btn" v-if="!item.signedIn">
                    <u-button 
                        type="primary" 
                        size="mini" 
                        @click="handleMakeup(item)"
                    >
                        补签
                    </u-button>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="list" text="暂无数据"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="list.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
    </view>
</template>

<script setup>
import { ref, onMounted, onPullDownRefresh, computed } from 'vue'
import { useRoute } from 'vue-router'
import { activityApi, signInApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const activityId = computed(() => route.query.activityId || route.params.activityId)

const currentFilter = ref(1)
const keyword = ref('')
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 10
const hasMore = ref(true)
const activity = ref(null)
const statistics = ref(null)
const list = ref([])

const filterTabs = [
    { name: '全部' },
    { name: '未签到' },
    { name: '已签到' }
]

const formatDateTime = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')
const formatTime = (date) => dayjs(date).format('HH:mm:ss')

const getAvatarColor = (id) => {
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#722ed1', '#13c2c2']
    return colors[id % colors.length]
}

const loadActivity = async () => {
    try {
        const res = await activityApi.getById(activityId.value)
        activity.value = res.data || res
    } catch (e) {
        console.error('加载活动信息失败:', e)
    }
}

const loadStatistics = async () => {
    try {
        const res = await signInApi.getStatistics(activityId.value)
        statistics.value = res.data || res
    } catch (e) {
        console.error('加载统计数据失败:', e)
    }
}

const loadData = async (refresh = false) => {
    if (loading.value) return
    
    loading.value = true
    if (refresh) {
        pageNum.value = 1
        hasMore.value = true
        list.value = []
        loadStatus.value = 'loading'
    }
    
    try {
        const params = {
            page: pageNum.value,
            size: pageSize.value,
            keyword: keyword.value || undefined
        }
        
        if (currentFilter.value === 1) {
            params.signedIn = false
        } else if (currentFilter.value === 2) {
            params.signedIn = true
        }
        
        const res = await signInApi.getSignInList(activityId.value, params)
        const newList = res.content || res.data?.content || []
        
        if (refresh) {
            list.value = newList
        } else {
            list.value = [...list.value, ...newList]
        }
        
        hasMore.value = newList.length >= pageSize.value
        loadStatus.value = hasMore.value ? 'loadmore' : 'nomore'
    } catch (e) {
        loadStatus.value = 'loadmore'
    } finally {
        loading.value = false
        if (refresh) {
            uni.stopPullDownRefresh()
        }
    }
}

const loadMore = () => {
    if (!hasMore.value || loading.value) return
    pageNum.value++
    loadData()
}

const handleFilterChange = (item) => {
    currentFilter.value = item.index
    loadData(true)
}

const handleSearch = () => {
    loadData(true)
}

const handleMakeup = (item) => {
    uni.showModal({
        title: '手动补签',
        content: `确定要为 ${item.studentName} 进行补签吗？`,
        success: async (res) => {
            if (res.confirm) {
                try {
                    await signInApi.manualSignIn(activityId.value, item.id)
                    uni.showToast({ title: '补签成功', icon: 'success' })
                    loadData(true)
                    loadStatistics()
                } catch (e) {
                    console.error('补签失败:', e)
                    uni.showToast({ title: '补签失败', icon: 'none' })
                }
            }
        }
    })
}

onMounted(() => {
    loadActivity()
    loadStatistics()
    loadData()
})

onPullDownRefresh(() => {
    loadData(true)
    loadStatistics()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 40rpx 30rpx;
    color: #fff;
    
    .activity-name {
        font-size: 36rpx;
        font-weight: bold;
        margin-bottom: 8rpx;
    }
    
    .activity-time {
        font-size: 26rpx;
        opacity: 0.9;
    }
}

.stats-bar {
    display: flex;
    background: #fff;
    margin: -20rpx 20rpx 0;
    border-radius: 16rpx;
    padding: 24rpx 0;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    
    .stat-item {
        flex: 1;
        text-align: center;
        
        .stat-label {
            display: block;
            font-size: 24rpx;
            color: #999;
            margin-bottom: 4rpx;
        }
        
        .stat-value {
            font-size: 36rpx;
            font-weight: bold;
            
            &.primary { color: #3c9cff; }
            &.success { color: #5ac725; }
            &.warning { color: #f9ae3d; }
        }
    }
}

.filter-section {
    background: #fff;
    margin: 20rpx 20rpx 0;
    border-radius: 16rpx;
    padding: 0 16rpx;
}

.search-section {
    background: #fff;
    padding: 16rpx 20rpx;
    margin: 0 20rpx 20rpx;
    border-radius: 0 0 16rpx 16rpx;
    
    .search-bar {
        display: flex;
        align-items: center;
        background: #f5f5f5;
        border-radius: 40rpx;
        padding: 0 24rpx;
        height: 64rpx;
        
        input {
            flex: 1;
            margin-left: 12rpx;
            font-size: 26rpx;
            height: 64rpx;
        }
    }
}

.registration-list {
    padding: 0 20rpx;
}

.reg-item {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;
    
    .user-info {
        flex: 1;
        display: flex;
        align-items: center;
        gap: 16rpx;
        
        .user-detail {
            flex: 1;
            
            .user-name {
                font-size: 28rpx;
                color: #333;
                font-weight: 500;
                margin-bottom: 4rpx;
            }
            
            .user-meta {
                font-size: 22rpx;
                color: #999;
                display: flex;
                align-items: center;
                gap: 8rpx;
                
                .divider {
                    color: #e0e0e0;
                }
            }
        }
    }
    
    .status-info {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        gap: 4rpx;
        margin-right: 16rpx;
        
        .signin-time {
            font-size: 22rpx;
            color: #5ac725;
        }
    }
    
    .action-btn {
        margin-left: 16rpx;
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
