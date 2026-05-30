<template>
  <div class="game-analytics">
    <el-card class="filter-card">
      <el-form :inline="true" size="small">
        <el-form-item label="搜索游戏">
          <el-input
            v-model="searchKeyword"
            placeholder="请输入游戏名称"
            style="width: 200px"
            @input="searchGames"
          >
            <el-button slot="append" icon="el-icon-search" @click="searchGames"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="选择游戏">
          <el-select
            v-model="selectedGameId"
            placeholder="请选择游戏"
            style="width: 200px"
            @change="fetchGameDetail"
          >
            <el-option
              v-for="game in gameList"
              :key="game.id"
              :label="game.name"
              :value="game.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="fetchGameDetail"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="showCompareDialog">对比游戏</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div v-if="gameDetail" class="detail-section">
      <el-card :title="gameDetail.gameName">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-stat">
              <div class="stat-label">平均游玩时长</div>
              <div class="stat-value">{{ (gameDetail.averagePlayDuration || 0).toFixed(2) }} 秒</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-stat">
              <div class="stat-label">广告点击率</div>
              <div class="stat-value">{{ ((gameDetail.adClickRate || 0) * 100).toFixed(2) }}%</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-stat">
              <div class="stat-label">内购转化率</div>
              <div class="stat-value">{{ ((gameDetail.purchaseConversionRate || 0) * 100).toFixed(2) }}%</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-row :gutter="20" class="charts-row">
        <el-col :span="12">
          <el-card title="启动次数趋势">
            <div ref="launchTrendChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card title="留存情况">
            <div ref="retentionChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="charts-row">
        <el-col :span="12">
          <el-card title="用户来源渠道">
            <div ref="channelChart" style="height: 300px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card title="付费转化数据">
            <div ref="conversionChart" style="height: 300px"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-dialog title="游戏对比分析" :visible.sync="compareDialogVisible" width="90%">
      <el-card>
        <el-form :inline="true" size="small">
          <el-form-item label="选择对比游戏">
            <el-select
              v-model="compareGameIds"
              multiple
              placeholder="最多选择5款游戏"
              style="width: 400px"
            >
              <el-option
                v-for="game in gameList"
                :key="game.id"
                :label="game.name"
                :value="game.id"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchCompareData" :disabled="compareGameIds.length === 0 || compareGameIds.length > 5">
              开始对比
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <div v-if="compareData.length > 0" class="compare-section">
        <el-card title="对比数据" style="margin-top: 20px">
          <el-table :data="compareData" border>
            <el-table-column prop="gameName" label="游戏名称" width="120"></el-table-column>
            <el-table-column label="平均游玩时长(秒)">
              <template slot-scope="scope">{{ (scope.row.averagePlayDuration || 0).toFixed(2) }}</template>
            </el-table-column>
            <el-table-column label="广告点击率(%)">
              <template slot-scope="scope">{{ ((scope.row.adClickRate || 0) * 100).toFixed(2) }}</template>
            </el-table-column>
            <el-table-column label="内购转化率(%)">
              <template slot-scope="scope">{{ ((scope.row.purchaseConversionRate || 0) * 100).toFixed(2) }}</template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card title="趋势对比" style="margin-top: 20px">
          <div ref="compareTrendChart" style="height: 400px"></div>
        </el-card>
      </div>
    </el-dialog>
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
  name: 'GameAnalytics',
  data() {
    return {
      searchKeyword: '',
      gameList: [],
      selectedGameId: null,
      dateRange: [],
      gameDetail: null,
      compareDialogVisible: false,
      compareGameIds: [],
      compareData: [],
      launchTrendChart: null,
      retentionChart: null,
      channelChart: null,
      conversionChart: null,
      compareTrendChart: null
    }
  },
  mounted() {
    this.fetchGameList()
  },
  beforeDestroy() {
    this.disposeCharts()
  },
  methods: {
    disposeCharts() {
      if (this.launchTrendChart) this.launchTrendChart.dispose()
      if (this.retentionChart) this.retentionChart.dispose()
      if (this.channelChart) this.channelChart.dispose()
      if (this.conversionChart) this.conversionChart.dispose()
      if (this.compareTrendChart) this.compareTrendChart.dispose()
    },
    async fetchGameList() {
      try {
        this.gameList = await this.$api.searchGames('')
        if (this.gameList.length > 0) {
          this.selectedGameId = this.gameList[0].id
          this.fetchGameDetail()
        }
      } catch (error) {
        console.error('获取游戏列表失败:', error)
      }
    },
    async searchGames() {
      try {
        this.gameList = await this.$api.searchGames(this.searchKeyword)
      } catch (error) {
        console.error('搜索游戏失败:', error)
      }
    },
    async fetchGameDetail() {
      if (!this.selectedGameId) return

      const params = {}
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = formatDate(this.dateRange[0])
        params.endDate = formatDate(this.dateRange[1])
      }

      try {
        this.gameDetail = await this.$api.getGameDetailStats(this.selectedGameId, params)
        this.$nextTick(() => {
          this.renderDetailCharts()
        })
      } catch (error) {
        console.error('获取游戏详情失败:', error)
      }
    },
    renderDetailCharts() {
      this.renderLaunchTrendChart()
      this.renderRetentionChart()
      this.renderChannelChart()
      this.renderConversionChart()
    },
    renderLaunchTrendChart() {
      if (!this.launchTrendChart) {
        this.launchTrendChart = echarts.init(this.$refs.launchTrendChart)
      }
      const launchTrend = this.gameDetail.launchTrend || []
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: launchTrend.map(item => item.date)
        },
        yAxis: { type: 'value' },
        series: [{
          type: 'line',
          data: launchTrend.map(item => item.count),
          smooth: true,
          itemStyle: { color: '#409EFF' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
            ])
          }
        }]
      }
      this.launchTrendChart.setOption(option)
    },
    renderRetentionChart() {
      if (!this.retentionChart) {
        this.retentionChart = echarts.init(this.$refs.retentionChart)
      }
      const retention = this.gameDetail.retentionRates || {}
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['次日留存', '7日留存', '30日留存']
        },
        yAxis: {
          type: 'value',
          max: 100,
          axisLabel: { formatter: '{value}%' }
        },
        series: [{
          type: 'bar',
          data: [
            retention.day1 || 0,
            retention.day7 || 0,
            retention.day30 || 0
          ],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#67C23A' },
              { offset: 1, color: '#95D475' }
            ])
          }
        }]
      }
      this.retentionChart.setOption(option)
    },
    renderChannelChart() {
      if (!this.channelChart) {
        this.channelChart = echarts.init(this.$refs.channelChart)
      }
      const channels = this.gameDetail.sourceChannelDistribution || {}
      const channelMap = {
        'direct': '直接访问',
        'search': '搜索',
        'recommend': '推荐位',
        'share': '外部分享'
      }
      const data = Object.entries(channels).map(([key, value]) => ({
        name: channelMap[key] || key,
        value: value
      }))
      const option = {
        tooltip: { trigger: 'item' },
        legend: { bottom: '5%' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '45%'],
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.channelChart.setOption(option)
    },
    renderConversionChart() {
      if (!this.conversionChart) {
        this.conversionChart = echarts.init(this.$refs.conversionChart)
      }
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['广告点击率', '内购转化率']
        },
        yAxis: {
          type: 'value',
          max: 100,
          axisLabel: { formatter: '{value}%' }
        },
        series: [{
          type: 'bar',
          data: [
            (this.gameDetail.adClickRate || 0) * 100,
            (this.gameDetail.purchaseConversionRate || 0) * 100
          ],
          itemStyle: {
            color: function(params) {
              const colors = ['#E6A23C', '#F56C6C']
              return colors[params.dataIndex]
            }
          }
        }]
      }
      this.conversionChart.setOption(option)
    },
    showCompareDialog() {
      this.compareDialogVisible = true
      this.compareGameIds = []
      this.compareData = []
    },
    async fetchCompareData() {
      if (this.compareGameIds.length === 0 || this.compareGameIds.length > 5) return

      const params = {}
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = formatDate(this.dateRange[0])
        params.endDate = formatDate(this.dateRange[1])
      }

      try {
        this.compareData = await this.$api.compareGames(this.compareGameIds, params)
        this.$nextTick(() => {
          this.renderCompareTrendChart()
        })
      } catch (error) {
        console.error('获取对比数据失败:', error)
      }
    },
    renderCompareTrendChart() {
      if (!this.compareTrendChart) {
        this.compareTrendChart = echarts.init(this.$refs.compareTrendChart)
      }
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      const series = this.compareData.map((game, index) => ({
        name: game.gameName,
        type: 'line',
        data: (game.launchTrend || []).map(item => item.count),
        smooth: true,
        itemStyle: { color: colors[index] }
      }))
      const allDates = this.compareData.flatMap(game => 
        (game.launchTrend || []).map(item => item.date)
      )
      const dates = [...new Set(allDates)].sort()

      const option = {
        tooltip: { trigger: 'axis' },
        legend: { data: this.compareData.map(g => g.gameName) },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: { type: 'value', name: '启动次数' },
        series: series
      }
      this.compareTrendChart.setOption(option)
    }
  }
}
</script>

<style scoped>
.game-analytics {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-stat {
  text-align: center;
  padding: 20px;
}

.detail-stat .stat-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.detail-stat .stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.charts-row {
  margin-top: 20px;
}

.compare-section {
  margin-top: 20px;
}
</style>
