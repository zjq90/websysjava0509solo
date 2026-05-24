<template>
  <div class="blacklist">
    <div class="page-header" style="display: flex; justify-content: space-between; align-items: center;">
      <h2>黑名单管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        添加黑名单
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" stripe v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="plateNumber" label="车牌号" width="120">
          <template #default="{ row }">
            <span class="plate-number">{{ row.plateNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="原因" show-overflow-tooltip />
        <el-table-column label="证据图片" width="120">
          <template #default="{ row }">
            <img
              v-if="row.evidenceImage"
              :src="row.evidenceImage"
              class="image-thumbnail"
              @click="showImagePreview(row.evidenceImage)"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="添加时间" width="180" />
        <el-table-column prop="expireAt" label="过期时间" width="180" />
        <el-table-column prop="isActive" label="状态" width="100">
          <template #default="{ row }">
            <span :class="row.isActive ? 'active-badge' : 'inactive-badge'">
              {{ row.isActive ? '生效中' : '已解除' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="removeReason" label="解除原因" show-overflow-tooltip>
          <template #default="{ row }">
            <span>{{ row.removeReason || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.isActive"
              link
              type="warning"
              size="small"
              @click="showRemoveDialog(row)"
            >
              解除
            </el-button>
            <el-button link type="info" size="small" @click="viewDetail(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="addDialogVisible" title="添加黑名单" width="600px">
      <el-form :model="addForm" label-width="100px">
        <el-form-item label="车牌号" required>
          <el-input v-model="addForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="原因">
          <el-input v-model="addForm.reason" type="textarea" :rows="3" placeholder="请输入原因" />
        </el-form-item>
        <el-form-item label="证据图片">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleImageChange"
          >
            <div v-if="addForm.evidenceImage" class="image-preview">
              <img :src="addForm.evidenceImage" class="preview-thumb" />
              <el-icon class="preview-close" size="16" @click.stop="clearImage"><Close /></el-icon>
            </div>
            <el-icon v-else size="40"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="addForm.expireAt"
            type="datetime"
            placeholder="选择过期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAdd">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="removeDialogVisible" title="解除黑名单" width="500px">
      <el-form label-width="100px">
        <el-form-item label="车牌号">
          <el-input :model-value="selectedRecord?.plateNumber" readonly />
        </el-form-item>
        <el-form-item label="解除原因" required>
          <el-input
            v-model="removeReason"
            type="textarea"
            :rows="3"
            placeholder="请输入解除原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="removeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRemove">确认解除</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="黑名单详情" width="500px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="车牌号">{{ selectedRecord?.plateNumber }}</el-descriptions-item>
        <el-descriptions-item label="原因">{{ selectedRecord?.reason }}</el-descriptions-item>
        <el-descriptions-item label="添加时间">{{ selectedRecord?.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="过期时间">{{ selectedRecord?.expireAt }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span :class="selectedRecord?.isActive ? 'active-badge' : 'inactive-badge'">
            {{ selectedRecord?.isActive ? '生效中' : '已解除' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="解除原因" v-if="!selectedRecord?.isActive">
          {{ selectedRecord?.removeReason }}
        </el-descriptions-item>
        <el-descriptions-item label="证据图片" v-if="selectedRecord?.evidenceImage">
          <img :src="selectedRecord?.evidenceImage" style="max-width: 300px;" />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="imageDialogVisible" title="图片预览" width="70%">
      <img :src="previewImageUrl" style="width: 100%;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBlacklist, addBlacklist, removeBlacklist } from '../api'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])

const addDialogVisible = ref(false)
const addForm = reactive({
  plateNumber: '',
  reason: '',
  evidenceImage: '',
  expireAt: null,
  createdBy: 'admin'
})

const removeDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const imageDialogVisible = ref(false)
const selectedRecord = ref(null)
const removeReason = ref('')
const previewImageUrl = ref('')

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await getBlacklist()
  } catch (error) {
    console.error('加载黑名单失败:', error)
  } finally {
    loading.value = false
  }
}

const showAddDialog = () => {
  addForm.plateNumber = ''
  addForm.reason = ''
  addForm.evidenceImage = ''
  addForm.expireAt = dayjs().add(30, 'day').format('YYYY-MM-DDTHH:mm:ss')
  addDialogVisible.value = true
}

const handleImageChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    addForm.evidenceImage = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

const clearImage = () => {
  addForm.evidenceImage = ''
}

const confirmAdd = async () => {
  if (!addForm.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  try {
    await addBlacklist(addForm)
    ElMessage.success('添加成功')
    addDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const showRemoveDialog = (row) => {
  selectedRecord.value = row
  removeReason.value = ''
  removeDialogVisible.value = true
}

const confirmRemove = async () => {
  if (!removeReason.value) {
    ElMessage.warning('请输入解除原因')
    return
  }
  try {
    await removeBlacklist(selectedRecord.value.id, removeReason.value)
    ElMessage.success('解除成功')
    removeDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error('解除失败')
  }
}

const viewDetail = (row) => {
  selectedRecord.value = row
  detailDialogVisible.value = true
}

const showImagePreview = (url) => {
  previewImageUrl.value = url
  imageDialogVisible.value = true
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.plate-number {
  font-family: monospace;
  font-weight: bold;
  color: #f56c6c;
}

.upload-demo {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.image-preview {
  position: relative;
  width: 100%;
  height: 100%;
}

.preview-thumb {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-close {
  position: absolute;
  top: 2px;
  right: 2px;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  border-radius: 50%;
  padding: 2px;
}
</style>
