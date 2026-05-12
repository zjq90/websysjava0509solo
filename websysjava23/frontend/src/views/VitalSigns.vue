<template>
  <div>
    <h2>❤️ 生命体征记录</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="patientId" placeholder="选择患者查看记录" style="width: 300px" clearable @change="loadByPatient">
          <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">录入体征</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="temperature" label="体温(℃)" width="100"></el-table-column>
        <el-table-column prop="pulse" label="脉搏(次/分)" width="100"></el-table-column>
        <el-table-column prop="respiration" label="呼吸(次/分)" width="100"></el-table-column>
        <el-table-column prop="systolicPressure" label="收缩压(mmHg)" width="120"></el-table-column>
        <el-table-column prop="diastolicPressure" label="舒张压(mmHg)" width="120"></el-table-column>
        <el-table-column prop="oxygenSaturation" label="血氧饱和度(%)" width="120"></el-table-column>
        <el-table-column prop="bloodGlucose" label="血糖(mmol/L)" width="120"></el-table-column>
        <el-table-column prop="painScore" label="疼痛评分" width="100"></el-table-column>
        <el-table-column prop="consciousness" label="意识状态" width="100"></el-table-column>
        <el-table-column prop="nurseName" label="护士" width="100"></el-table-column>
        <el-table-column prop="recordTime" label="记录时间" width="160"></el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="录入生命体征" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="选择患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体温(℃)">
              <el-input-number v-model="form.temperature" :min="35" :max="42" :precision="1" :step="0.1"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="脉搏(次/分)">
              <el-input-number v-model="form.pulse" :min="30" :max="200"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="呼吸(次/分)">
              <el-input-number v-model="form.respiration" :min="10" :max="50"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收缩压(mmHg)">
              <el-input-number v-model="form.systolicPressure" :min="60" :max="220"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="舒张压(mmHg)">
              <el-input-number v-model="form.diastolicPressure" :min="40" :max="130"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血氧饱和度(%)">
              <el-input-number v-model="form.oxygenSaturation" :min="80" :max="100" :precision="1" :step="0.1"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="血糖(mmol/L)">
              <el-input-number v-model="form.bloodGlucose" :min="2" :max="20" :precision="1" :step="0.1"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="疼痛评分(0-10)">
              <el-input-number v-model="form.painScore" :min="0" :max="10"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="意识状态">
          <el-select v-model="form.consciousness" style="width: 100%">
            <el-option label="清醒" value="清醒"></el-option>
            <el-option label="嗜睡" value="嗜睡"></el-option>
            <el-option label="昏睡" value="昏睡"></el-option>
            <el-option label="昏迷" value="昏迷"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="护士">
          <el-select v-model="form.nurseId" style="width: 100%">
            <el-option v-for="nurse in nurseList" :key="nurse.id" :label="nurse.name" :value="nurse.id"></el-option>
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
  name: 'VitalSigns',
  data() {
    return {
      patientId: '',
      tableData: [],
      dialogVisible: false,
      form: {},
      hospitalizedList: [],
      nurseList: []
    }
  },
  mounted() {
    this.loadData()
    this.loadRelatedData()
  },
  methods: {
    loadData() {
      this.$http.get('/vital-signs').then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    loadByPatient() {
      if (this.patientId) {
        this.$http.get(`/vital-signs/patient/${this.patientId}`).then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data
          }
        })
      } else {
        this.loadData()
      }
    },
    loadRelatedData() {
      this.$http.get('/hospitalization/hospitalized').then(res => {
        if (res.data.code === 200) {
          this.hospitalizedList = res.data.data
        }
      })
      this.$http.get('/staff/nurses').then(res => {
        if (res.data.code === 200) {
          this.nurseList = res.data.data
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
      this.form = {}
      this.dialogVisible = true
    },
    save() {
      if (!this.form.patientId) {
        this.$message.warning('请选择患者')
        return
      }
      
      this.$http.post('/vital-signs', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadByPatient()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    remove(id) {
      this.$confirm('确定要删除该记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/vital-signs/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.loadByPatient()
          }
        })
      })
    }
  }
}
</script>
