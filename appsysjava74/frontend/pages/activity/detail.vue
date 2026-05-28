<template>
    <view class="container" v-if="activity">
        <scroll-view scroll-y class="scroll-container">
            <image class="poster" :src="activity.posterUrl || '/static/default-poster.png'" mode="aspectFill"></image>
            
            <view class="activity-header card">
                <view class="activity-title">{{ activity.name }}</view>
                <view class="activity-tags">
                    <u-tag 
                        :type="getStatusType(activity.status)" 
                        :text="getStatusText(activity.status)"
                        size="mini"
                    ></u-tag>
                    <u-tag 
                        :type="activity.registrationScope === 'ALL_STUDENTS' ? 'primary' : 'warning'" 
                        :text="activity.registrationScope === 'ALL_STUDENTS' ? '全校开放' : '仅社团成员'"
                        size="mini"
                    ></u-tag>
                    <u-tag 
                        v-if="activity.needApproval"
                        type="info"
                        text="需审核"
                        size="mini"
                    ></u-tag>
                </view>
                <view class="activity-stats">
                    <view class="stat-item">
                        <view class="stat-num text-primary">{{ activity.registeredCount }}</view>
                        <view class="stat-label">已报名</view>
                    </view>
                    <view class="stat-divider"></view>
                    <view class="stat-item">
                        <view class="stat-num text-primary">{{ activity.quota }}</view>
                        <view class="stat-label">名额</view>
                    </view>
                    <view class="stat-divider" v-if="activity.averageRating"></view>
                    <view class="stat-item" v-if="activity.averageRating">
                        <view class="stat-num text-warning">{{ activity.averageRating }}</view>
                        <view class="stat-label">评分</view>
                    </view>
                </view>
            </view>
            
            <view class="activity-info card">
                <view class="info-item">
                    <u-icon name="calendar" size="32" color="#3c9cff"></u-icon>
                    <view class="info-content">
                        <view class="info-label">活动时间</view>
                        <view class="info-value">{{ formatDateTime(activity.startTime) }} - {{ formatTime(activity.endTime) }}</view>
                    </view>
                </view>
                <view class="info-item">
                    <u-icon name="map" size="32" color="#5ac725"></u-icon>
                    <view class="info-content">
                        <view class="info-label">活动地点</view>
                        <view class="info-value">{{ activity.location }}</view>
                    </view>
                </view>
                <view class="info-item">
                    <u-icon name="calendar-fill" size="32" color="#f9ae3d"></u-icon>
                    <view class="info-content">
                        <view class="info-label">报名时间</view>
                        <view class="info-value">{{ formatDateTime(activity.registrationStartTime) }} - {{ formatDateTime(activity.registrationEndTime) }}</view>
                    </view>
                </view>
                <view class="info-item">
                    <u-icon name="clock" size="32" color="#f56c6c"></u-icon>
                    <view class="info-content">
                        <view class="info-label">签到时间</view>
                        <view class="info-value">{{ formatTime(activity.signInStartTime) }} - {{ formatTime(activity.signInEndTime) }}</view>
                    </view>
                </view>
                <view class="info-item">
                    <u-icon name="phone" size="32" color="#909399"></u-icon>
                    <view class="info-content">
                        <view class="info-label">联系方式</view>
                        <view class="info-value">{{ activity.organizerName }} {{ activity.organizerPhone }}</view>
                    </view>
                </view>
            </view>
            
            <view class="activity-desc card" v-if="activity.description">
                <view class="section-title">活动简介</view>
                <view class="desc-content">{{ activity.description }}</view>
            </view>
            
            <view class="activity-requirements card" v-if="activity.requirements">
                <view class="section-title">活动要求</view>
                <view class="desc-content">{{ activity.requirements }}</view>
            </view>
            
            <view class="section-title summary-title" v-if="summary">活动总结</view>
            <view class="activity-summary card" v-if="summary">
                <view class="summary-content">
                    <view class="summary-title">{{ summary.title }}</view>
                    <view class="summary-text">{{ summary.content }}</view>
                    <view class="summary-meta">
                        <view><text class="label">亮点：</text>{{ summary.highlights }}</view>
                        <view><text class="label">不足：</text>{{ summary.shortcomings }}</view>
                        <view><text class="label">改进：</text>{{ summary.improvements }}</view>
                    </view>
                    <view class="summary-photos" v-if="summary.photos">
                        <image 
                            v-for="(photo, index) in summary.photos.split(',')" 
                            :key="index"
                            :src="photo" 
                            mode="aspectFill"
                            class="photo-item"
                            @click="previewPhoto(summary.photos.split(','), index)"
                        ></image>
                    </view>
                </view>
            </view>
            
            <view class="section-title rating-title" v-if="hasRating">活动评价</view>
            <view class="activity-rating card" v-if="hasRating">
                <view class="rating-header">
                    <view class="rating-score">
                        <text class="score">{{ activity.averageRating || 0 }}</text>
                        <u-rate :current="Math.round(activity.averageRating || 0)" :count="5" size="24" readonly></u-rate>
                        <text class="count">({{ activity.ratingCount || 0 }}条评价)</text>
                    </view>
                    <u-button type="primary" size="mini" @click="goToRatingList">查看全部</u-button>
                </view>
                <view class="my-rating" v-if="myRating">
                    <view class="my-rating-title">我的评价</view>
                    <u-rate :current="myRating.rating" :count="5" size="28" readonly></u-rate>
                    <view class="my-rating-content">{{ myRating.comment }}</view>
                </view>
            </view>
            
            <view style="height: 160rpx;"></view>
        </scroll-view>
        
        <view class="bottom-bar">
            <view class="action-btns" v-if="isManager">
                <u-button type="warning" size="normal" @click="editActivity">编辑</u-button>
                <u-button type="primary" size="normal" @click="goToRegistrationList">报名管理</u-button>
                <u-button type="success" size="normal" @click="goToSignInQrcode">签到码</u-button>
            </view>
            
            <view class="action-btns" v-else>
                <u-button 
                    v-if="canRegister"
                    type="primary" 
                    size="normal" 
                    :loading="registering"
                    @click="handleRegister"
                >
                    立即报名
                </u-button>
                <u-button 
                    v-else-if="myRegistration && myRegistration.status === 'PENDING'"
                    type="warning" 
                    size="normal" 
                    disabled
                >
                    审核中
                </u-button>
                <u-button 
                    v-else-if="myRegistration && myRegistration.status === 'APPROVED'"
                    type="success" 
                    size="normal" 
                    @click="handleCancelRegister"
                >
                    已报名，取消报名
                </u-button>
                <u-button 
                    v-else-if="myRegistration && myRegistration.status === 'REJECTED'"
                    type="error" 
                    size="normal" 
                    disabled
                >
                    报名被拒绝
                </u-button>
                <u-button 
                    v-else-if="isRegistrationFull"
                    type="info" 
                    size="normal" 
                    disabled
                >
                    名额已满
                </u-button>
                <u-button 
                    v-else-if="!isRegistrationOpen"
                    type="info" 
                    size="normal" 
                    disabled
                >
                    报名未开始/已结束
                </u-button>
                
                <u-button 
                    v-if="canSignIn"
                    type="success" 
                    size="normal" 
                    @click="goToScan"
                >
                    扫码签到
                </u-button>
                
                <u-button 
                    v-if="canRate"
                    type="warning" 
                    size="normal" 
                    @click="goToRate"
                >
                    评价活动
                </u-button>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'
