<template>
  <div class="player-container">
    <div class="player" v-if="playerStore.currentMusic">
      <div class="player-left">
        <div class="album-cover" :class="{ 'animate-rotate': playerStore.isPlaying, 'animate-paused': !playerStore.isPlaying }">
          <img :src="playerStore.currentMusic.coverUrl || defaultCover" alt="封面" />
        </div>
        <div class="music-info">
          <div class="title text-ellipsis">{{ playerStore.currentMusic.title }}</div>
          <div class="artist text-ellipsis">{{ playerStore.currentMusic.artist }}</div>
        </div>
        <el-button type="text" size="small" @click="$router.push(`/lyrics/${playerStore.currentMusic.id}`)">
          <el-icon><Document /></el-icon> 歌词
        </el-button>
      </div>

      <div class="player-center">
        <div class="controls">
          <el-button-circle size="small" @click="playerStore.togglePlayMode()">
            <el-icon v-if="playerStore.playMode === 'sequence'"><Sort /></el-icon>
            <el-icon v-else-if="playerStore.playMode === 'loop'"><RefreshRight /></el-icon>
            <el-icon v-else><Shuffle /></el-icon>
          </el-button-circle>
          <el-button-circle size="default" @click="playerStore.prev()">
            <el-icon :size="20"><DArrowLeft /></el-icon>
          </el-button-circle>
          <el-button-circle size="large" type="primary" @click="playerStore.togglePlay()">
            <el-icon v-if="playerStore.isPlaying" :size="24"><VideoPause /></el-icon>
            <el-icon v-else :size="24"><VideoPlay /></el-icon>
          </el-button-circle>
          <el-button-circle size="default" @click="playerStore.next()">
            <el-icon :size="20"><DArrowRight /></el-icon>
          </el-button-circle>
          <el-button-circle size="small" @click="showPlaylist = !showPlaylist">
            <el-icon><List /></el-icon>
          </el-button-circle>
        </div>

        <div class="progress-bar">
          <span class="time">{{ formatTime(playerStore.currentTime) }}</span>
          <el-slider
            v-model="currentTimeVal"
            :max="playerStore.duration || 100"
            :format-tooltip="formatTime"
            @change="handleSeek"
            @input="isDragging = true"
            class="progress-slider"
          />
          <span class="time">{{ formatTime(playerStore.duration) }}</span>
        </div>
      </div>

      <div class="player-right">
        <div class="quality-selector">
          <el-dropdown @command="handleQualityChange">
            <span class="quality-label">
              {{ qualityLabel }} <el-icon><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="128" :disabled="!playerStore.currentMusic.filePath128">
                  普通音质 (128kbps)
                </el-dropdown-item>
                <el-dropdown-item command="320" :disabled="!playerStore.currentMusic.filePath320">
                  高清音质 (320kbps)
                </el-dropdown-item>
                <el-dropdown-item
                  command="flac"
                  :disabled="!playerStore.currentMusic.filePathFlac || !userStore.isPremium"
                >
                  无损音质 (FLAC)
                  <el-tag v-if="!userStore.isPremium" type="warning" size="small">VIP</el-tag>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <div class="volume-control">
          <el-button circle size="small" @click="playerStore.toggleMute()">
            <el-icon v-if="playerStore.isMuted || playerStore.volume === 0"><Mute /></el-icon>
            <el-icon v-else-if="playerStore.volume < 0.5"><Microphone /></el-icon>
            <el-icon v-else><Flag /></el-icon>
          </el-button>
          <el-slider
            v-model="volumeVal"
            :min="0"
            :max="1"
            :step="0.01"
            @change="handleVolumeChange"
            class="volume-slider"
          />
        </div>
      </div>
    </div>

    <div class="player-empty" v-else>
      <el-icon :size="32" color="#999"><Music /></el-icon>
      <span class="empty-text">暂无播放音乐，从音乐库选择一首吧</span>
    </div>

    <el-drawer
      v-model="showPlaylist"
      title="播放列表"
      direction="bottom"
      size="400px"
    >
      <div class="playlist-drawer">
        <div class="playlist-header">
          <span>共 {{ playerStore.playlist.length }} 首</span>
          <el-button type="danger" size="small" plain @click="playerStore.clearPlaylist()">
            清空列表
          </el-button>
        </div>
        <div class="playlist-content">
          <div
            v-for="(item, index) in playerStore.playlist"
            :key="item.id"
            class="playlist-item"
            :class="{ active: index === playerStore.currentIndex }"
            @click="playerStore.playMusic(item)"
          >
            <div class="item-index">{{ index + 1 }}</div>
            <div class="item-info">
              <div class="item-title">{{ item.title }}</div>
              <div class="item-artist">{{ item.artist }}</div>
            </div>
            <div class="item-actions">
              <el-button type="text" size="small" @click.stop="playerStore.removeFromPlaylist(index)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          <div v-if="playerStore.playlist.length === 0" class="empty-playlist">
            播放列表为空
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'

