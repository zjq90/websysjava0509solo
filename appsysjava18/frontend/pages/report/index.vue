<template>
  <view class="container">
    <view class="header">
      <text class="header-title">数据报表</text>
      <view class="refresh-btn" @click="loadData">
        <text class="refresh-icon">🔄</text>
        <text>刷新</text>
      </view>
    </view>

    <view class="overview-section">
      <view class="section-title">总览统计</view>
      <view class="stats-grid">
        <view class="stat-card" v-if="userStore.canAccessInventory">
          <view class="stat-icon" style="background: #007AFF1A;">📦</view>
          <view class="stat-content">
            <text class="stat-value">{{ reportData?.inventoryCount || 0 }}</text>
            <text class="stat-label">库存品种</text>
          </view>
        </view>
        <view class="stat-card" v-if="userStore.canAccessInventory">
          <view class="stat-icon" style="background: #FF95001A;">⚖️</view>
          <view class="stat-content">
            <text class="stat-value">{{ reportData?.totalQuantity || 0 }}</text>
            <text class="stat-label">总数量(公斤)</text>
          </view>
        </view>
        <view class="stat-card" v-if="userStore.canAccessOrder">
          <view class="stat-icon" style="background: #34C7591A;">📋</view>
          <view class="stat-content">
            <text class="stat-value">{{ reportData?.orderCount || 0 }}</text>
            <text class="stat-label">订单数量</text>
          </view>
        </view>
        <view class="stat-card" v-if="userStore.canAccessOrder">
          <view class="stat-icon" style="background: #FF3B301A;">💰</view>
          <view class="stat-content">
            <text class="stat-value">¥{{ (reportData?.totalAmount || 0).toFixed(0) }}</text>
            <text class="stat-label">销售总额</text>
          </view>
        </view>
        <view class="stat-card" v-if="userStore.canAccessField">
          <view class="stat-icon" style="background: #AF52DE1A;">🌾</view>
          <view class="stat-content">
            <text class="stat-value">{{ reportData?.fieldRecordCount || 0 }}</text>
            <text class="stat-label">田间记录</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon" style="background: #5AC8FA1A;">👥</view>
          <view class="stat-content">
            <text class="stat-value">{{ reportData?.customerCount || 0 }}</text>
            <text class="stat-label">客户数量</text>
          </view>
        </view>
      </view>
    </view>

    <view class="inventory-section" v-if="userStore.canAccessInventory">
      <view class="section-title">库存统计</view>
      <view class="chart-card">
        <view class="chart-header">
          <text class="chart-title">库存概览</text>
        </view>
        <view class="chart-content">
          <view class="chart-item" v-for="(item, index) in inventorySummary" :key="index">
            <view class="chart-info">
              <text class="chart-name">{{ item.seedName }}</text>
              <text class="chart-value">{{ item.quantity }} 公斤</text>
            </view>
            <view class="chart-bar">
              <view 
                class="chart-fill" 
                :style="{ width: item.percent + '%' }"
              ></view>
            </view>
          </view>
          <view class="empty-chart" v-if="inventorySummary.length === 0">
            <text>暂无库存数据</text>
          </view>
        </view>
      </view>
    </view>

    <view class="order-section" v-if="userStore.canAccessOrder">
      <view class="section-title">订单统计</view>
      <view class="status-summary">
        <view class="status-item">
          <view class="status-dot pending"></view>
          <text class="status-label">待处理</text>
          <text class="status-count">{{ reportData?.pendingCount || 0 }}</text>
        </view>
        <view class="status-item">
          <view class="status-dot processing"></view>
          <text class="status-label">处理中</text>
          <text class="status-count">{{ reportData?.processingCount || 0 }}</text>
        </view>
        <view class="status-item">
          <view class="status-dot completed"></view>
          <text class="status-label">已完成</text>
          <text class="status-count">{{ reportData?.completedCount || 0 }}</text>
        </view>
      </view>
    </view>

    <view class="field-section" v-if="userStore.canAccessField">
      <view class="section-title">田间记录统计</view>
      <view class="chart-card">
        <view class="chart-header">
          <text class="chart-title">生长阶段分布</text>
        </view>
        <view class="growth-stats">
          <view class="growth-item" v-for="(item, index) in growthSummary" :key="index">
            <view class="growth-info">
              <text class="growth-stage">{{ item.stage }}</text>
              <text class="growth-count">{{ item.count }} 条</text>
            </view>
            <view class="growth-bar">
              <view 
                class="growth-fill" 
                :style="{ width: item.percent + '%' }"
              ></view>
            </view>
          </view>
          <view class="empty-chart" v-if="growthSummary.length === 0">
            <text>暂无田间记录数据</text>
          </view>
        </view>
      </view>
    </view>

    <view class="update-time">
      <text>数据更新时间: {{ updateTime }}</text>
    </view>
  </view>
