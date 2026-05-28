<template>
  <view class="my-clubs-container">
    <view class="club-list" v-if="clubList.length > 0">
      <view 
        class="club-card" 
        v-for="club in clubList" 
        :key="club.id"
        @click="goClubDetail(club)"
      >
        <view class="club-header">
          <image 
            v-if="club.logo" 
            :src="club.logo" 
            class="club-logo" 
            mode="aspectFill"
          />
          <view v-else class="club-logo-placeholder">
            <text>{{ club.name ? club.name.substring(0, 1) : '社' }}</text>
          </view>
          <view class="club-info">
            <text class="club-name">{{ club.name }}</text>
            <text class="club-role">{{ getRoleText(club.role) }}</text>
          </view>
          <text class="arrow">›</text>
        </view>
        
        <view class="club-stats">
          <view class="stat-item">
            <text class="stat-value">{{ club.memberCount || 0 }}</text>
            <text class="stat-label">成员</text>
          </view>
          <view class="stat-item">
            <text class="stat-value">{{ club.activityCount || 0 }}</text>
            <text class="stat-label">活动</text>
          </view>
          <view class="stat-item">
            <text class="stat-value">{{ club.feedCount || 0 }}</text>
            <text class="stat-label">动态</text>
          </view>
        </view>
        
        <view class="club-actions">
          <button class="action-btn primary" @click.stop="goChat(club)">
            <text class="btn-icon">💬</text>
            群聊
          </button>
          <button class="action-btn" @click.stop="goActivities(club)">
            <text class="btn-icon">📅</text>
            活动
          </button>
          <button class="action-btn danger" @click.stop="quitClub(club)">
            <text class="btn-icon">🚪</text>
            退出
          </button>
        </view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">🏫</text>
      <text class="empty-text">还没有加入任何社团</text>
      <button class="join-btn" @click="goExplore">去发现社团</button>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      clubList: [],
      loading: false
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const res = await api.getMyClubs()
        this.clubList = res.data || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    getRoleText(role) {
      const roles = { 0: '成员', 1: '管理员', 2: '社长' }
      return roles[role] || '成员'
    },
    
    goClubDetail(club) {
      uni.navigateTo({ url: '/pages/club/detail?id=' + club.id })
    },
    
    goChat(club) {
      uni.navigateTo({ 
        url: '/pages/chat/detail?clubId=' + club.id + '&clubName=' + encodeURIComponent(club.name) 
      })
    },
    
    goActivities(club) {
      uni.navigateTo({ url: '/pages/activity/index?clubId=' + club.id })
    },
    
    async quitClub(club) {
      const confirm = await util.showModal(
        '退出社团',
        `确定要退出「${club.name}」吗？退出后将无法接收该社团的消息和活动通知。`
      )
      
      if (!confirm) return
      
      try {
        const res = await api.quitClub(club.id)
        if (res.code === 200) {
          util.toast('已退出社团', 'success')
          this.loadData()
        } else {
          util.toast(res.message || '退出失败')
        }
      } catch (e) {
        util.toast('退出失败，请重试')
        console.error(e)
      }
    },
    
    goExplore() {
      uni.switchTab({ url: '/pages/index/index' })
    }
  }
}
</script>

<style scoped>
.my-clubs-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding: 20rpx;
}

.club-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.club-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.club-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.club-logo,
.club-logo-placeholder {
  width: 100rpx;
  height: 100rpx;
  border-radius: 20rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.club-logo-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 36rpx;
  font-weight: bold;
}

.club-info {
  flex: 1;
}

.club-name {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.club-role {
  font-size: 24rpx;
  color: #5677fc;
  background: #e8ecff;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.club-stats {
  display: flex;
  justify-content: space-around;
  padding: 20rpx 0;
  border-top: 1rpx solid #f8f8f8;
  border-bottom: 1rpx solid #f8f8f8;
  margin-bottom: 24rpx;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 4rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

.club-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  flex: 1;
  height: 72rpx;
  background: #f5f6f8;
  color: #666;
  border-radius: 36rpx;
  font-size: 26rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.action-btn.danger {
  background: #ffebee;
  color: #dd524d;
}

.btn-icon {
  margin-right: 8rpx;
}

.empty-state {
  text-align: center;
  padding: 120rpx 0;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.join-btn {
  padding: 20rpx 60rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
