<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">Web平台管理系统</div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="el-icon-user"
            size="large"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            size="large"
            @keyup.enter.native="handleLogin"
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
      <div class="login-tips">
        默认账号：admin / admin123
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: 'admin123'
      },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    ...mapActions(['Login']),
    handleLogin() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            const response = await this.Login(this.loginForm)
            if (response.code === 200) {
              this.$message.success('登录成功')
              this.$router.push('/')
            } else {
              this.$message.error(response.message || '登录失败')
            }
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
