<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="calendar-header card">
      <view class="month-nav flex-between">
        <text class="nav-btn" @click="prevMonth">◀</text>
        <text class="month-text">{{ currentYear }}年{{ currentMonth }}月</text>
        <text class="nav-btn" @click="nextMonth">▶</text>
      </view>
      
      <view class="weekdays">
        <text v-for="day in weekDays" :key="day" class="weekday">{{ day }}</text>
      </view>
      
      <view class="days-grid">
        <view 
          v-for="day in calendarDays" 
          :key="day.date" 
          class="day-cell"
          :class="{ 
            'today': day.isToday,
            'other-month': day.isOtherMonth,
            'has-schedule': day.hasSchedule,
            'is-holiday': day.isHoliday
          }"
          @click="selectDate(day)"
        >
          <text class="day-number">{{ day.day }}</text>
          <view v-if="day.hasSchedule" class="schedule-dot"></view>
          <view v-if="day.isHoliday" class="holiday-mark">休</view>
        </view>
      </view>
    </view>
    
    <view class="schedule-section card" v-if="selectedDate">
      <view class="section-header flex-between">
        <text class="section-title">{{ selectedDateStr }} 排班</text>
        <text class="add-btn" @click="showAddSchedule">+ 添加排班</text>
      </view>
      
      <view class="schedule-list">
        <view v-for="schedule in daySchedules" :key="schedule.id" class="schedule-item">
          <view class="schedule-shift">{{ getShiftText(schedule.shiftType) }}</view>
          <view class="schedule-delete" @click="deleteSchedule(schedule.id)">×</view>
        </view>
        <view v-if="daySchedules.length === 0" class="empty-schedule">
          <text>暂无排班</text>
        </view>
      </view>
    </view>
    
    <view class="request-section card">
      <view class="section-header flex-between">
        <text class="section-title">调班申请</text>
        <text class="add-btn" @click="showChangeRequest">+ 申请调班</text>
      </view>
      
      <view class="request-list">
        <view v-for="req in changeRequests" :key="req.id" class="request-item">
          <view class="request-info">
            <text class="request-date">{{ req.targetDate }} {{ getShiftText(req.targetShift) }}</text>
            <text class="request-reason">{{ req.reason }}</text>
          </view>
          <view class="request-status" :class="'status-' + req.status.toLowerCase()">{{ getRequestStatusText(req.status) }}</view>
        </view>
        <view v-if="changeRequests.length === 0" class="empty-request">
          <text>暂无调班申请</text>
        </view>
      </view>
    </view>
    
    <view class="holiday-section card">
      <view class="section-header flex-between">
        <text class="section-title">休息日设置</text>
        <text class="add-btn" @click="showAddHoliday">+ 设置休息日</text>
      </view>
      
      <view class="holiday-list">
        <view v-for="holiday in holidays" :key="holiday.id" class="holiday-item">
          <text class="holiday-date">{{ holiday.holidayDate }}</text>
          <text class="holiday-type">{{ holiday.holidayType === 'PERSONAL' ? '事假' : '年假' }}</text>
          <view class="holiday-delete" @click="deleteHoliday(holiday.id)">×</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import scheduleApi from '../../api/schedule.js'

const userStore = useUserStore()

const weekDays = ['日', '一', '二', '三', '四', '五', '六']
const shiftOptions = ['上午班', '下午班', '夜班']

const currentYear = ref(new Date().getFullYear())
const currentMonth = ref(new Date().getMonth() + 1)
const selectedDate = ref(null)
const schedules = ref([])
const holidays = ref([])
const changeRequests = ref([])
const elderMode = computed(() => userStore.elderMode)

