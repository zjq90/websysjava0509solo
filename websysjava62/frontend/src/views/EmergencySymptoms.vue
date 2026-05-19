<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">紧急症状库管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增症状
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索症状名称"
        style="width: 300px"
        clearable
        @keyup.enter="fetchData"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" class="ml-10" @click="fetchData">查询</el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="symptomName" label="症状名称" width="150" />
        <el-table-column prop="description" label="症状描述" show-overflow-tooltip />
        <el-table-column prop="severityLevel" label="严重程度" width="120">
          <template #default="scope">
            <el-tag :type="getSeverityType(scope.row.severityLevel)">
              {{ getSeverityText(scope.row.severityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="petType" label="适用宠物" width="100" />
        <el-table-column prop="revisedBy" label="修订人" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑症状' : '新增症状'"
    width="600px"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="症状名称" prop="symptomName">
        <el-input v-model="form.symptomName" />
      </el-form-item>
      <el-form-item label="症状描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="紧急措施" prop="emergencyMeasure">
        <el-input v-model="form.emergencyMeasure" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="严重程度" prop="severityLevel">
        <el-select v-model="form.severityLevel" style="width: 100%">
          <el-option label="低" value="LOW" />
          <el-option label="中" value="MEDIUM" />
          <el-option label="高" value="HIGH" />
          <el-option label="危急" value="CRITICAL" />
        </el-select>
      </el-form-item>
      <el-form-item label="适用宠物" prop="petType">
        <el-input v-model="form.petType" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" style="width: 100%">
          <el-option label="启用" value="ACTIVE" />
          <el-option label="禁用" value="INACTIVE" />
        </el-select>
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
const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  symptomName: '',
  description: '',
  emergencyMeasure: '',
  severityLevel: 'MEDIUM',
  petType: '通用',
  status: 'ACTIVE',
  revisedBy: ''
})

const rules = {
  symptomName: [{ required: true, message: '请输入症状名称', trigger: 'blur' }]
}

const getSeverityType = (level) => {
  const map = { LOW: 'info', MEDIUM: 'warning', HIGH: 'danger', CRITICAL: 'danger' }
  return map[level] || 'info'
}

const getSeverityText = (level) => {
  const map = { LOW: '低', MEDIUM: '中', HIGH: '高', CRITICAL: '危急' }
  return map[level] || level
}

const fetchData = async () => {
  try {
    const res = await axios.get('/api/emergency-symptoms')
    if (res.data.code === 200) {
      tableData.value = res.data.data.filter(item =>
        !searchKeyword.value || item.symptomName.includes(searchKeyword.value)
      )
    }
  } catch (e) {
    ElMessage.error('获取数据失败')
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    symptomName: '',
    description: '',
    emergencyMeasure: '',
    severityLevel: 'MEDIUM',
    petType: '通用',
    status: 'ACTIVE',
    revisedBy: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const url = isEdit.value ? `/api/emergency-symptoms/${form.id}` : '/api/emergency-symptoms'
    const method = isEdit.value ? 'put' : 'post'
    const res = await axios[method](url, form)
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该数据吗?', '提示')
    const res = await axios.delete(`/api/emergency-symptoms/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>
