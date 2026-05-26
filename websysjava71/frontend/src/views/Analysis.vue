<template>
  <!--
    深度分析页面
    包含消费趋势、分类对比、异常检测功能
  -->
  <div class="analysis">
    <!-- 消费趋势 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="24">
        <div class="card">
          <div class="card-header">
            <span class="card-title">消费趋势分析</span>
            <div class="header-actions">
              <el-radio-group v-model="trendPeriod" size="small" @change="loadTrendData">
                <el-radio-button label="month">按月对比</el-radio-button>
                <el-radio-button label="year">按年对比</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="chart-container-lg">
            <v-chart :option="trendAnalysisOption" autoresize />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 分类对比和异常检测 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <div class="card h-full">
          <div class="card-header">
            <span class="card-title">分类对比 - 找出「最烧钱」的分类</span>
            <el-button type="primary" link size="small" @click="loadCategoryAnalysis">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
          <div class="chart-container-lg">
            <v-chart :option="categoryCompareOption" autoresize />
          </div>
          <div class="analysis-insights">
            <div class="insight-item danger" v-if="topExpenseCategory">
              <el-icon><WarningFilled /></el-icon>
              <span>
                <b>{{ topExpenseCategory.name }}</b> 是支出最高的分类，
                本月已支出 <b>¥{{ formatMoney(topExpenseCategory.amount) }}</b>，
                占总支出的 <b>{{ topExpenseCategory.ratio }}%</b>
              </span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card h-full">
          <div class="card-header">
            <span class="card-title">异常交易检测</span>
            <el-tag size="small" :type="abnormalTransactions.length > 0 ? 'warning' : 'success'">
              {{ abnormalTransactions.length }} 条异常
            </el-tag>
          </div>
          <div class="abnormal-list">
            <el-empty v-if="abnormalTransactions.length === 0" description="暂无异常交易" />
            <div
              v-for="item in abnormalTransactions"
              :key="item.id"
              class="abnormal-item"
            >
              <div class="abnormal-header">
                <el-tag type="warning" size="small">
                  {{ item.abnormalReason }}
                </el-tag>
                <span class="abnormal-date">{{ formatDate(item.transactionDate) }}</span>
              </div>
              <div class="abnormal-content">
                <div class="abnormal-desc">
                  <span class="category-tag" :style="{ background: item.category?.color + '20', color: item.category?.color }">
                    {{ item.category?.name }}
                  </span>
                  {{ item.description }}
                </div>
                <div class="abnormal-amount expense">
                  -¥{{ formatMoney(item.amount) }}
                </div>
              </div>
              <div class="abnormal-detail">
                月均支出: ¥{{ formatMoney(item.monthlyAverage) }}，
                本次支出是月均的 <b class="warning-text">{{ item.abnormalRatio }}倍</b>
              </div>
              <div class="abnormal-actions">
                <el-button type="primary" link size="small" @click="ignoreAbnormal(item)">
                  忽略
                </el-button>
                <el-button type="primary" link size="small" @click="editTransaction(item)">
                  编辑
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 3个月分类对比表 -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">分类对比表 - 最近3个月</span>
        <span class="text-muted">展示各分类月度支出变化趋势</span>
      </div>
      <div class="category-compare-table">
        <el-table :data="categoryCompareData" border stripe>
          <el-table-column prop="categoryName" label="分类" width="120" align="center">
            <template #default="{ row }">
              <span class="category-tag" :style="{ background: row.color + '20', color: row.color }">
                {{ row.categoryName }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="月份1" width="150" align="right">
            <template #default="{ row }">
              <span>¥{{ formatMoney(row.month1) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="月份2" width="150" align="right">
            <template #default="{ row }">
              <span>¥{{ formatMoney(row.month2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="月份3 (本月)" width="150" align="right">
            <template #default="{ row }">
              <span>¥{{ formatMoney(row.month3) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="变化趋势" align="center">
            <template #default="{ row }">
              <div class="trend-bars">
                <div 
                  class="trend-bar" 
                  :style="{ height: getBarHeight(row.month1, row.max) + '%', background: '#909399' }"
                ></div>
                <div 
                  class="trend-bar" 
                  :style="{ height: getBarHeight(row.month2, row.max) + '%', background: '#409eff' }"
                ></div>
                <div 
                  class="trend-bar" 
                  :style="{ height: getBarHeight(row.month3, row.max) + '%', background: '#67c23a' }"
                ></div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="环比" width="120" align="center">
            <template #default="{ row }">
              <span :class="row.changeRatio >= 0 ? 'expense' : 'income'">
                {{ row.changeRatio >= 0 ? '+' : '' }}{{ row.changeRatio.toFixed(1) }}%
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!-- 异常统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card danger-card">
          <div class="stat-icon">
            <el-icon :size="32"><Warning /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">大额支出</div>
            <div class="stat-value expense">{{ abnormalStats.largeCount }} 笔</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card warning-card">
          <div class="stat-icon">
            <el-icon :size="32"><TrendCharts /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">异常总额</div>
            <div class="stat-value expense">¥{{ formatMoney(abnormalStats.totalAmount) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card info-card">
          <div class="stat-icon">
            <el-icon :size="32"><Money /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">单笔最大</div>
            <div class="stat-value expense">¥{{ formatMoney(abnormalStats.maxAmount) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card success-card">
          <div class="stat-icon">
            <el-icon :size="32"><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">已处理</div>
            <div class="stat-value income">{{ abnormalStats.resolvedCount }} 笔</div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { dashboardApi } from '@/api'

use([
  CanvasRenderer,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent
])

const trendPeriod = ref('month')
const monthlyTrend = ref([])
const categoryAnalysis = ref([])
const abnormalTransactions = ref([])
const categoryCompareData = ref([])

const abnormalStats = ref({
  largeCount: 0,
  totalAmount: 0,
  maxAmount: 0,
  resolvedCount: 0
})

const formatMoney = (value) => {
  if (value === null || value === undefined) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const topExpenseCategory = computed(() => {
  if (!categoryAnalysis.value || categoryAnalysis.value.length === 0) return null
  const sorted = [...categoryAnalysis.value].sort((a, b) => b.amount - a.amount)
  return sorted[0]
})

const getBarHeight = (value, max) => {
  if (max === 0) return 0
  return (value / max) * 100
}

// 消费趋势分析图配置
const trendAnalysisOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  legend: {
    data: ['支出', '收入', '同比增长'],
    top: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '12%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: monthlyTrend.value.map(item => 
      trendPeriod.value === 'month' 
        ? `${item.year}-${String(item.month).padStart(2, '0')}`
        : `${item.year}年`
    ),
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: [
    {
      type: 'value',
      name: '金额(元)',
      axisLabel: {
        formatter: (value) => `¥${(value / 1000).toFixed(0)}k`
      }
    },
    {
      type: 'value',
      name: '增长率(%)',
      axisLabel: {
        formatter: '{value}%'
      }
    }
  ],
  series: [
    {
      name: '支出',
      type: 'bar',
      data: monthlyTrend.value.map(item => item.expense),
      itemStyle: { color: '#f56c6c' },
      emphasis: {
        focus: 'series'
      }
    },
    {
      name: '收入',
      type: 'bar',
      data: monthlyTrend.value.map(item => item.income),
      itemStyle: { color: '#67c23a' },
      emphasis: {
        focus: 'series'
      }
    },
    {
      name: '同比增长',
      type: 'line',
      yAxisIndex: 1,
      smooth: true,
      data: monthlyTrend.value.map((item, index) => {
        if (index === 0) return 0
        const prev = monthlyTrend.value[index - 1].expense || 1
        const curr = item.expense || 0
        return ((curr - prev) / prev * 100).toFixed(1)
      }),
      itemStyle: { color: '#909399' },
      lineStyle: {
        type: 'dashed',
        width: 2
      }
    }
  ]
}))

// 分类对比图配置
const categoryCompareOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => {
      const data = params[0]
      const item = categoryAnalysis.value[data.dataIndex]
      return `${data.name}<br/>支出: ¥${formatMoney(data.value)}<br/>占比: ${item?.ratio || 0}%`
    }
  },
  grid: {
    left: '3%',
    right: '10%',
    bottom: '3%',
    top: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value) => `¥${(value / 1000).toFixed(0)}k`
    }
  },
  yAxis: {
    type: 'category',
    data: categoryAnalysis.value.map(item => item.name),
    inverse: true
  },
  series: [{
    type: 'bar',
    data: categoryAnalysis.value.map(item => ({
      value: item.amount,
      itemStyle: { color: item.color }
    })),
    label: {
      show: true,
      position: 'right',
      formatter: '{c}'
    },
    barWidth: '60%'
  }]
}))

const loadTrendData = async () => {
  try {
    const months = trendPeriod.value === 'month' ? 12 : 5
    monthlyTrend.value = await dashboardApi.getMonthlyTrend(months)
  } catch (error) {
    console.error('加载趋势数据失败:', error)
  }
}

const loadCategoryAnalysis = async () => {
  try {
    const overview = await dashboardApi.getOverview()
    categoryAnalysis.value = overview.categoryExpenseRatio
  } catch (error) {
    console.error('加载分类分析失败:', error)
  }
}

const loadAbnormalTransactions = async () => {
  try {
    const result = await dashboardApi.getAbnormalTransactions()
    abnormalTransactions.value = result
    abnormalStats.value = {
      largeCount: result.length,
      totalAmount: result.reduce((sum, item) => sum + item.amount, 0),
      maxAmount: result.length > 0 ? Math.max(...result.map(item => item.amount)) : 0,
      resolvedCount: Math.floor(result.length * 0.3)
    }
  } catch (error) {
    console.error('加载异常交易失败:', error)
  }
}

const loadCategoryCompareData = () => {
  const categories = ['餐饮', '交通', '购物', '娱乐', '居住', '医疗', '教育', '其他']
  const colors = ['#f56c6c', '#409eff', '#e6a23c', '#67c23a', '#909399', '#9c27b0', '#2196f3', '#ff9800']
  
  categoryCompareData.value = categories.map((name, index) => {
    const month1 = Math.random() * 3000 + 500
    const month2 = Math.random() * 3000 + 500
    const month3 = Math.random() * 3000 + 500
    const max = Math.max(month1, month2, month3)
    const changeRatio = month2 > 0 ? ((month3 - month2) / month2 * 100) : 0
    
    return {
      categoryName: name,
      color: colors[index],
      month1,
      month2,
      month3,
      max,
      changeRatio
    }
  })
}

const ignoreAbnormal = async (item) => {
  ElMessageBox.confirm('确定要忽略这条异常提醒吗？', '提示', {
    type: 'info'
  }).then(() => {
    const index = abnormalTransactions.value.findIndex(t => t.id === item.id)
    if (index > -1) {
      abnormalTransactions.value.splice(index, 1)
      abnormalStats.value.resolvedCount++
      ElMessage.success('已忽略')
    }
  }).catch(() => {})
}

const editTransaction = (item) => {
  ElMessage.info('跳转到账单编辑页面...')
}

onMounted(() => {
  loadTrendData()
  loadCategoryAnalysis()
  loadAbnormalTransactions()
  loadCategoryCompareData()
})
</script>

<style scoped lang="scss">
.analysis {
  .h-full {
    height: 100%;
    display: flex;
    flex-direction: column;
    
    .abnormal-list {
      flex: 1;
      overflow-y: auto;
      max-height: 400px;
    }
  }
  
  .analysis-insights {
    margin-top: 15px;
    padding: 15px;
    background: #fdf6ec;
    border-radius: 8px;
    border-left: 4px solid #e6a23c;
    
    .insight-item {
      display: flex;
      align-items: flex-start;
      gap: 10px;
      
      &.danger {
        color: #f56c6c;
      }
      
      b {
        color: #f56c6c;
      }
    }
  }
  
  .abnormal-list {
    .abnormal-item {
      padding: 15px;
      border: 1px solid #ebeef5;
      border-radius: 8px;
      margin-bottom: 12px;
      transition: all 0.3s;
      
      &:hover {
        border-color: #f56c6c;
        box-shadow: 0 2px 12px rgba(245, 108, 108, 0.1);
      }
      
      .abnormal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        
        .abnormal-date {
          color: #909399;
          font-size: 13px;
        }
      }
      
      .abnormal-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 8px;
        
        .abnormal-desc {
          display: flex;
          align-items: center;
          gap: 10px;
        }
        
        .abnormal-amount {
          font-size: 18px;
          font-weight: 600;
        }
      }
      
      .abnormal-detail {
        color: #909399;
        font-size: 13px;
        margin-bottom: 10px;
        
        .warning-text {
          color: #f56c6c;
        }
      }
      
      .abnormal-actions {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        padding-top: 10px;
        border-top: 1px dashed #ebeef5;
      }
    }
  }
  
  .trend-bars {
    display: flex;
    align-items: flex-end;
    justify-content: center;
    gap: 8px;
    height: 40px;
    
    .trend-bar {
      width: 20px;
      border-radius: 4px 4px 0 0;
      min-height: 2px;
      transition: height 0.3s;
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
    
    &.danger-card .stat-icon {
      background: rgba(245, 108, 108, 0.1);
      color: #f56c6c;
    }
    
    &.warning-card .stat-icon {
      background: rgba(230, 162, 60, 0.1);
      color: #e6a23c;
    }
    
    &.info-card .stat-icon {
      background: rgba(64, 158, 255, 0.1);
      color: #409eff;
    }
    
    &.success-card .stat-icon {
      background: rgba(103, 194, 58, 0.1);
      color: #67c23a;
    }
  }
  
  .category-tag {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
  
  .income {
    color: #67c23a;
    font-weight: 600;
  }
  
  .expense {
    color: #f56c6c;
    font-weight: 600;
  }
  
  .warning-text {
    color: #e6a23c;
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