const calendarDays = computed(() => {
  const days = []
  const firstDay = new Date(currentYear.value, currentMonth.value - 1, 1)
  const lastDay = new Date(currentYear.value, currentMonth.value, 0)
  const today = new Date()
  
  const startPadding = firstDay.getDay()
  
  for (let i = 0; i < startPadding; i++) {
    const prevDate = new Date(currentYear.value, currentMonth.value - 1, -i)
    days.unshift({
      date: prevDate.toISOString().split('T')[0],
      day: prevDate.getDate(),
      isOtherMonth: true,
      isToday: false,
      hasSchedule: hasSchedule(prevDate),
      isHoliday: isHoliday(prevDate)
    })
  }
  
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const date = new Date(currentYear.value, currentMonth.value - 1, i)
    days.push({
      date: date.toISOString().split('T')[0],
      day: i,
      isOtherMonth: false,
      isToday: date.toDateString() === today.toDateString(),
      hasSchedule: hasSchedule(date),
      isHoliday: isHoliday(date)
    })
  }
  
  const remainingDays = 42 - days.length
  for (let i = 1; i <= remainingDays; i++) {
    const nextDate = new Date(currentYear.value, currentMonth.value, i)
    days.push({
      date: nextDate.toISOString().split('T')[0],
      day: nextDate.getDate(),
      isOtherMonth: true,
      isToday: false,
      hasSchedule: hasSchedule(nextDate),
      isHoliday: isHoliday(nextDate)
    })
  }
  
  return days
})

const selectedDateStr = computed(() => {
  if (!selectedDate.value) return ''
  return selectedDate.value.date
})

const daySchedules = computed(() => {
  if (!selectedDate.value) return []
  return schedules.value.filter(s => s.scheduleDate === selectedDate.value.date)
})

const hasSchedule = (date) => {
  const dateStr = date.toISOString().split('T')[0]
  return schedules.value.some(s => s.scheduleDate === dateStr)
}

const isHoliday = (date) => {
  const dateStr = date.toISOString().split('T')[0]
  return holidays.value.some(h => h.holidayDate === dateStr)
}

const getShiftText = (shift) => {
  const map = {
    'MORNING': '上午班',
    'AFTERNOON': '下午班',
    'NIGHT': '夜班'
  }
  return map[shift] || shift
}

const getRequestStatusText = (status) => {
  const map = {
    'PENDING': '待审批',
    'APPROVED': '已通过',
    'REJECTED': '已拒绝'
  }
  return map[status] || status
}

const prevMonth = () => {
  if (currentMonth.value === 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value--
  }
}

const nextMonth = () => {
  if (currentMonth.value === 12) {
    currentMonth.value = 1
    currentYear.value++
  } else {
    currentMonth.value++
  }
}

const selectDate = (day) => {
  selectedDate.value = day
}

const showAddSchedule = () => {
  uni.showActionSheet({
    itemList: shiftOptions,
    success: async (res) => {
      const shiftType = ['MORNING', 'AFTERNOON', 'NIGHT'][res.tapIndex]
      try {
        await scheduleApi.addSchedule({
          doctorId: userStore.userInfo?.id || 1,
          scheduleDate: selectedDateStr.value,
          shiftType,
          status: 'CONFIRMED'
        })
        loadSchedules()
        uni.showToast({ title: '排班添加成功', icon: 'success' })
      } catch (e) {
        console.error(e)
      }
    }
  })
}

