<template>
  <div class="page-container">
    <div class="page-title">销售报表</div>
    
    <el-form :inline="true" :model="queryForm" class="search-bar">
      <el-form-item label="开始日期">
        <el-date-picker
          v-model="queryForm.startDate"
          type="date"
          placeholder="选择开始日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="结束日期">
        <el-date-picker
          v-model="queryForm.endDate"
          type="date"
          placeholder="选择结束日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="时间维度">
        <el-select v-model="queryForm.dimension" placeholder="请选择时间维度">
          <el-option label="日" value="DAY" />
          <el-option label="周" value="WEEK" />
          <el-option label="月" value="MONTH" />
          <el-option label="年" value="YEAR" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付渠道">
        <el-select v-model="queryForm.payChannel" placeholder="请选择支付渠道" clearable>
          <el-option label="微信支付" value="WECHAT" />
          <el-option label="支付宝" value="ALIPAY" />
          <el-option label="银联" value="UNIONPAY" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">
          <el-icon><Search /></el-icon>查询
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="page-container" style="padding: 0;">
          <div style="padding: 20px; border-bottom: 1px solid #ebeef5;">
            <span style="font-weight: bold; font-size: 16px;">销售趋势图</span>
          </div>
          <div ref="chartRef" class="chart-container" style="padding: 20px;"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <div class="page-container">
          <div style="margin-bottom: 20px; font-weight: bold; font-size: 16px;">数据明细</div>
          <el-table
            :data="tableData"
            border
            stripe
            style="width: 100%"
          >
            <el-table-column prop="timeLabel" label="时间" width="150" />
            <el-table-column prop="salesAmount" label="销售额">
              <template #default="scope">
                <span style="color: #409eff; font-weight: bold;">
                  ¥{{ formatAmount(scope.row.salesAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="orderCount" label="订单数" width="120" />
            <el-table-column prop="avgOrderAmount" label="平均订单金额">
              <template #default="scope">
                ¥{{ formatAmount(scope.row.avgOrderAmount) }}
              </template>
            </el-table-column>
          </el-table>
          
          <div style="margin-top: 20px; display: flex; justify-content: space-between; align-items: center;">
            <div>
              <span style="margin-right: 20px;">总销售额：<strong style="color: #409eff;">¥{{ formatAmount(totalSales) }}</strong></span>
              <span>总订单数：<strong style="color: #67c23a;">{{ totalOrders }}</strong></span>
            </div>
            <el-button type="primary" @click="exportData">
              <el-icon><Download /></el-icon>导出数据
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { salesReportApi } from '@/api'

const chartRef = ref(null)
let chart = null

const queryForm = ref({
  startDate: '',
  endDate: '',
  dimension: 'DAY',
  payChannel: ''
})

const tableData = ref([])

const totalSales = computed(() => {
  return tableData.value.reduce((sum, item) => sum + Number(item.salesAmount || 0), 0)
})

const totalOrders = computed(() => {
  return tableData.value.reduce((sum, item) => sum + Number(item.orderCount || 0), 0)
})

const formatAmount = (amount) => {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['销售额', '订单数']
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
      data: []
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
        type: 'line',
        smooth: true,
        areaStyle: {
          opacity: 0.3
        },
        data: [],
        itemStyle: {
          color: '#409eff'
        }
      },
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        yAxisIndex: 1,
        data: [],
        itemStyle: {
          color: '#67c23a'
        }
      }
    ]
  })

  window.addEventListener('resize', () => {
    chart && chart.resize()
  })
}

const updateChart = () => {
  if (!chart) return
  
  chart.setOption({
    xAxis: {
      data: tableData.value.map(item => item.timeLabel)
    },
    series: [
      {
        data: tableData.value.map(item => item.salesAmount)
      },
      {
        data: tableData.value.map(item => item.orderCount)
      }
    ]
  })
}

const loadData = async () => {
  try {
    const params = { ...queryForm.value }
    const data = await salesReportApi.getSalesReport(params)
    tableData.value = data || []
    await nextTick()
    updateChart()
  } catch (error) {
    console.error('Failed to load data:', error)
  }
}

const handleQuery = () => {
  loadData()
}

const handleReset = () => {
  queryForm.value = {
    startDate: '',
    endDate: '',
    dimension: 'DAY',
    payChannel: ''
  }
  loadData()
}

const exportData = () => {
  const headers = ['时间', '销售额', '订单数', '平均订单金额']
  const rows = tableData.value.map(item => [
    item.timeLabel,
    formatAmount(item.salesAmount),
    item.orderCount,
    formatAmount(item.avgOrderAmount)
  ])
  
  let csvContent = headers.join(',') + '\n'
  rows.forEach(row => {
    csvContent += row.join(',') + '\n'
  })
  
  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `销售报表_${new Date().toISOString().slice(0, 10)}.csv`
  link.click()
}

onMounted(() => {
  initChart()
  loadData()
})
</script>
