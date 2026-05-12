<template>
  <div>
    <h2>💰 费用管理</h2>
    
    <el-card>
      <div style="margin-bottom: 15px">
        <el-select v-model="patientId" placeholder="选择患者查看费用" style="width: 300px" clearable @change="loadByPatient">
          <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
        </el-select>
        <el-button type="success" style="margin-left: 10px" @click="openDialog">新增费用</el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="feeNo" label="费用编号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="feeType" label="费用类型" width="100"></el-table-column>
        <el-table-column prop="itemName" label="项目名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="itemSpec" label="项目规格" width="120"></el-table-column>
        <el-table-column prop="price" label="单价" width="100"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="amount" label="金额" width="100"></el-table-column>
        <el-table-column prop="operatorName" label="操作人" width="100"></el-table-column>
        <el-table-column prop="paymentStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'warning'" size="mini">{{ scope.row.paymentStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="pay(scope.row.id)" v-if="scope.row.paymentStatus === '未缴费'">缴费</el-button>
            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="新增费用" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="patient in hospitalizedList" :key="patient.id" :label="patient.patientName" :value="patient.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="费用类型">
          <el-select v-model="form.feeType" style="width: 100%">
            <el-option label="药品" value="药品"></el-option>
            <el-option label="检查" value="检查"></el-option>
            <el-option label="治疗" value="治疗"></el-option>
            <el-option label="护理" value="护理"></el-option>
            <el-option label="手术" value="手术"></el-option>
            <el-option label="床位" value="床位"></el-option>
            <el-option label="材料" value="材料"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="form.itemName"></el-input>
        </el-form-item>
        <el-form-item label="项目规格">
          <el-input v-model="form.itemSpec"></el-input>
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit"></el-input>
        </el-form-item>
        <el-form-item label="操作员">
          <el-select v-model="form.operatorId" style="width: 100%">
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
  name: 'Fee',
  data() {
    return {
      patientId: '',
      tableData: [],
      dialogVisible: false,
      form: {
        paymentStatus: '未缴费'
      },
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
      this.$http.get('/fee-record').then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data
        }
      })
    },
    loadByPatient() {
      if (this.patientId) {
        this.$http.get(`/fee-record/patient/${this.patientId}`).then(res => {
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
      this.form = { paymentStatus: '未缴费' }
      this.dialogVisible = true
    },
    save() {
      if (!this.form.patientId) {
        this.$message.warning('请选择患者')
        return
      }
      if (!this.form.itemName) {
        this.$message.warning('请输入项目名称')
        return
      }
      
      this.$http.post('/fee-record', this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadByPatient()
        } else {
          this.$message.error(res.data.message || '保存失败')
        }
      })
    },
    pay(id) {
      this.$confirm('确认该费用已缴费?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(`/fee-record/pay/${id}`).then(res => {
          if (res.data.code === 200) {
            this.$message.success('缴费成功')
            this.loadByPatient()
          }
        })
      })
    },
    remove(id) {
      this.$confirm('确定要删除该费用记录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.delete(`/fee-record/${id}`).then(res => {
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
