<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-icon">🚲</text>
      </view>
      <text class="title">共享单车运维</text>
      <text class="subtitle">运维人员登录</text>
    </view>
    
    <view class="login-form">
      <view class="form-item">
        <text class="label">手机号</text>
        <input 
          class="input" 
          v-model="phone" 
          type="number" 
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input 
          class="input" 
          v-model="password" 
          type="password" 
          placeholder="请输入密码"
          password
        />
      </view>
      
      <button class="login-btn" @click="handleLogin" :loading="loading">
        登 录
      </button>
      
      <view class="tips">
        <text>测试账号: 13800138001</text>
        <text>密码: 123456</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'
import { api } from '@/api'

const store = useStore()
const phone = ref('13800138001')
const password = ref('123456')
const loading = ref(false)

const handleLogin = async () => {
  if (!phone.value) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  if (!password.value) {
    uni.showToast({ title: '请输入密码', icon: 'none' })
    return
  }
  
  loading.value = true
  try {
    const res = await api.login(phone.value, password.value)
    store.dispatch('login', res)
    uni.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => {
      uni.switchTab({ url: '/pages/tasks/tasks' })
    }, 1000)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 100rpx 40rpx;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
  
  .logo {
    width: 160rpx;
    height: 160rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    margin: 0 auto 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    
    .logo-icon {
      font-size: 80rpx;
    }
  }
  
  .title {
    display: block;
    font-size: 48rpx;
    color: #fff;
    font-weight: bold;
    margin-bottom: 16rpx;
  }
  
  .subtitle {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
  }
}

.login-form {
  background: #fff;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  
  .form-item {
    margin-bottom: 40rpx;
    
    .label {
      display: block;
      font-size: 28rpx;
      color: #666;
      margin-bottom: 16rpx;
    }
    
    .input {
      width: 100%;
      height: 88rpx;
      border: 2rpx solid #eee;
      border-radius: 12rpx;
      padding: 0 24rpx;
      font-size: 30rpx;
      box-sizing: border-box;
    }
  }
  
  .login-btn {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    border: none;
    border-radius: 12rpx;
    font-size: 32rpx;
    font-weight: bold;
  }
  
  .tips {
    margin-top: 40rpx;
    text-align: center;
    
    text {
      display: block;
      font-size: 24rpx;
      color: #999;
      line-height: 1.8;
    }
  }
}
</style>
