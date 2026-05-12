<template>
  <div class="page-container">
    <div class="page-header">
      <span>权限管理</span>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增权限</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="权限ID" width="80"></el-table-column>
        <el-table-column prop="permissionCode" label="权限编码" width="180"></el-table-column>
        <el-table-column prop="permissionName" label="权限名称" width="150"></el-table-column>
        <el-table-column prop="resourceType" label="资源类型" width="120"></el-table-column>
        <el-table-column prop="parentId" label="父级ID" width="100"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form ref="permissionForm" :model="permissionForm" label-width="100px">
        <el-form-item label="权限编码" prop="permissionCode">
          <el-input v-model="permissionForm.permissionCode" placeholder="请输入权限编码"></el-input>
        </el-form-item>
        <el-form-item label="权限名称" prop="permissionName">
          <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称"></el-input>
        </el-form-item>
        <el-form-item label="资源类型" prop="resourceType">
          <el-select v-model="permissionForm.resourceType" placeholder="请选择资源类型" style="width: 100%">
            <el-option label="菜单" value="menu"></el-option>
            <el-option label="按钮" value="button"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父级权限" prop="parentId">
          <el-select v-model="permissionForm.parentId" placeholder="请选择父级权限" style="width: 100%">
            <el-option label="顶级" :value="0"></el-option>
            <el-option v-for="item in tableData" :key="item.id" :label="item.permissionName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="permissionForm.sortOrder" :min="1" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="permissionForm.status">
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
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'PermissionManage',
  data() {
    return {
      loading: false,
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增权限',
      permissionForm: {
        id: null,
        permissionCode: '',
        permissionName: '',
        resourceType: '',
        parentId: 0,
        sortOrder: 1,
        status: 1
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      request({
        url: '/system/permission/list',
        method: 'get'
      }).then(res => {
        this.tableData = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    handleAdd() {
      this.dialogTitle = '新增权限'
      this.permissionForm = {
        id: null,
        permissionCode: '',
        permissionName: '',
        resourceType: '',
        parentId: 0,
        sortOrder: 1,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑权限'
      this.permissionForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该权限吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/permission/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      const method = this.permissionForm.id ? 'put' : 'post'
      request({
        url: '/system/permission',
        method: method,
        data: this.permissionForm
      }).then(() => {
        this.$message.success(this.permissionForm.id ? '更新成功' : '新增成功')
        this.dialogVisible = false
        this.fetchData()
      })
    }
  }
}
</script>
