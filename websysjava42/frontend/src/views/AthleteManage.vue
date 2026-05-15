<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>运动员管理</span>
          <el-button type="primary" size="small" @click="handleAdd">新增运动员</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="athleteNo" label="运动员编号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="school" label="学校/单位" />
        <el-table-column prop="event" label="参赛项目" width="150" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEvaluate(scope.row)">评价</el-button>
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑运动员' : '新增运动员'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="运动员编号">
          <el-input v-model="form.athleteNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" style="width: 100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="10" :max="50" />
        </el-form-item>
        <el-form-item label="学校/单位">
          <el-input v-model="form.school" />
        </el-form-item>
        <el-form-item label="参赛项目">
          <el-input v-model="form.event" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="评价">
          <el-input v-model="form.evaluation" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="evaluateDialogVisible" title="评价运动员" width="500px">
      <el-form :model="evaluateForm" label-width="80px">
        <el-form-item label="运动员">
          <span>{{ evaluateForm.name }}</span>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluateForm.evaluation" type="textarea" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evaluateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveEvaluate" :loading="loading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

const tableData = ref([])
const dialogVisible = ref(false)
const evaluateDialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const form = ref({})
const evaluateForm = ref({})

const loadData = async () => {
  const res = await api.getAthletes()
  tableData.value = res.data || []
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    athleteNo: '',
    name: '',
    gender: '男',
    age: 20,
    school: '',
    event: '',
    phone: '',
    email: '',
    evaluation: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该运动员吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await api.deleteAthlete(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  }).catch(() => {})
}

const handleEvaluate = (row) => {
  evaluateForm.value = {
    id: row.id,
    name: row.name,
    evaluation: row.evaluation || ''
  }
  evaluateDialogVisible.value = true
}

const handleSaveEvaluate = async () => {
  if (!evaluateForm.value.evaluation) {
    ElMessage.warning('请填写评价内容')
    return
  }
  loading.value = true
  try {
    const res = await api.evaluateAthlete(evaluateForm.value.id, evaluateForm.value.evaluation)
    if (res.code === 200) {
      ElMessage.success('评价成功')
      evaluateDialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '评价失败')
    }
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!form.value.athleteNo || !form.value.name) {
    ElMessage.warning('请填写必填项')
    return
  }
  loading.value = true
  try {
    const res = isEdit.value 
      ? await api.updateAthlete(form.value.id, form.value)
      : await api.addAthlete(form.value)
    if (res.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
      dialogVisible.value = false
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
