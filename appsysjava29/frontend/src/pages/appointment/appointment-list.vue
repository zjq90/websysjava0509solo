<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="tabs">
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'all' }"
        @click="switchTab('all')"
      >
        全部
      </view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'pending' }"
        @click="switchTab('pending')"
      >
        待支付
      </view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'confirmed' }"
        @click="switchTab('confirmed')"
      >
        已预约
      </view>
      <view 
        class="tab-item" 
        :class="{ active: currentTab === 'cancelled' }"
        @click="switchTab('cancelled')"
      >
        已取消
      </view>
    </view>

    <view class="appointment-list" v-if="appointments.length > 0">
      <view 
        class="appointment-card" 
        v-for="appointment in appointments" 
        :key="appointment.id"
        @click="goToDetail(appointment.id)"
      >
        <view class="card-header">
          <view class="doctor-info">
            <view class="avatar">
              <text>👨‍⚕️</text>
            </view>
            <view class="info">
              <view class="name-row">
                <text class="name">{{ appointment.doctorName }}</text>
                <text class="title" v-if="appointment.doctorTitle">{{ appointment.doctorTitle }}</text>
              </view>
              <text class="dept">{{ appointment.deptName }}</text>
            </view>
          </view>
          <view class="status" :class="getStatusClass(appointment.status)">
            <text>{{ getStatusText(appointment.status) }}</text>
          </view>
        </view>

        <view class="card-body">
          <view class="info-row">
            <text class="label">就诊人</text>
            <text class="value">{{ appointment.patientName }}</text>
          </view>
          <view class="info-row">
            <text class="label">就诊时间</text>
            <text class="value">{{ appointment.appointmentDate }} {{ appointment.startTime }}-{{ appointment.endTime }}</text>
          </view>
          <view class="info-row" v-if="appointment.symptoms">
            <text class="label">症状描述</text>
            <text class="value">{{ appointment.symptoms }}</text>
          </view>
        </view>

        <view class="card-footer">
          <view class="price">
            <text class="fee">¥{{ appointment.totalAmount }}</text>
            <text class="refund" v-if="appointment.insuranceAmount > 0">医保报销¥{{ appointment.insuranceAmount }}</text>
          </view>
          <view class="actions">
            <view 
              class="action-btn cancel" 
              v-if="appointment.status === 'PENDING' || appointment.status === 'CONFIRMED'"
              @click.stop="cancelAppointment(appointment)"
            >
              取消预约
            </view>
            <view 
              class="action-btn pay" 
              v-if="appointment.status === 'PENDING'"
              @click.stop="goToPayment(appointment)"
            >
              去支付
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="empty" v-else>
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无预约记录</text>
      <view class="empty-btn" @click="goToBook">
        <text>去预约</text>
      </view>
    </view>

    <view class="cancel-popup" v-if="showCancelPopup">
      <view class="popup-content">
        <text class="popup-title">取消预约</text>
        <textarea 
          class="reason-input" 
          v-model="cancelReason" 
          placeholder="请输入取消原因（选填）"
        />
        <view class="popup-actions">
          <view class="popup-btn" @click="showCancelPopup = false">
            <text>取消</text>
          </view>
          <view class="popup-btn confirm" @click="confirmCancel">
            <text>确认取消</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onShow } from 'vue'
import { useStore } from 'vuex'
import { appointmentApi } from '@/utils/api'

const store = useStore()

const currentTab = ref('all')
const appointments = ref([])
const showCancelPopup = ref(false)
const selectedAppointment = ref(null)
const cancelReason = ref('')

const elderMode = computed(() => store.getters.elderMode)

const getStatusText = (status) => {
  const map = {
    'PENDING': '待支付',
    'PAID': '已支付',
    'CONFIRMED': '已预约',
    'CANCELLED': '已取消',
    'COMPLETED': '已完成'
  }
  return map[status] || status
}

const getStatusClass = (status) => {
  const map = {
    'PENDING': 'pending',
    'PAID': 'paid',
    'CONFIRMED': 'confirmed',
    'CANCELLED': 'cancelled',
    'COMPLETED': 'completed'
  }
  return map[status] || ''
}

