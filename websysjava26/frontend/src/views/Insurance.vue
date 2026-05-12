<template>
  <div>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header">
        <span>医保结算管理</span>
        <el-button type="primary" style="float: right" size="small" @click="showDialog">新增结算</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="settlementNo" label="结算单号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="insuranceNo" label="医保编号" width="120"></el-table-column>
        <el-table-column prop="chargeType" label="收费类型" width="100"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="insurancePayAmount" label="医保支付" width="100">
          <template slot-scope="scope">¥{{ scope.row.insurancePayAmount }}</template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自付金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.selfPayAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已结算' ? 'success' : 'warning'" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="settlementTime" label="结算时间" width="160"></el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <div style="display: flex; gap: 8px; justify-content: center;">
              <el-button size="mini" type="success" @click="completeSettlement(scope.row.id)" :disabled="scope.row.status === '已结算'">完成结算</el-button>
              <el-button size="mini" type="danger" @click="cancelSettlement(scope.row.id)" :disabled="scope.row.status === '已撤销'">撤销</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="新增医保结算" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="患者">
          <el-select v-model="form.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="p in patients" :key="p.id" :label="p.name" :value="p.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="收费类型">
          <el-select v-model="form.chargeType" placeholder="请选择收费类型">
            <el-option label="门诊" value="门诊"></el-option>
            <el-option label="住院" value="住院"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总金额">
          <el-input-number v-model="form.totalAmount" :min="0" :precision="2" style="width: 200px"></el-input-number>
        </el-form-item>
        <el-form-item label="医保范围内金额">
          <el-input-number v-model="form.withinScopeAmount" :min="0" :precision="2" style="width: 200px"></el-input-number>
        </el-form-item>
        <el-form-item label="医保支付金额">
          <el-input-number v-model="form.insurancePayAmount" :min="0" :precision="2" style="width: 200px"></el-input-number>
        </el-form-item>
        <el-form-item label="自付金额">
          <el-input-number v-model="form.selfPayAmount" :min="0" :precision="2" style="width: 200px"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSettlement">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Insurance',
  data() {
    return {
      tableData: [],
      patients: [],
      dialogVisible: false,
      form: {
        patientId: null,
        patientName: '',
        insuranceNo: '',
        chargeType: '门诊',
        totalAmount: 0,
        withinScopeAmount: 0,
        outsideScopeAmount: 0,
        insurancePayAmount: 0,
        selfPayAmount: 0
      }
    }
  },
  mounted() {
    this.loadSettlements()
    this.loadPatients()
  },
  methods: {
    loadSettlements() {
      this.$http.get('/insurance/settlements').then(res => {
        this.tableData = res.data
      })
    },
    loadPatients() {
      this.$http.get('/patients').then(res => {
        this.patients = res.data
      })
    },
    showDialog() {
      this.form = {
        patientId: null,
        patientName: '',
        insuranceNo: '',
        chargeType: '门诊',
        totalAmount: 0,
        withinScopeAmount: 0,
        outsideScopeAmount: 0,
        insurancePayAmount: 0,
        selfPayAmount: 0
      }
      this.dialogVisible = true
    },
    onPatientChange(patientId) {
      const patient = this.patients.find(p => p.id === patientId)
      if (patient) {
        this.form.patientName = patient.name
        this.form.insuranceNo = patient.insuranceNo
      }
    },
    saveSettlement() {
      this.$http.post('/insurance/settlements', this.form).then(() => {
        this.$message.success('结算记录创建成功')
        this.dialogVisible = false
        this.loadSettlements()
      })
    },
    completeSettlement(id) {
      this.$confirm('确定要完成该医保结算吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/insurance/settlements/' + id + '/complete', {
          operator: '管理员',
          insuranceResponse: '结算成功'
        }).then(() => {
          this.$message.success('结算完成')
          this.loadSettlements()
        })
      }).catch(() => {})
    },
    cancelSettlement(id) {
      this.$prompt('请输入撤销原因', '撤销确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入撤销原因'
      }).then(({ value }) => {
        this.$http.post('/insurance/settlements/' + id + '/cancel?remark=' + value).then(() => {
          this.$message.success('撤销成功')
          this.loadSettlements()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
