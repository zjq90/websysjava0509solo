<template>
  <view class="message-page">
    <view class="header">
      <view class="header-title">消息通知</view>
      <view class="header-subtitle">及时了解重要信息</view>
    </view>

    <view class="quick-access">
      <view class="quick-item" @click="goToSystemMessage">
        <view class="quick-icon bg-primary">
          <u-icon name="notification" size="32" color="#fff"></u-icon>
        </view>
        <view class="quick-text">系统消息</view>
        <view class="unread-badge" v-if="systemUnread > 0">{{ systemUnread }}</view>
      </view>
      <view class="quick-item" @click="goToChatList">
        <view class="quick-icon bg-success">
          <u-icon name="chat" size="32" color="#fff"></u-icon>
        </view>
        <view class="quick-text">群聊</view>
        <view class="unread-badge" v-if="groupUnread > 0">{{ groupUnread }}</view>
      </view>
      <view class="quick-item" @click="goToPrivateChat">
        <view class="quick-icon bg-warning">
          <u-icon name="account" size="32" color="#fff"></u-icon>
        </view>
        <view class="quick-text">私聊</view>
        <view class="unread-badge" v-if="privateUnread > 0">{{ privateUnread }}</view>
      </view>
    </view>

    <view class="section-title">
      <text class="title">最近消息</text>
      <text class="more" @click="goToSystemMessage">查看全部</text>
    </view>

    <view class="message-list" v-if="messageList.length > 0">
      <view class="message-item" v-for="(item, index) in messageList" :key="item.id" @click="handleMessageClick(item)">
        <view class="message-icon" :style="{ backgroundColor: getMessageTypeColor(item.messageType) }">
          <u-icon :name="getMessageTypeIcon(item.messageType)" size="24" color="#fff"></u-icon>
        </view>
        <view class="message-content">
          <view class="message-header">
            <text class="message-type">{{ getMessageTypeName(item.messageType) }}</text>
            <text class="message-time">{{ formatTime(item.createTime) }}</text>
          </view>
          <view class="message-title">{{ item.title }}</view>
          <view class="message-desc">{{ item.content }}</view>
        </view>
        <view class="unread-dot" v-if="item.isRead === 0"></view>
      </view>
    </view>

    <view class="empty" v-else>
      <u-icon name="email" size="80" color="#ddd"></u-icon>
      <view class="empty-text">暂无消息</view>
    </view>
  </view>
</template>

<script>
import { getSystemMessageList, getUnreadMessageCount, getPrivateUnreadCount } from '@/api/message.js'
import { MESSAGE_TYPE_NAME, MESSAGE_TYPE_COLOR } from '@/utils/constants.js'

export default {
  data() {
    return {
      messageList: [],
      systemUnread: 0,
      privateUnread: 0,
      groupUnread: 0,
      pageNum: 1,
      pageSize: 10
    }
  },
  onShow() {
    this.getMessageList()
    this.getUnreadCount()
  },
  methods: {
    getMessageList() {
      const userId = uni.getStorageSync('userId') || 2
      getSystemMessageList({ userId, pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.messageList = res.list || []
      }).catch(err => {
        console.error('获取消息列表失败', err)
        this.messageList = this.getMockData()
      })
    },
    getUnreadCount() {
      const userId = uni.getStorageSync('userId') || 2
      Promise.all([
        getUnreadMessageCount(userId).catch(() => 3),
        getPrivateUnreadCount(userId).catch(() => 2)
      ]).then(res => {
        this.systemUnread = res[0] || 0
        this.privateUnread = res[1] || 0
        this.groupUnread = 5
      })
    },
    getMessageTypeName(type) {
      return MESSAGE_TYPE_NAME[type] || '系统通知'
    },
    getMessageTypeColor(type) {
      return MESSAGE_TYPE_COLOR[type] || '#4A90E2'
    },
    getMessageTypeIcon(type) {
      const icons = ['checkmark-circle', 'clock', 'volume', 'users', 'info-circle']
      return icons[type] || 'info-circle'
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      return time.substring(5, 16)
    },
    handleMessageClick(item) {
      uni.navigateTo({
        url: `/pages/message/system?id=${item.id}`
      })
    },
    goToSystemMessage() {
      uni.navigateTo({
        url: '/pages/message/system'
      })
    },
    goToChatList() {
      uni.navigateTo({
        url: '/pages/message/chat-list'
      })
    },
    goToPrivateChat() {
      uni.navigateTo({
        url: '/pages/message/private-chat?userId=3&name=李四'
      })
    },
    getMockData() {
      return [
        {
          id: 1,
          messageType: 0,
          title: '社团招新审核结果通知',
          content: '恭喜您！您申请加入的计算机协会已审核通过，请及时参加新人见面会。',
          createTime: '2024-10-15 10:30:00',
          isRead: 0
        },
        {
          id: 2,
          messageType: 1,
          title: '活动开始提醒',
          content: '您报名参加的"编程大赛培训"活动将于明天下午2点在计算机楼301室开始，请准时参加。',
          createTime: '2024-10-15 09:00:00',
          isRead: 0
        },
        {
          id: 3,
          messageType: 2,
          title: '社团公告',
          content: '计算机协会本周六将组织户外拓展活动，请各位成员准时参加！',
          createTime: '2024-10-14 18:00:00',
          isRead: 1
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.message-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding-bottom: 40rpx;
}

.header {
  padding: 60rpx 40rpx 40rpx;
  color: #fff;

  .header-title {
    font-size: 48rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
  }

  .header-subtitle {
    font-size: 28rpx;
    opacity: 0.9;
  }
}

.quick-access {
  display: flex;
  justify-content: space-around;
  background: #fff;
  margin: 0 30rpx 30rpx;
  border-radius: 24rpx;
  padding: 40rpx 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;

    .quick-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
    }

    .quick-text {
      font-size: 26rpx;
      color: #333;
    }

    .unread-badge {
      position: absolute;
      top: -10rpx;
      right: 20rpx;
      background: #f5222d;
      color: #fff;
      font-size: 20rpx;
      min-width: 36rpx;
      height: 36rpx;
      border-radius: 18rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 0 8rpx;
    }
  }
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 40rpx 20rpx;
  color: #fff;

  .title {
    font-size: 32rpx;
    font-weight: bold;
  }

  .more {
    font-size: 26rpx;
    opacity: 0.9;
  }
}

.message-list {
  background: #fff;
  margin: 0 30rpx;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .message-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
    position: relative;

    &:last-child {
      border-bottom: none;
    }

    .message-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 20rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 24rpx;
      flex-shrink: 0;
    }

    .message-content {
      flex: 1;
      overflow: hidden;

      .message-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8rpx;

        .message-type {
          font-size: 26rpx;
          color: #4A90E2;
          font-weight: 500;
        }

        .message-time {
          font-size: 22rpx;
          color: #999;
        }
      }

      .message-title {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
        margin-bottom: 6rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .message-desc {
        font-size: 24rpx;
        color: #999;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .unread-dot {
      width: 16rpx;
      height: 16rpx;
      border-radius: 50%;
      background: #f5222d;
      margin-left: 16rpx;
      flex-shrink: 0;
    }
  }
}

.empty {
  background: #fff;
  margin: 0 30rpx;
  border-radius: 24rpx;
  padding: 80rpx 40rpx;
  text-align: center;

  .empty-text {
    margin-top: 20rpx;
    font-size: 28rpx;
    color: #999;
  }
}
</style>
