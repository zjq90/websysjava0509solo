<template>
  <div class="page-container">
    <div class="page-header">
      <h1>分类浏览</h1>
      <p>按风格、语言、场景探索音乐</p>
    </div>

    <div class="chart-tabs" style="margin-bottom: 24px;">
      <button class="chart-tab" :class="{ active: activeType === 'genre' }" @click="activeType = 'genre'">🎵 风格</button>
      <button class="chart-tab" :class="{ active: activeType === 'language' }" @click="activeType = 'language'">🌍 语言</button>
      <button class="chart-tab" :class="{ active: activeType === 'scene' }" @click="activeType = 'scene'">🎬 场景</button>
    </div>

    <section style="margin-bottom: 32px;">
      <div class="category-grid">
        <div class="category-card" v-for="cat in currentCategories" :key="cat.id"
             :class="{ active: selectedCategory?.id === cat.id }" @click="selectCategory(cat)">
          <div class="icon">{{ cat.icon }}</div>
          <div class="name">{{ cat.name }}</div>
        </div>
      </div>
    </section>

    <section v-if="selectedCategory" style="margin-bottom: 32px;">
      <div class="section-header">
        <h2>{{ selectedCategory.icon }} {{ selectedCategory.name }}</h2>
      </div>
      <div class="music-grid" v-if="categoryMusic.length > 0">
        <div class="music-card" v-for="music in categoryMusic" :key="music.id" @click="playMusic(music)">
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
      <div class="empty-state" v-else>
        <div class="empty-icon">🎵</div>
        <p>该分类暂无音乐</p>
      </div>
    </section>

    <section style="margin-top: 40px;">
      <div class="section-header">
        <h2>🏷️ 标签</h2>
        <div class="tag-create-bar">
          <input class="input tag-input" v-model="newTagName" placeholder="输入自定义标签名..."
                 @keyup.enter="createTag" />
          <button class="btn btn-primary btn-sm" @click="createTag" :disabled="!newTagName.trim()">添加</button>
        </div>
      </div>

      <div class="tag-section" style="margin-bottom: 16px;">
        <div class="tag-section-title">我的标签</div>
        <div class="tag-list" v-if="userTags.length > 0">
          <span class="tag my-tag" v-for="tag in userTags" :key="tag.id"
                :class="{ active: selectedTag?.id === tag.id }" @click="selectTag(tag)">
            {{ tag.name }}
            <button class="tag-remove" @click.stop="deleteTag(tag)" title="删除标签">×</button>
          </span>
        </div>
        <div v-else class="tag-empty">登录后可创建自定义标签</div>
      </div>

      <div class="tag-section">
        <div class="tag-section-title">热门标签</div>
        <div class="tag-list">
          <span class="tag" v-for="tag in systemTags" :key="tag.id"
                :class="{ active: selectedTag?.id === tag.id }" @click="selectTag(tag)">
            {{ tag.name }}
          </span>
        </div>
      </div>

      <div class="music-grid" v-if="tagMusic.length > 0" style="margin-top: 24px;">
        <div class="music-card" v-for="music in tagMusic" :key="music.id" @click="playMusic(music)">
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
import { ref, computed, onMounted } from 'vue'
import { usePlayerStore, useUserStore } from '../store'
import api from '../api'

const playerStore = usePlayerStore()
const userStore = useUserStore()
const activeType = ref('genre')
const allCategories = ref([])
const selectedCategory = ref(null)
const categoryMusic = ref([])
const allTags = ref([])
const selectedTag = ref(null)
const tagMusic = ref([])
const newTagName = ref('')

const currentCategories = computed(() => {
  return allCategories.value.filter(c => c.type === activeType.value)
})

const userTags = computed(() => {
  if (!userStore.isLoggedIn) return []
  const uid = userStore.currentUser.id
  return allTags.value.filter(t => t.userId === uid)
})

const systemTags = computed(() => {
  if (!userStore.isLoggedIn) return allTags.value
  const uid = userStore.currentUser.id
  return allTags.value.filter(t => t.userId !== uid)
})

onMounted(async () => {
  const [catRes, tagRes] = await Promise.all([
    api.category.all(),
    api.tag.all()
  ])
  if (catRes.code === 200) allCategories.value = catRes.data
  if (tagRes.code === 200) allTags.value = tagRes.data
})

async function selectCategory(cat) {
  selectedCategory.value = cat
  const res = await api.category.music(cat.id)
  if (res.code === 200) categoryMusic.value = res.data
}

async function selectTag(tag) {
  if (selectedTag.value?.id === tag.id) {
    selectedTag.value = null
    tagMusic.value = []
    return
  }
  selectedTag.value = tag
  const res = await api.tag.music(tag.id)
  if (res.code === 200) {
    tagMusic.value = res.data || []
  }
}

async function createTag() {
  const name = newTagName.value.trim()
  if (!name) return
  if (!userStore.isLoggedIn) return

  const exists = allTags.value.some(t => t.name.toLowerCase() === name.toLowerCase())
  if (exists) return

  try {
    const res = await api.tag.create({ name, userId: userStore.currentUser.id })
    if (res.code === 200 && res.data) {
      const existing = allTags.value.some(t => t.id === res.data.id)
      if (!existing) {
        allTags.value = [...allTags.value, res.data]
      }
      newTagName.value = ''
    }
  } catch (e) {
    console.error('Create tag failed:', e)
  }
}

async function deleteTag(tag) {
  if (!userStore.isLoggedIn) return
  if (tag.userId !== userStore.currentUser.id) return

  try {
    await api.tag.delete(tag.id)
    allTags.value = allTags.value.filter(t => t.id !== tag.id)
    if (selectedTag.value?.id === tag.id) {
      selectedTag.value = null
      tagMusic.value = []
    }
  } catch (e) {
    console.error('Delete tag failed:', e)
  }
}

function playMusic(music) {
  playerStore.play(music)
}
</script>