</template>

<script>
import { getReport } from '@/api/report'
import { useUserStore } from '@/stores/user'

export default {
  data() {
    return {
      reportData: null,
      updateTime: '-'
    }
  },

  computed: {
    userStore() {
      return useUserStore()
    },

    // 库存统计
    inventorySummary() {
      if (!this.reportData?.topSeeds) return []
      const maxQty = Math.max(...this.reportData.topSeeds.map(s => s.quantity), 1)
      return this.reportData.topSeeds.map(item => ({
        ...item,
        percent: (item.quantity / maxQty) * 100
      }))
    },

    // 生长阶段统计
    growthSummary() {
      if (!this.reportData?.growthStageStats) return []
      const maxCount = Math.max(...Object.values(this.reportData.growthStageStats), 1)
      return Object.entries(this.reportData.growthStageStats).map(([stage, count]) => ({
        stage,
        count,
        percent: (count / maxCount) * 100
      }))
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
        this.updateTime = this.formatDateTime(new Date())
      } catch (e) {
        console.error('加载报表数据失败', e)
      }
    },

    // 格式化日期时间
    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
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
  margin-bottom: 30rpx;
}

.header-title {
  font-size: 40rpx;
  font-weight: 600;
  color: #333333;
}

.refresh-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: #FFFFFF;
  padding: 16rpx 24rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #007AFF;
}

.refresh-icon {
  font-size: 28rpx;
}

.overview-section,
.inventory-section,
.order-section,
.field-section {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 20rpx;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.stat-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  gap: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.stat-icon {
  width: 70rpx;
  height: 70rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
}

.stat-content {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
}

.stat-label {
  font-size: 24rpx;
  color: #999999;
  margin-top: 4rpx;
}

.chart-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.chart-header {
  margin-bottom: 24rpx;
}

.chart-title {
  font-size: 28rpx;
  color: #666666;
}

.chart-content {
  margin-top: 20rpx;
}

.chart-item {
  margin-bottom: 24rpx;
}

.chart-item:last-child {
  margin-bottom: 0;
}

.chart-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.chart-name {
  font-size: 26rpx;
  color: #333333;
}

.chart-value {
  font-size: 24rpx;
  color: #999999;
}

.chart-bar {
  height: 16rpx;
  background: #F5F5F5;
  border-radius: 8rpx;
  overflow: hidden;
}

.chart-fill {
  height: 100%;
  background: linear-gradient(90deg, #007AFF 0%, #34C759 100%);
  border-radius: 8rpx;
}

.status-summary {
  display: flex;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.status-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.status-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  margin-bottom: 12rpx;
}

.status-dot.pending {
  background: #FF9500;
}

.status-dot.processing {
  background: #007AFF;
}

.status-dot.completed {
  background: #34C759;
}

.status-label {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 8rpx;
}

.status-count {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
}

.growth-stats {
  margin-top: 20rpx;
}

.growth-item {
  margin-bottom: 24rpx;
}

.growth-item:last-child {
  margin-bottom: 0;
}

.growth-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.growth-stage {
  font-size: 26rpx;
  color: #333333;
}

.growth-count {
  font-size: 24rpx;
  color: #999999;
}

.growth-bar {
  height: 16rpx;
  background: #F5F5F5;
  border-radius: 8rpx;
  overflow: hidden;
}

.growth-fill {
  height: 100%;
  background: linear-gradient(90deg, #FF9500 0%, #34C759 100%);
  border-radius: 8rpx;
}

.empty-chart {
  text-align: center;
  padding: 40rpx 0;
  color: #999999;
  font-size: 26rpx;
}

.update-time {
  text-align: center;
  padding: 30rpx 0;
  font-size: 24rpx;
  color: #999999;
}
</style>
