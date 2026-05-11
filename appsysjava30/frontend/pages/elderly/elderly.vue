<template>
  <view class="elderly-container elderly-mode">
    <view class="header">
      <text class="title">长辈模式</text>
      <text class="subtitle">大字版，更清晰</text>
    </view>
    
    <view class="quick-actions">
      <view class="action-card" @click="goRegistration">
        <view class="action-icon">🏥</view>
        <text class="action-text">预约挂号</text>
        <text class="action-desc">选择科室和医生</text>
      </view>
      
      <view class="action-card" @click="goRecords">
        <view class="action-icon">📋</view>
        <text class="action-text">我的预约</text>
        <text class="action-desc">查看预约记录</text>
      </view>
      
      <view class="action-card" @click="callService">
        <view class="action-icon">📞</view>
        <text class="action-text">电话客服</text>
        <text class="action-desc">400-123-4567</text>
      </view>
      
      <view class="action-card" @click="toggleElderlyMode">
        <view class="action-icon">🔙</view>
        <text class="action-text">退出模式</text>
        <text class="action-desc">返回普通版</text>
      </view>
    </view>
    
    <view class="today-section card" v-if="todayAppointments.length > 0">
      <view class="section-title">📅 今日预约</view>
      <view 
        v-for="item in todayAppointments" 
        :key="item.id" 
        class="appointment-item"
        @click="goDetail(item.registrationNo)"
      >
        <view class="dept">{{ item.deptName }}</view>
        <view class="doctor">{{ item.doctorName }} {{ item.doctorTitle }}</view>
        <view class="time">{{ item.visitDate }} {{ item.timeSlot }}</view>
        <view class="tag tag-hightlight">{{ item.statusTag }}</view>
      </view>
    </view>
    
    <view class="tips-section">
      <view class="section-title">📌 重要提醒</view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">就诊时请携带身份证和医保卡</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">请提前15分钟到达医院</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">如需帮助请拨打客服电话</text>
      </view>
    </view>
    
    <view class="voice-section">
      <view class="section-title">🎙️ 语音输入</view>
      <view class="voice-area" @click="startVoice">
        <text class="voice-icon">{{ isRecording ? '🔴' : '🎤' }}</text>
        <text class="voice-text">{{ isRecording ? '正在聆听...' : '点击说话描述症状' }}</text>
      </view>
      <view class="voice-result" v-if="voiceResult">
        <text class="result-label">识别结果：</text>
        <text class="result-text">{{ voiceResult }}</text>
      </view>
      <view class="voice-tip">
        <text>示例："我有点感冒，头有点痛"</text>
      </view>
    </view>
    
    <view class="bottom-tips">
      <text>如需帮助，请联系家人或拨打客服电话</text>
    </view>
  </view>
</template>

<script>
import { registrationApi } from '@/utils/api.js'

export default {
  data() {
    return {
      todayAppointments: [],
      isRecording: false,
      voiceResult: ''
    }
  },
  
  onShow() {
    this.loadTodayAppointments()
  },
  
  methods: {
    async loadTodayAppointments() {
      try {
        const res = await registrationApi.today()
        this.todayAppointments = res.data || []
      } catch (e) {}
    },
    
    goRegistration() {
      uni.navigateTo({ url: '/pages/registration/registration' })
    },
    
    goRecords() {
      uni.switchTab({ url: '/pages/record/record' })
    },
    
    goDetail(registrationNo) {
      uni.navigateTo({ url: `/pages/detail/detail?registrationNo=${registrationNo}` })
    },
    
    callService() {
      uni.makePhoneCall({
        phoneNumber: '400-123-4567',
        fail: () => {
          uni.showToast({ title: '客服电话：400-123-4567', icon: 'none', duration: 3000 })
        }
      })
    },
    
    toggleElderlyMode() {
      uni.setStorageSync('elderlyMode', false)
      uni.showToast({ title: '已退出长辈模式', icon: 'none' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/index/index' })
      }, 1000)
    },
    
    startVoice() {
      if (this.isRecording) {
        this.stopVoice()
        return
      }
      
      this.isRecording = true
      
      setTimeout(() => {
        this.isRecording = false
        const mockResults = [
          '我有点咳嗽，喉咙痛',
          '最近睡眠不好，有点头痛',
          '胃痛，没有胃口',
          '有点发烧，浑身酸痛',
          '感冒了，鼻塞流鼻涕'
        ]
        this.voiceResult = mockResults[Math.floor(Math.random() * mockResults.length)]
        
        uni.showToast({
          title: '识别成功',
          icon: 'success'
        })
      }, 2000)
    },
    
    stopVoice() {
      this.isRecording = false
    }
  }
}
</script>

<style scoped>
.elderly-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 40rpx;
}

.header {
  text-align: center;
  padding: 40rpx 0;
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.title {
  display: block;
  font-size: 56rpx;
  font-weight: bold;
  color: #1890ff;
  margin-bottom: 12rpx;
}

.subtitle {
  font-size: 32rpx;
  color: #666;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.action-card {
  width: calc(50% - 10rpx);
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx 20rpx;
  text-align: center;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.action-card:active {
  transform: scale(0.98);
  background: #fafafa;
}

.action-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.action-text {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.action-desc {
  font-size: 28rpx;
  color: #999;
}

.today-section {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
}

.appointment-item {
  background: #fafafa;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
}

.appointment-item:last-child {
  margin-bottom: 0;
}

.dept {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.doctor {
  font-size: 30rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.time {
  font-size: 28rpx;
  color: #1890ff;
  margin-bottom: 12rpx;
}

.tips-section {
  padding: 32rpx;
  background: #fffbe6;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-dot {
  font-size: 32rpx;
  color: #faad14;
  margin-right: 12rpx;
}

.tip-text {
  flex: 1;
  font-size: 30rpx;
  color: #faad14;
  line-height: 1.8;
}

.voice-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 32rpx;
  margin-bottom: 20rpx;
}

.voice-area {
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
  border-radius: 24rpx;
  padding: 48rpx;
  text-align: center;
  margin-bottom: 24rpx;
}

.voice-area:active {
  background: linear-gradient(135deg, #bae7ff 0%, #91d5ff 100%);
}

.voice-icon {
  display: block;
  font-size: 96rpx;
  margin-bottom: 16rpx;
}

.voice-text {
  font-size: 32rpx;
  color: #1890ff;
}

.voice-result {
  background: #f6ffed;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
}

.result-label {
  display: block;
  font-size: 28rpx;
  color: #52c41a;
  margin-bottom: 12rpx;
}

.result-text {
  font-size: 30rpx;
  color: #333;
  line-height: 1.6;
}

.voice-tip {
  text-align: center;
  font-size: 26rpx;
  color: #999;
}

.bottom-tips {
  text-align: center;
  padding: 32rpx;
  font-size: 28rpx;
  color: #999;
}
</style>
