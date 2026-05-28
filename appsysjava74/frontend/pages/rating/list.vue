<template>
    <view class="container">
        <view class="header" v-if="activity">
            <view class="activity-name">{{ activity.name }}</view>
            <view class="activity-info">
                <text>平均评分</text>
                <view class="avg-score">
                    <u-rate 
                        :value="averageRating" 
                        readonly 
                        size="28"
                        activeColor="#f9ae3d"
                    ></u-rate>
                    <text class="score-text">{{ averageRating.toFixed(1) }}</text>
                </view>
            </view>
        </view>
        
        <view class="summary-stats" v-if="ratingStats">
            <view class="stats-item" v-for="item in ratingDistribution" :key="item.stars">
                <view class="stars-label">{{ item.stars }}星</view>
                <view class="progress-bar">
                    <view class="progress-fill" :style="{ width: item.percentage + '%' }"></view>
                </view>
                <view class="count">{{ item.count }}人</view>
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
        
        <view class="rating-list">
            <view 
                class="rating-item" 
                v-for="item in list" 
                :key="item.id"
            >
                <view class="rating-header">
                    <view class="user-info">
                        <u-avatar 
                            :text="item.anonymous ? '匿' : item.userName?.charAt(0)" 
                            size="80"
                            :bgColor="getAvatarColor(item.id)"
                        ></u-avatar>
                        <view class="user-detail">
                            <view class="user-name">{{ item.anonymous ? '匿名用户' : item.userName }}</view>
                            <view class="user-meta">
                                {{ item.department || '' }} {{ item.major || '' }}
                            </view>
                        </view>
                    </view>
                    <view class="rating-score">
                        <u-rate 
                            :value="item.overallRating" 
                            readonly 
                            size="22"
                            activeColor="#f9ae3d"
                        ></u-rate>
                        <text class="score-num">{{ item.overallRating }}.0</text>
                    </view>
                </view>
                
                <view class="sub-ratings" v-if="item.subRatings">
                    <view class="sub-item" v-for="(value, key) in item.subRatings" :key="key">
                        <text class="sub-label">{{ getSubRatingLabel(key) }}</text>
                        <u-rate 
                            :value="value" 
                            readonly 
                            size="18"
                            activeColor="#f9ae3d"
                        ></u-rate>
                    </view>
                </view>
                
                <view class="rating-content" v-if="item.comment">
                    {{ item.comment }}
                </view>
                
                <view class="rating-suggestion" v-if="item.suggestion">
                    <view class="suggestion-label">建议:</view>
                    <view class="suggestion-content">{{ item.suggestion }}</view>
                </view>
                
                <view class="rating-footer">
                    <text class="rating-time">{{ formatDate(item.createTime) }}</text>
                    <view class="reply-section" v-if="item.reply && (store.getters.isClubAdmin || store.getters.isAdmin)">
                        <u-tag type="primary" size="mini" text="已回复"></u-tag>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="list.length === 0 && !loading">
            <u-empty mode="comment" text="暂无评价"></u-empty>
        </view>
        
        <u-loadmore 
            :status="loadStatus" 
            v-if="list.length > 0"
            @loadmore="loadMore"
        ></u-loadmore>
    </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onPullDownRefresh } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { activityApi, ratingApi } from '@/utils/api'
import dayjs from 'dayjs'

const store = useStore()
const route = useRoute()
const activityId = computed(() => route.query.activityId || route.params.activityId)

const currentFilter = ref(0)
const loading = ref(false)
const loadStatus = ref('loadmore')
const pageNum = ref(1)
const pageSize = 10
const hasMore = ref(true)
const activity = ref(null)
const ratingStats = ref(null)
const list = ref([])

const filterTabs = [
    { name: '全部' },
    { name: '5星' },
    { name: '4星' },
    { name: '3星' },
    { name: '2星' },
    { name: '1星' }
]

const subRatingLabels = {
    content: '活动内容',
    organization: '组织安排',
    venue: '场地环境'
}

const getSubRatingLabel = (key) => subRatingLabels[key] || key

const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

const getAvatarColor = (id) => {
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#722ed1', '#13c2c2']
    return colors[id % colors.length]
}

const averageRating = computed(() => {
    if (!ratingStats.value) return 0
    return ratingStats.value.averageRating || 0
})

