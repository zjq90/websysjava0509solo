<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="cart-list" v-if="cartList.length > 0">
            <view class="cart-item" v-for="item in cartList" :key="item.id">
                <view class="item-checkbox" @click="toggleSelect(item.id)">
                    <text class="checkbox-icon" :class="{ checked: selectedIds.includes(item.id) }">
                        {{ selectedIds.includes(item.id) ? '✓' : '' }}
                    </text>
                </view>
                <view class="item-img" :style="{background: getRandomColor()}">
                    <text class="item-emoji">💐</text>
                </view>
                <view class="item-info">
                    <text class="item-name">{{ item.productName }}</text>
                    <view class="item-price-row">
                        <text class="item-price text-primary font-bold">¥{{ item.price }}</text>
                        <view class="quantity-box">
                            <button class="quantity-btn" @click="updateQuantity(item.id, item.quantity - 1)" :disabled="item.quantity <= 1">-</button>
                            <text class="quantity-value">{{ item.quantity }}</text>
                            <button class="quantity-btn" @click="updateQuantity(item.id, item.quantity + 1)">+</button>
                        </view>
                    </view>
                </view>
                <view class="item-delete" @click="deleteItem(item.id)">
                    <text class="delete-icon">🗑️</text>
                </view>
            </view>
        </view>
        
        <view class="empty-cart" v-else>
            <text class="empty-icon">🛒</text>
            <text class="empty-text">购物车是空的</text>
            <button class="go-shopping-btn" @click="goShopping">去逛逛</button>
        </view>
        
        <view class="bottom-bar" v-if="cartList.length > 0">
            <view class="bar-left">
                <view class="select-all" @click="toggleSelectAll">
                    <text class="checkbox-icon" :class="{ checked: isAllSelected }">
                        {{ isAllSelected ? '✓' : '' }}
                    </text>
                    <text class="select-text">全选</text>
                </view>
            </view>
            <view class="bar-right">
                <view class="total-box">
                    <text class="total-label">合计：</text>
                    <text class="total-price text-primary font-bold">¥{{ totalPrice }}</text>
                </view>
                <button class="checkout-btn" :disabled="selectedIds.length === 0" @click="goCheckout">
                    结算({{ selectedIds.length }})
                </button>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            cartList: [],
            selectedIds: [],
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
        this.loadCart()
    },
    computed: {
        isAllSelected() {
            return this.cartList.length > 0 && this.selectedIds.length === this.cartList.length
        },
        totalPrice() {
            let total = 0
            this.cartList.forEach(item => {
                if (this.selectedIds.includes(item.id)) {
                    total += Number(item.price) * item.quantity
                }
            })
            return total.toFixed(2)
        }
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        loadCart() {
            this.$request.get('/cart/list').then(res => {
                this.cartList = res
                this.selectedIds = res.map(item => item.id)
            }).catch(err => {
                console.error(err)
            })
        },
        
        toggleSelect(id) {
            const index = this.selectedIds.indexOf(id)
            if (index > -1) {
                this.selectedIds.splice(index, 1)
            } else {
                this.selectedIds.push(id)
            }
        },
        
        toggleSelectAll() {
            if (this.isAllSelected) {
                this.selectedIds = []
            } else {
                this.selectedIds = this.cartList.map(item => item.id)
            }
        },
        
        updateQuantity(id, quantity) {
            if (quantity < 1) return
            this.$request.put(`/cart/update/${id}`, { quantity }).then(res => {
                this.loadCart()
            }).catch(err => {
                console.error(err)
            })
        },
        
        deleteItem(id) {
            uni.showModal({
                title: '提示',
                content: '确定删除该商品吗？',
                success: (res) => {
                    if (res.confirm) {
                        this.$request.delete(`/cart/remove/${id}`).then(() => {
                            uni.showToast({
                                title: '已删除',
                                icon: 'success'
                            })
                            this.loadCart()
                        }).catch(err => {
                            console.error(err)
                        })
                    }
                }
            })
        },
        
        goCheckout() {
            if (this.selectedIds.length === 0) {
                uni.showToast({
                    title: '请选择商品',
                    icon: 'none'
                })
                return
            }
            uni.navigateTo({
                url: `/pages/order/checkout?cartItemIds=${this.selectedIds.join(',')}`
            })
        },
        
        goShopping() {
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
    background: #f5f5f5;
}

.cart-list {
    padding: 20rpx;
}

.cart-item {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    gap: 20rpx;
}

.item-checkbox {
    width: 50rpx;
}

.checkbox-icon {
    display: inline-block;
    width: 40rpx;
    height: 40rpx;
    line-height: 40rpx;
    text-align: center;
    border: 2rpx solid #ddd;
    border-radius: 50%;
    font-size: 24rpx;
    color: #fff;
}

.checkbox-icon.checked {
    background: #FF6B6B;
    border-color: #FF6B6B;
}

.item-img {
    width: 160rpx;
    height: 160rpx;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
}

.item-emoji {
    font-size: 80rpx;
}

.item-info {
    flex: 1;
    min-width: 0;
}

.item-name {
    font-size: 28rpx;
    font-weight: 500;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 15rpx;
}

.item-price-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.item-price {
    font-size: 30rpx;
}

.quantity-box {
    display: flex;
    align-items: center;
    gap: 10rpx;
}

.quantity-btn {
    width: 50rpx;
    height: 50rpx;
    line-height: 50rpx;
    text-align: center;
    background: #f5f5f5;
    border-radius: 8rpx;
    font-size: 28rpx;
    border: none;
    padding: 0;
}

.quantity-btn:disabled {
    opacity: 0.5;
}

.quantity-value {
    min-width: 50rpx;
    text-align: center;
    font-size: 28rpx;
}

.item-delete {
    width: 60rpx;
    text-align: center;
}

.delete-icon {
    font-size: 40rpx;
}

.empty-cart {
    padding: 200rpx 0;
    text-align: center;
}

.empty-icon {
    font-size: 160rpx;
    display: block;
}

.empty-text {
    display: block;
    margin-top: 30rpx;
    color: #999;
    font-size: 28rpx;
}

.go-shopping-btn {
    margin-top: 40rpx;
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

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 120rpx;
    background: #fff;
    display: flex;
    align-items: center;
    padding: 0 30rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}

.bar-left {
    display: flex;
    align-items: center;
}

.select-all {
    display: flex;
    align-items: center;
    gap: 15rpx;
}

.select-text {
    font-size: 26rpx;
    color: #666;
}

.bar-right {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    gap: 30rpx;
}

.total-box {
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

.checkout-btn {
    width: 200rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 40rpx;
    font-size: 28rpx;
    border: none;
    padding: 0;
}

.checkout-btn:disabled {
    opacity: 0.5;
}
</style>