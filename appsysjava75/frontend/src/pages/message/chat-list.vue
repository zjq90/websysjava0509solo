<template>
  <view class="chat-list-page">
    <view class="search-bar">
      <u-input placeholder="搜索群聊" v-model="searchKeyword" border="none" shape="round" :clearable="true">
        <template #prefix>
          <u-icon name="search" size="18" color="#999"></u-icon>
        </template>
      </u-input>
    </view>

    <view class="chat-list">
      <view class="chat-item" v-for="item in filteredList" :key="item.id" @click="goToChat(item)">
        <image :src="item.groupAvatar" class="chat-avatar" mode="aspectFill"></image>
        <view class="chat-info">
          <view class="chat-header">
            <text class="chat-name">{{ item.groupName }}</text>
            <text class="chat-time">{{ formatTime(item.lastMessageTime) }}</text>
          </view>
          <view class="chat-preview">
            <text class="last-message">{{ item.lastMessage }}</text>
            <view class="unread-badge" v-if="item.unreadCount > 0">{{ item.unreadCount }}</view>
          </view>
        </view>
      </view>
    </view>

    <view class="empty" v-if="filteredList.length === 0">
      <u-icon name="chat" size="80" color="#ddd"></u-icon>
      <view class="empty-text">暂无群聊</view>
    </view>
  </view>
</template>

<script>
import { getChatGroupList } from '@/api/message.js'

export default {
  data() {
    return {
      searchKeyword: '',
      chatList: [],
      pageNum: 1,
      pageSize: 20
    }
  },
  computed: {
    filteredList() {
      if (!this.searchKeyword) return this.chatList
      return this.chatList.filter(item =>
        item.groupName.includes(this.searchKeyword)
      )
    }
  },
  onLoad() {
    this.getChatList()
  },
  methods: {
    getChatList() {
      const userId = uni.getStorageSync('userId') || 2
      getChatGroupList({ userId, pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.chatList = res.list || []
      }).catch(err => {
        console.error('获取群聊列表失败', err)
        this.chatList = this.getMockData()
      })
    },
    goToChat(item) {
      uni.navigateTo({
        url: `/pages/message/chat?groupId=${item.id}&groupName=${item.groupName}`
      })
    },
    formatTime(time) {
      if (!time) return ''
      return time.substring(11, 16)
    },
    getMockData() {
      return [
        {
          id: 1,
          groupName: '计算机协会交流群',
          groupAvatar: 'https://api.dicebear.com/7.x/icons/svg?seed=group1',
          lastMessage: '大家好，欢迎加入群聊！',
          lastMessageTime: '2024-10-15 14:30:00',
          unreadCount: 5
        },
        {
          id: 2,
          groupName: '计算机协会-技术部',
          groupAvatar: 'https://api.dicebear.com/7.x/icons/svg?seed=group2',
          lastMessage: '下周的技术分享会准备好了吗？',
          lastMessageTime: '2024-10-15 12:15:00',
          unreadCount: 3
        },
        {
          id: 3,
          groupName: '科技创新社官方群',
          groupAvatar: 'https://api.dicebear.com/7.x/icons/svg?seed=group3',
          lastMessage: '比赛报名截止时间快到了',
          lastMessageTime: '2024-10-14 20:00:00',
          unreadCount: 0
        },
        {
          id: 4,
          groupName: '文学社交流群',
          groupAvatar: 'https://api.dicebear.com/7.x/icons/svg?seed=group4',
          lastMessage: '本周的读书会很精彩',
          lastMessageTime: '2024-10-14 18:30:00',
          unreadCount: 0
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-list-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.search-bar {
  padding: 20rpx;
  background: #fff;
}

.chat-list {
  padding: 20rpx;

  .chat-item {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 16rpx;

    .chat-avatar {
      width: 96rpx;
      height: 96rpx;
      border-radius: 20rpx;
      margin-right: 24rpx;
      background: #f0f0f0;
    }

    .chat-info {
      flex: 1;
      overflow: hidden;

      .chat-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12rpx;

        .chat-name {
          font-size: 30rpx;
          font-weight: 500;
          color: #333;
        }

        .chat-time {
          font-size: 22rpx;
          color: #999;
        }
      }

      .chat-preview {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .last-message {
          font-size: 26rpx;
          color: #999;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          flex: 1;
          margin-right: 16rpx;
        }

        .unread-badge {
          background: #f5222d;
          color: #fff;
          font-size: 20rpx;
          min-width: 36rpx;
          height: 36rpx;
          border-radius: 18rpx;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 0 10rpx;
          flex-shrink: 0;
        }
      }
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
