<template>
  <view class="coupon-page">
    <view class="tabs">
      <text 
        class="tab-item" 
        :class="{active: currentTab === tab.value}" 
        v-for="tab in tabs" 
        :key="tab.value"
        @click="currentTab = tab.value"
      >{{ tab.label }}</text>
    </view>

    <view class="coupon-list">
      <view class="coupon-card" v-for="coupon in filteredCoupons" :key="coupon.id">
        <view class="coupon-left">
          <text class="coupon-amount">¥{{ coupon.value }}</text>
          <text class="coupon-condition">满{{ coupon.minAmount }}可用</text>
        </view>
        <view class="coupon-right">
          <text class="coupon-name">{{ coupon.name }}</text>
          <text class="coupon-desc">{{ coupon.desc }}</text>
          <text class="coupon-expire">有效期至 {{ coupon.expireTime }}</text>
          <button 
            class="use-btn" 
            :class="{used: coupon.status === 'used', expired: coupon.status === 'expired'}"
            @click="useCoupon(coupon)"
          >{{ getBtnText(coupon.status) }}</button>
        </view>
      </view>

      <view class="empty" v-if="filteredCoupons.length === 0">
        <text class="empty-icon">🎫</text>
        <text class="empty-text">暂无优惠券</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentTab: 'available',
      tabs: [
        { label: '可使用', value: 'available' },
        { label: '已使用', value: 'used' },
        { label: '已过期', value: 'expired' }
      ],
      coupons: [
        { id: 1, name: '新人专享券', value: 20, minAmount: 100, desc: '全场通用', expireTime: '2024-12-31', status: 'available' },
        { id: 2, name: '满减优惠券', value: 50, minAmount: 200, desc: '全场通用', expireTime: '2024-12-31', status: 'available' },
        { id: 3, name: '情人节特惠券', value: 30, minAmount: 150, desc: '鲜花类专用', expireTime: '2024-02-15', status: 'used' },
        { id: 4, name: '母亲节优惠券', value: 15, minAmount: 80, desc: '康乃馨专用', expireTime: '2024-05-12', status: 'expired' }
      ]
    }
  },
  computed: {
    filteredCoupons() {
      return this.coupons.filter(c => c.status === this.currentTab)
    }
  },
  methods: {
    getBtnText(status) {
      const map = {
        'available': '立即使用',
        'used': '已使用',
        'expired': '已过期'
      }
      return map[status]
    },
    useCoupon(coupon) {
      if (coupon.status !== 'available') {
        return
      }
      uni.showToast({ title: '优惠券已选择', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/product/list' })
      }, 1000)
    }
  }
}
</script>

<style scoped>
.coupon-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.tabs {
  display: flex;
  background: #fff;
  padding: 0 10rpx;
  position: sticky;
  top: 0;
  z-index: 10;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  font-size: 28rpx;
  color: #666;
  position: relative;
}

.tab-item.active {
  color: #ff6b6b;
  font-weight: bold;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 50rpx;
  height: 4rpx;
  background: #ff6b6b;
  border-radius: 2rpx;
}

.coupon-list {
  padding: 20rpx;
}

.coupon-card {
  display: flex;
  background: linear-gradient(135deg, #fff5f5 0%, #fff 100%);
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 8rpx rgba(0,0,0,0.05);
}

.coupon-left {
  width: 200rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff8e8e 100%);
  padding: 30rpx 20rpx;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
}

.coupon-left::after {
  content: '';
  position: absolute;
  right: -10rpx;
  top: 50%;
  transform: translateY(-50%);
  width: 20rpx;
  height: 20rpx;
  background: #f5f5f5;
  border-radius: 50%;
}

.coupon-amount {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  line-height: 1;
}

.coupon-condition {
  display: block;
  font-size: 22rpx;
  color: rgba(255,255,255,0.9);
  margin-top: 8rpx;
}

.coupon-right {
  flex: 1;
  padding: 25rpx;
  display: flex;
  flex-direction: column;
}

.coupon-name {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.coupon-desc {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.coupon-expire {
  display: block;
  font-size: 22rpx;
  color: #999;
  margin-bottom: 15rpx;
}

.use-btn {
  align-self: flex-end;
  background: #ff6b6b;
  color: #fff;
  border: none;
  border-radius: 30rpx;
  padding: 10rpx 30rpx;
  font-size: 24rpx;
}

.use-btn.used, .use-btn.expired {
  background: #ccc;
}

.empty {
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
</style>