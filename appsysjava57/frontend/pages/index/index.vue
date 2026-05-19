<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="banner">
      <view class="banner-text">
        <text class="banner-title">宠物健康管家</text>
        <text class="banner-desc">专业、便捷、贴心的宠物医疗服务</text>
      </view>
    </view>

    <view class="quick-actions">
      <view class="action-item" @click="goToConsultation">
        <view class="action-icon consultation-icon">💬</view>
        <text class="action-text">在线问诊</text>
      </view>
      <view class="action-item" @click="goToAppointment">
        <view class="action-icon appointment-icon">📅</view>
        <text class="action-text">预约挂号</text>
      </view>
      <view class="action-item" @click="goToPetList">
        <view class="action-icon pet-icon">🐾</view>
        <text class="action-text">我的宠物</text>
      </view>
      <view class="action-item" @click="goToMedicine">
        <view class="action-icon medicine-icon">💊</view>
        <text class="action-text">药品购买</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">紧急提醒</text>
      </view>
      <view class="emergency-card" v-if="vaccineReminders.length > 0">
        <view class="emergency-icon">⚠️</view>
        <view class="emergency-content">
          <text class="emergency-title">疫苗即将到期</text>
          <text class="emergency-desc" v-for="(item, index) in vaccineReminders" :key="index">
            {{ item.message }}
          </text>
        </view>
      </view>
      <view class="no-reminder" v-else>
        <text class="no-reminder-text">暂无紧急提醒 🎉</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">推荐医生</text>
        <text class="section-more" @click="goToAppointment">查看更多</text>
      </view>
      <view class="doctor-list">
        <view class="doctor-item" v-for="doctor in doctors" :key="doctor.id" @click="viewDoctor(doctor)">
          <view class="doctor-avatar">
            <text class="avatar-text">{{ doctor.name.charAt(0) }}</text>
          </view>
          <view class="doctor-info">
            <text class="doctor-name">{{ doctor.name }}</text>
            <text class="doctor-dept">{{ doctor.department }} · {{ doctor.title }}</text>
            <view class="doctor-rating">
              <text class="rating-star">⭐</text>
              <text class="rating-score">{{ doctor.rating }}</text>
              <text class="rating-count">({{ doctor.reviewCount }}条评价)</text>
            </view>
          </view>
          <view class="doctor-fee">
            <text class="fee-price">¥{{ doctor.consultationFee }}</text>
            <text class="fee-label">咨询费</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { petApi, appointmentApi } from '@/utils/api.js'

export default {
  data() {
    return {
      isElderMode: false,
      vaccineReminders: [],
      doctors: []
    }
  },
  onLoad() {
    this.checkElderMode()
    this.loadData()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
    },
    async loadData() {
      await this.loadVaccineReminders()
      await this.loadDoctors()
    },
    async loadVaccineReminders() {
      try {
        // 使用模拟的宠物ID 1，实际项目中应该从用户信息获取
        const res = await petApi.getVaccineReminders(1)
        if (res.code === 200 || res.code === 0) {
          this.vaccineReminders = Object.values(res.data || {})
        }
      } catch (e) {
        console.log('加载提醒失败', e)
        // 使用模拟数据
        this.vaccineReminders = [
          { message: '金毛豆豆的狂犬疫苗还有28天到期' },
          { message: '英短咪咪的狂犬疫苗还有30天到期' }
        ]
      }
    },
    async loadDoctors() {
      try {
        const res = await appointmentApi.getDoctorsByDepartment('内科')
        if (res.code === 200 || res.code === 0) {
          this.doctors = (res.data || []).slice(0, 3)
        }
      } catch (e) {
        console.log('加载医生失败', e)
        this.doctors = [
          { id: 1, name: '王医生', department: '内科', title: '主任医师', rating: 4.8, reviewCount: 156, consultationFee: 50 }
        ]
      }
    },
    goToConsultation() {
      uni.switchTab({ url: '/pages/consultation/index' })
    },
    goToAppointment() {
      uni.navigateTo({ url: '/pages/appointment/index' })
    },
    goToPetList() {
      uni.switchTab({ url: '/pages/pet/list' })
    },
    goToMedicine() {
      uni.navigateTo({ url: '/pages/medicine/index' })
    },
    viewDoctor(doctor) {
      uni.showToast({ title: `选择了${doctor.name}`, icon: 'none' })
    }
  }
}
</script>

<style scoped>
.banner {
  position: relative;
  height: 300rpx;
  margin-bottom: 30rpx;
  border-radius: 20rpx;
  overflow: hidden;
  background: linear-gradient(135deg, #4CAF50, #2196F3);
  display: flex;
  align-items: center;
  padding: 0 30rpx;
}

.banner-text {
  color: #fff;
}

.banner-title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.banner-desc {
  font-size: 26rpx;
  opacity: 0.9;
}

.quick-actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 23%;
}

.action-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 44rpx;
  margin-bottom: 12rpx;
}

.consultation-icon { background: #E3F2FD; }
.appointment-icon { background: #E8F5E9; }
.pet-icon { background: #FFF3E0; }
.medicine-icon { background: #F3E5F5; }

.action-text {
  font-size: 26rpx;
  color: #666;
}

.section {
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 26rpx;
  color: #4CAF50;
}

.emergency-card {
  background: linear-gradient(135deg, #FFF3E0, #FFE0B2);
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: flex-start;
}

.emergency-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.emergency-content {
  flex: 1;
}

.emergency-title {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #E65100;
  margin-bottom: 10rpx;
}

.emergency-desc {
  display: block;
  font-size: 26rpx;
  color: #F57C00;
  line-height: 1.6;
}

.no-reminder {
  text-align: center;
  padding: 40rpx;
  background: #f9f9f9;
  border-radius: 16rpx;
}

.no-reminder-text {
  font-size: 28rpx;
  color: #999;
}

.doctor-list {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.doctor-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.doctor-item:last-child {
  border-bottom: none;
}

.doctor-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4CAF50, #45a049);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  color: #fff;
  font-size: 36rpx;
  font-weight: bold;
}

.doctor-info {
  flex: 1;
}

.doctor-name {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.doctor-dept {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.doctor-rating {
  display: flex;
  align-items: center;
}

.rating-star {
  font-size: 24rpx;
  margin-right: 6rpx;
}

.rating-score {
  font-size: 24rpx;
  color: #FF9800;
  font-weight: bold;
  margin-right: 10rpx;
}

.rating-count {
  font-size: 22rpx;
  color: #999;
}

.doctor-fee {
  text-align: right;
}

.fee-price {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #4CAF50;
}

.fee-label {
  font-size: 22rpx;
  color: #999;
}
</style>
