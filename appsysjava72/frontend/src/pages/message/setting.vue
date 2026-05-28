<template>
  <view class="setting-container">
    <view class="setting-list">
      <view class="setting-item">
        <view class="item-left">
          <text class="item-icon">🔔</text>
          <text class="item-label">活动提醒</text>
        </view>
        <switch 
          :checked="settings.activityReminder" 
          @change="toggleSetting('activityReminder')"
          color="#5677fc"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-left">
          <text class="item-icon">📢</text>
          <text class="item-label">社团公告</text>
        </view>
        <switch 
          :checked="settings.clubNotice" 
          @change="toggleSetting('clubNotice')"
          color="#5677fc"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-left">
          <text class="item-icon">💬</text>
          <text class="item-label">群聊消息</text>
        </view>
        <switch 
          :checked="settings.chatMessage" 
          @change="toggleSetting('chatMessage')"
          color="#5677fc"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-left">
          <text class="item-icon">👍</text>
          <text class="item-label">互动通知</text>
        </view>
        <switch 
          :checked="settings.interaction" 
          @change="toggleSetting('interaction')"
          color="#5677fc"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-left">
          <text class="item-icon">📧</text>
          <text class="item-label">系统通知</text>
        </view>
        <switch 
          :checked="settings.systemNotice" 
          @change="toggleSetting('systemNotice')"
          color="#5677fc"
        />
      </view>
    </view>
    
    <view class="tip-section">
      <text class="tip-title">温馨提示</text>
      <text class="tip-text">关闭通知后，您将不再接收相关消息推送</text>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      settings: {
        activityReminder: true,
        clubNotice: true,
        chatMessage: true,
        interaction: true,
        systemNotice: true
      }
    }
  },
  onLoad() {
    this.loadSettings()
  },
  methods: {
    async loadSettings() {
      try {
        const res = await api.getNotificationSettings()
        if (res.data) {
          this.settings = { ...this.settings, ...res.data }
        }
      } catch (e) {
        console.error(e)
      }
    },
    
    async toggleSetting(key) {
      const oldValue = this.settings[key]
      this.settings[key] = !this.settings[key]
      
      try {
        await api.updateNotificationSettings(this.settings)
      } catch (e) {
        this.settings[key] = oldValue
        util.toast('设置失败，请重试')
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.setting-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding: 20rpx;
}

.setting-list {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.setting-item:last-child {
  border-bottom: none;
}

.item-left {
  display: flex;
  align-items: center;
}

.item-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.item-label {
  font-size: 28rpx;
  color: #333;
}

.tip-section {
  margin-top: 40rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 16rpx;
}

.tip-title {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}

.tip-text {
  font-size: 24rpx;
  color: #999;
  line-height: 1.6;
}
</style>
