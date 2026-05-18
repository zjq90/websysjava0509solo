<template>
  <view class="container">
    <view class="chat-header">
      <text class="chat-title">客服对话</text>
      <text class="chat-status">在线</text>
    </view>

    <scroll-view class="chat-content" scroll-y="true" :scroll-top="scrollTop">
      <view class="message-list">
        <view class="message-item system">
          <view class="message-avatar system">
            <text>🤖</text>
          </view>
          <view class="message-content system">
            <text class="message-text">您好，欢迎咨询！有什么可以帮您的？</text>
            <text class="message-time">{{ formatTime(new Date()) }}</text>
          </view>
        </view>
        <view class="message-item" v-for="(msg, index) in messages" :key="index">
          <view class="message-avatar" :class="msg.isUser ? 'user' : 'service'">
            <text>{{ msg.isUser ? '👤' : '🤖' }}</text>
          </view>
          <view class="message-content" :class="msg.isUser ? 'user' : 'service'">
            <text class="message-text">{{ msg.content }}</text>
            <text class="message-time">{{ msg.time }}</text>
          </view>
        </view>
      </view>
    </scroll-view>

    <view class="quick-replies">
      <scroll-view scroll-x="true" class="replies-scroll">
        <view class="reply-list">
          <view class="reply-item" v-for="(reply, index) in quickReplies" :key="index" @click="sendQuickReply(reply)">
            {{ reply }}
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="input-bar">
      <input 
        v-model="inputText" 
        placeholder="请输入您的问题" 
        class="chat-input" 
        @confirm="sendMessage"
      />
      <button class="send-btn" @click="sendMessage">发送</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      inputText: '',
      messages: [],
      scrollTop: 0,
      quickReplies: [
        '如何发布商品？',
        '如何下单购买？',
        '订单怎么取消？',
        '怎么退款？',
        '如何联系卖家？',
        '积分怎么用？',
        '附近商品范围？',
        '拼团怎么玩？'
      ]
    }
  },
  methods: {
    sendQuickReply(reply) {
      this.inputText = reply
      this.sendMessage()
    },
    sendMessage() {
      if (!this.inputText.trim()) return
      
      const userMsg = {
        content: this.inputText,
        isUser: true,
        time: this.formatTime(new Date())
      }
      this.messages.push(userMsg)
      
      const question = this.inputText.trim()
      this.inputText = ''
      
      setTimeout(() => {
        this.autoReply(question)
      }, 800)
    },
    autoReply(question) {
      let reply = ''
      
      if (question.includes('发布') || question.includes('商品')) {
        reply = '发布商品步骤：1. 点击首页"发布"按钮 2. 填写商品信息 3. 上传商品图片 4. 设置价格和分类 5. 确认发布即可。如有问题可随时联系我们！'
      } else if (question.includes('下单') || question.includes('购买')) {
        reply = '购买商品步骤：1. 找到心仪商品 2. 点击"立即购买" 3. 确认订单信息 4. 完成支付 4. 等待收货。祝您购物愉快！'
      } else if (question.includes('取消') || question.includes('订单')) {
        reply = '取消订单操作：进入"我的订单"页面，找到要取消的订单，点击"取消订单"按钮即可。订单状态为"待发货"状态下可直接取消。'
      } else if (question.includes('退款')) {
        reply = '退款申请：在"我的订单"中找到对应订单，点击"申请退款"，填写退款原因后提交。我们会在1-3个工作日内处理您的申请。'
      } else if (question.includes('联系') || question.includes('卖家')) {
        reply = '联系卖家：进入商品详情页，点击"联系卖家"按钮即可直接与卖家沟通。也可以在订单详情页找到联系入口。'
      } else if (question.includes('积分')) {
        reply = '积分获取方式：1. 每日签到获得积分 2. 发布商品获得积分 3. 完成订单获得积分。积分可用于兑换优惠券，在"积分中心"查看和使用。'
      } else if (question.includes('附近') || question.includes('范围')) {
        reply = '附近商品基于您的地理位置，默认显示5公里范围内的商品。您可以在"附近"页面调整搜索范围，支持1-20公里范围选择。'
      } else if (question.includes('拼团')) {
        reply = '拼团玩法：1. 选择拼团商品 2. 开团或参团 3. 邀请好友参团 4. 人数达标即成团。3人即可成团，享受优惠价格！'
      } else {
        reply = '感谢您的咨询！如需更多帮助，您可以：1. 查看帮助中心 2. 拨打客服热线 3. 发送邮件咨询。我们将竭诚为您服务！'
      }
      
      const serviceMsg = {
        content: reply,
        isUser: false,
        time: this.formatTime(new Date())
      }
      this.messages.push(serviceMsg)
      
      this.scrollToBottom()
    },
    formatTime(date) {
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${hours}:${minutes}`
    },
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollTop = 99999
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}

.chat-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.chat-status {
  font-size: 24rpx;
  color: #52c41a;
}

.chat-content {
  flex: 1;
  padding: 20rpx;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.message-item {
  display: flex;
  align-items: flex-start;
}

.message-item.system {
  justify-content: flex-start;
}

.message-item:has(.message-content.user) {
  justify-content: flex-end;
}

.message-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  flex-shrink: 0;
}

.message-avatar.system {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.message-avatar.user {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
}

.message-content {
  max-width: 70%;
  margin: 0 20rpx;
  display: flex;
  flex-direction: column;
}

.message-content.system,
.message-content.service {
  align-items: flex-start;
}

.message-content.user {
  align-items: flex-end;
}

.message-text {
  padding: 20rpx 25rpx;
  border-radius: 16rpx;
  font-size: 28rpx;
  line-height: 1.6;
  word-break: break-all;
}

.message-content.system .message-text,
.message-content.service .message-text {
  background: #fff;
  color: #333;
}

.message-content.user .message-text {
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
}

.message-time {
  font-size: 20rpx;
  color: #999;
  margin-top: 8rpx;
}

.quick-replies {
  background: #fff;
  padding: 20rpx 0;
  border-top: 1rpx solid #eee;
}

.replies-scroll {
  white-space: nowrap;
}

.reply-list {
  display: flex;
  gap: 15rpx;
  padding: 0 20rpx;
}

.reply-item {
  display: inline-block;
  padding: 12rpx 24rpx;
  background: #f0f7ff;
  color: #1890ff;
  border-radius: 24rpx;
  font-size: 24rpx;
  flex-shrink: 0;
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #fff;
  gap: 20rpx;
}

.chat-input {
  flex: 1;
  height: 70rpx;
  padding: 0 25rpx;
  background: #f5f5f5;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.send-btn {
  width: 120rpx;
  height: 70rpx;
  line-height: 70rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 35rpx;
  font-size: 26rpx;
  border: none;
}
</style>
