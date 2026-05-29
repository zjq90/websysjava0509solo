<template>
  <div class="app-container">
    <HeaderBar />
    <div class="main-content">
      <router-view />
    </div>
    <MusicPlayer />
  </div>
</template>

<script setup>
import HeaderBar from '@/components/HeaderBar.vue'
import MusicPlayer from '@/components/MusicPlayer.vue'
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.main-content {
  flex: 1;
  padding: 20px;
  padding-bottom: 120px;
}
</style>
