<template>
  <div class="patient-list">
    <el-card>
      <div slot="header" class="clearfix">
        <span>患者管理</span>
        <el-button style="float: right; margin-left: 10px" type="primary" icon="el-icon-plus" @click="handleAdd">新增患者</el-button>
        <el-button style="float: right" type="success" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="patients" style="width: 100%" border>
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="patientNo" label="患者编号" width="120"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="120"></el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.gender === 'MALE'" type="primary" size="small">男</el-tag>
            <el-tag v-else-if="scope.row.gender === 'FEMALE'" type="danger" size="small">女</el-tag>
            <el-tag v-else type="info" size="small">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="birthDate" label="出生日期" width="120"></el-table-column>
        <el-table-column prop="age" label="年龄" width="80"></el-table-column>
        <el-table-column prop="idCard" label="身份证号" width="180"></el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130"></el-table-column>
        <el-table-column prop="address" label="地址" width="200"></el-table-column>
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
        <el-form-item label="患者编号" required>
          <el-input v-model="form.patientNo" placeholder="请输入患者编号"></el-input>
        </el-form-item>
        <el-form-item label="患者姓名" required>
          <el-input v-model="form.patientName" placeholder="请输入患者姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" required>
          <el-select v-model="form.gender" style="width: 100%">
            <el-option label="男" value="MALE"></el-option>
            <el-option label="女" value="FEMALE"></el-option>
            <el-option label="未知" value="UNKNOWN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="form.birthDate" type="date" placeholder="选择出生日期" style="width: 100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :step="1" :min="0" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input type="textarea" v-model="form.allergyHistory" placeholder="请输入过敏史"></el-input>
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
  name: 'PatientList',
  data() {
    return {
      patients: [],
      dialogVisible: false,
      dialogTitle: '新增患者',
      isEdit: false,
      form: {
        patientNo: '',
        patientName: '',
        gender: 'MALE',
        birthDate: '',
        age: 0,
        idCard: '',
        phone: '',
        address: '',
        allergyHistory: '',
        status: 'NORMAL'
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$http.get('/api/patients').then(response => {
        this.patients = response.data
      }).catch(error => {
        console.error('加载患者失败:', error)
        this.$message.error('加载数据失败')
      })
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增患者'
      this.form = {
        patientNo: '',
        patientName: '',
        gender: 'MALE',
        birthDate: '',
        age: 0,
        idCard: '',
        phone: '',
        address: '',
        allergyHistory: '',
        status: 'NORMAL'
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑患者'
      this.form = { ...row }
      this.dialogVisible = true
    },
    saveData() {
      if (this.isEdit) {
        this.$http.put(`/api/patients/${this.form.id}`, this.form).then(() => {
          this.$message.success('更新成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('更新患者失败:', error)
          this.$message.error('操作失败')
        })
      } else {
        this.$http.post('/api/patients', this.form).then(() => {
          this.$message.success('新增成功')
          this.dialogVisible = false
          this.loadData()
        }).catch(error => {
          console.error('新增患者失败:', error)
          this.$message.error('操作失败')
        })
      }
    },
    handleDelete(id) {
      this.$confirm('确认删除该患者?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/api/patients/${id}`).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        }).catch(error => {
          console.error('删除患者失败:', error)
          this.$message.error('操作失败')
        })
      })
    }
  }
}
</script>

<style scoped>
.patient-list {
  padding: 0;
}
</style>
