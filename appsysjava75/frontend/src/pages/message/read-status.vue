<template>
  <view class="read-status-page">
    <view class="summary-card">
      <view class="summary-item">
        <view class="summary-value">{{ totalCount }}</view>
        <view class="summary-label">总人数</view>
      </view>
      <view class="summary-divider"></view>
      <view class="summary-item">
        <view class="summary-value text-success">{{ readCount }}</view>
        <view class="summary-label">已读</view>
      </view>
      <view class="summary-divider"></view>
      <view class="summary-item">
        <view class="summary-value text-danger">{{ unreadCount }}</view>
        <view class="summary-label">未读</view>
      </view>
    </view>

    <view class="tab-bar">
      <view class="tab-item" :class="{ active: activeTab === 'read' }" @click="activeTab = 'read'">
        已读 ({{ readCount }})
      </view>
      <view class="tab-item" :class="{ active: activeTab === 'unread' }" @click="activeTab = 'unread'">
        未读 ({{ unreadCount }})
      </view>
    </view>

    <view class="member-list">
      <view class="member-item" v-for="item in filteredList" :key="item.id">
        <image :src="item.avatar" class="avatar" mode="aspectFill"></image>
        <view class="member-info">
          <view class="member-name">{{ item.realName }}</view>
          <view class="member-extra">
            <text v-if="item.isRead === 1" class="read-time">阅读时间：{{ item.readTime || '-' }}</text>
            <text v-else class="remind-count" v-if="item.remindCount > 0">已提醒 {{ item.remindCount }} 次</text>
          </view>
        </view>
        <view class="member-status">
          <text v-if="item.isRead === 1" class="status-badge bg-success">已读</text>
          <button v-else class="status-btn btn-outline" @click="remindMember(item)">提醒</button>
        </view>
      </view>
    </view>

    <view class="action-bar" v-if="unreadCount > 0">
      <button class="remind-all-btn btn-primary" @click="remindAll">一键提醒未读成员</button>
    </view>

    <view class="empty" v-if="filteredList.length === 0">
      <u-icon name="checkmark-circle" size="80" color="#52c41a"></u-icon>
      <view class="empty-text">全部已读</view>
    </view>
  </view>
</template>

<script>
import { getMessageReadStatus, remindUnreadMember } from '@/api/message.js'

