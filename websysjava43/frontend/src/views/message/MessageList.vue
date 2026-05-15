<template>
  <div class="page-container">
    <div class="page-header">
      <h2>消息中心</h2>
      <el-button type="primary" @click="markAllAsRead">全部标为已读</el-button>
    </div>

    <div class="message-list">
      <div
        v-for="msg in messages"
        :key="msg.id"
        class="message-item"
        :class="{ unread: !msg.isRead }"
        @click="viewMessage(msg)"
      >
        <div class="message-header">
          <span class="message-title">{{ msg.title }}</span>
          <span class="message-time">{{ formatTime(msg.createTime) }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
        <div class="message-sender">来自: {{ msg.senderName || '系统' }}</div>
      </div>
    </div>

    <el-empty v-if="messages.length === 0" description="暂无消息"></el-empty>
  </div>
</template>

<script>
import { getMyMessages, markAsRead, markAllAsRead } from '@/api/message'
import dayjs from 'dayjs'

export default {
  name: 'MessageList',
  data() {
    return {
      messages: []
    }
  },
  mounted() {
    this.loadMessages()
  },
  methods: {
    async loadMessages() {
      try {
        const res = await getMyMessages()
        this.messages = res.data || []
      } catch (error) {
        console.error(error)
      }
    },
    async viewMessage(msg) {
      if (!msg.isRead) {
        try {
          await markAsRead(msg.id)
          msg.isRead = true
        } catch (error) {
          console.error(error)
        }
      }
    },
    async markAllAsRead() {
      try {
        await markAllAsRead()
        this.messages.forEach(msg => msg.isRead = true)
        this.$message.success('全部标为已读')
      } catch (error) {
        console.error(error)
      }
    },
    formatTime(time) {
      return dayjs(time).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>

<style scoped>
.message-list {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
}

.message-item {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: all 0.3s;
}

.message-item:hover {
  background: #f5f7fa;
}

.message-item:last-child {
  border-bottom: none;
}

.message-item.unread {
  background: #f0f9ff;
  border-left: 3px solid #409eff;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.message-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 10px;
}

.message-sender {
  font-size: 12px;
  color: #999;
  text-align: right;
}
</style>
