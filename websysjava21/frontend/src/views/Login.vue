<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">医院管理系统</h2>
      <el-form ref="loginForm" :model="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="login-tips">
        <p>测试账号：</p>
        <p>管理员 admin / 123456</p>
        <p>医生 doctor / 123456</p>
        <p>护士 nurse / 123456</p>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import store from '@/store'
import router from '@/router'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: 'admin',
        password: '123456'
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.loading = true
      request({
        url: '/auth/login',
        method: 'post',
        data: this.loginForm
      }).then(res => {
        store.dispatch('setToken', res.data.token)
        store.dispatch('setUserInfo', {
          username: res.data.username,
          realName: res.data.realName
        })
        this.$message.success('登录成功')
        router.push('/')
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
}

.login-form {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
}

.login-tips {
  padding-top: 20px;
  border-top: 1px solid #eee;
  font-size: 12px;
  color: #999;
}

.login-tips p {
  margin: 5px 0;
}
</style>
