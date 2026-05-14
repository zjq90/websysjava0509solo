<template>
  <div class="transfer-requests-page">
    <div class="page-header">
      <el-select
        v-model="filterStatus"
        placeholder="筛选状态"
        class="filter-select"
        @change="loadRequests"
      >
        <el-option label="全部" value="" />
        <el-option label="待审批" value="PENDING" />
        <el-option label="已通过" value="APPROVED" />
        <el-option label="已拒绝" value="REJECTED" />
        <el-option label="已取消" value="CANCELLED" />
      </el-select>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        申请调班
      </el-button>
    </div>

    <el-card>
      <el-table :data="requests" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="requester.name" label="申请人" width="100" />
        <el-table-column prop="requester.position" label="职位" width="120" :formatter="formatPosition" />
        <el-table-column prop="currentScheduleId" label="原排班ID" width="100" />
        <el-table-column prop="targetScheduleId" label="目标排班ID" width="100" />
        <el-table-column prop="reason" label="申请原因" width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusDesc(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approver?.name" label="审批人" width="100" />
        <el-table-column prop="approvalRemark" label="审批意见" width="150" />
        <el-table-column prop="createTime" label="申请时间" width="150" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button
              v-if="scope.row.status === 'PENDING'"
              size="mini"
              type="primary"
              @click="handleApprove(scope.row)"
            >通过</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              size="mini"
              type="danger"
              @click="handleReject(scope.row)"
            >拒绝</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              size="mini"
              @click="handleCancel(scope.row)"
            >撤销</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      title="申请调班"
      :visible.sync="showAddModal"
      width="450px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="申请人" prop="requesterId">
          <el-select v-model="formData.requesterId" placeholder="请选择申请人">
            <el-option
              v-for="emp in employees"
              :key="emp.id"
              :label="emp.name + ' (' + getPositionDesc(emp.position) + ')'"
              :value="emp.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原排班ID" prop="currentScheduleId">
          <el-input v-model="formData.currentScheduleId" placeholder="请输入原排班ID" />
        </el-form-item>
        <el-form-item label="目标排班ID" prop="targetScheduleId">
          <el-input v-model="formData.targetScheduleId" placeholder="请输入目标排班ID" />
        </el-form-item>
        <el-form-item label="申请原因" prop="reason">
          <el-textarea v-model="formData.reason" placeholder="请输入申请原因" rows="3" />
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
import { transferRequestApi } from '../api/transferRequest'
import { employeeApi } from '../api/employee'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const requests = ref([])
const filterStatus = ref('')
const showAddModal = ref(false)
const employees = ref([])

const formData = ref({
  requesterId: null,
  currentScheduleId: null,
  targetScheduleId: null,
  reason: ''
})

const loadRequests = async () => {
  try {
    if (filterStatus.value) {
      requests.value = await transferRequestApi.getByStatus(filterStatus.value)
    } else {
      requests.value = await transferRequestApi.getAll()
    }
  } catch (error) {
    ElMessage.error('加载调班申请失败')
  }
}

const loadEmployees = async () => {
  try {
    employees.value = await employeeApi.getActive()
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
  return positions[row] || row
}

const getStatusType = (status) => {
  switch (status) {
    case 'PENDING':
      return 'warning'
    case 'APPROVED':
      return 'success'
    case 'REJECTED':
      return 'danger'
    case 'CANCELLED':
      return 'info'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    PENDING: '待审批',
    APPROVED: '已通过',
    REJECTED: '已拒绝',
    CANCELLED: '已取消'
  }
  return statusMap[status] || status
}

const getPositionDesc = (position) => {
  const positions = {
    PHOTOGRAPHER: '摄影师',
    MAKEUP_ARTIST: '化妆师',
    FILM_SELECTOR: '选片师',
    ASSISTANT: '助理',
    MANAGER: '经理'
  }
  return positions[position] || position
}

const handleApprove = async (request) => {
  try {
    await transferRequestApi.approve(request.id, {
      approverId: 5,
      approved: true,
      remark: '同意调班'
    })
    ElMessage.success('审批通过')
    loadRequests()
  } catch (error) {
    ElMessage.error('审批失败')
  }
}

const handleReject = async (request) => {
  try {
    await transferRequestApi.approve(request.id, {
      approverId: 5,
      approved: false,
      remark: '不同意调班'
    })
    ElMessage.success('已拒绝')
    loadRequests()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancel = async (request) => {
  try {
    await transferRequestApi.cancel(request.id)
    ElMessage.success('已撤销')
    loadRequests()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    await transferRequestApi.create({
      requester: { id: formData.value.requesterId },
      currentScheduleId: formData.value.currentScheduleId,
      targetScheduleId: formData.value.targetScheduleId,
      reason: formData.value.reason
    })
    ElMessage.success('申请提交成功')
    showAddModal.value = false
    loadRequests()
  } catch (error) {
    ElMessage.error('提交失败')
  }
}

onMounted(() => {
  loadRequests()
  loadEmployees()
})
</script>

<style scoped>
.transfer-requests-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-select {
  width: 150px;
}
</style>
