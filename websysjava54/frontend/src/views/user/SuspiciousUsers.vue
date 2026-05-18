<template>
  <div class="suspicious-users">
    <h2>可疑用户管理</h2>
    <el-table :data="userList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="creditScore" label="信用分" width="100">
        <template slot-scope="scope">
          <el-tag :type="getCreditType(scope.row.creditScore)">{{ scope.row.creditScore }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="suspiciousReason" label="可疑原因" width="200"></el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="ignoreSuspicious(scope.row.id)">忽略</el-button>
          <el-button size="mini" type="warning" @click="freezeFunds(scope.row)">冻结资金</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="冻结资金" :visible.sync="freezeVisible" width="500px">
      <el-form :model="freezeForm">
        <el-form-item label="冻结原因">
          <el-input type="textarea" v-model="freezeForm.reason" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="freezeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFreeze">确认冻结</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'SuspiciousUsers',
  data() {
    return {
      userList: [],
      freezeVisible: false,
      freezeForm: {
        userId: null,
        reason: ''
      }
    }
  },
  mounted() {
    this.loadSuspiciousUsers()
  },
  methods: {
    loadSuspiciousUsers() {
      this.$http.get('/users/suspicious').then(res => {
        if (res.data.code === 200) {
          this.userList = res.data.data
        }
      })
    },
    getCreditType(score) {
      if (score >= 80) return 'success'
      if (score >= 60) return 'warning'
      return 'danger'
    },
    ignoreSuspicious(id) {
      this.$confirm('确认忽略该用户的可疑标记?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(`/users/${id}/ignore-suspicious`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('忽略成功')
            this.loadSuspiciousUsers()
          }
        })
      })
    },
    freezeFunds(row) {
      this.freezeForm.userId = row.id
      this.freezeForm.reason = ''
      this.freezeVisible = true
    },
    submitFreeze() {
      this.$http.post(`/users/${this.freezeForm.userId}/freeze-funds`, null, {
        params: { reason: this.freezeForm.reason }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('冻结成功')
          this.freezeVisible = false
          this.loadSuspiciousUsers()
        }
      })
    }
  }
}
</script>

<style scoped>
.suspicious-users {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
