<template>
  <!--
    仪表盘页面
    包含今日/本周/本月收支统计、支出占比环形图、账户余额趋势折线图
  -->
  <div class="dashboard">
    <!-- 收支统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <div class="stat-card income-card">
          <div class="stat-icon">
            <el-icon :size="32"><ArrowUp /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日收入</div>
            <div class="stat-value income">¥{{ formatMoney(overview.todayIncome) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card expense-card">
          <div class="stat-icon">
            <el-icon :size="32"><ArrowDown /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">今日支出</div>
            <div class="stat-value expense">¥{{ formatMoney(overview.todayExpense) }}</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card week-card">
          <div class="stat-icon">
            <el-icon :size="32"><Calendar /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">本月结余</div>
            <div class="stat-value" :class="overview.monthIncome - overview.monthExpense >= 0 ? 'income' : 'expense'">
              ¥{{ formatMoney(overview.monthIncome - overview.monthExpense) }}
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card total-card">
          <div class="stat-icon">
            <el-icon :size="32"><Wallet /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">总资产</div>
            <div class="stat-value">¥{{ formatMoney(overview.totalAssets) }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 本周/本月快捷切换 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">收支概览</span>
            <el-radio-group v-model="period" size="small">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
              <el-radio-button label="year">本年</el-radio-button>
            </el-radio-group>
          </div>
          <div class="period-stats">
            <div class="period-item">
              <span class="period-label">收入</span>
              <span class="period-value income">
                ¥{{ formatMoney(period === 'week' ? overview.weekIncome : overview.monthIncome) }}
              </span>
            </div>
            <div class="period-item">
              <span class="period-label">支出</span>
              <span class="period-value expense">
                ¥{{ formatMoney(period === 'week' ? overview.weekExpense : overview.monthExpense) }}
              </span>
            </div>
            <div class="period-item">
              <span class="period-label">结余</span>
              <span class="period-value" :class="getNet() >= 0 ? 'income' : 'expense'">
                ¥{{ formatMoney(getNet()) }}
              </span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">账户余额趋势</span>
            <span class="text-muted">最近30天</span>
          </div>
          <v-chart class="chart-container" :option="balanceTrendOption" autoresize />
        </div>
      </el-col>
    </el-row>

    <!-- 支出占比和月度趋势 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <div class="card">
          <div class="card-header">
            <span class="card-title">支出分类占比</span>
            <el-button type="primary" link size="small" @click="$router.push('/budget')">查看预算</el-button>
          </div>
          <v-chart class="chart-container-lg" :option="pieOption" autoresize />
        </div>
      </el-col>
      <el-col :span="16">
        <div class="card">
          <div class="card-header">
            <span class="card-title">月度收支趋势</span>
            <span class="text-muted">最近12个月</span>
          </div>
          <v-chart class="chart-container-lg" :option="trendOption" autoresize />
        </div>
      </el-col>
    </el-row>

    <!-- 自定义看板区域（可拖拽） -->
    <div class="card mb-20">
      <div class="card-header">
        <span class="card-title">自定义看板</span>
        <div class="header-actions">
          <el-button size="small" @click="saveTemplate">
            <el-icon><Save /></el-icon>
            保存模板
          </el-button>
          <el-button size="small" @click="loadTemplate">
            <el-icon><FolderOpened /></el-icon>
            加载模板
          </el-button>
        </div>
      </div>
      <div ref="widgetContainer" class="widget-grid">
        <div
          v-for="(widget, index) in widgets"
          :key="widget.id"
          class="widget-item draggable"
          :data-index="index"
        >
          <div class="widget-header">
            <span class="widget-title">{{ widget.title }}</span>
            <el-dropdown @command="(cmd) => handleWidgetCommand(cmd, widget)">
              <el-button type="primary" text size="small">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="replace">替换图表</el-dropdown-item>
                  <el-dropdown-item command="remove">移除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div class="widget-content">
            <component :is="widget.component" :data="widget.data" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { PieChart, LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import { ElMessage, ElMessageBox } from 'element-plus'
import Sortable from 'sortablejs'
import dayjs from 'dayjs'
import { dashboardApi } from '@/api'

use([
  CanvasRenderer,
  PieChart,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent
])

const overview = ref({
  todayIncome: 0,
  todayExpense: 0,
  weekIncome: 0,
  weekExpense: 0,
  monthIncome: 0,
  monthExpense: 0,
  totalAssets: 0,
  categoryExpenseRatio: [],
  balanceTrend: []
})

const monthlyTrend = ref([])
const period = ref('month')
const widgetContainer = ref(null)

const widgets = ref([
  { id: 1, title: '最近支出', component: 'RecentExpenses', data: null },
  { id: 2, title: '分类排行', component: 'CategoryRanking', data: null },
  { id: 3, title: '异常提醒', component: 'AbnormalAlert', data: null }
])

const formatMoney = (value) => {
  if (value === null || value === undefined) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getNet = () => {
  if (period.value === 'week') {
    return overview.value.weekIncome - overview.value.weekExpense
  }
  return overview.value.monthIncome - overview.value.monthExpense
}

// 支出占比环形图配置
const pieOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: ¥{c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: 10,
    top: 'center',
    formatter: (name) => {
      const item = overview.value.categoryExpenseRatio.find(i => i.categoryName === name)
      return `${name}  ${item ? item.ratio.toFixed(1) : 0}%`
    }
  },
  series: [{
    type: 'pie',
    radius: ['45%', '75%'],
    center: ['35%', '50%'],
    avoidLabelOverlap: false,
    itemStyle: {
      borderRadius: 8,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: {
      show: false
    },
    emphasis: {
      label: {
        show: true,
        fontSize: 14,
        fontWeight: 'bold'
      }
    },
    data: overview.value.categoryExpenseRatio.map(item => ({
      value: item.amount,
      name: item.categoryName,
      itemStyle: { color: item.color }
    }))
  }]
}))

// 余额趋势折线图配置
const balanceTrendOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: (params) => {
      const data = params[0]
      return `${data.name}<br/>余额: ¥${formatMoney(data.value)}`
    }
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
    data: overview.value.balanceTrend.map(item => dayjs(item.date).format('MM-DD'))
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value) => `¥${(value / 1000).toFixed(0)}k`
    }
  },
  series: [{
    type: 'line',
    smooth: true,
    symbol: 'none',
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
    },
    lineStyle: {
      color: '#409eff',
      width: 2
    },
    data: overview.value.balanceTrend.map(item => item.balance)
  }]
}))

