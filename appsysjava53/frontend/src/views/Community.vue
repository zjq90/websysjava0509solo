<template>
  <div class="community-container">
    <el-button type="primary" class="publish-btn" @click="goPublish">
      <el-icon><Plus /></el-icon>
      发布帖子
    </el-button>

    <div class="post-list">
      <div
        class="post-card"
        v-for="post in postList"
        :key="post.id"
        @click="goToDetail(post.id)"
      >
        <div class="post-header">
          <div class="post-avatar">
            <el-icon :size="20"><User /></el-icon>
          </div>
          <div class="post-user">
            <div class="post-username">{{ post.username }}</div>
            <div class="post-time text-light">{{ post.time }}</div>
          </div>
          <el-tag v-if="post.isTop" type="danger" size="small">置顶</el-tag>
        </div>

        <h3 class="post-title" v-if="post.title">{{ post.title }}</h3>
        <div class="post-content">{{ post.content }}</div>

        <div class="post-images" v-if="post.images && post.images.length">
          <img
            v-for="(img, idx) in post.images.slice(0, 3)"
            :key="idx"
            :src="img"
            class="post-image"
          />
        </div>

        <div class="post-footer">
          <div class="post-action" @click.stop="likePost(post)">
            <el-icon :size="18" :color="post.isLiked ? '#ff4d4f' : '#999'">
              <Star :fill="post.isLiked ? '#ff4d4f' : 'none'" />
            </el-icon>
            <span>{{ post.likes }}</span>
          </div>
          <div class="post-action">
            <el-icon :size="18"><ChatDotRound /></el-icon>
            <span>{{ post.comments }}</span>
          </div>
          <div class="post-action">
            <el-icon :size="18"><View /></el-icon>
            <span>{{ post.views }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="load-more" v-if="hasMore" @click="loadMore">
      <el-button type="text" size="large">加载更多</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, User, Star, ChatDotRound, View } from '@element-plus/icons-vue'

const router = useRouter()
const hasMore = ref(true)

const postList = ref([
  {
    id: 1,
    username: '收藏爱好者小王',
    title: '我的清代青花瓷瓶收藏之路',
    content: '分享一下我收藏这件清代青花瓷瓶的经历，从偶然发现到最终入手，过程非常有趣。',
    time: '2小时前',
    likes: 128,
    comments: 32,
    views: 2560,
    isTop: true,
    isLiked: false,
    images: ['https://picsum.photos/300/300?random=20', 'https://picsum.photos/300/300?random=21']
  },
  {
    id: 2,
    username: '古董藏家老李',
    title: '如何辨别青铜器真伪？',
    content: '作为一个有十几年收藏经验的藏家，今天给大家分享一些青铜器辨别的小技巧，希望对大家有所帮助。',
    time: '5小时前',
    likes: 256,
    comments: 48,
    views: 5120,
    isTop: false,
    isLiked: true,
    images: ['https://picsum.photos/300/300?random=22']
  },
  {
    id: 3,
    username: '鉴定专家张教授',
    title: '专家解读：明清瓷器鉴定要点',
    content: '作为文物鉴定专家，今天给大家系统讲解一下明清瓷器的鉴定要点，包括胎质、釉色、纹饰、款识等方面。',
    time: '昨天',
    likes: 512,
    comments: 86,
    views: 10240,
    isTop: false,
    isLiked: false,
    images: []
  }
])

const goPublish = () => {
  ElMessage.info('发布功能开发中')
}

const goToDetail = (id) => {
  router.push(`/post/${id}`)
}

const likePost = (post) => {
  post.isLiked = !post.isLiked
  post.likes += post.isLiked ? 1 : -1
}

const loadMore = () => {
  ElMessage.info('加载中...')
  setTimeout(() => {
    hasMore.value = false
  }, 1000)
}
</script>

<style scoped>
.community-container {
  padding: 20px;
}

.publish-btn {
  width: 100%;
  height: 48px;
  margin-bottom: 20px;
  background: linear-gradient(135deg, #8B4513 0%, #A0522D 100%);
  border: none;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.post-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f0eb;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #8B4513;
}

.post-user {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.post-username {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 0 0 12px;
}

.post-content {
  font-size: 15px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
}

.post-images {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

.post-image {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  object-fit: cover;
}

.post-footer {
  display: flex;
  gap: 32px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.post-action {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #999;
}

.load-more {
  text-align: center;
  padding: 20px;
}

.elder-mode .publish-btn {
  height: 56px;
  font-size: 18px;
}

.elder-mode .post-title {
  font-size: 20px;
}

.elder-mode .post-content {
  font-size: 18px;
}

.elder-mode .post-action {
  font-size: 16px;
}
</style>
