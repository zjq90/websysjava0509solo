<template>
  <div class="page-container">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="card-wrapper">
          <div class="user-avatar-section">
            <div class="avatar-wrapper">
              <el-avatar :size="100" :src="userInfo.avatar || ''">
                {{ (userInfo.realName || userInfo.username || 'U').charAt(0) }}
              </el-avatar>
            </div>
            <h3>{{ userInfo.realName || userInfo.username }}</h3>
            <p class="username">@{{ userInfo.username }}</p>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="card-wrapper">
          <div slot="header">
            <span>基本信息</span>
          </div>
          <el-form :model="profileForm" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="profileForm.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-input v-model="profileForm.realName"></el-input>
            </el-form-item>
            <el-form-item label="性别">
              <el-select v-model="profileForm.gender" placeholder="请选择性别">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="手机号">
              <el-input v-model="profileForm.phone"></el-input>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" disabled>
                <el-button
                  slot="append"
                  type="primary"
                  size="small"
                  @click="showBindEmailDialog"
                >{{ userInfo.emailBound ? '已绑定' : '绑定邮箱' }}</el-button>
              </el-input>
            </el-form-item>
            <el-form-item label="个人签名">
              <el-input
                v-model="profileForm.signature"
                type="textarea"
                :rows="3"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveProfile">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="card-wrapper mt-20">
          <div slot="header">
            <span>修改密码</span>
          </div>
          <el-form :model="passwordForm" label-width="100px">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="绑定邮箱" :visible.sync="bindEmailDialogVisible" width="400px">
      <el-form :model="bindEmailForm" label-width="80px">
        <el-form-item label="邮箱">
          <el-input v-model="bindEmailForm.email"></el-input>
        </el-form-item>
        <el-form-item label="验证码">
          <el-input v-model="bindEmailForm.code" style="width: 60%">
            <el-button
              slot="append"
              :disabled="codeCountdown > 0"
              @click="sendBindCode"
            >{{ codeCountdown > 0 ? `${codeCountdown}s` : '发送验证码' }}</el-button>
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="bindEmailDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="bindEmail">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex'
import { updateUserInfo, changePassword, bindEmail, sendResetCode } from '@/api/user'

export default {
  name: 'Profile',
  computed: {
    ...mapState('user', ['userInfo'])
  },
  data() {
    return {
      profileForm: {
        username: '',
        realName: '',
        gender: '',
        phone: '',
        email: '',
        signature: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: ''
      },
      bindEmailDialogVisible: false,
      bindEmailForm: {
        email: '',
        code: ''
      },
      codeCountdown: 0
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    ...mapActions('user', ['getUserInfo']),
    loadUserInfo() {
      this.profileForm = {
        username: this.userInfo.username,
        realName: this.userInfo.realName,
        gender: this.userInfo.gender,
        phone: this.userInfo.phone,
        email: this.userInfo.email,
        signature: this.userInfo.signature
      }
    },
    async saveProfile() {
      try {
        await updateUserInfo(this.profileForm)
        await this.getUserInfo()
        this.$message.success('保存成功')
      } catch (error) {
        console.error(error)
      }
    },
    async changePassword() {
      if (!this.passwordForm.oldPassword || !this.passwordForm.newPassword) {
        this.$message.warning('请输入完整信息')
        return
      }
      try {
        await changePassword(this.passwordForm)
        this.$message.success('密码修改成功')
        this.passwordForm = { oldPassword: '', newPassword: '' }
      } catch (error) {
        console.error(error)
      }
    },
    showBindEmailDialog() {
      if (this.userInfo.emailBound) {
        return
      }
      this.bindEmailDialogVisible = true
    },
    async sendBindCode() {
      if (!this.bindEmailForm.email) {
        this.$message.warning('请输入邮箱')
        return
      }
      try {
        await sendResetCode(this.bindEmailForm.email)
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
    async bindEmail() {
      try {
        await bindEmail(this.bindEmailForm)
        await this.getUserInfo()
        this.loadUserInfo()
        this.bindEmailDialogVisible = false
        this.$message.success('邮箱绑定成功')
      } catch (error) {
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.user-avatar-section {
  text-align: center;
  padding: 20px 0;
}

.avatar-wrapper {
  margin-bottom: 20px;
}

.avatar-wrapper .el-avatar {
  border: 3px solid #409eff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.user-avatar-section h3 {
  color: #333;
  margin-bottom: 10px;
}

.user-avatar-section .username {
  color: #999;
  font-size: 14px;
}
</style>
