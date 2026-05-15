<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <h2>教学辅助系统</h2>
        <p>Teaching Assistant System</p>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="el-icon-user"
            size="large"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="el-icon-lock"
            size="large"
            @keyup.enter.native="handleLogin"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            @click="handleLogin"
            :loading="loading"
          >登录</el-button>
        </el-form-item>
        <div class="login-links">
          <router-link to="/register">注册账号</router-link>
          <span class="divider">|</span>
          <a href="javascript:;" @click="showResetDialog = true">忘记密码？</a>
        </div>
      </el-form>
      <div class="test-info">
        <p>测试账号：admin / teacher / student1 / student2</p>
        <p>密码：123456</p>
      </div>
    </div>

    <el-dialog title="重置密码" :visible.sync="showResetDialog" width="400px">
      <el-form :model="resetForm" label-width="80px">
        <el-form-item label="邮箱">
          <el-input v-model="resetForm.email"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="resetForm.code" style="width: 60%">
            <el-button
              slot="append"
              :disabled="codeCountdown > 0"
              @click="sendCode"
            >{{ codeCountdown > 0 ? `${codeCountdown}s` : '发送验证码' }}</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="resetForm.newPassword" type="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="showResetDialog = false">取消</el-button>
        <el-button type="primary" @click="handleResetPassword">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { login, sendResetCode, resetPassword } from '@/api/user'
import { mapActions } from 'vuex'

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
      loading: false,
      showResetDialog: false,
      resetForm: {
        email: '',
        code: '',
        newPassword: ''
      },
      codeCountdown: 0
    }
  },
  methods: {
    ...mapActions('user', ['login']),
    async handleLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            await this.login(this.loginForm)
            this.$message.success('登录成功')
            this.$router.push('/dashboard')
          } catch (error) {
            console.error(error)
          } finally {
            this.loading = false
          }
        }
      })
    },
    async sendCode() {
      if (!this.resetForm.email) {
        this.$message.warning('请输入邮箱')
        return
      }
      try {
        await sendResetCode(this.resetForm.email)
        this.$message.success('验证码已发送')
        this.codeCountdown = 60
        const timer = setInterval(() => {
          this.codeCountdown--
          if (this.codeCountdown <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } catch (error) {
        console.error(error)
      }
    },
    async handleResetPassword() {
      try {
        await resetPassword(this.resetForm)
        this.$message.success('密码重置成功，请使用新密码登录')
        this.showResetDialog = false
        this.resetForm = { email: '', code: '', newPassword: '' }
      } catch (error) {
        console.error(error)
      }
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

.login-links .divider {
  margin: 0 10px;
  color: #ddd;
}

.test-info {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.test-info p {
  margin: 5px 0;
}
</style>
