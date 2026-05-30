<template>
  <div class="trend-chart">
    <div class="chart-header">
      <span class="title">{{ title }}</span>
      <el-radio-group v-model="chartType" size="small">
        <el-radio-button value="active">活跃用户</el-radio-button>
        <el-radio-button value="launch">游戏启动</el-radio-button>
      </el-radio-group>
    </div>
    <v-chart class="chart" :option="chartOption" autoresize />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import { getTrendData } from '@/api/dashboard'

use([CanvasRenderer, LineChart, GridComponent, TooltipComponent, LegendComponent])

const props = defineProps({
  title: {
    type: String,
    default: '趋势图'
  },
  days: {
    type: Number,
    default: 7
  }
})

const chartType = ref('active')
const trendData = ref(null)

const loadData = async () => {
  trendData.value = await getTrendData(props.days)
}

const chartOption = computed(() => {
  if (!trendData.value) {
    return {
      tooltip: { trigger: 'axis' }
    }
  }

  const series = []
  if (chartType.value === 'active' || chartType.value === 'all') {
    series.push({
      name: '活跃用户',
      type: 'line',
      smooth: true,
      data: trendData.value.activeUsers,
      lineStyle: { width: 3 },
      itemStyle: { color: '#409eff' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ]
        }
      }
    })
  }
  if (chartType.value === 'launch' || chartType.value === 'all') {
    series.push({
      name: '游戏启动',
      type: 'line',
      smooth: true,
      data: trendData.value.gameLaunches,
      lineStyle: { width: 3 },
      itemStyle: { color: '#67c23a' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ]
        }
      }
    })
  }

  return {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e4e7ed',
      borderWidth: 1,
      textStyle: { color: '#303133' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: trendData.value.dates,
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisLabel: { color: '#909399' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: '#f0f0f0' } },
      axisLabel: { color: '#909399' }
    },
    series
  }
})

watch(() => props.days, loadData)

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.trend-chart {
  height: 100%;
  display: flex;
  flex-direction: column;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    .title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
    }
  }

  .chart {
    flex: 1;
    min-height: 250px;
  }
}
</style>
