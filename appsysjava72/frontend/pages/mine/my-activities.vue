<template>
  <view class="my-activities-container">
    <view class="filter-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'all' }"
        @click="activeStatus = 'all'"
      >
        全部
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'upcoming' }"
        @click="activeStatus = 'upcoming'"
      >
        即将开始
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'ongoing' }"
        @click="activeStatus = 'ongoing'"
      >
        进行中
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'ended' }"
        @click="activeStatus = 'ended'"
      >
        已结束
      </view>
    </view>
    
    <view class="activity-list" v-if="filteredActivities.length > 0">
      <view 
        class="activity-card" 
        v-for="activity in filteredActivities" 
        :key="activity.id"
        @click="goDetail(activity)"
      >
        <view class="activity-status" :class="getActivityStatusClass(activity)">
          {{ getActivityStatusText(activity) }}
        </view>
        
        <view class="activity-header">
          <text class="activity-title">{{ activity.title }}</text>
          <text class="activity-club">{{ activity.clubName }}</text>
        </view>
        
        <view class="activity-info">
          <view class="info-item">
            <text class="info-icon">📅</text>
            <text class="info-text">{{ util.formatDateTime(activity.startTime) }}</text>
          </view>
          <view class="info-item">
            <text class="info-icon">📍</text>
            <text class="info-text">{{ activity.location || '待定' }}</text>
          </view>
        </view>
        
        <view class="activity-actions">
          <button 
            v-if="canSignIn(activity)" 
            class="action-btn primary"
            @click.stop="goSignIn(activity)"
          >
            去签到
          </button>
          <button 
            v-else-if="canSignUp(activity)" 
            class="action-btn primary"
            @click.stop="signUp(activity)"
          >
            报名活动
          </button>
          <button 
            v-else-if="isSignedUp(activity) && !isStarted(activity)"
            class="action-btn"
            @click.stop="cancelSignUp(activity)"
          >
            取消报名
          </button>
          <view v-else class="sign-status">
            <text v-if="activity.isSignedIn" class="signed">✅ 已签到</text>
            <text v-else-if="activity.isSignedUp" class="registered">✅ 已报名</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">📅</text>
      <text class="empty-text">{{ getEmptyText() }}</text>
      <button class="explore-btn" @click="goExplore">去发现活动</button>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activeStatus: 'all',
      activityList: [],
      loading: false
    }
  },
  computed: {
    filteredActivities() {
      if (this.activeStatus === 'all') {
        return this.activityList
      }
      
      const now = Date.now()
      return this.activityList.filter(activity => {
        const startTime = new Date(activity.startTime).getTime()
        const endTime = new Date(activity.endTime).getTime()
        
        switch (this.activeStatus) {
          case 'upcoming':
            return now < startTime
          case 'ongoing':
            return now >= startTime && now <= endTime
          case 'ended':
            return now > endTime
          default:
            return true
        }
      })
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const res = await api.getMyActivities()
        this.activityList = res.data || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    getActivityStatusClass(activity) {
      const now = Date.now()
      const startTime = new Date(activity.startTime).getTime()
      const endTime = new Date(activity.endTime).getTime()
      
      if (now > endTime) return 'ended'
      if (now >= startTime && now <= endTime) return 'ongoing'
      return 'upcoming'
    },
    
    getActivityStatusText(activity) {
      const now = Date.now()
      const startTime = new Date(activity.startTime).getTime()
      const endTime = new Date(activity.endTime).getTime()
      
      if (now > endTime) return '已结束'
      if (now >= startTime && now <= endTime) return '进行中'
      return '即将开始'
    },
    
    canSignUp(activity) {
      const now = Date.now()
      const startTime = new Date(activity.startTime).getTime()
      return now < startTime && !activity.isSignedUp
    },
    
    canSignIn(activity) {
      const now = Date.now()
      const startTime = new Date(activity.startTime).getTime()
      const endTime = new Date(activity.endTime).getTime()
      return now >= startTime && now <= endTime && activity.isSignedUp && !activity.isSignedIn
    },
    
    isSignedUp(activity) {
      return activity.isSignedUp
    },
    
    isStarted(activity) {
      const now = Date.now()
      const startTime = new Date(activity.startTime).getTime()
      return now >= startTime
    },
    
    goDetail(activity) {
      uni.navigateTo({ url: '/pages/activity/detail?id=' + activity.id })
    },
    
    goSignIn(activity) {
      uni.navigateTo({ url: '/pages/activity/signin?id=' + activity.id })
    },
    
    async signUp(activity) {
      try {
        const res = await api.signUpActivity(activity.id, '')
        if (res.code === 200) {
          util.toast('报名成功', 'success')
          this.loadData()
        } else {
          util.toast(res.message || '报名失败')
        }
      } catch (e) {
        util.toast('报名失败，请重试')
        console.error(e)
      }
    },
    
    async cancelSignUp(activity) {
      const confirm = await util.showModal(
        '取消报名',
        `确定要取消报名「${activity.title}」吗？`
      )
      
      if (!confirm) return
      
      try {
        const res = await api.cancelSignUp(activity.id)
        if (res.code === 200) {
          util.toast('已取消报名', 'success')
          this.loadData()
        } else {
          util.toast(res.message || '取消失败')
        }
      } catch (e) {
        util.toast('取消失败，请重试')
        console.error(e)
      }
    },
    
    getEmptyText() {
      switch (this.activeStatus) {
        case 'upcoming':
          return '暂无即将开始的活动'
        case 'ongoing':
          return '暂无进行中的活动'
        case 'ended':
          return '暂无已结束的活动'
        default:
          return '还没有参与任何活动'
      }
    },
    
    goExplore() {
      uni.switchTab({ url: '/pages/activity/index' })
    }
  }
}
</script>

