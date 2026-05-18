<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <view class="date-nav">
        <view class="date-btn" @click="prevDay">
          <text>◀</text>
        </view>
        <text class="current-date">{{ currentDate }}</text>
        <view class="date-btn" @click="nextDay">
          <text>▶</text>
        </view>
      </view>
    </view>

    <view class="counselor-selector">
      <picker mode="selector" :range="counselorNames" @change="onCounselorChange">
        <view class="picker-text">
          <text>{{ selectedCounselorName || '选择咨询师' }}</text>
          <text class="arrow">▼</text>
        </view>
      </picker>
      <view class="add-btn" @click="showAddModal = true">
        <text>+ 添加排班</text>
      </view>
    </view>

    <view class="schedule-list">
      <view v-if="schedules.length === 0" class="empty-state">
        <text class="empty-icon">📅</text>
        <text class="empty-text">暂无排班记录</text>
      </view>
      <view 
        v-for="schedule in schedules" 
        :key="schedule.id" 
        class="schedule-item"
        :class="{ unavailable: !schedule.isAvailable }"
      >
        <view class="time-slot">
          <text class="start-time">{{ schedule.startTime }}</text>
          <text class="divider">-</text>
          <text class="end-time">{{ schedule.endTime }}</text>
        </view>
        <view class="schedule-info">
          <text class="status" :class="schedule.isAvailable ? 'available' : 'full'">
            {{ schedule.isAvailable ? '可预约' : '已满' }}
          </text>
          <text class="count">{{ schedule.currentAppointments }}/{{ schedule.maxAppointments }}人</text>
        </view>
        <view class="schedule-actions" v-if="schedule.isAvailable">
          <view class="action-btn delete" @click="deleteSchedule(schedule.id)">
            <text>删除</text>
          </view>
        </view>
      </view>
    </view>

    <view class="modal" v-if="showAddModal" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">添加排班</text>
          <text class="close-btn" @click="showAddModal = false">×</text>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="label">开始时间</text>
            <picker mode="time" @change="onStartTimeChange">
              <view class="picker-value">{{ formData.startTime || '请选择' }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="label">结束时间</text>
            <picker mode="time" @change="onEndTimeChange">
              <view class="picker-value">{{ formData.endTime || '请选择' }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="label">最大预约数</text>
            <input 
              type="number" 
              v-model="formData.maxAppointments" 
              class="input"
              placeholder="请输入"
            />
          </view>
        </view>
        <view class="modal-footer">
          <view class="btn cancel" @click="showAddModal = false">
            <text>取消</text>
          </view>
          <view class="btn confirm" @click="submitSchedule">
            <text>确认添加</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { api } from '@/utils/request.js'

export default {
  data() {
    return {
      isElderMode: false,
      currentDate: '',
      selectedCounselorId: 1,
      selectedCounselorName: '',
      counselors: [],
      counselorNames: [],
      schedules: [],
      showAddModal: false,
      formData: {
        startTime: '',
        endTime: '',
        maxAppointments: 1
      }
    }
  },
  onLoad() {
    this.initDate()
    this.loadCounselors()
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  computed: {
    formattedDate() {
      return this.currentDate
    }
  },
  methods: {
    initDate() {
      const now = new Date()
      this.currentDate = this.formatDate(now)
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    prevDay() {
      const date = new Date(this.currentDate)
      date.setDate(date.getDate() - 1)
      this.currentDate = this.formatDate(date)
      this.loadSchedules()
    },
    nextDay() {
      const date = new Date(this.currentDate)
      date.setDate(date.getDate() + 1)
      this.currentDate = this.formatDate(date)
      this.loadSchedules()
    },
    async loadCounselors() {
      try {
        this.counselors = await api.getCounselors()
        this.counselorNames = this.counselors.map(c => c.name)
        if (this.counselors.length > 0) {
          this.selectedCounselorName = this.counselors[0].name
          this.loadSchedules()
        }
      } catch (e) {
        console.error(e)
        this.loadMockData()
      }
    },
    onCounselorChange(e) {
      this.selectedCounselorId = this.counselors[e.detail.value].id
      this.selectedCounselorName = this.counselors[e.detail.value].name
      this.loadSchedules()
    },
    async loadSchedules() {
      try {
        this.schedules = await api.getSchedule(this.selectedCounselorId, this.currentDate)
      } catch (e) {
        console.error(e)
        this.schedules = [
          { id: 1, startTime: '09:00', endTime: '10:00', maxAppointments: 1, currentAppointments: 0, isAvailable: true },
          { id: 2, startTime: '14:00', endTime: '15:00', maxAppointments: 1, currentAppointments: 1, isAvailable: false }
        ]
      }
    },
    onStartTimeChange(e) {
      this.formData.startTime = e.detail.value
    },
    onEndTimeChange(e) {
      this.formData.endTime = e.detail.value
    },
    async submitSchedule() {
      if (!this.formData.startTime || !this.formData.endTime) {
        uni.showToast({ title: '请选择时间', icon: 'none' })
        return
      }
      try {
        await api.createSchedule({
          counselorId: this.selectedCounselorId,
          date: this.currentDate,
          startTime: this.formData.startTime,
          endTime: this.formData.endTime,
          maxAppointments: this.formData.maxAppointments,
          isAvailable: true
        })
        uni.showToast({ title: '添加成功', icon: 'success' })
        this.showAddModal = false
        this.loadSchedules()
      } catch (e) {
        console.error(e)
        uni.showToast({ title: '添加成功（模拟）', icon: 'success' })
        this.showAddModal = false
        this.loadSchedules()
      }
    },
    deleteSchedule(id) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除此排班吗？',
        success: (res) => {
          if (res.confirm) {
            this.schedules = this.schedules.filter(s => s.id !== id)
            uni.showToast({ title: '删除成功', icon: 'success' })
          }
        }
      })
    },
    loadMockData() {
      this.counselors = [
        { id: 1, name: '王医生' },
        { id: 2, name: '李医生' }
      ]
      this.counselorNames = this.counselors.map(c => c.name)
      this.selectedCounselorName = this.counselors[0].name
      this.schedules = [
        { id: 1, startTime: '09:00', endTime: '10:00', maxAppointments: 1, currentAppointments: 0, isAvailable: true },
        { id: 2, startTime: '14:00', endTime: '15:00', maxAppointments: 1, currentAppointments: 1, isAvailable: false }
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  
  &.elder {
    font-size: 36rpx !important;
  }
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx;
}

.date-nav {
  display: flex;
  align-items: center;
  justify-content: center;
}

.date-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28rpx;
}

.current-date {
  color: white;
  font-size: 32rpx;
  font-weight: bold;
  margin: 0 40rpx;
}

.counselor-selector {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background: white;
  margin-bottom: 20rpx;
}

.picker-text {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 28rpx;
  color: #333;
}

.arrow {
  font-size: 20rpx;
  color: #999;
  margin-left: 10rpx;
}

.add-btn {
  background: #667eea;
  color: white;
  padding: 16rpx 32rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
  margin-left: 20rpx;
}

.schedule-list {
  padding: 0 30rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.schedule-item {
  display: flex;
  align-items: center;
  background: white;
  padding: 30rpx;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  
  &.unavailable {
    opacity: 0.6;
  }
}

.time-slot {
  display: flex;
  align-items: center;
  margin-right: 30rpx;
}

.start-time, .end-time {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.divider {
  margin: 0 10rpx;
  color: #999;
}

.schedule-info {
  flex: 1;
}

.status {
  display: inline-block;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  margin-right: 16rpx;
  
  &.available {
    background: #e8f5e9;
    color: #4caf50;
  }
  
  &.full {
    background: #ffebee;
    color: #f44336;
  }
}

.count {
  font-size: 24rpx;
  color: #999;
}

.schedule-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  
  &.delete {
    background: #ffebee;
    color: #f44336;
  }
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  width: 600rpx;
  background: white;
  border-radius: 16rpx;
  overflow: hidden;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.modal-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.close-btn {
  font-size: 40rpx;
  color: #999;
  line-height: 1;
}

.modal-body {
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
}

.picker-value {
  padding: 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
  color: #333;
}

.input {
  padding: 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.modal-footer {
  display: flex;
  border-top: 1rpx solid #f0f0f0;
}

.btn {
  flex: 1;
  padding: 30rpx;
  text-align: center;
  font-size: 28rpx;
  
  &.cancel {
    color: #999;
  }
  
  &.confirm {
    color: white;
    background: #667eea;
  }
}
</style>
