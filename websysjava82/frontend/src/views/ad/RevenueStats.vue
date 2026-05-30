<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">收益统计</h2>
      <el-button type="primary" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出报表
      </el-button>
    </div>

    <div class="filter-bar">
      <el-form :inline="true" :model="filter">
        <el-form-item label="广告位">
          <el-select v-model="filter.adSlotId" placeholder="全部广告位" clearable style="width: 180px">
            <el-option v-for="slot in adSlots" :key="slot.id" :label="slot.name" :value="slot.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filter.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 280px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">总曝光量</div>
          <div class="stat-value">{{ summary.totalImpressions?.toLocaleString() || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">总点击量</div>
          <div class="stat-value">{{ summary.totalClicks?.toLocaleString() || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-label">点击率(CTR)</div>
          <div class="stat-value">{{ summary.ctr?.toFixed(2) || 0 }}%</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card purple">
          <div class="stat-label">总收入(元)</div>
          <div class="stat-value">¥{{ summary.totalRevenue?.toLocaleString() || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="16">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 20px">收益趋势图</h3>
          <div ref="chartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 20px">广告位收入占比</h3>
          <div ref="pieChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <div class="card-wrapper">
      <h3 style="margin-bottom: 20px">明细数据</h3>
      <el-table :data="stats" style="width: 100%" border>
        <el-table-column prop="statDate" label="日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.statDate) }}
          </template>
        </el-table-column>
        <el-table-column label="广告位" width="150">
          <template #default="{ row }">
            {{ getSlotName(row.adSlotId) }}
          </template>
        </el-table-column>
        <el-table-column prop="impressions" label="曝光量" sortable>
          <template #default="{ row }">
            {{ row.impressions?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="clicks" label="点击量" sortable>
          <template #default="{ row }">
            {{ row.clicks?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="点击率(%)" width="120" sortable>
          <template #default="{ row }">
            {{ row.ctr?.toFixed(2) || 0 }}%
          </template>
        </el-table-column>
        <el-table-column label="收入(元)" width="120" sortable>
          <template #default="{ row }">
            ¥{{ row.revenue?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="eCPM(元)" width="120" sortable>
          <template #default="{ row }">
            ¥{{ row.ecpm || 0 }}
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import dayjs from 'dayjs'
import { revenueStatsApi, adSlotApi } from '../../api'

const chartRef = ref(null)
const pieChartRef = ref(null)
const stats = ref([])
const adSlots = ref([])
const summary = ref({})

const filter = reactive({
  adSlotId: null,
  dateRange: [dayjs().subtract(13, 'day').toDate(), dayjs().toDate()]
})

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const getSlotName = (id) => {
  const slot = adSlots.value.find(s => s.id === id)
  return slot ? slot.name : '-'
}

const loadData = async () => {
  const [slotRes] = await Promise.all([adSlotApi.list()])
  adSlots.value = slotRes.data || []

  const startDate = formatDate(filter.dateRange[0])
  const endDate = formatDate(filter.dateRange[1])

  const params = { startDate, endDate }
  if (filter.adSlotId) {
    params.adSlotId = filter.adSlotId
  }

  const res = await revenueStatsApi.summary(params)
  summary.value = res.data || {}
  stats.value = res.data?.stats || []

  await nextTick()
  initCharts()
}

const initCharts = () => {
  if (chartRef.value) {
    const chart = echarts.init(chartRef.value)
    const dates = [...new Set(stats.value.map(s => s.statDate))].sort()

    const seriesData = []
    const colors = ['#667eea', '#f5576c', '#4facfe', '#43e97b']
    
    const slotIds = [...new Set(stats.value.map(s => s.adSlotId))]
    slotIds.forEach((slotId, index) => {
      const slotStats = stats.value.filter(s => s.adSlotId === slotId)
      const dataMap = {}
      slotStats.forEach(s => { dataMap[s.statDate] = s.revenue })
      seriesData.push({
        name: getSlotName(slotId),
        type: 'line',
        smooth: true,
        data: dates.map(d => dataMap[d] || 0),
        itemStyle: { color: colors[index % colors.length] }
      })
    })

    chart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: slotIds.map(id => getSlotName(id)) },
      xAxis: {
        type: 'category',
        data: dates.map(d => dayjs(d).format('MM-DD'))
      },
      yAxis: { type: 'value', name: '收入(元)' },
      series: seriesData
    })
  }

  if (pieChartRef.value) {
    const pieChart = echarts.init(pieChartRef.value)
    const slotTotals = {}
    stats.value.forEach(s => {
      if (!slotTotals[s.adSlotId]) slotTotals[s.adSlotId] = 0
      slotTotals[s.adSlotId] += parseFloat(s.revenue)
    })

    pieChart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: Object.keys(slotTotals).map(id => ({
          name: getSlotName(id),
          value: slotTotals[id].toFixed(2)
        }))
      }]
    })
  }
}

const resetFilter = () => {
  filter.adSlotId = null
  filter.dateRange = [dayjs().subtract(13, 'day').toDate(), dayjs().toDate()]
  loadData()
}

const handleExport = async () => {
  const startDate = formatDate(filter.dateRange[0])
  const endDate = formatDate(filter.dateRange[1])

  const params = { startDate, endDate }
  if (filter.adSlotId) {
    params.adSlotId = filter.adSlotId
  }

  const res = await revenueStatsApi.export(params)
  const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = `收益统计_${startDate}_${endDate}.xlsx`
  link.click()
  URL.revokeObjectURL(url)
  ElMessage.success('导出成功')
}

onMounted(loadData)
</script>
