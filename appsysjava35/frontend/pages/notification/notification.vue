<template>
  <view class="container">
    <view class="notification-header card">
      <text class="card-title">消息通知</text>
      <text class="unread-count" v-if="unreadCount > 0">{{ unreadCount }} 条未读</text>
      <button class="btn btn-outline read-all-btn" @click="handleMarkAllRead">全部已读</button>
    </view>

    <view class="notification-tabs card">
      <view class="tabs-header">
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeType === 'all' }"
          @click="activeType = 'all'"
        >全部</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeType === 1 }"
          @click="activeType = 1"
        >账单</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeType === 2 }"
          @click="activeType = 2"
        >进度</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeType === 3 }"
          @click="activeType = 3"
        >活动</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeType === 4 }"
          @click="activeType = 4"
        >安全</text>
      </view>
    </view>

    <view class="notification-list">
      <view 
        class="notification-card card" 
        v-for="item in filteredNotifications" 
        :key="item.id"
        :class="{ 'notification-unread': !item.isRead }"
        @click="handleMarkRead(item)"
      >
        <view class="notification-icon" :style="getTypeBgColor(item.type)">
          <text>{{ getTypeIcon(item.type) }}</text>
        </view>
        <view class="notification-content">
          <view class="notification-header-row">
            <text class="notification-title">{{ item.title }}</text>
            <text class="notification-time">{{ formatTime(item.createTime) }}</text>
          </view>
          <text class="notification-desc">{{ item.content }}</text>
        </view>
        <view class="notification-unread-dot" v-if="!item.isRead"></view>
      </view>
    </view>

    <view class="empty-state card" v-if="filteredNotifications.length === 0">
      <text class="empty-icon">🔔</text>
      <text class="empty-text">暂无通知消息</text>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      notifications: [],
      activeType: 'all',
      unreadCount: 0
    }
  },
  computed: {
    filteredNotifications() {
      if (this.activeType === 'all') {
        return this.notifications
      }
      return this.notifications.filter(n => n.type === this.activeType)
    }
  },
  onShow() {
    this.loadNotifications()
    this.loadUnreadCount()
  },
  methods: {
    async loadNotifications() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          this.notifications = await api.getNotifications(userId)
        } catch (e) {
          console.error(e)
        }
      }
    },

    async loadUnreadCount() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          this.unreadCount = await api.getUnreadCount(userId)
        } catch (e) {
          console.error(e)
        }
      }
    },

    async handleMarkRead(item) {
      if (item.isRead) return
      
      try {
        await api.markAsRead(item.id)
        item.isRead = true
        this.unreadCount = Math.max(0, this.unreadCount - 1)
      } catch (e) {
        console.error(e)
      }
    },

    async handleMarkAllRead() {
      const userId = uni.getStorageSync('userId')
      if (!userId) return

      try {
        await api.markAllAsRead(userId)
        this.notifications.forEach(n => n.isRead = true)
        this.unreadCount = 0
        uni.showToast({
          title: '已全部标记为已读',
          icon: 'success'
        })
      } catch (e) {
        console.error(e)
      }
    },

    getTypeIcon(type) {
      const icons = {
        1: '📊',
        2: '📈',
        3: '🎉',
        4: '⚠️'
      }
      return icons[type] || '📢'
    },

    getTypeBgColor(type) {
      const colors = {
        1: 'background-color: #e8f3ff;',
        2: 'background-color: #e8f9f1;',
        3: 'background-color: #fff3e8;',
        4: 'background-color: #ffeef0;'
      }
      return colors[type] || colors[1]
    },

    formatTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      if (diff < 604800000) return Math.floor(diff / 86400000) + '天前'
      
      return `${date.getMonth() + 1}/${date.getDate()}`
    }
  }
}
</script>

<style scoped>
.notification-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.unread-count {
  font-size: 24rpx;
  color: #ee0a24;
}

.read-all-btn {
  height: 56rpx;
  line-height: 56rpx;
  padding: 0 24rpx;
  font-size: 24rpx;
}

.tabs-header {
  display: flex;
  justify-content: space-between;
}

.tab-item {
  padding: 16rpx 20rpx;
  font-size: 26rpx;
  color: #666;
  border-radius: 8rpx;
}

.tab-item.tab-active {
  color: #1989fa;
  background: #e8f3ff;
  font-weight: 500;
}

.notification-list {
  margin-top: 20rpx;
}

.notification-card {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
  position: relative;
}

.notification-card.notification-unread {
  background: #f8faff;
}

.notification-icon {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-header-row {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8rpx;
}

.notification-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  flex: 1;
  margin-right: 16rpx;
}

.notification-time {
  font-size: 22rpx;
  color: #999;
  flex-shrink: 0;
}

.notification-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.notification-unread-dot {
  position: absolute;
  top: 30rpx;
  right: 30rpx;
  width: 12rpx;
  height: 12rpx;
  border-radius: 50%;
  background: #ee0a24;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
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
</style>
