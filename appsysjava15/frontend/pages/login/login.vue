<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-icon">🌱</text>
      </view>
      <text class="app-title">生产批次管理系统</text>
      <text class="app-subtitle">智慧生产 · 质量保障</text>
    </view>
    
    <view class="login-form">
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">👤</text>
          <input 
            class="form-input" 
            type="text" 
            v-model="form.username" 
            placeholder="请输入用户名" 
            placeholder-class="input-placeholder"
          />
        </view>
      </view>
      
      <view class="form-item">
        <view class="input-wrapper">
          <text class="input-icon">🔒</text>
          <input 
            class="form-input" 
            type="password" 
            v-model="form.password" 
            placeholder="请输入密码" 
            placeholder-class="input-placeholder"
          />
        </view>
      </view>
      
      <view class="btn-login" @click="handleLogin">
        <text>登 录</text>
      </view>
      
      <view class="demo-accounts">
        <text class="demo-title">测试账号：</text>
        <view class="demo-list">
          <view class="demo-item" @click="quickLogin('admin', '123456')">
            <text class="demo-role">管理员</text>
            <text class="demo-account">admin/123456</text>
          </view>
          <view class="demo-item" @click="quickLogin('supervisor', '123456')">
            <text class="demo-role">主管</text>
            <text class="demo-account">supervisor/123456</text>
          </view>
          <view class="demo-item" @click="quickLogin('operator1', '123456')">
            <text class="demo-role">操作员</text>
            <text class="demo-account">operator1/123456</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    handleLogin() {
      if (!this.form.username || !this.form.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }
      this.doLogin()
    },
    quickLogin(username, password) {
      this.form.username = username
      this.form.password = password
      this.doLogin()
    },
    async doLogin() {
      uni.showLoading({
        title: '登录中...'
      })
      try {
        const res = await api.getUsers()
        const users = res.data || []
        const user = users.find(u => u.username === this.form.username && u.password === this.form.password && u.enabled)
        if (user) {
          uni.setStorageSync('userInfo', user)
          uni.hideLoading()
          uni.showToast({
            title: '登录成功',
            icon: 'success'
          })
          setTimeout(() => {
            uni.switchTab({
              url: '/pages/index/index'
            })
          }, 500)
        } else {
          uni.hideLoading()
          uni.showToast({
            title: '用户名或密码错误',
            icon: 'none'
          })
        }
      } catch (e) {
        uni.hideLoading()
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #409EFF 0%, #67C23A 100%);
  display: flex;
  flex-direction: column;
  padding: 0;
}

.login-header {
  padding-top: 120rpx;
  padding-bottom: 80rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
}

.logo-icon {
  font-size: 80rpx;
}

.app-title {
  font-size: 44rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 16rpx;
}

.app-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.85);
}

.login-form {
  flex: 1;
  background-color: #ffffff;
  border-radius: 40rpx 40rpx 0 0;
  padding: 50rpx 40rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  height: 96rpx;
}

.input-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}

.form-input {
  flex: 1;
  font-size: 30rpx;
  color: #333333;
}

.input-placeholder {
  color: #c0c4cc;
}

.btn-login {
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
  color: #ffffff;
  border-radius: 48rpx;
  height: 96rpx;
  line-height: 96rpx;
  text-align: center;
  font-size: 32rpx;
  font-weight: 500;
  margin-top: 40rpx;
}

.btn-login:active {
  opacity: 0.85;
}

.demo-accounts {
  margin-top: 50rpx;
}

.demo-title {
  font-size: 26rpx;
  color: #909399;
  margin-bottom: 20rpx;
  display: block;
}

.demo-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.demo-item {
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 16rpx 24rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.demo-role {
  font-size: 24rpx;
  color: #606266;
  margin-bottom: 6rpx;
}

.demo-account {
  font-size: 22rpx;
  color: #909399;
}
</style>
