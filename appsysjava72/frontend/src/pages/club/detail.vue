<template>
  <view class="detail-container">
    <view class="club-header" v-if="club">
      <view class="club-avatar">
        <image v-if="club.logo" :src="club.logo" mode="aspectFill" class="logo-img" />
        <view v-else class="avatar-placeholder">
          <text class="avatar-text">{{ club.name.substring(0, 1) }}</text>
        </view>
      </view>
      <view class="club-info">
        <text class="club-name">{{ club.name }}</text>
        <view class="club-tags">
          <text class="tag">{{ getCategoryName(club.categoryId) }}</text>
          <text class="tag">👥 {{ club.memberCount || 0 }}人</text>
        </view>
      </view>
    </view>
    
    <view class="action-bar">
      <button 
        v-if="!isJoined && !hasApplied" 
        class="action-btn primary" 
        @click="goApply"
      >
        申请加入
      </button>
      <button 
        v-else-if="hasApplied && application" 
        class="action-btn disabled" 
        :disabled="true"
      >
        {{ getStatusText(application.status) }}
      </button>
      <button 
        v-else-if="isJoined" 
        class="action-btn success" 
        :disabled="true"
      >
        ✓ 已加入
      </button>
      <button 
        v-if="isJoined" 
        class="action-btn outline" 
        @click="goChat"
      >
        进入群聊
      </button>
      <button 
        v-if="isJoined" 
        class="action-btn danger-outline" 
        @click="handleQuit"
      >
        退出社团
      </button>
    </view>
    
    <view class="section" v-if="club">
      <view class="section-title">社团简介</view>
      <view class="section-content">
        <text class="desc-text">{{ club.description || '暂无简介' }}</text>
      </view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <view class="section-title">近期活动</view>
        <text class="more-link" @click="viewAllActivities">查看全部</text>
      </view>
      <view class="activity-list" v-if="activities.length > 0">
        <view 
          class="activity-item" 
          v-for="activity in activities" 
          :key="activity.id"
          @click="goActivityDetail(activity.id)"
        >
          <view class="activity-date">
            <text class="date-month">{{ formatMonth(activity.startTime) }}</text>
            <text class="date-day">{{ formatDay(activity.startTime) }}</text>
          </view>
          <view class="activity-info">
            <text class="activity-name">{{ activity.name }}</text>
            <text class="activity-time">⏰ {{ util.formatTime(activity.startTime) }}</text>
            <text class="activity-location">📍 {{ activity.location || '待定' }}</text>
          </view>
          <view class="activity-status" :style="{ color: util.getActivityStatusColor(activity.status) }">
            {{ util.getActivityStatusText(activity.status) }}
          </view>
        </view>
      </view>
      <view class="empty-tip" v-else>暂无活动</view>
    </view>
    
    <view class="section">
      <view class="section-header">
        <view class="section-title">社团动态</view>
        <text class="more-link" @click="viewAllFeeds">查看全部</text>
      </view>
      <view class="feed-list" v-if="feeds.length > 0">
        <view 
          class="feed-item" 
          v-for="feed in feeds" 
          :key="feed.id"
          @click="goFeedDetail(feed.id)"
        >
          <view class="feed-header">
            <text class="feed-content">{{ feed.content }}</text>
          </view>
          <view class="feed-images" v-if="feed.images">
            <image 
              v-for="(img, idx) in feed.images.split(',').slice(0, 3)" 
              :key="idx" 
              :src="img" 
              mode="aspectFill"
              class="feed-img"
            />
          </view>
          <view class="feed-footer">
            <text class="feed-time">{{ util.fromNow(feed.createTime) }}</text>
            <view class="feed-stats">
              <text class="stat-item">👍 {{ feed.likeCount || 0 }}</text>
              <text class="stat-item">💬 {{ feed.commentCount || 0 }}</text>
            </view>
          </view>
        </view>
      </view>
      <view class="empty-tip" v-else>暂无动态</view>
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
      clubId: null,
      club: null,
      categories: [],
      isJoined: false,
      hasApplied: false,
      application: null,
      activities: [],
      feeds: [],
      loading: true
    }
  },
  onLoad(options) {
    this.clubId = options.id
    this.loadData()
  },
  methods: {
    util,
    
    getCategoryName(categoryId) {
      const cat = this.categories.find(c => c.id === categoryId)
      return cat ? cat.name : '其他'
    },
    
    getStatusText(status) {
      return util.getStatusText(status)
    },
    
    formatMonth(time) {
      return util.formatDate(time, 'MM月')
    },
    
    formatDay(time) {
      return util.formatDate(time, 'DD')
    },
    
    async loadData() {
      this.loading = true
      try {
        const [clubRes, catRes, joinRes, applyRes, actRes, feedRes] = await Promise.all([
          api.getClubDetail(this.clubId),
          api.getCategories(),
          api.isJoinedClub(this.clubId),
          api.getMyApplications(),
          api.getActivityPage({ pageNum: 1, pageSize: 5, clubId: this.clubId }),
          api.getFeedPage({ pageNum: 1, pageSize: 5, clubId: this.clubId })
        ])
        
        this.club = clubRes.data
        this.categories = catRes.data || []
        this.isJoined = joinRes.data
        
        const myApp = applyRes.data.find(a => a.clubId == this.clubId)
        this.hasApplied = !!myApp
        this.application = myApp
        
        this.activities = actRes.data.list || []
        this.feeds = feedRes.data.list || []
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    goApply() {
      uni.navigateTo({ url: '/pages/club/apply?clubId=' + this.clubId })
    },
    
    goChat() {
      uni.navigateTo({ url: '/pages/chat/detail?clubId=' + this.clubId + '&clubName=' + encodeURIComponent(this.club.name) })
    },
    
    async handleQuit() {
      const confirm = await util.showModal('提示', '确定要退出该社团吗？')
      if (confirm) {
        try {
          await api.quitClub(this.clubId)
          util.showToast('已退出社团', 'success')
          this.isJoined = false
        } catch (e) {
          console.error(e)
        }
      }
    },
    
    goActivityDetail(id) {
      uni.navigateTo({ url: '/pages/activity/detail?id=' + id })
    },
    
    goFeedDetail(id) {
      uni.navigateTo({ url: '/pages/feed/detail?id=' + id })
    },
    
    viewAllActivities() {
      uni.switchTab({ url: '/pages/activity/index' })
    },
    
    viewAllFeeds() {
      uni.switchTab({ url: '/pages/feed/index' })
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 40rpx;
}

.club-header {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  padding: 40rpx 30rpx;
  display: flex;
  align-items: center;
}

.club-avatar {
  width: 140rpx;
  height: 140rpx;
  margin-right: 30rpx;
  flex-shrink: 0;
}

.logo-img {
  width: 100%;
  height: 100%;
  border-radius: 24rpx;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 24rpx;
  background: rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 50rpx;
  font-weight: bold;
  color: #fff;
}

.club-info {
  flex: 1;
}

.club-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 15rpx;
}

.club-tags {
  display: flex;
  gap: 15rpx;
  flex-wrap: wrap;
}

.tag {
  padding: 6rpx 20rpx;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.action-bar {
  display: flex;
  gap: 15rpx;
  padding: 20rpx 30rpx;
  background: #fff;
  margin-bottom: 20rpx;
}

.action-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.action-btn.success {
  background: #4cd964;
  color: #fff;
}

.action-btn.outline {
  background: #fff;
  color: #5677fc;
  border: 2rpx solid #5677fc;
}

.action-btn.danger-outline {
  background: #fff;
  color: #dd524d;
  border: 2rpx solid #dd524d;
}

.action-btn.disabled {
  background: #f0f0f0;
  color: #999;
}

.section {
  background: #fff;
  margin: 0 30rpx 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
}

.section-header .section-title {
  margin-bottom: 0;
}

.more-link {
  font-size: 24rpx;
  color: #5677fc;
}

.desc-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.activity-list {
  margin-top: 20rpx;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-date {
  width: 90rpx;
  text-align: center;
  background: #f5f6f8;
  border-radius: 12rpx;
  padding: 10rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.date-month {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.date-day {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #5677fc;
}

.activity-info {
  flex: 1;
  overflow: hidden;
}

.activity-name {
  display: block;
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 8rpx;
}

.activity-time,
.activity-location {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 4rpx;
}

.activity-status {
  font-size: 24rpx;
  font-weight: 500;
  flex-shrink: 0;
}

.feed-list {
  margin-top: 20rpx;
}

.feed-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.feed-item:last-child {
  border-bottom: none;
}

.feed-content {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.feed-images {
  display: flex;
  gap: 10rpx;
  margin-top: 15rpx;
}

.feed-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  background: #f0f0f0;
}

.feed-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15rpx;
}

.feed-time {
  font-size: 24rpx;
  color: #999;
}

.feed-stats {
  display: flex;
  gap: 20rpx;
}

.stat-item {
  font-size: 24rpx;
  color: #999;
}

.empty-tip,
.loading {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 26rpx;
}
</style>
