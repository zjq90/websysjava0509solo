<template>
  <!--
    预算与目标页面
    包含预算设置、预算进度条、储蓄目标跟踪功能
  -->
  <div class="budget">
    <!-- 预算设置 -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">分类月度预算设置</span>
        <el-button type="primary" size="small" @click="showAddBudgetDialog">
          <el-icon><Plus /></el-icon>
          新增预算
        </el-button>
      </div>
      <div class="budget-grid">
        <div
          v-for="item in budgetProgress"
          :key="item.id"
          class="budget-card"
          :class="getBudgetStatusClass(item)"
        >
          <div class="budget-header">
            <div class="budget-category">
              <span class="category-dot" :style="{ background: item.categoryColor }"></span>
              <span class="category-name">{{ item.categoryName }}</span>
            </div>
            <el-dropdown @command="(cmd) => handleBudgetCommand(cmd, item)">
              <el-button type="primary" text size="small">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">编辑预算</el-dropdown-item>
                  <el-dropdown-item command="reset">重置本月</el-dropdown-item>
                  <el-dropdown-item command="delete" class="danger-text">删除预算</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          
          <div class="budget-amounts">
            <div class="spent">
              <span class="label">已支出</span>
              <span class="value expense">¥{{ formatMoney(item.spent) }}</span>
            </div>
            <div class="total">
              <span class="label">预算</span>
              <span class="value">¥{{ formatMoney(item.budgetAmount) }}</span>
            </div>
          </div>
          
          <div class="progress-section">
            <div class="progress-info">
              <span class="remaining" :class="item.remaining >= 0 ? 'income' : 'expense'">
                {{ item.remaining >= 0 ? '剩余' : '超支' }}: ¥{{ formatMoney(Math.abs(item.remaining)) }}
              </span>
              <span class="percentage">{{ item.progress.toFixed(1) }}%</span>
            </div>
            <el-progress
              :percentage="Math.min(item.progress, 100)"
              :color="getProgressColor(item.progress)"
              :stroke-width="12"
              :show-text="false"
            />
          </div>
          
          <div class="budget-footer">
            <span class="text-muted">月度预算</span>
            <el-tag v-if="item.progress >= 100" type="danger" size="small">已超支</el-tag>
            <el-tag v-else-if="item.progress >= 80" type="warning" size="small">即将超支</el-tag>
            <el-tag v-else type="success" size="small">正常</el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 预算概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <div class="stat-card primary-card">
          <div class="stat-icon">
            <el-icon :size="32"><PieChart /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">总预算</div>
            <div class="stat-value">¥{{ formatMoney(budgetSummary.totalBudget) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card success-card">
          <div class="stat-icon">
            <el-icon :size="32"><Coin /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">已支出</div>
            <div class="stat-value expense">¥{{ formatMoney(budgetSummary.totalSpent) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card warning-card">
          <div class="stat-icon">
            <el-icon :size="32"><AlarmClock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">剩余预算</div>
            <div class="stat-value" :class="budgetSummary.remaining >= 0 ? 'income' : 'expense'">
              ¥{{ formatMoney(budgetSummary.remaining) }}
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 储蓄目标 -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">储蓄目标</span>
        <el-button type="primary" size="small" @click="showAddGoalDialog">
          <el-icon><Plus /></el-icon>
          新增目标
        </el-button>
      </div>
      
      <el-empty v-if="savingGoals.length === 0" description="还没有储蓄目标，赶快创建一个吧！" />
      
      <div v-else class="goals-grid">
        <div
          v-for="goal in savingGoals"
          :key="goal.id"
          class="goal-card"
        >
          <div class="goal-header">
            <div class="goal-icon" :style="{ background: goal.color + '20', color: goal.color }">
              <el-icon :size="24"><Target /></el-icon>
            </div>
            <div class="goal-info">
              <h3 class="goal-name">{{ goal.name }}</h3>
              <p class="goal-deadline">目标日期: {{ formatDate(goal.targetDate) }}</p>
            </div>
          </div>
          
          <div class="goal-amounts">
            <div>
              <span class="text-muted">已存入</span>
              <div class="amount income">¥{{ formatMoney(goal.currentAmount) }}</div>
            </div>
            <div class="arrow">
              <el-icon><Right /></el-icon>
            </div>
            <div>
              <span class="text-muted">目标金额</span>
              <div class="amount">¥{{ formatMoney(goal.targetAmount) }}</div>
            </div>
          </div>
          
          <div class="goal-progress">
            <div class="progress-info">
              <span>进度: {{ goal.progress.toFixed(1) }}%</span>
              <span>还差: ¥{{ formatMoney(goal.remaining) }}</span>
            </div>
            <el-progress
              :percentage="Math.min(goal.progress, 100)"
              :color="goal.color"
              :stroke-width="14"
              :show-text="false"
            />
          </div>
          
          <div class="goal-actions">
            <el-button size="small" @click="depositToGoal(goal)">
              <el-icon><Plus /></el-icon>
              存入
            </el-button>
            <el-button size="small" @click="withdrawFromGoal(goal)">
              <el-icon><Minus /></el-icon>
              取出
            </el-button>
            <el-button size="small" type="primary" @click="editGoal(goal)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
          </div>
          
          <div class="goal-tips" v-if="goal.monthlyContribution > 0">
            <el-tag type="info" size="small">
              每月需存 ¥{{ formatMoney(goal.monthlyContribution) }} 即可达成
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 预算建议 -->
    <div class="card">
      <div class="card-header">
        <span class="card-title">智能预算建议</span>
        <el-button type="primary" link size="small" @click="generateSuggestions">
          <el-icon><MagicStick /></el-icon>
          重新生成
        </el-button>
      </div>
      <div class="suggestions-list">
        <div v-for="(suggestion, index) in suggestions" :key="index" class="suggestion-item">
          <el-icon class="suggestion-icon" :class="suggestion.type">
            <component :is="getSuggestionIcon(suggestion.type)" />
          </el-icon>
          <div class="suggestion-content">
            <div class="suggestion-title">{{ suggestion.title }}</div>
            <div class="suggestion-desc">{{ suggestion.description }}</div>
          </div>
          <el-button type="primary" link size="small" @click="applySuggestion(suggestion)">
            应用
          </el-button>
        </div>
      </div>
    </div>

    <!-- 新增/编辑预算弹窗 -->
    <el-dialog v-model="budgetDialogVisible" :title="isEditBudget ? '编辑预算' : '新增预算'" width="500px">
      <el-form ref="budgetFormRef" :model="budgetForm" :rules="budgetFormRules" label-width="80px">
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="budgetForm.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            >
              <span :style="{ color: cat.color }">●</span>
              {{ cat.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预算金额" prop="budgetAmount">
          <el-input-number
            v-model="budgetForm.budgetAmount"
            :min="0"
            :precision="2"
            :step="100"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="生效月份" prop="month">
          <el-date-picker
            v-model="budgetForm.month"
            type="month"
            value-format="YYYY-MM"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="budgetForm.notes" type="textarea" :rows="3" placeholder="可选" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="budgetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBudget">确定</el-button>
      </template>
    </el-dialog>

    <!-- 新增/编辑储蓄目标弹窗 -->
    <el-dialog v-model="goalDialogVisible" :title="isEditGoal ? '编辑目标' : '新增储蓄目标'" width="500px">
      <el-form ref="goalFormRef" :model="goalForm" :rules="goalFormRules" label-width="100px">
        <el-form-item label="目标名称" prop="name">
          <el-input v-model="goalForm.name" placeholder="如：旅行基金" />
        </el-form-item>
        <el-form-item label="目标金额" prop="targetAmount">
          <el-input-number
            v-model="goalForm.targetAmount"
            :min="0"
            :precision="2"
            :step="1000"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="已存金额" prop="currentAmount">
          <el-input-number
            v-model="goalForm.currentAmount"
            :min="0"
            :precision="2"
            :step="100"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="目标日期" prop="targetDate">
          <el-date-picker
            v-model="goalForm.targetDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="图标颜色">
          <el-color-picker v-model="goalForm.color" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="goalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGoal">确定</el-button>
      </template>
    </el-dialog>

    <!-- 存入/取出金额弹窗 -->
    <el-dialog v-model="depositDialogVisible" :title="depositType === 'deposit' ? '存入金额' : '取出金额'" width="400px">
      <el-form label-width="80px">
        <el-form-item label="目标">
          <span>{{ currentGoal?.name }}</span>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number
            v-model="depositAmount"
            :min="0"
            :precision="2"
            :step="100"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="depositDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDeposit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { budgetApi, savingGoalApi, categoryApi } from '@/api'

const budgetProgress = ref([])
const savingGoals = ref([])
const categories = ref([])

const budgetSummary = reactive({
  totalBudget: 0,
  totalSpent: 0,
  remaining: 0
})

const suggestions = ref([])

const budgetDialogVisible = ref(false)
const goalDialogVisible = ref(false)
const depositDialogVisible = ref(false)
const isEditBudget = ref(false)
const isEditGoal = ref(false)
const editBudgetId = ref(null)
const editGoalId = ref(null)
const currentGoal = ref(null)
const depositType = ref('deposit')
const depositAmount = ref(0)

const budgetFormRef = ref(null)
const goalFormRef = ref(null)

const budgetForm = reactive({
  categoryId: null,
  budgetAmount: 0,
  month: dayjs().format('YYYY-MM'),
  notes: ''
})

const goalForm = reactive({
  name: '',
  targetAmount: 0,
  currentAmount: 0,
  targetDate: dayjs().add(1, 'year').format('YYYY-MM-DD'),
  color: '#409eff'
})

const budgetFormRules = {
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  budgetAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }],
  month: [{ required: true, message: '请选择生效月份', trigger: 'change' }]
}

const goalFormRules = {
  name: [{ required: true, message: '请输入目标名称', trigger: 'blur' }],
  targetAmount: [{ required: true, message: '请输入目标金额', trigger: 'blur' }],
  currentAmount: [{ required: true, message: '请输入已存金额', trigger: 'blur' }],
  targetDate: [{ required: true, message: '请选择目标日期', trigger: 'change' }]
}

const formatMoney = (value) => {
  if (value === null || value === undefined) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const getBudgetStatusClass = (item) => {
  if (item.progress >= 100) return 'over-budget'
  if (item.progress >= 80) return 'warning-budget'
  return 'normal-budget'
}

const getProgressColor = (progress) => {
  if (progress >= 100) return '#f56c6c'
  if (progress >= 80) return '#e6a23c'
  return '#67c23a'
}

const getSuggestionIcon = (type) => {
  const icons = {
    warning: 'WarningFilled',
    info: 'InfoFilled',
    success: 'CircleCheckFilled'
  }
  return icons[type] || 'InfoFilled'
}

const loadBudgetProgress = async () => {
  try {
    budgetProgress.value = await budgetApi.getBudgetProgress(dayjs().format('YYYY-MM'))
    budgetSummary.totalBudget = budgetProgress.value.reduce((sum, item) => sum + item.budgetAmount, 0)
    budgetSummary.totalSpent = budgetProgress.value.reduce((sum, item) => sum + item.spent, 0)
    budgetSummary.remaining = budgetSummary.totalBudget - budgetSummary.totalSpent
  } catch (error) {
    console.error('加载预算进度失败:', error)
  }
}

const loadSavingGoals = async () => {
  try {
    savingGoals.value = await savingGoalApi.getAllSavingGoals()
  } catch (error) {
    console.error('加载储蓄目标失败:', error)
  }
}

const loadCategories = async () => {
  try {
    categories.value = await categoryApi.getAllCategories()
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const generateSuggestions = () => {
  suggestions.value = [
    {
      type: 'warning',
      title: '餐饮支出占比过高',
      description: '本月餐饮支出已达预算的85%，建议减少外出就餐次数',
      action: { category: '餐饮', reduceBy: 500 }
    },
    {
      type: 'info',
      title: '交通预算有剩余',
      description: '交通分类本月仅使用40%预算，可考虑优化预算分配',
      action: { category: '交通', transferTo: '娱乐', amount: 200 }
    },
    {
      type: 'success',
      title: '整体预算控制良好',
      description: '目前总支出占总预算的65%，继续保持良好的消费习惯',
      action: null
    }
  ]
}

const handleBudgetCommand = (command, item) => {
  if (command === 'edit') {
    isEditBudget.value = true
    editBudgetId.value = item.id
    Object.assign(budgetForm, {
      categoryId: item.categoryId,
      budgetAmount: item.budgetAmount,
      month: item.month,
      notes: ''
    })
    budgetDialogVisible.value = true
  } else if (command === 'reset') {
    ElMessageBox.confirm('确定要重置本月该分类的已支出金额吗？', '提示', {
      type: 'warning'
    }).then(() => {
      ElMessage.success('已重置')
    }).catch(() => {})
  } else if (command === 'delete') {
    ElMessageBox.confirm('确定要删除这个预算吗？', '删除确认', {
      type: 'warning'
    }).then(async () => {
      try {
        await budgetApi.deleteBudget(item.id)
        ElMessage.success('删除成功')
        loadBudgetProgress()
      } catch (error) {
        console.error('删除预算失败:', error)
        ElMessage.error('删除失败')
      }
    }).catch(() => {})
  }
}

const showAddBudgetDialog = () => {
  isEditBudget.value = false
  editBudgetId.value = null
  budgetFormRef.value?.resetFields()
  Object.assign(budgetForm, {
    categoryId: null,
    budgetAmount: 0,
    month: dayjs().format('YYYY-MM'),
    notes: ''
  })
  budgetDialogVisible.value = true
}

const submitBudget = async () => {
  await budgetFormRef.value.validate()
  try {
    if (isEditBudget.value) {
      await budgetApi.updateBudget(editBudgetId.value, budgetForm)
      ElMessage.success('更新成功')
    } else {
      await budgetApi.createBudget(budgetForm)
      ElMessage.success('创建成功')
    }
    budgetDialogVisible.value = false
    loadBudgetProgress()
  } catch (error) {
    console.error('保存预算失败:', error)
    ElMessage.error('保存失败')
  }
}

const showAddGoalDialog = () => {
  isEditGoal.value = false
  editGoalId.value = null
  goalFormRef.value?.resetFields()
  Object.assign(goalForm, {
    name: '',
    targetAmount: 0,
    currentAmount: 0,
    targetDate: dayjs().add(1, 'year').format('YYYY-MM-DD'),
    color: '#409eff'
  })
  goalDialogVisible.value = true
}

const editGoal = (goal) => {
  isEditGoal.value = true
  editGoalId.value = goal.id
  Object.assign(goalForm, {
    name: goal.name,
    targetAmount: goal.targetAmount,
    currentAmount: goal.currentAmount,
    targetDate: goal.targetDate,
    color: goal.color
  })
  goalDialogVisible.value = true
}

const submitGoal = async () => {
  await goalFormRef.value.validate()
  try {
    if (isEditGoal.value) {
      await savingGoalApi.updateSavingGoal(editGoalId.value, goalForm)
      ElMessage.success('更新成功')
    } else {
      await savingGoalApi.createSavingGoal(goalForm)
      ElMessage.success('创建成功')
    }
    goalDialogVisible.value = false
    loadSavingGoals()
  } catch (error) {
    console.error('保存目标失败:', error)
    ElMessage.error('保存失败')
  }
}

const depositToGoal = (goal) => {
  currentGoal.value = goal
  depositType.value = 'deposit'
  depositAmount.value = 0
  depositDialogVisible.value = true
}

const withdrawFromGoal = (goal) => {
  currentGoal.value = goal
  depositType.value = 'withdraw'
  depositAmount.value = 0
  depositDialogVisible.value = true
}

const submitDeposit = async () => {
  try {
    const amount = depositType.value === 'deposit' ? depositAmount.value : -depositAmount.value
    await savingGoalApi.updateProgress(currentGoal.value.id, { amount })
    ElMessage.success(depositType.value === 'deposit' ? '存入成功' : '取出成功')
    depositDialogVisible.value = false
    loadSavingGoals()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

const applySuggestion = (suggestion) => {
  if (!suggestion.action) {
    ElMessage.info('这只是一条建议，无需操作')
    return
  }
  ElMessageBox.confirm('确定要应用这条建议吗？', '应用建议', {
    type: 'info'
  }).then(() => {
    ElMessage.success('建议已应用')
    loadBudgetProgress()
  }).catch(() => {})
}

onMounted(() => {
  loadBudgetProgress()
  loadSavingGoals()
  loadCategories()
  generateSuggestions()
})
</script>

<style scoped lang="scss">
.budget {
  .budget-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    
    .budget-card {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      border: 1px solid #ebeef5;
      transition: all 0.3s;
      
      &:hover {
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }
      
      &.over-budget {
        border-color: #f56c6c;
        background: #fef0f0;
      }
      
      &.warning-budget {
        border-color: #e6a23c;
        background: #fdf6ec;
      }
      
      .budget-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        
        .budget-category {
          display: flex;
          align-items: center;
          gap: 8px;
          
          .category-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
          }
          
          .category-name {
            font-weight: 600;
          }
        }
      }
      
      .budget-amounts {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        
        .label {
          display: block;
          font-size: 12px;
          color: #909399;
          margin-bottom: 4px;
        }
        
        .value {
          font-size: 18px;
          font-weight: 600;
        }
      }
      
      .progress-section {
        margin-bottom: 15px;
        
        .progress-info {
          display: flex;
          justify-content: space-between;
          margin-bottom: 8px;
          font-size: 13px;
          
          .percentage {
            font-weight: 600;
          }
        }
      }
      
      .budget-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 10px;
        border-top: 1px dashed #ebeef5;
      }
    }
  }
  
  .goals-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    
    .goal-card {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      border: 1px solid #ebeef5;
      transition: all 0.3s;
      
      &:hover {
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        transform: translateY(-2px);
      }
      
      .goal-header {
        display: flex;
        align-items: center;
        gap: 15px;
        margin-bottom: 20px;
        
        .goal-icon {
          width: 50px;
          height: 50px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
        }
        
        .goal-info {
          .goal-name {
            font-size: 16px;
            font-weight: 600;
            margin: 0 0 4px 0;
          }
          
          .goal-deadline {
            font-size: 13px;
            color: #909399;
            margin: 0;
          }
        }
      }
      
      .goal-amounts {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        
        .amount {
          font-size: 20px;
          font-weight: 600;
          margin-top: 4px;
        }
        
        .arrow {
          color: #c0c4cc;
        }
      }
      
      .goal-progress {
        margin-bottom: 15px;
        
        .progress-info {
          display: flex;
          justify-content: space-between;
          margin-bottom: 8px;
          font-size: 13px;
        }
      }
      
      .goal-actions {
        display: flex;
        gap: 10px;
        margin-bottom: 15px;
        
        .el-button {
          flex: 1;
        }
      }
      
      .goal-tips {
        text-align: center;
      }
    }
  }
  
  .suggestions-list {
    .suggestion-item {
      display: flex;
      align-items: center;
      gap: 15px;
      padding: 15px;
      border-bottom: 1px solid #ebeef5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .suggestion-icon {
        font-size: 24px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.warning {
          color: #e6a23c;
        }
        
        &.info {
          color: #409eff;
        }
        
        &.success {
          color: #67c23a;
        }
      }
      
      .suggestion-content {
        flex: 1;
        
        .suggestion-title {
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .suggestion-desc {
          color: #909399;
          font-size: 13px;
        }
      }
    }
  }
  
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 15px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .stat-content {
      flex: 1;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 5px;
    }
    
    .stat-value {
      font-size: 24px;
      font-weight: 600;
    }
    
    &.primary-card .stat-icon {
      background: rgba(64, 158, 255, 0.1);
      color: #409eff;
    }
    
    &.success-card .stat-icon {
      background: rgba(103, 194, 58, 0.1);
      color: #67c23a;
    }
    
    &.warning-card .stat-icon {
      background: rgba(230, 162, 60, 0.1);
      color: #e6a23c;
    }
  }
  
  .income {
    color: #67c23a;
  }
  
  .expense {
    color: #f56c6c;
  }
  
  .danger-text {
    color: #f56c6c;
  }
}

.text-muted {
  color: #909399;
  font-size: 13px;
}
</style>
