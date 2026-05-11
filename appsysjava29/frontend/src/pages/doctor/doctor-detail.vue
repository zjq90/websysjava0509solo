<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="doctor-card" v-if="doctor">
      <view class="header">
        <view class="avatar">
          <text>👨‍⚕️</text>
        </view>
        <view class="info">
          <view class="name-row">
            <text class="name">{{ doctor.doctorName }}</text>
            <text class="title" v-if="doctor.title">{{ doctor.title }}</text>
          </view>
          <text class="dept">{{ doctor.deptName }}</text>
          <view class="tags">
            <text class="tag" v-if="doctor.isExpert === 1">专家号</text>
            <text class="tag fee">¥{{ doctor.consultationFee }}</text>
          </view>
        </view>
      </view>
      
      <view class="section">
        <text class="section-title">擅长领域</text>
        <text class="section-content">{{ doctor.specialty || '暂无介绍' }}</text>
      </view>
    </view>

    <view class="schedule-section" v-if="doctor">
      <view class="section-header">
        <text class="section-title">可预约时间</text>
      </view>
      
      <view class="date-list">
        <view 
          class="date-item" 
          v-for="date in availableDates" 
          :key="date"
          :class="{ active: selectedDate === date }"
          @click="selectDate(date)"
        >
          <text class="weekday">{{ getWeekday(date) }}</text>
          <text class="date-num">{{ formatDate(date) }}</text>
        </view>
      </view>

      <view class="slots-section" v-if="slots.length > 0">
        <text class="slots-title">选择时段（15分钟/段）</text>
        
        <view class="time-group" v-if="morningSlots.length > 0">
          <text class="group-title">上午</text>
          <view class="slot-list">
            <view 
              class="slot-item" 
              v-for="slot in morningSlots" 
              :key="slot.id"
              :class="{ 
                selected: selectedSlot?.id === slot.id,
                locked: slot.status === 'LOCKED',
                booked: slot.status === 'BOOKED'
              }"
              @click="selectSlot(slot)"
            >
              <text class="slot-time">{{ slot.startTime }}-{{ slot.endTime }}</text>
              <text class="slot-status" v-if="slot.status === 'AVAILABLE'">可预约</text>
              <text class="slot-status locked" v-else-if="slot.status === 'LOCKED'">已锁定</text>
              <text class="slot-status booked" v-else-if="slot.status === 'BOOKED'">已约满</text>
            </view>
          </view>
        </view>

        <view class="time-group" v-if="afternoonSlots.length > 0">
          <text class="group-title">下午</text>
          <view class="slot-list">
            <view 
              class="slot-item" 
              v-for="slot in afternoonSlots" 
              :key="slot.id"
              :class="{ 
                selected: selectedSlot?.id === slot.id,
                locked: slot.status === 'LOCKED',
                booked: slot.status === 'BOOKED'
              }"
              @click="selectSlot(slot)"
            >
              <text class="slot-time">{{ slot.startTime }}-{{ slot.endTime }}</text>
              <text class="slot-status" v-if="slot.status === 'AVAILABLE'">可预约</text>
              <text class="slot-status locked" v-else-if="slot.status === 'LOCKED'">已锁定</text>
              <text class="slot-status booked" v-else-if="slot.status === 'BOOKED'">已约满</text>
            </view>
          </view>
        </view>
      </view>

      <view class="empty-slots" v-else-if="selectedDate">
        <text>当日暂无号源</text>
      </view>
    </view>

    <view class="bottom-bar" v-if="selectedSlot">
      <view class="lock-info" v-if="lockCountdown > 0">
        <text class="lock-icon">🔒</text>
        <text class="lock-text">号源已锁定，请在 {{ lockCountdown }} 秒内完成支付</text>
      </view>
      <view class="btn-primary confirm-btn" @click="goToBooking">
        <text>确认预约</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onLoad, onUnmounted } from 'vue'
import { useStore } from 'vuex'
import { doctorApi, scheduleApi } from '@/utils/api'

const store = useStore()

const doctorId = ref(null)
const doctor = ref(null)
const availableDates = ref([])
const selectedDate = ref('')
const slots = ref([])
const selectedSlot = ref(null)
const lockCountdown = ref(0)
let lockTimer = null

const elderMode = computed(() => store.getters.elderMode)

const morningSlots = computed(() => 
  slots.value.filter(s => parseInt(s.startTime) < 1200)
)

const afternoonSlots = computed(() => 
  slots.value.filter(s => parseInt(s.startTime) >= 1200)
)

onLoad((options) => {
  doctorId.value = options.id
})

const getWeekday = (dateStr) => {
  const date = new Date(dateStr)
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  return weekdays[date.getDay()]
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return `${date.getMonth() + 1}/${date.getDate()}`
}

const loadDoctor = async () => {
  try {
    doctor.value = await doctorApi.getById(doctorId.value)
  } catch (e) {
    console.error('加载医生信息失败:', e)
  }
}

const loadAvailableDates = async () => {
  try {
    const dates = await scheduleApi.getAvailableDates()
    availableDates.value = dates.slice(0, 7)
    if (availableDates.value.length > 0) {
      selectedDate.value = availableDates.value[0]
      loadSlots()
    }
  } catch (e) {
    console.error('加载日期失败:', e)
  }
}

