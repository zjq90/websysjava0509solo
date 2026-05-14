<template>
  <div class="employees-page">
    <div class="page-header">
      <div class="search-area">
        <el-input
          v-model="searchName"
          placeholder="搜索员工姓名"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="loadEmployees"
        />
        <el-select
          v-model="filterPosition"
          placeholder="筛选职位"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="摄影师" value="PHOTOGRAPHER" />
          <el-option label="化妆师" value="MAKEUP_ARTIST" />
          <el-option label="选片师" value="FILM_SELECTOR" />
          <el-option label="助理" value="ASSISTANT" />
          <el-option label="经理" value="MANAGER" />
        </el-select>
        <el-button type="primary" @click="loadEmployees">搜索</el-button>
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        添加员工
      </el-button>
    </div>

    <el-card>
      <el-table :data="employees" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="employeeNo" label="工号" width="100" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="position" label="职位" width="120" :formatter="formatPosition" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'warning'">
              {{ scope.row.status === 'ACTIVE' ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="入职时间" width="150" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="mini"
              :type="scope.row.status === 'ACTIVE' ? 'warning' : 'success'"
              @click="handleStatus(scope.row)"
            >
              {{ scope.row.status === 'ACTIVE' ? '离职' : '复职' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      :title="isEdit ? '编辑员工' : '添加员工'"
      :visible.sync="showAddModal"
      width="400px"
    >
      <el-form :model="formData" label-width="80px">
        <el-form-item label="工号" prop="employeeNo">
          <el-input v-model="formData.employeeNo" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-select v-model="formData.position" placeholder="请选择职位">
            <el-option label="摄影师" value="PHOTOGRAPHER" />
            <el-option label="化妆师" value="MAKEUP_ARTIST" />
            <el-option label="选片师" value="FILM_SELECTOR" />
            <el-option label="助理" value="ASSISTANT" />
            <el-option label="经理" value="MANAGER" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { employeeApi } from '../api/employee'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const employees = ref([])
const searchName = ref('')
const filterPosition = ref('')
const showAddModal = ref(false)
const isEdit = ref(false)

const formData = ref({
  id: null,
  employeeNo: '',
  name: '',
  position: '',
  phone: '',
  email: ''
})

const loadEmployees = async () => {
  try {
    const data = await employeeApi.search(searchName.value, filterPosition.value)
    employees.value = data
  } catch (error) {
    ElMessage.error('加载员工列表失败')
  }
}

const formatPosition = (row, column) => {
  const positions = {
    PHOTOGRAPHER: '摄影师',
    MAKEUP_ARTIST: '化妆师',
    FILM_SELECTOR: '选片师',
    ASSISTANT: '助理',
    MANAGER: '经理'
  }
  return positions[row.position] || row.position
}

const handleAdd = () => {
  isEdit.value = false
  formData.value = {
    id: null,
    employeeNo: '',
    name: '',
    position: '',
    phone: '',
    email: ''
  }
  showAddModal.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  formData.value = {
    id: row.id,
    employeeNo: row.employeeNo,
    name: row.name,
    position: row.position,
    phone: row.phone,
    email: row.email
  }
  showAddModal.value = true
}

const handleStatus = async (row) => {
  try {
    const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
    await employeeApi.updateStatus(row.id, newStatus)
    ElMessage.success('状态更新成功')
    loadEmployees()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await employeeApi.update(formData.value.id, formData.value)
      ElMessage.success('修改成功')
    } else {
      await employeeApi.create(formData.value)
      ElMessage.success('添加成功')
    }
    showAddModal.value = false
    loadEmployees()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
  }
}

onMounted(() => {
  loadEmployees()
})
</script>

<style scoped>
.employees-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-area {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.filter-select {
  width: 150px;
}
</style>
