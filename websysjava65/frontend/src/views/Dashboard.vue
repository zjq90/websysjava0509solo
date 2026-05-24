<template>
  <div class="page-container">
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-title">🚴 今日骑行次数</div>
          <div class="stat-value">{{ dashboardData.operationStats?.todayRides || 0 }}</div>
          <div class="stat-sub">本月累计: {{ dashboardData.operationStats?.monthRides || 0 }} 次</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-2">
          <div class="stat-title">💰 今日收入</div>
          <div class="stat-value">¥{{ dashboardData.operationStats?.todayRevenue || 0 }}</div>
          <div class="stat-sub">本月累计: ¥{{ dashboardData.operationStats?.monthRevenue || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-3">
          <div class="stat-title">👥 今日新增用户</div>
          <div class="stat-value">{{ dashboardData.operationStats?.todayNewUsers || 0 }}</div>
          <div class="stat-sub">本月新增: {{ dashboardData.operationStats?.monthNewUsers || 0 }} 人</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card-4">
          <div class="stat-title">🚲 在线车辆</div>
          <div class="stat-value">{{ dashboardData.bikeStatus?.onlineCount || 0 }}</div>
          <div class="stat-sub">车辆总数: {{ totalBikes }} 辆</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">车辆状态分布</div>
          <div ref="bikeStatusChart" style="height: 300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">区域使用率对比</div>
          <div ref="areaUsageChart" style="height: 300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <div class="chart-card">
          <div class="chart-title">实时运营趋势</div>
          <div ref="trendChart" style="height: 300px"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'
import { getDashboard } from '../api'

const bikeStatusChart = ref(null)
const areaUsageChart = ref(null)
const trendChart = ref(null)
const dashboardData = ref({})

const totalBikes = computed(() => {
  const status = dashboardData.value.bikeStatus || {}
  return (status.onlineCount || 0) + (status.offlineCount || 0) + 
         (status.faultCount || 0) + (status.maintenanceCount || 0)
})

const initBikeStatusChart = () => {
  const status = dashboardData.value.bikeStatus || {}
  const chart = echarts.init(bikeStatusChart.value)
  const option = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%', left: 'center' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
      labelLine: { show: false },
      data: [
        { value: status.onlineCount || 0, name: '在线', itemStyle: { color: '#67c23a' } },
        { value: status.offlineCount || 0, name: '离线', itemStyle: { color: '#909399' } },
        { value: status.faultCount || 0, name: '故障', itemStyle: { color: '#f56c6c' } },
        { value: status.maintenanceCount || 0, name: '维修中', itemStyle: { color: '#e6a23c' } }
      ]
    }]
  }
  chart.setOption(option)
}

const initAreaUsageChart = () => {
  const areas = dashboardData.value.areaUsage || []
  const chart = echarts.init(areaUsageChart.value)
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: areas.map(a => a.areaName) },
    yAxis: { type: 'value' },
    series: [{
      type: 'bar',
      data: areas.map(a => a.rideCount),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#4facfe' },
          { offset: 1, color: '#00f2fe' }
        ]),
        borderRadius: [8, 8, 0, 0]
      }
    }]
  }
  chart.setOption(option)
}

const initTrendChart = () => {
  const chart = echarts.init(trendChart.value)
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: ['骑行次数', '收入(元)'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', boundaryGap: false, data: days },
    yAxis: { type: 'value' },
    series: [
      {
        name: '骑行次数',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210],
        itemStyle: { color: '#667eea' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
          { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
        ])}
      },
      {
        name: '收入(元)',
        type: 'line',
        smooth: true,
        data: [200, 230, 180, 250, 160, 400, 380],
        itemStyle: { color: '#f5576c' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(245, 87, 108, 0.3)' },
          { offset: 1, color: 'rgba(245, 87, 108, 0.05)' }
        ])}
      }
    ]
  }
  chart.setOption(option)
}

const loadData = async () => {
  try {
    const res = await getDashboard()
    dashboardData.value = res.data
    initBikeStatusChart()
    initAreaUsageChart()
    initTrendChart()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>
