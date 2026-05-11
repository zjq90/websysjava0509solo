<template>
  <view class="registration-container" :class="{ 'elderly-mode': isElderlyMode }">
    <view class="step-nav">
      <view class="step-item" :class="{ active: step === 1 }">
        <view class="step-dot">1</view>
        <text class="step-text">选择科室</text>
      </view>
      <view class="step-line" :class="{ active: step > 1 }"></view>
      <view class="step-item" :class="{ active: step === 2 }">
        <view class="step-dot">2</view>
        <text class="step-text">选择号源</text>
      </view>
      <view class="step-line" :class="{ active: step > 2 }"></view>
      <view class="step-item" :class="{ active: step === 3 }">
        <view class="step-dot">3</view>
        <text class="step-text">确认预约</text>
      </view>
    </view>
    
    <view class="step-content">
      <view v-if="step === 1" class="step-dept">
        <view class="section-title">请选择科室</view>
        <view class="dept-list">
          <view 
            v-for="dept in departments" 
            :key="dept.id" 
            :class="['dept-item', selectedDept && selectedDept.deptCode === dept.deptCode ? 'selected' : '']"
            @click="selectDept(dept)"
          >
            <text class="dept-name">{{ dept.deptName }}</text>
            <text class="check" v-if="selectedDept && selectedDept.deptCode === dept.deptCode">✓</text>
          </view>
        </view>
        <view class="btn-primary" @click="nextStep">下一步</view>
      </view>
      
      <view v-if="step === 2" class="step-schedule">
        <view class="dept-header">
          <text class="back" @click="prevStep">← 返回</text>
          <text class="dept-info">{{ selectedDept.deptName }}</text>
        </view>
        
        <view class="date-tabs">
          <scroll-view scroll-x class="date-scroll">
            <view 
              v-for="(d, idx) in weekDays" 
              :key="idx" 
              :class="['date-item', selectedDate === d.date ? 'active' : '']"
              @click="selectDate(d.date)"
            >
              <text class="week">{{ d.week }}</text>
              <text class="day">{{ d.day }}</text>
            </view>
          </scroll-view>
        </view>
        
        <view class="schedule-list" v-if="schedules.length > 0">
          <view 
            v-for="sch in schedules" 
            :key="sch.id" 
            :class="['schedule-item', sch.availableCount === 0 ? 'disabled' : '', selectedSchedule && selectedSchedule.id === sch.id ? 'selected' : '']"
            @click="selectSchedule(sch)"
          >
            <view class="schedule-left">
              <view class="doctor-info">
                <text class="doctor-name">{{ sch.doctorName }}</text>
                <text class="doctor-title">{{ sch.doctorTitle }}</text>
              </view>
              <view class="time-slot">
                <text class="time">{{ sch.timeSlot }}</text>
                <text class="fee">¥{{ sch.registrationFee }}</text>
              </view>
            </view>
            <view class="schedule-right">
              <text :class="['available', sch.availableCount <= 3 ? 'few' : '']">
                {{ sch.availableCount }}/{{ sch.totalCount }}
              </text>
              <text class="status">{{ sch.availableCount === 0 ? '已约满' : '可预约' }}</text>
            </view>
          </view>
        </view>
        
        <view class="empty" v-else>
          <text>该日期暂无号源</text>
        </view>
        
        <view class="btn-primary" @click="nextStep" v-if="selectedSchedule">确认预约</view>
      </view>
      
      <view v-if="step === 3" class="step-confirm">
        <view class="dept-header">
          <text class="back" @click="prevStep">← 返回</text>
          <text class="dept-info">确认预约信息</text>
        </view>
        
        <view class="confirm-card card">
          <view class="confirm-row">
            <text class="label">就诊科室</text>
            <text class="value">{{ selectedDept.deptName }}</text>
          </view>
          <view class="confirm-row">
            <text class="label">就诊医生</text>
            <text class="value">{{ selectedSchedule.doctorName }} {{ selectedSchedule.doctorTitle }}</text>
          </view>
          <view class="confirm-row">
            <text class="label">就诊日期</text>
            <text class="value">{{ selectedDate }}</text>
          </view>
          <view class="confirm-row">
            <text class="label">就诊时间</text>
            <text class="value">{{ selectedSchedule.timeSlot }}</text>
          </view>
          <view class="confirm-row">
            <text class="label">挂号费用</text>
            <text class="value price">¥{{ selectedSchedule.registrationFee }}</text>
          </view>
        </view>
        
        <view class="symptoms-card card">
          <view class="label">症状描述（选填）</view>
          <textarea 
            v-model="symptoms" 
            class="symptoms-input" 
            placeholder="请简要描述您的症状，如：发热、咳嗽、头痛等"
            maxlength="200"
          />
          <text class="count">{{ symptoms.length }}/200</text>
        </view>
        
        <view class="tips">
          <text class="tip-text">• 就诊前一日17:00前可自助取消</text>
          <text class="tip-text">• 改约请先取消再重新挂号</text>
          <text class="tip-text">• 请提前15分钟到达医院</text>
        </view>
        
        <view class="btn-primary" @click="submitRegistration">提交预约</view>
      </view>
    </view>
    
    <view class="payment-modal" v-if="showPayment">
      <view class="payment-mask" @click="showPayment = false"></view>
      <view class="payment-content">
        <view class="payment-header">
          <text class="payment-title">选择支付方式</text>
          <text class="close" @click="showPayment = false">×</text>
        </view>
        <view class="payment-amount">
          <text class="label">挂号费用</text>
          <text class="amount">¥{{ selectedSchedule.registrationFee }}</text>
        </view>
        <view class="payment-methods">
          <view 
            :class="['method-item', paymentMethod === 'WECHAT' ? 'selected' : '']"
            @click="paymentMethod = 'WECHAT'"
          >
            <text class="method-icon">💚</text>
            <text class="method-name">微信支付</text>
            <text class="radio" v-if="paymentMethod === 'WECHAT'">●</text>
          </view>
          <view 
            :class="['method-item', paymentMethod === 'ALIPAY' ? 'selected' : '']"
            @click="paymentMethod = 'ALIPAY'"
          >
            <text class="method-icon">💙</text>
            <text class="method-name">支付宝</text>
            <text class="radio" v-if="paymentMethod === 'ALIPAY'">●</text>
          </view>
        </view>
        <view class="btn-primary" @click="pay">确认支付</view>
      </view>
    </view>
  </view>
