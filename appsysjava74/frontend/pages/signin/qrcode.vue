<template>
    <view class="container">
        <view class="qrcode-container">
            <view class="activity-info card">
                <view class="activity-title">{{ activity?.name }}</view>
                <view class="activity-time">
                    <u-icon name="calendar" size="28" color="#999"></u-icon>
                    <text>{{ formatDateTime(activity?.startTime) }}</text>
                </view>
                <view class="activity-location">
                    <u-icon name="map" size="28" color="#999"></u-icon>
                    <text>{{ activity?.location }}</text>
                </view>
            </view>
            
            <view class="qrcode-card card">
                <view class="qrcode-title">签到二维码</view>
                <view class="qrcode-tip">请学生使用APP扫码签到</view>
                
                <view class="qrcode-image" v-if="qrCodeBase64">
                    <image :src="'data:image/png;base64,' + qrCodeBase64" mode="aspectFit"></image>
                </view>
                <view class="qrcode-loading" v-else>
                    <u-loading mode="circle"></u-loading>
                    <text>二维码生成中...</text>
                </view>
                
                <view class="qrcode-time" v-if="qrCodeExpireTime">
                    <u-count-down 
                        :time="remainingTime" 
                        format="mm:ss"
                        :auto-start="true"
                        @finish="refreshQrCode"
                    ></u-count-down>
                    <text class="time-tip">二维码有效时间</text>
                </view>
                
                <view class="signin-info">
                    <view class="info-item">
                        <text class="label">签到开始</text>
                        <text class="value">{{ formatTime(activity?.signInStartTime) }}</text>
                    </view>
                    <view class="info-item">
                        <text class="label">签到结束</text>
                        <text class="value">{{ formatTime(activity?.signInEndTime) }}</text>
                    </view>
                </view>
            </view>
            
            <view class="action-bar">
                <u-button type="warning" size="large" @click="refreshQrCode" :loading="refreshing">
                    <u-icon name="reload" size="32" style="margin-right: 8rpx;"></u-icon>
                    刷新二维码
                </u-button>
                <u-button type="primary" size="large" @click="goToSignInList">查看签到</u-button>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { activityApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const activityId = ref(route.query.activityId)
const activity = ref(null)
const qrCodeBase64 = ref('')
const qrCodeExpireTime = ref(null)
const remainingTime = ref(0)
const refreshing = ref(false)
let timer = null

const formatDateTime = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')
const formatTime = (date) => dayjs(date).format('HH:mm')

const loadQrCode = async () => {
    try {
        const res = await activityApi.getQrCode(activityId.value)
        qrCodeBase64.value = res.qrCodeBase64
        qrCodeExpireTime.value = res.expireTime
        
        if (qrCodeExpireTime.value) {
            const expire = dayjs(qrCodeExpireTime.value)
            const now = dayjs()
            remainingTime.value = expire.diff(now, 'second') * 1000
        }
    } catch (e) {
        console.error('获取二维码失败:', e)
    }
}

const refreshQrCode = async () => {
    refreshing.value = true
    try {
        const res = await activityApi.refreshQrCode(activityId.value)
        qrCodeBase64.value = res.qrCodeBase64
        qrCodeExpireTime.value = res.expireTime
        
        if (qrCodeExpireTime.value) {
            const expire = dayjs(qrCodeExpireTime.value)
            const now = dayjs()
            remainingTime.value = expire.diff(now, 'second') * 1000
        }
        
        uni.showToast({ title: '二维码已刷新', icon: 'success' })
    } catch (e) {
        console.error('刷新二维码失败:', e)
    } finally {
        refreshing.value = false
    }
}

const loadActivity = async () => {
    try {
        activity.value = await activityApi.getDetail(activityId.value)
    } catch (e) {
        console.error('加载活动信息失败:', e)
    }
}

const goToSignInList = () => {
    uni.navigateTo({
        url: `/pages/signin/list?activityId=${activityId.value}`
    })
}

const startTimer = () => {
    timer = setInterval(() => {
        if (remainingTime.value > 0) {
            remainingTime.value -= 1000
        }
    }, 1000)
}

onMounted(() => {
    loadActivity()
    loadQrCode()
    startTimer()
})

onUnmounted(() => {
    if (timer) {
        clearInterval(timer)
    }
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 40rpx;
}

.card {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
}

.activity-info {
    .activity-title {
        font-size: 36rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 16rpx;
    }
    
    .activity-time, .activity-location {
        display: flex;
        align-items: center;
        font-size: 26rpx;
        color: #666;
        margin-bottom: 8rpx;
        
        text {
            margin-left: 8rpx;
        }
    }
}

.qrcode-card {
    text-align: center;
    
    .qrcode-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
    }
    
    .qrcode-tip {
        font-size: 24rpx;
        color: #999;
        margin-bottom: 30rpx;
    }
    
    .qrcode-image {
        width: 400rpx;
        height: 400rpx;
        margin: 0 auto 30rpx;
        padding: 20rpx;
        background: #fff;
        border: 1rpx solid #f0f0f0;
        border-radius: 16rpx;
        
        image {
            width: 100%;
            height: 100%;
        }
    }
    
    .qrcode-loading {
        width: 400rpx;
        height: 400rpx;
        margin: 0 auto 30rpx;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background: #f8f9fa;
        border-radius: 16rpx;
        
        text {
            margin-top: 16rpx;
            font-size: 24rpx;
            color: #999;
        }
    }
    
    .qrcode-time {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20rpx;
        
        .time-tip {
            font-size: 24rpx;
            color: #999;
            margin-top: 8rpx;
        }
    }
    
    .signin-info {
        display: flex;
        justify-content: space-around;
        padding-top: 20rpx;
        border-top: 1rpx solid #f0f0f0;
        
        .info-item {
            display: flex;
            flex-direction: column;
            align-items: center;
            
            .label {
                font-size: 24rpx;
                color: #999;
            }
            
            .value {
                font-size: 28rpx;
                font-weight: bold;
                color: #333;
                margin-top: 4rpx;
            }
        }
    }
}

.action-bar {
    display: flex;
    gap: 20rpx;
    margin-top: 20rpx;
    
    button {
        flex: 1;
    }
}
</style>
