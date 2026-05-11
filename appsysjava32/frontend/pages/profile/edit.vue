<template>
    <view class="profile-edit" :class="{ 'elder-mode': elderMode }">
        <view class="avatar-section">
            <view class="avatar">
                <text class="avatar-text">{{ getAvatarText() }}</text>
            </view>
            <text class="avatar-hint">点击头像更换</text>
        </view>

        <view class="form-section">
            <view class="form-item">
                <text class="form-label">昵称</text>
                <input 
                    class="form-input"
                    v-model="form.nickname"
                    placeholder="请输入昵称"
                    maxlength="20"
                />
            </view>

            <view class="form-item">
                <text class="form-label">真实姓名</text>
                <input 
                    class="form-input"
                    v-model="form.realName"
                    placeholder="请输入真实姓名"
                    maxlength="20"
                />
            </view>

            <view class="form-item">
                <text class="form-label">手机号</text>
                <view class="form-input-wrapper">
                    <text class="input-value">{{ maskPhone(userInfo.phone) }}</text>
                    <text class="input-tag">已绑定</text>
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">身份证号</text>
                <input 
                    class="form-input"
                    v-model="form.idCard"
                    placeholder="请输入身份证号"
                    maxlength="18"
                />
            </view>

            <view class="form-item">
                <text class="form-label">性别</text>
                <view class="gender-options">
                    <view 
                        class="gender-option"
                        :class="{ active: form.gender === 1 }"
                        @click="form.gender = 1"
                    >
                        <text class="gender-icon">👨</text>
                        <text class="gender-text">男</text>
                    </view>
                    <view 
                        class="gender-option"
                        :class="{ active: form.gender === 2 }"
                        @click="form.gender = 2"
                    >
                        <text class="gender-icon">👩</text>
                        <text class="gender-text">女</text>
                    </view>
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">出生日期</text>
                <picker mode="date" :value="form.birthday" @change="onBirthdayChange">
                    <view class="form-picker">
                        <text :class="{ placeholder: !form.birthday }">
                            {{ form.birthday || '请选择出生日期' }}
                        </text>
                        <text class="picker-arrow">›</text>
                    </view>
                </picker>
            </view>

            <view class="form-item">
                <text class="form-label">联系地址</text>
                <input 
                    class="form-input"
                    v-model="form.address"
                    placeholder="请输入联系地址"
                    maxlength="100"
                />
            </view>

            <view class="form-item">
                <text class="form-label">紧急联系人</text>
                <input 
                    class="form-input"
                    v-model="form.emergencyContact"
                    placeholder="请输入紧急联系人姓名"
                    maxlength="20"
                />
            </view>

            <view class="form-item">
                <text class="form-label">紧急联系电话</text>
                <input 
                    class="form-input"
                    v-model="form.emergencyPhone"
                    placeholder="请输入紧急联系电话"
                    maxlength="11"
                    type="number"
                />
            </view>
        </view>

        <view class="save-section">
            <button 
                class="btn btn-primary btn-block btn-large"
                @click="saveProfile"
            >
                保存修改
            </button>
        </view>
    </view>
</template>

<script>
import api from '@/common/js/api.js'
import util from '@/common/js/util.js'

