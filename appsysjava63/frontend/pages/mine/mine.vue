<template>
  <view class="mine-container">
    <view class="header">
      <view class="user-info" v-if="userStore.isLoggedIn">
        <image class="avatar" :src="getAvatar()" mode="aspectFill"></image>
        <view class="info">
          <text class="nickname">{{ getNickname() }}</text>
          <view class="credit-badge">
            <text class="credit-score">{{ getCreditScore() }}</text>
            <text class="credit-label">信用分</text>
          </view>
        </view>
        <text class="arrow">›</text>
      </view>
      <view class="user-info login-btn" v-else @click="goToLogin">
        <view class="avatar-placeholder">👤</view>
        <view class="info">
          <text class="nickname">点击登录</text>
          <text class="sub-title">登录后享受更多服务</text>
        </view>
        <text class="arrow">›</text>
      </view>
    </view>

    <view class="balance-card" v-if="userStore.isLoggedIn">
      <view class="balance-item">
        <text class="balance-value">¥{{ getBalance() }}</text>
        <text class="balance-label">账户余额</text>
      </view>
      <view class="balance-divider"></view>
      <view class="balance-item" @click="goToCoupons">
        <text class="balance-value">{{ couponCount }}</text>
        <text class="balance-label">优惠券</text>
      </view>
      <view class="balance-divider"></view>
      <view class="balance-item">
        <text class="balance-value">{{ totalRides }}</text>
        <text class="balance-label">骑行次数</text>
      </view>
    </view>

    <view class="menu-list">
      <view class="menu-section">
        <view class="menu-item" @click="goToRealName">
          <text class="menu-icon">📋</text>
          <text class="menu-title">实名认证</text>
          <text class="menu-status" :class="{ verified: isRealNameVerified() }">
            {{ isRealNameVerified() ? '已认证' : '未认证' }}
          </text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToDeposit">
          <text class="menu-icon">💰</text>
          <text class="menu-title">押金管理</text>
          <text class="menu-status" :class="{ paid: isDepositPaid() }">
            {{ isDepositPaid() ? '已缴纳' : '未缴纳' }}
          </text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToRecharge">
          <text class="menu-icon">💳</text>
          <text class="menu-title">账户充值</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item" @click="goToRecords">
          <text class="menu-icon">📊</text>
          <text class="menu-title">骑行记录</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToCredit">
          <text class="menu-icon">⭐</text>
          <text class="menu-title">信用分</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item" @click="goToCoupons">
          <text class="menu-icon">🎫</text>
          <text class="menu-title">我的优惠券</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-section">
        <view class="menu-item">
          <text class="menu-icon">📞</text>
          <text class="menu-title">客服中心</text>
          <text class="menu-arrow">›</text>
        </view>
        <view class="menu-item">
          <text class="menu-icon">⚙️</text>
          <text class="menu-title">设置</text>
          <text class="menu-arrow">›</text>
        </view>
      </view>

      <view class="menu-section" v-if="userStore.isLoggedIn">
        <view class="menu-item logout" @click="handleLogout">
          <text class="menu-title">退出登录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'

export default {
  setup() {
    const userStore = useUserStore()

    const couponCount = ref(3)
    const totalRides = ref(28)

    const getAvatar = () => {
      return userStore.userInfo && userStore.userInfo.avatar ? userStore.userInfo.avatar : '/static/default-avatar.png'
    }

    const getNickname = () => {
      return userStore.userInfo && userStore.userInfo.nickname ? userStore.userInfo.nickname : '骑行用户'
    }

    const getCreditScore = () => {
      return userStore.userInfo && userStore.userInfo.creditScore ? userStore.userInfo.creditScore : 100
    }

    const getBalance = () => {
      return userStore.userInfo && userStore.userInfo.balance ? userStore.userInfo.balance : 0
    }

    const isRealNameVerified = () => {
      return userStore.userInfo && userStore.userInfo.realNameVerified ? userStore.userInfo.realNameVerified : false
    }

    const isDepositPaid = () => {
      return userStore.userInfo && userStore.userInfo.depositStatus === 1
    }

    const goToLogin = () => {
      uni.navigateTo({ url: '/pages/login/login' })
    }

    const goToRealName = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/realname/realname' })
    }

    const goToDeposit = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/deposit/deposit' })
    }

    const goToRecharge = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/recharge/recharge' })
    }

    const goToRecords = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/records/records' })
    }

    const goToCredit = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/credit/credit' })
    }

    const goToCoupons = () => {
      if (!userStore.isLoggedIn) {
        goToLogin()
        return
      }
      uni.navigateTo({ url: '/pages/coupons/coupons' })
    }

    const handleLogout = () => {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            userStore.doLogout()
            uni.showToast({ title: '已退出登录', icon: 'success' })
          }
        }
      })
    }

    onMounted(() => {
      if (userStore.isLoggedIn) {
        userStore.fetchUserInfo()
      }
    })

    return {
      userStore,
      couponCount,
      totalRides,
      getAvatar,
      getNickname,
      getCreditScore,
      getBalance,
      isRealNameVerified,
      isDepositPaid,
      goToLogin,
      goToRealName,
      goToDeposit,
      goToRecharge,
      goToRecords,
      goToCredit,
      goToCoupons,
      handleLogout
    }
  }
}
</script>

<style lang="scss" scoped>
.mine-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  padding: 60px 20px 30px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  border: 3px solid white;
  background: white;
}

.avatar-placeholder {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: rgba(255,255,255,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
}

.info {
  flex: 1;
}

.nickname {
  display: block;
  font-size: 20px;
  font-weight: bold;
  color: white;
  margin-bottom: 8px;
}

.sub-title {
  font-size: 14px;
  color: rgba(255,255,255,0.8);
}

.credit-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: rgba(255,255,255,0.2);
  padding: 4px 12px;
  border-radius: 12px;
}

.credit-score {
  font-size: 18px;
  font-weight: bold;
  color: #ffd700;
}

.credit-label {
  font-size: 12px;
  color: white;
}

.arrow {
  font-size: 24px;
  color: rgba(255,255,255,0.6);
}

.balance-card {
  background: white;
  margin: -20px 16px 16px;
  border-radius: 16px;
  padding: 24px 16px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  position: relative;
  z-index: 10;
}

.balance-item {
  flex: 1;
  text-align: center;
}

.balance-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.balance-label {
  font-size: 12px;
  color: #999;
}

.balance-divider {
  width: 1px;
  height: 40px;
  background: #eee;
}

.menu-list {
  padding: 0 16px 16px;
}

.menu-section {
  background: white;
  border-radius: 16px;
  margin-bottom: 16px;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 20px;
  margin-right: 12px;
}

.menu-title {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.menu-status {
  font-size: 13px;
  color: #999;
  margin-right: 8px;
}

.menu-status.verified,
.menu-status.paid {
  color: #00A862;
}

.menu-arrow {
  font-size: 18px;
  color: #ccc;
}

.menu-item.logout {
  justify-content: center;
}

.menu-item.logout .menu-title {
  color: #f44336;
  text-align: center;
}
</style>
