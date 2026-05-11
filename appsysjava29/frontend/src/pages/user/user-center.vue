<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="user-header" v-if="userInfo">
      <view class="avatar">
        <text class="avatar-text">{{ userInfo.realName ? userInfo.realName.charAt(0) : 'U' }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ userInfo.realName || userInfo.username }}</text>
        <text class="user-username">@{{ userInfo.username }}</text>
      </view>
    </view>

    <view class="user-header login-prompt" v-else @click="goToLogin">
      <view class="avatar">
        <text>👤</text>
      </view>
      <view class="user-info">
        <text class="user-name">点击登录</text>
        <text class="user-username">登录后享受更多服务</text>
      </view>
    </view>

    <view class="menu-section" v-if="isLoggedIn">
      <view class="menu-item" @click="goToAppointments">
        <view class="menu-left">
          <text class="menu-icon">📋</text>
          <text class="menu-text">我的预约</text>
        </view>
        <text class="menu-arrow">></text>
      </view>

      <view class="menu-item" @click="goToPatients">
        <view class="menu-left">
          <text class="menu-icon">👨‍👩‍👧</text>
          <text class="menu-text">就诊人管理</text>
        </view>
        <text class="menu-arrow">></text>
      </view>

      <view class="menu-item">
        <view class="menu-left">
          <text class="menu-icon">👴</text>
          <text class="menu-text">长辈模式</text>
        </view>
        <switch :checked="elderMode" @change="toggleElderMode" color="#1890ff" />
      </view>
    </view>

    <view class="menu-section" v-if="isLoggedIn">
      <view class="menu-item" @click="goToProfile">
        <view class="menu-left">
          <text class="menu-icon">👤</text>
          <text class="menu-text">个人信息</text>
        </view>
        <text class="menu-arrow">></text>
      </view>

      <view class="menu-item" @click="goToPassword">
        <view class="menu-left">
          <text class="menu-icon">🔐</text>
          <text class="menu-text">修改密码</text>
        </view>
        <text class="menu-arrow">></text>
      </view>
    </view>

    <view class="menu-section" v-if="isLoggedIn">
      <view class="menu-item" @click="showAbout">
        <view class="menu-left">
          <text class="menu-icon">ℹ️</text>
          <text class="menu-text">关于我们</text>
        </view>
        <text class="menu-arrow">></text>
      </view>

      <view class="menu-item" @click="showHelp">
        <view class="menu-left">
          <text class="menu-icon">❓</text>
          <text class="menu-text">帮助中心</text>
        </view>
        <text class="menu-arrow">></text>
      </view>
    </view>

    <view class="logout-section" v-if="isLoggedIn">
      <view class="logout-btn" @click="logout">
        <text>退出登录</text>
      </view>
    </view>

    <view class="version-info">
      <text>智慧医院 v1.0.0</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onShow } from 'vue'
import { useStore } from 'vuex'
import { authApi } from '@/utils/api'

const store = useStore()

const userInfo = computed(() => store.getters.userInfo)
const isLoggedIn = computed(() => store.getters.isLoggedIn)
const elderMode = computed(() => store.getters.elderMode)

const loadUserInfo = async () => {
  if (!isLoggedIn.value) return
  
  try {
    const info = await authApi.getUserInfo()
    store.commit('SET_USER_INFO', info)
  } catch (e) {
    console.error('加载用户信息失败:', e)
  }
}

const goToLogin = () => {
  uni.navigateTo({ url: '/pages/login/login' })
}

const goToAppointments = () => {
  uni.switchTab({ url: '/pages/appointment/appointment-list' })
}

const goToPatients = () => {
  uni.navigateTo({ url: '/pages/patient/patient-list' })
}

const goToProfile = () => {
  uni.showToast({ title: '个人信息页面开发中', icon: 'none' })
}

const goToPassword = () => {
  uni.showToast({ title: '修改密码页面开发中', icon: 'none' })
}

const toggleElderMode = async () => {
  store.dispatch('toggleElderMode')
  try {
    await authApi.toggleElderMode(elderMode.value ? 1 : 0)
  } catch (e) {}
}

const showAbout = () => {
  uni.showModal({
    title: '关于我们',
    content: '智慧医院预约挂号系统\n\n版本：v1.0.0\n\n致力于为用户提供便捷、安全的在线预约挂号服务。',
    showCancel: false
  })
}

const showHelp = () => {
  uni.showModal({
    title: '帮助中心',
    content: '常见问题：\n\n1. 如何预约挂号？\n点击首页"预约挂号"，选择科室和医生，按提示完成预约。\n\n2. 号源锁定时间？\n选定号源后将锁定10分钟，请尽快完成支付。\n\n3. 如何取消预约？\n进入预约详情页，点击"取消预约"即可。',
    showCancel: false
  })
}

const logout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        store.dispatch('logout')
        uni.showToast({ title: '已退出登录', icon: 'success' })
      }
    }
  })
}

onShow(() => {
  loadUserInfo()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 60rpx;
}

.user-header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 60rpx 40rpx;
  display: flex;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 28rpx;
  font-size: 48rpx;
}

.avatar-text {
  font-size: 52rpx;
  font-weight: bold;
  color: #fff;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 8rpx;
}

.user-username {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx 28rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-left {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.menu-icon {
  font-size: 36rpx;
}

.menu-text {
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 28rpx;
  color: #ccc;
}

.logout-section {
  padding: 40rpx;
}

.logout-btn {
  height: 88rpx;
  line-height: 88rpx;
  background: #fff;
  text-align: center;
  border-radius: 44rpx;
  font-size: 30rpx;
  color: #ff4d4f;
  border: 2rpx solid #ff4d4f;
}

.version-info {
  text-align: center;
  padding: 40rpx 0;
  font-size: 24rpx;
  color: #ccc;
}

.elder-mode-enabled {
  .user-name {
    font-size: 40rpx;
  }
  
  .menu-text {
    font-size: 32rpx;
  }
  
  .logout-btn {
    font-size: 34rpx;
  }
}
</style>
