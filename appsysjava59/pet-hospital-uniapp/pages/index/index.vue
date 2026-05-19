<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="header-section flex-between">
      <text class="title">待接诊列表</text>
      <view class="user-info" @click="handleLogout">
        <text class="user-name">{{ userInfo?.realName || '医生' }}</text>
        <text class="logout-icon">🚪</text>
      </view>
    </view>
    
    <view class="consultation-list">
      <view 
        v-for="item in consultationList" 
        :key="item.id" 
        class="consultation-item card"
        @click="goToChat(item)"
      >
        <view class="item-header flex-between">
          <view class="pet-info">
            <text class="pet-name">{{ item.title }}</text>
            <text v-if="item.emergencyLevel === 'URGENT'" class="tag tag-urgent">紧急</text>
          </view>
          <text class="status" :class="'status-' + item.status.toLowerCase()">
            {{ getStatusText(item.status) }}
          </text>
        </view>
        
        <view class="symptom">
          <text>症状：{{ item.symptom || '暂无描述' }}</text>
        </view>
        
        <view class="item-footer flex-between">
          <text class="time">{{ formatTime(item.createTime) }}</text>
          <text class="type">{{ item.consultationType === 'VIDEO' ? '视频问诊' : '图文问诊' }}</text>
        </view>
      </view>
      
      <view v-if="consultationList.length === 0" class="empty">
        <text>暂无待接诊问诊</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import consultationApi from '../../api/consultation.js'

const userStore = useUserStore()

const consultationList = ref([])
const userInfo = computed(() => userStore.userInfo)
const elderMode = computed(() => userStore.elderMode)

const getStatusText = (status) => {
  const map = {
    'PENDING': '待接诊',
    'IN_PROGRESS': '问诊中',
    'COMPLETED': '已完成'
  }
  return map[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return `${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
}

const loadData = async () => {
  if (!userInfo.value?.id) return
  
  try {
    const res = await consultationApi.getPendingList(userInfo.value.id)
    consultationList.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const goToChat = (item) => {
  uni.navigateTo({
    url: '/pages/chat/chat?id=' + item.id
  })
}

const handleLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
      }
    }
  })
}

onShow(() => {
  loadData()
})
</script>

<style scoped>
.header-section {
  padding: 20rpx 0;
  margin-bottom: 20rpx;
}

.title {
  font-size: 32rpx;
  font-weight: bold;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.user-name {
  font-size: 28rpx;
  color: #409EFF;
}

.logout-icon {
  font-size: 24rpx;
}

.consultation-item {
  margin-bottom: 20rpx;
}

.item-header {
  margin-bottom: 16rpx;
}

.pet-info {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.pet-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
}

.status {
  font-size: 24rpx;
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
}

.status-pending {
  background-color: #FEF0F0;
  color: #F56C6C;
}

.status-in_progress {
  background-color: #ECF5FF;
  color: #409EFF;
}

.status-completed {
  background-color: #F0F9EB;
  color: #67C23A;
}

.symptom {
  font-size: 26rpx;
  color: #606266;
  margin-bottom: 16rpx;
  line-height: 1.6;
}

.item-footer {
  font-size: 24rpx;
  color: #909399;
}

.type {
  color: #409EFF;
}

.elder-mode .title {
  font-size: 36rpx;
}

.elder-mode .pet-name {
  font-size: 34rpx;
}

.elder-mode .symptom {
  font-size: 30rpx;
}
</style>