</template>

<script>
import { scheduleApi, registrationApi } from '@/utils/api.js'
import { getWeekDays } from '@/utils/date.js'

export default {
  data() {
    return {
      step: 1,
      departments: [],
      selectedDept: null,
      weekDays: [],
      selectedDate: '',
      schedules: [],
      selectedSchedule: null,
      symptoms: '',
      paymentMethod: 'WECHAT',
      showPayment: false,
      pendingRegistrationNo: '',
      isElderlyMode: false
    }
  },
  
  onLoad(options) {
    this.weekDays = getWeekDays()
    this.selectedDate = this.weekDays[0].date
    this.loadDepartments()
    this.isElderlyMode = uni.getStorageSync('elderlyMode') || false
    
    if (options.deptCode) {
      this.selectedDept = { deptCode: options.deptCode }
      this.step = 2
      this.loadSchedules()
    }
  },
  
  methods: {
    async loadDepartments() {
      try {
        const res = await scheduleApi.getDepartments()
        this.departments = res.data || []
        
        if (this.selectedDept) {
          const matched = this.departments.find(d => d.deptCode === this.selectedDept.deptCode)
          if (matched) this.selectedDept = matched
        }
      } catch (e) {}
    },
    
    selectDept(dept) {
      this.selectedDept = dept
    },
    
    selectDate(date) {
      this.selectedDate = date
      this.selectedSchedule = null
      this.loadSchedules()
    },
    
    selectSchedule(sch) {
      if (sch.availableCount === 0) {
        uni.showToast({ title: '号源已约满', icon: 'none' })
        return
      }
      this.selectedSchedule = sch
    },
    
    async loadSchedules() {
      if (!this.selectedDept) return
      
      uni.showLoading({ title: '加载中...' })
      try {
        const res = await scheduleApi.getSchedulesByDept(this.selectedDept.deptCode, this.selectedDate)
        this.schedules = res.data || []
      } catch (e) {
        this.schedules = []
      } finally {
        uni.hideLoading()
      }
    },
    
    nextStep() {
      if (this.step === 1) {
        if (!this.selectedDept) {
          uni.showToast({ title: '请选择科室', icon: 'none' })
          return
        }
        this.step = 2
        this.loadSchedules()
      } else if (this.step === 2) {
        if (!this.selectedSchedule) {
          uni.showToast({ title: '请选择号源', icon: 'none' })
          return
        }
        this.step = 3
      }
    },
    
    prevStep() {
      if (this.step > 1) {
        this.step--
      }
    },
    
    async submitRegistration() {
      uni.showLoading({ title: '提交中...' })
      try {
        const res = await registrationApi.create({
          scheduleId: this.selectedSchedule.id,
          symptoms: this.symptoms
        })
        this.pendingRegistrationNo = res.data.registrationNo
        uni.hideLoading()
        this.showPayment = true
      } catch (e) {
        uni.hideLoading()
      }
    },
    
    async pay() {
      uni.showLoading({ title: '支付中...' })
      try {
        await registrationApi.pay({
          registrationNo: this.pendingRegistrationNo,
          paymentMethod: this.paymentMethod
        })
        uni.hideLoading()
        this.showPayment = false
        uni.showToast({ title: '预约成功', icon: 'success' })
        setTimeout(() => {
          uni.redirectTo({ url: `/pages/detail/detail?registrationNo=${this.pendingRegistrationNo}` })
        }, 1000)
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.registration-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.step-nav {
  background: #fff;
  padding: 40rpx 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.step-dot {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: #e8e8e8;
  color: #999;
  text-align: center;
  line-height: 48rpx;
  font-size: 24rpx;
  margin-bottom: 8rpx;
}

.step-item.active .step-dot {
  background: #1890ff;
  color: #fff;
}

.step-text {
  font-size: 24rpx;
  color: #999;
}

.step-item.active .step-text {
  color: #1890ff;
}

.step-line {
  width: 80rpx;
  height: 4rpx;
  background: #e8e8e8;
  margin: 0 12rpx 36rpx;
}

.step-line.active {
  background: #1890ff;
}

.step-content {
  padding: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin: 20rpx 0;
}

.dept-list {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 40rpx;
}

.dept-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.dept-item:last-child {
  border-bottom: none;
}

.dept-item.selected {
  background: #e6f7ff;
}

.dept-name {
  font-size: 30rpx;
  color: #333;
}

.check {
  color: #1890ff;
  font-size: 32rpx;
}

.dept-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.back {
  font-size: 28rpx;
  color: #1890ff;
  margin-right: 16rpx;
}

.dept-info {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.date-tabs {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  padding: 20rpx 0;
}

.date-scroll {
  white-space: nowrap;
}

.date-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 32rpx;
}

.date-item.active {
  background: #e6f7ff;
  border-radius: 12rpx;
}

.week {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
}

.date-item.active .week {
  color: #1890ff;
}

.day {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.date-item.active .day {
  color: #1890ff;
}

.schedule-list {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 40rpx;
}

.schedule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.schedule-item:last-child {
  border-bottom: none;
}

.schedule-item.selected {
  background: #e6f7ff;
}

.schedule-item.disabled {
  opacity: 0.5;
}

.doctor-info {
  margin-bottom: 12rpx;
}

.doctor-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-right: 12rpx;
}

.doctor-title {
  font-size: 24rpx;
  color: #999;
}

.time-slot {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.time {
  font-size: 26rpx;
  color: #1890ff;
}

.fee {
  font-size: 26rpx;
  color: #ff4d4f;
}

.schedule-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.available {
  font-size: 26rpx;
  color: #52c41a;
}

.available.few {
  color: #faad14;
}

.status {
  font-size: 22rpx;
  color: #999;
  margin-top: 4rpx;
}

.empty {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
}

.confirm-card {
  padding: 0;
}

.confirm-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.confirm-row:last-child {
  border-bottom: none;
}

.confirm-row .label {
  font-size: 28rpx;
  color: #666;
}

.confirm-row .value {
  font-size: 28rpx;
  color: #333;
}

.confirm-row .value.price {
  color: #ff4d4f;
  font-weight: bold;
}

.symptoms-card {
  margin-top: 20rpx;
}

.symptoms-input {
  width: 100%;
  height: 160rpx;
  padding: 20rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 12rpx;
  font-size: 28rpx;
  margin-top: 16rpx;
  box-sizing: border-box;
}

.count {
  display: block;
  text-align: right;
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.tips {
  padding: 24rpx;
  background: #fffbe6;
  border-radius: 12rpx;
  margin: 20rpx 0 40rpx;
}

.tip-text {
  display: block;
  font-size: 24rpx;
  color: #faad14;
  line-height: 2;
}

.payment-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
}

.payment-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.payment-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 32rpx;
}

.payment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.payment-title {
  font-size: 32rpx;
  font-weight: bold;
}

.close {
  font-size: 48rpx;
  color: #999;
}

.payment-amount {
  text-align: center;
  padding: 40rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.payment-amount .label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.payment-amount .amount {
  font-size: 48rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.payment-methods {
  padding: 24rpx 0;
}

.method-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.method-item:last-child {
  border-bottom: none;
}

.method-item.selected {
  background: #fafafa;
}

.method-icon {
  font-size: 40rpx;
  margin-right: 16rpx;
}

.method-name {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.radio {
  color: #1890ff;
  font-size: 24rpx;
}
</style>
