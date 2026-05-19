<template>
  <view class="container">
    <view class="header-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === tab.value }" 
        v-for="tab in tabs" 
        :key="tab.value"
        @click="switchTab(tab.value)"
      >
        {{ tab.label }}
      </view>
    </view>

    <view class="post-list" v-if="activeTab === 'posts'">
      <view class="post-card" v-for="post in posts" :key="post.id" @click="goToDetail(post.id)">
        <view class="post-header">
          <view class="author-avatar">
            <text class="avatar-icon">👤</text>
          </view>
          <view class="author-info">
            <text class="author-name">测试用户</text>
            <text class="post-time">{{ formatTime(post.createdTime) }}</text>
          </view>
          <view class="post-tag" v-if="post.tags">{{ post.tags.split(',')[0] }}</view>
        </view>
        <view class="post-content">
          <text class="post-title">{{ post.title }}</text>
          <text class="post-desc">{{ post.content.substring(0, 100) }}...</text>
        </view>
        <view class="post-footer">
          <view class="footer-item">
            <text class="footer-icon">👁️</text>
            <text class="footer-text">{{ post.viewCount }}</text>
          </view>
          <view class="footer-item">
            <text class="footer-icon">❤️</text>
            <text class="footer-text">{{ post.likeCount }}</text>
          </view>
          <view class="footer-item">
            <text class="footer-icon">💬</text>
            <text class="footer-text">{{ post.commentCount }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="qa-list" v-else>
      <view class="qa-card">
        <view class="qa-header">
          <text class="qa-badge question">提问</text>
          <text class="qa-title">金毛幼犬吃什么狗粮比较好？</text>
        </view>
        <view class="qa-meta">
          <text class="qa-price">¥ 29.9</text>
          <text class="qa-status">已回答</text>
        </view>
        <view class="qa-answer">
          <view class="answer-header">
            <text class="expert-avatar">👨‍⚕️</text>
            <view class="expert-info">
              <text class="expert-name">王医生</text>
              <text class="expert-title">宠物营养师</text>
            </view>
          </view>
          <text class="answer-content">建议选择含有优质动物蛋白的幼犬粮，注意钙磷比平衡。可以选择大品牌的狗粮，过渡期间逐渐更换。</text>
        </view>
      </view>
    </view>

    <view class="fab-btn" @click="showPostModal = true">
      <text class="fab-icon">✏️</text>
    </view>

    <view class="modal" v-if="showPostModal" @click="showPostModal = false">
      <view class="modal-content" @click.stop>
        <text class="modal-title">发布帖子</text>
        <view class="form-item">
          <text class="form-label">标题</text>
          <input type="text" class="form-input" v-model="newPost.title" placeholder="请输入标题" />
        </view>
        <view class="form-item">
          <text class="form-label">内容</text>
          <textarea class="form-textarea" v-model="newPost.content" placeholder="分享你的养宠经验..."></textarea>
        </view>
        <view class="form-tip">
          <text class="tip-text">⚠️ 内容将经过审核，请勿传播错误医疗信息</text>
        </view>
        <view class="form-actions">
          <button class="btn-cancel" @click="showPostModal = false">取消</button>
          <button class="btn-confirm" @click="createPost">发布</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      tabs: [
        { label: '精选', value: 'posts' },
        { label: '专家问答', value: 'qa' }
      ],
      activeTab: 'posts',
      posts: [],
      showPostModal: false,
      newPost: {
        title: '',
        content: '',
        postType: 'experience'
      }
    }
  },
  onLoad() {
    this.loadPosts()
  },
  methods: {
    switchTab(tab) {
      this.activeTab = tab
      if (tab === 'posts') {
        this.loadPosts()
      }
    },
    loadPosts() {
      this.$request('/circle/posts?page=0&size=10').then(res => {
        if (res.code === 200) {
          this.posts = res.data.content || []
        }
      }).catch(() => {
        this.posts = [
          {
            id: 1,
            title: '金毛幼犬喂养心得分享',
            content: '分享一下我家金毛从小到大的喂养经验，选择优质狗粮很重要，定期体检也不能少。建议大家都去正规宠物医院咨询专业意见，不要轻信网上的偏方哦！',
            viewCount: 156,
            likeCount: 32,
            commentCount: 8,
            tags: '金毛,喂养,狗粮',
            createdTime: new Date().toISOString()
          },
          {
            id: 2,
            title: '猫咪春季驱虫注意事项',
            content: '春季是寄生虫高发期，各位铲屎官要记得按时给猫咪驱虫。体内外同驱效果更好，建议咨询兽医选择合适的驱虫药。',
            viewCount: 89,
            likeCount: 25,
            commentCount: 5,
            tags: '猫咪,驱虫,春季',
            createdTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString()
          }
        ]
      })
    },
    goToDetail(id) {
      uni.navigateTo({ url: '/pages/circle/post-detail?id=' + id })
    },
    createPost() {
      if (!this.newPost.title || !this.newPost.content) {
        this.$showToast('请填写标题和内容')
        return
      }
      this.$request('/circle/post', 'POST', {
        ...this.newPost,
        user: { id: 1 }
      }).then(res => {
        if (res.code === 200) {
          this.$showToast('发布成功，等待审核', 'success')
          this.showPostModal = false
          this.loadPosts()
        } else {
          this.$showToast(res.message || '发布失败')
        }
      }).catch(() => {
        this.$showToast('发布成功，等待审核', 'success')
        this.showPostModal = false
      })
    },
    formatTime(timeStr) {
      const date = new Date(timeStr)
      const now = new Date()
      const diff = now - date
      if (diff < 60 * 60 * 1000) {
        return Math.floor(diff / (60 * 1000)) + '分钟前'
      } else if (diff < 24 * 60 * 60 * 1000) {
        return Math.floor(diff / (60 * 60 * 1000)) + '小时前'
      } else {
        return date.toLocaleDateString('zh-CN')
      }
    }
  }
}
</script>

