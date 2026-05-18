<template>
  <view class="container">
    <view class="logo-section">
      <view class="logo"></view>
      <text class="app-name">二手交易</text>
    </view>

    <view class="form-section">
      <view class="form-item">
        <input v-model="username" placeholder="用户名" class="input" />
      </view>
      <view class="form-item">
        <input v-model="password" type="password" placeholder="密码" class="input" />
      </view>

      <button @click="login" class="login-btn" :disabled="loading">
        {{ loading ? '登录中...' : '登录' }}
      </button>

      <view class="register-link">
        <text @click="showRegister = true">还没有账号？立即注册</text>
      </view>
    </view>

    <view class="quick-login">
      <text class="quick-title">快捷登录</text>
      <view class="quick-icons">
        <view class="quick-icon wechat" @click="wechatLogin">
          <text>微信</text>
        </view>
      </view>
    </view>

    <view class="custom-popup" v-if="showRegister" @click.self="showRegister = false">
      <view class="register-popup">
        <view class="popup-header">
          <text class="popup-title">注册账号</text>
          <text class="popup-close" @click="showRegister = false">×</text>
        </view>
        <view class="popup-content">
          <view class="form-item">
            <input v-model="regUsername" placeholder="用户名" class="input" />
          </view>
          <view class="form-item">
            <input v-model="regPassword" type="password" placeholder="密码" class="input" />
          </view>
          <view class="form-item">
            <input v-model="regNickname" placeholder="昵称" class="input" />
          </view>
          <button @click="register" class="register-btn" :disabled="registerLoading">
            {{ registerLoading ? '注册中...' : '注册' }}
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      username: 'user1',
      password: '123456',
      regUsername: '',
      regPassword: '',
      regNickname: '',
      loading: false,
      registerLoading: false,
      showRegister: false
    }
  },
  methods: {
    async login() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      this.loading = true
      try {
        const res = await this.$request({
          url: '/user/login',
          method: 'POST',
          data: {
            username: this.username,
            password: this.password
          }
        })

        uni.setStorageSync('token', res.data.token)
        uni.setStorageSync('user', res.data.user)

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
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    async register() {
      if (!this.regUsername || !this.regPassword) {
        uni.showToast({
          title: '请输入用户名和密码',
          icon: 'none'
        })
        return
      }

      this.registerLoading = true
      try {
        await this.$request({
          url: '/user/register',
          method: 'POST',
          data: {
            username: this.regUsername,
            password: this.regPassword,
            nickname: this.regNickname
          }
        })

        uni.showToast({
          title: '注册成功',
          icon: 'success'
        })

        this.showRegister = false
        this.username = this.regUsername
      } catch (e) {
        console.error(e)
      } finally {
        this.registerLoading = false
      }
    },
    wechatLogin() {
      uni.showToast({
        title: '微信登录开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 100rpx 50rpx;
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 80rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
  background: #fff;
  border-radius: 40rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 10rpx 30rpx rgba(0,0,0,0.2);
}

.app-name {
  font-size: 40rpx;
  color: #fff;
  font-weight: bold;
}

.form-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 60rpx 40rpx;
  margin-bottom: 40rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  border: none;
}

.register-link {
  text-align: center;
  margin-top: 30rpx;
}

.register-link text {
  color: #667eea;
  font-size: 26rpx;
}

.quick-login {
  text-align: center;
}

.quick-title {
  color: #fff;
  font-size: 26rpx;
  margin-bottom: 30rpx;
}

.quick-icons {
  display: flex;
  justify-content: center;
}

.quick-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24rpx;
}

.quick-icon.wechat {
  background: #07c160;
}

.custom-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.register-popup {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.popup-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #eee;
}

.popup-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.popup-close {
  font-size: 48rpx;
  color: #999;
  line-height: 1;
}

.popup-content {
  padding: 30rpx;
}

.register-btn {
  width: 100%;
  height: 80rpx;
  line-height: 80rpx;
  background: #3cc51f;
  color: #fff;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
}
</style>
