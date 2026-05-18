<template>
  <view class="container">
    <view class="search-bar" @click="goToSearch">
      <text class="search-icon">🔍</text>
      <text class="search-placeholder">搜索鲜花...</text>
    </view>
    
    <view class="tab-bar">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'scene' }"
        @click="switchTab('scene')"
      >
        <text>场景分类</text>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'material' }"
        @click="switchTab('material')"
      >
        <text>花材分类</text>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'price' }"
        @click="switchTab('price')"
      >
        <text>价格区间</text>
      </view>
    </view>
    
    <view class="category-list">
      <view 
        class="category-item" 
        v-for="category in categories" 
        :key="category.id"
        @click="selectCategory(category.id)"
      >
        <view class="category-icon-wrapper">
          <text class="category-icon">{{ getCategoryIcon(category.name) }}</text>
        </view>
        <view class="category-info">
          <text class="category-name">{{ category.name }}</text>
          <text class="category-desc">{{ category.description }}</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>
    
    <view class="products-section" v-if="selectedCategoryId">
      <view class="section-title">相关商品</view>
      <view class="product-grid">
        <view class="product-item" v-for="product in products" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <view class="product-price">
              <text class="price">¥{{ (product.price / 100).toFixed(2) }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      currentTab: 'scene',
      categories: [],
      selectedCategoryId: null,
      products: []
    }
  },
  onLoad() {
    this.loadCategories()
  },
  methods: {
    async loadCategories() {
      try {
        const res = await api.getCategories(this.currentTab)
        this.categories = res.data || []
      } catch (e) {
        console.error('加载分类失败', e)
        this.loadMockCategories()
      }
    },
    
    loadMockCategories() {
      const sceneCategories = [
        { id: 1, name: '情人节专区', description: '浪漫情人节，为爱绽放' },
        { id: 2, name: '生日鲜花', description: '生日快乐，美好相伴' },
        { id: 3, name: '感恩致谢', description: '感恩的心，感谢有你' },
        { id: 4, name: '婚礼用花', description: '浪漫婚礼，永恒见证' },
        { id: 5, name: '探病慰问', description: '温馨祝福，早日康复' },
        { id: 6, name: '商务用花', description: '高端大气，彰显品位' }
      ]
      
      const materialCategories = [
        { id: 7, name: '玫瑰', description: '爱情的象征，浪漫首选' },
        { id: 8, name: '百合', description: '纯洁高雅，百年好合' },
        { id: 9, name: '康乃馨', description: '温馨感恩，母爱之花' },
        { id: 10, name: '向日葵', description: '阳光向上，充满活力' },
        { id: 11, name: '郁金香', description: '高贵优雅，荷兰国花' },
        { id: 12, name: '满天星', description: '点缀之美，不可或缺' }
      ]
      
      const priceCategories = [
        { id: 13, name: '0-100元', description: '实惠之选' },
        { id: 14, name: '100-200元', description: '品质之选' },
        { id: 15, name: '200-500元', description: '豪华之选' },
        { id: 16, name: '500元以上', description: '尊享定制' }
      ]
      
      if (this.currentTab === 'scene') {
        this.categories = sceneCategories
      } else if (this.currentTab === 'material') {
        this.categories = materialCategories
      } else {
        this.categories = priceCategories
      }
    },
    
    switchTab(tab) {
      this.currentTab = tab
      this.selectedCategoryId = null
      this.products = []
      this.loadCategories()
    },
    
    async selectCategory(categoryId) {
      this.selectedCategoryId = categoryId
      try {
        const res = await api.getProductsByCategory(categoryId)
        this.products = res.data || []
      } catch (e) {
        console.error('加载商品失败', e)
        this.loadMockProducts()
      }
    },
    
    loadMockProducts() {
      const products = []
      for (let i = 1; i <= 6; i++) {
        products.push({
          id: i,
          name: ['红玫瑰浪漫花束', '粉色爱恋', '白色纯洁花束', 
                 '百合百年好合', '康乃馨感恩花束', '向日葵阳光花束'][i-1],
          mainImage: `https://picsum.photos/400/400?random=${i + 20}`,
          price: (9900 + i * 2000)
        })
      }
      this.products = products
    },
    
    getCategoryIcon(name) {
      const icons = {
        '情人节专区': '💕',
        '生日鲜花': '🎂',
        '感恩致谢': '🙏',
        '婚礼用花': '💒',
        '探病慰问': '🌸',
        '商务用花': '💼',
        '玫瑰': '🌹',
        '百合': '💮',
        '康乃馨': '🌷',
        '向日葵': '🌻',
        '郁金香': '🌷',
        '满天星': '✨'
      }
      return icons[name] || '💐'
    },
    
    goToSearch() {
      uni.navigateTo({ url: '/pages/search/search' })
    },
    
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
    }
  }
}
</script>

<style scoped>
.container {
  padding-bottom: 20rpx;
}

.search-bar {
  display: flex;
  align-items: center;
  background: #F5F5F5;
  border-radius: 40rpx;
  padding: 20rpx 30rpx;
  margin: 20rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
}

.search-placeholder {
  color: #999;
  font-size: 28rpx;
}

.tab-bar {
  display: flex;
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 10rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 28rpx;
  color: #666;
  border-radius: 12rpx;
  transition: all 0.3s;
}

.tab-item.active {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #fff;
  font-weight: bold;
}

.category-list {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.category-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.category-item:last-child {
  border-bottom: none;
}

.category-icon-wrapper {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.category-icon {
  font-size: 36rpx;
}

.category-info {
  flex: 1;
}

.category-name {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 8rpx;
}

.category-desc {
  font-size: 24rpx;
  color: #999;
}

.arrow {
  font-size: 28rpx;
  color: #ccc;
}

.products-section {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
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
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-price {
  display: flex;
  align-items: center;
}

.price {
  font-size: 32rpx;
  color: #FF4D4F;
  font-weight: bold;
}
</style>