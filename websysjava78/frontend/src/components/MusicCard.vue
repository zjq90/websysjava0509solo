<template>
  <div class="music-card" @click="handlePlay">
    <div class="cover-wrapper">
      <img :src="music.coverUrl || defaultCover" :alt="music.title" class="cover" />
      <div class="play-overlay">
        <el-icon :size="40" color="#fff"><VideoPlay /></el-icon>
      </div>
      <el-tag v-if="music.isPremium" type="warning" size="small" class="premium-tag">VIP</el-tag>
    </div>
    <div class="info">
      <div class="title text-ellipsis">{{ music.title }}</div>
      <div class="artist text-ellipsis">{{ music.artist }}</div>
    </div>
    <div class="actions">
      <el-dropdown @command="handleCommand" trigger="click">
        <el-button circle size="small">
          <el-icon><MoreFilled /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="play">
              <el-icon><VideoPlay /></el-icon> 播放
            </el-dropdown-item>
            <el-dropdown-item command="add">
              <el-icon><Plus /></el-icon> 添加到列表
            </el-dropdown-item>
            <el-dropdown-item command="lyrics">
              <el-icon><Document /></el-icon> 查看歌词
            </el-dropdown-item>
            <el-dropdown-item divided command="download-128">
              <el-icon><Download /></el-icon> 下载 128kbps
            </el-dropdown-item>
            <el-dropdown-item command="download-320">
              <el-icon><Download /></el-icon> 下载 320kbps
            </el-dropdown-item>
            <el-dropdown-item
              command="download-flac"
              :disabled="!userStore.isPremium"
            >
              <el-icon><Download /></el-icon> 下载 FLAC
              <el-tag v-if="!userStore.isPremium" type="warning" size="small">VIP</el-tag>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { usePlayerStore } from '@/stores/player'
import { useUserStore } from '@/stores/user'
import { canDownload, downloadMusic, getDownloadUrl } from '@/api/music'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps({
  music: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const playerStore = usePlayerStore()
const userStore = useUserStore()

const defaultCover = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 200 200"%3E%3Crect fill="%23667eea" width="200" height="200"/%3E%3Ctext x="50%25" y="50%25" text-anchor="middle" dy=".3em" fill="white" font-size="60"%3E♪%3C/text%3E%3C/svg%3E'

const handlePlay = () => {
  playerStore.playMusic(props.music)
}

const handleCommand = async (command) => {
  switch (command) {
    case 'play':
      playerStore.playMusic(props.music)
      break
    case 'add':
      playerStore.addToPlaylist(props.music)
      ElMessage.success('已添加到播放列表')
      break
    case 'lyrics':
      router.push(`/lyrics/${props.music.id}`)
      break
    case 'download-128':
      await handleDownload('128')
      break
    case 'download-320':
      await handleDownload('320')
      break
    case 'download-flac':
      await handleDownload('flac')
      break
  }
}

const handleDownload = async (quality) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  try {
    const check = await canDownload(props.music.id, quality)
    if (!check.canDownload) {
      ElMessageBox.confirm(check.reason, '无法下载', {
        confirmButtonText: '了解',
        showCancelButton: false,
        type: 'warning'
      })
      return
    }

    await ElMessageBox.confirm(
      `确定下载 ${props.music.title} (${quality === 'flac' ? '无损FLAC' : quality + 'kbps'})？\n剩余下载次数: ${check.remaining}`,
      '下载确认',
      {
        confirmButtonText: '下载',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    await downloadMusic(props.music.id, quality)
    const url = getDownloadUrl(props.music.id, quality)
    const link = document.createElement('a')
    link.href = url
    link.download = `${props.music.title} - ${props.music.artist}.${quality === 'flac' ? 'flac' : 'mp3'}`
    link.click()
    ElMessage.success('开始下载')
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('下载失败')
    }
  }
}
</script>

<style scoped>
.music-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.music-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
}

.cover-wrapper {
  position: relative;
  width: 100%;
  padding-top: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.cover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.play-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.music-card:hover .play-overlay {
  opacity: 1;
}

.premium-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.info {
  flex: 1;
}

.title {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.artist {
  font-size: 13px;
  color: #999;
  margin-top: 4px;
}

.actions {
  display: flex;
  justify-content: flex-end;
}
</style>
