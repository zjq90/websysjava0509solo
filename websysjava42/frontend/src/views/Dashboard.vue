<template>
  <div class="dashboard">
    <h2>欢迎使用裁判管理系统</h2>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409EFF;">
            <el-icon :size="30"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ refereeCount }}</div>
            <div class="stat-label">裁判数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67C23A;">
            <el-icon :size="30"><Trophy /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ athleteCount }}</div>
            <div class="stat-label">运动员数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #E6A23C;">
            <el-icon :size="30"><Tickets /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ competitionCount }}</div>
            <div class="stat-label">比赛数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #F56C6C;">
            <el-icon :size="30"><Star /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ scoreCount }}</div>
            <div class="stat-label">评分记录</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card title="最近比赛">
          <el-table :data="recentCompetitions" border>
            <el-table-column prop="competitionNo" label="比赛编号" width="120" />
            <el-table-column prop="name" label="比赛名称" />
            <el-table-column prop="event" label="比赛项目" width="120" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="待办事项">
          <el-list>
            <el-list-item v-if="pendingAuditCount > 0">
              <el-icon style="color: #F56C6C; margin-right: 10px;"><Warning /></el-icon>
              有 {{ pendingAuditCount }} 条评分待审核
            </el-list-item>
            <el-list-item>
              <el-icon style="color: #409EFF; margin-right: 10px;"><InfoFilled /></el-icon>
              系统运行正常
            </el-list-item>
          </el-list>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'

const refereeCount = ref(0)
const athleteCount = ref(0)
const competitionCount = ref(0)
const scoreCount = ref(0)
const pendingAuditCount = ref(0)
const recentCompetitions = ref([])

const getStatusType = (status) => {
  const types = ['info', 'primary', 'success', 'danger']
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = ['未开始', '进行中', '已结束', '已取消']
  return texts[status] || '未知'
}

onMounted(async () => {
  try {
    const [referees, athletes, competitions, scores, pending] = await Promise.all([
      api.getReferees(),
      api.getAthletes(),
      api.getCompetitions(),
      api.getScores(),
      api.getPendingAuditScores()
    ])
    refereeCount.value = referees.data?.length || 0
    athleteCount.value = athletes.data?.length || 0
    competitionCount.value = competitions.data?.length || 0
    scoreCount.value = scores.data?.length || 0
    pendingAuditCount.value = pending.data?.length || 0
    recentCompetitions.value = competitions.data?.slice(0, 5) || []
  } catch (e) {
    console.log('数据加载失败')
  }
})
</script>

<style scoped>
.dashboard h2 {
  color: #333;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}
</style>
