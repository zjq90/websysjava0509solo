<template>
  <view class="container">
    <view class="header">
      <view class="user-info" v-if="userInfo" @click="goToLogin">
        <image :src="userInfo.avatar || '/static/default-avatar.png'" class="avatar"></image>
        <view class="user-text">
          <text class="username">{{ userInfo.nickname || userInfo.username || '点击登录' }}</text>
          <text class="member-level" v-if="userInfo.memberLevelId">{{ getMemberLevelText(userInfo.memberLevelId) }}</text>
        </view>
        <text class="points" v-if="userInfo.currentPoints">积分: {{ userInfo.currentPoints }}</text>
      </view>
      <view class="user-info" v-else @click="goToLogin">
        <view class="avatar-placeholder">👤</view>
        <view class="user-text">
          <text class="username">点击登录</text>
          <text class="member-level">登录享受更多优惠</text>
        </view>
      </view>
    </view>
    
    <view class="elder-mode-section" v-if="userInfo">
      <view class="elder-mode-item">
        <text class="elder-mode-icon">👵</text>
        <text class="elder-mode-text">长辈模式</text>
        <switch 
          :checked="elderMode === 1" 
          @change="toggleElderMode"
          color="#FF6B9D"
        />
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-item" @click="goToMember">
        <text class="menu-icon">👑</text>
        <text class="menu-text">会员中心</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToPoints">
        <text class="menu-icon">⭐</text>
        <text class="menu-text">积分商城</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToOrders">
        <text class="menu-icon">📋</text>
        <text class="menu-text">我的订单</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToAddress">
        <text class="menu-icon">📍</text>
        <text class="menu-text">收货地址</text>
        <text class="menu-arrow">></text>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-item" @click="goToDiy">
        <text class="menu-icon">💐</text>
        <text class="menu-text">我的DIY花束</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToEnterprise">
        <text class="menu-icon">🏢</text>
        <text class="menu-text">企业定制记录</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToCoupon">
        <text class="menu-icon">🎫</text>
        <text class="menu-text">优惠券</text>
        <text class="menu-arrow">></text>
      </view>
    </view>
    
    <view class="menu-section">
      <view class="menu-item" @click="goToSetting">
        <text class="menu-icon">⚙️</text>
        <text class="menu-text">设置</text>
        <text class="menu-arrow">></text>
      </view>
      <view class="menu-item" @click="goToAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-text">关于我们</text>
        <text class="menu-arrow">></text>
      </view>
    </view>
    
    <view class="menu-section" v-if="userInfo">
      <view class="menu-item" @click="logout">
        <text class="menu-icon">🚪</text>
        <text class="menu-text">退出登录</text>
        <text class="menu-arrow">></text>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      userInfo: null,
      elderMode: 0
    }
  },
  onShow() {
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      const token = uni.getStorageSync('token')
      if (token) {
        try {
          const res = await api.getUserInfo()
          this.userInfo = res.data
          this.elderMode = res.data.elderMode || 0
        } catch (e) {
          console.error('加载用户信息失败', e)
          this.userInfo = {
            id: 1,
            username: 'testuser',
            nickname: '测试用户',
            avatar: '',
            currentPoints: 520,
            memberLevelId: 1,
            elderMode: 0
          }
        }
      }
    },
    
    getMemberLevelText(level) {
      const levels = {
        1: '普通会员',
        2: 'VIP会员',
        3: '高级VIP'
      }
      return levels[level] || '普通会员'
    },
    
    goToLogin() {
      if (!this.userInfo) {
        uni.navigateTo({ url: '/pages/login/login' })
      }
    },
    
    async toggleElderMode(e) {
      const newMode = e.detail.value ? 1 : 0
      try {
        await api.toggleElderMode(newMode)
        this.elderMode = newMode
        uni.setStorageSync('elderMode', newMode)
        uni.showToast({
          title: newMode === 1 ? '已开启长辈模式' : '已关闭长辈模式',
          icon: 'success'
        })
      } catch (e) {
        console.error('切换长辈模式失败', e)
        this.elderMode = newMode
        uni.setStorageSync('elderMode', newMode)
      }
    },
    
    goToMember() {
      uni.navigateTo({ url: '/pages/member/member' })
    },
    
    goToPoints() {
      uni.navigateTo({ url: '/pages/points/points' })
    },
    
    goToOrders() {
      uni.showToast({ title: '订单功能开发中', icon: 'none' })
    },
    
    goToAddress() {
      uni.showToast({ title: '地址功能开发中', icon: 'none' })
    },
    
    goToDiy() {
      uni.navigateTo({ url: '/pages/diy/diy' })
    },
    
    goToEnterprise() {
      uni.navigateTo({ url: '/pages/enterprise/enterprise' })
    },
    
    goToCoupon() {
      uni.showToast({ title: '优惠券功能开发中', icon: 'none' })
    },
    
    goToSetting() {
      uni.showToast({ title: '设置功能开发中', icon: 'none' })
    },
    
    goToAbout() {
      uni.showToast({ title: '关于我们功能开发中', icon: 'none' })
    },
    
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            this.userInfo = null
            uni.showToast({ title: '已退出登录', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  padding: 60rpx 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid #fff;
  margin-right: 30rpx;
}

.avatar-placeholder {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  border: 4rpx solid #fff;
  background: rgba(255,255,255,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 60rpx;
  margin-right: 30rpx;
}

.user-text {
  flex: 1;
}

.username {
  font-size: 36rpx;
  color: #fff;
  font-weight: bold;
  display: block;
  margin-bottom: 10rpx;
}

.member-level {
  font-size: 24rpx;
  color: rgba(255,255,255,0.9);
  display: block;
}

.points {
  font-size: 28rpx;
  color: #fff;
  background: rgba(255,255,255,0.2);
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
}

.elder-mode-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.elder-mode-item {
  display: flex;
  align-items: center;
}

.elder-mode-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.elder-mode-text {
  font-size: 30rpx;
  color: #333;
  flex: 1;
}

.menu-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
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
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 28rpx;
  color: #ccc;
}
</style>