<template>
  <view class="container chat-container" :class="{ 'elder-mode': elderMode }">
    <view class="chat-messages" id="chatContainer">
      <view v-for="msg in messages" :key="msg.id" class="message-item" :class="{ user: msg.senderType === 'USER', ai: msg.senderType !== 'USER' }">
        <view class="message-avatar">
          <text>{{ msg.senderType === 'USER' ? '👤' : '🤖' }}</text>
        </view>
        <view class="message-content">
          <view class="message-bubble">
            <text class="message-text">{{ msg.content }}</text>
          </view>
          <view v-if="msg.transferToHuman" class="transfer-tip">
            <text>正在为您转接人工客服...</text>
          </view>
        </view>
      </view>
    </view>

    <view class="quick-questions">
      <text class="quick-title">常见问题</text>
      <view class="quick-list">
        <view v-for="(q, index) in quickQuestions" :key="index" class="quick-item" @click="sendQuickQuestion(q)">
          <text>{{ q }}</text>
        </view>
      </view>
    </view>

    <view class="input-area">
      <input class="message-input" v-model="inputText" placeholder="请输入您的问题..." @confirm="sendMessage" />
      <button class="send-btn" @click="sendMessage" :disabled="!inputText.trim()">
        <text>发送</text>
      </button>
    </view>
  </view>
</template>

<script>
import { chatApi } from '@/api/index.js'

export default {
  data() {
    return {
      messages: [],
      inputText: '',
      sessionId: 'session_' + Date.now(),
      elderMode: false,
      quickQuestions: [
        '无法上网怎么办？',
        '网速慢怎么解决？',
        'WiFi经常掉线',
        '转人工客服'
      ]
    }
  },
  onLoad() {
    this.initChat()
  },
  methods: {
    initChat() {
      this.messages.push({
        id: Date.now(),
        senderType: 'AI',
        content: '您好！我是智能客服，有什么可以帮助您的？您可以直接描述问题，或选择下方常见问题。'
      })
    },
    sendQuickQuestion(question) {
      this.inputText = question
      this.sendMessage()
    },
    async sendMessage() {
      if (!this.inputText.trim()) return

      const userMsg = {
        id: Date.now(),
        senderType: 'USER',
        content: this.inputText
      }
      this.messages.push(userMsg)
      const text = this.inputText
      this.inputText = ''

      this.scrollToBottom()

      try {
        const response = await chatApi.send(1, this.sessionId, text)
        if (response) {
          this.messages.push({
            id: Date.now() + 1,
            senderType: 'AI',
            content: response.content,
            transferToHuman: response.transferToHuman
          })
        }
      } catch (e) {
        console.error(e)
        this.messages.push({
          id: Date.now() + 1,
          senderType: 'AI',
          content: '抱歉，服务暂时不可用，请稍后重试'
        })
      }

      this.scrollToBottom()
    },
    scrollToBottom() {
      setTimeout(() => {
        const container = document.getElementById('chatContainer')
        if (container) {
          container.scrollTop = container.scrollHeight
        }
      }, 100)
    }
  }
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding: 0;
}

.chat-messages {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;
  background: #f5f5f5;
}

.message-item {
  display: flex;
  margin-bottom: 24rpx;
  gap: 12rpx;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  flex-shrink: 0;
}

.message-item.user .message-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.message-item.ai .message-avatar {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.message-content {
  max-width: 70%;
}

.message-bubble {
  padding: 20rpx 24rpx;
  border-radius: 20rpx;
  background: #fff;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
}

.message-item.user .message-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.message-text {
  font-size: 28rpx;
  line-height: 1.5;
  color: #333;
}

.message-item.user .message-text {
  color: #fff;
}

.transfer-tip {
  margin-top: 12rpx;
  padding: 12rpx 16rpx;
  background: #fff3e0;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #f57c00;
}

.quick-questions {
  padding: 20rpx;
  background: #fff;
  border-top: 1rpx solid #f0f0f0;
}

.quick-title {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 16rpx;
  display: block;
}

.quick-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.quick-item {
  padding: 12rpx 20rpx;
  background: #f0f0f0;
  border-radius: 20rpx;
  font-size: 24rpx;
  color: #333;
}

.input-area {
  display: flex;
  padding: 20rpx;
  background: #fff;
  gap: 16rpx;
  border-top: 1rpx solid #f0f0f0;
}

.message-input {
  flex: 1;
  height: 72rpx;
  padding: 0 24rpx;
  background: #f5f5f5;
  border-radius: 36rpx;
  font-size: 28rpx;
}

.send-btn {
  width: 120rpx;
  height: 72rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 36rpx;
  font-size: 26rpx;
}

.send-btn:disabled {
  opacity: 0.5;
}
</style>
