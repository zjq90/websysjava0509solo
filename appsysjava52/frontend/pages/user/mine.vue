<template>
  <view class="mine-page">
    <!-- 头部信息 -->
    <view class="header">
      <view class="user-info">
        <image class="avatar" :src="user.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
        <view class="user-detail">
          <text class="username">{{ user.nickname || '文物收藏家' }}</text>
          <text class="user-type" v-if="user.userType === 2">专家认证</text>
          <text class="user-type" v-else-if="user.userType === 3">管理员</text>
        </view>
      </view>
      <view class="elder-mode-toggle" @click="toggleElderMode">
        <text class="toggle-label">长辈模式</text>
        <switch :checked="elderModeEnabled" color="#667eea" />
      </view>
    </view>

    <!-- 收藏统计 -->
    <view class="stats-card card">
      <view class="stat-item">
        <text class="stat-number">{{ collectionStats.total }}</text>
        <text class="stat-label">收藏总数</text>
      </view>
      <view class="stat-item">
        <text class="stat-number">{{ collectionStats.monitoring }}</text>
        <text class="stat-label">环境监测</text>
      </view>
      <view class="stat-item">
        <text class="stat-number">{{ collectionStats.value }}</text>
        <text class="stat-label">估值(万)</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-group card">
        <view class="menu-item" @click="goToCollection">
          <text class="menu-icon">📦</text>
          <text class="menu-title">我的收藏</text>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="goToAuthList">
          <text class="menu-icon">🔍</text>
          <text class="menu-title">鉴定申请</text>
          <view class="menu-badge" v-if="authPendingCount > 0">{{ authPendingCount }}</view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="goToTrace">
          <text class="menu-icon">🔗</text>
          <text class="menu-title">溯源查询</text>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <view class="menu-group card">
        <view class="menu-item" @click="goToMaintenance">
          <text class="menu-icon">🔧</text>
          <text class="menu-title">保养提醒</text>
          <view class="menu-badge warning" v-if="maintenancePendingCount > 0">{{ maintenancePendingCount }}</view>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="goToExpertApply">
          <text class="menu-icon">🎓</text>
          <text class="menu-title">专家认证申请</text>
          <text class="menu-arrow">></text>
        </view>
      </view>

      <view class="menu-group card">
        <view class="menu-item" @click="goToSettings">
          <text class="menu-icon">⚙️</text>
          <text class="menu-title">系统设置</text>
          <text class="menu-arrow">></text>
        </view>
        <view class="menu-item" @click="goToAbout">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-title">关于我们</text>
          <text class="menu-arrow">></text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-section">
      <button class="logout-btn" @click="logout">退出登录</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      user: {
        nickname: '文物收藏家',
        userType: 1,
        avatar: ''
      },
      elderModeEnabled: false,
      collectionStats: {
        total: 12,
        monitoring: 5,
        value: '328.5'
      },
      authPendingCount: 2,
      maintenancePendingCount: 3
    }
  },
  onLoad() {
    this.loadUserInfo()
    this.elderModeEnabled = uni.getStorageSync('elderMode') == 1
  },
  onShow() {
    this.loadStats()
  },
  methods: {
    loadUserInfo() {
      // 加载用户信息
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo) {
        this.user = userInfo
      }
    },
    loadStats() {
      // 加载统计数据
    },
    toggleElderMode() {
      this.elderModeEnabled = !this.elderModeEnabled
      uni.setStorageSync('elderMode', this.elderModeEnabled ? 1 : 0)
      
      // 长辈模式：放大字体，简化界面
      if (this.elderModeEnabled) {
        uni.showToast({
          title: '已开启长辈模式',
          icon: 'success'
        })
      } else {
        uni.showToast({
          title: '已关闭长辈模式',
          icon: 'success'
        })
      }
    },
    goToCollection() {
      uni.switchTab({
        url: '/pages/collection/list'
      })
    },
    goToAuthList() {
      uni.switchTab({
        url: '/pages/authentication/list'
      })
    },
    goToTrace() {
      uni.switchTab({
        url: '/pages/trace/index'
      })
    },
    goToMaintenance() {
      uni.navigateTo({
        url: '/pages/collection/detail?id=1'
      })
    },
    goToExpertApply() {
      uni.showToast({
        title: '专家认证功能开发中',
        icon: 'none'
      })
    },
    goToSettings() {
      uni.showToast({
        title: '设置功能开发中',
        icon: 'none'
      })
    },
    goToAbout() {
      uni.showToast({
        title: '关于功能开发中',
        icon: 'none'
      })
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.reLaunch({
              url: '/pages/login/login'
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.mine-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 30rpx 40rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: #fff;
  margin-right: 24rpx;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.user-detail {
  flex: 1;
}

.username {
  display: block;
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
  margin-bottom: 8rpx;
}

.user-type {
  display: inline-block;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.elder-mode-toggle {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.15);
  padding: 20rpx 24rpx;
  border-radius: 12rpx;
}

.toggle-label {
  font-size: 28rpx;
  color: #fff;
}

.stats-card {
  display: flex;
  margin: -30rpx 20rpx 20rpx;
  position: relative;
  z-index: 10;
}

.stat-item {
  flex: 1;
  text-align: center;
  padding: 30rpx 0;
}

.stat-number {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #667eea;
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
  margin-bottom: 20rpx;
  padding: 0;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx 24rpx;
  border-bottom: 1rpx solid #f5f5f5;
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
  font-size: 30rpx;
  color: #333;
}

.menu-badge {
  background: #ff6b6b;
  color: #fff;
  padding: 4rpx 12rpx;
  border-radius: 20rpx;
  font-size: 20rpx;
  margin-right: 12rpx;
}

.menu-badge.warning {
  background: #ff9800;
}

.menu-arrow {
  font-size: 28rpx;
  color: #ccc;
}

.logout-section {
  padding: 40rpx 20rpx;
}

.logout-btn {
  width: 100%;
  background: #fff;
  color: #ff6b6b;
  border: 2rpx solid #ff6b6b;
  border-radius: 12rpx;
  padding: 28rpx;
  font-size: 30rpx;
}

/* 长辈模式样式 */
.elder-mode .username {
  font-size: 44rpx;
}

.elder-mode .menu-title {
  font-size: 36rpx;
}

.elder-mode .stat-number {
  font-size: 48rpx;
}

.elder-mode .stat-label {
  font-size: 28rpx;
}

.elder-mode .logout-btn {
  font-size: 36rpx;
  padding: 36rpx;
}
</style>