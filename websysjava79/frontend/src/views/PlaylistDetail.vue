<template>
  <div class="playlist-detail" v-if="playlist">
    <div class="playlist-header">
      <img :src="playlist.coverUrl" :alt="playlist.name" class="playlist-cover" />
      <div class="playlist-info">
        <h1 class="playlist-name">{{ playlist.name }}</h1>
        <p class="playlist-description">{{ playlist.description }}</p>
        <div class="playlist-meta">
          <span>创建者: {{ playlist.creator?.nickname }}</span>
          <span>{{ playlist.playCount }} 次播放</span>
          <span>{{ playlist.likeCount }} 收藏</span>
        </div>
        <div class="playlist-actions">
          <el-button type="primary" @click="playAll">
            <el-icon><VideoPlay /></el-icon>
            播放全部
          </el-button>
          <el-button @click="toggleLike">
            <el-icon><Star /></el-icon>
            收藏
          </el-button>
          <el-button>
            <el-icon><Share /></el-icon>
            分享
          </el-button>
        </div>
      </div>
    </div>

    <div class="playlist-content">
      <h2>歌曲列表</h2>
      <MusicList :musics="playlist.musics || []" @play="playMusic" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMusicStore } from '../stores/music'
import request from '../utils/request'
import MusicList from '../components/MusicList.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const musicStore = useMusicStore()

const playlist = ref<any>(null)

const loadPlaylist = async () => {
  const id = route.params.id
  try {
    playlist.value = await request.get(`/playlists/${id}`)
  } catch (error) {
    console.error('Failed to load playlist')
  }
}

const playMusic = (music: any) => {
  musicStore.playMusic(music)
  musicStore.addToPlaylist(music)
}

const playAll = () => {
  if (playlist.value?.musics?.length > 0) {
    playlist.value.musics.forEach((m: any) => musicStore.addToPlaylist(m))
    musicStore.playMusic(playlist.value.musics[0])
    ElMessage.success('开始播放')
  }
}

const toggleLike = async () => {
  try {
    await request.post(`/playlists/${playlist.value.id}/favorite`)
    ElMessage.success('收藏成功')
  } catch (error) {
    console.error('Failed to toggle like')
  }
}

onMounted(() => {
  loadPlaylist()
})
</script>

<style scoped>
.playlist-detail {
  padding-bottom: 100px;
}

.playlist-header {
  display: flex;
  gap: 32px;
  background: white;
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
}

.playlist-cover {
  width: 240px;
  height: 240px;
  border-radius: 8px;
  object-fit: cover;
}

.playlist-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.playlist-name {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 16px;
}

.playlist-description {
  color: #666;
  margin: 0 0 20px;
  line-height: 1.6;
}

.playlist-meta {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: #999;
  margin-bottom: 24px;
}

.playlist-actions {
  display: flex;
  gap: 12px;
}

.playlist-content h2 {
  font-size: 20px;
  margin-bottom: 16px;
}
</style>
