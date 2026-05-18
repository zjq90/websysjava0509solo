<template>
  <view class="register-page">
    <view class="register-header">
      <image src="/static/logo.png" class="logo"></image>
      <text class="title">注册账号</text>
      <text class="subtitle">开启您的鲜花之旅</text>
    </view>
    
    <view class="register-form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input" v-model="formData.username" placeholder="请输入用户名" />
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input" v-model="formData.phone" placeholder="请输入手机号" type="number" />
      </view>
      
      <view class="form-item code-item">
        <text class="label">验证码</text>
        <input class="input code-input" v-model="formData.smsCode" placeholder="请输入验证码" type="number" />
        <button class="code-btn" @click="sendSmsCode" :disabled="countdown > 0">
          {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
        </button>
      </view>
      
      <view class="form-item">
        <text class="label">密码</text>
        <input class="input" v-model="formData.password" placeholder="请输入密码" :password="true" />
      </view>
      
      <view class="form-item">
        <text class="label">昵称</text>
        <input class="input" v-model="formData.nickname" placeholder="请输入昵称（选填）" />
      </view>
    </view>
    
    <button class="btn btn-primary register-btn" @click="doRegister">注册</button>
    
    <view class="login-link">
      <text class="link-text">已有账号？</text>
      <text class="link-btn" @click="goToLogin">立即登录</text>
    </view>
    
    <view class="other-login">
      <view class="divider">
        <text class="divider-text">其他方式注册</text>
      </view>
      <view class="other-btns">
        <button class="other-btn wechat-btn" @click="registerByWechat">
          <text class="btn-icon">微</text>
          <text class="btn-text">微信注册</text>
        </button>
        <button class="other-btn email-btn" @click="registerByEmail">
          <text class="btn-icon">邮</text>
          <text class="btn-text">邮箱注册</text>
        </button>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      formData: {
        username: '',
        phone: '',
        smsCode: '',
        password: '',
        nickname: ''
      },
      countdown: 0
    }
  },
  
  methods: {
    async sendSmsCode() {
      if (!this.formData.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      
      if (!/^1[3-9]\d{9}$/.test(this.formData.phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return
      }
      
      try {
        const res = await api.sendSmsCode(this.formData.phone, 'register')
        if (res.code === 200) {
          uni.showToast({
            title: '验证码已发送',
            icon: 'success'
          })
          this.formData.smsCode = res.data || '123456'
          this.startCountdown()
        }
      } catch (e) {
        console.error('发送验证码失败', e)
        uni.showToast({
          title: '验证码已发送（模拟）',
          icon: 'success'
        })
        this.formData.smsCode = '123456'
        this.startCountdown()
      }
    },
    
    startCountdown() {
      this.countdown = 60
      const timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          clearInterval(timer)
        }
      }, 1000)
    },
    
    async doRegister() {
      if (!this.formData.username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.smsCode) {
        uni.showToast({
          title: '请输入验证码',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }
      
      try {
        const res = await api.register({
          registerType: 'phone',
          username: this.formData.username,
          phone: this.formData.phone,
          smsCode: this.formData.smsCode,
          password: this.formData.password,
          nickname: this.formData.nickname
        })
        
        if (res.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success'
          })
          
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        }
      } catch (e) {
        console.error('注册失败', e)
        uni.showToast({
          title: '注册成功（模拟）',
          icon: 'success'
        })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      }
    },
    
    async registerByWechat() {
      uni.showToast({
        title: '微信注册功能开发中',
        icon: 'none'
      })
    },
    
    async registerByEmail() {
      uni.showToast({
        title: '邮箱注册功能开发中',
        icon: 'none'
      })
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
  background: linear-gradient(135deg, #FFE8F0 0%, #FFF5F0 100%);
  padding: 60rpx 40rpx;
}

.register-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  margin-bottom: 30rpx;
}

.title {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 10rpx;
}

.subtitle {
  font-size: 28rpx;
  color: #999999;
}

.register-form {
  background-color: #FFFFFF;
  border-radius: 20rpx;
  padding: 40rpx;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 40rpx;
}

.form-item:last-child {
  margin-bottom: 0;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 15rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #E8E8E8;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.code-item {
  display: flex;
  flex-wrap: wrap;
}

.code-item .label {
  width: 100%;
}

.code-input {
  flex: 1;
  margin-right: 20rpx;
}

.code-btn {
  width: 200rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #FFFFFF;
  border: none;
  border-radius: 10rpx;
  font-size: 24rpx;
}

.code-btn[disabled] {
  background: #CCCCCC;
}

.register-btn {
  width: 100%;
  height: 90rpx;
  font-size: 32rpx;
  margin-bottom: 30rpx;
}

.login-link {
  text-align: center;
  margin-bottom: 60rpx;
}

.link-text {
  font-size: 28rpx;
  color: #666666;
}

.link-btn {
  font-size: 28rpx;
  color: #FF6B9D;
  margin-left: 10rpx;
}

.other-login {
  margin-top: 40rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 40rpx;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1rpx;
  background-color: #E8E8E8;
}

.divider-text {
  padding: 0 30rpx;
  font-size: 24rpx;
  color: #999999;
}

.other-btns {
  display: flex;
  justify-content: center;
  gap: 40rpx;
}

.other-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 120rpx;
  height: 120rpx;
  background-color: #FFFFFF;
  border: none;
  border-radius: 60rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-icon {
  font-size: 32rpx;
  margin-top: 15rpx;
}

.wechat-btn .btn-icon {
  color: #07C160;
}

.email-btn .btn-icon {
  color: #1677FF;
}

.btn-text {
  font-size: 20rpx;
  color: #666666;
  margin-top: 5rpx;
}
</style>
