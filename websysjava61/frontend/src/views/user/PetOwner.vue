<template>
  <div class="pet-owner-page">
    <div class="page-card">
      <div class="page-title">宠物主人管理</div>
      
      <div class="toolbar">
        <el-select v-model="auditStatusFilter" placeholder="实名认证状态" style="width: 150px; margin-right: 10px;">
          <el-option label="全部" :value="null" />
          <el-option label="未认证" :value="0" />
          <el-option label="认证中" :value="1" />
          <el-option label="认证通过" :value="2" />
          <el-option label="认证驳回" :value="3" />
        </el-select>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增用户
        </el-button>
      </div>

      <el-table :data="filteredData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="creditScore" label="信用分" width="100">
          <template #default="{ row }">
            <el-tag :type="row.creditScore >= 100 ? 'success' : row.creditScore >= 80 ? 'warning' : 'danger'">
              {{ row.creditScore }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="realNameStatus" label="实名认证" width="120">
          <template #default="{ row }">
            <el-tag :type="row.realNameStatus === 2 ? 'success' : row.realNameStatus === 3 ? 'danger' : 'warning'">
              {{ ['未认证', '认证中', '认证通过', '认证驳回'][row.realNameStatus] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleAudit(row)" v-if="row.realNameStatus === 1">
              审核
            </el-button>
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" :rows="2" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="实名认证状态">
              <el-select v-model="form.realNameStatus">
                <el-option label="未认证" :value="0" />
                <el-option label="认证中" :value="1" />
                <el-option label="认证通过" :value="2" />
                <el-option label="认证驳回" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status">
                <el-option label="正常" :value="1" />
                <el-option label="禁用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="auditDialogVisible" title="实名认证审核" width="500px">
      <el-form label-width="100px">
        <el-form-item label="用户姓名">
          <span>{{ auditForm.name }}</span>
        </el-form-item>
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.status">
            <el-radio :label="2">审核通过</el-radio>
            <el-radio :label="3">审核驳回</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAuditSubmit">提交审核</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPetOwners, createPetOwner, updatePetOwner, deletePetOwner, auditPetOwner } from '@/api'

const tableData = ref([])
const dialogVisible = ref(false)
const auditDialogVisible = ref(false)
const isEdit = ref(false)
const auditStatusFilter = ref(null)
const form = ref({
  name: '',
  phone: '',
  idCard: '',
  email: '',
  address: '',
  realNameStatus: 0,
  status: 1
})
const auditForm = ref({
  id: null,
  name: '',
  status: 2
})

const filteredData = computed(() => {
  if (auditStatusFilter.value !== null) {
    return tableData.value.filter(item => item.realNameStatus === auditStatusFilter.value)
  }
  return tableData.value
})

const loadData = async () => {
  const res = await getPetOwners()
  tableData.value = res.data
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    name: '',
    phone: '',
    idCard: '',
    email: '',
    address: '',
    realNameStatus: 0,
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (isEdit.value) {
    await updatePetOwner(form.value.id, form.value)
    ElMessage.success('编辑成功')
  } else {
    await createPetOwner(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deletePetOwner(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

const handleAudit = (row) => {
  auditForm.value.id = row.id
  auditForm.value.name = row.name
  auditForm.value.status = 2
  auditDialogVisible.value = true
}

const handleAuditSubmit = async () => {
  await auditPetOwner(auditForm.value.id, auditForm.value.status)
  ElMessage.success('审核成功')
  auditDialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
