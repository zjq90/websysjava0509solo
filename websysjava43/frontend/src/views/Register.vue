<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <h2>用户注册</h2>
        <p>Join Teaching Assistant System</p>
      </div>
      <el-form :model="registerForm" :rules="rules" ref="registerForm" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="用户名"
            prefix-icon="el-icon-user"
            size="large"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="el-icon-lock"
            size="large"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="邮箱"
            prefix-icon="el-icon-message"
            size="large"
          ></el-input>
        </el-form-item>
        <el-form-item prop="realName">
          <el-input
            v-model="registerForm.realName"
            placeholder="真实姓名"
            prefix-icon="el-icon-document"
            size="large"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            @click="handleRegister"
            :loading="loading"
          >注册</el-button>
        </el-form-item>
        <div class="login-links">
          <router-link to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { register } from '@/api/user'

export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        email: '',
        realName: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    async handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            await register(this.registerForm)
            this.$message.success('注册成功，请登录')
            this.$router.push('/login')
          } catch (error) {
            console.error(error)
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
}

.login-title h2 {
  color: #333;
  margin-bottom: 10px;
}

.login-title p {
  color: #999;
  font-size: 14px;
}

.login-form {
  margin-bottom: 20px;
}

.login-links {
  text-align: center;
  font-size: 14px;
}

.login-links a {
  color: #409eff;
  text-decoration: none;
}
</style>
