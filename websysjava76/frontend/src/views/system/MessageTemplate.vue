<template>
  <div class="message-template">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>消息模板配置</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>新增模板
          </el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="模板编码" width="150" />
        <el-table-column prop="name" label="模板名称" width="150" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="消息标题" />
        <el-table-column prop="enabled" label="状态" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.enabled" @change="toggleStatus(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑模板' : '新增模板'" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="模板编码">
          <el-input v-model="form.code" placeholder="请输入模板编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="模板名称">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="审核通知" value="APPROVAL" />
            <el-option label="活动提醒" value="ACTIVITY" />
            <el-option label="系统通知" value="SYSTEM" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="消息标题">
          <el-input v-model="form.title" placeholder="请输入消息标题" />
        </el-form-item>
        <el-form-item label="消息内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="请输入消息内容，支持占位符如：{{社团名称}}、{{活动名称}}等"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { systemApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

const form = ref({
  code: '',
  name: '',
  type: '',
  title: '',
  content: '',
  remark: ''
})

const getTypeText = (type) => {
  const types = {
    APPROVAL: '审核通知',
    ACTIVITY: '活动提醒',
    SYSTEM: '系统通知',
    OTHER: '其他'
  }
  return types[type] || type
}

const getTypeTagType = (type) => {
  const types = {
    APPROVAL: 'primary',
    ACTIVITY: 'success',
    SYSTEM: 'warning',
    OTHER: 'info'
  }
  return types[type] || ''
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await systemApi.getMessageTemplates()
    tableData.value = Array.isArray(res) ? res : (res.content || [])
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const toggleStatus = (row) => {
  ElMessage.success(row.enabled ? '已启用' : '已禁用')
}

const handleAdd = () => {
  isEdit.value = false
  currentId.value = null
  form.value = { code: '', name: '', type: '', title: '', content: '', remark: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  currentId.value = row.id
  form.value = { ...row }
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    if (isEdit.value) {
      await systemApi.updateMessageTemplate(currentId.value, form.value)
    } else {
      await systemApi.createMessageTemplate(form.value)
    }
    ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除模板"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await systemApi.deleteMessageTemplate(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.message-template {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
