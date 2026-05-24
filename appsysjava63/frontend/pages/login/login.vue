<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-icon">🚲</text>
        <text class="logo-text">共享单车</text>
      </view>
      <text class="slogan">绿色出行，便捷生活</text>
    </view>

    <view class="login-form">
      <view class="form-group">
        <text class="label">手机号</text>
        <input 
          class="input" 
          type="number" 
          v-model="form.phone" 
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>

      <view class="form-group">
        <text class="label">密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="form.password" 
          placeholder="请输入密码"
          password
        />
      </view>

      <view class="form-actions">
        <text class="forgot-pwd" @click="goToRegister">忘记密码？</text>
      </view>

      <button class="login-btn" :disabled="loading" @click="handleLogin">
        {{ loading ? '登录中...' : '登录' }}
      </button>

      <view class="divider">
        <text class="divider-text">其他登录方式</text>
      </view>

      <view class="third-party-login">
        <view class="third-party-item" @click="thirdPartyLogin('wechat')">
          <text class="third-party-icon wechat">💬</text>
          <text class="third-party-name">微信</text>
        </view>
        <view class="third-party-item" @click="thirdPartyLogin('alipay')">
          <text class="third-party-icon alipay">💰</text>
          <text class="third-party-name">支付宝</text>
        </view>
      </view>

      <view class="register-link">
        <text class="link-text">还没有账号？</text>
        <text class="link-btn" @click="goToRegister">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()

const loading = ref(false)
const form = ref({
  phone: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.value.phone) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  if (!form.value.password) {
    uni.showToast({ title: '请输入密码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await userStore.doLogin(form.value)
    if (res.code === 200) {
      uni.showToast({ title: '登录成功', icon: 'success' })
      setTimeout(() => {
        uni.switchTab({ url: '/pages/index/index' })
      }, 1000)
    } else {
      uni.showToast({ title: res.message || '登录失败', icon: 'none' })
    }
  } catch (error) {
    uni.showToast({ title: '登录失败，请重试', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const thirdPartyLogin = (type) => {
  uni.showToast({ title: `${type === 'wechat' ? '微信' : '支付宝'}登录开发中`, icon: 'none' })
}

const goToRegister = () => {
  uni.navigateTo({ url: '/pages/register/register' })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #00A862 0%, #00c874 100%);
  padding: 80px 32px 32px;
}

.login-header {
  text-align: center;
  margin-bottom: 60px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 16px;
}

.logo-icon {
  font-size: 48px;
}

.logo-text {
  font-size: 32px;
  font-weight: bold;
  color: white;
}

.slogan {
  font-size: 14px;
  color: rgba(255,255,255,0.8);
}

.login-form {
  background: white;
  border-radius: 24px;
  padding: 32px 24px;
}

.form-group {
  margin-bottom: 20px;
}

.label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.input {
  width: 100%;
  height: 48px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 16px;
  font-size: 16px;
  box-sizing: border-box;

  &:focus {
    border-color: #00A862;
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 24px;
}

.forgot-pwd {
  font-size: 14px;
  color: #00A862;
}

.login-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: bold;

  &:disabled {
    opacity: 0.6;
  }
}

.divider {
  text-align: center;
  margin: 32px 0 24px;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    height: 1px;
    background: #e0e0e0;
  }
}

.divider-text {
  background: white;
  padding: 0 16px;
  font-size: 14px;
  color: #999;
  position: relative;
}

.third-party-login {
  display: flex;
  justify-content: center;
  gap: 48px;
  margin-bottom: 32px;
}

.third-party-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.third-party-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.wechat {
  background: #07c160;
}

.alipay {
  background: #1677ff;
}

.third-party-name {
  font-size: 12px;
  color: #666;
}

.register-link {
  text-align: center;
}

.link-text {
  font-size: 14px;
  color: #999;
}

.link-btn {
  font-size: 14px;
  color: #00A862;
  margin-left: 4px;
}
</style>
