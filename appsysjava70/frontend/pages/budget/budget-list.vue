<template>
    <view class="budget-list">
        <view class="month-summary">
            <view class="summary-label">本月预算</view>
            <view class="summary-amount">¥{{ totalBudget | formatMoney }}</view>
            <view class="summary-sub">
                <text class="spent-text">已支出: ¥{{ totalSpent | formatMoney }}</text>
                <text class="remaining-text" :class="remaining >= 0 ? 'text-safe' : 'text-danger'">
                    剩余: ¥{{ Math.abs(remaining) | formatMoney }}
                </text>
            </view>
        </view>

        <view class="budget-list-inner">
            <view class="budget-card" v-for="budget in budgets" :key="budget.id"
                  @click="showActions(budget)">
                <view class="budget-header">
                    <view class="budget-info">
                        <text class="budget-icon">{{ budget.category ? budget.category.icon : '📊' }}</text>
                        <view class="budget-detail">
                            <text class="budget-name">{{ budget.category ? budget.category.name : '总预算' }}</text>
                            <text class="budget-date">{{ budget.month }}</text>
                        </view>
                    </view>
                    <text class="budget-amount">¥{{ budget.amount | formatMoney }}</text>
                </view>
                <view class="budget-progress">
                    <view class="progress-info">
                        <text>已用 ¥{{ budget.spentAmount | formatMoney }}</text>
                        <text>{{ budget.usagePercent.toFixed(1) }}%</text>
                    </view>
                    <view class="progress-bar">
                        <view class="progress-fill" 
                              :style="{ width: Math.min(budget.usagePercent, 100) + '%' }"
                              :class="getProgressClass(budget.usagePercent)"></view>
                    </view>
                    <view class="progress-remaining" :class="budget.remaining >= 0 ? 'text-safe' : 'text-danger'">
                        {{ budget.remaining >= 0 ? '剩余' : '超支' }} ¥{{ Math.abs(budget.remaining) | formatMoney }}
                    </view>
                </view>
                <view class="budget-warning" v-if="budget.usagePercent >= 80">
                    <text class="warning-icon">⚠️</text>
                    <text class="warning-text">{{ budget.usagePercent >= 100 ? '已超支！' : '即将超支' }}</text>
                </view>
            </view>
        </view>

        <view class="empty" v-if="budgets.length === 0 && !loading">
            <view class="empty-icon">📊</view>
            <text>暂无预算</text>
            <text class="empty-desc">点击下方按钮设置月度预算</text>
        </view>

        <view class="fab-btn" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            budgets: [],
            totalBudget: 0,
            totalSpent: 0,
            remaining: 0,
            loading: false
        }
    },
    onLoad() {
        this.loadBudgets()
    },
    onShow() {
        this.loadBudgets()
    },
    onPullDownRefresh() {
        this.loadBudgets().then(() => uni.stopPullDownRefresh())
    },
    methods: {
        async loadBudgets() {
            this.loading = true
            try {
                const data = await this.$api.getActiveBudgets()
                this.budgets = data.map(b => ({
                    ...b,
                    usagePercent: b.spentAmount ? (b.spentAmount / b.amount * 100) : 0,
                    remaining: b.amount - (b.spentAmount || 0)
                }))
                this.calculateTotal()
            } catch (e) {
                console.error('加载预算失败:', e)
            }
            this.loading = false
        },

        calculateTotal() {
            this.totalBudget = 0
            this.totalSpent = 0
            this.budgets.forEach(b => {
                this.totalBudget += parseFloat(b.amount)
                this.totalSpent += parseFloat(b.spentAmount || 0)
            })
            this.remaining = this.totalBudget - this.totalSpent
        },

        getProgressClass(percent) {
            if (percent >= 100) return 'progress-danger'
            if (percent >= 80) return 'progress-warning'
            return 'progress-safe'
        },

        showActions(budget) {
            uni.showActionSheet({
                itemList: ['编辑预算', '删除预算'],
                success: (res) => {
                    if (res.tapIndex === 1) {
                        this.deleteBudget(budget)
                    }
                }
            })
        },

        deleteBudget(budget) {
            uni.showModal({
                title: '确认删除',
                content: `确定要删除${budget.category ? budget.category.name : '总'}预算吗？`,
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await this.$api.deleteBudget(budget.id)
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            this.loadBudgets()
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        goToAdd() {
            uni.showModal({
                title: '设置预算',
                editable: true,
                placeholderText: '请输入本月预算金额',
                success: async (res) => {
                    if (res.confirm && res.content) {
                        const amount = parseFloat(res.content)
                        if (isNaN(amount) || amount <= 0) {
                            uni.showToast({ title: '请输入有效金额', icon: 'none' })
                            return
                        }
                        try {
                            const now = new Date()
                            const month = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
                            await this.$api.createBudget({
                                amount: amount,
                                month: month,
                                categoryId: null
                            })
                            uni.showToast({ title: '设置成功', icon: 'success' })
                            this.loadBudgets()
                        } catch (e) {
                            uni.showToast({ title: '设置失败', icon: 'none' })
                        }
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.budget-list {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.month-summary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 50rpx 40rpx;
    text-align: center;
    color: #fff;
    border-radius: 0 0 40rpx 40rpx;
}

.summary-label {
    font-size: 28rpx;
    opacity: 0.9;
}

.summary-amount {
    font-size: 56rpx;
    font-weight: bold;
    margin: 15rpx 0;
}

.summary-sub {
    display: flex;
    justify-content: center;
    gap: 40rpx;
    font-size: 24rpx;
}

.spent-text {
    color: #ffccc7;
}

.remaining-text {
    color: #95de64;
}

.text-safe {
    color: #95de64;
}

.text-danger {
    color: #ffccc7;
}

.budget-list-inner {
    padding: 20rpx;
}

.budget-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.budget-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.budget-info {
    display: flex;
    align-items: center;
}

.budget-icon {
    font-size: 48rpx;
    margin-right: 15rpx;
}

.budget-detail {
    display: flex;
    flex-direction: column;
}

.budget-name {
    font-size: 30rpx;
    color: #333;
    font-weight: bold;
}

.budget-date {
    font-size: 24rpx;
    color: #999;
    margin-top: 5rpx;
}

.budget-amount {
    font-size: 36rpx;
    font-weight: bold;
    color: #667eea;
}

.budget-progress {
    margin-top: 15rpx;
}

.progress-info {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 10rpx;
}

.progress-bar {
    height: 12rpx;
    background: #f0f0f0;
    border-radius: 6rpx;
    overflow: hidden;
}

.progress-fill {
    height: 100%;
    border-radius: 6rpx;
    transition: width 0.3s;
}

.progress-safe {
    background: linear-gradient(90deg, #52c41a, #73d13d);
}

.progress-warning {
    background: linear-gradient(90deg, #faad14, #ffc53d);
}

.progress-danger {
    background: linear-gradient(90deg, #ff4d4f, #ff7875);
}

.progress-remaining {
    font-size: 24rpx;
    margin-top: 10rpx;
    text-align: right;
}

.budget-warning {
    display: flex;
    align-items: center;
    margin-top: 15rpx;
    padding: 15rpx;
    background: #fffbe6;
    border-radius: 8rpx;
}

.warning-icon {
    font-size: 28rpx;
    margin-right: 10rpx;
}

.warning-text {
    font-size: 24rpx;
    color: #faad14;
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
</style>
