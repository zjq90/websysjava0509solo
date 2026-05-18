<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="product-img" :style="{background: getRandomColor()}">
            <text class="product-emoji">💐</text>
        </view>
        
        <view class="product-base">
            <view class="price-row">
                <text class="price-current text-primary font-bold">¥{{ product.price }}</text>
                <text class="price-original text-sm line-through text-muted">¥{{ product.originalPrice }}</text>
            </view>
            <text class="product-name">{{ product.name }}</text>
            <text class="product-desc text-sm text-muted">{{ product.description }}</text>
            <view class="sales-row">
                <text class="sales text-sm text-muted">已售{{ product.sales || 0 }}件</text>
                <text class="stock text-sm text-muted">库存{{ product.stock || 0 }}件</text>
            </view>
        </view>
        
        <view class="product-detail">
            <view class="detail-title">商品详情</view>
            <view class="detail-content">
                <text>{{ product.detail }}</text>
            </view>
        </view>
        
        <view class="bottom-bar">
            <view class="bar-left">
                <view class="bar-item" @click="goToCart">
                    <text class="bar-icon">🛒</text>
                    <text class="bar-text">购物车</text>
                    <view class="cart-badge" v-if="cartCount > 0">{{ cartCount > 99 ? '99+' : cartCount }}</view>
                </view>
                <view class="bar-item" @click="goToHome">
                    <text class="bar-icon">🏠</text>
                    <text class="bar-text">首页</text>
                </view>
            </view>
            <view class="bar-right">
                <button class="btn-cart" @click="addToCart">加入购物车</button>
                <button class="btn-buy" @click="buyNow">立即购买</button>
            </view>
        </view>
        
        <view class="quantity-popup" v-if="showQuantity">
            <view class="popup-mask" @click="showQuantity = false"></view>
            <view class="popup-content">
                <view class="popup-header">
                    <text class="popup-title">选择数量</text>
                    <text class="popup-close" @click="showQuantity = false">✕</text>
                </view>
                <view class="popup-body">
                    <view class="quantity-row">
                        <button class="quantity-btn" @click="quantity > 1 && quantity--" :disabled="quantity <= 1">-</button>
                        <text class="quantity-value">{{ quantity }}</text>
                        <button class="quantity-btn" @click="quantity++">+</button>
                    </view>
                </view>
                <view class="popup-footer">
                    <button class="popup-btn" @click="confirmAddToCart">确定</button>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            productId: null,
            product: {},
            quantity: 1,
            showQuantity: false,
            cartCount: 0,
            elderMode: false
        }
    },
    onLoad(options) {
        this.elderMode = getApp().globalData.elderMode
        this.productId = options.id
        this.loadProductDetail()
        this.loadCartCount()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
        this.loadCartCount()
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        loadProductDetail() {
            this.$request.get(`/product/${this.productId}`).then(res => {
                this.product = res
            }).catch(err => {
                console.error(err)
            })
        },
        
        loadCartCount() {
            this.$request.get('/cart/list').then(res => {
                this.cartCount = res.length
            }).catch(err => {
                console.error(err)
            })
        },
        
        addToCart() {
            this.showQuantity = true
        },
        
        confirmAddToCart() {
            this.$request.post('/cart/add', {
                productId: this.productId,
                quantity: this.quantity
            }).then(res => {
                uni.showToast({
                    title: '已加入购物车',
                    icon: 'success'
                })
                this.showQuantity = false
                this.cartCount++
            }).catch(err => {
                console.error(err)
            })
        },
        
        buyNow() {
            this.$request.post('/cart/add', {
                productId: this.productId,
                quantity: 1
            }).then(res => {
                uni.navigateTo({
                    url: '/pages/cart/cart'
                })
            }).catch(err => {
                console.error(err)
            })
        },
        
        goToCart() {
            uni.switchTab({
                url: '/pages/cart/cart'
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
    padding-bottom: 120rpx;
}

.product-img {
    height: 500rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-emoji {
    font-size: 200rpx;
}

.product-base {
    background: #fff;
    padding: 30rpx;
    margin-top: 20rpx;
}

.price-row {
    display: flex;
    align-items: baseline;
    gap: 20rpx;
    margin-bottom: 20rpx;
}

.price-current {
    font-size: 48rpx;
}

.price-original {
    font-size: 28rpx;
}

.product-name {
    font-size: 32rpx;
    font-weight: 500;
    display: block;
    margin-bottom: 15rpx;
}

.product-desc {
    display: block;
    margin-bottom: 20rpx;
}

.sales-row {
    display: flex;
    gap: 30rpx;
}

.product-detail {
    background: #fff;
    margin-top: 20rpx;
    padding: 30rpx;
}

.detail-title {
    font-size: 30rpx;
    font-weight: 500;
    margin-bottom: 20rpx;
    padding-bottom: 20rpx;
    border-bottom: 2rpx solid #f5f5f5;
}

.detail-content {
    line-height: 1.8;
    color: #666;
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
    padding: 0 20rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.bar-left {
    display: flex;
    gap: 40rpx;
}

.bar-item {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.bar-icon {
    font-size: 40rpx;
}

.bar-text {
    font-size: 22rpx;
    color: #666;
    margin-top: 5rpx;
}

.cart-badge {
    position: absolute;
    top: -10rpx;
    right: -15rpx;
    min-width: 36rpx;
    height: 36rpx;
    line-height: 36rpx;
    text-align: center;
    background: #FF6B6B;
    color: #fff;
    border-radius: 18rpx;
    font-size: 22rpx;
    padding: 0 8rpx;
}

.bar-right {
    flex: 1;
    display: flex;
    gap: 20rpx;
    margin-left: 30rpx;
}

.btn-cart {
    flex: 1;
    height: 80rpx;
    line-height: 80rpx;
    background: #FFE4E1;
    color: #FF6B6B;
    border-radius: 40rpx;
    font-size: 28rpx;
    border: none;
    padding: 0;
}

.btn-buy {
    flex: 1;
    height: 80rpx;
    line-height: 80rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 40rpx;
    font-size: 28rpx;
    border: none;
    padding: 0;
}

.quantity-popup {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 999;
}

.popup-mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0,0,0,0.5);
}

.popup-content {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
    border-radius: 30rpx 30rpx 0 0;
    overflow: hidden;
}

.popup-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
    border-bottom: 2rpx solid #f5f5f5;
}

.popup-title {
    font-size: 32rpx;
    font-weight: 500;
}

.popup-close {
    font-size: 40rpx;
    color: #999;
}

.popup-body {
    padding: 40rpx 30rpx;
}

.quantity-row {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 30rpx;
}

.quantity-btn {
    width: 80rpx;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    background: #f5f5f5;
    border-radius: 16rpx;
    font-size: 36rpx;
    border: none;
    padding: 0;
}

.quantity-btn:disabled {
    opacity: 0.5;
}

.quantity-value {
    font-size: 36rpx;
    min-width: 100rpx;
    text-align: center;
}

.popup-footer {
    padding: 30rpx;
}

.popup-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    border: none;
    padding: 0;
}
</style>