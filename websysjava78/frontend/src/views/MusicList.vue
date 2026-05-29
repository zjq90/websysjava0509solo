<template>
  <div class="music-list">
    <div class="header-section">
      <h1 class="page-title">音乐库</h1>
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索歌曲名或歌手"
          clearable
          size="large"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" size="large" @click="handleSearch">搜索</el-button>
      </div>
    </div>

    <div class="music-grid">
      <div v-if="loading" v-for="i in 12" :key="i" class="skeleton-card">
        <el-skeleton :rows="3" animated />
      </div>
      <template v-else>
        <MusicCard v-for="music in musicList" :key="music.id" :music="music" />
        <el-empty v-if="musicList.length === 0" description="暂无音乐" />
      </template>
    </div>

    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[12, 24, 48, 96]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import MusicCard from '@/components/MusicCard.vue'
import { getMusicList } from '@/api/music'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const musicList = ref([])
const loading = ref(true)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const fetchMusic = async () => {
  loading.value = true
  try {
    const result = await getMusicList(currentPage.value - 1, pageSize.value, searchKeyword.value)
    musicList.value = result.content
    total.value = result.totalElements
  } catch (e) {
    ElMessage.error('加载音乐列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchMusic()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchMusic()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchMusic()
}

onMounted(fetchMusic)
</script>

<style scoped>
.music-list {
  max-width: 1400px;
  margin: 0 auto;
}

.header-section {
  margin-bottom: 30px;
  color: white;
}

.page-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  gap: 15px;
  max-width: 600px;
}

.search-bar .el-input {
  flex: 1;
}

.music-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.skeleton-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.pagination :deep(.el-pagination) {
  --el-pagination-bg-color: rgba(255, 255, 255, 0.9);
}
</style>