const switchTab = (tab) => {
  currentTab.value = tab
  loadAppointments()
}

const loadAppointments = async () => {
  try {
    const status = currentTab.value === 'all' ? '' : currentTab.value
    appointments.value = await appointmentApi.getList(status)
  } catch (e) {
    console.error('加载预约失败:', e)
    appointments.value = []
  }
}

const cancelAppointment = (appointment) => {
  selectedAppointment.value = appointment
  cancelReason.value = ''
  showCancelPopup.value = true
}

const confirmCancel = async () => {
  if (!selectedAppointment.value) return

  try {
    await appointmentApi.cancel(selectedAppointment.value.id, cancelReason.value)
    uni.showToast({ title: '预约已取消', icon: 'success' })
    showCancelPopup.value = false
    loadAppointments()
  } catch (e) {
    uni.showToast({ title: e.message || '取消失败', icon: 'none' })
  }
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/appointment/appointment-detail?id=${id}` })
}

const goToPayment = (appointment) => {
  uni.navigateTo({ 
    url: `/pages/appointment/appointment-payment?id=${appointment.id}` 
  })
}

const goToBook = () => {
  uni.switchTab({ url: '/pages/index/index' })
}

onShow(() => {
  loadAppointments()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.tabs {
  display: flex;
  background: #fff;
  padding: 0 16rpx;
  position: sticky;
  top: 0;
  z-index: 10;
}

.tab-item {
  flex: 1;
  padding: 28rpx 0;
  text-align: center;
  font-size: 26rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab-item.active {
  color: #1890ff;
  border-bottom-color: #1890ff;
}

.appointment-list {
  padding: 20rpx;
}

.appointment-card {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.doctor-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  background: #e6f7ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 16rpx;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 8rpx;
}

.name {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.title {
  font-size: 22rpx;
  color: #1890ff;
}

.dept {
  font-size: 24rpx;
  color: #999;
}

.status {
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.status.pending {
  color: #fa8c16;
  background: #fff7e6;
}

.status.paid,
.status.confirmed {
  color: #52c41a;
  background: #f6ffed;
}

.status.cancelled {
  color: #999;
  background: #f5f5f5;
}

.status.completed {
  color: #1890ff;
  background: #e6f7ff;
}

.card-body {
  padding: 20rpx 24rpx;
}

.info-row {
  display: flex;
  padding: 8rpx 0;
}

.label {
  font-size: 26rpx;
  color: #999;
  width: 140rpx;
  flex-shrink: 0;
}

.value {
  font-size: 26rpx;
  color: #333;
  flex: 1;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 24rpx;
  background: #fafafa;
}

.price {
  display: flex;
  flex-direction: column;
}

.fee {
  font-size: 30rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.refund {
  font-size: 22rpx;
  color: #52c41a;
}

.actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  padding: 12rpx 28rpx;
  border-radius: 24rpx;
  font-size: 24rpx;
}

.action-btn.cancel {
  background: #fff;
  color: #666;
  border: 2rpx solid #d9d9d9;
}

.action-btn.pay {
  background: #1890ff;
  color: #fff;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.empty-btn {
  padding: 20rpx 60rpx;
  background: #1890ff;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.cancel-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.popup-content {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
}

.popup-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 30rpx;
}

.reason-input {
  width: 100%;
  height: 200rpx;
  padding: 20rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  font-size: 26rpx;
  box-sizing: border-box;
  margin-bottom: 30rpx;
}

.popup-actions {
  display: flex;
  gap: 20rpx;
}

.popup-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  border-radius: 40rpx;
  font-size: 28rpx;
  background: #f5f5f5;
  color: #666;
}

.popup-btn.confirm {
  background: #1890ff;
  color: #fff;
}

.elder-mode-enabled {
  .tab-item {
    font-size: 30rpx;
  }
  
  .name {
    font-size: 32rpx;
  }
  
  .fee {
    font-size: 34rpx;
  }
}
</style>
