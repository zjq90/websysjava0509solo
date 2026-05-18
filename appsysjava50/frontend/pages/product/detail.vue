<template>
  <view class="container">
    <swiper class="product-swiper" :indicator-dots="true">
      <swiper-item v-for="(img, index) in images" :key="index">
        <image :src="img" class="swiper-image" mode="aspectFill" />
      </swiper-item>
    </swiper>

    <view class="product-info">
      <view class="price-section">
        <text class="current-price">¥{{ product.price }}</text>
        <text class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice }}</text>
        <view class="distance" v-if="product.distance !== undefined">
          <text>{{ product.distance }}km</text>
        </view>
      </view>
      <text class="product-title">{{ product.title }}</text>
      <text class="product-desc">{{ product.description }}</text>
      
      <view class="seller-info" v-if="product.seller">
        <image :src="product.seller.avatar || '/static/user-avatar.png'" class="seller-avatar" />
        <view class="seller-detail">
          <text class="seller-name">{{ product.seller.nickname }}</text>
          <text class="seller-phone" v-if="product.seller.phone">{{ product.seller.phone }}</text>
        </view>
      </view>
    </view>

    <view class="address-section" v-if="product.address">
      <view class="section-title">交易地点</view>
      <view class="address-content">
        <text class="address-text">{{ product.address }}</text>
        <button class="nav-btn" @click="openMap">导航</button>
      </view>
    </view>

    <view class="bottom-bar">
      <button class="contact-btn" @click="contactSeller">联系卖家</button>
      <button class="buy-btn" @click="buyNow">立即购买</button>
    </view>

    <view class="custom-popup" v-if="showOrderPopup" @click.self="closeOrderPopup">
      <view class="order-popup">
        <view class="popup-header">
          <text class="popup-title">确认订单</text>
          <text class="popup-close" @click="closeOrderPopup">×</text>
        </view>
        <view class="popup-body">
          <view class="order-product">
            <image :src="images[0]" class="order-product-image" />
            <view class="order-product-info">
              <text class="order-product-title">{{ product.title }}</text>
              <text class="order-product-price">¥{{ product.price }}</text>
            </view>
          </view>
          <view class="address-input">
            <input v-model="receiverName" placeholder="收货人姓名" class="input" />
          </view>
          <view class="address-input">
            <input v-model="receiverPhone" placeholder="联系电话" class="input" />
          </view>
          <view class="address-input">
            <input v-model="receiverAddress" placeholder="收货地址" class="input" />
          </view>
        </view>
        <view class="popup-footer">
          <button class="submit-order-btn" @click="submitOrder">提交订单</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      productId: null,
      product: {},
      images: [],
      receiverName: '',
      receiverPhone: '',
      receiverAddress: '',
      showOrderPopup: false
    }
  },
  onLoad(options) {
    this.productId = options.id
    this.loadProductDetail()
  },
  methods: {
    async loadProductDetail() {
      try {
        const res = await this.$request({
          url: `/product/${this.productId}`
        })
        this.product = res.data
        this.images = res.data.images ? res.data.images.split(',') : ['/static/placeholder.png']
      } catch (e) {
        console.error(e)
      }
    },
    openMap() {
      if (this.product.latitude && this.product.longitude) {
        uni.openLocation({
          latitude: this.product.latitude,
          longitude: this.product.longitude,
          address: this.product.address,
          name: this.product.title
        })
      } else {
        uni.showToast({
          title: '位置信息不可用',
          icon: 'none'
        })
      }
    },
    contactSeller() {
      uni.navigateTo({
        url: '/pages/chat/index?toUserId=' + this.product.userId
      })
    },
    buyNow() {
      this.showOrderPopup = true
    },
    closeOrderPopup() {
      this.showOrderPopup = false
    },
    async submitOrder() {
      if (!this.receiverName || !this.receiverPhone) {
        uni.showToast({
          title: '请填写联系信息',
          icon: 'none'
        })
        return
      }

      try {
        const res = await this.$request({
          url: '/order/create',
          method: 'POST',
          data: {
            productId: this.productId,
            address: this.receiverAddress,
            receiver: this.receiverName,
            phone: this.receiverPhone
          }
        })

        uni.showToast({
          title: '订单创建成功',
          icon: 'success'
        })

        this.closeOrderPopup()
        setTimeout(() => {
          uni.navigateTo({
            url: `/pages/order/detail?orderNo=${res.data.orderNo}`
          })
        }, 1000)
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.container {
  padding-bottom: 140rpx;
}

.product-swiper {
  width: 100%;
  height: 600rpx;
}

.swiper-image {
  width: 100%;
  height: 100%;
}

.product-info {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.price-section {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.current-price {
  font-size: 48rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.original-price {
  margin-left: 20rpx;
  font-size: 28rpx;
  color: #999;
  text-decoration: line-through;
}

.distance {
  margin-left: auto;
  background: #f0f9eb;
  color: #67c23a;
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.product-title {
  font-size: 32rpx;
  color: #333;
  line-height: 1.5;
  margin-bottom: 15rpx;
}

.product-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.seller-info {
  display: flex;
  align-items: center;
  margin-top: 30rpx;
  padding-top: 30rpx;
  border-top: 1rpx solid #eee;
}

.seller-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
}

.seller-detail {
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
}

.seller-name {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.seller-phone {
  font-size: 24rpx;
  color: #999;
}

.address-section {
  background: #fff;
  padding: 30rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.address-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.address-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
}

.nav-btn {
  width: 120rpx;
  height: 60rpx;
  line-height: 60rpx;
  background: #409eff;
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
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
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.1);
}

.contact-btn {
  width: 160rpx;
  height: 80rpx;
  line-height: 80rpx;
  background: #f5f5f5;
  color: #333;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
  margin-right: 20rpx;
}

.buy-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 32rpx;
  border: none;
}

.custom-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.order-popup {
  width: 650rpx;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.popup-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.popup-close {
  font-size: 48rpx;
  color: #999;
  line-height: 1;
}

.popup-body {
  padding: 30rpx;
}

.order-product {
  display: flex;
  margin-bottom: 30rpx;
}

.order-product-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
}

.order-product-info {
  margin-left: 20rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.order-product-title {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
}

.order-product-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.address-input {
  margin-bottom: 20rpx;
}

.input {
  width: 100%;
  height: 70rpx;
  padding: 0 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.popup-footer {
  padding: 30rpx;
}

.submit-order-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 30rpx;
  border: none;
}
</style>
