<template>
  <view class="container">
    <view class="step-bar">
      <view :class="['step', step >= 1 ? 'active' : '']">
        <text class="step-num">1</text>
        <text class="step-text">选择客户</text>
      </view>
      <view class="step-line"></view>
      <view :class="['step', step >= 2 ? 'active' : '']">
        <text class="step-num">2</text>
        <text class="step-text">选择商品</text>
      </view>
      <view class="step-line"></view>
      <view :class="['step', step >= 3 ? 'active' : '']">
        <text class="step-num">3</text>
        <text class="step-text">确认订单</text>
      </view>
    </view>

    <view v-if="step === 1" class="step-content">
      <view class="section-title">选择客户</view>
      <view class="search-bar">
        <input 
          class="search-input" 
          placeholder="搜索客户姓名/电话" 
          v-model="customerKeyword"
          @confirm="searchCustomers"
        />
      </view>
      <view class="customer-list">
        <view 
          :class="['customer-item', selectedCustomer?.id === c.id ? 'selected' : '']" 
          v-for="c in filteredCustomers" 
          :key="c.id"
          @click="selectCustomer(c)"
        >
          <view class="customer-avatar">{{ c.name.charAt(0) }}</view>
          <view class="customer-info">
            <view class="customer-name">{{ c.name }}</view>
            <view class="customer-level">{{ getLevelText(c.level) }}</view>
          </view>
          <view class="check-icon" v-if="selectedCustomer?.id === c.id">✓</view>
        </view>
      </view>
      <view class="quick-add" @click="goAddCustomer">
        <text class="add-icon">+</text>
        <text>快速添加临时客户</text>
      </view>
      <button class="next-btn" :disabled="!selectedCustomer" @click="step = 2">下一步</button>
    </view>

    <view v-else-if="step === 2" class="step-content">
      <view class="section-title">选择商品</view>
      <view class="customer-brief">
        <text>客户：{{ selectedCustomer.name }}</text>
        <text class="level-badge">{{ getLevelText(selectedCustomer.level) }}</text>
      </view>
      <view class="search-bar">
        <input 
          class="search-input" 
          placeholder="搜索商品名称" 
          v-model="productKeyword"
          @confirm="searchProducts"
        />
      </view>
      <view class="product-list">
        <view class="product-item" v-for="p in filteredProducts" :key="p.id">
          <view class="product-info">
            <view class="product-name">{{ p.name }}</view>
            <view class="product-meta">
              <text>批次: {{ p.batchNumber }}</text>
              <text>库存: {{ p.stockQuantity }}</text>
            </view>
            <view class="price-row">
              <text class="original-price">原价: ¥{{ p.basePrice }}</text>
              <text class="actual-price">
                {{ getLevelText(selectedCustomer.level) }}价: ¥{{ getDiscountPrice(p) }}
              </text>
            </view>
          </view>
          <view class="quantity-control">
            <view class="qty-btn" @click="decreaseQty(p)">-</view>
            <text class="qty-value">{{ getCartQuantity(p) }}</text>
            <view class="qty-btn" @click="increaseQty(p)">+</view>
          </view>
        </view>
      </view>
      <view class="bottom-bar">
        <view class="summary">
          <text>已选 {{ totalItems }} 件</text>
          <text class="total-price">合计: ¥{{ totalAmount }}</text>
        </view>
        <button class="next-btn" :disabled="cartItems.length === 0" @click="step = 3">下一步</button>
      </view>
      <view class="step-btn-group">
        <button class="prev-btn" @click="step = 1">上一步</button>
      </view>
    </view>

    <view v-else-if="step === 3" class="step-content">
      <view class="section-title">确认订单</view>
      <view class="summary-card">
        <view class="summary-row">
          <text class="label">客户</text>
          <text class="value">{{ selectedCustomer.name }}</text>
        </view>
        <view class="summary-row">
          <text class="label">客户等级</text>
          <text class="value">{{ getLevelText(selectedCustomer.level) }}</text>
        </view>
        <view class="summary-row">
          <text class="label">折扣率</text>
          <text class="value highlight">{{ (1 - getDiscountRate()) * 100 }}% 折扣</text>
        </view>
        <view class="divider"></view>
        <view v-for="item in cartItems" :key="item.productId" class="cart-item">
          <view class="cart-item-name">{{ item.productName }}</view>
          <view class="cart-item-detail">
            <text>¥{{ item.unitPrice }} x {{ item.quantity }}</text>
            <text class="item-total">¥{{ (item.unitPrice * item.quantity).toFixed(2) }}</text>
          </view>
        </view>
        <view class="divider"></view>
        <view class="summary-row total">
          <text class="label">订单合计</text>
          <text class="value price">¥{{ totalAmount }}</text>
        </view>
      </view>
      <view class="form-card">
        <view class="form-item">
          <text class="label">备注</text>
          <textarea class="textarea" placeholder="请输入订单备注" v-model="form.notes"></textarea>
        </view>
      </view>
      <view class="step-btn-group">
        <button class="prev-btn" @click="step = 2">上一步</button>
        <button class="submit-btn" @click="submitOrder">提交订单</button>
      </view>
    </view>
  </view>
