<template>
  <view class="message-container">
    <view class="header-bar">
      <text class="header-title">系统消息</text>
      <view class="header-actions">
        <text class="action-btn" @click="markAllAsRead">全部已读</text>
      </view>
    </view>
    
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
    
    <view class="load-more" v-if="hasMore && !loading" @click="loadMore">
      <text>加载更多</text>
    </view>
    
    <view class="message-detail-modal" v-if="showDetail" @click="showDetail = false">
      <view class="detail-content" @click.stop>
        <view class="detail-header">
          <text class="detail-title">{{ currentMessage?.title }}</text>
          <text class="detail-close" @click="showDetail = false">×</text>
        </view>
        <view class="detail-body">
          <view class="detail-meta">
            <text class="meta-type" :class="currentMessage?.type">
              {{ util.getMessageTypeText(currentMessage?.type) }}
            </text>
            <text class="meta-time">{{ util.formatDateTime(currentMessage?.createTime) }}</text>
          </view>
          <text class="detail-text">{{ currentMessage?.content }}</text>
        </view>
        <view class="detail-footer">
          <button class="detail-btn" @click="handleDetailAction">
            {{ getActionText() }}
          </button>
        </view>
      </view>
    </view>
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
      pageNum: 1,
      pageSize: 20,
      hasMore: true,
      loading: false,
      showDetail: false,
      currentMessage: null
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
    this.refreshList()
  },
  onPullDownRefresh() {
    this.refreshList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  watch: {
    activeType() {
      this.refreshList()
    }
  },
  methods: {
    util,
    
    getTypeIcon(type) {
      const icons = {
        system: '🔔',
        activity: '📅',
        club: '🏫',
        chat: '💬'
      }
      return icons[type] || '📧'
    },
    
    async refreshList() {
      this.pageNum = 1
      this.hasMore = true
      this.loading = true
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        if (this.activeType !== 'all') {
          params.type = this.activeType
        }
        const res = await api.getMessagePage(params)
        this.messageList = res.data.list || []
        this.hasMore = this.pageNum < res.data.totalPage
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async loadMore() {
      this.pageNum++
      this.loading = true
      try {
        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
        if (this.activeType !== 'all') {
          params.type = this.activeType
        }
        const res = await api.getMessagePage(params)
        this.messageList = this.messageList.concat(res.data.list || [])
        this.hasMore = this.pageNum < res.data.totalPage
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async viewMessage(msg) {
      try {
        await api.markAsRead(msg.id)
        msg.status = 1
      } catch (e) {
        console.error(e)
      }
      
      this.currentMessage = msg
      this.showDetail = true
    },
    
    async markAllAsRead() {
      try {
        const params = {}
        if (this.activeType !== 'all') {
          params.type = this.activeType
        }
        await api.markAllAsRead(this.activeType === 'all' ? null : this.activeType)
        this.messageList.forEach(msg => {
          if (this.activeType === 'all' || msg.type === this.activeType) {
            msg.status = 1
          }
        })
        util.toast('已全部标记为已读')
      } catch (e) {
        console.error(e)
      }
    },
    
    getActionText() {
      if (!this.currentMessage) return '关闭'
      switch (this.currentMessage.type) {
        case 'activity':
          return '查看活动'
        case 'club':
          return '查看社团'
        default:
          return '我知道了'
      }
    },
    
    handleDetailAction() {
      this.showDetail = false
      if (!this.currentMessage) return
      
      switch (this.currentMessage.type) {
        case 'activity':
          if (this.currentMessage.businessId) {
            uni.navigateTo({ 
              url: '/pages/activity/detail?id=' + this.currentMessage.businessId 
            })
          }
          break
        case 'club':
          if (this.currentMessage.businessId) {
            uni.navigateTo({ 
              url: '/pages/club/detail?id=' + this.currentMessage.businessId 
            })
          }
          break
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

.header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 24rpx 30rpx;
}

.header-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.action-btn {
  font-size: 28rpx;
  color: #5677fc;
}

.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 20rpx;
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
  margin-bottom: 16rpx;
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
  background: #fff3e0;
}

.msg-icon.chat {
  background: #fce4ec;
}

.msg-content {
  flex: 1;
  overflow: hidden;
}

.msg-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8rpx;
}

.msg-type {
  font-size: 26rpx;
  font-weight: 600;
  color: #333;
}

.msg-time {
  font-size: 22rpx;
  color: #999;
}

.msg-title {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 6rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.msg-summary {
  display: block;
  font-size: 24rpx;
  color: #999;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-dot {
  position: absolute;
  top: 24rpx;
  right: 60rpx;
  width: 16rpx;
  height: 16rpx;
  background: #dd524d;
  border-radius: 50%;
}

.arrow {
  font-size: 32rpx;
  color: #ccc;
  margin-left: 10rpx;
}

.empty-state,
.loading-state,
.load-more {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}

.empty-icon {
  display: block;
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.load-more {
  color: #5677fc;
}

.message-detail-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.detail-content {
  width: 600rpx;
  max-height: 80vh;
  background: #fff;
  border-radius: 20rpx;
  display: flex;
  flex-direction: column;
}

.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.detail-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  flex: 1;
}

.detail-close {
  font-size: 48rpx;
  color: #999;
  line-height: 1;
  padding: 0 10rpx;
}

.detail-body {
  flex: 1;
  overflow-y: auto;
  padding: 30rpx;
}

.detail-meta {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.meta-type {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  margin-right: 16rpx;
}

.meta-type.system {
  background: #e3f2fd;
  color: #1976d2;
}

.meta-type.activity {
  background: #e8f5e9;
  color: #388e3c;
}

.meta-type.club {
  background: #fff3e0;
  color: #f57c00;
}

.meta-time {
  font-size: 24rpx;
  color: #999;
}

.detail-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.8;
}

.detail-footer {
  padding: 24rpx 30rpx;
  border-top: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.detail-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}
</style>
