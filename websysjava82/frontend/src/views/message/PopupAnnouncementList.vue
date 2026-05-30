<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">弹窗公告</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        新增公告
      </el-button>
    </div>

    <div class="card-wrapper">
      <el-table :data="announcements" style="width: 100%" border>
        <el-table-column label="图片" width="100">
          <template #default="{ row }">
            <el-image v-if="row.imageUrl" :src="row.imageUrl" class="image-preview" fit="cover" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column prop="content" label="内容" min-width="250" show-overflow-tooltip />
        <el-table-column prop="displayTrigger" label="展示时机" width="120">
          <template #default="{ row }">
            <el-tag>{{ getTriggerName(row.displayTrigger) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="displayFrequency" label="展示频率" width="120">
          <template #default="{ row }">
            {{ getFrequencyName(row.displayFrequency) }}
          </template>
        </el-table-column>
        <el-table-column label="有效期" width="220">
          <template #default="{ row }">
            <div v-if="row.startTime && row.endTime">
              {{ formatDate(row.startTime) }}
              <br />
              {{ formatDate(row.endTime) }}
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="buttonText" label="按钮文字" width="100" />
        <el-table-column prop="viewCount" label="浏览量" width="80" />
        <el-table-column prop="clickCount" label="点击量" width="80" />
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

    <el-dialog v-model="dialogVisible" :title="editData.id ? '编辑公告' : '新增公告'" width="700px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editData.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="editData.content"
            type="textarea"
            :rows="4"
            placeholder="请输入公告内容，支持换行"
          />
        </el-form-item>
        <el-form-item label="图片链接">
          <el-input v-model="editData.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="editData.jumpUrl" placeholder="请输入跳转链接" />
        </el-form-item>
        <el-form-item label="按钮文字">
          <el-input v-model="editData.buttonText" placeholder="请输入按钮文字，如：立即查看" />
        </el-form-item>
        <el-form-item label="展示时机" prop="displayTrigger">
          <el-select v-model="editData.displayTrigger" style="width: 100%">
            <el-option label="登录后" value="LOGIN" />
            <el-option label="打开首页时" value="HOME" />
          </el-select>
        </el-form-item>
        <el-form-item label="展示频率" prop="displayFrequency">
          <el-select v-model="editData.displayFrequency" style="width: 100%">
            <el-option label="每次" value="ALWAYS" />
            <el-option label="每日一次" value="DAILY" />
            <el-option label="仅一次" value="ONCE" />
          </el-select>
        </el-form-item>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-date-picker
                v-model="editData.startTime"
                type="datetime"
                placeholder="选择开始时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-date-picker
                v-model="editData.endTime"
                type="datetime"
                placeholder="选择结束时间"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { popupAnnouncementApi } from '../../api'

const announcements = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const editData = reactive({
  id: null,
  title: '',
  content: '',
  imageUrl: '',
  jumpUrl: '',
  buttonText: '',
  displayTrigger: 'HOME',
  displayFrequency: 'DAILY',
  startTime: null,
  endTime: null,
  priority: 0,
  active: true
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  displayTrigger: [{ required: true, message: '请选择展示时机', trigger: 'change' }],
  displayFrequency: [{ required: true, message: '请选择展示频率', trigger: 'change' }]
}

const getTriggerName = (trigger) => {
  const map = { LOGIN: '登录后', HOME: '打开首页时' }
  return map[trigger] || trigger
}

const getFrequencyName = (freq) => {
  const map = { ALWAYS: '每次', DAILY: '每日一次', ONCE: '仅一次' }
  return map[freq] || freq
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

const loadData = async () => {
  const res = await popupAnnouncementApi.list()
  announcements.value = res.data || []
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(editData, { ...row })
  } else {
    Object.assign(editData, {
      id: null,
      title: '',
      content: '',
      imageUrl: '',
      jumpUrl: '',
      buttonText: '',
      displayTrigger: 'HOME',
      displayFrequency: 'DAILY',
      startTime: null,
      endTime: null,
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
  if (editData.id) {
    await popupAnnouncementApi.update(editData.id, payload)
    ElMessage.success('更新成功')
  } else {
    await popupAnnouncementApi.create(payload)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const toggleActive = async (row) => {
  await popupAnnouncementApi.toggle(row.id)
  ElMessage.success(row.active ? '已启用' : '已禁用')
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除公告"${row.title}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await popupAnnouncementApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(loadData)
</script>
