<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">预算管理</span>
      <div>
        <el-date-picker
          v-model="selectedMonth"
          type="month"
          placeholder="选择月份"
          value-format="YYYY-MM"
          style="margin-right: 12px"
          @change="loadBudgets"
        />
        <el-button type="primary" @click="openDialog" v-if="isAdmin">
          <el-icon><Plus /></el-icon>
          添加预算
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8" v-for="budget in budgets" :key="budget.id">
        <div class="budget-card card-shadow p-20">
          <div class="flex-between mb-16">
            <div>
              <h3>{{ budget.budgetName }}</h3>
              <el-tag size="small">{{ formatCategory(budget.category) }}</el-tag>
            </div>
            <el-dropdown v-if="isAdmin" @command="(cmd) => handleCommand(cmd, budget)">
              <el-button type="primary" link>
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">编辑</el-dropdown-item>
                  <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          
          <div class="mb-16">
            <div class="flex-between mb-8">
              <span class="text-muted">已消费</span>
              <span class="text-danger">¥{{ formatMoney(budget.spentAmount) }}</span>
            </div>
            <div class="progress-bar">
              <div 
                class="progress-bar-inner" 
                :class="getProgressClass(budget)"
                :style="{ width: getProgressPercent(budget) + '%' }"
              ></div>
            </div>
            <div class="flex-between mt-8">
              <span class="text-muted">预算总额</span>
              <span>¥{{ formatMoney(budget.totalAmount) }}</span>
            </div>
          </div>

          <div class="flex-between">
            <span class="text-muted">剩余</span>
            <span :class="budget.remainingAmount >= 0 ? 'text-success' : 'text-danger'">
              ¥{{ formatMoney(budget.remainingAmount) }}
            </span>
          </div>

          <el-button type="primary" plain style="width: 100%; margin-top: 16px" @click="openExpenseDialog(budget)">
            记一笔消费
          </el-button>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <div class="page-header" style="margin-bottom: 16px">
        <span class="page-title" style="font-size: 16px">消费记录</span>
        <el-select v-model="filterBudget" placeholder="全部预算" style="width: 150px" clearable>
          <el-option v-for="b in budgets" :key="b.id" :label="b.budgetName" :value="b.id" />
        </el-select>
      </div>
      <el-table :data="filteredExpenses" style="width: 100%">
        <el-table-column prop="expenseDate" label="日期" width="120" />
        <el-table-column prop="budgetName" label="预算分类" width="120" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="nickname" label="消费人" width="100" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            <span class="text-danger">-¥{{ formatMoney(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" v-if="isAdmin">
          <template #default="{ row }">
            <el-button type="danger" link @click="deleteExpense(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑预算' : '添加预算'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="预算名称" prop="budgetName">
          <el-input v-model="form.budgetName" placeholder="请输入预算名称" />
        </el-form-item>
        <el-form-item label="预算分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="餐饮" value="DINING" />
            <el-option label="交通" value="TRANSPORTATION" />
            <el-option label="购物" value="SHOPPING" />
            <el-option label="娱乐" value="ENTERTAINMENT" />
            <el-option label="医疗" value="MEDICAL" />
            <el-option label="教育" value="EDUCATION" />
            <el-option label="住房" value="HOUSING" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算月份" prop="budgetMonth">
          <el-date-picker v-model="form.budgetMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预算金额" prop="totalAmount">
          <el-input-number v-model="form.totalAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="expenseDialogVisible" title="记一笔消费" width="500px">
      <el-form :model="expenseForm" :rules="expenseRules" ref="expenseFormRef" label-width="100px">
        <el-form-item label="预算">
          <span class="text-primary">{{ currentBudget?.budgetName }}</span>
        </el-form-item>
        <el-form-item label="消费金额" prop="amount">
          <el-input-number v-model="expenseForm.amount" :min="0.01" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="消费日期" prop="expenseDate">
          <el-date-picker v-model="expenseForm.expenseDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="expenseForm.description" placeholder="请输入描述（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="expenseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddExpense">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { budgetApi, familyApi, API } from '@/api'

const budgets = ref([])
const expenses = ref([])
const selectedMonth = ref(new Date().toISOString().slice(0, 7))
const filterBudget = ref(null)
const dialogVisible = ref(false)
const expenseDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const expenseFormRef = ref(null)
const currentBudget = ref(null)
const isAdmin = ref(false)

const form = ref({
  id: null,
  familyId: API.currentFamilyId,
  budgetName: '',
  category: '',
  budgetMonth: '',
  totalAmount: 0
})

const expenseForm = ref({
  budgetId: null,
  userId: API.currentUserId,
  amount: 0,
  expenseDate: '',
  description: ''
})

const rules = {
  budgetName: [{ required: true, message: '请输入预算名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  budgetMonth: [{ required: true, message: '请选择月份', trigger: 'change' }],
  totalAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }]
}

const expenseRules = {
  amount: [{ required: true, message: '请输入消费金额', trigger: 'blur' }],
  expenseDate: [{ required: true, message: '请选择消费日期', trigger: 'change' }]
}

const filteredExpenses = computed(() => {
  if (!filterBudget.value) return expenses.value
  return expenses.value.filter(e => e.budgetId === filterBudget.value)
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatCategory = (cat) => {
  const map = {
    DINING: '餐饮', TRANSPORTATION: '交通', SHOPPING: '购物',
    ENTERTAINMENT: '娱乐', MEDICAL: '医疗', EDUCATION: '教育',
    HOUSING: '住房', OTHER: '其他'
  }
  return map[cat] || cat
}

const getProgressPercent = (budget) => {
  if (budget.totalAmount <= 0) return 0
  return Math.min(100, (budget.spentAmount / budget.totalAmount) * 100)
}

const getProgressClass = (budget) => {
  const percent = getProgressPercent(budget)
  if (percent >= 90) return 'warning'
  return 'normal'
}

const checkAdmin = async () => {
  try {
    isAdmin.value = await familyApi.isAdmin(API.currentFamilyId, API.currentUserId)
  } catch (e) {
    console.error(e)
  }
}

const loadBudgets = async () => {
  try {
    budgets.value = await budgetApi.listByFamily(API.currentFamilyId, selectedMonth.value)
    loadExpenses()
  } catch (e) {
    console.error(e)
  }
}

const loadExpenses = async () => {
  try {
    expenses.value = await budgetApi.getFamilyExpenses(API.currentFamilyId)
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
      familyId: API.currentFamilyId,
      budgetName: '',
      category: '',
      budgetMonth: selectedMonth.value,
      totalAmount: 0
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await budgetApi.update(form.value.id, form.value, API.currentUserId)
      ElMessage.success('更新成功')
    } else {
      await budgetApi.create(form.value, API.currentUserId)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadBudgets()
  } catch (e) {
    console.error(e)
  }
}

const handleCommand = (cmd, row) => {
  if (cmd === 'edit') {
    openDialog(row)
  } else if (cmd === 'delete') {
    ElMessageBox.confirm('确定要删除这个预算吗？', '提示', {
      type: 'warning'
    }).then(async () => {
      try {
        await budgetApi.delete(row.id, API.currentUserId)
        ElMessage.success('删除成功')
        loadBudgets()
      } catch (e) {
        console.error(e)
      }
    }).catch(() => {})
  }
}

const openExpenseDialog = (budget) => {
  currentBudget.value = budget
  expenseForm.value = {
    budgetId: budget.id,
    userId: API.currentUserId,
    amount: 0,
    expenseDate: new Date().toISOString().split('T')[0],
    description: ''
  }
  expenseDialogVisible.value = true
}

const handleAddExpense = async () => {
  try {
    await expenseFormRef.value.validate()
    await budgetApi.addExpense(expenseForm.value, API.currentUserId)
    ElMessage.success('消费已记录')
    expenseDialogVisible.value = false
    loadBudgets()
  } catch (e) {
    console.error(e)
  }
}

const deleteExpense = (row) => {
  ElMessageBox.confirm('确定要删除这条消费记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await budgetApi.deleteExpense(row.id, API.currentUserId)
      ElMessage.success('删除成功')
      loadBudgets()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  checkAdmin()
  loadBudgets()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.mb-16 { margin-bottom: 16px; }
.mb-20 { margin-bottom: 20px; }
.mt-8 { margin-top: 8px; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }

.budget-card {
  border-radius: 12px;
  margin-bottom: 20px;
}

.budget-card h3 {
  font-size: 18px;
  color: #303133;
  margin-bottom: 8px;
}

.text-muted { color: #909399; font-size: 14px; }
.text-primary { color: #409eff; }
.text-success { color: #67c23a; }
.text-danger { color: #f56c6c; }

.progress-bar {
  height: 8px;
  background: #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar-inner {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-bar-inner.warning {
  background: linear-gradient(90deg, #e6a23c, #f56c6c);
}

.progress-bar-inner.normal {
  background: linear-gradient(90deg, #409eff, #67c23a);
}
</style>
