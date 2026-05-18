<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="address-section">
            <view class="section-title">配送信息</view>
            <view class="address-form">
                <view class="form-item">
                    <text class="form-label">收货人</text>
                    <input class="form-input" v-model="receiverName" placeholder="请输入收货人姓名" />
                </view>
                <view class="form-item">
                    <text class="form-label">手机号</text>
                    <input class="form-input" v-model="receiverPhone" placeholder="请输入手机号" type="number" />
                </view>
                <view class="form-item">
                    <text class="form-label">收货地址</text>
                    <textarea class="form-textarea" v-model="receiverAddress" placeholder="请输入详细地址"></textarea>
                </view>
            </view>
        </view>
        
        <view class="delivery-section">
            <view class="section-title">配送方式</view>
            <view class="delivery-options">
                <view class="delivery-option" 
                      :class="{ active: deliveryType === 'express' }"
                      @click="deliveryType = 'express'">
                    <text class="option-icon">🚚</text>
                    <text class="option-name">快递配送</text>
                    <text class="option-desc">预计2-3天送达</text>
                </view>
                <view class="delivery-option"
                      :class="{ active: deliveryType === 'same-day' }"
                      @click="deliveryType = 'same-day'">
                    <text class="option-icon">🏃</text>
                    <text class="option-name">同城速递</text>
                    <text class="option-desc">当日送达</text>
                </view>
            </view>
        </view>
        
        <view class="goods-section">
            <view class="section-title">商品信息</view>
            <view class="goods-list">
                <view class="goods-item" v-for="item in goodsList" :key="item.id">
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
        
        <view class="remark-section">
            <view class="section-title">订单备注</view>
            <textarea class="remark-input" v-model="remark" placeholder="选填，请输入订单备注"></textarea>
        </view>
        
        <view class="bottom-bar">
            <view class="total-info">
                <text class="total-label">共{{ totalCount }}件，合计：</text>
                <text class="total-price text-primary font-bold">¥{{ totalAmount }}</text>
            </view>
            <button class="submit-btn" @click="submitOrder">提交订单</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            cartItemIds: [],
            receiverName: '',
            receiverPhone: '',
            receiverAddress: '',
            deliveryType: 'express',
            remark: '',
            goodsList: [],
            elderMode: false
        }
    },
    onLoad(options) {
        this.elderMode = getApp().globalData.elderMode
        if (options.cartItemIds) {
            this.cartItemIds = options.cartItemIds.split(',').map(Number)
        }
        this.loadGoodsInfo()
        this.loadUserInfo()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    computed: {
        totalCount() {
            return this.goodsList.reduce((sum, item) => sum + item.quantity, 0)
        },
        totalAmount() {
            let total = this.goodsList.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0)
            return total.toFixed(2)
        }
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        loadGoodsInfo() {
            this.$request.get('/cart/list').then(res => {
                this.goodsList = res.filter(item => this.cartItemIds.includes(item.id))
            }).catch(err => {
                console.error(err)
            })
        },
        
        loadUserInfo() {
            this.$request.get('/user/info').then(res => {
                if (res.phone) {
                    this.receiverPhone = res.phone
                }
                if (res.nickname) {
                    this.receiverName = res.nickname
                }
            }).catch(err => {
                console.error(err)
            })
        },
        
        submitOrder() {
            if (!this.receiverName.trim()) {
                uni.showToast({
                    title: '请输入收货人姓名',
                    icon: 'none'
                })
                return
            }
            if (!this.receiverPhone.trim()) {
                uni.showToast({
                    title: '请输入手机号',
                    icon: 'none'
                })
                return
            }
            if (!this.receiverAddress.trim()) {
                uni.showToast({
                    title: '请输入收货地址',
                    icon: 'none'
                })
                return
            }
            
            const orderData = {
                cartItemIds: this.cartItemIds,
                deliveryType: this.deliveryType,
                deliveryTime: '',
                receiverName: this.receiverName,
                receiverPhone: this.receiverPhone,
                receiverAddress: this.receiverAddress,
                couponId: null,
                pointsUsed: 0,
                remark: this.remark
            }
            
            this.$request.post('/order/create', orderData).then(res => {
                uni.showToast({
                    title: '下单成功',
                    icon: 'success'
                })
                setTimeout(() => {
                    uni.redirectTo({
                        url: `/pages/order/detail?id=${res.orderId}`
                    })
                }, 1500)
            }).catch(err => {
                console.error(err)
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

.section-title {
    font-size: 30rpx;
    font-weight: 500;
    margin-bottom: 20rpx;
    padding-left: 10rpx;
    border-left: 6rpx solid #FF6B6B;
}

.address-section, .delivery-section, .goods-section, .remark-section {
    background: #fff;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.address-form {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.form-item {
    display: flex;
    align-items: center;
    gap: 20rpx;
}

.form-label {
    width: 140rpx;
    font-size: 28rpx;
    color: #666;
    flex-shrink: 0;
}

.form-input {
    flex: 1;
    height: 70rpx;
    line-height: 70rpx;
    padding: 0 20rpx;
    background: #f9f9f9;
    border-radius: 10rpx;
    font-size: 28rpx;
}

.form-textarea {
    flex: 1;
    min-height: 100rpx;
    padding: 20rpx;
    background: #f9f9f9;
    border-radius: 10rpx;
    font-size: 28rpx;
}

.delivery-options {
    display: flex;
    gap: 20rpx;
}

.delivery-option {
    flex: 1;
    padding: 30rpx 20rpx;
    background: #f9f9f9;
    border-radius: 16rpx;
    text-align: center;
    border: 2rpx solid transparent;
}

.delivery-option.active {
    background: #fff5f5;
    border-color: #FF6B6B;
}

.option-icon {
    font-size: 60rpx;
    display: block;
    margin-bottom: 10rpx;
}

.option-name {
    font-size: 28rpx;
    font-weight: 500;
    display: block;
    margin-bottom: 5rpx;
}

.option-desc {
    font-size: 24rpx;
    color: #999;
    display: block;
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

.remark-input {
    width: 100%;
    min-height: 150rpx;
    padding: 20rpx;
    background: #f9f9f9;
    border-radius: 10rpx;
    font-size: 28rpx;
    box-sizing: border-box;
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
    justify-content: space-between;
    padding: 0 30rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.total-info {
    display: flex;
    align-items: baseline;
}

.total-label {
    font-size: 26rpx;
    color: #666;
}

.total-price {
    font-size: 36rpx;
}

.submit-btn {
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
</style>