<template>
  <div class="cameras">
    <div class="page-header" style="display: flex; justify-content: space-between; align-items: center;">
      <h2>摄像头管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        添加摄像头
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" stripe v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="cameraId" label="摄像头ID" width="120" />
        <el-table-column prop="name" label="名称" width="150" />
        <el-table-column prop="location" label="位置" show-overflow-tooltip />
        <el-table-column label="坐标" width="200">
          <template #default="{ row }">
            {{ row.longitude?.toFixed(4) }}, {{ row.latitude?.toFixed(4) }}
          </template>
        </el-table-column>
        <el-table-column prop="isOnline" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isOnline ? 'success' : 'info'">
              {{ row.isOnline ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nightMode" label="夜间模式" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.nightMode" @change="toggleNightMode(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="streamUrl" label="流地址" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="viewStream(row)">
              查看
            </el-button>
            <el-button link type="success" size="small" @click="showEditDialog(row)">
              编辑
            </el-button>
            <el-button link type="danger" size="small" @click="deleteCameraData(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑摄像头' : '添加摄像头'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="摄像头ID" required>
          <el-input v-model="form.cameraId" placeholder="请输入摄像头ID" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="名称" required>
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="位置">
          <el-input v-model="form.location" placeholder="请输入位置" />
        </el-form-item>
        <el-form-item label="经度">
          <el-input-number v-model="form.longitude" :precision="6" :step="0.0001" />
        </el-form-item>
        <el-form-item label="纬度">
          <el-input-number v-model="form.latitude" :precision="6" :step="0.0001" />
        </el-form-item>
        <el-form-item label="是否在线">
          <el-switch v-model="form.isOnline" />
        </el-form-item>
        <el-form-item label="夜间模式">
          <el-switch v-model="form.nightMode" />
        </el-form-item>
        <el-form-item label="流地址">
          <el-input v-model="form.streamUrl" placeholder="rtsp://..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSave">确认</el-button>
      </template>
    </el-dialog>

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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllCameras, createCamera, updateCamera, deleteCamera } from '../api'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const streamDialogVisible = ref(false)
const isEdit = ref(false)
const selectedCamera = ref(null)

const form = reactive({
  id: null,
  cameraId: '',
  name: '',
  location: '',
  longitude: 116.397,
  latitude: 39.908,
  isOnline: true,
  nightMode: false,
  streamUrl: ''
})

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await getAllCameras()
  } catch (error) {
    console.error('加载摄像头失败:', error)
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  isEdit.value = false
  form.id = null
  form.cameraId = ''
  form.name = ''
  form.location = ''
  form.longitude = 116.397
  form.latitude = 39.908
  form.isOnline = true
  form.nightMode = false
  form.streamUrl = ''
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  form.id = row.id
  form.cameraId = row.cameraId
  form.name = row.name
  form.location = row.location
  form.longitude = row.longitude
  form.latitude = row.latitude
  form.isOnline = row.isOnline
  form.nightMode = row.nightMode
  form.streamUrl = row.streamUrl
  dialogVisible.value = true
}

const confirmSave = async () => {
  if (!form.cameraId || !form.name) {
    ElMessage.warning('请填写必填项')
    return
  }
  try {
    if (isEdit.value) {
      await updateCamera(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createCamera(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const deleteCameraData = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这个摄像头吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCamera(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const toggleNightMode = async (row) => {
  try {
    await updateCamera(row.id, row)
    ElMessage.success(row.nightMode ? '已开启夜间模式' : '已关闭夜间模式')
  } catch (error) {
    row.nightMode = !row.nightMode
    ElMessage.error('操作失败')
  }
}

const viewStream = (row) => {
  selectedCamera.value = row
  streamDialogVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
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
