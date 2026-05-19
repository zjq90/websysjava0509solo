<template>
  <div class="user-growth-page">
    <div class="page-card">
      <div class="page-title">用户增长分析</div>
      
      <div class="toolbar">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 350px; margin-right: 10px;"
        />
        <el-button type="primary" @click="loadData">查询</el-button>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <div class="chart-container">
            <div ref="lineChartRef" style="height: 400px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">用户类型分布</h3>
            <div ref="pieChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">用户增长柱状图</h3>
            <div ref="barChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getUserGrowth } from '@/api'

const lineChartRef = ref(null)
const pieChartRef = ref(null)
const barChartRef = ref(null)
const dateRange = ref(null)

let lineChart = null
let pieChart = null
let barChart = null

const loadData = async () => {
  const params = {}
  if (dateRange.value && dateRange.value.length === 2) {
    params.startTime = dateRange.value[0]
    params.endTime = dateRange.value[1]
  }
  // 模拟数据
  const mockDailyData = {
    '2024-01-01': 15, '2024-01-02': 23, '2024-01-03': 18, '2024-01-04': 25, '2024-01-05': 30,
    '2024-01-06': 28, '2024-01-07': 35, '2024-01-08': 42, '2024-01-09': 38, '2024-01-10': 45,
    '2024-01-11': 50, '2024-01-12': 55, '2024-01-13': 48, '2024-01-14': 60, '2024-01-15': 65
  }
  
  nextTick(() => {
    initLineChart(mockDailyData)
    initPieChart()
    initBarChart(mockDailyData)
  })
}

const initLineChart = (dailyData) => {
  if (!lineChartRef.value) return
  if (lineChart) lineChart.dispose()
  lineChart = echarts.init(lineChartRef.value)

  const dates = Object.keys(dailyData).sort()
  const counts = dates.map(d => dailyData[d])
  
  // 计算累计用户
  let total = 0
  const cumulative = []
  counts.forEach(c => {
    total += c
    cumulative.push(total)
  })

  const option = {
    title: {
      text: '用户增长趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['日新增', '累计用户'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates
    },
    yAxis: [
      {
        type: 'value',
        name: '日新增',
        position: 'left'
      },
      {
        type: 'value',
        name: '累计用户',
        position: 'right'
      }
    ],
    series: [
      {
        name: '日新增',
        type: 'line',
        smooth: true,
        data: counts,
        itemStyle: { color: '#409EFF' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        }
      },
      {
        name: '累计用户',
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        data: cumulative,
        itemStyle: { color: '#67C23A' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(103, 194, 58, 0.5)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
          ])
        }
      }
    ]
  }

  lineChart.setOption(option)
}

const initPieChart = () => {
  if (!pieChartRef.value) return
  if (pieChart) pieChart.dispose()
  pieChart = echarts.init(pieChartRef.value)

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 75, name: '宠物主人' },
        { value: 25, name: '医生' }
      ],
      color: ['#409EFF', '#67C23A'],
      label: {
        show: true,
        formatter: '{b}: {c}人 ({d}%)'
      }
    }]
  }

  pieChart.setOption(option)
}

const initBarChart = (dailyData) => {
  if (!barChartRef.value) return
  if (barChart) barChart.dispose()
  barChart = echarts.init(barChartRef.value)

  const dates = Object.keys(dailyData).sort()
  const counts = dates.map(d => dailyData[d])

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value',
      name: '新增用户数'
    },
    series: [{
      type: 'bar',
      data: counts,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#83bff6' },
          { offset: 0.5, color: '#188df0' },
          { offset: 1, color: '#188df0' }
        ])
      }
    }]
  }

  barChart.setOption(option)
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    lineChart?.resize()
    pieChart?.resize()
    barChart?.resize()
  })
})
</script>

<style lang="scss" scoped>
.chart-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}
</style>
