<template>
  <div class="user-profile" v-if="userProfile">
    <div class="profile-header">
      <el-avatar :size="120" :src="userProfile.avatar" />
      <div class="profile-info">
        <h1 class="profile-name">
          {{ userProfile.nickname }}
          <el-tag v-if="userProfile.role === 'ARTIST'" type="info">音乐人</el-tag>
          <el-tag v-else-if="userProfile.isVip" type="warning">VIP</el-tag>
        </h1>
        <p class="profile-bio">{{ userProfile.bio || '这个人很懒，什么都没写' }}</p>
        <div class="profile-stats">
          <div class="stat">
            <div class="stat-value">{{ userProfile.followerCount }}</div>
            <div class="stat-label">粉丝</div>
          </div>
          <div class="stat">
            <div class="stat-value">{{ userProfile.followingCount }}</div>
            <div class="stat-label">关注</div>
          </div>
        </div>
        <div class="profile-actions" v-if="!isOwnProfile">
          <el-button :type="userProfile.isFollowing ? '' : 'primary'" @click="toggleFollow">
            {{ userProfile.isFollowing ? '已关注' : '关注' }}
          </el-button>
          <el-button>私信</el-button>
        </div>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="profile-tabs">
      <el-tab-pane label="音乐" name="music">
        <div v-if="userMusic.length > 0" class="music-section">
          <MusicList :musics="userMusic" @play="playMusic" />
        </div>
        <el-empty v-else description="暂无音乐" />
      </el-tab-pane>

      <el-tab-pane label="歌单" name="playlist">
        <div v-if="userPlaylists.length > 0" class="playlist-grid">
          <div 
            v-for="playlist in userPlaylists" 
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
        <el-empty v-else description="暂无歌单" />
      </el-tab-pane>

      <el-tab-pane label="动态" name="posts">
        <div v-if="userPosts.length > 0" class="post-list">
          <div v-for="post in userPosts" :key="post.id" class="post-item">
            <div class="post-content">{{ post.content }}</div>
            <div class="post-time">{{ formatTime(post.createdAt) }}</div>
          </div>
        </div>
        <el-empty v-else description="暂无动态" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useMusicStore } from '../stores/music'
import request from '../utils/request'
import dayjs from 'dayjs'
import MusicList from '../components/MusicList.vue'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const musicStore = useMusicStore()

const userProfile = ref<any>(null)
const activeTab = ref('music')
const userMusic = ref<any[]>([])
const userPlaylists = ref<any[]>([])
const userPosts = ref<any[]>([])

const userId = computed(() => Number(route.params.id))
const isOwnProfile = computed(() => userStore.user?.id === userId.value)

const loadUserProfile = async () => {
  try {
    userProfile.value = await request.get(`/users/${userId.value}`)
  } catch (error) {
    console.error('Failed to load user profile')
  }
}

const loadUserMusic = async () => {
  try {
    const data: any = await request.get(`/musics/artist/${userId.value}?page=0&size=20`)
    userMusic.value = data?.content || []
  } catch (error) {
    console.error('Failed to load user music')
  }
}

const loadUserPlaylists = async () => {
  try {
    const data: any = await request.get(`/playlists/user/${userId.value}?page=0&size=20`)
    userPlaylists.value = data?.content || []
  } catch (error) {
    console.error('Failed to load user playlists')
  }
}

const loadUserPosts = async () => {
  try {
    const data: any = await request.get(`/posts/user/${userId.value}?page=0&size=20`)
    userPosts.value = data?.content || []
  } catch (error) {
    console.error('Failed to load user posts')
  }
}

const toggleFollow = async () => {
  try {
    if (userProfile.value.isFollowing) {
      await request.post(`/users/${userId.value}/unfollow`)
      userProfile.value.isFollowing = false
      userProfile.value.followerCount--
    } else {
      await request.post(`/users/${userId.value}/follow`)
      userProfile.value.isFollowing = true
      userProfile.value.followerCount++
    }
    ElMessage.success(userProfile.value.isFollowing ? '关注成功' : '已取消关注')
  } catch (error) {
    console.error('Failed to toggle follow')
  }
}

const playMusic = (music: any) => {
  musicStore.playMusic(music)
  musicStore.addToPlaylist(music)
}

const goToPlaylist = (id: number) => {
  router.push(`/playlist/${id}`)
}

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  loadUserProfile()
  loadUserMusic()
  loadUserPlaylists()
  loadUserPosts()
})
</script>

<style scoped>
.user-profile {
  padding-bottom: 100px;
}

.profile-header {
  display: flex;
  gap: 32px;
  background: white;
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
}

.profile-info {
  flex: 1;
}

.profile-name {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 12px;
}

.profile-bio {
  color: #666;
  margin: 0 0 20px;
}

.profile-stats {
  display: flex;
  gap: 32px;
  margin-bottom: 20px;
}

.stat {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.profile-actions {
  display: flex;
  gap: 12px;
}

.profile-tabs {
  background: white;
  border-radius: 12px;
  padding: 24px;
}

.playlist-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
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

.post-content {
  margin-bottom: 8px;
}

.post-time {
  font-size: 12px;
  color: #999;
}
</style>
