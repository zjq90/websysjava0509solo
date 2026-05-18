<template>
  <view class="container">
    <view class="search-bar" @click="goToSearch">
      <text class="search-icon">🔍</text>
      <text class="search-placeholder">搜索鲜花...</text>
    </view>
    
    <swiper class="banner" indicator-dots autoplay circular interval="3000" duration="500">
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image :src="item.image" mode="aspectFill" class="banner-image"></image>
      </swiper-item>
    </swiper>
    
    <view class="quick-entry">
      <view class="entry-item" @click="goToDiy">
        <view class="entry-icon">💐</view>
        <text class="entry-text">DIY花束</text>
      </view>
      <view class="entry-item" @click="goToEnterprise">
        <view class="entry-icon">🏢</view>
        <text class="entry-text">企业定制</text>
      </view>
      <view class="entry-item" @click="goToMember">
        <view class="entry-icon">👑</view>
        <text class="entry-text">会员中心</text>
      </view>
      <view class="entry-item" @click="goToPoints">
        <view class="entry-icon">⭐</view>
        <text class="entry-text">积分商城</text>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">🔥 热门推荐</text>
        <text class="section-more" @click="goToCategory">查看更多 ></text>
      </view>
      <view class="product-grid">
        <view class="product-item" v-for="product in hotProducts" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-subtitle">{{ product.subtitle }}</text>
            <view class="product-price">
              <text class="price">¥{{ (product.price / 100).toFixed(2) }}</text>
              <text class="original-price">¥{{ (product.originalPrice / 100).toFixed(2) }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">✨ 新品上市</text>
        <text class="section-more" @click="goToCategory">查看更多 ></text>
      </view>
      <scroll-view class="product-scroll" scroll-x>
        <view class="product-scroll-item" v-for="product in newProducts" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.mainImage" mode="aspectFill" class="product-scroll-image"></image>
          <text class="product-scroll-name">{{ product.name }}</text>
          <text class="product-scroll-price">¥{{ (product.price / 100).toFixed(2) }}</text>
        </view>
      </scroll-view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <text class="section-title">💝 为你推荐</text>
      </view>
      <view class="product-grid">
        <view class="product-item" v-for="product in recommendProducts" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.mainImage" mode="aspectFill" class="product-image"></image>
          <view class="product-info">
            <text class="product-name">{{ product.name }}</text>
            <text class="product-subtitle">{{ product.subtitle }}</text>
            <view class="product-price">
              <text class="price">¥{{ (product.price / 100).toFixed(2) }}</text>
              <text class="original-price">¥{{ (product.originalPrice / 100).toFixed(2) }}</text>
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
      banners: [
        { image: 'https://picsum.photos/750/300?random=101' },
        { image: 'https://picsum.photos/750/300?random=102' },
        { image: 'https://picsum.photos/750/300?random=103' }
      ],
      hotProducts: [],
      newProducts: [],
      recommendProducts: []
    }
  },
  onLoad() {
    this.loadHomeData()
  },
  onPullDownRefresh() {
    this.loadHomeData()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    async loadHomeData() {
      try {
        const res = await api.getHomeData()
        this.hotProducts = res.data.hotProducts || []
        this.newProducts = res.data.newProducts || []
        this.recommendProducts = res.data.recommendProducts || []
      } catch (e) {
        console.error('加载首页数据失败', e)
        this.loadMockData()
      }
    },
    
    loadMockData() {
      const products = []
      for (let i = 1; i <= 8; i++) {
        products.push({
          id: i,
          name: ['红玫瑰浪漫花束', '粉色爱恋', '白色纯洁花束', '百合百年好合', 
                 '康乃馨感恩花束', '向日葵阳光花束', '郁金香高贵花束', '满天星思念花束'][i-1],
          subtitle: '精选优质鲜花，新鲜配送',
          mainImage: `https://picsum.photos/400/400?random=${i}`,
          price: (9900 + i * 2000),
          originalPrice: (12800 + i * 2000)
        })
      }
      this.hotProducts = products.slice(0, 4)
      this.newProducts = products.slice(0, 6)
      this.recommendProducts = products
    },
    
    goToSearch() {
      uni.navigateTo({ url: '/pages/search/search' })
    },
    
    goToCategory() {
      uni.switchTab({ url: '/pages/category/category' })
    },
    
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/product/detail?id=${id}` })
    },
    
    goToDiy() {
      uni.navigateTo({ url: '/pages/diy/diy' })
    },
    
    goToEnterprise() {
      uni.navigateTo({ url: '/pages/enterprise/enterprise' })
    },
    
    goToMember() {
      uni.navigateTo({ url: '/pages/member/member' })
    },
    
    goToPoints() {
      uni.navigateTo({ url: '/pages/points/points' })
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

.banner {
  height: 300rpx;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.quick-entry {
  display: flex;
  justify-content: space-around;
  padding: 30rpx 20rpx;
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
}

.entry-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.entry-icon {
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.entry-text {
  font-size: 24rpx;
  color: #333;
}

.section {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 24rpx;
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
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-subtitle {
  font-size: 22rpx;
  color: #999;
  display: block;
  margin-bottom: 10rpx;
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

.original-price {
  font-size: 22rpx;
  color: #999;
  text-decoration: line-through;
  margin-left: 10rpx;
}

.product-scroll {
  white-space: nowrap;
}

.product-scroll-item {
  display: inline-block;
  width: 200rpx;
  margin-right: 20rpx;
}

.product-scroll-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  margin-bottom: 10rpx;
}

.product-scroll-name {
  font-size: 24rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-scroll-price {
  font-size: 28rpx;
  color: #FF4D4F;
  font-weight: bold;
}
</style>