// 月度收支趋势图配置
const trendOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  legend: {
    data: ['收入', '支出', '净收入'],
    top: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '15%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: monthlyTrend.value.map(item => `${item.year}-${String(item.month).padStart(2, '0')}`)
  },
  yAxis: {
    type: 'value',
    axisLabel: {
      formatter: (value) => `¥${(value / 1000).toFixed(0)}k`
    }
  },
  series: [
    {
      name: '收入',
      type: 'bar',
      data: monthlyTrend.value.map(item => item.income),
      itemStyle: { color: '#67c23a' }
    },
    {
      name: '支出',
      type: 'bar',
      data: monthlyTrend.value.map(item => item.expense),
      itemStyle: { color: '#f56c6c' }
    },
    {
      name: '净收入',
      type: 'line',
      smooth: true,
      data: monthlyTrend.value.map(item => item.netIncome),
      itemStyle: { color: '#409eff' },
      lineStyle: { width: 3 }
    }
  ]
}))

const loadData = async () => {
  try {
    const [overviewData, trendData] = await Promise.all([
      dashboardApi.getOverview(),
      dashboardApi.getMonthlyTrend(12)
    ])
    overview.value = overviewData
    monthlyTrend.value = trendData
  } catch (error) {
    console.error('加载仪表盘数据失败:', error)
  }
}

const initSortable = () => {
  if (widgetContainer.value) {
    Sortable.create(widgetContainer.value, {
      animation: 150,
      handle: '.widget-header',
      ghostClass: 'sortable-ghost',
      onEnd: (evt) => {
        const newIndex = evt.newIndex
        const oldIndex = evt.oldIndex
        const item = widgets.value.splice(oldIndex, 1)[0]
        widgets.value.splice(newIndex, 0, item)
      }
    })
  }
}

const handleWidgetCommand = (command, widget) => {
  if (command === 'remove') {
    ElMessageBox.confirm('确定要移除这个组件吗？', '提示', {
      type: 'warning'
    }).then(() => {
      const index = widgets.value.findIndex(w => w.id === widget.id)
      if (index > -1) {
        widgets.value.splice(index, 1)
        ElMessage.success('已移除')
      }
    }).catch(() => {})
  } else if (command === 'replace') {
    ElMessage.info('替换图表功能开发中...')
  }
}

