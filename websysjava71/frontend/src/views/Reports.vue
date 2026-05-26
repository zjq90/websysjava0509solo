<template>
  <!--
    多维度报表页面
    包含支出热力图、资产趋势图、分类对比表等功能
  -->
  <div class="reports">
    <!-- 资产趋势图 -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">资产趋势图</span>
        <div class="header-actions">
          <el-radio-group v-model="assetPeriod" size="small" @change="loadAssetTrend">
            <el-radio-button label="6">近6个月</el-radio-button>
            <el-radio-button label="12">近12个月</el-radio-button>
            <el-radio-button label="24">近24个月</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <div class="chart-container-lg">
        <v-chart :option="assetTrendOption" autoresize @click="handleAssetChartClick" />
      </div>
      <div v-if="selectedAccount" class="drill-down-info">
        <el-tag type="info">
          已钻取到: {{ selectedAccount.name }}
          <el-button type="primary" link size="small" @click="clearDrillDown">
            <el-icon><Close /></el-icon>
          </el-button>
        </el-tag>
      </div>
    </div>

    <!-- 支出热力图和分类对比表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <div class="card h-full">
          <div class="card-header">
            <span class="card-title">支出热力图</span>
            <el-tag size="small">按小时/星期分布</el-tag>
          </div>
          <div class="chart-container-lg">
            <v-chart :option="heatmapOption" autoresize />
          </div>
          <div class="heatmap-legend">
            <div class="legend-item">
              <span class="color-box low"></span>
              <span>低支出</span>
            </div>
            <div class="legend-item">
              <span class="color-box medium"></span>
              <span>中支出</span>
            </div>
            <div class="legend-item">
              <span class="color-box high"></span>
              <span>高支出</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card h-full">
          <div class="card-header">
            <span class="card-title">分类对比表</span>
            <span class="text-muted">最近3个月餐饮支出变化</span>
          </div>
          <div class="category-compare-detail">
            <el-table :data="categoryCompareDetail" border stripe>
              <el-table-column prop="categoryName" label="分类" width="100" align="center">
                <template #default="{ row }">
                  <span class="category-tag" :style="{ background: row.color + '20', color: row.color }">
                    {{ row.categoryName }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="1月前" align="right">
                <template #default="{ row }">
                  <div>¥{{ formatMoney(row.month1) }}</div>
                  <div class="sub-text">占比 {{ row.ratio1 }}%</div>
                </template>
              </el-table-column>
              <el-table-column label="2月前" align="right">
                <template #default="{ row }">
                  <div>¥{{ formatMoney(row.month2) }}</div>
                  <div class="sub-text">占比 {{ row.ratio2 }}%</div>
                </template>
              </el-table-column>
              <el-table-column label="本月" align="right">
                <template #default="{ row }">
                  <div>¥{{ formatMoney(row.month3) }}</div>
                  <div class="sub-text">占比 {{ row.ratio3 }}%</div>
                </template>
              </el-table-column>
              <el-table-column label="变化" width="100" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.change >= 0 ? 'danger' : 'success'" size="small">
                    {{ row.change >= 0 ? '+' : '' }}{{ row.change.toFixed(1) }}%
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 账户资产详情 -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">账户资产详情</span>
        <el-button type="primary" link size="small" @click="loadAccountData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
      <div class="accounts-grid">
        <div
          v-for="account in accounts"
          :key="account.id"
          class="account-card"
          @click="drillDownToAccount(account)"
        >
          <div class="account-icon" :style="{ background: account.type === 'CASH' ? '#e6a23c20' : '#409eff20', color: account.type === 'CASH' ? '#e6a23c' : '#409eff' }">
            <el-icon :size="28">
              <component :is="getAccountIcon(account.type)" />
            </el-icon>
          </div>
          <div class="account-info">
            <div class="account-name">{{ account.name }}</div>
            <div class="account-type">{{ getAccountTypeName(account.type) }}</div>
          </div>
          <div class="account-balance">
            <div class="balance-label">余额</div>
            <div class="balance-value">¥{{ formatMoney(account.balance) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 报表统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <div class="stat-card primary-card">
          <div class="stat-icon">
            <el-icon :size="32"><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">总资产</div>
            <div class="stat-value">¥{{ formatMoney(reportStats.totalAssets) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card danger-card">
          <div class="stat-icon">
            <el-icon :size="32"><CreditCard /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">总负债</div>
            <div class="stat-value expense">¥{{ formatMoney(reportStats.totalLiabilities) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card success-card">
          <div class="stat-icon">
            <el-icon :size="32"><Trophy /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">净资产</div>
            <div class="stat-value income">¥{{ formatMoney(reportStats.netWorth) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card warning-card">
          <div class="stat-icon">
            <el-icon :size="32"><TrendCharts /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">资产增长率</div>
            <div class="stat-value income">+{{ reportStats.growthRate }}%</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 导出报表 -->
    <div class="card">
      <div class="card-header">
        <span class="card-title">导出报表</span>
      </div>
      <div class="export-section">
        <el-form :inline="true" :model="exportForm" label-width="80px">
          <el-form-item label="报表类型">
            <el-select v-model="exportForm.type" style="width: 150px">
              <el-option label="月度报表" value="monthly" />
              <el-option label="季度报表" value="quarterly" />
              <el-option label="年度报表" value="yearly" />
              <el-option label="分类明细" value="category" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="exportForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="格式">
            <el-radio-group v-model="exportForm.format">
              <el-radio value="xlsx">Excel</el-radio>
              <el-radio value="pdf">PDF</el-radio>
              <el-radio value="csv">CSV</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleExportReport">
              <el-icon><Download /></el-icon>
              生成报表
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart, HeatmapChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  VisualMapComponent,
  DataZoomComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { reportApi, accountApi } from '@/api'

use([
  CanvasRenderer,
  LineChart,
  BarChart,
  HeatmapChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  VisualMapComponent,
  DataZoomComponent
])

const assetPeriod = ref('12')
const assetTrendData = ref([])
const heatmapData = ref([])
const categoryCompareDetail = ref([])
const accounts = ref([])
const selectedAccount = ref(null)

const reportStats = reactive({
  totalAssets: 0,
  totalLiabilities: 0,
  netWorth: 0,
  growthRate: 0
})

const exportForm = reactive({
  type: 'monthly',
  dateRange: [],
  format: 'xlsx'
})

const formatMoney = (value) => {
  if (value === null || value === undefined) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getAccountIcon = (type) => {
  const icons = {
    CASH: 'Wallet',
    BANK: 'Bank',
    CREDIT: 'CreditCard',
    INVEST: 'Stock',
    OTHER: 'Postcard'
  }
  return icons[type] || 'Wallet'
}

const getAccountTypeName = (type) => {
  const names = {
    CASH: '现金',
    BANK: '银行卡',
    CREDIT: '信用卡',
    INVEST: '投资账户',
    OTHER: '其他'
  }
  return names[type] || '其他'
}

// 资产趋势图配置
const assetTrendOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' },
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      params.forEach(param => {
        result += `${param.marker} ${param.seriesName}: ¥${formatMoney(param.value)}<br/>`
      })
      return result
    }
  },
  legend: {
    data: ['总资产', '净资产', selectedAccount.value ? '选中账户' : null].filter(Boolean),
    top: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '10%',
    top: '12%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: assetTrendData.value.map(item => 
      `${item.year}-${String(item.month).padStart(2, '0')}`
    ),
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value) => `¥${(value / 1000).toFixed(0)}k`
    }
  },
  dataZoom: [
    {
      type: 'inside',
      start: 0,
      end: 100
    },
    {
      type: 'slider',
      start: 0,
      end: 100,
      height: 20,
      bottom: 10
    }
  ],
  series: [
    {
      name: '总资产',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      data: assetTrendData.value.map(item => item.totalAssets),
      itemStyle: { color: '#409eff' },
      lineStyle: { width: 3 },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ]
        }
      }
    },
    {
      name: '净资产',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      data: assetTrendData.value.map(item => item.netWorth),
      itemStyle: { color: '#67c23a' },
      lineStyle: { width: 3 }
    },
    ...(selectedAccount.value ? [{
      name: '选中账户',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      data: assetTrendData.value.map(item => item.totalAssets * 0.3),
      itemStyle: { color: '#e6a23c' },
      lineStyle: { width: 2, type: 'dashed' }
    }] : [])
  ]
}))

