<template>
  <view class="container">
    <view class="search-bar">
      <text class="search-icon">🔍</text>
      <input 
        class="search-input" 
        v-model="keyword" 
        placeholder="搜索鲜花..."
        @input="onSearchInput"
        @confirm="doSearch"
      />
      <text class="clear-btn" v-if="keyword" @click="clearKeyword">✕</text>
      <text class="search-btn" @click="doSearch">搜索</text>
    </view>
    
    <view class="history-section" v-if="searchHistory.length > 0 && !showResults">
      <view class="section-header">
        <text class="section-title">搜索历史</text>
        <text class="clear-history" @click="clearHistory">清空</text>
      </view>
      <view class="history-tags">
        <text 
          class="history-tag" 
          v-for="(item, index) in searchHistory" 
          :key="index"
          @click="searchByHistory(item)"
        >{{ item }}</text>
      </view>
    </view>
    
    <view class="suggestion-section" v-if="suggestions.length > 0 && !showResults">
      <view class="section-header">
        <text class="section-title">搜索建议</text>
      </view>
      <view class="suggestion-list">
        <view 
          class="suggestion-item" 
          v-for="(item, index) in suggestions" 
          :key="index"
          @click="searchByHistory(item)"
        >
          <text class="suggestion-icon">🔍</text>
          <text class="suggestion-text">{{ item }}</text>
        </view>
      </view>
    </view>
    
    <view class="hot-section" v-if="!showResults">
      <view class="section-header">
        <text class="section-title">🔥 热门搜索</text>
      </view>
      <view class="hot-tags">
        <text 
          class="hot-tag" 
          v-for="(item, index) in hotKeywords" 
          :key="index"
          @click="searchByHistory(item)"
        >{{ item }}</text>
      </view>
    </view>
    
    <view class="results-section" v-if="showResults">
      <view class="results-header">
        <text class="results-count">找到 {{ searchResults.length }} 件商品</text>
      </view>
      <view class="product-grid" v-if="searchResults.length > 0">
        <view class="product-item" v-for="product in searchResults" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.image" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-price">¥{{ (product.price / 100).toFixed(2) }}</text>
          </view>
        </view>
      </view>
      <view class="empty-results" v-else>
        <text class="empty-icon">😕</text>
        <text class="empty-text">没有找到相关商品</text>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      keyword: '',
      searchHistory: ['玫瑰', '百合', '情人节花束', '康乃馨'],
      hotKeywords: ['红玫瑰', '向日葵', '满天星', '永生花', '混搭花束', '生日鲜花', '求婚花束', '感恩花束'],
      suggestions: [],
      searchResults: [],
      showResults: false
    }
  },
  methods: {
    onSearchInput() {
      if (this.keyword.length > 0) {
        this.getSuggestions()
      } else {
        this.suggestions = []
        this.showResults = false
      }
    },
    
    async getSuggestions() {
      try {
        const res = await api.getSearchSuggestions(this.keyword)
        this.suggestions = res.data || []
      } catch (e) {
        console.error('获取搜索建议失败', e)
        this.suggestions = []
      }
    },
    
    async doSearch() {
      if (!this.keyword.trim()) {
        uni.showToast({ title: '请输入搜索关键词', icon: 'none' })
        return
      }
      
      if (!this.searchHistory.includes(this.keyword)) {
        this.searchHistory.unshift(this.keyword)
        if (this.searchHistory.length > 10) {
          this.searchHistory.pop()
        }
      }
      
      try {
        const res = await api.searchProducts(this.keyword)
        this.searchResults = res.data || []
        if (this.searchResults.length === 0) {
          this.loadMockResults()
        }
      } catch (e) {
        console.error('搜索失败', e)
        this.loadMockResults()
      }
      
      this.showResults = true
      this.suggestions = []
    },
    
    loadMockResults() {
      const products = []
      for (let i = 1; i <= 6; i++) {
        products.push({
          id: i,
          name: `${this.keyword} - ${['红玫瑰浪漫花束', '粉色爱恋花束', '百合百年好合', '康乃馨感恩花束', '向日葵阳光花束', '满天星思念花束'][i-1]}`,
          price: (9900 + i * 2000),
          image: `https://picsum.photos/400/400?random=${i + 50}`
        })
      }
      this.searchResults = products
    },
    
    searchByHistory(keyword) {
      this.keyword = keyword
      this.doSearch()
    },
    
    clearKeyword() {
      this.keyword = ''
      this.showResults = false
      this.suggestions = []
    },
    
    clearHistory() {
      uni.showModal({
        title: '提示',
        content: '确定清空搜索历史吗？',
        success: (res) => {
          if (res.confirm) {
            this.searchHistory = []
          }
        }
      })
    },
    
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.search-bar {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 40rpx;
  padding: 15rpx 20rpx;
  margin-bottom: 30rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 15rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.clear-btn {
  font-size: 28rpx;
  color: #999;
  padding: 10rpx;
  margin-right: 10rpx;
}

.search-btn {
  font-size: 28rpx;
  color: #FF6B9D;
  font-weight: bold;
  padding: 10rpx 20rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.clear-history {
  font-size: 24rpx;
  color: #999;
}

.history-section,
.suggestion-section,
.hot-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.history-tags,
.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.history-tag,
.hot-tag {
  background: #f5f5f5;
  color: #666;
  font-size: 26rpx;
  padding: 15rpx 25rpx;
  border-radius: 30rpx;
}

.suggestion-list {
  display: flex;
  flex-direction: column;
}

.suggestion-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-icon {
  font-size: 28rpx;
  color: #999;
  margin-right: 15rpx;
}

.suggestion-text {
  font-size: 28rpx;
  color: #333;
  flex: 1;
}

.results-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.results-header {
  margin-bottom: 30rpx;
}

.results-count {
  font-size: 26rpx;
  color: #999;
}

.product-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.product-item {
  width: 320rpx;
  margin-bottom: 30rpx;
}

.product-image {
  width: 320rpx;
  height: 320rpx;
  border-radius: 12rpx;
  margin-bottom: 15rpx;
}

.product-info {
  padding: 0 10rpx;
}

.product-name {
  font-size: 26rpx;
  color: #333;
  display: block;
  margin-bottom: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  font-size: 30rpx;
  color: #FF4D4F;
  font-weight: bold;
}

.empty-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>