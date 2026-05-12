<template>
  <div class="appointment-page">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="日期">
          <el-date-picker
            v-model="filterForm.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="filterForm.status" placeholder="选择状态" clearable>
            <el-option label="已预约" value="已预约" />
            <el-option label="已叫号" value="已叫号" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
          <el-button type="primary" @click="showAddDialog">新增预约</el-button>
        </div>
      </template>

      <el-table :data="appointmentList" border stripe>
        <el-table-column prop="queueNumber" label="排队号" width="100" />
        <el-table-column prop="patient.name" label="患者姓名" width="120" />
        <el-table-column prop="doctor.name" label="医生姓名" width="120" />
        <el-table-column prop="department.deptName" label="科室" width="120" />
        <el-table-column prop="appointmentTime" label="预约时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.appointmentDate) }} {{ row.appointmentTime }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="callNumber(row)" :disabled="row.status !== '已预约'">
            叫号
            </el-button>
            <el-button size="small" type="success" @click="complete(row)" :disabled="row.status !== '已叫号'">
              完成
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" title="新增预约" width="600px">
      <el-form :model="appointmentForm" label-width="100px">
        <el-form-item label="患者">
          <el-input v-model="appointmentForm.patientName" placeholder="请输入患者姓名" />
        </el-form-item>
        <el-form-item label="医生">
          <el-select v-model="appointmentForm.doctorId" placeholder="请选择医生" style="width: 100%">
            <el-option
              v-for="doctor in doctorList"
              :key="doctor.id"
              :label="doctor.name"
              :value="doctor.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期">
          <el-date-picker
            v-model="appointmentForm.appointmentDate"
            type="date"
            style="width: 100%"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="预约时间">
          <el-time-picker
            v-model="appointmentForm.appointmentTime"
            format="HH:mm"
            value-format="HH:mm"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi, doctorApi } from '../api'

const filterForm = ref({
  date: new Date().toISOString().split('T')[0],
  status: ''
})

const appointmentList = ref([])
const doctorList = ref([])
const dialogVisible = ref(false)
const appointmentForm = ref({
  patientName: '',
  doctorId: null,
  appointmentDate: new Date().toISOString().split('T')[0],
  appointmentTime: '09:00'
})

const loadAppointments = async () => {
  try {
    const res = await appointmentApi.getAll()
    if (res.code === 200) {
      appointmentList.value = res.data || []
    }
  } catch (error) {
    ElMessage.error('加载预约列表失败')
  }
}

const loadDoctors = async () => {
  try {
    const res = await doctorApi.getAll()
    if (res.code === 200) {
      doctorList.value = res.data || []
    }
  } catch (error) {
    ElMessage.error('加载医生列表失败')
  }
}

const search = () => {
  loadAppointments()
}

const reset = () => {
  filterForm.value = {
    date: new Date().toISOString().split('T')[0],
    status: ''
  }
  loadAppointments()
}

const showAddDialog = () => {
  dialogVisible.value = true
}

const submitForm = async () => {
  try {
    ElMessage.success('预约添加成功')
    dialogVisible.value = false
    loadAppointments()
  } catch (error) {
    ElMessage.error('添加预约失败')
  }
}

const callNumber = async (row) => {
  try {
    const res = await appointmentApi.callNumber(row.id)
    if (res.code === 200) {
      ElMessage.success('叫号成功')
      loadAppointments()
    }
  } catch (error) {
    ElMessage.error('叫号失败')
  }
}

const complete = async (row) => {
  try {
    const res = await appointmentApi.complete(row.id)
    if (res.code === 200) {
      ElMessage.success('完成就诊')
      loadAppointments()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const getStatusType = (status) => {
  const map = {
    '已预约': 'warning',
    '已叫号': 'primary',
    '已完成': 'success'
  }
  return map[status] || 'info'
}

const formatDateTime = (date) => {
  return date
}

onMounted(() => {
  loadAppointments()
  loadDoctors()
})
</script>

<style scoped>
.appointment-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.appointment-page :deep(.el-card) {
  width: 100%;
}

.appointment-page :deep(.el-table) {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
