<template>
  <view class="container">
    <view class="location-bar">
      <view class="location-info" @click="getLocation">
        <text class="location-icon">📍</text>
        <text class="location-text">{{ locationText }}</text>
      </view>
      <button class="refresh-btn" @click="loadNearbyProducts">刷新</button>
    </view>

    <view class="distance-filter">
      <text class="filter-label">距离范围：</text>
      <picker :range="distanceOptions" @change="onDistanceChange">
        <view class="filter-value">{{ selectedDistance }}km以内</view>
      </picker>
    </view>

    <view class="product-list" v-if="products.length > 0">
      <view class="product-item" v-for="product in products" :key="product.id" @click="goToDetail(product.id)">
        <image :src="product.images ? product.images.split(',')[0] : '/static/placeholder.png'" class="product-image" mode="aspectFill" />
        <view class="product-info">
          <text class="product-title">{{ product.title }}</text>
          <view class="product-price">
            <text class="price-symbol">¥</text>
            <text class="price-value">{{ product.price }}</text>
          </view>
          <view class="product-distance" v-if="product.distance !== undefined">
            <text>🚶 {{ product.distance }}km</text>
          </view>
          <view class="product-address" v-if="product.address">
            <text>{{ product.address }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📦</text>
      <text class="empty-text">附近暂无商品</text>
      <text class="empty-hint">点击上方定位按钮试试</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      locationText: '点击定位',
      latitude: null,
      longitude: null,
      selectedDistance: 5,
      distanceOptions: [1, 3, 5, 10, 20],
      products: []
    }
  },
  onLoad() {
    this.getLocation()
  },
  onShow() {
    if (this.latitude && this.longitude) {
      this.loadNearbyProducts()
    }
  },
  methods: {
    getLocation() {
      uni.getLocation({
        type: 'gcj02',
        success: (res) => {
          this.latitude = res.latitude
          this.longitude = res.longitude
          this.locationText = '已定位'
          uni.showToast({
            title: '定位成功',
            icon: 'success'
          })
          this.loadNearbyProducts()
        },
        fail: () => {
          uni.showToast({
            title: '定位失败',
            icon: 'none'
          })
        }
      })
    },
    onDistanceChange(e) {
      this.selectedDistance = this.distanceOptions[e.detail.value]
      this.loadNearbyProducts()
    },
    async loadNearbyProducts() {
      if (!this.latitude || !this.longitude) {
        uni.showToast({
          title: '请先定位',
          icon: 'none'
        })
        return
      }

      uni.showLoading({
        title: '加载中...'
      })

      try {
        const res = await this.$request({
          url: '/product/nearby',
          method: 'GET',
          data: {
            lat: this.latitude,
            lon: this.longitude,
            maxDistance: this.selectedDistance
          }
        })
        this.products = res.data
      } catch (e) {
        console.error(e)
      } finally {
        uni.hideLoading()
      }
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
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 30rpx;
}

.location-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  background: #fff;
}

.location-info {
  display: flex;
  align-items: center;
}

.location-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.location-text {
  font-size: 28rpx;
  color: #333;
}

.refresh-btn {
  width: 120rpx;
  height: 60rpx;
  line-height: 60rpx;
  background: #409eff;
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
}

.distance-filter {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #fff;
  margin-top: 1px;
}

.filter-label {
  font-size: 26rpx;
  color: #666;
}

.filter-value {
  font-size: 26rpx;
  color: #3cc51f;
  margin-left: 10rpx;
  padding: 8rpx 16rpx;
  background: #f0f9eb;
  border-radius: 20rpx;
}

.product-list {
  padding: 20rpx;
}

.product-item {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  padding: 20rpx;
}

.product-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-title {
  font-size: 28rpx;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.product-price {
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 24rpx;
  color: #ff4d4f;
}

.price-value {
  font-size: 36rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.product-distance {
  font-size: 24rpx;
  color: #67c23a;
}

.product-address {
  font-size: 22rpx;
  color: #999;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
}

.empty-state {
  padding: 150rpx 0;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 32rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.empty-hint {
  display: block;
  font-size: 26rpx;
  color: #999;
}
</style>
