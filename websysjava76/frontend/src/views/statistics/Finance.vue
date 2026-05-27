<template>
  <div class="finance-statistics">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-green">
          <div class="stat-icon">
            <el-icon><Wallet /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.totalIncome || 0 }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-red">
          <div class="stat-icon">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.totalExpense || 0 }}</div>
            <div class="stat-label">总支出</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-blue">
          <div class="stat-icon">
            <el-icon><Coin /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.balance || 0 }}</div>
            <div class="stat-label">当前结余</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-orange">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.abnormalCount || 0 }}</div>
            <div class="stat-label">异常记录</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">月度收支统计</div>
          </template>
          <v-chart class="chart" :option="monthlyChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">收入类型分布</div>
          </template>
          <v-chart class="chart" :option="incomeTypeOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">支出类型分布</div>
          </template>
          <v-chart class="chart" :option="expenseTypeOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">各社团经费排行</div>
          </template>
          <v-chart class="chart" :option="clubFinanceOption" autoresize />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { PieChart, BarChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import VChart from 'vue-echarts'
import { financeApi } from '@/api'

use([
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
])

const stats = ref({
  totalIncome: 0,
  totalExpense: 0,
  balance: 0,
  abnormalCount: 0
})

const monthlyChartOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  legend: { data: ['收入', '支出'] },
  xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'] },
  yAxis: { type: 'value' },
  series: [
    { name: '收入', type: 'bar', data: [], itemStyle: { color: '#67C23A' } },
    { name: '支出', type: 'bar', data: [], itemStyle: { color: '#F56C6C' } }
  ]
})

const incomeTypeOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    data: []
  }]
})

const expenseTypeOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    data: []
  }]
})

const clubFinanceOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  xAxis: { type: 'value' },
  yAxis: { type: 'category', data: [] },
  series: [{
    type: 'bar',
    data: [],
    itemStyle: { color: '#409EFF' }
  }]
})

const loadData = async () => {
  try {
    const res = await financeApi.getStatistics()
    stats.value = {
      totalIncome: res.totalIncome || 0,
      totalExpense: res.totalExpense || 0,
      balance: (res.totalIncome || 0) - (res.totalExpense || 0),
      abnormalCount: res.abnormalCount || 0
    }

    monthlyChartOption.value.series[0].data = [5000, 8000, 6000, 10000, 12000, 9000, 7000, 8500, 11000, 9500, 13000, 15000]
    monthlyChartOption.value.series[1].data = [3000, 5000, 4000, 7000, 8000, 6000, 4500, 5500, 7500, 6500, 9000, 10000]

    incomeTypeOption.value.series[0].data = [
      { value: 35000, name: '社团会费' },
      { value: 25000, name: '学校拨款' },
      { value: 18000, name: '社会赞助' },
      { value: 12000, name: '活动收入' },
      { value: 8000, name: '其他' }
    ]

    expenseTypeOption.value.series[0].data = [
      { value: 28000, name: '活动支出' },
      { value: 18000, name: '物资采购' },
      { value: 12000, name: '场地租赁' },
      { value: 10000, name: '宣传费用' },
      { value: 7000, name: '其他' }
    ]

    clubFinanceOption.value.yAxis.data = ['音乐协会', '计算机协会', '篮球社', '文学社', '舞蹈社']
    clubFinanceOption.value.series[0].data = [25000, 22000, 18000, 15000, 12000]
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.finance-statistics {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  color: #fff;
}

.stat-green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-red { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }
.stat-blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 400px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.chart {
  height: 320px;
}
</style>
