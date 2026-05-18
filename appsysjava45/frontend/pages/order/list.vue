<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="tab-bar">
            <view class="tab-item" 
                  :class="{ active: currentStatus === '' }"
                  @click="currentStatus = ''">
                全部
            </view>
            <view class="tab-item"
                  :class="{ active: currentStatus === 'pending_payment' }"
                  @click="currentStatus = 'pending_payment'">
                待支付
            </view>
            <view class="tab-item"
                  :class="{ active: currentStatus === 'producing' }"
                  @click="currentStatus = 'producing'">
                制作中
            </view>
            <view class="tab-item"
                  :class="{ active: currentStatus === 'delivering' }"
                  @click="currentStatus = 'delivering'">
                配送中
            </view>
            <view class="tab-item"
                  :class="{ active: currentStatus === 'completed' }"
                  @click="currentStatus = 'completed'">
                已完成
            </view>
        </view>
        
        <view class="order-list">
            <view class="order-card" v-for="order in orderList" :key="order.id" @click="goToDetail(order.id)">
                <view class="order-header">
                    <text class="order-no">订单号：{{ order.orderNo }}</text>
                    <text class="order-status" :class="order.status">{{ getStatusName(order.status) }}</text>
                </view>
                <view class="order-content">
                    <view class="goods-icon">💐</view>
                    <view class="goods-summary">
                        <text class="goods-count">共{{ getOrderItemCount(order.id) }}件商品</text>
                        <text class="order-amount">实付：<text class="text-primary font-bold">¥{{ order.payAmount }}</text></text>
                    </view>
                </view>
                <view class="order-footer">
                    <text class="order-time">{{ order.createTime }}</text>
                    <view class="order-actions">
                        <button class="action-btn secondary" v-if="order.status === 'pending_payment'" @click.stop="cancelOrder(order.id)">取消订单</button>
                        <button class="action-btn primary" v-if="order.status === 'pending_payment'" @click.stop="payOrder(order.id)">立即支付</button>
                        <button class="action-btn primary" v-if="order.status === 'delivering'" @click.stop="completeOrder(order.id)">确认收货</button>
                        <button class="action-btn secondary" v-if="order.status === 'completed'">再次购买</button>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="orderList.length === 0 && !loading">
            <text class="empty-icon">📦</text>
            <text class="empty-text">暂无订单</text>
            <button class="go-shopping-btn" @click="goToHome">去逛逛</button>
        </view>
        
        <view class="loading" v-if="loading">
            <text>加载中...</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            currentStatus: '',
            orderList: [],
            orderItemsMap: {},
            loading: false,
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
        this.loadOrders()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
        this.loadOrders()
    },
    methods: {
        getStatusName(status) {
            const names = {
                'pending_payment': '待支付',
                'producing': '制作中',
                'delivering': '配送中',
                'completed': '已完成',
                'cancelled': '已取消'
            }
            return names[status] || '未知'
        },
        
        getOrderItemCount(orderId) {
            return this.orderItemsMap[orderId] || 0
        },
        
        loadOrders() {
            this.loading = true
            this.$request.get('/order/list', { status: this.currentStatus, page: 0, size: 20 }).then(res => {
                this.orderList = res.content || []
                this.loadOrderItems()
                this.loading = false
            }).catch(err => {
                this.loading = false
                console.error(err)
            })
        },
        
        loadOrderItems() {
            this.orderItemsMap = {}
            this.orderList.forEach(order => {
                this.$request.get(`/order/${order.id}`).then(res => {
                    this.orderItemsMap[order.id] = res.items ? res.items.length : 0
                }).catch(err => {
                    console.error(err)
                })
            })
        },
        
        goToDetail(orderId) {
            uni.navigateTo({
                url: `/pages/order/detail?id=${orderId}`
            })
        },
        
        payOrder(orderId) {
            uni.showModal({
                title: '支付',
                content: '确认支付吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/pay/${orderId}`, { payMethod: 'wechat' }).then(() => {
                            uni.showToast({
                                title: '支付成功',
                                icon: 'success'
                            })
                            this.loadOrders()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        cancelOrder(orderId) {
            uni.showModal({
                title: '取消订单',
                content: '确认取消该订单吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/cancel/${orderId}`, { reason: '用户主动取消' }).then(() => {
                            uni.showToast({
                                title: '订单已取消',
                                icon: 'success'
                            })
                            this.loadOrders()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        completeOrder(orderId) {
            uni.showModal({
                title: '确认收货',
                content: '确认已收到商品吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.post(`/order/complete/${orderId}`).then(() => {
                            uni.showToast({
                                title: '订单已完成',
                                icon: 'success'
                            })
                            this.loadOrders()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        goToHome() {
            uni.switchTab({
                url: '/pages/index/index'
            })
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background: #f5f5f5;
}

.tab-bar {
    display: flex;
    background: #fff;
    border-bottom: 2rpx solid #f0f0f0;
    position: sticky;
    top: 0;
    z-index: 100;
}

.tab-item {
    flex: 1;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    font-size: 26rpx;
    color: #666;
}

.tab-item.active {
    color: #FF6B6B;
    font-weight: 500;
    border-bottom: 4rpx solid #FF6B6B;
}

.order-list {
    padding: 20rpx;
}

.order-card {
    background: #fff;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
}

.order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: 20rpx;
    border-bottom: 2rpx solid #f5f5f5;
}

.order-no {
    font-size: 24rpx;
    color: #999;
}

.order-status {
    font-size: 26rpx;
    font-weight: 500;
}

.order-status.pending_payment {
    color: #f39c12;
}

.order-status.producing {
    color: #9b59b6;
}

.order-status.delivering {
    color: #3498db;
}

.order-status.completed {
    color: #27ae60;
}

.order-status.cancelled {
    color: #999;
}

.order-content {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    gap: 20rpx;
}

.goods-icon {
    font-size: 60rpx;
}

.goods-summary {
    flex: 1;
}

.goods-count {
    font-size: 26rpx;
    color: #666;
    display: block;
    margin-bottom: 10rpx;
}

.order-amount {
    font-size: 26rpx;
    color: #666;
}

.order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 20rpx;
    border-top: 2rpx solid #f5f5f5;
}

.order-time {
    font-size: 24rpx;
    color: #999;
}

.order-actions {
    display: flex;
    gap: 15rpx;
}

.action-btn {
    height: 56rpx;
    line-height: 56rpx;
    padding: 0 24rpx;
    border-radius: 28rpx;
    font-size: 24rpx;
    border: none;
}

.action-btn.secondary {
    background: #f5f5f5;
    color: #666;
}

.action-btn.primary {
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
}

.empty-state {
    padding: 200rpx 0;
    text-align: center;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999;
    display: block;
    margin-bottom: 30rpx;
}

.go-shopping-btn {
    width: 240rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 40rpx;
    font-size: 28rpx;
    border: none;
    padding: 0;
}

.loading {
    padding: 50rpx;
    text-align: center;
    color: #999;
    font-size: 26rpx;
}
</style>