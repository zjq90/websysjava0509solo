<template>
  <div>
    <div class="page-title">👥 用户行为统计</div>

    <div class="card">
      <div class="card-title">📊 活跃用户统计 (近7天)</div>
      <div class="stats-grid" v-if="activeUsersData">
        <div class="stats-item">
          <div class="value">{{ activeUsersData.activeUsers }}</div>
          <div class="label">活跃用户数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ activeUsersData.totalUsers }}</div>
          <div class="label">总用户数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ activeUsersData.activeStatusUsers }}</div>
          <div class="label">活跃状态用户</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ activeUsersData.activeRate?.toFixed(2) }}%</div>
          <div class="label">活跃率</div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-title">🔄 复购率统计</div>
      <div class="stats-grid" v-if="repurchaseData">
        <div class="stats-item">
          <div class="value">{{ repurchaseData.totalBuyers }}</div>
          <div class="label">购买用户总数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ repurchaseData.oneTimeBuyers }}</div>
          <div class="label">单次购买用户</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ repurchaseData.repeatBuyers }}</div>
          <div class="label">复购用户</div>
        </div>
        <div class="stats-item">
          <div class="value" :style="{ color: repurchaseData.repurchaseRate > 30 ? '#67c23a' : '#e6a23c' }">
            {{ repurchaseData.repurchaseRate?.toFixed(2) }}%
          </div>
          <div class="label">复购率</div>
        </div>
      </div>
      <div ref="repurchaseChart" class="chart-container" style="height: 300px;"></div>
    </div>

    <div class="card">
      <div class="card-title">📈 新用户增长趋势 (近30天)</div>
      <div ref="growthChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">🕐 购买时段分布热力图</div>
      <div class="stats-grid" v-if="heatmapData">
        <div class="stats-item">
          <div class="value">{{ heatmapData.totalOrders }}</div>
          <div class="label">总订单数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ heatmapData.peakHour }}时</div>
          <div class="label">高峰时段</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ heatmapData.peakOrders }}</div>
          <div class="label">高峰订单数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ heatmapData.days }}天</div>
          <div class="label">统计周期</div>
        </div>
      </div>
      <div ref="heatmapChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">📋 用户列表</div>
      <el-table :data="users" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="purchaseCount" label="购买次数" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? '活跃' : '不活跃' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import api from '../api'

export default {
  name: 'Users',
  setup() {
    const users = ref([])
    const activeUsersData = ref(null)
    const repurchaseData = ref(null)
    const heatmapData = ref(null)
    const repurchaseChart = ref(null)
    const growthChart = ref(null)
    const heatmapChart = ref(null)
    let repurchaseChartInstance = null
    let growthChartInstance = null
    let heatmapChartInstance = null

    const loadData = async () => {
      try {
        const [usersRes, activeRes, repurchaseRes, growthRes, heatmapRes] = await Promise.all([
          api.getUsers(0, 50),
          api.getActiveUsersStatistics(7),
          api.getRepurchaseRateStatistics(),
          api.getNewUserGrowthTrend(),
          api.getPurchaseTimeHeatmap(7)
        ])

        users.value = usersRes.data.content || []
        activeUsersData.value = activeRes.data
        repurchaseData.value = repurchaseRes.data
        heatmapData.value = heatmapRes.data

        renderRepurchaseChart(repurchaseRes.data)
        renderGrowthChart(growthRes.data)
        renderHeatmapChart(heatmapRes.data)
      } catch (error) {
        console.error('Failed to load users data:', error)
      }
    }

    const renderRepurchaseChart = (data) => {
      if (!repurchaseChart.value) return
      if (!repurchaseChartInstance) {
        repurchaseChartInstance = echarts.init(repurchaseChart.value)
      }

      const option = {
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', right: '5%', top: 'center' },
        series: [{
          name: '购买用户',
          type: 'pie',
          radius: '60%',
          data: [
            { value: data.oneTimeBuyers, name: '单次购买', itemStyle: { color: '#E6A23C' } },
            { value: data.repeatBuyers, name: '复购用户', itemStyle: { color: '#67C23A' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      repurchaseChartInstance.setOption(option)
    }

    const renderGrowthChart = (chartData) => {
      if (!growthChart.value) return
      if (!growthChartInstance) {
        growthChartInstance = echarts.init(growthChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: chartData.map(item => item.date.slice(5)),
          axisLabel: { rotate: 45 }
        },
        yAxis: { type: 'value' },
        series: [{
          name: '新用户数',
          type: 'line',
          smooth: true,
          areaStyle: {},
          data: chartData.map(item => item.newUsers),
          itemStyle: { color: '#409EFF' }
        }]
      }
      growthChartInstance.setOption(option)
    }

    const renderHeatmapChart = (chartData) => {
      if (!heatmapChart.value || !chartData.hourlyData) return
      if (!heatmapChartInstance) {
        heatmapChartInstance = echarts.init(heatmapChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: chartData.hourlyData.map(item => item.hour + '时')
        },
        yAxis: { type: 'value' },
        visualMap: {
          min: 0,
          max: Math.max(...chartData.hourlyData.map(item => item.orderCount)),
          inRange: {
            color: ['#50a3ba', '#eac763', '#d94e5d']
          },
          show: false
        },
        series: [{
          name: '订单数',
          type: 'bar',
          data: chartData.hourlyData.map(item => item.orderCount),
          itemStyle: {
            color: function(params) {
              const max = Math.max(...chartData.hourlyData.map(item => item.orderCount))
              const value = params.value
              const ratio = value / max
              if (ratio > 0.7) return '#d94e5d'
              if (ratio > 0.4) return '#eac763'
              return '#50a3ba'
            }
          }
        }]
      }
      heatmapChartInstance.setOption(option)
    }

    const handleResize = () => {
      repurchaseChartInstance?.resize()
      growthChartInstance?.resize()
      heatmapChartInstance?.resize()
    }

    onMounted(() => {
      loadData()
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      repurchaseChartInstance?.dispose()
      growthChartInstance?.dispose()
      heatmapChartInstance?.dispose()
      window.removeEventListener('resize', handleResize)
    })

    return {
      users,
      activeUsersData,
      repurchaseData,
      heatmapData,
      repurchaseChart,
      growthChart,
      heatmapChart
    }
  }
}
</script>
