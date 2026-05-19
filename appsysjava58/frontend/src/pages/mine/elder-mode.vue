<template>
  <view class="container elder-mode">
    <view class="header">
      <text class="title">👓 长辈模式</text>
      <text class="subtitle">字体放大，界面简化，操作更简单</text>
    </view>

    <view class="toggle-section">
      <view class="toggle-card">
        <view class="toggle-info">
          <text class="toggle-title">长辈模式开关</text>
          <text class="toggle-desc">开启后将放大字体，简化界面</text>
        </view>
        <switch :checked="elderMode" color="#667eea" @change="toggleElderMode" />
      </view>
    </view>

    <view class="features-section">
      <text class="section-title">功能特性</text>
      <view class="feature-list">
        <view class="feature-item">
          <text class="feature-icon">🔤</text>
          <view class="feature-text">
            <text class="feature-title">字体放大</text>
            <text class="feature-desc">所有文字放大30%，看得更清楚</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🎯</text>
          <view class="feature-text">
            <text class="feature-title">界面简化</text>
            <text class="feature-desc">只显示核心功能，界面更清爽</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">🎤</text>
          <view class="feature-text">
            <text class="feature-title">语音输入</text>
            <text class="feature-desc">支持语音输入，打字更轻松</text>
          </view>
        </view>
        <view class="feature-item">
          <text class="feature-icon">📞</text>
          <view class="feature-text">
            <text class="feature-title">一键呼叫</text>
            <text class="feature-desc">快速联系宠物医院和医生</text>
          </view>
        </view>
      </view>
    </view>

    <view class="quick-actions">
      <text class="section-title">快捷功能</text>
      <view class="action-grid">
        <view class="action-item" @click="quickCall">
          <text class="action-icon">📞</text>
          <text class="action-text">一键呼叫</text>
        </view>
        <view class="action-item" @click="goToHealth">
          <text class="action-icon">💊</text>
          <text class="action-text">健康提醒</text>
        </view>
        <view class="action-item" @click="goToHospital">
          <text class="action-icon">🏥</text>
          <text class="action-text">附近医院</text>
        </view>
        <view class="action-item" @click="goToDiet">
          <text class="action-icon">🍽️</text>
          <text class="action-text">饮食建议</text>
        </view>
      </view>
    </view>

    <view class="voice-demo">
      <text class="section-title">语音输入演示</text>
      <view class="voice-card" @click="startVoice">
        <text class="voice-icon">🎤</text>
        <text class="voice-text">{{ voiceText || '点击开始语音输入' }}</text>
      </view>
    </view>

    <view class="preview-section">
      <text class="section-title">预览效果</text>
      <view class="preview-card">
        <text class="preview-normal">这是正常字体大小</text>
        <text class="preview-elder">这是长辈模式字体大小</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      elderMode: false,
      voiceText: ''
    }
  },
  onLoad() {
    this.elderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    toggleElderMode(e) {
      this.elderMode = e.detail.value
      uni.setStorageSync('elderMode', this.elderMode)
      this.$showToast(this.elderMode ? '长辈模式已开启' : '长辈模式已关闭', 'success')
    },
    quickCall() {
      uni.makePhoneCall({
        phoneNumber: '010-88888888',
        fail: () => {
          this.$showToast('模拟呼叫: 010-88888888')
        }
      })
    },
    goToHealth() {
      uni.navigateTo({ url: '/pages/health/cards' })
    },
    goToHospital() {
      uni.switchTab({ url: '/pages/hospital/list' })
    },
    goToDiet() {
      uni.navigateTo({ url: '/pages/diet/suggestion' })
    },
    startVoice() {
      this.voiceText = '正在聆听...'
      setTimeout(() => {
        this.voiceText = '我家狗狗最近食欲不好怎么办？'
        this.$showToast('语音识别成功')
      }, 2000)
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
}

.header {
  text-align: center;
  padding: 40rpx 0;
  margin-bottom: 30rpx;
}

.title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 15rpx;
}

.subtitle {
  font-size: 28rpx;
  color: #666;
}

.toggle-section {
  margin-bottom: 30rpx;
}

.toggle-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.toggle-info {
  flex: 1;
}

.toggle-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.toggle-desc {
  font-size: 26rpx;
  color: #999;
}

.section-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.features-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.feature-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.feature-icon {
  font-size: 50rpx;
  margin-right: 25rpx;
}

.feature-text {
  flex: 1;
}

.feature-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.feature-desc {
  font-size: 24rpx;
  color: #666;
}

.quick-actions {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 35rpx;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  border-radius: 16rpx;
}

.action-icon {
  font-size: 60rpx;
  margin-bottom: 15rpx;
}

.action-text {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.voice-demo {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.voice-card {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  background: linear-gradient(135deg, #11998e15 0%, #38ef7d15 100%);
  border-radius: 16rpx;
}

.voice-icon {
  font-size: 50rpx;
  margin-right: 20rpx;
}

.voice-text {
  font-size: 26rpx;
  color: #333;
}

.preview-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.preview-card {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.preview-normal {
  font-size: 26rpx;
  color: #666;
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 10rpx;
}

.preview-elder {
  font-size: 34rpx;
  color: #333;
  font-weight: bold;
  padding: 20rpx;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  border-radius: 10rpx;
}
</style>