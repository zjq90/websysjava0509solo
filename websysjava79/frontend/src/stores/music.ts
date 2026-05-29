import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../utils/request'

export interface Music {
  id: number
  title: string
  description: string
  artistName: string
  album: string
  coverUrl: string
  audioUrl: string
  duration: string
  playCount: number
  likeCount: number
  commentCount: number
  price: number
  tags: string[]
}

export const useMusicStore = defineStore('music', () => {
  const currentMusic = ref<Music | null>(null)
  const playlist = ref<Music[]>([])
  const isPlaying = ref(false)
  const volume = ref(0.7)

  const playMusic = (music: Music) => {
    currentMusic.value = music
    isPlaying.value = true
    request.post(`/musics/${music.id}/play`)
  }

  const addToPlaylist = (music: Music) => {
    if (!playlist.value.find(m => m.id === music.id)) {
      playlist.value.push(music)
    }
  }

  const removeFromPlaylist = (index: number) => {
    playlist.value.splice(index, 1)
  }

  const togglePlay = () => {
    isPlaying.value = !isPlaying.value
  }

  const nextMusic = () => {
    if (playlist.value.length > 0 && currentMusic.value) {
      const currentIndex = playlist.value.findIndex(m => m.id === currentMusic.value!.id)
      const nextIndex = (currentIndex + 1) % playlist.value.length
      currentMusic.value = playlist.value[nextIndex]
      isPlaying.value = true
    }
  }

  const prevMusic = () => {
    if (playlist.value.length > 0 && currentMusic.value) {
      const currentIndex = playlist.value.findIndex(m => m.id === currentMusic.value!.id)
      const prevIndex = currentIndex === 0 ? playlist.value.length - 1 : currentIndex - 1
      currentMusic.value = playlist.value[prevIndex]
      isPlaying.value = true
    }
  }

  return {
    currentMusic,
    playlist,
    isPlaying,
    volume,
    playMusic,
    addToPlaylist,
    removeFromPlaylist,
    togglePlay,
    nextMusic,
    prevMusic
  }
})
