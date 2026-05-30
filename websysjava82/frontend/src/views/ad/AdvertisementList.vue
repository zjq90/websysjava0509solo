<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">广告管理</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        新增广告
      </el-button>
    </div>

    <div class="filter-bar">
      <el-form :inline="true" :model="filter">
        <el-form-item label="广告位">
          <el-select v-model="filter.adSlotId" placeholder="全部" clearable style="width: 180px">
            <el-option v-for="slot in adSlots" :key="slot.id" :label="slot.name" :value="slot.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filter.active" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card-wrapper">
      <el-table :data="filteredAds" style="width: 100%" border>
        <el-table-column label="素材" width="100">
          <template #default="{ row }">
            <el-image v-if="row.materialUrl" :src="row.materialUrl" class="image-preview" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column label="广告位" width="120">
          <template #default="{ row }">
            {{ getSlotName(row.adSlotId) }}
          </template>
        </el-table-column>
        <el-table-column prop="adType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getAdTypeTag(row.adType)">{{ getAdTypeName(row.adType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="展示时间段" width="250">
          <template #default="{ row }">
            <div v-if="row.startTime && row.endTime">
              {{ formatDate(row.startTime) }} ~ {{ formatDate(row.endTime) }}
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="displayFrequency" label="展示频率" width="120">
          <template #default="{ row }">
            {{ getFrequencyName(row.displayFrequency) }}
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" />
        <el-table-column prop="active" label="状态" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.active" @change="toggleActive(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editData.id ? '编辑广告' : '新增广告'" width="700px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="广告位" prop="adSlotId">
          <el-select v-model="editData.adSlotId" placeholder="请选择广告位" style="width: 100%">
            <el-option v-for="slot in adSlots" :key="slot.id" :label="slot.name" :value="slot.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="editData.title" placeholder="请输入广告标题" />
        </el-form-item>
        <el-form-item label="类型" prop="adType">
          <el-select v-model="editData.adType" placeholder="请选择广告类型" style="width: 100%">
            <el-option label="图片" value="IMAGE" />
            <el-option label="视频" value="VIDEO" />
            <el-option label="H5链接" value="H5" />
          </el-select>
        </el-form-item>
        <el-form-item label="素材链接" prop="materialUrl">
          <el-input v-model="editData.materialUrl" placeholder="请输入素材URL" />
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="editData.jumpUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-date-picker v-model="editData.startTime" type="datetime" placeholder="选择开始时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker v-model="editData.endTime" type="datetime" placeholder="选择结束时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="展示开始时间">
              <el-time-picker v-model="editData.displayStartTime" placeholder="选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="展示结束时间">
              <el-time-picker v-model="editData.displayEndTime" placeholder="选择时间" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="展示频率" prop="displayFrequency">
          <el-select v-model="editData.displayFrequency" placeholder="请选择展示频率" style="width: 100%">
            <el-option label="每次展示" value="ALWAYS" />
            <el-option label="每日一次" value="DAILY_ONCE" />
            <el-option label="每小时一次" value="HOURLY_ONCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="editData.priority" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="editData.active" active-text="启用" inactive-text="禁用" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="modal-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { advertisementApi, adSlotApi } from '../../api'

const advertisements = ref([])
const adSlots = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const filter = reactive({
  adSlotId: null,
  active: null
})

const editData = reactive({
  id: null,
  adSlotId: null,
  title: '',
  adType: 'IMAGE',
  materialUrl: '',
  jumpUrl: '',
  startTime: null,
  endTime: null,
  displayStartTime: null,
  displayEndTime: null,
  displayFrequency: 'ALWAYS',
  priority: 0,
  active: true
})

const rules = {
  adSlotId: [{ required: true, message: '请选择广告位', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  adType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  materialUrl: [{ required: true, message: '请输入素材链接', trigger: 'blur' }],
  displayFrequency: [{ required: true, message: '请选择展示频率', trigger: 'change' }]
}

const filteredAds = computed(() => {
  let result = advertisements.value
  if (filter.adSlotId) {
    result = result.filter(a => a.adSlotId === filter.adSlotId)
  }
  if (filter.active !== null) {
    result = result.filter(a => a.active === filter.active)
  }
  return result
})

const getSlotName = (id) => {
  const slot = adSlots.value.find(s => s.id === id)
  return slot ? slot.name : '-'
}

const getAdTypeName = (type) => {
  const map = { IMAGE: '图片', VIDEO: '视频', H5: 'H5链接' }
  return map[type] || type
}

const getAdTypeTag = (type) => {
  const map = { IMAGE: 'success', VIDEO: 'primary', H5: 'warning' }
  return map[type] || ''
}

const getFrequencyName = (freq) => {
  const map = { ALWAYS: '每次展示', DAILY_ONCE: '每日一次', HOURLY_ONCE: '每小时一次' }
  return map[freq] || freq
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const loadData = async () => {
  const [adRes, slotRes] = await Promise.all([
    advertisementApi.list(),
    adSlotApi.list()
  ])
  advertisements.value = adRes.data || []
  adSlots.value = slotRes.data || []
}

const resetFilter = () => {
  filter.adSlotId = null
  filter.active = null
  loadData()
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(editData, { ...row })
  } else {
    Object.assign(editData, {
      id: null,
      adSlotId: null,
      title: '',
      adType: 'IMAGE',
      materialUrl: '',
      jumpUrl: '',
      startTime: null,
      endTime: null,
      displayStartTime: null,
      displayEndTime: null,
      displayFrequency: 'ALWAYS',
      priority: 0,
      active: true
    })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  const payload = { ...editData }
  if (payload.startTime) {
    payload.startTime = dayjs(payload.startTime).format('YYYY-MM-DD HH:mm:ss')
  }
  if (payload.endTime) {
    payload.endTime = dayjs(payload.endTime).format('YYYY-MM-DD HH:mm:ss')
  }
  if (payload.displayStartTime) {
    payload.displayStartTime = dayjs(payload.displayStartTime).format('HH:mm:ss')
  }
  if (payload.displayEndTime) {
    payload.displayEndTime = dayjs(payload.displayEndTime).format('HH:mm:ss')
  }
  if (editData.id) {
    await advertisementApi.update(editData.id, payload)
    ElMessage.success('更新成功')
  } else {
    await advertisementApi.create(payload)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const toggleActive = async (row) => {
  await advertisementApi.toggle(row.id)
  ElMessage.success(row.active ? '已启用' : '已禁用')
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除广告"${row.title}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await advertisementApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(loadData)
</script>
