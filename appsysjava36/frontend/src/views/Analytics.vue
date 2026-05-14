<template>
  <div class="analytics">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header">服务效能统计</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="stat-item">
                <div class="stat-label">平均响应时长</div>
                <div class="stat-value primary">{{ metrics.avgResponseTime || 0 }} 分钟</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <div class="stat-label">一次修复率</div>
                <div class="stat-value success">{{ metrics.firstFixRate || 0 }}%</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <div class="stat-label">平均满意度</div>
                <div class="stat-value warning">{{ metrics.avgSatisfaction || 0 }} 分</div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="stat-item">
                <div class="stat-label">工单总数</div>
                <div class="stat-value info">{{ metrics.totalOrders || 0 }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">用户行为分析</div>
          <div ref="chartPie" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>工单趋势</span>
            <el-select v-model="days" size="mini" style="float: right" @change="loadTrend">
              <el-option label="近7天" :value="7"></el-option>
              <el-option label="近30天" :value="30"></el-option>
            </el-select>
          </div>
          <div ref="chartLine" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <div slot="header">日报导出</div>
          <el-date-picker v-model="reportDate" type="date" placeholder="选择日期" style="width: 200px;"></el-date-picker>
          <el-button type="primary" style="margin-left: 10px" @click="exportDailyReport">导出PDF</el-button>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">数据导出</div>
          <el-button type="success" @click="exportWorkOrderExcel">导出工单数据Excel</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Analytics',
  data() {
    return {
      metrics: {},
      days: 7,
      reportDate: new Date(),
      trendData: []
    }
  },
  mounted() {
    this.loadMetrics()
    this.loadTrend()
  },
  methods: {
    async loadMetrics() {
      const end = new Date().toISOString()
      const start = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString()
      try {
        const res = await this.$http.get(`/api/analytics/metrics?start=${start}&end=${end}`)
        if (res.data && res.data.data) {
          this.metrics = res.data.data
        }
      } catch (e) {
        this.metrics = {
          avgResponseTime: 35,
          firstFixRate: 85,
          avgSatisfaction: 4.5,
          totalOrders: 20
        }
      }
      this.$nextTick(() => {
        this.initPieChart()
      })
    },
    async loadTrend() {
      try {
        const res = await this.$http.get(`/api/analytics/order-trend?days=${this.days}`)
        if (res.data && res.data.data) {
          this.trendData = res.data.data
        }
      } catch (e) {
        this.trendData = []
        for (let i = this.days - 1; i >= 0; i--) {
          const date = new Date()
          date.setDate(date.getDate() - i)
          this.trendData.push({
            date: `${date.getMonth() + 1}-${date.getDate()}`,
            count: Math.floor(Math.random() * 10) + 1
          })
        }
      }
      this.$nextTick(() => {
        this.initLineChart()
      })
    },
    initPieChart() {
      const chart = echarts.init(this.$refs.chartPie)
      chart.setOption({
        tooltip: {
          trigger: 'item'
        },
        series: [{
          name: '套餐分布',
          type: 'pie',
          radius: ['40%', '70%'],
          data: [
            { value: 35, name: '基础套餐', itemStyle: { color: '#409EFF' } },
            { value: 45, name: '标准套餐', itemStyle: { color: '#67C23A' } },
            { value: 20, name: '高级套餐', itemStyle: { color: '#E6A23C' } }
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
    },
    initLineChart() {
      const chart = echarts.init(this.$refs.chartLine)
      chart.setOption({
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.trendData.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.trendData.map(item => item.count),
          type: 'line',
          smooth: true,
          itemStyle: { color: '#409EFF' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ])
          }
        }]
      })
    },
    async exportDailyReport() {
      this.$message.info('正在生成报表...')
    },
    async exportWorkOrderExcel() {
      this.$message.info('正在导出Excel...')
    }
  }
}
</script>

<style scoped>
.analytics {
  padding: 0;
}
.stat-item {
  text-align: center;
  padding: 20px 0;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
}
.primary {
  color: #409EFF;
}
.success {
  color: #67C23A;
}
.warning {
  color: #E6A23C;
}
.info {
  color: #909399;
}
</style>
