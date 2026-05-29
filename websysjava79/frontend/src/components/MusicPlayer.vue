<template>
  <div class="music-player">
    <div class="player-content">
      <div class="player-left">
        <el-avatar :size="48" :src="musicStore.currentMusic?.coverUrl || 'https://picsum.photos/100/100'" shape="square" />
        <div class="music-info">
          <div class="music-title">{{ musicStore.currentMusic?.title || '暂无播放' }}</div>
          <div class="music-artist">{{ musicStore.currentMusic?.artistName || '-' }}</div>
        </div>
      </div>
      <div class="player-controls">
        <el-button circle @click="musicStore.prevMusic()">
          <el-icon><VideoPlay /></el-icon>
        </el-button>
        <el-button type="primary" circle @click="musicStore.togglePlay()">
          <el-icon v-if="musicStore.isPlaying"><Pause /></el-icon>
          <el-icon v-else><VideoPlay /></el-icon>
        </el-button>
        <el-button circle @click="musicStore.nextMusic()">
          <el-icon><VideoPlay /></el-icon>
        </el-button>
      </div>
      <div class="player-progress">
        <el-progress :percentage="50" :show-text="false" />
      </div>
      <div class="player-right">
        <el-button circle>
          <el-icon><Share /></el-icon>
        </el-button>
        <el-button circle>
          <el-icon><Collection /></el-icon>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useMusicStore } from '../stores/music'

const musicStore = useMusicStore()
</script>

<style scoped>
.music-player {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 72px;
  background: #fff;
  border-top: 1px solid #e8e8e8;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.player-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.player-left {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.music-info {
  display: flex;
  flex-direction: column;
}

.music-title {
  font-weight: 500;
  font-size: 14px;
}

.music-artist {
  font-size: 12px;
  color: #999;
}

.player-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.player-progress {
  flex: 2;
  padding: 0 24px;
}

.player-right {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
