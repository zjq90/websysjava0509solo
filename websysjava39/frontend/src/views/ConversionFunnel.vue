<template>
  <div class="conversion-funnel">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <div slot="header" class="card-header">
            <span>客户转化漏斗</span>
          </div>
          <div ref="funnelChart" style="height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span>转化数据</span>
          </div>
          <div class="funnel-stats">
            <div class="funnel-stat-item">
              <div class="funnel-label">咨询量</div>
              <div class="funnel-value">{{ funnelData.consultingCount || 0 }}</div>
            </div>
            <div class="funnel-stat-item">
              <div class="funnel-label">下单量</div>
              <div class="funnel-value">{{ funnelData.orderedCount || 0 }}</div>
              <div class="funnel-rate">{{ funnelData.consultToOrderRate || 0 }}%</div>
            </div>
            <div class="funnel-stat-item">
              <div class="funnel-label">拍摄完成</div>
              <div class="funnel-value">{{ funnelData.shootCompletedCount || 0 }}</div>
              <div class="funnel-rate">{{ funnelData.orderToShootRate || 0 }}%</div>
            </div>
            <div class="funnel-stat-item">
              <div class="funnel-label">已交付</div>
              <div class="funnel-value">{{ funnelData.deliveredCount || 0 }}</div>
              <div class="funnel-rate">{{ funnelData.shootToDeliverRate || 0 }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>老客户复购率</span>
          </div>
          <div ref="repeatChart" style="height: 250px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>转介绍率</span>
          </div>
          <div ref="referralChart" style="height: 250px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ConversionFunnel',
  data() {
    return {
      funnelData: {}
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        const res = await this.$http.get('/dashboard/conversion-funnel')
        if (res.data.success) {
          this.funnelData = res.data.data
          this.initCharts()
        }
      } catch (error) {
        console.error('获取数据失败', error)
      }
    },
    initCharts() {
      this.initFunnelChart()
      this.initRepeatChart()
      this.initReferralChart()
    },
    initFunnelChart() {
      const chart = echarts.init(this.$refs.funnelChart)
      const data = [
        { value: this.funnelData.consultingCount || 1000, name: '咨询' },
        { value: this.funnelData.orderedCount || 600, name: '下单' },
        { value: this.funnelData.shootCompletedCount || 450, name: '拍摄完成' },
        { value: this.funnelData.deliveredCount || 400, name: '交付' }
      ]

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}'
        },
        legend: {
          data: ['咨询', '下单', '拍摄完成', '交付']
        },
        series: [
          {
            name: '转化漏斗',
            type: 'funnel',
            left: '10%',
            top: 60,
            bottom: 60,
            width: '80%',
            min: 0,
            max: 1000,
            minSize: '0%',
            maxSize: '100%',
            sort: 'descending',
            gap: 2,
            label: {
              show: true,
              position: 'inside',
              formatter: '{b}: {c}'
            },
            labelLine: {
              length: 10,
              lineStyle: {
                width: 1,
                type: 'solid'
              }
            },
            itemStyle: {
              borderColor: '#fff',
              borderWidth: 1
            },
            emphasis: {
              label: {
                fontSize: 16
              }
            },
            data: data
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initRepeatChart() {
      const chart = echarts.init(this.$refs.repeatChart)
      const option = {
        tooltip: {
          formatter: '{a} <br/>{b}: {c}%'
        },
        series: [
          {
            name: '复购率',
            type: 'gauge',
            radius: '90%',
            startAngle: 180,
            endAngle: 0,
            min: 0,
            max: 100,
            splitNumber: 10,
            itemStyle: {
              color: '#667eea'
            },
            progress: {
              show: true,
              roundCap: true,
              width: 18
            },
            pointer: {
              show: false
            },
            axisLine: {
              roundCap: true,
              lineStyle: {
                width: 18,
                color: [[1, '#e5e7eb']]
              }
            },
            axisTick: {
              show: false
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              show: false
            },
            anchor: {
              show: false
            },
            title: {
              show: false
            },
            detail: {
              valueAnimation: true,
              fontSize: 30,
              offsetCenter: [0, '-10%'],
              formatter: '{value}%',
              color: '#333'
            },
            data: [{ value: this.funnelData.repeatPurchaseRate || 25 }]
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initReferralChart() {
      const chart = echarts.init(this.$refs.referralChart)
      const option = {
        tooltip: {
          formatter: '{a} <br/>{b}: {c}%'
        },
        series: [
          {
            name: '转介绍率',
            type: 'gauge',
            radius: '90%',
            startAngle: 180,
            endAngle: 0,
            min: 0,
            max: 100,
            splitNumber: 10,
            itemStyle: {
              color: '#67c23a'
            },
            progress: {
              show: true,
              roundCap: true,
              width: 18
            },
            pointer: {
              show: false
            },
            axisLine: {
              roundCap: true,
              lineStyle: {
                width: 18,
                color: [[1, '#e5e7eb']]
              }
            },
            axisTick: {
              show: false
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              show: false
            },
            anchor: {
              show: false
            },
            title: {
              show: false
            },
            detail: {
              valueAnimation: true,
              fontSize: 30,
              offsetCenter: [0, '-10%'],
              formatter: '{value}%',
              color: '#333'
            },
            data: [{ value: this.funnelData.referralRate || 18 }]
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    }
  }
}
</script>

<style scoped>
.card-header {
  font-weight: bold;
  font-size: 16px;
}

.funnel-stats {
  padding: 20px 0;
}

.funnel-stat-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.funnel-stat-item:last-child {
  border-bottom: none;
}

.funnel-label {
  font-size: 14px;
  color: #666;
}

.funnel-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.funnel-rate {
  font-size: 14px;
  color: #67c23a;
  background: #f0f9eb;
  padding: 2px 8px;
  border-radius: 10px;
}
</style>
