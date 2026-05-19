<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="prescription-list">
      <view v-for="prescription in prescriptionList" :key="prescription.id" class="prescription-item card">
        <view class="prescription-header flex-between">
          <text class="prescription-no">处方号：{{ prescription.prescriptionNo }}</text>
          <text class="prescription-status" :class="'status-' + prescription.status.toLowerCase()">{{ getStatusText(prescription.status) }}</text>
        </view>
        
        <view class="prescription-info">
          <view class="info-row">
            <text class="label">宠物：</text>
            <text class="value">{{ prescription.petId }}</text>
          </view>
          <view class="info-row">
            <text class="label">开具时间：</text>
            <text class="value">{{ formatTime(prescription.createTime) }}</text>
          </view>
        </view>
        
        <view class="prescription-items" v-if="prescription.items">
          <view v-for="item in prescription.items" :key="item.id" class="medicine-item">
            <text class="medicine-name">{{ item.medicineName }}</text>
            <text class="medicine-spec">{{ item.specification }}</text>
            <text class="medicine-dosage">{{ item.dosage }} {{ item.frequency }} {{ item.duration }}</text>
          </view>
        </view>
        
        <view class="prescription-notes" v-if="prescription.notes">
          <text class="notes-label">备注：</text>
          <text class="notes-text">{{ prescription.notes }}</text>
        </view>
      </view>
      
      <view v-if="prescriptionList.length === 0" class="empty">
        <text>暂无处方记录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import prescriptionApi from '../../api/prescription.js'

const userStore = useUserStore()

const prescriptionList = ref([])
const elderMode = computed(() => userStore.elderMode)

const getStatusText = (status) => {
  const map = {
    'DRAFT': '草稿',
    'ISSUED': '已开具',
    'DISPENSED': '已配药'
  }
  return map[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
}

const loadPrescriptions = async () => {
  if (!userStore.userInfo?.id) return
  
  try {
    const res = await prescriptionApi.getDoctorPrescriptions(userStore.userInfo.id)
    prescriptionList.value = res || []
    
    for (const p of prescriptionList.value) {
      loadPrescriptionItems(p)
    }
  } catch (e) {
    console.error(e)
  }
}

const loadPrescriptionItems = async (prescription) => {
  try {
    const res = await prescriptionApi.getPrescriptionItems(prescription.id)
    prescription.items = res || []
  } catch (e) {
    console.error(e)
  }
}

onShow(() => {
  loadPrescriptions()
})
</script>

<style scoped>
.prescription-item {
  margin-bottom: 20rpx;
}

.prescription-header {
  margin-bottom: 16rpx;
  padding-bottom: 16rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.prescription-no {
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
}

.prescription-status {
  font-size: 24rpx;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
}

.status-draft {
  background-color: #F4F4F5;
  color: #909399;
}

.status-issued {
  background-color: #ECF5FF;
  color: #409EFF;
}

.status-dispensed {
  background-color: #F0F9EB;
  color: #67C23A;
}

.prescription-info {
  margin-bottom: 16rpx;
}

.info-row {
  display: flex;
  margin-bottom: 8rpx;
  font-size: 26rpx;
}

.label {
  color: #909399;
  width: 140rpx;
}

.value {
  color: #606266;
  flex: 1;
}

.prescription-items {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 16rpx;
  margin-bottom: 16rpx;
}

.medicine-item {
  padding: 12rpx 0;
  border-bottom: 1rpx solid #e4e7ed;
}

.medicine-item:last-child {
  border-bottom: none;
}

.medicine-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4rpx;
}

.medicine-spec {
  display: block;
  font-size: 24rpx;
  color: #909399;
  margin-bottom: 4rpx;
}

.medicine-dosage {
  display: block;
  font-size: 24rpx;
  color: #606266;
}

.prescription-notes {
  font-size: 24rpx;
  color: #606266;
}

.notes-label {
  color: #909399;
}

.elder-mode .prescription-no {
  font-size: 32rpx;
}

.elder-mode .medicine-name {
  font-size: 32rpx;
}
</style>
