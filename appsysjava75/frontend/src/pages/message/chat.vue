<template>
  <view class="chat-page">
    <view class="chat-messages" ref="messageContainer" id="messageContainer">
      <view class="message-item" v-for="(msg, index) in messageList" :key="msg.id" :class="msg.senderId === currentUserId ? 'self' : 'other'">
        <image v-if="msg.senderId !== currentUserId" :src="msg.senderAvatar" class="avatar" mode="aspectFill"></image>
        <view class="message-content">
          <text v-if="msg.senderId !== currentUserId" class="sender-name">{{ msg.senderName }}</text>
          <view class="message-bubble" :class="msg.messageType === 1 ? 'image-bubble' : ''">
            <image v-if="msg.messageType === 1" :src="msg.fileUrl" class="message-image" mode="widthFix" @click="previewImage(msg.fileUrl)"></image>
            <text v-else class="message-text">{{ msg.content }}</text>
          </view>
          <text class="message-time">{{ formatTime(msg.createTime) }}</text>
        </view>
        <image v-if="msg.senderId === currentUserId" :src="currentUserAvatar" class="avatar" mode="aspectFill"></image>
      </view>

      <view class="read-status" v-if="showReadStatus">
        <view class="status-text">已读 {{ currentReadCount }}/{{ currentTotalCount }}</view>
        <view class="view-all" @click="viewReadStatus">查看详情</view>
      </view>
    </view>

    <view class="input-bar">
      <view class="input-wrapper">
        <u-input v-model="inputMessage" placeholder="输入消息..." border="none" :adjust-position="true" />
      </view>
      <view class="action-btn" @click="chooseImage">
        <u-icon name="photograph" size="28" color="#666"></u-icon>
      </view>
      <button class="send-btn" :disabled="!inputMessage.trim()" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script>
import { getGroupMessageList, sendGroupMessage, markGroupMessageAsRead } from '@/api/message.js'

export default {
  data() {
    return {
      groupId: '',
      groupName: '',
      messageList: [],
      inputMessage: '',
      currentUserId: 2,
      currentUserAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
      currentReadCount: 0,
      currentTotalCount: 6,
      showReadStatus: false,
      pageNum: 1,
      pageSize: 30
    }
  },
  onLoad(options) {
    this.groupId = options.groupId
    this.groupName = options.groupName
    uni.setNavigationBarTitle({ title: this.groupName })
    this.currentUserId = uni.getStorageSync('userId') || 2
    this.getMessageList()
    this.markAsRead()
  },
  methods: {
    getMessageList() {
      getGroupMessageList(this.groupId, { pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.messageList = (res.list || []).reverse()
        this.scrollToBottom()
      }).catch(err => {
        console.error('获取群消息失败', err)
        this.messageList = this.getMockData()
        this.scrollToBottom()
      })
    },
    markAsRead() {
      markGroupMessageAsRead(this.groupId, this.currentUserId).catch(() => {})
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return

      const messageData = {
        groupId: this.groupId,
        senderId: this.currentUserId,
        senderName: '张三',
        senderAvatar: this.currentUserAvatar,
        messageType: 0,
        content: this.inputMessage.trim()
      }

      sendGroupMessage(messageData).then(res => {
        this.messageList.push(res)
        this.inputMessage = ''
        this.scrollToBottom()
      }).catch(() => {
        this.messageList.push({
          id: Date.now(),
          ...messageData,
          readCount: 0,
          totalCount: 6
        })
        this.inputMessage = ''
        this.scrollToBottom()
      })
    },
    chooseImage() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          const messageData = {
            groupId: this.groupId,
            senderId: this.currentUserId,
            senderName: '张三',
            senderAvatar: this.currentUserAvatar,
            messageType: 1,
            fileUrl: res.tempFilePaths[0],
            content: '[图片]'
          }
          this.messageList.push(messageData)
          this.scrollToBottom()
        }
      })
    },
    previewImage(url) {
      uni.previewImage({
        urls: [url]
      })
    },
    formatTime(time) {
      if (!time) return ''
      return time.substring(11, 16)
    },
    scrollToBottom() {
      this.$nextTick(() => {
        uni.pageScrollTo({
          scrollTop: 999999,
          duration: 100
        })
      })
    },
    viewReadStatus() {
      uni.navigateTo({
        url: `/pages/message/read-status?messageId=${this.groupId}&messageType=1`
      })
    },
    getMockData() {
      const mockData = [
        {
          id: 1,
          senderId: 3,
          senderName: '李四',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          messageType: 0,
          content: '大家好，新人报到，请多关照！',
          createTime: '2024-10-15 14:30:00',
          readCount: 6,
          totalCount: 6
        },
        {
          id: 2,
          senderId: 4,
          senderName: '王五',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4',
          messageType: 0,
          content: '欢迎欢迎！我们正在准备下周的编程大赛，有兴趣参加吗？',
          createTime: '2024-10-15 14:32:00',
          readCount: 5,
          totalCount: 6
        },
        {
          id: 3,
          senderId: 5,
          senderName: '赵六',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=5',
          messageType: 1,
          fileUrl: 'https://picsum.photos/400/300?random=1',
          content: '[图片]',
          createTime: '2024-10-15 14:35:00',
          readCount: 4,
          totalCount: 6
        },
        {
          id: 4,
          senderId: 2,
          senderName: '张三',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          messageType: 0,
          content: '我想参加，需要准备什么吗？',
          createTime: '2024-10-15 14:40:00',
          readCount: 3,
          totalCount: 6
        },
        {
          id: 5,
          senderId: 3,
          senderName: '李四',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          messageType: 0,
          content: '只需要带笔记本电脑就可以了，我把资料发到群文件里',
          createTime: '2024-10-15 14:42:00',
          readCount: 2,
          totalCount: 6
        }
      ]
      return mockData
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f5f5;
}

