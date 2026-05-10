<template>
  <view class="container">
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索产品名称..." 
        v-model="searchKeyword"
        @confirm="searchProducts"
      />
      <button class="search-btn" @click="searchProducts">搜索</button>
    </view>

    <view class="category-tags">
      <view 
        :class="['category-tag', currentCategory === '' ? 'active' : '']" 
        @click="filterByCategory('')"
      >全部</view>
      <view 
        :class="['category-tag', currentCategory === cat ? 'active' : '']" 
        v-for="cat in categories" 
        :key="cat"
        @click="filterByCategory(cat)"
      >{{ cat }}</view>
    </view>

    <view v-if="productList.length > 0">
      <view 
        class="product-card" 
        v-for="product in productList" 
        :key="product.id"
        @click="goToDetail(product.id)"
      >
        <view class="product-info">
          <view class="product-name">{{ product.name }}</view>
          <view class="product-meta">
            <text class="meta-item">批次: {{ product.batchNumber }}</text>
            <text class="meta-item">{{ product.specification }}</text>
          </view>
          <view class="product-stats">
            <view class="stat-item">
              <text class="stat-label">发芽率</text>
              <text class="stat-value">{{ product.germinationRate }}%</text>
            </view>
            <view class="stat-item">
              <text class="stat-label">库存</text>
              <text class="stat-value">{{ product.stockQuantity }}</text>
            </view>
          </view>
        </view>
        <view class="product-price">
          <text class="price-label">¥</text>
          <text class="price-value">{{ product.basePrice }}</text>
        </view>
      </view>
    </view>
    <view v-else class="empty">暂无产品数据</view>
  </view>
</template>

<script>
import productApi from '@/api/product.js'

export default {
  data() {
    return {
      searchKeyword: '',
      currentCategory: '',
      categories: ['玉米种子', '小麦种子', '水稻种子', '大豆种子', '棉花种子'],
      productList: [],
      allProducts: []
    }
  },
  onShow() {
    this.loadProducts()
  },
  methods: {
    async loadProducts() {
      uni.showLoading({ title: '加载中...' })
      try {
        const res = await productApi.getAllProducts()
        this.allProducts = res
        this.productList = res
      } catch (e) {
        console.error('加载产品失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async searchProducts() {
      if (!this.searchKeyword.trim()) {
        this.productList = this.allProducts
        return
      }
      uni.showLoading({ title: '搜索中...' })
      try {
        const res = await productApi.searchProducts(this.searchKeyword)
        this.productList = res
      } catch (e) {
        console.error('搜索失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    filterByCategory(category) {
      this.currentCategory = category
      if (category === '') {
        this.productList = this.allProducts
      } else {
        this.productList = this.allProducts.filter(p => p.category === category)
      }
    },
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
    }
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  margin-bottom: 20rpx;
  gap: 16rpx;
}

.search-input {
  flex: 1;
  height: 72rpx;
  background: #fff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  border: 2rpx solid #eee;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 36rpx;
  height: 72rpx;
  line-height: 72rpx;
  padding: 0 32rpx;
  font-size: 28rpx;
  border: none;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.category-tag {
  padding: 12rpx 24rpx;
  background: #fff;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
  border: 2rpx solid #eee;
}

.category-tag.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
}

.product-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 12rpx;
}

.product-meta {
  margin-bottom: 16rpx;
}

.meta-item {
  font-size: 24rpx;
  color: #999;
  margin-right: 20rpx;
}

.product-stats {
  display: flex;
  gap: 32rpx;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.stat-label {
  font-size: 22rpx;
  color: #999;
  margin-bottom: 4rpx;
}

.stat-value {
  font-size: 28rpx;
  font-weight: 500;
  color: #667eea;
}

.product-price {
  text-align: right;
}

.price-label {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #ff4d4f;
}
</style>
