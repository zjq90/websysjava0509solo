<template>
  <div class="dashboard">
    <div class="stats-grid">
      <el-card class="stat-card">
        <div class="stat-icon employees">
          <el-icon><component :is="icons.User" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.employees }}</div>
          <div class="stat-label">员工总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon schedules">
          <el-icon><component :is="icons.Calendar" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.schedules }}</div>
          <div class="stat-label">今日排班</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon venues">
          <el-icon><component :is="icons.Building" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.venues }}</div>
          <div class="stat-label">可用场地</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon costumes">
          <el-icon><component :is="icons.Shirt" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.costumes }}</div>
          <div class="stat-label">可用服装</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon orders">
          <el-icon><component :is="icons.Shop" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.orders }}</div>
          <div class="stat-label">待处理订单</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-icon express">
          <el-icon><component :is="icons.Truck" /></el-icon>
        </div>
        <div class="stat-content">
          <div class="stat-value">{{ stats.express }}</div>
          <div class="stat-label">配送中快递</div>
        </div>
      </el-card>
    </div>

    <div class="info-section">
      <el-card title="今日排班" class="info-card">
        <div v-if="todaySchedules.length > 0">
          <el-timeline>
            <el-timeline-item
              v-for="schedule in todaySchedules"
              :key="schedule.id"
              :timestamp="formatTime(schedule.startTime) + ' - ' + formatTime(schedule.endTime)"
            >
              <div class="timeline-content">
                <span class="schedule-name">{{ schedule.employee.name }}</span>
                <span class="schedule-role">({{ getPositionDesc(schedule.employee.position) }})</span>
                <span class="schedule-customer">- {{ schedule.customerName }}</span>
                <span class="schedule-theme">{{ schedule.shootingTheme }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
        <div v-else class="empty-text">今日暂无排班</div>
      </el-card>

      <el-card title="待审批调班申请" class="info-card">
        <div v-if="pendingRequests.length > 0">
          <el-table :data="pendingRequests" size="small">
            <el-table-column prop="requester.name" label="申请人" />
            <el-table-column prop="createTime" label="申请时间" formatter="formatDateTime" />
            <el-table-column prop="reason" label="原因" />
            <el-table-column label="操作">
              <template #default="scope">
                <el-button size="mini" type="primary" @click="handleApprove(scope.row)">通过</el-button>
                <el-button size="mini" type="danger" @click="handleReject(scope.row)">拒绝</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="empty-text">暂无待审批申请</div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { User, Calendar, Building, Shirt, Shop, Truck } from '@element-plus/icons-vue'
import { employeeApi } from '../api/employee'
import { scheduleApi } from '../api/schedule'
import { venueApi } from '../api/venue'
import { costumeApi } from '../api/costume'
import { orderApi } from '../api/order'
import { expressApi } from '../api/express'
import { transferRequestApi } from '../api/transferRequest'
import { ElMessage } from 'element-plus'

const icons = { User, Calendar, Building, Shirt, Shop, Truck }

const stats = ref({
  employees: 0,
  schedules: 0,
  venues: 0,
  costumes: 0,
  orders: 0,
  express: 0
})

const todaySchedules = ref([])
const pendingRequests = ref([])

const loadStats = async () => {
  try {
    const [employees, schedules, venues, costumes, orders, express] = await Promise.all([
      employeeApi.getActive(),
      scheduleApi.getByDate(new Date().toISOString().split('T')[0]),
      venueApi.getAvailable(),
      costumeApi.getAvailableAndClean(),
      orderApi.getByStatus('EDITING'),
      expressApi.getByStatus('IN_TRANSIT')
    ])
    stats.value = {
      employees: employees.length,
      schedules: schedules.length,
      venues: venues.length,
      costumes: costumes.length,
      orders: orders.length,
      express: express.length
    }
    todaySchedules.value = schedules
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadPendingRequests = async () => {
  try {
    pendingRequests.value = await transferRequestApi.getPending()
  } catch (error) {
    console.error('加载待审批申请失败:', error)
  }
}

const formatTime = (time) => {
  return time?.substring(0, 5) || ''
}

const formatDateTime = (row, column) => {
  return row.createTime?.substring(0, 16) || ''
}

const getPositionDesc = (position) => {
  const positions = {
    PHOTOGRAPHER: '摄影师',
    MAKEUP_ARTIST: '化妆师',
    FILM_SELECTOR: '选片师',
    ASSISTANT: '助理',
    MANAGER: '经理'
  }
  return positions[position] || position
}

const handleApprove = async (request) => {
  try {
    await transferRequestApi.approve(request.id, { approverId: 5, approved: true, remark: '同意调班' })
    ElMessage.success('审批通过')
    loadPendingRequests()
  } catch (error) {
    ElMessage.error('审批失败')
  }
}

const handleReject = async (request) => {
  try {
    await transferRequestApi.approve(request.id, { approverId: 5, approved: false, remark: '不同意调班' })
    ElMessage.success('已拒绝')
    loadPendingRequests()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadStats()
  loadPendingRequests()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-icon.employees {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.schedules {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon.venues {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-icon.costumes {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon.orders {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.stat-icon.express {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.info-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.info-card {
  min-height: 300px;
}

.timeline-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.schedule-name {
  font-weight: 600;
}

.schedule-role {
  color: #666;
  font-size: 12px;
}

.schedule-customer {
  color: #333;
}

.schedule-theme {
  color: #999;
  font-size: 12px;
}

.empty-text {
  text-align: center;
  color: #999;
  padding: 40px;
}
</style>