</template>

<script>
import customerApi from '@/api/customer.js'
import productApi from '@/api/product.js'
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      step: 1,
      customerKeyword: '',
      productKeyword: '',
      customers: [],
      products: [],
      selectedCustomer: null,
      cartItems: [],
      form: {
        notes: ''
      }
    }
  },
  computed: {
    filteredCustomers() {
      if (!this.customerKeyword.trim()) return this.customers
      const kw = this.customerKeyword.toLowerCase()
      return this.customers.filter(c => 
        c.name.toLowerCase().includes(kw)
      )
    },
    filteredProducts() {
      if (!this.productKeyword.trim()) return this.products
      const kw = this.productKeyword.toLowerCase()
      return this.products.filter(p => 
        p.name.toLowerCase().includes(kw)
      )
    },
    totalItems() {
      return this.cartItems.reduce((sum, item) => sum + item.quantity, 0)
    },
    totalAmount() {
      return this.cartItems.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0).toFixed(2)
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      uni.showLoading({ title: '加载中...' })
      try {
        const [customers, products] = await Promise.all([
          customerApi.getAllCustomers(),
          productApi.getAllProducts()
        ])
        this.customers = customers
        this.products = products
      } catch (e) {
        console.error('加载数据失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    getLevelText(level) {
      const map = {
        'TEMPORARY': '临时',
        'NORMAL': '普通',
        'VIP': 'VIP',
        'SVIP': 'SVIP',
        'DIAMOND': '钻石'
      }
      return map[level] || level
    },
    getDiscountRate() {
      const map = {
        'TEMPORARY': 1.00,
        'NORMAL': 0.95,
        'VIP': 0.90,
        'SVIP': 0.85,
        'DIAMOND': 0.80
      }
      return map[this.selectedCustomer?.level] || 1.00
    },
    getDiscountPrice(product) {
      return (product.basePrice * this.getDiscountRate()).toFixed(2)
    },
    selectCustomer(customer) {
      this.selectedCustomer = customer
    },
    getCartQuantity(product) {
      const item = this.cartItems.find(i => i.productId === product.id)
      return item ? item.quantity : 0
    },
    increaseQty(product) {
      const discountPrice = parseFloat(this.getDiscountPrice(product))
      const index = this.cartItems.findIndex(i => i.productId === product.id)
      if (index >= 0) {
        if (this.cartItems[index].quantity < product.stockQuantity) {
          this.cartItems[index].quantity++
        }
      } else {
        this.cartItems.push({
          productId: product.id,
          productName: product.name,
          unitPrice: discountPrice,
          quantity: 1
        })
      }
    },
    decreaseQty(product) {
      const index = this.cartItems.findIndex(i => i.productId === product.id)
      if (index >= 0) {
        if (this.cartItems[index].quantity > 1) {
          this.cartItems[index].quantity--
        } else {
          this.cartItems.splice(index, 1)
        }
      }
    },
    goAddCustomer() {
      uni.navigateTo({ url: '/pages/customer/add' })
    },
    async searchCustomers() {
      if (!this.customerKeyword.trim()) {
        this.customers = await customerApi.getAllCustomers()
        return
      }
      this.customers = await customerApi.searchCustomers(this.customerKeyword)
    },
    async searchProducts() {
      if (!this.productKeyword.trim()) {
        this.products = await productApi.getAllProducts()
        return
      }
      this.products = await productApi.searchProducts(this.productKeyword)
    },
    async submitOrder() {
      if (!this.selectedCustomer) {
        uni.showToast({ title: '请选择客户', icon: 'none' })
        return
      }
      if (this.cartItems.length === 0) {
        uni.showToast({ title: '请选择商品', icon: 'none' })
        return
      }
      uni.showLoading({ title: '提交中...' })
      try {
        const orderData = {
          customerId: this.selectedCustomer.id,
          notes: this.form.notes,
          orderItems: this.cartItems.map(item => ({
            productId: item.productId,
            quantity: item.quantity
          }))
        }
        const order = await orderApi.createOrder(orderData)
        uni.showToast({ title: '订单创建成功', icon: 'success' })
        setTimeout(() => {
          uni.redirectTo({ url: `/pages/order/detail?id=${order.id}` })
        }, 1500)
      } catch (e) {
        console.error('创建订单失败', e)
        uni.showToast({ title: '创建失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.step-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32rpx 0;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.step-num {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background: #e0e0e0;
  color: #999;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8rpx;
}

.step.active .step-num {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.step-text {
  font-size: 24rpx;
  color: #999;
}

.step.active .step-text {
  color: #667eea;
}

.step-line {
  width: 80rpx;
  height: 4rpx;
  background: #e0e0e0;
  margin: 0 8rpx 24rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 16rpx;
}

.customer-brief {
  background: #f5f7ff;
  padding: 20rpx 24rpx;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.level-badge {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 22rpx;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
}

.search-bar {
  margin-bottom: 20rpx;
}

.search-input {
  width: 100%;
  height: 72rpx;
  background: #fff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  border: 2rpx solid #eee;
}

.customer-list,
.product-list {
  margin-bottom: 24rpx;
}

.customer-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 12rpx;
  display: flex;
  align-items: center;
  border: 2rpx solid transparent;
}

.customer-item.selected {
  border-color: #667eea;
  background: #f5f7ff;
}

.customer-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}

.customer-info {
  flex: 1;
}

.customer-name {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 4rpx;
}

.customer-level {
  font-size: 24rpx;
  color: #667eea;
}

.check-icon {
  font-size: 36rpx;
  color: #667eea;
}

.product-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 12rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.product-name {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.product-meta {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
  display: flex;
  gap: 20rpx;
}

.price-row {
  display: flex;
  gap: 20rpx;
  align-items: center;
}

.original-price {
  font-size: 24rpx;
  color: #999;
  text-decoration: line-through;
}

.actual-price {
  font-size: 28rpx;
  color: #ff4d4f;
  font-weight: 500;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.qty-btn {
  width: 56rpx;
  height: 56rpx;
  border-radius: 50%;
  background: #f5f5f5;
  color: #666;
  font-size: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.qty-value {
  font-size: 32rpx;
  color: #333;
  min-width: 48rpx;
  text-align: center;
}

.quick-add {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.add-icon {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}

.summary-card,
.form-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.summary-row .label {
  font-size: 28rpx;
  color: #666;
}

.summary-row .value {
  font-size: 28rpx;
  color: #333;
}

.summary-row .value.highlight {
  color: #ff4d4f;
}

.summary-row.total .label {
  font-size: 30rpx;
  font-weight: 500;
}

.summary-row.total .value.price {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.divider {
  height: 2rpx;
  background: #f5f5f5;
  margin: 16rpx 0;
}

.cart-item {
  padding: 12rpx 0;
}

.cart-item-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.cart-item-detail {
  display: flex;
  justify-content: space-between;
  font-size: 26rpx;
  color: #666;
}

.item-total {
  color: #ff4d4f;
}

.form-item .label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 12rpx;
  display: block;
}

.textarea {
  width: 100%;
  min-height: 160rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.bottom-bar {
  position: fixed;
  left: 32rpx;
  right: 32rpx;
  bottom: 32rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.summary {
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.total-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.step-btn-group {
  display: flex;
  gap: 20rpx;
  margin-top: 24rpx;
}

.prev-btn,
.next-btn,
.submit-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}

.prev-btn {
  background: #f5f5f5;
  color: #666;
}

.next-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.submit-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.next-btn[disabled] {
  background: #ccc;
}
</style>
