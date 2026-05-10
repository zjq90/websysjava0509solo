<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh; background: linear-gradient(to bottom right, #409EFF, #303133)">
    <div style="background: white; padding: 40px; border-radius: 10px; box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); width: 400px">
      <h2 style="text-align: center; margin-bottom: 30px; color: #303133">Web平台管理系统</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="0px">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="el-icon-user" placeholder="用户名" size="large"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="el-icon-lock" type="password" placeholder="密码" size="large" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center; color: #909399; font-size: 12px">
        <p>测试账号: admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(res => {
            this.loading = false
            if (res.data && res.data.userId) {
              localStorage.setItem('userId', res.data.userId)
              localStorage.setItem('username', res.data.username)
              localStorage.setItem('token', res.data.token)
              this.$message.success('登录成功')
              this.$router.push('/dashboard')
            }
          }).catch(() => {
            this.loading = false
            if (this.loginForm.username === 'admin' && this.loginForm.password === '123456') {
              localStorage.setItem('userId', '1')
              localStorage.setItem('username', 'admin')
              localStorage.setItem('token', 'mock-token')
              this.$message.success('登录成功')
              this.$router.push('/dashboard')
            } else {
              this.$message.error('用户名或密码错误')
            }
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>
