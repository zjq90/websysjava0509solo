<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <el-icon :size="48" color="#667eea"><Music /></el-icon>
        <h1>创建账户</h1>
        <p>加入我们，享受无限音乐</p>
      </div>
      <el-form
        ref="registerForm"
        :model="form"
        :rules="rules"
        class="register-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名 (3-50字符)"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码 (6-100字符)"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="form.email"
            placeholder="邮箱 (可选)"
            size="large"
            :prefix-icon="Message"
          />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="昵称 (可选)"
            size="large"
            :prefix-icon="UserFilled"
          />
        </el-form-item>
        <el-button
          type="primary"
          size="large"
          class="register-btn"
          :loading="loading"
          @click="handleRegister"
        >
          注册
        </el-button>
      </el-form>
      <div class="register-footer">
        <span>已有账户？</span>
        <el-button type="primary" link @click="$router.push('/login')">立即登录</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const registerForm = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  nickname: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.value.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度必须在3-50个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 100, message: '密码长度必须在6-100个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerForm.value) return
  await registerForm.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await userStore.register({
          username: form.value.username,
          password: form.value.password,
          email: form.value.email || undefined,
          nickname: form.value.nickname || undefined
        })
        ElMessage.success('注册成功，请登录')
        router.push('/login')
      } catch (e) {
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.register-container {
  min-height: calc(100vh - 164px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 40px;
  width: 100%;
  max-width: 450px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h1 {
  margin: 15px 0 8px;
  font-size: 28px;
  color: #333;
}

.register-header p {
  color: #999;
  margin: 0;
}

.register-form {
  margin-bottom: 20px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
}

.register-footer {
  text-align: center;
  color: #999;
}
</style>
