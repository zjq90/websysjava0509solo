<template>
  <div class="page-container">
    <div class="page-header">
      <h1>发现音乐</h1>
      <p>探索各类榜单和推荐</p>
    </div>

    <div class="chart-tabs">
      <button class="chart-tab" :class="{ active: activeTab === 'hot' }" @click="activeTab = 'hot'">🔥 热歌榜</button>
      <button class="chart-tab" :class="{ active: activeTab === 'new' }" @click="activeTab = 'new'">🆕 新歌榜</button>
      <button class="chart-tab" :class="{ active: activeTab === 'rising' }" @click="activeTab = 'rising'">🚀 飙升榜</button>
    </div>

    <div class="chart-list">
      <div class="chart-item" v-for="(music, idx) in currentChart" :key="music.id" @click="playMusic(music)">
        <span class="rank" :class="{ 'top-1': idx === 0, 'top-2': idx === 1, 'top-3': idx === 2 }">{{ idx + 1 }}</span>
        <img class="item-cover" :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
        <div class="item-info">
          <div class="title">{{ music.title }}</div>
          <div class="artist">{{ music.artist }} · {{ music.album || '未知专辑' }}</div>
        </div>
        <span class="item-plays">{{ formatCount(music.playCount) }} 播放</span>
        <span class="item-plays">♡ {{ formatCount(music.likeCount) }}</span>
      </div>
    </div>

    <section style="margin-top: 48px;">
      <div class="section-header">
        <h2>🎯 场景歌单</h2>
      </div>
      <div class="playlist-grid">
        <div class="playlist-card" v-for="pl in scenePlaylists" :key="pl.id" @click="viewPlaylist(pl)">
          <div class="pl-cover">{{ getSceneIcon(pl.name) }}</div>
          <div class="pl-info">
            <h3>{{ pl.name }}</h3>
            <p>{{ pl.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <section v-if="playlistMusic.length > 0" style="margin-top: 32px;">
      <div class="section-header">
        <h2>📋 {{ selectedPlaylist?.name }}</h2>
      </div>
      <div class="chart-list">
        <div class="chart-item" v-for="(music, idx) in playlistMusic" :key="music.id" @click="playMusic(music)">
          <span class="rank">{{ idx + 1 }}</span>
          <img class="item-cover" :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
          <div class="item-info">
            <div class="title">{{ music.title }}</div>
            <div class="artist">{{ music.artist }}</div>
          </div>
          <span class="item-plays">{{ formatCount(music.playCount) }} 播放</span>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { usePlayerStore } from '../store'
import api from '../api'

const playerStore = usePlayerStore()
const activeTab = ref('hot')
const hotChart = ref([])
const newChart = ref([])
const risingChart = ref([])
const scenePlaylists = ref([])
const selectedPlaylist = ref(null)
const playlistMusic = ref([])

const currentChart = computed(() => {
  if (activeTab.value === 'hot') return hotChart.value
  if (activeTab.value === 'new') return newChart.value
  return risingChart.value
})

onMounted(async () => {
  const [hotRes, newRes, risingRes, sceneRes] = await Promise.all([
    api.recommend.hotChart(),
    api.recommend.newChart(),
    api.recommend.risingChart(),
    api.recommend.scenePlaylists()
  ])
  if (hotRes.code === 200) hotChart.value = hotRes.data
  if (newRes.code === 200) newChart.value = newRes.data
  if (risingRes.code === 200) risingChart.value = risingRes.data
  if (sceneRes.code === 200) scenePlaylists.value = sceneRes.data
})

async function viewPlaylist(pl) {
  selectedPlaylist.value = pl
  const res = await api.recommend.playlistMusic(pl.id)
  if (res.code === 200) playlistMusic.value = res.data
}

function playMusic(music) {
  playerStore.play(music)
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
