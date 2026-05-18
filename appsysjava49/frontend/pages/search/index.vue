<template>
  <view class="search-page">
    <view class="search-header">
      <view class="search-input-wrapper">
        <text class="search-icon">🔍</text>
        <input
          v-model="keyword"
          class="search-input"
          placeholder="搜索二手商品"
          focus
          @input="onSearchInput"
          @confirm="doSearch"
        />
        <text v-if="keyword" class="clear-icon" @click="clearKeyword">✕</text>
      </view>
      <text class="cancel-btn" @click="goBack">取消</text>
    </view>

    <view class="suggestions" v-if="showSuggestions && suggestions.length">
      <view class="suggestion-title">搜索建议</view>
      <view
        v-for="(item, index) in suggestions"
        :key="index"
        class="suggestion-item"
        @click="selectSuggestion(item)"
      >
        <text class="suggestion-text">{{ item }}</text>
      </view>
    </view>

    <view class="search-results" v-if="!showSuggestions">
      <view class="filter-bar">
        <view
          class="filter-item"
          :class="{ active: currentSort === 'default' }"
          @click="changeSort('default')"
        >
          <text>综合</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentSort === 'hot' }"
          @click="changeSort('hot')"
        >
          <text>热度</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentSort === 'price-asc' }"
          @click="changeSort('price-asc')"
        >
          <text>价格↑</text>
        </view>
        <view
          class="filter-item"
          :class="{ active: currentSort === 'price-desc' }"
          @click="changeSort('price-desc')"
        >
          <text>价格↓</text>
        </view>
      </view>

      <view class="product-list" v-if="products.length">
        <view
          v-for="product in products"
          :key="product.id"
          class="product-card"
          @click="goToDetail(product.id)"
        >
          <image
            :src="product.coverImage || '/static/default-product.jpg'"
            class="product-image"
            mode="aspectFill"
          />
          <view class="product-info">
            <text class="product-title">{{ product.title }}</text>
            <view class="product-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ product.price }}</text>
            </view>
            <view class="product-meta">
              <text class="product-condition">{{ product.condition }}</text>
              <text class="product-location">{{ product.location }}</text>
            </view>
          </view>
        </view>
      </view>

      <view class="empty-state" v-else-if="searched">
        <text class="empty-icon">🔍</text>
        <text class="empty-text">没有找到相关商品</text>
      </view>

      <view class="hot-search" v-else>
        <text class="hot-title">热门搜索</text>
        <view class="hot-tags">
          <text
            v-for="(tag, index) in hotTags"
            :key="index"
            class="hot-tag"
            @click="selectTag(tag)"
          >
            {{ tag }}
          </text>
        </view>

        <text class="hot-title">历史搜索</text>
        <view class="history-tags" v-if="historyTags.length">
          <text
            v-for="(tag, index) in historyTags"
            :key="index"
            class="history-tag"
            @click="selectTag(tag)"
          >
            {{ tag }}
          </text>
        </view>
        <view class="empty-history" v-else>
          <text class="empty-text">暂无搜索历史</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      keyword: '',
      showSuggestions: false,
      suggestions: [],
      products: [],
      currentSort: 'default',
      searched: false,
      hotTags: ['iPhone 13', 'MacBook Pro', '华为Mate40', '小米12', 'AirPods Pro', 'Nike球鞋'],
      historyTags: []
    }
  },
  onLoad() {
    this.loadHistory()
  },
  methods: {
    onSearchInput() {
      if (this.keyword.length > 0) {
        this.showSuggestions = true
        this.loadSuggestions()
      } else {
        this.showSuggestions = false
        this.suggestions = []
      }
    },
    loadSuggestions() {
      this.suggestions = [
        this.keyword + '手机',
        this.keyword + '电脑',
        this.keyword + '配件'
      ]
    },
    doSearch() {
      if (!this.keyword.trim()) {
        return
      }
      this.showSuggestions = false
      this.searched = true
      this.addToHistory(this.keyword)
      this.loadMockProducts()
    },
    loadMockProducts() {
      this.products = []
      for (let i = 1; i <= 6; i++) {
        this.products.push({
          id: i,
          title: this.keyword + '相关商品 ' + i,
          price: Math.floor(Math.random() * 5000 + 100),
          condition: '95新',
          location: '北京·朝阳',
          coverImage: ''
        })
      }
    },
    clearKeyword() {
      this.keyword = ''
      this.showSuggestions = false
      this.searched = false
      this.products = []
    },
    selectSuggestion(suggestion) {
      this.keyword = suggestion
      this.doSearch()
    },
    selectTag(tag) {
      this.keyword = tag
      this.doSearch()
    },
    changeSort(sort) {
      this.currentSort = sort
      if (this.searched) {
        this.doSearch()
      }
    },
    loadHistory() {
      const history = uni.getStorageSync('searchHistory')
      if (history) {
        this.historyTags = JSON.parse(history)
      }
    },
    addToHistory(keyword) {
      if (!this.historyTags.includes(keyword)) {
        this.historyTags.unshift(keyword)
        if (this.historyTags.length > 10) {
          this.historyTags.pop()
        }
        uni.setStorageSync('searchHistory', JSON.stringify(this.historyTags))
      }
    },
    goToDetail(id) {
      uni.navigateTo({
        url: '/pages/product/detail?id=' + id
      })
    },
    goBack() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.search-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.search-header {
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
}

.search-input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: #f5f5f5;
  border-radius: 50rpx;
  padding: 16rpx 24rpx;
  margin-right: 20rpx;
}

.search-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.clear-icon {
  font-size: 28rpx;
  color: #999;
  padding: 8rpx;
}

.cancel-btn {
  font-size: 28rpx;
  color: #409EFF;
}

.suggestions {
  background: #fff;
}

.suggestion-title {
  font-size: 24rpx;
  color: #999;
  padding: 20rpx 30rpx;
  display: block;
}

.suggestion-item {
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.suggestion-text {
  font-size: 28rpx;
  color: #333;
}

.search-results {
  padding: 20rpx;
}

.filter-bar {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.filter-item {
  flex: 1;
  text-align: center;
  font-size: 28rpx;
  color: #666;
}

.filter-item.active {
  color: #409EFF;
  font-weight: bold;
}

.product-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.product-card {
  width: calc(50% - 10rpx);
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 280rpx;
}

.product-info {
  padding: 20rpx;
}

.product-title {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  margin-bottom: 12rpx;
}

.product-price {
  display: flex;
  align-items: baseline;
  margin-bottom: 12rpx;
}

.price-symbol {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 36rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.product-meta {
  display: flex;
  justify-content: space-between;
}

.product-condition {
  font-size: 22rpx;
  color: #52c41a;
  background: #f6ffed;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.product-location {
  font-size: 22rpx;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  display: block;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.hot-search {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.hot-title {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 20rpx;
}

.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-bottom: 40rpx;
}

.hot-tag {
  background: #f5f5f5;
  color: #666;
  font-size: 26rpx;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
}

.history-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.history-tag {
  background: #e6f7ff;
  color: #1890ff;
  font-size: 26rpx;
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
}

.empty-history {
  text-align: center;
  padding: 40rpx 0;
}
</style>
