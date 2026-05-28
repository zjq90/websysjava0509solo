<template>
  <view class="chat-container">
    <view class="header-bar">
      <text class="header-title">消息</text>
      <view class="header-actions">
        <text class="action-icon" @click="goMessage">🔔</text>
        <text v-if="unreadCount > 0" class="badge">{{ unreadCount > 99 ? '99+' : unreadCount }}</text>
      </view>
    </view>
    
    <view class="chat-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'chat' }"
        @click="activeTab = 'chat'"
      >
        群聊
        <text v-if="chatUnreadCount > 0" class="tab-badge">{{ chatUnreadCount }}</text>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'message' }"
        @click="activeTab = 'message'"
      >
        通知
      </view>
    </view>
    
    <view v-if="activeTab === 'chat'">
      <view class="chat-list" v-if="clubList.length > 0">
        <view 
          class="chat-item" 
          v-for="club in clubList" 
          :key="club.id"
          @click="goChatDetail(club)"
        >
          <view class="chat-avatar">
            <image v-if="club.logo" :src="club.logo" class="logo-img" mode="aspectFill" />
            <view v-else class="avatar-placeholder">
              <text>{{ club.name.substring(0, 1) }}</text>
            </view>
            <text v-if="club.unreadCount > 0" class="unread-badge">{{ club.unreadCount > 99 ? '99+' : club.unreadCount }}</text>
          </view>
          <view class="chat-info">
            <view class="chat-header">
              <text class="chat-name">{{ club.name }}</text>
              <text class="chat-time">{{ club.lastTime ? util.fromNow(club.lastTime) : '' }}</text>
            </view>
            <text class="chat-preview">{{ club.lastMessage || '暂无消息' }}</text>
          </view>
        </view>
      </view>
      <view class="empty-state" v-else-if="!loading">
        <text class="empty-icon">💬</text>
        <text class="empty-text">还没有加入任何社团</text>
        <button class="join-btn" @click="goIndex">去加入社团</button>
      </view>
    </view>
    
    <view v-else>
      <view class="message-list" v-if="messageList.length > 0">
        <view 
          class="message-item" 
          v-for="msg in messageList" 
          :key="msg.id"
          @click="goMessageDetail(msg)"
        >
          <view class="msg-icon" :class="msg.type">
            {{ getTypeIcon(msg.type) }}
          </view>
          <view class="msg-content">
            <view class="msg-header">
              <text class="msg-title">{{ util.getMessageTypeText(msg.type) }}</text>
              <text class="msg-time">{{ util.fromNow(msg.createTime) }}</text>
            </view>
            <text class="msg-text">{{ msg.title }}</text>
            <view v-if="msg.status === 0" class="unread-dot"></view>
          </view>
        </view>
      </view>
      <view class="empty-state" v-else-if="!loading">
        <text class="empty-icon">🔕</text>
        <text class="empty-text">暂无通知消息</text>
      </view>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activeTab: 'chat',
      clubList: [],
      messageList: [],
      chatUnreadCount: 0,
      loading: false
    }
  },
  computed: {
    ...mapGetters(['unreadCount'])
  },
  onShow() {
    this.loadData()
    this.fetchUnreadCount()
  },
  methods: {
    util,
    ...mapActions(['fetchUnreadCount']),
    
    getTypeIcon(type) {
      const icons = {
        system: '🔔',
        activity: '📅',
        club: '🏫',
        chat: '💬'
      }
      return icons[type] || '📧'
    },
    
    async loadData() {
      this.loading = true
      try {
        const [clubsRes, msgRes] = await Promise.all([
          api.getMyClubs(),
          api.getMessagePage({ pageNum: 1, pageSize: 20 })
        ])
        
        this.clubList = clubsRes.data || []
        for (const club of this.clubList) {
          try {
            const unreadRes = await api.getChatUnreadCount(club.id)
            club.unreadCount = unreadRes.data || 0
            this.chatUnreadCount += club.unreadCount
          } catch (e) {
            club.unreadCount = 0
          }
        }
        
        this.messageList = msgRes.data.list || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    goChatDetail(club) {
      uni.navigateTo({ 
        url: '/pages/chat/detail?clubId=' + club.id + '&clubName=' + encodeURIComponent(club.name) 
      })
    },
    
    goMessageDetail(msg) {
      uni.navigateTo({ url: '/pages/message/index' })
    },
    
    goMessage() {
      uni.navigateTo({ url: '/pages/message/index' })
    },
    
    goIndex() {
      uni.switchTab({ url: '/pages/index/index' })
    }
  }
}
</script>

<style scoped>
.chat-container {
  min-height: 100vh;
  background: #f5f6f8;
}

.header-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.header-actions {
  position: relative;
  padding: 10rpx;
}

.action-icon {
  font-size: 40rpx;
}

.badge {
  position: absolute;
  top: 0;
  right: 0;
  min-width: 32rpx;
  height: 32rpx;
  background: #dd524d;
  color: #fff;
  border-radius: 16rpx;
  font-size: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.chat-tabs {
  display: flex;
  background: #fff;
  padding: 0 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.tab-item {
  position: relative;
  padding: 25rpx 40rpx;
  font-size: 30rpx;
  color: #666;
  margin-right: 20rpx;
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
  width: 50rpx;
  height: 6rpx;
  background: #5677fc;
  border-radius: 3rpx;
}

.tab-badge {
  position: absolute;
  top: 15rpx;
  right: 15rpx;
  min-width: 30rpx;
  height: 30rpx;
  background: #dd524d;
  color: #fff;
  border-radius: 15rpx;
  font-size: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 6rpx;
}

.chat-list {
  background: #fff;
  margin-top: 20rpx;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 25rpx 30rpx;
  border-bottom: 1rpx solid #f8f8f8;
}

.chat-item:last-child {
  border-bottom: none;
}

.chat-avatar {
  position: relative;
  width: 90rpx;
  height: 90rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.logo-img {
  width: 100%;
  height: 100%;
  border-radius: 18rpx;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 18rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}

.unread-badge {
  position: absolute;
  top: -8rpx;
  right: -8rpx;
  min-width: 36rpx;
  height: 36rpx;
  background: #dd524d;
  color: #fff;
  border-radius: 18rpx;
  font-size: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.chat-info {
  flex: 1;
  overflow: hidden;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8rpx;
}

.chat-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.chat-time {
  font-size: 24rpx;
  color: #999;
}

.chat-preview {
  font-size: 26rpx;
  color: #999;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.message-list {
  background: #fff;
  margin-top: 20rpx;
}

.message-item {
  display: flex;
  align-items: flex-start;
  padding: 25rpx 30rpx;
  border-bottom: 1rpx solid #f8f8f8;
  position: relative;
}

.message-item:last-child {
  border-bottom: none;
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

.msg-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.msg-time {
  font-size: 24rpx;
  color: #999;
}

.msg-text {
  font-size: 26rpx;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.unread-dot {
  position: absolute;
  top: 35rpx;
  right: 30rpx;
  width: 16rpx;
  height: 16rpx;
  background: #dd524d;
  border-radius: 50%;
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
  margin-bottom: 30rpx;
}

.join-btn {
  padding: 15rpx 50rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
