<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%)">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ formatNumber(dashboardData.totalRevenue || 0) }}</div>
              <div class="stat-label">总营收</div>
              <div class="stat-growth" :class="{ positive: dashboardData.revenueGrowthRate >= 0 }">
                {{ dashboardData.revenueGrowthRate || 0 }}% 同比
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%)">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ dashboardData.totalOrders || 0 }}单</div>
              <div class="stat-label">订单量</div>
              <div class="stat-growth" :class="{ positive: dashboardData.orderGrowthRate >= 0 }">
                {{ dashboardData.orderGrowthRate || 0 }}% 同比
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)">
              <i class="el-icon-s-custom"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ formatNumber(dashboardData.avgOrderValue || 0) }}</div>
              <div class="stat-label">客单价</div>
              <div class="stat-growth" :class="{ positive: dashboardData.avgOrderGrowthRate >= 0 }">
                {{ dashboardData.avgOrderGrowthRate || 0 }}% 同比
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ dashboardData.conversionRate || 0 }}%</div>
              <div class="stat-label">转化率</div>
              <div class="stat-growth" :class="{ positive: dashboardData.conversionGrowthRate >= 0 }">
                {{ dashboardData.conversionGrowthRate || 0 }}% 同比
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>营收趋势</span>
          </div>
          <div ref="revenueChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>套餐销量分布</span>
          </div>
          <div ref="packageChart" style="height: 300px"></div>
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
      dashboardData: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await this.$http.get('/dashboard/sales')
        if (res.data.success) {
          this.dashboardData = res.data.data
          this.initCharts()
        }
      } catch (error) {
        console.error('获取数据失败', error)
      }
    },
    initCharts() {
      this.initRevenueChart()
      this.initPackageChart()
    },
    initRevenueChart() {
      const chart = echarts.init(this.$refs.revenueChart)
      const option = {
        tooltip: {
          trigger: 'axis'
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
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '营收',
            type: 'line',
            smooth: true,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(102, 126, 234, 0.3)' },
                { offset: 1, color: 'rgba(102, 126, 234, 0.05)' }
              ])
            },
            lineStyle: {
              color: '#667eea',
              width: 2
            },
            itemStyle: {
              color: '#667eea'
            },
            data: [82000, 93200, 90100, 93400, 129000, 133000, 132000]
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initPackageChart() {
      const chart = echarts.init(this.$refs.packageChart)
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '销量',
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
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048, name: '婚纱照' },
              { value: 735, name: '个人写真' },
              { value: 580, name: '全家福' },
              { value: 484, name: '儿童摄影' },
              { value: 300, name: '商业摄影' }
            ]
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    formatNumber(num) {
      return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.stat-icon i {
  font-size: 28px;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 5px;
}

.stat-growth {
  font-size: 12px;
  color: #f56c6c;
}

.stat-growth.positive {
  color: #67c23a;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}
</style>
