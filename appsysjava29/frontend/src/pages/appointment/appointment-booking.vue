<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="section" v-if="currentAppointment">
      <view class="section-header">
        <text class="section-title">预约信息</text>
      </view>
      <view class="info-card">
        <view class="info-row">
          <text class="label">医生</text>
          <text class="value">{{ currentAppointment.doctor?.doctorName }}</text>
        </view>
        <view class="info-row">
          <text class="label">科室</text>
          <text class="value">{{ currentAppointment.doctor?.deptName }}</text>
        </view>
        <view class="info-row">
          <text class="label">日期</text>
          <text class="value">{{ currentAppointment.date }}</text>
        </view>
        <view class="info-row">
          <text class="label">时段</text>
          <text class="value">{{ currentAppointment.slot?.startTime }}-{{ currentAppointment.slot?.endTime }}</text>
        </view>
        <view class="info-row">
          <text class="label">挂号费</text>
          <text class="value highlight">¥{{ currentAppointment.doctor?.consultationFee }}</text>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">选择就诊人</text>
        <text class="section-more" @click="goToAddPatient">添加就诊人</text>
      </view>
      
      <view class="patient-list" v-if="patients.length > 0">
        <view 
          class="patient-item" 
          v-for="patient in patients" 
          :key="patient.id"
          :class="{ selected: selectedPatientId === patient.id }"
          @click="selectPatient(patient)"
        >
          <view class="patient-info">
            <view class="patient-name-row">
              <text class="patient-name">{{ patient.patientName }}</text>
              <text class="patient-relation">{{ patient.relation || '本人' }}</text>
              <text class="default-tag" v-if="patient.isDefault === 1">默认</text>
            </view>
            <text class="patient-idcard" v-if="patient.idCard">
              {{ maskIdCard(patient.idCard) }}
            </text>
            <text class="patient-phone" v-if="patient.phone">
              {{ maskPhone(patient.phone) }}
            </text>
          </view>
          <view class="radio" :class="{ checked: selectedPatientId === patient.id }">
            <text v-if="selectedPatientId === patient.id">✓</text>
          </view>
        </view>
      </view>
      
      <view class="empty-patients" v-else>
        <text>暂无就诊人，请添加</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">症状描述（选填）</text>
      </view>
      <textarea 
        class="symptoms-input"
        v-model="symptoms"
        placeholder="请简要描述您的症状，如咳嗽、发热等..."
        :maxlength="200"
      />
      <view class="voice-btn" @click="startVoiceInput">
        <text>🎤 语音输入症状</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">支付方式</text>
      </view>
      
      <view class="payment-list">
        <view 
          class="payment-item" 
          v-for="payment in paymentMethods" 
          :key="payment.value"
          :class="{ selected: selectedPayment === payment.value }"
          @click="selectedPayment = payment.value"
        >
          <view class="payment-left">
            <text class="payment-icon">{{ payment.icon }}</text>
            <view class="payment-info">
              <text class="payment-name">{{ payment.name }}</text>
              <text class="payment-desc" v-if="payment.desc">{{ payment.desc }}</text>
            </view>
          </view>
          <view class="radio" :class="{ checked: selectedPayment === payment.value }">
            <text v-if="selectedPayment === payment.value">✓</text>
          </view>
        </view>
      </view>

      <view class="insurance-checkbox" @click="useInsurance = !useInsurance">
        <view class="checkbox" :class="{ checked: useInsurance }">
          <text v-if="useInsurance">✓</text>
        </view>
        <text class="checkbox-label">使用医保电子凭证结算</text>
        <text class="insurance-hint">预计报销60%</text>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="price-info">
        <text class="total-label">应付金额</text>
        <text class="total-price">¥{{ payableAmount }}</text>
        <text class="insurance-refund" v-if="useInsurance">医保报销¥{{ insuranceAmount }}</text>
      </view>
      <view class="btn-primary submit-btn" @click="submitAppointment">
        <text>确认预约</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { patientApi, appointmentApi } from '@/utils/api'

const store = useStore()

const patients = ref([])
const selectedPatientId = ref(null)
const symptoms = ref('')
const selectedPayment = ref('WECHAT')
const useInsurance = ref(false)

const elderMode = computed(() => store.getters.elderMode)
const currentAppointment = computed(() => store.getters.currentAppointment)

const paymentMethods = [
  { value: 'WECHAT', name: '微信支付', icon: '💚', desc: '推荐使用' },
  { value: 'ALIPAY', name: '支付宝', icon: '💙', desc: '' },
  { value: 'UNIONPAY', name: '银联云闪付', icon: '💳', desc: '' }
]

const totalFee = computed(() => {
  return currentAppointment.value?.doctor?.consultationFee || 0
})

const insuranceAmount = computed(() => {
  if (!useInsurance.value) return 0
  return Math.floor(totalFee.value * 0.6)
})