const ratingDistribution = computed(() => {
    if (!ratingStats.value) return []
    const distribution = ratingStats.value.distribution || {}
    let total = Object.values(distribution).reduce((sum, count) => sum + count, 0) || 1
    
    return [5, 4, 3, 2, 1].map(stars => ({
        stars,
        count: distribution[stars] || 0,
        percentage: ((distribution[stars] || 0) / total) * 100
    }))
})

const loadActivity = async () => {
    try {
        const res = await activityApi.getById(activityId.value)
        activity.value = res.data || res
    } catch (e) {
        console.error('加载活动信息失败:', e)
    }
}

const loadStats = async () => {
    try {
        const res = await ratingApi.getStatistics(activityId.value)
        ratingStats.value = res.data || res
    } catch (e) {
        console.error('加载评分统计失败:', e)
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
            size: pageSize.value
        }
        
        if (currentFilter.value > 0) {
            params.rating = 6 - currentFilter.value
        }
        
        const res = await ratingApi.getByActivityId(activityId.value, params)
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

onMounted(() => {
    loadActivity()
    loadStats()
    loadData()
})

onPullDownRefresh(() => {
    loadStats()
    loadData(true)
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
        margin-bottom: 20rpx;
    }
    
    .activity-info {
        display: flex;
        align-items: center;
        gap: 16rpx;
        font-size: 26rpx;
        
        .avg-score {
            display: flex;
            align-items: center;
            gap: 8rpx;
            
            .score-text {
                font-size: 32rpx;
                font-weight: bold;
            }
        }
    }
}

.summary-stats {
    background: #fff;
    margin: -20rpx 20rpx 20rpx;
    border-radius: 16rpx;
    padding: 24rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    
    .stats-item {
        display: flex;
        align-items: center;
        gap: 16rpx;
        margin-bottom: 12rpx;
        
        &:last-child {
            margin-bottom: 0;
        }
        
        .stars-label {
            width: 80rpx;
            font-size: 24rpx;
            color: #666;
        }
        
        .progress-bar {
            flex: 1;
            height: 12rpx;
            background: #f0f0f0;
            border-radius: 6rpx;
            overflow: hidden;
            
            .progress-fill {
                height: 100%;
                background: #f9ae3d;
                border-radius: 6rpx;
                transition: width 0.3s ease;
            }
        }
        
        .count {
            width: 80rpx;
            font-size: 24rpx;
            color: #999;
            text-align: right;
        }
    }
}

.filter-section {
    background: #fff;
    margin: 0 20rpx 20rpx;
    border-radius: 16rpx;
    padding: 0 16rpx;
}

.rating-list {
    padding: 0 20rpx;
}

.rating-item {
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;
    
    .rating-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16rpx;
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 16rpx;
            
            .user-detail {
                .user-name {
                    font-size: 28rpx;
                    color: #333;
                    font-weight: 500;
                    margin-bottom: 4rpx;
                }
                
                .user-meta {
                    font-size: 22rpx;
                    color: #999;
                }
            }
        }
        
        .rating-score {
            display: flex;
            align-items: center;
            gap: 6rpx;
            
            .score-num {
                font-size: 24rpx;
                color: #f9ae3d;
                font-weight: bold;
            }
        }
    }
    
    .sub-ratings {
        padding-left: 96rpx;
        margin-bottom: 16rpx;
        
        .sub-item {
            display: flex;
            align-items: center;
            margin-bottom: 8rpx;
            
            .sub-label {
                width: 120rpx;
                font-size: 22rpx;
                color: #999;
            }
        }
    }
    
    .rating-content {
        padding-left: 96rpx;
        font-size: 26rpx;
        color: #333;
        line-height: 1.6;
        margin-bottom: 12rpx;
    }
    
    .rating-suggestion {
        padding-left: 96rpx;
        margin-bottom: 16rpx;
        
        .suggestion-label {
            font-size: 24rpx;
            color: #3c9cff;
            margin-bottom: 4rpx;
        }
        
        .suggestion-content {
            font-size: 24rpx;
            color: #666;
            line-height: 1.6;
        }
    }
    
    .rating-footer {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 96rpx;
        
        .rating-time {
            font-size: 22rpx;
            color: #999;
        }
    }
}

.empty-state {
    margin-top: 100rpx;
}
</style>
