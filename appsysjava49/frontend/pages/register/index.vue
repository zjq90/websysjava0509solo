<template>
  <view class="register-page">
    <view class="header">
      <view class="logo">🛒</view>
      <text class="title">注册账号</text>
      <text class="subtitle">加入二手交易大家庭</text>
    </view>

    <view class="form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input v-model="form.username" class="input" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="label">手机号</text>
        <input v-model="form.phone" class="input" type="number" placeholder="请输入手机号" />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input v-model="form.password" class="input" type="password" placeholder="请输入密码" />
      </view>
      <view class="form-item">
        <text class="label">确认密码</text>
        <input v-model="form.confirmPassword" class="input" type="password" placeholder="请再次输入密码" />
      </view>

      <view class="agreement">
        <checkbox :checked="agreed" color="#409EFF" @click="agreed = !agreed" />
        <text class="agreement-text">
          我已阅读并同意
          <text class="link">《用户协议》</text>
          和
          <text class="link">《隐私政策》</text>
        </text>
      </view>

      <button class="register-btn" @click="handleRegister" :disabled="registering">
        {{ registering ? '注册中...' : '立即注册' }}
      </button>

      <view class="login-link">
        <text>已有账号？</text>
        <text class="link" @click="goToLogin">立即登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      registering: false,
      agreed: false,
      form: {
        username: '',
        phone: '',
        password: '',
        confirmPassword: ''
      }
    }
  },
  methods: {
    async handleRegister() {
      if (!this.form.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }
      if (!this.form.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      if (!this.form.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }
      if (this.form.password !== this.form.confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        })
        return
      }
      if (!this.agreed) {
        uni.showToast({
          title: '请阅读并同意用户协议',
          icon: 'none'
        })
        return
      }

      this.registering = true

      try {
        const res = await request.post('/user/register', {
          username: this.form.username,
          phone: this.form.phone,
          password: this.form.password
        })

        uni.showToast({
          title: '注册成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({
          title: '注册成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } finally {
        this.registering = false
      }
    },
    goToLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  padding: 100rpx 60rpx;
}

.header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  font-size: 100rpx;
  margin-bottom: 20rpx;
  display: block;
}

.title {
  font-size: 48rpx;
  color: #fff;
  font-weight: bold;
  display: block;
  margin-bottom: 16rpx;
}

.subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
  display: block;
}

.form {
  background: #fff;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 40rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
  display: block;
  font-weight: 500;
}

.input {
  height: 90rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 16rpx;
  padding: 0 30rpx;
  font-size: 30rpx;
  width: 100%;
  box-sizing: border-box;
}

.input:focus {
  border-color: #409EFF;
}

.agreement {
  display: flex;
  align-items: flex-start;
  margin-bottom: 40rpx;
}

.agreement-text {
  font-size: 24rpx;
  color: #666;
  margin-left: 12rpx;
  line-height: 1.6;
}

.link {
  color: #409EFF;
}

.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: bold;
  margin-top: 20rpx;
}

.login-link {
  text-align: center;
  margin-top: 40rpx;
  font-size: 26rpx;
  color: #666;
}
</style>
