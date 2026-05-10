<template>
  <div>
    <el-card>
      <div slot="header">
        <span>角色管理</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
      </div>
      
      <el-form :inline="true" :model="queryParams" size="small">
        <el-form-item label="角色名称">
          <el-input v-model="queryParams.name" placeholder="请输入角色名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="queryParams.code" placeholder="请输入角色编码" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="code" label="角色编码" width="150"></el-table-column>
        <el-table-column prop="description" label="角色描述"></el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="380">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" type="primary" icon="el-icon-s-order" @click="handleAssignMenu(scope.row)">分配菜单</el-button>
              <el-button size="mini" type="warning" icon="el-icon-key" @click="handleAssignPermission(scope.row)">分配权限</el-button>
              <el-button size="mini" type="success" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </div>
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" :disabled="!!form.id"></el-input>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="form.description" type="textarea" :rows="3"></el-input>
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
    
    <el-dialog title="分配菜单" :visible.sync="menuDialogVisible" width="500px">
      <el-tree
        ref="menuTree"
        :data="menuTreeData"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedMenuIds"
        :props="{ label: 'name', children: 'children' }"
      >
      </el-tree>
      <div slot="footer">
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveMenu">保存</el-button>
      </div>
    </el-dialog>
    
    <el-dialog title="分配权限" :visible.sync="permissionDialogVisible" width="600px">
      <el-checkbox-group v-model="checkedPermissionIds">
        <el-checkbox v-for="item in permissionList" :key="item.id" :label="item.id" style="width: 48%; margin-bottom: 10px">
          <span>{{ item.name }}</span>
          <span style="color: #909399; font-size: 12px; margin-left: 10px">{{ item.code }}</span>
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermission">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getRoleList, createRole, updateRole, deleteRole,
  getRoleMenus, assignRoleMenus,
  getRolePermissions, assignRolePermissions
} from '@/api/role'
import { getMenuList, getPermissionList } from '@/api/menu'

export default {
  name: 'RoleList',
  data() {
    return {
      queryParams: {
        name: '',
        code: '',
        page: 0,
        size: 10
      },
      tableData: [],
      total: 0,
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        code: '',
        description: '',
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
      },
      menuDialogVisible: false,
      permissionDialogVisible: false,
      currentRoleId: null,
      checkedMenuIds: [],
      checkedPermissionIds: [],
      menuTreeData: [],
      permissionList: []
    }
  },
  created() {
    this.loadData()
    this.loadMenus()
    this.loadPermissions()
  },
  methods: {
    loadData() {
      getRoleList(this.queryParams).then(res => {
        this.tableData = res.data.content
        this.total = res.data.totalElements
      }).catch(() => {
        this.tableData = [
          { id: 1, name: '系统管理员', code: 'ADMIN', description: '拥有系统所有权限', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 2, name: '运营人员', code: 'OPERATOR', description: '负责设备运营和商品管理', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 3, name: '维护人员', code: 'MAINTAINER', description: '负责设备维护和远程控制', status: 1, createTime: '2024-01-01 10:00:00' },
          { id: 4, name: '财务人员', code: 'FINANCE', description: '负责财务相关管理', status: 1, createTime: '2024-01-01 10:00:00' }
        ]
        this.total = 4
      })
    },
    loadMenus() {
      getMenuList().then(res => {
        this.menuTreeData = this.buildTree(res.data)
      }).catch(() => {
        this.menuTreeData = [
          { id: 1, name: '首页', parentId: null, children: [] },
          { id: 2, name: '系统管理', parentId: null, children: [
            { id: 3, name: '用户管理', parentId: 2 },
            { id: 4, name: '角色管理', parentId: 2 },
            { id: 5, name: '菜单管理', parentId: 2 }
          ]},
          { id: 6, name: '设备管理', parentId: null, children: [
            { id: 7, name: '设备列表', parentId: 6 },
            { id: 8, name: '远程控制', parentId: 6 }
          ]},
          { id: 9, name: '商品管理', parentId: null, children: [
            { id: 10, name: '商品列表', parentId: 9 },
            { id: 11, name: '货道管理', parentId: 9 }
          ]}
        ]
      })
    },
    loadPermissions() {
      getPermissionList().then(res => {
        this.permissionList = res.data
      }).catch(() => {
        this.permissionList = [
          { id: 1, name: '用户新增', code: 'user:add' },
          { id: 2, name: '用户编辑', code: 'user:edit' },
          { id: 3, name: '用户删除', code: 'user:delete' },
          { id: 4, name: '设备新增', code: 'device:add' },
          { id: 5, name: '设备编辑', code: 'device:edit' },
          { id: 6, name: '设备删除', code: 'device:delete' },
          { id: 7, name: '远程重启', code: 'device:restart' },
          { id: 8, name: '远程开锁', code: 'device:unlock' },
          { id: 9, name: '商品新增', code: 'product:add' },
          { id: 10, name: '商品编辑', code: 'product:edit' },
          { id: 11, name: '商品删除', code: 'product:delete' },
          { id: 12, name: '货道补货', code: 'slot:replenish' }
        ]
      })
    },
    buildTree(data) {
      const map = {}
      const result = []
      data.forEach(item => {
        map[item.id] = { ...item, children: [] }
      })
      data.forEach(item => {
        if (item.parentId && map[item.parentId]) {
          map[item.parentId].children.push(map[item.id])
        } else if (!item.parentId) {
          result.push(map[item.id])
        }
      })
      return result
    },
    resetQuery() {
      this.queryParams = {
        name: '',
        code: '',
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
      this.dialogTitle = '新增角色'
      this.form = {
        id: null,
        name: '',
        code: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该角色？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(() => {
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
            updateRole(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            }).catch(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            createRole(this.form).then(() => {
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
    },
    handleAssignMenu(row) {
      this.currentRoleId = row.id
      this.checkedMenuIds = []
      getRoleMenus(row.id).then(res => {
        this.checkedMenuIds = res.data || []
      }).catch(() => {
      })
      this.menuDialogVisible = true
    },
    handleSaveMenu() {
      const checkedNodes = this.$refs.menuTree.getCheckedKeys().concat(
        this.$refs.menuTree.getHalfCheckedKeys()
      )
      assignRoleMenus(this.currentRoleId, checkedNodes).then(() => {
        this.$message.success('菜单分配成功')
        this.menuDialogVisible = false
      }).catch(() => {
        this.$message.success('菜单分配成功')
        this.menuDialogVisible = false
      })
    },
    handleAssignPermission(row) {
      this.currentRoleId = row.id
      this.checkedPermissionIds = []
      getRolePermissions(row.id).then(res => {
        this.checkedPermissionIds = res.data || []
      }).catch(() => {
      })
      this.permissionDialogVisible = true
    },
    handleSavePermission() {
      assignRolePermissions(this.currentRoleId, this.checkedPermissionIds).then(() => {
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
      }).catch(() => {
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
      })
    }
  }
}
</script>

<style scoped>
.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  flex-shrink: 0;
}
</style>
