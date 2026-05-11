<template>
  <view class="home-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="header">
      <view class="header-content">
        <view class="user-info" v-if="userInfo" @click="goToCenter">
          <view class="avatar">
            <text class="avatar-text">{{ userInfo.realName ? userInfo.realName.charAt(0) : 'U' }}</text>
          </view>
          <view class="user-detail">
            <text class="user-name">{{ userInfo.realName || userInfo.username }}</text>
            <text class="user-hint">点击查看个人中心</text>
          </view>
        </view>
        <view class="user-info login-link" v-else @click="goToLogin">
          <view class="avatar">
            <text class="avatar-text">👤</text>
          </view>
          <view class="user-detail">
            <text class="user-name">点击登录</text>
            <text class="user-hint">登录后享受更多服务</text>
          </view>
        </view>
        <view class="search-btn" @click="goToSearch">
          <text class="search-icon">🔍</text>
        </view>
      </view>
    </view>

    <view class="quick-actions">
      <view class="action-item" @click="goToDoctorList">
        <view class="action-icon" style="background: #e6f7ff">
          <text>👨‍⚕️</text>
        </view>
        <text class="action-text">预约挂号</text>
      </view>
      <view class="action-item" @click="goToAppointments">
        <view class="action-icon" style="background: #f6ffed">
          <text>📋</text>
        </view>
        <text class="action-text">我的预约</text>
      </view>
      <view class="action-item" @click="goToPatients">
        <view class="action-icon" style="background: #fff7e6">
          <text>👨‍👩‍👧</text>
        </view>
        <text class="action-text">就诊人</text>
      </view>
      <view class="action-item" @click="showElderMode">
        <view class="action-icon" style="background: #fff1f0">
          <text>👴</text>
        </view>
        <text class="action-text">{{ elderMode ? '关闭长辈' : '长辈模式' }}</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">热门科室</text>
        <text class="section-more" @click="goToDoctorList">更多 ></text>
      </view>
      <view class="dept-grid">
        <view class="dept-item" v-for="dept in departments" :key="dept.id" @click="goToDeptDoctors(dept.id)">
          <view class="dept-icon">{{ getDeptIcon(dept.deptCode) }}</view>
          <text class="dept-name">{{ dept.deptName }}</text>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">推荐医生</text>
        <text class="section-more" @click="goToDoctorList">更多 ></text>
      </view>
      <view class="doctor-list">
        <view class="doctor-card" v-for="doctor in doctors" :key="doctor.id" @click="goToDoctorDetail(doctor.id)">
          <view class="doctor-avatar">
            <text class="avatar-icon">👨‍⚕️</text>
          </view>
          <view class="doctor-info">
            <view class="doctor-header">
              <text class="doctor-name">{{ doctor.doctorName }}</text>
              <text class="doctor-title" v-if="doctor.title">{{ doctor.title }}</text>
              <text class="expert-tag" v-if="doctor.isExpert === 1">专家</text>
            </view>
            <text class="doctor-dept">{{ doctor.deptName }}</text>
            <text class="doctor-specialty">{{ doctor.specialty }}</text>
            <view class="doctor-footer">
              <text class="fee">¥{{ doctor.consultationFee }}</text>
              <text class="available">可预约</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="section tips-section">
      <view class="tip-card">
        <view class="tip-icon">💡</view>
        <view class="tip-content">
          <text class="tip-title">挂号小贴士</text>
          <text class="tip-desc">选定号源后将锁定10分钟，请尽快完成支付，超时将自动释放号源</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onPullDownRefresh } from 'vue'
import { useStore } from 'vuex'
import { departmentApi, doctorApi } from '@/utils/api'

const store = useStore()

const departments = ref([])
const doctors = ref([])

const userInfo = computed(() => store.getters.userInfo)
const elderMode = computed(() => store.getters.elderMode)

const getDeptIcon = (code) => {
  const icons = {
    'INTERNAL': '❤️',
    'SURGERY': '🩹',
    'OBGYN': '👶',
    'PEDIATRICS': '🧒',
    'ORTHOPEDICS': '🦴',
    'OPHTHALMOLOGY': '👁️',
    'ENT': '👂',
    'DERMATOLOGY': '🧴'
  }
  return icons[code] || '🏥'
}

const loadData = async () => {
  try {
    const [deptData, doctorData] = await Promise.all([
      departmentApi.getAll(),
      doctorApi.getAll({})
    ])
    departments.value = deptData.slice(0, 8)
    doctors.value = doctorData.slice(0, 3)
  } catch (e) {
    console.error('加载数据失败:', e)
  } finally {
    uni.stopPullDownRefresh()
  }
}

