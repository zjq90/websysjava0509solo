<template>
    <view class="deposit-pay" :class="{ 'elder-mode': elderMode }">
        <view class="amount-card">
            <text class="amount-label">请选择补缴金额</text>
            <view class="amount-display">
                <text class="currency">¥</text>
                <text class="amount-value">{{ formatAmount(currentAmount) }}</text>
            </view>
        </view>

        <view class="quick-amounts">
            <view class="section-title">快捷金额</view>
            <view class="amount-grid">
                <view 
                    class="amount-item"
                    :class="{ active: currentAmount === 500 }"
                    @click="selectAmount(500)"
                >
                    <text class="item-amount">500</text>
                    <text class="item-unit">元</text>
                </view>
                <view 
                    class="amount-item"
                    :class="{ active: currentAmount === 1000 }"
                    @click="selectAmount(1000)"
                >
                    <text class="item-amount">1000</text>
                    <text class="item-unit">元</text>
                </view>
                <view 
                    class="amount-item"
                    :class="{ active: currentAmount === 2000 }"
                    @click="selectAmount(2000)"
                >
                    <text class="item-amount">2000</text>
                    <text class="item-unit">元</text>
                </view>
                <view 
                    class="amount-item"
                    :class="{ active: currentAmount === 5000 }"
                    @click="selectAmount(5000)"
                >
                    <text class="item-amount">5000</text>
                    <text class="item-unit">元</text>
                </view>
            </view>
        </view>

        <view class="custom-amount">
            <view class="section-title">自定义金额</view>
            <view class="input-wrapper">
                <text class="input-prefix">¥</text>
                <input 
                    class="custom-input"
                    type="digit"
                    v-model="customAmount"
                    placeholder="请输入金额"
                    @input="onCustomInput"
                />
                <text class="input-suffix">元</text>
            </view>
            <text class="input-tip">最低充值金额：100元</text>
        </view>

        <view class="payment-methods">
            <view class="section-title">支付方式</view>
            <view class="method-list">
                <view 
                    class="method-item"
                    :class="{ active: payMethod === 'wechat' }"
                    @click="selectMethod('wechat')"
                >
                    <text class="method-icon">💚</text>
                    <text class="method-name">微信支付</text>
                    <view class="method-radio" :class="{ active: payMethod === 'wechat' }"></view>
                </view>
                <view 
                    class="method-item"
                    :class="{ active: payMethod === 'alipay' }"
                    @click="selectMethod('alipay')"
                >
                    <text class="method-icon">💙</text>
                    <text class="method-name">支付宝</text>
                    <view class="method-radio" :class="{ active: payMethod === 'alipay' }"></view>
                </view>
                <view 
                    class="method-item"
                    :class="{ active: payMethod === 'balance' }"
                    @click="selectMethod('balance')"
                >
                    <text class="method-icon">💰</text>
                    <text class="method-name">账户余额</text>
                    <text class="method-balance">可用: ¥0.00</text>
                    <view class="method-radio" :class="{ active: payMethod === 'balance' }"></view>
                </view>
            </view>
        </view>

        <view class="pay-notice">
            <text class="notice-title">温馨提示</text>
            <text class="notice-content">1. 支付成功后，押金会自动入账，可在押金记录中查看。</text>
            <text class="notice-content">2. 如需退款，请联系医院财务处办理。</text>
            <text class="notice-content">3. 如有疑问，请拨打客服热线：400-123-4567。</text>
        </view>

        <view class="bottom-bar">
            <view class="pay-summary">
                <text class="summary-label">支付金额：</text>
                <text class="summary-amount">¥{{ formatAmount(currentAmount) }}</text>
            </view>
            <button 
                class="btn btn-primary btn-large"
                :disabled="!canPay"
                :class="{ disabled: !canPay }"
                @click="payNow"
            >
                立即支付
            </button>
        </view>
    </view>
</template>

<script>
import api from '@/common/js/api.js'
import util from '@/common/js/util.js'

export default {
    data() {
        return {
            elderMode: false,
            admissionId: '',
            currentAmount: 1000,
            customAmount: '',
            payMethod: 'wechat',
            paying: false
        }
    },
    computed: {
        canPay() {
            return this.currentAmount >= 100 && this.payMethod
        }
    },
    onLoad(options) {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        if (options.admissionId) {
            this.admissionId = options.admissionId
        }
    },
    methods: {
        formatAmount(amount) {
            return util.formatAmount(amount)
        },

        selectAmount(amount) {
            this.currentAmount = amount
            this.customAmount = ''
        },

        onCustomInput(e) {
            let val = e.detail.value
            val = val.replace(/[^\d.]/g, '')
            
            const parts = val.split('.')
            if (parts.length > 2) {
                val = parts[0] + '.' + parts[1]
            }
            if (parts[1] && parts[1].length > 2) {
                val = parts[0] + '.' + parts[1].substring(0, 2)
            }
            
            this.customAmount = val
            
            const numVal = parseFloat(val) || 0
            if (numVal > 0) {
                this.currentAmount = numVal
            }
        },

        selectMethod(method) {
            this.payMethod = method
        },

        async payNow() {
            if (this.currentAmount < 100) {
                util.showToast('最低充值金额100元', 'none')
                return
            }
            if (!this.payMethod) {
                util.showToast('请选择支付方式', 'none')
                return
            }

            const confirmed = await util.confirm(`确认支付 ¥${this.formatAmount(this.currentAmount)} 吗？`)
            if (!confirmed) return

            this.paying = true
            try {
                util.showLoading('支付中...')
                
                const createRes = await api.deposit.create({
                    admissionId: this.admissionId || null,
                    amount: this.currentAmount,
                    payMethod: this.payMethod
                })
                
                if (createRes.code !== 200) {
                    throw new Error(createRes.message || '创建订单失败')
                }
                
                const orderNo = createRes.data.orderNo
                
                await new Promise(resolve => setTimeout(resolve, 1500))
                
                const payRes = await api.deposit.pay(orderNo)
                
                util.hideLoading()
                
                if (payRes.code === 200) {
                    util.showToast('支付成功', 'success')
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                } else {
                    util.showToast(payRes.message || '支付失败', 'none')
                }
            } catch (e) {
                util.hideLoading()
                console.error('支付失败:', e)
                util.showToast('支付失败，请重试', 'none')
            } finally {
                this.paying = false
            }
        }
    }
}
</script>