const selectDate = (date) => {
  if (selectedSlot.value) {
    releaseSlot()
  }
  selectedDate.value = date
  loadSlots()
}

const loadSlots = async () => {
  if (!doctorId.value || !selectedDate.value) return
  
  try {
    const schedules = await scheduleApi.getByDoctor(doctorId.value, selectedDate.value)
    if (schedules && schedules.length > 0) {
      slots.value = await scheduleApi.getSlots(schedules[0].id)
    } else {
      slots.value = []
    }
  } catch (e) {
    console.error('加载号源失败:', e)
    slots.value = []
  }
}

const selectSlot = async (slot) => {
  if (slot.status !== 'AVAILABLE') return
  
  if (!store.getters.isLoggedIn) {
    uni.navigateTo({ url: '/pages/login/login' })
    return
  }

  if (selectedSlot.value && selectedSlot.value.id !== slot.id) {
    await releaseSlot()
  }

  try {
    await scheduleApi.lockSlot(slot.id)
    selectedSlot.value = slot
    startLockCountdown()
  } catch (e) {
    uni.showToast({ title: '号源已被锁定', icon: 'none' })
  }
}

const releaseSlot = async () => {
  if (selectedSlot.value) {
    try {
      await scheduleApi.unlockSlot(selectedSlot.value.id)
    } catch (e) {}
    selectedSlot.value = null
    stopLockCountdown()
  }
}

const startLockCountdown = () => {
  lockCountdown.value = 600
  lockTimer = setInterval(() => {
    lockCountdown.value--
    if (lockCountdown.value <= 0) {
      stopLockCountdown()
      selectedSlot.value = null
      loadSlots()
      uni.showToast({ title: '号源锁定已超时', icon: 'none' })
    }
  }, 1000)
}

const stopLockCountdown = () => {
  if (lockTimer) {
    clearInterval(lockTimer)
    lockTimer = null
  }
  lockCountdown.value = 0
}

const goToBooking = () => {
  store.commit('SET_CURRENT_APPOINTMENT', {
    doctor: doctor.value,
    slot: selectedSlot.value,
    date: selectedDate.value
  })
  uni.navigateTo({ url: '/pages/appointment/appointment-booking' })
}

onUnmounted(() => {
  stopLockCountdown()
  releaseSlot()
})

onMounted(() => {
  loadDoctor()
  loadAvailableDates()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.doctor-card {
  background: #fff;
  padding: 32rpx;
}

.header {
  display: flex;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background: #e6f7ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 56rpx;
  margin-right: 24rpx;
}

.info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 8rpx;
}

.name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.title {
  font-size: 26rpx;
  color: #1890ff;
}

.dept {
  font-size: 26rpx;
  color: #999;
  display: block;
  margin-bottom: 12rpx;
}

.tags {
  display: flex;
  gap: 12rpx;
}

.tag {
  font-size: 22rpx;
  color: #ff4d4f;
  background: #fff1f0;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.tag.fee {
  color: #52c41a;
  background: #f6ffed;
}

.section {
  padding-top: 24rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 12rpx;
}

.section-content {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.schedule-section {
  margin-top: 20rpx;
  background: #fff;
  padding: 24rpx;
}

.date-list {
  display: flex;
  gap: 16rpx;
  overflow-x: auto;
  padding-bottom: 20rpx;
  margin-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.date-item {
  min-width: 120rpx;
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  text-align: center;
}

.date-item.active {
  background: #1890ff;
}

.date-item.active .weekday,
.date-item.active .date-num {
  color: #fff;
}

.weekday {
  font-size: 24rpx;
  color: #666;
  display: block;
  margin-bottom: 8rpx;
}

.date-num {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.slots-title {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 20rpx;
}

.time-group {
  margin-bottom: 28rpx;
}

.group-title {
  font-size: 26rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 16rpx;
}

.slot-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16rpx;
}

.slot-item {
  padding: 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  text-align: center;
}

.slot-item.selected {
  border-color: #1890ff;
  background: #e6f7ff;
}

.slot-item.locked {
  background: #fffbe6;
  border-color: #ffec3d;
}

.slot-item.booked {
  background: #f5f5f5;
  opacity: 0.5;
}

.slot-time {
  font-size: 26rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.slot-status {
  font-size: 22rpx;
  color: #52c41a;
}

.slot-status.locked {
  color: #faad14;
}

.slot-status.booked {
  color: #999;
}

.empty-slots {
  text-align: center;
  padding: 60rpx 0;
  color: #999;
  font-size: 28rpx;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.lock-info {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12rpx 0;
  background: #fffbe6;
  border-radius: 8rpx;
  margin-bottom: 16rpx;
}

.lock-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.lock-text {
  font-size: 24rpx;
  color: #d48806;
}

.confirm-btn {
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.elder-mode-enabled {
  .slot-time {
    font-size: 30rpx;
  }
  
  .name {
    font-size: 40rpx;
  }
  
  .section-title {
    font-size: 32rpx;
  }
}
</style>
