<template>
  <view class="register-container">
    <view class="form-card">
      <view class="form-header">
        <text class="title">用户注册</text>
        <text class="subtitle">加入社团管理系统，开启精彩校园生活</text>
      </view>
      
      <view class="form-item">
        <text class="label">手机号</text>
        <view class="input-group">
          <input 
            class="input-field" 
            type="number" 
            placeholder="请输入手机号" 
            v-model="form.phone"
            maxlength="11"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">验证码</text>
        <view class="input-group">
          <input 
            class="input-field" 
            type="number" 
            placeholder="请输入验证码" 
            v-model="form.code"
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
      
      <view class="form-item">
        <text class="label">昵称</text>
        <view class="input-group">
          <input 
            class="input-field" 
            placeholder="请输入昵称" 
            v-model="form.nickname"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">设置密码</text>
        <view class="input-group">
          <input 
            class="input-field" 
            :password="true" 
            placeholder="请设置6-16位密码" 
            v-model="form.password"
            maxlength="16"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">确认密码</text>
        <view class="input-group">
          <input 
            class="input-field" 
            :password="true" 
            placeholder="请再次输入密码" 
            v-model="form.confirmPassword"
            maxlength="16"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">学号</text>
        <view class="input-group">
          <input 
            class="input-field" 
            placeholder="请输入学号（选填）" 
            v-model="form.studentNo"
          />
        </view>
      </view>
      
      <view class="form-item">
        <text class="label">院系</text>
        <view class="input-group">
          <input 
            class="input-field" 
            placeholder="请输入院系（选填）" 
            v-model="form.college"
          />
        </view>
      </view>
      
      <button class="register-btn" @click="handleRegister" :disabled="loading">
        {{ loading ? '注册中...' : '立即注册' }}
      </button>
      
      <view class="login-link">
        <text>已有账号？</text>
        <text class="link-text" @click="goLogin">立即登录</text>
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
      form: {
        phone: '',
        code: '',
        nickname: '',
        password: '',
        confirmPassword: '',
        studentNo: '',
        college: '',
        major: '',
        grade: ''
      },
      countdown: 0,
      loading: false
    }
  },
  methods: {
    ...mapActions(['register']),
    
    async sendCode() {
      if (this.countdown > 0) return
      if (!util.validatePhone(this.form.phone)) {
        util.showToast('请输入正确的手机号')
        return
      }
      
      try {
        await api.sendCode(this.form.phone)
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
    
    async handleRegister() {
      if (!util.validatePhone(this.form.phone)) {
        util.showToast('请输入正确的手机号')
        return
      }
      if (!this.form.code || this.form.code.length !== 6) {
        util.showToast('请输入6位验证码')
        return
      }
      if (!this.form.nickname) {
        util.showToast('请输入昵称')
        return
      }
      if (!this.form.password || this.form.password.length < 6) {
        util.showToast('密码至少6位')
        return
      }
      if (this.form.password !== this.form.confirmPassword) {
        util.showToast('两次输入的密码不一致')
        return
      }
      
      this.loading = true
      try {
        await this.register(this.form)
        util.showToast('注册成功', 'success')
        setTimeout(() => {
          uni.switchTab({ url: '/pages/index/index' })
        }, 1000)
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    goLogin() {
      uni.navigateBack()
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 60rpx 40rpx;
  box-sizing: border-box;
}

.form-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  box-shadow: 0 8rpx 40rpx rgba(0, 0, 0, 0.1);
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
  margin-bottom: 15rpx;
}

.subtitle {
  font-size: 26rpx;
  color: #999;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.input-group {
  display: flex;
  align-items: center;
  height: 90rpx;
  background: #f8f9fb;
  border-radius: 12rpx;
  padding: 0 24rpx;
  position: relative;
}

.input-field {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.code-btn {
  padding: 12rpx 24rpx;
  background: #5677fc;
  color: #fff;
  border-radius: 24rpx;
  font-size: 24rpx;
  white-space: nowrap;
}

.code-btn.disabled {
  background: #ccc;
}

.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  border-radius: 45rpx;
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  margin-top: 30rpx;
  border: none;
}

.register-btn[disabled] {
  opacity: 0.6;
}

.login-link {
  text-align: center;
  margin-top: 30rpx;
  font-size: 28rpx;
  color: #999;
}

.link-text {
  color: #5677fc;
  margin-left: 10rpx;
}
</style>
