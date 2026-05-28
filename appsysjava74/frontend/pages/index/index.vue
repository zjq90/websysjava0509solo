<template>
    <view class="container">
        <view class="header">
            <view class="search-bar">
                <u--input 
                    v-model="searchKey" 
                    placeholder="搜索活动名称" 
                    clearable
                    @confirm="handleSearch"
                >
                    <template #suffix>
                        <u-icon name="search" size="32" color="#999" @click="handleSearch"></u-icon>
                    </template>
                </u--input>
            </view>
            
            <u-tabs 
                :list="statusTabs" 
                :current="currentStatus"
                @change="handleStatusChange"
                lineWidth="40"
                activeColor="#3c9cff"
            ></u-tabs>
        </view>
        
        <view class="quick-actions" v-if="store.getters.isClubAdmin || store.getters.isAdmin">
            <view class="action-item" @click="goToPublish">
                <u-icon name="plus-circle" size="48" color="#3c9cff"></u-icon>
                <text>发布活动</text>
            </view>
            <view class="action-item" @click="goToScan">
                <u-icon name="scan" size="48" color="#5ac725"></u-icon>
                <text>扫码签到</text>
            </view>
        </view>
        
        <view class="activity-list" v-if="activityList.length > 0">
            <view 
                class="activity-card" 
                v-for="item in activityList" 
                :key="item.id"
                @click="goToDetail(item.id)"
            >
                <image class="poster" :src="item.posterUrl || '/static/default-poster.png'" mode="aspectFill"></image>
                <view class="activity-info">
                    <view class="activity-title ellipsis">{{ item.name }}</view>
                    <view class="activity-meta">
                        <view class="meta-item">
                            <u-icon name="calendar" size="24" color="#999"></u-icon>
                            <text>{{ formatDate(item.startTime) }}</text>
                        </view>
                        <view class="meta-item">
                            <u-icon name="map" size="24" color="#999"></u-icon>
                            <text class="ellipsis">{{ item.location }}</text>
                        </view>
                        <view class="meta-item">
                            <u-icon name="account" size="24" color="#999"></u-icon>
                            <text>{{ item.registeredCount }}/{{ item.quota }}人</text>
                        </view>
                    </view>
                    <view class="activity-footer">
                        <u-tag 
                            :type="getStatusType(item.status)" 
                            :text="getStatusText(item.status)"
                            size="mini"
                        ></u-tag>
                        <view class="scope-tag">
                            {{ item.registrationScope === 'ALL_STUDENTS' ? '全校开放' : '仅社团成员' }}
                        </view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-else-if="!loading">
            <u-empty mode="list" text="暂无活动"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="activityList.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
    </view>
</template>

<script setup>
import { ref, onMounted, onPullDownRefresh } from 'vue'
import { useStore } from 'vuex'
import { activityApi } from '@/utils/api'
import dayjs from 'dayjs'

const store = useStore()
const searchKey = ref('')
const currentStatus = ref(0)
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 10
const hasMore = ref(true)
const activityList = ref([])

const statusTabs = [
    { name: '全部' },
    { name: '报名中' },
    { name: '进行中' },
    { name: '已结束' }
]

const statusMap = {
    DRAFT: { text: '草稿', type: 'info' },
    PUBLISHED: { text: '已发布', type: 'primary' },
    REGISTRATION_OPEN: { text: '报名中', type: 'success' },
    REGISTRATION_CLOSED: { text: '报名结束', type: 'warning' },
    ONGOING: { text: '进行中', type: 'primary' },
    COMPLETED: { text: '已结束', type: 'info' },
    CANCELLED: { text: '已取消', type: 'error' }
}

const getStatusText = (status) => statusMap[status]?.text || status
const getStatusType = (status) => statusMap[status]?.type || 'info'

const formatDate = (date) => dayjs(date).format('MM-DD HH:mm')

const loadData = async (refresh = false) => {
    if (loading.value) return
    
    loading.value = true
    if (refresh) {
        pageNum.value = 1
        hasMore.value = true
        activityList.value = []
        loadStatus.value = 'loading'
    }
    
    try {
        const params = {
            page: pageNum.value,
            size: pageSize.value,
            keyword: searchKey.value
        }
        
        if (currentStatus.value === 1) {
            params.status = 'REGISTRATION_OPEN'
        } else if (currentStatus.value === 2) {
            params.status = 'ONGOING'
        } else if (currentStatus.value === 3) {
            params.status = 'COMPLETED'
        }
        
        const res = await activityApi.getList(params)
        const newList = res.content || res.data?.content || []
        
        if (refresh) {
            activityList.value = newList
        } else {
            activityList.value = [...activityList.value, ...newList]
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

const handleSearch = () => {
    loadData(true)
}

const handleStatusChange = (item) => {
    currentStatus.value = item.index
    loadData(true)
}

const goToDetail = (id) => {
    uni.navigateTo({
        url: `/pages/activity/detail?id=${id}`
    })
}

const goToPublish = () => {
    uni.navigateTo({
        url: '/pages/activity/publish'
    })
}

const goToScan = () => {
    uni.navigateTo({
        url: '/pages/scan/scan'
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

.header {
    background: #fff;
    padding: 20rpx;
    position: sticky;
    top: 0;
    z-index: 100;
}

.search-bar {
    margin-bottom: 20rpx;
}

.quick-actions {
    display: flex;
    background: #fff;
    margin: 20rpx;
    border-radius: 16rpx;
    padding: 30rpx 0;
    
    .action-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        
        text {
            margin-top: 12rpx;
            font-size: 26rpx;
            color: #333;
        }
    }
}

.activity-list {
    padding: 0 20rpx;
}

.activity-card {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
    
    .poster {
        width: 100%;
        height: 280rpx;
    }
    
    .activity-info {
        padding: 24rpx;
        
        .activity-title {
            font-size: 32rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 16rpx;
        }
        
        .activity-meta {
            .meta-item {
                display: flex;
                align-items: center;
                font-size: 26rpx;
                color: #666;
                margin-bottom: 10rpx;
                
                text {
                    margin-left: 8rpx;
                    flex: 1;
                }
            }
        }
        
        .activity-footer {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 16rpx;
            
            .scope-tag {
                font-size: 24rpx;
                color: #999;
            }
        }
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
