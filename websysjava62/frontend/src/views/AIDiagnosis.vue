<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">AI诊断建议管理</h2>
    </div>
    <div class="table-container">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="consultationNo" label="问诊编号" width="150" />
        <el-table-column prop="aiModelVersion" label="AI模型版本" width="120" />
        <el-table-column prop="possibleDiseases" label="可能疾病" show-overflow-tooltip />
        <el-table-column prop="suggestion" label="诊断建议" show-overflow-tooltip />
        <el-table-column prop="confidence" label="置信度" width="100">
          <template #default="scope">{{ scope.row.confidence }}%</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看详情</el-button>
            <el-button size="small" type="success" v-if="scope.row.status === 'GENERATED'" @click="reviewSuggestion(scope.row.id, 'REVIEWED')">
              审核
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog v-model="viewDialogVisible" title="AI诊断详情" width="700px">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="问诊编号">{{ currentData.consultationNo }}</el-descriptions-item>
      <el-descriptions-item label="AI模型版本">{{ currentData.aiModelVersion }}</el-descriptions-item>
      <el-descriptions-item label="可能的疾病">{{ currentData.possibleDiseases }}</el-descriptions-item>
      <el-descriptions-item label="诊断建议">{{ currentData.suggestion }}</el-descriptions-item>
      <el-descriptions-item label="推荐检查项目">{{ currentData.recommendedTests }}</el-descriptions-item>
      <el-descriptions-item label="推荐治疗方案">{{ currentData.recommendedTreatment }}</el-descriptions-item>
      <el-descriptions-item label="置信度">{{ currentData.confidence }}%</el-descriptions-item>
      <el-descriptions-item label="风险提示" :content-style="{ color: '#f56c6c' }">{{ currentData.riskWarning }}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const tableData = ref([])
const viewDialogVisible = ref(false)
const currentData = reactive({})

const getStatusType = (status) => {
  const map = { GENERATED: 'warning', REVIEWED: 'primary', ADOPTED: 'success' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { GENERATED: '已生成', REVIEWED: '已审核', ADOPTED: '已采纳' }
  return map[status] || status
}

const fetchData = async () => {
  try {
    const res = await axios.get('/api/consultation/suggestions')
    if (res.data.code === 200) tableData.value = res.data.data
  } catch (e) { ElMessage.error('获取数据失败') }
}

const handleView = (row) => {
  Object.assign(currentData, row)
  viewDialogVisible.value = true
}

const reviewSuggestion = async (id, status) => {
  try {
    const res = await axios.put(`/api/consultation/suggestions/${id}/review`, null, {
      params: { status, reviewedBy: '兽医专家' }
    })
    if (res.data.code === 200) { ElMessage.success('审核成功'); fetchData() }
  } catch (e) { ElMessage.error('审核失败') }
}

onMounted(() => { fetchData() })
</script>