export default {
    data() {
        return {
            elderMode: false,
            userInfo: {},
            form: {
                nickname: '',
                realName: '',
                idCard: '',
                gender: 0,
                birthday: '',
                address: '',
                emergencyContact: '',
                emergencyPhone: ''
            }
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        this.loadUserInfo()
    },
    methods: {
        async loadUserInfo() {
            try {
                const res = await api.auth.getCurrentUser()
                if (res.code === 200) {
                    this.userInfo = res.data
                    this.form = {
                        nickname: res.data.nickname || '',
                        realName: res.data.realName || '',
                        idCard: res.data.idCard || '',
                        gender: res.data.gender || 0,
                        birthday: res.data.birthday || '',
                        address: res.data.address || '',
                        emergencyContact: res.data.emergencyContact || '',
                        emergencyPhone: res.data.emergencyPhone || ''
                    }
                }
            } catch (e) {
                console.error('加载用户信息失败:', e)
            }
        },

        getAvatarText() {
            const name = this.form.nickname || this.userInfo.username || '用'
            return name.charAt(0).toUpperCase()
        },

        maskPhone(phone) {
            if (!phone) return ''
            phone = phone.toString()
            if (phone.length >= 11) {
                return phone.substring(0, 3) + '****' + phone.substring(7)
            }
            return phone
        },

        onBirthdayChange(e) {
            this.form.birthday = e.detail.value
        },

        async saveProfile() {
            if (this.form.idCard && this.form.idCard.length !== 18) {
                util.showToast('身份证号格式不正确', 'none')
                return
            }
            if (this.form.emergencyPhone && !/^1[3-9]\d{9}$/.test(this.form.emergencyPhone)) {
                util.showToast('紧急联系电话格式不正确', 'none')
                return
            }

            try {
                util.showLoading('保存中...')
                const res = await api.user.updateProfile(this.form)
                util.hideLoading()
                if (res.code === 200) {
                    util.showToast('保存成功', 'success')
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                } else {
                    util.showToast(res.message || '保存失败', 'none')
                }
            } catch (e) {
                util.hideLoading()
            }
        }
    }
}
</script>

<style scoped>
.profile-edit {
    min-height: 100vh;
    background: #F5F5F5;
    padding-bottom: 200rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.avatar-section {
    background: #fff;
    padding: 40rpx 0;
    text-align: center;
    margin-bottom: 20rpx;
}

.avatar {
    width: 160rpx;
    height: 160rpx;
    background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 16rpx;
}

.elder-mode .avatar {
    width: 180rpx;
    height: 180rpx;
}

.avatar-text {
    font-size: 64rpx;
    font-weight: 600;
    color: #fff;
}

.elder-mode .avatar-text {
    font-size: 72rpx;
}

.avatar-hint {
    font-size: 26rpx;
    color: #999;
}

.elder-mode .avatar-hint {
    font-size: 30rpx;
}

.form-section {
    background: #fff;
    padding: 0 28rpx;
}

.form-item {
    padding: 28rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.form-item:last-child {
    border-bottom: none;
}

.form-label {
    display: block;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 16rpx;
}

.elder-mode .form-label {
    font-size: 32rpx;
}

.form-input {
    width: 100%;
    height: 88rpx;
    padding: 0 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 30rpx;
    color: #333;
    box-sizing: border-box;
}

.elder-mode .form-input {
    height: 100rpx;
    font-size: 34rpx;
}

.form-input-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 88rpx;
    padding: 0 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
}

.elder-mode .form-input-wrapper {
    height: 100rpx;
}

.input-value {
    font-size: 30rpx;
    color: #333;
}

.elder-mode .input-value {
    font-size: 34rpx;
}

.input-tag {
    font-size: 24rpx;
    color: #52C41A;
    background: #F6FFED;
    padding: 6rpx 16rpx;
    border-radius: 8rpx;
}

.elder-mode .input-tag {
    font-size: 28rpx;
    padding: 8rpx 20rpx;
}

.gender-options {
    display: flex;
    gap: 20rpx;
}

.gender-option {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12rpx;
    padding: 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    border: 2rpx solid transparent;
}

.gender-option.active {
    background: #E6F7FF;
    border-color: #1890FF;
}

.gender-icon {
    font-size: 40rpx;
}

.elder-mode .gender-icon {
    font-size: 48rpx;
}

.gender-text {
    font-size: 30rpx;
    color: #333;
}

.elder-mode .gender-text {
    font-size: 34rpx;
}

.form-picker {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 88rpx;
    padding: 0 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 30rpx;
    color: #333;
}

.elder-mode .form-picker {
    height: 100rpx;
    font-size: 34rpx;
}

.placeholder {
    color: #999;
}

.picker-arrow {
    color: #ccc;
    font-size: 36rpx;
}

.save-section {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.elder-mode .save-section {
    padding: 28rpx;
}
</style>
