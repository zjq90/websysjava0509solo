<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">债务管理</span>
      <div>
        <el-button type="primary" @click="openSimulateDialog">
          <el-icon><Calculator /></el-icon>
          还款模拟
        </el-button>
        <el-button type="primary" @click="openDialog">
          <el-icon><Plus /></el-icon>
          添加债务
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-label">债务总数</div>
          <div class="stat-value">{{ debts.length }}</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card orange">
          <div class="stat-label">待还总额</div>
          <div class="stat-value">¥{{ formatMoney(totalRemaining) }}</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card blue">
          <div class="stat-label">月供总额</div>
          <div class="stat-value">¥{{ formatMoney(totalMonthlyPayment) }}</div>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <el-table :data="debts" style="width: 100%" v-loading="loading">
        <el-table-column prop="debtName" label="债务名称" />
        <el-table-column prop="debtType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ formatDebtType(row.debtType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creditor" label="债权人" />
        <el-table-column prop="principalAmount" label="本金" width="120">
          <template #default="{ row }">¥{{ formatMoney(row.principalAmount) }}</template>
        </el-table-column>
        <el-table-column prop="remainingAmount" label="待还金额" width="120">
          <template #default="{ row }">¥{{ formatMoney(row.remainingAmount) }}</template>
        </el-table-column>
        <el-table-column prop="annualInterestRate" label="年利率" width="100">
          <template #default="{ row }">{{ row.annualInterestRate }}%</template>
        </el-table-column>
        <el-table-column prop="monthlyPayment" label="月供" width="100">
          <template #default="{ row }">¥{{ formatMoney(row.monthlyPayment) }}</template>
        </el-table-column>
        <el-table-column prop="nextPaymentDate" label="还款日" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.nextPaymentDate" :type="getDaysLeft(row.nextPaymentDate) <= 3 ? 'danger' : 'warning'" size="small">
              {{ row.nextPaymentDate }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isPaidOff" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isPaidOff ? 'success' : 'warning'" size="small">
              {{ row.isPaidOff ? '已结清' : '未结清' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="makePayment(row)">还款</el-button>
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑债务' : '添加债务'" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="债务名称" prop="debtName">
          <el-input v-model="form.debtName" placeholder="请输入债务名称" />
        </el-form-item>
        <el-form-item label="债务类型" prop="debtType">
          <el-select v-model="form.debtType" placeholder="请选择债务类型">
            <el-option label="信用卡" value="CREDIT_CARD" />
            <el-option label="个人贷款" value="PERSONAL_LOAN" />
            <el-option label="房贷" value="MORTGAGE" />
            <el-option label="车贷" value="CAR_LOAN" />
            <el-option label="学生贷款" value="STUDENT_LOAN" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="债权人" prop="creditor">
          <el-input v-model="form.creditor" placeholder="请输入债权人/机构" />
        </el-form-item>
        <el-form-item label="本金" prop="principalAmount">
          <el-input-number v-model="form.principalAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="待还金额" prop="remainingAmount">
          <el-input-number v-model="form.remainingAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="年利率(%)" prop="annualInterestRate">
          <el-input-number v-model="form.annualInterestRate" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="还款方式" prop="repaymentMethod">
          <el-select v-model="form.repaymentMethod" placeholder="请选择还款方式">
            <el-option label="等额本息" value="EQUAL_PRINCIPAL_INTEREST" />
            <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
            <el-option label="先息后本" value="INTEREST_FIRST_THEN_PRINCIPAL" />
            <el-option label="到期一次还本付息" value="ONE_TIME_REPAYMENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="分期期数(月)" prop="loanTermMonths">
          <el-input-number v-model="form.loanTermMonths" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="下次还款日" prop="nextPaymentDate">
          <el-date-picker v-model="form.nextPaymentDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input type="textarea" v-model="form.notes" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="paymentDialogVisible" title="还款" width="400px">
      <el-form :model="paymentForm" ref="paymentFormRef" label-width="80px">
        <el-form-item label="债务名称">
          <span>{{ currentDebt?.debtName }}</span>
        </el-form-item>
        <el-form-item label="待还金额">
          <span class="text-danger">¥{{ formatMoney(currentDebt?.remainingAmount) }}</span>
        </el-form-item>
        <el-form-item label="还款金额" prop="paymentAmount">
          <el-input-number v-model="paymentForm.paymentAmount" :min="0.01" :precision="2" :max="currentDebt?.remainingAmount" style="width: 100%" />
        </el-form-item>
        <el-form-item label="还款日期" prop="paymentDate">
          <el-date-picker v-model="paymentForm.paymentDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="paymentForm.notes" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="paymentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePayment">确认还款</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="simulateDialogVisible" title="还款方式模拟" width="700px">
      <el-form :model="simulateForm" ref="simulateFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="贷款本金" prop="principal">
              <el-input-number v-model="simulateForm.principal" :min="1" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年利率(%)" prop="annualInterestRate">
              <el-input-number v-model="simulateForm.annualInterestRate" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="贷款期限(月)" prop="loanTermMonths">
              <el-input-number v-model="simulateForm.loanTermMonths" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="还款方式" prop="repaymentMethod">
              <el-select v-model="simulateForm.repaymentMethod" style="width: 100%">
                <el-option label="等额本息" value="EQUAL_PRINCIPAL_INTEREST" />
                <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="doSimulate" style="width: 100%">计算还款计划</el-button>
      </el-form>
      
      <div v-if="simulateResult" class="mt-20">
        <el-alert 
          :title="`总还款: ¥${formatMoney(simulateResult.totalPayment)} | 总利息: ¥${formatMoney(simulateResult.totalInterest)}`" 
          type="info" 
          :closable="false"
          class="mb-16"
        />
        <el-table :data="simulateResult.repaymentPlans" style="width: 100%" max-height="300px">
          <el-table-column prop="period" label="期数" width="60" />
          <el-table-column prop="paymentDate" label="还款日期" width="120" />
          <el-table-column prop="paymentAmount" label="月供">
            <template #default="{ row }">¥{{ formatMoney(row.paymentAmount) }}</template>
          </el-table-column>
          <el-table-column prop="principalPart" label="本金">
            <template #default="{ row }">¥{{ formatMoney(row.principalPart) }}</template>
          </el-table-column>
          <el-table-column prop="interestPart" label="利息">
            <template #default="{ row }">¥{{ formatMoney(row.interestPart) }}</template>
          </el-table-column>
          <el-table-column prop="remainingPrincipal" label="剩余本金">
            <template #default="{ row }">¥{{ formatMoney(row.remainingPrincipal) }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { debtApi, API } from '@/api'

const loading = ref(false)
const debts = ref([])
const dialogVisible = ref(false)
const paymentDialogVisible = ref(false)
const simulateDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const paymentFormRef = ref(null)
const simulateFormRef = ref(null)
const currentDebt = ref(null)
const simulateResult = ref(null)

const form = ref({
  id: null,
  userId: API.currentUserId,
  debtName: '',
  debtType: '',
  creditor: '',
  principalAmount: 0,
  remainingAmount: 0,
  annualInterestRate: 0,
  repaymentMethod: '',
  loanTermMonths: 12,
  nextPaymentDate: '',
  notes: '',
  isPaidOff: false
})

const paymentForm = ref({
  paymentAmount: 0,
  paymentDate: '',
  notes: ''
})

const simulateForm = ref({
  principal: 100000,
  annualInterestRate: 4.2,
  loanTermMonths: 360,
  repaymentMethod: 'EQUAL_PRINCIPAL_INTEREST'
})

const rules = {
  debtName: [{ required: true, message: '请输入债务名称', trigger: 'blur' }],
  debtType: [{ required: true, message: '请选择债务类型', trigger: 'change' }],
  principalAmount: [{ required: true, message: '请输入本金', trigger: 'blur' }],
  remainingAmount: [{ required: true, message: '请输入待还金额', trigger: 'blur' }],
  annualInterestRate: [{ required: true, message: '请输入年利率', trigger: 'blur' }],
  repaymentMethod: [{ required: true, message: '请选择还款方式', trigger: 'change' }],
  loanTermMonths: [{ required: true, message: '请输入分期期数', trigger: 'blur' }]
}

const totalRemaining = computed(() => {
  return debts.value.filter(d => !d.isPaidOff).reduce((sum, d) => sum + Number(d.remainingAmount || 0), 0)
})

const totalMonthlyPayment = computed(() => {
  return debts.value.filter(d => !d.isPaidOff).reduce((sum, d) => sum + Number(d.monthlyPayment || 0), 0)
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDebtType = (type) => {
  const map = { CREDIT_CARD: '信用卡', PERSONAL_LOAN: '个人贷款', MORTGAGE: '房贷', CAR_LOAN: '车贷', STUDENT_LOAN: '学生贷款', OTHER: '其他' }
  return map[type] || type
}

const getDaysLeft = (date) => {
  if (!date) return 999
  const today = new Date()
  const target = new Date(date)
  return Math.ceil((target - today) / (1000 * 60 * 60 * 24))
}

const loadDebts = async () => {
  loading.value = true
  try {
    debts.value = await debtApi.list(API.currentUserId)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const openDialog = (row = null) => {
  if (row && row.id) {
    isEdit.value = true
    form.value = {
      id: row.id,
      userId: row.userId || API.currentUserId,
      debtName: row.debtName || '',
      debtType: row.debtType || '',
      creditor: row.creditor || '',
      principalAmount: row.principalAmount || 0,
      remainingAmount: row.remainingAmount || 0,
      annualInterestRate: row.annualInterestRate || 0,
      repaymentMethod: row.repaymentMethod || '',
      loanTermMonths: row.loanTermMonths || 12,
      nextPaymentDate: row.nextPaymentDate || '',
      notes: row.notes || '',
      isPaidOff: row.isPaidOff || false
    }
  } else {
    isEdit.value = false
    form.value = {
      id: null,
      userId: API.currentUserId,
      debtName: '',
      debtType: '',
      creditor: '',
      principalAmount: 0,
      remainingAmount: 0,
      annualInterestRate: 0,
      repaymentMethod: '',
      loanTermMonths: 12,
      nextPaymentDate: '',
      notes: '',
      isPaidOff: false
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (form.value.id && isEdit.value) {
      await debtApi.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      const submitData = { ...form.value }
      delete submitData.id
      await debtApi.create(submitData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadDebts()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条债务记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await debtApi.delete(row.id)
      ElMessage.success('删除成功')
      loadDebts()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const makePayment = (row) => {
  currentDebt.value = row
  paymentForm.value = {
    paymentAmount: row.monthlyPayment || 0,
    paymentDate: new Date().toISOString().split('T')[0],
    notes: ''
  }
  paymentDialogVisible.value = true
}

const handlePayment = async () => {
  try {
    await debtApi.makePayment(currentDebt.value.id, paymentForm.value)
    ElMessage.success('还款成功')
    paymentDialogVisible.value = false
    loadDebts()
  } catch (e) {
    console.error(e)
  }
}

const openSimulateDialog = () => {
  simulateResult.value = null
  simulateDialogVisible.value = true
}

const doSimulate = async () => {
  try {
    simulateResult.value = await debtApi.simulate(simulateForm.value)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadDebts()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.mt-20 { margin-top: 20px; }
.mb-16 { margin-bottom: 16px; }
</style>
