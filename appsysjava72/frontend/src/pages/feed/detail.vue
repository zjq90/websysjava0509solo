<template>
  <view class="feed-detail-container">
    <view class="feed-content" v-if="feed">
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
      
      <view class="feed-body">
        <text class="content-text">{{ feed.content }}</text>
        
        <view class="feed-images" v-if="feed.images">
          <image 
            v-for="(img, idx) in feed.images.split(',')" 
            :key="idx" 
            :src="img" 
            mode="aspectFill"
            class="feed-img"
            @click="previewImage(idx)"
          />
        </view>
      </view>
      
      <view class="feed-stats">
        <text class="stat-item">👁️ {{ feed.viewCount || 0 }} 浏览</text>
        <text class="stat-item">❤️ {{ feed.likeCount || 0 }} 点赞</text>
        <text class="stat-item">💬 {{ feed.commentCount || 0 }} 评论</text>
      </view>
      
      <view class="feed-actions">
        <view 
          class="action-btn" 
          :class="{ active: isLiked }"
          @click="toggleLike"
        >
          <text class="action-icon">{{ isLiked ? '❤️' : '🤍' }}</text>
          <text class="action-text">{{ feed.likeCount || 0 }}</text>
        </view>
        <view class="action-btn" @click="focusComment">
          <text class="action-icon">💬</text>
          <text class="action-text">{{ feed.commentCount || 0 }}</text>
        </view>
        <view class="action-btn" @click="shareFeed">
          <text class="action-icon">📤</text>
          <text class="action-text">分享</text>
        </view>
      </view>
    </view>
    
    <view class="comment-section">
      <view class="section-header">
        <text class="section-title">全部评论 ({{ commentList.length }})</text>
      </view>
      
      <view class="comment-list" v-if="commentList.length > 0">
        <view 
          class="comment-item" 
          v-for="comment in commentList" 
          :key="comment.id"
        >
          <image 
            v-if="comment.userAvatar" 
            :src="comment.userAvatar" 
            class="comment-avatar" 
            mode="aspectFill"
          />
          <view v-else class="comment-avatar-placeholder">
            <text>{{ comment.userName ? comment.userName.substring(0, 1) : '用' }}</text>
          </view>
          <view class="comment-content">
            <view class="comment-header">
              <text class="comment-user">{{ comment.userName || '用户' }}</text>
              <text class="comment-time">{{ util.fromNow(comment.createTime) }}</text>
            </view>
            <text class="comment-text">{{ comment.content }}</text>
            <view class="comment-actions">
              <text class="reply-btn" @click="replyTo(comment)">回复</text>
            </view>
            
            <view class="reply-list" v-if="comment.replies && comment.replies.length > 0">
              <view 
                class="reply-item" 
                v-for="reply in comment.replies" 
                :key="reply.id"
              >
                <text class="reply-user">{{ reply.userName }}</text>
                <text class="reply-text" v-if="reply.replyToName"> 回复 @{{ reply.replyToName }}：{{ reply.content }}</text>
                <text class="reply-text" v-else>：{{ reply.content }}</text>
                <text class="reply-time">{{ util.fromNow(reply.createTime) }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <view class="empty-comments" v-else>
        <text class="empty-icon">💬</text>
        <text class="empty-text">还没有评论，快来抢沙发吧~</text>
      </view>
    </view>
    
    <view class="input-bar">
      <input 
        v-model="commentText" 
        :placeholder="replyTo ? '回复 @' + replyTo.userName : '发表评论...'" 
        class="comment-input"
        confirm-type="send"
        @confirm="sendComment"
      />
      <button 
        class="send-btn" 
        :disabled="!commentText.trim()"
        @click="sendComment"
      >
        发送
      </button>
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
      feedId: null,
      feed: null,
      commentList: [],
      commentText: '',
      isLiked: false,
      replyTo: null,
      loading: false
    }
  },
  onLoad(options) {
    this.feedId = options.id
    this.loadData()
  },
  methods: {
    util,
    
    async loadData() {
      this.loading = true
      try {
        const [feedRes, likeRes, commentRes] = await Promise.all([
          api.getFeedDetail(this.feedId),
          api.isLiked(this.feedId),
          api.getComments(this.feedId)
        ])
        this.feed = feedRes.data
        this.isLiked = likeRes.data
        this.commentList = this.buildCommentTree(commentRes.data || [])
      } catch (e) {
        util.toast('加载失败')
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    buildCommentTree(comments) {
      const map = {}
      const roots = []
      
      comments.forEach(comment => {
        comment.replies = []
        map[comment.id] = comment
      })
      
      comments.forEach(comment => {
        if (comment.parentId && map[comment.parentId]) {
          map[comment.parentId].replies.push(comment)
        } else {
          roots.push(comment)
        }
      })
      
      return roots
    },
    
    async toggleLike() {
      try {
        await api.toggleLike(this.feedId)
        this.isLiked = !this.isLiked
        if (this.isLiked) {
          this.feed.likeCount = (this.feed.likeCount || 0) + 1
        } else {
          this.feed.likeCount = Math.max(0, (this.feed.likeCount || 0) - 1)
        }
      } catch (e) {
        console.error(e)
      }
    },
    
    previewImage(index) {
      const images = this.feed.images.split(',')
      uni.previewImage({
        current: index,
        urls: images
      })
    },
    
    focusComment() {
      this.$refs.commentInput && this.$refs.commentInput.focus()
    },
    
    replyTo(comment) {
      this.replyTo = comment
    },
    
    async sendComment() {
      if (!this.commentText.trim()) return
      
      try {
        const res = await api.addComment(
          this.feedId,
          this.commentText.trim(),
          this.replyTo ? this.replyTo.id : null,
          this.replyTo ? this.replyTo.userId : null
        )
        
        if (res.code === 200) {
          util.toast('评论成功')
          this.commentText = ''
          this.replyTo = null
          this.loadData()
        } else {
          util.toast(res.message || '评论失败')
        }
      } catch (e) {
        util.toast('评论失败，请重试')
        console.error(e)
      }
    },
    
    shareFeed() {
      uni.showShareMenu({
        withShareTicket: true
      })
    }
  }
}
</script>

<style scoped>
.feed-detail-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 120rpx;
}

