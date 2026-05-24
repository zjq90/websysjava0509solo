<template>
  <div class="tasks-container">
    <div class="header">
      <div class="stats-row">
        <div class="stat-item">
          <span class="stat-value urgent">{{ urgentCount }}</span>
          <span class="stat-label">紧急</span>
        </div>
        <div class="stat-item">
          <span class="stat-value high">{{ highCount }}</span>
          <span class="stat-label">高优</span>
        </div>
        <div class="stat-item">
          <span class="stat-value medium">{{ mediumCount }}</span>
          <span class="stat-label">中优</span>
        </div>
        <div class="stat-item">
          <span class="stat-value low">{{ lowCount }}</span>
          <span class="stat-label">低优</span>
        </div>
      </div>
    </div>
    
    <div class="tabs">
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'all' }"
        @click="switchTab('all')"
      >
        全部任务
      </div>
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'dispatch' }"
        @click="switchTab('dispatch')"
      >
        调度任务
      </div>
      <div 
        class="tab-item" 
        :class="{ active: activeTab === 'repair' }"
        @click="switchTab('repair')"
      >
        维修工单
      </div>
    </div>
    
    <div class="task-list">
      <div 
        class="task-card" 
        v-for="task in filteredTasks" 
        :key="task.id"
        @click="goToDetail(task)"
      >
        <div class="task-header">
          <div class="task-type" :class="getTaskTypeClass(task)">
            {{ getTaskTypeText(task) }}
          </div>
          <div class="task-priority" :class="'priority-' + task.priority?.toLowerCase()">
            {{ getPriorityText(task.priority) }}
          </div>
          <div class="task-status" :class="'status-' + task.status?.toLowerCase()">
            {{ getStatusText(task.status) }}
          </div>
        </div>
        
        <div class="task-content">
          <div class="task-title">{{ task.description || task.faultDescription || '暂无描述' }}</div>
          <div class="task-info" v-if="task.targetLocation || task.bikeLocation">
            <span class="info-icon">📍</span>
            <span>{{ task.targetLocation || task.bikeLocation }}</span>
          </div>
          <div class="task-info" v-if="task.bikeNo">
            <span class="info-icon">🚲</span>
            <span>车辆编号: {{ task.bikeNo }}</span>
          </div>
          <div class="task-info" v-if="task.faultType">
            <span class="info-icon">⚠️</span>
            <span>故障类型: {{ task.faultType }}</span>
          </div>
        </div>
        
        <div class="task-footer">
          <span class="time">{{ formatTime(task.createTime) }}</span>
          <span class="staff" v-if="task.staffName">负责人: {{ task.staffName }}</span>
        </div>
      </div>
      
      <div class="empty" v-if="filteredTasks.length === 0">
        <el-empty description="暂无任务" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'

const router = useRouter()
const activeTab = ref('all')
const dispatchTasks = ref([])
const repairTasks = ref([])

const allTasks = computed(() => {
  const combined = [
    ...dispatchTasks.value.map(t => ({ ...t, _type: 'dispatch' })), 
    ...repairTasks.value.map(t => ({ ...t, _type: 'repair' }))
  ]
  return combined.sort((a, b) => {
    const priorityOrder = { URGENT: 0, HIGH: 1, MEDIUM: 2, LOW: 3 }
    const pa = priorityOrder[a.priority] ?? 4
    const pb = priorityOrder[b.priority] ?? 4
    return pa - pb || new Date(b.createTime) - new Date(a.createTime)
  })
})

const filteredTasks = computed(() => {
  if (activeTab.value === 'all') return allTasks.value
  if (activeTab.value === 'dispatch') return dispatchTasks.value.map(t => ({ ...t, _type: 'dispatch' }))
  if (activeTab.value === 'repair') return repairTasks.value.map(t => ({ ...t, _type: 'repair' }))
  return []
})

const urgentCount = computed(() => allTasks.value.filter(t => t.priority === 'URGENT').length)
const highCount = computed(() => allTasks.value.filter(t => t.priority === 'HIGH').length)
const mediumCount = computed(() => allTasks.value.filter(t => t.priority === 'MEDIUM').length)
const lowCount = computed(() => allTasks.value.filter(t => t.priority === 'LOW').length)

const switchTab = (tab) => {
  activeTab.value = tab
}

