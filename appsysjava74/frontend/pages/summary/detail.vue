<template>
    <view class="container">
        <view class="header" v-if="summary">
            <view class="poster" v-if="summary.activityPoster">
                <image :src="summary.activityPoster" mode="aspectFill"></image>
            </view>
            <view class="header-content">
                <view class="title">{{ summary.activityName }}</view>
                <view class="meta">
                    <u-icon name="calendar" size="24" color="#999"></u-icon>
                    <text>{{ formatDate(summary.activityStartTime) }}</text>
                </view>
                <view class="club">
                    <u-tag type="primary" :text="summary.clubName" size="mini"></u-tag>
                </view>
            </view>
        </view>
        
        <view class="summary-section" v-if="summary">
            <view class="section-title">
                <u-icon name="file-text" size="32" color="#3c9cff"></u-icon>
                <text>活动总结</text>
            </view>
            
            <view class="summary-title">{{ summary.title }}</view>
            <view class="publish-time">发布时间: {{ formatDate(summary.publishTime) }}</view>
            
            <view class="photo-grid" v-if="summary.photos && summary.photos.length > 0">
                <image 
                    v-for="(photo, index) in summary.photos" 
                    :key="index"
                    :src="photo" 
                    mode="aspectFill"
                    class="photo-item"
                ></image>
            </view>
            
            <view class="content-section" v-if="summary.highlights">
                <view class="content-title">
                    <u-icon name="star-fill" size="24" color="#f9ae3d"></u-icon>
                    <text>活动亮点</text>
                </view>
                <view class="content-text">{{ summary.highlights }}</view>
            </view>
            
            <view class="content-section" v-if="summary.details">
                <view class="content-title">
                    <u-icon name="list-dot" size="24" color="#3c9cff"></u-icon>
                    <text>活动详情</text>
                </view>
                <view class="content-text">{{ summary.details }}</view>
            </view>
            
            <view class="content-section" v-if="summary.achievements">
                <view class="content-title">
                    <u-icon name="checkmark-circle" size="24" color="#5ac725"></u-icon>
                    <text>活动成果</text>
                </view>
                <view class="content-text">{{ summary.achievements }}</view>
            </view>
            
            <view class="content-section" v-if="summary.shortcomings">
                <view class="content-title">
                    <u-icon name="info-circle" size="24" color="#f56c6c"></u-icon>
                    <text>不足之处</text>
                </view>
                <view class="content-text">{{ summary.shortcomings }}</view>
            </view>
            
            <view class="content-section" v-if="summary.improvements">
                <view class="content-title">
                    <u-icon name="arrow-upward" size="24" color="#722ed1"></u-icon>
                    <text>改进措施</text>
                </view>
                <view class="content-text">{{ summary.improvements }}</view>
            </view>
        </view>
        
        <view class="rating-section" v-if="canRate && store.getters.isStudent">
            <view class="section-title">
                <u-icon name="heart-fill" size="32" color="#f56c6c"></u-icon>
                <text>活动评价</text>
            </view>
            
            <view class="rate-btn">
                <u-button type="primary" @click="goToRate">
                    立即评价
                </u-button>
            </view>
        </view>
        
        <view class="ratings-list" v-if="ratings.length > 0">
            <view class="section-title">
                <u-icon name="chatbubbles" size="32" color="#722ed1"></u-icon>
                <text>用户评价 ({{ ratings.length }})</text>
            </view>
            
            <view class="rating-item" v-for="item in ratings" :key="item.id">
                <view class="rating-header">
                    <view class="user-info">
                        <u-avatar 
                            :text="item.anonymous ? '匿' : item.userName?.charAt(0)" 
                            size="60"
                            :bgColor="getAvatarColor(item.id)"
                        ></u-avatar>
                        <view class="user-name">{{ item.anonymous ? '匿名用户' : item.userName }}</view>
                    </view>
                    <view class="rating-score">
                        <u-rate 
                            :value="item.overallRating" 
                            readonly 
                            size="24"
                            activeColor="#f9ae3d"
                        ></u-rate>
                        <text class="score-text">{{ item.overallRating }}.0</text>
                    </view>
                </view>
                
                <view class="rating-content" v-if="item.subRatings">
                    <view class="sub-rating" v-for="(value, key) in item.subRatings">
                        <text class="sub-label">{{ getSubRatingLabel(key) }}</text>
                        <u-rate 
                            :value="value" 
                            readonly 
                            size="20"
                            activeColor="#f9ae3d"
                        ></u-rate>
                    </view>
                </view>
                
                <view class="rating-comment" v-if="item.comment">{{ item.comment }}</view>
                <view class="rating-suggestion" v-if="item.suggestion">
                    <text class="suggestion-label">建议:</text>
                    <text>{{ item.suggestion }}</text>
                </view>
                <view class="rating-time">{{ formatDate(item.createTime) }}</view>
            </view>
        </view>
        
        <view class="loading" v-if="loading">
            <u-loading mode="circle"></u-loading>
        </view>
    </view>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { summaryApi, ratingApi } from '@/utils/api'
