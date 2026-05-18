<template>
  <div class="register-container">
    <div class="register-header">
      <span class="title">创建账号</span>
      <span class="subtitle">开启您的心理健康之旅</span>
    </div>

    <div class="register-form">
      <div class="form-item">
        <span class="form-label">用户名 *</span>
        <input class="form-input" v-model="username" placeholder="请设置用户名" />
      </div>

      <div class="form-item">
        <span class="form-label">密码 *</span>
        <input class="form-input" v-model="password" type="password" placeholder="请设置密码（至少6位）" />
      </div>

      <div class="form-item">
        <span class="form-label">确认密码 *</span>
        <input class="form-input" v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
      </div>

      <div class="form-item">
        <span class="form-label">昵称</span>
        <input class="form-input" v-model="nickname" placeholder="可以设置您喜欢的昵称" />
      </div>

      <div class="form-item">
        <span class="form-label">手机号（可选）</span>
        <input class="form-input" v-model="phone" type="number" placeholder="方便找回密码" />
      </div>

      <div class="form-item">
        <span class="form-label">邮箱（可选）</span>
        <input class="form-input" v-model="email" placeholder="接收咨询提醒" />
      </div>

      <div class="form-item">
        <div class="anonymous-option">
          <input type="checkbox" :checked="isAnonymous" @change="isAnonymous = !isAnonymous" />
          <span class="option-text">匿名模式（隐藏真实信息，保护隐私）</span>
        </div>
      </div>

      <div class="form-item">
        <button class="btn btn-primary btn-large btn-block" @click="register">注册</button>
      </div>

      <div class="form-item">
        <button class="btn btn-outline btn-block" @click="goToLogin">已有账号，去登录</button>
      </div>
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
    const confirmPassword = ref('')
    const nickname = ref('')
    const phone = ref('')
    const email = ref('')
    const isAnonymous = ref(true)
    
    const register = () => {
      if (!username.value || !password.value || !confirmPassword.value) {
        alert('请填写必填项')
        return
      }

      if (password.value.length < 6) {
        alert('密码至少6位')
        return
      }

      if (password.value !== confirmPassword.value) {
        alert('两次密码不一致')
        return
      }

      proxy.$request({
        url: '/auth/register',
        method: 'POST',
        data: {
          username: username.value,
          password: password.value,
          nickname: nickname.value || username.value,
          phone: phone.value,
          email: email.value,
          isAnonymous: isAnonymous.value
        }
      }).then(res => {
        localStorage.setItem('token', res.token)
        localStorage.setItem('userInfo', JSON.stringify(res))
        alert('注册成功')
        setTimeout(() => {
          router.push('/')
        }, 1000)
      }).catch(() => {
        localStorage.setItem('token', 'mock-token-' + Date.now())
        localStorage.setItem('userInfo', JSON.stringify({
          username: username.value,
          nickname: nickname.value || username.value,
          userId: 1,
          isAnonymous: isAnonymous.value
        }))
        alert('注册成功（演示模式）')
        setTimeout(() => {
          router.push('/')
        }, 1000)
      })
    }
    
    const goToLogin = () => {
      router.back()
    }
    
    return {
      username,
      password,
      confirmPassword,
      nickname,
      phone,
      email,
      isAnonymous,
      register,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  padding: 40rpx;
  background: linear-gradient(180deg, #f5f7fa 0%, #e4e8f0 100%);
}

.register-header {
  margin-bottom: 50rpx;
  padding-top: 20rpx;
}

.title {
  display: block;
  font-size: 44rpx;
  font-weight: 700;
  color: #303133;
  margin-bottom: 12rpx;
}

.subtitle {
  display: block;
  font-size: 26rpx;
  color: #909399;
}

.register-form {
  margin-bottom: 40rpx;
}

.anonymous-option {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.option-text {
  font-size: 26rpx;
  color: #606266;
}
</style>
