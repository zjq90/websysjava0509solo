<template>
  <view class="container">
    <view class="search-bar">
      <input v-model="keyword" placeholder="搜索商品" class="search-input" />
      <button @click="search" class="search-btn">搜索</button>
    </view>

    <view class="banner">
      <swiper :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000">
        <swiper-item v-for="(item, index) in banners" :key="index">
          <image :src="item.image" class="banner-image" mode="aspectFill" />
        </swiper-item>
      </swiper>
    </view>

    <view class="nav-grid">
      <view class="nav-item" @click="goToPage('/pages/nearby/index')">
        <view class="nav-icon nearby"></view>
        <text>附近</text>
      </view>
      <view class="nav-item" @click="goToPage('/pages/groupbuy/index')">
        <view class="nav-icon groupbuy"></view>
        <text>拼团</text>
      </view>
      <view class="nav-item" @click="goToPage('/pages/bargain/index')">
        <view class="nav-icon bargain"></view>
        <text>砍价</text>
      </view>
      <view class="nav-item" @click="goToPage('/pages/coupon/index')">
        <view class="nav-icon coupon"></view>
        <text>优惠券</text>
      </view>
      <view class="nav-item" @click="goToPage('/pages/chat/index')">
        <view class="nav-icon chat"></view>
        <text>客服</text>
      </view>
      <view class="nav-item" @click="goToPublish">
        <view class="nav-icon publish"></view>
        <text>发布</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">热门商品</text>
        <text class="section-more" @click="loadMoreHot">更多</text>
      </view>
      <view class="product-list">
        <view class="product-item" v-for="product in hotProducts" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.images ? product.images.split(',')[0] : '/static/placeholder.png'" class="product-image" mode="aspectFill" />
          <view class="product-info">
            <text class="product-title">{{ product.title }}</text>
            <view class="product-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ product.price }}</text>
            </view>
            <view class="product-meta" v-if="product.distance !== undefined">
              <text>{{ product.distance }}km</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">最新上架</text>
        <text class="section-more" @click="loadMoreLatest">更多</text>
      </view>
      <view class="product-list">
        <view class="product-item" v-for="product in latestProducts" :key="product.id" @click="goToDetail(product.id)">
          <image :src="product.images ? product.images.split(',')[0] : '/static/placeholder.png'" class="product-image" mode="aspectFill" />
          <view class="product-info">
            <text class="product-title">{{ product.title }}</text>
            <view class="product-price">
              <text class="price-symbol">¥</text>
              <text class="price-value">{{ product.price }}</text>
            </view>
          </view>
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
      banners: [
        { image: 'https://via.placeholder.com/750x300?text=二手交易' },
        { image: 'https://via.placeholder.com/750x300?text=拼团优惠' },
        { image: 'https://via.placeholder.com/750x300?text=砍价活动' }
      ],
      hotProducts: [],
      latestProducts: []
    }
  },
  onLoad() {
    this.loadHotProducts()
    this.loadLatestProducts()
  },
  methods: {
    search() {
      uni.showToast({
        title: '搜索功能开发中',
        icon: 'none'
      })
    },
    goToPage(url) {
      uni.navigateTo({ url })
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/product/detail?id=${id}`
      })
    },
    goToPublish() {
      uni.navigateTo({
        url: '/pages/product/publish'
      })
    },
    async loadHotProducts() {
      try {
        const res = await this.$request({
          url: '/product/hot'
        })
        this.hotProducts = res.data
      } catch (e) {
        console.error(e)
      }
    },
    async loadLatestProducts() {
      try {
        const res = await this.$request({
          url: '/product/latest'
        })
        this.latestProducts = res.data
      } catch (e) {
        console.error(e)
      }
    },
    loadMoreHot() {
      uni.showToast({
        title: '加载更多开发中',
        icon: 'none'
      })
    },
    loadMoreLatest() {
      uni.showToast({
        title: '加载更多开发中',
        icon: 'none'
      })
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
  padding: 20rpx;
  background: #fff;
}

.search-input {
  flex: 1;
  height: 70rpx;
  padding: 0 20rpx;
  background: #f5f5f5;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.search-btn {
  margin-left: 20rpx;
  width: 120rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: #3cc51f;
  color: #fff;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.banner {
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 300rpx;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  padding: 20rpx;
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.nav-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  margin-bottom: 10rpx;
}

.nav-icon.nearby {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.nav-icon.groupbuy {
  background: linear-gradient(135deg, #ff6b6b 0%, #feca57 100%);
}

.nav-icon.bargain {
  background: linear-gradient(135deg, #a55eea 0%, #8854d0 100%);
}

.nav-icon.coupon {
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
}

.nav-icon.chat {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
}

.nav-icon.publish {
  background: linear-gradient(135deg, #fdcb6e 0%, #e17055 100%);
}

.section {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
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

.product-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.product-item {
  border-radius: 12rpx;
  overflow: hidden;
  background: #f9f9f9;
}

.product-image {
  width: 100%;
  height: 250rpx;
}

.product-info {
  padding: 15rpx;
}

.product-title {
  font-size: 26rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  line-height: 1.4;
}

.product-price {
  margin-top: 10rpx;
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.product-meta {
  margin-top: 8rpx;
  font-size: 22rpx;
  color: #999;
}
</style>
