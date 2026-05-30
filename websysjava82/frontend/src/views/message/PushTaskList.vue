<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">站内信推送</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        新建推送
      </el-button>
    </div>

    <div class="filter-bar">
      <el-form :inline="true" :model="filter">
        <el-form-item label="状态">
          <el-select v-model="filter.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待发送" value="PENDING" />
            <el-option label="发送中" value="SENDING" />
            <el-option label="已发送" value="SENT" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card-wrapper">
      <el-table :data="filteredTasks" style="width: 100%" border>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="targetType" label="目标用户" width="120">
          <template #default="{ row }">
            <el-tag :type="getTargetTypeTag(row.targetType)">
              {{ getTargetTypeName(row.targetType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="筛选条件" width="150">
          <template #default="{ row }">
            <div v-if="row.memberLevel">
              会员等级 ≥ {{ row.memberLevel }}
            </div>
            <div v-if="row.activeDays">
              {{ row.activeDays }}天内活跃
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="定时发送" width="180">
          <template #default="{ row }">
            <span v-if="row.timed">
              <el-icon><Clock /></el-icon>
              {{ formatDateTime(row.scheduledTime) }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalUsers" label="目标人数" width="100" />
        <el-table-column prop="deliveredCount" label="送达量" width="100" />
        <el-table-column prop="openedCount" label="打开量" width="100" />
        <el-table-column label="打开率" width="100">
          <template #default="{ row }">
            {{ row.deliveredCount > 0 ? ((row.openedCount / row.deliveredCount) * 100).toFixed(1) : 0 }}%
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button
                size="small"
                type="success"
                :disabled="row.status === 'SENT' || row.status === 'SENDING'"
                @click="handleSend(row)"
              >
                发送
              </el-button>
              <el-button size="small" type="primary" @click="handleView(row)">查看</el-button>
              <el-button
                size="small"
                type="danger"
                :disabled="row.status === 'SENDING'"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editData.id ? '编辑推送' : '新建推送'" width="700px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="选择模板">
          <el-select v-model="templateId" placeholder="选择模板（可选）" style="width: 100%" @change="loadTemplate">
            <el-option v-for="tpl in templates" :key="tpl.id" :label="tpl.name" :value="tpl.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="editData.title" placeholder="请输入推送标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="editData.content"
            type="textarea"
            :rows="6"
            placeholder="请输入推送内容"
          />
        </el-form-item>
        <el-form-item label="目标用户" prop="targetType">
          <el-radio-group v-model="editData.targetType">
            <el-radio value="ALL">全体用户</el-radio>
            <el-radio value="MEMBER_LEVEL">按会员等级</el-radio>
            <el-radio value="ACTIVE">按活跃时间</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row v-if="editData.targetType === 'MEMBER_LEVEL' || editData.targetType === 'ACTIVE'" :gutter="10">
          <el-col v-if="editData.targetType === 'MEMBER_LEVEL'" :span="12">
            <el-form-item label="会员等级">
              <el-select v-model="editData.memberLevel" placeholder="请选择等级" style="width: 100%">
                <el-option v-for="i in 5" :key="i" :label="`VIP${i}`" :value="i" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="活跃天数">
              <el-input-number v-model="editData.activeDays" :min="1" :max="30" style="width: 100%" placeholder="最近N天活跃" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="定时发送">
          <el-switch v-model="editData.timed" />
        </el-form-item>
        <el-form-item v-if="editData.timed" label="发送时间" prop="scheduledTime">
          <el-date-picker
            v-model="editData.scheduledTime"
            type="datetime"
            placeholder="选择发送时间"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="modal-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="handleSave(false)">保存草稿</el-button>
          <el-button type="primary" @click="handleSave(true)">立即发送</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="推送详情" width="600px">
      <div v-if="detailData">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="标题">{{ detailData.title }}</el-descriptions-item>
          <el-descriptions-item label="目标用户">{{ getTargetTypeName(detailData.targetType) }}</el-descriptions-item>
          <el-descriptions-item label="内容">
            <div style="white-space: pre-wrap;">{{ detailData.content }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTag(detailData.status)">
              {{ getStatusName(detailData.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="送达量">{{ detailData.deliveredCount }}</el-descriptions-item>
          <el-descriptions-item label="打开量">{{ detailData.openedCount }}</el-descriptions-item>
          <el-descriptions-item label="发送时间">{{ formatDateTime(detailData.sentTime) }}</el-descriptions-item>
        </el-descriptions>
        <h4 style="margin-top: 20px; margin-bottom: 10px">送达记录</h4>
        <el-table :data="messageList" style="width: 100%" size="small">
          <el-table-column prop="userId" label="用户ID" width="100" />
          <el-table-column prop="read" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.read ? 'success' : 'info'">
                {{ row.read ? '已读' : '未读' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sentTime" label="发送时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.sentTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="readTime" label="阅读时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.readTime) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { pushTaskApi, messageTemplateApi, messageApi } from '../../api'

const pushTasks = ref([])
const templates = ref([])
const messageList = ref([])
const dialogVisible = ref(false)
const detailVisible = ref(false)
const formRef = ref(null)
const templateId = ref(null)
const detailData = ref(null)

const filter = reactive({
  status: null
})

const editData = reactive({
  id: null,
  title: '',
  content: '',
  targetType: 'ALL',
  memberLevel: null,
  activeDays: null,
  timed: false,
  scheduledTime: null,
  status: 'DRAFT',
  createdBy: 'admin'
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  targetType: [{ required: true, message: '请选择目标用户', trigger: 'change' }]
}

const filteredTasks = computed(() => {
  if (filter.status) {
    return pushTasks.value.filter(t => t.status === filter.status)
  }
  return pushTasks.value
})

const getTargetTypeName = (type) => {
  const map = { ALL: '全体用户', MEMBER_LEVEL: '会员等级', ACTIVE: '活跃用户' }
  return map[type] || type
}

const getTargetTypeTag = (type) => {
  const map = { ALL: '', MEMBER_LEVEL: 'warning', ACTIVE: 'success' }
  return map[type] || ''
}

const getStatusName = (status) => {
  const map = { DRAFT: '草稿', PENDING: '待发送', SENDING: '发送中', SENT: '已发送' }
  return map[status] || status
}

const getStatusTag = (status) => {
  const map = { DRAFT: 'info', PENDING: 'warning', SENDING: 'primary', SENT: 'success' }
  return map[status] || ''
}

const formatDateTime = (date) => {
  return date ? dayjs(date).format('YYYY-MM-DD HH:mm:ss') : '-'
}

const loadData = async () => {
  const [taskRes, tplRes] = await Promise.all([
    pushTaskApi.list(),
    messageTemplateApi.list()
  ])
  pushTasks.value = taskRes.data || []
  templates.value = tplRes.data || []
}

const resetFilter = () => {
  filter.status = null
  loadData()
}

const loadTemplate = async () => {
  if (templateId.value) {
    const res = await messageTemplateApi.get(templateId.value)
    if (res.data) {
      editData.title = res.data.title
      editData.content = res.data.content
    }
  }
}

const openDialog = () => {
  templateId.value = null
  Object.assign(editData, {
    id: null,
    title: '',
    content: '',
    targetType: 'ALL',
    memberLevel: null,
    activeDays: null,
    timed: false,
    scheduledTime: null,
    status: 'DRAFT',
    createdBy: 'admin'
  })
  dialogVisible.value = true
}

const handleSave = async (sendNow) => {
  await formRef.value.validate()
  const payload = { ...editData }
  if (payload.scheduledTime) {
    payload.scheduledTime = dayjs(payload.scheduledTime).format('YYYY-MM-DD HH:mm:ss')
  }
  if (sendNow) {
    payload.status = 'PENDING'
  }
  const res = await pushTaskApi.create(payload)
  if (sendNow) {
    await pushTaskApi.send(res.data.id)
    ElMessage.success('发送成功')
  } else {
    ElMessage.success('保存成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleSend = (row) => {
  ElMessageBox.confirm(`确定发送推送"${row.title}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await pushTaskApi.send(row.id)
    ElMessage.success('发送成功')
    loadData()
  })
}

const handleView = async (row) => {
  detailData.value = row
  const res = await messageApi.listByTask(row.id)
  messageList.value = res.data || []
  detailVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除推送"${row.title}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await pushTaskApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(loadData)
</script>
