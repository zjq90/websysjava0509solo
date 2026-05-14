<template>
  <div class="user">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="float: left; font-size: 18px; font-weight: bold;">用户管理</span>
        <el-button style="float: right" type="primary" @click="showCreateDialog">新增用户</el-button>
      </div>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column prop="address" label="地址" width="200"></el-table-column>
        <el-table-column prop="packageType" label="套餐类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.packageType === 'BASIC'" type="info">基础套餐</el-tag>
            <el-tag v-else-if="scope.row.packageType === 'STANDARD'" type="success">标准套餐</el-tag>
            <el-tag v-else type="warning">高级套餐</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isActive" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="editUser(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="isEdit ? '编辑用户' : '新增用户'" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address"></el-input>
        </el-form-item>
        <el-form-item label="套餐类型">
          <el-select v-model="form.packageType" placeholder="请选择">
            <el-option label="基础套餐" value="BASIC"></el-option>
            <el-option label="标准套餐" value="STANDARD"></el-option>
            <el-option label="高级套餐" value="PREMIUM"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'User',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      isEdit: false,
      form: {
        id: null,
        username: '',
        realName: '',
        phone: '',
        email: '',
        address: '',
        packageType: 'BASIC'
      }
    }
  },
  mounted() {
    this.loadUsers()
  },
  methods: {
    async loadUsers() {
      try {
        const res = await this.$http.get('/api/users')
        if (res.data && res.data.data) {
          this.tableData = res.data.data
        }
      } catch (e) {
        console.log('使用模拟数据')
      }
    },
    showCreateDialog() {
      this.isEdit = false
      this.form = {
        id: null,
        username: '',
        realName: '',
        phone: '',
        email: '',
        address: '',
        packageType: 'BASIC'
      }
      this.dialogVisible = true
    },
    editUser(row) {
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    async saveUser() {
      try {
        if (this.isEdit) {
          await this.$http.put(`/api/users/${this.form.id}`, this.form)
        } else {
          await this.$http.post('/api/users', this.form)
        }
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.loadUsers()
      } catch (e) {
        this.$message.error('保存失败')
      }
    },
    async deleteUser(row) {
      this.$confirm('确认删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await this.$http.delete(`/api/users/${row.id}`)
          this.$message.success('删除成功')
          this.loadUsers()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.user {
  padding: 0;
}
</style>
