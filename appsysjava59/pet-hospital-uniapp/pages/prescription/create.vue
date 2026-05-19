<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="form-card card">
      <view class="form-title">开具电子处方</view>
      
      <view class="form-group">
        <text class="form-label">药品搜索</text>
        <view class="search-box">
          <input class="search-input" v-model="searchKeyword" placeholder="输入药品名称搜索..." @input="searchMedicine" />
        </view>
        <view class="search-results" v-if="searchResults.length > 0">
          <view v-for="med in searchResults" :key="med.id" class="result-item" @click="addMedicine(med)">
            <text class="result-name">{{ med.name }}</text>
            <text class="result-spec">{{ med.specification }}</text>
            <text class="result-price">¥{{ med.price }} / {{ med.unit }}</text>
            <text class="result-stock" :class="{ 'stock-low': med.stock < 10 }">库存: {{ med.stock }}</text>
          </view>
        </view>
      </view>
      
      <view class="form-group">
        <text class="form-label">已选药品 ({{ selectedMedicines.length }})</text>
        <view v-for="(item, index) in selectedMedicines" :key="item.id" class="medicine-selected">
          <view class="medicine-info">
            <text class="medicine-name">{{ item.medicineName }}</text>
            <text class="medicine-spec">{{ item.specification }}</text>
          </view>
          <view class="medicine-quantity">
            <text class="quantity-btn" @click="changeQuantity(index, -1)">-</text>
            <input class="quantity-input" type="number" v-model="item.quantity" />
            <text class="quantity-btn" @click="changeQuantity(index, 1)">+</text>
          </view>
          <view class="medicine-remove" @click="removeMedicine(index)">×</view>
        </view>
        <view v-if="selectedMedicines.length === 0" class="empty-medicine">
          <text>请搜索并添加药品</text>
        </view>
      </view>
      
      <view class="form-group" v-for="(item, index) in selectedMedicines" :key="'dosage-' + index">
        <text class="form-label">{{ item.medicineName }} - 用法用量</text>
        <picker class="dosage-picker" mode="selector" :range="dosageOptions" @change="(e) => item.dosage = dosageOptions[e.detail.value]">
          <view class="picker-value">{{ item.dosage || '选择用量' }}</view>
        </picker>
        <picker class="dosage-picker" mode="selector" :range="frequencyOptions" @change="(e) => item.frequency = frequencyOptions[e.detail.value]">
          <view class="picker-value">{{ item.frequency || '选择频次' }}</view>
        </picker>
        <picker class="dosage-picker" mode="selector" :range="durationOptions" @change="(e) => item.duration = durationOptions[e.detail.value]">
          <view class="picker-value">{{ item.duration || '选择疗程' }}</view>
        </picker>
      </view>
      
      <view class="form-group">
        <text class="form-label">备注</text>
        <textarea class="notes-input" v-model="prescriptionNotes" placeholder="请输入用药说明或注意事项..." />
      </view>
      
      <view class="total-section">
        <text class="total-label">总计：</text>
        <text class="total-amount">¥{{ totalAmount.toFixed(2) }}</text>
      </view>
      
      <view class="btn btn-primary" @click="submitPrescription">开具处方</view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import medicineApi from '../../api/medicine.js'
import prescriptionApi from '../../api/prescription.js'

const userStore = useUserStore()

const consultationId = ref('')
const petId = ref('')
const searchKeyword = ref('')
const searchResults = ref([])
const selectedMedicines = ref([])
const prescriptionNotes = ref('')
const elderMode = computed(() => userStore.elderMode)

const dosageOptions = ['1粒/次', '2粒/次', '1袋/次', '1ml/次', '5ml/次', '10ml/次']
const frequencyOptions = ['每日1次', '每日2次', '每日3次', '每12小时1次', '每8小时1次']
const durationOptions = ['3天', '5天', '7天', '10天', '14天', '30天']

const totalAmount = computed(() => {
  return selectedMedicines.value.reduce((sum, item) => {
    return sum + (item.price * (item.quantity || 1))
  }, 0)
})

