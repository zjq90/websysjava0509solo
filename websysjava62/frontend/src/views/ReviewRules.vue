<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">审核规则管理</h2>
      <el-button type="primary" @click="handleAdd">新增规则</el-button>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="ruleName" label="规则名称" width="150" />
        <el-table-column prop="ruleType" label="规则类型" width="120">
          <template #default="scope">
            <el-tag type="info">{{ scope.row.ruleType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="enabled" label="是否启用" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.enabled" @change="toggleEnabled(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column prop="configuredBy" label="配置人" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑规则' : '新增规则'" width="600px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="规则名称">
        <el-input v-model="form.ruleName" />
      </el-form-item>
      <el-form-item label="规则类型">
        <el-select v-model="form.ruleType" style="width: 100%">
          <el-option label="敏感词" value="SENSITIVE_WORD" />
          <el-option label="内容审核" value="CONTENT_AUDIT" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="规则内容">
        <el-input v-model="form.ruleContent" type="textarea" :rows="3" placeholder="JSON格式" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" type="textarea" :rows="2" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({
  id: null,
  ruleName: '',
  ruleType: 'SENSITIVE_WORD',
  ruleContent: '',
  description: '',
  enabled: true
})

const fetchData = async () => {
  try {
    const res = await axios.get('/api/review-rules')
    if (res.data.code === 200) tableData.value = res.data.data
  } catch (e) { ElMessage.error('获取数据失败') }
}

const toggleEnabled = async (row) => {
  try {
    const res = await axios.put(`/api/review-rules/${row.id}`, row)
    if (res.data.code === 200) ElMessage.success('更新成功')
  } catch (e) { ElMessage.error('更新失败') }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, ruleName: '', ruleType: 'SENSITIVE_WORD', ruleContent: '', description: '', enabled: true })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const url = isEdit.value ? `/api/review-rules/${form.id}` : '/api/review-rules'
    const method = isEdit.value ? 'put' : 'post'
    const res = await axios[method](url, form)
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchData()
    }
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除吗?', '提示')
    const res = await axios.delete(`/api/review-rules/${id}`)
    if (res.data.code === 200) { ElMessage.success('删除成功'); fetchData() }
  } catch (e) { if (e !== 'cancel') ElMessage.error('删除失败') }
}

onMounted(() => { fetchData() })
</script>