import { activityApi, registrationApi, summaryApi, ratingApi } from '@/utils/api'
import dayjs from 'dayjs'

const store = useStore()
const route = useRoute()
const activityId = ref(route.query.id)
const activity = ref(null)
const myRegistration = ref(null)
const summary = ref(null)
const myRating = ref(null)
const registering = ref(false)

const isManager = computed(() => {
    return store.getters.isAdmin || store.getters.isClubAdmin
})

const hasRating = computed(() => activity.value?.ratingCount > 0)

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

const formatDateTime = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')
const formatTime = (date) => dayjs(date).format('HH:mm')

const isRegistrationOpen = computed(() => {
    if (!activity.value) return false
    const now = dayjs()
    return now.isAfter(dayjs(activity.value.registrationStartTime)) && 
           now.isBefore(dayjs(activity.value.registrationEndTime))
})

const isRegistrationFull = computed(() => {
    if (!activity.value) return false
    return activity.value.registeredCount >= activity.value.quota
})

const canRegister = computed(() => {
    if (!activity.value || !store.getters.isStudent) return false
    return isRegistrationOpen.value && !isRegistrationFull.value && !myRegistration.value
})

const canSignIn = computed(() => {
    if (!activity.value || !store.getters.isStudent) return false
    if (!myRegistration.value || myRegistration.value.status !== 'APPROVED') return false
    const now = dayjs()
    return now.isAfter(dayjs(activity.value.signInStartTime)) && 
           now.isBefore(dayjs(activity.value.signInEndTime))
})

const canRate = computed(() => {
    if (!activity.value || !store.getters.isStudent) return false
    if (activity.value.status !== 'COMPLETED') return false
    if (!myRegistration.value || myRegistration.value.status !== 'APPROVED') return false
    return !myRating.value
})

const loadData = async () => {
    try {
        activity.value = await activityApi.getDetail(activityId.value)
        
        if (store.getters.isStudent) {
            try {
                const myRegList = await registrationApi.getMyList({ page: 1, size: 100 })
                myRegistration.value = (myRegList.content || myRegList.data?.content || [])
                    .find(r => r.activityId === Number(activityId.value))
            } catch (e) {}
        }
        
        try {
            summary.value = await summaryApi.getByActivity(activityId.value)
        } catch (e) {}
        
        if (store.getters.isStudent && activity.value.status === 'COMPLETED') {
            try {
                myRating.value = await ratingApi.getMyRating(activityId.value)
            } catch (e) {}
        }
    } catch (e) {
        console.error('加载活动详情失败:', e)
    }
}

