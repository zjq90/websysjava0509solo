<template>
  <div class="dashboard">
    <el-card class="filter-card">
      <el-form :inline="true" size="small">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="fetchData"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="聚合方式">
          <el-select v-model="aggregateType" placeholder="请选择" @change="fetchData">
            <el-option label="按小时" value="hour"></el-option>
            <el-option label="按天" value="day"></el-option>
            <el-option label="按周" value="week"></el-option>
            <el-option label="按月" value="month"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">刷新数据</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-user-solid stat-icon blue"></i>
            <div class="stat-content">
              <div class="stat-label">日活用户</div>
              <div class="stat-value">{{ userStats.dailyActiveUsers || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-user stat-icon green"></i>
            <div class="stat-content">
              <div class="stat-label">月活用户</div>
              <div class="stat-value">{{ userStats.monthlyActiveUsers || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-user-add stat-icon orange"></i>
            <div class="stat-content">
              <div class="stat-label">新增用户</div>
              <div class="stat-value">{{ userStats.newUsers || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-gamepad stat-icon purple"></i>
            <div class="stat-content">
              <div class="stat-label">游戏启动次数</div>
              <div class="stat-value">{{ gameStats.totalLaunches || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="stats-cards">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-time stat-icon cyan"></i>
            <div class="stat-content">
              <div class="stat-label">平均游玩时长(秒)</div>
              <div class="stat-value">{{ gameStats.averagePlayDuration?.toFixed(2) || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-goods stat-icon red"></i>
            <div class="stat-content">
              <div class="stat-label">广告总收入</div>
              <div class="stat-value">¥{{ revenueStats.totalAdRevenue?.toFixed(2) || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-item">
            <i class="el-icon-vip stat-icon gold"></i>
            <div class="stat-content">
              <div class="stat-label">会员收入</div>
              <div class="stat-value">¥{{ revenueStats.totalVipRevenue?.toFixed(2) || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card title="留存率">
          <div ref="retentionChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="收入趋势">
          <div ref="revenueChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card title="用户地域分布">
          <div ref="regionChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="游戏人均游玩次数TOP10">
          <el-table :data="topGames" size="small">
            <el-table-column prop="rank" label="排名" width="70"></el-table-column>
            <el-table-column prop="gameName" label="游戏名称"></el-table-column>
            <el-table-column prop="playCount" label="游玩次数"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

function formatDate(date) {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export default {
  name: 'Dashboard',
  data() {
    return {
      dateRange: [],
      aggregateType: 'day',
      userStats: {},
      gameStats: {},
      revenueStats: {},
      topGames: [],
      retentionChart: null,
      revenueChart: null,
      regionChart: null
    }
  },
  mounted() {
    this.fetchData()
  },
  beforeDestroy() {
    if (this.retentionChart) this.retentionChart.dispose()
    if (this.revenueChart) this.revenueChart.dispose()
    if (this.regionChart) this.regionChart.dispose()
  },
  methods: {
    async fetchData() {
      const params = {}
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = formatDate(this.dateRange[0])
        params.endDate = formatDate(this.dateRange[1])
      }

      try {
        const [userStats, gameStats, revenueStats] = await Promise.all([
          this.$api.getUserStats(params),
          this.$api.getGameStats(params),
          this.$api.getRevenueStats(params)
        ])
        this.userStats = userStats
        this.gameStats = gameStats
        this.revenueStats = revenueStats
        this.topGames = (gameStats.topGames || []).slice(0, 10).map((item, index) => ({
          ...item,
          rank: index + 1
        }))

        this.$nextTick(() => {
          this.renderRetentionChart()
          this.renderRevenueChart()
          this.renderRegionChart()
        })
      } catch (error) {
        console.error('获取数据失败:', error)
      }
    },
    renderRetentionChart() {
      if (!this.retentionChart) {
        this.retentionChart = echarts.init(this.$refs.retentionChart)
      }
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['次日留存', '7日留存', '30日留存']
        },
        yAxis: {
          type: 'value',
          max: 100,
          axisLabel: {
            formatter: '{value}%'
          }
        },
        series: [{
          type: 'bar',
          data: [
            this.userStats.retentionRateDay1 || 0,
            this.userStats.retentionRateDay7 || 0,
            this.userStats.retentionRateDay30 || 0
          ],
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.retentionChart.setOption(option)
    },
    renderRevenueChart() {
      if (!this.revenueChart) {
        this.revenueChart = echarts.init(this.$refs.revenueChart)
      }
      const trendData = this.revenueStats.revenueTrend || []
      const dates = [...new Set(trendData.map(item => item.date))]
      const adData = dates.map(date => {
        const item = trendData.find(t => t.date === date && t.type === 'AD')
        return item ? item.amount : 0
      })
      const vipData = dates.map(date => {
        const item = trendData.find(t => t.date === date && t.type === 'VIP')
        return item ? item.amount : 0
      })

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['广告收入', '会员收入']
        },
        xAxis: {
          type: 'category',
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
            name: '广告收入',
            type: 'line',
            data: adData,
            smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '会员收入',
            type: 'line',
            data: vipData,
            smooth: true,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      this.revenueChart.setOption(option)
    },
    renderRegionChart() {
      if (!this.regionChart) {
        this.regionChart = echarts.init(this.$refs.regionChart)
      }
      const regionData = this.userStats.regionDistribution || []
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          data: regionData.map(item => ({
            value: item.count,
            name: item.region
          }))
        }]
      }
      this.regionChart.setOption(option)
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
}

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
}

.stat-icon.blue { color: #409EFF; }
.stat-icon.green { color: #67C23A; }
.stat-icon.orange { color: #E6A23C; }
.stat-icon.purple { color: #909399; }
.stat-icon.cyan { color: #00BFFF; }
.stat-icon.red { color: #F56C6C; }
.stat-icon.gold { color: #FFD700; }

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.charts-row {
  margin-bottom: 20px;
}
</style>
