<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="header-card card">
      <view class="user-info flex-between">
        <view class="flex">
          <view class="avatar">
            <text class="avatar-text">{{ user.realName ? user.realName.charAt(0) : '用' }}</text>
          </view>
          <view class="info">
            <view class="name">{{ user.realName || '用户' }}</view>
            <view class="phone">{{ user.phone }}</view>
          </view>
        </view>
        <switch :checked="elderMode" @change="toggleElderMode" color="#667eea" />
      </view>
    </view>

    <view class="quick-actions card">
      <view class="card-title">快捷服务</view>
      <view class="action-grid">
        <view class="action-item" @click="goToPage('/pages/service-order/index')">
          <view class="action-icon service-icon">
            <text>🔧</text>
          </view>
          <text class="action-text">服务进度</text>
        </view>
        <view class="action-item" @click="goToPage('/pages/fault-report/index')">
          <view class="action-icon fault-icon">
            <text>⚠️</text>
          </view>
          <text class="action-text">故障报修</text>
        </view>
        <view class="action-item" @click="goToPage('/pages/network/index')">
          <view class="action-icon network-icon">
            <text>📶</text>
          </view>
          <text class="action-text">网络管理</text>
        </view>
        <view class="action-item" @click="goToPage('/pages/chat/index')">
          <view class="action-icon chat-icon">
            <text>💬</text>
          </view>
          <text class="action-text">AI客服</text>
        </view>
      </view>
    </view>

    <view class="service-list card">
      <view class="card-title">我的服务</view>
      <view v-for="order in serviceOrders" :key="order.id" class="service-item" @click="viewServiceDetail(order)">
        <view class="service-header flex-between">
          <text class="service-type">{{ order.serviceType }}</text>
          <text class="service-status" :class="getStatusClass(order.status)">{{ order.status }}</text>
        </view>
        <view class="service-progress">
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: (order.currentStep / order.totalSteps * 100) + '%' }"></view>
          </view>
          <view class="steps">
            <text v-for="(step, index) in getSteps(order)" :key="index" class="step" :class="{ active: index < order.currentStep }">
              {{ step }}
            </text>
          </view>
        </view>
        <view class="service-worker" v-if="order.workerName">
          <text>师傅：{{ order.workerName }} {{ order.workerPhone }}</text>
        </view>
      </view>
      <view v-if="serviceOrders.length === 0" class="empty-state">
        <text>暂无服务记录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { serviceOrderApi, userApi } from '@/api/index.js'

export default {
  data() {
    return {
      user: {},
      serviceOrders: [],
      elderMode: false,
      userId: 1
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        this.user = await userApi.getById(this.userId)
        this.elderMode = this.user.elderMode
        this.serviceOrders = await serviceOrderApi.list(this.userId)
      } catch (e) {
        console.error(e)
      }
    },
    async toggleElderMode(e) {
      this.elderMode = e.detail.value
      try {
        await userApi.toggleElderMode(this.userId)
      } catch (e) {
        console.error(e)
      }
    },
    goToPage(path) {
      uni.navigateTo({ url: path })
    },
    viewServiceDetail(order) {
      uni.navigateTo({ url: '/pages/service-order/index?id=' + order.id })
    },
    getStatusClass(status) {
      if (status.includes('完成')) return 'status-completed'
      if (status.includes('审核')) return 'status-review'
      return 'status-processing'
    },
    getSteps(order) {
      return ['资料审核', '装维派单', '上门安装', '已完成']
    }
  }
}
</script>

<style scoped>
.header-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.user-info {
  padding: 20rpx 0;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.info .name {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.info .phone {
  font-size: 24rpx;
  opacity: 0.9;
}

.quick-actions .action-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.action-item {
  width: calc(50% - 10rpx);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 0;
  border-radius: 12rpx;
  background: #f8f9fa;
}

.action-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-bottom: 12rpx;
}

.service-icon {
  background: #e3f2fd;
}

.fault-icon {
  background: #fff3e0;
}

.network-icon {
  background: #e8f5e9;
}

.chat-icon {
  background: #fce4ec;
}

.action-text {
  font-size: 26rpx;
  color: #333;
}

.service-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.service-item:last-child {
  border-bottom: none;
}

.service-header {
  margin-bottom: 20rpx;
}

.service-type {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
}

.service-status {
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

.status-completed {
  background: #e8f5e9;
  color: #4caf50;
}

.status-review {
  background: #fff3e0;
  color: #ff9800;
}

.status-processing {
  background: #e3f2fd;
  color: #2196f3;
}

.service-progress {
  margin-bottom: 16rpx;
}

.progress-bar {
  height: 8rpx;
  background: #f0f0f0;
  border-radius: 4rpx;
  overflow: hidden;
  margin-bottom: 16rpx;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 4rpx;
  transition: width 0.3s;
}

.steps {
  display: flex;
  justify-content: space-between;
}

.step {
  font-size: 22rpx;
  color: #999;
}

.step.active {
  color: #667eea;
  font-weight: 500;
}

.service-worker {
  font-size: 24rpx;
  color: #666;
}

.empty-state {
  text-align: center;
  padding: 60rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
