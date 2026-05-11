<template>
  <view class="login-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="login-header">
      <view class="logo">
        <text class="logo-icon">🏥</text>
        <text class="logo-text">智慧医院</text>
      </view>
      <text class="slogan">便捷预约，健康相伴</text>
    </view>

    <view class="login-form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input 
          class="input" 
          v-model="username" 
          placeholder="请输入用户名"
          :disabled="loading"
        />
      </view>

      <view class="form-item">
        <text class="label">密码</text>
        <input 
          class="input" 
          v-model="password" 
          type="password"
          placeholder="请输入密码"
          :disabled="loading"
        />
      </view>

      <view class="form-item">
        <view class="elder-toggle" @click="toggleElderMode">
          <text class="elder-label">长辈模式</text>
          <switch :checked="elderMode" color="#1890ff" @change="toggleElderMode" />
        </view>
      </view>

      <view class="btn-primary login-btn" @click="handleLogin" :class="{ disabled: loading }">
        <text v-if="!loading">登录</text>
        <text v-else>登录中...</text>
      </view>

      <view class="login-links">
        <text class="link" @click="goToRegister">注册账号</text>
        <text class="link" @click="goToForgot">忘记密码？</text>
      </view>
    </view>

    <view class="quick-login">
      <view class="quick-item" @click="quickLogin('user1')">
        <text class="quick-label">测试用户</text>
        <text class="quick-hint">user1 / 123456</text>
      </view>
      <view class="quick-item" @click="quickLogin('admin')">
        <text class="quick-label">管理员</text>
        <text class="quick-hint">admin / 123456</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { authApi } from '@/utils/api'

const store = useStore()
const username = ref('')
const password = ref('')
const loading = ref(false)

const elderMode = computed(() => store.getters.elderMode)

const toggleElderMode = () => {
  store.dispatch('toggleElderMode')
}

const quickLogin = (user) => {
  username.value = user
  password.value = '123456'
  handleLogin()
}

const handleLogin = async () => {
  if (!username.value || !password.value) {
    uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const data = await authApi.login({
      username: username.value,
      password: password.value
    })
    
    store.dispatch('login', data)
    
    uni.showToast({ title: '登录成功', icon: 'success' })
    
    setTimeout(() => {
      uni.switchTab({ url: '/pages/index/index' })
    }, 1000)
  } catch (e) {
    console.error('登录失败:', e)
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  uni.navigateTo({ url: '/pages/register/register' })
}

const goToForgot = () => {
  uni.showToast({ title: '请联系管理员', icon: 'none' })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #1890ff 0%, #1890ff 30%, #f5f5f5 30%);
  padding: 0 40rpx;
}

.login-header {
  padding: 100rpx 0 80rpx;
  text-align: center;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.logo-icon {
  font-size: 80rpx;
  margin-right: 20rpx;
}

.logo-text {
  font-size: 56rpx;
  font-weight: bold;
  color: #fff;
}

.slogan {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-form {
  background: #fff;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 40rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 16rpx;
}

.input {
  width: 100%;
  height: 88rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 30rpx;
}

.elder-toggle {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
}

.elder-label {
  font-size: 28rpx;
  color: #666;
}

.login-btn {
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.login-btn.disabled {
  opacity: 0.6;
}

.login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 40rpx;
}

.link {
  font-size: 26rpx;
  color: #1890ff;
}

.quick-login {
  margin-top: 60rpx;
  display: flex;
  justify-content: space-around;
}

.quick-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx 40rpx;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.quick-label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.quick-hint {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.elder-mode-enabled {
  .login-form {
    padding: 60rpx 50rpx;
  }
  
  .label {
    font-size: 32rpx;
  }
  
  .input {
    font-size: 34rpx;
    height: 100rpx;
  }
  
  .login-btn {
    height: 100rpx;
    line-height: 100rpx;
    font-size: 36rpx;
  }
  
  .logo-text {
    font-size: 64rpx;
  }
  
  .quick-item {
    padding: 32rpx 50rpx;
  }
}
</style>
