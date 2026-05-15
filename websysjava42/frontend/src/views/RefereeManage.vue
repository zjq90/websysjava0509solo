<template>
  <div class="page-container">
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>裁判管理</span>
          <el-button type="primary" size="small" @click="handleAdd">新增裁判</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="refereeNo" label="裁判编号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80" />
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="level" label="裁判等级" width="100" />
        <el-table-column prop="organization" label="所属单位" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="isChief" label="是否裁判长" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.isChief === 1 ? 'success' : 'info'">
              {{ scope.row.isChief === 1 ? '是' : '否' }}
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
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑裁判' : '新增裁判'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="裁判编号">
          <el-input v-model="form.refereeNo" :disabled="isEdit" />
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
          <el-input-number v-model="form.age" :min="18" :max="80" />
        </el-form-item>
        <el-form-item label="裁判等级">
          <el-select v-model="form.level" style="width: 100%">
            <el-option label="国家级" value="国家级" />
            <el-option label="一级" value="一级" />
            <el-option label="二级" value="二级" />
            <el-option label="三级" value="三级" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属单位">
          <el-input v-model="form.organization" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="是否裁判长">
          <el-radio-group v-model="form.isChief">
            <el-radio :value="1">是</el-radio>
            <el-radio :value="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="loading">保存</el-button>
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
const isEdit = ref(false)
const loading = ref(false)
const form = ref({
  refereeNo: '',
  name: '',
  gender: '男',
  age: 30,
  level: '一级',
  organization: '',
  specialty: '',
  phone: '',
  email: '',
  isChief: 0,
  remark: ''
})

const loadData = async () => {
  const res = await api.getReferees()
  tableData.value = res.data || []
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    refereeNo: '',
    name: '',
    gender: '男',
    age: 30,
    level: '一级',
    organization: '',
    specialty: '',
    phone: '',
    email: '',
    isChief: 0,
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该裁判吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await api.deleteReferee(id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  }).catch(() => {})
}

const handleSave = async () => {
  if (!form.value.refereeNo || !form.value.name) {
    ElMessage.warning('请填写必填项')
    return
  }
  loading.value = true
  try {
    const res = isEdit.value 
      ? await api.updateReferee(form.value.id, form.value)
      : await api.addReferee(form.value)
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
