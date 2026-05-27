<template>
  <div class="club-statistics">
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
            <el-icon><School /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalDepartments || 0 }}</div>
            <div class="stat-label">覆盖院系</div>
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
          <v-chart class="chart" :option="typeChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">社团状态分布</div>
          </template>
          <v-chart class="chart" :option="statusChartOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">各院系社团数量</div>
          </template>
          <v-chart class="chart" :option="departmentChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">月度活动趋势</div>
          </template>
          <v-chart class="chart" :option="activityTrendOption" autoresize />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { use } from 'echarts/core'
import { PieChart, BarChart, LineChart } from 'echarts/charts'
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import VChart from 'vue-echarts'
import { clubApi } from '@/api'

use([
  PieChart,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  CanvasRenderer
])

const stats = ref({
  totalClubs: 0,
  totalMembers: 0,
  totalActivities: 0,
  totalDepartments: 0
})

const typeChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    data: []
  }]
})

const statusChartOption = ref({
  tooltip: { trigger: 'item' },
  legend: { bottom: '5%' },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
    data: []
  }]
})

const departmentChartOption = ref({
  tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
  xAxis: { type: 'category', data: [] },
  yAxis: { type: 'value' },
  series: [{
    type: 'bar',
    data: [],
    itemStyle: {
      color: '#409EFF'
    }
  }]
})

const activityTrendOption = ref({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'] },
  yAxis: { type: 'value' },
  series: [{
    type: 'line',
    data: [],
    smooth: true,
    itemStyle: { color: '#67C23A' },
    areaStyle: { opacity: 0.3 }
  }]
})

const loadData = async () => {
  try {
    const res = await clubApi.getStatistics()
    stats.value = res
    
    const typeData = res.byType?.map(item => ({
      name: item[0],
      value: item[1]
    })) || []
    typeChartOption.value.series[0].data = typeData
    
    const statusData = res.byStatus?.map(item => ({
      name: item[0],
      value: item[1]
    })) || []
    statusChartOption.value.series[0].data = statusData

    const deptData = res.byDepartment?.map(item => item[0]) || []
    const deptValues = res.byDepartment?.map(item => item[1]) || []
    departmentChartOption.value.xAxis.data = deptData
    departmentChartOption.value.series[0].data = deptValues

    activityTrendOption.value.series[0].data = [5, 10, 8, 15, 20, 25, 18, 12, 22, 28, 30, 25]
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.club-statistics {
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
