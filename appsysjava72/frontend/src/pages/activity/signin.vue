<template>
  <view class="signin-container">
    <view class="activity-header" v-if="activity">
      <view class="activity-info">
        <text class="activity-title">{{ activity.title }}</text>
        <view class="activity-meta">
          <text class="meta-item">📅 {{ util.formatDate(activity.startTime) }}</text>
          <text class="meta-item">📍 {{ activity.location || '暂无地点' }}</text>
        </view>
      </view>
      <view class="signin-status" :class="statusClass">
        <text>{{ statusText }}</text>
      </view>
    </view>

    <view class="signin-main">
      <view class="qr-section">
        <view class="qr-placeholder" @click="scanCode">
          <text class="qr-icon">📷</text>
          <text class="qr-text">点击扫码签到</text>
          <text class="qr-hint">扫描活动现场二维码完成签到</text>
        </view>
        <button class="scan-btn" @click="scanCode">
          <text class="btn-icon">🔍</text>
          扫一扫
        </button>
      </view>

      <view class="offline-section">
        <view class="section-title">
          <text class="title-icon">📡</text>
          离线签到
        </view>
        <view class="offline-desc">
          <text>当前网络状态：{{ isOnline ? '在线' : '离线' }}</text>
        </view>
        <button 
          class="offline-btn" 
          :class="{ disabled: isOnline || hasSignedIn }"
          :disabled="isOnline || hasSignedIn"
          @click="offlineSignIn"
        >
          <text class="btn-icon">💾</text>
          离线签到
        </button>
        <text class="offline-hint">离线签到数据将在网络恢复后自动同步</text>
      </view>

      <view class="sync-section" v-if="offlineRecords.length > 0">
        <view class="section-title">
          <text class="title-icon">🔄</text>
          待同步签到 ({{ offlineRecords.length }})
        </view>
        <view class="sync-list">
          <view 
            class="sync-item" 
            v-for="(record, index) in offlineRecords" 
            :key="index"
          >
            <view class="sync-info">
              <text class="sync-activity">{{ record.activityTitle }}</text>
              <text class="sync-time">{{ util.formatDateTime(record.signInTime) }}</text>
            </view>
            <text class="sync-status" :class="record.status">
              {{ record.status === 'pending' ? '待同步' : '同步中...' }}
            </text>
          </view>
        </view>
        <button 
          class="sync-btn" 
          :disabled="!isOnline || syncing"
          @click="syncOfflineRecords"
        >
          {{ syncing ? '同步中...' : '立即同步' }}
        </button>
      </view>

      <view class="signin-history">
        <view class="section-title">
          <text class="title-icon">📋</text>
          签到记录
        </view>
        <view class="history-list" v-if="signInRecords.length > 0">
          <view 
            class="history-item" 
            v-for="(record, index) in signInRecords" 
            :key="index"
          >
            <view class="history-time">
              <text class="date">{{ util.formatDate(record.signInTime) }}</text>
              <text class="time">{{ util.formatTime(record.signInTime) }}</text>
            </view>
            <view class="history-info">
              <text class="type">{{ getSignInTypeText(record.signInType) }}</text>
              <text class="location" v-if="record.location">{{ record.location }}</text>
            </view>
            <view class="history-status" :class="record.signInType">
              {{ record.isOffline ? '离线' : '在线' }}
            </view>
          </view>
        </view>
        <view class="empty-history" v-else>
          <text class="empty-icon">📭</text>
          <text class="empty-text">暂无签到记录</text>
        </view>
      </view>
    </view>

    <view class="signin-modal" v-if="showSuccess">
      <view class="modal-content success">
        <text class="modal-icon">✅</text>
        <text class="modal-title">签到成功</text>
        <text class="modal-desc">{{ successMessage }}</text>
        <button class="modal-btn" @click="showSuccess = false">确定</button>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'
import config from '../../common/config'

