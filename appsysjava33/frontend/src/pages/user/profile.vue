<template>
    <view class="container">
        <view class="avatar-card">
            <view class="avatar">
                <text class="avatar-icon">{{ userInfo.name ? userInfo.name.charAt(0) : '👤' }}</text>
            </view>
            <button class="upload-btn" @click="uploadAvatar">更换头像</button>
        </view>

        <view class="info-card">
            <view class="info-item" @click="editField('name')">
                <text class="info-label">姓名</text>
                <text class="info-value">{{ userInfo.name || '未填写' }}</text>
                <text class="info-arrow">›</text>
            </view>
            <view class="info-item" @click="editField('phone')">
                <text class="info-label">手机号</text>
                <text class="info-value">{{ userInfo.phone || '未绑定' }}</text>
                <text class="info-arrow">›</text>
            </view>
            <view class="info-item" @click="editField('email')">
                <text class="info-label">邮箱</text>
                <text class="info-value">{{ userInfo.email || '未填写' }}</text>
                <text class="info-arrow">›</text>
            </view>
            <view class="info-item">
                <text class="info-label">注册时间</text>
                <text class="info-value">{{ userInfo.createTime || '2024-01-01' }}</text>
            </view>
        </view>

        <button class="save-btn" @click="saveProfile">保存修改</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const userInfo = ref({})

onMounted(() => {
    loadUserInfo()
})

const loadUserInfo = () => {
    userInfo.value = {
        id: 1,
        name: '张三',
        phone: '138****8888',
        email: '',
        createTime: '2024-01-01'
    }
}

const uploadAvatar = () => {
    uni.chooseImage({
        count: 1,
        success: (res) => {
            uni.showToast({
                title: '上传成功',
                icon: 'success'
            })
        }
    })
}

const editField = (field) => {
    uni.showModal({
        title: '修改信息',
        editable: true,
        placeholderText: '请输入' + (field === 'name' ? '姓名' : field === 'phone' ? '手机号' : '邮箱'),
        success: (res) => {
            if (res.confirm && res.content) {
                userInfo.value[field] = res.content
                uni.showToast({
                    title: '修改成功',
                    icon: 'success'
                })
            }
        }
    })
}

const saveProfile = () => {
    uni.showToast({
        title: '保存成功',
        icon: 'success'
    })
    setTimeout(() => {
        uni.navigateBack()
    }, 1000)
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.avatar-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 40rpx 24rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48rpx;
    color: #FFFFFF;
    font-weight: 500;
    margin-bottom: 20rpx;
}

.upload-btn {
    background: #E8F3FF;
    color: #007AFF;
    font-size: 24rpx;
    padding: 12rpx 24rpx;
    border-radius: 24rpx;
    margin: 0;
}

.info-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 40rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.info-item {
    display: flex;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.info-item:last-child {
    border-bottom: none;
}

.info-label {
    font-size: 28rpx;
    color: #666666;
    width: 120rpx;
}

.info-value {
    flex: 1;
    font-size: 28rpx;
    color: #333333;
    text-align: right;
}

.info-arrow {
    font-size: 28rpx;
    color: #CCCCCC;
    margin-left: 12rpx;
}

.save-btn {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}
</style>
