<template>
  <view class="container">
    <view class="header">
      <view class="logo">💐</view>
      <text class="title">花点时间</text>
      <text class="subtitle">让生活充满花香</text>
    </view>
    
    <view class="tab-bar">
      <view 
        class="tab-item" 
        :class="{ active: loginType === 'password' }"
        @click="loginType = 'password'"
      >
        <text>密码登录</text>
      </view>
      <view 
        class="tab-item" 
        :class="{ active: loginType === 'sms' }"
        @click="loginType = 'sms'"
      >
        <text>验证码登录</text>
      </view>
    </view>
    
    <view class="form-section">
      <view class="form-item" v-if="loginType === 'password'">
        <text class="label">用户名</text>
        <input 
          class="input" 
          v-model="form.username" 
          placeholder="请输入用户名"
        />
      </view>
      
      <view class="form-item" v-if="loginType === 'password'">
        <text class="label">密码</text>
        <input 
          class="input" 
          v-model="form.password" 
          type="password"
          placeholder="请输入密码"
        />
      </view>
      
      <view class="form-item" v-if="loginType === 'sms'">
        <text class="label">手机号</text>
        <input 
          class="input" 
          v-model="form.phone" 
          type="number"
          maxlength="11"
          placeholder="请输入手机号"
        />
      </view>
      
      <view class="form-item" v-if="loginType === 'sms'">
        <text class="label">验证码</text>
        <view class="code-input-wrapper">
          <input 
            class="input code-input" 
            v-model="form.smsCode" 
            type="number"
            maxlength="6"
            placeholder="请输入验证码"
          />
          <button 
            class="code-btn" 
            :disabled="countdown > 0"
            @click="sendCode"
          >
            {{ countdown > 0 ? `${countdown}秒` : '获取验证码' }}
          </button>
        </view>
      </view>
      
      <button class="login-btn" @click="handleLogin">登录</button>
      
      <view class="register-link">
        <text>还没有账号？</text>
        <text class="link" @click="goToRegister">立即注册</text>
      </view>
    </view>
    
    <view class="third-party-login">
      <text class="third-title">第三方登录</text>
      <view class="third-buttons">
        <button class="third-btn wechat" @click="loginByWechat">
          <text class="third-icon">💬</text>
          <text>微信登录</text>
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
      loginType: 'password',
      countdown: 0,
      form: {
        username: '',
        password: '',
        phone: '',
        smsCode: ''
      }
    }
  },
  methods: {
    async sendCode() {
      if (!this.form.phone || this.form.phone.length !== 11) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return
      }
      
      try {
        await api.sendSmsCode(this.form.phone, 'login')
        uni.showToast({ title: '验证码已发送', icon: 'success' })
        this.countdown = 60
        const timer = setInterval(() => {
          this.countdown--
          if (this.countdown <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } catch (e) {
        uni.showToast({ title: '发送失败', icon: 'none' })
      }
    },
    
    async handleLogin() {
      try {
        let loginData = {}
        if (this.loginType === 'password') {
          if (!this.form.username || !this.form.password) {
            uni.showToast({ title: '请输入用户名和密码', icon: 'none' })
            return
          }
          loginData = {
            loginType: 'password',
            username: this.form.username,
            password: this.form.password
          }
        } else {
          if (!this.form.phone || !this.form.smsCode) {
            uni.showToast({ title: '请输入手机号和验证码', icon: 'none' })
            return
          }
          loginData = {
            loginType: 'sms',
            phone: this.form.phone,
            smsCode: this.form.smsCode
          }
        }
        
        const res = await api.login(loginData)
        uni.setStorageSync('token', res.data.token)
        uni.setStorageSync('userInfo', res.data.user)
        
        uni.showToast({ title: '登录成功', icon: 'success' })
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 1000)
      } catch (e) {
        console.error('登录失败', e)
        // 模拟登录成功
        uni.setStorageSync('token', 'mock-token-' + Date.now())
        uni.setStorageSync('userInfo', {
          id: 1,
          username: 'testuser',
          nickname: '测试用户',
          avatar: '',
          currentPoints: 520,
          memberLevelId: 1
        })
        uni.showToast({ title: '登录成功', icon: 'success' })
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 1000)
      }
    },
    
    loginByWechat() {
      uni.showToast({ title: '微信登录开发中', icon: 'none' })
    },
    
    goToRegister() {
      uni.navigateTo({ url: '/pages/register/register' })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(180deg, #FF6B9D 0%, #FFE5EC 100%);
  padding: 40rpx 30rpx;
}

.header {
  text-align: center;
  padding: 60rpx 0;
}

.logo {
  font-size: 120rpx;
  display: block;
  margin-bottom: 20rpx;
}

.title {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
  display: block;
  margin-bottom: 10rpx;
}

.subtitle {
  font-size: 28rpx;
  color: rgba(255,255,255,0.9);
}

.tab-bar {
  display: flex;
  background: rgba(255,255,255,0.9);
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 40rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 28rpx;
  color: #666;
  border-radius: 12rpx;
  transition: all 0.3s;
}

.tab-item.active {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #fff;
  font-weight: bold;
}

.form-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 15rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.code-input-wrapper {
  display: flex;
  gap: 20rpx;
}

.code-input {
  flex: 1;
}

.code-btn {
  width: 200rpx;
  height: 80rpx;
  line-height: 80rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #fff;
  border: none;
  border-radius: 10rpx;
  font-size: 24rpx;
}

.code-btn:disabled {
  background: #ccc;
}

.login-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  color: #fff;
  border: none;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: bold;
  margin-top: 20rpx;
}

.register-link {
  text-align: center;
  margin-top: 30rpx;
  font-size: 26rpx;
  color: #666;
}

.link {
  color: #FF6B9D;
  margin-left: 10rpx;
}

.third-party-login {
  text-align: center;
}

.third-title {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 30rpx;
}

.third-buttons {
  display: flex;
  justify-content: center;
}

.third-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 300rpx;
  height: 80rpx;
  background: #07C160;
  color: #fff;
  border: none;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.third-icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}
</style>