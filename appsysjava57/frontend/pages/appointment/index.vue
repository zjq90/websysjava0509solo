<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="department-section">
      <text class="section-title">选择科室</text>
      <view class="department-grid">
        <view 
          class="department-item" 
          :class="{ active: selectedDept === dept.id }"
          v-for="dept in departments" 
          :key="dept.id"
          @click="selectDepartment(dept)"
        >
          <text class="dept-icon">{{ dept.icon }}</text>
          <text class="dept-name">{{ dept.name }}</text>
        </view>
      </view>
    </view>

    <view class="doctor-section" v-if="selectedDept">
      <text class="section-title">选择医生</text>
      <view class="doctor-list">
        <view 
          class="doctor-card" 
          :class="{ active: selectedDoctor === doctor.id }"
          v-for="doctor in doctors" 
          :key="doctor.id"
          @click="selectDoctor(doctor)"
        >
          <view class="doctor-avatar">
            <text class="avatar-text">{{ doctor.name.charAt(0) }}</text>
          </view>
          <view class="doctor-info">
            <text class="doctor-name">{{ doctor.name }}</text>
            <text class="doctor-title">{{ doctor.title }}</text>
            <view class="doctor-rating">
              <text class="rating-star">⭐</text>
              <text class="rating-score">{{ doctor.rating }}</text>
            </view>
            <text class="doctor-specialty">擅长：{{ doctor.specialties }}</text>
          </view>
          <view class="doctor-fee">
            <text class="fee-price">¥{{ doctor.consultationFee }}</text>
            <text class="fee-label">挂号费</text>
          </view>
        </view>
      </view>
    </view>

    <view class="schedule-section" v-if="selectedDoctor">
      <text class="section-title">选择时间</text>
      <view class="schedule-list">
        <view 
          class="schedule-item" 
          :class="{ active: selectedSchedule === schedule.id, disabled: !schedule.available }"
          v-for="schedule in schedules" 
          :key="schedule.id"
          @click="selectSchedule(schedule)"
        >
          <text class="schedule-date">{{ schedule.date }}</text>
          <text class="schedule-time">{{ schedule.time }}</text>
          <text class="schedule-available" v-if="schedule.available">可预约</text>
          <text class="schedule-full" v-else>已满</text>
        </view>
      </view>
    </view>

    <view class="submit-section" v-if="selectedSchedule">
      <button class="submit-btn" @click="submitAppointment">
        确认预约
      </button>
    </view>
  </view>
</template>

<script>
import { appointmentApi } from '@/utils/api.js'

