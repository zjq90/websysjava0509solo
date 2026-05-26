<template>
    <view class="dashboard">
        <view class="header-gradient">
            <view class="total-card">
                <view class="total-label">总资产 (元)</view>
                <view class="total-amount">{{ dashboard.totalAssets | formatMoney }}</view>
                <view class="liability-row">
                    <text class="liability-text">总负债: {{ dashboard.totalLiabilities | formatMoney }}</text>
                </view>
            </view>

            <view class="today-card">
                <view class="today-row">
                    <view class="today-item">
                        <text class="today-label">今日收入</text>
                        <text class="today-income">+{{ dashboard.todayIncome | formatMoney }}</text>
                    </view>
                    <view class="today-divider"></view>
                    <view class="today-item">
                        <text class="today-label">今日支出</text>
                        <text class="today-expense">-{{ dashboard.todayExpense | formatMoney }}</text>
                    </view>
                </view>
                <view class="balance-row" :class="dashboard.todayBalance >= 0 ? 'balance-positive' : 'balance-negative'">
                    <text class="balance-label">今日收支差额: </text>
                    <text class="balance-amount">{{ dashboard.todayBalance >= 0 ? '+' : '' }}{{ dashboard.todayBalance | formatMoney }}</text>
                </view>
            </view>

            <view class="budget-warning" v-if="dashboard.budgetWarning">
                <view class="warning-icon">⚠️</view>
                <view class="warning-text">
                    <text class="warning-title">预算超支警告</text>
                    <text class="warning-desc">已使用预算的 {{ dashboard.budgetUsagePercent.toFixed(1) }}%</text>
                </view>
            </view>
        </view>

        <view class="quick-actions card">
            <view class="action-item" @click="goToAddBill">
                <view class="action-icon bg-primary">📝</view>
                <text class="action-text">快速记账</text>
            </view>
            <view class="action-item" @click="goToScan">
                <view class="action-icon bg-green">📷</view>
                <text class="action-text">扫一扫</text>
            </view>
            <view class="action-item" @click="goToVoice">
                <view class="action-icon bg-orange">🎤</view>
                <text class="action-text">语音记账</text>
            </view>
            <view class="action-item" @click="goToTemplates">
                <view class="action-icon bg-purple">📋</view>
                <text class="action-text">记账模板</text>
            </view>
        </view>

        <view class="month-summary card">
            <view class="card-header">
                <text class="card-title">本月统计</text>
                <text class="card-more" @click="goToBillList">查看全部 →</text>
            </view>
            <view class="month-row">
                <view class="month-item">
                    <text class="month-label">本月收入</text>
                    <text class="month-income">+{{ dashboard.monthIncome | formatMoney }}</text>
                </view>
                <view class="month-item">
                    <text class="month-label">本月支出</text>
                    <text class="month-expense">-{{ dashboard.monthExpense | formatMoney }}</text>
                </view>
                <view class="month-item">
                    <text class="month-label">日均支出</text>
                    <text class="month-avg">{{ dashboard.monthAverageExpense | formatMoney }}</text>
                </view>
            </view>
            <view class="budget-progress" v-if="dashboard.budgetUsagePercent > 0">
                <view class="progress-label">
                    <text>预算使用</text>
                    <text>{{ dashboard.budgetUsagePercent.toFixed(1) }}%</text>
                </view>
                <view class="progress-bar">
                    <view class="progress-fill" :style="{ width: Math.min(dashboard.budgetUsagePercent, 100) + '%' }"
                          :class="dashboard.budgetUsagePercent >= 80 ? 'progress-danger' : ''"></view>
                </view>
            </view>
        </view>

        <view class="abnormal-list card" v-if="dashboard.abnormalExpenses && dashboard.abnormalExpenses.length > 0">
            <view class="card-header">
                <text class="card-title danger-color">异常消费提醒</text>
            </view>
            <view class="abnormal-item" v-for="(item, index) in dashboard.abnormalExpenses" :key="index">
                <view class="abnormal-icon">🔔</view>
                <view class="abnormal-content">
                    <view class="abnormal-title">{{ item.categoryName }} - {{ item.merchant || '消费' }}</view>
                    <view class="abnormal-desc">单笔消费 ¥{{ item.amount | formatMoney }}，是月均的 {{ item.multiple }} 倍</view>
                </view>
                <view class="abnormal-amount">-{{ item.amount | formatMoney }}</view>
            </view>
        </view>

        <view class="recent-bills card">
            <view class="card-header">
                <text class="card-title">最近账单</text>
                <text class="card-more" @click="goToBillList">更多 →</text>
            </view>
            <view class="bill-list">
                <view class="bill-item" v-for="(bill, index) in recentBills" :key="index"
                      @click="goToBillDetail(bill)"
                      @longpress="showBillActions(bill)"
                      @touchstart="onTouchStart($event, index)"
                      @touchmove="onTouchMove($event, index)"
                      @touchend="onTouchEnd(index)">
                    <view class="bill-content" :style="{ transform: 'translateX(' + (billItemOffset[index] || 0) + 'px)' }">
                        <view class="bill-icon" :class="bill.type === 'INCOME' ? 'icon-income' : 'icon-expense'">
                            {{ bill.category ? bill.category.icon : '💰' }}
                        </view>
                        <view class="bill-info">
                            <view class="bill-title">
                                {{ bill.merchant || (bill.category ? bill.category.name : '消费') }}
                            </view>
                            <view class="bill-time">
                                {{ bill.transactionTime | formatDate('MM-dd HH:mm') }}
                                <text class="sync-status" v-if="bill.syncStatus !== 'SYNCED'"
                                      :class="bill.syncStatus === 'FAILED' ? 'status-failed' : 'status-pending'">
                                    {{ bill.syncStatus | formatSyncStatus }}
                                </text>
                            </view>
                        </view>
                        <view class="bill-amount" :class="bill.type === 'INCOME' ? 'amount-income' : 'amount-expense'">
                            {{ bill.type === 'INCOME' ? '+' : '-' }}{{ bill.amount | formatMoney }}
                        </view>
                    </view>
                    <view class="bill-actions" :style="{ right: '-' + (billItemOffset[index] || 0) + 'px' }">
                        <view class="action-btn btn-edit" @click.stop="editCategory(bill)">修改分类</view>
                        <view class="action-btn btn-delete" @click.stop="deleteBill(bill)">删除</view>
                    </view>
                </view>
                <view class="empty" v-if="recentBills.length === 0">
                    <view class="empty-icon">📭</view>
                    <text>暂无账单记录</text>
                </view>
            </view>
        </view>

        <view class="fab-btn" @click="goToAddBill">
            <text class="fab-icon">+</text>
        </view>

        <view class="category-picker" v-if="showCategoryPicker" @click="showCategoryPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择分类</text>
                    <text class="picker-close" @click="showCategoryPicker = false">×</text>
                </view>
                <scroll-view scroll-y class="category-list">
                    <view class="category-item" v-for="cat in categories" :key="cat.id"
                          @click="confirmCategory(cat)">
                        <text class="category-icon">{{ cat.icon }}</text>
                        <text class="category-name">{{ cat.name }}</text>
                    </view>
                </scroll-view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            dashboard: {
                totalAssets: 0,
                totalLiabilities: 0,
                todayIncome: 0,
                todayExpense: 0,
                todayBalance: 0,
                monthIncome: 0,
                monthExpense: 0,
                monthAverageExpense: 0,
                budgetUsagePercent: 0,
                budgetWarning: false,
                abnormalExpenses: []
            },
            recentBills: [],
            categories: [],
            showCategoryPicker: false,
            currentBill: null,
            billItemOffset: {},
            touchStartX: 0,
            currentTouchIndex: -1
        }
    },
    onLoad() {
        this.loadData()
    },
    onShow() {
        this.loadData()
    },
    onPullDownRefresh() {
        this.loadData().then(() => {
            uni.stopPullDownRefresh()
        })
    },
    methods: {
        async loadData() {
            try {
                await Promise.all([
                    this.loadDashboard(),
                    this.loadRecentBills(),
                    this.loadCategories()
                ])
            } catch (e) {
                console.error('加载数据失败:', e)
                this.loadCachedData()
            }
        },

        async loadDashboard() {
            try {
                const data = await this.$api.getDashboard()
                this.dashboard = data
                if (data.budgetWarning) {
                    uni.vibrateShort()
                }
                if (data.abnormalExpenses && data.abnormalExpenses.length > 0) {
                    uni.vibrateLong()
                }
            } catch (e) {
                console.error('加载看板数据失败:', e)
            }
        },

        async loadRecentBills() {
            try {
                const data = await this.$api.getRecentBills(30)
                this.recentBills = data.slice(0, 10)
                this.$storage.cacheRecentBills(data)
            } catch (e) {
                console.error('加载账单失败:', e)
                this.recentBills = this.$storage.getRecentBillsFromCache().slice(0, 10)
            }
        },

        async loadCategories() {
            try {
                const data = await this.$api.getCategoriesByType('EXPENSE')
                this.categories = data
                this.$storage.saveCategories(data)
            } catch (e) {
                console.error('加载分类失败:', e)
                this.categories = this.$storage.getCategories().filter(c => c.type === 'EXPENSE')
            }
        },

        loadCachedData() {
            this.recentBills = this.$storage.getRecentBillsFromCache().slice(0, 10)
        },

        goToAddBill() {
            uni.navigateTo({ url: '/pages/bill/add-bill' })
        },

        goToScan() {
            uni.chooseImage({
                count: 1,
                sourceType: ['camera'],
                success: async (res) => {
                    try {
                        const result = await this.$api.ocrRecognition(res.tempFilePaths[0])
                        uni.navigateTo({
                            url: '/pages/bill/add-bill?ocrData=' + encodeURIComponent(JSON.stringify(result))
                        })
                    } catch (e) {
                        uni.showToast({ title: '识别失败', icon: 'none' })
                    }
                }
            })
        },

        goToVoice() {
            uni.showModal({
                title: '语音记账',
                editable: true,
                placeholderText: '请输入语音转换后的文本，如：午餐花费35元',
                success: async (res) => {
                    if (res.confirm && res.content) {
                        try {
                            const result = await this.$api.voiceRecognition(res.content)
                            uni.navigateTo({
                                url: '/pages/bill/add-bill?voiceData=' + encodeURIComponent(JSON.stringify(result))
                            })
                        } catch (e) {
                            uni.showToast({ title: '识别失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        goToTemplates() {
            uni.navigateTo({ url: '/pages/template/template-list' })
        },

        goToBillList() {
            uni.switchTab({ url: '/pages/bill/bill-list' })
        },

        goToBillDetail(bill) {
            if (bill.id) {
                uni.navigateTo({ url: '/pages/bill/bill-detail?id=' + bill.id })
            } else {
                uni.showToast({ title: '待同步账单，请先同步', icon: 'none' })
            }
        },

        showBillActions(bill) {
            uni.vibrateShort()
            uni.showActionSheet({
                itemList: ['修改分类', '删除账单', '查看详情'],
                success: (res) => {
                    if (res.tapIndex === 0) {
                        this.editCategory(bill)
                    } else if (res.tapIndex === 1) {
                        this.deleteBill(bill)
                    } else if (res.tapIndex === 2) {
                        this.goToBillDetail(bill)
                    }
                }
            })
        },

        editCategory(bill) {
            this.currentBill = bill
            this.showCategoryPicker = true
        },

        async confirmCategory(category) {
            if (!this.currentBill) return
            try {
                if (this.currentBill.id) {
                    await this.$api.updateBillCategory(this.currentBill.id, category.id)
                    uni.showToast({ title: '分类已更新', icon: 'success' })
                } else {
                    const pendingBills = this.$storage.getPendingBills()
                    const bill = pendingBills.find(b => b.clientId === this.currentBill.clientId)
                    if (bill) {
                        bill.categoryId = category.id
                        bill.category = category
                        this.$storage.set('pendingBills', pendingBills)
                    }
                }
                this.showCategoryPicker = false
                this.loadRecentBills()
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
                            this.loadRecentBills()
                            this.loadDashboard()
                        } catch (e) {
                            uni.showToast({ title: '删除失败', icon: 'none' })
                        }
                    }
                }
            })
        },

        onTouchStart(e, index) {
            this.touchStartX = e.touches[0].clientX
            this.currentTouchIndex = index
        },

        onTouchMove(e, index) {
            if (this.currentTouchIndex !== index) return
            const moveX = e.touches[0].clientX - this.touchStartX
            if (moveX < 0 && moveX > -120) {
                this.$set(this.billItemOffset, index, moveX)
            }
        },

        onTouchEnd(index) {
            const offset = this.billItemOffset[index] || 0
            if (offset < -60) {
                this.$set(this.billItemOffset, index, -100)
            } else {
                this.$set(this.billItemOffset, index, 0)
            }
            this.currentTouchIndex = -1
        }
    }
}
</script>

<style scoped>
.dashboard {
    min-height: 100vh;
    padding-bottom: 120rpx;
}

.header-gradient {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 30rpx 30rpx 60rpx;
    border-radius: 0 0 40rpx 40rpx;
}

.total-card {
    text-align: center;
    color: #fff;
    padding: 30rpx 0;
}

.total-label {
    font-size: 28rpx;
    opacity: 0.9;
}

.total-amount {
    font-size: 72rpx;
    font-weight: bold;
    margin: 20rpx 0;
}

.liability-row {
    font-size: 24rpx;
    opacity: 0.85;
}

.today-card {
    background: rgba(255, 255, 255, 0.15);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-top: 20rpx;
    backdrop-filter: blur(10px);
}

.today-row {
    display: flex;
    justify-content: space-around;
}

.today-item {
    text-align: center;
    flex: 1;
}

.today-label {
    display: block;
    color: rgba(255, 255, 255, 0.85);
    font-size: 24rpx;
    margin-bottom: 10rpx;
}

.today-income {
    color: #52c41a;
    font-size: 36rpx;
    font-weight: bold;
}

.today-expense {
    color: #ff4d4f;
    font-size: 36rpx;
    font-weight: bold;
}

.today-divider {
    width: 1rpx;
    background: rgba(255, 255, 255, 0.3);
}

.balance-row {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid rgba(255, 255, 255, 0.2);
}

.balance-label {
    color: rgba(255, 255, 255, 0.85);
    font-size: 26rpx;
}

.balance-amount {
    font-size: 32rpx;
    font-weight: bold;
}

.balance-positive .balance-amount {
    color: #52c41a;
}

.balance-negative .balance-amount {
    color: #ff4d4f;
}

.budget-warning {
    display: flex;
    align-items: center;
    background: rgba(255, 77, 79, 0.3);
    border-radius: 12rpx;
    padding: 20rpx;
    margin-top: 20rpx;
}

.warning-icon {
    font-size: 40rpx;
    margin-right: 15rpx;
}

.warning-text {
    flex: 1;
}

.warning-title {
    display: block;
    color: #fff;
    font-weight: bold;
    font-size: 28rpx;
}

.warning-desc {
    display: block;
    color: rgba(255, 255, 255, 0.9);
    font-size: 24rpx;
    margin-top: 5rpx;
}

.quick-actions {
    display: flex;
    justify-content: space-around;
    margin-top: -40rpx;
    position: relative;
    z-index: 10;
}

.action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.action-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-bottom: 10rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.bg-primary { background: linear-gradient(135deg, #667eea, #764ba2); }
.bg-green { background: linear-gradient(135deg, #52c41a, #73d13d); }
.bg-orange { background: linear-gradient(135deg, #fa8c16, #ffa940); }
.bg-purple { background: linear-gradient(135deg, #9254de, #b37feb); }

.action-text {
    font-size: 24rpx;
    color: #666;
}

.card {
    background: #fff;
    border-radius: 16rpx;
    margin: 20rpx;
    padding: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.card-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.card-more {
    font-size: 26rpx;
    color: #667eea;
}

.month-row {
    display: flex;
    justify-content: space-around;
}

.month-item {
    text-align: center;
    flex: 1;
}

.month-label {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 10rpx;
}

.month-income {
    color: #52c41a;
    font-size: 32rpx;
    font-weight: bold;
}

.month-expense {
    color: #ff4d4f;
    font-size: 32rpx;
    font-weight: bold;
}

.month-avg {
    color: #666;
    font-size: 32rpx;
    font-weight: bold;
}

.budget-progress {
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #f0f0f0;
}

.progress-label {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #666;
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
    background: linear-gradient(90deg, #52c41a, #73d13d);
    border-radius: 6rpx;
    transition: width 0.3s;
}

.progress-fill.progress-danger {
    background: linear-gradient(90deg, #fa8c16, #ff4d4f);
}

.abnormal-list {
    background: #fff1f0;
    border: 1rpx solid #ffa39e;
}

.abnormal-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #ffccc7;
}

.abnormal-item:last-child {
    border-bottom: none;
}

.abnormal-icon {
    font-size: 36rpx;
    margin-right: 20rpx;
}

.abnormal-content {
    flex: 1;
}

.abnormal-title {
    font-size: 28rpx;
    color: #333;
    font-weight: bold;
}

.abnormal-desc {
    font-size: 24rpx;
    color: #999;
    margin-top: 5rpx;
}

.abnormal-amount {
    color: #ff4d4f;
    font-size: 32rpx;
    font-weight: bold;
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
    padding: 25rpx 0;
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
    width: 100rpx;
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
}

.bill-time {
    font-size: 24rpx;
    color: #999;
    margin-top: 8rpx;
}

.sync-status {
    margin-left: 10rpx;
    padding: 2rpx 8rpx;
    border-radius: 4rpx;
    font-size: 20rpx;
}

.status-pending {
    background: #fffbe6;
    color: #faad14;
}

.status-failed {
    background: #fff1f0;
    color: #ff4d4f;
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

.category-list {
    max-height: 50vh;
    padding: 20rpx;
}

.category-item {
    display: flex;
    align-items: center;
    padding: 25rpx;
    border-radius: 12rpx;
}

.category-item:active {
    background: #f5f7fa;
}

.category-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.category-name {
    font-size: 30rpx;
    color: #333;
}

.empty {
    padding: 80rpx 0;
    text-align: center;
    color: #999;
}

.empty-icon {
    font-size: 100rpx;
    display: block;
    margin-bottom: 20rpx;
}
</style>
