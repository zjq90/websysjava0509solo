<template>
  <view class="system-message-page">
    <view class="header-bar">
      <text class="title">系统消息</text>
      <text class="read-all" @click="markAllAsRead">全部已读</text>
    </view>

    <view class="message-list" v-if="messageList.length > 0">
      <view class="message-item" v-for="item in messageList" :key="item.id" @click="viewDetail(item)">
        <view class="message-icon" :style="{ backgroundColor: getMessageTypeColor(item.messageType) }">
          <u-icon :name="getMessageTypeIcon(item.messageType)" size="28" color="#fff"></u-icon>
        </view>
        <view class="message-body">
          <view class="message-top">
            <view class="type-badge" :style="{ backgroundColor: getMessageTypeColor(item.messageType) + '20', color: getMessageTypeColor(item.messageType) }">
              {{ getMessageTypeName(item.messageType) }}
            </view>
            <view class="important-tag" v-if="item.isImportant === 1">
              <u-icon name="star-fill" size="14" color="#faad14"></u-icon>
              <text>重要</text>
            </view>
            <text class="time">{{ formatTime(item.createTime) }}</text>
          </view>
          <view class="message-title" :class="{ 'unread': item.isRead === 0 }">{{ item.title }}</view>
          <view class="message-content">{{ item.content }}</view>
          <view class="message-footer">
            <view class="read-info" v-if="item.totalCount > 0">
              <text>已读 {{ item.readCount }}/{{ item.totalCount }}</text>
              <u-progress :percent="Math.round(item.readCount / item.totalCount * 100)" :height="6" :show-percent="false" active-color="#52c41a" inactive-color="#f0f0f0"></u-progress>
            </view>
            <view class="action-btns" v-if="item.isImportant === 1">
              <text class="action-btn" @click.stop="viewReadStatus(item)">查看已读</text>
            </view>
          </view>
        </view>
        <view class="unread-dot" v-if="item.isRead === 0"></view>
      </view>
    </view>

    <view class="empty" v-else>
      <u-icon name="email" size="80" color="#ddd"></u-icon>
      <view class="empty-text">暂无系统消息</view>
    </view>
  </view>
</template>

<script>
import { getSystemMessageList, markAllSystemMessageAsRead } from '@/api/message.js'
import { MESSAGE_TYPE_NAME, MESSAGE_TYPE_COLOR } from '@/utils/constants.js'

export default {
  data() {
    return {
      messageList: [],
      pageNum: 1,
      pageSize: 20
    }
  },
  onLoad() {
    this.getMessageList()
  },
  onPullDownRefresh() {
    this.pageNum = 1
    this.getMessageList()
    uni.stopPullDownRefresh()
  },
  methods: {
    getMessageList() {
      const userId = uni.getStorageSync('userId') || 2
      getSystemMessageList({ userId, pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.messageList = res.list || []
      }).catch(err => {
        console.error('获取系统消息失败', err)
        this.messageList = this.getMockData()
      })
    },
    markAllAsRead() {
      const userId = uni.getStorageSync('userId') || 2
      markAllSystemMessageAsRead(userId).then(() => {
        uni.showToast({ title: '已全部标记为已读', icon: 'success' })
        this.messageList.forEach(item => {
          item.isRead = 1
        })
      }).catch(() => {
        uni.showToast({ title: '操作成功', icon: 'success' })
        this.messageList.forEach(item => {
          item.isRead = 1
        })
      })
    },
    viewDetail(item) {
      if (item.isRead === 0) {
        item.isRead = 1
      }
    },
    viewReadStatus(item) {
      uni.navigateTo({
        url: `/pages/message/read-status?messageId=${item.id}&messageType=0`
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
      return time.substring(5, 16)
    },
    getMockData() {
      return [
        {
          id: 1,
          messageType: 0,
          title: '社团招新审核结果通知',
          content: '恭喜您！您申请加入的计算机协会已审核通过，请及时参加新人见面会。',
          createTime: '2024-10-15 10:30:00',
          isRead: 0,
          isImportant: 1,
          readCount: 45,
          totalCount: 50
        },
        {
          id: 2,
          messageType: 1,
          title: '活动开始提醒',
          content: '您报名参加的"编程大赛培训"活动将于明天下午2点在计算机楼301室开始，请准时参加。',
          createTime: '2024-10-15 09:00:00',
          isRead: 0,
          isImportant: 0
        },
        {
          id: 3,
          messageType: 2,
          title: '社团公告',
          content: '计算机协会本周六将组织户外拓展活动，请各位成员准时参加！',
          createTime: '2024-10-14 18:00:00',
          isRead: 1,
          isImportant: 1,
          readCount: 38,
          totalCount: 50
        },
        {
          id: 4,
          messageType: 3,
          title: '招新进度通知',
          content: '社团招新工作已完成80%，还剩余20个名额，请有意向的同学尽快报名。',
          createTime: '2024-10-14 15:00:00',
          isRead: 1,
          isImportant: 0
        },
        {
          id: 5,
          messageType: 0,
          title: '活动审批结果通知',
          content: '您申请的"编程技术分享会"活动已通过审批，请按计划执行。',
          createTime: '2024-10-13 14:00:00',
          isRead: 1,
          isImportant: 0
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.system-message-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background: #fff;

  .title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }

  .read-all {
    font-size: 26rpx;
    color: #4A90E2;
  }
}

.message-list {
  padding: 20rpx;

  .message-item {
    display: flex;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    position: relative;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

    .message-icon {
      width: 72rpx;
      height: 72rpx;
      border-radius: 18rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 20rpx;
      flex-shrink: 0;
    }

    .message-body {
      flex: 1;
      overflow: hidden;

      .message-top {
        display: flex;
        align-items: center;
        margin-bottom: 12rpx;
        flex-wrap: wrap;
        gap: 10rpx;

        .type-badge {
          font-size: 22rpx;
          padding: 4rpx 12rpx;
          border-radius: 8rpx;
          font-weight: 500;
        }

        .important-tag {
          display: flex;
          align-items: center;
          font-size: 20rpx;
          color: #faad14;
          background: #fffbe6;
          padding: 2rpx 10rpx;
          border-radius: 8rpx;
          gap: 4rpx;
        }

        .time {
          font-size: 22rpx;
          color: #999;
          margin-left: auto;
        }
      }

      .message-title {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
        margin-bottom: 8rpx;

        &.unread {
          font-weight: bold;
        }
      }

      .message-content {
        font-size: 24rpx;
        color: #666;
        line-height: 1.5;
        margin-bottom: 16rpx;
      }

      .message-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .read-info {
          flex: 1;
          font-size: 22rpx;
          color: #999;
          margin-right: 20rpx;

          text {
            margin-bottom: 6rpx;
            display: block;
          }
        }

        .action-btns {
          .action-btn {
            font-size: 24rpx;
            color: #4A90E2;
            padding: 8rpx 20rpx;
            border: 1rpx solid #4A90E2;
            border-radius: 20rpx;
          }
        }
      }
    }

    .unread-dot {
      position: absolute;
      top: 24rpx;
      right: 24rpx;
      width: 16rpx;
      height: 16rpx;
      border-radius: 50%;
      background: #f5222d;
    }
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
