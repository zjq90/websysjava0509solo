<template>
    <view class="account-list">
        <view class="total-section">
            <view class="total-label">总资产 (元)</view>
            <view class="total-amount">{{ totalBalance | formatMoney }}</view>
            <view class="total-sub">
                <text class="asset-text">资产: {{ totalAsset | formatMoney }}</text>
                <text class="liability-text">负债: {{ totalLiability | formatMoney }}</text>
            </view>
        </view>

        <view class="account-group">
            <view class="group-title">我的账户</view>
            <view class="account-card" v-for="acc in accounts" :key="acc.id"
                  @click="goToDetail(acc)"
                  @longpress="showBalanceQuick(acc)">
                <view class="acc-icon" :style="{ background: acc.color + '20', color: acc.color }">
                    {{ acc.icon }}
                </view>
                <view class="acc-info">
                    <view class="acc-name">{{ acc.name }}</view>
                    <view class="acc-type">{{ acc.type === 'ASSET' ? '资产' : '负债' }}</view>
                </view>
                <view class="acc-balance" v-if="!acc.hideBalance">
                    <text class="balance-label">¥</text>
                    <text class="balance-value">{{ acc.balance | formatMoney }}</text>
                </view>
                <view class="acc-balance hide-balance" v-else>
                    <text>****</text>
                </view>
            </view>
        </view>

        <view class="empty" v-if="accounts.length === 0 && !loading">
            <view class="empty-icon">💳</view>
            <text>暂无账户</text>
            <text class="empty-desc">点击下方按钮添加账户</text>
        </view>

        <view class="fab-btn" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>

        <view class="balance-modal" v-if="showBalanceModal" @click="showBalanceModal = false">
            <view class="modal-content" @click.stop>
                <view class="modal-icon">{{ quickAccount ? quickAccount.icon : '💰' }}</view>
                <view class="modal-name">{{ quickAccount ? quickAccount.name : '' }}</view>
                <view class="modal-balance">
                    <text class="balance-symbol">¥</text>
                    <text class="balance-amount">{{ quickAccount ? quickAccount.balance : 0 | formatMoney }}</text>
                </view>
                <view class="modal-type" :class="quickAccount && quickAccount.type === 'ASSET' ? 'type-asset' : 'type-liability'">
                    {{ quickAccount && quickAccount.type === 'ASSET' ? '资产账户' : '负债账户' }}
                </view>
                <button class="modal-close" @click="showBalanceModal = false">关闭</button>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            accounts: [],
            totalBalance: 0,
            totalAsset: 0,
            totalLiability: 0,
            loading: false,
            showBalanceModal: false,
            quickAccount: null
        }
    },
    onLoad() {
        this.loadAccounts()
    },
    onShow() {
        this.loadAccounts()
    },
    onPullDownRefresh() {
        this.loadAccounts().then(() => uni.stopPullDownRefresh())
    },
    methods: {
        async loadAccounts() {
            this.loading = true
            try {
                const data = await this.$api.getAccounts()
                this.accounts = data
                this.$storage.saveAccounts(data)
                this.calculateTotal()
            } catch (e) {
                console.error('加载账户失败:', e)
                this.accounts = this.$storage.getAccounts()
                this.calculateTotal()
            }
            this.loading = false
        },

        calculateTotal() {
            this.totalAsset = 0
            this.totalLiability = 0
            this.accounts.forEach(acc => {
                if (acc.type === 'ASSET') {
                    this.totalAsset += parseFloat(acc.balance)
                } else {
                    this.totalLiability += parseFloat(acc.balance)
                }
            })
            this.totalBalance = this.totalAsset - this.totalLiability
        },

        showBalanceQuick(acc) {
            uni.vibrateShort()
            this.quickAccount = acc
            this.showBalanceModal = true
        },

        goToDetail(acc) {
            uni.navigateTo({ url: '/pages/account/account-edit?id=' + acc.id })
        },

        goToAdd() {
            uni.navigateTo({ url: '/pages/account/account-edit' })
        }
    }
}
</script>

<style scoped>
.account-list {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.total-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 50rpx 40rpx;
    text-align: center;
    color: #fff;
    border-radius: 0 0 40rpx 40rpx;
}

.total-label {
    font-size: 28rpx;
    opacity: 0.9;
}

.total-amount {
    font-size: 64rpx;
    font-weight: bold;
    margin: 15rpx 0;
}

.total-sub {
    display: flex;
    justify-content: center;
    gap: 40rpx;
    font-size: 24rpx;
    opacity: 0.85;
}

.asset-text {
    color: #95de64;
}

.liability-text {
    color: #ff7875;
}

.account-group {
    padding: 20rpx;
}

.group-title {
    font-size: 28rpx;
    color: #999;
    padding: 20rpx 10rpx;
    font-weight: bold;
}

.account-card {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 15rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.acc-icon {
    width: 90rpx;
    height: 90rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-right: 20rpx;
}

.acc-info {
    flex: 1;
}

.acc-name {
    font-size: 30rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 8rpx;
}

.acc-type {
    font-size: 24rpx;
    color: #999;
}

.acc-balance {
    text-align: right;
}

.balance-label {
    font-size: 24rpx;
    color: #999;
    margin-right: 4rpx;
}

.balance-value {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.hide-balance {
    font-size: 32rpx;
    color: #ccc;
    font-weight: bold;
}

.fab-btn {
    position: fixed;
    right: 40rpx;
    bottom: 120rpx;
    width: 110rpx;
    height: 110rpx;
    background: linear-gradient(135deg, #667eea, #764ba2);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 6rpx 20rpx rgba(102, 126, 234, 0.4);
    z-index: 100;
}

.fab-icon {
    color: #fff;
    font-size: 60rpx;
    font-weight: bold;
    line-height: 1;
}

.empty {
    padding: 120rpx 40rpx;
    text-align: center;
    color: #999;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-desc {
    display: block;
    font-size: 24rpx;
    margin-top: 10rpx;
    color: #ccc;
}

.balance-modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
}

.modal-content {
    background: #fff;
    border-radius: 24rpx;
    padding: 60rpx 50rpx;
    text-align: center;
    width: 80%;
    max-width: 500rpx;
}

.modal-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.modal-name {
    font-size: 32rpx;
    color: #333;
    font-weight: bold;
    margin-bottom: 20rpx;
}

.modal-balance {
    margin: 30rpx 0;
}

.balance-symbol {
    font-size: 32rpx;
    color: #667eea;
    margin-right: 8rpx;
}

.balance-amount {
    font-size: 56rpx;
    font-weight: bold;
    color: #333;
}

.modal-type {
    display: inline-block;
    padding: 8rpx 24rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    margin-bottom: 30rpx;
}

.type-asset {
    background: #f6ffed;
    color: #52c41a;
}

.type-liability {
    background: #fff1f0;
    color: #ff4d4f;
}

.modal-close {
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: #fff;
    border-radius: 40rpx;
    font-size: 28rpx;
    border: none;
}
</style>