const searchMedicine = async () => {
  if (!searchKeyword.value.trim()) {
    searchResults.value = []
    return
  }
  
  try {
    const res = await medicineApi.searchMedicine(searchKeyword.value)
    searchResults.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const addMedicine = (medicine) => {
  const exists = selectedMedicines.value.find(m => m.medicineId === medicine.id)
  if (exists) {
    uni.showToast({ title: '该药品已添加', icon: 'none' })
    return
  }
  
  selectedMedicines.value.push({
    medicineId: medicine.id,
    medicineName: medicine.name,
    specification: medicine.specification,
    price: medicine.price,
    quantity: 1,
    unit: medicine.unit,
    dosage: '',
    frequency: '',
    duration: '',
    notes: ''
  })
  
  searchResults.value = []
  searchKeyword.value = ''
}

const removeMedicine = (index) => {
  selectedMedicines.value.splice(index, 1)
}

const changeQuantity = (index, delta) => {
  const newValue = (selectedMedicines.value[index].quantity || 1) + delta
  if (newValue >= 1) {
    selectedMedicines.value[index].quantity = newValue
  }
}

const submitPrescription = async () => {
  if (selectedMedicines.value.length === 0) {
    uni.showToast({ title: '请至少添加一种药品', icon: 'none' })
    return
  }
  
  for (const item of selectedMedicines.value) {
    if (!item.dosage || !item.frequency || !item.duration) {
      uni.showToast({ title: '请完善所有药品的用法用量', icon: 'none' })
      return
    }
  }
  
  try {
    uni.showLoading({ title: '提交中...' })
    
    await prescriptionApi.createPrescription({
      consultationId: consultationId.value,
      doctorId: userStore.userInfo.id,
      petId: petId.value,
      ownerId: 1,
      notes: prescriptionNotes.value,
      items: selectedMedicines.value
    })
    
    uni.hideLoading()
    uni.showToast({ title: '处方开具成功', icon: 'success' })
    
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (e) {
    uni.hideLoading()
    console.error(e)
  }
}

onLoad((options) => {
  consultationId.value = options.consultationId
  petId.value = options.petId
})
</script>

<style scoped>
.form-card {
  margin: 20rpx;
}

.form-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
  text-align: center;
  margin-bottom: 32rpx;
}

.form-group {
  margin-bottom: 32rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #303133;
  margin-bottom: 16rpx;
  font-weight: 500;
}

.search-box {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 20rpx;
}

.search-input {
  height: 72rpx;
  font-size: 28rpx;
}

.search-results {
  margin-top: 16rpx;
  background-color: #fff;
  border: 1rpx solid #e4e7ed;
  border-radius: 12rpx;
  max-height: 400rpx;
  overflow-y: auto;
}

.result-item {
  padding: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12rpx;
}

.result-item:last-child {
  border-bottom: none;
}

.result-name {
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
  flex: 1;
  min-width: 200rpx;
}

.result-spec {
  font-size: 24rpx;
  color: #909399;
}

.result-price {
  font-size: 26rpx;
  color: #F56C6C;
  font-weight: bold;
}

.result-stock {
  font-size: 24rpx;
  color: #606266;
}

.stock-low {
  color: #F56C6C;
}

.medicine-selected {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
}

.medicine-info {
  flex: 1;
}

.medicine-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #303133;
}

.medicine-spec {
  display: block;
  font-size: 24rpx;
  color: #909399;
}

.medicine-quantity {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-right: 20rpx;
}

.quantity-btn {
  width: 48rpx;
  height: 48rpx;
  line-height: 48rpx;
  text-align: center;
  background-color: #e4e7ed;
  border-radius: 8rpx;
  font-size: 32rpx;
  color: #606266;
}

.quantity-input {
  width: 80rpx;
  text-align: center;
  font-size: 28rpx;
}

.medicine-remove {
  font-size: 40rpx;
  color: #F56C6C;
  width: 48rpx;
  text-align: center;
}

.empty-medicine {
  text-align: center;
  padding: 40rpx 0;
  color: #909399;
  font-size: 26rpx;
}

.dosage-picker {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 12rpx;
  font-size: 26rpx;
}

.picker-value {
  color: #606266;
}

.notes-input {
  width: 100%;
  height: 160rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.total-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 20rpx 0;
  margin-bottom: 24rpx;
  border-top: 1rpx solid #f0f0f0;
}

.total-label {
  font-size: 28rpx;
  color: #606266;
  margin-right: 12rpx;
}

.total-amount {
  font-size: 36rpx;
  font-weight: bold;
  color: #F56C6C;
}

.elder-mode .form-title {
  font-size: 36rpx;
}

.elder-mode .form-label {
  font-size: 32rpx;
}

.elder-mode .search-input {
  font-size: 32rpx;
  height: 88rpx;
}
</style>
