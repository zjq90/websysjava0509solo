<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">成本分析</div>
      <el-date-picker
        v-model="startDate"
        type="month"
        placeholder="选择月份"
        style="width: 200px"
        value-format="YYYY-MM-DD"
        @change="loadCostAnalysis"
      />
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
          <div class="stat-title">📉 车辆折旧</div>
          <div class="stat-value">¥{{ costAnalysis.totalDepreciation || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
          <div class="stat-title">🔧 维护维修</div>
          <div class="stat-value">¥{{ costAnalysis.totalMaintenance || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
          <div class="stat-title">⚙️ 运营支出</div>
          <div class="stat-value">¥{{ costAnalysis.totalOperation || 0 }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)">
          <div class="stat-title">💵 总成本</div>
          <div class="stat-value">¥{{ totalCost }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">成本构成分析</div>
          <div ref="costPieChart" style="height: 300px"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="chart-card">
          <div class="chart-title">成本明细分类</div>
          <div ref="costBarChart" style="height: 300px"></div>
        </div>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center">
          <span>成本记录</span>
          <el-radio-group v-model="costTypeFilter" @change="loadCostRecords" size="small">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="DEPRECIATION">折旧</el-radio-button>
            <el-radio-button label="MAINTENANCE">维修</el-radio-button>
            <el-radio-button label="OPERATION">运营</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <el-table :data="costRecords" stripe style="width: 100%">
        <el-table-column prop="recordNo" label="记录编号" width="150" />
        <el-table-column prop="costType" label="类型" width="100">
          <template #default="{ row }">
            <span :class="getTypeClass(row.costType)">{{ getTypeText(row.costType) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="costCategory" label="分类" width="150" />
        <el-table-column prop="bikeNo" label="车辆编号" width="120" />
        <el-table-column prop="areaName" label="区域" width="100" />
        <el-table-column prop="amount" label="金额" width="100">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
        <el-table-column prop="costDate" label="日期" width="120" />
        <el-table-column prop="operator" label="操作人" width="100" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getCostAnalysis, getCosts } from '../../api'

const costAnalysis = ref({})
const costRecords = ref([])
const startDate = ref(new Date().toISOString().substring(0, 7) + '-01')
const costTypeFilter = ref('')
const costPieChart = ref(null)
const costBarChart = ref(null)

const totalCost = computed(() => {
  const c = costAnalysis.value
  return ((parseFloat(c.totalDepreciation || 0) + 
           parseFloat(c.totalMaintenance || 0) + 
           parseFloat(c.totalOperation || 0)).toFixed(2))
})

const getTypeClass = (type) => {
  const map = {
    DEPRECIATION: 'badge-info',
    MAINTENANCE: 'badge-warning',
    OPERATION: 'badge-primary'
  }
  return map[type] || 'badge-info'
}

const getTypeText = (type) => {
  const map = {
    DEPRECIATION: '折旧',
    MAINTENANCE: '维修',
    OPERATION: '运营'
  }
  return map[type] || type
}

const initPieChart = () => {
  const c = costAnalysis.value
  const chart = echarts.init(costPieChart.value)
  const option = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%', left: 'center' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      data: [
        { value: c.totalDepreciation || 0, name: '车辆折旧', itemStyle: { color: '#667eea' } },
        { value: c.totalMaintenance || 0, name: '维护维修', itemStyle: { color: '#f5576c' } },
        { value: c.totalOperation || 0, name: '运营支出', itemStyle: { color: '#4facfe' } }
      ]
    }]
  }
  chart.setOption(option)
}

const initBarChart = () => {
  const details = costAnalysis.value.categoryDetails || []
  const chart = echarts.init(costBarChart.value)
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'value' },
    yAxis: { type: 'category', data: details.map(d => d.category || '其他') },
    series: [{
      type: 'bar',
      data: details.map(d => d.amount || 0),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: '#667eea' },
          { offset: 1, color: '#764ba2' }
        ]),
        borderRadius: [0, 8, 8, 0]
      }
    }]
  }
  chart.setOption(option)
}

const loadCostAnalysis = async () => {
  try {
    const res = await getCostAnalysis(startDate.value)
    costAnalysis.value = res.data
    await nextTick()
    initPieChart()
    initBarChart()
  } catch (e) {
    console.error(e)
  }
}

const loadCostRecords = async () => {
  try {
    const res = await getCosts(costTypeFilter.value)
    costRecords.value = res.data
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadCostAnalysis()
  loadCostRecords()
})
</script>
