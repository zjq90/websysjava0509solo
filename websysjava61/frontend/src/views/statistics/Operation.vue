<template>
  <div class="operation-page">
    <div class="page-card">
      <div class="page-title">医院运营数据</div>
      
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
        <el-button type="success" @click="exportData" style="margin-left: 10px;">
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>

      <!-- 统计卡片 -->
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="6">
          <div class="stat-card primary">
            <div class="stat-value">{{ stats.totalConsultations || 0 }}</div>
            <div class="stat-label">总接诊量</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card success">
            <div class="stat-value">¥{{ stats.totalRevenue || 0 }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card warning">
            <div class="stat-value">{{ stats.emergencyCount || 0 }}</div>
            <div class="stat-label">急诊数量</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card danger">
            <div class="stat-value">{{ stats.avgFee || 0 }}</div>
            <div class="stat-label">平均客单价</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">接诊量趋势</h3>
            <div ref="consultationChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">收入趋势</h3>
            <div ref="revenueChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">科室接诊分布</h3>
            <div ref="departmentChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h3 style="margin-bottom: 15px;">宠物类型分布</h3>
            <div ref="petTypeChartRef" style="height: 350px; width: 100%;"></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getHospitalOperation } from '@/api'

const consultationChartRef = ref(null)
const revenueChartRef = ref(null)
const departmentChartRef = ref(null)
const petTypeChartRef = ref(null)
const dateRange = ref(null)
const stats = ref({})

let consultationChart = null
let revenueChart = null
let departmentChart = null
let petTypeChart = null

const loadData = async () => {
  const params = {}
  if (dateRange.value && dateRange.value.length === 2) {
    params.startTime = dateRange.value[0]
    params.endTime = dateRange.value[1]
  }
  
  // 模拟统计数据
  stats.value = {
    totalConsultations: 1256,
    totalRevenue: 187500,
    emergencyCount: 156,
    avgFee: 149
  }
  
  nextTick(() => {
    initConsultationChart()
    initRevenueChart()
    initDepartmentChart()
    initPetTypeChart()
  })
}

const initConsultationChart = () => {
  if (!consultationChartRef.value) return
  if (consultationChart) consultationChart.dispose()
  consultationChart = echarts.init(consultationChartRef.value)

  const dates = ['01-01', '01-02', '01-03', '01-04', '01-05', '01-06', '01-07']
  const data = [85, 92, 78, 105, 98, 120, 115]

  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', name: '接诊量' },
    series: [{
      type: 'line',
      smooth: true,
      data: data,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
        ])
      },
      itemStyle: { color: '#409EFF' }
    }]
  }

  consultationChart.setOption(option)
}

const initRevenueChart = () => {
  if (!revenueChartRef.value) return
  if (revenueChart) revenueChart.dispose()
  revenueChart = echarts.init(revenueChartRef.value)

  const dates = ['01-01', '01-02', '01-03', '01-04', '01-05', '01-06', '01-07']
  const data = [12500, 13800, 11600, 15750, 14700, 18000, 17250]

  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', name: '收入(元)' },
    series: [{
      type: 'bar',
      data: data,
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#67C23A' },
          { offset: 1, color: '#95d475' }
        ])
      }
    }]
  }

  revenueChart.setOption(option)
}

const initDepartmentChart = () => {
  if (!departmentChartRef.value) return
  if (departmentChart) departmentChart.dispose()
  departmentChart = echarts.init(departmentChartRef.value)

  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 350, name: '内科' },
        { value: 280, name: '外科' },
        { value: 156, name: '急诊' },
        { value: 200, name: '皮肤科' },
        { value: 180, name: '口腔科' },
        { value: 90, name: '影像科' }
      ],
      color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#00CED1']
    }]
  }

  departmentChart.setOption(option)
}

const initPetTypeChart = () => {
  if (!petTypeChartRef.value) return
  if (petTypeChart) petTypeChart.dispose()
  petTypeChart = echarts.init(petTypeChartRef.value)

  const option = {
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 520, name: '猫' },
        { value: 480, name: '狗' },
        { value: 150, name: '兔子' },
        { value: 80, name: '仓鼠' },
        { value: 26, name: '鹦鹉' }
      ],
      roseType: 'radius',
      itemStyle: {
        borderRadius: 8
      }
    }]
  }

  petTypeChart.setOption(option)
}

const exportData = async () => {
  try {
    // 模拟导出
    ElMessage.success('数据导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  loadData()
  window.addEventListener('resize', () => {
    consultationChart?.resize()
    revenueChart?.resize()
    departmentChart?.resize()
    petTypeChart?.resize()
  })
})
</script>

<style lang="scss" scoped>
.chart-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
}

.stat-card {
  padding: 25px;
  border-radius: 8px;
  color: #fff;
  text-align: center;
  
  &.primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
  &.success { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
  &.warning { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
  &.danger { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
  
  .stat-value {
    font-size: 32px;
    font-weight: bold;
    margin-bottom: 8px;
  }
  
  .stat-label {
    font-size: 14px;
    opacity: 0.9;
  }
}
</style>
