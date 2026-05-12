<template>
  <div>
    <h2>📋 医嘱管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="searchStatus" placeholder="按状态筛选" style="width: 150px" clearable @change="loadData">
          <el-option label="待执行" value="待执行"></el-option>
          <el-option label="执行中" value="执行中"></el-option>
          <el-option label="已执行" value="已执行"></el-option>
          <el-option label="已停止" value="已停止"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">开具医嘱</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="orderNo" label="医嘱号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="orderType" label="类型" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.orderType === '长期' ? 'warning' : 'info'" size="mini">{{ scope.row.orderType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="80"></el-table-column>
        <el-table-column prop="name" label="医嘱名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="dosage" label="剂量" width="80"></el-table-column>
        <el-table-column prop="frequency" label="频次" width="100"></el-table-column>
        <el-table-column prop="doctorName" label="医生" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getOrderStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="executeOrder(scope.row)" v-if="scope.row.status === '待执行'">执行</el-button>
            <el-button size="mini" type="warning" @click="stopOrder(scope.row.id)" v-if="scope.row.status === '待执行' || scope.row.status === '执行中'">停止</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="开具医嘱" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="选择患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="医嘱类型">
          <el-radio-group v-model="form.orderType">
            <el-radio label="长期">长期</el-radio>
            <el-radio label="临时">临时</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="医嘱分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="药品" value="药品"></el-option>
            <el-option label="检查" value="检查"></el-option>
            <el-option label="治疗" value="治疗"></el-option>
            <el-option label="护理" value="护理"></el-option>
            <el-option label="手术" value="手术"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="医嘱名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="医嘱内容">
          <el-input v-model="form.content" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="剂量">
          <el-input v-model="form.dosage"></el-input>
        </el-form-item>
        <el-form-item label="频次">
          <el-select v-model="form.frequency" style="width: 100%">
            <el-option label="每日一次" value="每日一次"></el-option>
            <el-option label="每日二次" value="每日二次"></el-option>
            <el-option label="每日三次" value="每日三次"></el-option>
            <el-option label="每小时一次" value="每小时一次"></el-option>
            <el-option label="必要时" value="必要时"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="给药途径">
          <el-select v-model="form.route" style="width: 100%">
            <el-option label="口服" value="口服"></el-option>
            <el-option label="静脉滴注" value="静脉滴注"></el-option>
            <el-option label="肌肉注射" value="肌肉注射"></el-option>
            <el-option label="外用" value="外用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="开具医生">
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
  name: 'MedicalOrder',
  data() {
    return {
      searchStatus: '',
      tableData: [],
      dialogVisible: false,
      form: {
        orderType: '长期'
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
      let url = '/medical-order'
      if (this.searchStatus) {
        url = `/medical-order/status/${this.searchStatus}`
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
      this.form = {
        orderType: '长期'
      }
      this.dialogVisible = true
    },
    save() {
      if (!this.form.patientId) {
        this.$message.warning('请选择患者')
        return
      }
      if (!this.form.name) {
        this.$message.warning('请输入医嘱名称')
        return
      }
      
      this.$http.post('/medical-order', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('医嘱开具成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    executeOrder(row) {
      this.$prompt('请输入执行护士ID', '执行医嘱', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /\d+/,
        inputErrorMessage: '请输入有效的护士ID'
      }).then(({ value }) => {
        this.$http.post(`/medical-order/execute/${row.id}?nurseId=${value}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('医嘱执行成功')
            this.loadData()
          } else {
            this.$message.error(res.data.message || '执行失败')
          }
        })
      }).catch(() => {
      })
    },
    stopOrder(id) {
      this.$confirm('确定要停止该医嘱吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(`/medical-order/stop/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('医嘱已停止')
            this.loadData()
          }
        })
      })
    },
    remove(id) {
      this.$confirm('确定要删除该医嘱吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/medical-order/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('删除成功')
            this.loadData()
          }
        })
      })
    },
    getOrderStatusType(status) {
      const types = {
        '待执行': 'warning',
        '执行中': 'primary',
        '已执行': 'success',
        '已停止': 'info',
        '已作废': 'danger'
      }
      return types[status] || 'info'
    }
  }
}
</script>
