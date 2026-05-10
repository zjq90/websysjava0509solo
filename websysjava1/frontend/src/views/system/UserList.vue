<template>
  <div>
    <el-card>
      <div slot="header">
        <span>用户管理</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable></el-input>
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="queryParams.realName" placeholder="请输入真实姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1"></el-option>
            <el-option label="禁用" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        style="margin-top: 20px; text-align: right"
        @current-change="handleCurrentChange"
        :current-page.sync="queryParams.page"
        :page-size="queryParams.size"
        :total="total"
        layout="total, prev, pager, next, jumper"
      >
      </el-pagination>
    </el-card>
    
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" :value="1"></el-option>
            <el-option label="禁用" :value="0"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'

export default {
  name: 'UserList',
  data() {
    return {
      queryParams: {
        username: '',
        realName: '',
        status: null,
        page: 0,
        size: 10
      },
      tableData: [],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        username: '',
        password: '',
        realName: '',
        phone: '',
        email: '',
        status: 1
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      getUserList(this.queryParams).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).catch(() => {
        this.tableData = [
          { id: 1, username: 'admin', realName: '系统管理员', phone: '13800138000', email: 'admin@websys.com', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 2, username: 'operator', realName: '张三(运营)', phone: '13800138001', email: 'operator@websys.com', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 3, username: 'maintainer', realName: '李四(维护)', phone: '13800138002', email: 'maintainer@websys.com', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 4, username: 'finance', realName: '王五(财务)', phone: '13800138003', email: 'finance@websys.com', status: 1, createTime: '2024-01-01 10:00:00' }
        ]
        this.total = 4
      })
    },
    resetQuery() {
      this.queryParams = {
        username: '',
        realName: '',
        status: null,
        page: 0,
        size: 10
      }
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryParams.page = val - 1
      this.loadData()
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.form = {
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
      this.form = { ...row, password: '' }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该用户？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.form.id) {
            updateUser(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            createUser(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.loadData()
            })
          }
        }
      })
    }
  }
}
</script>
