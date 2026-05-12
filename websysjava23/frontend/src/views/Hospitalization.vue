<template>
  <div>
    <h2>🏥 住院登记管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-button type="success" @click="openDialog">办理入院</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="hospitalNo" label="住院号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="bedNo" label="床位号" width="100"></el-table-column>
        <el-table-column prop="wardName" label="病区" width="120"></el-table-column>
        <el-table-column prop="department" label="科室" width="100"></el-table-column>
        <el-table-column prop="doctorName" label="主治医生" width="100"></el-table-column>
        <el-table-column prop="admissionDate" label="入院日期" width="160"></el-table-column>
        <el-table-column prop="admissionDiagnosis" label="入院诊断" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '住院中' ? 'success' : 'info'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" @click="discharge(scope.row)" v-if="scope.row.status === '住院中'">办理出院</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="办理入院" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="选择患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%">
            <el-option v-for="patient in patientList" :key="patient.id" :label="patient.name" :value="patient.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择床位">
          <el-select v-model="form.bedId" placeholder="请选择床位" style="width: 100%">
            <el-option v-for="bed in availableBeds" :key="bed.id" :label="bed.bedNo + ' - ' + bed.wardName" :value="bed.id"></el-option>
          </el-select>
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
        <el-form-item label="主治医生">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%">
            <el-option v-for="doctor in doctorList" :key="doctor.id" :label="doctor.name" :value="doctor.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="责任护士">
          <el-select v-model="form.nurseId" placeholder="请选择护士" style="width: 100%">
            <el-option v-for="nurse in nurseList" :key="nurse.id" :label="nurse.name" :value="nurse.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="入院诊断">
          <el-input v-model="form.admissionDiagnosis" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="admission">办理入院</el-button>
      </div>
    </el-dialog>

    <el-dialog title="办理出院" :visible.sync="dischargeDialogVisible" width="500px">
      <el-form :model="dischargeForm" label-width="120px">
        <el-form-item label="患者姓名">
          <span>{{ dischargeForm.patientName }}</span>
        </el-form-item>
        <el-form-item label="住院号">
          <span>{{ dischargeForm.hospitalNo }}</span>
        </el-form-item>
        <el-form-item label="出院诊断">
          <el-input v-model="dischargeForm.dischargeDiagnosis" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dischargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmDischarge">确认出院</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Hospitalization',
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dischargeDialogVisible: false,
      form: {},
      dischargeForm: {},
      patientList: [],
      availableBeds: [],
      doctorList: [],
      nurseList: []
    }
  },
  mounted() {
    this.loadData()
    this.loadRelatedData()
  },
  methods: {
    loadData() {
      this.$http.get('/hospitalization/hospitalized').then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    loadRelatedData() {
      this.$http.get('/patient/status/正常').then(res => {
        if (res.data.code === 200) {
          this.patientList = res.data.data
        }
      })
      this.$http.get('/bed/status/空闲').then(res => {
        if (res.data.code === 200) {
          this.availableBeds = res.data.data
        }
      })
      this.$http.get('/staff/doctors').then(res => {
        if (res.data.code === 200) {
          this.doctorList = res.data.data
        }
      })
      this.$http.get('/staff/nurses').then(res => {
        if (res.data.code === 200) {
          this.nurseList = res.data.data
        }
      })
    },
    openDialog() {
      this.form = {}
      this.dialogVisible = true
    },
    admission() {
      if (!this.form.patientId) {
        this.$message.warning('请选择患者')
        return
      }
      if (!this.form.bedId) {
        this.$message.warning('请选择床位')
        return
      }
      
      this.$http.post('/hospitalization/admission', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('入院办理成功')
          this.dialogVisible = false
          this.loadData()
          this.loadRelatedData()
        } else {
          this.$message.error(res.data.message || '办理失败')
        }
      })
    },
    discharge(row) {
      this.dischargeForm = Object.assign({}, row)
      this.dischargeDialogVisible = true
    },
    confirmDischarge() {
      if (!this.dischargeForm.dischargeDiagnosis) {
        this.$message.warning('请填写出院诊断')
        return
      }
      
      this.$http.post(`/hospitalization/discharge/${this.dischargeForm.id}?dischargeDiagnosis=${this.dischargeForm.dischargeDiagnosis}`).then(res => {
        if (res.data.code === 200) {
          this.$message.success('出院办理成功')
          this.dischargeDialogVisible = false
          this.loadData()
          this.loadRelatedData()
        } else {
          this.$message.error(res.data.message || '办理失败')
        }
      })
    },
    remove(id) {
      this.$confirm('确定要删除该记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/hospitalization/${id}`).then(res => {
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
