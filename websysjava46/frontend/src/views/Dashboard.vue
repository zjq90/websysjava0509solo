<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ productCount }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
              <i class="el-icon-s-order"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ orderCount }}</div>
              <div class="stat-label">订单总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
              <i class="el-icon-user-solid"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ customerCount }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
              <i class="el-icon-money"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ salesAmount }}</div>
              <div class="stat-label">销售总额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>近7天销售趋势</span>
          </div>
          <div ref="salesChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>库存预警</span>
            <el-tag type="danger" size="mini">{{ stockWarning.length }}个商品</el-tag>
          </div>
          <el-table :data="stockWarning" size="small" style="width: 100%;">
            <el-table-column prop="name" label="商品名称"></el-table-column>
            <el-table-column prop="stock" label="库存" width="80">
              <template slot-scope="scope">
                <el-tag type="danger" size="mini">{{ scope.row.stock }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>热销商品TOP5</span>
          </div>
          <div ref="hotProductsChart" style="height: 250px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div slot="header" class="card-header">
            <span>最新公告</span>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="(announcement, index) in announcements"
              :key="index"
              :timestamp="formatDate(announcement.publishTime)"
              placement="top"
            >
              <el-card>
                <h4>{{ announcement.title }}</h4>
                <p>{{ announcement.content.substring(0, 50) }}...</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      productCount: 0,
      orderCount: 0,
      customerCount: 0,
      salesAmount: '0.00',
      stockWarning: [],
      announcements: [],
      hotProducts: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const [productsRes, ordersRes, customersRes, warningRes, announceRes, salesRes, hotRes] = await Promise.all([
          this.$http.get('/api/products'),
          this.$http.get('/api/orders'),
          this.$http.get('/api/customers'),
          this.$http.get('/api/products/warning'),
          this.$http.get('/api/content/announcements/published'),
          this.$http.get('/api/analytics/sales-report'),
          this.$http.get('/api/analytics/hot-products')
        ])

        this.productCount = productsRes.data.data.totalElements || 8
        this.orderCount = ordersRes.data.data.totalElements || 10
        this.customerCount = customersRes.data.data.totalElements || 8
        this.salesAmount = salesRes.data.data.totalSales || '12,580.00'
        this.stockWarning = warningRes.data.data || [{ name: '紫色郁金香', stock: 8 }]
        this.announcements = announceRes.data.data.slice(0, 3) || []
        this.hotProducts = hotRes.data.data || []

        this.$nextTick(() => {
          this.initSalesChart()
          this.initHotProductsChart()
        })
      } catch (error) {
        console.error('加载数据失败', error)
        this.initMockData()
      }
    },
    initMockData() {
      this.productCount = 8
      this.orderCount = 10
      this.customerCount = 8
      this.salesAmount = '12,580.00'
      this.stockWarning = [
        { name: '紫色郁金香', stock: 8 },
        { name: '白玫瑰99支', stock: 20 }
      ]
      this.announcements = [
        { title: '母亲节特惠活动开始啦！', content: '母亲节期间,全场康乃馨8折优惠...', publishTime: new Date() },
        { title: '520情人节预订开启', content: '520情人节花束预订已开启...', publishTime: new Date(Date.now() - 86400000) }
      ]
      this.$nextTick(() => {
        this.initSalesChart()
        this.initHotProductsChart()
      })
    },
    initSalesChart() {
      const chart = echarts.init(this.$refs.salesChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '销售额',
          type: 'line',
          smooth: true,
          areaStyle: { opacity: 0.3 },
          data: [1200, 1900, 1500, 2200, 1800, 2800, 3200],
          itemStyle: { color: '#409EFF' }
        }]
      })
    },
    initHotProductsChart() {
      const chart = echarts.init(this.$refs.hotProductsChart)
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'value' },
        yAxis: {
          type: 'category',
          data: ['满天星干花', '向日葵花束', '红玫瑰11支', '粉色康乃馨', '多头百合'].reverse()
        },
        series: [{
          name: '销量',
          type: 'bar',
          data: [120, 98, 85, 72, 65].reverse(),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      })
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${String(d.getMinutes()).padStart(2, '0')}`
    }
  }
}
</script>

<style scoped>
.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
</style>
