<template>
  <view class="login-page">
    <view class="logo-section">
      <view class="logo-icon">
        <text>🏺</text>
      </view>
      <text class="app-name">文物收藏</text>
      <text class="app-desc">专业的文物收藏管理平台</text>
    </view>

    <view class="form-section card">
      <view class="form-item">
        <text class="form-label">用户名</text>
        <input 
          class="form-input" 
          placeholder="请输入用户名" 
          v-model="loginForm.username"
        />
      </view>
      <view class="form-item">
        <text class="form-label">密码</text>
        <input 
          class="form-input" 
          type="password" 
          placeholder="请输入密码" 
          v-model="loginForm.password"
        />
      </view>

      <button class="login-btn" @click="handleLogin">登录</button>
      
      <view class="register-link">
        <text>还没有账号？</text>
        <text class="link-text" @click="goToRegister">立即注册</text>
      </view>
    </view>

    <view class="demo-accounts">
      <text class="demo-title">演示账号</text>
      <view class="account-item">
        <text class="account-label">普通用户：</text>
        <text class="account-value">user / 123456</text>
      </view>
      <view class="account-item">
        <text class="account-label">管理员：</text>
        <text class="account-value">admin / 123456</text>
      </view>
    </view>

    <!-- 语音输入按钮（长辈模式） -->
    <view class="voice-input-btn" v-if="elderModeEnabled" @click="startVoiceInput">
      <text class="voice-icon">🎤</text>
      <text class="voice-text">语音输入</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      elderModeEnabled: false
    }
  },
  onLoad() {
    this.elderModeEnabled = uni.getStorageSync('elderMode') == 1
  },
  methods: {
    async handleLogin() {
      if (!this.loginForm.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }
      if (!this.loginForm.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }

      // 模拟登录
      uni.showLoading({
        title: '登录中...'
      })

      setTimeout(() => {
        uni.hideLoading()
        
        // 保存登录状态
        uni.setStorageSync('token', 'demo_token_' + Date.now())
        uni.setStorageSync('userInfo', {
          username: this.loginForm.username,
          nickname: this.loginForm.username === 'admin' ? '管理员' : '文物收藏家',
          userType: this.loginForm.username === 'admin' ? 3 : 1
        })

        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          })
        }, 1000)
      }, 1000)
    },
    goToRegister() {
      uni.showToast({
        title: '注册功能开发中',
        icon: 'none'
      })
    },
    startVoiceInput() {
      uni.showToast({
        title: '语音输入功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped lang="scss">
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 100rpx 40rpx 40rpx;
}

.logo-section {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo-icon {
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
  font-size: 80rpx;
}

.app-name {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 16rpx;
}

.app-desc {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.form-section {
  padding: 50rpx 40rpx;
}

.form-item {
  margin-bottom: 40rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.form-input {
  width: 100%;
  height: 88rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 12rpx;
  font-size: 32rpx;
  font-weight: 500;
  margin-top: 20rpx;
}

.register-link {
  text-align: center;
  margin-top: 40rpx;
  font-size: 26rpx;
  color: #999;
}

.link-text {
  color: #667eea;
  margin-left: 8rpx;
}

.demo-accounts {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12rpx;
  padding: 30rpx;
  margin-top: 40rpx;
}

.demo-title {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 20rpx;
  text-align: center;
}

.account-item {
  display: flex;
  justify-content: center;
  margin-bottom: 12rpx;
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.account-label {
  margin-right: 10rpx;
}

.voice-input-btn {
  position: fixed;
  right: 40rpx;
  bottom: 200rpx;
  width: 120rpx;
  height: 120rpx;
  background: #ff6b6b;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(255, 107, 107, 0.4);
}

.voice-icon {
  font-size: 40rpx;
}

.voice-text {
  font-size: 20rpx;
  color: #fff;
  margin-top: 4rpx;
}

/* 长辈模式样式 */
.elder-mode .form-label {
  font-size: 32rpx;
}

.elder-mode .form-input {
  height: 100rpx;
  font-size: 32rpx;
}

.elder-mode .login-btn {
  height: 100rpx;
  font-size: 36rpx;
}

.elder-mode .app-name {
  font-size: 56rpx;
}

.elder-mode .app-desc {
  font-size: 32rpx;
}
</style>