<template>
  <div class="dashboard">
    <h2>数据统计</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>文物总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #409EFF">{{ stats.totalHeritage || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #67C23A">{{ stats.totalUsers || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>交易总数</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #E6A23C">{{ stats.totalTransactions || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>待审核</span>
          </div>
          <div style="font-size: 32px; text-align: center; color: #F56C6C">{{ stats.pendingAudit || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户信用分分布</span>
          </div>
          <div ref="creditChart" style="height: 300px; width: 100%"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>专家用户</span>
          </div>
          <div style="font-size: 32px; text-align: center; margin-top: 80px; color: #909399">{{ stats.totalExperts || 0 }} 人</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {},
      creditData: {}
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadCreditDistribution()
  },
  methods: {
    loadStatistics() {
      this.$http.get('/dashboard/statistics').then(res => {
        if (res.data.code === 200) {
          this.stats = res.data.data
        }
      })
    },
    loadCreditDistribution() {
      this.$http.get('/dashboard/user/credit-score').then(res => {
        if (res.data.code === 200) {
          this.creditData = res.data.data
          this.initChart()
        }
      })
    },
    initChart() {
      const chart = echarts.init(this.$refs.creditChart)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: '0%',
          left: 'center'
        },
        series: [
          {
            name: '信用分',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: this.creditData['优秀(90-100)'] || 0, name: '优秀(90-100)', itemStyle: { color: '#67C23A' } },
              { value: this.creditData['良好(70-89)'] || 0, name: '良好(70-89)', itemStyle: { color: '#409EFF' } },
              { value: this.creditData['中等(50-69)'] || 0, name: '中等(50-69)', itemStyle: { color: '#E6A23C' } },
              { value: this.creditData['较差(0-49)'] || 0, name: '较差(0-49)', itemStyle: { color: '#F56C6C' } }
            ]
          }
        ]
      }
      chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
