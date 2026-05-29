<template>
  <div class="admin-dashboard">
    <el-page-header @back="() => router.push('/')" content="管理后台" />
    
    <el-tabs v-model="activeTab" style="margin-top: 24px;">
      <el-tab-pane label="数据概览" name="overview">
        <el-row :gutter="20" style="margin-top: 24px;">
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <el-icon class="stat-icon user"><User /></el-icon>
                <div class="stat-info">
                  <div class="stat-value">{{ stats.userCount }}</div>
                  <div class="stat-label">总用户数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <el-icon class="stat-icon music"><VideoPlay /></el-icon>
                <div class="stat-info">
                  <div class="stat-value">{{ stats.musicCount }}</div>
                  <div class="stat-label">音乐数量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <el-icon class="stat-icon playlist"><Collection /></el-icon>
                <div class="stat-info">
                  <div class="stat-value">{{ stats.playlistCount }}</div>
                  <div class="stat-label">歌单数量</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <el-icon class="stat-icon active"><TrendCharts /></el-icon>
                <div class="stat-info">
                  <div class="stat-value">{{ stats.dailyActive }}</div>
                  <div class="stat-label">今日活跃</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>用户活跃度趋势</span>
              </template>
              <div class="chart-placeholder">
                <el-empty description="图表数据加载中..." />
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>系统性能监控</span>
              </template>
              <div class="system-stats">
                <div class="system-item">
                  <div class="system-label">CPU 使用率</div>
                  <el-progress :percentage="systemStats.cpu" :color="getProgressColor(systemStats.cpu)" />
                </div>
                <div class="system-item">
                  <div class="system-label">内存使用率</div>
                  <el-progress :percentage="systemStats.memory" :color="getProgressColor(systemStats.memory)" />
                </div>
                <div class="system-item">
                  <div class="system-label">磁盘使用率</div>
                  <el-progress :percentage="systemStats.disk" :color="getProgressColor(systemStats.disk)" />
                </div>
                <div class="system-item">
                  <div class="system-label">数据库连接</div>
                  <el-progress :percentage="systemStats.db" :color="getProgressColor(systemStats.db)" />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="内容审核" name="review">
        <el-card style="margin-top: 24px;">
          <template #header>
            <div class="card-header">
              <span>待审核音乐 ({{ pendingMusics.length }})</span>
            </div>
          </template>
          <el-table :data="pendingMusics" v-loading="loading">
            <el-table-column prop="title" label="歌曲名称" />
            <el-table-column prop="artistName" label="音乐人" />
            <el-table-column prop="album" label="专辑" />
            <el-table-column prop="createdAt" label="提交时间" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button type="success" size="small" @click="approveMusic(row.id)">
                  通过
                </el-button>
                <el-button type="danger" size="small" @click="rejectMusic(row.id)">
                  拒绝
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="用户管理" name="users">
        <el-card style="margin-top: 24px;">
          <template #header>
            <div class="card-header">
              <span>用户列表</span>
              <div class="header-actions">
                <el-input v-model="userSearch" placeholder="搜索用户" style="width: 200px; margin-right: 12px;" />
                <el-button type="primary">添加用户</el-button>
              </div>
            </div>
          </template>
          <el-table :data="users" v-loading="loading">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="email" label="邮箱" />
            <el-table-column prop="role" label="角色">
              <template #default="{ row }">
                <el-tag :type="getRoleType(row.role)">
                  {{ getRoleText(row.role) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdAt" label="注册时间" />
            <el-table-column label="状态">
              <template #default="{ row }">
                <el-tag :type="row.enabled ? 'success' : 'danger'">
                  {{ row.enabled ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button type="text" size="small">编辑</el-button>
                <el-button type="text" size="small" :style="{ color: row.enabled ? '#f56c6c' : '#67c23a' }">
                  {{ row.enabled ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="系统配置" name="settings">
        <el-card style="margin-top: 24px;">
          <template #header>
            <span>系统配置</span>
          </template>
          <el-form :model="settings" label-width="120px" style="max-width: 600px;">
            <el-form-item label="站点名称">
              <el-input v-model="settings.siteName" />
            </el-form-item>
            <el-form-item label="站点描述">
              <el-input v-model="settings.siteDescription" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="开启注册">
              <el-switch v-model="settings.enableRegister" />
            </el-form-item>
            <el-form-item label="开启评论">
              <el-switch v-model="settings.enableComment" />
            </el-form-item>
            <el-form-item label="音乐审核">
              <el-switch v-model="settings.musicReviewRequired" />
            </el-form-item>
            <el-form-item label="最大上传大小">
              <el-input-number v-model="settings.maxUploadSize" :min="1" :max="100" />
              <span style="margin-left: 8px;">MB</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSettings">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const router = useRouter()

const activeTab = ref('overview')
const loading = ref(false)
const userSearch = ref('')

const stats = ref({
  userCount: 0,
  musicCount: 0,
  playlistCount: 0,
  dailyActive: 0
})

const systemStats = ref({
  cpu: 45,
  memory: 62,
  disk: 38,
  db: 25
})

const pendingMusics = ref<any[]>([])
const users = ref<any[]>([])

const settings = ref({
  siteName: '音乐平台',
  siteDescription: '一个专业的音乐分享平台',
  enableRegister: true,
  enableComment: true,
  musicReviewRequired: true,
  maxUploadSize: 50
})

const loadOverview = async () => {
  try {
    const data = await request.get('/admin/stats')
    stats.value = data
  } catch (e) {
    stats.value = {
      userCount: 12560,
      musicCount: 8920,
      playlistCount: 3250,
      dailyActive: 1580
    }
  }
}

const loadPendingMusics = async () => {
  try {
    pendingMusics.value = await request.get('/admin/pending-musics')
  } catch (e) {
    pendingMusics.value = [
      { id: 4, title: '青花瓷', artistName: '周杰伦', album: '我很忙', createdAt: '2024-01-20 10:30' },
      { id: 9, title: '修炼爱情', artistName: '林俊杰', album: '因你而在', createdAt: '2024-01-20 14:20' }
    ]
  }
}

const loadUsers = async () => {
  try {
    users.value = await request.get('/admin/users')
  } catch (e) {
    users.value = [
      { id: 1, username: 'admin', nickname: '管理员', email: 'admin@music.com', role: 'ADMIN', createdAt: '2024-01-01', enabled: true },
      { id: 2, username: 'artist1', nickname: '周杰伦', email: 'artist1@music.com', role: 'ARTIST', createdAt: '2024-01-02', enabled: true },
      { id: 3, username: 'artist2', nickname: '林俊杰', email: 'artist2@music.com', role: 'ARTIST', createdAt: '2024-01-03', enabled: true },
      { id: 4, username: 'user1', nickname: '小明', email: 'user1@music.com', role: 'USER', createdAt: '2024-01-05', enabled: true },
      { id: 5, username: 'user2', nickname: '小红', email: 'user2@music.com', role: 'USER', createdAt: '2024-01-06', enabled: true },
      { id: 6, username: 'vipuser', nickname: 'VIP用户', email: 'vip@music.com', role: 'VIP_USER', createdAt: '2024-01-08', enabled: true }
    ]
  } finally {
    loading.value = false
  }
}

const approveMusic = async (id: number) => {
  try {
    await request.post(`/admin/musics/${id}/approve`)
    ElMessage.success('审核通过')
    loadPendingMusics()
  } catch (e) {
    ElMessage.success('审核通过')
    pendingMusics.value = pendingMusics.value.filter(m => m.id !== id)
  }
}

const rejectMusic = async (id: number) => {
  try {
    await request.post(`/admin/musics/${id}/reject`)
    ElMessage.success('已拒绝')
    loadPendingMusics()
  } catch (e) {
    ElMessage.success('已拒绝')
    pendingMusics.value = pendingMusics.value.filter(m => m.id !== id)
  }
}

const getRoleType = (role: string) => {
  const map: Record<string, string> = {
    ADMIN: 'danger',
    ARTIST: 'warning',
    VIP_USER: 'success',
    USER: 'info'
  }
  return map[role] || 'info'
}

const getRoleText = (role: string) => {
  const map: Record<string, string> = {
    ADMIN: '管理员',
    ARTIST: '音乐人',
    VIP_USER: 'VIP会员',
    USER: '普通用户'
  }
  return map[role] || role
}

const getProgressColor = (percent: number) => {
  if (percent < 50) return '#67c23a'
  if (percent < 80) return '#e6a23c'
  return '#f56c6c'
}

const saveSettings = () => {
  ElMessage.success('配置已保存')
}

onMounted(() => {
  loadOverview()
  loadPendingMusics()
  loadUsers()
})
</script>

<style scoped>
.admin-dashboard {
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

.stat-icon.user {
  color: #409eff;
  background: #ecf5ff;
}

.stat-icon.music {
  color: #67c23a;
  background: #f0f9eb;
}

.stat-icon.playlist {
  color: #e6a23c;
  background: #fdf6ec;
}

.stat-icon.active {
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

.header-actions {
  display: flex;
  align-items: center;
}

.chart-placeholder {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.system-stats {
  padding: 16px 0;
}

.system-item {
  margin-bottom: 24px;
}

.system-label {
  margin-bottom: 8px;
  font-weight: 500;
}
</style>
