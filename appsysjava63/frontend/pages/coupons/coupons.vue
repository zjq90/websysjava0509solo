<template>
  <view class="coupons-container">
    <view class="tabs-header">
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'available' }"
        @click="activeTab = 'available'"
      >可使用 ({{ availableCount }})</text>
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'used' }"
        @click="activeTab = 'used'"
      >已使用</text>
      <text 
        class="tab-item" 
        :class="{ active: activeTab === 'expired' }"
        @click="activeTab = 'expired'"
      >已过期</text>
    </view>

    <view class="coupon-list">
      <view 
        class="coupon-item" 
        v-for="coupon in filteredCoupons" 
        :key="coupon.id"
        :class="{ disabled: activeTab !== 'available' }"
      >
        <view class="coupon-left">
          <text class="coupon-amount">¥{{ coupon.amount }}</text>
          <text class="coupon-condition" v-if="coupon.minAmount">满{{ coupon.minAmount }}可用</text>
          <text class="coupon-condition" v-else>无门槛</text>
        </view>
        <view class="coupon-right">
          <text class="coupon-name">{{ coupon.name }}</text>
          <text class="coupon-type">{{ coupon.type }}</text>
          <text class="coupon-expire">{{ coupon.expireDate }}</text>
          <button 
            class="use-btn" 
            v-if="activeTab === 'available'"
            @click="useCoupon(coupon)"
          >立即使用</button>
          <text class="status-text" v-else-if="activeTab === 'used'">已使用</text>
          <text class="status-text" v-else>已过期</text>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="filteredCoupons.length === 0">
      <text class="empty-icon">🎫</text>
      <text class="empty-text">暂无优惠券</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const activeTab = ref('available')
const availableCount = ref(3)

const coupons = ref([
  {
    id: 1,
    name: '骑行优惠券',
    type: '全车型通用',
    amount: '5',
    minAmount: '10',
    expireDate: '2024-02-28到期',
    status: 'available'
  },
  {
    id: 2,
    name: '新人专享券',
    type: '电动车专享',
    amount: '3',
    minAmount: '0',
    expireDate: '2024-01-31到期',
    status: 'available'
  },
  {
    id: 3,
    name: '故障上报奖励',
    type: '全车型通用',
    amount: '2',
    minAmount: '0',
    expireDate: '2024-03-15到期',
    status: 'available'
  },
  {
    id: 4,
    name: '周末骑行券',
    type: '普通单车专享',
    amount: '1',
    minAmount: '0',
    expireDate: '2024-01-01到期',
    status: 'used'
  },
  {
    id: 5,
    name: '节日优惠券',
    type: '全车型通用',
    amount: '10',
    minAmount: '20',
    expireDate: '2023-12-31到期',
    status: 'expired'
  }
])

const filteredCoupons = computed(() => {
  return coupons.value.filter(c => c.status === activeTab.value)
})

const useCoupon = (coupon) => {
  uni.showToast({ title: '去使用优惠券', icon: 'none' })
  uni.switchTab({ url: '/pages/index/index' })
}
</script>

<style lang="scss" scoped>
.coupons-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.tabs-header {
  display: flex;
  background: white;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.tab-item {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #666;
  padding: 8px 0;
  position: relative;

  &.active {
    color: #00A862;
    font-weight: bold;

    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 40px;
      height: 2px;
      background: #00A862;
      border-radius: 1px;
    }
  }
}

.coupon-list {
  padding: 12px;
}

.coupon-item {
  background: white;
  border-radius: 12px;
  display: flex;
  overflow: hidden;
  margin-bottom: 12px;
  position: relative;

  &.disabled {
    opacity: 0.6;
  }
}

.coupon-left {
  width: 120px;
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  padding: 20px 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;

  &::before, &::after {
    content: '';
    position: absolute;
    right: 0;
    width: 12px;
    height: 12px;
    background: #f5f5f5;
    border-radius: 50%;
  }

  &::before {
    top: -6px;
  }

  &::after {
    bottom: -6px;
  }
}

.coupon-amount {
  font-size: 32px;
  font-weight: bold;
  color: white;
  line-height: 1;
}

.coupon-condition {
  font-size: 12px;
  color: rgba(255,255,255,0.8);
  margin-top: 4px;
}

.coupon-right {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.coupon-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.coupon-type {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.coupon-expire {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.use-btn {
  background: #00A862;
  color: white;
  border: none;
  border-radius: 16px;
  padding: 6px 16px;
  font-size: 12px;
  margin-top: 12px;
  align-self: flex-start;
}

.status-text {
  font-size: 12px;
  color: #999;
  margin-top: 12px;
}

.empty-state {
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 64px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 14px;
  color: #999;
}
</style>
