<template>
  <view class="detail-container">
    <view class="card" v-if="task">
      <view class="status-bar" :class="'status-' + task.status.toLowerCase()">
        <text class="status-text">{{ getStatusText(task.status) }}</text>
        <text class="priority-text" :class="'priority-' + task.priority.toLowerCase()">
          {{ getPriorityText(task.priority) }}
        </text>
      </view>
      
      <view class="info-section">
        <view class="section-title">
          <text class="title-icon">{{ taskType === 'repair' ? '🔧' : '🚚' }}</text>
          <text>{{ taskType === 'repair' ? '维修工单' : '调度任务' }}</text>
        </view>
        
        <view class="info-row">
          <text class="label">任务编号</text>
          <text class="value">{{ task.taskNo || task.orderNo }}</text>
        </view>
        
        <view class="info-row">
          <text class="label">任务描述</text>
          <text class="value desc">{{ task.description || task.faultDescription }}</text>
        </view>
        
        <view class="info-row" v-if="task.faultType">
          <text class="label">故障类型</text>
          <text class="value fault">{{ task.faultType }}</text>
        </view>
        
        <view class="info-row" v-if="task.bikeNo">
          <text class="label">车辆编号</text>
          <text class="value link" @click="goToBikeDetail">{{ task.bikeNo }}</text>
        </view>
        
        <view class="info-row" v-if="task.targetLocation || task.bikeLocation">
          <text class="label">位置</text>
          <text class="value">{{ task.targetLocation || task.bikeLocation }}</text>
        </view>
        
        <view class="info-row" v-if="task.staffName">
          <text class="label">负责人</text>
          <text class="value">{{ task.staffName }}</text>
        </view>
        
        <view class="info-row">
          <text class="label">创建时间</text>
          <text class="value">{{ formatTime(task.createTime) }}</text>
        </view>
      </view>
      
      <view class="location-section" v-if="task.targetLongitude || task.bikeLongitude">
        <view class="section-title">
          <text class="title-icon">📍</text>
          <text>位置导航</text>
        </view>
        <view class="map-placeholder" @click="openMap">
          <text class="map-icon">🗺️</text>
          <text class="map-text">点击查看地图导航</text>
        </view>
      </view>
      
      <view class="action-section">
        <button 
          v-if="task.status === 'PENDING'" 
          class="btn btn-primary"
          @click="acceptTask"
        >
          接受任务
        </button>
        
        <button 
          v-if="task.status === 'ACCEPTED' && taskType === 'repair'" 
          class="btn btn-warning"
          @click="startRepair"
        >
          开始维修
        </button>
        
        <button 
          v-if="task.status === 'IN_PROGRESS' || task.status === 'ACCEPTED'" 
          class="btn btn-success"
          @click="showCompleteDialog = true"
        >
          完成任务
        </button>
        
        <button 
          v-if="task.targetLongitude || task.bikeLongitude" 
          class="btn btn-info"
          @click="openMap"
        >
          导航前往
        </button>
      </view>
    </view>
    
    <uni-popup ref="completePopup" type="bottom">
      <view class="complete-dialog">
        <view class="dialog-title">完成任务</view>
        <textarea 
          v-model="completeRemark" 
          class="remark-input" 
          placeholder="请输入完成说明..."
        ></textarea>
        <view class="dialog-actions">
          <button class="btn-cancel" @click="showCompleteDialog = false">取消</button>
          <button class="btn-confirm" @click="completeTask">确认完成</button>
        </view>
      </view>
    </uni-popup>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { api } from '@/api'

const store = useStore()
const task = ref(null)
const taskType = ref('dispatch')
const showCompleteDialog = ref(false)
const completeRemark = ref('')

const getStatusText = (s) => {
  const map = { PENDING: '待接单', ACCEPTED: '已接单', IN_PROGRESS: '进行中', COMPLETED: '已完成' }
  return map[s] || s
}

const getPriorityText = (p) => {
  const map = { URGENT: '紧急', HIGH: '高优', MEDIUM: '中优', LOW: '低优' }
  return map[p] || p
}

const formatTime = (time) => {
  if (!time) return ''
  const d = new Date(time)
  return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')} ${d.getHours().toString().padStart(2,'0')}:${d.getMinutes().toString().padStart(2,'0')}`
}

const goToBikeDetail = () => {
  uni.navigateTo({ url: `/pages/bike-detail/bike-detail?id=${task.value.bikeId}` })
}

