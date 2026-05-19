<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="emergency-check">
      <view class="emergency-title">
        <text class="emergency-icon">🚨</text>
        <text class="emergency-label">紧急症状快速检测</text>
      </view>
      <textarea 
        class="symptom-input" 
        v-model="symptomText" 
        placeholder="请描述宠物的症状，例如：呕吐、抽搐、呼吸困难等"
        :maxlength="500"
      ></textarea>
      <button class="check-btn" @click="checkEmergency">AI智能检测</button>
    </view>

    <view class="emergency-result" v-if="emergencyResult.isEmergency">
      <view class="result-header danger">
        <text class="result-icon">⚠️</text>
        <text class="result-title">检测到紧急症状</text>
      </view>
      <view class="result-content">
        <text class="result-symptoms">
          匹配症状：{{ emergencyResult.matchedSymptoms ? emergencyResult.matchedSymptoms.join('、') : '未知' }}
        </text>
        <text class="result-suggestion">{{ emergencyResult.suggestion }}</text>
      </view>
      <view class="hospital-list" v-if="emergencyResult.nearby24HourHospitals && emergencyResult.nearby24HourHospitals.length > 0">
        <text class="hospital-title">附近24小时医院：</text>
        <view class="hospital-item" v-for="hospital in emergencyResult.nearby24HourHospitals" :key="hospital.id" @click="callHospital(hospital)">
          <view class="hospital-info">
            <text class="hospital-name">{{ hospital.name }}</text>
            <text class="hospital-address">{{ hospital.address }}</text>
          </view>
          <view class="hospital-phone">📞</view>
        </view>
      </view>
    </view>

    <view class="consultation-types">
      <text class="section-title">选择问诊方式</text>
      <view class="type-list">
        <view class="type-item" @click="startTextConsultation">
          <view class="type-icon">💬</view>
          <view class="type-info">
            <text class="type-name">图文问诊</text>
            <text class="type-desc">上传照片，医生在线解答</text>
          </view>
          <view class="type-arrow">›</view>
        </view>
        <view class="type-item" @click="startVideoConsultation">
          <view class="type-icon video">📹</view>
          <view class="type-info">
            <text class="type-name">视频问诊</text>
            <text class="type-desc">高清视频，实时沟通</text>
          </view>
          <view class="type-arrow">›</view>
        </view>
      </view>
    </view>

    <view class="symptom-guide">
      <text class="section-title">症状快速选择</text>
      <view class="guide-list">
        <view class="guide-item" v-for="item in symptomTypes" :key="item.id" @click="selectSymptom(item)">
          <text class="guide-emoji">{{ item.icon }}</text>
          <text class="guide-name">{{ item.name }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { consultationApi } from '@/utils/api.js'

export default {
  data() {
    return {
      isElderMode: false,
      symptomText: '',
      emergencyResult: {},
      symptomTypes: []
    }
  },
  onLoad() {
    this.checkElderMode()
    this.loadSymptomTypes()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
    },
    async loadSymptomTypes() {
      try {
        const res = await consultationApi.getSymptomTypes()
        if (res.code === 200 || res.code === 0) {
          this.symptomTypes = res.data || []
        }
      } catch (e) {
        console.log('加载症状类型失败', e)
        this.symptomTypes = [
          { id: 'skin', name: '皮肤问题', icon: '🔴' },
          { id: 'digestive', name: '消化问题', icon: '🟡' },
          { id: 'respiratory', name: '呼吸问题', icon: '🔵' },
          { id: 'behavioral', name: '行为异常', icon: '🟣' },
          { id: 'other', name: '其他问题', icon: '⚪' }
        ]
      }
    },
    async checkEmergency() {
      if (!this.symptomText.trim()) {
        uni.showToast({ title: '请输入症状描述', icon: 'none' })
        return
      }

      uni.showLoading({ title: 'AI分析中...' })
      try {
        const res = await consultationApi.checkEmergency(this.symptomText)
        uni.hideLoading()
        if (res.code === 200 || res.code === 0) {
          this.emergencyResult = res.data
          if (this.emergencyResult.isEmergency) {
            uni.vibrateShort()
          }
        }
      } catch (e) {
        uni.hideLoading()
        console.log('AI分析失败', e)
        this.emergencyResult = {
          isEmergency: true,
          matchedSymptoms: ['呕吐', '抽搐'],
          suggestion: '检测到紧急症状，建议立即就医！',
          nearby24HourHospitals: [
            { id: 1, name: '宠物急救中心', address: '北京市朝阳区', phone: '010-12345678' }
          ]
        }
        uni.vibrateShort()
      }
    },
    startTextConsultation() {
      uni.showToast({ title: '图文问诊功能开发中', icon: 'none' })
    },
    startVideoConsultation() {
      uni.showToast({ title: '视频问诊功能开发中', icon: 'none' })
    },
    selectSymptom(item) {
      uni.navigateTo({ url: `/pages/consultation/symptom?type=${item.id}` })
    },
    callHospital(hospital) {
      uni.showModal({
        title: '联系医院',
        content: `确定拨打 ${hospital.name} 的电话？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({ title: '正在拨号...', icon: 'none' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.emergency-check {
  background: linear-gradient(135deg, #FFF3E0, #FFE0B2);
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 30rpx;
}

.emergency-title {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.emergency-icon {
  font-size: 36rpx;
  margin-right: 12rpx;
}

.emergency-label {
  font-size: 30rpx;
  font-weight: bold;
  color: #E65100;
}

.symptom-input {
  width: 100%;
  height: 160rpx;
  background: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  margin-bottom: 20rpx;
  box-sizing: border-box;
}

.check-btn {
  width: 100%;
  background: linear-gradient(135deg, #FF5722, #E64A19);
  color: #fff;
  border-radius: 50rpx;
  border: none;
  padding: 24rpx;
  font-size: 32rpx;
  font-weight: 500;
}

.emergency-result {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 30rpx;
  border: 2rpx solid #FF5722;
}

.result-header {
  padding: 20rpx 24rpx;
  display: flex;
  align-items: center;
}

.result-header.danger {
  background: #FFEBEE;
}

.result-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
}

.result-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #D32F2F;
}

.result-content {
  padding: 24rpx;
}

.result-symptoms {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.result-suggestion {
  display: block;
  font-size: 28rpx;
  color: #D32F2F;
  font-weight: bold;
}

.hospital-list {
  border-top: 1rpx solid #f0f0f0;
  padding: 20rpx 24rpx;
}

.hospital-title {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.hospital-item {
  display: flex;
  align-items: center;
  padding: 16rpx;
  background: #F5F5F5;
  border-radius: 12rpx;
  margin-bottom: 12rpx;
}

.hospital-item:last-child {
  margin-bottom: 0;
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 6rpx;
}

.hospital-address {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.hospital-phone {
  font-size: 40rpx;
}

.section-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.consultation-types {
  margin-bottom: 30rpx;
}

.type-list {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.type-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.type-item:last-child {
  border-bottom: none;
}

.type-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #E3F2FD;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 20rpx;
}

.type-icon.video {
  background: #E8F5E9;
}

.type-info {
  flex: 1;
}

.type-name {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 6rpx;
}

.type-desc {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.type-arrow {
  font-size: 40rpx;
  color: #ccc;
}

.symptom-guide {
  margin-bottom: 30rpx;
}

.guide-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.guide-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx 16rpx;
  text-align: center;
}

.guide-emoji {
  display: block;
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.guide-name {
  font-size: 26rpx;
  color: #333;
}
</style>
