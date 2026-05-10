<template>
  <view class="container">
    <view class="form-card">
      <view class="form-title">创建订单</view>

      <view class="form-item">
        <text class="form-label">选择客户 <text class="required">*</text></text>
        <picker :value="customerIndex" :range="customerNames" @change="onCustomerChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.customerId }">
              {{ customerNames[customerIndex] || '请选择客户' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">订单备注</text>
        <textarea 
          class="form-textarea" 
          v-model="form.remark" 
          placeholder="请输入订单备注"
          :maxlength="500"
        />
      </view>
    </view>

    <view class="items-section">
      <view class="section-header">
        <text class="section-title">订单商品</text>
        <view class="add-item-btn" @click="addItem">
          <text class="add-icon">+</text>
          <text>添加商品</text>
        </view>
      </view>

      <view class="item-card" v-for="(item, index) in form.items" :key="index">
        <view class="item-row">
          <text class="item-label">商品 {{ index + 1 }}</text>
          <text class="remove-btn" @click="removeItem(index)">删除</text>
        </view>
        
        <picker :value="item.seedIndex" :range="seedNames" @change="onSeedChange(index, $event)">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !item.inventoryId }">
              {{ seedNames[item.seedIndex] || '请选择商品' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>

        <view class="item-quantity">
          <view class="qty-btn" @click="decreaseQty(index)">-</view>
          <input class="qty-input" type="number" v-model="item.quantity" placeholder="数量" />
          <view class="qty-btn" @click="increaseQty(index)">+</view>
          <text class="qty-unit">公斤</text>
        </view>

        <view class="item-price">
          <text class="price-label">单价</text>
          <input class="price-input" type="digit" v-model="item.price" placeholder="0.00" />
          <text class="price-unit">元</text>
        </view>

        <view class="item-subtotal" v-if="item.quantity && item.price">
          小计: ¥{{ (Number(item.quantity) * Number(item.price)).toFixed(2) }}
        </view>
      </view>
    </view>

    <view class="summary-section" v-if="form.items.length > 0">
      <view class="summary-row">
        <text class="summary-label">订单金额</text>
        <text class="summary-value">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
    </view>

    <view class="submit-section">
      <view class="submit-btn" @click="handleSubmit">
        <text class="submit-text">确认提交订单</text>
      </view>
    </view>
  </view>
</template>

<script>
import { createOrder } from '@/api/order'
import { getCustomerList } from '@/api/order'
import { getInventoryList } from '@/api/inventory'

export default {
  data() {
    return {
      customerList: [],
      customerNames: ['请选择客户'],
      customerIndex: 0,
      inventoryList: [],
      seedNames: ['请选择商品'],
      form: {
        customerId: null,
        remark: '',
        items: []
      }
    }
  },

  computed: {
    totalAmount() {
      return this.form.items.reduce((sum, item) => {
        return sum + (Number(item.quantity) || 0) * (Number(item.price) || 0)
      }, 0)
    }
  },

  onLoad() {
    this.loadCustomers()
    this.loadInventory()
  },

  methods: {
    // 加载客户列表
    async loadCustomers() {
      try {
        const res = await getCustomerList()
        this.customerList = res.content || []
        this.customerNames = ['请选择客户', ...this.customerList.map(c => c.name)]
      } catch (e) {
        console.error('加载客户列表失败', e)
      }
    },

    // 加载库存列表
    async loadInventory() {
      try {
        const params = { page: 0, size: 100 }
        const res = await getInventoryList(params)
        this.inventoryList = res.content || []
        this.seedNames = ['请选择商品', ...this.inventoryList.map(i => `${i.seedName} (${i.batchNumber})`)]
      } catch (e) {
        console.error('加载库存失败', e)
      }
    },

    // 客户选择
    onCustomerChange(e) {
      this.customerIndex = e.detail.value
      if (this.customerIndex > 0) {
        this.form.customerId = this.customerList[this.customerIndex - 1].id
      } else {
        this.form.customerId = null
      }
    },

    // 添加商品项
    addItem() {
      this.form.items.push({
        inventoryId: null,
        seedIndex: 0,
        quantity: '',
        price: ''
      })
    },

    // 删除商品项
    removeItem(index) {
      this.form.items.splice(index, 1)
    },

    // 商品选择
    onSeedChange(index, e) {
      const seedIndex = e.detail.value
      this.form.items[index].seedIndex = seedIndex
      if (seedIndex > 0) {
        this.form.items[index].inventoryId = this.inventoryList[seedIndex - 1].id
      } else {
        this.form.items[index].inventoryId = null
      }
    },

    // 减少数量
    decreaseQty(index) {
      let qty = Number(this.form.items[index].quantity) || 0
      if (qty > 0) {
        this.form.items[index].quantity = String(qty - 1)
      }
    },

    // 增加数量
    increaseQty(index) {
      let qty = Number(this.form.items[index].quantity) || 0
      this.form.items[index].quantity = String(qty + 1)
    },

    // 提交表单
    async handleSubmit() {
      if (!this.form.customerId) {
        uni.showToast({ title: '请选择客户', icon: 'none' })
        return
      }

      if (this.form.items.length === 0) {
        uni.showToast({ title: '请添加商品', icon: 'none' })
        return
      }

      for (const item of this.form.items) {
        if (!item.inventoryId) {
          uni.showToast({ title: '请选择商品', icon: 'none' })
          return
        }
        if (!item.quantity || Number(item.quantity) <= 0) {
          uni.showToast({ title: '请输入正确的数量', icon: 'none' })
          return
        }
        if (!item.price || Number(item.price) <= 0) {
          uni.showToast({ title: '请输入正确的单价', icon: 'none' })
          return
        }
      }

      uni.showLoading({ title: '提交中...', mask: true })

      try {
        const data = {
          customerId: this.form.customerId,
          remark: this.form.remark,
          items: this.form.items.map(item => ({
            inventoryId: item.inventoryId,
            quantity: Number(item.quantity),
            price: Number(item.price)
          }))
        }

        await createOrder(data)

        uni.hideLoading()
        uni.showToast({ title: '订单创建成功', icon: 'success' })

        setTimeout(() => {
          uni.navigateBack()
        }, 1000)
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  padding-bottom: 200rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.form-card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 40rpx;
  text-align: center;
}

.form-item {
  margin-bottom: 36rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 16rpx;
}

.required {
  color: #FF3B30;
}

.picker-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 88rpx;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 0 24rpx;
}

.picker-value {
  font-size: 28rpx;
  color: #333333;
}

.picker-value.placeholder {
  color: #999999;
}

.picker-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 24rpx;
  font-size: 28rpx;
  color: #333333;
  box-sizing: border-box;
}

.items-section {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.add-item-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #007AFF;
  font-size: 26rpx;
}

.add-icon {
  font-size: 32rpx;
}

.item-card {
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.item-card:last-child {
  margin-bottom: 0;
}

.item-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.item-label {
  font-size: 28rpx;
  font-weight: 500;
  color: #333333;
}

.remove-btn {
  font-size: 26rpx;
  color: #FF3B30;
}

.item-quantity {
  display: flex;
  align-items: center;
  margin-top: 20rpx;
  gap: 20rpx;
}

.qty-btn {
  width: 60rpx;
  height: 60rpx;
  background: #FFFFFF;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: #007AFF;
}

.qty-input {
  flex: 1;
  height: 60rpx;
  background: #FFFFFF;
  border-radius: 8rpx;
  text-align: center;
  font-size: 28rpx;
}

.qty-unit {
  font-size: 26rpx;
  color: #999999;
}

.item-price {
  display: flex;
  align-items: center;
  margin-top: 20rpx;
  gap: 20rpx;
}

.price-label {
  font-size: 26rpx;
  color: #999999;
}

.price-input {
  flex: 1;
  height: 60rpx;
  background: #FFFFFF;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.price-unit {
  font-size: 26rpx;
  color: #999999;
}

.item-subtotal {
  margin-top: 20rpx;
  font-size: 26rpx;
  color: #FF3B30;
  font-weight: 500;
  text-align: right;
}

.summary-section {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-label {
  font-size: 30rpx;
  color: #333333;
}

.summary-value {
  font-size: 40rpx;
  font-weight: 600;
  color: #FF3B30;
}

.submit-section {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 30rpx;
  background: #FFFFFF;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 16rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.submit-text {
  font-size: 32rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
