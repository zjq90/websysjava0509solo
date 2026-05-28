<template>
  <view class="detail-container" v-if="activity">
    <view class="activity-banner">
      <view class="banner-content">
        <text class="activity-name">{{ activity.name }}</text>
        <view class="activity-tags">
          <text class="type-tag">{{ activity.activityType || '其他' }}</text>
          <text 
            class="status-tag" 
            :style="{ background: util.getActivityStatusColor(activity.status) + '20', color: util.getActivityStatusColor(activity.status) }"
          >
            {{ util.getActivityStatusText(activity.status) }}
          </text>
        </view>
      </view>
    </view>
    
    <view class="info-section">
      <view class="info-item">
        <text class="info-icon">⏰</text>
        <view class="info-content">
          <text class="info-label">活动时间</text>
          <text class="info-value">{{ util.formatTime(activity.startTime) }} - {{ util.formatTime(activity.endTime) }}</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-icon">📍</text>
        <view class="info-content">
          <text class="info-label">活动地点</text>
          <text class="info-value">{{ activity.location || '待定' }}</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-icon">👥</text>
        <view class="info-content">
          <text class="info-label">报名人数</text>
          <text class="info-value">{{ activity.signUpCount || 0 }} / {{ activity.maxParticipants || '不限' }} 人</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-icon">🏫</text>
        <view class="info-content">
          <text class="info-label">主办社团</text>
          <text class="info-value" @click="goClubDetail">{{ clubName }}</text>
        </view>
      </view>
    </view>
    
    <view class="section">
      <view class="section-title">活动介绍</view>
      <view class="section-content">
        <text class="desc-text">{{ activity.description || '暂无活动介绍' }}</text>
      </view>
    </view>
    
    <view class="section" v-if="activity.notes">
      <view class="section-title">注意事项</view>
      <view class="section-content">
        <text class="desc-text">{{ activity.notes }}</text>
      </view>
    </view>
    
    <view class="bottom-bar">
      <view class="sign-info" v-if="isSignedUp">
        <text class="sign-status" :class="{ success: isSignedIn }">
          {{ isSignedIn ? '✓ 已签到' : '报名成功' }}
        </text>
      </view>
      <view class="sign-info" v-else-if="activity.status === 2">
        <text class="sign-status ended">活动已结束</text>
      </view>
      <view class="sign-info" v-else-if="activity.maxParticipants && activity.signUpCount >= activity.maxParticipants">
        <text class="sign-status full">报名已满</text>
      </view>
      
      <view class="action-buttons">
        <button 
          v-if="activity.status === 1 && isSignedUp && !isSignedIn" 
          class="action-btn signin" 
          @click="scanSignIn"
        >
          📷 扫码签到
        </button>
        <button 
          v-if="isSignedUp && activity.status !== 2" 
          class="action-btn cancel" 
          @click="handleCancel"
        >
          取消报名
        </button>
        <button 
          v-if="!isSignedUp && activity.status === 0 && !(activity.maxParticipants && activity.signUpCount >= activity.maxParticipants)" 
          class="action-btn signup" 
          @click="handleSignUp"
        >
          立即报名
        </button>
      </view>
    </view>
    
    <view class="loading" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activityId: null,
      activity: null,
      clubName: '',
      isSignedUp: false,
      isSignedIn: false,
      loading: true
    }
  },
  onLoad(options) {
    this.activityId = options.id
    this.loadData()
  },
  onShow() {
    if (this.activityId) {
      this.checkStatus()
    }
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const res = await api.getActivityDetail(this.activityId)
        this.activity = res.data
        if (this.activity.clubId) {
          const clubRes = await api.getClubDetail(this.activity.clubId)
          this.clubName = clubRes.data.name
        }
        this.checkStatus()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async checkStatus() {
      try {
        const [signupRes, signinRes] = await Promise.all([
          api.isSignedUp(this.activityId),
          api.isSignedIn(this.activityId)
        ])
        this.isSignedUp = signupRes.data
        this.isSignedIn = signinRes.data
      } catch (e) {
        console.error(e)
      }
    },
    
    async handleSignUp() {
      const confirm = await util.showModal('确认报名', '确定要报名参加此活动吗？')
      if (confirm) {
        try {
          await api.signUpActivity(this.activityId)
          util.showToast('报名成功', 'success')
          this.isSignedUp = true
          this.activity.signUpCount++
        } catch (e) {
          console.error(e)
        }
      }
    },
    
    async handleCancel() {
      const confirm = await util.showModal('提示', '确定要取消报名吗？')
      if (confirm) {
        try {
          await api.cancelSignUp(this.activityId)
          util.showToast('已取消报名', 'success')
          this.isSignedUp = false
          this.activity.signUpCount--
        } catch (e) {
          console.error(e)
        }
      }
    },
    
    async scanSignIn() {
      const hasNetwork = await util.checkNetwork()
      
      if (!hasNetwork) {
        const confirm = await util.showModal('离线签到', '当前无网络连接，是否使用离线签到？网络恢复后将自动同步。')
        if (confirm) {
          const signInData = {
            activityId: this.activityId,
            signInType: 1,
            location: '离线签到',
            signInTime: Date.now()
          }
          util.saveOfflineSignIn(signInData)
          util.showToast('离线签到成功，网络恢复后将自动同步', 'success')
          this.isSignedIn = true
          
          uni.onNetworkStatusChange(async (res) => {
            if (res.isConnected) {
              this.syncOfflineData()
            }
          })
        }
        return
      }
      
      uni.scanCode({
        onlyFromCamera: false,
        scanType: ['qrCode'],
        success: async (res) => {
          try {
            await api.signInActivity(this.activityId, 1, '扫码签到', false)
            util.showToast('签到成功', 'success')
            this.isSignedIn = true
          } catch (e) {
            console.error(e)
          }
        },
        fail: () => {
          util.showToast('扫码取消')
        }
      })
    },
    
    async syncOfflineData() {
      const offlineData = util.getOfflineSignIn()
      if (offlineData.length > 0) {
        try {
          await api.syncOfflineSignIn(offlineData)
          util.clearOfflineSignIn()
          util.showToast('离线数据同步成功', 'success')
        } catch (e) {
          console.error('同步失败', e)
        }
      }
    },
    
    goClubDetail() {
      if (this.activity.clubId) {
        uni.navigateTo({ url: '/pages/club/detail?id=' + this.activity.clubId })
      }
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 140rpx;
}

.activity-banner {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 60rpx 30rpx;
}

.banner-content {
  color: #fff;
}

.activity-name {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.activity-tags {
  display: flex;
  gap: 15rpx;
}

.type-tag {
  padding: 6rpx 20rpx;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.status-tag {
  padding: 6rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 500;
}

.info-section {
  background: #fff;
  margin: -30rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 10rpx 0;
  position: relative;
  z-index: 10;
}

.info-item {
  display: flex;
  align-items: flex-start;
  padding: 25rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
  margin-top: 2rpx;
  flex-shrink: 0;
}

.info-content {
  flex: 1;
}

.info-label {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 6rpx;
}

.info-value {
  font-size: 28rpx;
  color: #333;
}

.section {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
}

.desc-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.sign-info {
  flex: 1;
}

.sign-status {
  font-size: 28rpx;
  color: #5677fc;
  font-weight: 500;
}

.sign-status.success {
  color: #4cd964;
}

.sign-status.ended,
.sign-status.full {
  color: #999;
}

.action-buttons {
  display: flex;
  gap: 15rpx;
}

.action-btn {
  padding: 15rpx 35rpx;
  border-radius: 40rpx;
  font-size: 26rpx;
  border: none;
}

.action-btn.signup {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.action-btn.signin {
  background: linear-gradient(135deg, #4cd964 0%, #2ecc71 100%);
  color: #fff;
}

.action-btn.cancel {
  background: #fff;
  color: #dd524d;
  border: 2rpx solid #dd524d;
}

.loading {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
}
</style>
