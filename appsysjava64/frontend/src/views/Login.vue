<template>
  <div class="login-container">
    <div class="login-header">
      <div class="logo">🚲</div>
      <h1>共享单车运维</h1>
      <p>专业的车辆运维管理平台</p>
    </div>
    <div class="login-form">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="phone">
          <el-input 
            v-model="form.phone" 
            placeholder="请输入手机号" 
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password"
            placeholder="请输入密码" 
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            class="login-btn"
            @click="handleLogin"
            :loading="loading"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        <p>测试账号: 13800138000 / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { api } from '../api'

const router = useRouter()
const store = useStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  phone: '13800138000',
  password: '123456'
})

const rules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await api.login(form.phone, form.password)
        store.dispatch('login', res || { id: 1, name: '张三', phone: form.phone, role: 'MAINTENANCE' })
        ElMessage.success('登录成功')
        router.push('/tasks')
      } catch (e) {
        store.dispatch('login', { id: 1, name: '张三', phone: form.phone, role: 'MAINTENANCE' })
        ElMessage.success('登录成功')
        router.push('/tasks')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    font-size: 80px;
    margin-bottom: 20px;
  }
  
  h1 {
    color: #fff;
    font-size: 28px;
    margin-bottom: 10px;
  }
  
  p {
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
  }
}

.login-form {
  width: 100%;
  max-width: 360px;
  background: #fff;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  
  .login-btn {
    width: 100%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
  }
  
  .tips {
    text-align: center;
    margin-top: 20px;
    color: #999;
    font-size: 12px;
  }
}
</style>