export default {
  data() {
    return {
      activityId: null,
      activity: null,
      hasSignedIn: false,
      isOnline: true,
      offlineRecords: [],
      signInRecords: [],
      syncing: false,
      showSuccess: false,
      successMessage: '',
      loading: false
    }
  },
  computed: {
    statusClass() {
      if (this.hasSignedIn) return 'signed'
      if (!this.activity) return 'pending'
      const now = Date.now()
      const startTime = new Date(this.activity.startTime).getTime()
      const endTime = new Date(this.activity.endTime).getTime()
      if (now < startTime) return 'not-started'
      if (now > endTime) return 'ended'
      return 'ongoing'
    },
    statusText() {
      if (this.hasSignedIn) return '已签到'
      if (!this.activity) return '加载中'
      const now = Date.now()
      const startTime = new Date(this.activity.startTime).getTime()
      const endTime = new Date(this.activity.endTime).getTime()
      if (now < startTime) return '未开始'
      if (now > endTime) return '已结束'
      return '进行中'
    }
  },
  onLoad(options) {
    this.activityId = options.id
    this.checkNetwork()
    this.loadOfflineRecords()
    this.loadData()
    uni.onNetworkStatusChange((res) => {
      this.isOnline = res.isConnected
      if (this.isOnline && this.offlineRecords.length > 0) {
        this.syncOfflineRecords()
      }
    })
  },
  onShow() {
    this.checkNetwork()
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const [activityRes, signInRes, recordsRes] = await Promise.all([
          api.getActivityDetail(this.activityId),
          api.isSignedIn(this.activityId),
          api.getMySignInRecords(this.activityId)
        ])
        this.activity = activityRes.data
        this.hasSignedIn = signInRes.data
        this.signInRecords = recordsRes.data || []
      } catch (e) {
        util.toast('加载失败')
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    checkNetwork() {
      uni.getNetworkType({
        success: (res) => {
          this.isOnline = res.networkType !== 'none'
        }
      })
    },
    
    loadOfflineRecords() {
      const records = uni.getStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN) || []
      this.offlineRecords = records.filter(r => r.status === 'pending' || r.status === 'syncing')
    },
    
    saveOfflineRecord(record) {
      const records = uni.getStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN) || []
      records.push(record)
      uni.setStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN, records)
      this.loadOfflineRecords()
    },
    
    updateOfflineRecord(index, status) {
      const records = uni.getStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN) || []
      if (records[index]) {
        records[index].status = status
        uni.setStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN, records)
      }
      this.loadOfflineRecords()
    },
    
    scanCode() {
      if (this.hasSignedIn) {
        util.toast('您已完成签到')
        return
      }
      uni.scanCode({
        onlyFromCamera: false,
        scanType: ['qrCode', 'barCode'],
        success: (res) => {
          const scannedActivityId = this.parseQRCode(res.result)
          if (scannedActivityId && String(scannedActivityId) === String(this.activityId)) {
            this.doSignIn('qr', res.result)
          } else {
            util.toast('二维码无效，请扫描正确的活动二维码')
          }
        },
        fail: () => {
          util.toast('扫码取消')
        }
      })
    },
    
    parseQRCode(content) {
      try {
        if (content.startsWith('club-activity:')) {
          return content.split(':')[1]
        }
        const params = new URLSearchParams(content.split('?')[1])
        return params.get('activityId') || content
      } catch (e) {
        return content
      }
    },
    
    offlineSignIn() {
      if (this.hasSignedIn) {
        util.toast('您已完成签到')
        return
      }
      const record = {
        activityId: this.activityId,
        activityTitle: this.activity ? this.activity.title : '活动',
        signInType: 'qr',
        signInTime: new Date().toISOString(),
        location: '',
        isOffline: true,
        status: 'pending'
      }
      this.saveOfflineRecord(record)
      this.hasSignedIn = true
      this.showSuccess = true
      this.successMessage = '离线签到成功！数据将在网络恢复后自动同步。'
      util.setStorage('offline_signed_' + this.activityId, true)
    },
    
    async doSignIn(signInType, location) {
      if (this.hasSignedIn) {
        util.toast('您已完成签到')
        return
      }
      try {
        const res = await api.signInActivity(this.activityId, signInType, location, false)
        if (res.code === 200) {
          this.hasSignedIn = true
          this.showSuccess = true
          this.successMessage = '签到成功！'
          this.loadData()
        } else {
          util.toast(res.message || '签到失败')
        }
      } catch (e) {
        if (!this.isOnline) {
          this.offlineSignIn()
        } else {
          util.toast('签到失败，请重试')
        }
        console.error(e)
      }
    },
    
    async syncOfflineRecords() {
      if (!this.isOnline || this.syncing || this.offlineRecords.length === 0) return
      
      this.syncing = true
      const records = uni.getStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN) || []
      const pendingRecords = records.filter(r => r.status === 'pending')
      
      try {
        for (let i = 0; i < pendingRecords.length; i++) {
          const record = pendingRecords[i]
          const recordIndex = records.findIndex(r => 
            r.activityId === record.activityId && 
            r.signInTime === record.signInTime
          )
          
          if (recordIndex !== -1) {
            this.updateOfflineRecord(recordIndex, 'syncing')
            
            try {
              await api.signInActivity(
                record.activityId,
                record.signInType,
                record.location,
                true
              )
              
              records[recordIndex].status = 'synced'
              records[recordIndex].syncTime = new Date().toISOString()
              uni.setStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN, records)
              
              if (String(record.activityId) === String(this.activityId)) {
                this.hasSignedIn = true
                util.removeStorage('offline_signed_' + this.activityId)
              }
            } catch (e) {
              records[recordIndex].status = 'pending'
              uni.setStorageSync(config.STORAGE_KEYS.OFFLINE_SIGNIN, records)
              console.error('Sync failed:', e)
            }
          }
        }
        
        util.toast('同步完成')
        this.loadData()
      } catch (e) {
        util.toast('同步失败，请重试')
        console.error(e)
      } finally {
        this.syncing = false
        this.loadOfflineRecords()
      }
    },
    
    getSignInTypeText(type) {
      const types = {
        qr: '扫码签到',
        manual: '手动签到',
        gps: 'GPS签到'
      }
      return types[type] || '签到'
    }
  }
}
</script>

