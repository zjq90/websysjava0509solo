<template>
  <view class="login-container">
    <view class="logo-section">
      <view class="logo-icon">🏥</view>
      <text class="logo-text">宠物医院问诊系统</text>
    </view>
    
    <view class="form-section card">
      <view class="input-group">
        <text class="input-label">用户名</text>
        <input class="input-box" v-model="form.username" placeholder="请输入用户名" />
      </view>
      
      <view class="input-group">
        <text class="input-label">密码</text>
        <input class="input-box" v-model="form.password" type="password" placeholder="请输入密码" />
      </view>
      
      <view class="btn btn-primary" @click="handleLogin">登录</view>
      
      <view class="elder-mode-toggle" @click="toggleElderMode">
        <text class="toggle-text">👴 长辈模式：{{ elderMode ? '已开启' : '已关闭' }}</text>
      </view>
    </view>
    
    <view class="tips">
      <text>测试账号：doctor1 / 123456</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../store/index.js'
import userApi from '../../api/user.js'

const userStore = useUserStore()

const form = ref({
  username: 'doctor1',
  password: '123456'
})

const elderMode = ref(userStore.elderMode)

const toggleElderMode = () => {
  elderMode.value = !elderMode.value
  userStore.setElderMode(elderMode.value)
}

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
    return
  }
  
  try {
    uni.showLoading({ title: '登录中...' })
    const res = await userApi.login(form.value)
    uni.hideLoading()
    
    userStore.setUserInfo(res)
    userStore.setToken('token')
    
    uni.showToast({ title: '登录成功', icon: 'success' })
    
    setTimeout(() => {
      uni.switchTab({ url: '/pages/index/index' })
    }, 1000)
  } catch (e) {
    uni.hideLoading()
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  padding: 100rpx 40rpx;
}

.logo-section {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo-icon {
  font-size: 120rpx;
  margin-bottom: 20rpx;
}

.logo-text {
  font-size: 36rpx;
  font-weight: bold;
  color: #303133;
}

.form-section {
  margin-bottom: 40rpx;
}

.elder-mode-toggle {
  text-align: center;
  margin-top: 30rpx;
  padding: 20rpx;
}

.toggle-text {
  font-size: 28rpx;
  color: #409EFF;
}

.tips {
  text-align: center;
  font-size: 24rpx;
  color: #909399;
}
</style>
