<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
    </div>
    <div class="table-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" style="width: 150px;"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入昵称" style="width: 150px;"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="searchForm.roleId" placeholder="请选择角色" style="width: 150px;" clearable>
            <el-option label="管理员" :value="1"></el-option>
            <el-option label="卖家" :value="2"></el-option>
            <el-option label="客服" :value="3"></el-option>
            <el-option label="买家" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 120px;" clearable>
            <el-option label="正常" value="ACTIVE"></el-option>
            <el-option label="禁用" value="DISABLED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="150"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="roleName" label="角色" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRoleType(scope.row.roleId)" size="small">{{ scope.row.roleName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 'ACTIVE' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" icon="el-icon-lock" @click="handleToggleStatus(scope.row)">
              {{ scope.row.status === 'ACTIVE' ? '禁用' : '启用' }}
            </el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        ></el-pagination>
      </div>
    </div>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" :value="1"></el-option>
            <el-option label="卖家" :value="2"></el-option>
            <el-option label="客服" :value="3"></el-option>
            <el-option label="买家" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="ACTIVE">正常</el-radio>
            <el-radio label="DISABLED">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserManagement',
  data() {
    return {
      searchForm: {
        username: '',
        nickname: '',
        roleId: '',
        status: ''
      },
      tableData: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        username: '',
        password: '',
        nickname: '',
        email: '',
        phone: '',
        roleId: '',
        status: 'ACTIVE'
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      const roles = ['管理员', '卖家', '客服', '买家']
      const mockData = []
      for (let i = 1; i <= 15; i++) {
        const roleIndex = (i - 1) % 4
        mockData.push({
          id: i,
          username: 'user' + i,
          nickname: '测试用户' + i,
          email: 'user' + i + '@example.com',
          phone: '13800' + String(i).padStart(5, '0'),
          roleId: roleIndex + 1,
          roleName: roles[roleIndex],
          status: i % 5 === 0 ? 'DISABLED' : 'ACTIVE',
          createTime: '2024-01-' + String(i).padStart(2, '0') + ' 10:00:00'
        })
      }
      this.tableData = mockData
      this.pagination.total = mockData.length
    },
    getRoleType(roleId) {
      const types = ['danger', 'warning', 'success', 'info']
      return types[roleId - 1] || 'info'
    },
    handleSearch() {
      this.$message.success('搜索成功')
      this.fetchData()
    },
    handleReset() {
      this.searchForm = {
        username: '',
        nickname: '',
        roleId: '',
        status: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.form = {
        id: null,
        username: '',
        password: '',
        nickname: '',
        email: '',
        phone: '',
        roleId: '',
        status: 'ACTIVE'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleToggleStatus(row) {
      const action = row.status === 'ACTIVE' ? '禁用' : '启用'
      this.$confirm(`确定要${action}用户"${row.nickname}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success(`${action}成功`)
        this.fetchData()
      })
    },
    handleDelete(row) {
      this.$confirm(`确定要删除用户"${row.nickname}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleSave() {
      this.$message.success('保存成功')
      this.dialogVisible = false
      this.fetchData()
    },
    handleSizeChange(size) {
      this.pagination.size = size
    },
    handleCurrentChange(page) {
      this.pagination.page = page
    }
  }
}
</script>

<style scoped>
</style>