<template>
  <div class="employee-performance">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card>
          <div slot="header" style="display: flex; justify-content: space-between; align-items: center">
            <span>员工绩效排行</span>
            <el-select v-model="filterPosition" placeholder="选择岗位" style="width: 150px" @change="fetchData">
              <el-option label="全部" value=""></el-option>
              <el-option label="摄影师" value="PHOTOGRAPHER"></el-option>
              <el-option label="化妆师" value="MAKEUP_ARTIST"></el-option>
              <el-option label="选片师" value="PHOTO_SELECTOR"></el-option>
              <el-option label="修图师" value="PHOTO_EDITOR"></el-option>
              <el-option label="销售员" value="SALES"></el-option>
            </el-select>
          </div>
          <el-table :data="performanceList" style="width: 100%" v-loading="loading">
            <el-table-column label="排名" width="80" align="center">
              <template slot-scope="scope">
                <span v-if="scope.$index === 0" class="rank-badge rank-1">1</span>
                <span v-else-if="scope.$index === 1" class="rank-badge rank-2">2</span>
                <span v-else-if="scope.$index === 2" class="rank-badge rank-3">3</span>
                <span v-else class="rank-badge">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="employeeName" label="姓名" width="120"></el-table-column>
            <el-table-column prop="position" label="岗位" width="120">
              <template slot-scope="scope">
                <el-tag size="small">{{ getPositionName(scope.row.position) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="orderCount" label="接单数量" width="120" align="center">
              <template slot-scope="scope">
                {{ scope.row.orderCount || scope.row.editPhotoCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="avgRating" label="客户评分" width="120" align="center">
              <template slot-scope="scope">
                <el-rate v-model="scope.row.avgRating" disabled show-score text-color="#ff9900" score-template="{value}"></el-rate>
              </template>
            </el-table-column>
            <el-table-column prop="totalRevenue" label="成单金额" width="150" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.totalRevenue" style="color: #f56c6c; font-weight: bold">¥{{ formatNumber(scope.row.totalRevenue) }}</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="reworkRate" label="返修率" width="120" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.reworkRate !== undefined" style="color: #e6a23c">{{ scope.row.reworkRate }}%</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>成单金额排行</span>
          </div>
          <div ref="revenueChart" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>客户评分排行</span>
          </div>
          <div ref="ratingChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'EmployeePerformance',
  data() {
    return {
      loading: false,
      filterPosition: '',
      performanceList: []
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {}
        if (this.filterPosition) {
          params.position = this.filterPosition
        }
        const res = await this.$http.get('/dashboard/employee-performance', { params })
        if (res.data.success) {
          this.performanceList = res.data.data
          this.initCharts()
        }
      } catch (error) {
        console.error('获取数据失败', error)
      } finally {
        this.loading = false
      }
    },
    getPositionName(position) {
      const map = {
        'PHOTOGRAPHER': '摄影师',
        'MAKEUP_ARTIST': '化妆师',
        'PHOTO_SELECTOR': '选片师',
        'PHOTO_EDITOR': '修图师',
        'SALES': '销售员',
        'MANAGER': '管理人员'
      }
      return map[position] || position
    },
    initCharts() {
      this.initRevenueChart()
      this.initRatingChart()
    },
    initRevenueChart() {
      const chart = echarts.init(this.$refs.revenueChart)
      const topData = this.performanceList.slice(0, 8)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          axisLabel: {
            formatter: '¥{value}'
          }
        },
        yAxis: {
          type: 'category',
          data: topData.map(item => item.employeeName).reverse()
        },
        series: [
          {
            name: '成单金额',
            type: 'bar',
            data: topData.map(item => item.totalRevenue || 0).reverse(),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ])
            },
            barWidth: '50%'
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initRatingChart() {
      const chart = echarts.init(this.$refs.ratingChart)
      const topData = this.performanceList.slice(0, 8)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          max: 5,
          axisLabel: {
            formatter: '{value}分'
          }
        },
        yAxis: {
          type: 'category',
          data: topData.map(item => item.employeeName).reverse()
        },
        series: [
          {
            name: '客户评分',
            type: 'bar',
            data: topData.map(item => item.avgRating || 0).reverse(),
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: '#f093fb' },
                { offset: 1, color: '#f5576c' }
              ])
            },
            barWidth: '50%'
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

.rank-badge {
  display: inline-block;
  width: 28px;
  height: 28px;
  line-height: 28px;
  text-align: center;
  border-radius: 50%;
  background: #f0f0f0;
  font-weight: bold;
  font-size: 14px;
}

.rank-1 {
  background: linear-gradient(135deg, #ffd700 0%, #ffaa00 100%);
  color: white;
}

.rank-2 {
  background: linear-gradient(135deg, #c0c0c0 0%, #a0a0a0 100%);
  color: white;
}

.rank-3 {
  background: linear-gradient(135deg, #cd7f32 0%, #a0522d 100%);
  color: white;
}
</style>
