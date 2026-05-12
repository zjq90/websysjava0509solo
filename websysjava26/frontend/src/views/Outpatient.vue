<template>
  <div>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header">
        <span>门诊收费</span>
        <el-button type="primary" style="float: right" size="small" @click="showChargeDialog">新增收费</el-button>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="chargeNo" label="收费单号" width="150"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
        <el-table-column prop="department" label="科室" width="100"></el-table-column>
        <el-table-column prop="doctorName" label="医生" width="100"></el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="insuranceAmount" label="医保报销" width="100">
          <template slot-scope="scope">¥{{ scope.row.insuranceAmount }}</template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自付金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.selfPayAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '已缴费' ? 'success' : 'warning'" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="chargeTime" label="收费时间" width="160"></el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <div style="display: flex; gap: 8px; flex-wrap: wrap; justify-content: center;">
              <el-button size="mini" type="primary" @click="viewDetail(scope.row.id)">明细</el-button>
              <el-button size="mini" type="success" @click="settleCharge(scope.row.id)" :disabled="scope.row.status === '已缴费'">结算</el-button>
              <el-button size="mini" type="danger" @click="refundCharge(scope.row.id)" :disabled="scope.row.status !== '已缴费'">退费</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="新增门诊收费" :visible.sync="chargeDialogVisible" width="700px">
      <el-form :model="chargeForm" label-width="100px">
        <el-form-item label="患者">
          <el-select v-model="chargeForm.patientId" placeholder="请选择患者" style="width: 100%" @change="onPatientChange">
            <el-option v-for="p in patients" :key="p.id" :label="p.name" :value="p.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="科室">
          <el-input v-model="chargeForm.department" placeholder="请输入科室"></el-input>
        </el-form-item>
        <el-form-item label="医生">
          <el-input v-model="chargeForm.doctorName" placeholder="请输入医生姓名"></el-input>
        </el-form-item>
      </el-form>

      <div style="margin: 20px 0">
        <el-button type="primary" size="small" @click="showItemDialog">添加费用项目</el-button>
      </div>

      <el-table :data="chargeDetails" border stripe style="width: 100%">
        <el-table-column prop="itemType" label="项目类型" width="100"></el-table-column>
        <el-table-column prop="itemName" label="项目名称" width="150"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="100">
          <template slot-scope="scope">¥{{ scope.row.unitPrice }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.amount }}</template>
        </el-table-column>
        <el-table-column prop="isInsurance" label="医保" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isInsurance ? 'success' : 'info'" size="mini">
              {{ scope.row.isInsurance ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="removeItem(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; text-align: right; font-size: 16px; font-weight: bold">
        总金额：¥{{ totalChargeAmount.toFixed(2) }}
      </div>

      <div slot="footer">
        <el-button @click="chargeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveCharge">保存收费</el-button>
      </div>
    </el-dialog>

    <el-dialog title="添加费用项目" :visible.sync="itemDialogVisible" width="500px">
      <el-form :model="itemForm" label-width="100px">
        <el-form-item label="项目类型">
          <el-select v-model="itemForm.itemType" placeholder="请选择项目类型">
            <el-option label="药品" value="药品"></el-option>
            <el-option label="检查" value="检查"></el-option>
            <el-option label="治疗" value="治疗"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="itemForm.itemName" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="itemForm.quantity" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="itemForm.unitPrice" :min="0" :precision="2"></el-input-number>
        </el-form-item>
        <el-form-item label="医保报销">
          <el-switch v-model="itemForm.isInsurance"></el-switch>
        </el-form-item>
        <el-form-item v-if="itemForm.isInsurance" label="报销比例">
          <el-select v-model="itemForm.insuranceRatio" placeholder="请选择报销比例">
            <el-option label="50%" :value="0.5"></el-option>
            <el-option label="60%" :value="0.6"></el-option>
            <el-option label="70%" :value="0.7"></el-option>
            <el-option label="80%" :value="0.8"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addItem">添加</el-button>
      </div>
    </el-dialog>

    <el-dialog title="收费明细" :visible.sync="detailDialogVisible" width="700px">
      <el-table :data="currentDetails" border stripe style="width: 100%">
        <el-table-column prop="itemType" label="项目类型" width="100"></el-table-column>
        <el-table-column prop="itemName" label="项目名称" width="150"></el-table-column>
        <el-table-column prop="quantity" label="数量" width="80"></el-table-column>
        <el-table-column prop="unitPrice" label="单价" width="100">
          <template slot-scope="scope">¥{{ scope.row.unitPrice }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100">
          <template slot-scope="scope">¥{{ scope.row.amount }}</template>
        </el-table-column>
        <el-table-column prop="isInsurance" label="医保" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isInsurance ? 'success' : 'info'" size="mini">
              {{ scope.row.isInsurance ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="insuranceAmount" label="医保报销" width="100">
          <template slot-scope="scope">¥{{ scope.row.insuranceAmount }}</template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Outpatient',
  data() {
    return {
      tableData: [],
      patients: [],
      chargeDialogVisible: false,
      itemDialogVisible: false,
      detailDialogVisible: false,
      chargeForm: {
        patientId: null,
        patientName: '',
        department: '',
        doctorName: ''
      },
      chargeDetails: [],
      currentDetails: [],
      itemForm: {
        itemType: '药品',
        itemCode: '',
        itemName: '',
        specification: '',
        unit: '次',
        quantity: 1,
        unitPrice: 0,
        amount: 0,
        isInsurance: true,
        insuranceRatio: 0.7
      }
    }
  },
  computed: {
    totalChargeAmount() {
      return this.chargeDetails.reduce((sum, item) => sum + parseFloat(item.amount), 0)
    }
  },
  mounted() {
    this.loadCharges()
    this.loadPatients()
  },
  methods: {
    loadCharges() {
      this.$http.get('/outpatient-charges').then(res => {
        this.tableData = res.data
      })
    },
    loadPatients() {
      this.$http.get('/patients').then(res => {
        this.patients = res.data
      })
    },
    showChargeDialog() {
      this.chargeForm = {
        patientId: null,
        patientName: '',
        department: '',
        doctorName: ''
      }
      this.chargeDetails = []
      this.chargeDialogVisible = true
    },
    onPatientChange(patientId) {
      const patient = this.patients.find(p => p.id === patientId)
      if (patient) {
        this.chargeForm.patientName = patient.name
      }
    },
    showItemDialog() {
      this.itemForm = {
        itemType: '药品',
        itemCode: '',
        itemName: '',
        specification: '',
        unit: '次',
        quantity: 1,
        unitPrice: 0,
        amount: 0,
        isInsurance: true,
        insuranceRatio: 0.7
      }
      this.itemDialogVisible = true
    },
    addItem() {
      const amount = this.itemForm.quantity * this.itemForm.unitPrice
      this.chargeDetails.push({
        ...this.itemForm,
        amount: amount
      })
      this.itemDialogVisible = false
    },
    removeItem(index) {
      this.chargeDetails.splice(index, 1)
    },
    saveCharge() {
      const data = {
        ...this.chargeForm,
        details: this.chargeDetails
      }
      this.$http.post('/outpatient-charges', data).then(() => {
        this.$message.success('收费记录创建成功')
        this.chargeDialogVisible = false
        this.loadCharges()
      })
    },
    viewDetail(id) {
      this.$http.get('/outpatient-charges/' + id + '/details').then(res => {
        this.currentDetails = res.data
        this.detailDialogVisible = true
      })
    },
    settleCharge(id) {
      this.$prompt('请选择支付方式', '收费结算', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'select',
        inputPattern: /.+/,
        inputErrorMessage: '请选择支付方式',
        inputPlaceholder: '请选择支付方式',
        inputValue: '微信',
        inputOptions: [
          { value: '微信', label: '微信' },
          { value: '支付宝', label: '支付宝' },
          { value: '现金', label: '现金' },
          { value: '银行卡', label: '银行卡' }
        ]
      }).then(({ value }) => {
        this.$http.post('/outpatient-charges/' + id + '/settle?paymentMethod=' + value + '&operator=管理员').then(() => {
          this.$message.success('结算成功')
          this.loadCharges()
        })
      }).catch(() => {})
    },
    refundCharge(id) {
      this.$prompt('请输入退费原因', '退费确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请输入退费原因'
      }).then(({ value }) => {
        this.$http.post('/outpatient-charges/' + id + '/refund?remark=' + value).then(() => {
          this.$message.success('退费成功')
          this.loadCharges()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
</style>
