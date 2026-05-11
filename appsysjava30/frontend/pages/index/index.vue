<template>
  <view class="index-container" :class="{ 'elderly-mode': isElderlyMode }">
    <view class="header">
      <view class="user-info">
        <text class="greeting">你好，{{ userInfo.realName || '用户' }}</text>
        <text class="date">{{ todayStr }}</text>
      </view>
      <view class="elderly-toggle" @click="toggleElderlyMode">
        <text>{{ isElderlyMode ? '👵 退出长辈模式' : '👵 长辈模式' }}</text>
      </view>
    </view>
    
    <view class="today-section card" v-if="todayAppointments.length > 0">
      <view class="section-header">
        <text class="section-title">📅 今日待就诊</text>
        <text class="section-more" @click="goRecords">查看全部</text>
      </view>
      <view 
        v-for="item in todayAppointments" 
        :key="item.id" 
        class="appointment-item"
        @click="goDetail(item.registrationNo)"
      >
        <view class="item-left">
          <view class="dept-name">{{ item.deptName }}</view>
          <view class="doctor-info">
            <text class="doctor-name">{{ item.doctorName }}</text>
            <text class="doctor-title">{{ item.doctorTitle }}</text>
          </view>
          <view class="time-info">
            <text class="visit-time">{{ item.timeSlot }}</text>
            <text class="tag tag-hightlight">{{ item.statusTag }}</text>
          </view>
        </view>
        <view class="item-right">
          <text class="arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="quick-actions card">
      <view class="section-title">快捷功能</view>
      <view class="action-grid">
        <view class="action-item" @click="goRegistration">
          <view class="action-icon registration">🏥</view>
          <text class="action-text">预约挂号</text>
        </view>
        <view class="action-item" @click="goRecords">
          <view class="action-icon record">📋</view>
          <text class="action-text">我的预约</text>
        </view>
        <view class="action-item" @click="goProfile">
          <view class="action-icon profile">👤</view>
          <text class="action-text">个人中心</text>
        </view>
        <view class="action-item" @click="goElderlyMode">
          <view class="action-icon elderly">👵</view>
          <text class="action-text">长辈模式</text>
        </view>
      </view>
    </view>
    
    <view class="departments-section card">
      <view class="section-header">
        <text class="section-title">🏥 科室列表</text>
      </view>
      <view class="dept-grid">
        <view 
          v-for="dept in departments" 
          :key="dept.id" 
          class="dept-item"
          @click="goRegistrationByDept(dept.deptCode)"
        >
          <text class="dept-name">{{ dept.deptName }}</text>
        </view>
      </view>
    </view>
    
    <view class="tips-section card">
      <view class="section-title">📌 温馨提示</view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">就诊前一日17:00前可自助取消</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">改约请先取消再重新挂号</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="tip-text">退费原路返回，微信/支付宝1-3个工作日到账</text>
      </view>
    </view>
  </view>
</template>

<script>
import { scheduleApi, registrationApi } from '@/utils/api.js'
import { formatDate, getToday } from '@/utils/date.js'

export default {
  data() {
    return {
      userInfo: {},
      todayStr: '',
      todayAppointments: [],
      departments: [],
      isElderlyMode: false
    }
  },
  
  onShow() {
    this.checkLogin()
    this.loadData()
    this.isElderlyMode = uni.getStorageSync('elderlyMode') || false
  },
  
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.reLaunch({ url: '/pages/login/login' })
        return
      }
      this.userInfo = uni.getStorageSync('userInfo') || {}
    },
    
    async loadData() {
      this.todayStr = formatDate(new Date(), 'yyyy年MM月dd日')
      
      try {
        const [deptRes, todayRes] = await Promise.all([
          scheduleApi.getDepartments(),
          registrationApi.today()
        ])
        this.departments = deptRes.data || []
        this.todayAppointments = todayRes.data || []
      } catch (e) {
        console.error('加载数据失败', e)
      }
    },
    
    toggleElderlyMode() {
      this.isElderlyMode = !this.isElderlyMode
      uni.setStorageSync('elderlyMode', this.isElderlyMode)
      uni.showToast({
        title: this.isElderlyMode ? '已进入长辈模式' : '已退出长辈模式',
        icon: 'none'
      })
    },
    
    goRegistration() {
      uni.navigateTo({ url: '/pages/registration/registration' })
    },
    
    goRegistrationByDept(deptCode) {
      uni.navigateTo({ url: `/pages/registration/registration?deptCode=${deptCode}` })
    },
    
    goRecords() {
      uni.switchTab({ url: '/pages/record/record' })
    },
    
    goProfile() {
      uni.switchTab({ url: '/pages/profile/profile' })
    },
    
    goElderlyMode() {
      uni.navigateTo({ url: '/pages/elderly/elderly' })
    },
    
    goDetail(registrationNo) {
      uni.navigateTo({ url: `/pages/detail/detail?registrationNo=${registrationNo}` })
    }
  }
}
</script>

<style scoped>
.index-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 40rpx 32rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  color: #fff;
}

.greeting {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.date {
  font-size: 26rpx;
  opacity: 0.9;
}

.elderly-toggle {
  background: rgba(255, 255, 255, 0.2);
  padding: 12rpx 24rpx;
  border-radius: 24rpx;
  color: #fff;
  font-size: 26rpx;
}

.today-section {
  margin-top: 32rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 26rpx;
  color: #1890ff;
}

.appointment-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  background: #fafafa;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
}

.item-left {
  flex: 1;
}

.dept-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.doctor-info {
  margin-bottom: 12rpx;
}

.doctor-name {
  font-size: 28rpx;
  color: #666;
  margin-right: 12rpx;
}

.doctor-title {
  font-size: 24rpx;
  color: #999;
}

.time-info {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.visit-time {
  font-size: 26rpx;
  color: #1890ff;
}

.arrow {
  font-size: 40rpx;
  color: #ccc;
}

.quick-actions {
  margin-top: 32rpx;
}

.action-grid {
  display: flex;
  flex-wrap: wrap;
  margin-top: 24rpx;
}

.action-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.action-icon {
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.action-icon.registration {
  background: #e6f7ff;
}

.action-icon.record {
  background: #fffbe6;
}

.action-icon.profile {
  background: #f6ffed;
}

.action-icon.elderly {
  background: #fff2e8;
}

.action-text {
  font-size: 26rpx;
  color: #666;
}

.departments-section {
  margin-top: 32rpx;
}

.dept-grid {
  display: flex;
  flex-wrap: wrap;
  margin-top: 24rpx;
}

.dept-item {
  width: 33.33%;
  padding: 24rpx 16rpx;
  text-align: center;
  border: 2rpx solid #f0f0f0;
  margin: -1rpx;
  background: #fafafa;
}

.dept-item:active {
  background: #e6f7ff;
}

.tips-section {
  margin-top: 32rpx;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-top: 16rpx;
}

.tip-dot {
  color: #1890ff;
  margin-right: 8rpx;
}

.tip-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}
</style>
