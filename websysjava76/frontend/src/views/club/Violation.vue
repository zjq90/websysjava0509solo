<template>
  <div class="violation-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>违规社团处理</span>
          <div class="header-actions">
            <el-select v-model="queryParams.type" placeholder="违规类型" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="长期不活动" value="INACTIVE" />
              <el-option label="财务问题" value="FINANCE" />
              <el-option label="违规活动" value="ILLEGAL_ACTIVITY" />
              <el-option label="其他" value="OTHER" />
            </el-select>
            <el-button type="danger" @click="handleAdd">
              <el-icon><Warning /></el-icon>新增违规记录
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clubName" label="社团名称" width="150" />
        <el-table-column prop="type" label="违规类型" width="120">
          <template #default="{ row }">
            <el-tag type="danger">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="违规描述" />
        <el-table-column prop="level" label="严重程度" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.level)">{{ getLevelText(row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="action" label="处理措施" width="120">
          <template #default="{ row }">
            {{ getActionText(row.action) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 'PENDING'" type="warning" link @click="handleProcess(row)">处理</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; text-align: right"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog v-model="processDialogVisible" title="处理违规" width="600px">
      <el-descriptions :column="2" border v-if="currentViolation" style="margin-bottom: 20px">
        <el-descriptions-item label="社团名称">{{ currentViolation.clubName }}</el-descriptions-item>
        <el-descriptions-item label="违规类型">{{ getTypeText(currentViolation.type) }}</el-descriptions-item>
        <el-descriptions-item label="严重程度">{{ getLevelText(currentViolation.level) }}</el-descriptions-item>
        <el-descriptions-item label="违规描述" :span="2">{{ currentViolation.description }}</el-descriptions-item>
      </el-descriptions>
      <el-form :model="form" label-width="100px">
        <el-form-item label="处理措施">
          <el-radio-group v-model="form.action">
            <el-radio label="WARNING">警告</el-radio>
            <el-radio label="SUSPEND">暂停运营</el-radio>
            <el-radio label="CANCEL">注销社团</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="处理意见">
          <el-input
            v-model="form.handlerOpinion"
            type="textarea"
            :rows="4"
            placeholder="请输入处理意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitProcess">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { clubApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const processDialogVisible = ref(false)
const currentViolation = ref(null)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  type: ''
})

const form = ref({
  action: '',
  handlerOpinion: ''
})

const getTypeText = (type) => {
  const types = {
    INACTIVE: '长期不活动',
    FINANCE: '财务问题',
    ILLEGAL_ACTIVITY: '违规活动',
    OTHER: '其他'
  }
  return types[type] || type
}

const getLevelText = (level) => {
  const levels = {
    LIGHT: '轻度',
    MEDIUM: '中度',
    SERIOUS: '严重'
  }
  return levels[level] || level
}

const getLevelTagType = (level) => {
  const types = {
    LIGHT: 'warning',
    MEDIUM: 'danger',
    SERIOUS: 'danger'
  }
  return types[level] || ''
}

const getActionText = (action) => {
  const actions = {
    WARNING: '警告',
    SUSPEND: '暂停运营',
    CANCEL: '注销社团'
  }
  return actions[action] || '待处理'
}

const getStatusText = (status) => {
  const statuses = {
    PENDING: '待处理',
    PROCESSED: '已处理',
    CLOSED: '已关闭'
  }
  return statuses[status] || status
}

const getStatusTagType = (status) => {
  const types = {
    PENDING: 'warning',
    PROCESSED: 'success',
    CLOSED: 'info'
  }
  return types[status] || ''
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...queryParams.value }
    if (!params.type) delete params.type
    const res = await clubApi.getViolationList(params)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  ElMessage.info('新增违规记录功能')
}

const handleView = (row) => {
  ElMessage.info(`查看违规记录: ${row.clubName}`)
}

const handleProcess = (row) => {
  currentViolation.value = row
  form.value.action = ''
  form.value.handlerOpinion = ''
  processDialogVisible.value = true
}

const submitProcess = async () => {
  if (!form.value.action) {
    ElMessage.warning('请选择处理措施')
    return
  }
  try {
    await clubApi.handleViolation(
      currentViolation.value.id,
      form.value.action,
      form.value.handlerOpinion,
      1,
      '管理员'
    )
    ElMessage.success('处理成功')
    processDialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除该违规记录吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await clubApi.deleteViolation(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.violation-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
