<template>
    <view class="mine-container">
        <view class="user-header">
            <view class="avatar-section">
                <view class="avatar">
                    <text class="avatar-text">{{ avatarText }}</text>
                </view>
                <view class="user-info">
                    <text class="user-name">{{ userInfo.username || '未登录' }}</text>
                    <text class="user-role">{{ getRoleLabel(userInfo.role) }}</text>
                </view>
            </view>
            <view class="login-btn" v-if="!userInfo" @click="goToLogin">
                <text>登录/注册</text>
            </view>
        </view>

        <view class="stats-section" v-if="userInfo">
            <view class="stat-card" @click="goToRecords">
                <text class="stat-icon">📝</text>
                <text class="stat-value">{{ stats.recordCount || 0 }}</text>
                <text class="stat-label">田间记录</text>
            </view>
            <view class="stat-card" @click="goToOffline">
                <text class="stat-icon">📴</text>
                <text class="stat-value">{{ stats.offlineCount || 0 }}</text>
                <text class="stat-label">离线记录</text>
            </view>
            <view class="stat-card" @click="goToPlots">
                <text class="stat-icon">🗺️</text>
                <text class="stat-value">{{ stats.plotCount || 0 }}</text>
                <text class="stat-label">管理地块</text>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-group">
                <text class="group-title">功能模块</text>
                <view class="menu-item" @click="goToRecords">
                    <view class="menu-left">
                        <text class="menu-icon">📝</text>
                        <text class="menu-text">田间记录</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToPlots">
                    <view class="menu-left">
                        <text class="menu-icon">🗺️</text>
                        <text class="menu-text">地块管理</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToCrops">
                    <view class="menu-left">
                        <text class="menu-icon">🌾</text>
                        <text class="menu-text">作物品种</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToWeather">
                    <view class="menu-left">
                        <text class="menu-icon">🌤️</text>
                        <text class="menu-text">气象数据</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
            </view>

            <view class="menu-group">
                <text class="group-title">数据管理</text>
                <view class="menu-item" @click="syncOffline">
                    <view class="menu-left">
                        <text class="menu-icon">🔄</text>
                        <text class="menu-text">同步离线数据</text>
                    </view>
                    <view class="menu-badge" v-if="stats.offlineCount > 0">
                        {{ stats.offlineCount }}
                    </view>
                </view>
                <view class="menu-item" @click="goToOffline">
                    <view class="menu-left">
                        <text class="menu-icon">📴</text>
                        <text class="menu-text">离线记录管理</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
            </view>

            <view class="menu-group">
                <text class="group-title">其他</text>
                <view class="menu-item" @click="showAbout">
                    <view class="menu-left">
                        <text class="menu-icon">ℹ️</text>
                        <text class="menu-text">关于系统</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="showSettings">
                    <view class="menu-left">
                        <text class="menu-icon">⚙️</text>
                        <text class="menu-text">设置</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="logout-section" v-if="userInfo">
            <view class="logout-btn" @click="logout">
                <text>退出登录</text>
            </view>
        </view>

        <view class="version-info">
            <text>版本 v1.0.0</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            userInfo: null,
            stats: {
                recordCount: 0,
                offlineCount: 0,
                plotCount: 0
            }
        }
    },

    computed: {
        avatarText() {
            if (this.userInfo && this.userInfo.username) {
                return this.userInfo.username.charAt(0).toUpperCase()
            }
            return 'U'
        }
    },

    onShow() {
        this.loadUserInfo()
        this.loadStats()
    },

    methods: {
        loadUserInfo() {
            this.userInfo = this.$store.getters.userInfo || uni.getStorageSync('userInfo')
        },

        async loadStats() {
            const userId = this.userInfo && this.userInfo.userId
            if (!userId) {
                this.stats = { recordCount: 0, offlineCount: 0, plotCount: 0 }
                return
            }

            try {
                const offlineRecords = uni.getStorageSync('offlineRecords') || []
                this.stats.offlineCount = offlineRecords.length

                const records = await this.$request({
                    url: '/field-records/observer/' + userId,
                    method: 'GET'
                })
                this.stats.recordCount = (records && records.length) || 0

                const plots = await this.$request({
                    url: '/plots',
                    method: 'GET'
                })
                this.stats.plotCount = (plots && plots.length) || 0
            } catch (e) {
                console.error('加载统计数据失败', e)
            }
        },

        getRoleLabel(role) {
            const map = {
                'ADMIN': '系统管理员',
                'AGRONOMIST': '农艺师',
                'TECHNICIAN': '技术员',
                'OBSERVER': '观测员'
            }
            return map[role] || role || '普通用户'
        },

        goToLogin() {
            uni.navigateTo({ url: '/pages/login/login' })
        },

        goToRecords() {
            uni.switchTab({ url: '/pages/field-record/list' })
        },

        goToOffline() {
            uni.showToast({ title: '离线记录管理功能开发中', icon: 'none' })
        },

        goToPlots() {
            uni.navigateTo({ url: '/pages/plot/list' })
        },

        goToCrops() {
            uni.navigateTo({ url: '/pages/crop/list' })
        },

        goToWeather() {
            uni.navigateTo({ url: '/pages/weather/weather' })
        },

        async syncOffline() {
            if (this.stats.offlineCount === 0) {
                uni.showToast({ title: '暂无待同步数据', icon: 'none' })
                return
            }

            uni.showModal({
                title: '同步确认',
                content: `确定要同步 ${this.stats.offlineCount} 条离线记录吗？`,
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$store.dispatch('syncOfflineRecords')
                            uni.showToast({ title: '同步成功', icon: 'success' })
                            this.loadStats()
                        } catch (e) {
                            uni.showToast({ title: '同步失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        showAbout() {
            uni.showModal({
                title: '关于系统',
                content: '田间数据采集系统 v1.0.0\n\n技术支持：\n- 后端: Spring Boot + H2\n- 前端: uni-app\n- 数据加密: AES-256',
                showCancel: false,
                confirmText: '知道了'
            })
        },

        showSettings() {
            uni.showToast({ title: '设置功能开发中', icon: 'none' })
        },

        logout() {
            uni.showModal({
                title: '退出登录',
                content: '确定要退出当前账号吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$store.dispatch('logout')
                        uni.reLaunch({ url: '/pages/login/login' })
                    }
                }
            })
        }
    }
}
</script>

<style>
.mine-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 40rpx;
}

