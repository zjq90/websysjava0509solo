<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header">
            <el-radio-group v-model="timeType" size="small">
              <el-radio-button label="day">今日</el-radio-button>
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
            </el-radio-group>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-box" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                <div class="stat-label">销售总额</div>
                <div class="stat-value">¥{{ salesReport.totalSales || '12,580.00' }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-box" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                <div class="stat-label">订单总数</div>
                <div class="stat-value">{{ salesReport.orderCount || 86 }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-box" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                <div class="stat-label">客单价</div>
                <div class="stat-value">¥{{ salesReport.avgOrderValue || '146.28' }}</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-box" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                <div class="stat-label">新增客户</div>
                <div class="stat-value">{{ newCustomers || 12 }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>销售趋势</span>
            <el-radio-group v-model="trendType" size="small">
              <el-radio-button label="day">按日</el-radio-button>
              <el-radio-button label="week">按周</el-radio-button>
              <el-radio-button label="month">按月</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="salesTrendChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>商品分类销售占比</span>
          </div>
          <div ref="categoryChart" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>热销商品TOP10</span>
          </div>
          <el-table :data="hotProducts" size="small">
            <el-table-column prop="rank" label="排名" width="70" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.$index < 3" type="danger" size="mini">{{ scope.$index + 1 }}</el-tag>
                <span v-else>{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="productName" label="商品名称"></el-table-column>
            <el-table-column prop="totalQuantity" label="销量" width="100" align="center"></el-table-column>
            <el-table-column prop="totalAmount" label="销售额" width="120" align="center">
              <template slot-scope="scope">¥{{ scope.row.totalAmount }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>客户行为分析</span>
          </div>
          <div ref="behaviorChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Analytics',
  data() {
    return {
      timeType: 'day',
      trendType: 'day',
      salesReport: {
        totalSales: '12,580.00',
        orderCount: 86,
        avgOrderValue: '146.28'
      },
      newCustomers: 12,
      hotProducts: [
        { productName: '红玫瑰11支', totalQuantity: 128, totalAmount: '12,672.00' },
        { productName: '粉玫瑰19支', totalQuantity: 96, totalAmount: '15,264.00' },
        { productName: '向日葵花束', totalQuantity: 85, totalAmount: '7,565.00' },
        { productName: '粉色康乃馨', totalQuantity: 72, totalAmount: '5,688.00' },
        { productName: '多头百合', totalQuantity: 65, totalAmount: '8,385.00' },
        { productName: '满天星干花', totalQuantity: 58, totalAmount: '2,842.00' },
        { productName: '紫色郁金香', totalQuantity: 45, totalAmount: '7,605.00' },
        { productName: '白玫瑰99支', totalQuantity: 32, totalAmount: '19,168.00' }
      ]
    }
  },
  mounted() {
    this.loadData()
  },
  watch: {
    timeType() {
      this.loadData()
    },
    trendType() {
      this.initSalesTrendChart()
    }
  },
  methods: {
    async loadData() {
      try {
        const res = await this.$http.get('/api/analytics/sales-report', { params: { type: this.timeType } })
        this.salesReport = res.data.data || this.salesReport
      } catch (error) {
        console.error(error)
      }
      this.initCharts()
    },
    initCharts() {
      this.initSalesTrendChart()
      this.initCategoryChart()
      this.initBehaviorChart()
    },
    initSalesTrendChart() {
      const chart = echarts.init(this.$refs.salesTrendChart)
      let xAxisData, seriesData

      if (this.trendType === 'day') {
        xAxisData = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        seriesData = [1200, 1900, 1500, 2200, 1800, 2800, 3200]
      } else if (this.trendType === 'week') {
        xAxisData = ['第1周', '第2周', '第3周', '第4周']
        seriesData = [8500, 12600, 9800, 15200]
      } else {
        xAxisData = ['1月', '2月', '3月', '4月', '5月', '6月']
        seriesData = [32000, 28000, 45000, 38000, 52000, 48000]
      }

      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', boundaryGap: false, data: xAxisData },
        yAxis: { type: 'value' },
        series: [{
          name: '销售额',
          type: 'line',
          smooth: true,
          areaStyle: { opacity: 0.3 },
          data: seriesData,
          itemStyle: { color: '#409EFF' },
          lineStyle: { width: 3 }
        }]
      })
    },
    initCategoryChart() {
      const chart = echarts.init(this.$refs.categoryChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { orient: 'vertical', right: 10, top: 'center' },
        series: [{
          name: '销售占比',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
          labelLine: { show: false },
          data: [
            { value: 35, name: '玫瑰花' },
            { value: 20, name: '康乃馨' },
            { value: 15, name: '百合' },
            { value: 12, name: '向日葵' },
            { value: 10, name: '郁金香' },
            { value: 8, name: '其他' }
          ]
        }]
      })
    },
    initBehaviorChart() {
      const chart = echarts.init(this.$refs.behaviorChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: ['浏览量', '加购数', '下单数', '支付数', '复购数'] },
        yAxis: { type: 'value' },
        series: [{
          name: '数量',
          type: 'bar',
          barWidth: '50%',
          data: [5680, 1890, 980, 860, 210],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      })
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-box {
  padding: 25px;
  border-radius: 10px;
  color: #fff;
  text-align: center;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
}
</style>
