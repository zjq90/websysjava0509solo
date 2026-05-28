<template>
    <view class="container">
        <view class="activity-info card">
            <view class="activity-name">{{ activity?.name }}</view>
            <view class="activity-time">{{ formatDate(activity?.startTime) }}</view>
        </view>
        
        <view class="rating-section card">
            <view class="section-title">综合评分</view>
            <view class="rating-main">
                <u-rate 
                    v-model="form.rating" 
                    :count="5" 
                    size="48"
                    active-color="#f9ae3d"
                    @change="onRatingChange"
                ></u-rate>
                <text class="rating-text">{{ ratingTexts[form.rating - 1] || '点击评分' }}</text>
            </view>
        </view>
        
        <view class="rating-detail card">
            <view class="section-title">分项评分</view>
            
            <view class="rating-item">
                <text class="item-label">活动内容</text>
                <u-rate 
                    v-model="form.contentRating" 
                    :count="5" 
                    size="36"
                    active-color="#f9ae3d"
                ></u-rate>
            </view>
            
            <view class="rating-item">
                <text class="item-label">组织安排</text>
                <u-rate 
                    v-model="form.organizationRating" 
                    :count="5" 
                    size="36"
                    active-color="#f9ae3d"
                ></u-rate>
            </view>
            
            <view class="rating-item">
                <text class="item-label">场地设施</text>
                <u-rate 
                    v-model="form.venueRating" 
                    :count="5" 
                    size="36"
                    active-color="#f9ae3d"
                ></u-rate>
            </view>
        </view>
        
        <view class="comment-section card">
            <view class="section-title">评价内容</view>
            <u--textarea 
                v-model="form.comment" 
                placeholder="请输入您对本次活动的评价和建议..."
                :maxlength="500"
                :auto-height="true"
                :border="false"
                count
            ></u--textarea>
        </view>
        
        <view class="suggestion-section card">
            <view class="section-title">改进建议</view>
            <u--textarea 
                v-model="form.suggestion" 
                placeholder="请输入您对后续活动的改进建议..."
                :maxlength="500"
                :auto-height="true"
                :border="false"
                count
            ></u--textarea>
        </view>
        
        <view class="anonymous-section card">
            <view class="flex-between">
                <text>匿名评价</text>
                <u-switch v-model="form.anonymous"></u-switch>
            </view>
            <view class="anonymous-tip">匿名评价后，其他人将无法看到您的个人信息</view>
        </view>
        
        <view class="submit-bar">
            <u-button type="primary" size="large" :loading="submitting" @click="submitRating">
                提交评价
            </u-button>
        </view>
    </view>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ratingApi, activityApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const activityId = ref(route.query.activityId)
const activity = ref(null)
const submitting = ref(false)

const ratingTexts = ['非常差', '较差', '一般', '较好', '非常好']

const form = reactive({
    activityId: null,
    rating: 3,
    contentRating: 3,
    organizationRating: 3,
    venueRating: 3,
    comment: '',
    suggestion: '',
    anonymous: false
})

const formatDate = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')

const onRatingChange = (value) => {
    console.log('评分:', value)
}

const loadActivity = async () => {
    try {
        activity.value = await activityApi.getDetail(activityId.value)
        form.activityId = Number(activityId.value)
    } catch (e) {
        console.error('加载活动信息失败:', e)
    }
}

const validateForm = () => {
    if (!form.rating) {
        uni.showToast({ title: '请进行综合评分', icon: 'none' })
        return false
    }
    if (!form.comment) {
        uni.showToast({ title: '请输入评价内容', icon: 'none' })
        return false
    }
    return true
}

const submitRating = async () => {
    if (!validateForm()) return
    
    submitting.value = true
    try {
        await ratingApi.submit(form)
        uni.showToast({ title: '评价提交成功', icon: 'success' })
        setTimeout(() => {
            uni.navigateBack()
        }, 1500)
    } catch (e) {
        console.error('提交评价失败:', e)
    } finally {
        submitting.value = false
    }
}

onMounted(() => {
    loadActivity()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding: 20rpx;
    padding-bottom: 140rpx;
}

.card {
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
}

.activity-info {
    .activity-name {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
    }
    
    .activity-time {
        font-size: 26rpx;
        color: #999;
    }
}

.section-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
}

.rating-section {
    .rating-main {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx 0;
        
        .rating-text {
            margin-top: 16rpx;
            font-size: 26rpx;
            color: #f9ae3d;
        }
    }
}

.rating-detail {
    .rating-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 16rpx 0;
        border-bottom: 1rpx solid #f5f5f5;
        
        &:last-child {
            border-bottom: none;
        }
        
        .item-label {
            font-size: 26rpx;
            color: #666;
        }
    }
}

.anonymous-section {
    .anonymous-tip {
        font-size: 24rpx;
        color: #999;
        margin-top: 8rpx;
    }
}

.flex-between {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.submit-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
}
</style>
