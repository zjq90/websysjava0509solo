<template>
    <view class="template-edit">
        <view class="type-tabs">
            <view class="tab-item" :class="{ active: form.type === 'EXPENSE' }" @click="form.type = 'EXPENSE'">
                <text>支出</text>
            </view>
            <view class="tab-item" :class="{ active: form.type === 'INCOME' }" @click="form.type = 'INCOME'">
                <text>收入</text>
            </view>
        </view>

        <view class="form-section">
            <view class="form-item">
                <text class="label">模板名称</text>
                <view class="value">
                    <input class="input" v-model="form.name" placeholder="如：地铁通勤" />
                </view>
            </view>

            <view class="form-item">
                <text class="label">金额</text>
                <view class="value">
                    <text class="currency">¥</text>
                    <input class="input amount-input" type="digit" v-model="form.amount" placeholder="0.00" />
                </view>
            </view>

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
                    <input class="input" v-model="form.merchant" placeholder="如：星巴克" />
                </view>
            </view>

            <view class="form-item">
                <text class="label">备注</text>
                <view class="value">
                    <input class="input" v-model="form.remark" placeholder="添加备注信息" />
                </view>
            </view>
        </view>

        <view class="bottom-bar">
            <button class="btn-save" @click="saveTemplate">保存模板</button>
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
            form: {
                id: null,
                name: '',
                type: 'EXPENSE',
                amount: '',
                categoryId: null,
                accountId: null,
                merchant: '',
                remark: ''
            },
            categories: [],
            accounts: [],
            selectedCategory: null,
            selectedAccount: null,
            showCategoryPicker: false,
            showAccountPicker: false
        }
    },
    onLoad(options) {
        if (options.id) {
            this.loadTemplate(options.id)
        }
        this.loadData()
    },
    methods: {
        async loadData() {
            try {
                const [categories, accounts] = await Promise.all([
                    this.$api.getCategoriesByType(this.form.type),
                    this.$api.getAccounts()
                ])
                this.categories = categories
                this.accounts = accounts
                if (accounts.length > 0 && !this.selectedAccount) {
                    this.selectedAccount = accounts[0]
                    this.form.accountId = accounts[0].id
                }
            } catch (e) {
                console.error('加载数据失败:', e)
                this.categories = this.$storage.getCategories().filter(c => c.type === this.form.type)
                this.accounts = this.$storage.getAccounts()
            }
        },

        async loadTemplate(id) {
            try {
                const tpl = await this.$api.getTemplateById(id)
                this.form = {
                    id: tpl.id,
                    name: tpl.name,
                    type: tpl.type,
                    amount: tpl.amount.toString(),
                    categoryId: tpl.categoryId,
                    accountId: tpl.accountId,
                    merchant: tpl.merchant || '',
                    remark: tpl.remark || ''
                }
                if (tpl.category) this.selectedCategory = tpl.category
                if (tpl.account) this.selectedAccount = tpl.account
            } catch (e) {
                console.error('加载模板失败:', e)
                uni.showToast({ title: '加载失败', icon: 'none' })
            }
        },

        selectCategory(cat) {
            this.selectedCategory = cat
            this.form.categoryId = cat.id
            this.showCategoryPicker = false
        },

        selectAccount(acc) {
            this.selectedAccount = acc
            this.form.accountId = acc.id
            this.showAccountPicker = false
        },

        async saveTemplate() {
            if (!this.form.name) {
                uni.showToast({ title: '请输入模板名称', icon: 'none' })
                return
            }
            if (!this.form.amount || parseFloat(this.form.amount) <= 0) {
                uni.showToast({ title: '请输入金额', icon: 'none' })
                return
            }
            if (!this.form.categoryId) {
                uni.showToast({ title: '请选择分类', icon: 'none' })
                return
            }
            if (!this.form.accountId) {
                uni.showToast({ title: '请选择账户', icon: 'none' })
                return
            }

            const data = {
                ...this.form,
                amount: parseFloat(this.form.amount)
            }

            try {
                if (this.form.id) {
                    await this.$api.updateTemplate(this.form.id, data)
                } else {
                    await this.$api.createTemplate(data)
                }
                uni.showToast({ title: '保存成功', icon: 'success' })
                setTimeout(() => {
                    uni.navigateBack()
                }, 1000)
            } catch (e) {
                uni.showToast({ title: '保存失败', icon: 'none' })
            }
        }
    },
    watch: {
        'form.type'() {
            this.selectedCategory = null
            this.form.categoryId = null
            this.loadData()
        }
    }
}
</script>

<style scoped>
.template-edit {
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

.amount-input {
    text-align: right;
}

.currency {
    color: #667eea;
    font-weight: bold;
    margin-right: 10rpx;
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
