<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>评分管理</span>
          <el-button v-if="!isAthlete" type="primary" size="small" @click="handleAdd">提交评分</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="competitionName" label="比赛名称" width="200" />
        <el-table-column prop="athleteName" label="运动员" width="100" />
        <el-table-column prop="refereeName" label="裁判" width="100" />
        <el-table-column prop="technicalScore" label="技术分" width="100" />
        <el-table-column prop="performanceScore" label="表现分" width="100" />
        <el-table-column prop="totalScore" label="总分" width="100" />
        <el-table-column prop="comment" label="评语" show-overflow-tooltip />
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.auditStatus === 1 ? 'success' : scope.row.auditStatus === 2 ? 'danger' : 'warning'">
              {{ ['待审核', '已通过', '已驳回'][scope.row.auditStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="scope">
            <el-button v-if="!isAthlete && scope.row.auditStatus !== 1" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="isChief && scope.row.auditStatus === 0" size="small" type="success" @click="handleAudit(scope.row, 1)">通过</el-button>
            <el-button v-if="isChief && scope.row.auditStatus === 0" size="small" type="danger" @click="handleAudit(scope.row, 2)">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑评分' : '提交评分'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="选择比赛">
          <el-select v-model="form.competitionId" style="width: 100%" placeholder="请选择比赛" :disabled="isEdit">
            <el-option v-for="comp in competitions" :key="comp.id" :label="comp.name" :value="comp.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择运动员">
          <el-select v-model="form.athleteId" style="width: 100%" placeholder="请选择运动员" :disabled="isEdit">
            <el-option v-for="ath in athletes" :key="ath.id" :label="ath.name + ' (' + ath.athleteNo + ')'" :value="ath.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="裁判">
          <span>{{ isEdit ? form.refereeName : currentUser.realName }}</span>
        </el-form-item>
        <el-form-item label="技术分">
          <el-input-number v-model="form.technicalScore" :min="0" :max="50" :precision="1" :step="0.5" />
        </el-form-item>
        <el-form-item label="表现分">
          <el-input-number v-model="form.performanceScore" :min="0" :max="50" :precision="1" :step="0.5" />
        </el-form-item>
        <el-form-item label="总分">
          <span style="font-weight: bold; font-size: 18px;">{{ (form.technicalScore + form.performanceScore).toFixed(1) }}</span>
        </el-form-item>
        <el-form-item label="评语">
          <el-input v-model="form.comment" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="auditDialogVisible" title="审核评分" width="500px">
      <el-form :model="auditForm" label-width="100px">
        <el-form-item label="比赛">
          <span>{{ auditForm.competitionName }}</span>
        </el-form-item>
        <el-form-item label="运动员">
          <span>{{ auditForm.athleteName }}</span>
        </el-form-item>
        <el-form-item label="技术分">
          <span>{{ auditForm.technicalScore }}</span>
        </el-form-item>
        <el-form-item label="表现分">
          <span>{{ auditForm.performanceScore }}</span>
        </el-form-item>
        <el-form-item label="总分">
          <span>{{ auditForm.totalScore }}</span>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input v-model="auditForm.auditComment" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="success" @click="submitAudit(1)">通过</el-button>
        <el-button type="danger" @click="submitAudit(2)">驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const isAthlete = computed(() => user.role === 'ATHLETE')
const isChief = computed(() => user.role === 'CHIEF_REFEREE' || user.role === 'ADMIN')

const tableData = ref([])
const competitions = ref([])
const athletes = ref([])
const currentUser = ref(user)
const dialogVisible = ref(false)
const auditDialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const form = ref({
  competitionId: null,
  athleteId: null,
  technicalScore: 0,
  performanceScore: 0,
  comment: ''
})
const auditForm = ref({
  id: null,
  competitionName: '',
  athleteName: '',
  technicalScore: 0,
  performanceScore: 0,
  totalScore: 0,
  auditComment: ''
})

const loadData = async () => {
  try {
    const res = await api.getScores()
    tableData.value = res.data || []
    const compRes = await api.getCompetitions()
    competitions.value = (compRes.data || []).filter(c => c.status === 1)
    const athRes = await api.getAthletes()
    athletes.value = athRes.data || []
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    competitionId: null,
    athleteId: null,
    technicalScore: 0,
    performanceScore: 0,
    comment: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!isEdit && (!form.value.competitionId || !form.value.athleteId)) {
    ElMessage.warning('请选择比赛和运动员')
    return
  }
  loading.value = true
  try {
    form.value.totalScore = form.value.technicalScore + form.value.performanceScore
    if (!isEdit) {
      form.value.refereeId = currentUser.value.refereeId || currentUser.value.id
      form.value.refereeNo = currentUser.value.username
      form.value.refereeName = currentUser.value.realName
    }
    const res = isEdit.value ? await api.updateScore(form.value.id, form.value) : await api.submitScore(form.value)
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '提交成功')
      dialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } finally {
    loading.value = false
  }
}

const handleAudit = (row, auditStatus) => {
  auditForm.value = {
    id: row.id,
    competitionName: row.competitionName,
    athleteName: row.athleteName,
    technicalScore: row.technicalScore,
    performanceScore: row.performanceScore,
    totalScore: row.totalScore,
    auditComment: ''
  }
  auditDialogVisible.value = true
}

const submitAudit = async (auditStatus) => {
  loading.value = true
  try {
    const auditorId = currentUser.value.refereeId || currentUser.value.id
    const res = await api.auditScore(auditForm.value.id, auditStatus, auditorId, auditForm.value.auditComment)
    if (res.code === 200) {
      ElMessage.success(auditStatus === 1 ? '审核通过' : '审核驳回')
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