<style scoped>
.my-activities-container {
  min-height: 100vh;
  background: #f5f6f8;
}

.filter-tabs {
  display: flex;
  background: #fff;
  padding: 0 10rpx;
  border-bottom: 1rpx solid #f0f0f0;
  overflow-x: auto;
}

.tab-item {
  padding: 24rpx 30rpx;
  font-size: 28rpx;
  color: #666;
  white-space: nowrap;
  position: relative;
}

.tab-item.active {
  color: #5677fc;
  font-weight: 600;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 40rpx;
  height: 6rpx;
  background: #5677fc;
  border-radius: 3rpx;
}

.activity-list {
  padding: 20rpx;
}

.activity-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  position: relative;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.activity-status {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  font-weight: 600;
}

.activity-status.upcoming {
  background: #e3f2fd;
  color: #1976d2;
}

.activity-status.ongoing {
  background: #e8f5e9;
  color: #388e3c;
}

.activity-status.ended {
  background: #f5f5f5;
  color: #999;
}

.activity-header {
  padding-right: 100rpx;
  margin-bottom: 16rpx;
}

.activity-title {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.activity-club {
  font-size: 24rpx;
  color: #5677fc;
  background: #e8ecff;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
}

.activity-info {
  margin-bottom: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.info-icon {
  font-size: 24rpx;
  margin-right: 10rpx;
}

.info-text {
  font-size: 26rpx;
  color: #666;
}

.activity-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-top: 16rpx;
  border-top: 1rpx solid #f8f8f8;
}

.action-btn {
  padding: 14rpx 32rpx;
  background: #f5f6f8;
  color: #666;
  border-radius: 32rpx;
  font-size: 26rpx;
  border: none;
  margin-left: 16rpx;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.sign-status {
  display: flex;
  align-items: center;
}

.signed {
  font-size: 26rpx;
  color: #4caf50;
}

.registered {
  font-size: 26rpx;
  color: #5677fc;
}

.empty-state {
  text-align: center;
  padding: 120rpx 0;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 28rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.explore-btn {
  padding: 20rpx 60rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