const openMap = () => {
  const lat = task.value.targetLatitude || task.value.bikeLatitude
  const lng = task.value.targetLongitude || task.value.bikeLongitude
  uni.openLocation({
    latitude: lat || 39.9,
    longitude: lng || 116.4,
    name: task.value.targetLocation || task.value.bikeLocation || '目标位置',
    address: task.value.targetLocation || task.value.bikeLocation || '目标位置'
  })
}

const acceptTask = async () => {
  const user = store.state.user
  if (!user) {
    uni.showToast({ title: '请先登录', icon: 'none' })
    return
  }
  
  try {
    if (taskType.value === 'repair') {
      await api.acceptRepairOrder(task.value.id, user.id, user.name)
    } else {
      await api.acceptTask(task.value.id, user.id, user.name)
    }
    uni.showToast({ title: '接单成功', icon: 'success' })
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const startRepair = async () => {
  try {
    await api.startRepair(task.value.id)
    uni.showToast({ title: '已开始维修', icon: 'success' })
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const completeTask = async () => {
  try {
    if (taskType.value === 'repair') {
      await api.completeRepair(task.value.id, completeRemark.value, '', 0)
    } else {
      await api.completeTask(task.value.id)
    }
    showCompleteDialog.value = false
    uni.showToast({ title: '任务已完成', icon: 'success' })
    setTimeout(() => {
      uni.navigateBack()
    }, 1000)
  } catch (e) {
    console.error(e)
  }
}

const loadData = async () => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  
  taskType.value = options.type || 'dispatch'
  
  try {
    if (taskType.value === 'repair') {
      task.value = await api.getRepairOrderDetail(options.id)
    } else {
      task.value = await api.getTaskDetail(options.id)
    }
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
}

.card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.status-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  
  &.status-pending { background: #f4f4f5; }
  &.status-accepted { background: #e6f7ff; }
  &.status-in_progress { background: #fff7e6; }
  &.status-completed { background: #f6ffed; }
  
  .status-text {
    font-size: 32rpx;
    font-weight: bold;
  }
  
  .priority-text {
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    
    &.priority-urgent { background: #ffebee; color: #e64340; }
    &.priority-high { background: #fff7e6; color: #ff976a; }
    &.priority-medium { background: #ecf5ff; color: #1989fa; }
    &.priority-low { background: #f0f9eb; color: #67c23a; }
  }
}

.info-section {
  padding: 30rpx;
  
  .section-title {
    display: flex;
    align-items: center;
    gap: 12rpx;
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 30rpx;
    
    .title-icon {
      font-size: 36rpx;
    }
  }
  
  .info-row {
    display: flex;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .label {
      width: 160rpx;
      color: #999;
      font-size: 28rpx;
    }
    
    .value {
      flex: 1;
      color: #333;
      font-size: 28rpx;
      
      &.desc {
        line-height: 1.6;
      }
      
      &.link {
        color: #1989fa;
      }
      
      &.fault {
        color: #e64340;
      }
    }
  }
}

.location-section {
  padding: 30rpx;
  border-top: 20rpx solid #f5f5f5;
  
  .section-title {
    display: flex;
    align-items: center;
    gap: 12rpx;
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    
    .title-icon {
      font-size: 36rpx;
    }
  }
  
  .map-placeholder {
    height: 200rpx;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 16rpx;
    
    .map-icon {
      font-size: 60rpx;
    }
    
    .map-text {
      color: #fff;
      font-size: 28rpx;
    }
  }
}

.action-section {
  padding: 30rpx;
  border-top: 20rpx solid #f5f5f5;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  
  .btn {
    width: 100%;
    height: 88rpx;
    border-radius: 12rpx;
    font-size: 30rpx;
    border: none;
    
    &.btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
    
    &.btn-warning {
      background: #ff976a;
      color: #fff;
    }
    
    &.btn-success {
      background: #67c23a;
      color: #fff;
    }
    
    &.btn-info {
      background: #1989fa;
      color: #fff;
    }
  }
}

.complete-dialog {
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding: 40rpx;
  
  .dialog-title {
    font-size: 32rpx;
    font-weight: bold;
    text-align: center;
    margin-bottom: 30rpx;
  }
  
  .remark-input {
    width: 100%;
    height: 200rpx;
    border: 2rpx solid #eee;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    box-sizing: border-box;
  }
  
  .dialog-actions {
    display: flex;
    gap: 20rpx;
    margin-top: 30rpx;
    
    button {
      flex: 1;
      height: 80rpx;
      border-radius: 12rpx;
      font-size: 28rpx;
      border: none;
    }
    
    .btn-cancel {
      background: #f5f5f5;
      color: #666;
    }
    
    .btn-confirm {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: #fff;
    }
  }
}
</style>
