<template>
  <div class="disease-heatmap-page">
    <div class="page-card">
      <div class="page-title">疾病分布热力图</div>
      
      <div class="toolbar">
        <div>
          <el-select v-model="filterPetType" placeholder="宠物类型" style="width: 150px; margin-right: 10px;" clearable>
            <el-option label="猫" value="猫" />
            <el-option label="狗" value="狗" />
            <el-option label="兔子" value="兔子" />
          </el-select>
          <el-button type="primary" @click="loadData">查询</el-button>
        </div>
      </div>

      <el-row :gutter="20">
        <el-col :span="24">
          <div class="chart-container">
            <div ref="heatmapChartRef" style="height: 500px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">按疾病类型统计</h3>
            <div ref="diseasePieChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">按年龄分布统计</h3>
            <div ref="ageBarChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDiseaseHeatmap } from '@/api'

const heatmapChartRef = ref(null)
const diseasePieChartRef = ref(null)
const ageBarChartRef = ref(null)
const filterPetType = ref('')

const heatmapData = ref([])
let heatmapChart = null
let diseasePieChart = null
let ageBarChart = null

const loadData = async () => {
  const res = await getDiseaseHeatmap({ petType: filterPetType.value })
  heatmapData.value = res.data
  nextTick(() => {
    initHeatmapChart()
    initDiseasePieChart()
    initAgeBarChart()
  })
}

const initHeatmapChart = () => {
  if (!heatmapChartRef.value) return
  if (heatmapChart) heatmapChart.dispose()
  heatmapChart = echarts.init(heatmapChartRef.value)

  // 构建热力图数据
  const diseases = [...new Set(heatmapData.value.map(d => d.diseaseName))]
  const ages = [...new Set(heatmapData.value.map(d => d.petAge))].sort((a, b) => a - b)
  
  const data = []
  heatmapData.value.forEach(item => {
    const x = diseases.indexOf(item.diseaseName)
    const y = ages.indexOf(item.petAge)
    if (x >= 0 && y >= 0) {
      data.push([x, y, item.count])
    }
  })

  const option = {
    title: {
      text: '疾病-年龄分布热力图',
      left: 'center'
    },
    tooltip: {
      position: 'top',
      formatter: function(params) {
        return `疾病: ${diseases[params.data[0]]}<br/>年龄: ${ages[params.data[1]]}岁<br/>病例数: ${params.data[2]}`
      }
    },
    grid: {
      height: '70%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: diseases,
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'category',
      data: ages.map(a => a + '岁'),
      name: '年龄'
    },
    visualMap: {
      min: 0,
      max: Math.max(...data.map(d => d[2]), 10),
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '0%',
      inRange: {
        color: ['#50a3ba', '#eac763', '#d94e5d']
      }
    },
    series: [{
      name: '病例数',
      type: 'heatmap',
      data: data,
      label: {
        show: true,
        fontSize: 12
      },
      emphasis: {
        itemStyle: {
          borderColor: '#333',
          borderWidth: 2
        }
      }
    }]
  }

  heatmapChart.setOption(option)
}

const initDiseasePieChart = () => {
  if (!diseasePieChartRef.value) return
  if (diseasePieChart) diseasePieChart.dispose()
  diseasePieChart = echarts.init(diseasePieChartRef.value)

  const diseaseCount = {}
  heatmapData.value.forEach(item => {
    diseaseCount[item.diseaseName] = (diseaseCount[item.diseaseName] || 0) + item.count
  })
  
  const data = Object.entries(diseaseCount).map(([name, value]) => ({ name, value }))

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
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}: {c}'
      },
      data: data
    }]
  }

  diseasePieChart.setOption(option)
}

const initAgeBarChart = () => {
  if (!ageBarChartRef.value) return
  if (ageBarChart) ageBarChart.dispose()
  ageBarChart = echarts.init(ageBarChartRef.value)

  const ageCount = {}
  heatmapData.value.forEach(item => {
    ageCount[item.petAge] = (ageCount[item.petAge] || 0) + item.count
  })
  
  const ages = Object.keys(ageCount).sort((a, b) => a - b)
  const data = ages.map(age => ageCount[age])

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: ages.map(a => a + '岁'),
      name: '年龄'
    },
    yAxis: {
      type: 'value',
      name: '病例数'
    },
    series: [{
      type: 'bar',
      data: data,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#83bff6' },
          { offset: 0.5, color: '#188df0' },
          { offset: 1, color: '#188df0' }
        ])
      },
      emphasis: {
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#2378f7' },
            { offset: 0.7, color: '#2378f7' },
            { offset: 1, color: '#83bff6' }
          ])
        }
      }
    }]
  }

  ageBarChart.setOption(option)
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    heatmapChart?.resize()
    diseasePieChart?.resize()
    ageBarChart?.resize()
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
