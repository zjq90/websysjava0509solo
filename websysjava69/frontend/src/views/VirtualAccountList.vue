<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">虚拟账户</span>
      <div>
        <el-radio-group v-model="viewType" @change="loadAccounts">
          <el-radio-button value="personal">我的账户</el-radio-button>
          <el-radio-button value="family" v-if="currentFamily">家庭账户</el-radio-button>
        </el-radio-group>
        <el-button type="primary" style="margin-left: 12px" @click="openDialog">
          <el-icon><Plus /></el-icon>
          新建账户
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8" v-for="account in accounts" :key="account.id">
        <div class="account-card card-shadow p-20">
          <div class="flex-between mb-16">
            <div class="flex" style="align-items: center; gap: 12px">
              <div class="account-icon">
                <el-icon :size="24"><Wallet /></el-icon>
              </div>
              <div>
                <h3>{{ account.accountName }}</h3>
                <span class="text-muted" v-if="account.isFamilyShared">
                  <el-tag size="small" type="success">家庭共享</el-tag>
                </span>
              </div>
            </div>
            <el-dropdown @command="(cmd) => handleCommand(cmd, account)">
              <el-button type="primary" link>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="deposit">存入</el-dropdown-item>
                  <el-dropdown-item command="withdraw">取出</el-dropdown-item>
                  <el-dropdown-item command="edit" divided>编辑</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>

          <div class="balance-section">
            <div class="text-muted mb-8">当前余额</div>
            <div class="balance">¥{{ formatMoney(account.balance) }}</div>
            <div class="text-muted mt-8" v-if="account.targetAmount">
              目标: ¥{{ formatMoney(account.targetAmount) }} ({{ getTargetPercent(account) }}%)
            </div>
            <div class="progress-bar mt-8" v-if="account.targetAmount">
              <div 
                class="progress-bar-inner normal"
                :style="{ width: getTargetPercent(account) + '%' }"
              ></div>
            </div>
          </div>

          <el-divider />

          <div class="flex-between">
            <div>
              <div class="text-muted">累计存入</div>
              <div class="text-success">+¥{{ formatMoney(account.totalDeposited || 0) }}</div>
            </div>
            <div>
              <div class="text-muted">累计取出</div>
              <div class="text-danger">-¥{{ formatMoney(account.totalWithdrawn || 0) }}</div>
            </div>
            <div>
              <div class="text-muted">交易笔数</div>
              <div>{{ account.transactionCount || 0 }}</div>
            </div>
          </div>

          <el-button type="primary" plain style="width: 100%; margin-top: 16px" @click="viewTransactions(account)">
            查看交易记录
          </el-button>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20" v-if="accounts.length === 0">
      <el-empty description="暂无虚拟账户，点击上方按钮创建" />
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑账户' : '新建账户'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="form.accountName" placeholder="如：旅行基金、教育储蓄等" />
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="form.accountType" placeholder="请选择类型" style="width: 100%">
            <el-option label="储蓄账户" value="SAVINGS" />
            <el-option label="旅行基金" value="TRAVEL" />
            <el-option label="教育基金" value="EDUCATION" />
            <el-option label="应急基金" value="EMERGENCY" />
            <el-option label="投资账户" value="INVESTMENT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标金额">
          <el-input-number v-model="form.targetAmount" :min="0" :precision="2" style="width: 100%" placeholder="可选，设置储蓄目标" />
        </el-form-item>
        <el-form-item label="初始金额">
          <el-input-number v-model="form.initialBalance" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="家庭共享" v-if="currentFamily">
          <el-switch v-model="form.isFamilyShared" />
          <span class="text-muted" style="margin-left: 8px">开启后家庭成员可见</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.description" :rows="2" placeholder="请输入备注（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="transactionDialogVisible" :title="transactionType === 'deposit' ? '存入' : '取出'" width="400px">
      <el-form :model="transactionForm" :rules="transactionRules" ref="transactionFormRef" label-width="80px">
        <el-form-item label="账户">
          <span class="text-primary">{{ currentAccount?.accountName }}</span>
        </el-form-item>
        <el-form-item label="当前余额">
          <span>¥{{ formatMoney(currentAccount?.balance) }}</span>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number 
            v-model="transactionForm.amount" 
            :min="0.01" 
            :precision="2" 
            :max="transactionType === 'withdraw' ? currentAccount?.balance : undefined"
            style="width: 100%" 
          />
        </el-form-item>
        <el-form-item label="日期" prop="transactionDate">
          <el-date-picker v-model="transactionForm.transactionDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="transactionForm.description" placeholder="请输入备注（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="transactionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTransaction">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="historyDialogVisible" title="交易记录" width="700px">
      <div class="mb-16">
        <span class="text-primary">{{ currentAccount?.accountName }}</span>
        <span class="text-muted" style="margin-left: 12px">余额: ¥{{ formatMoney(currentAccount?.balance) }}</span>
      </div>
      <el-table :data="transactions" style="width: 100%" max-height="400px">
        <el-table-column prop="transactionDate" label="日期" width="120" />
        <el-table-column prop="transactionType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.transactionType === 'DEPOSIT' ? 'success' : 'danger'" size="small">
              {{ row.transactionType === 'DEPOSIT' ? '存入' : '取出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额">
          <template #default="{ row }">
            <span :class="row.transactionType === 'DEPOSIT' ? 'text-success' : 'text-danger'">
              {{ row.transactionType === 'DEPOSIT' ? '+' : '-' }}¥{{ formatMoney(row.amount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="备注" />
        <el-table-column prop="nickname" label="操作人" width="100" />
        <el-table-column label="操作" width="80" v-if="isAdmin || canDeleteTransaction">
          <template #default="{ row }">
            <el-button type="danger" link @click="deleteTransaction(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { virtualAccountApi, familyApi, API } from '@/api'

const accounts = ref([])
const transactions = ref([])
const viewType = ref('personal')
const currentFamily = ref(null)
const dialogVisible = ref(false)
const transactionDialogVisible = ref(false)
const historyDialogVisible = ref(false)
const isEdit = ref(false)
const transactionType = ref('deposit')
const formRef = ref(null)
const transactionFormRef = ref(null)
const currentAccount = ref(null)
const isAdmin = ref(false)

const form = ref({
  id: null,
  userId: API.currentUserId,
  familyId: null,
  accountName: '',
  accountType: '',
  targetAmount: null,
  initialBalance: 0,
  isFamilyShared: false,
  description: ''
})

const transactionForm = ref({
  amount: 0,
  transactionDate: '',
  description: ''
})

const rules = {
  accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
  accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }]
}

const transactionRules = {
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  transactionDate: [{ required: true, message: '请选择日期', trigger: 'change' }]
}

const canDeleteTransaction = computed(() => {
  return true
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getTargetPercent = (account) => {
  if (!account.targetAmount || account.targetAmount <= 0) return 0
  return Math.min(100, (account.balance / account.targetAmount) * 100).toFixed(1)
}

const loadFamily = async () => {
  try {
    const families = await familyApi.listByUser(API.currentUserId)
    if (families && families.length > 0) {
      currentFamily.value = families[0]
      API.currentFamilyId = currentFamily.value.id
      checkAdmin()
    }
  } catch (e) {
    console.error(e)
  }
}

const checkAdmin = async () => {
  try {
    isAdmin.value = await familyApi.isAdmin(API.currentFamilyId, API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const loadAccounts = async () => {
  try {
    if (viewType.value === 'personal') {
      accounts.value = await virtualAccountApi.listByUser(API.currentUserId)
    } else if (currentFamily.value) {
      accounts.value = await virtualAccountApi.listByFamily(currentFamily.value.id)
    }
  } catch (e) {
    console.error(e)
  }
}

const loadTransactions = async () => {
  try {
    transactions.value = await virtualAccountApi.getTransactions(currentAccount.value.id)
  } catch (e) {
    console.error(e)
  }
}

const openDialog = (row = null) => {
  isEdit.value = !!row
  if (row) {
    form.value = { ...row }
  } else {
    form.value = {
      id: null,
      userId: API.currentUserId,
      familyId: currentFamily.value?.id || null,
      accountName: '',
      accountType: '',
      targetAmount: null,
      initialBalance: 0,
      isFamilyShared: false,
      description: ''
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await virtualAccountApi.update(form.value.id, form.value, API.currentUserId)
      ElMessage.success('更新成功')
    } else {
      await virtualAccountApi.create(form.value, API.currentUserId)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadAccounts()
  } catch (e) {
    console.error(e)
  }
}

const handleCommand = (cmd, row) => {
  currentAccount.value = row
  if (cmd === 'deposit') {
    transactionType.value = 'deposit'
    openTransactionDialog()
  } else if (cmd === 'withdraw') {
    transactionType.value = 'withdraw'
    openTransactionDialog()
  } else if (cmd === 'edit') {
    openDialog(row)
  } else if (cmd === 'delete') {
    ElMessageBox.confirm('确定要删除这个账户吗？所有交易记录将被移除！', '提示', {
      type: 'warning'
    }).then(async () => {
      try {
        await virtualAccountApi.delete(row.id, API.currentUserId)
        ElMessage.success('删除成功')
        loadAccounts()
      } catch (e) {
        console.error(e)
      }
    }).catch(() => {})
  }
}

const openTransactionDialog = () => {
  transactionForm.value = {
    amount: 0,
    transactionDate: new Date().toISOString().split('T')[0],
    description: ''
  }
  transactionDialogVisible.value = true
}

const handleTransaction = async () => {
  try {
    await transactionFormRef.value.validate()
    const data = {
      ...transactionForm.value,
      transactionType: transactionType.value === 'deposit' ? 'DEPOSIT' : 'WITHDRAW',
      userId: API.currentUserId
    }
    await virtualAccountApi.createTransaction(currentAccount.value.id, data, API.currentUserId)
    ElMessage.success(transactionType.value === 'deposit' ? '存入成功' : '取出成功')
    transactionDialogVisible.value = false
    loadAccounts()
  } catch (e) {
    console.error(e)
  }
}

const viewTransactions = (account) => {
  currentAccount.value = account
  loadTransactions()
  historyDialogVisible.value = true
}

const deleteTransaction = (row) => {
  ElMessageBox.confirm('确定要删除这条交易记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await virtualAccountApi.deleteTransaction(row.id, API.currentUserId)
      ElMessage.success('删除成功')
      loadTransactions()
      loadAccounts()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadFamily()
  loadAccounts()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.mb-8 { margin-bottom: 8px; }
.mb-16 { margin-bottom: 16px; }
.mb-20 { margin-bottom: 20px; }
.mt-8 { margin-top: 8px; }
.mt-16 { margin-top: 16px; }
.flex { display: flex; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }

.account-card {
  border-radius: 12px;
  margin-bottom: 20px;
}

.account-card h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 4px;
}

.account-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.balance-section {
  padding: 16px 0;
}

.balance {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
}

.text-muted { color: #909399; font-size: 14px; }
.text-primary { color: #409eff; font-weight: 500; }
.text-success { color: #67c23a; font-weight: 500; }
.text-danger { color: #f56c6c; font-weight: 500; }

.progress-bar {
  height: 6px;
  background: #ebeef5;
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar-inner {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-bar-inner.normal {
  background: linear-gradient(90deg, #409eff, #67c23a);
}
</style>
