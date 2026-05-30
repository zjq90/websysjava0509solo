<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">广告位配置</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        新增广告位
      </el-button>
    </div>

    <div class="card-wrapper">
      <el-table :data="adSlots" style="width: 100%" border>
        <el-table-column prop="code" label="编码" width="180" />
        <el-table-column prop="name" label="名称" width="150" />
        <el-table-column prop="slotType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.slotType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" />
        <el-table-column label="尺寸" width="120">
          <template #default="{ row }">
            {{ row.width && row.height ? `${row.width}×${row.height}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="displayFrequency" label="展示频率" width="120">
          <template #default="{ row }">
            {{ getFrequencyName(row.displayFrequency) }}
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" @click="openDialog(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editData.id ? '编辑广告位' : '新增广告位'" width="600px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="editData.code" placeholder="请输入广告位编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="editData.name" placeholder="请输入广告位名称" />
        </el-form-item>
        <el-form-item label="类型" prop="slotType">
          <el-select v-model="editData.slotType" placeholder="请选择类型" style="width: 100%">
            <el-option label="加载页" value="加载页" />
            <el-option label="Banner" value="Banner" />
            <el-option label="弹窗" value="弹窗" />
            <el-option label="激励视频" value="激励视频" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editData.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="宽度">
              <el-input-number v-model="editData.width" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="高度">
              <el-input-number v-model="editData.height" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="展示频率" prop="displayFrequency">
          <el-select v-model="editData.displayFrequency" placeholder="请选择展示频率" style="width: 100%">
            <el-option label="每次展示" value="ALWAYS" />
            <el-option label="每日一次" value="DAILY_ONCE" />
            <el-option label="每小时一次" value="HOURLY_ONCE" />
            <el-option label="仅展示一次" value="ONLY_ONCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="editData.enabled" active-text="启用" inactive-text="禁用" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adSlotApi } from '../../api'

const adSlots = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const editData = reactive({
  id: null,
  code: '',
  name: '',
  slotType: '',
  description: '',
  width: null,
  height: null,
  displayFrequency: 'ALWAYS',
  enabled: true
})

const rules = {
  code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  slotType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  displayFrequency: [{ required: true, message: '请选择展示频率', trigger: 'change' }]
}

const getFrequencyName = (freq) => {
  const map = { ALWAYS: '每次展示', DAILY_ONCE: '每日一次', HOURLY_ONCE: '每小时一次', ONLY_ONCE: '仅展示一次' }
  return map[freq] || freq
}

const loadData = async () => {
  const res = await adSlotApi.list()
  adSlots.value = res.data || []
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(editData, row)
  } else {
    Object.assign(editData, {
      id: null,
      code: '',
      name: '',
      slotType: '',
      description: '',
      width: null,
      height: null,
      displayFrequency: 'ALWAYS',
      enabled: true
    })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  if (editData.id) {
    await adSlotApi.update(editData.id, editData)
    ElMessage.success('更新成功')
  } else {
    await adSlotApi.create(editData)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除广告位"${row.name}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await adSlotApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(loadData)
</script>
