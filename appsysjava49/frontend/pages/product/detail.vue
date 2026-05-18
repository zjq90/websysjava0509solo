<template>
  <view class="product-detail">
    <scroll-view scroll-y class="content">
      <swiper class="image-swiper" :indicator-dots="true" :autoplay="true" :interval="3000" :duration="500">
        <swiper-item v-for="(img, index) in images" :key="index">
          <image :src="img || '/static/default-product.jpg'" mode="aspectFill" class="product-image" />
        </swiper-item>
      </swiper>

      <view class="info-section">
        <view class="price-row">
          <text class="price-symbol">¥</text>
          <text class="price-value">{{ product.price }}</text>
          <text v-if="product.originalPrice" class="original-price">¥{{ product.originalPrice }}</text>
        </view>
        <text class="product-title">{{ product.title }}</text>
        <view class="meta-row">
          <text class="condition-tag">{{ product.condition }}</text>
          <text class="view-count">浏览 {{ product.viewCount }}</text>
        </view>
      </view>

      <view class="seller-section" @click="goToSeller">
        <image :src="seller.avatar || '/static/default-avatar.png'" class="seller-avatar" mode="aspectFill" />
        <view class="seller-info">
          <text class="seller-name">{{ seller.nickname }}</text>
          <view class="credit-stars">
            <text v-for="i in 5" :key="i" class="star" :class="{ active: i <= Math.round(seller.creditScore) }">★</text>
            <text class="score">{{ seller.creditScore }}分</text>
          </view>
        </view>
        <text class="arrow">›</text>
      </view>

      <view class="desc-section">
        <text class="section-title">商品描述</text>
        <text class="description">{{ product.description }}</text>
      </view>

      <view class="location-section">
        <text class="location-icon">📍</text>
        <text class="location-text">{{ product.location }}</text>
      </view>
    </scroll-view>

    <view class="bottom-bar">
      <view class="action-btn contact-btn" @click="contactSeller">
        <text class="btn-icon">💬</text>
        <text class="btn-text">联系卖家</text>
      </view>
      <view class="action-btn buy-btn" @click="buyNow">
        <text class="btn-text">立即购买</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      productId: null,
      product: {
        id: null,
        title: '',
        price: 0,
        originalPrice: 0,
        description: '',
        condition: '',
        location: '',
        viewCount: 0,
        userId: null
      },
      seller: {
        id: null,
        nickname: '',
        avatar: '',
        creditScore: 4.5
      },
      images: []
    }
  },
  onLoad(options) {
    this.productId = options.id
    this.loadProductDetail()
  },
  methods: {
    async loadProductDetail() {
      try {
        const res = await request.get(`/product/${this.productId}`)
        this.product = res.data
        if (this.product.images) {
          this.images = this.product.images.split(',')
        }
        this.loadSellerInfo()
      } catch (e) {
        this.loadMockData()
      }
    },
    async loadSellerInfo() {
      this.seller = {
        id: this.product.userId,
        nickname: '卖家昵称',
        avatar: '',
        creditScore: 4.5
      }
    },
    loadMockData() {
      this.product = {
        id: this.productId,
        title: 'iPhone 13 Pro 256G 石墨色 99新 无磕碰 电池健康92%',
        price: 4599,
        originalPrice: 8999,
        description: '自用手机，保护很好，无磕碰，电池健康92%，所有功能正常。配件齐全，带原装充电器、耳机。',
        condition: '99新',
        location: '北京·朝阳',
        viewCount: 328,
        userId: 1
      }
      this.images = ['/static/default-product.jpg', '/static/default-product.jpg']
      this.seller = {
        id: 1,
        nickname: '数码达人',
        avatar: '',
        creditScore: 4.8
      }
    },
    goToSeller() {
      uni.showToast({
        title: '查看卖家主页',
        icon: 'none'
      })
    },
    contactSeller() {
      uni.showToast({
        title: '消息功能开发中',
        icon: 'none'
      })
    },
    buyNow() {
      uni.showModal({
        title: '确认购买',
        content: `确认以¥${this.product.price}购买该商品？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '订单创建成功',
              icon: 'success'
            })
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/pay/index?id=' + this.productId
              })
            }, 1000)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.product-detail {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.content {
  height: calc(100vh - 120rpx);
}

.image-swiper {
  width: 100%;
  height: 500rpx;
  background: #fff;
}

.product-image {
  width: 100%;
  height: 100%;
}

.info-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.price-row {
  display: flex;
  align-items: baseline;
  margin-bottom: 20rpx;
}

.price-symbol {
  font-size: 28rpx;
  color: #F56C6C;
  font-weight: bold;
}

.price-value {
  font-size: 48rpx;
  color: #F56C6C;
  font-weight: bold;
  margin-right: 20rpx;
}

.original-price {
  font-size: 26rpx;
  color: #999;
  text-decoration: line-through;
}

.product-title {
  font-size: 32rpx;
  color: #333;
  line-height: 1.5;
  display: block;
  margin-bottom: 20rpx;
}

.meta-row {
  display: flex;
  align-items: center;
}

.condition-tag {
  background: #ecf5ff;
  color: #409EFF;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  margin-right: 20rpx;
}

.view-count {
  font-size: 24rpx;
  color: #999;
}

.seller-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  display: flex;
  align-items: center;
}

.seller-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  background: #f5f5f5;
}

.seller-info {
  flex: 1;
}

.seller-name {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.credit-stars {
  display: flex;
  align-items: center;
}

.star {
  font-size: 24rpx;
  color: #ddd;
  margin-right: 4rpx;
}

.star.active {
  color: #FFD700;
}

.score {
  font-size: 22rpx;
  color: #999;
  margin-left: 8rpx;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.desc-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 20rpx;
}

.description {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
}

.location-section {
  background: #fff;
  padding: 30rpx;
  display: flex;
  align-items: center;
}

.location-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.location-text {
  font-size: 26rpx;
  color: #666;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120rpx;
  background: #fff;
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.action-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.contact-btn {
  background: #f5f5f5;
  margin-right: 20rpx;
}

.contact-btn .btn-icon {
  font-size: 32rpx;
  margin-right: 8rpx;
}

.contact-btn .btn-text {
  font-size: 28rpx;
  color: #333;
}

.buy-btn {
  background: linear-gradient(135deg, #409EFF, #67C23A);
}

.buy-btn .btn-text {
  font-size: 30rpx;
  color: #fff;
  font-weight: bold;
}
</style>
