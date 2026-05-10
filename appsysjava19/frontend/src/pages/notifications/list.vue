<template>
  <view class="container">
    <view class="header-bar">
      <view class="header-left">
        <text class="fs-32 fw-bold">消息通知</text>
        <view class="unread-badge" v-if="unreadCount > 0">
          <text>{{ unreadCount }}</text>
        </view>
      </view>
      <text class="fs-28 text-primary" @click="markAllRead" v-if="unreadCount > 0">全部已读</text>
    </view>
    
    <view class="filter-tabs">
      <view 
        class="filter-tab" 
        :class="{ active: currentFilter === 'all' }"
        @click="setFilter('all')"
      >
        <text>全部</text>
      </view>
      <view 
        class="filter-tab" 
        :class="{ active: currentFilter === 'unread' }"
        @click="setFilter('unread')"
      >
        <text>未读</text>
      </view>
      <view 
        class="filter-tab" 
        :class="{ active: currentFilter === 'expiry' }"
        @click="setFilter('expiry')"
      >
        <text>近效期</text>
      </view>
      <view 
        class="filter-tab" 
        :class="{ active: currentFilter === 'stock' }"
        @click="setFilter('stock')"
      >
        <text>缺货</text>
      </view>
    </view>
    
    <view class="notification-list">
      <view 
        class="notification-card card" 
        v-for="item in filteredList" 
        :key="item.id"
        :class="{ unread: item.status === 'UNREAD' }"
        @click="handleNotification(item)"
      >
        <view class="card-left">
          <view class="notification-icon" :class="getIconClass(item.notificationType)">
            <text class="emoji">{{ getNotificationEmoji(item.notificationType) }}</text>
          </view>
        </view>
        <view class="card-right">
          <view class="card-header">
            <text class="fs-30 fw-bold">{{ item.title }}</text>
            <view class="status-dot" v-if="item.status === 'UNREAD'"></view>
          </view>
          <text class="fs-26 text-muted mt-10" style="display: block;">{{ item.content }}</text>
          <view class="card-footer mt-15">
            <text class="fs-24 text-muted">{{ formatTime(item.createTime) }}</text>
            <view :class="getStatusBadgeClass(item.status)">
              <text>{{ getStatusText(item.status) }}</text>
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-if="filteredList.length === 0">
        <text class="fs-28 text-muted">暂无消息</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      notifications: [],
      currentFilter: 'all',
      unreadCount: 0
    }
  },
  computed: {
    filteredList() {
      let list = this.notifications
      
      switch (this.currentFilter) {
        case 'unread':
          list = list.filter(item => item.status === 'UNREAD')
          break
        case 'expiry':
          list = list.filter(item => item.notificationType === 'NEAR_EXPIRY')
          break
        case 'stock':
          list = list.filter(item => item.notificationType === 'LOW_STOCK')
          break
      }
      
      return list
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await request.get('/api/notifications')
        this.notifications = res.data || []
        this.unreadCount = this.notifications.filter(n => n.status === 'UNREAD').length
      } catch (e) {
        console.error('加载通知失败', e)
      }
    },
    
    setFilter(filter) {
      this.currentFilter = filter
    },
    
    getIconClass(type) {
      const map = {
        'NEAR_EXPIRY': 'warning',
        'LOW_STOCK': 'danger',
        'SYSTEM': 'info'
      }
      return map[type] || 'info'
    },
    
    getNotificationEmoji(type) {
      const map = {
        'NEAR_EXPIRY': '⚠️',
        'LOW_STOCK': '📉',
        'SYSTEM': '🔔'
      }
      return map[type] || '📢'
    },
    
    getStatusBadgeClass(status) {
      const map = {
        'UNREAD': 'badge-normal',
        'READ': 'badge-normal',
        'HANDLED': 'badge-success'
      }
      return map[status] || 'badge-normal'
    },
    
    getStatusText(status) {
      const map = {
        'UNREAD': '未读',
        'READ': '已读',
        'HANDLED': '已处理'
      }
      return map[status] || '未知'
    },
    
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      
      const month = date.getMonth() + 1
      const day = date.getDate()
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${month}/${day} ${hours}:${minutes}`
    },
    
    async handleNotification(item) {
      if (item.status === 'UNREAD') {
        try {
          await request.put(`/api/notifications/${item.id}/read`)
          item.status = 'READ'
          this.unreadCount = Math.max(0, this.unreadCount - 1)
        } catch (e) {
          console.error('标记已读失败', e)
        }
      }
      
      if (item.batchNo) {
        uni.navigateTo({ url: `/pages/inventory/detail?batchNo=${item.batchNo}` })
      }
    },
    
    async markAllRead() {
      try {
        await request.put('/api/notifications/read-all')
        this.notifications.forEach(item => {
          if (item.status === 'UNREAD') {
            item.status = 'READ'
          }
        })
        this.unreadCount = 0
        uni.showToast({ title: '已全部标记为已读', icon: 'success' })
      } catch (e) {
        console.error('批量标记失败', e)
      }
    }
  }
}
</script>

<style scoped>
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #ffffff;
  margin-bottom: 20rpx;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15rpx;
}

.unread-badge {
  background-color: #FF5252;
  color: #ffffff;
  border-radius: 20rpx;
  padding: 4rpx 14rpx;
  font-size: 22rpx;
  min-width: 36rpx;
  text-align: center;
}

.filter-tabs {
  display: flex;
  background-color: #ffffff;
  padding: 10rpx 20rpx;
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.filter-tab {
  flex: 1;
  text-align: center;
  padding: 18rpx 0;
  border-radius: 20rpx;
  background-color: #f5f7fa;
  font-size: 26rpx;
  color: #666;
}

.filter-tab.active {
  background-color: #2979FF;
  color: #ffffff;
}

.notification-card {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.notification-card.unread {
  border-left: 6rpx solid #2979FF;
}

.card-left {
  flex-shrink: 0;
}

.notification-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-icon.warning {
  background-color: #FFF3E0;
}

.notification-icon.danger {
  background-color: #FFEBEE;
}

.notification-icon.info {
  background-color: #E3F2FD;
}

.notification-icon .emoji {
  font-size: 36rpx;
}

.card-right {
  flex: 1;
  min-width: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.status-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background-color: #FF5252;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mt-10 {
  margin-top: 10rpx;
}

.mt-15 {
  margin-top: 15rpx;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}
</style>
