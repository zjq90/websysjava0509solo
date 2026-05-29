<template>
  <div class="app-layout">
    <aside class="sidebar">
      <div class="sidebar-logo">
        <div class="logo-icon">🎵</div>
        <span class="logo-text">SoundWave</span>
      </div>
      <nav class="sidebar-nav">
        <div class="nav-section">
          <div class="nav-section-title">发现</div>
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
            <span class="nav-icon">🏠</span>首页
          </router-link>
          <router-link to="/discover" class="nav-item" :class="{ active: $route.path === '/discover' }">
            <span class="nav-icon">🔍</span>发现
          </router-link>
        </div>
        <div class="nav-section">
          <div class="nav-section-title">音乐库</div>
          <router-link to="/category" class="nav-item" :class="{ active: $route.path === '/category' }">
            <span class="nav-icon">📂</span>分类浏览
          </router-link>
          <router-link to="/search" class="nav-item" :class="{ active: $route.path === '/search' }">
            <span class="nav-icon">🔎</span>搜索
          </router-link>
        </div>
        <div class="nav-section">
          <div class="nav-section-title">创作</div>
          <router-link to="/upload" class="nav-item" :class="{ active: $route.path === '/upload' }">
            <span class="nav-icon">⬆️</span>上传音乐
          </router-link>
          <router-link to="/copyright" class="nav-item" :class="{ active: $route.path === '/copyright' }">
            <span class="nav-icon">©️</span>版权管理
          </router-link>
        </div>
      </nav>
      <div style="padding: 16px; border-top: 1px solid var(--border);">
        <div v-if="userStore.isLoggedIn" class="nav-item" style="cursor:default;">
          <span class="nav-icon">👤</span>{{ userStore.currentUser?.nickname || userStore.currentUser?.username }}
        </div>
        <router-link v-else to="/login" class="nav-item">
          <span class="nav-icon">🔑</span>登录
        </router-link>
        <div v-if="userStore.isLoggedIn" class="nav-item" @click="userStore.logout()" style="margin-top:4px;">
          <span class="nav-icon">🚪</span>退出
        </div>
      </div>
    </aside>

    <main class="main-content">
      <router-view />
    </main>

    <div class="player-bar" v-if="playerStore.currentMusic">
      <div class="player-cover">
        <img :src="playerStore.currentMusic.coverPath || '/uploads/covers/default.jpg'" alt="" />
      </div>
      <div class="player-info">
        <div class="title">{{ playerStore.currentMusic.title }}</div>
        <div class="artist">{{ playerStore.currentMusic.artist }}</div>
      </div>
      <div class="player-controls">
        <button class="ctrl-btn" @click="playerStore.playPrev()">⏮</button>
        <button class="ctrl-btn play-pause" @click="playerStore.togglePlay()">
          {{ playerStore.isPlaying ? '⏸' : '▶' }}
        </button>
        <button class="ctrl-btn" @click="playerStore.playNext()">⏭</button>
      </div>
      <div class="player-actions">
        <button class="action-btn" :class="{ liked: isLiked }" @click="toggleLike">
          {{ isLiked ? '❤️' : '🤍' }}
        </button>
        <button class="action-btn" :class="{ collected: isCollected }" @click="toggleCollect">
          {{ isCollected ? '⭐' : '☆' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useUserStore, usePlayerStore } from './store'
import api from './api'

const userStore = useUserStore()
const playerStore = usePlayerStore()

const isLiked = ref(false)
const isCollected = ref(false)

onMounted(() => {
  userStore.init()
})

watch(() => playerStore.currentMusic, async (music) => {
  if (music && music.id && userStore.isLoggedIn) {
    try {
      const likeRes = await api.recommend.checkBehavior(userStore.currentUser.id, music.id, 'like')
      isLiked.value = likeRes.data === true
      const collectRes = await api.recommend.checkBehavior(userStore.currentUser.id, music.id, 'collect')
      isCollected.value = collectRes.data === true
    } catch (e) { /* ignore */ }
  }
}, { immediate: true })

async function toggleLike() {
  if (!userStore.isLoggedIn) return
  const musicId = playerStore.currentMusic.id
  if (isLiked.value) {
    await api.recommend.removeBehavior({ userId: userStore.currentUser.id, musicId, type: 'like' })
  } else {
    await api.recommend.recordBehavior({ userId: userStore.currentUser.id, musicId, type: 'like' })
  }
  isLiked.value = !isLiked.value
}

async function toggleCollect() {
  if (!userStore.isLoggedIn) return
  const musicId = playerStore.currentMusic.id
  if (isCollected.value) {
    await api.recommend.removeBehavior({ userId: userStore.currentUser.id, musicId, type: 'collect' })
  } else {
    await api.recommend.recordBehavior({ userId: userStore.currentUser.id, musicId, type: 'collect' })
  }
  isCollected.value = !isCollected.value
}
</script>
