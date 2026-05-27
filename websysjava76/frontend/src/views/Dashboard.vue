<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-blue">
          <div class="stat-icon">
            <el-icon><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalClubs || 0 }}</div>
            <div class="stat-label">社团总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-green">
          <div class="stat-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalMembers || 0 }}</div>
            <div class="stat-label">成员总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-orange">
          <div class="stat-icon">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalActivities || 0 }}</div>
            <div class="stat-label">年度活动数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-purple">
          <div class="stat-icon">
            <el-icon><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ pendingCounts.clubApplications || 0 }}</div>
            <div class="stat-label">待审核申请</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">社团类型分布</div>
          </template>
          <v-chart class="chart" :option="clubTypeChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">社团状态分布</div>
          </template>
          <v-chart class="chart" :option="clubStatusChartOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">经费收支统计（元）</div>
          </template>
          <v-chart class="chart" :option="financeChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">待处理事项</div>
          </template>
          <el-table :data="pendingList" style="width: 100%">
            <el-table-column prop="type" label="类型" width="120" />
            <el-table-column prop="count" label="数量" width="100" align="center">
              <template #default="{ row }">
                <el-tag type="danger">{{ row.count }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="desc" label="描述" />
          </el-table>
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
import { clubApi, financeApi } from '@/api'

use([
  PieChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
])

const stats = ref({})
const pendingCounts = ref({})

const pendingList = ref([
  { type: '社团申请', count: 0, desc: '新社团成立等待审核' },
  { type: '年度注册', count: 0, desc: '社团年度注册待审核' },
  { type: '违规处理', count: 0, desc: '违规社团待整改' }
])

const clubTypeChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    avoidLabelOverlap: false,
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    label: { show: false },
    emphasis: {
      label: { show: true, fontSize: 16, fontWeight: 'bold' }
    },
    labelLine: { show: false },
    data: []
  }]
})

const clubStatusChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    data: []
  }]
})

const financeChartOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  legend: { data: ['收入', '支出'] },
  xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'] },
  yAxis: { type: 'value' },
  series: [
    { name: '收入', type: 'bar', data: [] },
    { name: '支出', type: 'bar', data: [] }
  ]
})

const loadData = async () => {
  try {
    const clubStats = await clubApi.getStatistics()
    stats.value = clubStats
    
    const typeData = clubStats.byType?.map(item => ({
      name: item[0],
      value: item[1]
    })) || []
    clubTypeChartOption.value.series[0].data = typeData
    
    const statusData = clubStats.byStatus?.map(item => ({
      name: item[0],
      value: item[1]
    })) || []
    clubStatusChartOption.value.series[0].data = statusData

    const counts = await clubApi.getPendingCounts()
    pendingCounts.value = counts
    pendingList.value[0].count = counts.clubApplications
    pendingList.value[1].count = counts.annualRegistrations
    pendingList.value[2].count = counts.violations

    const financeStats = await financeApi.getStatistics()
    financeChartOption.value.series[0].data = [5000, 8000, 6000, 10000, financeStats.totalIncome || 0, 0]
    financeChartOption.value.series[1].data = [3000, 5000, 4000, 7000, financeStats.totalExpense || 0, 0]
  } catch (e) {
    console.error('加载数据失败', e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
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

.stat-blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
.stat-purple { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
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
