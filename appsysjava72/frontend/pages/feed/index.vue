<template>
  <view class="feed-container">
    <view class="publish-bar" @click="goPublish">
      <view class="publish-input">
        <text class="publish-icon">✏️</text>
        <text class="placeholder">发布新动态...</text>
      </view>
      <button class="publish-btn">发布</button>
    </view>
    
    <view class="feed-list" v-if="feedList.length > 0">
      <view 
        class="feed-card" 
        v-for="feed in feedList" 
        :key="feed.id"
        @click="goDetail(feed.id)"
      >
        <view class="feed-header">
          <image 
            v-if="feed.userAvatar" 
            :src="feed.userAvatar" 
            class="user-avatar" 
            mode="aspectFill"
          />
          <view v-else class="avatar-placeholder">
            <text>{{ feed.userName ? feed.userName.substring(0, 1) : '用' }}</text>
          </view>
          <view class="user-info">
            <text class="user-name">{{ feed.userName || '社团用户' }}</text>
            <text class="feed-time">{{ util.fromNow(feed.createTime) }}</text>
          </view>
          <text class="club-tag" v-if="feed.clubName">{{ feed.clubName }}</text>
        </view>
        
        <view class="feed-content">
          <text class="content-text">{{ feed.content }}</text>
        </view>
        
        <view class="feed-images" v-if="feed.images">
          <image 
            v-for="(img, idx) in feed.images.split(',').slice(0, 9)" 
            :key="idx" 
            :src="img" 
            mode="aspectFill"
            class="feed-img"
            :class="'grid-' + Math.min(feed.images.split(',').length, 9)"
          />
        </view>
        
        <view class="feed-footer">
          <view 
            class="action-item" 
            :class="{ active: likeMap[feed.id] }"
            @click.stop="toggleLike(feed)"
          >
            <text class="action-icon">{{ likeMap[feed.id] ? '❤️' : '🤍' }}</text>
            <text class="action-text">{{ feed.likeCount || 0 }}</text>
          </view>
          <view class="action-item" @click.stop="goDetail(feed.id)">
            <text class="action-icon">💬</text>
            <text class="action-text">{{ feed.commentCount || 0 }}</text>
          </view>
          <view class="action-item" @click.stop="shareFeed(feed)">
            <text class="action-icon">📤</text>
            <text class="action-text">分享</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">📝</text>
      <text class="empty-text">还没有动态，快来发布第一条吧~</text>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
    
    <view class="load-more" v-if="hasMore && !loading" @click="loadMore">
      <text>加载更多</text>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      feedList: [],
      likeMap: {},
      pageNum: 1,
      pageSize: 10,
      hasMore: true,
      loading: false
    }
  },
  onShow() {
    this.refreshList()
  },
  onPullDownRefresh() {
    this.refreshList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    util,
    
    async refreshList() {
      this.pageNum = 1
      this.hasMore = true
      this.loading = true
      try {
        const res = await api.getFeedPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        this.feedList = res.data.list || []
        this.hasMore = this.pageNum < res.data.totalPage
        this.checkLikeStatus()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async loadMore() {
      this.pageNum++
      this.loading = true
      try {
        const res = await api.getFeedPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        this.feedList = this.feedList.concat(res.data.list || [])
        this.hasMore = this.pageNum < res.data.totalPage
        this.checkLikeStatus()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async checkLikeStatus() {
      for (const feed of this.feedList) {
        try {
          const res = await api.isLiked(feed.id)
          this.likeMap[feed.id] = res.data
        } catch (e) {
          console.error(e)
        }
      }
    },
    
    async toggleLike(feed) {
      try {
        await api.toggleLike(feed.id)
        this.likeMap[feed.id] = !this.likeMap[feed.id]
        if (this.likeMap[feed.id]) {
          feed.likeCount = (feed.likeCount || 0) + 1
        } else {
          feed.likeCount = Math.max(0, (feed.likeCount || 0) - 1)
        }
      } catch (e) {
        console.error(e)
      }
    },
    
    goDetail(id) {
      uni.navigateTo({ url: '/pages/feed/detail?id=' + id })
    },
    
    goPublish() {
      uni.navigateTo({ url: '/pages/feed/publish' })
    },
    
    shareFeed(feed) {
      uni.showToast({ title: '分享功能开发中', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.feed-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 120rpx;
}

.publish-bar {
  display: flex;
  align-items: center;
  background: #fff;
  padding: 20rpx 30rpx;
  margin-bottom: 20rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.publish-input {
  flex: 1;
  display: flex;
  align-items: center;
  height: 72rpx;
  background: #f5f6f8;
  border-radius: 36rpx;
  padding: 0 24rpx;
  margin-right: 20rpx;
}

.publish-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
}

.placeholder {
  font-size: 26rpx;
  color: #999;
}

.publish-btn {
  padding: 12rpx 30rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 26rpx;
  border: none;
}

.feed-list {
  padding: 0 20rpx;
}

.feed-card {
  background: #fff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  padding: 25rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.feed-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.user-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.avatar-placeholder {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.user-info {
  flex: 1;
}

.user-name {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 4rpx;
}

.feed-time {
  font-size: 24rpx;
  color: #999;
}

.club-tag {
  padding: 4rpx 16rpx;
  background: #e8ecff;
  color: #5677fc;
  border-radius: 20rpx;
  font-size: 22rpx;
  flex-shrink: 0;
}

.feed-content {
  margin-bottom: 20rpx;
}

.content-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.7;
}

.feed-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8rpx;
  margin-bottom: 20rpx;
}

.feed-img {
  background: #f0f0f0;
  border-radius: 12rpx;
}

.feed-img.grid-1 {
  width: 100%;
  height: 400rpx;
}

.feed-img.grid-2 {
  width: calc(50% - 4rpx);
  height: 300rpx;
}

.feed-img.grid-3 {
  width: calc(33.33% - 6rpx);
  height: 200rpx;
}

.feed-img.grid-4,
.feed-img.grid-5,
.feed-img.grid-6,
.feed-img.grid-7,
.feed-img.grid-8,
.feed-img.grid-9 {
  width: calc(33.33% - 6rpx);
  height: 200rpx;
}

.feed-footer {
  display: flex;
  justify-content: space-around;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.action-item {
  display: flex;
  align-items: center;
  padding: 10rpx 20rpx;
  border-radius: 20rpx;
  transition: all 0.3s;
}

.action-item.active {
  background: #fff0f0;
}

.action-icon {
  font-size: 32rpx;
  margin-right: 8rpx;
}

.action-text {
  font-size: 26rpx;
  color: #666;
}

.action-item.active .action-text {
  color: #dd524d;
}

.empty-state,
.loading-state,
.load-more {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}

.empty-icon {
  display: block;
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.load-more {
  color: #5677fc;
}
</style>
