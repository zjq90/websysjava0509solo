<template>
  <div class="home">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF">
              <i class="el-icon-s-management"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.total || 0 }}</div>
              <div class="stat-label">文物总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A">
              <i class="el-icon-s-data"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.masterDataCount || 0 }}</div>
              <div class="stat-label">主数据量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C">
              <i class="el-icon-s-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.highQualityCount || 0 }}</div>
              <div class="stat-label">高质量数据</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #F56C6C">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">1</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>文物朝代分布</span>
          </div>
          <div ref="dynastyChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>文物类别分布</span>
          </div>
          <div ref="categoryChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="card-header">
            <span>数据质量评分分布</span>
          </div>
          <div ref="qualityChart" style="height: 300px"></div>
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
      statistics: {},
      heritageList: []
    }
  },
  mounted() {
    this.loadStatistics()
    this.loadHeritages()
  },
  methods: {
    async loadStatistics() {
      try {
        const response = await this.$http.get('/heritages/statistics')
        if (response.data.code === 200) {
          this.statistics = response.data.data
        }
      } catch (error) {
        console.error('加载统计数据失败', error)
      }
    },
    async loadHeritages() {
      try {
        const response = await this.$http.get('/heritages', { params: { page: 0, size: 100 } })
        if (response.data.code === 200) {
          this.heritageList = response.data.data.content
          this.initCharts()
        }
      } catch (error) {
        console.error('加载文物数据失败', error)
      }
    },
    initCharts() {
      this.initDynastyChart()
      this.initCategoryChart()
      this.initQualityChart()
    },
    initDynastyChart() {
      const dynastyMap = {}
      this.heritageList.forEach(h => {
        const dynasty = h.dynasty || '未知'
        dynastyMap[dynasty] = (dynastyMap[dynasty] || 0) + 1
      })

      const chart = echarts.init(this.$refs.dynastyChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: '0%' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
          labelLine: { show: false },
          data: Object.keys(dynastyMap).map(key => ({ value: dynastyMap[key], name: key }))
        }]
      })
    },
    initCategoryChart() {
      const categoryMap = {}
      this.heritageList.forEach(h => {
        const category = h.category || '未知'
        categoryMap[category] = (categoryMap[category] || 0) + 1
      })

      const chart = echarts.init(this.$refs.categoryChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: Object.keys(categoryMap) },
        yAxis: { type: 'value' },
        series: [{
          type: 'bar',
          data: Object.values(categoryMap),
          itemStyle: { color: '#409EFF' }
        }]
      })
    },
    initQualityChart() {
      const qualityRanges = { '0-59': 0, '60-79': 0, '80-89': 0, '90-100': 0 }
      this.heritageList.forEach(h => {
        const score = h.qualityScore || 0
        if (score < 60) qualityRanges['0-59']++
        else if (score < 80) qualityRanges['60-79']++
        else if (score < 90) qualityRanges['80-89']++
        else qualityRanges['90-100']++
      })

      const chart = echarts.init(this.$refs.qualityChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['数据质量评分'] },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: Object.keys(qualityRanges) },
        yAxis: { type: 'value' },
        series: [{
          name: '数据质量评分',
          type: 'line',
          stack: 'Total',
          data: Object.values(qualityRanges),
          areaStyle: { color: '#67C23A' },
          lineStyle: { color: '#67C23A' }
        }]
      })
    }
  }
}
</script>

<style scoped>
.stat-card {
  margin-bottom: 20px;
}
.stat-content {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 28px;
  color: white;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}
.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}
.card-header {
  font-weight: bold;
  font-size: 16px;
}
</style>