const deleteSchedule = async (id) => {
  uni.showModal({
    title: '提示',
    content: '确定删除该排班吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await scheduleApi.deleteSchedule(id)
          loadSchedules()
          uni.showToast({ title: '删除成功', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

const showChangeRequest = () => {
  uni.showModal({
    title: '申请调班',
    editable: true,
    placeholderText: '请输入调班原因',
    success: async (res) => {
      if (res.confirm && res.content) {
        try {
          await scheduleApi.createChangeRequest({
            doctorId: userStore.userInfo?.id || 1,
            targetDate: selectedDateStr.value || new Date().toISOString().split('T')[0],
            targetShift: 'MORNING',
            reason: res.content,
            status: 'PENDING'
          })
          loadChangeRequests()
          uni.showToast({ title: '申请已提交', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

const showAddHoliday = () => {
  uni.showActionSheet({
    itemList: ['事假', '年假'],
    success: async (res) => {
      const holidayType = ['PERSONAL', 'ANNUAL'][res.tapIndex]
      try {
        await scheduleApi.addHoliday({
          doctorId: userStore.userInfo?.id || 1,
          holidayDate: selectedDateStr.value || new Date().toISOString().split('T')[0],
          holidayType,
          status: 'APPROVED'
        })
        loadHolidays()
        uni.showToast({ title: '休息日设置成功', icon: 'success' })
      } catch (e) {
        console.error(e)
      }
    }
  })
}

const deleteHoliday = async (id) => {
  uni.showModal({
    title: '提示',
    content: '确定取消该休息日吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await scheduleApi.deleteHoliday(id)
          loadHolidays()
          uni.showToast({ title: '取消成功', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

const loadSchedules = async () => {
  try {
    const startDate = new Date(currentYear.value, currentMonth.value - 2, 1).toISOString().split('T')[0]
    const endDate = new Date(currentYear.value, currentMonth.value + 1, 0).toISOString().split('T')[0]
    const res = await scheduleApi.getAllSchedule(startDate, endDate)
    schedules.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const loadHolidays = async () => {
  try {
    const res = await scheduleApi.getAllHolidays()
    holidays.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const loadChangeRequests = async () => {
  try {
    const res = await scheduleApi.getPendingRequests()
    changeRequests.value = res || []
  } catch (e) {
    console.error(e)
  }
}

onShow(() => {
  loadSchedules()
  loadHolidays()
  loadChangeRequests()
})
</script>

<style scoped>
.container {
  padding: 20rpx;
}

.card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.month-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.nav-btn {
  font-size: 32rpx;
  color: #409EFF;
  padding: 10rpx 20rpx;
}

.month-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
}

.weekdays {
  display: flex;
  margin-bottom: 16rpx;
}

.weekday {
  flex: 1;
  text-align: center;
  font-size: 24rpx;
  color: #909399;
}

.days-grid {
  display: flex;
  flex-wrap: wrap;
}

.day-cell {
  width: calc(100% / 7);
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  color: #303133;
  border-radius: 8rpx;
  position: relative;
}

.day-cell.today {
  background-color: #ECF5FF;
  color: #409EFF;
  font-weight: bold;
}

.day-cell.other-month {
  color: #C0C4CC;
}

.day-cell.has-schedule {
  background-color: #F0F9EB;
}

.day-cell.is-holiday {
  background-color: #FEF0F0;
  color: #F56C6C;
}

.schedule-dot {
  width: 8rpx;
  height: 8rpx;
  background-color: #67C23A;
  border-radius: 50%;
  position: absolute;
  bottom: 8rpx;
}

.holiday-mark {
  font-size: 18rpx;
  color: #F56C6C;
  position: absolute;
  bottom: 4rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
}

.add-btn {
  font-size: 26rpx;
  color: #409EFF;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.schedule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 20rpx;
  background-color: #f5f7fa;
  border-radius: 8rpx;
}

.schedule-shift {
  font-size: 26rpx;
  color: #303133;
}

.schedule-delete {
  font-size: 32rpx;
  color: #F56C6C;
  width: 40rpx;
  text-align: center;
}

.empty-schedule, .empty-request {
  text-align: center;
  padding: 40rpx 0;
  color: #909399;
  font-size: 26rpx;
}

.request-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.request-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16rpx 20rpx;
  background-color: #f5f7fa;
  border-radius: 8rpx;
}

.request-info {
  flex: 1;
}

.request-date {
  display: block;
  font-size: 26rpx;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4rpx;
}

.request-reason {
  display: block;
  font-size: 24rpx;
  color: #606266;
}

.request-status {
  font-size: 22rpx;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
}

.status-pending {
  background-color: #FDF6EC;
  color: #E6A23C;
}

.status-approved {
  background-color: #F0F9EB;
  color: #67C23A;
}

.status-rejected {
  background-color: #FEF0F0;
  color: #F56C6C;
}

.holiday-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.holiday-item {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 12rpx 20rpx;
  background-color: #FEF0F0;
  border-radius: 8rpx;
}

.holiday-date {
  font-size: 26rpx;
  color: #F56C6C;
  font-weight: bold;
}

.holiday-type {
  font-size: 22rpx;
  color: #F56C6C;
}

.holiday-delete {
  font-size: 28rpx;
  color: #F56C6C;
}

.elder-mode .month-text {
  font-size: 36rpx;
}

.elder-mode .section-title {
  font-size: 32rpx;
}

.elder-mode .day-cell {
  font-size: 30rpx;
}

.elder-mode .schedule-shift {
  font-size: 30rpx;
}

.elder-mode .request-date {
  font-size: 30rpx;
}

.elder-mode .holiday-date {
  font-size: 30rpx;
}
</style>
