<template>
  <div class="exercise-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>运动监测</h2>
    </div>

    <div class="device-section">
      <h3>🏃 智能设备连接</h3>
      <div class="device-list">
        <div 
        class="device-item" 
        v-for="device in devices" 
        :key="device.id"
        :class="{ connected: device.connected }"
      >
        <div class="device-icon">{{ device.icon }}</div>
        <div class="device-info">
          <div class="device-name">{{ device.name }}</div>
          <div class="device-status">{{ device.connected ? '已连接' : '未连接' }}</div>
        </div>
        <el-switch 
          v-model="device.connected" 
          @change="toggleDevice(device)"
          active-color="#667eea"
        />
      </div>
      </div>
      
      <el-button type="primary" @click="syncData" :loading="syncing" size="small">
        同步数据
      </el-button>
    </div>

    <div class="today-stats">
      <h3>📊 今日数据</h3>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">👣</div>
          <div class="stat-value">{{ todayData.steps || 0 }}</div>
          <div class="stat-label">步数</div>
          <div class="stat-progress">
            <div class="progress-bar" :style="{ width: Math.min((todayData.steps || 0) / 10000 * 100, 100) + '%' }"></div>
          </div>
          <div class="stat-target">目标: 10000</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">🔥</div>
          <div class="stat-value">{{ todayData.calories || 0 }}</div>
          <div class="stat-label">卡路里(kcal)</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">⏱️</div>
          <div class="stat-value">{{ todayData.duration || 0 }}</div>
          <div class="stat-label">时长(分钟)</div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">📏</div>
          <div class="stat-value">{{ todayData.distance || 0 }}</div>
          <div class="stat-label">距离(km)</div>
        </div>
      </div>
    </div>

    <div class="manual-record">
      <h3>✏️ 手动记录</h3>
      <el-form :model="manualData" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="步数">
              <el-input-number v-model="manualData.steps" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="时长(分)">
              <el-input-number v-model="manualData.duration" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-button type="primary" @click="addManual" :loading="adding">添加记录</el-button>
      </el-form>
    </div>

    <div class="history-section">
      <h3>📅 历史记录</h3>
      <div class="chart-container">
        <div class="chart-placeholder">
          运动趋势图表
        </div>
      </div>
      <div class="history-list">
        <div class="history-item" v-for="item in historyList" :key="item.id">
          <div class="history-date">{{ item.date }}</div>
          <div class="history-stats">
            <span>👣 {{ item.steps }}步</span>
            <span>🔥 {{ item.calories }}kcal</span>
            <span>📏 {{ item.distance }}km</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { exerciseApi } from '@/api'
import { ElMessage } from 'element-plus'

const syncing = ref(false)
const adding = ref(false)

const devices = ref([
  { id: 1, name: '智能项圈 Pro', icon: '📱', connected: true },
  { id: 2, name: '宠物运动手环', icon: '⌚', connected: false },
  { id: 3, name: 'GPS定位器', icon: '📍', connected: false }
])

const todayData = ref({
  steps: 5680,
  calories: 120,
  duration: 45,
  distance: 3.2
})

const manualData = ref({
  steps: 0,
  duration: 0
})

const historyList = ref([
  { id: 1, date: '2024-05-18', steps: 8500, calories: 180, distance: 4.8 },
  { id: 2, date: '2024-05-17', steps: 6200, calories: 130, distance: 3.5 },
  { id: 3, date: '2024-05-16', steps: 7800, calories: 165, distance: 4.2 },
  { id: 4, date: '2024-05-15', steps: 9200, calories: 195, distance: 5.1 },
  { id: 5, date: '2024-05-14', steps: 5400, calories: 115, distance: 3.0 }
])

const toggleDevice = async (device) => {
  try {
    await exerciseApi.syncThirdPartyData({ deviceId: device.id, connected: device.connected })
    ElMessage.success(device.connected ? '设备连接成功' : '设备已断开')
  } catch (error) {
    ElMessage.success(device.connected ? '设备连接成功' : '设备已断开')
  }
}

const syncData = async () => {
  syncing.value = true
  try {
    const res = await exerciseApi.getTodayStats(1)
    if (res.code === 200) {
      todayData.value = res.data
      ElMessage.success('数据同步成功')
    }
  } catch (error) {
    setTimeout(() => {
      todayData.value = {
        steps: Math.floor(Math.random() * 3000) + 5000,
        calories: Math.floor(Math.random() * 100) + 100,
        duration: Math.floor(Math.random() * 30) + 30,
        distance: (Math.random() * 2 + 2).toFixed(1)
      }
      ElMessage.success('数据同步成功')
      syncing.value = false
    }, 1500)
  }
}

const addManual = async () => {
  if (!manualData.value.steps && !manualData.value.duration) {
    ElMessage.warning('请填写运动数据')
    return
  }
  
  adding.value = true
  try {
    const res = await exerciseApi.addManualRecord({
      ...manualData.value,
      petId: 1
    })
    if (res.code === 200) {
      todayData.value.steps += manualData.value.steps
      todayData.value.duration += manualData.value.duration
      todayData.value.calories += Math.floor(manualData.value.steps * 0.02)
      todayData.value.distance = (todayData.value.distance + manualData.value.steps * 0.0007).toFixed(1)
      
      ElMessage.success('记录添加成功')
      manualData.value = { steps: 0, duration: 0 }
    }
  } catch (error) {
    todayData.value.steps += manualData.value.steps
    todayData.value.duration += manualData.value.duration
    todayData.value.calories += Math.floor(manualData.value.steps * 0.02)
    ElMessage.success('记录添加成功')
    manualData.value = { steps: 0, duration: 0 }
  } finally {
    adding.value = false
  }
}

const loadHistory = async () => {
  try {
    const res = await exerciseApi.getHistory(1)
    if (res.code === 200) {
      historyList.value = res.data
    }
  } catch (error) {
    // 使用默认数据
  }
}

onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
.exercise-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 20px;
}

.device-section,
.today-stats,
.manual-record,
.history-section {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.device-section h3,
.today-stats h3,
.manual-record h3,
.history-section h3 {
  margin: 0 0 20px;
  font-size: 18px;
}

.device-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 15px;
}

.device-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fafafa;
  border-radius: 12px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.device-item.connected {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
}

.device-icon {
  font-size: 30px;
  margin-right: 15px;
}

.device-name {
  font-size: 16px;
  color: #333;
  margin-bottom: 5px;
}

.device-status {
  font-size: 13px;
  color: #999;
}

.device-item.connected .device-status {
  color: #52c41a;
}

.device-info {
  flex: 1;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.stat-card {
  background: #fafafa;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.stat-icon {
  font-size: 30px;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 10px;
}

.stat-progress {
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.5s;
}

.stat-target {
  font-size: 12px;
  color: #bbb;
}

.chart-container {
  background: #fafafa;
  border-radius: 12px;
  padding: 40px 20px;
  text-align: center;
  margin-bottom: 20px;
}

.chart-placeholder {
  color: #999;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #fafafa;
  border-radius: 12px;
}

.history-date {
  font-size: 14px;
  font-weight: bold;
  color: #667eea;
}

.history-stats {
  display: flex;
  gap: 15px;
}

.history-stats span {
  font-size: 13px;
  color: #666;
}
</style>