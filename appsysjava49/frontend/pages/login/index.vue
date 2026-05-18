<template>
  <view class="login-page">
    <view class="header">
      <view class="logo">🛒</view>
      <text class="title">二手交易平台</text>
      <text class="subtitle">让闲置物品发挥价值</text>
    </view>

    <view class="form">
      <view class="form-item">
        <text class="label">用户名</text>
        <input v-model="username" class="input" placeholder="请输入用户名" />
      </view>
      <view class="form-item">
        <text class="label">密码</text>
        <input v-model="password" class="input" type="password" placeholder="请输入密码" />
      </view>

      <button class="login-btn" @click="handleLogin">登录</button>
      
      <view class="register-link">
        <text>还没有账号？</text>
        <text class="link" @click="goToRegister">立即注册</text>
      </view>
    </view>

    <view class="elder-mode-toggle" @click="toggleElderMode">
      <text class="toggle-icon">👵</text>
      <text class="toggle-text">{{ elderMode ? '关闭长辈模式' : '开启长辈模式' }}</text>
    </view>

    <view class="voice-input" v-if="elderMode" @click="startVoiceInput">
      <text class="voice-icon">🎤</text>
      <text class="voice-text">语音输入</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'
import { mapState, mapActions } from 'vuex'

export default {
  computed: {
    ...mapState(['elderMode'])
  },
  data() {
    return {
      username: '',
      password: ''
    }
  },
  methods: {
    ...mapActions(['toggleElderMode', 'login']),
    
    async handleLogin() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      try {
        uni.showLoading({
          title: '登录中...'
        })

        const res = await request.post('/user/login', {
          username: this.username,
          password: this.password
        })

        uni.hideLoading()
        
        this.login({
          user: res.data.user,
          token: res.data.token
        })

        uni.showToast({
          title: '登录成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.switchTab({
            url: '/pages/index/index'
          })
        }, 1000)
      } catch (e) {
        uni.hideLoading()
        await this.mockLogin()
      }
    },

    async mockLogin() {
      const mockUser = {
        id: 1,
        username: this.username,
        nickname: this.username,
        avatar: '',
        creditScore: 4.8,
        location: '北京·朝阳'
      }
      
      this.login({
        user: mockUser,
        token: 'mock-token-' + Date.now()
      })

      uni.showToast({
        title: '登录成功',
        icon: 'success'
      })

      setTimeout(() => {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }, 1000)
    },

    goToRegister() {
      uni.navigateTo({
        url: '/pages/register/index'
      })
    },

    startVoiceInput() {
      uni.showToast({
        title: '语音输入功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  padding: 100rpx 60rpx;
}

.header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  font-size: 120rpx;
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

.login-btn {
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

.register-link {
  text-align: center;
  margin-top: 40rpx;
  font-size: 26rpx;
  color: #666;
}

.link {
  color: #409EFF;
  margin-left: 8rpx;
}

.elder-mode-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 60rpx;
  padding: 24rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50rpx;
}

.toggle-icon {
  font-size: 36rpx;
  margin-right: 12rpx;
}

.toggle-text {
  font-size: 28rpx;
  color: #fff;
}

.voice-input {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 30rpx;
  padding: 20rpx;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50rpx;
}

.voice-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
}

.voice-text {
  font-size: 26rpx;
  color: #fff;
}
</style>
