<template>
  <div class="user-center">
    <div class="card profile-card">
      <div class="profile-header">
        <div class="avatar">
          <el-icon :size="64" color="#667eea"><UserFilled /></el-icon>
        </div>
        <div class="profile-info">
          <h2>{{ user?.nickname || user?.username }}</h2>
          <p>@{{ user?.username }}</p>
          <el-tag :type="user?.role === 'PREMIUM' || user?.role === 'ADMIN' ? 'warning' : 'info'" size="large">
            {{ user?.role === 'PREMIUM' ? 'VIP会员' : user?.role === 'ADMIN' ? '管理员' : '免费用户' }}
          </el-tag>
        </div>
        <div class="profile-actions">
          <el-button
            v-if="user?.role === 'FREE'"
            type="warning"
            @click="handleUpgrade"
          >
            <el-icon><Crown /></el-icon> 升级会员
          </el-button>
        </div>
      </div>
      <div class="stats">
        <div class="stat-item">
          <div class="stat-value">{{ remainingDownloads }}</div>
          <div class="stat-label">今日剩余下载</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ downloadLimit }}</div>
          <div class="stat-label">每日下载限额</div>
        </div>
      </div>
    </div>

    <div class="card">
      <h3 class="card-title">
        <el-icon><Download /></el-icon> 我的下载
      </h3>
      <el-table :data="downloads" v-loading="loading" style="width: 100%">
        <el-table-column prop="music.title" label="歌曲名" />
        <el-table-column prop="music.artist" label="歌手" />
        <el-table-column prop="quality" label="音质">
          <template #default="{ row }">
            <span v-if="row.quality === 'QUALITY_128'">128kbps</span>
            <span v-else-if="row.quality === 'QUALITY_320'">320kbps</span>
            <span v-else>FLAC 无损</span>
          </template>
        </el-table-column>
        <el-table-column prop="downloadedAt" label="下载时间">
          <template #default="{ row }">
            {{ formatDate(row.downloadedAt) }}
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="downloads.length === 0 && !loading" description="暂无下载记录" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getMyDownloads } from '@/api/music'
import { upgradeToPremium, getRemainingDownloads } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const user = ref(null)
const downloads = ref([])
const loading = ref(true)
const remainingDownloads = ref(0)
const downloadLimit = ref(5)

const fetchData = async () => {
  loading.value = true
  try {
    user.value = await userStore.fetchCurrentUser()
    const downloadsData = await getMyDownloads(0, 50)
    downloads.value = downloadsData.content || []

    const remaining = await getRemainingDownloads()
    remainingDownloads.value = remaining.remaining
    downloadLimit.value = remaining.limit
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleUpgrade = async () => {
  try {
    await ElMessageBox.confirm(
      '确定升级为VIP会员？升级后可享受无损音质下载和每日100次下载限额',
      '升级会员',
      {
        confirmButtonText: '确定升级',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await upgradeToPremium(user.value.id)
    ElMessage.success('升级成功！')
    await fetchData()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('升级失败')
    }
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  userStore.initFromStorage()
  fetchData()
})
</script>

<style scoped>
.user-center {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.profile-info {
  flex: 1;
}

.profile-info h2 {
  margin: 0 0 8px;
  font-size: 24px;
  color: #333;
}

.profile-info p {
  margin: 0 0 12px;
  color: #999;
}

.stats {
  display: flex;
  gap: 40px;
  margin-top: 20px;
  padding-top: 20px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #667eea;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.card-title {
  font-size: 18px;
  margin: 0 0 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
