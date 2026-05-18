<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <view class="user-info">
        <view class="avatar">
          <text>👤</text>
        </view>
        <view class="info">
          <text class="name">心理咨询师</text>
          <text class="role">专业心理咨询师</text>
        </view>
      </view>
    </view>

    <view class="menu-list">
      <view class="menu-item" @click="toggleElderMode">
        <view class="menu-icon">
          <text>👓</text>
        </view>
        <view class="menu-content">
          <text class="menu-title">长辈模式</text>
          <text class="menu-desc">{{ isElderMode ? '已开启' : '点击开启' }}</text>
        </view>
        <view class="switch" :class="{ on: isElderMode }">
          <view class="switch-dot"></view>
        </view>
      </view>

      <view class="menu-item" @click="goToRecordDetail">
        <view class="menu-icon blue">
          <text>📝</text>
        </view>
        <view class="menu-content">
          <text class="menu-title">咨询记录详情示例</text>
          <text class="menu-desc">查看记录详情页面</text>
        </view>
        <text class="arrow">></text>
      </view>

      <view class="menu-item" @click="goToAppointment">
        <view class="menu-icon green">
          <text>📅</text>
        </view>
        <view class="menu-content">
          <text class="menu-title">预约咨询示例</text>
          <text class="menu-desc">查看预约页面</text>
        </view>
        <text class="arrow">></text>
      </view>

      <view class="menu-item" @click="goToCounselors">
        <view class="menu-icon orange">
          <text>👥</text>
        </view>
        <view class="menu-content">
          <text class="menu-title">咨询师列表</text>
          <text class="menu-desc">查看所有咨询师</text>
        </view>
        <text class="arrow">></text>
      </view>

      <view class="menu-item" @click="goToCourses">
        <view class="menu-icon purple">
          <text>📚</text>
        </view>
        <view class="menu-content">
          <text class="menu-title">专业课程</text>
          <text class="menu-desc">学习专业知识</text>
        </view>
        <text class="arrow">></text>
      </view>
    </view>

    <view class="version">
      <text>版本 1.0.0</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      isElderMode: false
    }
  },
  onLoad() {
    this.isElderMode = uni.getStorageSync('elderMode') || false
  },
  methods: {
    toggleElderMode() {
      this.isElderMode = !this.isElderMode
      uni.setStorageSync('elderMode', this.isElderMode)
      uni.showToast({
        title: this.isElderMode ? '已开启长辈模式' : '已关闭长辈模式',
        icon: 'success'
      })
    },
    goToRecordDetail() {
      uni.navigateTo({ url: '/pages/record-detail/record-detail' })
    },
    goToAppointment() {
      uni.navigateTo({ url: '/pages/appointment/appointment' })
    },
    goToCounselors() {
      uni.navigateTo({ url: '/pages/counselors/counselors' })
    },
    goToCourses() {
      uni.navigateTo({ url: '/pages/courses/courses' })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  
  &.elder {
    font-size: 36rpx !important;
  }
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 60rpx;
  margin-right: 30rpx;
}

.info {
  flex: 1;
}

.name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: white;
  margin-bottom: 10rpx;
}

.role {
  display: block;
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.menu-list {
  background: white;
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.menu-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 24rpx;
  
  &.blue { background: #e3f2fd; }
  &.green { background: #e8f5e9; }
  &.orange { background: #fff3e0; }
  &.purple { background: #f3e5f5; }
}

.menu-content {
  flex: 1;
}

.menu-title {
  display: block;
  font-size: 30rpx;
  color: #333;
  margin-bottom: 8rpx;
}

.menu-desc {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.arrow {
  font-size: 28rpx;
  color: #ccc;
}

.switch {
  width: 100rpx;
  height: 60rpx;
  background: #e0e0e0;
  border-radius: 30rpx;
  position: relative;
  transition: all 0.3s;
  
  &.on {
    background: #4caf50;
  }
}

.switch-dot {
  width: 52rpx;
  height: 52rpx;
  background: white;
  border-radius: 50%;
  position: absolute;
  top: 4rpx;
  left: 4rpx;
  transition: all 0.3s;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
  
  .switch.on & {
    left: 44rpx;
  }
}

.version {
  text-align: center;
  padding: 40rpx;
  font-size: 24rpx;
  color: #ccc;
}
</style>
