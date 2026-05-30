<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">数据概览</h2>
    </div>

    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-label">今日曝光量</div>
          <div class="stat-value">{{ summary.totalImpressions?.toLocaleString() || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-label">今日点击量</div>
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
          <div class="stat-label">今日收入(元)</div>
          <div class="stat-value">¥{{ summary.totalRevenue || 0 }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 20px">近14天收益趋势</h3>
          <div ref="chartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 20px">广告位分布</h3>
          <div ref="pieChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <div class="card-wrapper">
          <div class="page-header" style="margin-bottom: 10px">
            <h3>最近推送任务</h3>
          </div>
          <el-table :data="recentTasks" style="width: 100%">
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="targetType" label="目标用户">
              <template #default="{ row }">
                <el-tag :type="getTargetTypeTag(row.targetType)">
                  {{ getTargetTypeName(row.targetType) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusTag(row.status)">
                  {{ getStatusName(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="deliveredCount" label="送达量" />
            <el-table-column prop="openedCount" label="打开量" />
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card-wrapper">
          <div class="page-header" style="margin-bottom: 10px">
            <h3>活跃公告</h3>
          </div>
          <el-table :data="activeAnnouncements" style="width: 100%">
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="displayTrigger" label="展示时机">
              <template #default="{ row }">
                {{ getTriggerName(row.displayTrigger) }}
              </template>
            </el-table-column>
            <el-table-column prop="viewCount" label="浏览量" />
            <el-table-column prop="clickCount" label="点击量" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import dayjs from 'dayjs'
import { revenueStatsApi, pushTaskApi, popupAnnouncementApi } from '../api'

const chartRef = ref(null)
const pieChartRef = ref(null)
const summary = ref({})
const recentTasks = ref([])
const activeAnnouncements = ref([])

const getTargetTypeName = (type) => {
  const map = { ALL: '全体用户', MEMBER_LEVEL: '会员等级', ACTIVE: '活跃用户' }
  return map[type] || type
}

const getTargetTypeTag = (type) => {
  const map = { ALL: '', MEMBER_LEVEL: 'warning', ACTIVE: 'success' }
  return map[type] || ''
}

const getStatusName = (status) => {
  const map = { DRAFT: '草稿', PENDING: '待发送', SENDING: '发送中', SENT: '已发送' }
  return map[status] || status
}

const getStatusTag = (status) => {
  const map = { DRAFT: 'info', PENDING: 'warning', SENDING: 'primary', SENT: 'success' }
  return map[status] || ''
}

const getTriggerName = (trigger) => {
  const map = { LOGIN: '登录后', HOME: '打开首页时' }
  return map[trigger] || trigger
}

const loadData = async () => {
  const today = dayjs()
  const startDate = today.subtract(13, 'day').format('YYYY-MM-DD')
  const endDate = today.format('YYYY-MM-DD')

  const [summaryRes, tasksRes, announcementsRes] = await Promise.all([
    revenueStatsApi.summary({ startDate, endDate }),
    pushTaskApi.list(),
    popupAnnouncementApi.listActive()
  ])

  summary.value = summaryRes.data || {}
  recentTasks.value = (tasksRes.data || []).slice(0, 5)
  activeAnnouncements.value = announcementsRes.data || []

  initCharts(summaryRes.data?.stats || [])
}

const initCharts = (stats) => {
  const chart = echarts.init(chartRef.value)
  const dates = [...new Set(stats.map(s => s.statDate))].sort()
  
  const seriesData = []
  const slotNames = { 1: '加载页', 2: '首页Banner', 3: '弹窗', 4: '激励视频' }
  const colors = ['#667eea', '#f5576c', '#4facfe', '#43e97b']
  
  ;[1, 2, 3, 4].forEach(slotId => {
    const slotStats = stats.filter(s => s.adSlotId === slotId)
    const dataMap = {}
    slotStats.forEach(s => { dataMap[s.statDate] = s.revenue })
    seriesData.push({
      name: slotNames[slotId],
      type: 'line',
      smooth: true,
      data: dates.map(d => dataMap[d] || 0),
      itemStyle: { color: colors[slotId - 1] }
    })
  })

  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: Object.values(slotNames) },
    xAxis: {
      type: 'category',
      data: dates.map(d => dayjs(d).format('MM-DD'))
    },
    yAxis: { type: 'value', name: '收入(元)' },
    series: seriesData
  })

  const pieChart = echarts.init(pieChartRef.value)
  const slotTotals = {}
  stats.forEach(s => {
    if (!slotTotals[s.adSlotId]) slotTotals[s.adSlotId] = 0
    slotTotals[s.adSlotId] += s.impressions
  })

  pieChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: Object.keys(slotTotals).map(id => ({
        name: slotNames[id],
        value: slotTotals[id]
      }))
    }]
  })
}

onMounted(loadData)
</script>

<style scoped>
.stat-row {
  margin-bottom: 20px;
}
</style>