export default {
  data() {
    return {
      isElderMode: false,
      selectedDept: null,
      selectedDoctor: null,
      selectedSchedule: null,
      departments: [
        { id: 1, name: '内科', icon: '❤️' },
        { id: 2, name: '外科', icon: '🩹' },
        { id: 3, name: '皮肤科', icon: '🟡' },
        { id: 4, name: '牙科', icon: '🦷' },
        { id: 5, name: '眼科', icon: '👁️' },
        { id: 6, name: '骨科', icon: '🦴' }
      ],
      doctors: [],
      schedules: []
    }
  },
  onLoad() {
    this.checkElderMode()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
    },
    selectDepartment(dept) {
      this.selectedDept = dept.id
      this.selectedDoctor = null
      this.selectedSchedule = null
      this.loadDoctors(dept.name)
    },
    async loadDoctors(department) {
      try {
        const res = await appointmentApi.getDoctorsByDepartment(department)
        if (res.code === 200 || res.code === 0) {
          this.doctors = res.data || []
        }
      } catch (e) {
        console.log('加载医生失败', e)
        // 使用模拟数据
        this.doctors = [
          { id: 1, name: '王医生', title: '主任医师', rating: 4.9, consultationFee: 50, specialties: '内科常见病、慢性病诊治' },
          { id: 2, name: '李医生', title: '副主任医师', rating: 4.7, consultationFee: 40, specialties: '消化系统疾病' },
          { id: 3, name: '张医生', title: '主治医师', rating: 4.5, consultationFee: 30, specialties: '呼吸系统疾病' }
        ]
      }
    },
    selectDoctor(doctor) {
      this.selectedDoctor = doctor.id
      this.selectedSchedule = null
      this.loadSchedules(doctor.id)
    },
    loadSchedules(doctorId) {
      const today = new Date()
      this.schedules = []
      for (let i = 1; i <= 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + i)
        const dateStr = `${date.getMonth() + 1}月${date.getDate()}日`
        const weekDay = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][date.getDay()]
        
        this.schedules.push({
          id: i,
          date: `${dateStr} ${weekDay}`,
          time: '上午 09:00-12:00',
          available: i <= 5
        })
        this.schedules.push({
          id: i + 10,
          date: `${dateStr} ${weekDay}`,
          time: '下午 14:00-17:00',
          available: i <= 3
        })
      }
    },
    selectSchedule(schedule) {
      if (schedule.available) {
        this.selectedSchedule = schedule.id
      }
    },
    submitAppointment() {
      uni.showModal({
        title: '预约确认',
        content: '确认提交预约？预约成功后将收到短信和App通知。',
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '提交中...' })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '预约成功！',
                icon: 'success',
                duration: 2000
              })
              setTimeout(() => {
                uni.navigateBack()
              }, 2000)
            }, 1000)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
  padding-bottom: 120rpx;
}

.section-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.department-section,
.doctor-section,
.schedule-section {
  margin-bottom: 30rpx;
}

.department-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.department-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx 16rpx;
  text-align: center;
  border: 2rpx solid transparent;
}

.department-item.active {
  background: #E8F5E9;
  border-color: #4CAF50;
}

.dept-icon {
  display: block;
  font-size: 48rpx;
  margin-bottom: 8rpx;
}

.dept-name {
  font-size: 26rpx;
  color: #333;
}

.doctor-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.doctor-card {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  border: 2rpx solid transparent;
}

.doctor-card.active {
  background: #E8F5E9;
  border-color: #4CAF50;
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
  font-size: 40rpx;
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
  margin-bottom: 6rpx;
}

.doctor-title {
  display: block;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 6rpx;
}

.doctor-rating {
  display: flex;
  align-items: center;
  margin-bottom: 6rpx;
}

.rating-star {
  font-size: 22rpx;
  margin-right: 4rpx;
}

.rating-score {
  font-size: 22rpx;
  color: #FF9800;
  font-weight: bold;
}

.doctor-specialty {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.doctor-fee {
  text-align: right;
}

.fee-price {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #4CAF50;
}

.fee-label {
  font-size: 20rpx;
  color: #999;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.schedule-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  display: flex;
  align-items: center;
  border: 2rpx solid transparent;
}

.schedule-item.active {
  background: #E8F5E9;
  border-color: #4CAF50;
}

.schedule-item.disabled {
  opacity: 0.5;
}

.schedule-date {
  font-size: 28rpx;
  color: #333;
  width: 180rpx;
}

.schedule-time {
  flex: 1;
  font-size: 26rpx;
  color: #666;
}

.schedule-available {
  font-size: 24rpx;
  color: #4CAF50;
}

.schedule-full {
  font-size: 24rpx;
  color: #999;
}

.submit-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.submit-btn {
  width: 100%;
  background: linear-gradient(135deg, #4CAF50, #45a049);
  color: #fff;
  border-radius: 50rpx;
  border: none;
  padding: 28rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.elder-mode .section-title {
  font-size: 36rpx;
}

.elder-mode .dept-name {
  font-size: 30rpx;
}

.elder-mode .doctor-name,
.elder-mode .fee-price {
  font-size: 34rpx;
}

.elder-mode .schedule-date,
.elder-mode .schedule-time {
  font-size: 30rpx;
}

.elder-mode .submit-btn {
  font-size: 36rpx;
  padding: 36rpx;
}
</style>
