<template>
  <div class="home-page">
    <el-row :gutter="24">
      <el-col :span="18">
        <div class="section">
          <h2 class="section-title">热门音乐</h2>
          <MusicList :musics="popularMusic" @play="playMusic" />
        </div>
        
        <div class="section">
          <h2 class="section-title">推荐歌单</h2>
          <div class="playlist-grid">
            <div 
              v-for="playlist in playlists" 
              :key="playlist.id" 
              class="playlist-card"
              @click="goToPlaylist(playlist.id)"
            >
              <div class="playlist-cover">
                <img :src="playlist.coverUrl" :alt="playlist.name" />
                <div class="play-overlay">
                  <el-icon :size="48"><VideoPlay /></el-icon>
                </div>
              </div>
              <div class="playlist-info">
                <h3 class="playlist-name">{{ playlist.name }}</h3>
                <p class="playlist-meta">
                  <span>{{ playlist.playCount }} 次播放</span>
                </p>
              </div>
            </div>
          </div>
        </div>

        <div class="section">
          <h2 class="section-title">最新动态</h2>
          <div class="post-list">
            <div v-for="post in posts" :key="post.id" class="post-item">
              <div class="post-header">
                <el-avatar :src="post.user?.avatar || 'https://picsum.photos/100/100'" />
                <div class="post-user">
                  <div class="post-username">{{ post.user?.nickname }}</div>
                  <div class="post-time">{{ formatTime(post.createdAt) }}</div>
                </div>
              </div>
              <div class="post-content">{{ post.content }}</div>
              <div class="post-actions">
                <el-button type="text">
                  <el-icon><Star /></el-icon>
                  {{ post.likeCount }}
                </el-button>
                <el-button type="text">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ post.commentCount }}
                </el-button>
                <el-button type="text">
                  <el-icon><Share /></el-icon>
                  {{ post.shareCount }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      
      <el-col :span="6">
        <div class="sidebar">
          <div class="user-card">
            <el-avatar :size="64" :src="userStore.user?.avatar" />
            <div class="user-name">{{ userStore.user?.nickname }}</div>
            <div class="user-role" v-if="userStore.user?.role === 'ARTIST'">音乐人</div>
            <div class="user-role vip" v-else-if="userStore.user?.role === 'VIP_USER' || userStore.user?.isVip">VIP会员</div>
            <el-divider />
            <div class="user-stats">
              <div class="stat-item">
                <div class="stat-value">{{ userStats?.followerCount || 0 }}</div>
                <div class="stat-label">粉丝</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ userStats?.followingCount || 0 }}</div>
                <div class="stat-label">关注</div>
              </div>
            </div>
          </div>

          <div class="sidebar-section">
            <h3>我的歌单</h3>
            <el-button type="primary" style="width: 100%; margin-bottom: 12px;" @click="goToMyPlaylists">
              管理歌单
            </el-button>
          </div>

          <div class="sidebar-section">
            <h3>热门音乐人</h3>
            <div class="artist-list">
              <div v-for="artist in artists" :key="artist.id" class="artist-item">
                <el-avatar :src="artist.avatar" />
                <div class="artist-info">
                  <div class="artist-name">{{ artist.nickname }}</div>
                  <div class="artist-fans">{{ artist.followerCount }} 粉丝</div>
                </div>
                <el-button type="primary" link>关注</el-button>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useMusicStore } from '../stores/music'
import request from '../utils/request'
import dayjs from 'dayjs'

const router = useRouter()
const userStore = useUserStore()
const musicStore = useMusicStore()

const popularMusic = ref<any[]>([])
const playlists = ref<any[]>([])
const posts = ref<any[]>([])
const userStats = ref<any>({})
const artists = ref<any[]>([])

const loadData = async () => {
  try {
    popularMusic.value = await request.get('/musics/popular?limit=10')
  } catch (error) {
    console.error('Failed to load popular music')
  }

  try {
    const playlistData: any = await request.get('/playlists/popular?limit=6')
    playlists.value = playlistData || []
  } catch (error) {
    console.error('Failed to load playlists')
  }

  try {
    const postsData: any = await request.get('/posts?page=0&size=10')
    posts.value = postsData?.content || []
  } catch (error) {
    console.error('Failed to load posts')
  }

  try {
    userStats.value = await request.get(`/users/${userStore.user?.id}`)
  } catch (error) {
    console.error('Failed to load user stats')
  }

  try {
    const usersData: any = await request.get('/users/search?keyword=artist')
    artists.value = usersData?.content?.slice(0, 5) || []
  } catch (error) {
    console.error('Failed to load artists')
  }
}

const playMusic = (music: any) => {
  musicStore.playMusic(music)
  musicStore.addToPlaylist(music)
}

const goToPlaylist = (id: number) => {
  router.push(`/playlist/${id}`)
}

const goToMyPlaylists = () => {
  router.push('/my-playlists')
}

const formatTime = (time: string) => {
  return dayjs(time).fromNow()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.home-page {
  padding-bottom: 100px;
}

.section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
}

.section-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 20px;
  color: #333;
}

.playlist-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
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
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 1;
}

.playlist-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
  color: white;
}

.playlist-card:hover .play-overlay {
  opacity: 1;
}

.playlist-info {
  padding: 12px 0;
}

.playlist-name {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.playlist-meta {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-item {
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.post-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.post-user {
  flex: 1;
}

.post-username {
  font-weight: 500;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-content {
  margin-bottom: 12px;
  line-height: 1.6;
}

.post-actions {
  display: flex;
  gap: 16px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.user-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  text-align: center;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  margin: 12px 0 4px;
}

.user-role {
  font-size: 12px;
  color: #409eff;
  background: #ecf5ff;
  padding: 2px 8px;
  border-radius: 10px;
  display: inline-block;
}

.user-role.vip {
  color: #f59e0b;
  background: #fffbeb;
}

.user-stats {
  display: flex;
  justify-content: space-around;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #999;
}

.sidebar-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
}

.sidebar-section h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 16px;
}

.artist-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.artist-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.artist-info {
  flex: 1;
}

.artist-name {
  font-size: 14px;
  font-weight: 500;
}

.artist-fans {
  font-size: 12px;
  color: #999;
}
</style>
