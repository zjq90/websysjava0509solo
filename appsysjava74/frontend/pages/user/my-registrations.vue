<template>
    <view class="container">
        <view class="filter-tabs">
            <u-tabs 
                :list="statusTabs" 
                :current="currentStatus"
                @change="handleStatusChange"
                lineWidth="40"
                activeColor="#3c9cff"
            ></u-tabs>
        </view>
        
        <view class="registration-list">
            <view 
                class="reg-card" 
                v-for="item in list" 
                :key="item.id"
                @click="goToDetail(item.activityId)"
            >
                <view class="reg-header">
                    <view class="activity-name ellipsis">{{ item.activityName }}</view>
                    <u-tag 
                        :type="getStatusType(item.status)" 
                        :text="getStatusText(item.status)"
                        size="mini"
                    ></u-tag>
                </view>
                
                <view class="reg-meta">
                    <view class="meta-item">
                        <u-icon name="calendar" size="24" color="#999"></u-icon>
                        <text>{{ formatDate(item.activityStartTime) }}</text>
                    </view>
                    <view class="meta-item">
                        <u-icon name="map" size="24" color="#999"></u-icon>
                        <text class="ellipsis">{{ item.activityLocation }}</text>
                    </view>
                </view>
                
                <view class="reg-footer">
                    <view class="signin-status" v-if="item.status === 'APPROVED'">
                        <u-tag 
                            :type="item.signedIn ? 'success' : 'warning'" 
                            :text="item.signedIn ? '已签到' : '未签到'"
                            size="mini"
                        ></u-tag>
                        <text class="signin-time" v-if="item.signInTime">{{ formatTime(item.signInTime) }}</text>
                    </view>
                    
                    <view class="reg-actions">
                        <u-button 
                            type="success" 
                            size="mini" 
                            v-if="canSignIn(item)"
                            @click.stop="goToScan"
                        >
                            签到
                        </u-button>
                        <u-button 
                            type="warning" 
                            size="mini" 
                            v-if="canRate(item)"
                            @click.stop="goToRate(item.activityId)"
                        >
                            评价
                        </u-button>
                        <u-button 
                            type="error" 
                            size="mini" 
                            v-if="item.status === 'APPROVED' && canCancel(item)"
                            @click.stop="cancelRegistration(item)"
                        >
                            取消报名
                        </u-button>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="list" text="暂无报名记录"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="list.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
    </view>
</template>

<script setup>
import { ref, onMounted, onPullDownRefresh } from 'vue'
import { registrationApi } from '@/utils/api'
import dayjs from 'dayjs'

const currentStatus = ref(0)
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 10
const hasMore = ref(true)
const list = ref([])

const statusTabs = [
    { name: '全部' },
    { name: '待审核' },
    { name: '已通过' },
    { name: '已拒绝' }
]

const statusMap = {
    PENDING: { text: '待审核', type: 'warning' },
    APPROVED: { text: '已通过', type: 'success' },
    REJECTED: { text: '已拒绝', type: 'error' },
    CANCELLED: { text: '已取消', type: 'info' }
}

const getStatusText = (status) => statusMap[status]?.text || status
const getStatusType = (status) => statusMap[status]?.type || 'info'

const formatDate = (date) => dayjs(date).format('MM-DD HH:mm')
const formatTime = (date) => dayjs(date).format('HH:mm:ss')

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
            size: pageSize.value
        }
        
        if (currentStatus.value === 1) {
            params.status = 'PENDING'
        } else if (currentStatus.value === 2) {
            params.status = 'APPROVED'
        } else if (currentStatus.value === 3) {
            params.status = 'REJECTED'
        }
        
        const res = await registrationApi.getMyList(params)
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

const handleStatusChange = (item) => {
    currentStatus.value = item.index
    loadData(true)
}

const canSignIn = (item) => {
    if (item.status !== 'APPROVED' || item.signedIn) return false
    const now = dayjs()
    const startTime = dayjs(item.activityStartTime).subtract(30, 'minute')
    const endTime = dayjs(item.activityStartTime).add(30, 'minute')
    return now.isAfter(startTime) && now.isBefore(endTime)
}

const canRate = (item) => {
    if (item.status !== 'APPROVED') return false
    const now = dayjs()
    const endTime = dayjs(item.activityEndTime)
    return now.isAfter(endTime)
}

const canCancel = (item) => {
    const now = dayjs()
    const startTime = dayjs(item.activityStartTime)
    return now.isBefore(startTime)
}

const goToDetail = (activityId) => {
    uni.navigateTo({
        url: `/pages/activity/detail?id=${activityId}`
    })
}

const goToScan = () => {
    uni.navigateTo({
        url: '/pages/scan/scan'
    })
}

const goToRate = (activityId) => {
    uni.navigateTo({
        url: `/pages/rating/submit?activityId=${activityId}`
    })
}

const cancelRegistration = async (item) => {
    uni.showModal({
        title: '取消报名',
        content: '确定要取消报名吗？',
        success: async (res) => {
            if (res.confirm) {
                try {
                    await registrationApi.cancel(item.id)
                    uni.showToast({ title: '取消成功', icon: 'success' })
                    loadData(true)
                } catch (e) {
                    console.error('取消报名失败:', e)
                }
            }
        }
    })
}

onMounted(() => {
    loadData()
})

onPullDownRefresh(() => {
    loadData(true)
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.filter-tabs {
    background: #fff;
    position: sticky;
    top: 0;
    z-index: 100;
}

.registration-list {
    padding: 20rpx;
}

.reg-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;
    
    .reg-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16rpx;
        
        .activity-name {
            flex: 1;
            font-size: 30rpx;
            font-weight: bold;
            color: #333;
            margin-right: 16rpx;
        }
    }
    
    .reg-meta {
        margin-bottom: 16rpx;
        
        .meta-item {
            display: flex;
            align-items: center;
            font-size: 24rpx;
            color: #666;
            margin-bottom: 8rpx;
            
            text {
                margin-left: 8rpx;
                flex: 1;
            }
        }
    }
    
    .reg-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-top: 16rpx;
        border-top: 1rpx solid #f5f5f5;
        
        .signin-status {
            display: flex;
            align-items: center;
            gap: 8rpx;
            
            .signin-time {
                font-size: 24rpx;
                color: #999;
            }
        }
        
        .reg-actions {
            display: flex;
            gap: 12rpx;
        }
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
