<template>
    <view class="container">
        <view class="form-card">
            <view class="form-title">原安装地址</view>
            <view class="form-item">
                <input class="input" v-model="oldAddress" placeholder="请输入原安装地址" />
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">新安装地址</view>
            <view class="form-item">
                <input class="input" v-model="newAddress" placeholder="请输入新安装地址" />
            </view>
            <view class="form-item">
                <input class="input" v-model="contactName" placeholder="联系人姓名" />
            </view>
            <view class="form-item">
                <input class="input" v-model="contactPhone" placeholder="联系电话" />
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">预约时间</view>
            <view class="form-item">
                <picker mode="date" :value="appointmentDate" @change="onDateChange">
                    <view class="picker-text">
                        <text v-if="appointmentDate">{{ appointmentDate }}</text>
                        <text v-else class="placeholder">请选择预约日期</text>
                    </view>
                </picker>
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">上传地址证明（选填）</view>
            <view class="upload-area" @click="uploadProof">
                <text class="upload-icon">📎</text>
                <text class="upload-text">点击上传房产证/租赁合同</text>
            </view>
            <view class="file-list" v-if="uploadedFiles.length > 0">
                <view class="file-item" v-for="(file, index) in uploadedFiles" :key="index">
                    <text class="file-name">{{ file.name }}</text>
                    <text class="file-delete" @click="deleteFile(index)">✕</text>
                </view>
            </view>
        </view>

        <button class="submit-btn" :disabled="!canSubmit" @click="submitOrder">提交申请</button>
    </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const oldAddress = ref('')
const newAddress = ref('')
const contactName = ref('')
const contactPhone = ref('')
const appointmentDate = ref('')
const uploadedFiles = ref([])

const canSubmit = computed(() => {
    return oldAddress.value && newAddress.value && contactName.value && contactPhone.value && appointmentDate.value
})

const onDateChange = (e) => {
    appointmentDate.value = e.detail.value
}

const uploadProof = () => {
    uni.chooseImage({
        count: 3,
        success: (res) => {
            res.tempFilePaths.forEach((path, index) => {
                uploadedFiles.value.push({
                    name: '证明材料' + (uploadedFiles.value.length + 1) + '.jpg',
                    path: path
                })
            })
            uni.showToast({
                title: '上传成功',
                icon: 'success'
            })
        }
    })
}

const deleteFile = (index) => {
    uploadedFiles.value.splice(index, 1)
}

const submitOrder = () => {
    uni.showModal({
        title: '确认提交',
        content: '确认提交移机申请吗？工作人员会在1个工作日内联系您。',
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

.picker-text {
    width: 100%;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333333;
}

.placeholder {
    color: #999999;
}

.upload-area {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 48rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    border: 2rpx dashed #CCCCCC;
}

.upload-icon {
    font-size: 48rpx;
    margin-bottom: 12rpx;
}

.upload-text {
    font-size: 26rpx;
    color: #666666;
}

.file-list {
    margin-top: 20rpx;
}

.file-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 20rpx;
    background: #E8F3FF;
    border-radius: 8rpx;
    margin-bottom: 12rpx;
}

.file-name {
    font-size: 26rpx;
    color: #007AFF;
}

.file-delete {
    font-size: 28rpx;
    color: #FF3B30;
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