const payableAmount = computed(() => {
  return totalFee.value - insuranceAmount.value
})

const maskIdCard = (idCard) => {
  if (!idCard) return ''
  return idCard.substring(0, 4) + '**********' + idCard.substring(14)
}

const maskPhone = (phone) => {
  if (!phone) return ''
  return phone.substring(0, 3) + '****' + phone.substring(7)
}

const loadPatients = async () => {
  try {
    patients.value = await patientApi.getList()
    const defaultPatient = patients.value.find(p => p.isDefault === 1)
    if (defaultPatient) {
      selectedPatientId.value = defaultPatient.id
    } else if (patients.value.length > 0) {
      selectedPatientId.value = patients.value[0].id
    }
  } catch (e) {
    console.error('加载就诊人失败:', e)
  }
}

const selectPatient = (patient) => {
  selectedPatientId.value = patient.id
}

const goToAddPatient = () => {
  uni.navigateTo({ url: '/pages/patient/patient-edit' })
}

const startVoiceInput = () => {
  uni.showToast({ title: '语音输入功能开发中', icon: 'none' })
}

const submitAppointment = async () => {
  if (!selectedPatientId.value) {
    uni.showToast({ title: '请选择就诊人', icon: 'none' })
    return
  }

  try {
    const appointment = await appointmentApi.create({
      slotId: currentAppointment.value.slot.id,
      patientId: selectedPatientId.value,
      symptoms: symptoms.value
    })

    uni.navigateTo({ 
      url: `/pages/appointment/appointment-payment?id=${appointment.id}&payment=${selectedPayment.value}&insurance=${useInsurance.value}` 
    })
  } catch (e) {
    uni.showToast({ title: e.message || '预约失败', icon: 'none' })
  }
}

onMounted(() => {
  loadPatients()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 160rpx;
}

.section {
  background: #fff;
  margin-bottom: 20rpx;
  padding: 24rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 26rpx;
  color: #1890ff;
}

.info-card {
  background: #fafafa;
  border-radius: 12rpx;
  padding: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 26rpx;
  color: #999;
}

.value {
  font-size: 26rpx;
  color: #333;
}

.value.highlight {
  color: #ff4d4f;
  font-weight: bold;
}

.patient-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.patient-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background: #fafafa;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
}

.patient-item.selected {
  border-color: #1890ff;
  background: #e6f7ff;
}

.patient-name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 8rpx;
}

.patient-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.patient-relation {
  font-size: 22rpx;
  color: #1890ff;
  background: #e6f7ff;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.default-tag {
  font-size: 22rpx;
  color: #52c41a;
  background: #f6ffed;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.patient-idcard,
.patient-phone {
  font-size: 24rpx;
  color: #999;
  display: block;
}

.radio {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  color: #fff;
}

.radio.checked {
  background: #1890ff;
  border-color: #1890ff;
}

.empty-patients {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 28rpx;
}

.symptoms-input {
  width: 100%;
  height: 200rpx;
  padding: 20rpx;
  background: #fafafa;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.voice-btn {
  display: flex;
  justify-content: center;
  padding: 20rpx;
  margin-top: 16rpx;
  background: #f6ffed;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #52c41a;
}

.payment-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.payment-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background: #fafafa;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
}

.payment-item.selected {
  border-color: #1890ff;
  background: #e6f7ff;
}

.payment-left {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.payment-icon {
  font-size: 44rpx;
}

.payment-info {
  display: flex;
  flex-direction: column;
}

.payment-name {
  font-size: 28rpx;
  color: #333;
}

.payment-desc {
  font-size: 22rpx;
  color: #1890ff;
}

.insurance-checkbox {
  display: flex;
  align-items: center;
  padding: 24rpx;
  margin-top: 16rpx;
  background: linear-gradient(135deg, #f6ffed 0%, #d9f7be 100%);
  border-radius: 12rpx;
}

.checkbox {
  width: 40rpx;
  height: 40rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 6rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  color: #fff;
  margin-right: 16rpx;
}

.checkbox.checked {
  background: #52c41a;
  border-color: #52c41a;
}

.checkbox-label {
  font-size: 28rpx;
  color: #333;
  flex: 1;
}

.insurance-hint {
  font-size: 24rpx;
  color: #52c41a;
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
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.price-info {
  display: flex;
  flex-direction: column;
}

.total-label {
  font-size: 24rpx;
  color: #999;
}

.total-price {
  font-size: 40rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.insurance-refund {
  font-size: 22rpx;
  color: #52c41a;
}

.submit-btn {
  width: 280rpx;
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.elder-mode-enabled {
  .section-title {
    font-size: 34rpx;
  }
  
  .patient-name {
    font-size: 34rpx;
  }
  
  .total-price {
    font-size: 44rpx;
  }
}
</style>
