<template>
  <div class="page-container">
    <div class="page-header">
      <span>角色管理</span>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="角色ID" width="80"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码" width="150"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="description" label="角色描述"></el-table-column>
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
            <el-button type="warning" size="mini" icon="el-icon-s-custom" @click="handleAssignPermission(scope.row)">权限分配</el-button>
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
      <el-form ref="roleForm" :model="roleForm" label-width="80px">
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" placeholder="请输入角色描述"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
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

    <el-dialog title="权限分配" :visible.sync="permissionDialogVisible" width="600px">
      <el-tree
        ref="permissionTree"
        :data="permissionTree"
        :props="{ label: 'label' }"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedPermissionIds"
        style="height: 400px; overflow-y: auto;">
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="permissionDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSavePermissions">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'RoleManage',
  data() {
    return {
      loading: false,
      tableData: [],
      page: 1,
      size: 10,
      total: 0,
      dialogVisible: false,
      dialogTitle: '新增角色',
      roleForm: {
        id: null,
        roleCode: '',
        roleName: '',
        description: '',
        status: 1
      },
      permissionDialogVisible: false,
      currentRoleId: null,
      permissionTree: [],
      checkedPermissionIds: []
    }
  },
  created() {
    this.fetchData()
    this.fetchPermissionTree()
  },
  methods: {
    fetchData() {
      this.loading = true
      request({
        url: '/system/role/list',
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
      this.dialogTitle = '新增角色'
      this.roleForm = {
        id: null,
        roleCode: '',
        roleName: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.roleForm = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该角色吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          url: `/system/role/${row.id}`,
          method: 'delete'
        }).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    handleSubmit() {
      const method = this.roleForm.id ? 'put' : 'post'
      request({
        url: '/system/role',
        method: method,
        data: this.roleForm
      }).then(() => {
        this.$message.success(this.roleForm.id ? '更新成功' : '新增成功')
        this.dialogVisible = false
        this.fetchData()
      })
    },
    fetchPermissionTree() {
      request({
        url: '/system/permission/tree',
        method: 'get'
      }).then(res => {
        this.permissionTree = res.data
      })
    },
    handleAssignPermission(row) {
      this.currentRoleId = row.id
      this.checkedPermissionIds = []
      this.permissionDialogVisible = true
      this.fetchRolePermissions(row.id)
    },
    fetchRolePermissions(roleId) {
      request({
        url: `/system/role/${roleId}/permissions`,
        method: 'get'
      }).then(res => {
        this.checkedPermissionIds = res.data
      })
    },
    handleSavePermissions() {
      const checkedKeys = this.$refs.permissionTree.getCheckedKeys()
      request({
        url: `/system/role/${this.currentRoleId}/permissions`,
        method: 'post',
        data: { permissionIds: checkedKeys }
      }).then(() => {
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
      })
    }
  }
}
</script>
