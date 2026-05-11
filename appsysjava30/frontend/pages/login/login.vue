<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-icon">🏥</text>
      </view>
      <text class="app-name">医疗挂号系统</text>
      <text class="app-desc">便捷预约，智慧就医</text>
    </view>
    
    <view class="login-form card">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          v-model="form.username" 
          class="input" 
          placeholder="请输入用户名" 
          maxlength="20"
        />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input 
          v-model="form.password" 
          class="input" 
          type="password" 
          placeholder="请输入密码"
          maxlength="20"
          :password="!showPassword"
        />
        <text class="toggle-pwd" @click="showPassword = !showPassword">
          {{ showPassword ? '🙈' : '👁️' }}
        </text>
      </view>
      
      <view class="quick-login">
        <text class="quick-btn" @click="quickLogin('zhangshan')">快速登录：张三</text>
        <text class="quick-btn" @click="quickLogin('lisi')">快速登录：李四</text>
      </view>
      
      <view class="btn-primary" @click="handleLogin">登录</view>
      
      <view class="footer">
        <text class="link" @click="goRegister">注册账号</text>
        <text class="divider">|</text>
        <text class="link" @click="goElderlyMode">长辈模式</text>
      </view>
    </view>
    
    <view class="tips">
      <text class="tip-text">温馨提示：默认密码 123456</text>
    </view>
  </view>
</template>

<script>
import { authApi, testApi } from '@/utils/api.js'

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      showPassword: false
    }
  },
  
  methods: {
    async handleLogin() {
      if (!this.form.username) {
        uni.showToast({ title: '请输入用户名', icon: 'none' })
        return
      }
      if (!this.form.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
      }
      
      uni.showLoading({ title: '登录中...' })
      
      try {
        const res = await authApi.login(this.form)
        uni.setStorageSync('token', res.data.token)
        uni.setStorageSync('userInfo', res.data)
        
        uni.hideLoading()
        uni.showToast({ title: '登录成功', icon: 'success' })
        
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 500)
      } catch (e) {
        uni.hideLoading()
      }
    },
    
    quickLogin(username) {
      this.form.username = username
      this.form.password = '123456'
      this.handleLogin()
    },
    
    goRegister() {
      uni.navigateTo({ url: '/pages/register/register' })
    },
    
    goElderlyMode() {
      uni.navigateTo({ url: '/pages/elderly/elderly' })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #1890ff 0%, #e6f7ff 100%);
  padding: 100rpx 40rpx 40rpx;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(24, 144, 255, 0.3);
}

.logo-icon {
  font-size: 80rpx;
}

.app-name {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.app-desc {
  display: block;
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-form {
  padding: 48rpx;
}

.form-item {
  position: relative;
  margin-bottom: 40rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.input {
  width: 100%;
  height: 88rpx;
  padding: 0 24rpx;
  border: 2rpx solid #d9d9d9;
  border-radius: 12rpx;
  font-size: 30rpx;
  background: #fafafa;
}

.input:focus {
  border-color: #1890ff;
  background: #fff;
}

.toggle-pwd {
  position: absolute;
  right: 24rpx;
  bottom: 26rpx;
  font-size: 36rpx;
}

.quick-login {
  display: flex;
  justify-content: center;
  gap: 40rpx;
  margin-bottom: 40rpx;
}

.quick-btn {
  font-size: 26rpx;
  color: #1890ff;
}

.footer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 40rpx;
  gap: 20rpx;
}

.link {
  font-size: 28rpx;
  color: #1890ff;
}

.divider {
  color: #d9d9d9;
}

.tips {
  text-align: center;
  margin-top: 40rpx;
}

.tip-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
}
</style>
