<template>
    <view class="bill-list">
        <view class="date-filter">
            <picker mode="date" :value="currentMonth" @change="onMonthChange" :fields="'month'">
                <view class="month-picker">
                    <text>{{ currentMonth }}</text>
                    <text class="arrow">▼</text>
                </view>
            </picker>
            <view class="month-stats">
                <view class="stat-item">
                    <text class="stat-label">收入</text>
                    <text class="stat-income">+{{ monthIncome | formatMoney }}</text>
                </view>
                <view class="stat-divider"></view>
                <view class="stat-item">
                    <text class="stat-label">支出</text>
                    <text class="stat-expense">-{{ monthExpense | formatMoney }}</text>
                </view>
                <view class="stat-divider"></view>
                <view class="stat-item">
                    <text class="stat-label">结余</text>
                    <text :class="monthBalance >= 0 ? 'stat-income' : 'stat-expense'">
                        {{ monthBalance >= 0 ? '+' : '' }}{{ monthBalance | formatMoney }}
                    </text>
                </view>
            </view>
        </view>

        <view class="bills-container">
            <view class="day-group" v-for="(group, date) in groupedBills" :key="date">
                <view class="day-header">
                    <text class="day-date">{{ date }}</text>
                    <view class="day-stats">
                        <text class="day-income" v-if="getDayIncome(group) > 0">+{{ getDayIncome(group) | formatMoney }}</text>
                        <text class="day-expense" v-if="getDayExpense(group) > 0">-{{ getDayExpense(group) | formatMoney }}</text>
                    </view>
                </view>
                <view class="bill-list-inner">
                    <view class="bill-item" v-for="(bill, index) in group" :key="bill.id || bill.clientId"
                          @click="goToDetail(bill)"
                          @longpress="showActions(bill)"
                          @touchstart="onTouchStart($event, bill.clientId || bill.id)"
                          @touchmove="onTouchMove($event, bill.clientId || bill.id)"
                          @touchend="onTouchEnd(bill.clientId || bill.id)">
                        <view class="bill-content" :style="{ transform: 'translateX(' + (billOffsets[bill.clientId || bill.id] || 0) + 'px)' }">
                            <view class="bill-icon" :class="bill.type === 'INCOME' ? 'icon-income' : 'icon-expense'">
                                {{ bill.category ? bill.category.icon : '💰' }}
                            </view>
                            <view class="bill-info">
                                <view class="bill-title">
                                    {{ bill.merchant || (bill.category ? bill.category.name : '消费') }}
                                    <text class="sync-tag" v-if="bill.syncStatus !== 'SYNCED'"
                                          :class="'sync-' + (bill.syncStatus || '').toLowerCase()">
                                        {{ bill.syncStatus | formatSyncStatus }}
                                    </text>
                                </view>
                                <view class="bill-sub">
                                    {{ bill.category ? bill.category.name : '' }}
                                    <text v-if="bill.account"> · {{ bill.account.name }}</text>
                                    <text v-if="bill.remark"> · {{ bill.remark }}</text>
                                </view>
                            </view>
                            <view class="bill-amount" :class="bill.type === 'INCOME' ? 'amount-income' : 'amount-expense'">
                                {{ bill.type === 'INCOME' ? '+' : '-' }}{{ bill.amount | formatMoney }}
                            </view>
                        </view>
                        <view class="bill-actions" :style="{ right: '-' + (billOffsets[bill.clientId || bill.id] || 0) + 'px' }">
                            <view class="action-btn btn-edit" @click.stop="editCategory(bill)">改分类</view>
                            <view class="action-btn btn-delete" @click.stop="deleteBill(bill)">删除</view>
                        </view>
                    </view>
                </view>
            </view>

            <view class="empty" v-if="Object.keys(groupedBills).length === 0 && !loading">
                <view class="empty-icon">📋</view>
                <text>本月暂无账单记录</text>
            </view>
        </view>

        <view class="category-picker" v-if="showCategoryPicker" @click="showCategoryPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择分类</text>
                    <text class="picker-close" @click="showCategoryPicker = false">×</text>
                </view>
                <scroll-view scroll-y class="picker-body">
                    <view class="category-list">
                        <view class="cat-item" v-for="cat in categories" :key="cat.id"
                              @click="confirmCategory(cat)">
                            <text class="cat-icon">{{ cat.icon }}</text>
                            <text class="cat-name">{{ cat.name }}</text>
                        </view>
                    </view>
                </scroll-view>
            </view>
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
            currentMonth: '',
            bills: [],
            categories: [],
            monthIncome: 0,
            monthExpense: 0,
            monthBalance: 0,
            loading: false,
            billOffsets: {},
            touchStartX: 0,
            currentTouchKey: null,
            showCategoryPicker: false,
            currentBill: null
        }
    },
    computed: {
        groupedBills() {
            const groups = {}
            this.bills.forEach(bill => {
                const date = new Date(bill.transactionTime || bill.createdAt)
                const key = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
                if (!groups[key]) groups[key] = []
                groups[key].push(bill)
            })
            return groups
        }
    },
    onLoad() {
        const now = new Date()
        this.currentMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
        this.loadCategories()
        this.loadBills()
    },
    onShow() {
        this.loadBills()
    },
    onPullDownRefresh() {
        this.loadBills().then(() => uni.stopPullDownRefresh())
    },
    methods: {
        async loadCategories() {
            try {
                this.categories = await this.$api.getCategoriesByType('EXPENSE')
            } catch (e) {
                this.categories = this.$storage.getCategories().filter(c => c.type === 'EXPENSE')
            }
        },

        async loadBills() {
            this.loading = true
            try {
                const [year, month] = this.currentMonth.split('-')
                const startDate = `${year}-${month}-01`
                const lastDay = new Date(parseInt(year), parseInt(month), 0).getDate()
                const endDate = `${year}-${month}-${lastDay}`

                const data = await this.$api.getBillsByRange(startDate, endDate)
                const pendingBills = this.$storage.getPendingBills()

                const filteredPending = pendingBills.filter(b => {
                    const d = new Date(b.transactionTime || b.createdAt)
                    return d.getFullYear() === parseInt(year) && (d.getMonth() + 1) === parseInt(month)
                })

                this.bills = [...filteredPending, ...data].sort((a, b) => {
                    return new Date(b.transactionTime || b.createdAt) - new Date(a.transactionTime || a.createdAt)
                })

                this.$storage.cacheRecentBills(this.bills)
                this.calculateStats()
            } catch (e) {
                console.error('加载账单失败:', e)
                this.bills = this.$storage.getRecentBillsFromCache()
                this.calculateStats()
            }
            this.loading = false
        },

        calculateStats() {
            this.monthIncome = 0
            this.monthExpense = 0
            this.bills.forEach(bill => {
                if (bill.type === 'INCOME') {
                    this.monthIncome += parseFloat(bill.amount)
                } else {
                    this.monthExpense += parseFloat(bill.amount)
                }
            })
            this.monthBalance = this.monthIncome - this.monthExpense
        },

        getDayIncome(bills) {
            return bills.filter(b => b.type === 'INCOME').reduce((sum, b) => sum + parseFloat(b.amount), 0)
        },

        getDayExpense(bills) {
            return bills.filter(b => b.type === 'EXPENSE').reduce((sum, b) => sum + parseFloat(b.amount), 0)
        },

        onMonthChange(e) {
            this.currentMonth = e.detail.value
            this.loadBills()
        },

        goToDetail(bill) {
            if (bill.id) {
                uni.navigateTo({ url: '/pages/bill/bill-detail?id=' + bill.id })
            } else {
                uni.showToast({ title: '待同步账单', icon: 'none' })
            }
        },

        goToAdd() {
            uni.navigateTo({ url: '/pages/bill/add-bill' })
        },

        showActions(bill) {
            uni.vibrateShort()
            uni.showActionSheet({
                itemList: ['修改分类', '删除账单'],
                success: (res) => {
                    if (res.tapIndex === 0) this.editCategory(bill)
                    else if (res.tapIndex === 1) this.deleteBill(bill)
                }
            })
        },

        editCategory(bill) {
            this.currentBill = bill
            this.showCategoryPicker = true
        },

        async confirmCategory(cat) {
            if (!this.currentBill) return
            try {
                if (this.currentBill.id) {
                    await this.$api.updateBillCategory(this.currentBill.id, cat.id)
                    uni.showToast({ title: '分类已更新', icon: 'success' })
                } else {
                    const pendingBills = this.$storage.getPendingBills()
                    const bill = pendingBills.find(b => b.clientId === this.currentBill.clientId)
                    if (bill) {
                        bill.categoryId = cat.id
                        bill.category = cat
                        this.$storage.set('pendingBills', pendingBills)
                    }
                }
                this.showCategoryPicker = false
                this.loadBills()
            } catch (e) {
                uni.showToast({ title: '更新失败', icon: 'none' })
            }
        },

        deleteBill(bill) {
            uni.showModal({
                title: '确认删除',
                content: '确定要删除这条账单吗？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            if (bill.id) {
                                await this.$api.deleteBill(bill.id)
                            } else {
                                this.$storage.removePendingBill(bill.clientId)
                            }
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            this.loadBills()
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        onTouchStart(e, key) {
            this.touchStartX = e.touches[0].clientX
            this.currentTouchKey = key
        },

        onTouchMove(e, key) {
            if (this.currentTouchKey !== key) return
            const moveX = e.touches[0].clientX - this.touchStartX
            if (moveX < 0 && moveX > -140) {
                this.$set(this.billOffsets, key, moveX)
            }
        },

        onTouchEnd(key) {
            const offset = this.billOffsets[key] || 0
            if (offset < -70) {
                this.$set(this.billOffsets, key, -120)
            } else {
                this.$set(this.billOffsets, key, 0)
            }
            this.currentTouchKey = null
        }
    }
}
</script>

<style scoped>
.bill-list {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 120rpx;
}

.date-filter {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 30rpx;
    color: #fff;
}

.month-picker {
    display: inline-flex;
    align-items: center;
    padding: 10rpx 20rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 30rpx;
    font-size: 28rpx;
    margin-bottom: 20rpx;
}

.month-picker .arrow {
    font-size: 20rpx;
    margin-left: 10rpx;
}

.month-stats {
    display: flex;
    justify-content: space-around;
    padding: 20rpx 0;
}

.stat-item {
    text-align: center;
    flex: 1;
}

.stat-label {
    display: block;
    font-size: 24rpx;
    opacity: 0.85;
    margin-bottom: 8rpx;
}

.stat-income {
    color: #95de64;
    font-size: 32rpx;
    font-weight: bold;
}

.stat-expense {
    color: #ff7875;
    font-size: 32rpx;
    font-weight: bold;
}

.stat-divider {
    width: 1rpx;
    background: rgba(255, 255, 255, 0.3);
}

.bills-container {
    padding: 20rpx;
}

.day-group {
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
}

.day-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    background: #fafafa;
    border-bottom: 1rpx solid #f0f0f0;
}

