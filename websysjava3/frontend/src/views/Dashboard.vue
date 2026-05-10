<template>
  <div>
    <div class="page-title">数据概览</div>
    
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-title">今日销售额</div>
          <div class="stat-value">¥{{ formatAmount(dashboard.todaySales) }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-title">今日订单数</div>
          <div class="stat-value">{{ dashboard.todayOrders || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-title">待处理异常订单</div>
          <div class="stat-value">{{ dashboard.pendingExceptionOrders || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card red">
          <div class="stat-title">待审核发票</div>
          <div class="stat-value">{{ dashboard.pendingInvoices || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="12">
        <div class="page-container">
          <div class="page-title">本月统计</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="stat-card blue">
                <div class="stat-title">本月销售额</div>
                <div class="stat-value">¥{{ formatAmount(dashboard.monthSales) }}</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-card green">
                <div class="stat-title">本月订单数</div>
                <div class="stat-value">{{ dashboard.monthOrders || 0 }}</div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="page-container">
          <div class="page-title">快捷操作</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card shadow="hover" style="text-align: center; cursor: pointer;" @click="goTo('/exception-orders')">
                <el-icon :size="30" color="#f56c6c"><Warning /></el-icon>
                <div style="margin-top: 10px; font-weight: bold;">异常订单处理</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" style="text-align: center; cursor: pointer;" @click="goTo('/reconciliations')">
                <el-icon :size="30" color="#409eff"><Tickets /></el-icon>
                <div style="margin-top: 10px; font-weight: bold;">对账管理</div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card shadow="hover" style="text-align: center; cursor: pointer;" @click="goTo('/invoices')">
                <el-icon :size="30" color="#67c23a"><Document /></el-icon>
                <div style="margin-top: 10px; font-weight: bold;">发票审核</div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>

    <div class="page-container">
      <div class="page-title">销售趋势（最近30天）</div>
      <div ref="chartRef" class="chart-container"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'
import { salesReportApi } from '@/api'

const router = useRouter()
const chartRef = ref(null)
let chart = null

const dashboard = ref({
  todaySales: 0,
  todayOrders: 0,
  pendingExceptionOrders: 0,
  pendingInvoices: 0,
  monthSales: 0,
  monthOrders: 0
})

const formatAmount = (amount) => {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const goTo = (path) => {
  router.push(path)
}

const loadDashboard = async () => {
  try {
    const data = await salesReportApi.getDashboardStats()
    dashboard.value = data
  } catch (error) {
    console.error('Failed to load dashboard:', error)
  }
}

const loadChartData = async () => {
  try {
    const data = await salesReportApi.getSalesReport({ dimension: 'DAY' })
    
    await nextTick()
    
    if (!chartRef.value) return
    
    chart = echarts.init(chartRef.value)
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        }
      },
      legend: {
        data: ['销售额', '订单数']
      },
      xAxis: {
        type: 'category',
        data: data.map(item => item.timeLabel),
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: [
        {
          type: 'value',
          name: '销售额(元)',
          position: 'left'
        },
        {
          type: 'value',
          name: '订单数',
          position: 'right'
        }
      ],
      series: [
        {
          name: '销售额',
          type: 'bar',
          data: data.map(item => item.salesAmount),
          itemStyle: {
            color: '#409eff'
          }
        },
        {
          name: '订单数',
          type: 'line',
          yAxisIndex: 1,
          data: data.map(item => item.orderCount),
          itemStyle: {
            color: '#67c23a'
          }
        }
      ]
    }
    
    chart.setOption(option)
    
    window.addEventListener('resize', () => {
      chart && chart.resize()
    })
  } catch (error) {
    console.error('Failed to load chart data:', error)
  }
}

onMounted(() => {
  loadDashboard()
  loadChartData()
})
</script>
