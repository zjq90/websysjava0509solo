<template>
  <view class="login-container">
    <view class="logo-section">
      <view class="logo-icon">
        <text class="logo-text">U</text>
      </view>
      <text class="app-name">用户端APP</text>
      <text class="app-desc">会员服务 · 积分兑换 · 消息中心</text>
    </view>

    <view class="form-section">
      <view class="input-group">
        <text class="input-label">用户名</text>
        <input 
          class="input-field" 
          v-model="username" 
          placeholder="请输入用户名"
          type="text"
        />
      </view>

      <view class="input-group">
        <text class="input-label">密码</text>
        <input 
          class="input-field" 
          v-model="password" 
          placeholder="请输入密码"
          type="password"
        />
      </view>

      <button class="btn btn-primary login-btn" @click="handleLogin">
        登录
      </button>

      <view class="test-accounts">
        <text class="test-title">测试账号：</text>
        <view class="account-item">
          <text class="account-label">普通用户：</text>
          <text class="account-value">zhangsan / 123456</text>
        </view>
        <view class="account-item">
          <text class="account-label">金卡会员：</text>
          <text class="account-value">lisi / 123456</text>
        </view>
        <view class="account-item">
          <text class="account-label">钻石会员：</text>
          <text class="account-value">wangwu / 123456</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    async handleLogin() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      uni.showLoading({ title: '登录中...' })

      try {
        const user = await api.login(this.username, this.password)
        uni.hideLoading()

        uni.setStorageSync('userId', user.id)
        uni.setStorageSync('userInfo', user)

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
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  padding: 100rpx 40rpx;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80rpx;
}

.logo-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24rpx;
}

.logo-text {
  font-size: 60rpx;
  font-weight: bold;
  color: #ffffff;
}

.app-name {
  font-size: 40rpx;
  font-weight: 600;
  color: #ffffff;
  margin-bottom: 12rpx;
}

.app-desc {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.form-section {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  margin-top: 40rpx;
}

.test-accounts {
  margin-top: 60rpx;
  padding-top: 40rpx;
  border-top: 1rpx solid #f0f0f0;
}

.test-title {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 20rpx;
  display: block;
}

.account-item {
  display: flex;
  margin-bottom: 12rpx;
}

.account-label {
  font-size: 24rpx;
  color: #999;
  width: 140rpx;
}

.account-value {
  font-size: 24rpx;
  color: #1989fa;
  font-weight: 500;
}
</style>
