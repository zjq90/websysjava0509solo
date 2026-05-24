<template>
  <view class="tasks-container">
    <view class="header">
      <view class="stats-row">
        <view class="stat-item">
          <text class="stat-value urgent">{{ urgentCount }}</text>
          <text class="stat-label">紧急</text>
        </view>
        <view class="stat-item">
          <text class="stat-value high">{{ highCount }}</text>
          <text class="stat-label">高优</text>
        </view>
        <view class="stat-item">
          <text class="stat-value medium">{{ mediumCount }}</text>
          <text class="stat-label">中优</text>
        </view>
        <view class="stat-item">
          <text class="stat-value low">{{ lowCount }}</text>
          <text class="stat-label">低优</text>
        </view>
      </view>
    </view>
    
    <view class="tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'all' }"
        @click="switchTab('all')"
      >
        全部任务
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'dispatch' }"
        @click="switchTab('dispatch')"
      >
        调度任务
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'repair' }"
        @click="switchTab('repair')"
      >
        维修工单
      </view>
    </view>
    
    <scroll-view class="task-list" scroll-y>
      <view 
        class="task-card" 
        v-for="task in filteredTasks" 
        :key="task.id"
        @click="goToDetail(task)"
      >
        <view class="task-header">
          <view class="task-type" :class="getTaskTypeClass(task)">
            {{ getTaskTypeText(task) }}
          </view>
          <view class="task-priority" :class="'priority-' + task.priority.toLowerCase()">
            {{ getPriorityText(task.priority) }}
          </view>
          <view class="task-status" :class="'status-' + task.status.toLowerCase()">
            {{ getStatusText(task.status) }}
          </view>
        </view>
        
        <view class="task-content">
          <view class="task-title">{{ task.description || task.faultDescription }}</view>
          <view class="task-info" v-if="task.targetLocation || task.bikeLocation">
            <text class="info-icon">📍</text>
            <text>{{ task.targetLocation || task.bikeLocation }}</text>
          </view>
          <view class="task-info" v-if="task.bikeNo">
            <text class="info-icon">🚲</text>
            <text>车辆编号: {{ task.bikeNo }}</text>
          </view>
          <view class="task-info" v-if="task.faultType">
            <text class="info-icon">⚠️</text>
            <text>故障类型: {{ task.faultType }}</text>
          </view>
        </view>
        
        <view class="task-footer">
          <text class="time">{{ formatTime(task.createTime) }}</text>
          <text class="staff" v-if="task.staffName">负责人: {{ task.staffName }}</text>
        </view>
      </view>
      
      <view class="empty" v-if="filteredTasks.length === 0">
        <text>暂无任务</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { api } from '@/api'

const activeTab = ref('all')
const dispatchTasks = ref([])
const repairTasks = ref([])
const loading = ref(false)

const allTasks = computed(() => {
  const combined = [...dispatchTasks.value.map(t => ({ ...t, _type: 'dispatch' })), 
                   ...repairTasks.value.map(t => ({ ...t, _type: 'repair' }))]
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
  if (task._type === 'repair') {
    uni.navigateTo({ url: `/pages/task-detail/task-detail?id=${task.id}&type=repair` })
  } else {
    uni.navigateTo({ url: `/pages/task-detail/task-detail?id=${task.id}&type=dispatch` })
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const [dispatch, repair] = await Promise.all([
      api.getTasks(),
      api.getRepairOrders()
    ])
    dispatchTasks.value = dispatch || []
    repairTasks.value = repair || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.tasks-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx 20rpx;
  
  .stats-row {
    display: flex;
    justify-content: space-around;
    
    .stat-item {
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 48rpx;
        font-weight: bold;
        color: #fff;
        
        &.urgent { color: #ff6b6b; }
        &.high { color: #ffa502; }
        &.medium { color: #ffd93d; }
        &.low { color: #7bed9f; }
      }
      
      .stat-label {
        display: block;
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.8);
        margin-top: 8rpx;
      }
    }
  }
}

.tabs {
  display: flex;
  background: #fff;
  border-bottom: 1rpx solid #eee;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 24rpx 0;
    font-size: 28rpx;
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
        width: 60rpx;
        height: 4rpx;
        background: #667eea;
        border-radius: 2rpx;
      }
    }
  }
}

.task-list {
  height: calc(100vh - 340rpx);
  padding: 20rpx;
}

.task-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  
  .task-header {
    display: flex;
    align-items: center;
    gap: 16rpx;
    margin-bottom: 20rpx;
    
    .task-type {
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
      font-size: 22rpx;
      
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
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
      font-size: 22rpx;
      
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
      padding: 6rpx 16rpx;
      border-radius: 8rpx;
      font-size: 22rpx;
      
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
      font-size: 30rpx;
      color: #333;
      font-weight: 500;
      margin-bottom: 16rpx;
      line-height: 1.5;
    }
    
    .task-info {
      display: flex;
      align-items: center;
      gap: 10rpx;
      font-size: 26rpx;
      color: #666;
      margin-bottom: 10rpx;
      
      .info-icon {
        font-size: 24rpx;
      }
    }
  }
  
  .task-footer {
    display: flex;
    justify-content: space-between;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #f0f0f0;
    font-size: 24rpx;
    color: #999;
  }
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
