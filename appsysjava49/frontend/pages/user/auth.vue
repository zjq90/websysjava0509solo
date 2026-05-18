<template>
  <view class="auth-page">
    <view class="header">
      <text class="title">实名认证</text>
    </view>

    <view class="content" v-if="status === 'success'">
      <view class="success-section">
        <text class="success-icon">✅</text>
        <text class="success-title">认证成功</text>
        <text class="success-desc">您的实名认证已通过审核</text>
      </view>
      <view class="info-section">
        <view class="info-item">
          <text class="info-label">真实姓名</text>
          <text class="info-value">{{ authInfo.realName }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">身份证号</text>
          <text class="info-value">{{ authInfo.idCard }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">认证时间</text>
          <text class="info-value">{{ authInfo.authTime }}</text>
        </view>
      </view>
    </view>

    <view class="content" v-else>
      <view class="steps">
        <view
          v-for="(step, index) in steps"
          :key="index"
          class="step-item"
          :class="{ active: currentStep >= index + 1, completed: currentStep > index + 1 }"
        >
          <view class="step-circle">
            <text v-if="currentStep > index + 1" class="check-icon">✓</text>
            <text v-else>{{ index + 1 }}</text>
          </view>
          <text class="step-text">{{ step }}</text>
        </view>
        <view class="step-line" />
      </view>

      <view class="form-section" v-if="currentStep === 1">
        <view class="form-item">
          <text class="label">真实姓名</text>
          <input v-model="form.realName" class="input" placeholder="请输入真实姓名" />
        </view>
        <view class="form-item">
          <text class="label">身份证号</text>
          <input v-model="form.idCard" class="input" placeholder="请输入身份证号码" />
        </view>
        <view class="tips">
          <text class="tips-icon">⚠️</text>
          <text class="tips-text">请确保您填写的信息真实有效，身份信息仅用于实名认证，我们将严格保密</text>
        </view>
        <button class="next-btn" @click="goToNext">下一步</button>
      </view>

      <view class="form-section" v-if="currentStep === 2">
        <text class="section-title">上传身份证照片</text>
        <view class="id-card-section">
          <view class="id-card-item" @click="uploadFront">
            <image v-if="form.idCardFront" :src="form.idCardFront" class="id-card-image" mode="aspectFill" />
            <view v-else class="id-card-placeholder">
              <text class="placeholder-icon">📷</text>
              <text class="placeholder-text">上传身份证正面</text>
            </view>
          </view>
          <view class="id-card-item" @click="uploadBack">
            <image v-if="form.idCardBack" :src="form.idCardBack" class="id-card-image" mode="aspectFill" />
            <view v-else class="id-card-placeholder">
              <text class="placeholder-icon">📷</text>
              <text class="placeholder-text">上传身份证背面</text>
            </view>
          </view>
        </view>
        <button class="next-btn" @click="goToNext">下一步</button>
      </view>

      <view class="form-section" v-if="currentStep === 3">
        <text class="section-title">人脸验证</text>
        <view class="face-section">
          <view class="face-camera" @click="startFaceVerify">
            <view v-if="form.faceImage" class="face-preview">
              <image :src="form.faceImage" class="face-image" mode="aspectFill" />
            </view>
            <view v-else class="face-placeholder">
              <text class="face-icon">👤</text>
              <text class="face-text">点击开始人脸识别</text>
            </view>
          </view>
          <view class="face-tips">
            <text class="tip-item">• 请正对摄像头，保持光线充足</text>
            <text class="tip-item">• 请摘下帽子、口罩、眼镜等物品</text>
            <text class="tip-item">• 请确保是本人进行验证</text>
          </view>
        </view>
        <button class="submit-btn" @click="submitAuth" :disabled="submitting">
          {{ submitting ? '提交中...' : '提交认证' }}
        </button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentStep: 1,
      submitting: false,
      status: 'pending',
      steps: ['填写信息', '上传证件', '人脸验证'],
      form: {
        realName: '',
        idCard: '',
        idCardFront: '',
        idCardBack: '',
        faceImage: ''
      },
      authInfo: {
        realName: '张*',
        idCard: '110**********1234',
        authTime: '2024-01-01 12:00'
      }
    }
  },
  methods: {
    goToNext() {
      if (this.currentStep === 1) {
        if (!this.form.realName) {
          uni.showToast({
            title: '请输入真实姓名',
            icon: 'none'
          })
          return
        }
        if (!this.form.idCard) {
          uni.showToast({
            title: '请输入身份证号',
            icon: 'none'
          })
          return
        }
      } else if (this.currentStep === 2) {
        if (!this.form.idCardFront) {
          uni.showToast({
            title: '请上传身份证正面',
            icon: 'none'
          })
          return
        }
        if (!this.form.idCardBack) {
          uni.showToast({
            title: '请上传身份证背面',
            icon: 'none'
          })
          return
        }
      }

      if (this.currentStep < 3) {
        this.currentStep++
      }
    },
    uploadFront() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.form.idCardFront = res.tempFilePaths[0]
        }
      })
    },
    uploadBack() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.form.idCardBack = res.tempFilePaths[0]
        }
      })
    },
    startFaceVerify() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.form.faceImage = res.tempFilePaths[0]
        }
      })
    },
    submitAuth() {
      if (!this.form.faceImage) {
        uni.showToast({
          title: '请完成人脸验证',
          icon: 'none'
        })
        return
      }

      this.submitting = true

      setTimeout(() => {
        this.submitting = false
        this.status = 'success'
        this.authInfo = {
          realName: this.form.realName.charAt(0) + '*',
          idCard: this.form.idCard.substring(0, 3) + '**********' + this.form.idCard.substring(this.form.idCard.length - 4),
          authTime: new Date().toLocaleString()
        }

        const user = uni.getStorageSync('user')
        if (user) {
          user.isVerified = true
          uni.setStorageSync('user', user)
        }

        uni.showToast({
          title: '认证成功',
          icon: 'success'
        })
      }, 2000)
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: #fff;
  padding: 30rpx;
  text-align: center;
  border-bottom: 1rpx solid #eee;
}

