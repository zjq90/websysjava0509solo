<template>
  <div class="post-detail">
    <div class="post-card">
      <div class="post-header">
        <div class="post-avatar">
          <el-icon :size="20"><User /></el-icon>
        </div>
        <div class="post-user">
          <div class="post-username">收藏爱好者小王</div>
          <div class="post-time text-light">2小时前</div>
        </div>
      </div>

      <h1 class="post-title">我的清代青花瓷瓶收藏之路</h1>
      <div class="post-content">
        分享一下我收藏这件清代青花瓷瓶的经历，从偶然发现到最终入手，过程非常有趣。
        <br /><br />
        那是在去年的一个古玩市场上，我本来只是随便逛逛，并没有特别想要买什么东西。
        就在我准备离开的时候，一个角落里的摊位上，这件青花瓷瓶吸引了我的目光。
        <br /><br />
        它的釉色温润，花纹精细，造型也非常优美。我一眼就看出这是清代康熙年间的作品。
        经过一番讨价还价，最终以一个比较合理的价格入手了。
        <br /><br />
        现在这件藏品已经成为我收藏中的珍品了，每次看到它都让我感到非常满足。
      </div>

      <div class="post-images">
        <img src="https://picsum.photos/400/400?random=50" class="post-image" />
        <img src="https://picsum.photos/400/400?random=51" class="post-image" />
      </div>

      <div class="post-stats">
        <span><el-icon><View /></el-icon> 2560 浏览</span>
        <span><el-icon><Star /></el-icon> 128 点赞</span>
        <span><el-icon><ChatDotRound /></el-icon> 32 评论</span>
      </div>
    </div>

    <div class="comment-section">
      <h3 class="section-title">评论 (32)</h3>
      
      <div class="comment-input">
        <el-input
          v-model="commentText"
          type="textarea"
          :rows="3"
          placeholder="发表你的评论..."
        />
        <el-button type="primary" class="send-btn" @click="sendComment">
          发表评论
        </el-button>
      </div>

      <div class="comment-list">
        <div class="comment-item" v-for="(comment, index) in comments" :key="index">
          <div class="comment-avatar">
            <el-icon :size="18"><User /></el-icon>
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-user">{{ comment.user }}</span>
              <span class="comment-time text-light">{{ comment.time }}</span>
            </div>
            <div class="comment-text">{{ comment.text }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { User, View, Star, ChatDotRound } from '@element-plus/icons-vue'

const commentText = ref('')

const comments = ref([
  { user: '古玩爱好者', time: '1小时前', text: '恭喜入手好藏品！这件青花瓷瓶看起来确实是真品，釉色和花纹都很到位。' },
  { user: '瓷器鉴定师', time: '30分钟前', text: '从图片来看，确实是康熙年间的青花瓷。胎质细腻，釉面光亮，值得收藏。' },
  { user: '藏友小李', time: '10分钟前', text: '羡慕啊，我也一直在找这样的好东西，请问花了多少大洋入手的？' }
])

const sendComment = () => {
  if (!commentText.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  ElMessage.success('评论发表成功')
  commentText.value = ''
}
</script>

<style scoped>
.post-detail {
  padding: 20px;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.post-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #f5f0eb;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #8B4513;
}

.post-user {
  display: flex;
  flex-direction: column;
}

.post-username {
  font-size: 15px;
  font-weight: bold;
  color: #333;
}

.post-time {
  font-size: 13px;
  color: #999;
}

.post-title {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin: 0 0 20px;
  line-height: 1.4;
}

.post-content {
  font-size: 16px;
  color: #666;
  line-height: 1.8;
  margin-bottom: 24px;
}

.post-images {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.post-image {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  object-fit: cover;
}

.post-stats {
  display: flex;
  gap: 32px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
  font-size: 14px;
  color: #999;
}

.post-stats span {
  display: flex;
  align-items: center;
  gap: 6px;
}

.comment-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0 0 20px;
}

.comment-input {
  margin-bottom: 24px;
}

.send-btn {
  margin-top: 12px;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  border: none;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #f5f0eb;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8B4513;
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.comment-user {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-text {
  font-size: 15px;
  color: #666;
  line-height: 1.6;
}

.elder-mode .post-title {
  font-size: 26px;
}

.elder-mode .post-content {
  font-size: 20px;
}

.elder-mode .comment-text {
  font-size: 18px;
}
</style>
