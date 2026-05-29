<template>
  <div class="lyrics-container">
    <div v-if="!lyrics" class="no-lyrics">
      <el-icon :size="48" color="#999"><Document /></el-icon>
      <p>暂无歌词</p>
    </div>
    <div v-else-if="lyrics.hasSync && parsedLyrics.length > 0" class="lyrics-scroll" ref="lyricsScroll">
      <div
        v-for="(line, index) in parsedLyrics"
        :key="index"
        class="lyric-line"
        :class="{ active: currentLineIndex === index }"
        @click="seekTo(line.time)"
      >
        {{ line.text }}
      </div>
    </div>
    <div v-else-if="lyrics.content" class="lyrics-plain">
      <pre>{{ lyrics.content }}</pre>
    </div>
    <div v-else class="no-lyrics">
      <el-icon :size="48" color="#999"><Document /></el-icon>
      <p>暂无歌词</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { usePlayerStore } from '@/stores/player'

const props = defineProps({
  lyrics: {
    type: Object,
    default: null
  }
})

const playerStore = usePlayerStore()
const lyricsScroll = ref(null)
const currentLineIndex = ref(-1)
let updateInterval = null

const parsedLyrics = computed(() => {
  if (!props.lyrics?.lrcContent) return []
  return parseLrc(props.lyrics.lrcContent)
})

const parseLrc = (lrcContent) => {
  const lines = lrcContent.split('\n')
  const result = []
  const timeRegex = /\[(\d{2}):(\d{2})\.(\d{2,3})\]/g

  for (const line of lines) {
    const matches = [...line.matchAll(timeRegex)]
    const text = line.replace(timeRegex, '').trim()

    if (matches.length > 0 && text) {
      for (const match of matches) {
        const minutes = parseInt(match[1])
        const seconds = parseInt(match[2])
        const milliseconds = parseInt(match[3].padEnd(3, '0'))
        const time = minutes * 60 + seconds + milliseconds / 1000
        result.push({ time, text })
      }
    }
  }

  return result.sort((a, b) => a.time - b.time)
}

const updateCurrentLine = () => {
  if (parsedLyrics.value.length === 0) return

  const currentTime = playerStore.currentTime
  let index = -1

  for (let i = 0; i < parsedLyrics.value.length; i++) {
    if (currentTime >= parsedLyrics.value[i].time) {
      index = i
    } else {
      break
    }
  }

  currentLineIndex.value = index

  if (index >= 0 && lyricsScroll.value) {
    const activeElement = lyricsScroll.value.querySelector('.lyric-line.active')
    if (activeElement) {
      const containerHeight = lyricsScroll.value.clientHeight
      const elementTop = activeElement.offsetTop
      const elementHeight = activeElement.clientHeight
      const scrollTop = elementTop - containerHeight / 2 + elementHeight / 2
      lyricsScroll.value.scrollTo({
        top: scrollTop,
        behavior: 'smooth'
      })
    }
  }
}

const seekTo = (time) => {
  playerStore.seek(time)
}

watch(() => props.lyrics, () => {
  currentLineIndex.value = -1
})

onMounted(() => {
  updateInterval = setInterval(updateCurrentLine, 100)
})

onUnmounted(() => {
  if (updateInterval) {
    clearInterval(updateInterval)
  }
})
</script>

<style scoped>
.lyrics-container {
  height: 100%;
  overflow: hidden;
}

.no-lyrics {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
  color: #999;
}

.lyrics-scroll {
  height: 100%;
  overflow-y: auto;
  padding: 50px 20px;
  scroll-behavior: smooth;
}

.lyric-line {
  padding: 12px 0;
  text-align: center;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.3s ease;
  line-height: 1.8;
}

.lyric-line:hover {
  color: rgba(255, 255, 255, 0.8);
}

.lyric-line.active {
  color: white;
  font-size: 20px;
  font-weight: 500;
  transform: scale(1.05);
}

.lyrics-plain {
  height: 100%;
  overflow-y: auto;
  padding: 20px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 2;
  white-space: pre-wrap;
}

.lyrics-plain pre {
  font-family: inherit;
  white-space: pre-wrap;
  margin: 0;
}
</style>
