<template>
  <div>
    <el-card>
      <div slot="header">
        <span>菜单管理</span>
        <el-button style="float: right" type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增菜单</el-button>
      </div>
      
      <el-table :data="tableData" border stripe row-key="id">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="菜单名称"></el-table-column>
        <el-table-column prop="path" label="菜单路径"></el-table-column>
        <el-table-column prop="component" label="组件路径"></el-table-column>
        <el-table-column prop="icon" label="图标" width="100"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column label="类型" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.type === 1 ? '目录' : scope.row.type === 2 ? '菜单' : '按钮' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="菜单路径">
          <el-input v-model="form.path"></el-input>
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="form.component"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.icon"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0"></el-input-number>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.type">
            <el-option label="目录" :value="1"></el-option>
            <el-option label="菜单" :value="2"></el-option>
            <el-option label="按钮" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="显示" :value="1"></el-option>
            <el-option label="隐藏" :value="0"></el-option>
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
export default {
  name: 'MenuList',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        id: null,
        name: '',
        path: '',
        component: '',
        icon: '',
        sortOrder: 0,
        type: 2,
        status: 1
      },
      rules: {
        name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.tableData = [
        { id: 1, name: '系统管理', path: '/system', component: '', icon: 'setting', sortOrder: 1, type: 1, status: 1 },
        { id: 2, name: '用户管理', path: '/system/user', component: 'system/UserList', icon: 'user', sortOrder: 2, type: 2, status: 1 },
        { id: 3, name: '角色管理', path: '/system/role', component: 'system/RoleList', icon: 'team', sortOrder: 3, type: 2, status: 1 },
        { id: 4, name: '菜单管理', path: '/system/menu', component: 'system/MenuList', icon: 'menu', sortOrder: 4, type: 2, status: 1 },
        { id: 5, name: '设备管理', path: '/device', component: '', icon: 'desktop', sortOrder: 2, type: 1, status: 1 },
        { id: 6, name: '设备列表', path: '/device/list', component: 'device/DeviceList', icon: 'appstore', sortOrder: 5, type: 2, status: 1 },
        { id: 7, name: '远程控制', path: '/device/remote', component: 'device/RemoteControl', icon: 'control', sortOrder: 6, type: 2, status: 1 }
      ]
    },
    handleAdd() {
      this.dialogTitle = '新增菜单'
      this.form = {
        id: null,
        name: '',
        path: '',
        component: '',
        icon: '',
        sortOrder: 0,
        type: 2,
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该菜单？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$message.success(this.form.id ? '更新成功' : '新增成功')
          this.dialogVisible = false
          this.loadData()
        }
      })
    }
  }
}
</script>