.chat-messages {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;

  .message-item {
    display: flex;
    margin-bottom: 30rpx;
    align-items: flex-start;

    &.self {
      flex-direction: row-reverse;

      .message-content {
        align-items: flex-end;

        .message-bubble {
          background: #4A90E2;
          color: #fff;
          border-radius: 24rpx 24rpx 4rpx 24rpx;
        }

        .message-text {
          color: #fff;
        }
      }
    }

    &.other {
      .message-content {
        align-items: flex-start;

        .message-bubble {
          background: #fff;
          color: #333;
          border-radius: 24rpx 24rpx 24rpx 4rpx;
        }
      }
    }

    .avatar {
      width: 72rpx;
      height: 72rpx;
      border-radius: 50%;
      margin: 0 16rpx;
      background: #eee;
    }

    .message-content {
      display: flex;
      flex-direction: column;
      max-width: 70%;

      .sender-name {
        font-size: 22rpx;
        color: #999;
        margin-bottom: 6rpx;
        padding-left: 8rpx;
      }

      .message-bubble {
        padding: 20rpx 24rpx;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

        &.image-bubble {
          padding: 8rpx;
          background: transparent;
          box-shadow: none;
        }

        .message-image {
          max-width: 400rpx;
          border-radius: 16rpx;
        }

        .message-text {
          font-size: 28rpx;
          line-height: 1.5;
          word-break: break-all;
        }
      }

      .message-time {
        font-size: 20rpx;
        color: #ccc;
        margin-top: 6rpx;
        padding: 0 8rpx;
      }
    }
  }

  .read-status {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    background: #f0f7ff;
    margin: 20rpx;
    border-radius: 12rpx;

    .status-text {
      font-size: 24rpx;
      color: #4A90E2;
    }

    .view-all {
      font-size: 24rpx;
      color: #4A90E2;
    }
  }
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;
  border-top: 1rpx solid #eee;

  .input-wrapper {
    flex: 1;
    background: #f5f5f5;
    border-radius: 40rpx;
    padding: 10rpx 24rpx;
    margin-right: 16rpx;
  }

  .action-btn {
    width: 72rpx;
    height: 72rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16rpx;
  }

  .send-btn {
    background: #4A90E2;
    color: #fff;
    font-size: 26rpx;
    padding: 16rpx 32rpx;
    border-radius: 40rpx;
    line-height: 1.2;
    border: none;

    &[disabled] {
      background: #ccc;
    }
  }
}
</style>
