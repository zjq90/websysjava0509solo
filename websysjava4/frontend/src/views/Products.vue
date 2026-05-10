<template>
  <div>
    <div class="page-title">📦 商品统计分析</div>

    <div class="card">
      <div class="card-title">🔥 热销商品排行 TOP10</div>
      <div ref="hotChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">📉 滞销商品分析</div>
      <el-table :data="unsoldProducts" border stripe>
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="category" label="分类" />
        <el-table-column prop="stockQuantity" label="库存数量" />
        <el-table-column prop="costPrice" label="成本价" />
        <el-table-column prop="salePrice" label="售价" />
        <el-table-column prop="inventoryValue" label="库存价值">
          <template #default="scope">
            ¥{{ formatNumber(scope.row.inventoryValue) }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card">
      <div class="card-title">📊 毛利率分析</div>
      <div class="stats-grid" v-if="grossMarginData">
        <div class="stats-item">
          <div class="value">¥{{ formatNumber(grossMarginData.totalRevenue) }}</div>
          <div class="label">总营收</div>
        </div>
        <div class="stats-item">
          <div class="value">¥{{ formatNumber(grossMarginData.totalCost) }}</div>
          <div class="label">总成本</div>
        </div>
        <div class="stats-item">
          <div class="value">¥{{ formatNumber(grossMarginData.totalProfit) }}</div>
          <div class="label">总利润</div>
        </div>
        <div class="stats-item">
          <div class="value" :style="{ color: grossMarginData.overallGrossMargin > 30 ? '#67c23a' : '#e6a23c' }">
            {{ grossMarginData.overallGrossMargin }}%
          </div>
          <div class="label">综合毛利率</div>
        </div>
      </div>
      <div ref="marginChart" class="chart-container"></div>
    </div>

    <div class="card">
      <div class="card-title">📋 商品列表</div>
      <el-table :data="products" border stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" />
        <el-table-column prop="category" label="分类" />
        <el-table-column prop="costPrice" label="成本价" />
        <el-table-column prop="salePrice" label="售价" />
        <el-table-column prop="stockQuantity" label="库存" />
        <el-table-column prop="salesQuantity" label="销量" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? '在售' : '下架' }}
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
  name: 'Products',
  setup() {
    const products = ref([])
    const unsoldProducts = ref([])
    const grossMarginData = ref(null)
    const hotChart = ref(null)
    const marginChart = ref(null)
    let hotChartInstance = null
    let marginChartInstance = null

    const formatNumber = (num) => {
      if (num == null) return '0'
      return Number(num).toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    }

    const loadData = async () => {
      try {
        const [productsRes, hotRes, unsoldRes, marginRes] = await Promise.all([
          api.getProducts(0, 25),
          api.getHotSellingProducts(10),
          api.getUnsoldProducts(10),
          api.getGrossMarginAnalysis()
        ])

        products.value = productsRes.data.content || []
        unsoldProducts.value = unsoldRes.data || []
        grossMarginData.value = marginRes.data || {}

        renderHotChart(hotRes.data)
        renderMarginChart(marginRes.data)
      } catch (error) {
        console.error('Failed to load products data:', error)
      }
    }

    const renderHotChart = (chartData) => {
      if (!hotChart.value) return
      if (!hotChartInstance) {
        hotChartInstance = echarts.init(hotChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['销售数量', '毛利率(%)'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: chartData.map(item => item.name)
        },
        yAxis: [
          { type: 'value', name: '销售数量' },
          { type: 'value', name: '毛利率(%)', max: 100 }
        ],
        series: [
          {
            name: '销售数量',
            type: 'bar',
            data: chartData.map(item => item.salesQuantity),
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '毛利率(%)',
            type: 'line',
            yAxisIndex: 1,
            data: chartData.map(item => item.grossMargin),
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      hotChartInstance.setOption(option)
    }

    const renderMarginChart = (data) => {
      if (!marginChart.value || !data.byCategory) return
      if (!marginChartInstance) {
        marginChartInstance = echarts.init(marginChart.value)
      }

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: ['营收', '成本', '利润'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          data: data.byCategory.map(item => item.category)
        },
        yAxis: { type: 'value' },
        series: [
          {
            name: '营收',
            type: 'bar',
            data: data.byCategory.map(item => item.revenue),
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '成本',
            type: 'bar',
            data: data.byCategory.map(item => item.cost),
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: '利润',
            type: 'bar',
            data: data.byCategory.map(item => item.profit),
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      marginChartInstance.setOption(option)
    }

    const handleResize = () => {
      hotChartInstance?.resize()
      marginChartInstance?.resize()
    }

    onMounted(() => {
      loadData()
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      hotChartInstance?.dispose()
      marginChartInstance?.dispose()
      window.removeEventListener('resize', handleResize)
    })

    return {
      products,
      unsoldProducts,
      grossMarginData,
      hotChart,
      marginChart,
      formatNumber
    }
  }
}
</script>
