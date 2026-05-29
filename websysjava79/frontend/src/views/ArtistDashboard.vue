<template>
  <div class="artist-dashboard">
    <el-page-header @back="() => router.push('/')" content="音乐人中心" />
    
    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon play"><VideoPlay /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalPlays }}</div>
              <div class="stat-label">总播放量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon download"><Download /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalDownloads }}</div>
              <div class="stat-label">总下载量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon revenue"><Money /></el-icon>
            <div class="stat-info">
              <div class="stat-value">¥{{ stats.totalRevenue }}</div>
              <div class="stat-label">总收益</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <el-icon class="stat-icon fan"><User /></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ stats.fanCount }}</div>
              <div class="stat-label">粉丝数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>粉丝增长趋势</span>
            </div>
          </template>
          <div class="chart-placeholder">
            <el-empty description="图表数据加载中..." />
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>用户地域分布</span>
            </div>
          </template>
          <div class="region-list">
            <div v-for="(region, index) in regions" :key="index" class="region-item">
              <span class="region-name">{{ region.name }}</span>
              <div class="region-bar">
                <div class="region-fill" :style="{ width: region.percent + '%' }"></div>
              </div>
              <span class="region-percent">{{ region.percent }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 24px;">
      <template #header>
        <div class="card-header">
          <span>我的作品</span>
          <el-button type="primary" @click="goToUpload">
            <el-icon><Upload /></el-icon>
            上传新作品
          </el-button>
        </div>
      </template>
      <el-table :data="musics" v-loading="loading">
        <el-table-column prop="title" label="歌曲名称" />
        <el-table-column prop="album" label="专辑" />
        <el-table-column prop="playCount" label="播放量" />
        <el-table-column prop="likeCount" label="喜欢数" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishedAt" label="发布时间" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="text" size="small">编辑</el-button>
            <el-button type="text" size="small">数据</el-button>
            <el-button type="text" size="small" style="color: #f56c6c;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()

const loading = ref(false)
const stats = ref({
  totalPlays: 0,
  totalDownloads: 0,
  totalRevenue: 0,
  fanCount: 0
})
const musics = ref<any[]>([])
const regions = ref([
  { name: '广东', percent: 18 },
  { name: '北京', percent: 15 },
  { name: '上海', percent: 12 },
  { name: '浙江', percent: 10 },
  { name: '江苏', percent: 9 },
  { name: '四川', percent: 8 },
  { name: '其他', percent: 28 }
])

const loadStats = async () => {
  loading.value = true
  try {
    const data = await request.get('/artist/stats')
    stats.value = data
  } catch (e) {
    stats.value = {
      totalPlays: 125680,
      totalDownloads: 8920,
      totalRevenue: 25680.50,
      fanCount: 32560
    }
  }
}

const loadMusics = async () => {
  try {
    musics.value = await request.get('/artist/musics')
  } catch (e) {
    musics.value = [
      { id: 1, title: '晴天', album: '叶惠美', playCount: 45680, likeCount: 3256, status: 'APPROVED', publishedAt: '2024-01-15' },
      { id: 2, title: '七里香', album: '七里香', playCount: 38920, likeCount: 2890, status: 'APPROVED', publishedAt: '2024-01-10' },
      { id: 3, title: '稻香', album: '魔杰座', playCount: 28650, likeCount: 2156, status: 'APPROVED', publishedAt: '2024-01-05' },
      { id: 4, title: '青花瓷', album: '我很忙', playCount: 12430, likeCount: 980, status: 'PENDING', publishedAt: '2024-01-20' }
    ]
  } finally {
    loading.value = false
  }
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    APPROVED: 'success',
    PENDING: 'warning',
    REJECTED: 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    APPROVED: '已通过',
    PENDING: '审核中',
    REJECTED: '已拒绝'
  }
  return map[status] || status
}

const goToUpload = () => {
  router.push('/upload')
}

onMounted(() => {
  loadStats()
  loadMusics()
})
</script>

<style scoped>
.artist-dashboard {
  max-width: 1400px;
  margin: 0 auto;
}

.stat-card {
  text-align: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  font-size: 40px;
  padding: 16px;
  border-radius: 12px;
}

.stat-icon.play {
  color: #409eff;
  background: #ecf5ff;
}

.stat-icon.download {
  color: #67c23a;
  background: #f0f9eb;
}

.stat-icon.revenue {
  color: #e6a23c;
  background: #fdf6ec;
}

.stat-icon.fan {
  color: #f56c6c;
  background: #fef0f0;
}

.stat-info {
  text-align: left;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.region-list {
  padding: 16px 0;
}

.region-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.region-name {
  width: 60px;
  font-size: 14px;
}

.region-bar {
  flex: 1;
  height: 12px;
  background: #f0f0f0;
  border-radius: 6px;
  overflow: hidden;
}

.region-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 6px;
  transition: width 0.3s;
}

.region-percent {
  width: 50px;
  text-align: right;
  font-size: 14px;
  color: #666;
}
</style>
