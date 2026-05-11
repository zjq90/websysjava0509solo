<template>
  <view class="register-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="form-card">
      <view class="form-header">
        <text class="title">用户注册</text>
        <text class="subtitle">注册账号，享受便捷就医服务</text>
      </view>

      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" v-model="form.username" placeholder="请输入用户名" />
      </view>

      <view class="form-item">
        <text class="label">真实姓名</text>
        <input class="input" v-model="form.realName" placeholder="请输入真实姓名" />
      </view>

      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" v-model="form.phone" type="number" placeholder="请输入手机号" maxlength="11" />
      </view>

      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" v-model="form.password" type="password" placeholder="请输入密码" />
      </view>

      <view class="form-item">
        <text class="label">确认密码</text>
        <input class="input" v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" />
      </view>

      <view class="btn-primary register-btn" @click="handleRegister">
        <text>立即注册</text>
      </view>

      <view class="tips">
        <text class="tip-text">注册即表示同意《用户协议》和《隐私政策》</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { authApi } from '@/utils/api'

const store = useStore()

const form = reactive({
  username: '',
  realName: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

const elderMode = computed(() => store.getters.elderMode)

const validateForm = () => {
  if (!form.username || form.username.length < 4) {
    uni.showToast({ title: '用户名至少4位', icon: 'none' })
    return false
  }
  if (!form.realName) {
    uni.showToast({ title: '请输入真实姓名', icon: 'none' })
    return false
  }
  if (!form.phone || form.phone.length !== 11) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return false
  }
  if (!form.password || form.password.length < 6) {
    uni.showToast({ title: '密码至少6位', icon: 'none' })
    return false
  }
  if (form.password !== form.confirmPassword) {
    uni.showToast({ title: '两次密码不一致', icon: 'none' })
    return false
  }
  return true
}

const handleRegister = async () => {
  if (!validateForm()) return

  try {
    await authApi.register({
      username: form.username,
      password: form.password,
      realName: form.realName,
      phone: form.phone
    })

    uni.showToast({ title: '注册成功', icon: 'success' })
    
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (e) {
    console.error('注册失败:', e)
  }
}
</script>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 40rpx;
}

.form-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
}

.form-header {
  text-align: center;
  margin-bottom: 50rpx;
}

.title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 16rpx;
}

.subtitle {
  font-size: 26rpx;
  color: #999;
}

.form-item {
  margin-bottom: 36rpx;
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

.register-btn {
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: bold;
  margin-top: 40rpx;
}

.tips {
  margin-top: 30rpx;
  text-align: center;
}

.tip-text {
  font-size: 24rpx;
  color: #999;
}

.elder-mode-enabled {
  .form-card {
    padding: 60rpx 50rpx;
  }
  
  .title {
    font-size: 48rpx;
  }
  
  .label {
    font-size: 32rpx;
  }
  
  .input {
    font-size: 34rpx;
    height: 100rpx;
  }
  
  .register-btn {
    height: 100rpx;
    line-height: 100rpx;
    font-size: 36rpx;
  }
}
</style>
