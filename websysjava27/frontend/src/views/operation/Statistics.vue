<template>
  <div class="operation-statistics">
    <el-card>
      <div slot="header" class="clearfix">
        <span>运营指标统计分析</span>
      </div>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="开始日期">
          <el-date-picker v-model="startDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="endDate" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>门诊量趋势分析</span>
          </div>
          <div ref="outpatientTrendChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>各科室运营数据对比</span>
          </div>
          <div ref="departmentCompareChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>床位使用率趋势</span>
          </div>
          <div ref="bedUsageChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>药占比与耗占比分析</span>
          </div>
          <div ref="ratioChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'OperationStatistics',
  data() {
    return {
      startDate: '',
      endDate: ''
    }
  },
  mounted() {
    const now = new Date()
    const before = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
    this.startDate = this.formatDate(before)
    this.endDate = this.formatDate(now)
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  methods: {
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    loadStatistics() {
      this.initCharts()
    },
    initCharts() {
      this.initOutpatientTrendChart()
      this.initDepartmentCompareChart()
      this.initBedUsageChart()
      this.initRatioChart()
    },
    initOutpatientTrendChart() {
      const chart = echarts.init(this.$refs.outpatientTrendChart)
      const dates = []
      const data = []
      for (let i = 29; i >= 0; i--) {
        const d = new Date(Date.now() - i * 24 * 60 * 60 * 1000)
        dates.push(`${d.getMonth() + 1}/${d.getDate()}`)
        data.push(Math.floor(Math.random() * 300) + 800)
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value' },
        series: [{
          name: '门诊量',
          data,
          type: 'line',
          smooth: true,
          areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
          lineStyle: { color: '#409EFF' }
        }]
      })
    },
    initDepartmentCompareChart() {
      const chart = echarts.init(this.$refs.departmentCompareChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['门诊量', '住院量', '手术量'] },
        xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科', '口腔科', '肿瘤科'] },
        yAxis: { type: 'value' },
        series: [
          { name: '门诊量', type: 'bar', data: [3200, 2800, 2400, 2000, 1800, 1500, 1200, 1600] },
          { name: '住院量', type: 'bar', data: [850, 720, 650, 580, 490, 380, 290, 420] },
          { name: '手术量', type: 'bar', data: [220, 380, 180, 80, 280, 120, 60, 150] }
        ]
      })
    },
    initBedUsageChart() {
      const chart = echarts.init(this.$refs.bedUsageChart)
      const dates = []
      const data = []
      for (let i = 29; i >= 0; i--) {
        const d = new Date(Date.now() - i * 24 * 60 * 60 * 1000)
        dates.push(`${d.getMonth() + 1}/${d.getDate()}`)
        data.push((75 + Math.random() * 20).toFixed(1))
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: dates },
        yAxis: { type: 'value', max: 100 },
        series: [{
          name: '床位使用率(%)',
          data,
          type: 'line',
          smooth: true,
          lineStyle: { color: '#67C23A' },
          areaStyle: { color: 'rgba(103, 194, 58, 0.3)' }
        }]
      })
    },
    initRatioChart() {
      const chart = echarts.init(this.$refs.ratioChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['药占比', '耗占比'] },
        xAxis: { type: 'category', data: ['内科', '外科', '妇产科', '儿科', '骨科', '眼科', '口腔科', '肿瘤科'] },
        yAxis: { type: 'value', max: 60 },
        series: [
          { name: '药占比', type: 'bar', data: [42, 38, 45, 48, 35, 32, 36, 50] },
          { name: '耗占比', type: 'bar', data: [28, 35, 25, 20, 38, 30, 22, 25] }
        ]
      })
    }
  }
}
</script>
