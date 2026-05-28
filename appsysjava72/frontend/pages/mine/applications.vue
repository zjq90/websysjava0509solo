<template>
  <view class="applications-container">
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
        :class="{ active: activeStatus === 'pending' }"
        @click="activeStatus = 'pending'"
      >
        待审核
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'approved' }"
        @click="activeStatus = 'approved'"
      >
        已通过
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'rejected' }"
        @click="activeStatus = 'rejected'"
      >
        已拒绝
      </view>
    </view>
    
    <view class="application-list" v-if="filteredApplications.length > 0">
      <view 
        class="application-card" 
        v-for="app in filteredApplications" 
        :key="app.id"
        @click="viewDetail(app)"
      >
        <view class="application-header">
          <image 
            v-if="app.clubLogo" 
            :src="app.clubLogo" 
            class="club-logo" 
            mode="aspectFill"
          />
          <view v-else class="club-logo-placeholder">
            <text>{{ app.clubName ? app.clubName.substring(0, 1) : '社' }}</text>
          </view>
          <view class="club-info">
            <text class="club-name">{{ app.clubName }}</text>
            <text class="apply-time">{{ util.formatDateTime(app.createTime) }}</text>
          </view>
          <view class="status-badge" :class="getStatusClass(app.status)">
            {{ util.getStatusText(app.status) }}
          </view>
        </view>
        
        <view class="application-content">
          <view class="content-item" v-if="app.reason">
            <text class="item-label">申请理由</text>
            <text class="item-value">{{ app.reason }}</text>
          </view>
          <view class="content-item" v-if="app.personalProfile">
            <text class="item-label">个人简介</text>
            <text class="item-value">{{ app.personalProfile }}</text>
          </view>
        </view>
        
        <view class="application-footer">
          <view class="reject-reason" v-if="app.status === 2 && app.rejectReason">
            <text class="reason-label">拒绝原因：</text>
            <text class="reason-text">{{ app.rejectReason }}</text>
          </view>
          <view class="footer-actions">
            <button 
              v-if="app.status === 0" 
              class="action-btn danger"
              @click.stop="cancelApplication(app)"
            >
              取消申请
            </button>
            <button 
              v-if="app.status === 1" 
              class="action-btn primary"
              @click.stop="goClubDetail(app)"
            >
              进入社团
            </button>
            <button 
              v-if="app.status === 2" 
              class="action-btn"
              @click.stop="reapply(app)"
            >
              重新申请
            </button>
          </view>
        </view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">📝</text>
      <text class="empty-text">{{ getEmptyText() }}</text>
      <button class="explore-btn" @click="goExplore">去发现社团</button>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
    
    <view class="detail-modal" v-if="showDetail" @click="showDetail = false">
      <view class="detail-content" @click.stop>
        <view class="detail-header">
          <text class="detail-title">申请详情</text>
          <text class="detail-close" @click="showDetail = false">×</text>
        </view>
        <scroll-view class="detail-body" scroll-y>
          <view class="detail-item" v-if="currentApp">
            <view class="detail-section">
              <text class="section-title">申请社团</text>
              <view class="club-info-row">
                <image 
                  v-if="currentApp.clubLogo" 
                  :src="currentApp.clubLogo" 
                  class="detail-logo" 
                  mode="aspectFill"
                />
                <view v-else class="detail-logo-placeholder">
                  <text>{{ currentApp.clubName ? currentApp.clubName.substring(0, 1) : '社' }}</text>
                </view>
                <text class="detail-club-name">{{ currentApp.clubName }}</text>
              </view>
            </view>
            
            <view class="detail-section">
              <text class="section-title">申请状态</text>
              <text class="section-value" :class="getStatusClass(currentApp.status)">
                {{ util.getStatusText(currentApp.status) }}
              </text>
            </view>
            
            <view class="detail-section" v-if="currentApp.reason">
              <text class="section-title">申请理由</text>
              <text class="section-value">{{ currentApp.reason }}</text>
            </view>
            
            <view class="detail-section" v-if="currentApp.personalProfile">
              <text class="section-title">个人简介</text>
              <text class="section-value">{{ currentApp.personalProfile }}</text>
            </view>
            
            <view class="detail-section" v-if="currentApp.works">
              <text class="section-title">个人作品</text>
              <view class="works-list">
                <image 
                  v-for="(img, idx) in currentApp.works.split(',')" 
                  :key="idx" 
                  :src="img" 
                  class="work-img"
                  mode="aspectFill"
                  @click="previewImage(idx, currentApp.works)"
                />
              </view>
            </view>
            
            <view class="detail-section" v-if="currentApp.status === 2 && currentApp.rejectReason">
              <text class="section-title">拒绝原因</text>
              <text class="section-value reject">{{ currentApp.rejectReason }}</text>
            </view>
            
            <view class="detail-section">
              <text class="section-title">申请时间</text>
              <text class="section-value">{{ util.formatDateTime(currentApp.createTime) }}</text>
            </view>
            
            <view class="detail-section" v-if="currentApp.updateTime">
              <text class="section-title">更新时间</text>
              <text class="section-value">{{ util.formatDateTime(currentApp.updateTime) }}</text>
            </view>
          </view>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      activeStatus: 'all',
      applicationList: [],
      loading: false,
      showDetail: false,
      currentApp: null
    }
  },
  computed: {
    filteredApplications() {
      if (this.activeStatus === 'all') {
        return this.applicationList
      }
      
      const statusMap = {
        pending: 0,
        approved: 1,
        rejected: 2
      }
      
      return this.applicationList.filter(app => app.status === statusMap[this.activeStatus])
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
        const res = await api.getMyApplications()
        this.applicationList = res.data || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    getStatusClass(status) {
      const classes = { 0: 'pending', 1: 'approved', 2: 'rejected', 3: 'cancelled' }
      return classes[status] || 'pending'
    },
    
    viewDetail(app) {
      this.currentApp = app
      this.showDetail = true
    },
    
    async cancelApplication(app) {
      const confirm = await util.showModal(
        '取消申请',
        `确定要取消加入「${app.clubName}」的申请吗？`
      )
      
      if (!confirm) return
      
      try {
        util.toast('取消功能开发中')
      } catch (e) {
        util.toast('操作失败，请重试')
        console.error(e)
      }
    },
    
    goClubDetail(app) {
      uni.navigateTo({ url: '/pages/club/detail?id=' + app.clubId })
    },
    
    reapply(app) {
      uni.navigateTo({ url: '/pages/club/apply?clubId=' + app.clubId })
    },
    
    getEmptyText() {
      switch (this.activeStatus) {
        case 'pending':
          return '暂无待审核的申请'
        case 'approved':
          return '暂无已通过的申请'
        case 'rejected':
          return '暂无已拒绝的申请'
        default:
          return '还没有提交任何申请'
      }
    },
    
    goExplore() {
      uni.switchTab({ url: '/pages/index/index' })
    },
    
    previewImage(index, works) {
      const images = works.split(',')
      uni.previewImage({
        current: index,
        urls: images
      })
    }
  }
}
</script>

