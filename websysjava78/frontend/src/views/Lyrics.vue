<template>
  <div class="lyrics-page">
    <div class="lyrics-container">
      <div class="music-info">
        <div class="album-cover" :class="{ 'animate-rotate': isPlaying, 'animate-paused': !isPlaying }">
          <img :src="music?.coverUrl || defaultCover" :alt="music?.title" />
        </div>
        <div class="info">
          <h1 class="title">{{ music?.title || '加载中...' }}</h1>
          <p class="artist">{{ music?.artist || '' }}</p>
          <p class="album" v-if="music?.album">{{ music.album }}</p>
          <div class="actions">
            <el-button type="primary" circle size="large" @click="togglePlay">
              <el-icon v-if="isPlaying" :size="24"><VideoPause /></el-icon>
              <el-icon v-else :size="24"><VideoPlay /></el-icon>
            </el-button>
            <el-button circle size="large" @click="playerStore.prev()">
              <el-icon><DArrowLeft /></el-icon>
            </el-button>
            <el-button circle size="large" @click="playerStore.next()">
              <el-icon><DArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
      </div>

      <div class="lyrics-display">
        <LyricsDisplay :lyrics="lyrics" />
      </div>
    </div>

    <el-button class="back-btn" circle @click="$router.back()">
      <el-icon><ArrowLeft /></el-icon>
    </el-button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { getMusicDetail, getLyrics } from '@/api/music'
import LyricsDisplay from '@/components/LyricsDisplay.vue'

const route = useRoute()
const playerStore = usePlayerStore()

const music = ref(null)
const lyrics = ref(null)
const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200"%3E%3Crect fill="%23667eea" width="200" height="200"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="white" font-size="60"%3E♪%3C/text%3E%3C/svg%3E'

const isPlaying = computed(() => playerStore.isPlaying)

const fetchData = async () => {
  const musicId = route.params.id
  try {
    music.value = await getMusicDetail(musicId)
    lyrics.value = await getLyrics(musicId)

    const currentMusic = playerStore.currentMusic
    if (!currentMusic || currentMusic.id !== Number(musicId)) {
      playerStore.playMusic(music.value)
    }
  } catch (e) {
    console.error('加载失败', e)
  }
}

const togglePlay = () => {
  if (playerStore.currentMusic?.id !== music.value?.id) {
    playerStore.playMusic(music.value)
  } else {
    playerStore.togglePlay()
  }
}

watch(() => route.params.id, fetchData)

onMounted(fetchData)
</script>

<style scoped>
.lyrics-page {
  position: relative;
  min-height: calc(100vh - 164px);
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  border-radius: 16px;
  overflow: hidden;
}

.lyrics-container {
  display: flex;
  height: calc(100vh - 164px);
}

.music-info {
  width: 400px;
  padding: 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  background: rgba(0, 0, 0, 0.3);
}

.album-cover {
  width: 250px;
  height: 250px;
  border-radius: 50%;
  overflow: hidden;
  border: 8px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info {
  text-align: center;
  color: white;
}

.title {
  font-size: 28px;
  margin: 0 0 10px;
  font-weight: bold;
}

.artist {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0 0 5px;
}

.album {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  margin: 0 0 20px;
}

.actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.lyrics-display {
  flex: 1;
  overflow: hidden;
}

.back-btn {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 10;
}
</style>
