<template>
    <view class="add-bill">
        <view class="type-tabs">
            <view class="tab-item" :class="{ active: billType === 'EXPENSE' }" @click="billType = 'EXPENSE'">
                <text>支出</text>
            </view>
            <view class="tab-item" :class="{ active: billType === 'INCOME' }" @click="billType = 'INCOME'">
                <text>收入</text>
            </view>
        </view>

        <view class="amount-section">
            <text class="currency-symbol">¥</text>
            <input class="amount-input" type="digit" v-model="amount" placeholder="0.00" focus />
        </view>

        <view class="form-section">
            <view class="form-item" @click="showCategoryPicker = true">
                <text class="label">分类</text>
                <view class="value">
                    <text v-if="selectedCategory" class="category-display">
                        <text class="cat-icon">{{ selectedCategory.icon }}</text>
                        <text class="cat-name">{{ selectedCategory.name }}</text>
                    </text>
                    <text v-else class="placeholder">请选择分类</text>
                    <text class="arrow">›</text>
                </view>
            </view>

            <view class="form-item" @click="showAccountPicker = true">
                <text class="label">账户</text>
                <view class="value">
                    <text v-if="selectedAccount" class="account-display">
                        <text class="acc-icon">{{ selectedAccount.icon }}</text>
                        <text class="acc-name">{{ selectedAccount.name }}</text>
                    </text>
                    <text v-else class="placeholder">请选择账户</text>
                    <text class="arrow">›</text>
                </view>
            </view>

            <view class="form-item">
                <text class="label">商家</text>
                <view class="value">
                    <input class="input" v-model="merchant" placeholder="如：星巴克、麦当劳" />
                </view>
            </view>

            <view class="form-item" @click="showDatePicker = true">
                <text class="label">时间</text>
                <view class="value">
                    <text>{{ formattedDate }}</text>
                    <text class="arrow">›</text>
                </view>
            </view>

            <picker mode="date" v-if="showDatePicker" :value="dateValue" @change="onDateChange" @cancel="showDatePicker = false" :end="today">
                <view></view>
            </picker>
            <picker mode="time" v-if="showDatePicker" :value="timeValue" @change="onTimeChange">
                <view></view>
            </picker>

            <view class="form-item">
                <text class="label">备注</text>
                <view class="value">
                    <input class="input" v-model="remark" placeholder="添加备注信息" />
                </view>
            </view>
        </view>

        <view class="quick-templates" v-if="templates.length > 0">
            <view class="section-title">快捷模板</view>
            <scroll-view scroll-x class="template-scroll">
                <view class="template-item" v-for="tpl in templates" :key="tpl.id"
                      @click="useTemplate(tpl)">
                    <text class="tpl-icon">{{ tpl.category ? tpl.category.icon : '💰' }}</text>
                    <text class="tpl-name">{{ tpl.name }}</text>
                    <text class="tpl-amount">{{ tpl.type === 'INCOME' ? '+' : '-' }}{{ tpl.amount | formatMoney }}</text>
                </view>
            </scroll-view>
        </view>

        <view class="bottom-bar">
            <button class="btn-save" @click="saveBill">保存账单</button>
        </view>

        <view class="picker-mask" v-if="showCategoryPicker" @click="showCategoryPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择分类</text>
                    <text class="picker-close" @click="showCategoryPicker = false">×</text>
                </view>
                <scroll-view scroll-y class="picker-body">
                    <view class="category-grid">
                        <view class="cat-item" v-for="cat in categories" :key="cat.id"
                              :class="{ selected: selectedCategory && selectedCategory.id === cat.id }"
                              @click="selectCategory(cat)">
                            <text class="cat-icon">{{ cat.icon }}</text>
                            <text class="cat-name">{{ cat.name }}</text>
                        </view>
                    </view>
                </scroll-view>
            </view>
        </view>

        <view class="picker-mask" v-if="showAccountPicker" @click="showAccountPicker = false">
            <view class="picker-content" @click.stop>
                <view class="picker-header">
                    <text>选择账户</text>
                    <text class="picker-close" @click="showAccountPicker = false">×</text>
                </view>
                <scroll-view scroll-y class="picker-body">
                    <view class="account-list">
                        <view class="acc-item" v-for="acc in accounts" :key="acc.id"
                              :class="{ selected: selectedAccount && selectedAccount.id === acc.id }"
                              @click="selectAccount(acc)">
                            <text class="acc-icon">{{ acc.icon }}</text>
                            <view class="acc-info">
                                <text class="acc-name">{{ acc.name }}</text>
                                <text class="acc-balance">¥{{ acc.balance | formatMoney }}</text>
                            </view>
                            <text class="check" v-if="selectedAccount && selectedAccount.id === acc.id">✓</text>
                        </view>
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
            billType: 'EXPENSE',
            amount: '',
            merchant: '',
            remark: '',
            transactionTime: new Date(),
            selectedCategory: null,
            selectedAccount: null,
            categories: [],
            accounts: [],
            templates: [],
            showCategoryPicker: false,
            showAccountPicker: false,
            showDatePicker: false,
            dateValue: '',
            timeValue: '',
            today: ''
        }
    },
    computed: {
        formattedDate() {
            const d = new Date(this.transactionTime)
            const y = d.getFullYear()
            const m = String(d.getMonth() + 1).padStart(2, '0')
            const day = String(d.getDate()).padStart(2, '0')
            const h = String(d.getHours()).padStart(2, '0')
            const min = String(d.getMinutes()).padStart(2, '0')
            return `${y}-${m}-${day} ${h}:${min}`
        }
    },
    onLoad(options) {
        const now = new Date()
        this.today = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`
        this.dateValue = this.today
        this.timeValue = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`

        if (options.voiceData) {
            try {
                const data = JSON.parse(decodeURIComponent(options.voiceData))
                this.fillFromRecognition(data)
            } catch (e) {
                console.error('解析语音数据失败:', e)
            }
        }
        if (options.ocrData) {
            try {
                const data = JSON.parse(decodeURIComponent(options.ocrData))
                this.fillFromRecognition(data)
            } catch (e) {
                console.error('解析OCR数据失败:', e)
            }
        }

        this.loadData()
    },
    methods: {
        async loadData() {
            try {
                const [categories, accounts, templates] = await Promise.all([
                    this.$api.getCategoriesByType(this.billType),
                    this.$api.getAccounts(),
                    this.$api.getTemplates()
                ])
                this.categories = categories
                this.accounts = accounts
                this.templates = templates

                if (accounts.length > 0 && !this.selectedAccount) {
                    this.selectedAccount = accounts[0]
                }
            } catch (e) {
                console.error('加载数据失败:', e)
                this.categories = this.$storage.getCategories().filter(c => c.type === this.billType)
                this.accounts = this.$storage.getAccounts()
                this.templates = this.$storage.getTemplates()
            }
        },

        fillFromRecognition(data) {
            if (data.amount) this.amount = data.amount.toString()
            if (data.merchant) this.merchant = data.merchant
            if (data.transactionTime) this.transactionTime = new Date(data.transactionTime)
        },

        selectCategory(cat) {
            this.selectedCategory = cat
            this.showCategoryPicker = false
        },

        selectAccount(acc) {
            this.selectedAccount = acc
            this.showAccountPicker = false
        },

        onDateChange(e) {
            const parts = e.detail.value.split('-')
            const d = new Date(this.transactionTime)
            d.setFullYear(parseInt(parts[0]))
            d.setMonth(parseInt(parts[1]) - 1)
            d.setDate(parseInt(parts[2]))
            this.transactionTime = d
            this.dateValue = e.detail.value
            this.showDatePicker = false
        },

        onTimeChange(e) {
            const parts = e.detail.value.split(':')
            const d = new Date(this.transactionTime)
            d.setHours(parseInt(parts[0]))
            d.setMinutes(parseInt(parts[1]))
            this.transactionTime = d
            this.timeValue = e.detail.value
        },

        useTemplate(tpl) {
            this.$api.useTemplate(tpl.id)
            this.billType = tpl.type
            this.amount = tpl.amount.toString()
            this.selectedCategory = tpl.category
            this.selectedAccount = tpl.account
            this.merchant = tpl.merchant || ''
            this.remark = tpl.remark || ''
            this.loadData()
            uni.showToast({ title: '模板已应用', icon: 'success' })
        },

        async saveBill() {
            if (!this.amount || parseFloat(this.amount) <= 0) {
                uni.showToast({ title: '请输入金额', icon: 'none' })
                return
            }
            if (!this.selectedCategory) {
                uni.showToast({ title: '请选择分类', icon: 'none' })
                return
            }
            if (!this.selectedAccount) {
                uni.showToast({ title: '请选择账户', icon: 'none' })
                return
            }

            const billData = {
                type: this.billType,
                amount: parseFloat(this.amount),
                categoryId: this.selectedCategory.id,
                accountId: this.selectedAccount.id,
                transactionTime: this.transactionTime.toISOString(),
                merchant: this.merchant,
                remark: this.remark,
                deviceId: this.$storage.getDeviceId(),
                clientId: this.$storage.generateClientId()
            }

            uni.showLoading({ title: '保存中...' })

            try {
                await this.$api.createBill(billData)
                uni.hideLoading()
                uni.showToast({ title: '保存成功', icon: 'success' })
                setTimeout(() => {
                    uni.navigateBack()
                }, 1000)
            } catch (e) {
                uni.hideLoading()
                console.error('保存失败，保存到本地:', e)
                this.$storage.savePendingBill(billData)
                uni.showModal({
                    title: '网络异常',
                    content: '账单已保存到本地，网络恢复后将自动同步',
                    showCancel: false,
                    success: () => {
                        uni.navigateBack()
                    }
                })
            }
        }
    },
    watch: {
        billType() {
            this.selectedCategory = null
            this.loadData()
        }
    }
}
</script>

