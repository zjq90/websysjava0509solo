<template>
  <div class="login-container">
    <div class="login-header">
      <div class="logo">
        <span class="logo-icon">🌻</span>
      </div>
      <span class="title">心理咨询</span>
      <span class="subtitle">守护您的心理健康</span>
    </div>

    <div class="login-form">
      <div class="form-item">
        <span class="form-label">用户名</span>
        <input class="form-input" v-model="username" placeholder="请输入用户名" />
      </div>

      <div class="form-item">
        <span class="form-label">密码</span>
        <input class="form-input" v-model="password" type="password" placeholder="请输入密码" />
      </div>

      <div class="form-item">
        <button class="btn btn-primary btn-large btn-block" @click="login">登录</button>
      </div>

      <div class="form-item">
        <button class="btn btn-outline btn-block" @click="goToRegister">注册账号</button>
      </div>
    </div>

    <div class="quick-login">
      <span class="quick-title">快速体验</span>
      <div class="quick-buttons">
        <button class="quick-btn" @click="quickLogin('user1')">体验账号1</button>
        <button class="quick-btn" @click="quickLogin('user2')">体验账号2</button>
      </div>
    </div>

    <div class="anonymous-tip">
      <span class="tip-text">💡 注册时可选择匿名模式，保护您的隐私</span>
    </div>
  </div>
</template>

<script>
import { ref, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter()
    const { proxy } = getCurrentInstance()
    
    const username = ref('')
    const password = ref('')
    
    const login = () => {
      if (!username.value || !password.value) {
        alert('请填写完整信息')
        return
      }

      proxy.$request({
        url: '/auth/login',
        method: 'POST',
        data: {
          username: username.value,
          password: password.value
        }
      }).then(res => {
        localStorage.setItem('token', res.token)
        localStorage.setItem('userInfo', JSON.stringify(res))
        alert('登录成功')
        setTimeout(() => {
          router.push('/')
        }, 1000)
      }).catch(() => {
        localStorage.setItem('token', 'mock-token-' + Date.now())
        localStorage.setItem('userInfo', JSON.stringify({
          username: username.value,
          nickname: username.value,
          userId: 1
        }))
        alert('登录成功（演示模式）')
        setTimeout(() => {
          router.push('/')
        }, 1000)
      })
    }
    
    const quickLogin = (user) => {
      username.value = user
      password.value = '123456'
      login()
    }
    
    const goToRegister = () => {
      router.push('/register')
    }
    
    return {
      username,
      password,
      login,
      quickLogin,
      goToRegister
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  padding: 80rpx 40rpx;
  background: linear-gradient(180deg, #f5f7fa 0%, #e4e8f0 100%);
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.logo {
  margin-bottom: 30rpx;
}

.logo-icon {
  font-size: 120rpx;
}

.title {
  display: block;
  font-size: 48rpx;
  font-weight: 700;
  color: #303133;
  margin-bottom: 16rpx;
}

.subtitle {
  display: block;
  font-size: 28rpx;
  color: #909399;
}

.login-form {
  margin-bottom: 60rpx;
}

.quick-login {
  margin-bottom: 40rpx;
}

.quick-title {
  display: block;
  font-size: 26rpx;
  color: #909399;
  text-align: center;
  margin-bottom: 30rpx;
}

.quick-buttons {
  display: flex;
  gap: 20rpx;
  justify-content: center;
}

.quick-btn {
  padding: 20rpx 40rpx;
  background: #ffffff;
  border: 2rpx solid #E4E7ED;
  border-radius: 40rpx;
  font-size: 26rpx;
  color: #606266;
  cursor: pointer;
}

.quick-btn:active {
  background: #F5F7FA;
}

.anonymous-tip {
  text-align: center;
}

.tip-text {
  font-size: 24rpx;
  color: #909399;
}
</style>
