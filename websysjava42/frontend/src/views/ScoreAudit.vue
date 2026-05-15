<template>
  <div class="page-container">
    <el-card title="评分审核">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="competitionName" label="比赛名称" width="200" />
        <el-table-column prop="athleteName" label="运动员" width="100" />
        <el-table-column prop="refereeName" label="评分裁判" width="100" />
        <el-table-column prop="technicalScore" label="技术分" width="100" />
        <el-table-column prop="performanceScore" label="表现分" width="100" />
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="comment" label="评语" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button size="small" type="success" @click="handleAudit(scope.row, 1)">通过</el-button>
            <el-button size="small" type="danger" @click="handleAudit(scope.row, 2)">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="auditDialogVisible" title="审核意见" width="500px">
      <el-form :model="auditForm" label-width="80px">
        <el-form-item label="审核结果">
          <el-tag :type="auditForm.auditStatus === 1 ? 'success' : 'danger'">
            {{ auditForm.auditStatus === 1 ? '审核通过' : '审核驳回' }}
          </el-tag>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="auditForm.auditComment" type="textarea" :rows="4" placeholder="请输入审核意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAudit" :loading="loading">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const user = JSON.parse(localStorage.getItem('user') || '{}')

const tableData = ref([])
const auditDialogVisible = ref(false)
const loading = ref(false)
const auditForm = ref({
  id: null,
  auditStatus: 1,
  auditorId: user.refereeId || user.id,
  auditComment: ''
})

const loadData = async () => {
  const res = await api.getPendingAuditScores()
  tableData.value = res.data || []
}

const handleAudit = (row, status) => {
  auditForm.value = {
    id: row.id,
    auditStatus: status,
    auditorId: user.refereeId || user.id,
    auditComment: ''
  }
  auditDialogVisible.value = true
}

const saveAudit = async () => {
  loading.value = true
  try {
    const res = await api.auditScore(
      auditForm.value.id,
      auditForm.value.auditStatus,
      auditForm.value.auditorId,
      auditForm.value.auditComment
    )
    if (res.code === 200) {
      ElMessage.success(auditForm.value.auditStatus === 1 ? '审核通过' : '已驳回')
      auditDialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-container {
  padding: 0;
}
</style>
