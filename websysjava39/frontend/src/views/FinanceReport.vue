<template>
  <div class="finance-report">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center">
            <span>财务报表</span>
            <el-radio-group v-model="period" size="small" @change="fetchData">
              <el-radio-button label="DAILY">日报</el-radio-button>
              <el-radio-button label="WEEKLY">周报</el-radio-button>
              <el-radio-button label="MONTHLY">月报</el-radio-button>
            </el-radio-group>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-card income">
                <div class="stat-icon">
                  <i class="el-icon-finance"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-label">总收入</div>
                  <div class="stat-value">¥{{ formatNumber(financeData.totalIncome || 0) }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card expense">
                <div class="stat-icon">
                  <i class="el-icon-tickets"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-label">总支出</div>
                  <div class="stat-value">¥{{ formatNumber(financeData.totalExpense || 0) }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card profit">
                <div class="stat-icon">
                  <i class="el-icon-wallet"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-label">净利润</div>
                  <div class="stat-value">¥{{ formatNumber(financeData.netProfit || 0) }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card order">
                <div class="stat-icon">
                  <i class="el-icon-s-order"></i>
                </div>
                <div class="stat-content">
                  <div class="stat-label">订单收入</div>
                  <div class="stat-value">¥{{ formatNumber(financeData.orderIncome || 0) }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="14">
        <el-card>
          <div slot="header" class="card-header">
            <span>收支趋势</span>
          </div>
          <div ref="trendChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card>
          <div slot="header" class="card-header">
            <span>支出构成</span>
          </div>
          <div ref="expenseChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinanceReport',
  data() {
    return {
      period: 'MONTHLY',
      financeData: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$http.get('/dashboard/finance-report', { params: { period: this.period } }).then(res => {
        if (res.data.success) {
          this.financeData = res.data.data
          this.initCharts()
        }
      })
    },
    initCharts() {
      this.initTrendChart()
      this.initExpenseChart()
    },
    initTrendChart() {
      const chart = echarts.init(this.$refs.trendChart)
      const dates = ['1日', '2日', '3日', '4日', '5日', '6日', '7日']
      const incomeData = [12000, 15000, 18000, 16000, 20000, 19000, 22000]
      const expenseData = [8000, 6000, 9000, 7000, 10000, 8500, 9500]
      const profitData = incomeData.map((income, i) => income - expenseData[i])
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['收入', '支出', '利润']
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
          data: dates
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        series: [
          {
            name: '收入',
            type: 'line',
            smooth: true,
            data: incomeData,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(102, 198, 255, 0.3)' },
                { offset: 1, color: 'rgba(102, 198, 255, 0.05)' }
              ])
            },
            lineStyle: {
              color: '#66c6ff',
              width: 2
            },
            itemStyle: {
              color: '#66c6ff'
            }
          },
          {
            name: '支出',
            type: 'line',
            smooth: true,
            data: expenseData,
            lineStyle: {
              color: '#e6a23c',
              width: 2
            },
            itemStyle: {
              color: '#e6a23c'
            }
          },
          {
            name: '利润',
            type: 'bar',
            data: profitData,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#67c23a' },
                { offset: 1, color: '#95d475' }
              ])
            }
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initExpenseChart() {
      const chart = echarts.init(this.$refs.expenseChart)
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: ¥{c} ({d}%)'
        },
        series: [
          {
            name: '支出',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {d}%'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: true
            },
            data: [
              { value: this.financeData.salaryExpense || 30000, name: '工资', itemStyle: { color: '#667eea' } },
              { value: this.financeData.rentExpense || 15000, name: '房租', itemStyle: { color: '#f093fb' } },
              { value: this.financeData.materialsExpense || 8000, name: '耗材', itemStyle: { color: '#4facfe' } },
              { value: this.financeData.otherExpense || 5000, name: '其他', itemStyle: { color: '#43e97b' } }
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
.card-header {
  font-weight: bold;
  font-size: 16px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  color: white;
}

.stat-card.income {
  background: linear-gradient(135deg, #66c6ff 0%, #4facfe 100%);
}

.stat-card.expense {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-card.profit {
  background: linear-gradient(135deg, #67c23a 0%, #95d475 100%);
}

.stat-card.order {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
}
</style>