const playerStore = usePlayerStore()
const userStore = useUserStore()

const showPlaylist = ref(false)
const isDragging = ref(false)
const currentTimeVal = ref(0)
const volumeVal = ref(0.8)
const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200"%3E%3Crect fill="%23667eea" width="200" height="200"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="white" font-size="60"%3E♪%3C/text%3E%3C/svg%3E'

const qualityLabel = computed(() => {
  const q = playerStore.currentQuality
  if (q === 'flac') return '无损 FLAC'
  if (q === '320') return '高清 320kbps'
  return '普通 128kbps'
})

watch(() => playerStore.currentTime, (val) => {
  if (!isDragging.value) {
    currentTimeVal.value = val
  }
})

watch(() => playerStore.volume, (val) => {
  volumeVal.value = val
})

const formatTime = (seconds) => {
  if (!seconds || isNaN(seconds)) return '0:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const handleSeek = (val) => {
  isDragging.value = false
  playerStore.seek(val)
}

const handleVolumeChange = (val) => {
  playerStore.setVolume(val)
}

const handleQualityChange = (quality) => {
  if (quality === 'flac' && !userStore.isPremium) {
    return
  }
  playerStore.setQuality(quality)
}

onMounted(() => {
  playerStore.initAudio()
})
</script>

<style scoped>
.player-container {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: rgba(20, 20, 30, 0.95);
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.player {
  max-width: 1400px;
  margin: 0 auto;
  height: 100px;
  display: flex;
  align-items: center;
  padding: 0 30px;
  gap: 30px;
}

.player-empty {
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
  color: #999;
}

.empty-text {
  font-size: 14px;
}

.player-left {
  display: flex;
  align-items: center;
  gap: 15px;
  min-width: 300px;
}

.album-cover {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 3px solid rgba(255, 255, 255, 0.2);
}

.album-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.music-info {
  flex: 1;
  min-width: 0;
}

.title {
  color: white;
  font-size: 14px;
  font-weight: 500;
}

.artist {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  margin-top: 4px;
}

.player-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.controls {
  display: flex;
  align-items: center;
  gap: 15px;
}

.progress-bar {
  display: flex;
  align-items: center;
  gap: 15px;
  width: 100%;
  max-width: 600px;
}

.time {
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  min-width: 40px;
  text-align: center;
}

.progress-slider {
  flex: 1;
}

.player-right {
  display: flex;
  align-items: center;
  gap: 20px;
  min-width: 250px;
}

.quality-label {
  color: white;
  font-size: 13px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 4px;
}

.volume-control {
  display: flex;
  align-items: center;
  gap: 10px;
}

.volume-slider {
  width: 100px;
}

.playlist-drawer {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.playlist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0 20px;
  border-bottom: 1px solid #eee;
}

.playlist-content {
  flex: 1;
  overflow-y: auto;
}

.playlist-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px 15px;
  cursor: pointer;
  border-radius: 8px;
  transition: background 0.2s;
}

.playlist-item:hover {
  background: #f5f5f5;
}

.playlist-item.active {
  background: rgba(102, 126, 234, 0.1);
}

.playlist-item.active .item-title {
  color: #667eea;
}

.item-index {
  width: 24px;
  text-align: center;
  color: #999;
  font-size: 14px;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 14px;
  color: #333;
}

.item-artist {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.empty-playlist {
  text-align: center;
  color: #999;
  padding: 40px;
}
</style>
