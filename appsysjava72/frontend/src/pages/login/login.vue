<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo">
        <text class="logo-text">社团管理</text>
      </view>
      <text class="subtitle">大学生社团管理系统</text>
    </view>
    
    <view class="login-content">
      <view class="login-tabs">
        <view 
          class="tab-item" 
          :class="{ active: loginType === 'phone' }"
          @click="loginType = 'phone'"
        >
          手机号登录
        </view>
        <view 
          class="tab-item" 
          :class="{ active: loginType === 'password' }"
          @click="loginType = 'password'"
        >
          账号密码
        </view>
      </view>
      
      <view v-if="loginType === 'phone'" class="form-group">
        <view class="input-item">
          <text class="input-icon">📱</text>
          <input 
            class="input-field" 
            type="number" 
            placeholder="请输入手机号" 
            v-model="phone"
            maxlength="11"
          />
        </view>
        <view class="input-item">
          <text class="input-icon">🔐</text>
          <input 
            class="input-field" 
            type="number" 
            placeholder="请输入验证码" 
            v-model="code"
            maxlength="6"
          />
          <view 
            class="code-btn" 
            :class="{ disabled: countdown > 0 }"
            @click="sendCode"
          >
            {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
          </view>
        </view>
      </view>
      
      <view v-if="loginType === 'password'" class="form-group">
        <view class="input-item">
          <text class="input-icon">👤</text>
          <input 
            class="input-field" 
            placeholder="请输入用户名/手机号" 
            v-model="username"
          />
        </view>
        <view class="input-item">
          <text class="input-icon">🔒</text>
          <input 
            class="input-field" 
            :password="showPassword" 
            placeholder="请输入密码" 
            v-model="password"
          />
          <text class="eye-icon" @click="showPassword = !showPassword">
            {{ showPassword ? '🙈' : '👁️' }}
          </text>
        </view>
      </view>
      
      <button class="login-btn" @click="handleLogin" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>
      
      <view class="register-link">
        <text>还没有账号？</text>
        <text class="link-text" @click="goRegister">立即注册</text>
      </view>
      
      <view class="third-party">
        <text class="third-title">其他登录方式</text>
        <view class="third-icons">
          <view class="third-item" @click="handleThirdLogin('wechat')">
            <text class="icon">💚</text>
            <text class="label">微信</text>
          </view>
          <view class="third-item" @click="handleThirdLogin('qq')">
            <text class="icon">🐧</text>
            <text class="label">QQ</text>
          </view>
          <view class="third-item" @click="handleThirdLogin('campus')">
            <text class="icon">🏫</text>
            <text class="label">校园认证</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { mapActions } from 'vuex'
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      loginType: 'phone',
      phone: '',
      code: '',
      username: '',
      password: '',
      showPassword: false,
      countdown: 0,
      loading: false
    }
  },
  onLoad() {
    const token = uni.getStorageSync('token')
    if (token) {
      uni.switchTab({ url: '/pages/index/index' })
    }
  },
  methods: {
    ...mapActions(['login']),
    
    async sendCode() {
      if (this.countdown > 0) return
      if (!util.validatePhone(this.phone)) {
        util.showToast('请输入正确的手机号')
        return
      }
      
      try {
        await api.sendCode(this.phone)
        util.showToast('验证码发送成功', 'success')
        this.countdown = 60
        const timer = setInterval(() => {
          this.countdown--
          if (this.countdown <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } catch (e) {
        console.error(e)
      }
    },
    
    async handleLogin() {
      let loginData = {}
      
      if (this.loginType === 'phone') {
        if (!util.validatePhone(this.phone)) {
          util.showToast('请输入正确的手机号')
          return
        }
        if (!this.code || this.code.length !== 6) {
          util.showToast('请输入6位验证码')
          return
        }
        loginData = {
          loginType: 'phone',
          phone: this.phone,
          code: this.code
        }
      } else {
        if (!this.username) {
          util.showToast('请输入用户名')
          return
        }
        if (!this.password || this.password.length < 6) {
          util.showToast('密码至少6位')
          return
        }
        loginData = {
          loginType: 'password',
          username: this.username,
          password: this.password
        }
      }
      
      this.loading = true
      try {
        await this.login(loginData)
        util.showToast('登录成功', 'success')
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 1000)
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async handleThirdLogin(type) {
      if (type === 'campus') {
        uni.showModal({
          title: '校园认证',
          content: '即将跳转至校园统一身份认证页面',
          success: async (res) => {
            if (res.confirm) {
              this.loading = true
              try {
                await this.login({
                  loginType: 'campus',
                  campusToken: 'mock_campus_token_' + Date.now()
                })
                util.showToast('校园认证登录成功', 'success')
                setTimeout(() => {
                  uni.switchTab({ url: '/pages/index/index' })
                }, 1000)
              } catch (e) {
                console.error(e)
              } finally {
                this.loading = false
              }
            }
          }
        })
      } else {
        const platform = type === 'wechat' ? '微信' : 'QQ'
        uni.showModal({
          title: platform + '登录',
          content: '即将打开' + platform + '进行授权登录',
          success: async (res) => {
            if (res.confirm) {
              this.loading = true
              try {
                const mockOpenid = type === 'wechat' ? 'wx_' + Date.now() : 'qq_' + Date.now()
                await this.login({
                  loginType: type,
                  [type === 'wechat' ? 'openid' : 'qqOpenid']: mockOpenid
                })
                util.showToast(platform + '登录成功', 'success')
                setTimeout(() => {
                  uni.switchTab({ url: '/pages/index/index' })
                }, 1000)
              } catch (e) {
                console.error(e)
              } finally {
                this.loading = false
              }
            }
          }
        })
      }
    },
    
    goRegister() {
      uni.navigateTo({ url: '/pages/register/register' })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 100rpx 40rpx;
  box-sizing: border-box;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 32rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 30rpx;
}

.logo-text {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
}

.subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-content {
  background: #fff;
  border-radius: 24rpx;
  padding: 60rpx 40rpx;
  box-shadow: 0 8rpx 40rpx rgba(0, 0, 0, 0.1);
}

.login-tabs {
  display: flex;
  margin-bottom: 60rpx;
  border-bottom: 2rpx solid #eee;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 30rpx;
  color: #999;
  position: relative;
}

.tab-item.active {
  color: #5677fc;
  font-weight: 600;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -2rpx;
  left: 50%;
  transform: translateX(-50%);
  width: 60rpx;
  height: 4rpx;
  background: #5677fc;
  border-radius: 2rpx;
}

.form-group {
  margin-bottom: 40rpx;
}

.input-item {
  display: flex;
  align-items: center;
  height: 100rpx;
  border-bottom: 2rpx solid #eee;
  position: relative;
}

.input-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.input-field {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.code-btn {
  padding: 12rpx 24rpx;
  background: #e8ecff;
  color: #5677fc;
  border-radius: 24rpx;
  font-size: 26rpx;
}

.code-btn.disabled {
  background: #f0f0f0;
  color: #999;
}

.eye-icon {
  font-size: 36rpx;
  padding: 10rpx;
}

.login-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  border-radius: 45rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  margin-top: 20rpx;
  border: none;
}

.login-btn[disabled] {
  opacity: 0.6;
}

.register-link {
  text-align: center;
  margin-top: 30rpx;
  font-size: 28rpx;
  color: #999;
}

.link-text {
  color: #5677fc;
  margin-left: 10rpx;
}

.third-party {
  margin-top: 60rpx;
  text-align: center;
}

.third-title {
  font-size: 26rpx;
  color: #999;
  position: relative;
  display: inline-block;
  padding: 0 30rpx;
}

.third-title::before,
.third-title::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 80rpx;
  height: 2rpx;
  background: #eee;
}

.third-title::before {
  left: -80rpx;
}

.third-title::after {
  right: -80rpx;
}

.third-icons {
  display: flex;
  justify-content: center;
  margin-top: 40rpx;
}

.third-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 40rpx;
}

.third-item .icon {
  font-size: 70rpx;
  margin-bottom: 10rpx;
}

.third-item .label {
  font-size: 24rpx;
  color: #666;
}
</style>
