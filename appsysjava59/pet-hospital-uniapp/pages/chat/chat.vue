<template>
  <view class="chat-container" :class="{ 'elder-mode': elderMode }">
    <view class="chat-info card">
      <view class="info-header flex-between">
        <view>
          <text class="pet-name">{{ consultation?.title }}</text>
          <text v-if="consultation?.emergencyLevel === 'URGENT'" class="tag tag-urgent">紧急</text>
        </view>
        <view class="consultation-type">
          <text @click="toggleType">{{ currentType === 'TEXT' ? '📝 图文' : '🎥 视频' }}</text>
        </view>
      </view>
      <view class="symptom-text">症状：{{ consultation?.symptom }}</view>
      
      <view class="status-actions" v-if="consultation?.status === 'PENDING'">
        <view class="btn btn-primary" @click="acceptConsultation">接诊</view>
      </view>
      
      <view class="status-actions" v-else-if="consultation?.status === 'IN_PROGRESS'">
        <view class="btn btn-outline" @click="showPrescription">📋 开处方</view>
        <view class="btn btn-outline" @click="showChecklist">🔬 检查单</view>
        <view class="btn btn-outline" @click="showTransfer">🔄 转诊</view>
        <view class="btn btn-danger" @click="completeConsultation">完成问诊</view>
      </view>
    </view>
    
    <scroll-view class="message-list" scroll-y scroll-with-animation :scroll-top="scrollTop">
      <view v-for="msg in messages" :key="msg.id" class="message-item" :class="msg.senderType === 'DOCTOR' ? 'message-right' : 'message-left'">
        <view class="message-content">
          <text class="message-text">{{ msg.content }}</text>
          <text class="message-time">{{ formatTime(msg.createTime) }}</text>
        </view>
      </view>
    </scroll-view>
    
    <view class="input-area">
      <view class="input-wrapper">
        <input class="message-input" v-model="inputText" placeholder="输入消息..." @confirm="sendMessage" />
        <view class="voice-btn" @click="startVoiceInput">🎤</view>
      </view>
      <view class="send-btn" @click="sendMessage">发送</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import consultationApi from '../../api/consultation.js'

const userStore = useUserStore()

const consultationId = ref('')
const consultation = ref(null)
const messages = ref([])
const inputText = ref('')
const currentType = ref('TEXT')
const scrollTop = ref(0)
const elderMode = computed(() => userStore.elderMode)

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
}

const loadConsultation = async () => {
  try {
    const res = await consultationApi.getConsultationDetail(consultationId.value)
    consultation.value = res
    currentType.value = res.consultationType || 'TEXT'
  } catch (e) {
    console.error(e)
  }
}

const loadMessages = async () => {
  try {
    const res = await consultationApi.getMessages(consultationId.value)
    messages.value = res || []
    setTimeout(() => {
      scrollTop.value = 99999
    }, 100)
  } catch (e) {
    console.error(e)
  }
}

const sendMessage = async () => {
  if (!inputText.value.trim()) return
  
  try {
    await consultationApi.sendMessage(consultationId.value, {
      senderType: 'DOCTOR',
      senderId: userStore.userInfo.id,
      content: inputText.value,
      messageType: 'TEXT'
    })
    inputText.value = ''
    loadMessages()
  } catch (e) {
    console.error(e)
  }
}

const startVoiceInput = () => {
  uni.showToast({ title: '语音输入功能', icon: 'none' })
}

const toggleType = () => {
  currentType.value = currentType.value === 'TEXT' ? 'VIDEO' : 'TEXT'
}

const acceptConsultation = async () => {
  try {
    await consultationApi.updateStatus(consultationId.value, 'IN_PROGRESS')
    consultation.value.status = 'IN_PROGRESS'
    uni.showToast({ title: '接诊成功', icon: 'success' })
  } catch (e) {
    console.error(e)
  }
}

const completeConsultation = async () => {
  uni.showModal({
    title: '提示',
    content: '确定完成本次问诊吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await consultationApi.updateStatus(consultationId.value, 'COMPLETED')
          consultation.value.status = 'COMPLETED'
          uni.showToast({ title: '问诊已完成', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

const showPrescription = () => {
  uni.navigateTo({
    url: `/pages/prescription/create?consultationId=${consultationId.value}&petId=${consultation.value.petId}`
  })
}

const showChecklist = () => {
  uni.showToast({ title: '检查单功能开发中', icon: 'none' })
}

const showTransfer = () => {
  uni.showToast({ title: '转诊功能开发中', icon: 'none' })
}

onLoad((options) => {
  consultationId.value = options.id
  loadConsultation()
  loadMessages()
})

onShow(() => {
  loadMessages()
})
</script>

<style scoped>
.chat-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.chat-info {
  margin: 20rpx;
  flex-shrink: 0;
}

.info-header {
  margin-bottom: 12rpx;
}

.pet-name {
  font-size: 30rpx;
  font-weight: bold;
  margin-right: 12rpx;
}

.consultation-type {
  font-size: 26rpx;
  color: #409EFF;
}

.symptom-text {
  font-size: 26rpx;
  color: #606266;
  margin-bottom: 20rpx;
}

.status-actions {
  display: flex;
  gap: 16rpx;
  flex-wrap: wrap;
}

.status-actions .btn {
  flex: 1;
  min-width: 120rpx;
  font-size: 24rpx;
  height: 64rpx;
  line-height: 64rpx;
}

.message-list {
  flex: 1;
  padding: 20rpx;
}

.message-item {
  margin-bottom: 24rpx;
  display: flex;
}

.message-left {
  justify-content: flex-start;
}

.message-right {
  justify-content: flex-end;
}

.message-content {
  max-width: 70%;
  padding: 20rpx 24rpx;
  border-radius: 16rpx;
  background-color: #fff;
}

.message-right .message-content {
  background-color: #409EFF;
  color: #fff;
}

.message-text {
  font-size: 28rpx;
  line-height: 1.6;
  word-break: break-all;
}

.message-time {
  display: block;
  font-size: 20rpx;
  opacity: 0.7;
  margin-top: 8rpx;
}

.input-area {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #fff;
  border-top: 1rpx solid #ebeef5;
  flex-shrink: 0;
}

.input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 40rpx;
  padding: 0 20rpx;
  margin-right: 20rpx;
}

.message-input {
  flex: 1;
  height: 72rpx;
  font-size: 28rpx;
}

.voice-btn {
  font-size: 32rpx;
  padding: 0 10rpx;
}

.send-btn {
  width: 120rpx;
  height: 72rpx;
  line-height: 72rpx;
  text-align: center;
  background-color: #409EFF;
  color: #fff;
  border-radius: 36rpx;
  font-size: 28rpx;
}

.elder-mode .pet-name {
  font-size: 34rpx;
}

.elder-mode .message-text {
  font-size: 32rpx;
}

.elder-mode .message-input {
  font-size: 32rpx;
  height: 88rpx;
}

.elder-mode .send-btn {
  height: 88rpx;
  line-height: 88rpx;
  font-size: 30rpx;
}
</style>
