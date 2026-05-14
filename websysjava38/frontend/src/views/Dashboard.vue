<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF">
              <el-icon :size="30"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalOrders || 0 }}</div>
              <div class="stat-label">总订单数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A">
              <el-icon :size="30"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCustomers || 0 }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C">
              <el-icon :size="30"><Money /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">¥{{ statistics.monthAmount || 0 }}</div>
              <div class="stat-label">本月营收</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C">
              <el-icon :size="30"><Sunny /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.todayOrders || 0 }}</div>
              <div class="stat-label">今日新增</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单状态分布</span>
            </div>
          </template>
          <div ref="statusChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单来源分布</span>
            </div>
          </template>
          <div ref="sourceChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近订单</span>
              <el-button type="primary" link @click="$router.push('/orders')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentOrders" stripe>
            <el-table-column prop="orderNo" label="订单编号" width="150" />
            <el-table-column prop="customerName" label="客户姓名" width="100" />
            <el-table-column prop="packageName" label="套餐名称" />
            <el-table-column prop="totalAmount" label="订单金额" width="100">
              <template #default="{ row }">
                ¥{{ row.totalAmount }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="订单状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="currentStage" label="进度" width="200">
              <template #default="{ row }">
                <el-progress :percentage="getProgressPercentage(row.currentStage)" :show-text="false" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import * as echarts from 'echarts'
import { Document, User, Money, Sunny } from '@element-plus/icons-vue'

const statistics = ref({})
const recentOrders = ref([])
const statusChartRef = ref(null)
const sourceChartRef = ref(null)

const loadStatistics = async () => {
  try {
    const res = await request.get('/orders/statistics')
    statistics.value = res.data
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

const loadRecentOrders = async () => {
  try {
    const res = await request.get('/orders/page', { params: { page: 0, size: 5 } })
    recentOrders.value = res.data.content || []
  } catch (error) {
    console.error('加载最近订单失败', error)
  }
}

const getStatusType = (status) => {
  const typeMap = {
    '待付定金': 'warning',
    '已付定金': 'info',
    '拍摄中': 'primary',
    '选片中': 'primary',
    '修片中': 'primary',
    '产品制作中': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return typeMap[status] || 'info'
}

const getProgressPercentage = (stage) => {
  const stages = [0, 1, 2, 3, 4, 5, 6]
  const index = stages.indexOf(stage)
  return index >= 0 ? Math.round((index / (stages.length - 1)) * 100) : 0
}

const initCharts = () => {
  const statusChart = echarts.init(statusChartRef.value)
  const statusOption = {
    tooltip: { trigger: 'item' },
    legend: { bottom: '5%', left: 'center' },
    series: [
      {
        name: '订单状态',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
        labelLine: { show: false },
        data: [
          { value: 2, name: '待付定金', itemStyle: { color: '#E6A23C' } },
          { value: 3, name: '拍摄中', itemStyle: { color: '#409EFF' } },
          { value: 2, name: '选片中', itemStyle: { color: '#67C23A' } },
          { value: 1, name: '已完成', itemStyle: { color: '#909399' } }
        ]
      }
    ]
  }
  statusChart.setOption(statusOption)

  const sourceChart = echarts.init(sourceChartRef.value)
  const sourceOption = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: ['美团', '抖音', '小程序', '门店', '其他'] },
    yAxis: { type: 'value' },
    series: [
      {
        name: '订单数',
        type: 'bar',
        barWidth: '40%',
        data: [12, 8, 5, 15, 3],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  sourceChart.setOption(sourceOption)
}

onMounted(() => {
  loadStatistics()
  loadRecentOrders()
  setTimeout(initCharts, 100)
})
</script>

<style scoped>
.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
