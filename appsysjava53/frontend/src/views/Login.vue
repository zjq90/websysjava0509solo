<template>
  <div class="login-container">
    <div class="login-card">
      <div class="logo-section">
        <div class="logo">
          <el-icon :size="48"><House /></el-icon>
        </div>
        <h1 class="app-title">文物收藏</h1>
        <p class="app-desc">专业的文物收藏交流平台</p>
      </div>

      <el-form :model="form" class="login-form">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="elder-login" @click="handleVoiceLogin">
        <el-icon :size="24"><Microphone /></el-icon>
        <span>语音登录（长辈专属）</span>
      </div>

      <div class="register-link">
        还没有账号？<span class="link-text">立即注册</span>
      </div>

      <div class="agreement">
        登录即表示同意《用户协议》和《隐私政策》
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/app'
import { House, User, Lock, Microphone } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const form = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!form.value.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (!form.value.password) {
    ElMessage.warning('请输入密码')
    return
  }

  loading.value = true

  setTimeout(() => {
    const user = {
      id: 1,
      username: form.value.username,
      nickname: '收藏爱好者',
      isVerified: 1,
      balance: '8,888.00'
    }
    userStore.login(user, 'mock-token-' + Date.now())
    loading.value = false
    ElMessage.success('登录成功')
    router.push('/home')
  }, 1000)
}

const handleVoiceLogin = () => {
  ElMessage.info('语音识别功能开发中')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #f8f5f2 0%, #fff 100%);
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 20px;
  box-shadow: 0 8px 30px rgba(139, 69, 19, 0.3);
}

.app-title {
  font-size: 32px;
  font-weight: bold;
  color: #8B4513;
  margin: 0 0 12px;
}

.app-desc {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.login-form {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 48px;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  border: none;
  font-size: 16px;
}

.elder-login {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 16px;
  background: #f8f5f2;
  border-radius: 12px;
  color: #8B4513;
  cursor: pointer;
  border: 2px dashed #8B4513;
  margin-bottom: 20px;
  font-size: 14px;
}

.register-link {
  text-align: center;
  font-size: 14px;
  color: #666;
  margin-bottom: 20px;
}

.link-text {
  color: #8B4513;
  cursor: pointer;
}

.agreement {
  text-align: center;
  font-size: 12px;
  color: #999;
  line-height: 1.6;
}

.elder-mode .login-btn {
  height: 56px;
  font-size: 18px;
}

.elder-mode .elder-login {
  font-size: 18px;
  padding: 20px;
}

.elder-mode .app-title {
  font-size: 36px;
}
</style>
