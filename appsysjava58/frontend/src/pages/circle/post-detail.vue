<template>
  <view class="container">
    <view class="post-detail">
      <view class="post-header">
        <view class="author-avatar">
          <text class="avatar-icon">👤</text>
        </view>
        <view class="author-info">
          <text class="author-name">测试用户</text>
          <text class="post-time">2小时前</text>
        </view>
      </view>

      <view class="post-content">
        <text class="post-title">{{ post.title }}</text>
        <text class="post-text">{{ post.content }}</text>
      </view>

      <view class="post-stats">
        <view class="stat-item">
          <text class="stat-icon">👁️</text>
          <text class="stat-text">{{ post.viewCount }} 浏览</text>
        </view>
        <view class="stat-item">
          <text class="stat-icon">❤️</text>
          <text class="stat-text">{{ post.likeCount }} 点赞</text>
        </view>
        <view class="stat-item">
          <text class="stat-icon">💬</text>
          <text class="stat-text">{{ post.commentCount }} 评论</text>
        </view>
      </view>
    </view>

    <view class="comment-section">
      <view class="section-header">
        <text class="section-title">评论区</text>
      </view>
      <view class="comment-list">
        <view class="comment-item" v-for="comment in comments" :key="comment.id">
          <view class="comment-avatar">
            <text class="avatar-icon">👤</text>
          </view>
          <view class="comment-content">
            <view class="comment-header">
              <text class="comment-author">{{ comment.author }}</text>
              <text class="comment-time">{{ comment.time }}</text>
            </view>
            <text class="comment-text">{{ comment.text }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="comment-input">
      <input type="text" class="input-field" v-model="newComment" placeholder="写下你的评论..." />
      <button class="send-btn" @click="sendComment">发送</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      post: {
        title: '',
        content: '',
        viewCount: 0,
        likeCount: 0,
        commentCount: 0
      },
      comments: [],
      newComment: ''
    }
  },
  onLoad(options) {
    this.loadPostDetail(options.id)
  },
  methods: {
    loadPostDetail(id) {
      this.post = {
        id: id,
        title: '金毛幼犬喂养心得分享',
        content: '分享一下我家金毛从小到大的喂养经验，选择优质狗粮很重要，定期体检也不能少。建议大家都去正规宠物医院咨询专业意见，不要轻信网上的偏方哦！\n\n1. 0-3个月：羊奶粉+奶糕粮，少量多餐\n2. 3-6个月：幼犬粮，注意补钙\n3. 6-12个月：逐渐过渡到成犬粮\n4. 定期驱虫，按时接种疫苗\n\n希望对大家有帮助！',
        viewCount: 156,
        likeCount: 32,
        commentCount: 8
      }

      this.comments = [
        { id: 1, author: '爱猫人士', time: '1小时前', text: '写得很详细，收藏了！' },
        { id: 2, author: '铲屎官小王', time: '30分钟前', text: '我家也是金毛，请问你们吃的什么狗粮？' }
      ]
    },
    sendComment() {
      if (!this.newComment.trim()) {
        this.$showToast('请输入评论内容')
        return
      }
      this.comments.unshift({
        id: Date.now(),
        author: '我',
        time: '刚刚',
        text: this.newComment
      })
      this.post.commentCount++
      this.newComment = ''
      this.$showToast('评论成功', 'success')
    }
  }
}
</script>

<style scoped>
.post-detail {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 25rpx;
  padding-bottom: 25rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.author-avatar {
  width: 80rpx;
  height: 80rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-icon {
  font-size: 40rpx;
}

.author-info {
  flex: 1;
}

.author-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.post-time {
  font-size: 22rpx;
  color: #999;
}

.post-content {
  margin-bottom: 25rpx;
}

.post-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  line-height: 1.5;
}

.post-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
  white-space: pre-wrap;
}

.post-stats {
  display: flex;
  gap: 40rpx;
  padding-top: 25rpx;
  border-top: 1rpx solid #f0f0f0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.stat-icon {
  font-size: 28rpx;
}

.stat-text {
  font-size: 24rpx;
  color: #999;
}

.comment-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 120rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.section-header {
  margin-bottom: 25rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.comment-item {
  display: flex;
  gap: 20rpx;
}

.comment-avatar {
  width: 60rpx;
  height: 60rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.comment-author {
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
}

.comment-time {
  font-size: 22rpx;
  color: #999;
}

.comment-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.comment-input {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 20rpx 30rpx;
  background: #fff;
  border-top: 1rpx solid #f0f0f0;
}

.input-field {
  flex: 1;
  height: 70rpx;
  background: #f5f5f5;
  border-radius: 35rpx;
  padding: 0 25rpx;
  font-size: 26rpx;
}

.send-btn {
  padding: 15rpx 30rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 35rpx;
  font-size: 26rpx;
  border: none;
}
</style>