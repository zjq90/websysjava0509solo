<template>
  <div class="page-container">
    <div class="page-header">
      <span>用户管理</span>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="用户ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="warning" size="mini" icon="el-icon-s-custom" @click="handleAssignRole(scope.row)">角色分配</el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form ref="userForm" :model="userForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="角色分配" :visible.sync="roleDialogVisible" width="500px">
      <el-checkbox-group v-model="checkedRoleIds">
        <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">
          {{ role.roleName }}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveRoles">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'UserManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增用户',
      userForm: {
        id: null,
        username: '',
        password: '',
        realName: '',
        phone: '',
        email: '',
        status: 1
      },
      roleDialogVisible: false,
      currentUserId: null,
      allRoles: [],
      checkedRoleIds: []
    }
  },
  created() {
    this.fetchData()
    this.fetchAllRoles()
  },
  methods: {
    fetchData() {
      this.loading = true
      request({
        url: '/system/user/list',
        method: 'get',
        params: {
          page: this.page - 1,
          size: this.size
        }
      }).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).finally(() => {
        this.loading = false
      })
    },
    handleSizeChange(size) {
      this.size = size
      this.fetchData()
    },
    handleCurrentChange(page) {
      this.page = page
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.userForm = {
        id: null,
        username: '',
        password: '',
        realName: '',
        phone: '',
        email: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.userForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/user/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      const method = this.userForm.id ? 'put' : 'post'
      request({
        url: '/system/user',
        method: method,
        data: this.userForm
      }).then(() => {
        this.$message.success(this.userForm.id ? '更新成功' : '新增成功')
        this.dialogVisible = false
        this.fetchData()
      })
    },
    fetchAllRoles() {
      request({
        url: '/system/role/list',
        method: 'get',
        params: { page: 0, size: 100 }
      }).then(res => {
        this.allRoles = res.data.content
      })
    },
    handleAssignRole(row) {
      this.currentUserId = row.id
      this.checkedRoleIds = []
      this.roleDialogVisible = true
      this.fetchUserRoles(row.id)
    },
    fetchUserRoles(userId) {
      request({
        url: `/system/user/${userId}/roles`,
        method: 'get'
      }).then(res => {
        this.checkedRoleIds = res.data
      })
    },
    handleSaveRoles() {
      request({
        url: `/system/user/${this.currentUserId}/roles`,
        method: 'post',
        data: { roleIds: this.checkedRoleIds }
      }).then(() => {
        this.$message.success('角色分配成功')
        this.roleDialogVisible = false
      })
    }
  }
}
</script>
