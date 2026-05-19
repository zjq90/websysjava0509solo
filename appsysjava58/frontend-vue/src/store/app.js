import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    elderMode: false,
    currentPet: null
  }),
  actions: {
    toggleElderMode() {
      this.elderMode = !this.elderMode
      localStorage.setItem('elderMode', this.elderMode)
    },
    loadSettings() {
      const saved = localStorage.getItem('elderMode')
      if (saved !== null) {
        this.elderMode = saved === 'true'
      }
    }
  }
})