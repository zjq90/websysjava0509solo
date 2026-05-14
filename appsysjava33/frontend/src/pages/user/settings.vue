<template>
    <view class="container">
        <view class="section-card">
            <view class="section-title">通用设置</view>
            <view class="setting-item">
                <text class="setting-icon">🔔</text>
                <text class="setting-text">消息通知</text>
                <switch :checked="notifyEnabled" color="#007AFF" @click="notifyEnabled = !notifyEnabled" />
            </view>
            <view class="setting-item">
                <text class="setting-icon">🔊</text>
                <text class="setting-text">声音提醒</text>
                <switch :checked="soundEnabled" color="#007AFF" @click="soundEnabled = !soundEnabled" />
            </view>
            <view class="setting-item" @click="toggleElderMode">
                <text class="setting-icon">👴</text>
                <text class="setting-text">长辈模式</text>
                <switch :checked="isElderMode" color="#007AFF" />
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">账号设置</view>
            <view class="setting-item" @click="goChangePhone">
                <text class="setting-icon">📱</text>
                <text class="setting-text">更换手机号</text>
                <text class="setting-arrow">›</text>
            </view>
            <view class="setting-item" @click="goChangePassword">
                <text class="setting-icon">🔐</text>
                <text class="setting-text">密码设置</text>
                <text class="setting-arrow">›</text>
            </view>
            <view class="setting-item" @click="goPrivacy">
                <text class="setting-icon">🔒</text>
                <text class="setting-text">隐私设置</text>
                <text class="setting-arrow">›</text>
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">关于</view>
            <view class="setting-item" @click="goAbout">
                <text class="setting-icon">ℹ️</text>
                <text class="setting-text">关于我们</text>
                <text class="setting-arrow">›</text>
            </view>
            <view class="setting-item" @click="goHelp">
                <text class="setting-icon">❓</text>
                <text class="setting-text">帮助中心</text>
                <text class="setting-arrow">›</text>
            </view>
            <view class="setting-item" @click="goFeedback">
                <text class="setting-icon">💬</text>
                <text class="setting-text">意见反馈</text>
                <text class="setting-arrow">›</text>
            </view>
        </view>

        <view class="version-info">
            <text class="version-text">当前版本 v1.0.0</text>
        </view>

        <button class="logout-btn" v-if="isLoggedIn" @click="handleLogout">退出登录</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const notifyEnabled = ref(true)
const soundEnabled = ref(true)
const isElderMode = ref(false)
const isLoggedIn = ref(false)

onMounted(() => {
    const user = uni.getStorageSync('userInfo')
    isLoggedIn.value = user && user.id
    isElderMode.value = uni.getStorageSync('elderMode') || false
})

const toggleElderMode = () => {
    isElderMode.value = !isElderMode.value
    uni.setStorageSync('elderMode', isElderMode.value)
    uni.showToast({
        title: isElderMode.value ? '已开启长辈模式' : '已关闭长辈模式',
        icon: 'success'
    })
}

const goChangePhone = () => {
    uni.showToast({ title: '更换手机号功能', icon: 'none' })
}

const goChangePassword = () => {
    uni.showToast({ title: '密码设置功能', icon: 'none' })
}

const goPrivacy = () => {
    uni.showToast({ title: '隐私设置功能', icon: 'none' })
}

const goAbout = () => {
    uni.showModal({
        title: '关于我们',
        content: '宽带服务App v1.0.0\n为您提供便捷的宽带服务体验',
        showCancel: false
    })
}

const goHelp = () => {
    uni.showToast({ title: '帮助中心功能', icon: 'none' })
}

const goFeedback = () => {
    uni.showToast({ title: '意见反馈功能', icon: 'none' })
}

const handleLogout = () => {
    uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
            if (res.confirm) {
                uni.removeStorageSync('userInfo')
                uni.removeStorageSync('token')
                uni.removeStorageSync('userId')
                isLoggedIn.value = false
                uni.showToast({
                    title: '已退出登录',
                    icon: 'success'
                })
                setTimeout(() => {
                    uni.navigateBack()
                }, 1000)
            }
        }
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.section-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
    display: block;
    font-size: 24rpx;
    color: #999999;
    padding: 20rpx 24rpx 8rpx;
}

.setting-item {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.setting-item:last-child {
    border-bottom: none;
}

.setting-icon {
    font-size: 32rpx;
    margin-right: 16rpx;
}

.setting-text {
    flex: 1;
    font-size: 28rpx;
    color: #333333;
}

.setting-arrow {
    font-size: 28rpx;
    color: #CCCCCC;
}

.version-info {
    text-align: center;
    padding: 40rpx 0;
}

.version-text {
    font-size: 24rpx;
    color: #999999;
}

.logout-btn {
    width: 100%;
    background: #FFFFFF;
    color: #FF3B30;
    border-radius: 16rpx;
    padding: 28rpx;
    font-size: 30rpx;
    font-weight: 500;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
</style>
