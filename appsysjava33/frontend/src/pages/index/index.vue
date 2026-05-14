<template>
    <view class="container">
        <view class="user-card">
            <view class="user-info" @click="goToUserCenter">
                <view class="avatar">
                    <text v-if="userInfo.name">{{ userInfo.name.charAt(0) }}</text>
                    <text v-else>👤</text>
                </view>
                <view class="info-text">
                    <text class="name">{{ userInfo.name || '点击登录' }}</text>
                    <text class="phone" v-if="userInfo.phone">{{ userInfo.phone }}</text>
                </view>
                <text class="arrow">›</text>
            </view>
            
            <view class="quick-btns" v-if="userInfo.id">
                <view class="quick-btn" @click="goToInstall">
                    <text class="btn-icon">📦</text>
                    <text class="btn-text">新装办理</text>
                </view>
                <view class="quick-btn" @click="goToMove">
                    <text class="btn-icon">🏠</text>
                    <text class="btn-text">移机办理</text>
                </view>
                <view class="quick-btn" @click="goToCancel">
                    <text class="btn-icon">❌</text>
                    <text class="btn-text">销户办理</text>
                </view>
                <view class="quick-btn" @click="goToBill">
                    <text class="btn-icon">📄</text>
                    <text class="btn-text">账单缴费</text>
                </view>
            </view>
        </view>

        <view class="banner">
            <swiper class="banner-swiper" autoplay indicator-dots circular>
                <swiper-item>
                    <view class="banner-item banner-1">
                        <view class="banner-content">
                            <text class="banner-title">千兆宽带</text>
                            <text class="banner-subtitle">极速体验，限时优惠</text>
                        </view>
                    </view>
                </swiper-item>
                <swiper-item>
                    <view class="banner-item banner-2">
                        <view class="banner-content">
                            <text class="banner-title">5G融合套餐</text>
                            <text class="banner-subtitle">手机宽带一起享</text>
                        </view>
                    </view>
                </swiper-item>
                <swiper-item>
                    <view class="banner-item banner-3">
                        <view class="banner-content">
                            <text class="banner-title">网络安全防护</text>
                            <text class="banner-subtitle">为您的网络保驾护航</text>
                        </view>
                    </view>
                </swiper-item>
            </swiper>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">热门套餐</text>
                <text class="section-more" @click="goToPackage">查看全部 ›</text>
            </view>
            <view class="package-list">
                <view class="package-item" v-for="pkg in hotPackages" :key="pkg.id" @click="goToPackageDetail(pkg.id)">
                    <view class="package-header">
                        <text class="package-name">{{ pkg.name }}</text>
                        <view class="package-tag" v-if="pkg.type === 1">基础套餐</view>
                    </view>
                    <view class="package-bandwidth">
                        <text class="bandwidth-value">{{ pkg.bandwidth }}</text>
                        <text class="bandwidth-unit">Mbps</text>
                    </view>
                    <view class="package-price">
                        <text class="price-symbol">¥</text>
                        <text class="price-value">{{ pkg.monthlyFee }}</text>
                        <text class="price-unit">/月</text>
                    </view>
                    <view class="package-features">
                        <text class="feature-item" v-for="(f, i) in pkg.features" :key="i">{{ f }}</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="section">
            <view class="section-header">
                <text class="section-title">增值服务</text>
            </view>
            <view class="service-grid">
                <view class="service-item" v-for="service in extraServices" :key="service.id">
                    <text class="service-icon">{{ service.icon }}</text>
                    <text class="service-name">{{ service.name }}</text>
                    <text class="service-price">¥{{ service.price }}/月</text>
                </view>
            </view>
        </view>

        <view class="elder-mode-banner" @click="toggleElderMode">
            <text class="elder-icon">👴</text>
            <view class="elder-text">
                <text class="elder-title">长辈模式</text>
                <text class="elder-desc">字体放大，界面简化</text>
            </view>
            <switch :checked="isElderMode" color="#007AFF" />
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            userInfo: {},
            isElderMode: false,
            hotPackages: [],
            extraServices: [
                { id: 1, name: '提速包', icon: '⚡', price: '30' },
                { id: 2, name: '安全防护', icon: '🛡️', price: '20' },
                { id: 3, name: '全屋WiFi', icon: '📶', price: '25' },
                { id: 4, name: '上门服务', icon: '🔧', price: '50' }
            ]
        }
    },
    onShow() {
        this.userInfo = uni.getStorageSync('userInfo') || {}
        this.isElderMode = uni.getStorageSync('elderMode') || false
        this.loadPackages()
    },
    methods: {
        loadPackages() {
            uni.request({
                url: '/api/package',
                method: 'GET',
                success: (res) => {
                    if (res.data.code === 200) {
                        this.hotPackages = res.data.data.slice(0, 3)
                    }
                },
                fail: () => {
                    this.hotPackages = [
                        { id: 1, name: '100M光纤宽带', bandwidth: 100, monthlyFee: 99, type: 1, features: ['免费安装', '光猫免费'] },
                        { id: 2, name: '300M光纤宽带', bandwidth: 300, monthlyFee: 159, type: 1, features: ['免费安装', '免费提速'] },
                        { id: 3, name: '500M光纤宽带', bandwidth: 500, monthlyFee: 219, type: 1, features: ['专属客服', '上门服务'] }
                    ]
                }
            })
        },

        goToUserCenter() {
            if (!this.userInfo.id) {
                uni.navigateTo({
                    url: '/pages/login/login'
                })
            } else {
                uni.switchTab({
                    url: '/pages/user/index'
                })
            }
        },

        goToInstall() {
            if (!this.checkLogin()) return
            uni.navigateTo({
                url: '/pages/order/install'
            })
        },

        goToMove() {
            if (!this.checkLogin()) return
            uni.navigateTo({
                url: '/pages/order/move'
            })
        },

        goToCancel() {
            if (!this.checkLogin()) return
            uni.navigateTo({
                url: '/pages/order/cancel'
            })
        },

        goToBill() {
            if (!this.checkLogin()) return
            uni.switchTab({
                url: '/pages/bill/list'
            })
        },

        goToPackage() {
            uni.switchTab({
                url: '/pages/package/list'
            })
        },

        goToPackageDetail(id) {
            uni.navigateTo({
                url: '/pages/package/detail?id=' + id
            })
        },

        checkLogin() {
            if (!this.userInfo.id) {
                uni.navigateTo({
                    url: '/pages/login/login'
                })
                return false
            }
            return true
        },

        toggleElderMode() {
            this.isElderMode = !this.isElderMode
            uni.setStorageSync('elderMode', this.isElderMode)
            if (this.isElderMode) {
                document.body.classList.add('elder-mode')
            } else {
                document.body.classList.remove('elder-mode')
            }
            uni.showToast({
                title: this.isElderMode ? '已开启长辈模式' : '已关闭长辈模式',
                icon: 'success'
            })
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.user-card {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 24rpx;
    padding: 32rpx;
    margin-bottom: 20rpx;
    color: #FFFFFF;
}

.user-info {
    display: flex;
    align-items: center;
}

.avatar {
    width: 80rpx;
    height: 80rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
    margin-right: 20rpx;
}

.info-text {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.name {
    font-size: 32rpx;
    font-weight: 500;
    margin-bottom: 8rpx;
}

.phone {
    font-size: 24rpx;
    opacity: 0.8;
}

.arrow {
    font-size: 40rpx;
    opacity: 0.6;
}

.quick-btns {
    display: flex;
    justify-content: space-around;
    margin-top: 30rpx;
    padding-top: 30rpx;
    border-top: 1rpx solid rgba(255, 255, 255, 0.2);
}

.quick-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.btn-icon {
    font-size: 40rpx;
    margin-bottom: 8rpx;
}

.btn-text {
    font-size: 24rpx;
}

.banner {
    margin-bottom: 30rpx;
}

.banner-swiper {
    height: 280rpx;
}

.banner-item {
    height: 100%;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    padding: 40rpx;
}

.banner-1 {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.banner-2 {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.banner-3 {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.banner-content {
    display: flex;
    flex-direction: column;
    color: #FFFFFF;
}

.banner-title {
    font-size: 36rpx;
    font-weight: bold;
    margin-bottom: 12rpx;
}

.banner-subtitle {
    font-size: 26rpx;
    opacity: 0.9;
}

.section {
    margin-bottom: 30rpx;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    padding: 0 8rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333333;
}

.section-more {
    font-size: 26rpx;
    color: #999999;
}

.package-list {
    display: flex;
    gap: 20rpx;
    overflow-x: auto;
    padding: 0 8rpx 8rpx;
}

.package-item {
    min-width: 300rpx;
    background: #FFFFFF;
    border-radius: 20rpx;
    padding: 24rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.package-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
}

.package-name {
    font-size: 28rpx;
    font-weight: 500;
    color: #333333;
}

.package-tag {
    background: #E8F3FF;
    color: #007AFF;
    font-size: 20rpx;
    padding: 4rpx 12rpx;
    border-radius: 8rpx;
}

.package-bandwidth {
    display: flex;
    align-items: baseline;
    margin-bottom: 8rpx;
}

.bandwidth-value {
    font-size: 48rpx;
    font-weight: bold;
    color: #007AFF;
}

.bandwidth-unit {
    font-size: 24rpx;
    color: #999999;
    margin-left: 4rpx;
}

.package-price {
    display: flex;
    align-items: baseline;
    margin-bottom: 16rpx;
}

.price-symbol {
    font-size: 24rpx;
    color: #FF3B30;
}

.price-value {
    font-size: 40rpx;
    font-weight: bold;
    color: #FF3B30;
}

.price-unit {
    font-size: 22rpx;
    color: #999999;
    margin-left: 4rpx;
}

.package-features {
    display: flex;
    flex-wrap: wrap;
    gap: 8rpx;
}

.feature-item {
    font-size: 20rpx;
    color: #666666;
    background: #F5F7FA;
    padding: 4rpx 12rpx;
    border-radius: 8rpx;
}

.service-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
}

.service-item {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx 16rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.service-icon {
    font-size: 48rpx;
    margin-bottom: 12rpx;
}

.service-name {
    font-size: 24rpx;
    color: #333333;
    margin-bottom: 8rpx;
}

.service-price {
    font-size: 20rpx;
    color: #FF3B30;
}

.elder-mode-banner {
    background: linear-gradient(135deg, #FFECD2 0%, #FCB69F 100%);
    border-radius: 20rpx;
    padding: 32rpx;
    display: flex;
    align-items: center;
    gap: 20rpx;
}

.elder-icon {
    font-size: 60rpx;
}

.elder-text {
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
</style>