<style scoped>
.deposit-pay {
    min-height: 100vh;
    background: #F5F5F5;
    padding: 20rpx;
    padding-bottom: 200rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.amount-card {
    background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
    border-radius: 24rpx;
    padding: 48rpx;
    text-align: center;
    color: #fff;
    margin-bottom: 20rpx;
}

.amount-label {
    font-size: 28rpx;
    opacity: 0.9;
    margin-bottom: 16rpx;
    display: block;
}

.elder-mode .amount-label {
    font-size: 32rpx;
}

.amount-display {
    display: flex;
    align-items: baseline;
    justify-content: center;
}

.currency {
    font-size: 40rpx;
    font-weight: 600;
}

.elder-mode .currency {
    font-size: 48rpx;
}

.amount-value {
    font-size: 80rpx;
    font-weight: 700;
    margin-left: 8rpx;
}

.elder-mode .amount-value {
    font-size: 96rpx;
}

.section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 20rpx;
}

.elder-mode .section-title {
    font-size: 34rpx;
}

.quick-amounts,
.custom-amount,
.payment-methods,
.pay-notice {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    margin-bottom: 20rpx;
}

.amount-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
}

.amount-item {
    background: #F5F5F5;
    border-radius: 16rpx;
    padding: 24rpx 16rpx;
    text-align: center;
    border: 2rpx solid transparent;
    transition: all 0.3s;
}

.amount-item.active {
    background: #E6F7FF;
    border-color: #1890FF;
}

.item-amount {
    font-size: 40rpx;
    font-weight: 700;
    color: #333;
    display: block;
}

.elder-mode .item-amount {
    font-size: 44rpx;
}

.item-unit {
    font-size: 24rpx;
    color: #999;
    margin-top: 4rpx;
    display: block;
}

.elder-mode .item-unit {
    font-size: 28rpx;
}

.amount-item.active .item-amount {
    color: #1890FF;
}

.input-wrapper {
    display: flex;
    align-items: center;
    background: #F5F5F5;
    border-radius: 16rpx;
    padding: 0 24rpx;
    height: 100rpx;
}

.input-prefix,
.input-suffix {
    font-size: 32rpx;
    color: #333;
    font-weight: 600;
}

.elder-mode .input-prefix,
.elder-mode .input-suffix {
    font-size: 36rpx;
}

.custom-input {
    flex: 1;
    height: 100rpx;
    font-size: 36rpx;
    color: #333;
    text-align: center;
}

.elder-mode .custom-input {
    font-size: 40rpx;
}

.input-tip {
    font-size: 24rpx;
    color: #999;
    margin-top: 12rpx;
    display: block;
}

.elder-mode .input-tip {
    font-size: 28rpx;
}

.method-list {
    
}

.method-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.method-item:last-child {
    border-bottom: none;
}

.method-icon {
    font-size: 48rpx;
    margin-right: 20rpx;
}

.elder-mode .method-icon {
    font-size: 56rpx;
}

.method-name {
    flex: 1;
    font-size: 30rpx;
    color: #333;
}

.elder-mode .method-name {
    font-size: 34rpx;
}

.method-balance {
    font-size: 26rpx;
    color: #999;
    margin-right: 20rpx;
}

.elder-mode .method-balance {
    font-size: 30rpx;
}

.method-radio {
    width: 40rpx;
    height: 40rpx;
    border-radius: 50%;
    border: 2rpx solid #D9D9D9;
    position: relative;
}

.method-radio.active {
    border-color: #1890FF;
}

.method-radio.active::after {
    content: '';
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 20rpx;
    height: 20rpx;
    background: #1890FF;
    border-radius: 50%;
}

.notice-title {
    font-size: 28rpx;
    color: #333;
    font-weight: 600;
    margin-bottom: 16rpx;
    display: block;
}

.elder-mode .notice-title {
    font-size: 32rpx;
}

.notice-content {
    font-size: 26rpx;
    color: #666;
    line-height: 1.8;
    display: block;
    margin-bottom: 8rpx;
}

.elder-mode .notice-content {
    font-size: 30rpx;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.elder-mode .bottom-bar {
    padding: 28rpx;
}

.pay-summary {
    display: flex;
    align-items: baseline;
}

.summary-label {
    font-size: 28rpx;
    color: #666;
}

.elder-mode .summary-label {
    font-size: 32rpx;
}

.summary-amount {
    font-size: 44rpx;
    font-weight: 700;
    color: #FF4D4F;
}

.elder-mode .summary-amount {
    font-size: 52rpx;
}

.btn.disabled {
    opacity: 0.5;
}
</style>
