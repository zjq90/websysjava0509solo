<template>
  <view class="fault-container">
    <view class="form-card">
      <view class="form-header">
        <text class="title">故障上报</text>
        <text class="subtitle">上报成功可获得骑行券奖励</text>
      </view>

      <view class="form-group">
        <text class="label">车辆编号</text>
        <input 
          class="input" 
          v-model="form.bikeNo" 
          placeholder="请输入车辆编号或扫码获取"
        />
        <button class="scan-btn" @click="scanBike">扫码</button>
      </view>

      <view class="form-group">
        <text class="label">故障类型</text>
        <view class="type-grid">
          <view 
            class="type-item" 
            v-for="type in faultTypes" 
            :key="type.id"
            :class="{ active: form.faultType === type.id }"
            @click="form.faultType = type.id"
          >
            <text class="type-icon">{{ type.icon }}</text>
            <text class="type-name">{{ type.name }}</text>
          </view>
        </view>
      </view>

      <view class="form-group">
        <text class="label">问题描述</text>
        <textarea 
          class="textarea" 
          v-model="form.description" 
          placeholder="请详细描述故障情况，方便我们快速处理"
          maxlength="200"
        />
        <text class="char-count">{{ form.description.length }}/200</text>
      </view>

      <view class="form-group">
        <text class="label">上传照片</text>
        <view class="photo-upload">
          <view 
            class="photo-item" 
            v-for="(photo, index) in photos" 
            :key="index"
          >
            <image :src="photo" class="photo-preview" mode="aspectFill" />
            <text class="delete-btn" @click="deletePhoto(index)">✕</text>
          </view>
          <view class="add-photo" v-if="photos.length < 3" @click="addPhoto">
            <text class="add-icon">📷</text>
            <text class="add-text">添加照片</text>
          </view>
        </view>
        <text class="photo-tip">最多上传3张照片</text>
      </view>

      <view class="form-group">
        <text class="label">所在位置</text>
        <view class="location-info" @click="getLocation">
          <text class="location-icon">📍</text>
          <text class="location-text">{{ location || '点击获取位置' }}</text>
        </view>
      </view>

      <view class="reward-tip">
        <text class="tip-icon">🎁</text>
        <text class="tip-text">上报成功后将获得2元骑行券奖励</text>
      </view>

      <button class="submit-btn" :disabled="loading" @click="submitFault">
        {{ loading ? '提交中...' : '提交上报' }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const loading = ref(false)
const photos = ref([])
const location = ref('')

const form = ref({
  bikeNo: '',
  faultType: '',
  description: ''
})

const faultTypes = ref([
  { id: 'brake', name: '刹车失灵', icon: '🛑' },
  { id: 'chain', name: '链条脱落', icon: '🔗' },
  { id: 'tire', name: '车胎漏气', icon: '⚫' },
  { id: 'seat', name: '车座损坏', icon: '🪑' },
  { id: 'lock', name: '车锁故障', icon: '🔒' },
  { id: 'battery', name: '电量不足', icon: '🔋' },
  { id: 'other', name: '其他问题', icon: '❓' }
])

const scanBike = () => {
  uni.showToast({ title: '扫码获取编号', icon: 'none' })
  form.value.bikeNo = 'E012'
}

const addPhoto = () => {
  uni.chooseImage({
    count: 3 - photos.value.length,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: (res) => {
      photos.value.push(...res.tempFilePaths)
    }
  })
}

const deletePhoto = (index) => {
  photos.value.splice(index, 1)
}

const getLocation = () => {
  uni.showLoading({ title: '获取位置中...' })
  setTimeout(() => {
    uni.hideLoading()
    location.value = '北京市朝阳区建国路88号SOHO现代城'
  }, 1000)
}

const submitFault = () => {
  if (!form.value.bikeNo) {
    uni.showToast({ title: '请输入车辆编号', icon: 'none' })
    return
  }
  if (!form.value.faultType) {
    uni.showToast({ title: '请选择故障类型', icon: 'none' })
    return
  }
  if (!form.value.description) {
    uni.showToast({ title: '请描述故障情况', icon: 'none' })
    return
  }

  loading.value = true
  setTimeout(() => {
    loading.value = false
    uni.showModal({
      title: '上报成功',
      content: '感谢您的反馈！2元骑行券已发放到您的账户。',
      showCancel: false,
      success: () => {
        uni.navigateBack()
      }
    })
  }, 2000)
}
</script>

<style lang="scss" scoped>
.fault-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 16px;
}

.form-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
}

.form-header {
  text-align: center;
  margin-bottom: 32px;
}

.title {
  display: block;
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: #00A862;
}

.form-group {
  margin-bottom: 24px;
  position: relative;
}

.label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.input {
  width: 100%;
  height: 48px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 70px 0 16px;
  font-size: 16px;
  box-sizing: border-box;

  &:focus {
    border-color: #00A862;
  }
}

.scan-btn {
  position: absolute;
  right: 8px;
  top: 36px;
  height: 32px;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0 12px;
  font-size: 12px;
}

.type-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.type-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background: #fafafa;

  &.active {
    border-color: #00A862;
    background: #f0f9f4;
  }
}

.type-icon {
  font-size: 24px;
}

.type-name {
  font-size: 12px;
  color: #333;
}

.textarea {
  width: 100%;
  height: 100px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 12px 16px;
  font-size: 16px;
  box-sizing: border-box;
  resize: none;

  &:focus {
    border-color: #00A862;
  }
}

.char-count {
  display: block;
  text-align: right;
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.photo-upload {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.photo-item {
  position: relative;
  width: 80px;
  height: 80px;
}

.photo-preview {
  width: 100%;
  height: 100%;
  border-radius: 8px;
}

.delete-btn {
  position: absolute;
  top: -8px;
  right: -8px;
  width: 20px;
  height: 20px;
  background: rgba(0,0,0,0.6);
  color: white;
  border-radius: 50%;
  font-size: 12px;
  text-align: center;
  line-height: 20px;
}

.add-photo {
  width: 80px;
  height: 80px;
  border: 1px dashed #ccc;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.add-icon {
  font-size: 24px;
}

.add-text {
  font-size: 10px;
  color: #999;
}

.photo-tip {
  display: block;
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}

.location-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #f5f5f5;
  border-radius: 12px;
}

.location-icon {
  font-size: 18px;
}

.location-text {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.reward-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px;
  background: #e8f5e9;
  border-radius: 8px;
  margin-bottom: 24px;
}

.tip-icon {
  font-size: 18px;
}

.tip-text {
  flex: 1;
  font-size: 14px;
  color: #2e7d32;
}

.submit-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: bold;

  &:disabled {
    opacity: 0.6;
  }
}
</style>
