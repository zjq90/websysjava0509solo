<template>
    <view class="container">
        <view class="user-card">
            <view class="user-avatar">
                <text class="avatar-icon">{{ userInfo.name ? userInfo.name.charAt(0) : '👤' }}</text>
            </view>
            <view class="user-info">
                <text class="user-name">{{ userInfo.name || '点击登录' }}</text>
                <text class="user-phone" v-if="userInfo.phone">{{ userInfo.phone }}</text>
            </view>
            <text class="arrow" v-if="!userInfo.id" @click="goLogin">›</text>
        </view>

        <view class="menu-list">
            <view class="menu-item" @click="goProfile">
                <text class="menu-icon">👤</text>
                <text class="menu-text">个人信息</text>
                <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goRealName">
                <text class="menu-icon">🪪</text>
                <text class="menu-text">实名认证</text>
                <text class="menu-tag" v-if="userInfo.realNameStatus === 2">已认证</text>
                <text class="menu-tag pending" v-else-if="userInfo.realNameStatus === 1">认证中</text>
                <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goSettings">
                <text class="menu-icon">⚙️</text>
                <text class="menu-text">设置</text>
                <text class="menu-arrow">›</text>
            </view>
        </view>

        <view class="elder-mode-card" @click="toggleElderMode">
            <text class="elder-icon">👴</text>
            <view class="elder-info">
                <text class="elder-title">长辈模式</text>
                <text class="elder-desc">字体放大，界面简化</text>
            </view>
            <switch :checked="isElderMode" color="#007AFF" />
        </view>

        <view class="menu-list" v-if="userInfo.id">
            <view class="menu-item" @click="goMyPackage">
                <text class="menu-icon">📦</text>
                <text class="menu-text">我的套餐</text>
                <text class="menu-arrow">›</text>
            </view>
            <view class="menu-item" @click="goMyNumber">
                <text class="menu-icon">📱</text>
                <text class="menu-text">我的号码</text>
                <text class="menu-arrow">›</text>
            </view>
        </view>

        <view class="logout-btn" v-if="userInfo.id" @click="handleLogout">
            退出登录
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const userInfo = ref({})
const isElderMode = ref(false)

onMounted(() => {
    loadUserInfo()
})

const loadUserInfo = () => {
    const user = uni.getStorageSync('userInfo')
    if (user) {
        userInfo.value = { ...user, realNameStatus: 0 }
    }
    isElderMode.value = uni.getStorageSync('elderMode') || false
}

const goLogin = () => {
    uni.navigateTo({
        url: '/pages/login/login'
    })
}

const goProfile = () => {
    if (!checkLogin()) return
    uni.navigateTo({
        url: '/pages/user/profile'
    })
}

const goRealName = () => {
    if (!checkLogin()) return
    uni.navigateTo({
        url: '/pages/user/realname'
    })
}

const goSettings = () => {
    uni.navigateTo({
        url: '/pages/user/settings'
    })
}

const goMyPackage = () => {
    uni.switchTab({
        url: '/pages/package/list'
    })
}

const goMyNumber = () => {
    uni.navigateTo({
        url: '/pages/number/select'
    })
}

const checkLogin = () => {
    if (!userInfo.value.id) {
        uni.navigateTo({
            url: '/pages/login/login'
        })
        return false
    }
    return true
}

const toggleElderMode = () => {
    isElderMode.value = !isElderMode.value
    uni.setStorageSync('elderMode', isElderMode.value)
    uni.showToast({
        title: isElderMode.value ? '已开启长辈模式' : '已关闭长辈模式',
        icon: 'success'
    })
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
                userInfo.value = {}
                uni.showToast({
                    title: '已退出登录',
                    icon: 'success'
                })
            }
        }
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.user-card {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 20rpx;
    padding: 40rpx 32rpx;
    display: flex;
    align-items: center;
    color: #FFFFFF;
    margin-bottom: 30rpx;
}

.user-avatar {
    width: 100rpx;
    height: 100rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 44rpx;
    margin-right: 24rpx;
}

.avatar-icon {
    font-weight: 500;
}

.user-info {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.user-name {
    font-size: 36rpx;
    font-weight: 600;
    margin-bottom: 8rpx;
}

.user-phone {
    font-size: 26rpx;
    opacity: 0.8;
}

.arrow {
    font-size: 44rpx;
    opacity: 0.6;
}

.menu-list {
    background: #FFFFFF;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 32rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-icon {
    font-size: 36rpx;
    margin-right: 20rpx;
}

.menu-text {
    flex: 1;
    font-size: 30rpx;
    color: #333333;
}

.menu-tag {
    font-size: 22rpx;
    color: #34C759;
    background: #E8F8ED;
    padding: 4rpx 12rpx;
    border-radius: 8rpx;
    margin-right: 16rpx;
}

.menu-tag.pending {
    color: #FF9500;
    background: #FFF5E6;
}

.menu-arrow {
    font-size: 32rpx;
    color: #CCCCCC;
}

.elder-mode-card {
    background: linear-gradient(135deg, #FFECD2 0%, #FCB69F 100%);
    border-radius: 16rpx;
    padding: 32rpx;
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.elder-icon {
    font-size: 56rpx;
    margin-right: 20rpx;
}

.elder-info {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.elder-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #8B4513;
    margin-bottom: 8rpx;
}

.elder-desc {
    font-size: 24rpx;
    color: #A0522D;
}

.logout-btn {
    background: #FFFFFF;
    color: #FF3B30;
    border-radius: 16rpx;
    padding: 32rpx;
    text-align: center;
    font-size: 30rpx;
    font-weight: 500;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
</style>
