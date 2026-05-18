<template>
  <view class="container">
    <swiper class="product-swiper" indicator-dots circular>
      <swiper-item v-for="(img, index) in productImages" :key="index">
        <image :src="img || 'https://picsum.photos/800/800?random=' + index" mode="aspectFill" class="product-image"></image>
      </swiper-item>
    </swiper>
    
    <view class="product-info">
      <view class="price-row">
        <text class="price">¥{{ (product.price / 100).toFixed(2) }}</text>
        <text class="original-price">¥{{ (product.originalPrice / 100).toFixed(2) }}</text>
        <text class="sales" v-if="product.sales">已售{{ product.sales }}件</text>
      </view>
      <text class="product-name">{{ product.name }}</text>
      <text class="product-subtitle">{{ product.subtitle }}</text>
    </view>
    
    <view class="section">
      <view class="section-title">💐 花材信息</view>
      <view class="info-row">
        <text class="info-label">主花材</text>
        <text class="info-value">{{ product.flowerMaterial || '玫瑰、百合等' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">花语</text>
        <text class="info-value">{{ product.flowerLanguage || '浪漫爱情、美好祝福' }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">适用场景</text>
        <text class="info-value">{{ product.applicableScene || '情人节、生日、纪念日' }}</text>
      </view>
    </view>
    
    <view class="section">
      <view class="section-title">🌱 养护指南</view>
      <view class="care-guide">
        <text class="care-text">{{ product.careGuide || '1. 收到鲜花后，立即剪根2-3cm，45度斜剪\n2. 插入清水中，水位约10-15cm\n3. 每天换水一次，保持水质清洁\n4. 避免阳光直射和空调出风口\n5. 室内温度保持18-25度最佳' }}</text>
      </view>
    </view>
    
    <view class="section">
      <view class="section-title">⭐ 用户评价 ({{ reviewCount }})</view>
      <view class="review-item" v-for="(review, index) in reviews" :key="index">
        <view class="review-header">
          <view class="review-user">
            <text class="avatar">{{ review.avatar || '👤' }}</text>
            <text class="username">{{ review.username || '匿名用户' }}</text>
          </view>
          <view class="rating">
            <text v-for="i in 5" :key="i" class="star">{{ i <= review.rating ? '⭐' : '☆' }}</text>
          </view>
        </view>
        <text class="review-content">{{ review.content }}</text>
        <text class="review-time">{{ review.createTime || '2024-01-01' }}</text>
      </view>
    </view>
    
    <view class="bottom-bar">
      <view class="bar-item" @click="goToCart">
        <text class="bar-icon">🛒</text>
        <text class="bar-text">购物车</text>
      </view>
      <view class="bar-item" @click="addToCart">
        <text class="bar-icon">➕</text>
        <text class="bar-text">加入购物车</text>
      </view>
      <view class="bar-item buy-now" @click="buyNow">
        <text class="bar-text">立即购买</text>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      productId: null,
      product: {
        id: 1,
        name: '红玫瑰浪漫花束',
        subtitle: '11朵红玫瑰，搭配满天星',
        price: 9900,
        originalPrice: 12800,
        sales: 520,
        flowerMaterial: '红玫瑰11枝、满天星适量',
        flowerLanguage: '热恋、真爱、美好祝福',
        applicableScene: '情人节、生日、纪念日、求婚',
        careGuide: '1. 收到鲜花后，立即剪根2-3cm，45度斜剪\n2. 插入清水中，水位约10-15cm\n3. 每天换水一次，保持水质清洁\n4. 避免阳光直射和空调出风口\n5. 室内温度保持18-25度最佳'
      },
      productImages: [
        'https://picsum.photos/800/800?random=1',
        'https://picsum.photos/800/800?random=2',
        'https://picsum.photos/800/800?random=3'
      ],
      reviews: [
        { username: '花仙子', rating: 5, content: '鲜花很新鲜，配送很快，女朋友很喜欢！', createTime: '2024-01-15' },
        { username: '爱花人士', rating: 5, content: '包装精美，花材质量很好，下次还会回购', createTime: '2024-01-14' },
        { username: '浪漫满屋', rating: 4, content: '整体不错，就是包装可以再精致一点', createTime: '2024-01-13' }
      ],
      reviewCount: 128
    }
  },
  onLoad(options) {
    this.productId = options.id
    this.loadProductDetail()
  },
  methods: {
    async loadProductDetail() {
      try {
        const res = await api.getProductDetail(this.productId)
        this.product = res.data.product || this.product
        this.reviewCount = res.data.reviewCount || this.reviewCount
        if (res.data.images && res.data.images.length > 0) {
          this.productImages = res.data.images.map(img => img.imageUrl)
        }
        if (res.data.reviews && res.data.reviews.length > 0) {
          this.reviews = res.data.reviews
        }
      } catch (e) {
        console.error('加载商品详情失败', e)
      }
    },
    
    goToCart() {
      uni.switchTab({ url: '/pages/cart/cart' })
    },
    
    addToCart() {
      uni.showToast({ title: '已加入购物车', icon: 'success' })
    },
    
    buyNow() {
      uni.showToast({ title: '立即购买功能开发中', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.container {
  padding-bottom: 120rpx;
  background: #f5f5f5;
}

.product-swiper {
  height: 600rpx;
  background: #fff;
}

.product-image {
  width: 100%;
  height: 100%;
}

.product-info {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.price-row {
  display: flex;
  align-items: baseline;
  margin-bottom: 20rpx;
}

.price {
  font-size: 48rpx;
  font-weight: bold;
  color: #FF4D4F;
  margin-right: 20rpx;
}

.original-price {
  font-size: 28rpx;
  color: #999;
  text-decoration: line-through;
  margin-right: 20rpx;
}

.sales {
  font-size: 24rpx;
  color: #999;
}

.product-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 15rpx;
}

.product-subtitle {
  font-size: 26rpx;
  color: #666;
}

.section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 30rpx;
}

.info-row {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  width: 160rpx;
  font-size: 28rpx;
  color: #999;
}

.info-value {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.care-guide {
  background: #FFFBE6;
  padding: 30rpx;
  border-radius: 12rpx;
}

.care-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
  white-space: pre-wrap;
}

.review-item {
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.review-user {
  display: flex;
  align-items: center;
}

.avatar {
  font-size: 36rpx;
  margin-right: 15rpx;
}

.username {
  font-size: 26rpx;
  color: #333;
}

.rating {
  font-size: 24rpx;
}

.star {
  margin-right: 5rpx;
}

.review-content {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
  display: block;
  margin-bottom: 10rpx;
}

.review-time {
  font-size: 24rpx;
  color: #999;
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
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.05);
}

.bar-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.bar-icon {
  font-size: 36rpx;
  margin-bottom: 5rpx;
}

.bar-text {
  font-size: 22rpx;
  color: #666;
}

.bar-item.buy-now {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
}

.bar-item.buy-now .bar-text {
  color: #fff;
  font-size: 30rpx;
  font-weight: bold;
}
</style>