<template>
  <div class="page-container">
    <div class="page-header">
      <div class="search-bar" style="max-width:500px;">
        <span class="search-icon">🔍</span>
        <input class="input" placeholder="搜索歌曲、歌手、专辑..." v-model="searchKeyword" @keyup.enter="doSearch" />
      </div>
    </div>

    <section style="margin-bottom: 40px;">
      <div class="section-header">
        <h2>🔥 热歌榜</h2>
        <router-link to="/discover" class="more-link">查看更多 →</router-link>
      </div>
      <div class="chart-list">
        <div class="chart-item" v-for="(music, idx) in hotChart" :key="music.id" @click="playMusic(music)">
          <span class="rank" :class="{ 'top-1': idx === 0, 'top-2': idx === 1, 'top-3': idx === 2 }">{{ idx + 1 }}</span>
          <img class="item-cover" :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
          <div class="item-info">
            <div class="title">{{ music.title }}</div>
            <div class="artist">{{ music.artist }}</div>
          </div>
          <span class="item-plays">{{ formatCount(music.playCount) }} 播放</span>
        </div>
      </div>
    </section>

    <section style="margin-bottom: 40px;">
      <div class="section-header">
        <h2>🆕 新歌速递</h2>
      </div>
      <div class="music-grid">
        <div class="music-card" v-for="music in newMusic" :key="music.id" @click="playMusic(music)">
          <div class="cover">
            <img :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
            <div class="play-overlay">
              <button class="play-btn" @click.stop="playMusic(music)">▶</button>
            </div>
          </div>
          <div class="info">
            <div class="title">{{ music.title }}</div>
            <div class="artist">{{ music.artist }}</div>
          </div>
        </div>
      </div>
    </section>

    <section style="margin-bottom: 40px;">
      <div class="section-header">
        <h2>🎯 场景歌单</h2>
      </div>
      <div class="playlist-grid">
        <div class="playlist-card" v-for="pl in scenePlaylists" :key="pl.id" @click="openPlaylist(pl)">
          <div class="pl-cover">{{ getSceneIcon(pl.name) }}</div>
          <div class="pl-info">
            <h3>{{ pl.name }}</h3>
            <p>{{ pl.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <section v-if="userStore.isLoggedIn" style="margin-bottom: 40px;">
      <div class="section-header">
        <h2>✨ 为你推荐</h2>
      </div>
      <div class="music-grid">
        <div class="music-card" v-for="music in recommendations" :key="music.id" @click="playMusic(music)">
          <div class="cover">
            <img :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
            <div class="play-overlay">
              <button class="play-btn" @click.stop="playMusic(music)">▶</button>
            </div>
          </div>
          <div class="info">
            <div class="title">{{ music.title }}</div>
            <div class="artist">{{ music.artist }}</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePlayerStore, useUserStore } from '../store'
import api from '../api'

const router = useRouter()
const playerStore = usePlayerStore()
const userStore = useUserStore()

const searchKeyword = ref('')
const hotChart = ref([])
const newMusic = ref([])
const scenePlaylists = ref([])
const recommendations = ref([])

onMounted(async () => {
  const [hotRes, newRes, sceneRes] = await Promise.all([
    api.recommend.hotChart(),
    api.recommend.newChart(),
    api.recommend.scenePlaylists()
  ])
  if (hotRes.code === 200) hotChart.value = hotRes.data
  if (newRes.code === 200) newMusic.value = newRes.data
  if (sceneRes.code === 200) scenePlaylists.value = sceneRes.data

  if (userStore.isLoggedIn) {
    const recRes = await api.recommend.personalized(userStore.currentUser.id, 12)
    if (recRes.code === 200) recommendations.value = recRes.data
  }
})

function playMusic(music) {
  playerStore.play(music)
}

function doSearch() {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/search', query: { q: searchKeyword.value.trim() } })
  }
}

function openPlaylist(pl) {
  playerStore.play({ id: null, title: pl.name, artist: '歌单', coverPath: pl.coverPath })
}

function getSceneIcon(name) {
  const icons = { '运动': '🏃', '睡眠': '😴', '工作': '💻', '学习': '📚', '派对': '🎉', '驾车': '🚗', '放松': '☕', '冥想': '🧘' }
  for (const [key, icon] of Object.entries(icons)) {
    if (name.includes(key)) return icon
  }
  return '🎵'
}

function formatCount(count) {
  if (!count) return '0'
  if (count >= 10000) return (count / 10000).toFixed(1) + '万'
  return count.toString()
}
</script>
