<template>
    <view class="bill-detail">
        <view class="detail-header" :class="bill.type === 'INCOME' ? 'header-income' : 'header-expense'">
            <view class="type-icon">{{ bill.category ? bill.category.icon : '💰' }}</view>
            <view class="type-name">{{ bill.category ? bill.category.name : (bill.type === 'INCOME' ? '收入' : '支出') }}</view>
            <view class="amount" :class="bill.type === 'INCOME' ? 'amount-income' : 'amount-expense'">
                {{ bill.type === 'INCOME' ? '+' : '-' }}{{ bill.amount | formatMoney }}
            </view>
            <view class="sync-status" v-if="bill.syncStatus !== 'SYNCED'"
                  :class="bill.syncStatus === 'FAILED' ? 'status-failed' : 'status-pending'">
                {{ bill.syncStatus | formatSyncStatus }}
                <text class="retry-btn" v-if="bill.syncStatus === 'FAILED'" @click="retrySync">重试</text>
            </view>
        </view>

        <view class="detail-content">
            <view class="info-item">
                <text class="label">账户</text>
                <text class="value">{{ bill.account ? bill.account.name : '-' }}</text>
            </view>
            <view class="info-item" v-if="bill.merchant">
                <text class="label">商家</text>
                <text class="value">{{ bill.merchant }}</text>
            </view>
            <view class="info-item">
                <text class="label">时间</text>
                <text class="value">{{ bill.transactionTime | formatDate('yyyy-MM-dd HH:mm') }}</text>
            </view>
            <view class="info-item" v-if="bill.remark">
                <text class="label">备注</text>
                <text class="value">{{ bill.remark }}</text>
            </view>
            <view class="info-item">
                <text class="label">创建时间</text>
                <text class="value">{{ bill.createdAt | formatDate('yyyy-MM-dd HH:mm:ss') }}</text>
            </view>
        </view>

        <view class="bottom-bar">
            <button class="btn-edit" @click="goToEdit">编辑</button>
            <button class="btn-delete" @click="deleteBill">删除</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            bill: {
                id: null,
                type: 'EXPENSE',
                amount: 0,
                category: null,
                account: null,
                merchant: '',
                transactionTime: '',
                remark: '',
                createdAt: '',
                syncStatus: 'SYNCED'
            }
        }
    },
    onLoad(options) {
        if (options.id) {
            this.loadBillDetail(options.id)
        }
    },
    methods: {
        async loadBillDetail(id) {
            try {
                this.bill = await this.$api.getBillById(id)
            } catch (e) {
                console.error('加载账单详情失败:', e)
                uni.showToast({ title: '加载失败', icon: 'none' })
            }
        },

        goToEdit() {
            if (this.bill.id) {
                uni.navigateTo({ url: '/pages/bill/add-bill?id=' + this.bill.id })
            } else {
                uni.showToast({ title: '待同步账单无法编辑', icon: 'none' })
            }
        },

        deleteBill() {
            uni.showModal({
                title: '确认删除',
                content: '确定要删除这条账单吗？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            if (this.bill.id) {
                                await this.$api.deleteBill(this.bill.id)
                            } else {
                                this.$storage.removePendingBill(this.bill.clientId)
                            }
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            setTimeout(() => {
                                uni.navigateBack()
                            }, 1000)
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        async retrySync() {
            try {
                const pendingBills = this.$storage.getPendingBills()
                const bill = pendingBills.find(b => b.clientId === this.bill.clientId)
                if (bill) {
                    uni.showLoading({ title: '同步中...' })
                    await this.$api.createBill(bill)
                    this.$storage.removePendingBill(bill.clientId)
                    uni.hideLoading()
                    uni.showToast({ title: '同步成功', icon: 'success' })
                    this.bill.syncStatus = 'SYNCED'
                }
            } catch (e) {
                uni.hideLoading()
                uni.showToast({ title: '同步失败', icon: 'none' })
            }
        }
    }
}
</script>

<style scoped>
.bill-detail {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.detail-header {
    padding: 60rpx 40rpx;
    text-align: center;
    color: #fff;
}

.header-income {
    background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
}

.header-expense {
    background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
}

.type-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.type-name {
    font-size: 32rpx;
    opacity: 0.9;
    margin-bottom: 20rpx;
}

.amount {
    font-size: 72rpx;
    font-weight: bold;
}

.amount-income {
    color: #fff;
}

.amount-expense {
    color: #fff;
}

.sync-status {
    margin-top: 20rpx;
    font-size: 24rpx;
    opacity: 0.9;
}

.status-pending {
    color: #ffe58f;
}

.status-failed {
    color: #fff;
}

.retry-btn {
    margin-left: 10rpx;
    padding: 4rpx 12rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 20rpx;
}

.detail-content {
    background: #fff;
    margin: 20rpx;
    border-radius: 16rpx;
    overflow: hidden;
}

.info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx 40rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
    border-bottom: none;
}

.label {
    font-size: 28rpx;
    color: #999;
}

.value {
    font-size: 28rpx;
    color: #333;
    max-width: 400rpx;
    text-align: right;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx 40rpx;
    display: flex;
    gap: 20rpx;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-edit {
    flex: 1;
    height: 88rpx;
    line-height: 88rpx;
    background: #1890ff;
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    border: none;
}

.btn-delete {
    flex: 1;
    height: 88rpx;
    line-height: 88rpx;
    background: #ff4d4f;
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    border: none;
}
</style>
