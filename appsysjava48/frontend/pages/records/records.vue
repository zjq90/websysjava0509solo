<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">咨询记录</text>
      <view class="add-btn" @click="goToAdd">
        <text>+ 新建</text>
      </view>
    </view>

    <view class="filter-bar">
      <view 
        v-for="tag in tags" 
        :key="tag"
        class="filter-tag"
        :class="{ active: activeTag === tag }"
        @click="activeTag = tag"
      >
        <text>{{ tag }}</text>
      </view>
    </view>

    <view class="record-list">
      <view v-if="records.length === 0" class="empty-state">
        <text class="empty-icon">📝</text>
        <text class="empty-text">暂无咨询记录</text>
      </view>
      <view 
        v-for="record in records" 
        :key="record.id" 
        class="record-card"
        @click="goToDetail(record.id)"
      >
        <view class="record-header">
          <text class="user-name">{{ record.userName || '用户' }}</text>
          <view class="risk-tag" :class="record.riskLevel">
            <text>{{ getRiskText(record.riskLevel) }}</text>
          </view>
        </view>
        <view class="record-content">
          <text class="chief-complaint">{{ record.chiefComplaint || '暂无主诉' }}</text>
        </view>
        <view class="record-footer">
          <text class="tags">{{ record.tags || '未分类' }}</text>
          <text class="date">{{ record.createTime || '2024-01-01' }}</text>
        </view>
        <view class="record-status" :class="record.status === 1 ? 'completed' : 'draft'">
          <text>{{ record.status === 1 ? '已完成' : '草稿' }}</text>
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
      activeTag: '全部',
      tags: ['全部', '抑郁', '焦虑', '恐慌', '婚恋情感'],
      records: []
    }
  },
  onLoad() {
    this.loadRecords()
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    getRiskText(level) {
      const map = { 'low': '低风险', 'medium': '中风险', 'high': '高风险' }
      return map[level] || '低风险'
    },
    async loadRecords() {
      try {
        this.records = await api.getRecordsByCounselor(1)
      } catch (e) {
        console.error(e)
        this.records = [
          { id: 1, userName: '张小明', chiefComplaint: '最近睡眠不好，情绪低落，对什么都没兴趣，不想活了', tags: '抑郁,焦虑', riskLevel: 'high', status: 1, createTime: '2024-01-15' },
          { id: 2, userName: '李小红', chiefComplaint: '工作压力大，经常感到恐慌不安', tags: '恐慌,工作压力', riskLevel: 'medium', status: 1, createTime: '2024-01-14' },
          { id: 3, userName: '王华', chiefComplaint: '婚恋问题，与伴侣经常吵架', tags: '婚恋情感', riskLevel: 'low', status: 0, createTime: '2024-01-13' }
        ]
      }
    },
    goToAdd() {
      uni.navigateTo({ url: '/pages/record-detail/record-detail' })
    },
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/record-detail/record-detail?id=${id}` })
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

.add-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
}

.filter-bar {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx 30rpx;
  background: white;
  gap: 16rpx;
  margin-bottom: 20rpx;
}

.filter-tag {
  padding: 12rpx 24rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  
  &.active {
    background: #667eea;
    color: white;
  }
}

.record-list {
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

.record-card {
  background: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  position: relative;
}

.record-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.user-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.risk-tag {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  
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
}

.record-content {
  margin-bottom: 20rpx;
}

.chief-complaint {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.record-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.tags {
  font-size: 24rpx;
  color: #667eea;
}

.date {
  font-size: 24rpx;
  color: #999;
}

.record-status {
  position: absolute;
  top: 30rpx;
  right: 30rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  
  &.completed {
    background: #e8f5e9;
    color: #4caf50;
  }
  
  &.draft {
    background: #fff3e0;
    color: #ff9800;
  }
}
</style>
