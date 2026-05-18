<template>
  <div>
    <h1 class="page-title">📊 数据概览</h1>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: 24px;">
      <el-col :span="6">
        <div class="stat-card">
          <div class="number">{{ stats.relicCount }}</div>
          <div class="label">📦 文物总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="number">{{ stats.nodeCount }}</div>
          <div class="label">🔗 区块链节点</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="number">{{ stats.deviceCount }}</div>
          <div class="label">📡 物联网设备</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="number">{{ stats.vrSceneCount }}</div>
          <div class="label">🎮 VR场景</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 16px;">📈 文物类别分布</h3>
          <div ref="categoryChartRef" class="chart-container"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 16px;">🏛️ 文物朝代分布</h3>
          <div ref="dynastyChartRef" class="chart-container"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="12">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 16px;">🔗 区块链节点状态</h3>
          <el-table :data="nodeList" style="width: 100%">
            <el-table-column prop="nodeName" label="节点名称" />
            <el-table-column prop="nodeAddress" label="节点地址" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
                  {{ scope.row.status === 1 ? '在线' : '离线' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="availabilityZone" label="可用区" />
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card-wrapper">
          <h3 style="margin-bottom: 16px;">📡 物联网设备监控</h3>
          <el-table :data="deviceList" style="width: 100%">
            <el-table-column prop="deviceName" label="设备名称" />
            <el-table-column prop="currentTemperature" label="温度(℃)" />
            <el-table-column prop="currentHumidity" label="湿度(%)" />
            <el-table-column prop="status" label="状态">
              <template #default="scope">
                <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
                  {{ scope.row.status === 1 ? '正常' : '告警' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import request from '@/api'

const categoryChartRef = ref(null)
const dynastyChartRef = ref(null)
const nodeList = ref([])
const deviceList = ref([])
const stats = ref({
  relicCount: 0,
  nodeCount: 0,
  deviceCount: 0,
  vrSceneCount: 0
})

const initCategoryChart = () => {
  const chart = echarts.init(categoryChartRef.value)
  chart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 35, name: '青铜器' },
        { value: 25, name: '瓷器' },
        { value: 20, name: '书画' },
        { value: 15, name: '玉器' },
        { value: 5, name: '其他' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  })
}

const initDynastyChart = () => {
  const chart = echarts.init(dynastyChartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['商代', '周代', '汉代', '唐代', '宋代', '元代', '明代', '清代']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [12, 8, 15, 22, 18, 10, 25, 30],
      type: 'bar',
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#3b82f6' },
          { offset: 1, color: '#8b5cf6' }
        ])
      }
    }]
  })
}

const loadData = async () => {
  try {
    const [nodeRes, deviceRes, relicRes] = await Promise.all([
      request.get('/blockchain-node'),
      request.get('/iot-device'),
      request.get('/relic')
    ])
    nodeList.value = nodeRes.data.slice(0, 5)
    deviceList.value = deviceRes.data.slice(0, 5)
    stats.value.relicCount = relicRes.data.length
    stats.value.nodeCount = nodeRes.data.length
    stats.value.deviceCount = deviceRes.data.length
    stats.value.vrSceneCount = 4
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

onMounted(() => {
  initCategoryChart()
  initDynastyChart()
  loadData()
})
</script>
