<template>
  <div class="user-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户权限管理</span>
          <div class="header-actions">
            <el-select v-model="queryParams.role" placeholder="角色筛选" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="校级管理员" value="SCHOOL_ADMIN" />
              <el-option label="社团管理者" value="CLUB_ADMIN" />
              <el-option label="普通用户" value="MEMBER" />
            </el-select>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增用户
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="studentId" label="学号" width="120" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="department" label="院系" width="150" />
        <el-table-column prop="role" label="角色" width="130">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.role)">{{ getRoleText(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="clubName" label="负责社团" width="150" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="success" link @click="handleAssignRole(row)">分配权限</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; text-align: right"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" title="分配权限" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户姓名">
          <span>{{ currentUser?.name }}</span>
        </el-form-item>
        <el-form-item label="当前角色">
          <el-tag>{{ getRoleText(currentUser?.role) }}</el-tag>
        </el-form-item>
        <el-form-item label="分配角色">
          <el-radio-group v-model="form.role">
            <el-radio label="SCHOOL_ADMIN">校级管理员</el-radio>
            <el-radio label="CLUB_ADMIN">社团管理者</el-radio>
            <el-radio label="MEMBER">普通用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.role === 'CLUB_ADMIN'" label="负责社团">
          <el-select v-model="form.clubId" placeholder="请选择社团" style="width: 100%">
            <el-option v-for="club in clubs" :key="club.id" :label="club.name" :value="club.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssign">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { systemApi, clubApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentUser = ref(null)
const clubs = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  role: ''
})

const form = ref({
  role: '',
  clubId: null
})

const getRoleText = (role) => {
  const roles = {
    SCHOOL_ADMIN: '校级管理员',
    CLUB_ADMIN: '社团管理者',
    MEMBER: '普通用户'
  }
  return roles[role] || role
}

const getRoleTagType = (role) => {
  const types = {
    SCHOOL_ADMIN: 'danger',
    CLUB_ADMIN: 'warning',
    MEMBER: 'info'
  }
  return types[role] || ''
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...queryParams.value }
    if (!params.role) delete params.role
    const res = await systemApi.getUserList(params)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const loadClubs = async () => {
  try {
    const res = await clubApi.getAllClubs()
    clubs.value = Array.isArray(res) ? res : (res.content || [])
  } catch (e) {
    console.error(e)
  }
}

const handleAdd = () => {
  ElMessage.info('新增用户功能')
}

const handleEdit = (row) => {
  ElMessage.info(`编辑用户: ${row.name}`)
}

const handleAssignRole = (row) => {
  currentUser.value = row
  form.value.role = row.role
  form.value.clubId = row.clubId
  dialogVisible.value = true
}

const submitAssign = async () => {
  try {
    await systemApi.assignRole(currentUser.value.id, form.value.role, form.value.clubId)
    ElMessage.success('权限分配成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await systemApi.deleteUser(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
  loadClubs()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
