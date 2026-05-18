<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">用户增长分析</h2>
      <div>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 300px;"
          @change="handleDateChange"
        ></el-date-picker>
      </div>
    </div>
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ totalUsers }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;">
              <i class="el-icon-user-add"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ newUsers }}</div>
              <div class="stat-label">新增用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;">
              <i class="el-icon-s-custom"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ activeUsers }}</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;">
              <i class="el-icon-share"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ growthRate }}%</div>
              <div class="stat-label">增长率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card>
          <div slot="header">用户增长趋势</div>
          <div ref="lineChart" style="height: 500px;"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card>
          <div slot="header">用户角色分布</div>
          <div ref="pieChart" style="height: 500px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 20px;">
      <div slot="header">用户增长详情</div>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="date" label="日期" width="120"></el-table-column>
        <el-table-column prop="newUsers" label="新增用户" width="120">
          <template slot-scope="scope">
            <span style="color: #67C23A;">+{{ scope.row.newUsers }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="activeUsers" label="活跃用户" width="120">
          <template slot-scope="scope">
            <span style="color: #409EFF;">{{ scope.row.activeUsers }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalUsers" label="累计用户" width="120">
          <template slot-scope="scope">
            <span style="color: #E6A23C; font-weight: bold;">{{ scope.row.totalUsers }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="growthRate" label="增长率" width="120">
          <template slot-scope="scope">
            <el-tag :type="parseFloat(scope.row.growthRate) >= 0 ? 'success' : 'danger'" size="small">
              {{ scope.row.growthRate }}%
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserGrowth',
  data() {
    return {
      dateRange: [],
      totalUsers: 5680,
      newUsers: 256,
      activeUsers: 3420,
      growthRate: '12.5',
      tableData: []
    }
  },
  mounted() {
    this.fetchData()
    this.initLineChart()
    this.initPieChart()
  },
  methods: {
    fetchData() {
      const mockData = []
      let total = 5000
      for (let i = 1; i <= 30; i++) {
        const newUsers = Math.floor(Math.random() * 100 + 50)
        const activeUsers = Math.floor(Math.random() * 500 + 100)
        total += newUsers
        const growthRate = ((Math.random() * 10 + 5)).toFixed(1)
        mockData.push({
          date: '2024-01-' + String(i).padStart(2, '0'),
          newUsers: newUsers,
          activeUsers: activeUsers,
          totalUsers: total,
          growthRate: growthRate
        })
      }
      this.tableData = mockData
    },
    initLineChart() {
      const chart = this.$echarts.init(this.$refs.lineChart)
      const xAxisData = this.tableData.slice(0, 15).map(item => item.date.substring(5))
      const newUserData = this.tableData.slice(0, 15).map(item => item.newUsers)
      const activeUserData = this.tableData.slice(0, 15).map(item => item.activeUsers)
      
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['新增用户', '活跃用户']
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
          type: 'value'
        },
        series: [
          {
            name: '新增用户',
            type: 'line',
            smooth: true,
            data: newUserData,
            itemStyle: {
              color: '#67C23A'
            },
            lineStyle: {
              width: 3
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                  { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
                ]
              }
            }
          },
          {
            name: '活跃用户',
            type: 'line',
            smooth: true,
            data: activeUserData,
            itemStyle: {
              color: '#409EFF'
            },
            lineStyle: {
              width: 3
            }
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initPieChart() {
      const chart = this.$echarts.init(this.$refs.pieChart)
      const pieData = [
        { name: '买家', value: 3520 },
        { name: '卖家', value: 1250 },
        { name: '客服', value: 580 },
        { name: '管理员', value: 330 }
      ]
      
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'center'
        },
        series: [
          {
            name: '用户角色',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['60%', '50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {c}'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            data: pieData,
            color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    handleDateChange() {
      this.fetchData()
      this.initLineChart()
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