const saveTemplate = () => {
  ElMessageBox.prompt('请输入模板名称', '保存看板模板', {
    confirmButtonText: '保存',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '模板名称不能为空'
  }).then(({ value }) => {
    ElMessage.success(`模板「${value}」保存成功`)
  }).catch(() => {})
}

const loadTemplate = () => {
  ElMessage.info('加载模板功能开发中...')
}

// 自定义子组件
const RecentExpenses = {
  props: ['data'],
  template: `
    <div class="recent-expenses">
      <div v-for="i in 5" :key="i" class="expense-item">
        <span class="expense-desc">餐饮支出</span>
        <span class="expense-amount expense">-¥{{ (Math.random() * 100 + 20).toFixed(2) }}</span>
      </div>
    </div>
  `
}

const CategoryRanking = {
  props: ['data'],
  template: `
    <div class="category-ranking">
      <div v-for="(item, index) in ['餐饮', '购物', '交通', '娱乐', '居住']" :key="item" class="rank-item">
        <span class="rank-no">{{ index + 1 }}</span>
        <span class="rank-name">{{ item }}</span>
        <div class="rank-bar">
          <div class="rank-bar-fill" :style="{ width: (100 - index * 15) + '%' }"></div>
        </div>
      </div>
    </div>
  `
}

const AbnormalAlert = {
  props: ['data'],
  template: `
    <div class="abnormal-alert">
      <el-alert
        v-for="i in 2"
        :key="i"
        title="检测到大额支出"
        :description="'单笔支出¥' + (Math.random() * 3000 + 2000).toFixed(2) + '，超过月均3倍'"
        type="warning"
        :closable="false"
        show-icon
        class="mb-10"
      />
    </div>
  `
}

onMounted(() => {
  loadData()
  initSortable()
})

watch(period, () => {
  // 切换时间周期时可以重新加载数据
})
</script>

<style scoped lang="scss">
.dashboard {
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
    
    &.income-card .stat-icon {
      background: rgba(103, 194, 58, 0.1);
      color: #67c23a;
    }
    
    &.expense-card .stat-icon {
      background: rgba(245, 108, 108, 0.1);
      color: #f56c6c;
    }
    
    &.week-card .stat-icon {
      background: rgba(64, 158, 255, 0.1);
      color: #409eff;
    }
    
    &.total-card .stat-icon {
      background: rgba(230, 162, 60, 0.1);
      color: #e6a23c;
    }
  }
  
  .period-stats {
    display: flex;
    justify-content: space-around;
    padding: 20px 0;
    
    .period-item {
      text-align: center;
      
      .period-label {
        display: block;
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }
      
      .period-value {
        font-size: 22px;
        font-weight: 600;
      }
    }
  }
  
  .widget-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    
    .widget-item {
      background: #f8f9fa;
      border-radius: 8px;
      border: 1px solid #ebeef5;
      overflow: hidden;
      
      .widget-header {
        padding: 12px 15px;
        background: #fff;
        border-bottom: 1px solid #ebeef5;
        display: flex;
        justify-content: space-between;
        align-items: center;
        cursor: move;
        
        .widget-title {
          font-weight: 600;
          font-size: 14px;
        }
      }
      
      .widget-content {
        padding: 15px;
        min-height: 150px;
      }
    }
  }
  
  .recent-expenses {
    .expense-item {
      display: flex;
      justify-content: space-between;
      padding: 8px 0;
      border-bottom: 1px dashed #ebeef5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .expense-desc {
        color: #606266;
      }
      
      .expense-amount {
        font-weight: 500;
      }
    }
  }
  
  .category-ranking {
    .rank-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 8px 0;
      
      .rank-no {
        width: 22px;
        height: 22px;
        border-radius: 50%;
        background: #409eff;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        font-weight: 600;
      }
      
      .rank-name {
        width: 60px;
        font-size: 13px;
      }
      
      .rank-bar {
        flex: 1;
        height: 8px;
        background: #ebeef5;
        border-radius: 4px;
        overflow: hidden;
        
        .rank-bar-fill {
          height: 100%;
          background: linear-gradient(90deg, #409eff, #67c23a);
          border-radius: 4px;
        }
      }
    }
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
