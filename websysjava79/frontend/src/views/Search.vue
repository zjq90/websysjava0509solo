<template>
  <div class="search-page">
    <h1>搜索结果</h1>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="音乐" name="music">
        <div v-if="musicResults.length > 0">
          <MusicList :musics="musicResults" @play="playMusic" />
        </div>
        <el-empty v-else description="暂无搜索结果" />
      </el-tab-pane>
      <el-tab-pane label="歌单" name="playlist">
        <div v-if="playlistResults.length > 0" class="playlist-grid">
          <div 
            v-for="playlist in playlistResults" 
            :key="playlist.id" 
            class="playlist-card"
            @click="goToPlaylist(playlist.id)"
          >
            <div class="playlist-cover">
              <img :src="playlist.coverUrl" :alt="playlist.name" />
            </div>
            <div class="playlist-name">{{ playlist.name }}</div>
          </div>
        </div>
        <el-empty v-else description="暂无搜索结果" />
      </el-tab-pane>
      <el-tab-pane label="用户" name="user">
        <div v-if="userResults.length > 0" class="user-list">
          <div v-for="user in userResults" :key="user.id" class="user-item">
            <el-avatar :size="64" :src="user.avatar" />
            <div class="user-info">
              <div class="user-name">{{ user.nickname }}</div>
              <div class="user-bio">{{ user.bio }}</div>
            </div>
            <el-button type="primary" @click="goToUser(user.id)">查看主页</el-button>
          </div>
        </div>
        <el-empty v-else description="暂无搜索结果" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMusicStore } from '../stores/music'
import request from '../utils/request'
import MusicList from '../components/MusicList.vue'

const route = useRoute()
const router = useRouter()
const musicStore = useMusicStore()

const activeTab = ref('music')
const musicResults = ref<any[]>([])
const playlistResults = ref<any[]>([])
const userResults = ref<any[]>([])

const keyword = ref('')

const search = async () => {
  keyword.value = route.query.keyword as string || ''
  
  if (!keyword.value) return

  try {
    const musicData: any = await request.get(`/musics/search?keyword=${keyword.value}&page=0&size=20`)
    musicResults.value = musicData?.content || []
  } catch (error) {
    console.error('Failed to search music')
  }

  try {
    const playlistData: any = await request.get(`/playlists/search?keyword=${keyword.value}&page=0&size=20`)
    playlistResults.value = playlistData?.content || []
  } catch (error) {
    console.error('Failed to search playlists')
  }

  try {
    const userData: any = await request.get(`/users/search?keyword=${keyword.value}&page=0&size=20`)
    userResults.value = userData?.content || []
  } catch (error) {
    console.error('Failed to search users')
  }
}

const playMusic = (music: any) => {
  musicStore.playMusic(music)
  musicStore.addToPlaylist(music)
}

const goToPlaylist = (id: number) => {
  router.push(`/playlist/${id}`)
}

const goToUser = (id: number) => {
  router.push(`/user/${id}`)
}

onMounted(() => {
  search()
})

watch(() => route.query.keyword, () => {
  search()
})
</script>

<style scoped>
.search-page {
  padding-bottom: 100px;
}

.search-page h1 {
  margin-bottom: 24px;
  font-size: 24px;
}

.playlist-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 20px;
}

.playlist-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.playlist-card:hover {
  transform: translateY(-4px);
}

.playlist-cover {
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 1;
  margin-bottom: 8px;
}

.playlist-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.playlist-name {
  font-size: 14px;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: white;
  border-radius: 8px;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 4px;
}

.user-bio {
  font-size: 14px;
  color: #999;
}
</style>
