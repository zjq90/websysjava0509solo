import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getMusicStreamUrl } from '@/api/music'

export const usePlayerStore = defineStore('player', () => {
  const playlist = ref([])
  const currentIndex = ref(-1)
  const isPlaying = ref(false)
  const currentTime = ref(0)
  const duration = ref(0)
  const volume = ref(0.8)
  const isMuted = ref(false)
  const playMode = ref('sequence')
  const currentQuality = ref('128')
  const audio = ref(null)

  const currentMusic = computed(() => {
    if (currentIndex.value >= 0 && currentIndex.value < playlist.value.length) {
      return playlist.value[currentIndex.value]
    }
    return null
  })

  const currentUrl = computed(() => {
    if (currentMusic.value) {
      return getMusicStreamUrl(currentMusic.value.id, currentQuality.value)
    }
    return ''
  })

  const progress = computed(() => {
    if (duration.value > 0) {
      return (currentTime.value / duration.value) * 100
    }
    return 0
  })

  const initAudio = () => {
    if (!audio.value) {
      audio.value = new Audio()
      audio.value.volume = volume.value

      audio.value.addEventListener('timeupdate', () => {
        currentTime.value = audio.value.currentTime
      })

      audio.value.addEventListener('loadedmetadata', () => {
        duration.value = audio.value.duration
      })

      audio.value.addEventListener('ended', () => {
        handleEnded()
      })

      audio.value.addEventListener('play', () => {
        isPlaying.value = true
      })

      audio.value.addEventListener('pause', () => {
        isPlaying.value = false
      })
    }
  }

  const setPlaylist = (list, index = 0) => {
    playlist.value = list
    currentIndex.value = index
    playCurrent()
  }

  const addToPlaylist = (music) => {
    const exists = playlist.value.findIndex(m => m.id === music.id)
    if (exists === -1) {
      playlist.value.push(music)
    }
    if (currentIndex.value === -1) {
      currentIndex.value = 0
      playCurrent()
    }
  }

  const playMusic = (music) => {
    const index = playlist.value.findIndex(m => m.id === music.id)
    if (index !== -1) {
      currentIndex.value = index
    } else {
      playlist.value.push(music)
      currentIndex.value = playlist.value.length - 1
    }
    playCurrent()
  }

  const playCurrent = () => {
    if (!currentMusic.value) return

    initAudio()
    audio.value.src = currentUrl.value
    audio.value.play().catch(() => {
      isPlaying.value = false
    })
  }

  const togglePlay = () => {
    if (!audio.value || !currentMusic.value) return

    if (isPlaying.value) {
      audio.value.pause()
    } else {
      audio.value.play().catch(() => {})
    }
  }

  const pause = () => {
    if (audio.value) {
      audio.value.pause()
    }
  }

  const play = () => {
    if (audio.value && currentMusic.value) {
      audio.value.play().catch(() => {})
    }
  }

  const seek = (time) => {
    if (audio.value) {
      audio.value.currentTime = time
      currentTime.value = time
    }
  }

  const setVolume = (vol) => {
    volume.value = vol
    if (audio.value) {
      audio.value.volume = vol
    }
    if (vol > 0) {
      isMuted.value = false
    }
  }

  const toggleMute = () => {
    isMuted.value = !isMuted.value
    if (audio.value) {
      audio.value.volume = isMuted.value ? 0 : volume.value
    }
  }

  const next = () => {
    if (playlist.value.length === 0) return

    if (playMode.value === 'random') {
      currentIndex.value = Math.floor(Math.random() * playlist.value.length)
    } else {
      currentIndex.value = (currentIndex.value + 1) % playlist.value.length
    }
    playCurrent()
  }

  const prev = () => {
    if (playlist.value.length === 0) return

    if (currentTime.value > 3) {
      seek(0)
      return
    }

    if (playMode.value === 'random') {
      currentIndex.value = Math.floor(Math.random() * playlist.value.length)
    } else {
      currentIndex.value = (currentIndex.value - 1 + playlist.value.length) % playlist.value.length
    }
    playCurrent()
  }

  const handleEnded = () => {
    if (playMode.value === 'loop') {
      seek(0)
      play()
    } else {
      next()
    }
  }

  const togglePlayMode = () => {
    const modes = ['sequence', 'loop', 'random']
    const current = modes.indexOf(playMode.value)
    playMode.value = modes[(current + 1) % modes.length]
  }

  const setQuality = (quality) => {
    const wasPlaying = isPlaying.value
    const currentPos = currentTime.value
    currentQuality.value = quality

    if (currentMusic.value) {
      initAudio()
      audio.value.src = currentUrl.value
      audio.value.currentTime = currentPos
      if (wasPlaying) {
        audio.value.play().catch(() => {})
      }
    }
  }

  const removeFromPlaylist = (index) => {
    if (index < 0 || index >= playlist.value.length) return

    playlist.value.splice(index, 1)

    if (index === currentIndex.value) {
      if (playlist.value.length > 0) {
        currentIndex.value = Math.min(index, playlist.value.length - 1)
        playCurrent()
      } else {
        currentIndex.value = -1
        if (audio.value) {
          audio.value.pause()
          audio.value.src = ''
        }
      }
    } else if (index < currentIndex.value) {
      currentIndex.value--
    }
  }

  const clearPlaylist = () => {
    playlist.value = []
    currentIndex.value = -1
    if (audio.value) {
      audio.value.pause()
      audio.value.src = ''
    }
  }

  return {
    playlist,
    currentIndex,
    isPlaying,
    currentTime,
    duration,
    volume,
    isMuted,
    playMode,
    currentQuality,
    currentMusic,
    currentUrl,
    progress,
    initAudio,
    setPlaylist,
    addToPlaylist,
    playMusic,
    playCurrent,
    togglePlay,
    pause,
    play,
    seek,
    setVolume,
    toggleMute,
    next,
    prev,
    togglePlayMode,
    setQuality,
    removeFromPlaylist,
    clearPlaylist
  }
})
