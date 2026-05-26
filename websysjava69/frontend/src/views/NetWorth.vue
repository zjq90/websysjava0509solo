<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">净资产统计</span>
      <el-radio-group v-model="viewMode" @change="loadNetWorth">
        <el-radio-button value="personal">个人</el-radio-button>
        <el-radio-button value="family">家庭</el-radio-button>
      </el-radio-group>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">总资产</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.totalAssets || 0) }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">总负债</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.totalLiabilities || 0) }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-label">净资产</div>
          <div class="stat-value">¥{{ formatMoney(netWorth?.netWorth || 0) }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-label">投资收益</div>
          <div class="stat-value" :class="netWorth?.totalInvestmentProfit >= 0 ? 'positive' : 'negative'">
            {{ netWorth?.totalInvestmentProfit >= 0 ? '+' : '' }}¥{{ formatMoney(netWorth?.totalInvestmentProfit || 0) }}
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card-shadow p-20 mb-20">
          <h3 class="section-title">资产构成</h3>
          <div class="chart-container" ref="assetChartRef"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card-shadow p-20 mb-20">
          <h3 class="section-title">负债构成</h3>
          <div class="chart-container" ref="liabilityChartRef"></div>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <h3 class="section-title">资产负债明细</h3>
      <el-row :gutter="20">
        <el-col :span="12">
          <h4>资产明细</h4>
          <el-table :data="assetList" style="width: 100%">
            <el-table-column label="类型">
              <template #default="{ row }">{{ formatAssetType(row.key) }}</template>
            </el-table-column>
            <el-table-column label="金额">
              <template #default="{ row }">¥{{ formatMoney(row.value) }}</template>
            </el-table-column>
            <el-table-column label="占比">
              <template #default="{ row }">
                {{ ((row.value / (netWorth?.totalAssets || 1)) * 100).toFixed(1) }}%
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span="12">
          <h4>负债明细</h4>
          <el-table :data="liabilityList" style="width: 100%">
            <el-table-column label="类型">
              <template #default="{ row }">{{ formatDebtType(row.key) }}</template>
            </el-table-column>
            <el-table-column label="金额">
              <template #default="{ row }">¥{{ formatMoney(row.value) }}</template>
            </el-table-column>
            <el-table-column label="占比">
              <template #default="{ row }">
                {{ ((row.value / (netWorth?.totalLiabilities || 1)) * 100).toFixed(1) }}%
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { netWorthApi, API } from '@/api'

const viewMode = ref('personal')
const netWorth = ref(null)
const assetChartRef = ref(null)
const liabilityChartRef = ref(null)
let assetChart = null
let liabilityChart = null

const assetList = computed(() => {
  if (!netWorth.value?.assetBreakdown) return []
  return Object.entries(netWorth.value.assetBreakdown).map(([key, value]) => ({ key, value }))
})

const liabilityList = computed(() => {
  if (!netWorth.value?.liabilityBreakdown) return []
  return Object.entries(netWorth.value.liabilityBreakdown).map(([key, value]) => ({ key, value }))
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatAssetType = (type) => {
  const map = { CASH: '现金', BANK_DEPOSIT: '银行存款', REAL_ESTATE: '房产', VEHICLE: '车辆', OTHER: '其他' }
  return map[type] || type
}

const formatDebtType = (type) => {
  const map = { CREDIT_CARD: '信用卡', PERSONAL_LOAN: '个人贷款', MORTGAGE: '房贷', CAR_LOAN: '车贷', STUDENT_LOAN: '学生贷款', OTHER: '其他' }
  return map[type] || type
}

const loadNetWorth = async () => {
  try {
    if (viewMode.value === 'personal') {
      netWorth.value = await netWorthApi.getPersonal(API.currentUserId)
    } else {
      netWorth.value = await netWorthApi.getFamily(API.currentFamilyId, API.currentUserId)
    }
    await nextTick()
    renderCharts()
  } catch (e) {
    console.error(e)
  }
}

const renderCharts = () => {
  if (assetChartRef.value) {
    if (!assetChart) {
      assetChart = echarts.init(assetChartRef.value)
    }
    const data = Object.entries(netWorth.value?.assetBreakdown || {}).map(([name, value]) => ({
      name: formatAssetType(name),
      value
    }))
    assetChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data
      }]
    })
  }

  if (liabilityChartRef.value) {
    if (!liabilityChart) {
      liabilityChart = echarts.init(liabilityChartRef.value)
    }
    const data = Object.entries(netWorth.value?.liabilityBreakdown || {}).map(([name, value]) => ({
      name: formatDebtType(name),
      value
    }))
    liabilityChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
      legend: { bottom: 0 },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        data
      }]
    })
  }
}

onMounted(() => {
  loadNetWorth()
  window.addEventListener('resize', () => {
    assetChart?.resize()
    liabilityChart?.resize()
  })
})
</script>

<style scoped>
.p-20 { padding: 20px; }
.section-title { font-size: 16px; font-weight: 600; margin-bottom: 16px; }
.chart-container { height: 300px; }
h4 { margin-bottom: 12px; color: #606266; }
</style>
