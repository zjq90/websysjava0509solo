<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="user-header">
            <view class="avatar" v-if="userInfo">
                <text class="avatar-icon">👤</text>
            </view>
            <view class="user-info" v-if="userInfo">
                <text class="username">{{ userInfo.nickname || userInfo.username }}</text>
                <text class="points">积分：{{ userInfo.points || 0 }}</text>
            </view>
            <view class="user-info" v-else @click="goToLogin">
                <text class="login-tip">点击登录</text>
            </view>
        </view>
        
        <view class="menu-section">
            <view class="menu-title">我的订单</view>
            <view class="menu-grid">
                <view class="menu-item" @click="goToOrderList('')">
                    <text class="menu-icon">📋</text>
                    <text class="menu-text">全部订单</text>
                </view>
                <view class="menu-item" @click="goToOrderList('pending_payment')">
                    <text class="menu-icon">💳</text>
                    <text class="menu-text">待支付</text>
                </view>
                <view class="menu-item" @click="goToOrderList('producing')">
                    <text class="menu-icon">🎨</text>
                    <text class="menu-text">制作中</text>
                </view>
                <view class="menu-item" @click="goToOrderList('delivering')">
                    <text class="menu-icon">🚚</text>
                    <text class="menu-text">配送中</text>
                </view>
                <view class="menu-item" @click="goToOrderList('completed')">
                    <text class="menu-icon">✅</text>
                    <text class="menu-text">已完成</text>
                </view>
            </view>
        </view>
        
        <view class="menu-section">
            <view class="menu-title">我的服务</view>
            <view class="menu-list">
                <view class="list-item" @click="goToCoupon">
                    <text class="item-icon">🎫</text>
                    <text class="item-text">优惠券中心</text>
                    <text class="item-arrow">›</text>
                </view>
                <view class="list-item" @click="toggleElderMode">
                    <text class="item-icon">👴</text>
                    <text class="item-text">长辈模式</text>
                    <switch :checked="elderMode" class="item-switch" />
                </view>
                <view class="list-item" @click="clearStorage" v-if="userInfo">
                    <text class="item-icon">🚪</text>
                    <text class="item-text">退出登录</text>
                    <text class="item-arrow">›</text>
                </view>
            </view>
        </view>
        
        <view class="about-section">
            <text class="about-text">花店APP v1.0.0</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            userInfo: null,
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
        this.loadUserInfo()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
        this.loadUserInfo()
    },
    methods: {
        loadUserInfo() {
            const token = uni.getStorageSync('token')
            if (token) {
                this.$request.get('/user/info').then(res => {
                    this.userInfo = res
                    getApp().globalData.userInfo = res
                }).catch(err => {
                    console.error(err)
                    this.userInfo = null
                })
            } else {
                this.userInfo = null
            }
        },
        
        goToLogin() {
            uni.navigateTo({
                url: '/pages/user/login'
            })
        },
        
        goToOrderList(status) {
            if (!this.userInfo) {
                this.goToLogin()
                return
            }
            uni.navigateTo({
                url: `/pages/order/list?status=${status}`
            })
        },
        
        goToCoupon() {
            if (!this.userInfo) {
                this.goToLogin()
                return
            }
            uni.navigateTo({
                url: '/pages/coupon/list'
            })
        },
        
        toggleElderMode() {
            this.elderMode = !this.elderMode
            getApp().globalData.elderMode = this.elderMode
            uni.setStorageSync('elderMode', this.elderMode)
            uni.showToast({
                title: this.elderMode ? '已开启长辈模式' : '已关闭长辈模式',
                icon: 'success'
            })
        },
        
        clearStorage() {
            uni.showModal({
                title: '提示',
                content: '确认退出登录吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        getApp().globalData.token = ''
                        getApp().globalData.userInfo = null
                        this.userInfo = null
                        uni.showToast({
                            title: '已退出登录',
                            icon: 'success'
                        })
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background: #f5f5f5;
}

.user-header {
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    padding: 60rpx 30rpx 40rpx;
    display: flex;
    align-items: center;
    gap: 30rpx;
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    background: rgba(255, 255, 255, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
}

.avatar-icon {
    font-size: 60rpx;
}

.user-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 10rpx;
}

.username {
    font-size: 32rpx;
    font-weight: 500;
    color: #fff;
}

.points {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.9);
}

.login-tip {
    font-size: 32rpx;
    color: #fff;
}

.menu-section {
    background: #fff;
    margin-top: 20rpx;
    padding: 30rpx;
}

.menu-title {
    font-size: 30rpx;
    font-weight: 500;
    margin-bottom: 20rpx;
    color: #333;
}

.menu-grid {
    display: flex;
    flex-wrap: wrap;
}

.menu-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
    gap: 10rpx;
}

.menu-icon {
    font-size: 50rpx;
}

.menu-text {
    font-size: 24rpx;
    color: #666;
}

.menu-list {
    display: flex;
    flex-direction: column;
}

.list-item {
    display: flex;
    align-items: center;
    padding: 25rpx 0;
    border-bottom: 2rpx solid #f5f5f5;
}

.list-item:last-child {
    border-bottom: none;
}

.item-icon {
    font-size: 40rpx;
    width: 60rpx;
}

.item-text {
    flex: 1;
    font-size: 28rpx;
    color: #333;
}

.item-arrow {
    font-size: 30rpx;
    color: #999;
}

.item-switch {
    transform: scale(0.8);
}

.about-section {
    padding: 60rpx 30rpx 30rpx;
    text-align: center;
}

.about-text {
    font-size: 24rpx;
    color: #999;
}
</style>