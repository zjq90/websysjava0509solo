<template>
  <div id="app" :class="{ 'elder-mode': isElderMode }">
    <router-view />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()
const isElderMode = computed(() => appStore.elderMode)
</script>

<style>
#app {
  min-height: 100vh;
  background: #f5f7fa;
}

.elder-mode {
  font-size: 18px !important;
}

.elder-mode .el-button {
  font-size: 18px !important;
  padding: 15px 30px !important;
}

.elder-mode .el-card {
  padding: 20px !important;
}
</style>