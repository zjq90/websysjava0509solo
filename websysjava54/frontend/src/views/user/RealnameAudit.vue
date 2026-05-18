<template>
  <div class="realname-audit">
    <h2>实名认证审核</h2>
    <el-table :data="userList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
      <el-table-column prop="idCard" label="身份证号" width="180"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="success" @click="audit(scope.row.id, 'APPROVED')">通过</el-button>
          <el-button size="mini" type="danger" @click="audit(scope.row.id, 'REJECTED')">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'RealnameAudit',
  data() {
    return {
      userList: []
    }
  },
  mounted() {
    this.loadPendingAudit()
  },
  methods: {
    loadPendingAudit() {
      this.$http.get('/users/realname-audit/pending').then(res => {
        if (res.data.code === 200) {
          this.userList = res.data.data
        }
      })
    },
    audit(id, status) {
      this.$http.post(`/users/${id}/realname-audit`, null, {
        params: { status }
      }).then(res => {
        if (res.data.code === 200) {
          this.$message.success('审核成功')
          this.loadPendingAudit()
        }
      })
    }
  }
}
</script>

<style scoped>
.realname-audit {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
