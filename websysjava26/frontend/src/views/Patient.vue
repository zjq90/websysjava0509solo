<template>
  <div>
    <el-card shadow="hover">
      <div slot="header">
        <span>患者管理</span>
        <el-button type="primary" style="float: right" size="small" @click="showDialog">新增患者</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="patientNo" label="患者编号" width="120"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="hasInsurance" label="医保" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.hasInsurance ? 'success' : 'info'" size="small">
              {{ scope.row.hasInsurance ? '有' : '无' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="insuranceType" label="医保类型" width="120"></el-table-column>
        <el-table-column prop="address" label="住址"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <div style="display: flex; gap: 8px; justify-content: center;">
              <el-button size="mini" type="primary" @click="editPatient(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="deletePatient(scope.row.id)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="患者编号">
          <el-input v-model="form.patientNo" placeholder="请输入患者编号"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="0" :max="150"></el-input-number>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="住址">
          <el-input v-model="form.address" type="textarea" placeholder="请输入住址"></el-input>
        </el-form-item>
        <el-form-item label="是否有医保">
          <el-switch v-model="form.hasInsurance"></el-switch>
        </el-form-item>
        <el-form-item v-if="form.hasInsurance" label="医保编号">
          <el-input v-model="form.insuranceNo" placeholder="请输入医保编号"></el-input>
        </el-form-item>
        <el-form-item v-if="form.hasInsurance" label="医保类型">
          <el-select v-model="form.insuranceType" placeholder="请选择医保类型">
            <el-option label="城镇职工" value="城镇职工"></el-option>
            <el-option label="城乡居民" value="城乡居民"></el-option>
            <el-option label="新农合" value="新农合"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePatient">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Patient',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增患者',
      form: {
        id: null,
        patientNo: '',
        name: '',
        gender: '男',
        age: 30,
        idCard: '',
        phone: '',
        address: '',
        hasInsurance: false,
        insuranceNo: '',
        insuranceType: ''
      }
    }
  },
  mounted() {
    this.loadPatients()
  },
  methods: {
    loadPatients() {
      this.$http.get('/patients').then(res => {
        this.tableData = res.data
      })
    },
    showDialog() {
      this.dialogTitle = '新增患者'
      this.form = {
        id: null,
        patientNo: 'P' + Date.now().toString().slice(-6),
        name: '',
        gender: '男',
        age: 30,
        idCard: '',
        phone: '',
        address: '',
        hasInsurance: false,
        insuranceNo: '',
        insuranceType: ''
      }
      this.dialogVisible = true
    },
    editPatient(row) {
      this.dialogTitle = '编辑患者'
      this.form = { ...row }
      this.dialogVisible = true
    },
    savePatient() {
      if (this.form.id) {
        this.$http.put('/patients/' + this.form.id, this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.loadPatients()
        })
      } else {
        this.$http.post('/patients', this.form).then(() => {
          this.$message.success('新增成功')
          this.dialogVisible = false
          this.loadPatients()
        })
      }
    },
    deletePatient(id) {
      this.$confirm('确定要删除该患者吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete('/patients/' + id).then(() => {
          this.$message.success('删除成功')
          this.loadPatients()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
