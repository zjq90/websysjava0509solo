<template>
  <div class="music-detail">
    <el-card v-loading="loading">
      <div class="music-header" v-if="music">
        <el-image :src="music.coverUrl" class="cover" fit="cover" />
        <div class="info">
          <h1>{{ music.title }}</h1>
          <div class="artist">歌手：{{ music.artistName }}</div>
          <div class="album">专辑：{{ music.album }}</div>
          <div class="tags">
            <el-tag v-for="tag in music.tags" :key="tag" size="small" type="info" style="margin-right: 8px;">
              {{ tag }}
            </el-tag>
          </div>
          <div class="stats">
            <span><el-icon><View /></el-icon> {{ music.playCount }} 播放</span>
            <span><el-icon><Star /></el-icon> {{ music.likeCount }} 喜欢</span>
            <span><el-icon><ChatDotRound /></el-icon> {{ music.commentCount }} 评论</span>
          </div>
          <div class="actions">
            <el-button type="primary" @click="handlePlay">
              <el-icon><VideoPlay /></el-icon>
              播放
            </el-button>
            <el-button @click="handleLike">
              <el-icon><Star /></el-icon>
              喜欢
            </el-button>
            <el-button @click="handleAddToPlaylist">
              <el-icon><Plus /></el-icon>
              加入歌单
            </el-button>
            <el-button @click="handleShare">
              <el-icon><Share /></el-icon>
              分享
            </el-button>
            <el-button type="success" v-if="music.price > 0" @click="handleBuy">
              <el-icon><Goods /></el-icon>
              购买 ¥{{ music.price }}
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <el-card class="comments-card" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>评论 ({{ comments.length }})</span>
        </div>
      </template>
      <div class="comment-input">
        <el-input
          v-model="newComment"
          type="textarea"
          :rows="3"
          placeholder="写下你的评论..."
        />
        <el-button type="primary" style="margin-top: 12px;" @click="submitComment">
          发表评论
        </el-button>
      </div>
      <el-divider />
      <div class="comment-list">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <el-avatar :src="comment.userAvatar" size="small" />
          <div class="comment-content">
            <div class="comment-header">
              <span class="username">{{ comment.username }}</span>
              <span class="time">{{ comment.createdAt }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <el-button type="text" size="small" @click="likeComment(comment.id)">
                <el-icon><ThumbUp /></el-icon>
                {{ comment.likeCount }}
              </el-button>
              <el-button type="text" size="small">回复</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMusicStore } from '../stores/music'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const musicStore = useMusicStore()

const loading = ref(false)
const music = ref<any>(null)
const comments = ref<any[]>([])
const newComment = ref('')

const loadMusic = async () => {
  loading.value = true
  try {
    const id = route.params.id
    music.value = await request.get(`/musics/${id}`)
    comments.value = await request.get(`/musics/${id}/comments`)
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handlePlay = () => {
  if (music.value) {
    musicStore.playMusic(music.value)
    musicStore.addToPlaylist(music.value)
  }
}

const handleLike = async () => {
  try {
    await request.post(`/musics/${music.value.id}/like`)
    music.value.likeCount++
    ElMessage.success('已添加到喜欢')
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleAddToPlaylist = () => {
  ElMessage.info('请选择歌单')
}

const handleShare = () => {
  ElMessage.info('分享功能')
}

const handleBuy = () => {
  ElMessage.info('即将跳转到支付页面')
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  try {
    await request.post(`/musics/${music.value.id}/comments`, {
      content: newComment.value
    })
    newComment.value = ''
    ElMessage.success('评论成功')
    loadMusic()
  } catch (e) {
    ElMessage.error('评论失败')
  }
}

const likeComment = async (commentId: number) => {
  try {
    await request.post(`/comments/${commentId}/like`)
    const comment = comments.value.find(c => c.id === commentId)
    if (comment) comment.likeCount++
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadMusic()
})
</script>

<style scoped>
.music-detail {
  max-width: 1200px;
  margin: 0 auto;
}

.music-header {
  display: flex;
  gap: 32px;
}

.cover {
  width: 240px;
  height: 240px;
  border-radius: 8px;
}

.info {
  flex: 1;
}

.info h1 {
  font-size: 28px;
  margin-bottom: 16px;
}

.artist, .album {
  color: #666;
  margin-bottom: 8px;
}

.tags {
  margin: 16px 0;
}

.stats {
  display: flex;
  gap: 24px;
  color: #999;
  margin: 16px 0;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.comments-card {
  margin-top: 24px;
}

.comment-input {
  margin-bottom: 16px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.username {
  font-weight: 500;
}

.time {
  color: #999;
  font-size: 12px;
}

.comment-text {
  color: #333;
  line-height: 1.6;
}

.comment-actions {
  margin-top: 8px;
}
</style>