<style scoped>
.add-bill {
    min-height: 100vh;
    background: #f5f7fa;
    padding-bottom: 140rpx;
}

.type-tabs {
    display: flex;
    background: #fff;
    padding: 20rpx 40rpx;
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx;
    font-size: 32rpx;
    color: #999;
    border-bottom: 4rpx solid transparent;
}

.tab-item.active {
    color: #667eea;
    border-bottom-color: #667eea;
    font-weight: bold;
}

.amount-section {
    background: #fff;
    padding: 40rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-bottom: 1rpx solid #f0f0f0;
}

.currency-symbol {
    font-size: 48rpx;
    color: #667eea;
    font-weight: bold;
    margin-right: 10rpx;
}

.amount-input {
    font-size: 72rpx;
    font-weight: bold;
    color: #333;
    width: 400rpx;
    text-align: center;
}

.form-section {
    background: #fff;
    margin-top: 20rpx;
}

.form-item {
    display: flex;
    align-items: center;
    padding: 30rpx 40rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.form-item:last-child {
    border-bottom: none;
}

.label {
    width: 120rpx;
    font-size: 30rpx;
    color: #666;
}

.value {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 30rpx;
    color: #333;
}

.input {
    flex: 1;
    text-align: right;
    font-size: 30rpx;
}

.placeholder {
    color: #ccc;
}

.arrow {
    color: #ccc;
    font-size: 36rpx;
    margin-left: 10rpx;
}

.category-display, .account-display {
    display: flex;
    align-items: center;
}

.cat-icon, .acc-icon {
    font-size: 36rpx;
    margin-right: 10rpx;
}

.quick-templates {
    margin-top: 20rpx;
    background: #fff;
    padding: 30rpx 0;
}

.section-title {
    padding: 0 40rpx 20rpx;
    font-size: 28rpx;
    color: #999;
}

.template-scroll {
    white-space: nowrap;
    padding: 0 30rpx;
}

.template-item {
    display: inline-flex;
    flex-direction: column;
    align-items: center;
    width: 160rpx;
    padding: 20rpx;
    margin: 0 10rpx;
    background: #f8f9fa;
    border-radius: 12rpx;
}

.template-item:active {
    background: #e9ecef;
}

.tpl-icon {
    font-size: 40rpx;
    margin-bottom: 10rpx;
}

.tpl-name {
    font-size: 24rpx;
    color: #333;
    margin-bottom: 5rpx;
}

.tpl-amount {
    font-size: 24rpx;
    color: #ff4d4f;
    font-weight: bold;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    padding: 20rpx 40rpx;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-save {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #667eea, #764ba2);
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    border: none;
}

.picker-mask {
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

.category-grid {
    display: flex;
    flex-wrap: wrap;
}

.cat-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;
    border-radius: 12rpx;
}

.cat-item.selected {
    background: #f0f5ff;
}

.cat-item .cat-icon {
    font-size: 48rpx;
    margin-bottom: 10rpx;
}

.cat-item .cat-name {
    font-size: 26rpx;
    color: #333;
}

.account-list {
    padding: 10rpx 0;
}

.acc-item {
    display: flex;
    align-items: center;
    padding: 30rpx 20rpx;
    border-radius: 12rpx;
}

.acc-item.selected {
    background: #f0f5ff;
}

.acc-item .acc-icon {
    font-size: 48rpx;
    margin-right: 20rpx;
}

.acc-info {
    flex: 1;
}

.acc-item .acc-name {
    display: block;
    font-size: 30rpx;
    color: #333;
}

.acc-balance {
    display: block;
    font-size: 26rpx;
    color: #999;
    margin-top: 5rpx;
}

.check {
    color: #667eea;
    font-size: 36rpx;
    font-weight: bold;
}
</style>