<style scoped>
.applications-container {
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

.application-list {
  padding: 20rpx;
}

.application-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.application-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.club-logo,
.club-logo-placeholder {
  width: 90rpx;
  height: 90rpx;
  border-radius: 18rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.club-logo-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
}

.club-info {
  flex: 1;
}

.club-name {
  display: block;
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 6rpx;
}

.apply-time {
  font-size: 24rpx;
  color: #999;
}

.status-badge {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 600;
  flex-shrink: 0;
}

.status-badge.pending {
  background: #fff3e0;
  color: #ff9800;
}

.status-badge.approved {
  background: #e8f5e9;
  color: #4caf50;
}

.status-badge.rejected {
  background: #ffebee;
  color: #dd524d;
}

.status-badge.cancelled {
  background: #f5f5f5;
  color: #999;
}

.application-content {
  padding: 16rpx 0;
  border-top: 1rpx solid #f8f8f8;
  border-bottom: 1rpx solid #f8f8f8;
  margin-bottom: 16rpx;
}

.content-item {
  margin-bottom: 12rpx;
}

.content-item:last-child {
  margin-bottom: 0;
}

.item-label {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
  display: block;
}

.item-value {
  font-size: 26rpx;
  color: #333;
  line-height: 1.5;
}

.application-footer {
  display: flex;
  flex-direction: column;
}

.reject-reason {
  margin-bottom: 16rpx;
  padding: 12rpx 16rpx;
  background: #ffebee;
  border-radius: 8rpx;
}

.reason-label {
  font-size: 24rpx;
  color: #dd524d;
  font-weight: 600;
}

.reason-text {
  font-size: 24rpx;
  color: #dd524d;
}

.footer-actions {
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  padding: 12rpx 28rpx;
  background: #f5f6f8;
  color: #666;
  border-radius: 28rpx;
  font-size: 24rpx;
  border: none;
  margin-left: 16rpx;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.action-btn.danger {
  background: #ffebee;
  color: #dd524d;
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

.detail-modal {
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

.detail-content {
  width: 640rpx;
  max-height: 80vh;
  background: #fff;
  border-radius: 20rpx;
  display: flex;
  flex-direction: column;
}

.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.detail-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.detail-close {
  font-size: 48rpx;
  color: #999;
  line-height: 1;
  padding: 0 10rpx;
}

.detail-body {
  flex: 1;
  overflow-y: auto;
  padding: 30rpx;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.detail-section {
  display: flex;
  flex-direction: column;
}

.section-title {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 10rpx;
}

.section-value {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.section-value.pending {
  color: #ff9800;
}

.section-value.approved {
  color: #4caf50;
}

.section-value.rejected,
.section-value.reject {
  color: #dd524d;
}

.club-info-row {
  display: flex;
  align-items: center;
}

.detail-logo,
.detail-logo-placeholder {
  width: 60rpx;
  height: 60rpx;
  border-radius: 12rpx;
  margin-right: 16rpx;
}

.detail-logo-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.detail-club-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 600;
}

.works-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.work-img {
  width: 180rpx;
  height: 180rpx;
  border-radius: 12rpx;
  background: #f0f0f0;
}
</style>
