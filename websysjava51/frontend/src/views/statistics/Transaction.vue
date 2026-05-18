<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">交易额统计</h2>
      <div>
        <el-radio-group v-model="period" @change="handlePeriodChange">
          <el-radio-button label="day">按日</el-radio-button>
          <el-radio-button label="week">按周</el-radio-button>
          <el-radio-button label="month">按月</el-radio-button>
        </el-radio-group>
      </div>
    </div>
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ totalAmount }}</div>
              <div class="stat-label">总交易额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalOrders }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalUsers }}</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ avgAmount }}</div>
              <div class="stat-label">客单价</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card>
      <div ref="chart" style="height: 500px;"></div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'TransactionStatistics',
  data() {
    return {
      period: 'day',
      totalAmount: '128,560.00',
      totalOrders: 1256,
      totalUsers: 892,
      avgAmount: '102.36'
    }
  },
  mounted() {
    this.initChart()
  },
  methods: {
    initChart() {
      const chart = this.$echarts.init(this.$refs.chart)
      const option = this.getChartOption()
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    getChartOption() {
      let xAxisData = []
      let seriesData = []
      
      if (this.period === 'day') {
        for (let i = 1; i <= 30; i++) {
          xAxisData.push(i + '日')
          seriesData.push(Math.floor(Math.random() * 5000) + 2000)
        }
      } else if (this.period === 'week') {
        for (let i = 1; i <= 12; i++) {
          xAxisData.push('第' + i + '周')
          seriesData.push(Math.floor(Math.random() * 20000) + 10000)
        }
      } else {
        const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        xAxisData = months
        for (let i = 0; i < 12; i++) {
          seriesData.push(Math.floor(Math.random() * 50000) + 30000)
        }
      }
      
      return {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>交易额: ¥{c}'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxisData
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '交易额',
            type: 'line',
            smooth: true,
            data: seriesData,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
                ]
              }
            },
            itemStyle: {
              color: '#409EFF'
            },
            lineStyle: {
              width: 3
            }
          }
        ]
      }
    },
    handlePeriodChange() {
      this.initChart()
    }
  }
}
</script>

<style scoped>
.stat-card {
  border: none;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>