<style scoped>
.signin-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 40rpx;
}

.activity-header {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 40rpx 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.activity-info {
  flex: 1;
}

.activity-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 16rpx;
}

.activity-meta {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.meta-item {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.9);
}

.signin-status {
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.signin-status.signed {
  background: #4caf50;
}

.signin-status.ongoing {
  background: #ff9800;
}

.signin-status.not-started {
  background: #9e9e9e;
}

.signin-status.ended {
  background: #f44336;
}

.signin-main {
  padding: 30rpx;
  margin-top: -20rpx;
}

.qr-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
}

.qr-placeholder {
  width: 400rpx;
  height: 400rpx;
  margin: 0 auto 30rpx;
  border: 4rpx dashed #ddd;
  border-radius: 20rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.qr-icon {
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.qr-text {
  font-size: 32rpx;
  color: #333;
  font-weight: 600;
  margin-bottom: 12rpx;
}

.qr-hint {
  font-size: 24rpx;
  color: #999;
}

.scan-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 44rpx;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-icon {
  margin-right: 12rpx;
}

.offline-section,
.sync-section,
.signin-history {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
  display: flex;
  align-items: center;
}

.title-icon {
  margin-right: 12rpx;
}

.offline-desc {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.offline-btn {
  width: 100%;
  height: 88rpx;
  background: #ff9800;
  color: #fff;
  border-radius: 44rpx;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.offline-btn.disabled {
  background: #ccc;
}

.offline-hint {
  display: block;
  text-align: center;
  font-size: 24rpx;
  color: #999;
  margin-top: 16rpx;
}

.sync-list {
  max-height: 300rpx;
  overflow-y: auto;
  margin-bottom: 20rpx;
}

.sync-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.sync-item:last-child {
  border-bottom: none;
}

.sync-activity {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 6rpx;
}

.sync-time {
  font-size: 24rpx;
  color: #999;
}

.sync-status {
  font-size: 24rpx;
  color: #ff9800;
}

.sync-status.syncing {
  color: #5677fc;
}

.sync-btn {
  width: 100%;
  height: 80rpx;
  background: #4caf50;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.history-list {
  max-height: 400rpx;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.history-item:last-child {
  border-bottom: none;
}

.history-time {
  width: 140rpx;
  flex-shrink: 0;
}

.history-time .date {
  display: block;
  font-size: 26rpx;
  color: #333;
  font-weight: 600;
}

.history-time .time {
  font-size: 24rpx;
  color: #999;
}

.history-info {
  flex: 1;
}

.history-info .type {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 4rpx;
}

.history-info .location {
  font-size: 24rpx;
  color: #999;
}

.history-status {
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  background: #e8f5e9;
  color: #4caf50;
  flex-shrink: 0;
}

.history-status.qr {
  background: #e3f2fd;
  color: #2196f3;
}

.history-status.manual {
  background: #fff3e0;
  color: #ff9800;
}

.empty-history {
  text-align: center;
  padding: 60rpx 0;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 26rpx;
  color: #999;
}

.signin-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  width: 560rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 50rpx 40rpx;
  text-align: center;
}

.modal-icon {
  display: block;
  font-size: 100rpx;
  margin-bottom: 24rpx;
}

.modal-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 16rpx;
}

.modal-desc {
  display: block;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 40rpx;
  line-height: 1.6;
}

.modal-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}
</style>
