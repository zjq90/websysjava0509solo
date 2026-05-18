<template>
  <div class="user-list">
    <h2>用户列表</h2>
    <el-table :data="userList" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="120"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
      <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
      <el-table-column prop="province" label="省份" width="120"></el-table-column>
      <el-table-column prop="creditScore" label="信用分" width="100">
        <template slot-scope="scope">
          <el-tag :type="getCreditType(scope.row.creditScore)">{{ scope.row.creditScore }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isExpert" label="专家" width="80">
        <template slot-scope="scope">
          <el-tag size="mini" type="success" v-if="scope.row.isExpert">是</el-tag>
          <el-tag size="mini" v-else>否</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'UserList',
  data() {
    return {
      userList: []
    }
  },
  mounted() {
    this.loadUserList()
  },
  methods: {
    loadUserList() {
      this.$http.get('/users').then(res => {
        if (res.data.code === 200) {
          this.userList = res.data.data
        }
      })
    },
    getCreditType(score) {
      if (score >= 80) return 'success'
      if (score >= 60) return 'warning'
      return 'danger'
    }
  }
}
</script>

<style scoped>
.user-list {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
