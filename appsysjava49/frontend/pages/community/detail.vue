<template>
  <view class="detail-container">
    <view class="post-detail">
      <view class="post-header">
        <view class="user-info">
          <image class="avatar" :src="post.avatar" mode="aspectFill"></image>
          <view class="user-detail">
            <text class="nickname">{{ post.nickname }}</text>
            <text class="time">{{ post.createTime }}</text>
          </view>
        </view>
        <view class="topic-tag" v-if="post.tag">{{ post.tag }}</view>
      </view>

      <view class="post-content">
        <text class="title">{{ post.title }}</text>
        <text class="desc">{{ post.content }}</text>
      </view>

      <view class="post-images" v-if="post.images && post.images.length > 0">
        <image 
          class="post-img" 
          :src="img" 
          mode="widthFix"
          v-for="(img, idx) in post.images" 
          :key="idx"
          @click="previewImage(idx)"
        ></image>
      </view>

      <view class="post-stats">
        <view class="stat" @click="toggleLike">
          <text class="stat-icon">{{ isLiked ? '❤️' : '🤍' }}</text>
          <text class="stat-num">{{ likeCount }}</text>
        </view>
        <view class="stat">
          <text class="stat-icon">💬</text>
          <text class="stat-num">{{ comments.length }}</text>
        </view>
        <view class="stat">
          <text class="stat-icon">👁</text>
          <text class="stat-num">{{ post.viewCount }}</text>
        </view>
      </view>
    </view>

    <view class="comment-section">
      <view class="section-title">
        <text>全部评论</text>
        <text class="count">({{ comments.length }})</text>
      </view>

      <view class="comment-list">
        <view class="comment-item" v-for="comment in comments" :key="comment.id">
          <image class="comment-avatar" :src="comment.avatar" mode="aspectFill"></image>
          <view class="comment-content">
            <view class="comment-header">
              <text class="comment-nickname">{{ comment.nickname }}</text>
              <text class="comment-time">{{ comment.createTime }}</text>
            </view>
            <text class="comment-text">{{ comment.content }}</text>
            <view class="comment-actions">
              <view class="action-item">
                <text class="action-icon">👍</text>
                <text class="action-num">{{ comment.likeCount }}</text>
              </view>
              <view class="action-item">
                <text class="action-icon">💬</text>
                <text>回复</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="comment-input-section">
      <input 
        class="comment-input" 
        v-model="commentText" 
        placeholder="发表你的评论..."
        @confirm="submitComment"
      />
      <button class="send-btn" :disabled="!commentText.trim()" @click="submitComment">
        发送
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      post: {
        id: 1,
        nickname: '二手达人',
        avatar: 'https://picsum.photos/100/100?random=1',
        createTime: '2小时前',
        title: '如何判断二手手机的真实成色？',
        content: '分享一下我多年买卖二手手机的经验，教大家如何快速判断手机的真实成色，避免被坑。首先看外观：检查屏幕是否有划痕、边框是否有磕碰、后盖是否有磨损。其次看功能：测试触摸屏、摄像头、扬声器、震动等是否正常。最重要的是验机：建议走平台验机，或者当面找专业人士检测。',
        tag: '经验分享',
        images: ['https://picsum.photos/400/300?random=10', 'https://picsum.photos/400/300?random=11'],
        viewCount: 1520
      },
      isLiked: false,
      likeCount: 128,
      commentText: '',
      comments: [
        {
          id: 1,
          nickname: '小明',
          avatar: 'https://picsum.photos/100/100?random=4',
          createTime: '1小时前',
          content: '非常实用的经验，学到了！',
          likeCount: 12
        },
        {
          id: 2,
          nickname: '数码小白',
          avatar: 'https://picsum.photos/100/100?random=5',
          createTime: '30分钟前',
          content: '请问平台验机要收费吗？',
          likeCount: 5
        }
      ]
    }
  },
  methods: {
    toggleLike() {
      this.isLiked = !this.isLiked
      this.likeCount += this.isLiked ? 1 : -1
    },
    previewImage(idx) {
      uni.previewImage({
        current: idx,
        urls: this.post.images
      })
    },
    submitComment() {
      if (!this.commentText.trim()) return
      
      this.comments.unshift({
        id: Date.now(),
        nickname: '我',
        avatar: 'https://picsum.photos/100/100?random=99',
        createTime: '刚刚',
        content: this.commentText,
        likeCount: 0
      })
      
      this.commentText = ''
      uni.showToast({
        title: '评论成功',
        icon: 'success'
      })
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 150rpx;
}

.post-detail {
  background-color: #fff;
  padding: 30rpx;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 90rpx;
  height: 90rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.time {
  font-size: 24rpx;
  color: #999;
}

.topic-tag {
  background-color: #ECF5FF;
  color: #409EFF;
  padding: 10rpx 25rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
}

.post-content {
  margin-bottom: 30rpx;
}

.title {
  display: block;
  font-size: 36rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
  line-height: 1.4;
}

.desc {
  display: block;
  font-size: 30rpx;
  color: #666;
  line-height: 1.8;
}

.post-images {
  margin-bottom: 30rpx;
}

.post-img {
  width: 100%;
  border-radius: 15rpx;
  margin-bottom: 15rpx;
  background-color: #f5f5f5;
}

.post-stats {
  display: flex;
  justify-content: space-around;
  padding: 25rpx 0;
  border-top: 1rpx solid #eee;
  border-bottom: 1rpx solid #eee;
}

.stat {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.stat-icon {
  font-size: 32rpx;
}

.stat-num {
  font-size: 28rpx;
  color: #666;
}

.comment-section {
  background-color: #fff;
  margin-top: 20rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.section-title .count {
  color: #999;
  font-weight: normal;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.comment-item {
  display: flex;
  gap: 20rpx;
}

.comment-avatar {
  width: 70rpx;
  height: 70rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.comment-nickname {
  font-size: 28rpx;
  color: #409EFF;
  font-weight: bold;
}

.comment-time {
  font-size: 24rpx;
  color: #999;
}

.comment-text {
  display: block;
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  margin-bottom: 15rpx;
}

.comment-actions {
  display: flex;
  gap: 40rpx;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.action-icon {
  font-size: 26rpx;
}

.action-num {
  font-size: 24rpx;
  color: #999;
}

.comment-input-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 20rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.comment-input {
  flex: 1;
  height: 70rpx;
  padding: 0 25rpx;
  background-color: #f5f5f5;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.send-btn {
  width: 120rpx;
  height: 70rpx;
  background-color: #409EFF;
  color: #fff;
  border: none;
  border-radius: 35rpx;
  font-size: 28rpx;
}

.send-btn[disabled] {
  background-color: #ccc;
}
</style>
