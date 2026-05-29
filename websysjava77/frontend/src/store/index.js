import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../api'

export const useUserStore = defineStore('user', () => {
  const currentUser = ref(null)
  const isLoggedIn = ref(false)

  async function login(username, password) {
    const res = await api.user.login({ username, password })
    if (res.code === 200) {
      currentUser.value = res.data
      isLoggedIn.value = true
      localStorage.setItem('userId', res.data.id)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('nickname', res.data.nickname)
      return true
    }
    return false
  }

  async function register(username, password, email, nickname) {
    const res = await api.user.register({ username, password, email, nickname })
    return res.code === 200
  }

  function logout() {
    currentUser.value = null
    isLoggedIn.value = false
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('nickname')
  }

  function init() {
    const userId = localStorage.getItem('userId')
    const username = localStorage.getItem('username')
    const nickname = localStorage.getItem('nickname')
    if (userId) {
      currentUser.value = { id: Number(userId), username, nickname }
      isLoggedIn.value = true
    }
  }

  return { currentUser, isLoggedIn, login, register, logout, init }
})

export const usePlayerStore = defineStore('player', () => {
  const currentMusic = ref(null)
  const isPlaying = ref(false)
  const playlist = ref([])
  const currentIndex = ref(-1)

  function play(music) {
    currentMusic.value = music
    isPlaying.value = true
    const idx = playlist.value.findIndex(m => m.id === music.id)
    if (idx >= 0) {
      currentIndex.value = idx
    } else {
      playlist.value.push(music)
      currentIndex.value = playlist.value.length - 1
    }
    if (music.id) {
      const userId = localStorage.getItem('userId')
      if (userId) {
        api.recommend.recordBehavior({ userId: Number(userId), musicId: music.id, type: 'play' })
      }
    }
  }

  function togglePlay() {
    isPlaying.value = !isPlaying.value
  }

  function playNext() {
    if (playlist.value.length === 0) return
    currentIndex.value = (currentIndex.value + 1) % playlist.value.length
    currentMusic.value = playlist.value[currentIndex.value]
    isPlaying.value = true
  }

  function playPrev() {
    if (playlist.value.length === 0) return
    currentIndex.value = (currentIndex.value - 1 + playlist.value.length) % playlist.value.length
    currentMusic.value = playlist.value[currentIndex.value]
    isPlaying.value = true
  }

  function setPlaylist(list) {
    playlist.value = list
    if (list.length > 0 && currentIndex.value < 0) {
      currentIndex.value = 0
      currentMusic.value = list[0]
    }
  }

  return { currentMusic, isPlaying, playlist, currentIndex, play, togglePlay, playNext, playPrev, setPlaylist }
})
