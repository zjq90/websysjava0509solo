<template>
    <view class="setting">
        <view class="user-section">
            <view class="user-avatar">👤</view>
            <view class="user-info">
                <text class="user-name">极速记账</text>
                <text class="user-desc">个人财务管家</text>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-group">
                <view class="menu-item" @click="goTo('/pages/budget/budget-list')">
                    <text class="menu-icon">📊</text>
                    <text class="menu-title">预算管理</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goTo('/pages/category/category-list')">
                    <text class="menu-icon">🏷️</text>
                    <text class="menu-title">分类管理</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goTo('/pages/template/template-list')">
                    <text class="menu-icon">📋</text>
                    <text class="menu-title">记账模板</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>

            <view class="menu-group">
                <view class="menu-item" @click="syncData">
                    <text class="menu-icon">🔄</text>
                    <text class="menu-title">数据同步</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="exportData">
                    <text class="menu-icon">📤</text>
                    <text class="menu-title">导出数据</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="clearCache">
                    <text class="menu-icon">🗑️</text>
                    <text class="menu-title">清除缓存</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>

            <view class="menu-group">
                <view class="menu-item" @click="showAbout">
                    <text class="menu-icon">ℹ️</text>
                    <text class="menu-title">关于我们</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="checkUpdate">
                    <text class="menu-icon">⬆️</text>
                    <text class="menu-title">检查更新</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="sync-status" v-if="pendingCount > 0">
            <view class="sync-icon">📡</view>
            <view class="sync-info">
                <text class="sync-title">待同步账单</text>
                <text class="sync-desc">{{ pendingCount }} 条账单待同步</text>
            </view>
            <button class="sync-btn" @click="syncPendingBills">立即同步</button>
        </view>

        <view class="version">
            <text>版本 1.0.0</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            pendingCount: 0
        }
    },
    onShow() {
        this.checkPendingBills()
    },
    methods: {
        checkPendingBills() {
            const pending = this.$storage.getPendingBills()
            this.pendingCount = pending.length
        },

        goTo(url) {
            uni.navigateTo({ url })
        },

        async syncPendingBills() {
            const pendingBills = this.$storage.getPendingBills()
            if (pendingBills.length === 0) {
                uni.showToast({ title: '暂无待同步数据', icon: 'none' })
                return
            }

            uni.showLoading({ title: '同步中...' })
            let successCount = 0
            let failCount = 0

            for (const bill of pendingBills) {
                try {
                    await this.$api.createBill(bill)
                    this.$storage.removePendingBill(bill.clientId)
                    successCount++
                } catch (e) {
                    this.$storage.updatePendingBillStatus(bill.clientId, 'FAILED', e.message)
                    failCount++
                }
            }

            uni.hideLoading()
            this.checkPendingBills()

            if (failCount === 0) {
                uni.showToast({ title: `同步成功 ${successCount} 条`, icon: 'success' })
            } else {
                uni.showModal({
                    title: '同步完成',
                    content: `成功 ${successCount} 条，失败 ${failCount} 条`,
                    showCancel: false
                })
            }
        },

        syncData() {
            uni.showModal({
                title: '数据同步',
                content: '确定要同步所有数据吗？',
                success: async (res) => {
                    if (res.confirm) {
                        await this.syncPendingBills()
                    }
                }
            })
        },

        exportData() {
            uni.showToast({ title: '功能开发中', icon: 'none' })
        },

        clearCache() {
            uni.showModal({
                title: '清除缓存',
                content: '确定要清除本地缓存吗？这不会删除已同步的数据。',
                success: (res) => {
                    if (res.confirm) {
                        this.$storage.remove('recentBills')
                        this.$storage.remove('categories')
                        this.$storage.remove('accounts')
                        this.$storage.remove('templates')
                        uni.showToast({ title: '缓存已清除', icon: 'success' })
                    }
                }
            })
        },

        showAbout() {
            uni.showModal({
                title: '关于极速记账',
                content: '极速记账 v1.0.0\n\n一款简洁、高效的个人记账应用，支持语音记账、拍照记账、离线记账等功能。\n\n© 2024 极速记账',
                showCancel: false
            })
        },

        checkUpdate() {
            uni.showToast({ title: '已是最新版本', icon: 'success' })
        }
    }
}
</script>

<style scoped>
.setting {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 40rpx;
}

.user-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 60rpx 40rpx;
    display: flex;
    align-items: center;
    color: #fff;
}

.user-avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60rpx;
    margin-right: 30rpx;
}

.user-info {
    flex: 1;
}

.user-name {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
}

.user-desc {
    display: block;
    font-size: 26rpx;
    opacity: 0.85;
}

.menu-section {
    padding: 20rpx;
}

.menu-group {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-icon {
    font-size: 36rpx;
    margin-right: 20rpx;
}

.menu-title {
    flex: 1;
    font-size: 30rpx;
    color: #333;
}

.menu-arrow {
    font-size: 36rpx;
    color: #ccc;
}

.sync-status {
    margin: 20rpx;
    padding: 30rpx;
    background: #fffbe6;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    border: 1rpx solid #ffe58f;
}

.sync-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.sync-info {
    flex: 1;
}

.sync-title {
    display: block;
    font-size: 28rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 5rpx;
}

.sync-desc {
    display: block;
    font-size: 24rpx;
    color: #faad14;
}

.sync-btn {
    padding: 15rpx 30rpx;
    background: #faad14;
    color: #fff;
    border: none;
    border-radius: 30rpx;
    font-size: 26rpx;
    line-height: 1.5;
}

.version {
    text-align: center;
    padding: 40rpx;
    font-size: 24rpx;
    color: #ccc;
}
</style>
