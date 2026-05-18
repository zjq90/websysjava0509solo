<template>
  <view class="container">
    <view class="header">
      <text class="header-title">优惠券中心</text>
      <text class="header-desc">积分兑换，好礼等你来</text>
    </view>

    <view class="points-card">
      <view class="points-info">
        <text class="points-label">当前积分</text>
        <text class="points-value">{{ userPoints }}</text>
      </view>
      <button class="points-btn" @click="goPoints">去赚积分</button>
    </view>

    <view class="section">
      <view class="section-title">可兑换优惠券</view>
      <view class="coupon-list">
        <view class="coupon-item" v-for="coupon in couponList" :key="coupon.id">
          <view class="coupon-left">
            <text class="coupon-value">¥{{ coupon.value }}</text>
            <text class="coupon-condition">满{{ coupon.min }}可用</text>
          </view>
          <view class="coupon-right">
            <text class="coupon-name">{{ coupon.name }}</text>
            <text class="coupon-expire">{{ coupon.expire }}</text>
            <button class="exchange-btn" @click="exchangeCoupon(coupon)" :disabled="userPoints < coupon.points">
              {{ userPoints >= coupon.points ? coupon.points + '积分兑换' : '积分不足' }}
            </button>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-title">我的优惠券</view>
      <view class="my-coupon-list" v-if="myCoupons.length > 0">
        <view class="my-coupon-item" v-for="coupon in myCoupons" :key="coupon.id">
          <view class="coupon-left">
            <text class="coupon-value">¥{{ coupon.value }}</text>
            <text class="coupon-condition">满{{ coupon.min }}可用</text>
          </view>
          <view class="coupon-right">
            <text class="coupon-name">{{ coupon.name }}</text>
            <text class="coupon-expire">{{ coupon.expire }}</text>
            <text class="coupon-status" :class="coupon.status">{{ coupon.status === 'unused' ? '立即使用' : '已使用' }}</text>
          </view>
        </view>
      </view>
      <view class="empty-state" v-else>
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
      userPoints: 500,
      couponList: [
        {
          id: 1,
          name: '新人专享券',
          value: 20,
          min: 100,
          points: 100,
          expire: '领取后7天有效'
        },
        {
          id: 2,
          name: '满减优惠券',
          value: 50,
          min: 500,
          points: 200,
          expire: '领取后7天有效'
        },
        {
          id: 3,
          name: '大额满减券',
          value: 100,
          min: 1000,
          points: 300,
          expire: '领取后7天有效'
        }
      ],
      myCoupons: []
    }
  },
  onShow() {
    const user = uni.getStorageSync('user')
    if (user && user.points !== undefined) {
      this.userPoints = user.points
    }
  },
  methods: {
    goPoints() {
      uni.navigateTo({
        url: '/pages/user/points'
      })
    },
    exchangeCoupon(coupon) {
      uni.showModal({
        title: '兑换确认',
        content: `确定使用${coupon.points}积分兑换${coupon.name}吗？`,
        success: (res) => {
          if (res.confirm) {
            if (this.userPoints >= coupon.points) {
              this.userPoints -= coupon.points
              this.myCoupons.unshift({
                ...coupon,
                status: 'unused'
              })
              
              const user = uni.getStorageSync('user')
              if (user) {
                user.points = this.userPoints
                uni.setStorageSync('user', user)
              }
              
              uni.showToast({
                title: '兑换成功',
                icon: 'success'
              })
            }
          }
        }
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

.header {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  padding: 60rpx 30rpx 80rpx;
  text-align: center;
}

.header-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.header-desc {
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
}

.points-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  margin: -40rpx 30rpx 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
  box-shadow: 0 8rpx 20rpx rgba(0,0,0,0.1);
}

.points-info {
  display: flex;
  flex-direction: column;
}

.points-label {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.points-value {
  font-size: 48rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.points-btn {
  width: 180rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
  border-radius: 35rpx;
  font-size: 26rpx;
  border: none;
}

.section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}

.coupon-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.coupon-item {
  display: flex;
  background: linear-gradient(135deg, #fff5f5 0%, #fff 100%);
  border-radius: 12rpx;
  overflow: hidden;
  border: 2rpx solid #ffccc7;
}

.coupon-left {
  width: 200rpx;
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.coupon-value {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.coupon-condition {
  font-size: 20rpx;
  color: rgba(255,255,255,0.8);
  margin-top: 8rpx;
}

.coupon-right {
  flex: 1;
  padding: 20rpx 20rpx 20rpx 30rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 8rpx;
}

.coupon-expire {
  font-size: 22rpx;
  color: #999;
  margin-bottom: 15rpx;
}

.exchange-btn {
  width: 160rpx;
  height: 50rpx;
  line-height: 50rpx;
  background: #ff4d4f;
  color: #fff;
  border-radius: 25rpx;
  font-size: 22rpx;
  border: none;
  align-self: flex-end;
}

.exchange-btn[disabled] {
  background: #ccc;
}

.my-coupon-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.my-coupon-item {
  display: flex;
  background: linear-gradient(135deg, #f6ffed 0%, #fff 100%);
  border-radius: 12rpx;
  overflow: hidden;
  border: 2rpx solid #b7eb8f;
}

.my-coupon-item .coupon-left {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
}

.coupon-status {
  font-size: 22rpx;
  color: #52c41a;
  text-align: right;
}

.coupon-status.used {
  color: #999;
}

.empty-state {
  padding: 80rpx 0;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
