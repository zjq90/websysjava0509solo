<template>
  <div class="records">
    <div class="page-header">
      <h2>历史记录查询</h2>
    </div>

    <div class="filter-form">
      <el-form :inline="true" :model="filterForm" label-width="80px">
        <el-form-item label="车牌号">
          <el-input v-model="filterForm.plateNumber" placeholder="请输入车牌号" clearable />
        </el-form-item>
        <el-form-item label="摄像头">
          <el-select v-model="filterForm.cameraId" placeholder="请选择摄像头" clearable>
            <el-option
              v-for="camera in cameras"
              :key="camera.cameraId"
              :label="camera.name"
              :value="camera.cameraId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="异常类型">
          <el-select v-model="filterForm.anomalyType" placeholder="请选择异常类型" clearable>
            <el-option label="超速行驶" value="超速行驶" />
            <el-option label="闯红灯" value="闯红灯" />
            <el-option label="违章停车" value="违章停车" />
            <el-option label="遮挡号牌" value="遮挡号牌" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryRecordsData">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetFilter">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button v-if="isAdmin" type="success" @click="showExportDialog">
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" stripe v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="plateNumber" label="车牌号" width="120">
          <template #default="{ row }">
            <span class="plate-number">{{ row.plateNumber }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="passTime" label="通过时间" width="180" />
        <el-table-column prop="cameraName" label="摄像头" width="150" />
        <el-table-column prop="confidence" label="置信度" width="120">
          <template #default="{ row }">
            <span :class="row.confidence < 90 ? 'confidence-low' : 'confidence-high'">
              <el-icon v-if="row.confidence < 90" size="16"><WarningFilled /></el-icon>
              {{ row.confidence?.toFixed(2) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="anomalyType" label="异常类型" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.anomalyType" type="danger" size="small">
              {{ row.anomalyType }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="图片" width="120">
          <template #default="{ row }">
            <img
              :src="row.imageUrl"
              class="image-thumbnail"
              @click="showImagePreview(row.imageUrl)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="addToBlacklist(row)">
              加入黑名单
            </el-button>
            <el-button link type="danger" size="small" @click="deleteRecordData(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="queryRecordsData"
        @current-change="queryRecordsData"
      />
    </div>

    <el-dialog v-model="imageDialogVisible" title="图片预览" width="70%">
      <div class="modal-image-container">
        <img
          :src="previewImageUrl"
          class="modal-image"
          :style="{ transform: `scale(${imageScale}) rotate(${imageRotation}deg)` }"
        />
        <div class="image-controls">
          <el-button @click="zoomIn">
            <el-icon><ZoomIn /></el-icon>
          </el-button>
          <el-button @click="zoomOut">
            <el-icon><ZoomOut /></el-icon>
          </el-button>
          <el-button @click="rotateLeft">
            <el-icon><RefreshLeft /></el-icon>
          </el-button>
          <el-button @click="rotateRight">
            <el-icon><RefreshRight /></el-icon>
          </el-button>
          <el-button @click="resetImage">重置</el-button>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="blacklistDialogVisible" title="加入黑名单" width="500px">
      <el-form :model="blacklistForm" label-width="100px">
        <el-form-item label="车牌号">
          <el-input v-model="blacklistForm.plateNumber" readonly />
        </el-form-item>
        <el-form-item label="原因">
          <el-input v-model="blacklistForm.reason" type="textarea" :rows="3" placeholder="请输入原因" />
        </el-form-item>
        <el-form-item label="过期时间">
          <el-date-picker
            v-model="blacklistForm.expireAt"
            type="datetime"
            placeholder="选择过期时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="blacklistDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAddBlacklist">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="exportDialogVisible" title="导出数据" width="400px">
      <div style="text-align: center; padding: 20px;">
        <el-button type="primary" size="large" @click="exportData('csv')">
          <el-icon><Document /></el-icon>
          导出 CSV
        </el-button>
        <el-button type="success" size="large" @click="exportData('excel')" style="margin-left: 20px;">
          <el-icon><Grid /></el-icon>
          导出 Excel
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryRecords, deleteRecord, getAllCameras, addBlacklist, exportCsv, exportExcel } from '../api'
import dayjs from 'dayjs'

const isAdmin = ref(true)
const loading = ref(false)
const tableData = ref([])
const cameras = ref([])

const filterForm = reactive({
  plateNumber: '',
  cameraId: '',
  anomalyType: ''
})

const dateRange = ref([])

const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

const imageDialogVisible = ref(false)
const previewImageUrl = ref('')
const imageScale = ref(1)
const imageRotation = ref(0)

const blacklistDialogVisible = ref(false)
const blacklistForm = reactive({
  plateNumber: '',
  reason: '',
  expireAt: null,
  evidenceImage: ''
})

const exportDialogVisible = ref(false)
const currentFilter = ref({})

const loadCameras = async () => {
  try {
    cameras.value = await getAllCameras()
  } catch (error) {
    console.error('加载摄像头失败:', error)
  }
}

const queryRecordsData = async () => {
  loading.value = true
  try {
    const params = {
      plateNumber: filterForm.plateNumber || null,
      cameraId: filterForm.cameraId || null,
      anomalyType: filterForm.anomalyType || null,
      startTime: dateRange.value?.[0] ? dayjs(dateRange.value[0]).toISOString() : null,
      endTime: dateRange.value?.[1] ? dayjs(dateRange.value[1]).toISOString() : null,
      page: pagination.page - 1,
      size: pagination.size
    }
    currentFilter.value = params
    const result = await queryRecords(params)
    tableData.value = result.content
    pagination.total = result.totalElements
  } catch (error) {
    console.error('查询记录失败:', error)
  } finally {
    loading.value = false
  }
}

const resetFilter = () => {
  filterForm.plateNumber = ''
  filterForm.cameraId = ''
  filterForm.anomalyType = ''
  dateRange.value = []
  pagination.page = 1
  queryRecordsData()
}

const showImagePreview = (url) => {
  previewImageUrl.value = url
  imageScale.value = 1
  imageRotation.value = 0
  imageDialogVisible.value = true
}

const zoomIn = () => {
  imageScale.value = Math.min(imageScale.value + 0.2, 3)
}

const zoomOut = () => {
  imageScale.value = Math.max(imageScale.value - 0.2, 0.5)
}

const rotateLeft = () => {
  imageRotation.value -= 90
}

const rotateRight = () => {
  imageRotation.value += 90
}

const resetImage = () => {
  imageScale.value = 1
  imageRotation.value = 0
}

const addToBlacklist = (row) => {
  blacklistForm.plateNumber = row.plateNumber
  blacklistForm.reason = ''
  blacklistForm.expireAt = dayjs().add(30, 'day').format('YYYY-MM-DDTHH:mm:ss')
  blacklistForm.evidenceImage = row.imageUrl
  blacklistDialogVisible.value = true
}

const confirmAddBlacklist = async () => {
  try {
    await addBlacklist(blacklistForm)
    ElMessage.success('已加入黑名单')
    blacklistDialogVisible.value = false
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const deleteRecordData = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteRecord(row.id)
    ElMessage.success('删除成功')
    queryRecordsData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const showExportDialog = () => {
  exportDialogVisible.value = true
}

const exportData = async (type) => {
  try {
    const params = { ...currentFilter.value, page: 0, size: 10000 }
    let data
    let filename
    let mimeType

    if (type === 'csv') {
      data = await exportCsv(params)
      filename = `records_${dayjs().format('YYYYMMDD_HHmmss')}.csv`
      mimeType = 'text/csv;charset=utf-8'
    } else {
      data = await exportExcel(params)
      filename = `records_${dayjs().format('YYYYMMDD_HHmmss')}.xlsx`
      mimeType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    }

    const blob = new Blob([data], { type: mimeType })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = filename
    link.click()
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
    exportDialogVisible.value = false
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  loadCameras()
  queryRecordsData()
})
</script>

<style scoped>
.plate-number {
  font-family: monospace;
  font-weight: bold;
  color: #409eff;
}
</style>
