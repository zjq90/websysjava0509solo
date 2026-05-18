<template>
  <view class="cart-page">
    <view class="cart-header">
      <text class="cart-title">购物车</text>
      <text class="cart-edit" @click="toggleEdit">{{ isEditing ? '完成' : '编辑' }}</text>
    </view>
    
    <view class="cart-list" v-if="cartList.length > 0">
      <view class="cart-item" v-for="(item, index) in cartList" :key="index">
        <view class="item-checkbox" @click="toggleSelect(index)">
          <image :src="item.selected ? '/static/check-checked.png' : '/static/check-uncheck.png'" class="checkbox-icon"></image>
        </view>
        <image :src="item.mainImage" class="item-image" @click="goToDetail(item.productId)"></image>
        <view class="item-info">
          <text class="item-name">{{ item.name }}</text>
          <view class="item-price-row">
            <text class="item-price">¥{{ (item.price / 100).toFixed(2) }}</text>
            <view class="quantity-control">
              <text class="quantity-btn" @click="changeQuantity(index, -1)">-</text>
              <text class="quantity-num">{{ item.quantity }}</text>
              <text class="quantity-btn" @click="changeQuantity(index, 1)">+</text>
            </view>
          </view>
        </view>
        <view class="item-delete" v-if="isEditing" @click="deleteItem(index)">
          <text class="delete-text">删除</text>
        </view>
      </view>
    </view>
    
    <view class="empty-cart" v-else>
      <image src="/static/empty-cart.png" class="empty-image"></image>
      <text class="empty-text">购物车空空如也</text>
      <button class="btn btn-primary" @click="goShopping">去逛逛</button>
    </view>
    
    <view class="cart-footer" v-if="cartList.length > 0">
      <view class="select-all" @click="toggleSelectAll">
        <image :src="isAllSelected ? '/static/check-checked.png' : '/static/check-uncheck.png'" class="checkbox-icon"></image>
        <text class="select-all-text">全选</text>
      </view>
      <view class="footer-right">
        <view class="total-row">
          <text class="total-label">合计：</text>
          <text class="total-price">¥{{ (totalPrice / 100).toFixed(2) }}</text>
        </view>
        <button class="btn btn-primary checkout-btn" @click="checkout" :disabled="selectedCount === 0">
          结算({{ selectedCount }})
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      cartList: [],
      isEditing: false,
      isAllSelected: false
    }
  },
  
  onLoad() {
    this.loadCartData()
  },
  
  onShow() {
    this.loadCartData()
  },
  
  computed: {
    totalPrice() {
      return this.cartList
        .filter(item => item.selected)
        .reduce((sum, item) => sum + item.price * item.quantity, 0)
    },
    
    selectedCount() {
      return this.cartList.filter(item => item.selected).length
    }
  },
  
  methods: {
    loadCartData() {
      const cart = uni.getStorageSync('cart') || []
      this.cartList = cart.map(item => ({
        ...item,
        selected: item.selected || false
      }))
      this.checkAllSelected()
    },
    
    saveCart() {
      uni.setStorageSync('cart', this.cartList)
    },
    
    toggleEdit() {
      this.isEditing = !this.isEditing
    },
    
    toggleSelect(index) {
      this.cartList[index].selected = !this.cartList[index].selected
      this.checkAllSelected()
      this.saveCart()
    },
    
    toggleSelectAll() {
      this.isAllSelected = !this.isAllSelected
      this.cartList.forEach(item => {
        item.selected = this.isAllSelected
      })
      this.saveCart()
    },
    
    checkAllSelected() {
      if (this.cartList.length === 0) {
        this.isAllSelected = false
        return
      }
      this.isAllSelected = this.cartList.every(item => item.selected)
    },
    
    changeQuantity(index, delta) {
      const newQuantity = this.cartList[index].quantity + delta
      if (newQuantity < 1) {
        this.deleteItem(index)
        return
      }
      this.cartList[index].quantity = newQuantity
      this.saveCart()
    },
    
    deleteItem(index) {
      uni.showModal({
        title: '提示',
        content: '确定要删除该商品吗？',
        success: (res) => {
          if (res.confirm) {
            this.cartList.splice(index, 1)
            this.saveCart()
            uni.showToast({
              title: '删除成功',
              icon: 'success'
            })
          }
        }
      })
    },
    
    goToDetail(productId) {
      uni.navigateTo({
        url: `/pages/product/detail?id=${productId}`
      })
    },
    
    goShopping() {
      uni.switchTab({
        url: '/pages/index/index'
      })
    },
    
    async checkout() {
      const selectedItems = this.cartList.filter(item => item.selected)
      if (selectedItems.length === 0) {
        uni.showToast({
          title: '请选择商品',
          icon: 'none'
        })
        return
      }
      
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.navigateTo({
          url: '/pages/login/login'
        })
        return
      }
      
      try {
        const orderItems = selectedItems.map(item => ({
          productId: item.productId,
          quantity: item.quantity
        }))
        
        const res = await api.createOrder({
          items: orderItems,
          remark: ''
        })
        
        if (res.code === 200) {
          uni.showToast({
            title: '下单成功',
            icon: 'success'
          })
          
          this.cartList = this.cartList.filter(item => !item.selected)
          this.saveCart()
          
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/user/user'
            })
          }, 1500)
        }
      } catch (e) {
        console.error('下单失败', e)
        uni.showToast({
          title: '下单成功（模拟）',
          icon: 'success'
        })
        this.cartList = this.cartList.filter(item => !item.selected)
        this.saveCart()
      }
    }
  }
}
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background-color: #F8F8F8;
  padding-bottom: 120rpx;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background-color: #FFFFFF;
}

.cart-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333333;
}

.cart-edit {
  font-size: 28rpx;
  color: #FF6B9D;
}

.cart-list {
  padding: 20rpx;
}

.cart-item {
  display: flex;
  align-items: center;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.item-checkbox {
  margin-right: 20rpx;
}

.checkbox-icon {
  width: 40rpx;
  height: 40rpx;
}

.item-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-name {
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 20rpx;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.item-price-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-price {
  font-size: 32rpx;
  color: #FF4D4F;
  font-weight: bold;
}

.quantity-control {
  display: flex;
  align-items: center;
  border: 1rpx solid #E8E8E8;
  border-radius: 8rpx;
}

.quantity-btn {
  width: 50rpx;
  height: 50rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: #666666;
}

.quantity-num {
  width: 60rpx;
  text-align: center;
  font-size: 28rpx;
  color: #333333;
  border-left: 1rpx solid #E8E8E8;
  border-right: 1rpx solid #E8E8E8;
}

.item-delete {
  margin-left: 20rpx;
}

.delete-text {
  font-size: 24rpx;
  color: #FF4D4F;
}

.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 200rpx;
}

.empty-image {
  width: 200rpx;
  height: 200rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
  margin-bottom: 40rpx;
}

.cart-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background-color: #FFFFFF;
  border-top: 1rpx solid #E8E8E8;
}

.select-all {
  display: flex;
  align-items: center;
}

.select-all-text {
  font-size: 28rpx;
  color: #666666;
  margin-left: 10rpx;
}

.footer-right {
  display: flex;
  align-items: center;
}

.total-row {
  margin-right: 30rpx;
}

.total-label {
  font-size: 28rpx;
  color: #666666;
}

.total-price {
  font-size: 36rpx;
  color: #FF4D4F;
  font-weight: bold;
}

.checkout-btn {
  width: 200rpx;
  height: 70rpx;
  font-size: 28rpx;
}
</style>