.day-date {
    font-size: 28rpx;
    color: #666;
    font-weight: bold;
}

.day-stats {
    font-size: 24rpx;
}

.day-income {
    color: #52c41a;
    margin-right: 20rpx;
}

.day-expense {
    color: #ff4d4f;
}

.bill-item {
    position: relative;
    overflow: hidden;
    border-bottom: 1rpx solid #f0f0f0;
}

.bill-item:last-child {
    border-bottom: none;
}

.bill-content {
    display: flex;
    align-items: center;
    padding: 25rpx 30rpx;
    background: #fff;
    position: relative;
    z-index: 2;
    transition: transform 0.2s;
}

.bill-actions {
    position: absolute;
    top: 0;
    right: 0;
    height: 100%;
    display: flex;
    z-index: 1;
}

.action-btn {
    width: 120rpx;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 26rpx;
}

.btn-edit {
    background: #1890ff;
}

.btn-delete {
    background: #ff4d4f;
}

.bill-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
    margin-right: 20rpx;
}

.icon-income {
    background: #f6ffed;
}

.icon-expense {
    background: #fff1f0;
}

.bill-info {
    flex: 1;
}

.bill-title {
    font-size: 30rpx;
    color: #333;
    display: flex;
    align-items: center;
}

.sync-tag {
    margin-left: 10rpx;
    padding: 2rpx 8rpx;
    border-radius: 4rpx;
    font-size: 20rpx;
}

