<template>
  <view class="container">
    <view class="header-card">
      <view class="header-left">
        <text class="header-count">{{ alerts.length }}</text>
        <text class="header-label">条预警通知</text>
      </view>
      <view class="header-right">
        <text class="header-icon">🔔</text>
      </view>
    </view>

    <view v-if="alerts.length === 0" class="empty-alerts card">
      <text class="empty-icon">✅</text>
      <text class="empty-text">暂无预警通知</text>
      <text class="empty-desc">所有生产环节运行正常</text>
    </view>

    <view v-else>
      <view 
        v-for="alert in alerts" 
        :key="alert.id" 
        class="alert-card"
        :class="{ 'alert-read': alert.status === 'READ' }"
        @click="handleAlertClick(alert)"
      >
        <view class="alert-header">
          <view class="alert-type">
            <text class="alert-icon" v-if="alert.alertType === 'TIMEOUT'">⏰</text>
            <text class="alert-type-text">超时预警</text>
          </view>
          <view v-if="alert.status === 'UNREAD'" class="unread-dot"></view>
        </view>
        <view class="alert-body">
          <view class="alert-row">
            <text class="alert-label">批次ID：</text>
            <text class="alert-value">{{ alert.batchId }}</text>
          </view>
          <view class="alert-row">
            <text class="alert-label">生产环节：</text>
            <text class="alert-value highlight">{{ alert.stageName }}</text>
          </view>
          <view class="alert-message">{{ alert.message }}</view>
        </view>
        <view class="alert-footer">
          <text class="alert-time">{{ formatDate(alert.createTime) }}</text>
          <view v-if="alert.status === 'UNREAD'" class="mark-read" @click.stop="markAsRead(alert.id)">
            <text>标记已读</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      alerts: [],
      userInfo: null
    }
  },
  onShow() {
    this.userInfo = uni.getStorageSync('userInfo')
    this.loadAlerts()
  },
  methods: {
    async loadAlerts() {
      try {
        const userId = this.userInfo && this.userInfo.id ? this.userInfo.id : 2
        const res = await api.getAlertsBySupervisor(userId)
        this.alerts = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      return dateStr.replace('T', ' ').substring(0, 16)
    },
    handleAlertClick(alert) {
      if (alert.status === 'UNREAD') {
        this.markAsRead(alert.id)
      }
      uni.navigateTo({
        url: `/pages/batch/detail?id=${alert.batchId}`
      })
    },
    async markAsRead(id) {
      try {
        await api.markAlertRead(id)
        this.loadAlerts()
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.header-card {
  background: linear-gradient(135deg, #f56c6c 0%, #e6a23c 100%);
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 24rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: baseline;
}

.header-count {
  font-size: 64rpx;
  font-weight: bold;
  color: #ffffff;
  margin-right: 16rpx;
}

.header-label {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
}

.header-icon {
  font-size: 80rpx;
}

.empty-alerts {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 32rpx;
  color: #606266;
  margin-bottom: 12rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #909399;
}

.alert-card {
  background-color: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  padding: 28rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
  position: relative;
}

.alert-card.alert-read {
  opacity: 0.7;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.alert-type {
  display: flex;
  align-items: center;
}

.alert-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.alert-type-text {
  font-size: 28rpx;
  font-weight: bold;
  color: #f56c6c;
}

.unread-dot {
  width: 16rpx;
  height: 16rpx;
  background-color: #f56c6c;
  border-radius: 50%;
}

.alert-row {
  display: flex;
  padding: 8rpx 0;
}

.alert-label {
  font-size: 26rpx;
  color: #909399;
}

.alert-value {
  font-size: 26rpx;
  color: #303133;
}

.alert-value.highlight {
  color: #f56c6c;
  font-weight: 500;
}

.alert-message {
  font-size: 26rpx;
  color: #606266;
  background-color: #fff6f6;
  border-radius: 8rpx;
  padding: 16rpx;
  margin-top: 12rpx;
  line-height: 1.6;
}

.alert-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20rpx;
  padding-top: 16rpx;
  border-top: 1rpx solid #f0f0f0;
}

.alert-time {
  font-size: 22rpx;
  color: #c0c4cc;
}

.mark-read {
  font-size: 24rpx;
  color: #409eff;
}
</style>
