<template>
  <div class="heatmap">
    <h2>文物分布热力图</h2>
    <el-card shadow="hover" style="margin-bottom: 20px">
      <div ref="chart" style="height: 500px; width: 100%"></div>
    </el-card>

    <el-card shadow="hover">
      <div slot="header" class="clearfix">
        <span>省份文物列表</span>
        <el-select v-model="selectedProvince" placeholder="选择省份" @change="loadHeritageByProvince" style="float: right">
          <el-option v-for="item in provinces" :key="item" :label="item" :value="item"></el-option>
        </el-select>
      </div>
      <el-table :data="heritageList" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="文物名称" width="180"></el-table-column>
        <el-table-column prop="dynasty" label="朝代" width="120"></el-table-column>
        <el-table-column prop="material" label="材质" width="120"></el-table-column>
        <el-table-column prop="estimatedValue" label="预估价值" width="150"></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskType(scope.row.riskLevel)">{{ getRiskText(scope.row.riskLevel) }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Heatmap',
  data() {
    return {
      chartData: [],
      provinces: [],
      selectedProvince: '',
      heritageList: []
    }
  },
  mounted() {
    this.loadHeatmapData()
  },
  methods: {
    loadHeatmapData() {
      this.$http.get('/dashboard/heritage/province').then(res => {
        if (res.data.code === 200) {
          const data = res.data.data
          this.provinces = Object.keys(data)
          this.chartData = Object.entries(data).map(([name, value]) => ({ name, value }))
          this.initChart()
        }
      })
    },
    initChart() {
      const chart = echarts.init(this.$refs.chart)
      const option = {
        title: {
          text: '全国文物分布热力图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} 件'
        },
        visualMap: {
          min: 0,
          max: Math.max(...this.chartData.map(d => d.value)),
          left: 'left',
          top: 'bottom',
          text: ['多', '少'],
          calculable: true,
          inRange: {
            color: ['#e0f3f8', '#abd9e9', '#74add1', '#4575b4', '#313695']
          }
        },
        xAxis: {
          type: 'category',
          data: this.chartData.map(d => d.name),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '文物数量'
        },
        series: [
          {
            name: '文物数量',
            type: 'bar',
            data: this.chartData.map(d => d.value),
            itemStyle: {
              color: function(params) {
                const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc']
                return colors[params.dataIndex % colors.length]
              }
            }
          }
        ]
      }
      chart.setOption(option)
      chart.on('click', (params) => {
        this.selectedProvince = params.name
        this.loadHeritageByProvince()
      })
    },
    loadHeritageByProvince() {
      if (!this.selectedProvince) return
      this.$http.get(`/dashboard/heritage/province/${this.selectedProvince}`).then(res => {
        if (res.data.code === 200) {
          this.heritageList = res.data.data
        }
      })
    },
    getRiskType(level) {
      const map = { 'HIGH': 'danger', 'MEDIUM': 'warning', 'LOW': 'success' }
      return map[level] || 'info'
    },
    getRiskText(level) {
      const map = { 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return map[level] || level
    }
  }
}
</script>

<style scoped>
.heatmap {
  padding: 20px;
}
h2 {
  margin-bottom: 20px;
}
</style>
