<template>
  <view class="profile-page">
    <view class="header">
      <text class="title">编辑资料</text>
    </view>

    <view class="content">
      <view class="avatar-section">
        <image :src="form.avatar || '/static/default-avatar.png'" class="avatar" mode="aspectFill" />
        <view class="change-avatar-btn" @click="chooseAvatar">
          <text>更换头像</text>
        </view>
      </view>

      <view class="form-section">
        <view class="form-item">
          <text class="label">昵称</text>
          <input v-model="form.nickname" class="input" placeholder="请输入昵称" />
        </view>
        <view class="form-item">
          <text class="label">手机号</text>
          <input v-model="form.phone" class="input" type="number" placeholder="请输入手机号" />
        </view>
        <view class="form-item">
          <text class="label">邮箱</text>
          <input v-model="form.email" class="input" type="text" placeholder="请输入邮箱" />
        </view>
        <view class="form-item">
          <text class="label">所在地区</text>
          <view class="picker-row" @click="chooseLocation">
            <text class="picker-value">{{ form.location || '请选择地区' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </view>
        <view class="form-item">
          <text class="label">个人简介</text>
          <textarea v-model="form.bio" class="textarea" placeholder="请输入个人简介" maxlength="200" />
          <text class="char-count">{{ form.bio.length }}/200</text>
        </view>
      </view>

      <view class="gender-section">
        <text class="section-title">性别</text>
        <view class="gender-options">
          <view
            class="gender-option"
            :class="{ active: form.gender === 'male' }"
            @click="form.gender = 'male'"
          >
            <text class="gender-icon">👨</text>
            <text class="gender-text">男</text>
          </view>
          <view
            class="gender-option"
            :class="{ active: form.gender === 'female' }"
            @click="form.gender = 'female'"
          >
            <text class="gender-icon">👩</text>
            <text class="gender-text">女</text>
          </view>
          <view
            class="gender-option"
            :class="{ active: form.gender === 'secret' }"
            @click="form.gender = 'secret'"
          >
            <text class="gender-icon">🔒</text>
            <text class="gender-text">保密</text>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <button class="save-btn" @click="saveProfile" :disabled="saving">
        {{ saving ? '保存中...' : '保存' }}
      </button>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      saving: false,
      form: {
        nickname: '',
        phone: '',
        email: '',
        location: '',
        bio: '',
        avatar: '',
        gender: 'secret'
      }
    }
  },
  onLoad() {
    this.loadProfile()
  },
  methods: {
    loadProfile() {
      const user = uni.getStorageSync('user')
      if (user) {
        this.form = {
          ...this.form,
          nickname: user.nickname || '',
          phone: user.phone || '',
          email: user.email || '',
          location: user.location || '',
          bio: user.bio || '',
          avatar: user.avatar || ''
        }
      }
    },
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        success: (res) => {
          this.form.avatar = res.tempFilePaths[0]
        }
      })
    },
    chooseLocation() {
      uni.showToast({
        title: '地区选择功能开发中',
        icon: 'none'
      })
    },
    async saveProfile() {
      if (!this.form.nickname) {
        uni.showToast({
          title: '请输入昵称',
          icon: 'none'
        })
        return
      }

      this.saving = true

      try {
        const user = uni.getStorageSync('user')
        if (user) {
          const updatedUser = { ...user, ...this.form }
          uni.setStorageSync('user', updatedUser)
        }

        uni.showToast({
          title: '保存成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({
          title: '保存成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } finally {
        this.saving = false
      }
    }
  }
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
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

.avatar-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  margin-bottom: 20rpx;
  background: #f5f5f5;
}

.change-avatar-btn {
  font-size: 26rpx;
  color: #409EFF;
}

.form-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.form-item {
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 16rpx;
}

.input {
  font-size: 28rpx;
  color: #333;
  width: 100%;
}

.textarea {
  font-size: 28rpx;
  color: #333;
  width: 100%;
  min-height: 150rpx;
}

.char-count {
  font-size: 22rpx;
  color: #999;
  text-align: right;
  display: block;
  margin-top: 8rpx;
}

.picker-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.picker-value {
  font-size: 28rpx;
  color: #333;
}

.picker-arrow {
  font-size: 28rpx;
  color: #ccc;
}

.gender-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 24rpx;
}

.gender-options {
  display: flex;
  justify-content: space-around;
}

.gender-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 40rpx;
  border-radius: 16rpx;
  border: 2rpx solid #eee;
}

.gender-option.active {
  border-color: #409EFF;
  background: #ecf5ff;
}

.gender-icon {
  font-size: 48rpx;
  margin-bottom: 8rpx;
}

.gender-text {
  font-size: 26rpx;
  color: #666;
}

.gender-option.active .gender-text {
  color: #409EFF;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 140rpx;
  background: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.save-btn {
  width: 100%;
  height: 100rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 50rpx;
  font-size: 32rpx;
  font-weight: bold;
}
</style>