.feed-content {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.feed-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.user-avatar,
.comment-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.avatar-placeholder,
.comment-avatar-placeholder {
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

.user-name,
.comment-user {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 4rpx;
}

.feed-time,
.comment-time,
.reply-time {
  font-size: 24rpx;
  color: #999;
}

.club-tag {
  padding: 6rpx 20rpx;
  background: #e8ecff;
  color: #5677fc;
  border-radius: 24rpx;
  font-size: 24rpx;
  flex-shrink: 0;
}

.feed-body {
  margin-bottom: 24rpx;
}

.content-text {
  font-size: 30rpx;
  color: #333;
  line-height: 1.8;
}

.feed-images {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-top: 24rpx;
}

.feed-img {
  width: calc(33.33% - 8rpx);
  height: 200rpx;
  background: #f0f0f0;
  border-radius: 12rpx;
}

.feed-stats {
  display: flex;
  padding: 20rpx 0;
  border-top: 1rpx solid #f0f0f0;
  border-bottom: 1rpx solid #f0f0f0;
  margin-bottom: 20rpx;
}

.stat-item {
  font-size: 26rpx;
  color: #999;
  margin-right: 40rpx;
}

.feed-actions {
  display: flex;
  justify-content: space-around;
}

.action-btn {
  display: flex;
  align-items: center;
  padding: 16rpx 40rpx;
  border-radius: 30rpx;
  transition: all 0.3s;
}

.action-btn.active {
  background: #fff0f0;
}

.action-btn.active .action-text {
  color: #dd524d;
}

.action-icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.action-text {
  font-size: 28rpx;
  color: #666;
}

.comment-section {
  background: #fff;
  padding: 30rpx;
}

.section-header {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.comment-list {
  padding-bottom: 30rpx;
}

.comment-item {
  display: flex;
  margin-bottom: 30rpx;
}

.comment-item:last-child {
  margin-bottom: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8rpx;
}

.comment-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  margin-bottom: 12rpx;
}

.comment-actions {
  margin-bottom: 16rpx;
}

.reply-btn {
  font-size: 24rpx;
  color: #5677fc;
}

.reply-list {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 20rpx;
}

.reply-item {
  margin-bottom: 16rpx;
}

.reply-item:last-child {
  margin-bottom: 0;
}

.reply-user {
  font-size: 26rpx;
  color: #5677fc;
  font-weight: 600;
}

.reply-text {
  font-size: 26rpx;
  color: #333;
  line-height: 1.5;
}

.reply-time {
  display: block;
  margin-top: 6rpx;
}

.empty-comments {
  text-align: center;
  padding: 80rpx 0;
}

.empty-icon {
  display: block;
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.input-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  border-top: 1rpx solid #f0f0f0;
  z-index: 100;
}

.comment-input {
  flex: 1;
  height: 72rpx;
  background: #f5f6f8;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  margin-right: 20rpx;
}

.send-btn {
  padding: 14rpx 36rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 36rpx;
  font-size: 28rpx;
  border: none;
}

.send-btn[disabled] {
  background: #ccc;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
