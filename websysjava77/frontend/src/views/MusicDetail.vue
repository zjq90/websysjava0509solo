<template>
  <div class="page-container">
    <div v-if="music" class="music-detail">
      <div class="detail-cover">
        <img :src="music.coverPath || '/uploads/covers/default.jpg'" alt="" />
      </div>
      <div class="detail-info">
        <h1>{{ music.title }}</h1>
        <div class="meta">
          <span>{{ music.artist }}</span>
          <span v-if="music.album"> · {{ music.album }}</span>
          <span> · {{ music.format?.toUpperCase() }}</span>
          <span v-if="music.duration"> · {{ formatDuration(music.duration) }}</span>
        </div>

        <div style="display: flex; gap: 16px; font-size: 13px; color: var(--text-muted); margin-bottom: 16px;">
          <span>▶ {{ formatCount(music.playCount) }} 播放</span>
          <span>♡ {{ formatCount(music.likeCount) }} 喜欢</span>
          <span>⭐ {{ formatCount(music.collectCount) }} 收藏</span>
        </div>

        <div class="actions">
          <button class="btn btn-primary" @click="playMusic">▶ 播放</button>
          <button class="btn btn-outline" :style="isLiked ? {color:'var(--accent-red)', borderColor:'var(--accent-red)'} : {}" @click="toggleLike">
            {{ isLiked ? '❤️ 已喜欢' : '🤍 喜欢' }}
          </button>
          <button class="btn btn-outline" :style="isCollected ? {color:'var(--accent-yellow)', borderColor:'var(--accent-yellow)'} : {}" @click="toggleCollect">
            {{ isCollected ? '⭐ 已收藏' : '☆ 收藏' }}
          </button>
        </div>

        <div v-if="categories.length > 0" style="margin-bottom: 16px;">
          <span style="font-size: 13px; color: var(--text-muted); margin-right: 8px;">分类：</span>
          <span class="tag" v-for="cat in categories" :key="cat.id">{{ cat.icon }} {{ cat.name }}</span>
        </div>

        <div style="margin-bottom: 16px;">
          <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 8px;">
            <span style="font-size: 13px; color: var(--text-muted);">标签：</span>
            <button v-if="userStore.isLoggedIn" class="btn btn-sm btn-outline" @click="showTagEditor = !showTagEditor">
              {{ showTagEditor ? '收起' : '管理标签' }}
            </button>
          </div>
          <div class="tag-list" v-if="tags.length > 0">
            <span class="tag" v-for="tag in tags" :key="tag.id">
              #{{ tag.name }}
              <button v-if="showTagEditor && userStore.isLoggedIn" class="tag-remove"
                      @click="removeTagFromMusic(tag)" title="移除标签">×</button>
            </span>
          </div>
          <div v-else style="font-size: 13px; color: var(--text-muted);">暂无标签</div>

          <div v-if="showTagEditor && userStore.isLoggedIn" class="tag-editor">
            <div class="tag-editor-row">
              <input class="input tag-input" v-model="newTagName" placeholder="输入新标签名..."
                     @keyup.enter="addNewTagToMusic" />
              <button class="btn btn-primary btn-sm" @click="addNewTagToMusic"
                      :disabled="!newTagName.trim()">创建并添加</button>
            </div>
            <div v-if="availableTags.length > 0" style="margin-top: 10px;">
              <div style="font-size: 12px; color: var(--text-muted); margin-bottom: 6px;">点击已有标签添加：</div>
              <div class="tag-list">
                <span class="tag addable" v-for="tag in availableTags" :key="tag.id" @click="addExistingTagToMusic(tag)">
                  + {{ tag.name }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div v-if="copyrightInfo" style="margin-bottom: 16px; padding: 12px; background: var(--bg-card); border-radius: var(--radius-sm);">
          <span style="font-size: 13px; color: var(--text-muted);">版权：</span>
          <span class="status-badge" :class="copyrightInfo.isOriginal ? 'active' : 'reported'">
            {{ copyrightInfo.type }}
          </span>
          <span style="font-size: 12px; color: var(--text-muted); margin-left: 8px;">{{ copyrightInfo.declaration }}</span>
        </div>

        <div v-if="music.lyrics">
          <h3 style="margin-bottom: 12px; font-size: 16px;">歌词</h3>
          <div class="lyrics-box">{{ music.lyrics }}</div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <div class="empty-icon">🎵</div>
      <p>音乐不存在或加载中</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { usePlayerStore, useUserStore } from '../store'
import api from '../api'

const route = useRoute()
const playerStore = usePlayerStore()
const userStore = useUserStore()

const music = ref(null)
const categories = ref([])
const tags = ref([])
const allTags = ref([])
const copyrightInfo = ref(null)
const isLiked = ref(false)
const isCollected = ref(false)
const showTagEditor = ref(false)
const newTagName = ref('')

const availableTags = computed(() => {
  const currentTagIds = new Set(tags.value.map(t => t.id))
  return allTags.value.filter(t => !currentTagIds.has(t.id))
})

onMounted(async () => {
  const id = route.params.id
  if (!id) return

  const [musicRes, catRes, tagRes, copyRes, allTagRes] = await Promise.all([
    api.music.get(id),
    api.music.getCategories(id),
    api.music.getTags(id),
    api.copyright.getByMusic(id).catch(() => ({ code: 404 })),
    api.tag.all()
  ])

  if (musicRes.code === 200) music.value = musicRes.data
  if (catRes.code === 200) categories.value = catRes.data
  if (tagRes.code === 200) tags.value = tagRes.data
  if (copyRes.code === 200) copyrightInfo.value = copyRes.data
  if (allTagRes.code === 200) allTags.value = allTagRes.data

  if (userStore.isLoggedIn && music.value) {
    try {
      const likeRes = await api.recommend.checkBehavior(userStore.currentUser.id, id, 'like')
      isLiked.value = likeRes.data === true
      const collectRes = await api.recommend.checkBehavior(userStore.currentUser.id, id, 'collect')
      isCollected.value = collectRes.data === true
    } catch { /* ignore */ }
  }
})

function playMusic() {
  if (music.value) playerStore.play(music.value)
}

async function toggleLike() {
  if (!userStore.isLoggedIn || !music.value) return
  const musicId = music.value.id
  if (isLiked.value) {
    await api.recommend.removeBehavior({ userId: userStore.currentUser.id, musicId, type: 'like' })
  } else {
    await api.recommend.recordBehavior({ userId: userStore.currentUser.id, musicId, type: 'like' })
  }
  isLiked.value = !isLiked.value
}

async function toggleCollect() {
  if (!userStore.isLoggedIn || !music.value) return
  const musicId = music.value.id
  if (isCollected.value) {
    await api.recommend.removeBehavior({ userId: userStore.currentUser.id, musicId, type: 'collect' })
  } else {
    await api.recommend.recordBehavior({ userId: userStore.currentUser.id, musicId, type: 'collect' })
  }
  isCollected.value = !isCollected.value
}

async function addNewTagToMusic() {
  const name = newTagName.value.trim()
  if (!name || !userStore.isLoggedIn || !music.value) return

  try {
    const createRes = await api.tag.create({ name, userId: userStore.currentUser.id })
    if (createRes.code === 200 && createRes.data) {
      const tag = createRes.data
      const alreadyOnMusic = tags.value.some(t => t.id === tag.id)
      if (!alreadyOnMusic) {
        const currentTagIds = tags.value.map(t => t.id)
        currentTagIds.push(tag.id)
        const assignRes = await api.music.assignTags(music.value.id, currentTagIds)
        if (assignRes.code === 200) {
          tags.value = [...tags.value, tag]
        }
      }
      const inAll = allTags.value.some(t => t.id === tag.id)
      if (!inAll) {
        allTags.value = [...allTags.value, tag]
      }
      newTagName.value = ''
    }
  } catch (e) {
    console.error('Add new tag failed:', e)
  }
}

async function addExistingTagToMusic(tag) {
  if (!userStore.isLoggedIn || !music.value) return

  try {
    const currentTagIds = tags.value.map(t => t.id)
    if (currentTagIds.includes(tag.id)) return
    currentTagIds.push(tag.id)
    const res = await api.music.assignTags(music.value.id, currentTagIds)
    if (res.code === 200) {
      tags.value = [...tags.value, tag]
    }
  } catch (e) {
    console.error('Add existing tag failed:', e)
  }
}

async function removeTagFromMusic(tag) {
  if (!userStore.isLoggedIn || !music.value) return

  try {
    const remainingTagIds = tags.value.filter(t => t.id !== tag.id).map(t => t.id)
    const res = await api.music.assignTags(music.value.id, remainingTagIds)
    if (res.code === 200) {
      tags.value = tags.value.filter(t => t.id !== tag.id)
    }
  } catch (e) {
    console.error('Remove tag failed:', e)
  }
}

function formatDuration(seconds) {
  if (!seconds) return '0:00'
  const min = Math.floor(seconds / 60)
  const sec = Math.floor(seconds % 60)
  return `${min}:${sec.toString().padStart(2, '0')}`
}

function formatCount(count) {
  if (!count) return '0'
  if (count >= 10000) return (count / 10000).toFixed(1) + '万'
  return count.toString()
}
</script>
