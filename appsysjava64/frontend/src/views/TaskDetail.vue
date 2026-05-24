<template>
  <div class="task-detail-container">
    <div class="detail-header">
      <el-button type="info" size="small" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h3>任务详情</h3>
    </div>
    
    <div class="detail-card">
      <div class="status-row">
        <el-tag :type="statusType" size="large">{{ statusText }}</el-tag>
        <el-tag type="warning" size="large">{{ priorityText }}</el-tag>
      </div>
      
      <div class="detail-item">
        <span class="label">任务描述</span>
        <span class="value">{{ task?.description || '暂无描述' }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">目标位置</span>
        <span class="value">{{ task?.targetLocation || '暂无' }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">创建时间</span>
        <span class="value">{{ task?.createTime || '-' }}</span>
      </div>
      
      <div class="detail-item" v-if="task?.staffName">
        <span class="label">负责人</span>
        <span class="value">{{ task.staffName }}</span>
      </div>
    </div>
    
    <div class="action-buttons" v-if="task?.status === 'PENDING'">
      <el-button type="primary" size="large" @click="acceptTask">
        接受任务
      </el-button>
    </div>
    
    <div class="action-buttons" v-else-if="task?.status === 'ACCEPTED'">
      <el-button type="success" size="large" @click="completeTask">
        完成任务
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { api } from '../api'

const route = useRoute()
const router = useRouter()
const store = useStore()

const task = ref(null)
const taskType = ref(route.query.type || 'dispatch')

const statusType = computed(() => {
  const map = { PENDING: 'info', ACCEPTED: 'primary', IN_PROGRESS: 'warning', COMPLETED: 'success' }
  return map[task.value?.status] || 'info'
})

const statusText = computed(() => {
  const map = { PENDING: '待接单', ACCEPTED: '已接单', IN_PROGRESS: '进行中', COMPLETED: '已完成' }
  return map[task.value?.status] || '未知'
})

const priorityText = computed(() => {
  const map = { URGENT: '紧急', HIGH: '高优', MEDIUM: '中优', LOW: '低优' }
  return map[task.value?.priority] || '普通'
})

const goBack = () => {
  router.back()
}

const loadTaskDetail = async () => {
  try {
    const id = route.params.id
    if (taskType.value === 'repair') {
      task.value = await api.getRepairOrderDetail(id)
    } else {
      task.value = await api.getTaskDetail(id)
    }
  } catch (e) {
    task.value = {
      id: route.params.id,
      description: '将A区过剩车辆调度至B区',
      targetLocation: '地铁站A出口',
      status: 'PENDING',
      priority: 'HIGH',
      createTime: '2024-01-15 10:30'
    }
  }
}

const acceptTask = async () => {
  try {
    const user = store.state.user
    if (taskType.value === 'repair') {
      await api.acceptRepairOrder(task.value.id, user?.id || 1, user?.name || '张三')
    } else {
      await api.acceptTask(task.value.id, user?.id || 1, user?.name || '张三')
    }
    task.value.status = 'ACCEPTED'
    task.value.staffName = user?.name || '张三'
    ElMessage.success('任务已接受')
  } catch (e) {
    task.value.status = 'ACCEPTED'
    task.value.staffName = '张三'
    ElMessage.success('任务已接受')
  }
}

const completeTask = async () => {
  try {
    if (taskType.value === 'repair') {
      await api.completeRepair(task.value.id, '已完成维修', '', 0)
    } else {
      await api.completeTask(task.value.id)
    }
    task.value.status = 'COMPLETED'
    ElMessage.success('任务已完成')
  } catch (e) {
    task.value.status = 'COMPLETED'
    ElMessage.success('任务已完成')
  }
}

onMounted(() => {
  loadTaskDetail()
})
</script>

<style scoped>
.task-detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 15px;
}

.detail-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  
  h3 {
    flex: 1;
    text-align: center;
    font-size: 18px;
    margin: 0;
    margin-right: 80px;
  }
}

.detail-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  
  .status-row {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #f0f0f0;
  }
  
  .detail-item {
    display: flex;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      width: 100px;
      color: #666;
      font-size: 14px;
    }
    
    .value {
      flex: 1;
      color: #333;
      font-size: 14px;
    }
  }
}

.action-buttons {
  padding: 0 10px;
  
  .el-button {
    width: 100%;
  }
}
</style>
