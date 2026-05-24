<template>
    <view class="container">
        <view class="setting-section">
            <view class="section-title">外观设置</view>
            <view class="setting-card">
                <view class="setting-item" @click="toggleTheme">
                    <view class="item-left">
                        <text class="item-icon">🌙</text>
                        <text class="item-label">夜间模式</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="isDarkMode" 
                            color="#1a73e8"
                            @change="toggleTheme"
                        />
                    </view>
                </view>
            </view>
        </view>

        <view class="setting-section">
            <view class="section-title">数据同步</view>
            <view class="setting-card">
                <view class="setting-item" @click="syncData">
                    <view class="item-left">
                        <text class="item-icon">🔄</text>
                        <text class="item-label">同步数据</text>
                    </view>
                    <view class="item-right">
                        <text class="item-value">{{ unsyncedCount }}条待同步</text>
                        <text class="item-arrow">›</text>
                    </view>
                </view>
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">📶</text>
                        <text class="item-label">仅WiFi同步</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="wifiOnly" 
                            color="#1a73e8"
                            @change="toggleWifiOnly"
                        />
                    </view>
                </view>
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">⏰</text>
                        <text class="item-label">自动同步</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="autoSync" 
                            color="#1a73e8"
                            @change="toggleAutoSync"
                        />
                    </view>
                </view>
            </view>
        </view>

        <view class="setting-section">
            <view class="section-title">通知设置</view>
            <view class="setting-card">
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">🔔</text>
                        <text class="item-label">任务推送</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="taskNotification" 
                            color="#1a73e8"
                            @change="toggleTaskNotification"
                        />
                    </view>
                </view>
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">📢</text>
                        <text class="item-label">声音提醒</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="soundAlert" 
                            color="#1a73e8"
                            @change="toggleSoundAlert"
                        />
                    </view>
                </view>
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">📳</text>
                        <text class="item-label">震动提醒</text>
                    </view>
                    <view class="item-right">
                        <switch 
                            :checked="vibrationAlert" 
                            color="#1a73e8"
                            @change="toggleVibrationAlert"
                        />
                    </view>
                </view>
            </view>
        </view>

        <view class="setting-section">
            <view class="section-title">快捷操作</view>
            <view class="setting-card">
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">🚨</text>
                        <text class="item-label">一键报警</text>
                    </view>
                    <view class="item-right">
                        <text class="item-value">110</text>
                        <text class="item-arrow">›</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="setting-section">
            <view class="section-title">关于</view>
            <view class="setting-card">
                <view class="setting-item">
                    <view class="item-left">
                        <text class="item-icon">ℹ️</text>
                        <text class="item-label">版本信息</text>
                    </view>
                    <view class="item-right">
                        <text class="item-value">v1.0.0</text>
                    </view>
                </view>
                <view class="setting-item" @click="clearCache">
                    <view class="item-left">
                        <text class="item-icon">🗑️</text>
                        <text class="item-label">清除缓存</text>
                    </view>
                    <view class="item-right">
                        <text class="item-arrow">›</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="version-info">
            <text class="version-text">车辆识别系统 v1.0.0</text>
            <text class="copyright-text">© 2024 Vehicle Recognition System</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            isDarkMode: false,
            wifiOnly: true,
            autoSync: true,
            taskNotification: true,
            soundAlert: true,
            vibrationAlert: true,
            unsyncedCount: 0
        }
    },
    onShow() {
        this.loadSettings()
        this.checkUnsynced()
    },
    methods: {
        loadSettings() {
            this.isDarkMode = uni.getStorageSync('theme') === 'dark'
            this.wifiOnly = uni.getStorageSync('wifiOnly') !== false
            this.autoSync = uni.getStorageSync('autoSync') !== false
            this.taskNotification = uni.getStorageSync('taskNotification') !== false
            this.soundAlert = uni.getStorageSync('soundAlert') !== false
            this.vibrationAlert = uni.getStorageSync('vibrationAlert') !== false
        },
        checkUnsynced() {
            const unsynced = uni.getStorageSync('unsyncedRecords') || []
            this.unsyncedCount = unsynced.length
        },
        toggleTheme(e) {
            this.isDarkMode = e.detail.value
            uni.setStorageSync('theme', this.isDarkMode ? 'dark' : 'light')
            
            if (this.isDarkMode) {
                uni.setNavigationBarColor({
                    frontColor: '#ffffff',
                    backgroundColor: '#1a1a1a'
                })
            } else {
                uni.setNavigationBarColor({
                    frontColor: '#ffffff',
                    backgroundColor: '#1a73e8'
                })
            }
            
            uni.showToast({
                title: this.isDarkMode ? '已开启夜间模式' : '已关闭夜间模式',
                icon: 'none'
            })
        },
        toggleWifiOnly(e) {
            this.wifiOnly = e.detail.value
            uni.setStorageSync('wifiOnly', this.wifiOnly)
        },
        toggleAutoSync(e) {
            this.autoSync = e.detail.value
            uni.setStorageSync('autoSync', this.autoSync)
        },
        toggleTaskNotification(e) {
            this.taskNotification = e.detail.value
            uni.setStorageSync('taskNotification', this.taskNotification)
        },
        toggleSoundAlert(e) {
            this.soundAlert = e.detail.value
            uni.setStorageSync('soundAlert', this.soundAlert)
        },
        toggleVibrationAlert(e) {
            this.vibrationAlert = e.detail.value
            uni.setStorageSync('vibrationAlert', this.vibrationAlert)
        },
        syncData() {
            uni.showLoading({
                title: '同步中...'
            })
            
            this.$syncData().then(() => {
                this.checkUnsynced()
                uni.hideLoading()
            }).catch(() => {
                uni.hideLoading()
            })
        },
        clearCache() {
            uni.showModal({
                title: '清除缓存',
                content: '确定要清除所有缓存数据吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.clearStorageSync()
                        uni.showToast({
                            title: '缓存已清除',
                            icon: 'success'
                        })
                        this.loadSettings()
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 60rpx;
}

.setting-section {
    margin-bottom: 30rpx;
}

.section-title {
    font-size: 26rpx;
    color: var(--text-secondary);
    margin-bottom: 16rpx;
    padding: 0 10rpx;
}

.setting-card {
    background: var(--card-bg);
    border-radius: 16rpx;
    overflow: hidden;
}

.setting-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid var(--border-color);
}

.setting-item:last-child {
    border-bottom: none;
}

.item-left {
    display: flex;
    align-items: center;
    gap: 20rpx;
}

.item-icon {
    font-size: 36rpx;
}

.item-label {
    font-size: 30rpx;
    color: var(--text-color);
}

.item-right {
    display: flex;
    align-items: center;
    gap: 12rpx;
}

.item-value {
    font-size: 28rpx;
    color: var(--text-secondary);
}

.item-arrow {
    font-size: 32rpx;
    color: var(--text-secondary);
}

.version-info {
    text-align: center;
    margin-top: 60rpx;
}

.version-text {
    font-size: 26rpx;
    color: var(--text-secondary);
    display: block;
    margin-bottom: 8rpx;
}

.copyright-text {
    font-size: 24rpx;
    color: var(--text-secondary);
    opacity: 0.6;
}
</style>