const handleRegister = async () => {
    uni.showModal({
        title: '确认报名',
        content: '确定要报名参加这个活动吗？',
        success: async (res) => {
            if (res.confirm) {
                registering.value = true
                try {
                    await registrationApi.register({ activityId: activityId.value })
                    uni.showToast({ title: '报名成功', icon: 'success' })
                    loadData()
                } catch (e) {
                    console.error('报名失败:', e)
                } finally {
                    registering.value = false
                }
            }
        }
    })
}

const handleCancelRegister = async () => {
    if (!myRegistration.value) return
    uni.showModal({
        title: '取消报名',
        content: '确定要取消报名吗？',
        success: async (res) => {
            if (res.confirm) {
                try {
                    await registrationApi.cancel(myRegistration.value.id)
                    uni.showToast({ title: '取消成功', icon: 'success' })
                    loadData()
                } catch (e) {
                    console.error('取消报名失败:', e)
                }
            }
        }
    })
}

const editActivity = () => {
    uni.navigateTo({
        url: `/pages/activity/edit?id=${activityId.value}`
    })
}

const goToRegistrationList = () => {
    uni.navigateTo({
        url: `/pages/registration/list?activityId=${activityId.value}`
    })
}

const goToSignInQrcode = () => {
    uni.navigateTo({
        url: `/pages/signin/qrcode?activityId=${activityId.value}`
    })
}

const goToScan = () => {
    uni.navigateTo({
        url: '/pages/scan/scan'
    })
}

const goToRate = () => {
    uni.navigateTo({
        url: `/pages/rating/submit?activityId=${activityId.value}`
    })
}

const goToRatingList = () => {
    uni.navigateTo({
        url: `/pages/rating/list?activityId=${activityId.value}`
    })
}

const previewPhoto = (photos, index) => {
    uni.previewImage({
        urls: photos,
        current: index
    })
}

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    position: relative;
}

.scroll-container {
    height: calc(100vh - 120rpx);
}

.poster {
    width: 100%;
    height: 400rpx;
}

.card {
    background: #fff;
    margin: 20rpx;
    border-radius: 16rpx;
    padding: 24rpx;
}

.activity-header {
    .activity-title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 16rpx;
    }
    
    .activity-tags {
        display: flex;
        gap: 12rpx;
        margin-bottom: 24rpx;
    }
    
    .activity-stats {
        display: flex;
        justify-content: space-around;
        padding-top: 20rpx;
        border-top: 1rpx solid #f0f0f0;
        
        .stat-item {
            text-align: center;
            
            .stat-num {
                font-size: 32rpx;
                font-weight: bold;
            }
            
            .stat-label {
                font-size: 24rpx;
                color: #999;
                margin-top: 4rpx;
            }
        }
        
        .stat-divider {
            width: 1rpx;
            background: #f0f0f0;
        }
    }
}

.activity-info {
    .info-item {
        display: flex;
        align-items: flex-start;
        padding: 16rpx 0;
        border-bottom: 1rpx solid #f5f5f5;
        
        &:last-child {
            border-bottom: none;
        }
        
        .info-content {
            flex: 1;
            margin-left: 16rpx;
            
            .info-label {
                font-size: 24rpx;
                color: #999;
                margin-bottom: 4rpx;
            }
            
            .info-value {
                font-size: 28rpx;
                color: #333;
            }
        }
    }
}

.section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
}

.summary-title, .rating-title {
    padding: 0 40rpx;
    margin-top: 20rpx;
}

.desc-content {
    font-size: 28rpx;
    color: #666;
    line-height: 1.8;
}

.activity-summary {
    .summary-content {
        .summary-title {
            font-size: 32rpx;
            font-weight: bold;
            margin-bottom: 16rpx;
        }
        
        .summary-text {
            font-size: 28rpx;
            color: #666;
            line-height: 1.8;
            margin-bottom: 20rpx;
        }
        
        .summary-meta {
            background: #f8f9fa;
            padding: 20rpx;
            border-radius: 12rpx;
            margin-bottom: 20rpx;
            
            view {
                font-size: 26rpx;
                color: #666;
                margin-bottom: 8rpx;
                
                .label {
                    color: #3c9cff;
                    font-weight: bold;
                }
            }
        }
        
        .summary-photos {
            display: flex;
            flex-wrap: wrap;
            gap: 10rpx;
            
            .photo-item {
                width: 200rpx;
                height: 200rpx;
                border-radius: 12rpx;
            }
        }
    }
}

.activity-rating {
    .rating-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 20rpx;
        
        .rating-score {
            display: flex;
            align-items: center;
            gap: 8rpx;
            
            .score {
                font-size: 40rpx;
                font-weight: bold;
                color: #f9ae3d;
            }
            
            .count {
                font-size: 24rpx;
                color: #999;
            }
        }
    }
    
    .my-rating {
        background: #f8f9fa;
        padding: 20rpx;
        border-radius: 12rpx;
        
        .my-rating-title {
            font-size: 26rpx;
            color: #999;
            margin-bottom: 8rpx;
        }
        
        .my-rating-content {
            font-size: 28rpx;
            color: #333;
            margin-top: 8rpx;
        }
    }
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
    
    .action-btns {
        display: flex;
        gap: 16rpx;
        
        button {
            flex: 1;
        }
    }
}
</style>
