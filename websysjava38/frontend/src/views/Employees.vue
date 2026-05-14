<template>
  <div class="employees">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>员工管理</span>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新增员工
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="员工姓名"
          style="width: 150px"
          clearable
        />
        <el-select v-model="searchForm.position" placeholder="职位" style="width: 150px" clearable>
          <el-option label="摄影师" value="摄影师" />
          <el-option label="化妆师" value="化妆师" />
          <el-option label="修图师" value="修图师" />
          <el-option label="设计师" value="设计师" />
          <el-option label="销售" value="销售" />
        </el-select>
        <el-button type="primary" @click="loadEmployees">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <el-table :data="employeeList" stripe border>
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="position" label="职位" width="100" />
        <el-table-column prop="specialty" label="专长" width="150" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="level" label="等级" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.level === 1" type="info">初级</el-tag>
            <el-tag v-else-if="row.level === 2" type="success">中级</el-tag>
            <el-tag v-else-if="row.level === 3" type="warning">高级</el-tag>
            <el-tag v-else-if="row.level === 4" type="danger">资深</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="100" />
        <el-table-column prop="taskCount" label="当前任务" width="100" />
        <el-table-column prop="completedCount" label="完成任务" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '在职' : '离职' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="editEmployee(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="deleteEmployee(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑员工' : '新增员工'" width="600px">
      <el-form :model="employeeForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="employeeForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="employeeForm.position" placeholder="请选择职位" style="width: 100%">
            <el-option label="摄影师" value="摄影师" />
            <el-option label="化妆师" value="化妆师" />
            <el-option label="修图师" value="修图师" />
            <el-option label="设计师" value="设计师" />
            <el-option label="销售" value="销售" />
          </el-select>
        </el-form-item>
        <el-form-item label="专长">
          <el-input v-model="employeeForm.specialty" placeholder="如：人像,婚纱,调色" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="employeeForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="employeeForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="等级">
          <el-radio-group v-model="employeeForm.level">
            <el-radio :label="1">初级</el-radio>
            <el-radio :label="2">中级</el-radio>
            <el-radio :label="3">高级</el-radio>
            <el-radio :label="4">资深</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="employeeForm.status">
            <el-radio :label="1">在职</el-radio>
            <el-radio :label="0">离职</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否外包">
          <el-radio-group v-model="employeeForm.isExternal">
            <el-radio :label="0">内部</el-radio>
            <el-radio :label="1">外包</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEmployee">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const employeeList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const searchForm = reactive({
  name: '',
  position: ''
})

const employeeForm = reactive({
  id: null,
  name: '',
  position: '摄影师',
  specialty: '',
  phone: '',
  email: '',
  level: 2,
  status: 1,
  isExternal: 0,
  rating: 5.0
})

const loadEmployees = async () => {
  try {
    const res = await request.get('/employees')
    employeeList.value = res.data || []
  } catch (error) {
    console.error('加载员工失败', error)
  }
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.position = ''
  loadEmployees()
}

const showCreateDialog = () => {
  isEdit.value = false
  employeeForm.id = null
  employeeForm.name = ''
  employeeForm.position = '摄影师'
  employeeForm.specialty = ''
  employeeForm.phone = ''
  employeeForm.email = ''
  employeeForm.level = 2
  employeeForm.status = 1
  employeeForm.isExternal = 0
  dialogVisible.value = true
}

const editEmployee = (row) => {
  isEdit.value = true
  employeeForm.id = row.id
  employeeForm.name = row.name
  employeeForm.position = row.position || '摄影师'
  employeeForm.specialty = row.specialty || ''
  employeeForm.phone = row.phone || ''
  employeeForm.email = row.email || ''
  employeeForm.level = row.level ?? 2
  employeeForm.status = row.status ?? 1
  employeeForm.isExternal = row.isExternal ?? 0
  dialogVisible.value = true
}

const submitEmployee = async () => {
  try {
    if (isEdit.value) {
      await request.put('/employees', employeeForm)
      ElMessage.success('员工更新成功')
    } else {
      await request.post('/employees', employeeForm)
      ElMessage.success('员工创建成功')
    }
    dialogVisible.value = false
    loadEmployees()
  } catch (error) {
    console.error('提交员工失败', error)
  }
}

const deleteEmployee = (row) => {
  ElMessageBox.confirm('确定要删除该员工吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/employees/${row.id}`)
      ElMessage.success('删除成功')
      loadEmployees()
    } catch (error) {
      console.error('删除员工失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadEmployees()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
