<template>
  <view class="message-container">
    <view class="tabs">
      <view 
        class="tab" 
        :class="{ active: activeTab === 'all' }"
        @click="activeTab = 'all'"
      >
        全部
      </view>
      <view 
        class="tab" 
        :class="{ active: activeTab === 'system' }"
        @click="activeTab = 'system'"
      >
        系统通知
      </view>
      <view 
        class="tab" 
        :class="{ active: activeTab === 'trade' }"
        @click="activeTab = 'trade'"
      >
        交易消息
      </view>
    </view>

    <view class="message-list">
      <view 
        class="message-item" 
        v-for="msg in filteredMessages" 
        :key="msg.id"
        @click="handleMessageClick(msg)"
      >
        <view class="message-icon" :class="msg.type">
          <text>{{ msg.icon }}</text>
        </view>
        <view class="message-content">
          <view class="message-header">
            <text class="message-title">{{ msg.title }}</text>
            <text class="message-time">{{ msg.createTime }}</text>
          </view>
          <text class="message-desc">{{ msg.content }}</text>
        </view>
        <view class="unread-dot" v-if="!msg.isRead"></view>
      </view>
    </view>

    <view class="empty-state" v-if="filteredMessages.length === 0">
      <text class="empty-icon">📭</text>
      <text class="empty-text">暂无消息</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      messages: [
        {
          id: 1,
          type: 'trade',
          icon: '🛒',
          title: '订单状态更新',
          content: '您购买的iPhone 13已发货，请注意查收',
          createTime: '10分钟前',
          isRead: false
        },
        {
          id: 2,
          type: 'system',
          icon: '🔔',
          title: '系统通知',
          content: '您的实名认证已通过，信用分提升至100分',
          createTime: '1小时前',
          isRead: false
        },
        {
          id: 3,
          type: 'trade',
          icon: '💰',
          title: '收款通知',
          content: '您出售的MacBook Pro已确认收货，款项已到账',
          createTime: '2小时前',
          isRead: true
        },
        {
          id: 4,
          type: 'system',
          icon: '📢',
          title: '活动通知',
          content: '二手狂欢节即将开始，百万商品5折起',
          createTime: '昨天',
          isRead: true
        },
        {
          id: 5,
          type: 'trade',
          icon: '💬',
          title: '买家留言',
          content: '买家"小明"给您留言：请问这个还能优惠吗？',
          createTime: '昨天',
          isRead: true
        }
      ]
    }
  },
  computed: {
    filteredMessages() {
      if (this.activeTab === 'all') {
        return this.messages
      }
      return this.messages.filter(msg => msg.type === this.activeTab)
    }
  },
  methods: {
    handleMessageClick(msg) {
      msg.isRead = true
      uni.showToast({
        title: '已标记为已读',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.message-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 30rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
  font-size: 30rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab.active {
  color: #409EFF;
  border-bottom-color: #409EFF;
  font-weight: bold;
}

.message-list {
  padding: 20rpx;
}

.message-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  position: relative;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.message-icon {
  width: 90rpx;
  height: 90rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-right: 25rpx;
  flex-shrink: 0;
}

.message-icon.trade {
  background-color: #ECF5FF;
}

.message-icon.system {
  background-color: #FEF0F0;
}

.message-content {
  flex: 1;
  min-width: 0;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.message-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
}

.message-time {
  font-size: 24rpx;
  color: #999;
  flex-shrink: 0;
  margin-left: 20rpx;
}

.message-desc {
  display: block;
  font-size: 26rpx;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.unread-dot {
  position: absolute;
  top: 35rpx;
  right: 35rpx;
  width: 16rpx;
  height: 16rpx;
  background-color: #ff4d4f;
  border-radius: 50%;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #999;
}
</style>
