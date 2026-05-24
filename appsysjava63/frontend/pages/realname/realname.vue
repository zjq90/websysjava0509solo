<template>
  <view class="realname-container">
    <view class="status-card" v-if="isVerified">
      <view class="status-icon verified">✓</view>
      <text class="status-title">已完成实名认证</text>
      <text class="status-desc">您的身份信息已通过审核</text>
    </view>

    <view class="form-card" v-else>
      <view class="form-header">
        <text class="title">实名认证</text>
        <text class="subtitle">为保障您的账户安全，请完成实名认证</text>
      </view>

      <view class="form-group">
        <text class="label">真实姓名</text>
        <input 
          class="input" 
          v-model="form.realName" 
          placeholder="请输入真实姓名"
        />
      </view>

      <view class="form-group">
        <text class="label">身份证号</text>
        <input 
          class="input" 
          v-model="form.idCard" 
          placeholder="请输入18位身份证号"
          maxlength="18"
        />
      </view>

      <view class="form-group">
        <text class="label">人脸识别</text>
        <view class="face-upload" @click="uploadFace">
          <image v-if="faceImage" :src="faceImage" class="face-preview" mode="aspectFill" />
          <view v-else class="face-placeholder">
            <text class="upload-icon">📷</text>
            <text class="upload-text">点击上传人脸照片</text>
          </view>
        </view>
      </view>

      <view class="notice">
        <text class="notice-icon">ℹ️</text>
        <text class="notice-text">您的身份信息仅用于身份验证，我们将严格保护您的隐私</text>
      </view>

      <button class="submit-btn" :disabled="loading" @click="submitVerify">
        {{ loading ? '提交中...' : '提交认证' }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'

const isVerified = ref(false)
const loading = ref(false)
const faceImage = ref('')
const form = ref({
  realName: '',
  idCard: ''
})

const uploadFace = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['camera', 'album'],
    success: (res) => {
      faceImage.value = res.tempFilePaths[0]
    }
  })
}

const submitVerify = () => {
  if (!form.value.realName) {
    uni.showToast({ title: '请输入真实姓名', icon: 'none' })
    return
  }
  if (!form.value.idCard || form.value.idCard.length !== 18) {
    uni.showToast({ title: '请输入正确的身份证号', icon: 'none' })
    return
  }
  if (!faceImage.value) {
    uni.showToast({ title: '请上传人脸照片', icon: 'none' })
    return
  }

  loading.value = true
  setTimeout(() => {
    loading.value = false
    isVerified.value = true
    uni.showToast({ title: '认证成功', icon: 'success' })
  }, 2000)
}
</script>

<style lang="scss" scoped>
.realname-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 16px;
}

.status-card {
  background: white;
  border-radius: 16px;
  padding: 48px 24px;
  text-align: center;
}

.status-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  margin: 0 auto 20px;

  &.verified {
    background: #e8f5e9;
    color: #4caf50;
  }
}

.status-title {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.status-desc {
  font-size: 14px;
  color: #666;
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
  color: #666;
}

.form-group {
  margin-bottom: 24px;
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
  padding: 0 16px;
  font-size: 16px;
  box-sizing: border-box;

  &:focus {
    border-color: #00A862;
  }
}

.face-upload {
  width: 100%;
  height: 160px;
  border: 2px dashed #e0e0e0;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.face-preview {
  width: 100%;
  height: 100%;
}

.face-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.upload-icon {
  font-size: 40px;
}

.upload-text {
  font-size: 14px;
  color: #999;
}

.notice {
  display: flex;
  gap: 8px;
  padding: 12px;
  background: #f5f5f5;
  border-radius: 8px;
  margin-bottom: 24px;
}

.notice-icon {
  font-size: 16px;
  flex-shrink: 0;
}

.notice-text {
  flex: 1;
  font-size: 12px;
  color: #666;
  line-height: 1.5;
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