.title {
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
}

.content {
  padding: 20rpx;
}

.success-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 60rpx 40rpx;
  text-align: center;
  margin-bottom: 20rpx;
}

.success-icon {
  font-size: 80rpx;
  display: block;
  margin-bottom: 20rpx;
}

.success-title {
  font-size: 32rpx;
  color: #67C23A;
  font-weight: bold;
  display: block;
  margin-bottom: 12rpx;
}

.success-desc {
  font-size: 26rpx;
  color: #999;
}

.info-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 20rpx 30rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #666;
}

.info-value {
  font-size: 28rpx;
  color: #333;
}

.steps {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 20rpx;
  display: flex;
  justify-content: space-between;
  position: relative;
}

.step-line {
  position: absolute;
  top: 60rpx;
  left: 80rpx;
  right: 80rpx;
  height: 2rpx;
  background: #e0e0e0;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 1;
}

.step-circle {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #999;
  margin-bottom: 12rpx;
}

.step-item.active .step-circle,
.step-item.completed .step-circle {
  background: #409EFF;
  color: #fff;
}

.step-text {
  font-size: 24rpx;
  color: #999;
}

.step-item.active .step-text,
.step-item.completed .step-text {
  color: #409EFF;
}

.check-icon {
  font-size: 32rpx;
}

.form-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #666;
  display: block;
  margin-bottom: 16rpx;
}

.input {
  height: 90rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 16rpx;
  padding: 0 30rpx;
  font-size: 30rpx;
  width: 100%;
  box-sizing: border-box;
}

.input:focus {
  border-color: #409EFF;
}

.tips {
  background: #fff7e6;
  border-radius: 12rpx;
  padding: 24rpx;
  margin-bottom: 30rpx;
  display: flex;
  align-items: flex-start;
}

.tips-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
}

.tips-text {
  font-size: 24rpx;
  color: #E6A23C;
  line-height: 1.6;
  flex: 1;
}

.id-card-section {
  margin-bottom: 30rpx;
}

.id-card-item {
  height: 240rpx;
  border: 2rpx dashed #ddd;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.id-card-image {
  width: 100%;
  height: 100%;
}

.id-card-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.placeholder-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.placeholder-text {
  font-size: 26rpx;
  color: #999;
}

.face-section {
  margin-bottom: 30rpx;
}

.face-camera {
  height: 400rpx;
  border: 2rpx dashed #ddd;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

.face-preview {
  width: 100%;
  height: 100%;
}

.face-image {
  width: 100%;
  height: 100%;
}

.face-placeholder {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.face-icon {
  font-size: 80rpx;
  margin-bottom: 16rpx;
}

.face-text {
  font-size: 28rpx;
  color: #999;
}

.face-tips {
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 24rpx;
}

.tip-item {
  font-size: 24rpx;
  color: #666;
  line-height: 1.8;
  display: block;
}

.next-btn,
.submit-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: bold;
}
</style>
