<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">数据概览</div>
    </div>

    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div>
              <div class="stat-title">本月收入</div>
              <div class="stat-value income-color">¥{{ formatMoney(monthlyIncome) }}</div>
            </div>
            <el-icon size="48" color="#67c23a"><Top /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div>
              <div class="stat-title">本月支出</div>
              <div class="stat-value expense-color">¥{{ formatMoney(monthlyExpense) }}</div>
            </div>
            <el-icon size="48" color="#f56c6c"><Bottom /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div>
              <div class="stat-title">本月结余</div>
              <div class="stat-value" :class="monthlyBalance >= 0 ? 'income-color' : 'expense-color'">¥{{ formatMoney(monthlyBalance) }}</div>
            </div>
            <el-icon size="48" color="#409EFF"><Wallet /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div style="display: flex; justify-content: space-between; align-items: center">
            <div>
              <div class="stat-title">账户总额</div>
              <div class="stat-value">¥{{ formatMoney(totalBalance) }}</div>
            </div>
            <el-icon size="48" color="#E6A23C"><Money /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div style="display: flex; justify-content: space-between">
              <span>账户余额分布</span>
            </div>
          </template>
          <div ref="accountChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div style="display: flex; justify-content: space-between">
              <span>本月支出分类占比</span>
            </div>
          </template>
          <div ref="categoryChartRef" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'
import { listAccounts } from '@/api/account'
import { listTransactions } from '@/api/transaction'
import dayjs from 'dayjs'

const accountChartRef = ref(null)
const categoryChartRef = ref(null)
const accounts = ref([])
const transactions = ref([])

const monthlyIncome = computed(() => {
  const startOfMonth = dayjs().startOf('month')
  return transactions.value
    .filter(t => t.transactionType === 'INCOME' && dayjs(t.transactionTime).isAfter(startOfMonth))
    .reduce((sum, t) => sum + Number(t.amount), 0)
})

const monthlyExpense = computed(() => {
  const startOfMonth = dayjs().startOf('month')
  return transactions.value
    .filter(t => t.transactionType === 'EXPENSE' && dayjs(t.transactionTime).isAfter(startOfMonth))
    .reduce((sum, t) => sum + Number(t.amount), 0)
})

const monthlyBalance = computed(() => monthlyIncome.value - monthlyExpense.value)

const totalBalance = computed(() => {
  return accounts.value.reduce((sum, a) => sum + Number(a.balance), 0)
})

const formatMoney = (value) => {
  return Number(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const loadData = async () => {
  try {
    accounts.value = await listAccounts()
    transactions.value = await listTransactions()
    renderCharts()
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const renderCharts = () => {
  if (accountChartRef.value) {
    const accountChart = echarts.init(accountChartRef.value)
    accountChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: ¥{c}' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: accounts.value.map(a => ({ name: a.accountName, value: Number(a.balance) })),
        label: { formatter: '{b}\n¥{c}' }
      }]
    })
  }

  if (categoryChartRef.value) {
    const startOfMonth = dayjs().startOf('month')
    const expenseCategories = {}
    transactions.value
      .filter(t => t.transactionType === 'EXPENSE' && dayjs(t.transactionTime).isAfter(startOfMonth))
      .forEach(t => {
        const name = t.categoryName || '其他'
        expenseCategories[name] = (expenseCategories[name] || 0) + Number(t.amount)
      })

    const categoryChart = echarts.init(categoryChartRef.value)
    categoryChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: ¥{c}' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: Object.entries(expenseCategories).map(([name, value]) => ({ name, value })),
        label: { formatter: '{b}\n¥{c}' }
      }]
    })
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    echarts.getInstanceByDom(accountChartRef.value)?.resize()
    echarts.getInstanceByDom(categoryChartRef.value)?.resize()
  })
})
</script>

<style lang="scss" scoped>
.stat-cards {
  .stat-card {
    .stat-title {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: 600;
    }
  }
}
</style>
