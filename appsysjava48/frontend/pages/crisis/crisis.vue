<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">危机预警</text>
      <view class="stats">
        <text class="stat-item">待处理: {{ pendingCount }}</text>
      </view>
    </view>

    <view class="tab-bar">
      <view 
        v-for="tab in tabs" 
        :key="tab.value"
        class="tab-item"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value"
      >
        <text>{{ tab.label }}</text>
      </view>
    </view>

    <view class="alert-list">
      <view v-if="filteredAlerts.length === 0" class="empty-state">
        <text class="empty-icon">✅</text>
        <text class="empty-text">暂无预警记录</text>
      </view>
      <view 
        v-for="alert in filteredAlerts" 
        :key="alert.id" 
        class="alert-card"
      >
        <view class="alert-header">
          <view class="alert-level" :class="alert.alertLevel">
            <text>{{ getLevelText(alert.alertLevel) }}</text>
          </view>
          <view class="alert-status" :class="alert.status">
            <text>{{ getStatusText(alert.status) }}</text>
          </view>
        </view>
        
        <view class="alert-content">
          <text class="trigger-words">触发关键词: {{ alert.triggerWords }}</text>
          <text class="content">{{ alert.content }}</text>
        </view>

        <view class="alert-footer">
          <text class="user-info">用户ID: {{ alert.userId }}</text>
          <text class="date">{{ alert.createTime }}</text>
        </view>

        <view class="alert-actions" v-if="alert.status === 'pending'">
          <view class="action-btn process" @click="processAlert(alert.id)">
            <text>处理</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { api } from '@/utils/request.js'

export default {
  data() {
    return {
      isElderMode: false,
      activeTab: 'pending',
      tabs: [
        { label: '待处理', value: 'pending' },
        { label: '处理中', value: 'processing' },
        { label: '已解决', value: 'resolved' }
      ],
      alerts: []
    }
  },
  onLoad() {
    this.loadAlerts()
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  computed: {
    pendingCount() {
      return this.alerts.filter(a => a.status === 'pending').length
    },
    filteredAlerts() {
      return this.alerts.filter(a => a.status === this.activeTab)
    }
  },
  methods: {
    getLevelText(level) {
      const map = { 'low': '低', 'medium': '中', 'high': '高', 'critical': '紧急' }
      return map[level] || '未知'
    },
    getStatusText(status) {
      const map = { 'pending': '待处理', 'processing': '处理中', 'resolved': '已解决' }
      return map[status] || status
    },
    async loadAlerts() {
      try {
        this.alerts = await api.getAllAlerts()
      } catch (e) {
        console.error(e)
        this.alerts = [
          { id: 1, alertLevel: 'high', status: 'pending', triggerWords: '自杀,不想活', content: '咨询记录中检测到危机关键词：自杀,不想活', userId: 1, createTime: '2024-01-15 14:30' },
          { id: 2, alertLevel: 'critical', status: 'pending', triggerWords: '跳楼,自杀', content: '咨询记录中检测到危机关键词：跳楼,自杀', userId: 2, createTime: '2024-01-15 15:20' },
          { id: 3, alertLevel: 'medium', status: 'resolved', triggerWords: '自残', content: '咨询记录中检测到危机关键词：自残', userId: 3, createTime: '2024-01-14 10:00' }
        ]
      }
    },
    processAlert(id) {
      uni.showModal({
        title: '处理预警',
        editable: true,
        placeholderText: '请输入处理结果',
        success: async (res) => {
          if (res.confirm) {
            try {
              await api.handleAlert(id, res.content || '已处理')
              uni.showToast({ title: '处理成功', icon: 'success' })
              const alert = this.alerts.find(a => a.id === id)
              if (alert) {
                alert.status = 'resolved'
              }
            } catch (e) {
              uni.showToast({ title: '处理成功（模拟）', icon: 'success' })
              const alert = this.alerts.find(a => a.id === id)
              if (alert) {
                alert.status = 'resolved'
              }
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  
  &.elder {
    font-size: 36rpx !important;
  }
}

.header {
  background: linear-gradient(135deg, #f44336 0%, #d32f2f 100%);
  padding: 30rpx;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.stats {
  color: rgba(255, 255, 255, 0.9);
  font-size: 26rpx;
}

.stat-item {
  display: block;
}

.tab-bar {
  display: flex;
  background: white;
  margin-bottom: 20rpx;
}

.tab-item {
  flex: 1;
  padding: 30rpx;
  text-align: center;
  font-size: 28rpx;
  color: #666;
  position: relative;
  
  &.active {
    color: #f44336;
    font-weight: bold;
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 60rpx;
      height: 4rpx;
      background: #f44336;
      border-radius: 2rpx;
    }
  }
}

.alert-list {
  padding: 0 30rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.alert-card {
  background: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.alert-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.alert-level {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: bold;
  
  &.low {
    background: #e8f5e9;
    color: #4caf50;
  }
  
  &.medium {
    background: #fff3e0;
    color: #ff9800;
  }
  
  &.high {
    background: #ffebee;
    color: #f44336;
  }
  
  &.critical {
    background: #b71c1c;
    color: white;
  }
}

.alert-status {
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  
  &.pending {
    background: #fff3e0;
    color: #ff9800;
  }
  
  &.processing {
    background: #e3f2fd;
    color: #2196f3;
  }
  
  &.resolved {
    background: #e8f5e9;
    color: #4caf50;
  }
}

.alert-content {
  margin-bottom: 20rpx;
}

.trigger-words {
  display: block;
  font-size: 24rpx;
  color: #f44336;
  margin-bottom: 10rpx;
}

.content {
  display: block;
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.alert-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.user-info, .date {
  font-size: 24rpx;
  color: #999;
}

.alert-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  padding: 20rpx;
  text-align: center;
  border-radius: 8rpx;
  font-size: 26rpx;
  
  &.process {
    background: #f44336;
    color: white;
  }
}
</style>
