<template>
  <div class="department-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>科室管理</span>
        <el-button style="float: right; margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleAdd">新增科室</el-button>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="departments" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="deptCode" label="科室编号" width="120"></el-table-column>
        <el-table-column prop="deptName" label="科室名称" width="150"></el-table-column>
        <el-table-column prop="deptType" label="科室类型" width="120">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.deptType === 'CLINICAL'" type="info" size="small">临床科室</el-tag>
            <el-tag v-else-if="scope.row.deptType === 'LABORATORY'" type="success" size="small">检验科室</el-tag>
            <el-tag v-else type="warning" size="small">检查科室</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="director" label="负责人" width="120"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'ACTIVE'" type="success" size="small">启用</el-tag>
            <el-tag v-else type="info" size="small">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="科室编号" required>
          <el-input v-model="form.deptCode" placeholder="请输入科室编号"></el-input>
        </el-form-item>
        <el-form-item label="科室名称" required>
          <el-input v-model="form.deptName" placeholder="请输入科室名称"></el-input>
        </el-form-item>
        <el-form-item label="科室类型" required>
          <el-select v-model="form.deptType" style="width: 100%">
            <el-option label="临床科室" value="CLINICAL"></el-option>
            <el-option label="检验科室" value="LABORATORY"></el-option>
            <el-option label="检查科室" value="EXAMINATION"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.director" placeholder="请输入负责人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" placeholder="请输入描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DepartmentList',
  data() {
    return {
      departments: [],
      dialogVisible: false,
      dialogTitle: '新增科室',
      isEdit: false,
      form: {
        deptCode: '',
        deptName: '',
        deptType: 'CLINICAL',
        director: '',
        phone: '',
        description: '',
        status: 'ACTIVE'
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/departments').then(response => {
        this.departments = response.data
      }).catch(error => {
        console.error('加载科室失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增科室'
      this.form = {
        deptCode: '',
        deptName: '',
        deptType: 'CLINICAL',
        director: '',
        phone: '',
        description: '',
        status: 'ACTIVE'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑科室'
      this.form = { ...row }
      this.dialogVisible = true
    },
    saveData() {
      if (this.isEdit) {
        this.$http.put(`/api/departments/${this.form.id}`, this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('更新科室失败:', error)
          this.$message.error('操作失败')
        })
      } else {
        this.$http.post('/api/departments', this.form).then(() => {
          this.$message.success('新增成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('新增科室失败:', error)
          this.$message.error('操作失败')
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确认删除该科室?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/departments/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(error => {
          console.error('删除科室失败:', error)
          this.$message.error('操作失败')
        })
      })
    }
  }
}
</script>

<style scoped>
.department-list {
  padding: 0;
}
</style>
