<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">商品销量排行</h2>
      <div>
        <el-select v-model="topN" @change="handleTopNChange" style="width: 120px;">
          <el-option label="Top 10" :value="10"></el-option>
          <el-option label="Top 20" :value="20"></el-option>
          <el-option label="Top 50" :value="50"></el-option>
        </el-select>
      </div>
    </div>
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card>
          <div ref="barChart" style="height: 600px;"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card>
          <div ref="pieChart" style="height: 600px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span>销量排行榜详情</span>
      </div>
      <el-table
        :data="tableData"
        style="width: 100%"
        border
      >
        <el-table-column prop="rank" label="排名" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.rank <= 3" :type="getRankType(scope.row.rank)" size="small">
              {{ scope.row.rank }}
            </el-tag>
            <span v-else>{{ scope.row.rank }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称" min-width="200"></el-table-column>
        <el-table-column prop="category" label="分类" width="100"></el-table-column>
        <el-table-column prop="price" label="单价" width="100">
          <template slot-scope="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="sales" label="销量" width="120" sortable>
          <template slot-scope="scope">
            <span style="color: #F56C6C; font-weight: bold;">{{ scope.row.sales }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="销售额" width="120" sortable>
          <template slot-scope="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100">
          <template slot-scope="scope">
            <span :class="scope.row.stock < 10 ? 'text-danger' : ''">{{ scope.row.stock }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ProductRanking',
  data() {
    return {
      topN: 10,
      tableData: []
    }
  },
  mounted() {
    this.fetchData()
    this.initBarChart()
    this.initPieChart()
  },
  methods: {
    fetchData() {
      const mockData = []
      const categories = ['电子产品', '服装', '家居', '图书', '运动']
      for (let i = 1; i <= this.topN; i++) {
        const price = (Math.random() * 500 + 50).toFixed(2)
        const sales = Math.floor(Math.random() * 500 + 100)
        mockData.push({
          rank: i,
          name: '热销商品' + i,
          category: categories[i % categories.length],
          price: price,
          sales: sales,
          totalAmount: (price * sales).toFixed(2),
          stock: Math.floor(Math.random() * 100 + 10)
        })
      }
      this.tableData = mockData.sort((a, b) => b.sales - a.sales)
      this.tableData.forEach((item, index) => {
        item.rank = index + 1
      })
    },
    getRankType(rank) {
      const types = ['danger', 'warning', 'success']
      return types[rank - 1] || 'info'
    },
    initBarChart() {
      const chart = this.$echarts.init(this.$refs.barChart)
      const xAxisData = this.tableData.slice(0, 10).map(item => item.name.substring(0, 8))
      const seriesData = this.tableData.slice(0, 10).map(item => item.sales)
      
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
          type: 'category',
          data: xAxisData,
          axisLabel: {
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          name: '销量'
        },
        series: [
          {
            name: '销量',
            type: 'bar',
            data: seriesData,
            itemStyle: {
              color: function(params) {
                const colors = ['#F56C6C', '#E6A23C', '#67C23A', '#409EFF', '#909399']
                return colors[params.dataIndex % colors.length]
              }
            },
            barWidth: '60%'
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    initPieChart() {
      const chart = this.$echarts.init(this.$refs.pieChart)
      const categoryData = {}
      this.tableData.forEach(item => {
        if (!categoryData[item.category]) {
          categoryData[item.category] = 0
        }
        categoryData[item.category] += parseInt(item.sales)
      })
      
      const pieData = Object.keys(categoryData).map(key => ({
        name: key,
        value: categoryData[key]
      }))
      
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
            name: '分类销量',
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
            data: pieData,
            color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
          }
        ]
      }
      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    handleTopNChange() {
      this.fetchData()
      this.initBarChart()
      this.initPieChart()
    }
  }
}
</script>

<style scoped>
.text-danger {
  color: #F56C6C;
}
</style>