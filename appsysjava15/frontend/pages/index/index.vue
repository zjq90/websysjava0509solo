<template>
  <view class="container">
    <view class="header-card">
      <view class="search-bar" @click="goToScan">
        <text class="search-icon">🔍</text>
        <text class="search-text">扫码查询批次信息...</text>
      </view>
      <view class="stat-row">
        <view class="stat-item">
          <text class="stat-value">{{ stats.total }}</text>
          <text class="stat-label">全部批次</text>
        </view>
        <view class="stat-item">
          <text class="stat-value progress">{{ stats.inProgress }}</text>
          <text class="stat-label">进行中</text>
        </view>
        <view class="stat-item">
          <text class="stat-value completed">{{ stats.completed }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-value pending">{{ stats.pending }}</text>
          <text class="stat-label">待开始</text>
        </view>
      </view>
    </view>

    <view class="section-header">
      <text class="section-title">生产批次列表</text>
      <view class="add-btn" @click="goToAdd">
        <text class="add-icon">+</text>
        <text>新建批次</text>
      </view>
    </view>

    <view v-if="batches.length === 0" class="empty-state">
      <text class="empty-icon">📦</text>
      <text class="empty-text">暂无批次数据</text>
    </view>

    <view v-else>
      <view 
        v-for="batch in batches" 
        :key="batch.id" 
        class="batch-card"
        @click="goToDetail(batch.id)"
      >
        <view class="batch-header">
          <view class="batch-no">{{ batch.batchNo }}</view>
          <view :class="getStatusClass(batch.status)">
            {{ getStatusText(batch.status) }}
          </view>
        </view>
        <view class="batch-body">
          <view class="batch-row">
            <text class="batch-label">产品名称</text>
            <text class="batch-value">{{ batch.productName }}</text>
          </view>
          <view class="batch-row">
            <text class="batch-label">生产数量</text>
            <text class="batch-value">{{ batch.quantity }} {{ batch.unit }}</text>
          </view>
          <view class="batch-row">
            <text class="batch-label">当前环节</text>
            <text class="batch-value highlight">{{ getStageName(batch.currentStage) || '-' }}</text>
          </view>
          <view class="batch-row">
            <text class="batch-label">客户</text>
            <text class="batch-value">{{ batch.customerName || '-' }}</text>
          </view>
        </view>
        <view class="batch-footer">
          <text class="batch-date">创建时间：{{ formatDate(batch.createTime) }}</text>
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
      batches: [],
      stats: {
        total: 0,
        inProgress: 0,
        completed: 0,
        pending: 0
      }
    }
  },
  onShow() {
    this.loadBatches()
  },
  onPullDownRefresh() {
    this.loadBatches().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    async loadBatches() {
      try {
        const res = await api.getBatches()
        this.batches = res.data || []
        this.calcStats()
      } catch (e) {
        console.error(e)
      }
    },
    calcStats() {
      this.stats.total = this.batches.length
      this.stats.inProgress = this.batches.filter(b => b.status === 'IN_PROGRESS').length
      this.stats.completed = this.batches.filter(b => b.status === 'COMPLETED').length
      this.stats.pending = this.batches.filter(b => b.status === 'PENDING').length
    },
    getStatusClass(status) {
      if (status === 'IN_PROGRESS') return 'status-progress'
      if (status === 'COMPLETED') return 'status-completed'
      return 'status-pending'
    },
    getStatusText(status) {
      if (status === 'IN_PROGRESS') return '进行中'
      if (status === 'COMPLETED') return '已完成'
      if (status === 'PENDING') return '待开始'
      return status
    },
    getStageName(code) {
      const map = {
        'CLEANING': '清选',
        'COATING': '包衣',
        'PACKAGING': '分装',
        'INSPECTION': '质检'
      }
      return map[code] || code
    },
    formatDate(dateStr) {
      if (!dateStr) return '-'
      return dateStr.replace('T', ' ').substring(0, 16)
    },
    goToScan() {
      uni.switchTab({
        url: '/pages/scan/scan'
      })
    },
    goToAdd() {
      uni.navigateTo({
        url: '/pages/batch/add'
      })
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/batch/detail?id=${id}`
      })
    }
  }
}
</script>

<style scoped>
.header-card {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
}

.search-bar {
  background-color: rgba(255, 255, 255, 0.25);
  border-radius: 48rpx;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-text {
  color: rgba(255, 255, 255, 0.9);
  font-size: 28rpx;
}

.stat-row {
  display: flex;
  justify-content: space-between;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 44rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 8rpx;
}

.stat-value.progress {
  color: #FFE066;
}

.stat-value.completed {
  color: #B7EB8F;
}

.stat-value.pending {
  color: #ADC6FF;
}

.stat-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
}

.add-btn {
  background-color: #409EFF;
  color: #ffffff;
  border-radius: 32rpx;
  padding: 12rpx 28rpx;
  font-size: 26rpx;
  display: flex;
  align-items: center;
}

.add-icon {
  margin-right: 8rpx;
  font-weight: bold;
}

.empty-state {
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
  font-size: 28rpx;
  color: #909399;
}

.batch-card {
  background-color: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  padding: 28rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
}

.batch-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.batch-no {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
}

.batch-body {
  margin-bottom: 16rpx;
}

.batch-row {
  display: flex;
  justify-content: space-between;
  padding: 10rpx 0;
}

.batch-label {
  font-size: 26rpx;
  color: #909399;
}

.batch-value {
  font-size: 26rpx;
  color: #303133;
}

.batch-value.highlight {
  color: #409EFF;
  font-weight: 500;
}

.batch-footer {
  border-top: 1rpx solid #f0f0f0;
  padding-top: 16rpx;
}

.batch-date {
  font-size: 24rpx;
  color: #c0c4cc;
}
</style>
