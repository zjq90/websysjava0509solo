<template>
  <div>
    <h2>👤 患者信息管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-input v-model="searchKeyword" placeholder="搜索患者姓名或电话" style="width: 300px" clearable></el-input>
        <el-button type="primary" style="margin-left: 10px" @click="search">搜索</el-button>
        <el-button type="success" style="float: right" @click="openDialog">新增患者</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="patientNo" label="患者编号" width="150"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="address" label="住址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '正常' ? 'success' : 'warning'" size="mini">{{ scope.row.status }}</el-tag>
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
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="0" :max="150"></el-input-number>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="住址">
          <el-input v-model="form.address" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="婚否">
          <el-select v-model="form.maritalStatus">
            <el-option label="已婚" value="已婚"></el-option>
            <el-option label="未婚" value="未婚"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="职业">
          <el-input v-model="form.occupation"></el-input>
        </el-form-item>
        <el-form-item label="既往病史">
          <el-input v-model="form.medicalHistory" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input v-model="form.allergyHistory" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Patient',
  data() {
    return {
      searchKeyword: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增患者',
      form: {}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/patient').then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    search() {
      if (this.searchKeyword) {
        this.$http.get(`/patient/search?keyword=${this.searchKeyword}`).then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data
          }
        })
      } else {
        this.loadData()
      }
    },
    openDialog() {
      this.dialogTitle = '新增患者'
      this.form = {}
      this.dialogVisible = true
    },
    edit(row) {
      this.dialogTitle = '编辑患者'
      this.form = Object.assign({}, row)
      this.dialogVisible = true
    },
    save() {
      if (!this.form.name) {
        this.$message.warning('请输入患者姓名')
        return
      }
      
      const method = this.form.id ? 'put' : 'post'
      this.$http[method]('/patient', this.form).then(res => {
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
      this.$confirm('确定要删除该患者吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/patient/${id}`).then(res => {
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
