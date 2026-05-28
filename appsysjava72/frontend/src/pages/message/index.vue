<template>
  <view class="message-container">
    <view class="filter-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeType === 'all' }"
        @click="activeType = 'all'"
      >
        全部
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeType === 'system' }"
        @click="activeType = 'system'"
      >
        系统
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeType === 'activity' }"
        @click="activeType = 'activity'"
      >
        活动
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeType === 'club' }"
        @click="activeType = 'club'"
      >
        社团
      </view>
    </view>
    
    <view class="message-list" v-if="messageList.length > 0">
      <view 
        class="message-item" 
        v-for="msg in messageList" 
        :key="msg.id"
        @click="viewMessage(msg)"
      >
        <view class="msg-icon" :class="msg.type">
          {{ getTypeIcon(msg.type) }}
        </view>
        <view class="msg-content">
          <view class="msg-header">
            <text class="msg-type">{{ util.getMessageTypeText(msg.type) }}</text>
            <text class="msg-time">{{ util.formatDateTime(msg.createTime) }}</text>
          </view>
          <text class="msg-title">{{ msg.title }}</text>
          <text class="msg-summary">{{ msg.content }}</text>
          <view v-if="msg.status === 0" class="unread-dot"></view>
        </view>
        <text class="arrow">›</text>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">🔕</text>
      <text class="empty-text">暂无消息</text>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activeType: 'all',
      messageList: [],
      loading: false
    }
  },
  computed: {
    filteredMessages() {
      if (this.activeType === 'all') {
        return this.messageList
      }
      return this.messageList.filter(msg => msg.type === this.activeType)
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const res = await api.getMessageList()
        this.messageList = res.data || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    getTypeIcon(type) {
      const icons = {
        system: '🔔',
        activity: '📅',
        club: '🏢'
      }
      return icons[type] || '📩'
    },
    
    async viewMessage(msg) {
      if (msg.status === 0) {
        try {
          await api.readMessage(msg.id)
          msg.status = 1
        } catch (e) {
          console.error(e)
        }
      }
    }
  }
}
</script>

<style scoped>
.message-container {
  min-height: 100vh;
  background: #f5f6f8;
}

.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 10rpx;
  border-bottom: 1rpx solid #f0f0f0;
  overflow-x: auto;
}

.tab-item {
  padding: 24rpx 30rpx;
  font-size: 28rpx;
  color: #666;
  white-space: nowrap;
  position: relative;
}

.tab-item.active {
  color: #5677fc;
  font-weight: 600;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 6rpx;
  background: #5677fc;
  border-radius: 3rpx;
}

.message-list {
  padding: 20rpx;
}

.message-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  position: relative;
}

.msg-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.msg-icon.system {
  background: #e3f2fd;
}

.msg-icon.activity {
  background: #e8f5e9;
}

.msg-icon.club {
  background: #fce4ec;
}

.msg-content {
  flex: 1;
  min-width: 0;
}

.msg-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8rpx;
}

.msg-type {
  font-size: 24rpx;
  color: #5677fc;
  font-weight: 600;
}

.msg-time {
  font-size: 22rpx;
  color: #999;
}

.msg-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 6rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.msg-summary {
  display: block;
  font-size: 24rpx;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-dot {
  position: absolute;
  top: 20rpx;
  right: 50rpx;
  width: 16rpx;
  height: 16rpx;
  background: #ff4d4f;
  border-radius: 50%;
}

.arrow {
  font-size: 40rpx;
  color: #ccc;
  margin-left: 10rpx;
}

.empty-state {
  text-align: center;
  padding: 120rpx 0;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 28rpx;
  color: #999;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