.sync-pending {
    background: #fffbe6;
    color: #faad14;
}

.sync-failed {
    background: #fff1f0;
    color: #ff4d4f;
}

.bill-sub {
    font-size: 24rpx;
    color: #999;
    margin-top: 6rpx;
}

.bill-amount {
    font-size: 32rpx;
    font-weight: bold;
}

.amount-income {
    color: #52c41a;
}

.amount-expense {
    color: #ff4d4f;
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

.category-picker {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    display: flex;
    align-items: flex-end;
}

.picker-content {
    background: #fff;
    width: 100%;
    max-height: 70vh;
    border-radius: 20rpx 20rpx 0 0;
}

.picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
    font-size: 32rpx;
    font-weight: bold;
}

.picker-close {
    font-size: 48rpx;
    color: #999;
    line-height: 1;
}

.picker-body {
    max-height: 50vh;
    padding: 20rpx;
}

.category-list {
    display: flex;
    flex-wrap: wrap;
}

.cat-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 25rpx 0;
    border-radius: 12rpx;
}

.cat-item:active {
    background: #f5f7fa;
}

.cat-icon {
    font-size: 44rpx;
    margin-bottom: 8rpx;
}

.cat-name {
    font-size: 26rpx;
    color: #333;
}

.empty {
    padding: 120rpx 0;
    text-align: center;
    color: #999;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}
</style>
