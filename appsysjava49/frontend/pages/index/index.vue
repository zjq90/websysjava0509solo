<template>
  <view class="index-page">
    <view class="search-bar">
      <view class="search-input-wrapper" @click="goToSearch">
        <text class="search-icon">🔍</text>
        <input class="search-input" placeholder="搜索二手商品" disabled />
      </view>
    </view>

    <scroll-view class="category-scroll" scroll-x>
      <view class="category-list">
        <view 
          v-for="category in categories" 
          :key="category.id" 
          class="category-item"
          @click="selectCategory(category)"
        >
          <view class="category-icon">{{ category.icon }}</view>
          <text class="category-name">{{ category.name }}</text>
        </view>
      </view>
    </scroll-view>

    <view class="filter-bar">
      <view class="filter-item" :class="{ active: currentSort === 'time' }" @click="changeSort('time')">
        <text>最新</text>
      </view>
      <view class="filter-item" :class="{ active: currentSort === 'hot' }" @click="changeSort('hot')">
        <text>最热</text>
      </view>
      <view class="filter-item" :class="{ active: currentSort === 'price-asc' }" @click="changeSort('price-asc')">
        <text>价格↑</text>
      </view>
      <view class="filter-item" :class="{ active: currentSort === 'price-desc' }" @click="changeSort('price-desc')">
        <text>价格↓</text>
      </view>
      <view class="filter-btn" @click="showFilter = !showFilter">
        <text>筛选</text>
        <text>▼</text>
      </view>
    </view>

    <view class="filter-panel" v-if="showFilter">
      <view class="filter-section">
        <text class="filter-title">价格区间</text>
        <view class="price-inputs">
          <input v-model="minPrice" type="number" placeholder="最低价" class="price-input" />
          <text class="price-sep">-</text>
          <input v-model="maxPrice" type="number" placeholder="最高价" class="price-input" />
        </view>
      </view>
      <view class="filter-section">
        <text class="filter-title">成色</text>
        <view class="condition-list">
          <view 
            v-for="cond in conditions" 
            :key="cond" 
            class="condition-item"
            :class="{ active: selectedCondition === cond }"
            @click="selectedCondition = cond"
          >
            {{ cond }}
          </view>
        </view>
      </view>
      <view class="filter-actions">
        <button class="reset-btn" @click="resetFilter">重置</button>
        <button class="confirm-btn" @click="applyFilter">确定</button>
      </view>
    </view>

    <view class="publish-btn" @click="goToPublish">
      <text class="publish-icon">+</text>
      <text>发布</text>
    </view>

    <view class="product-list">
      <view 
        v-for="product in products" 
        :key="product.id" 
        class="product-card"
        @click="goToDetail(product.id)"
      >
        <image :src="product.coverImage || '/static/default-product.jpg'" class="product-image" mode="aspectFill" />
        <view class="product-info">
          <text class="product-title">{{ product.title }}</text>
          <view class="product-price">
            <text class="price-symbol">¥</text>
            <text class="price-value">{{ product.price }}</text>
            <text v-if="product.originalPrice" class="original-price">¥{{ product.originalPrice }}</text>
          </view>
          <view class="product-meta">
            <text class="product-condition">{{ product.condition }}</text>
            <text class="product-location">{{ product.location }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="load-more" v-if="hasMore" @click="loadMore">
      <text>加载更多</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      categories: [
        { id: 1, name: '手机', icon: '📱' },
        { id: 2, name: '电脑', icon: '💻' },
        { id: 3, name: '数码', icon: '📷' },
        { id: 4, name: '服饰', icon: '👕' },
        { id: 5, name: '家电', icon: '📺' },
        { id: 6, name: '图书', icon: '📚' },
        { id: 7, name: '运动', icon: '⚽' },
        { id: 8, name: '母婴', icon: '🍼' }
      ],
      products: [],
      currentSort: 'time',
      showFilter: false,
      minPrice: '',
      maxPrice: '',
      selectedCondition: '',
      conditions: ['全新', '99新', '95新', '9成新', '8成新', '7成新及以下'],
      pageNum: 1,
      pageSize: 20,
      hasMore: true
    }
  },
  onLoad() {
    this.loadProducts()
  },
  onPullDownRefresh() {
    this.refresh()
  },
  onReachBottom() {
    this.loadMore()
  },
  methods: {
    async loadProducts() {
      try {
        const sortBy = this.currentSort === 'hot' ? 'viewCount' : 
                       this.currentSort === 'price-asc' ? 'priceAsc' :
                       this.currentSort === 'price-desc' ? 'priceDesc' : ''
        
        const res = await request.get('/product/search', {
          keyword: '',
          minPrice: this.minPrice || null,
          maxPrice: this.maxPrice || null,
          condition: this.selectedCondition || null,
          sortBy,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        
        if (this.pageNum === 1) {
          this.products = res.data.records
        } else {
          this.products = [...this.products, ...res.data.records]
        }
        this.hasMore = this.products.length < res.data.total
      } catch (e) {
        console.error('加载商品失败', e)
        this.loadMockProducts()
      }
    },
    loadMockProducts() {
      const mockProducts = []
      for (let i = 1; i <= 10; i++) {
        mockProducts.push({
          id: i,
          title: `二手商品${i} - 成色好 价格低`,
          price: (Math.random() * 1000 + 50).toFixed(0),
          originalPrice: (Math.random() * 2000 + 100).toFixed(0),
          condition: this.conditions[Math.floor(Math.random() * this.conditions.length)],
          location: '北京·朝阳',
          coverImage: ''
        })
      }
      this.products = mockProducts
    },
    refresh() {
      this.pageNum = 1
      this.loadProducts()
      setTimeout(() => {
        uni.stopPullDownRefresh()
      }, 1000)
    },
    loadMore() {
      if (this.hasMore) {
        this.pageNum++
        this.loadProducts()
      }
    },
    changeSort(sort) {
      this.currentSort = sort
      this.refresh()
    },
    resetFilter() {
      this.minPrice = ''
      this.maxPrice = ''
      this.selectedCondition = ''
    },
    applyFilter() {
      this.showFilter = false
      this.refresh()
    },
    selectCategory(category) {
      uni.showToast({
        title: `选择了${category.name}`,
        icon: 'none'
      })
    },
    goToSearch() {
      uni.navigateTo({
        url: '/pages/search/index'
      })
    },
    goToPublish() {
      uni.navigateTo({
        url: '/pages/product/publish'
      })
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/product/detail?id=${id}`
      })
    }
  }
}
</script>

<style scoped>
.index-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.search-bar {
  background: #409EFF;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 50rpx;
  padding: 20rpx 30rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.category-scroll {
  background: #fff;
  padding: 30rpx 0;
  white-space: nowrap;
}

.category-list {
  display: flex;
  padding: 0 20rpx;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 30rpx;
  flex-shrink: 0;
}

.category-icon {
  font-size: 60rpx;
  margin-bottom: 12rpx;
}

.category-name {
  font-size: 24rpx;
  color: #333;
}

.filter-bar {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #eee;
}

.filter-item {
  padding: 10rpx 24rpx;
  margin-right: 20rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
}

.filter-item.active {
  background: #e6f7ff;
  color: #409EFF;
}

.filter-btn {
  margin-left: auto;
  display: flex;
  align-items: center;
  font-size: 26rpx;
  color: #666;
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
  background: #f5f5f5;
}

.filter-panel {
  background: #fff;
  padding: 30rpx;
  border-top: 1rpx solid #eee;
}

.filter-section {
  margin-bottom: 30rpx;
}

.filter-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
  display: block;
}

.price-inputs {
  display: flex;
  align-items: center;
}

.price-input {
  flex: 1;
  height: 70rpx;
  border: 1rpx solid #ddd;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
}

.price-sep {
  margin: 0 20rpx;
  color: #999;
}

.condition-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.condition-item {
  padding: 12rpx 24rpx;
  border: 1rpx solid #ddd;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
}

.condition-item.active {
  border-color: #409EFF;
  background: #e6f7ff;
  color: #409EFF;
}

.filter-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 30rpx;
}

.reset-btn, .confirm-btn {
  flex: 1;
  height: 70rpx;
  border-radius: 10rpx;
  font-size: 28rpx;
}

.reset-btn {
  background: #f5f5f5;
  color: #666;
  border: none;
}

.confirm-btn {
  background: #409EFF;
  color: #fff;
  border: none;
}

.publish-btn {
  position: fixed;
  right: 40rpx;
  bottom: 200rpx;
  width: 120rpx;
  height: 120rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24rpx;
  box-shadow: 0 8rpx 24rpx rgba(64, 158, 255, 0.4);
  z-index: 999;
}

.publish-icon {
  font-size: 40rpx;
  font-weight: bold;
  line-height: 1;
}

.product-list {
  padding: 20rpx;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20rpx;
}

.product-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.product-image {
  width: 100%;
  height: 280rpx;
  background: #f5f5f5;
}

.product-info {
  padding: 20rpx;
}

.product-title {
  font-size: 28rpx;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: 16rpx;
}

.product-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 12rpx;
}

.price-symbol {
  font-size: 24rpx;
  color: #F56C6C;
  font-weight: bold;
}

.price-value {
  font-size: 36rpx;
  color: #F56C6C;
  font-weight: bold;
  margin-right: 12rpx;
}

.original-price {
  font-size: 24rpx;
  color: #999;
  text-decoration: line-through;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  font-size: 22rpx;
  color: #999;
}

.load-more {
  text-align: center;
  padding: 40rpx;
  font-size: 26rpx;
  color: #999;
}
</style>
