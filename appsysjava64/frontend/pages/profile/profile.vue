<template>
  <view class="profile-container">
    <view class="header">
      <view class="user-info">
        <view class="avatar">
          <text class="avatar-icon">👤</text>
        </view>
        <view class="user-detail">
          <view class="user-name">{{ userInfo.name || '运维人员' }}</view>
          <view class="user-role">{{ userRoleText }}</view>
        </view>
      </view>
      <view class="stats-row">
        <view class="stat-item">
          <text class="stat-value">{{ todayTasks }}</text>
          <text class="stat-label">今日任务</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ completedTasks }}</text>
          <text class="stat-label">已完成</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ workHours }}</text>
          <text class="stat-label">工时(h)</text>
        </view>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-title">工作管理</view>
      <view class="menu-list">
        <view class="menu-item" @click="goToMyTasks">
          <view class="menu-icon">📋</view>
          <text class="menu-text">我的任务</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToWorkRecord">
          <view class="menu-icon">📊</view>
          <text class="menu-text">工作记录</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToPerformance">
          <view class="menu-icon">🏆</view>
          <text class="menu-text">业绩统计</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-title">车辆管理</view>
      <view class="menu-list">
        <view class="menu-item" @click="goToBikeList">
          <view class="menu-icon">🚲</view>
          <text class="menu-text">车辆列表</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToFaultReport">
          <view class="menu-icon">⚠️</view>
          <text class="menu-text">故障上报</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToSpareParts">
          <view class="menu-icon">🔧</view>
          <text class="menu-text">备件库存</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-title">系统设置</view>
      <view class="menu-list">
        <view class="menu-item" @click="goToSettings">
          <view class="menu-icon">⚙️</view>
          <text class="menu-text">设置</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToHelp">
          <view class="menu-icon">❓</view>
          <text class="menu-text">帮助中心</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToAbout">
          <view class="menu-icon">ℹ️</view>
          <text class="menu-text">关于我们</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="logout-btn" @click="logout">
      退出登录
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import store from '@/store'

const userInfo = computed(() => store.state.user || {})
const userRoleText = computed(() => {
  const roles = { ADMIN: '管理员', DISPATCHER: '调度员', MAINTENANCE: '运维人员' }
  return roles[userInfo.value.role] || '运维人员'
})

const todayTasks = ref(12)
const completedTasks = ref(8)
const workHours = ref(6.5)

const goToMyTasks = () => {
  uni.switchTab({ url: '/pages/tasks/tasks' })
}

const goToWorkRecord = () => {
  uni.showToast({ title: '工作记录页面', icon: 'none' })
}

const goToPerformance = () => {
  uni.showToast({ title: '业绩统计页面', icon: 'none' })
}

const goToBikeList = () => {
  uni.showToast({ title: '车辆列表页面', icon: 'none' })
}

const goToFaultReport = () => {
  uni.showToast({ title: '故障上报页面', icon: 'none' })
}

const goToSpareParts = () => {
  uni.showToast({ title: '备件库存页面', icon: 'none' })
}

const goToSettings = () => {
  uni.showToast({ title: '设置页面', icon: 'none' })
}

const goToHelp = () => {
  uni.showToast({ title: '帮助中心', icon: 'none' })
}

const goToAbout = () => {
  uni.showModal({
    title: '关于我们',
    content: '共享单车运维端 v1.0.0\n\n提供专业的共享单车运维管理服务',
    showCancel: false
  })
}

const logout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        store.commit('logout')
        uni.reLaunch({ url: '/pages/login/login' })
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx 40rpx;
  
  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 40rpx;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 24rpx;
      
      .avatar-icon {
        font-size: 60rpx;
      }
    }
    
    .user-detail {
      .user-name {
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 8rpx;
      }
      
      .user-role {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
  
  .stats-row {
    display: flex;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 16rpx;
    padding: 24rpx 0;
    
    .stat-item {
      flex: 1;
      text-align: center;
      
      .stat-value {
        display: block;
        font-size: 40rpx;
        font-weight: bold;
        color: #fff;
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

.menu-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
  
  .menu-title {
    padding: 24rpx 24rpx 16rpx;
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
  }
  
  .menu-list {
    .menu-item {
      display: flex;
      align-items: center;
      padding: 28rpx 24rpx;
      border-bottom: 1rpx solid #f5f5f5;
      
      &:last-child {
        border-bottom: none;
      }
      
      .menu-icon {
        width: 48rpx;
        font-size: 32rpx;
        margin-right: 20rpx;
      }
      
      .menu-text {
        flex: 1;
        font-size: 28rpx;
        color: #333;
      }
      
      .menu-arrow {
        font-size: 32rpx;
        color: #ccc;
      }
    }
  }
}

.logout-btn {
  margin: 40rpx 20rpx;
  padding: 28rpx;
  background: #fff;
  border-radius: 16rpx;
  text-align: center;
  font-size: 30rpx;
  color: #e64340;
  font-weight: 500;
}
</style>
