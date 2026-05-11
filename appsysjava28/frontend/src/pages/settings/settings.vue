<template>
    <view class="settings-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="menu-section card">
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">🔔</text>
                    <text class="menu-label">消息通知</text>
                </view>
                <switch checked color="#1677ff" />
            </view>
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">👵</text>
                    <text class="menu-label">长辈模式</text>
                </view>
                <switch 
                    :checked="elderlyMode" 
                    color="#1677ff"
                    @change="onElderlyModeChange"
                />
            </view>
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">🔒</text>
                    <text class="menu-label">账户安全</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
        </view>

        <view class="menu-section card">
            <view class="menu-item" @click="clearCache">
                <view class="menu-left">
                    <text class="menu-icon">🗑️</text>
                    <text class="menu-label">清除缓存</text>
                </view>
                <text class="menu-value">{{ cacheSize }}</text>
            </view>
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">📱</text>
                    <text class="menu-label">检查更新</text>
                </view>
                <text class="menu-value">v1.0.0</text>
            </view>
            <view class="menu-item" @click="goToAbout">
                <view class="menu-left">
                    <text class="menu-icon">ℹ️</text>
                    <text class="menu-label">关于我们</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
        </view>

        <view class="menu-section card">
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">📜</text>
                    <text class="menu-label">用户协议</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
            <view class="menu-item">
                <view class="menu-left">
                    <text class="menu-icon">🛡️</text>
                    <text class="menu-label">隐私政策</text>
                </view>
                <text class="menu-arrow">></text>
            </view>
        </view>

        <view class="logout-section" v-if="isLoggedIn">
            <view class="logout-btn" @click="logout">
                退出登录
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            elderlyMode: false,
            isLoggedIn: false,
            cacheSize: '计算中...'
        }
    },
    onLoad() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        const token = uni.getStorageSync('token')
        this.isLoggedIn = !!token
        this.calculateCacheSize()
    },
    methods: {
        calculateCacheSize() {
            setTimeout(() => {
                this.cacheSize = '12.5MB'
            }, 500)
        },
        onElderlyModeChange(e) {
            const newValue = e.detail.value
            uni.setStorageSync('elderlyMode', newValue)
            this.elderlyMode = newValue
            uni.showToast({ 
                title: newValue ? '已开启长辈模式' : '已关闭长辈模式', 
                icon: 'none' 
            })
        },
        clearCache() {
            uni.showModal({
                title: '清除缓存',
                content: '确定要清除缓存吗？这不会影响您的个人数据。',
                success: (res) => {
                    if (res.confirm) {
                        uni.showLoading({ title: '清除中...' })
                        setTimeout(() => {
                            uni.hideLoading()
                            this.cacheSize = '0MB'
                            uni.showToast({ title: '清除成功', icon: 'success' })
                        }, 1000)
                    }
                }
            })
        },
        goToAbout() {
            uni.showModal({
                title: '关于我们',
                content: '医疗预约APP v1.0\n\n一个便捷的医疗预约平台，让就医更简单。\n\n服务热线：400-888-8888',
                showCancel: false
            })
        },
        logout() {
            uni.showModal({
                title: '退出登录',
                content: '确定要退出当前账号吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.removeStorageSync('token')
                        uni.removeStorageSync('userInfo')
                        this.isLoggedIn = false
                        uni.showToast({ title: '已退出登录', icon: 'success' })
                        setTimeout(() => {
                            uni.reLaunch({ url: '/pages/login/login' })
                        }, 1500)
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.settings-page {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.menu-section {
    margin: 20rpx 30rpx;
    padding: 0;
}

.menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 30rpx 25rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-left {
    display: flex;
    align-items: center;
}

.menu-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.menu-label {
    font-size: 30rpx;
    color: #333333;
}

.menu-arrow {
    font-size: 28rpx;
    color: #999999;
}

.menu-value {
    font-size: 28rpx;
    color: #999999;
}

.logout-section {
    padding: 40rpx 30rpx;
}

.logout-btn {
    background: #ffffff;
    color: #ff4d4f;
    text-align: center;
    padding: 30rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
    border: 1rpx solid #ff4d4f;
}

.elderly-mode .menu-label,
.elderly-mode .menu-value {
    font-size: 34rpx;
}
</style>