import dayjs from 'dayjs'

const store = useStore()
const route = useRoute()
const summaryId = computed(() => route.query.id || route.params.id)

const loading = ref(false)
const summary = ref(null)
const ratings = ref([])

const canRate = ref(false)

const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

const subRatingLabels = {
    content: '活动内容',
    organization: '组织安排',
    venue: '场地环境'
}

const getSubRatingLabel = (key) => subRatingLabels[key] || key

const getAvatarColor = (id) => {
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#722ed1']
    return colors[id % colors.length]
}

const loadData = async () => {
    loading.value = true
    try {
        const res = await summaryApi.getById(summaryId.value)
        summary.value = res.data || res
        
        if (summary.value.activityEndTime) {
            const now = dayjs()
            const endTime = dayjs(summary.value.activityEndTime)
            canRate.value = now.isAfter(endTime) && store.getters.isStudent
        }
        
        const ratingsRes = await ratingApi.getByActivityId(summary.value.activityId, { page: 1, size: 20 })
        ratings.value = ratingsRes.content || ratingsRes.data?.content || []
    } catch (e) {
        console.error('加载数据失败:', e)
    } finally {
        loading.value = false
    }
}

const goToRate = () => {
    uni.navigateTo({
        url: `/pages/rating/submit?activityId=${summary.value.activityId}`
    })
}

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 100rpx;
}

.header {
    background: #fff;
    margin-bottom: 20rpx;
    
    .poster {
        width: 100%;
        height: 300rpx;
        overflow: hidden;
        
        image {
            width: 100%;
            height: 100%;
        }
    }
    
    .header-content {
        padding: 30rpx;
        
        .title {
            font-size: 36rpx;
            font-weight: bold;
            color: #333;
            margin-bottom: 16rpx;
        }
        
        .meta {
            display: flex;
            align-items: center;
            font-size: 26rpx;
            color: #666;
            margin-bottom: 12rpx;
            
            text {
                margin-left: 8rpx;
            }
        }
    }
}

.summary-section {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.section-title {
    display: flex;
    align-items: center;
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
    
    text {
        margin-left: 12rpx;
    }
}

.summary-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 8rpx;
}

.publish-time {
    font-size: 24rpx;
    color: #999;
    margin-bottom: 24rpx;
}

.photo-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 8rpx;
    margin-bottom: 32rpx;
    
    .photo-item {
        width: calc(33.33% - 8rpx);
        height: 200rpx;
        border-radius: 8rpx;
    }
}

.content-section {
    margin-bottom: 24rpx;
    
    .content-title {
        display: flex;
        align-items: center;
        font-size: 28rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 12rpx;
        
        text {
            margin-left: 8rpx;
        }
    }
    
    .content-text {
        font-size: 26rpx;
        color: #666;
        line-height: 1.6;
        padding-left: 32rpx;
    }
}

.rating-section {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
    
    .rate-btn {
        margin-top: 16rpx;
    }
}

.ratings-list {
    background: #fff;
    padding: 30rpx;
    
    .rating-item {
        padding: 24rpx 0;
        border-bottom: 1rpx solid #f5f5f5;
        
        &:last-child {
            border-bottom: none;
        }
        
        .rating-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 16rpx;
            
            .user-info {
                display: flex;
                align-items: center;
                gap: 16rpx;
                
                .user-name {
                    font-size: 28rpx;
                    color: #333;
                }
            }
            
            .rating-score {
                display: flex;
                align-items: center;
                gap: 8rpx;
                
                .score-text {
                    font-size: 24rpx;
                    color: #f9ae3d;
                    font-weight: bold;
                }
            }
        }
        
        .rating {
            padding-left: 76rpx;
            margin-bottom: 16rpx;
            
            .sub-rating {
                display: flex;
                align-items: center;
                margin-bottom: 8rpx;
                
                .sub-label {
                    width: 120rpx;
                    font-size: 24rpx;
                    color: #999;
                }
            }
        }
        
        .rating-comment {
            padding-left: 76rpx;
            font-size: 26rpx;
            color: #333;
            line-height: 1.6;
            margin-bottom: 12rpx;
        }
        
        .rating-suggestion {
            padding-left: 76rpx;
            font-size: 24rpx;
            color: #666;
            line-height: 1.6;
            margin-bottom: 12rpx;
            
            .suggestion-label {
                color: #3c9cff;
            }
        }
        
        .rating-time {
            padding-left: 76rpx;
            font-size: 22rpx;
            color: #999;
        }
    }
}

.loading {
    display: flex;
    justify-content: center;
    padding: 100rpx 0;
}
</style>