.user-header {
    background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
    padding: 60rpx 30rpx 80rpx;
    position: relative;
}

.avatar-section {
    display: flex;
    align-items: center;
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
}

.avatar-text {
    font-size: 48rpx;
    color: #fff;
    font-weight: bold;
}

.user-info {
    display: flex;
    flex-direction: column;
}

.user-name {
    font-size: 36rpx;
    color: #fff;
    font-weight: bold;
}

.user-role {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-top: 8rpx;
}

.login-btn {
    position: absolute;
    right: 30rpx;
    top: 50%;
    transform: translateY(-50%);
    background-color: rgba(255, 255, 255, 0.2);
    padding: 12rpx 30rpx;
    border-radius: 30rpx;
}

.login-btn text {
    font-size: 26rpx;
    color: #fff;
}

.stats-section {
    display: flex;
    margin: -40rpx 20rpx 20rpx;
    background-color: #fff;
    border-radius: 16rpx;
    padding: 30rpx 10rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.stat-card {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.stat-icon {
    font-size: 40rpx;
    margin-bottom: 10rpx;
}

.stat-value {
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
}

.stat-label {
    font-size: 24rpx;
    color: #999;
    margin-top: 8rpx;
}

.menu-section {
    margin: 20rpx;
}

.menu-group {
    background-color: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 20rpx;
}

.group-title {
    font-size: 24rpx;
    color: #999;
    padding: 20rpx 24rpx 10rpx;
    display: block;
}

.menu-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 28rpx 24rpx;
    border-bottom: 1rpx solid #f5f5f5;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-left {
    display: flex;
    align-items: center;
}

.menu-icon {
    font-size: 32rpx;
    margin-right: 20rpx;
}

.menu-text {
    font-size: 30rpx;
    color: #333;
}

.menu-arrow {
    font-size: 32rpx;
    color: #ccc;
}

.menu-badge {
    background-color: #f44336;
    color: #fff;
    font-size: 22rpx;
    padding: 4rpx 14rpx;
    border-radius: 20rpx;
    min-width: 40rpx;
    text-align: center;
}

.logout-section {
    margin: 40rpx 20rpx;
}

.logout-btn {
    background-color: #fff;
    color: #f44336;
    height: 88rpx;
    line-height: 88rpx;
    text-align: center;
    border-radius: 12rpx;
    font-size: 32rpx;
}

.version-info {
    text-align: center;
    padding: 30rpx;
}

.version-info text {
    font-size: 24rpx;
    color: #ccc;
}
</style>
