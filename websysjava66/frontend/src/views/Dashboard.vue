<template>
  <div class="dashboard">
    <div class="page-header">
      <h2>实时监控大屏</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stats-card blue">
          <div class="stats-icon"><el-icon size="40"><VideoCamera /></el-icon></div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.onlineCameras }} / {{ stats.totalCameras }}</div>
            <div class="stats-label">在线摄像头 / 总摄像头</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stats-card green">
          <div class="stats-icon"><el-icon size="40"><Van /></el-icon></div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.todayRecognitions }}</div>
            <div class="stats-label">今日识别总量</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stats-card orange">
          <div class="stats-icon"><el-icon size="40"><Warning /></el-icon></div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.anomalyCount }}</div>
            <div class="stats-label">今日异常事件</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stats-card purple">
          <div class="stats-icon"><el-icon size="40"><User /></el-icon></div>
          <div class="stats-info">
            <div class="stats-value">{{ stats.activeBlacklist }}</div>
            <div class="stats-label">生效黑名单</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <div class="map-container card-shadow">
          <div class="map-header">
            <h3>摄像头分布地图</h3>
          </div>
          <div class="map-content">
            <div class="map-placeholder">
              <el-icon size="80" color="#409EFF"><Location /></el-icon>
              <p>高德地图集成区域</p>
              <p class="map-hint">点击地图上的摄像头图标可查看实时流</p>
            </div>
            <div class="camera-markers">
              <div
                v-for="camera in cameras"
                :key="camera.id"
                class="camera-marker"
                :class="{ offline: !camera.isOnline }"
                :style="{ left: getMarkerX(camera) + '%', top: getMarkerY(camera) + '%' }"
                @click="showCameraStream(camera)"
              >
                <el-icon size="24"><VideoCamera /></el-icon>
                <el-tooltip :content="camera.name" placement="top">
                  <div class="marker-label">{{ camera.cameraId }}</div>
                </el-tooltip>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="quick-actions card-shadow">
          <div class="actions-header">
            <h3>快捷操作</h3>
          </div>
          <div class="actions-content">
            <el-button type="primary" size="large" class="action-btn" @click="toggleNightMode">
              <el-icon><Moon /></el-icon>
              <span>一键切换夜间模式</span>
            </el-button>
            <el-button type="danger" size="large" class="action-btn" @click="showBroadcastDialog">
              <el-icon><Bell /></el-icon>
              <span>紧急广播</span>
            </el-button>
          </div>
          <div class="camera-list">
            <h4>摄像头状态</h4>
            <el-scrollbar height="250px">
              <div
                v-for="camera in cameras"
                :key="camera.id"
                class="camera-item"
                :class="{ offline: !camera.isOnline }"
              >
                <el-icon><VideoCamera /></el-icon>
                <span class="camera-name">{{ camera.name }}</span>
                <span class="camera-status">{{ camera.isOnline ? '在线' : '离线' }}</span>
              </div>
            </el-scrollbar>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog v-model="streamDialogVisible" title="实时视频流" width="70%">
      <div class="stream-container">
        <div class="stream-placeholder">
          <el-icon size="100" color="#909399"><VideoPlay /></el-icon>
          <p>实时流播放区域</p>
          <p class="stream-info">摄像头: {{ selectedCamera?.name }}</p>
          <p class="stream-info">地址: {{ selectedCamera?.streamUrl }}</p>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="broadcastDialogVisible" title="发送紧急广播" width="500px">
      <el-form label-width="80px">
        <el-form-item label="广播内容">
          <el-input
            v-model="broadcastContent"
            type="textarea"
            :rows="4"
            placeholder="请输入广播内容"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="broadcastDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sendBroadcastMessage">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getDashboardStats, getCameras, setAllNightMode, sendBroadcast } from '../api'

const stats = ref({
  onlineCameras: 0,
  totalCameras: 0,
  todayRecognitions: 0,
  anomalyCount: 0,
  activeBlacklist: 0
})

const cameras = ref([])
const nightMode = ref(false)
const streamDialogVisible = ref(false)
const broadcastDialogVisible = ref(false)
const selectedCamera = ref(null)
const broadcastContent = ref('')

const loadStats = async () => {
  try {
    stats.value = await getDashboardStats()
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadCameras = async () => {
  try {
    cameras.value = await getCameras()
  } catch (error) {
    console.error('加载摄像头失败:', error)
  }
}

const getMarkerX = (camera) => {
  const baseX = ((camera.longitude - 116.35) / 0.1) * 100
  return Math.max(5, Math.min(90, baseX))
}

const getMarkerY = (camera) => {
  const baseY = ((camera.latitude - 39.86) / 0.1) * 100
  return Math.max(5, Math.min(85, baseY))
}

const showCameraStream = (camera) => {
  selectedCamera.value = camera
  streamDialogVisible.value = true
}

const toggleNightMode = async () => {
  try {
    nightMode.value = !nightMode.value
    await setAllNightMode(nightMode.value)
    ElMessage.success(nightMode.value ? '已开启所有摄像头夜间模式' : '已关闭所有摄像头夜间模式')
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const showBroadcastDialog = () => {
  broadcastContent.value = ''
  broadcastDialogVisible.value = true
}

const sendBroadcastMessage = async () => {
  if (!broadcastContent.value.trim()) {
    ElMessage.warning('请输入广播内容')
    return
  }
  try {
    await sendBroadcast(broadcastContent.value, 'admin')
    ElMessage.success('广播发送成功')
    broadcastDialogVisible.value = false
  } catch (error) {
    ElMessage.error('发送失败')
  }
}

onMounted(() => {
  loadStats()
  loadCameras()
})
</script>

<style scoped>
.dashboard {
  height: 100%;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  display: flex;
  align-items: center;
  gap: 20px;
  height: 120px;
}

.stats-icon {
  opacity: 0.9;
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stats-label {
  font-size: 14px;
  opacity: 0.9;
}

.map-container,
.quick-actions {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.map-header,
.actions-header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.map-header h3,
.actions-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.map-content {
  height: 400px;
  position: relative;
  background: linear-gradient(135deg, #e8f4ff 0%, #f0f5fa 100%);
}

.map-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.map-placeholder p {
  margin: 5px 0;
}

.map-hint {
  font-size: 12px;
  color: #c0c4cc;
}

.camera-markers {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.camera-marker {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  color: #67c23a;
  pointer-events: auto;
  transition: transform 0.2s;
}

.camera-marker:hover {
  transform: scale(1.2);
}

.camera-marker.offline {
  color: #909399;
}

.marker-label {
  font-size: 10px;
  background: rgba(255, 255, 255, 0.9);
  padding: 1px 4px;
  border-radius: 2px;
  white-space: nowrap;
}

.actions-content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.action-btn {
  width: 100%;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.camera-list {
  padding: 0 20px 20px;
}

.camera-list h4 {
  margin: 0 0 10px;
  font-size: 14px;
  color: #606266;
}

.camera-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 5px;
  background: #f5f7fa;
}

.camera-item.offline {
  opacity: 0.5;
}

.camera-name {
  flex: 1;
  font-size: 14px;
}

.camera-status {
  font-size: 12px;
  color: #67c23a;
}

.camera-item.offline .camera-status {
  color: #909399;
}

.stream-container {
  width: 100%;
  height: 400px;
  background: #000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stream-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #606266;
}

.stream-info {
  margin: 5px 0;
  font-size: 14px;
}
</style>
