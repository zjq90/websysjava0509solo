<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总门诊量</span>
          </div>
          <div class="text item">
            <h2 style="color: #409EFF; font-size: 32px;">{{ summary.totalOutpatients }}</h2>
            <p>近30天</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总住院量</span>
          </div>
          <div class="text item">
            <h2 style="color: #67C23A; font-size: 32px;">{{ summary.totalInpatients }}</h2>
            <p>近30天</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>总手术量</span>
          </div>
          <div class="text item">
            <h2 style="color: #E6A23C; font-size: 32px;">{{ summary.totalSurgeries }}</h2>
            <p>近30天</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>平均床位使用率</span>
          </div>
          <div class="text item">
            <h2 style="color: #F56C6C; font-size: 32px;">{{ summary.avgBedUsageRate }}%</h2>
            <p>近30天</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>门诊量趋势</span>
          </div>
          <div ref="outpatientChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>科室收入占比</span>
          </div>
          <div ref="incomeChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>病历质量统计</span>
          </div>
          <div ref="qualityChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>成本效益分析</span>
          </div>
          <div ref="costChart" style="height: 350px;"></div>
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
      summary: {
        totalOutpatients: 0,
        totalInpatients: 0,
        totalSurgeries: 0,
        avgBedUsageRate: '0.00'
      }
    }
  },
  mounted() {
    this.loadSummary()
    this.initCharts()
  },
  methods: {
    async loadSummary() {
      try {
        const endDate = this.formatDate(new Date())
        const startDate = this.formatDate(new Date(Date.now() - 30 * 24 * 60 * 60 * 1000))
        const response = await this.$http.get(`/operation-metrics/statistics-summary?startDate=${startDate}&endDate=${endDate}`)
        this.summary = response.data
      } catch (error) {
        console.error('加载汇总数据失败', error)
        this.summary = {
          totalOutpatients: 15680,
          totalInpatients: 3240,
          totalSurgeries: 890,
          avgBedUsageRate: '87.50'
        }
      }
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    initCharts() {
      this.initOutpatientChart()
      this.initIncomeChart()
      this.initQualityChart()
      this.initCostChart()
    },
    initOutpatientChart() {
      const chart = echarts.init(this.$refs.outpatientChart)
      const dates = []
      const data = []
      for (let i = 29; i >= 0; i--) {
        const d = new Date(Date.now() - i * 24 * 60 * 60 * 1000)
        dates.push(`${d.getMonth() + 1}/${d.getDate()}`)
        data.push(Math.floor(Math.random() * 500) + 1000)
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value' },
        series: [{ data, type: 'line', smooth: true, areaStyle: { color: 'rgba(64, 158, 255, 0.3)' }, lineStyle: { color: '#409EFF' } }]
      })
    },
    initIncomeChart() {
      const chart = echarts.init(this.$refs.incomeChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: '5%' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          data: [
            { value: 250000, name: '内科' },
            { value: 220000, name: '外科' },
            { value: 180000, name: '妇产科' },
            { value: 150000, name: '儿科' },
            { value: 120000, name: '骨科' },
            { value: 200000, name: '其他科室' }
          ]
        }]
      })
    },
    initQualityChart() {
      const chart = echarts.init(this.$refs.qualityChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['甲级病历', '乙级病历', '丙级病历'] },
        xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科'] },
        yAxis: { type: 'value' },
        series: [
          { name: '甲级病历', type: 'bar', stack: 'total', data: [850, 780, 620, 580, 650, 420] },
          { name: '乙级病历', type: 'bar', stack: 'total', data: [80, 70, 55, 45, 50, 35] },
          { name: '丙级病历', type: 'bar', stack: 'total', data: [15, 12, 8, 6, 10, 5] }
        ]
      })
    },
    initCostChart() {
      const chart = echarts.init(this.$refs.costChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['收入', '成本', '利润'] },
        xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科'] },
        yAxis: { type: 'value' },
        series: [
          { name: '收入', type: 'bar', data: [250000, 220000, 180000, 150000, 160000, 120000] },
          { name: '成本', type: 'bar', data: [180000, 160000, 130000, 110000, 120000, 85000] },
          { name: '利润', type: 'line', data: [70000, 60000, 50000, 40000, 40000, 35000] }
        ]
      })
    }
  }
}
</script>

<style scoped>
.box-card {
  text-align: center;
}
</style>
