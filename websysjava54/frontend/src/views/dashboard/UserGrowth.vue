<template>
  <div class="user-growth">
    <h2>用户增长分析</h2>
    
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>数据概览</span>
        <el-select v-model="selectedDays" @change="loadGrowthData" style="float: right; width: 120px;">
          <el-option label="最近7天" :value="7"></el-option>
          <el-option label="最近15天" :value="15"></el-option>
          <el-option label="最近30天" :value="30"></el-option>
        </el-select>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ growthData.totalUsers || 0 }}</div>
            <div class="stat-label">总用户数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ growthData.newUsersToday || 0 }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value" :class="{ positive: growthData.growthRate >= 0, negative: growthData.growthRate < 0 }">
              {{ growthData.growthRate || 0 }}%
            </div>
            <div class="stat-label">增长率</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ growthData.expertCount || 0 }}</div>
            <div class="stat-label">专家用户</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>每日新增用户</span>
          </div>
          <div ref="dailyChart" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户累计增长</span>
          </div>
          <div ref="cumulativeChart" style="height: 350px; width: 100%;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>用户认证分布</span>
          </div>
          <div ref="verifyChart" style="height: 300px; width: 100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span>月度用户增长</span>
          </div>
          <div ref="monthlyChart" style="height: 300px; width: 100%;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'UserGrowth',
  data() {
    return {
      selectedDays: 7,
      growthData: {},
      monthlyData: {}
    }
  },
  mounted() {
    this.loadGrowthData()
    this.loadMonthlyData()
  },
  methods: {
    loadGrowthData() {
      this.$http.get(`/dashboard/user/growth?days=${this.selectedDays}`).then(res => {
        if (res.data.code === 200) {
          this.growthData = res.data.data
          this.$nextTick(() => {
            this.initDailyChart()
            this.initCumulativeChart()
            this.initVerifyChart()
          })
        }
      })
    },
    loadMonthlyData() {
      this.$http.get('/dashboard/user/monthly-growth').then(res => {
        if (res.data.code === 200) {
          this.monthlyData = res.data.data
          this.$nextTick(() => {
            this.initMonthlyChart()
          })
        }
      })
    },
    initDailyChart() {
      const chart = echarts.init(this.$refs.dailyChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>新增用户: {c}'
        },
        xAxis: {
          type: 'category',
          data: this.growthData.dateLabels || [],
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '新增用户数'
        },
        series: [{
          data: this.growthData.dailyNewUsers || [],
          type: 'bar',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          },
          emphasis: {
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#2378f7' },
                { offset: 0.7, color: '#2378f7' },
                { offset: 1, color: '#83bff6' }
              ])
            }
          }
        }]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initCumulativeChart() {
      const chart = echarts.init(this.$refs.cumulativeChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>累计用户: {c}'
        },
        xAxis: {
          type: 'category',
          data: this.growthData.dateLabels || [],
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '累计用户数'
        },
        series: [{
          data: this.growthData.cumulativeUsers || [],
          type: 'line',
          smooth: true,
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(128, 255, 165, 0.8)' },
              { offset: 1, color: 'rgba(1, 191, 236, 0.8)' }
            ])
          },
          lineStyle: {
            color: '#67C23A',
            width: 3
          },
          itemStyle: {
            color: '#67C23A'
          }
        }]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initVerifyChart() {
      const chart = echarts.init(this.$refs.verifyChart)
      const total = this.growthData.totalUsers || 0
      const verified = this.growthData.verifiedCount || 0
      const expert = this.growthData.expertCount || 0
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
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
            { value: expert, name: '专家用户', itemStyle: { color: '#E6A23C' } },
            { value: verified - expert, name: '已认证用户', itemStyle: { color: '#67C23A' } },
            { value: total - verified, name: '未认证用户', itemStyle: { color: '#F56C6C' } }
          ]
        }]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initMonthlyChart() {
      const chart = echarts.init(this.$refs.monthlyChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '{b}<br/>新增用户: {c}'
        },
        xAxis: {
          type: 'category',
          data: this.monthlyData.months || [],
          axisLabel: {
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          name: '新增用户数'
        },
        series: [{
          data: this.monthlyData.counts || [],
          type: 'line',
          smooth: true,
          lineStyle: {
            color: '#909399',
            width: 3
          },
          itemStyle: {
            color: '#909399'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(144, 147, 153, 0.6)' },
              { offset: 1, color: 'rgba(144, 147, 153, 0.1)' }
            ])
          }
        }]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    }
  }
}
</script>

<style scoped>
.user-growth {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
.stat-card {
  text-align: center;
  padding: 20px 0;
}
.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}
.stat-value.positive {
  color: #67C23A;
}
.stat-value.negative {
  color: #F56C6C;
}
.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
