<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">数据总览</div>
      <div class="header-actions">
        <el-select v-model="selectedConfig" @change="handleConfigChange" style="width: 180px; margin-right: 10px;">
          <el-option v-for="config in configs" :key="config.id" :label="config.name" :value="config.id" />
        </el-select>
        <el-button type="primary" @click="showSaveDialog">保存布局</el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <draggable v-model="widgets" item-key="id" class="widget-container" handle=".drag-handle">
      <template #item="{ element }">
        <div class="widget-item" :style="getWidgetStyle(element)">
          <div class="widget-header">
            <span class="drag-handle">
              <el-icon><Rank /></el-icon>
            </span>
            <span class="widget-title">{{ element.title }}</span>
          </div>
          <div class="widget-content">
            <StatCards v-if="element.id === 'stats'" :stats="stats" />
            <TrendChart v-else-if="element.id === 'trend7'" :title="'近7天趋势'" :days="7" />
            <TrendChart v-else-if="element.id === 'trend30'" :title="'近30天趋势'" :days="30" />
          </div>
        </div>
      </template>
    </draggable>

    <el-dialog v-model="saveDialogVisible" title="保存看板方案" width="400px">
      <el-form label-width="80px">
        <el-form-item label="方案名称">
          <el-input v-model="newConfig.name" placeholder="请输入方案名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="newConfig.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="newConfig.isDefault">设为默认</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="saveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveConfig">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import draggable from 'vuedraggable'
import StatCards from './components/StatCards.vue'
import TrendChart from './components/TrendChart.vue'
import { getTodayStats, getDashboardConfigs, saveDashboardConfig, setDefaultDashboard, deleteDashboardConfig } from '@/api/dashboard'

const stats = ref(null)
const configs = ref([])
const selectedConfig = ref(null)
const saveDialogVisible = ref(false)
const newConfig = reactive({
  name: '',
  description: '',
  isDefault: false
})

const widgets = ref([
  { id: 'stats', title: '核心指标', x: 0, y: 0, w: 12, h: 4 },
  { id: 'trend7', title: '近7天趋势', x: 0, y: 4, w: 6, h: 8 },
  { id: 'trend30', title: '近30天趋势', x: 6, y: 4, w: 6, h: 8 }
])

const loadStats = async () => {
  stats.value = await getTodayStats()
}

const loadConfigs = async () => {
  configs.value = await getDashboardConfigs()
  const defaultConfig = configs.value.find(c => c.isDefault === 1)
  if (defaultConfig) {
    selectedConfig.value = defaultConfig.id
    if (defaultConfig.layoutConfig) {
      widgets.value = JSON.parse(defaultConfig.layoutConfig)
    }
  }
}

const refreshData = () => {
  loadStats()
}

const handleConfigChange = async (id) => {
  const config = configs.value.find(c => c.id === id)
  if (config && config.layoutConfig) {
    widgets.value = JSON.parse(config.layoutConfig)
  }
}

const showSaveDialog = () => {
  newConfig.name = ''
  newConfig.description = ''
  newConfig.isDefault = false
  saveDialogVisible.value = true
}

const saveConfig = async () => {
  if (!newConfig.name.trim()) {
    ElMessage.warning('请输入方案名称')
    return
  }
  const config = {
    name: newConfig.name,
    description: newConfig.description,
    layoutConfig: JSON.stringify(widgets.value),
    isDefault: newConfig.isDefault ? 1 : 0
  }
  const saved = await saveDashboardConfig(config)
  if (saved && newConfig.isDefault) {
    await setDefaultDashboard(saved.id)
  }
  await loadConfigs()
  saveDialogVisible.value = false
  ElMessage.success('保存成功')
}

const getWidgetStyle = (widget) => {
  return {
    'grid-column': `span ${widget.w}`,
    'grid-row': `span ${widget.h}`
  }
}

onMounted(() => {
  loadStats()
  loadConfigs()
})
</script>

<style scoped lang="scss">
.header-actions {
  display: flex;
  align-items: center;
}

.widget-container {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  grid-auto-rows: 40px;
  gap: 16px;
}

.widget-item {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.widget-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;

  .drag-handle {
    cursor: move;
    color: #909399;
    display: flex;
    align-items: center;
  }

  .widget-title {
    font-weight: 600;
    color: #303133;
  }
}

.widget-content {
  flex: 1;
  padding: 16px;
  overflow: auto;
}
</style>
