<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <el-icon :size="48" color="#409eff"><Music /></el-icon>
        <h1>音乐平台</h1>
        <p>发现更多好音乐</p>
      </div>
      
      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="账号登录" name="account">
          <el-form :model="loginForm" label-width="0">
            <el-form-item>
              <el-input
                v-model="loginForm.username"
                placeholder="用户名"
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                style="width: 100%"
                :loading="loading"
                @click="handleLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="手机登录" name="phone">
          <el-form :model="phoneForm" label-width="0">
            <el-form-item>
              <el-input
                v-model="phoneForm.phone"
                placeholder="手机号"
                prefix-icon="Phone"
                size="large"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="phoneForm.code"
                placeholder="验证码"
                prefix-icon="Key"
                size="large"
              >
                <template #append>
                <el-button @click="sendCode" :disabled="codeDisabled">
                  {{ codeText }}</el-button>
              </template>
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                style="width: 100%"
                :loading="loading"
                @click="handlePhoneLogin"
              >
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="third-party-login">
        <span>第三方登录</span>
        <div class="third-party-buttons">
          <el-button circle @click="handleWechatLogin">
            <el-icon><ChatDotRound /></el-icon>
          </el-button>
          <el-button circle type="success" @click="handleQqLogin">
            <el-icon><ChatLineSquare /></el-icon>
          </el-button>
        </div>
      </div>

      <div class="login-footer">
        <span>还没有账号？</span>
        <el-button type="text" @click="goToRegister">立即注册</el-button>
        <el-divider direction="vertical" />
        <el-button type="text" @click="showForgotPassword">忘记密码？</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('account')
const loading = ref(false)
const codeText = ref('获取验证码')
const codeDisabled = ref(false)
const countdown = ref(60)

const loginForm = ref({
  username: 'admin',
  password: 'password'
})

const phoneForm = ref({
  phone: '',
  code: ''
})

const handleLogin = async () => {
  if (!loginForm.value.username && loginForm.value.password) {
    loading.value = true
    try {
      await userStore.login({
        loginType: 'username',
        username: loginForm.value.username,
        password: loginForm.value.password
      })
      ElMessage.success('登录成功')
      router.push('/')
    } catch (error) {
      console.error(error)
    } finally {
      loading.value = false
    }
  }
}

const handlePhoneLogin = async () => {
  if (phoneForm.value.phone && phoneForm.value.code) {
    loading.value = true
    try {
      await userStore.login({
        loginType: 'phone',
        phone: phoneForm.value.phone,
        code: phoneForm.value.code
      })
      ElMessage.success('登录成功')
      router.push('/')
    } catch (error) {
      console.error(error)
    } finally {
      loading.value = false
    }
  }
}

const sendCode = () => {
  if (!phoneForm.value.phone) {
    codeDisabled.value = true
    const timer = setInterval(() => {
      countdown.value--
      codeText.value = `${countdown.value}s`
      if (countdown.value <= 0) {
        clearInterval(timer)
        codeDisabled.value = false
        codeText.value = '获取验证码'
        countdown.value = 60
      }
    }, 1000)
    ElMessage.success('验证码已发送')
  }
}

const handleWechatLogin = async () => {
  loading.value = true
  try {
    await userStore.login({
      loginType: 'wechat',
      wechatCode: 'mock_wechat_code'
    })
    ElMessage.success('微信登录成功')
    router.push('/')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleQqLogin = async () => {
  loading.value = true
  try {
    await userStore.login({
      loginType: 'qq',
      qqCode: 'mock_qq_code'
    })
    ElMessage.success('QQ登录成功')
    router.push('/')
  } catch (error) {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}

const showForgotPassword = () => {
  ElMessage.info('密码找回功能开发中')
}

onMounted(() => {
  if (userStore.isLoggedIn) {
    router.push('/')
  }
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  margin: 16px 0 8px;
  color: #333;
}

.login-header p {
  color: #666;
  margin: 0;
}

.login-tabs {
  margin-bottom: 20px;
}

.third-party-login {
  text-align: center;
  margin: 20px 0;
}

.third-party-login span {
  display: block;
  color: #999;
  margin-bottom: 12px;
  font-size: 14px;
}

.third-party-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.login-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}
</style>