const getTaskTypeClass = (task) => task._type === 'repair' ? 'type-repair' : 'type-dispatch'
const getTaskTypeText = (task) => task._type === 'repair' ? '维修工单' : '调度任务'

const getPriorityText = (p) => {
  const map = { URGENT: '紧急', HIGH: '高优', MEDIUM: '中优', LOW: '低优' }
  return map[p] || p
}

const getStatusText = (s) => {
  const map = { PENDING: '待接单', ACCEPTED: '已接单', IN_PROGRESS: '进行中', COMPLETED: '已完成' }
  return map[s] || s
}

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
}

const goToDetail = (task) => {
  router.push(`/task-detail/${task.id}?type=${task._type}`)
}

const loadData = async () => {
  try {
    const [dispatch, repair] = await Promise.all([
      api.getTasks(),
      api.getRepairOrders()
    ])
    dispatchTasks.value = dispatch || []
    repairTasks.value = repair || []
  } catch (e) {
    dispatchTasks.value = [
      { id: 1, description: '将A区过剩车辆调度至B区', targetLocation: '地铁站A出口', priority: 'HIGH', status: 'PENDING', createTime: new Date() },
      { id: 2, description: '回收故障车辆', targetLocation: '商圈B入口', priority: 'URGENT', status: 'ACCEPTED', createTime: new Date(), staffName: '张三' }
    ]
    repairTasks.value = [
      { id: 3, faultDescription: '车辆刹车失灵', bikeNo: 'B001', faultType: '刹车故障', priority: 'URGENT', status: 'PENDING', createTime: new Date() },
      { id: 4, faultDescription: '车链脱落', bikeNo: 'B023', faultType: '链条故障', priority: 'MEDIUM', status: 'IN_PROGRESS', createTime: new Date(), staffName: '李四' }
    ]
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.tasks-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px 15px;
  
  .stats-row {
    display: flex;
    justify-content: space-around;
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 28px;
        font-weight: bold;
        color: #fff;
        
        &.urgent { color: #ff6b6b; }
        &.high { color: #ffa502; }
        &.medium { color: #ffd93d; }
        &.low { color: #7bed9f; }
      }
      
      .stat-label {
        display: block;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 5px;
      }
    }
  }
}

.tabs {
  display: flex;
  background: #fff;
  border-bottom: 1px solid #eee;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 15px 0;
    font-size: 14px;
    color: #666;
    position: relative;
    
    &.active {
      color: #667eea;
      font-weight: bold;
      
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 40px;
        height: 3px;
        background: #667eea;
        border-radius: 2px;
      }
    }
  }
}

.task-list {
  padding: 10px;
}

.task-card {
  background: #fff;
  border-radius: 12px;
  padding: 15px;
  margin-bottom: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  
  .task-header {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
    
    .task-type {
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 12px;
      
      &.type-dispatch {
        background: #e8f3ff;
        color: #1989fa;
      }
      
      &.type-repair {
        background: #fff0f0;
        color: #e64340;
      }
    }
    
    .task-priority {
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 12px;
      
      &.priority-urgent {
        background: #ffebee;
        color: #e64340;
      }
      
      &.priority-high {
        background: #fff7e6;
        color: #ff976a;
      }
      
      &.priority-medium {
        background: #ecf5ff;
        color: #1989fa;
      }
      
      &.priority-low {
        background: #f0f9eb;
        color: #67c23a;
      }
    }
    
    .task-status {
      margin-left: auto;
      padding: 4px 10px;
      border-radius: 6px;
      font-size: 12px;
      
      &.status-pending {
        background: #f4f4f5;
        color: #909399;
      }
      
      &.status-accepted {
        background: #e6f7ff;
        color: #1890ff;
      }
      
      &.status-in_progress {
        background: #fff7e6;
        color: #fa8c16;
      }
      
      &.status-completed {
        background: #f6ffed;
        color: #52c41a;
      }
    }
  }
  
  .task-content {
    .task-title {
      font-size: 15px;
      color: #333;
      font-weight: 500;
      margin-bottom: 10px;
      line-height: 1.5;
    }
    
    .task-info {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 13px;
      color: #666;
      margin-bottom: 6px;
      
      .info-icon {
        font-size: 12px;
      }
    }
  }
  
  .task-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 12px;
    padding-top: 12px;
    border-top: 1px solid #f0f0f0;
    font-size: 12px;
    color: #999;
  }
}

.empty {
  text-align: center;
  padding: 60px 0;
}
</style>
