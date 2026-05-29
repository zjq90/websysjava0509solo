<template>
  <div class="page-container">
    <div class="page-header">
      <h1>搜索</h1>
    </div>

    <div class="search-bar" style="max-width: 600px; margin-bottom: 24px;">
      <span class="search-icon">🔍</span>
      <input class="input" v-model="keyword" placeholder="搜索歌曲、歌手、专辑..." @keyup.enter="doSearch" />
    </div>

    <section v-if="!searchResults.length && !hasSearched" style="margin-bottom: 32px;">
      <div class="section-header">
        <h2>🔥 热门搜索</h2>
      </div>
      <div class="hot-keywords">
        <span class="hot-keyword" v-for="kw in hotKeywords" :key="kw" @click="keyword = kw; doSearch()">
          {{ kw }}
        </span>
      </div>

      <div v-if="searchHistory.length > 0" style="margin-top: 32px;">
        <div class="section-header">
          <h2>🕐 搜索历史</h2>
          <span class="more-link" @click="clearHistory">清除</span>
        </div>
        <div class="hot-keywords">
          <span class="hot-keyword" v-for="kw in searchHistory" :key="kw" @click="keyword = kw; doSearch()">
            {{ kw }}
          </span>
        </div>
      </div>
    </section>

    <section v-if="hasSearched">
      <div class="section-header">
        <h2>搜索结果：{{ searchedKeyword }}</h2>
        <span style="color: var(--text-muted); font-size: 13px;">共 {{ totalResults }} 条</span>
      </div>

      <div class="chart-list" v-if="searchResults.length > 0">
        <div class="chart-item" v-for="music in searchResults" :key="music.id" @click="playMusic(music)">
          <img class="item-cover" :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
          <div class="item-info">
            <div class="title" v-html="highlight(music.title)"></div>
            <div class="artist" v-html="highlight(music.artist)"></div>
          </div>
          <span class="item-plays">{{ music.album || '未知专辑' }}</span>
          <span class="item-plays">{{ formatCount(music.playCount) }} 播放</span>
        </div>
      </div>
      <div class="empty-state" v-else>
        <div class="empty-icon">🔍</div>
        <p>未找到相关结果，换个关键词试试吧</p>
      </div>

      <div style="text-align: center; margin-top: 24px;" v-if="searchResults.length < totalResults">
        <button class="btn btn-outline" @click="loadMore">加载更多</button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore, useUserStore } from '../store'
import api from '../api'

const route = useRoute()
const playerStore = usePlayerStore()
const userStore = useUserStore()

const keyword = ref('')
const searchedKeyword = ref('')
const searchResults = ref([])
const hotKeywords = ref([])
const searchHistory = ref([])
const hasSearched = ref(false)
const totalResults = ref(0)
const currentPage = ref(0)

onMounted(async () => {
  const hotRes = await api.search.hot()
  if (hotRes.code === 200) hotKeywords.value = hotRes.data

  if (userStore.isLoggedIn) {
    const histRes = await api.search.history(userStore.currentUser.id)
    if (histRes.code === 200) searchHistory.value = histRes.data
  }

  if (route.query.q) {
    keyword.value = route.query.q
    doSearch()
  }
})

watch(() => route.query.q, (newQ) => {
  if (newQ) {
    keyword.value = newQ
    doSearch()
  }
})

async function doSearch() {
  if (!keyword.value.trim()) return
  searchedKeyword.value = keyword.value.trim()
  currentPage.value = 0
  searchResults.value = []
  hasSearched.value = true
  await fetchResults()
}

async function fetchResults() {
  const userId = userStore.isLoggedIn ? userStore.currentUser.id : null
  const res = await api.search.search(searchedKeyword.value, userId, currentPage.value, 20)
  if (res.code === 200) {
    if (currentPage.value === 0) {
      searchResults.value = res.data.list || []
    } else {
      searchResults.value.push(...(res.data.list || []))
    }
    totalResults.value = res.data.total || 0
  }
}

async function loadMore() {
  currentPage.value++
  await fetchResults()
}

async function clearHistory() {
  if (userStore.isLoggedIn) {
    await api.search.clearHistory(userStore.currentUser.id)
    searchHistory.value = []
  }
}

function playMusic(music) {
  playerStore.play(music)
}

function highlight(text) {
  if (!searchedKeyword.value || !text) return text
  const regex = new RegExp(`(${searchedKeyword.value})`, 'gi')
  return text.replace(regex, '<span style="color: var(--primary-light); font-weight: 600;">$1</span>')
}

function formatCount(count) {
  if (!count) return '0'
  if (count >= 10000) return (count / 10000).toFixed(1) + '万'
  return count.toString()
}
</script>
