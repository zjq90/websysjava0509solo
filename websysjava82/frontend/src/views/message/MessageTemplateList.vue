<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">模板管理</h2>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        新增模板
      </el-button>
    </div>

    <div class="filter-bar">
      <el-form :inline="true" :model="filter">
        <el-form-item label="类型">
          <el-select v-model="filter.templateType" placeholder="全部" clearable style="width: 150px">
            <el-option label="系统模板" value="SYSTEM" />
            <el-option label="营销模板" value="MARKETING" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card-wrapper">
      <el-table :data="filteredTemplates" style="width: 100%" border>
        <el-table-column prop="name" label="模板名称" width="180" />
        <el-table-column prop="templateType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.templateType === 'SYSTEM' ? 'info' : 'warning'">
              {{ row.templateType === 'SYSTEM' ? '系统模板' : '营销模板' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" />
        <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
        <el-table-column prop="system" label="系统内置" width="100">
          <template #default="{ row }">
            <el-tag :type="row.system ? 'success' : 'info'">
              {{ row.system ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <el-button size="small" type="primary" @click="openDialog(row)">编辑</el-button>
              <el-button
                size="small"
                type="danger"
                :disabled="row.system"
                @click="handleDelete(row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editData.id ? '编辑模板' : '新增模板'" width="600px">
      <el-form :model="editData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="editData.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="类型" prop="templateType">
          <el-select v-model="editData.templateType" style="width: 100%">
            <el-option label="系统模板" value="SYSTEM" />
            <el-option label="营销模板" value="MARKETING" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="editData.title" placeholder="请输入消息标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="editData.content"
            type="textarea"
            :rows="8"
            placeholder="请输入消息内容"
          />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { messageTemplateApi } from '../../api'

const templates = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const filter = reactive({
  templateType: null
})

const editData = reactive({
  id: null,
  name: '',
  templateType: 'MARKETING',
  title: '',
  content: '',
  system: false,
  enabled: true
})

const rules = {
  name: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  templateType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}

const filteredTemplates = computed(() => {
  if (filter.templateType) {
    return templates.value.filter(t => t.templateType === filter.templateType)
  }
  return templates.value
})

const formatDateTime = (date) => {
  return date ? dayjs(date).format('YYYY-MM-DD HH:mm:ss') : '-'
}

const loadData = async () => {
  const res = await messageTemplateApi.list()
  templates.value = res.data || []
}

const resetFilter = () => {
  filter.templateType = null
  loadData()
}

const openDialog = (row = null) => {
  if (row) {
    Object.assign(editData, { ...row })
  } else {
    Object.assign(editData, {
      id: null,
      name: '',
      templateType: 'MARKETING',
      title: '',
      content: '',
      system: false,
      enabled: true
    })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  if (editData.id) {
    await messageTemplateApi.update(editData.id, editData)
    ElMessage.success('更新成功')
  } else {
    await messageTemplateApi.create(editData)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定删除模板"${row.name}"吗？`, '提示', {
    type: 'warning'
  }).then(async () => {
    await messageTemplateApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(loadData)
</script>
