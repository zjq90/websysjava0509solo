<template>
  <view class="register-container">
    <view class="register-header">
      <text class="title">注册账号</text>
      <text class="subtitle">加入绿色出行，开启便捷生活</text>
    </view>

    <view class="register-form">
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
        <text class="label">验证码</text>
        <view class="code-input-group">
          <input 
            class="input code-input" 
            type="number" 
            v-model="form.code" 
            placeholder="请输入验证码"
            maxlength="6"
          />
          <button 
            class="code-btn" 
            :disabled="countdown > 0"
            @click="sendCode"
          >
            {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
          </button>
        </view>
      </view>

      <view class="form-group">
        <text class="label">设置密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="form.password" 
          placeholder="请设置6-20位密码"
          password
        />
      </view>

      <view class="form-group">
        <text class="label">确认密码</text>
        <input 
          class="input" 
          type="password" 
          v-model="form.confirmPassword" 
          placeholder="请再次输入密码"
          password
        />
      </view>

      <view class="form-group">
        <text class="label">昵称</text>
        <input 
          class="input" 
          v-model="form.nickname" 
          placeholder="请输入昵称"
        />
      </view>

      <view class="agreement">
        <checkbox :checked="agreed" @click="agreed = !agreed" color="#00A862" />
        <text class="agreement-text">
          我已阅读并同意
          <text class="link">《用户协议》</text>
          和
          <text class="link">《隐私政策》</text>
        </text>
      </view>

      <button class="register-btn" :disabled="loading" @click="handleRegister">
        {{ loading ? '注册中...' : '注册' }}
      </button>

      <view class="login-link">
        <text class="link-text">已有账号？</text>
        <text class="link-btn" @click="goToLogin">立即登录</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '../../stores/user'

const userStore = useUserStore()

const loading = ref(false)
const countdown = ref(0)
const agreed = ref(false)
const form = ref({
  phone: '',
  code: '',
  password: '',
  confirmPassword: '',
  nickname: ''
})

const sendCode = () => {
  if (!form.value.phone || form.value.phone.length !== 11) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }
  
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer)
    }
  }, 1000)
  
  uni.showToast({ title: '验证码已发送', icon: 'success' })
}

const handleRegister = async () => {
  if (!form.value.phone) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }
  if (!form.value.code) {
    uni.showToast({ title: '请输入验证码', icon: 'none' })
    return
  }
  if (!form.value.password) {
    uni.showToast({ title: '请设置密码', icon: 'none' })
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return
  }
  if (!agreed.value) {
    uni.showToast({ title: '请阅读并同意用户协议', icon: 'none' })
    return
  }

  loading.value = true
  try {
    const res = await userStore.doRegister({
      phone: form.value.phone,
      password: form.value.password,
      nickname: form.value.nickname
    })
    if (res.code === 200) {
      uni.showToast({ title: '注册成功', icon: 'success' })
      setTimeout(() => {
        uni.navigateBack()
      }, 1000)
    } else {
      uni.showToast({ title: res.message || '注册失败', icon: 'none' })
    }
  } catch (error) {
    uni.showToast({ title: '注册失败，请重试', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.register-header {
  background: linear-gradient(180deg, #00A862 0%, #00c874 100%);
  padding: 60px 24px 40px;
}

.title {
  display: block;
  font-size: 28px;
  font-weight: bold;
  color: white;
  margin-bottom: 8px;
}

.subtitle {
  font-size: 14px;
  color: rgba(255,255,255,0.8);
}

.register-form {
  background: white;
  margin: -20px 16px 0;
  border-radius: 16px;
  padding: 24px;
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

.code-input-group {
  display: flex;
  gap: 12px;
}

.code-input {
  flex: 1;
}

.code-btn {
  width: 120px;
  height: 48px;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  flex-shrink: 0;

  &:disabled {
    background: #ccc;
  }
}

.agreement {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-bottom: 24px;
}

.agreement-text {
  flex: 1;
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}

.link {
  color: #00A862;
}

.register-btn {
  width: 100%;
  height: 52px;
  background: linear-gradient(90deg, #00A862 0%, #00c874 100%);
  color: white;
  border: none;
  border-radius: 26px;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 24px;

  &:disabled {
    opacity: 0.6;
  }
}

.login-link {
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
