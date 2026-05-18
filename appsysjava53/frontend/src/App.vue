<template>
  <div id="app" :class="{ 'elder-mode': isElderMode }">
    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()
const isElderMode = computed(() => appStore.isElderMode)
</script>

<style scoped>
#app {
  min-height: 100vh;
  background-color: #f5f5f5;
}
</style>
