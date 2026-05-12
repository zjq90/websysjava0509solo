<template>
  <div>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div slot="header">
        <span>数据概览</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div style="text-align: center; padding: 20px; background: #409EFF; color: white; border-radius: 8px">
            <div style="font-size: 36px; font-weight: bold">{{ patientCount }}</div>
            <div>患者总数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="text-align: center; padding: 20px; background: #67C23A; color: white; border-radius: 8px">
            <div style="font-size: 36px; font-weight: bold">{{ outpatientCount }}</div>
            <div>今日门诊</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="text-align: center; padding: 20px; background: #E6A23C; color: white; border-radius: 8px">
            <div style="font-size: 36px; font-weight: bold">{{ inpatientCount }}</div>
            <div>今日住院</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div style="text-align: center; padding: 20px; background: #F56C6C; color: white; border-radius: 8px">
            <div style="font-size: 36px; font-weight: bold">¥{{ totalAmount }}</div>
            <div>今日收入</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>门诊收费趋势</span>
          </div>
          <div ref="outpatientChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header">
            <span>住院收费趋势</span>
          </div>
          <div ref="inpatientChart" style="height: 300px"></div>
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
      patientCount: 0,
      outpatientCount: 0,
      inpatientCount: 0,
      totalAmount: '0.00'
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  methods: {
    loadData() {
      this.$http.get('/patients').then(res => {
        this.patientCount = res.data.length
      })
      this.$http.get('/outpatient-charges').then(res => {
        this.outpatientCount = res.data.filter(c => c.status === '已缴费').length
        const total = res.data.filter(c => c.status === '已缴费').reduce((sum, c) => sum + parseFloat(c.totalAmount), 0)
        this.totalAmount = total.toFixed(2)
      })
      this.$http.get('/inpatient-charges').then(res => {
        this.inpatientCount = res.data.filter(c => c.status === '已结算').length
        const total = res.data.filter(c => c.status === '已结算').reduce((sum, c) => sum + parseFloat(c.totalAmount), 0)
        this.totalAmount = (parseFloat(this.totalAmount) + total).toFixed(2)
      })
    },
    initCharts() {
      const outpatientChart = echarts.init(this.$refs.outpatientChart)
      const inpatientChart = echarts.init(this.$refs.inpatientChart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: 'line',
            smooth: true,
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            },
            lineStyle: {
              color: '#409EFF'
            },
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }

      outpatientChart.setOption(option)
      inpatientChart.setOption({
        ...option,
        series: [{
          ...option.series[0],
          data: [80, 120, 100, 150, 90, 70, 110],
          areaStyle: {
            color: 'rgba(103, 194, 58, 0.3)'
          },
          lineStyle: {
            color: '#67C23A'
          },
          itemStyle: {
            color: '#67C23A'
          }
        }]
      })

      window.addEventListener('resize', () => {
        outpatientChart.resize()
        inpatientChart.resize()
      })
    }
  }
}
</script>

<style scoped>
</style>
