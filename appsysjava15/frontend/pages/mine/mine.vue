<template>
  <view class="container">
    <view class="user-card card">
      <view class="user-avatar">
        <text class="avatar-text">{{ userInfo ? userInfo.realName.charAt(0) : '?' }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo ? userInfo.realName : '未登录' }}</text>
        <text class="user-role">{{ getRoleText(userInfo && userInfo.role) }}</text>
        <text class="user-username" v-if="userInfo">@{{ userInfo.username }}</text>
      </view>
    </view>

    <view class="menu-list card">
      <view class="menu-item" @click="goToScan">
        <text class="menu-icon">📷</text>
        <text class="menu-text">扫码查询</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="goToAlerts">
        <text class="menu-icon">🔔</text>
        <text class="menu-text">预警通知</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="showAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-text">关于系统</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="menu-list card">
      <view class="menu-item" @click="goToTestData">
        <text class="menu-icon">🧪</text>
        <text class="menu-text">测试数据说明</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="checkTimeout">
        <text class="menu-icon">⏰</text>
        <text class="menu-text">触发超时预警（测试）</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <view class="logout-btn" v-if="userInfo" @click="handleLogout">
      <text>退出登录</text>
    </view>

    <view class="login-btn" v-else @click="goToLogin">
      <text>去登录</text>
    </view>

    <view class="version-info">
      <text>生产批次管理系统 v1.0.0</text>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      userInfo: null
    }
  },
  onShow() {
    this.userInfo = uni.getStorageSync('userInfo')
  },
  methods: {
    getRoleText(role) {
      const map = {
        'ADMIN': '系统管理员',
        'SUPERVISOR': '生产主管',
        'OPERATOR': '生产操作员'
      }
      return map[role] || role || '-'
    },
    goToLogin() {
      uni.navigateTo({
        url: '/pages/login/login'
      })
    },
    goToScan() {
      uni.switchTab({
        url: '/pages/scan/scan'
      })
    },
    goToAlerts() {
      uni.switchTab({
        url: '/pages/alerts/alerts'
      })
    },
    showAbout() {
      uni.showModal({
        title: '关于系统',
        content: '生产批次管理系统\n\n版本：1.0.0\n\n技术栈：\n后端：Spring Boot + H2 + Swagger\n前端：uni-app\n\n功能：生产批次进度追踪、扫码质检、异常预警、加工报告生成',
        showCancel: false
      })
    },
    goToTestData() {
      uni.showModal({
        title: '测试数据说明',
        content: '系统已预设以下测试数据：\n\n📦 生产批次：3个\n- BATCH001：进行中（包衣环节）\n- BATCH002：已完成\n- BATCH003：待开始\n\n👤 用户账号：\n- admin/123456（管理员）\n- supervisor/123456（主管）\n- operator1/123456（操作员）\n- operator2/123456（操作员）\n\n🔬 质检记录：已预置部分数据',
        showCancel: false
      })
    },
    async checkTimeout() {
      uni.showLoading({ title: '检查中...' })
      try {
        await api.checkTimeout()
        uni.hideLoading()
        uni.showToast({
          title: '已触发超时检查',
          icon: 'success'
        })
      } catch (e) {
        uni.hideLoading()
      }
    },
    handleLogout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出当前账号吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('userInfo')
            this.userInfo = null
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

<style scoped>
.user-card {
  display: flex;
  align-items: center;
  padding: 40rpx 30rpx;
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
}

.user-avatar {
  width: 120rpx;
  height: 120rpx;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.avatar-text {
  font-size: 48rpx;
  font-weight: bold;
  color: #ffffff;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 34rpx;
  font-weight: bold;
  color: #ffffff;
  margin-bottom: 8rpx;
}

.user-role {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 6rpx;
}

.user-username {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
}

.menu-list {
  margin-top: 24rpx;
  padding: 0;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #303133;
}

.menu-arrow {
  font-size: 36rpx;
  color: #c0c4cc;
}

.logout-btn {
  background-color: #ffffff;
  color: #f56c6c;
  border-radius: 16rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  margin-top: 40rpx;
}

.login-btn {
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
  color: #ffffff;
  border-radius: 16rpx;
  height: 88rpx;
  line-height: 88rpx;
  text-align: center;
  font-size: 30rpx;
  margin-top: 40rpx;
}

.version-info {
  text-align: center;
  margin-top: 60rpx;
  font-size: 24rpx;
  color: #c0c4cc;
}
</style>
