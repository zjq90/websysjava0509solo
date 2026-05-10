<template>
  <div class="dashboard">
    <div class="dashboard-title">📊 Web平台管理系统 - 数据大屏</div>
    
    <div class="dashboard-row">
      <div class="dashboard-card">
        <h3>💰 销售概况</h3>
        <div class="stat-item">
          <div class="stat-value">¥{{ formatNumber(data.sales?.totalRevenue || 0) }}</div>
          <div class="stat-label">总销售额</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ formatNumber(data.sales?.totalOrders || 0) }}</div>
          <div class="stat-label">总订单数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">¥{{ formatNumber(data.sales?.todayRevenue || 0) }}</div>
          <div class="stat-label">今日销售额</div>
          <div :class="['stat-growth', data.sales?.revenueGrowth > 0 ? 'positive' : 'negative']">
            {{ data.sales?.revenueGrowth > 0 ? '+' : '' }}{{ data.sales?.revenueGrowth || 0 }}%
          </div>
        </div>
      </div>

      <div class="dashboard-card">
        <h3>📦 商品概况</h3>
        <div class="stat-item">
          <div class="stat-value">{{ data.products?.total || 0 }}</div>
          <div class="stat-label">商品总数</div>
        </div>
      </div>

      <div class="dashboard-card">
        <h3>⚙️ 设备概况</h3>
        <div class="stat-item">
          <div class="stat-value">{{ data.devices?.total || 0 }}</div>
          <div class="stat-label">设备总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ data.devices?.running || 0 }}</div>
          <div class="stat-label">运行中</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ data.devices?.utilizationRate || 0 }}%</div>
          <div class="stat-label">利用率</div>
        </div>
      </div>

      <div class="dashboard-card">
        <h3>👥 用户概况</h3>
        <div class="stat-item">
          <div class="stat-value">{{ data.users?.total || 0 }}</div>
          <div class="stat-label">用户总数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ data.users?.active7days || 0 }}</div>
          <div class="stat-label">7日活跃</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ data.users?.newToday || 0 }}</div>
          <div class="stat-label">今日新增</div>
        </div>
      </div>
    </div>

    <div class="dashboard-row">
      <div class="dashboard-card" style="flex: 2;">
        <h3>📈 热销商品TOP5</h3>
        <div ref="hotChart" class="chart-container" style="height: 350px;"></div>
      </div>
      <div class="dashboard-card" style="flex: 1;">
        <h3>⚙️ 设备状态</h3>
        <div ref="deviceChart" class="chart-container" style="height: 350px;"></div>
      </div>
    </div>

    <div class="dashboard-row">
      <div class="dashboard-card" style="flex: 1;">
        <h3>👥 用户增长趋势</h3>
        <div ref="userChart" class="chart-container" style="height: 350px;"></div>
      </div>
      <div class="dashboard-card" style="flex: 1;">
        <h3>🕐 购买时段分布</h3>
        <div ref="timeChart" class="chart-container" style="height: 350px;"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import api from '../api'

export default {
  name: 'Dashboard',
  setup() {
    const data = ref({})
    const hotChart = ref(null)
    const deviceChart = ref(null)
    const userChart = ref(null)
    const timeChart = ref(null)
    let hotChartInstance = null
    let deviceChartInstance = null
    let userChartInstance = null
    let timeChartInstance = null
    let timer = null

    const formatNumber = (num) => {
      if (typeof num === 'string') return num
      return num.toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    }

    const loadData = async () => {
      try {
        const res = await api.getDashboardData()
        data.value = res.data
        loadChartData()
      } catch (error) {
        console.error('Failed to load dashboard data:', error)
      }
    }

    const loadChartData = async () => {
      try {
        const [hotRes, userRes, timeRes] = await Promise.all([
          api.getHotSellingProducts(5),
          api.getNewUserGrowthTrend(),
          api.getPurchaseTimeHeatmap(7)
        ])

        renderHotChart(hotRes.data)
        renderDeviceChart()
        renderUserChart(userRes.data)
        renderTimeChart(timeRes.data)
      } catch (error) {
        console.error('Failed to load chart data:', error)
      }
    }

    const renderHotChart = (chartData) => {
      if (!hotChart.value) return
      if (!hotChartInstance) {
        hotChartInstance = echarts.init(hotChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: chartData.map(item => item.name),
          axisLabel: { color: '#aaa' }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#aaa' }
        },
        series: [{
          name: '销售数量',
          type: 'bar',
          data: chartData.map(item => item.salesQuantity),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#00d4ff' },
              { offset: 1, color: '#0066ff' }
            ])
          }
        }]
      }
      hotChartInstance.setOption(option)
    }

    const renderDeviceChart = () => {
      if (!deviceChart.value) return
      if (!deviceChartInstance) {
        deviceChartInstance = echarts.init(deviceChart.value)
      }

      const devices = data.value.devices || {}
      const option = {
        tooltip: { trigger: 'item' },
        legend: {
          top: '5%',
          textStyle: { color: '#aaa' }
        },
        series: [{
          name: '设备状态',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#0f1e3d',
            borderWidth: 2
          },
          label: { show: false },
          emphasis: {
            label: { show: true, fontSize: 16, fontWeight: 'bold' }
          },
          data: [
            { value: devices.running || 0, name: '运行中', itemStyle: { color: '#00ff88' } },
            { value: (devices.total || 0) - (devices.running || 0), name: '其他', itemStyle: { color: '#666' } }
          ]
        }]
      }
      deviceChartInstance.setOption(option)
    }

    const renderUserChart = (chartData) => {
      if (!userChart.value) return
      if (!userChartInstance) {
        userChartInstance = echarts.init(userChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: chartData.map(item => item.date.slice(5)),
          axisLabel: { color: '#aaa' }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#aaa' }
        },
        series: [{
          name: '新用户',
          type: 'line',
          smooth: true,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(0, 212, 255, 0.3)' },
              { offset: 1, color: 'rgba(0, 212, 255, 0.05)' }
            ])
          },
          lineStyle: { color: '#00d4ff' },
          itemStyle: { color: '#00d4ff' },
          data: chartData.map(item => item.newUsers)
        }]
      }
      userChartInstance.setOption(option)
    }

    const renderTimeChart = (chartData) => {
      if (!timeChart.value) return
      if (!timeChartInstance) {
        timeChartInstance = echarts.init(timeChart.value)
      }

      const hourlyData = chartData.hourlyData || []
      const option = {
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: hourlyData.map(item => item.hour + '时'),
          axisLabel: { color: '#aaa' }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#aaa' }
        },
        series: [{
          name: '订单数',
          type: 'line',
          smooth: true,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(255, 140, 0, 0.3)' },
              { offset: 1, color: 'rgba(255, 140, 0, 0.05)' }
            ])
          },
          lineStyle: { color: '#ff8c00' },
          itemStyle: { color: '#ff8c00' },
          data: hourlyData.map(item => item.orderCount)
        }]
      }
      timeChartInstance.setOption(option)
    }

    const handleResize = () => {
      hotChartInstance?.resize()
      deviceChartInstance?.resize()
      userChartInstance?.resize()
      timeChartInstance?.resize()
    }

    onMounted(() => {
      loadData()
      timer = setInterval(loadData, 30000)
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      if (timer) clearInterval(timer)
      hotChartInstance?.dispose()
      deviceChartInstance?.dispose()
      userChartInstance?.dispose()
      timeChartInstance?.dispose()
      window.removeEventListener('resize', handleResize)
    })

    return {
      data,
      hotChart,
      deviceChart,
      userChart,
      timeChart,
      formatNumber
    }
  }
}
</script>
