<template>
  <div class="home">
    <div class="hero-section">
      <h1 class="hero-title">发现美妙音乐</h1>
      <p class="hero-subtitle">海量曲库，无损音质，随时随地享受音乐</p>
      <div class="hero-actions">
        <el-button type="primary" size="large" @click="$router.push('/music')">
          <el-icon><Search /></el-icon> 浏览音乐
        </el-button>
        <el-button size="large" @click="$router.push('/register')">
          立即注册
        </el-button>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <h2 class="section-title">
          <el-icon><TrendCharts /></el-icon> 热门推荐
        </h2>
        <el-button type="primary" link @click="$router.push('/music')">查看更多 →</el-button>
      </div>
      <div v-if="loading" class="loading">
        <el-skeleton :rows="4" animated />
      </div>
      <div v-else class="music-grid">
        <MusicCard v-for="music in hotMusic" :key="music.id" :music="music" />
      </div>
      <el-empty v-if="!loading && hotMusic.length === 0" description="暂无音乐" />
    </div>

    <div class="features">
      <div class="feature-item">
        <el-icon :size="48" color="#667eea"><Headset /></el-icon>
        <h3>高清音质</h3>
        <p>支持128kbps、320kbps和FLAC无损音质</p>
      </div>
      <div class="feature-item">
        <el-icon :size="48" color="#764ba2"><Download /></el-icon>
        <h3>离线下载</h3>
        <p>会员专享高音质下载，随时随地畅听</p>
      </div>
      <div class="feature-item">
        <el-icon :size="48" color="#f093fb"><Reading /></el-icon>
        <h3>歌词同步</h3>
        <p>精准歌词同步显示，跟着唱更带感</p>
      </div>
      <div class="feature-item">
        <el-icon :size="48" color="#4facfe"><MagicStick /></el-icon>
        <h3>智能播放</h3>
        <p>顺序播放、循环播放、随机播放随心切换</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MusicCard from '@/components/MusicCard.vue'
import { getHotMusic } from '@/api/music'

const hotMusic = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    hotMusic.value = await getHotMusic(8)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.hero-section {
  text-align: center;
  padding: 80px 20px;
  color: white;
}

.hero-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.hero-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.section {
  margin: 40px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  color: white;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 0;
}

.music-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.loading {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 20px;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin: 60px 0;
}

.feature-item {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 40px 24px;
  text-align: center;
  transition: transform 0.3s;
}

.feature-item:hover {
  transform: translateY(-8px);
}

.feature-item h3 {
  margin: 16px 0 8px;
  font-size: 20px;
  color: #333;
}

.feature-item p {
  color: #666;
  margin: 0;
}
</style>
