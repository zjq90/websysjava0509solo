<template>
  <view class="mine-container">
    <view class="user-header">
      <view class="user-avatar" @click="goProfile">
        <image v-if="userInfo && userInfo.avatar" :src="userInfo.avatar" class="avatar-img" mode="aspectFill" />
        <view v-else class="avatar-placeholder">
          <text>{{ userInfo && userInfo.nickname ? userInfo.nickname.substring(0, 1) : '用' }}</text>
        </view>
      </view>
      <view class="user-info" @click="goProfile">
        <text class="user-name">{{ userInfo ? (userInfo.nickname || userInfo.username) : '未登录' }}</text>
        <text class="user-desc">
          {{ userInfo && userInfo.college ? userInfo.college : '完善个人信息' }}
          <text class="arrow">›</text>
        </text>
      </view>
      <view class="setting-btn" @click="goSetting">
        <text>⚙️</text>
      </view>
    </view>
    
    <view class="stats-section">
      <view class="stat-item" @click="goMyClubs">
        <text class="stat-num">{{ stats.clubs }}</text>
        <text class="stat-label">我的社团</text>
      </view>
      <view class="stat-item" @click="goMyActivities">
        <text class="stat-num">{{ stats.activities }}</text>
        <text class="stat-label">参与活动</text>
      </view>
      <view class="stat-item" @click="goApplications">
        <text class="stat-num">{{ stats.applications }}</text>
        <text class="stat-label">入团申请</text>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goMyClubs">
          <text class="menu-icon">🏫</text>
          <text class="menu-title">我的社团</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goMyActivities">
          <text class="menu-icon">📅</text>
          <text class="menu-title">参与的活动</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goApplications">
          <text class="menu-icon">📝</text>
          <text class="menu-title">我的申请</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
      
      <view class="menu-group">
        <view class="menu-item" @click="goProfile">
          <text class="menu-icon">👤</text>
          <text class="menu-title">个人资料</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goNotificationSetting">
          <text class="menu-icon">🔔</text>
          <text class="menu-title">通知设置</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goMessage">
          <text class="menu-icon">💬</text>
          <text class="menu-title">系统消息</text>
          <text class="menu-arrow">›</text>
          <text v-if="unreadCount > 0" class="menu-badge">{{ unreadCount }}</text>
        </view>
      </view>
      
      <view class="menu-group">
        <view class="menu-item" @click="clearCache">
          <text class="menu-icon">🗑️</text>
          <text class="menu-title">清除缓存</text>
          <text class="menu-arrow">{{ cacheSize }}</text>
        </view>
        <view class="menu-item" @click="aboutUs">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-title">关于我们</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>
    </view>
    
    <button class="logout-btn" @click="handleLogout" v-if="userInfo">
      退出登录
    </button>
  </view>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      stats: {
        clubs: 0,
        activities: 0,
        applications: 0
      },
      cacheSize: '0.0MB'
    }
  },
  computed: {
    ...mapGetters(['userInfo', 'unreadCount'])
  },
  onShow() {
    this.loadUserData()
    this.calculateCache()
  },
  methods: {
    ...mapActions(['logout', 'fetchUnreadCount']),
    
    async loadUserData() {
      try {
        const [clubsRes, actRes, appRes, unreadRes] = await Promise.all([
          api.getMyClubs(),
          api.getMyActivities(),
          api.getMyApplications(),
          this.fetchUnreadCount()
        ])
        
        this.stats.clubs = clubsRes.data ? clubsRes.data.length : 0
        this.stats.activities = actRes.data ? actRes.data.length : 0
        this.stats.applications = appRes.data ? appRes.data.length : 0
      } catch (e) {
        console.error(e)
      }
    },
    
    calculateCache() {
      plus.cache.calculate((size) => {
        this.cacheSize = (size / 1024 / 1024).toFixed(2) + 'MB'
      })
    },
    
    goProfile() {
      uni.navigateTo({ url: '/pages/mine/profile' })
    },
    
    goSetting() {
      uni.navigateTo({ url: '/pages/message/setting' })
    },
    
    goMyClubs() {
      uni.navigateTo({ url: '/pages/mine/my-clubs' })
    },
    
    goMyActivities() {
      uni.navigateTo({ url: '/pages/mine/my-activities' })
    },
    
    goApplications() {
      uni.navigateTo({ url: '/pages/mine/applications' })
    },
    
    goNotificationSetting() {
      uni.navigateTo({ url: '/pages/message/setting' })
    },
    
    goMessage() {
      uni.navigateTo({ url: '/pages/message/index' })
    },
    
    clearCache() {
      uni.showModal({
        title: '清除缓存',
        content: '确定要清除缓存吗？',
        success: async (res) => {
          if (res.confirm) {
            plus.cache.clear(() => {
              this.cacheSize = '0.0MB'
              util.showToast('缓存清除成功', 'success')
            })
          }
        }
      })
    },
    
    aboutUs() {
      uni.showModal({
        title: '关于社团管理',
        content: '大学生社团管理系统 v1.0.0\n\n致力于为大学生提供便捷的社团管理服务，让社团活动更加丰富多彩。',
        showCancel: false
      })
    },
    
    async handleLogout() {
      const confirm = await util.showModal('提示', '确定要退出登录吗？')
      if (confirm) {
        try {
          await this.logout()
          util.showToast('已退出登录', 'success')
          setTimeout(() => {
            uni.reLaunch({ url: '/pages/login/login' })
          }, 1000)
        } catch (e) {
          console.error(e)
          uni.reLaunch({ url: '/pages/login/login' })
        }
      }
    }
  }
}
</script>

<style scoped>
.mine-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 60rpx;
}

.user-header {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 60rpx 30rpx 80rpx;
  display: flex;
  align-items: center;
  position: relative;
}

.user-avatar {
  width: 140rpx;
  height: 140rpx;
  margin-right: 25rpx;
  flex-shrink: 0;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.avatar-placeholder text {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.user-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
  display: flex;
  align-items: center;
}

.arrow {
  font-size: 28rpx;
  margin-left: 8rpx;
}

.setting-btn {
  padding: 15rpx;
  font-size: 36rpx;
}

.stats-section {
  display: flex;
  background: #fff;
  margin: -40rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx 0;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 10;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-num {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #5677fc;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

.menu-section {
  padding: 0 20rpx;
}

.menu-group {
  background: #fff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx 25rpx;
  border-bottom: 1rpx solid #f8f8f8;
  position: relative;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.menu-title {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #ccc;
}

.menu-badge {
  position: absolute;
  right: 50rpx;
  min-width: 36rpx;
  height: 36rpx;
  background: #dd524d;
  color: #fff;
  border-radius: 18rpx;
  font-size: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.logout-btn {
  margin: 40rpx 20rpx 0;
  height: 90rpx;
  background: #fff;
  color: #dd524d;
  border-radius: 45rpx;
  font-size: 30rpx;
  border: 2rpx solid #dd524d;
}
</style>
