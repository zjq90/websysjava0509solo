<template>
    <view class="container">
        <view class="warning-card">
            <text class="warning-icon">⚠️</text>
            <text class="warning-title">销户须知</text>
            <text class="warning-text">1. 销户后宽带服务将立即停止</text>
            <text class="warning-text">2. 请确保已结清所有费用</text>
            <text class="warning-text">3. 已预存的费用将按实际使用情况结算</text>
        </view>

        <view class="form-card">
            <view class="form-title">宽带信息</view>
            <view class="form-item">
                <input class="input" v-model="broadbandNumber" placeholder="请输入宽带号码" />
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">销户原因（选填）</view>
            <view class="form-item">
                <textarea class="textarea" v-model="reason" placeholder="请输入销户原因" maxlength="200" />
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">联系信息</view>
            <view class="form-item">
                <input class="input" v-model="contactName" placeholder="联系人姓名" />
            </view>
            <view class="form-item">
                <input class="input" v-model="contactPhone" placeholder="联系电话" />
            </view>
        </view>

        <button class="submit-btn" :disabled="!canSubmit" @click="submitOrder">提交销户申请</button>
    </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const broadbandNumber = ref('')
const reason = ref('')
const contactName = ref('')
const contactPhone = ref('')

const canSubmit = computed(() => {
    return broadbandNumber.value && contactName.value && contactPhone.value
})

const submitOrder = () => {
    uni.showModal({
        title: '确认销户',
        content: '确认提交销户申请吗？工作人员会在1个工作日内联系您核实信息。',
        confirmColor: '#FF3B30',
        success: (res) => {
            if (res.confirm) {
                uni.showLoading({ title: '提交中...' })
                setTimeout(() => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '申请提交成功',
                        icon: 'success'
                    })
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                }, 1000)
            }
        }
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.warning-card {
    background: #FFF5E6;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    display: flex;
    flex-direction: column;
}

.warning-icon {
    font-size: 48rpx;
    text-align: center;
    margin-bottom: 16rpx;
}

.warning-title {
    font-size: 30rpx;
    font-weight: 500;
    color: #FF9500;
    text-align: center;
    margin-bottom: 16rpx;
}

.warning-text {
    font-size: 24rpx;
    color: #E67E22;
    margin-bottom: 8rpx;
    line-height: 1.6;
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
    margin-bottom: 16rpx;
}

.form-item:last-child {
    margin-bottom: 0;
}

.input {
    width: 100%;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333333;
}

.textarea {
    width: 100%;
    height: 160rpx;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333333;
}

.submit-btn {
    width: 100%;
    background: linear-gradient(135deg, #FF3B30 0%, #CC0000 100%);
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
