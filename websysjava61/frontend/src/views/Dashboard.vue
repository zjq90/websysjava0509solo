<template>
  <div class="dashboard">
    <div class="page-title">数据看板</div>
    
    <div class="stat-cards">
      <div class="stat-card">
        <div class="stat-title">今日接诊量</div>
        <div class="stat-value">{{ stats.todayConsultations || 0 }}</div>
      </div>
      <div class="stat-card success">
        <div class="stat-title">今日收入(元)</div>
        <div class="stat-value">{{ stats.todayRevenue || 0 }}</div>
      </div>
      <div class="stat-card warning">
        <div class="stat-title">今日急诊数</div>
        <div class="stat-value">{{ stats.todayEmergency || 0 }}</div>
      </div>
      <div class="stat-card danger">
        <div class="stat-title">待审核医生</div>
        <div class="stat-value">{{ stats.pendingDoctorAudit || 0 }}</div>
      </div>
      <div class="stat-card danger">
        <div class="stat-title">待审核宠物主人</div>
        <div class="stat-value">{{ stats.pendingOwnerAudit || 0 }}</div>
      </div>
      <div class="stat-card warning">
        <div class="stat-title">库存预警药品</div>
        <div class="stat-value">{{ stats.warningMedicineCount || 0 }}</div>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-container">
          <h3>接诊趋势</h3>
          <div ref="consultationChart" style="height: 300px;"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-container">
          <h3>收入趋势</h3>
          <div ref="revenueChart" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStats } from '@/api'
import * as echarts from 'echarts'

const stats = ref({})
const consultationChart = ref(null)
const revenueChart = ref(null)

const loadData = async () => {
  const res = await getDashboardStats()
  stats.value = res.data
}

const initCharts = () => {
  const consultationChartInstance = echarts.init(consultationChart.value)
  const revenueChartInstance = echarts.init(revenueChart.value)

  const consultationOption = {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [30, 45, 35, 50, 40, 60, 55],
      type: 'line',
      smooth: true,
      itemStyle: { color: '#409EFF' }
    }]
  }

  const revenueOption = {
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [3000, 4500, 3500, 5000, 4000, 6000, 5500],
      type: 'bar',
      itemStyle: { color: '#67C23A' }
    }]
  }

  consultationChartInstance.setOption(consultationOption)
  revenueChartInstance.setOption(revenueOption)
}

onMounted(() => {
  loadData()
  initCharts()
})
</script>

<style lang="scss" scoped>
.dashboard {
  h3 {
    margin-bottom: 20px;
    font-size: 16px;
    color: #303133;
  }
}
</style>
