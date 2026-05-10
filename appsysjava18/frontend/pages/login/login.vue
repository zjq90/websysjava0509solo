<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-text">农</text>
      </view>
      <view class="app-name">农业应用系统</view>
      <view class="app-desc">智慧农业 · 高效管理</view>
    </view>

    <view class="login-form">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">👤</text>
          <input 
            class="form-input" 
            type="text" 
            v-model="username" 
            placeholder="请输入用户名"
            placeholder-class="placeholder"
          />
        </view>
      </view>

      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">🔒</text>
          <input 
            class="form-input" 
            type="password" 
            v-model="password" 
            placeholder="请输入密码"
            placeholder-class="placeholder"
          />
        </view>
      </view>

      <view class="login-btn" @click="handleLogin">
        <text class="btn-text">登 录</text>
      </view>

      <view class="test-accounts">
        <view class="test-title">测试账号（密码均为：123456）</view>
        <view class="account-list">
          <view class="account-item" @click="fillAccount('warehouse')">仓管员</view>
          <view class="account-item" @click="fillAccount('salesman')">销售员</view>
          <view class="account-item" @click="fillAccount('agricultural')">农技员</view>
          <view class="account-item" @click="fillAccount('manager')">管理层</view>
          <view class="account-item" @click="fillAccount('admin')">管理员</view>
        </view>
      </view>
    </view>

    <view class="login-footer">
      <text class="footer-text">© 2025 农业应用系统</text>
    </view>
  </view>
</template>

<script>
import { useUserStore } from '@/stores/user'

export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },

  methods: {
    // 填充测试账号
    fillAccount(username) {
      this.username = username
      this.password = '123456'
    },

    // 登录
    async handleLogin() {
      if (!this.username.trim()) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }

      if (!this.password.trim()) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }

      uni.showLoading({
        title: '登录中...',
        mask: true
      })

      try {
        const userStore = useUserStore()
        await userStore.loginAction({
          username: this.username,
          password: this.password
        })

        uni.hideLoading()
        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          })
        }, 1000)
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  padding: 60rpx 40rpx;
  display: flex;
  flex-direction: column;
}

.login-header {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 80rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;
}

.logo-text {
  font-size: 80rpx;
  color: #FFFFFF;
  font-weight: bold;
}

.app-name {
  font-size: 48rpx;
  color: #FFFFFF;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.app-desc {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-form {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  margin-bottom: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 30rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 0 30rpx;
  height: 100rpx;
}

.input-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.form-input {
  flex: 1;
  font-size: 32rpx;
  color: #333333;
}

.placeholder {
  color: #999999;
}

.login-btn {
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 16rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20rpx;
}

.btn-text {
  color: #FFFFFF;
  font-size: 34rpx;
  font-weight: 500;
}

.test-accounts {
  margin-top: 40rpx;
  padding-top: 40rpx;
  border-top: 1rpx solid #EEEEEE;
}

.test-title {
  font-size: 26rpx;
  color: #999999;
  text-align: center;
  margin-bottom: 20rpx;
}

.account-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 16rpx;
}

.account-item {
  font-size: 26rpx;
  color: #007AFF;
  padding: 10rpx 20rpx;
  background: rgba(0, 122, 255, 0.1);
  border-radius: 8rpx;
}

.login-footer {
  text-align: center;
  padding-bottom: 40rpx;
}

.footer-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.6);
}
</style>
