<template>
  <view class="private-chat-page">
    <view class="chat-messages">
      <view class="message-item" v-for="msg in messageList" :key="msg.id" :class="msg.senderId === currentUserId ? 'self' : 'other'">
        <image v-if="msg.senderId !== currentUserId" :src="msg.senderAvatar" class="avatar" mode="aspectFill"></image>
        <view class="message-content">
          <view class="message-bubble" :class="msg.messageType === 1 ? 'image-bubble' : ''">
            <image v-if="msg.messageType === 1" :src="msg.fileUrl" class="message-image" mode="widthFix" @click="previewImage(msg.fileUrl)"></image>
            <text v-else class="message-text">{{ msg.content }}</text>
          </view>
          <view class="message-meta">
            <text class="message-time">{{ formatTime(msg.createTime) }}</text>
            <text class="read-status" v-if="msg.senderId === currentUserId">{{ msg.isRead === 1 ? '已读' : '未读' }}</text>
          </view>
        </view>
        <image v-if="msg.senderId === currentUserId" :src="currentUserAvatar" class="avatar" mode="aspectFill"></image>
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
import { getPrivateMessageList, sendPrivateMessage, markPrivateMessageAsRead } from '@/api/message.js'

export default {
  data() {
    return {
      otherUserId: '',
      otherUserName: '',
      otherUserAvatar: '',
      messageList: [],
      inputMessage: '',
      currentUserId: 2,
      currentUserAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
      pageNum: 1,
      pageSize: 30
    }
  },
  onLoad(options) {
    this.otherUserId = options.userId
    this.otherUserName = options.name || '用户'
    this.otherUserAvatar = options.avatar || `https://api.dicebear.com/7.x/avataaars/svg?seed=${this.otherUserId}`
    uni.setNavigationBarTitle({ title: this.otherUserName })
    this.currentUserId = uni.getStorageSync('userId') || 2
    this.getMessageList()
    this.markAsRead()
  },
  methods: {
    getMessageList() {
      getPrivateMessageList({
        userId1: this.currentUserId,
        userId2: this.otherUserId,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }).then(res => {
        this.messageList = (res.list || []).reverse()
        this.scrollToBottom()
      }).catch(err => {
        console.error('获取私聊消息失败', err)
        this.messageList = this.getMockData()
        this.scrollToBottom()
      })
    },
    markAsRead() {
      markPrivateMessageAsRead(this.otherUserId, this.currentUserId).catch(() => {})
    },
    sendMessage() {
      if (!this.inputMessage.trim()) return

      const messageData = {
        senderId: this.currentUserId,
        senderName: '张三',
        senderAvatar: this.currentUserAvatar,
        receiverId: this.otherUserId,
        receiverName: this.otherUserName,
        receiverAvatar: this.otherUserAvatar,
        messageType: 0,
        content: this.inputMessage.trim()
      }

      sendPrivateMessage(messageData).then(res => {
        this.messageList.push(res)
        this.inputMessage = ''
        this.scrollToBottom()
      }).catch(() => {
        this.messageList.push({
          id: Date.now(),
          ...messageData,
          isRead: 0
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
            senderId: this.currentUserId,
            senderName: '张三',
            senderAvatar: this.currentUserAvatar,
            receiverId: this.otherUserId,
            receiverName: this.otherUserName,
            receiverAvatar: this.otherUserAvatar,
            messageType: 1,
            fileUrl: res.tempFilePaths[0],
            content: '[图片]',
            isRead: 0
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
    getMockData() {
      return [
        {
          id: 1,
          senderId: 3,
          senderName: '李四',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          receiverId: 2,
          messageType: 0,
          content: '你好，关于明天的活动我想咨询一下',
          createTime: '2024-10-15 14:30:00',
          isRead: 1
        },
        {
          id: 2,
          senderId: 2,
          senderName: '张三',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          receiverId: 3,
          messageType: 0,
          content: '你好，请问有什么问题？',
          createTime: '2024-10-15 14:31:00',
          isRead: 1
        },
        {
          id: 3,
          senderId: 3,
          senderName: '李四',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          receiverId: 2,
          messageType: 0,
          content: '明天的活动需要准备什么材料吗？',
          createTime: '2024-10-15 14:32:00',
          isRead: 1
        },
        {
          id: 4,
          senderId: 2,
          senderName: '张三',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          receiverId: 3,
          messageType: 0,
          content: '只需要带笔记本电脑就可以了',
          createTime: '2024-10-15 14:33:00',
          isRead: 1
        },
        {
          id: 5,
          senderId: 3,
          senderName: '李四',
          senderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          receiverId: 2,
          messageType: 0,
          content: '好的，谢谢！',
          createTime: '2024-10-15 14:34:00',
          isRead: 0
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.private-chat-page {
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

        .read-status {
          color: #52c41a;
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

      .message-meta {
        display: flex;
        align-items: center;
        gap: 10rpx;
        margin-top: 6rpx;

        .message-time {
          font-size: 20rpx;
          color: #ccc;
        }

        .read-status {
          font-size: 20rpx;
          color: #999;
        }
      }
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
