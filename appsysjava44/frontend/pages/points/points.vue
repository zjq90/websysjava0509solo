<template>
  <view class="points-page">
    <view class="points-header">
      <view class="points-info">
        <text class="points-label">我的积分</text>
        <text class="points-value">{{ userPoints }}</text>
      </view>
      <view class="points-actions">
        <button class="action-btn" @click="goToHistory">积分明细</button>
      </view>
    </view>
    
    <view class="tabs-section">
      <view class="tabs">
        <view class="tab-item" :class="{ active: activeTab === 'coupon' }" @click="activeTab = 'coupon'">
          <text class="tab-text">优惠券</text>
        </view>
        <view class="tab-item" :class="{ active: activeTab === 'gift' }" @click="activeTab = 'gift'">
          <text class="tab-text">礼品兑换</text>
        </view>
      </view>
    </view>
    
    <view class="list-section" v-if="activeTab === 'coupon'">
      <view class="coupon-item" v-for="coupon in couponList" :key="coupon.id">
        <view class="coupon-left">
          <text class="coupon-value">¥{{ coupon.value / 100 }}</text>
          <text class="coupon-condition">满{{ coupon.minAmount / 100 }}可用</text>
        </view>
        <view class="coupon-right">
          <text class="coupon-name">{{ coupon.name }}</text>
          <text class="coupon-desc">{{ coupon.description }}</text>
          <button class="exchange-btn" @click="exchangeCoupon(coupon)" :disabled="userPoints < coupon.points">
            {{ coupon.points }}积分兑换
          </button>
        </view>
      </view>
    </view>
    
    <view class="list-section" v-if="activeTab === 'gift'">
      <view class="gift-grid">
        <view class="gift-item" v-for="gift in giftList" :key="gift.id" @click="exchangeGift(gift)">
          <image :src="gift.image" class="gift-image"></image>
          <text class="gift-name">{{ gift.name }}</text>
          <text class="gift-points">{{ gift.points }}积分</text>
          <button class="gift-btn" :disabled="userPoints < gift.points">
            {{ userPoints >= gift.points ? '立即兑换' : '积分不足' }}
          </button>
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
      activeTab: 'coupon',
      userPoints: 520,
      couponList: [
        { id: 1, name: '满100减15元券', value: 1500, minAmount: 10000, points: 100, description: '全场通用，有效期30天' },
        { id: 2, name: '满200减35元券', value: 3500, minAmount: 20000, points: 200, description: '全场通用，有效期30天' },
        { id: 3, name: '满500减100元券', value: 10000, minAmount: 50000, points: 500, description: '全场通用，有效期30天' },
        { id: 4, name: '免运费券', value: 0, minAmount: 0, points: 50, description: '全场免运费，不限金额' }
      ],
      giftList: [
        { id: 101, name: '精美花瓶', points: 300, image: 'https://picsum.photos/200/200?random=201' },
        { id: 102, name: '永生花礼盒', points: 800, image: 'https://picsum.photos/200/200?random=202' },
        { id: 103, name: '香薰蜡烛', points: 200, image: 'https://picsum.photos/200/200?random=203' },
        { id: 104, name: '鲜花保鲜剂', points: 100, image: 'https://picsum.photos/200/200?random=204' },
        { id: 105, name: '定制贺卡', points: 50, image: 'https://picsum.photos/200/200?random=205' },
        { id: 106, name: 'VIP会员月卡', points: 1000, image: 'https://picsum.photos/200/200?random=206' }
      ]
    }
  },
  
  onLoad() {
    this.loadUserPoints()
  },
  
  methods: {
    async loadUserPoints() {
      try {
        const res = await api.getMemberInfo()
        if (res.code === 200 && res.data) {
          this.userPoints = res.data.currentPoints || 0
        }
      } catch (e) {
        console.error('获取积分失败', e)
      }
    },
    
    async exchangeCoupon(coupon) {
      if (this.userPoints < coupon.points) {
        uni.showToast({
          title: '积分不足',
          icon: 'none'
        })
        return
      }
      
      uni.showModal({
        title: '确认兑换',
        content: `确定使用${coupon.points}积分兑换${coupon.name}吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              const result = await api.exchangeCoupon(coupon.id)
              if (result.code === 200) {
                this.userPoints -= coupon.points
                uni.showToast({
                  title: '兑换成功',
                  icon: 'success'
                })
              }
            } catch (e) {
              this.userPoints -= coupon.points
              uni.showToast({
                title: '兑换成功（模拟）',
                icon: 'success'
              })
            }
          }
        }
      })
    },
    
    exchangeGift(gift) {
      if (this.userPoints < gift.points) {
        uni.showToast({
          title: '积分不足',
          icon: 'none'
        })
        return
      }
      
      uni.showModal({
        title: '确认兑换',
        content: `确定使用${gift.points}积分兑换${gift.name}吗？`,
        success: (res) => {
          if (res.confirm) {
            this.userPoints -= gift.points
            uni.showToast({
              title: '兑换成功',
              icon: 'success'
            })
          }
        }
      })
    },
    
    goToHistory() {
      uni.navigateTo({
        url: '/pages/member/member'
      })
    }
  }
}
</script>

<style scoped>
.points-page {
  min-height: 100vh;
  background-color: #F8F8F8;
}

.points-header {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  padding: 50rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.points-info {
  color: #FFFFFF;
}

.points-label {
  display: block;
  font-size: 24rpx;
  opacity: 0.9;
  margin-bottom: 10rpx;
}

.points-value {
  display: block;
  font-size: 56rpx;
  font-weight: bold;
}

.action-btn {
  background-color: rgba(255, 255, 255, 0.2);
  color: #FFFFFF;
  border: none;
  font-size: 24rpx;
  padding: 15rpx 30rpx;
  border-radius: 30rpx;
}

.tabs-section {
  background-color: #FFFFFF;
  padding: 0 30rpx;
}

.tabs {
  display: flex;
  border-bottom: 1rpx solid #F0F0F0;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  position: relative;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60rpx;
  height: 4rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  border-radius: 2rpx;
}

.tab-text {
  font-size: 28rpx;
  color: #666666;
}

.tab-item.active .tab-text {
  color: #FF6B9D;
  font-weight: bold;
}

.list-section {
  padding: 30rpx;
}

.coupon-item {
  display: flex;
  background-color: #FFFFFF;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.coupon-left {
  width: 200rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  padding: 30rpx 20rpx;
  text-align: center;
  color: #FFFFFF;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-value {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.coupon-condition {
  font-size: 20rpx;
  opacity: 0.9;
}

.coupon-right {
  flex: 1;
  padding: 25rpx;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.coupon-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10rpx;
}

.coupon-desc {
  font-size: 22rpx;
  color: #999999;
  margin-bottom: 15rpx;
}

.exchange-btn {
  align-self: flex-start;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #FFFFFF;
  border: none;
  font-size: 24rpx;
  padding: 10rpx 25rpx;
  border-radius: 30rpx;
}

.exchange-btn[disabled] {
  background: #CCCCCC;
}

.gift-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.gift-item {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 20rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.gift-image {
  width: 180rpx;
  height: 180rpx;
  border-radius: 12rpx;
  margin-bottom: 15rpx;
}

.gift-name {
  display: block;
  font-size: 26rpx;
  color: #333333;
  margin-bottom: 10rpx;
}

.gift-points {
  display: block;
  font-size: 24rpx;
  color: #FF6B9D;
  font-weight: bold;
  margin-bottom: 15rpx;
}

.gift-btn {
  width: 100%;
  height: 60rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #FFFFFF;
  border: none;
  font-size: 24rpx;
  border-radius: 30rpx;
}

.gift-btn[disabled] {
  background: #CCCCCC;
}
</style>
