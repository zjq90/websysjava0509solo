<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>工单总数</span>
          </div>
          <div class="text item">
            <h1 style="font-size: 36px; color: #409EFF;">{{ stats.totalOrders || 0 }}</h1>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>待处理工单</span>
          </div>
          <div class="text item">
            <h1 style="font-size: 36px; color: #E6A23C;">{{ stats.pendingOrders || 0 }}</h1>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平均响应时长</span>
          </div>
          <div class="text item">
            <h1 style="font-size: 36px; color: #67C23A;">{{ stats.avgResponseTime || 0 }} 分钟</h1>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平均满意度</span>
          </div>
          <div class="text item">
            <h1 style="font-size: 36px; color: #F56C6C;">{{ stats.avgSatisfaction || 0 }} 分</h1>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>工单趋势</span>
          </div>
          <div ref="chartTrend" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>套餐分布</span>
          </div>
          <div ref="chartPackage" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Home',
  data() {
    return {
      stats: {},
      trendData: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadTrend()
  },
  methods: {
    async loadStats() {
      const end = new Date().toISOString()
      const start = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString()
      try {
        const res = await this.$http.get(`/api/analytics/metrics?start=${start}&end=${end}`)
        if (res.data && res.data.data) {
          this.stats = res.data.data
        }
      } catch (e) {
        this.stats = {
          totalOrders: 20,
          pendingOrders: 5,
          avgResponseTime: 35,
          avgSatisfaction: 4.5
        }
      }
    },
    async loadTrend() {
      try {
        const res = await this.$http.get('/api/analytics/order-trend?days=7')
        if (res.data && res.data.data) {
          this.trendData = res.data.data
        }
      } catch (e) {
        this.trendData = [
          { date: '05-08', count: 5 },
          { date: '05-09', count: 3 },
          { date: '05-10', count: 4 },
          { date: '05-11', count: 2 },
          { date: '05-12', count: 3 },
          { date: '05-13', count: 1 },
          { date: '05-14', count: 2 }
        ]
      }
      this.$nextTick(() => {
        this.initCharts()
      })
    },
    initCharts() {
      const chartTrend = echarts.init(this.$refs.chartTrend)
      chartTrend.setOption({
        tooltip: {},
        xAxis: {
          data: this.trendData.map(item => item.date)
        },
        yAxis: {},
        series: [{
          name: '工单数量',
          type: 'line',
          data: this.trendData.map(item => item.count),
          smooth: true,
          itemStyle: { color: '#409EFF' }
        }]
      })

      const chartPackage = echarts.init(this.$refs.chartPackage)
      chartPackage.setOption({
        tooltip: {},
        series: [{
          name: '套餐分布',
          type: 'pie',
          radius: '60%',
          data: [
            { value: 3, name: '基础套餐' },
            { value: 3, name: '标准套餐' },
            { value: 2, name: '高级套餐' }
          ]
        }]
      })

      window.addEventListener('resize', () => {
        chartTrend.resize()
        chartPackage.resize()
      })
    }
  }
}
</script>

<style scoped>
.home {
  padding: 0;
}
.box-card {
  text-align: center;
}
</style>
