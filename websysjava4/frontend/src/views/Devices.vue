<template>
  <div>
    <div class="page-title">⚙️ 设备效能统计</div>

    <div class="card">
      <div class="card-title">📈 单机产出排行 TOP10</div>
      <div ref="outputChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">⚠️ 故障率统计</div>
      <div class="stats-grid" v-if="faultRateData">
        <div class="stats-item">
          <div class="value">{{ faultRateData.totalDevices }}</div>
          <div class="label">设备总数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ faultRateData.runningDevices }}</div>
          <div class="label">运行中</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ faultRateData.totalFaults }}</div>
          <div class="label">故障总数</div>
        </div>
        <div class="stats-item">
          <div class="value">{{ faultRateData.averageFaultsPerDevice }}次</div>
          <div class="label">平均每台故障</div>
        </div>
      </div>
      <el-table :data="faultRateData?.deviceFaults || []" border stripe>
        <el-table-column prop="deviceCode" label="设备编号" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'running' ? 'success' : 'warning'">
              {{ scope.row.status === 'running' ? '运行中' : '维护中' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="faultCount" label="故障次数" />
        <el-table-column prop="totalRuntime" label="运行时长(小时)" />
        <el-table-column prop="faultRatePer100Hours" label="每百小时故障率">
          <template #default="scope">
            {{ scope.row.faultRatePer100Hours }}%
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <div class="card-title">💰 运维成本分析</div>
      <div ref="costChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">📋 设备列表</div>
      <el-table :data="devices" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="deviceCode" label="设备编号" />
        <el-table-column prop="name" label="设备名称" />
        <el-table-column prop="type" label="类型" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="totalOutput" label="总产出" />
        <el-table-column prop="faultCount" label="故障数" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
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
  name: 'Devices',
  setup() {
    const devices = ref([])
    const faultRateData = ref(null)
    const maintenanceData = ref(null)
    const outputChart = ref(null)
    const costChart = ref(null)
    let outputChartInstance = null
    let costChartInstance = null

    const getStatusType = (status) => {
      const types = { running: 'success', maintenance: 'warning', stopped: 'info' }
      return types[status] || 'info'
    }

    const getStatusText = (status) => {
      const texts = { running: '运行中', maintenance: '维护中', stopped: '停止' }
      return texts[status] || status
    }

    const loadData = async () => {
      try {
        const [devicesRes, outputRes, faultRes, maintenanceRes] = await Promise.all([
          api.getDevices(0, 20),
          api.getSingleMachineOutput(10),
          api.getFaultRateStatistics(),
          api.getMaintenanceCostAnalysis()
        ])

        devices.value = devicesRes.data.content || []
        faultRateData.value = faultRes.data
        maintenanceData.value = maintenanceRes.data

        renderOutputChart(outputRes.data)
        renderCostChart(maintenanceRes.data)
      } catch (error) {
        console.error('Failed to load devices data:', error)
      }
    }

    const renderOutputChart = (chartData) => {
      if (!outputChart.value) return
      if (!outputChartInstance) {
        outputChartInstance = echarts.init(outputChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['总产出', '每小时产出'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: chartData.map(item => item.name)
        },
        yAxis: [
          { type: 'value', name: '总产出' },
          { type: 'value', name: '每小时产出' }
        ],
        series: [
          {
            name: '总产出',
            type: 'bar',
            data: chartData.map(item => item.totalOutput),
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '每小时产出',
            type: 'line',
            yAxisIndex: 1,
            data: chartData.map(item => item.outputPerHour),
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      outputChartInstance.setOption(option)
    }

    const renderCostChart = (data) => {
      if (!costChart.value || !data.deviceCosts) return
      if (!costChartInstance) {
        costChartInstance = echarts.init(costChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['运维成本', '单位产出成本'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: data.deviceCosts.slice(0, 10).map(item => item.name)
        },
        yAxis: [
          { type: 'value', name: '运维成本' },
          { type: 'value', name: '单位产出成本' }
        ],
        series: [
          {
            name: '运维成本',
            type: 'bar',
            data: data.deviceCosts.slice(0, 10).map(item => item.maintenanceCost),
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: '单位产出成本',
            type: 'line',
            yAxisIndex: 1,
            data: data.deviceCosts.slice(0, 10).map(item => item.costPerUnitOutput),
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      costChartInstance.setOption(option)
    }

    const handleResize = () => {
      outputChartInstance?.resize()
      costChartInstance?.resize()
    }

    onMounted(() => {
      loadData()
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      outputChartInstance?.dispose()
      costChartInstance?.dispose()
      window.removeEventListener('resize', handleResize)
    })

    return {
      devices,
      faultRateData,
      maintenanceData,
      outputChart,
      costChart,
      getStatusType,
      getStatusText
    }
  }
}
</script>
