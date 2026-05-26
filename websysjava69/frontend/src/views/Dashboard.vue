<template>
  <div class="page-container">
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">总资产</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.totalAssets || 0) }}</div>
          <div class="stat-desc">包含资产和投资</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">总负债</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.totalLiabilities || 0) }}</div>
          <div class="stat-desc">未结清债务</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-label">净资产</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.netWorth || 0) }}</div>
          <div class="stat-desc">总资产 - 总负债</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-label">投资收益</div>
          <div class="stat-value" :class="netWorth?.totalInvestmentProfit >= 0 ? 'positive' : 'negative'">
            {{ netWorth?.totalInvestmentProfit >= 0 ? '+' : '' }}¥{{ formatMoney(netWorth?.totalInvestmentProfit || 0) }}
          </div>
          <div class="stat-desc">浮动盈亏</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card-shadow p-20 mb-20">
          <div class="page-header">
            <span class="page-title">即将到期债务</span>
          </div>
          <el-table :data="upcomingDebts" style="width: 100%">
            <el-table-column prop="debtName" label="债务名称" />
            <el-table-column prop="debtType" label="类型">
              <template #default="{ row }">
                <el-tag size="small">{{ formatDebtType(row.debtType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="monthlyPayment" label="应还金额">
              <template #default="{ row }">
                ¥{{ formatMoney(row.monthlyPayment) }}
              </template>
            </el-table-column>
            <el-table-column prop="nextPaymentDate" label="还款日">
              <template #default="{ row }">
                <el-tag :type="getDaysLeft(row.nextPaymentDate) <= 3 ? 'danger' : 'warning'" size="small">
                  {{ row.nextPaymentDate }} ({{ getDaysLeft(row.nextPaymentDate) }}天后)
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="card-shadow p-20">
          <div class="page-header">
            <span class="page-title">投资组合概览</span>
          </div>
          <el-table :data="investments" style="width: 100%">
            <el-table-column prop="investmentName" label="名称" />
            <el-table-column prop="investmentType" label="类型">
              <template #default="{ row }">
                <el-tag size="small">{{ formatInvestmentType(row.investmentType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="marketValue" label="市值">
              <template #default="{ row }">¥{{ formatMoney(row.marketValue) }}</template>
            </el-table-column>
            <el-table-column prop="profitLoss" label="盈亏">
              <template #default="{ row }">
                <span :class="row.profitLoss >= 0 ? 'positive' : 'negative'">
                  {{ row.profitLoss >= 0 ? '+' : '' }}¥{{ formatMoney(row.profitLoss) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="profitLossRate" label="收益率">
              <template #default="{ row }">
                <span :class="row.profitLossRate >= 0 ? 'positive' : 'negative'">
                  {{ row.profitLossRate >= 0 ? '+' : '' }}{{ row.profitLossRate?.toFixed(2) }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-shadow p-20 mb-20">
          <div class="page-header">
            <span class="page-title">家庭预算进度</span>
          </div>
          <div v-for="budget in budgets" :key="budget.id" class="budget-item">
            <div class="flex-between mb-8">
              <span>{{ formatBudgetCategory(budget.category) }}</span>
              <span>
                ¥{{ formatMoney(budget.spentAmount) }} / ¥{{ formatMoney(budget.budgetAmount) }}
              </span>
            </div>
            <div class="progress-bar">
              <div 
                class="progress-bar-inner" 
                :class="budget.spentAmount / budget.budgetAmount > 0.8 ? 'warning' : 'normal'"
                :style="{ width: Math.min(budget.spentAmount / budget.budgetAmount * 100, 100) + '%' }"
              ></div>
            </div>
            <div class="text-right mt-4" style="font-size: 12px; color: #909399">
              剩余 ¥{{ formatMoney(budget.budgetAmount - budget.spentAmount) }}
            </div>
          </div>
        </div>

        <div class="card-shadow p-20">
          <div class="page-header">
            <span class="page-title">虚拟账户</span>
          </div>
          <el-row :gutter="16">
            <el-col :span="12" v-for="account in virtualAccounts" :key="account.id">
              <div class="virtual-account-card" :style="{ background: account.color + '20', borderColor: account.color }">
                <div class="flex-between">
                  <div class="account-icon" :style="{ background: account.color }">
                    {{ account.icon }}
                  </div>
                  <el-tag v-if="account.isFamilyAccount" size="small" type="info">家庭</el-tag>
                </div>
                <div class="account-name">{{ account.accountName }}</div>
                <div class="account-balance">¥{{ formatMoney(account.currentBalance) }}</div>
                <div v-if="account.targetAmount" class="account-target">
                  目标: ¥{{ formatMoney(account.targetAmount) }} ({{ ((account.currentBalance / account.targetAmount) * 100).toFixed(1) }}%)
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { netWorthApi, debtApi, investmentApi, budgetApi, virtualAccountApi, API } from '@/api'

const netWorth = ref(null)
const upcomingDebts = ref([])
const investments = ref([])
const budgets = ref([])
const virtualAccounts = ref([])

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDebtType = (type) => {
  const map = { CREDIT_CARD: '信用卡', PERSONAL_LOAN: '个人贷款', MORTGAGE: '房贷', CAR_LOAN: '车贷', STUDENT_LOAN: '学生贷款', OTHER: '其他' }
  return map[type] || type
}

const formatInvestmentType = (type) => {
  const map = { STOCK: '股票', FUND: '基金', CRYPTOCURRENCY: '数字货币', BOND: '债券', DEPOSIT: '定期', OTHER: '其他' }
  return map[type] || type
}

const formatBudgetCategory = (type) => {
  const map = { FOOD: '餐饮', TRANSPORTATION: '交通', SHOPPING: '购物', ENTERTAINMENT: '娱乐', MEDICAL: '医疗', EDUCATION: '教育', UTILITIES: '水电', OTHER: '其他' }
  return map[type] || type
}

const getDaysLeft = (date) => {
  const today = new Date()
  const target = new Date(date)
  return Math.ceil((target - today) / (1000 * 60 * 60 * 24))
}

const loadData = async () => {
  try {
    const [nw, debts, inv, bud, va] = await Promise.all([
      netWorthApi.getPersonal(API.currentUserId),
      debtApi.getReminders(),
      investmentApi.list(API.currentUserId),
      budgetApi.listByFamily(API.currentFamilyId),
      virtualAccountApi.listByUserAndFamily(API.currentUserId, API.currentFamilyId)
    ])
    netWorth.value = nw
    upcomingDebts.value = debts
    investments.value = inv.slice(0, 5)
    budgets.value = bud
    virtualAccounts.value = va.slice(0, 4)
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.text-right { text-align: right; }
.mt-4 { margin-top: 4px; }
.mb-8 { margin-bottom: 8px; }

.budget-item {
  margin-bottom: 20px;
}

.budget-item:last-child {
  margin-bottom: 0;
}

.virtual-account-card {
  border: 1px solid;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.virtual-account-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.account-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #fff;
  margin-bottom: 12px;
}

.account-name {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.account-balance {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.account-target {
  font-size: 12px;
  color: #909399;
}
</style>