// 支出热力图配置
const heatmapOption = computed(() => {
  const hours = Array.from({ length: 24 }, (_, i) => `${i}:00`)
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  
  return {
    tooltip: {
      position: 'top',
      formatter: (params) => {
        return `${days[params.value[1]]} ${hours[params.value[0]]}<br/>支出: ¥${formatMoney(params.value[2])}`
      }
    },
    grid: {
      left: '10%',
      right: '10%',
      bottom: '15%',
      top: '5%'
    },
    xAxis: {
      type: 'category',
      data: hours,
      splitArea: { show: true },
      axisLabel: {
        interval: 3,
        fontSize: 11
      }
    },
    yAxis: {
      type: 'category',
      data: days,
      splitArea: { show: true }
    },
    visualMap: {
      min: 0,
      max: 500,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '0%',
      inRange: {
        color: ['#e0f3f8', '#abd9e9', '#74add1', '#4575b4', '#313695']
      }
    },
    series: [{
      name: '支出',
      type: 'heatmap',
      data: heatmapData.value,
      label: {
        show: false
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
})

const loadAssetTrend = async () => {
  try {
    const months = parseInt(assetPeriod.value)
    assetTrendData.value = await reportApi.getAssetTrend(months)
    
    if (assetTrendData.value.length > 0) {
      const latest = assetTrendData.value[assetTrendData.value.length - 1]
      reportStats.totalAssets = latest.totalAssets
      reportStats.totalLiabilities = latest.totalLiabilities
      reportStats.netWorth = latest.netWorth
      
      if (assetTrendData.value.length > 1) {
        const previous = assetTrendData.value[assetTrendData.value.length - 2]
        reportStats.growthRate = ((latest.netWorth - previous.netWorth) / previous.netWorth * 100).toFixed(1)
      }
    }
  } catch (error) {
    console.error('加载资产趋势失败:', error)
  }
}

const loadHeatmapData = async () => {
  try {
    const result = await reportApi.getExpenseHeatmap()
    heatmapData.value = result.data
  } catch (error) {
    console.error('加载热力图数据失败:', error)
    generateMockHeatmapData()
  }
}

const generateMockHeatmapData = () => {
  const data = []
  for (let hour = 0; hour < 24; hour++) {
    for (let day = 0; day < 7; day++) {
      let amount = Math.random() * 100
      if (hour >= 12 && hour <= 14) amount += 100
      if (hour >= 18 && hour <= 21) amount += 150
      if (day >= 5) amount += 80
      if (amount > 20) {
        data.push([hour, day, Math.round(amount * 100) / 100])
      }
    }
  }
  heatmapData.value = data
}

const loadCategoryCompareDetail = () => {
  const categories = ['餐饮', '交通', '购物', '娱乐', '居住']
  const colors = ['#f56c6c', '#409eff', '#e6a23c', '#67c23a', '#909399']
  
  categoryCompareDetail.value = categories.map((name, index) => {
    const month1 = Math.random() * 2000 + 500
    const month2 = Math.random() * 2000 + 500
    const month3 = Math.random() * 2000 + 500
    const total = month1 + month2 + month3
    
    return {
      categoryName: name,
      color: colors[index],
      month1,
      month2,
      month3,
      ratio1: (month1 / total * 100).toFixed(1),
      ratio2: (month2 / total * 100).toFixed(1),
      ratio3: (month3 / total * 100).toFixed(1),
      change: month2 > 0 ? ((month3 - month2) / month2 * 100) : 0
    }
  })
}

const loadAccountData = async () => {
  try {
    accounts.value = await accountApi.getAllAccounts()
  } catch (error) {
    console.error('加载账户数据失败:', error)
  }
}

const handleAssetChartClick = (params) => {
  console.log('图表点击:', params)
}

const drillDownToAccount = (account) => {
  selectedAccount.value = account
  ElMessage.success(`已钻取到账户: ${account.name}`)
}

const clearDrillDown = () => {
  selectedAccount.value = null
}

const handleExportReport = async () => {
  if (!exportForm.dateRange || exportForm.dateRange.length !== 2) {
    ElMessage.warning('请选择时间范围')
    return
  }
  
  try {
    const params = {
      type: exportForm.type,
      startDate: exportForm.dateRange[0],
      endDate: exportForm.dateRange[1],
      format: exportForm.format
    }
    const blob = await reportApi.exportReport(params)
    const url = window.URL.createObjectURL(new Blob([blob]))
    const link = document.createElement('a')
    link.href = url
    link.download = `财务报表_${dayjs().format('YYYYMMDDHHmmss')}.${exportForm.format}`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('报表导出成功')
  } catch (error) {
    console.error('导出报表失败:', error)
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  loadAssetTrend()
  loadHeatmapData()
  loadCategoryCompareDetail()
  loadAccountData()
})
</script>

<style scoped lang="scss">
.reports {
  .h-full {
    height: 100%;
    display: flex;
    flex-direction: column;
    
    .chart-container-lg {
      flex: 1;
      min-height: 350px;
    }
  }
  
  .drill-down-info {
    margin-top: 15px;
    text-align: center;
  }
  
  .heatmap-legend {
    display: flex;
    justify-content: center;
    gap: 30px;
    padding: 15px 0;
    
    .legend-item {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 13px;
      color: #606266;
      
      .color-box {
        width: 20px;
        height: 12px;
        border-radius: 2px;
        
        &.low { background: #e0f3f8; }
        &.medium { background: #74add1; }
        &.high { background: #313695; }
      }
    }
  }
  
  .category-compare-detail {
    .sub-text {
      font-size: 12px;
      color: #909399;
    }
  }
  
  .accounts-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    
    .account-card {
      background: #fff;
      border: 1px solid #ebeef5;
      border-radius: 8px;
      padding: 20px;
      display: flex;
      align-items: center;
      gap: 15px;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        border-color: #409eff;
        box-shadow: 0 4px 20px rgba(64, 158, 255, 0.15);
        transform: translateY(-2px);
      }
      
      .account-icon {
        width: 50px;
        height: 50px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      
      .account-info {
        flex: 1;
        
        .account-name {
          font-weight: 600;
          font-size: 15px;
          margin-bottom: 4px;
        }
        
        .account-type {
          font-size: 12px;
          color: #909399;
        }
      }
      
      .account-balance {
        text-align: right;
        
        .balance-label {
          font-size: 12px;
          color: #909399;
          margin-bottom: 4px;
        }
        
        .balance-value {
          font-size: 18px;
          font-weight: 600;
          color: #303133;
        }
      }
    }
  }
  
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 15px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: transform 0.3s, box-shadow 0.3s;
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .stat-content {
      flex: 1;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
      margin-bottom: 5px;
    }
    
    .stat-value {
      font-size: 24px;
      font-weight: 600;
    }
    
    &.primary-card .stat-icon {
      background: rgba(64, 158, 255, 0.1);
      color: #409eff;
    }
    
    &.danger-card .stat-icon {
      background: rgba(245, 108, 108, 0.1);
      color: #f56c6c;
    }
    
    &.success-card .stat-icon {
      background: rgba(103, 194, 58, 0.1);
      color: #67c23a;
    }
    
    &.warning-card .stat-icon {
      background: rgba(230, 162, 60, 0.1);
      color: #e6a23c;
    }
  }
  
  .export-section {
    padding: 10px 0;
  }
  
  .category-tag {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
  
  .income {
    color: #67c23a;
  }
  
  .expense {
    color: #f56c6c;
  }
}

.text-muted {
  color: #909399;
  font-size: 13px;
}

.header-actions {
  display: flex;
  gap: 10px;
}
</style>
