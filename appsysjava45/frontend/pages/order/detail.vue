<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="status-section" :style="{background: getStatusBgColor()}">
            <text class="status-icon">{{ getStatusIcon() }}</text>
            <view class="status-info">
                <text class="status-name">{{ getStatusName() }}</text>
                <text class="status-desc">{{ getStatusDesc() }}</text>
            </view>
        </view>
        
        <view class="address-section">
            <view class="address-icon">📍</view>
            <view class="address-info">
                <view class="address-name">
                    <text class="name">{{ order.receiverName }}</text>
                    <text class="phone">{{ order.receiverPhone }}</text>
                </view>
                <text class="address-detail">{{ order.receiverAddress }}</text>
            </view>
        </view>
        
        <view class="goods-section">
            <view class="section-header">
                <text class="section-title">商品信息</text>
                <text class="order-no">订单号：{{ order.orderNo }}</text>
            </view>
            <view class="goods-list">
                <view class="goods-item" v-for="item in orderItems" :key="item.id">
                    <view class="goods-img" :style="{background: getRandomColor()}">
                        <text class="goods-emoji">💐</text>
                    </view>
                    <view class="goods-info">
                        <text class="goods-name">{{ item.productName }}</text>
                        <view class="goods-price-row">
                            <text class="goods-price text-primary">¥{{ item.price }}</text>
                            <text class="goods-quantity">x{{ item.quantity }}</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="amount-section">
            <view class="amount-row">
                <text class="amount-label">商品金额</text>
                <text class="amount-value">¥{{ order.totalAmount }}</text>
            </view>
            <view class="amount-row" v-if="order.discountAmount > 0">
                <text class="amount-label">优惠金额</text>
                <text class="amount-value discount">-¥{{ order.discountAmount }}</text>
            </view>
            <view class="amount-row">
                <text class="amount-label">配送费</text>
                <text class="amount-value">¥{{ order.freightAmount || '0.00' }}</text>
            </view>
            <view class="amount-row total">
                <text class="amount-label">实付金额</text>
                <text class="amount-value text-primary font-bold">¥{{ order.payAmount }}</text>
            </view>
        </view>
        
        <view class="info-section" v-if="order.remark">
            <view class="info-row">
                <text class="info-label">订单备注</text>
                <text class="info-value">{{ order.remark }}</text>
            </view>
        </view>
        
        <view class="info-section">
            <view class="info-row">
                <text class="info-label">下单时间</text>
                <text class="info-value">{{ order.createTime }}</text>
            </view>
            <view class="info-row" v-if="order.payTime">
                <text class="info-label">支付时间</text>
                <text class="info-value">{{ order.payTime }}</text>
            </view>
        </view>
        
        <view class="bottom-bar" v-if="showPayBtn || showCancelBtn || showCompleteBtn">
            <button class="cancel-btn" v-if="showCancelBtn" @click="cancelOrder">取消订单</button>
            <button class="pay-btn" v-if="showPayBtn" @click="payOrder">立即支付</button>
            <button class="complete-btn" v-if="showCompleteBtn" @click="completeOrder">确认收货</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            orderId: null,
            order: {},
            orderItems: [],
            elderMode: false
        }
    },
    onLoad(options) {
        this.elderMode = getApp().globalData.elderMode
        this.orderId = options.id
        this.loadOrderDetail()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    computed: {
        showPayBtn() {
            return this.order.status === 'pending_payment'
        },
        showCancelBtn() {
            return this.order.status === 'pending_payment' || this.order.status === 'producing'
        },
        showCompleteBtn() {
            return this.order.status === 'delivering'
        }
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        getStatusBgColor() {
            const colors = {
                'pending_payment': 'linear-gradient(135deg, #667eea, #764ba2)',
                'producing': 'linear-gradient(135deg, #f093fb, #f5576c)',
                'delivering': 'linear-gradient(135deg, #4facfe, #00f2fe)',
                'completed': 'linear-gradient(135deg, #43e97b, #38f9d7)',
                'cancelled': 'linear-gradient(135deg, #a8a8a8, #c4c4c4)'
            }
            return colors[this.order.status] || colors['pending_payment']
        },
        
        getStatusIcon() {
            const icons = {
                'pending_payment': '💳',
                'producing': '🎨',
                'delivering': '🚚',
                'completed': '✅',
                'cancelled': '❌'
            }
            return icons[this.order.status] || '📦'
        },
        
        getStatusName() {
            const names = {
                'pending_payment': '待支付',
                'producing': '制作中',
                'delivering': '配送中',
                'completed': '已完成',
                'cancelled': '已取消'
            }
            return names[this.order.status] || '未知'
        },
        
        getStatusDesc() {
            const descs = {
                'pending_payment': '请尽快完成支付',
                'producing': '花艺师正在精心制作',
                'delivering': '配送员正在快马加鞭',
                'completed': '感谢您的购买',
                'cancelled': '订单已取消'
            }
            return descs[this.order.status] || ''
        },
        
        loadOrderDetail() {
            this.$request.get(`/order/${this.orderId}`).then(res => {
                this.order = res.order
                this.orderItems = res.items
            }).catch(err => {
                console.error(err)
            })
        },
        
        payOrder() {
            uni.showModal({
                title: '支付',
                content: '确认支付吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/pay/${this.orderId}`, { payMethod: 'wechat' }).then(() => {
                            uni.showToast({
                                title: '支付成功',
                                icon: 'success'
                            })
                            this.loadOrderDetail()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        cancelOrder() {
            uni.showModal({
                title: '取消订单',
                content: '确认取消该订单吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/cancel/${this.orderId}`, { reason: '用户主动取消' }).then(() => {
                            uni.showToast({
                                title: '订单已取消',
                                icon: 'success'
                            })
                            this.loadOrderDetail()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        completeOrder() {
            uni.showModal({
                title: '确认收货',
                content: '确认已收到商品吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/complete/${this.orderId}`).then(() => {
                            uni.showToast({
                                title: '订单已完成',
                                icon: 'success'
                            })
                            this.loadOrderDetail()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    padding-bottom: 140rpx;
    background: #f5f5f5;
}

.status-section {
    display: flex;
    align-items: center;
    padding: 50rpx 30rpx;
    color: #fff;
    gap: 30rpx;
}

.status-icon {
    font-size: 80rpx;
}

.status-info {
    flex: 1;
}

.status-name {
    font-size: 36rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 10rpx;
}

.status-desc {
    font-size: 26rpx;
    opacity: 0.9;
}

.address-section {
    display: flex;
    gap: 20rpx;
    padding: 30rpx;
    background: #fff;
    margin-top: 20rpx;
}

.address-icon {
    font-size: 40rpx;
    margin-top: 5rpx;
}

.address-info {
    flex: 1;
}

.address-name {
    display: flex;
    align-items: center;
    gap: 20rpx;
    margin-bottom: 10rpx;
}

.name {
    font-size: 30rpx;
    font-weight: 500;
}

.phone {
    font-size: 28rpx;
    color: #666;
}

.address-detail {
    font-size: 26rpx;
    color: #666;
    line-height: 1.6;
}

.goods-section {
    background: #fff;
    margin-top: 20rpx;
    padding: 30rpx;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.section-title {
    font-size: 30rpx;
    font-weight: 500;
}

.order-no {
    font-size: 24rpx;
    color: #999;
}

.goods-list {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.goods-item {
    display: flex;
    gap: 20rpx;
    padding: 20rpx;
    background: #f9f9f9;
    border-radius: 12rpx;
}

.goods-img {
    width: 120rpx;
    height: 120rpx;
    border-radius: 10rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.goods-emoji {
    font-size: 60rpx;
}

.goods-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.goods-name {
    font-size: 28rpx;
    font-weight: 500;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.goods-price-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.goods-price {
    font-size: 28rpx;
}

.goods-quantity {
    font-size: 26rpx;
    color: #999;
}

.amount-section {
    background: #fff;
    margin-top: 20rpx;
    padding: 30rpx;
}

.amount-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.amount-row:last-child {
    margin-bottom: 0;
}

.amount-row.total {
    padding-top: 20rpx;
    border-top: 2rpx solid #f0f0f0;
    margin-top: 10rpx;
}

.amount-label {
    font-size: 26rpx;
    color: #666;
}

.amount-value {
    font-size: 26rpx;
}

.amount-value.discount {
    color: #FF6B6B;
}

.info-section {
    background: #fff;
    margin-top: 20rpx;
    padding: 30rpx;
}

.info-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20rpx;
}

.info-row:last-child {
    margin-bottom: 0;
}

.info-label {
    font-size: 26rpx;
    color: #666;
}

.info-value {
    font-size: 26rpx;
    color: #333;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 120rpx;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 30rpx;
    gap: 20rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.cancel-btn {
    width: 180rpx;
    height: 70rpx;
    line-height: 70rpx;
    background: #f5f5f5;
    color: #666;
    border-radius: 35rpx;
    font-size: 26rpx;
    border: none;
    padding: 0;
}

.pay-btn, .complete-btn {
    width: 220rpx;
    height: 70rpx;
    line-height: 70rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 35rpx;
    font-size: 26rpx;
    border: none;
    padding: 0;
}
</style>