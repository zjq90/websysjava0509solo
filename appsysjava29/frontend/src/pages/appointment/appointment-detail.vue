<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="detail-section" v-if="appointment">
      <view class="status-bar" :class="getStatusClass(appointment.status)">
        <text class="status-icon">{{ getStatusIcon(appointment.status) }}</text>
        <text class="status-text">{{ getStatusText(appointment.status) }}</text>
      </view>

      <view class="info-card">
        <view class="card-title">就诊信息</view>
        
        <view class="info-row">
          <text class="label">医生</text>
          <text class="value">{{ appointment.doctorName }}</text>
        </view>
        <view class="info-row">
          <text class="label">科室</text>
          <text class="value">{{ appointment.deptName }}</text>
        </view>
        <view class="info-row">
          <text class="label">职称</text>
          <text class="value">{{ appointment.doctorTitle || '医师' }}</text>
        </view>
        <view class="info-row">
          <text class="label">就诊日期</text>
          <text class="value">{{ appointment.appointmentDate }}</text>
        </view>
        <view class="info-row">
          <text class="label">就诊时段</text>
          <text class="value">{{ appointment.startTime }}-{{ appointment.endTime }}</text>
        </view>
      </view>

      <view class="info-card">
        <view class="card-title">就诊人信息</view>
        
        <view class="info-row">
          <text class="label">姓名</text>
          <text class="value">{{ appointment.patientName }}</text>
        </view>
        <view class="info-row">
          <text class="label">关系</text>
          <text class="value">{{ appointment.patientRelation || '本人' }}</text>
        </view>
      </view>

      <view class="info-card" v-if="appointment.symptoms">
        <view class="card-title">症状描述</view>
        <text class="symptoms-text">{{ appointment.symptoms }}</text>
      </view>

      <view class="info-card">
        <view class="card-title">费用信息</view>
        
        <view class="info-row">
          <text class="label">挂号费</text>
          <text class="value">¥{{ appointment.totalAmount }}</text>
        </view>
        <view class="info-row" v-if="appointment.insuranceAmount > 0">
          <text class="label">医保报销</text>
          <text class="value refund">-¥{{ appointment.insuranceAmount }}</text>
        </view>
        <view class="info-row total">
          <text class="label">实付金额</text>
          <text class="value highlight">¥{{ appointment.payableAmount }}</text>
        </view>
        <view class="info-row" v-if="appointment.paymentMethod">
          <text class="label">支付方式</text>
          <text class="value">{{ getPaymentMethod(appointment.paymentMethod) }}</text>
        </view>
      </view>

      <view class="info-card" v-if="appointment.cancelReason">
        <view class="card-title">取消原因</view>
        <text class="reason-text">{{ appointment.cancelReason }}</text>
      </view>

      <view class="info-card">
        <view class="card-title">预约信息</view>
        
        <view class="info-row">
          <text class="label">预约号</text>
          <text class="value">{{ appointment.appointmentNo }}</text>
        </view>
        <view class="info-row">
          <text class="label">创建时间</text>
          <text class="value">{{ formatTime(appointment.createTime) }}</text>
        </view>
      </view>
    </view>

    <view class="bottom-bar" v-if="appointment">
      <view class="btn cancel-btn" v-if="appointment.status === 'PENDING' || appointment.status === 'CONFIRMED'" @click="cancelAppointment">
        <text>取消预约</text>
      </view>
      <view class="btn pay-btn" v-if="appointment.status === 'PENDING'" @click="goToPayment">
        <text>去支付</text>
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
import { ref, computed, onMounted, onLoad } from 'vue'
import { useStore } from 'vuex'
import { appointmentApi } from '@/utils/api'

const store = useStore()

const appointmentId = ref(null)
const appointment = ref(null)
const showCancelPopup = ref(false)
const cancelReason = ref('')

const elderMode = computed(() => store.getters.elderMode)

onLoad((options) => {
  appointmentId.value = options.id
})

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

const getStatusIcon = (status) => {
  const map = {
    'PENDING': '⏳',
    'PAID': '✅',
    'CONFIRMED': '✅',
    'CANCELLED': '❌',
    'COMPLETED': '🎉'
  }
  return map[status] || '📋'
}

const getStatusClass = (status) => {
  return status.toLowerCase()
}

const getPaymentMethod = (method) => {
  const map = {
    'WECHAT': '微信支付',
    'ALIPAY': '支付宝',
    'UNIONPAY': '银联云闪付'
  }
  return map[method] || method
}

const formatTime = (time) => {
  if (!time) return '-'
  return time.replace('T', ' ').substring(0, 19)
}

const loadAppointment = async () => {
  try {
    appointment.value = await appointmentApi.getDetail(appointmentId.value)
  } catch (e) {
    console.error('加载预约详情失败:', e)
  }
}

const cancelAppointment = () => {
  cancelReason.value = ''
  showCancelPopup.value = true
}

const confirmCancel = async () => {
  try {
    await appointmentApi.cancel(appointmentId.value, cancelReason.value)
    uni.showToast({ title: '预约已取消', icon: 'success' })
    showCancelPopup.value = false
    loadAppointment()
  } catch (e) {
    uni.showToast({ title: e.message || '取消失败', icon: 'none' })
  }
}

const goToPayment = () => {
  uni.navigateTo({ 
    url: `/pages/appointment/appointment-payment?id=${appointmentId.value}` 
  })
}

onMounted(() => {
  loadAppointment()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 160rpx;
}

.status-bar {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 20rpx;
}

.status-bar.pending {
  background: linear-gradient(135deg, #fff7e6 0%, #ffd666 100%);
}

.status-bar.paid,
.status-bar.confirmed {
  background: linear-gradient(135deg, #f6ffed 0%, #95de64 100%);
}

.status-bar.cancelled {
  background: linear-gradient(135deg, #f5f5f5 0%, #d9d9d9 100%);
}

.status-bar.completed {
  background: linear-gradient(135deg, #e6f7ff 0%, #69c0ff 100%);
}

.status-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.status-text {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.detail-section {
  padding: 20rpx;
}

.info-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
}

.card-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  padding-bottom: 20rpx;
  margin-bottom: 16rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;
}

.info-row.total {
  padding-top: 20rpx;
  margin-top: 12rpx;
  border-top: 2rpx dashed #f0f0f0;
}

.label {
  font-size: 26rpx;
  color: #999;
}

.value {
  font-size: 26rpx;
  color: #333;
}

.value.refund {
  color: #52c41a;
}

.value.highlight {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.symptoms-text,
.reason-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  display: flex;
  gap: 20rpx;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  border-radius: 44rpx;
  font-size: 30rpx;
  font-weight: bold;
}

.cancel-btn {
  background: #f5f5f5;
  color: #666;
}

.pay-btn {
  background: #1890ff;
  color: #fff;
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
  .status-text {
    font-size: 40rpx;
  }
  
  .card-title {
    font-size: 32rpx;
  }
  
  .value.highlight {
    font-size: 36rpx;
  }
}
</style>
