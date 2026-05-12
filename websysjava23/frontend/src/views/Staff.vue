<template>
  <div>
    <h2>👨‍⚕️ 医护人员管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="searchRole" placeholder="按角色筛选" style="width: 150px" clearable @change="loadData">
          <el-option label="医生" value="医生"></el-option>
          <el-option label="护士" value="护士"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">新增人员</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="staffNo" label="工号" width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="role" label="角色" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.role === '医生' ? 'primary' : 'success'" size="mini">{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="职位" width="120"></el-table-column>
        <el-table-column prop="title" label="职称" width="120"></el-table-column>
        <el-table-column prop="department" label="科室" width="120"></el-table-column>
        <el-table-column prop="specialty" label="专长" show-overflow-tooltip></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '在职' ? 'success' : 'warning'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="工号">
          <el-input v-model="form.staffNo"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="18" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="医生" value="医生"></el-option>
            <el-option label="护士" value="护士"></el-option>
            <el-option label="管理员" value="管理员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="form.position" style="width: 100%">
            <el-option label="主任医师" value="主任医师"></el-option>
            <el-option label="副主任医师" value="副主任医师"></el-option>
            <el-option label="主治医师" value="主治医师"></el-option>
            <el-option label="住院医师" value="住院医师"></el-option>
            <el-option label="护士长" value="护士长"></el-option>
            <el-option label="护士" value="护士"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="form.department" style="width: 100%">
            <el-option label="内科" value="内科"></el-option>
            <el-option label="外科" value="外科"></el-option>
            <el-option label="儿科" value="儿科"></el-option>
            <el-option label="妇产科" value="妇产科"></el-option>
            <el-option label="骨科" value="骨科"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="专长">
          <el-input v-model="form.specialty" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="在职" value="在职"></el-option>
            <el-option label="休假" value="休假"></el-option>
            <el-option label="离职" value="离职"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Staff',
  data() {
    return {
      searchRole: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增人员',
      form: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      let url = '/staff'
      if (this.searchRole === '医生') {
        url = '/staff/doctors'
      } else if (this.searchRole === '护士') {
        url = '/staff/nurses'
      }
      this.$http.get(url).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    openDialog() {
      this.dialogTitle = '新增人员'
      this.form = {}
      this.dialogVisible = true
    },
    edit(row) {
      this.dialogTitle = '编辑人员'
      this.form = Object.assign({}, row)
      this.dialogVisible = true
    },
    save() {
      if (!this.form.staffNo) {
        this.$message.warning('请输入工号')
        return
      }
      if (!this.form.name) {
        this.$message.warning('请输入姓名')
        return
      }
      
      const method = this.form.id ? 'put' : 'post'
      this.$http[method]('/staff', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    remove(id) {
      this.$confirm('确定要删除该人员吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/staff/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          }
        })
      })
    }
  }
}
</script>
