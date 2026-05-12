<template>
  <div>
    <h2>📝 病历管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="searchType" placeholder="按病历类型筛选" style="width: 200px" clearable @change="loadData">
          <el-option label="首次病程" value="首次病程"></el-option>
          <el-option label="病程记录" value="病程记录"></el-option>
          <el-option label="出院小结" value="出院小结"></el-option>
          <el-option label="手术记录" value="手术记录"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">新增病历</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="recordNo" label="病历编号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="recordType" label="病历类型" width="100"></el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="chiefComplaint" label="主诉" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已提交' ? 'success' : 'info'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="edit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="submit(scope.row.id)" v-if="scope.row.status === '草稿'">提交</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="病历类型">
          <el-select v-model="form.recordType" style="width: 100%">
            <el-option label="首次病程" value="首次病程"></el-option>
            <el-option label="病程记录" value="病程记录"></el-option>
            <el-option label="出院小结" value="出院小结"></el-option>
            <el-option label="手术记录" value="手术记录"></el-option>
            <el-option label="查房记录" value="查房记录"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="主诉">
          <el-input v-model="form.chiefComplaint" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="现病史">
          <el-input v-model="form.presentIllness" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="既往史">
          <el-input v-model="form.pastHistory" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="体格检查">
          <el-input v-model="form.physicalExamination" type="textarea" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="辅助检查">
          <el-input v-model="form.auxiliaryExamination" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="诊断">
          <el-input v-model="form.diagnosis" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="治疗计划">
          <el-input v-model="form.treatmentPlan" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="医生">
          <el-select v-model="form.doctorId" style="width: 100%">
            <el-option v-for="doctor in doctorList" :key="doctor.id" :label="doctor.name" :value="doctor.id"></el-option>
          </el-select>
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
  name: 'MedicalRecord',
  data() {
    return {
      searchType: '',
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增病历',
      form: {
        status: '草稿'
      },
      hospitalizedList: [],
      doctorList: []
    }
  },
  mounted() {
    this.loadData()
    this.loadRelatedData()
  },
  methods: {
    loadData() {
      let url = '/medical-record'
      if (this.searchType) {
        url = `/medical-record/type/${this.searchType}`
      }
      this.$http.get(url).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    loadRelatedData() {
      this.$http.get('/hospitalization/hospitalized').then(res => {
        if (res.data.code === 200) {
          this.hospitalizedList = res.data.data
        }
      })
      this.$http.get('/staff/doctors').then(res => {
        if (res.data.code === 200) {
          this.doctorList = res.data.data
        }
      })
    },
    onPatientChange(patientId) {
      const hospitalization = this.hospitalizedList.find(h => h.patientId === patientId)
      if (hospitalization) {
        this.form.hospitalizationId = hospitalization.id
      }
    },
    openDialog() {
      this.dialogTitle = '新增病历'
      this.form = { status: '草稿' }
      this.dialogVisible = true
    },
    edit(row) {
      this.dialogTitle = '编辑病历'
      this.form = Object.assign({}, row)
      this.dialogVisible = true
    },
    save() {
      if (!this.form.patientId) {
        this.$message.warning('请选择患者')
        return
      }
      if (!this.form.title) {
        this.$message.warning('请输入病历标题')
        return
      }
      
      const method = this.form.id ? 'put' : 'post'
      this.$http[method]('/medical-record', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    submit(id) {
      this.$confirm('确定要提交该病历吗?提交后将不能修改', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(`/medical-record/submit/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('提交成功')
            this.loadData()
          }
        })
      })
    },
    remove(id) {
      this.$confirm('确定要删除该病历吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/medical-record/${id}`).then(res => {
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
