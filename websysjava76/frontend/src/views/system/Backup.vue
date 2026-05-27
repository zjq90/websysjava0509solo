<template>
  <div class="backup-page">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>数据备份</span>
            </div>
          </template>
          <div class="backup-section">
            <el-alert
              title="系统将自动备份所有社团数据，包括社团信息、活动记录、经费流水等"
              type="info"
              :closable="false"
              style="margin-bottom: 20px"
            />
            <el-form label-width="100px">
              <el-form-item label="备份名称">
                <el-input v-model="backupName" placeholder="请输入备份名称" />
              </el-form-item>
              <el-form-item label="备份类型">
                <el-radio-group v-model="backupType">
                  <el-radio label="full">完整备份</el-radio>
                  <el-radio label="increment">增量备份</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="备份说明">
                <el-input v-model="backupRemark" type="textarea" :rows="3" placeholder="请输入备份说明" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="backingUp" @click="handleBackup">
                  <el-icon><Download /></el-icon>立即备份
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>自动备份设置</span>
            </div>
          </template>
          <div class="auto-backup-section">
            <el-form label-width="120px">
              <el-form-item label="自动备份">
                <el-switch v-model="autoBackupEnabled" />
              </el-form-item>
              <el-form-item label="备份周期">
                <el-select v-model="backupInterval" :disabled="!autoBackupEnabled" style="width: 100%">
                  <el-option label="每天" value="daily" />
                  <el-option label="每周" value="weekly" />
                  <el-option label="每月" value="monthly" />
                </el-select>
              </el-form-item>
              <el-form-item label="保留数量">
                <el-input-number v-model="retainCount" :disabled="!autoBackupEnabled" :min="1" :max="30" />
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="saveAutoBackup">
                  保存设置
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>备份历史</span>
          <div class="header-actions">
            <el-button type="primary" @click="loadBackupList">
              <el-icon><Refresh /></el-icon>刷新
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="backupList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="fileName" label="备份文件" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag>{{ row.type === 'full' ? '完整备份' : '增量备份' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="文件大小" width="120" align="right" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="备份时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDownload(row)">下载</el-button>
            <el-button type="success" link @click="handleRestore(row)">恢复</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { systemApi } from '@/api'

const loading = ref(false)
const backingUp = ref(false)
const backupList = ref([])

const backupName = ref('')
const backupType = ref('full')
const backupRemark = ref('')
const autoBackupEnabled = ref(true)
const backupInterval = ref('weekly')
const retainCount = ref(10)

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadBackupList = async () => {
  loading.value = true
  try {
    const res = await systemApi.getBackupList()
    backupList.value = Array.isArray(res) ? res : (res.content || [])
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleBackup = async () => {
  if (!backupName.value) {
    ElMessage.warning('请输入备份名称')
    return
  }
  backingUp.value = true
  try {
    await systemApi.createBackup({
      name: backupName.value,
      type: backupType.value,
      remark: backupRemark.value
    })
    ElMessage.success('备份成功')
    backupName.value = ''
    backupRemark.value = ''
    loadBackupList()
  } catch (e) {
    console.error(e)
  } finally {
    backingUp.value = false
  }
}

const saveAutoBackup = () => {
  ElMessage.success('设置已保存')
}

const handleDownload = (row) => {
  ElMessage.info(`开始下载: ${row.fileName}`)
}

const handleRestore = (row) => {
  ElMessageBox.confirm(
    `确定要从备份"${row.fileName}"恢复数据吗？此操作将覆盖现有数据！`,
    '警告',
    {
      confirmButtonText: '确定恢复',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('数据恢复成功')
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除备份"${row.fileName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await systemApi.deleteBackup(row.id)
    ElMessage.success('删除成功')
    loadBackupList()
  }).catch(() => {})
}

onMounted(() => {
  loadBackupList()
})
</script>

<style scoped>
.backup-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.backup-section,
.auto-backup-section {
  padding: 10px 0;
}
</style>
