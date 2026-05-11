<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="patient-list" v-if="patients.length > 0">
      <view 
        class="patient-card" 
        v-for="patient in patients" 
        :key="patient.id"
      >
        <view class="card-header">
          <view class="patient-info">
            <view class="name-row">
              <text class="name">{{ patient.patientName }}</text>
              <text class="relation" v-if="patient.relation">{{ patient.relation }}</text>
              <text class="default-tag" v-if="patient.isDefault === 1">默认</text>
            </view>
            <text class="idcard" v-if="patient.idCard">{{ maskIdCard(patient.idCard) }}</text>
            <text class="phone" v-if="patient.phone">{{ maskPhone(patient.phone) }}</text>
          </view>
        </view>
        
        <view class="card-footer">
          <view class="footer-item" @click="editPatient(patient)">
            <text class="icon">✏️</text>
            <text>编辑</text>
          </view>
          <view class="footer-item" @click="setDefault(patient)" v-if="patient.isDefault !== 1">
            <text class="icon">⭐</text>
            <text>设为默认</text>
          </view>
          <view class="footer-item delete" @click="deletePatient(patient)">
            <text class="icon">🗑️</text>
            <text>删除</text>
          </view>
        </view>
      </view>
    </view>

    <view class="empty" v-else>
      <text class="empty-icon">👨‍👩‍👧</text>
      <text class="empty-text">暂无就诊人</text>
      <text class="empty-desc">添加就诊人后可快速预约挂号</text>
    </view>

    <view class="bottom-bar">
      <view class="add-btn" @click="addPatient">
        <text>➕ 添加就诊人</text>
      </view>
    </view>

    <view class="delete-popup" v-if="showDeletePopup">
      <view class="popup-content">
        <text class="popup-title">确认删除</text>
        <text class="popup-desc">确定要删除该就诊人吗？</text>
        <view class="popup-actions">
          <view class="popup-btn" @click="showDeletePopup = false">
            <text>取消</text>
          </view>
          <view class="popup-btn delete" @click="confirmDelete">
            <text>删除</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onShow } from 'vue'
import { useStore } from 'vuex'
import { patientApi } from '@/utils/api'

const store = useStore()

const patients = ref([])
const showDeletePopup = ref(false)
const selectedPatient = ref(null)

const elderMode = computed(() => store.getters.elderMode)

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
  } catch (e) {
    console.error('加载就诊人失败:', e)
  }
}

const addPatient = () => {
  uni.navigateTo({ url: '/pages/patient/patient-edit' })
}

const editPatient = (patient) => {
  uni.navigateTo({ url: `/pages/patient/patient-edit?id=${patient.id}` })
}

const setDefault = async (patient) => {
  try {
    await patientApi.setDefault(patient.id)
    uni.showToast({ title: '已设为默认', icon: 'success' })
    loadPatients()
  } catch (e) {
    uni.showToast({ title: '设置失败', icon: 'none' })
  }
}

const deletePatient = (patient) => {
  selectedPatient.value = patient
  showDeletePopup.value = true
}

const confirmDelete = async () => {
  if (!selectedPatient.value) return

  try {
    await patientApi.delete(selectedPatient.value.id)
    uni.showToast({ title: '删除成功', icon: 'success' })
    showDeletePopup.value = false
    loadPatients()
  } catch (e) {
    uni.showToast({ title: '删除失败', icon: 'none' })
  }
}

onShow(() => {
  loadPatients()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.patient-list {
  padding: 20rpx;
}

.patient-card {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.card-header {
  padding: 28rpx;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 12rpx;
}

.name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.relation {
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

.idcard,
.phone {
  font-size: 26rpx;
  color: #999;
  display: block;
  margin-top: 8rpx;
}

.card-footer {
  display: flex;
  border-top: 2rpx solid #f0f0f0;
}

.footer-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  padding: 20rpx;
  font-size: 26rpx;
  color: #666;
  border-right: 2rpx solid #f0f0f0;
}

.footer-item:last-child {
  border-right: none;
}

.footer-item.delete {
  color: #ff4d4f;
}

.icon {
  font-size: 28rpx;
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
  font-size: 32rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #999;
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

.add-btn {
  height: 88rpx;
  line-height: 88rpx;
  background: #1890ff;
  color: #fff;
  text-align: center;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.delete-popup {
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
  width: 560rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 50rpx 40rpx;
  text-align: center;
}

.popup-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.popup-desc {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.popup-actions {
  display: flex;
  gap: 20rpx;
}

.popup-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  background: #f5f5f5;
  border-radius: 40rpx;
  font-size: 28rpx;
  color: #666;
}

.popup-btn.delete {
  background: #ff4d4f;
  color: #fff;
}

.elder-mode-enabled {
  .name {
    font-size: 36rpx;
  }
  
  .add-btn {
    font-size: 36rpx;
  }
}
</style>
