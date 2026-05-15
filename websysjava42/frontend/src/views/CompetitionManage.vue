<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>比赛管理</span>
          <el-button type="primary" size="small" @click="handleAdd">新增比赛</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="competitionNo" label="比赛编号" width="120" />
        <el-table-column prop="name" label="比赛名称" />
        <el-table-column prop="event" label="比赛项目" width="120" />
        <el-table-column prop="location" label="比赛地点" width="120" />
        <el-table-column prop="chiefRefereeName" label="裁判长" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleAthletes(scope.row)">参赛运动员</el-button>
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑比赛' : '新增比赛'" width="700px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="比赛编号">
          <el-input v-model="form.competitionNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="比赛名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="比赛项目">
          <el-input v-model="form.event" />
        </el-form-item>
        <el-form-item label="比赛地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="裁判长">
          <el-select v-model="form.chiefRefereeId" style="width: 100%" placeholder="请选择裁判长">
            <el-option v-for="ref in chiefReferees" :key="ref.id" :label="ref.name" :value="ref.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="比赛规则">
          <el-input v-model="form.rules" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已结束" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="athleteDialogVisible" title="参赛运动员管理" width="800px">
      <div style="margin-bottom: 15px;">
        <el-select v-model="selectedAthleteId" placeholder="选择运动员添加" style="width: 250px; margin-right: 10px;">
          <el-option v-for="athlete in allAthletes" :key="athlete.id" :label="athlete.name + ' (' + athlete.athleteNo + ')'" :value="athlete.id" />
        </el-select>
        <el-button type="primary" size="small" @click="addAthleteToCompetition" :disabled="!selectedAthleteId">添加</el-button>
      </div>
      <el-table :data="competitionAthletes" border stripe>
        <el-table-column prop="athleteNo" label="运动员编号" width="120" />
        <el-table-column prop="athleteName" label="姓名" width="100" />
        <el-table-column prop="lane" label="道次/组别" width="100" />
        <el-table-column prop="checkInStatus" label="签到状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.checkInStatus === 1 ? 'success' : 'info'">
              {{ scope.row.checkInStatus === 1 ? '已签到' : '未签到' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" type="success" @click="checkIn(scope.row)" :disabled="scope.row.checkInStatus === 1">签到</el-button>
            <el-button size="small" type="danger" @click="removeAthlete(scope.row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

const tableData = ref([])
const chiefReferees = ref([])
const allAthletes = ref([])
const competitionAthletes = ref([])
const dialogVisible = ref(false)
const athleteDialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const form = ref({})
const selectedAthleteId = ref(null)
const currentCompetitionId = ref(null)

const getStatusType = (status) => ['info', 'primary', 'success', 'danger'][status]
const getStatusText = (status) => ['未开始', '进行中', '已结束', '已取消'][status]

const loadData = async () => {
  try {
    const compRes = await api.getCompetitions()
    if (compRes.code === 200) {
      tableData.value = compRes.data || []
    }
    const refRes = await api.getChiefReferees()
    if (refRes.code === 200) {
      chiefReferees.value = refRes.data || []
    }
    const athRes = await api.getAthletes()
    if (athRes.code === 200) {
      allAthletes.value = athRes.data || []
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
    console.error('Load data error:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { competitionNo: '', name: '', event: '', location: '', chiefRefereeId: null, rules: '', status: 0 }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该比赛吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      const res = await api.deleteCompetition(id)
      if (res.code === 200) { 
        ElMessage.success('删除成功')
        loadData() 
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    }).catch(() => {})
}

const handleSave = async () => {
  if (!form.value.competitionNo || !form.value.name) { 
    ElMessage.warning('请填写必填项')
    return 
  }
  loading.value = true
  try {
    const chief = chiefReferees.value.find(r => r.id === form.value.chiefRefereeId)
    if (chief) {
      form.value.chiefRefereeName = chief.name
    }
    const res = isEdit.value ? await api.updateCompetition(form.value.id, form.value) : await api.addCompetition(form.value)
    if (res.code === 200) { 
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
      dialogVisible.value = false
      loadData() 
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('保存失败')
    console.error('Save error:', error)
  } finally { 
    loading.value = false 
  }
}

const handleAthletes = async (row) => {
  currentCompetitionId.value = row.id
  selectedAthleteId.value = null
  try {
    const res = await api.getCompetitionAthletes(row.id)
    if (res.code === 200) {
      competitionAthletes.value = (res.data || []).filter(a => a.status === 1)
    }
    athleteDialogVisible.value = true
  } catch (error) {
    ElMessage.error('加载参赛运动员失败')
  }
}

const addAthleteToCompetition = async () => {
  if (!selectedAthleteId.value) return
  try {
    const res = await api.addAthleteToCompetition(currentCompetitionId.value, selectedAthleteId.value)
    if (res.code === 200) {
      ElMessage.success('添加成功')
      selectedAthleteId.value = null
      handleAthletes({ id: currentCompetitionId.value })
    } else {
      ElMessage.error(res.message || '添加失败')
    }
  } catch (error) {
    ElMessage.error('添加失败')
  }
}

const checkIn = async (row) => {
  try {
    const res = await api.checkIn(currentCompetitionId.value, row.athleteId)
    if (res.code === 200) {
      ElMessage.success('签到成功')
      handleAthletes({ id: currentCompetitionId.value })
    } else {
      ElMessage.error(res.message || '签到失败')
    }
  } catch (error) {
    ElMessage.error('签到失败')
  }
}

const removeAthlete = async (row) => {
  ElMessageBox.confirm('确定要移除该运动员吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(async () => {
      try {
        const res = await api.removeAthleteFromCompetition(currentCompetitionId.value, row.athleteId)
        if (res.code === 200) {
          ElMessage.success('移除成功')
          handleAthletes({ id: currentCompetitionId.value })
        } else {
          ElMessage.error(res.message || '移除失败')
        }
      } catch (error) {
        ElMessage.error('移除失败')
      }
    }).catch(() => {})
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