const goToLogin = () => {
  uni.navigateTo({ url: '/pages/login/login' })
}

const goToCenter = () => {
  uni.switchTab({ url: '/pages/user/user-center' })
}

const goToDoctorList = () => {
  uni.navigateTo({ url: '/pages/doctor/doctor-list' })
}

const goToDeptDoctors = (deptId) => {
  uni.navigateTo({ url: `/pages/doctor/doctor-list?deptId=${deptId}` })
}

const goToDoctorDetail = (doctorId) => {
  uni.navigateTo({ url: `/pages/doctor/doctor-detail?id=${doctorId}` })
}

const goToAppointments = () => {
  if (!store.getters.isLoggedIn) {
    uni.navigateTo({ url: '/pages/login/login' })
    return
  }
  uni.switchTab({ url: '/pages/appointment/appointment-list' })
}

const goToPatients = () => {
  if (!store.getters.isLoggedIn) {
    uni.navigateTo({ url: '/pages/login/login' })
    return
  }
  uni.navigateTo({ url: '/pages/patient/patient-list' })
}

const goToSearch = () => {
  uni.showToast({ title: '搜索功能开发中', icon: 'none' })
}

const showElderMode = async () => {
  store.dispatch('toggleElderMode')
  if (store.getters.isLoggedIn) {
    try {
      await authApi.toggleElderMode(store.getters.elderMode ? 1 : 0)
    } catch (e) {}
  }
  uni.showToast({ 
    title: store.getters.elderMode ? '已开启长辈模式' : '已关闭长辈模式', 
    icon: 'none' 
  })
}

onMounted(() => {
  loadData()
})

onPullDownRefresh(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 80rpx 32rpx 60rpx;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-text {
  font-size: 44rpx;
  color: #fff;
}

.avatar-icon {
  font-size: 48rpx;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #fff;
}

.user-hint {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 8rpx;
}

.search-btn {
  width: 80rpx;
  height: 80rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-icon {
  font-size: 36rpx;
}

.quick-actions {
  background: #fff;
  margin: -30rpx 24rpx 24rpx;
  border-radius: 20rpx;
  padding: 40rpx 20rpx;
  display: flex;
  justify-content: space-around;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.action-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 44rpx;
  margin-bottom: 16rpx;
}

.action-text {
  font-size: 26rpx;
  color: #333;
}

.section {
  padding: 0 24rpx;
  margin-bottom: 24rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 26rpx;
  color: #999;
}

.dept-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx 20rpx;
}

.dept-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16rpx 0;
}

.dept-icon {
  font-size: 44rpx;
  margin-bottom: 12rpx;
}

.dept-name {
  font-size: 24rpx;
  color: #333;
  text-align: center;
}

.doctor-list {
  background: #fff;
  border-radius: 20rpx;
  padding: 20rpx;
}

.doctor-card {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
}

.doctor-card:last-child {
  border-bottom: none;
}

.doctor-avatar {
  width: 120rpx;
  height: 120rpx;
  background: #e6f7ff;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.doctor-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.doctor-header {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.doctor-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-right: 16rpx;
}

.doctor-title {
  font-size: 24rpx;
  color: #1890ff;
  margin-right: 12rpx;
}

.expert-tag {
  font-size: 20rpx;
  color: #ff4d4f;
  background: #fff1f0;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.doctor-dept {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.doctor-specialty {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 16rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.doctor-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fee {
  font-size: 28rpx;
  color: #ff4d4f;
  font-weight: bold;
}

.available {
  font-size: 24rpx;
  color: #52c41a;
}

.tips-section {
  padding: 0 24rpx;
}

.tip-card {
  display: flex;
  align-items: flex-start;
  background: linear-gradient(135deg, #fffbe6 0%, #fff1b8 100%);
  border-radius: 20rpx;
  padding: 30rpx;
}

.tip-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.tip-content {
  flex: 1;
}

.tip-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #d48806;
  margin-bottom: 8rpx;
}

.tip-desc {
  font-size: 24rpx;
  color: #ad6800;
  line-height: 1.6;
}

.elder-mode-enabled {
  .action-text {
    font-size: 30rpx;
  }
  
  .dept-name {
    font-size: 28rpx;
  }
  
  .doctor-name {
    font-size: 34rpx;
  }
  
  .section-title {
    font-size: 36rpx;
  }
  
  .tip-title {
    font-size: 32rpx;
  }
  
  .tip-desc {
    font-size: 28rpx;
  }
}
</style>
