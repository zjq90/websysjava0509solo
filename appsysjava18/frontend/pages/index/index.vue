<template>
  <view class="container">
    <view class="header">
      <view class="greeting">
        <text class="greeting-text">你好，{{ userStore.userInfo?.realName || '用户' }}</text>
        <text class="greeting-sub">{{ userStore.roleNames.join('、') }}</text>
      </view>
      <view class="avatar">
        <text class="avatar-text">{{ (userStore.userInfo?.realName || '用')[0] }}</text>
      </view>
    </view>

    <view class="stats-section">
      <view class="stats-card" v-if="userStore.canAccessInventory" @click="navigateToInventory">
        <view class="stats-icon" style="background: #007AFF1A;">📦</view>
        <view class="stats-info">
          <text class="stats-value">{{ reportData?.inventoryCount || 0 }}</text>
          <text class="stats-label">库存品种</text>
        </view>
      </view>
      <view class="stats-card" v-if="userStore.canAccessOrder" @click="navigateToOrder">
        <view class="stats-icon" style="background: #34C7591A;">📋</view>
        <view class="stats-info">
          <text class="stats-value">{{ reportData?.orderCount || 0 }}</text>
          <text class="stats-label">订单数量</text>
        </view>
      </view>
      <view class="stats-card" v-if="userStore.canAccessField" @click="navigateToField">
        <view class="stats-icon" style="background: #FF95001A;">🌾</view>
        <view class="stats-info">
          <text class="stats-value">{{ reportData?.fieldRecordCount || 0 }}</text>
          <text class="stats-label">田间记录</text>
        </view>
      </view>
      <view class="stats-card" @click="navigateToReport">
        <view class="stats-icon" style="background: #AF52DE1A;">📊</view>
        <view class="stats-info">
          <text class="stats-value">查看</text>
          <text class="stats-label">数据报表</text>
        </view>
      </view>
    </view>

    <view class="quick-actions">
      <view class="section-title">快捷功能</view>
      <view class="action-grid">
        <view class="action-item" v-if="userStore.canAccessInventory" @click="navigateTo('inventory')">
          <view class="action-icon" style="background: #007AFF;">📦</view>
          <text class="action-label">库存管理</text>
        </view>
        <view class="action-item" v-if="userStore.canAccessOrder" @click="navigateTo('order')">
          <view class="action-icon" style="background: #34C759;">📋</view>
          <text class="action-label">订单管理</text>
        </view>
        <view class="action-item" v-if="userStore.canAccessField" @click="navigateTo('field')">
          <view class="action-icon" style="background: #FF9500;">🌾</view>
          <text class="action-label">田间记录</text>
        </view>
        <view class="action-item" @click="navigateTo('report')">
          <view class="action-icon" style="background: #AF52DE;">📊</view>
          <text class="action-label">报表查看</text>
        </view>
      </view>
    </view>

    <view class="recent-activities" v-if="recentActivities.length > 0">
      <view class="section-title">最近活动</view>
      <view class="activity-list">
        <view class="activity-item" v-for="(item, index) in recentActivities" :key="index">
          <view class="activity-icon">{{ item.icon }}</view>
          <view class="activity-content">
            <text class="activity-title">{{ item.title }}</text>
            <text class="activity-time">{{ item.time }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { useUserStore } from '@/stores/user'
import { getReport } from '@/api/report'

export default {
  data() {
    return {
      reportData: null,
      recentActivities: []
    }
  },

  computed: {
    userStore() {
      return useUserStore()
    }
  },

  onShow() {
    this.loadData()
  },

  methods: {
    // 加载数据
    async loadData() {
      try {
        this.reportData = await getReport()
      } catch (e) {
        console.error('加载报表数据失败', e)
      }
    },

    // 导航到库存
    navigateToInventory() {
      if (this.userStore.canAccessInventory) {
        uni.switchTab({ url: '/pages/inventory/list' })
      }
    },

    // 导航到订单
    navigateToOrder() {
      if (this.userStore.canAccessOrder) {
        uni.switchTab({ url: '/pages/order/list' })
      }
    },

    // 导航到田间记录
    navigateToField() {
      if (this.userStore.canAccessField) {
        uni.switchTab({ url: '/pages/field/list' })
      }
    },

    // 导航到报表
    navigateToReport() {
      uni.navigateTo({ url: '/pages/report/index' })
    },

    // 导航
    navigateTo(type) {
      switch (type) {
        case 'inventory':
          if (this.userStore.canAccessInventory) {
            uni.switchTab({ url: '/pages/inventory/list' })
          }
          break
        case 'order':
          if (this.userStore.canAccessOrder) {
            uni.switchTab({ url: '/pages/order/list' })
          }
          break
        case 'field':
          if (this.userStore.canAccessField) {
            uni.switchTab({ url: '/pages/field/list' })
          }
          break
        case 'report':
          uni.navigateTo({ url: '/pages/report/index' })
          break
      }
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  margin-bottom: 30rpx;
}

.greeting {
  display: flex;
  flex-direction: column;
}

.greeting-text {
  font-size: 40rpx;
  font-weight: 600;
  color: #333333;
}

.greeting-sub {
  font-size: 26rpx;
  color: #999999;
  margin-top: 8rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 40rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.stats-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.stats-card {
  flex: 1;
  min-width: 45%;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  gap: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.stats-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
}

.stats-info {
  display: flex;
  flex-direction: column;
}

.stats-value {
  font-size: 44rpx;
  font-weight: 600;
  color: #333333;
}

.stats-label {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
}

.quick-actions {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 24rpx;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx 10rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-bottom: 16rpx;
}

.action-label {
  font-size: 24rpx;
  color: #666666;
}

.recent-activities {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.activity-list {
  margin-top: 20rpx;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F5F5F5;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-icon {
  width: 60rpx;
  height: 60rpx;
  background: #F5F5F5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  margin-right: 20rpx;
}

.activity-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.activity-title {
  font-size: 28rpx;
  color: #333333;
}

.activity-time {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
}
</style>