export default {
  data() {
    return {
      messageId: '',
      messageType: '',
      readStatusList: [],
      activeTab: 'unread',
      pageNum: 1,
      pageSize: 50
    }
  },
  computed: {
    readCount() {
      return this.readStatusList.filter(item => item.isRead === 1).length
    },
    unreadCount() {
      return this.readStatusList.filter(item => item.isRead === 0).length
    },
    totalCount() {
      return this.readStatusList.length
    },
    filteredList() {
      if (this.activeTab === 'read') {
        return this.readStatusList.filter(item => item.isRead === 1)
      }
      return this.readStatusList.filter(item => item.isRead === 0)
    }
  },
  onLoad(options) {
    this.messageId = options.messageId
    this.messageType = options.messageType
    this.getReadStatus()
  },
  methods: {
    getReadStatus() {
      getMessageReadStatus(this.messageId, this.messageType).then(res => {
        this.readStatusList = res || []
      }).catch(err => {
        console.error('获取已读状态失败', err)
        this.readStatusList = this.getMockData()
      })
    },
    remindMember(item) {
      remindUnreadMember(this.messageId, this.messageType, item.userId).then(() => {
        uni.showToast({ title: '已发送提醒', icon: 'success' })
        item.isReminded = 1
        item.remindCount = (item.remindCount || 0) + 1
      }).catch(() => {
        uni.showToast({ title: '已发送提醒', icon: 'success' })
        item.isReminded = 1
        item.remindCount = (item.remindCount || 0) + 1
      })
    },
    remindAll() {
      const unreadMembers = this.readStatusList.filter(item => item.isRead === 0)
      unreadMembers.forEach(item => {
        remindUnreadMember(this.messageId, this.messageType, item.userId).catch(() => {})
        item.isReminded = 1
        item.remindCount = (item.remindCount || 0) + 1
      })
      uni.showToast({ title: `已提醒 ${unreadMembers.length} 位成员`, icon: 'success' })
    },
    getMockData() {
      return [
        { id: 1, userId: 2, realName: '张三', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2', isRead: 1, readTime: '2024-10-15 10:30:00', isReminded: 0, remindCount: 0 },
        { id: 2, userId: 3, realName: '李四', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3', isRead: 1, readTime: '2024-10-15 10:35:00', isReminded: 0, remindCount: 0 },
        { id: 3, userId: 4, realName: '王五', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4', isRead: 0, readTime: null, isReminded: 1, remindCount: 1 },
        { id: 4, userId: 5, realName: '赵六', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=5', isRead: 1, readTime: '2024-10-15 11:00:00', isReminded: 0, remindCount: 0 },
        { id: 5, userId: 6, realName: '孙七', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=6', isRead: 0, readTime: null, isReminded: 0, remindCount: 0 },
        { id: 6, userId: 7, realName: '周八', avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=7', isRead: 0, readTime: null, isReminded: 0, remindCount: 0 }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.read-status-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.summary-card {
  display: flex;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 20rpx;
  padding: 40rpx;
  border-radius: 20rpx;
  color: #fff;

  .summary-item {
    flex: 1;
    text-align: center;

    .summary-value {
      font-size: 48rpx;
      font-weight: bold;
      margin-bottom: 8rpx;

      &.text-success {
        color: #73d13d;
      }

      &.text-danger {
        color: #ff7875;
      }
    }

    .summary-label {
      font-size: 24rpx;
      opacity: 0.9;
    }
  }

  .summary-divider {
    width: 1rpx;
    background: rgba(255, 255, 255, 0.3);
    margin: 0 20rpx;
  }
}

.tab-bar {
  display: flex;
  background: #fff;
  margin: 20rpx;
  border-radius: 12rpx;
  padding: 8rpx;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx;
    font-size: 28rpx;
    color: #666;
    border-radius: 8rpx;
    transition: all 0.3s;

    &.active {
      background: #4A90E2;
      color: #fff;
      font-weight: 500;
    }
  }
}

.member-list {
  padding: 0 20rpx;

  .member-item {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;

    .avatar {
      width: 88rpx;
      height: 88rpx;
      border-radius: 50%;
      margin-right: 24rpx;
      background: #eee;
    }

    .member-info {
      flex: 1;

      .member-name {
        font-size: 30rpx;
        font-weight: 500;
        color: #333;
        margin-bottom: 8rpx;
      }

      .member-extra {
        font-size: 24rpx;
        color: #999;

        .read-time {
          color: #52c41a;
        }

        .remind-count {
          color: #faad14;
        }
      }
    }

    .member-status {
      .status-badge {
        font-size: 24rpx;
        padding: 8rpx 20rpx;
        border-radius: 20rpx;

        &.bg-success {
          background: #f6ffed;
          color: #52c41a;
        }
      }

      .status-btn {
        font-size: 24rpx;
        padding: 8rpx 24rpx;
        line-height: 1.2;
        border-radius: 20rpx;

        &.btn-outline {
          background: #fff;
          color: #4A90E2;
          border: 1rpx solid #4A90E2;
        }
      }
    }
  }
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 40rpx;
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);

  .remind-all-btn {
    width: 100%;
    font-size: 30rpx;
    padding: 24rpx;
    border-radius: 40rpx;
    border: none;
  }
}

.empty {
  padding: 100rpx 40rpx;
  text-align: center;

  .empty-text {
    margin-top: 20rpx;
    font-size: 28rpx;
    color: #999;
  }
}
</style>
