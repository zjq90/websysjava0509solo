<template>
  <div class="page-container">
    <el-card title="用户管理">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
            <el-tag :type="getRoleType(scope.row.role)">{{ getRoleName(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.role !== 'ADMIN'" size="small" @click="handleResetPwd(scope.row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="pwdDialogVisible" title="重置密码" width="400px">
      <el-form :model="pwdForm" label-width="80px">
        <el-form-item label="用户">
          <span>{{ pwdForm.realName }} ({{ pwdForm.username }})</span>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveResetPwd" :loading="loading">确认重置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const tableData = ref([])
const pwdDialogVisible = ref(false)
const loading = ref(false)
const pwdForm = ref({ id: null, username: '', realName: '', newPassword: '' })

const getRoleName = (role) => {
  const map = { ADMIN: '管理员', CHIEF_REFEREE: '裁判长', REFEREE: '裁判', ATHLETE: '运动员' }
  return map[role] || role
}

const getRoleType = (role) => {
  const map = { ADMIN: 'danger', CHIEF_REFEREE: 'warning', REFEREE: 'primary', ATHLETE: 'success' }
  return map[role] || 'info'
}

const loadData = async () => {
  const res = await api.getUsers()
  tableData.value = res.data || []
}

const handleResetPwd = (row) => {
  pwdForm.value = { id: row.id, username: row.username, realName: row.realName, newPassword: '' }
  pwdDialogVisible.value = true
}

const saveResetPwd = async () => {
  if (!pwdForm.value.newPassword) {
    ElMessage.warning('请输入新密码')
    return
  }
  loading.value = true
  try {
    const res = await api.resetPassword(pwdForm.value.id, pwdForm.value.newPassword)
    if (res.code === 200) {
      ElMessage.success('密码重置成功')
      pwdDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '重置失败')
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
