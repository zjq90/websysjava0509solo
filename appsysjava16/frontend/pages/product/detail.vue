<template>
  <view class="container" v-if="product">
    <view class="product-header">
      <view class="product-avatar">
        <text class="product-avatar-text">{{ product.name.charAt(0) }}</text>
      </view>
      <view class="product-basic">
        <text class="product-name">{{ product.name }}</text>
        <view class="product-meta">
          <text class="meta-tag">{{ product.category }}</text>
          <text class="meta-tag">{{ product.specification }}</text>
        </view>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">基本信息</view>
      <view class="info-row">
        <text class="label">批次编号</text>
        <text class="value">{{ product.batchNumber }}</text>
      </view>
      <view class="info-row">
        <text class="label">种子类别</text>
        <text class="value">{{ product.seedCategory }}</text>
      </view>
      <view class="info-row">
        <text class="label">规格</text>
        <text class="value">{{ product.specification }}</text>
      </view>
      <view class="info-row">
        <text class="label">保质期</text>
        <text class="value">{{ formatDate(product.expiryDate) }}</text>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">质量参数</view>
      <view class="stats-grid">
        <view class="stat-card">
          <text class="stat-value">{{ product.germinationRate }}%</text>
          <text class="stat-label">发芽率</text>
        </view>
        <view class="stat-card">
          <text class="stat-value">{{ product.purity }}%</text>
          <text class="stat-label">纯度</text>
        </view>
        <view class="stat-card">
          <text class="stat-value">{{ product.moisture }}%</text>
          <text class="stat-label">水分</text>
        </view>
        <view class="stat-card">
          <text class="stat-value">{{ product.stockQuantity }}</text>
          <text class="stat-label">库存</text>
        </view>
      </view>
    </view>

    <view class="info-card">
      <view class="card-title">价格信息</view>
      <view class="info-row">
        <text class="label">基准价格</text>
        <text class="value price">¥{{ product.basePrice }}</text>
      </view>
      <view class="info-row">
        <text class="label">批发价格</text>
        <text class="value">¥{{ product.wholesalePrice }}</text>
      </view>
    </view>

    <view class="info-card" v-if="product.notes">
      <view class="card-title">备注</view>
      <text class="notes-text">{{ product.notes }}</text>
    </view>
  </view>
  <view v-else class="empty">加载中...</view>
</template>

<script>
import productApi from '@/api/product.js'

export default {
  data() {
    return {
      productId: '',
      product: null
    }
  },
  onLoad(options) {
    this.productId = options.id
    this.loadProduct()
  },
  methods: {
    async loadProduct() {
      uni.showLoading({ title: '加载中...' })
      try {
        this.product = await productApi.getProductById(this.productId)
      } catch (e) {
        console.error('加载产品失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const y = d.getFullYear()
      const m = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${y}-${m}-${day}`
    }
  }
}
</script>

<style scoped>
.product-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  display: flex;
  align-items: center;
}

.product-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.product-avatar-text {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
}

.product-basic {
  flex: 1;
}

.product-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 12rpx;
}

.product-meta {
  display: flex;
  gap: 12rpx;
}

.meta-tag {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.2);
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.info-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.card-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 12rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 0;
}

.info-row .label {
  font-size: 28rpx;
  color: #666;
}

.info-row .value {
  font-size: 28rpx;
  color: #333;
}

.info-row .value.price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.stats-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.stat-card {
  width: calc(50% - 10rpx);
  background: #f5f7ff;
  border-radius: 12rpx;
  padding: 24rpx;
  text-align: center;
}

.stat-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #667eea;
  display: block;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
}

.notes-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}
</style>