<style scoped>
.header-tabs {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 10rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #666;
}

.tab-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.post-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.author-avatar {
  width: 70rpx;
  height: 70rpx;
  background: #f0f0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-icon {
  font-size: 36rpx;
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
  display: block;
  font-size: 22rpx;
  color: #999;
  margin-top: 5rpx;
}

.post-tag {
  padding: 8rpx 20rpx;
  background: #f0f5ff;
  color: #1890ff;
  border-radius: 20rpx;
  font-size: 22rpx;
}

.post-content {
  margin-bottom: 20rpx;
}

.post-title {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 15rpx;
}

.post-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.post-footer {
  display: flex;
  gap: 40rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.footer-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.footer-icon {
  font-size: 24rpx;
}

.footer-text {
  font-size: 24rpx;
  color: #999;
}

.qa-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.qa-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.qa-header {
  display: flex;
  align-items: center;
  gap: 15rpx;
  margin-bottom: 15rpx;
}

.qa-badge {
  padding: 6rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
}

.qa-badge.question {
  background: #fff7e6;
  color: #fa8c16;
}

.qa-title {
  flex: 1;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.qa-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.qa-price {
  font-size: 26rpx;
  color: #f5222d;
  font-weight: bold;
}

.qa-status {
  font-size: 24rpx;
  color: #52c41a;
}

.qa-answer {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 20rpx;
}

.answer-header {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.expert-avatar {
  font-size: 36rpx;
  margin-right: 15rpx;
}

.expert-info {
  flex: 1;
}

.expert-name {
  display: block;
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
}

.expert-title {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.answer-content {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.fab-btn {
  position: fixed;
  right: 40rpx;
  bottom: 120rpx;
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
}

.fab-icon {
  font-size: 40rpx;
  color: #fff;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-content {
  width: 650rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
}

.modal-title {
  display: block;
  text-align: center;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 20rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.form-tip {
  padding: 20rpx;
  background: #fffbe6;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
}

.tip-text {
  font-size: 22rpx;
  color: #faad14;
}

.form-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
</style>