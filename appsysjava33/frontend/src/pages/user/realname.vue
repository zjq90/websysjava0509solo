<template>
    <view class="container">
        <view class="status-card" v-if="realNameStatus === 2">
            <text class="status-icon">✅</text>
            <text class="status-title">已完成实名认证</text>
            <text class="status-desc">您的身份信息已通过核验</text>
        </view>

        <view class="status-card pending" v-else-if="realNameStatus === 1">
            <text class="status-icon">⏳</text>
            <text class="status-title">审核中</text>
            <text class="status-desc">您的实名认证正在审核中，请耐心等待</text>
        </view>

        <view class="form-card" v-if="realNameStatus !== 2">
            <view class="form-title">身份信息</view>
            <view class="form-item">
                <text class="input-label">真实姓名</text>
                <input class="input" v-model="realName" placeholder="请输入真实姓名" />
            </view>
            <view class="form-item">
                <text class="input-label">身份证号</text>
                <input class="input" v-model="idCard" placeholder="请输入18位身份证号" maxlength="18" />
            </view>
        </view>

        <view class="form-card" v-if="realNameStatus !== 2">
            <view class="form-title">上传证件照片</view>
            <view class="upload-section">
                <view class="upload-item" @click="uploadFront">
                    <image v-if="idCardFront" :src="idCardFront" class="upload-image" />
                    <view v-else class="upload-placeholder">
                        <text class="upload-icon">📷</text>
                        <text class="upload-text">身份证正面</text>
                    </view>
                </view>
                <view class="upload-item" @click="uploadBack">
                    <image v-if="idCardBack" :src="idCardBack" class="upload-image" />
                    <view v-else class="upload-placeholder">
                        <text class="upload-icon">📷</text>
                        <text class="upload-text">身份证反面</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="form-card" v-if="realNameStatus !== 2">
            <view class="form-title">活体检测</view>
            <view class="liveness-section">
                <view class="liveness-btn" @click="startLiveness">
                    <text class="liveness-icon">👤</text>
                    <text class="liveness-text">{{ livenessVerified ? '已完成活体检测' : '点击开始活体检测' }}</text>
                </view>
                <view class="liveness-tips">
                    <text class="tip-text">• 请确保光线充足</text>
                    <text class="tip-text">• 请正对摄像头，保持面部清晰</text>
                    <text class="tip-text">• 请根据提示完成相应动作</text>
                </view>
            </view>
        </view>

        <view class="agreement" v-if="realNameStatus !== 2">
            <checkbox :checked="agreed" @click="agreed = !agreed" color="#007AFF" />
            <text class="agreement-text">我已阅读并同意</text>
            <text class="agreement-link">《实名认证服务协议》</text>
        </view>

        <button class="submit-btn" v-if="realNameStatus !== 2" :disabled="!canSubmit" @click="submitRealName">提交认证</button>
    </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const realNameStatus = ref(0)
const realName = ref('')
const idCard = ref('')
const idCardFront = ref('')
const idCardBack = ref('')
const livenessVerified = ref(false)
const agreed = ref(false)

onMounted(() => {
    const user = uni.getStorageSync('userInfo')
    if (user && user.realNameStatus !== undefined) {
        realNameStatus.value = user.realNameStatus
    }
})

const canSubmit = computed(() => {
    return realName.value && idCard.value && idCardFront.value && 
           idCardBack.value && livenessVerified.value && agreed.value
})

const uploadFront = () => {
    uni.chooseImage({
        count: 1,
        success: (res) => {
            idCardFront.value = res.tempFilePaths[0]
            uni.showToast({ title: '上传成功', icon: 'success' })
        }
    })
}

const uploadBack = () => {
    uni.chooseImage({
        count: 1,
        success: (res) => {
            idCardBack.value = res.tempFilePaths[0]
            uni.showToast({ title: '上传成功', icon: 'success' })
        }
    })
}

const startLiveness = () => {
    uni.showModal({
        title: '活体检测',
        content: '模拟活体检测功能，请在实际项目中接入专业的活体检测SDK',
        showCancel: false,
        success: () => {
            livenessVerified.value = true
            uni.showToast({ title: '活体检测通过', icon: 'success' })
        }
    })
}

const submitRealName = () => {
    uni.showLoading({ title: '提交中...' })
    setTimeout(() => {
        uni.hideLoading()
        realNameStatus.value = 1
        const user = uni.getStorageSync('userInfo')
        if (user) {
            user.realNameStatus = 1
            uni.setStorageSync('userInfo', user)
        }
        uni.showToast({ title: '提交成功，请等待审核', icon: 'none' })
    }, 1500)
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.status-card {
    background: #E8F8ED;
    border-radius: 16rpx;
    padding: 48rpx 24rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20rpx;
}

.status-card.pending {
    background: #FFF5E6;
}

.status-icon {
    font-size: 64rpx;
    margin-bottom: 16rpx;
}

.status-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 8rpx;
}

.status-desc {
    font-size: 24rpx;
    color: #666666;
}

.form-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-title {
    display: block;
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 20rpx;
}

.form-item {
    margin-bottom: 20rpx;
}

.form-item:last-child {
    margin-bottom: 0;
}

.input-label {
    display: block;
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 12rpx;
}

.input {
    width: 100%;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333333;
}

.upload-section {
    display: flex;
    gap: 20rpx;
}

.upload-item {
    flex: 1;
    height: 200rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

.upload-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.upload-icon {
    font-size: 48rpx;
    margin-bottom: 8rpx;
}

.upload-text {
    font-size: 24rpx;
    color: #999999;
}

.upload-image {
    width: 100%;
    height: 100%;
}

.liveness-section {
    display: flex;
    flex-direction: column;
}

.liveness-btn {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 32rpx;
    background: #E8F3FF;
    border-radius: 12rpx;
    margin-bottom: 20rpx;
}

.liveness-icon {
    font-size: 40rpx;
    margin-right: 12rpx;
}

.liveness-text {
    font-size: 28rpx;
    color: #007AFF;
}

.liveness-tips {
    display: flex;
    flex-direction: column;
    gap: 8rpx;
}

.tip-text {
    font-size: 24rpx;
    color: #999999;
}

.agreement {
    display: flex;
    align-items: center;
    padding: 20rpx;
    margin-bottom: 30rpx;
}

.agreement-text {
    font-size: 24rpx;
    color: #666666;
    margin-left: 12rpx;
}

.agreement-link {
    font-size: 24rpx;
    color: #007AFF;
}

.submit-btn {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.submit-btn[disabled] {
    background: #CCCCCC;
}
</style>
