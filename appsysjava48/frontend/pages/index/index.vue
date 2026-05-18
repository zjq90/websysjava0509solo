<template>
  <view class="container" :class="{ elder: isElderMode }">
    <view class="header">
      <text class="title">心理咨询服务</text>
      <view class="mode-toggle" @click="toggleElderMode">
        <text>{{ isElderMode ? '长辈模式' : '标准模式' }}</text>
      </view>
    </view>

    <view class="quick-actions">
      <view class="action-item" @click="goToCounselors">
        <view class="icon-wrapper blue">
          <text>👨‍⚕️</text>
        </view>
        <text class="action-text">咨询师列表</text>
      </view>
      <view class="action-item" @click="goToSchedule">
        <view class="icon-wrapper green">
          <text>📅</text>
        </view>
        <text class="action-text">日程管理</text>
      </view>
      <view class="action-item" @click="goToRecords">
        <view class="icon-wrapper orange">
          <text>📝</text>
        </view>
        <text class="action-text">咨询记录</text>
      </view>
      <view class="action-item" @click="goToCrisis">
        <view class="icon-wrapper red">
          <text>🚨</text>
        </view>
        <text class="action-text">危机预警</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">推荐咨询师</text>
        <text class="more-link" @click="goToCounselors">查看更多 ></text>
      </view>
      <view class="counselor-list">
        <view 
          v-for="counselor in counselors" 
          :key="counselor.id" 
          class="counselor-card"
          @click="goToAppointment(counselor.id)"
        >
          <view class="counselor-avatar">
            <text>{{ counselor.name.charAt(0) }}</text>
          </view>
          <view class="counselor-info">
            <text class="counselor-name">{{ counselor.name }}</text>
            <text class="counselor-title">{{ counselor.title }}</text>
            <text class="counselor-expertise">{{ counselor.expertise }}</text>
          </view>
          <view class="counselor-fee">
            <text>¥{{ counselor.consultationFee }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">专业课程</text>
        <text class="more-link" @click="goToCourses">查看更多 ></text>
      </view>
      <view class="course-list">
        <view 
          v-for="course in courses" 
          :key="course.id" 
          class="course-card"
        >
          <view class="course-cover">
            <text>📚</text>
          </view>
          <view class="course-info">
            <text class="course-title">{{ course.title }}</text>
            <text class="course-meta">{{ course.instructor }} · {{ course.lessonCount }}课时</text>
          </view>
        </view>
      </view>
    </view>

    <view class="section" v-if="pendingAlerts.length > 0">
      <view class="alert-banner" @click="goToCrisis">
        <text class="alert-icon">⚠️</text>
        <text class="alert-text">有 {{ pendingAlerts.length }} 条危机预警待处理</text>
      </view>
    </view>
  </view>
</template>

<script>
import { api } from '@/utils/request.js'

export default {
  data() {
    return {
      isElderMode: false,
      counselors: [],
      courses: [],
      pendingAlerts: []
    }
  },
  onLoad() {
    this.loadData()
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
    async loadData() {
      try {
        const [counselors, alerts] = await Promise.all([
          api.getCounselors(),
          api.getAlertsByStatus('pending')
        ])
        this.counselors = counselors.slice(0, 3)
        this.pendingAlerts = alerts
        this.courses = [
          { id: 1, title: '认知行为疗法(CBT)入门与实践', instructor: '王医生', lessonCount: 12 },
          { id: 2, title: '抑郁症的识别与干预', instructor: '李医生', lessonCount: 8 },
          { id: 3, title: '焦虑障碍的心理咨询实务', instructor: '王医生', lessonCount: 10 }
        ]
      } catch (e) {
        console.error(e)
        this.loadMockData()
      }
    },
    loadMockData() {
      this.counselors = [
        { id: 1, name: '王医生', title: '主任医师', expertise: '抑郁症,焦虑症,强迫症', consultationFee: '500.00' },
        { id: 2, name: '李医生', title: '副主任医师', expertise: '青少年心理,家庭关系', consultationFee: '400.00' }
      ]
      this.courses = [
        { id: 1, title: '认知行为疗法(CBT)入门与实践', instructor: '王医生', lessonCount: 12 },
        { id: 2, title: '抑郁症的识别与干预', instructor: '李医生', lessonCount: 8 },
        { id: 3, title: '焦虑障碍的心理咨询实务', instructor: '王医生', lessonCount: 10 }
      ]
    },
    goToCounselors() {
      uni.navigateTo({ url: '/pages/counselors/counselors' })
    },
    goToSchedule() {
      uni.switchTab({ url: '/pages/schedule/schedule' })
    },
    goToRecords() {
      uni.switchTab({ url: '/pages/records/records' })
    },
    goToCrisis() {
      uni.navigateTo({ url: '/pages/crisis/crisis' })
    },
    goToCourses() {
      uni.navigateTo({ url: '/pages/courses/courses' })
    },
    goToAppointment(counselorId) {
      uni.navigateTo({ url: `/pages/appointment/appointment?counselorId=${counselorId}` })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
  
  &.elder {
    font-size: 36rpx !important;
    
    .title {
      font-size: 44rpx !important;
    }
    
    .action-text {
      font-size: 30rpx !important;
    }
    
    .section-title {
      font-size: 36rpx !important;
    }
    
    .counselor-name {
      font-size: 34rpx !important;
    }
    
    .course-title {
      font-size: 32rpx !important;
    }
  }
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  color: white;
  font-size: 36rpx;
  font-weight: bold;
}

.mode-toggle {
  background: rgba(255, 255, 255, 0.2);
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
  color: white;
  font-size: 24rpx;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  padding: 30rpx;
  background: white;
  margin-bottom: 20rpx;
}

.action-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.icon-wrapper {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  margin-bottom: 16rpx;
  
  &.blue { background: #e3f2fd; }
  &.green { background: #e8f5e9; }
  &.orange { background: #fff3e0; }
  &.red { background: #ffebee; }
}

.action-text {
  font-size: 24rpx;
  color: #333;
}

.section {
  background: white;
  margin-bottom: 20rpx;
  padding: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.more-link {
  font-size: 26rpx;
  color: #999;
}

.counselor-card {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.counselor-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 36rpx;
  font-weight: bold;
  margin-right: 24rpx;
}

.counselor-info {
  flex: 1;
}

.counselor-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.counselor-title {
  font-size: 24rpx;
  color: #666;
  display: block;
  margin-bottom: 8rpx;
}

.counselor-expertise {
  font-size: 22rpx;
  color: #999;
  display: block;
}

.counselor-fee {
  color: #ff6b6b;
  font-weight: bold;
  font-size: 28rpx;
}

.course-card {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  
  &:last-child {
    border-bottom: none;
  }
}

.course-cover {
  width: 120rpx;
  height: 120rpx;
  border-radius: 12rpx;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  margin-right: 24rpx;
}

.course-info {
  flex: 1;
}

.course-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 12rpx;
}

.course-meta {
  font-size: 24rpx;
  color: #999;
}

.alert-banner {
  display: flex;
  align-items: center;
  background: #fff3cd;
  padding: 24rpx;
  border-radius: 12rpx;
}

.alert-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}

.alert-text {
  font-size: 28rpx;
  color: #856404;
}
</style>
