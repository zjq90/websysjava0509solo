<template>
  <div class="queue-page">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span class="card-title">当前叫号</span>
          </template>
          <div class="current-queue">
            <div class="queue-number">{{ currentNumber }}</div>
            <div class="queue-info">
              <p>科室：{{ currentDepartment }}</p>
              <p>医生：{{ currentDoctor }}</p>
              <p>诊室：{{ currentRoom }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span class="card-title">等待人数</span>
          </template>
          <div class="waiting-count">
            <div class="count">{{ waitingCount }}</div>
            <div class="count-label">人正在等待</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="queue-list-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>等待队列</span>
          <el-button type="primary" @click="callNext">下一位</el-button>
        </div>
      </template>

      <el-table :data="waitingList" border stripe>
        <el-table-column prop="queueNumber" label="排队号" width="120" />
        <el-table-column prop="patient.name" label="患者姓名" width="150" />
        <el-table-column prop="doctor.name" label="医生" width="150" />
        <el-table-column prop="department.deptName" label="科室" width="150" />
        <el-table-column prop="appointmentTime" label="预约时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" type="primary" @click="callPatient(row)">
              叫号
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '../api'

const waitingList = ref([])
const currentNumber = ref('--')
const currentDepartment = ref('--')
const currentDoctor = ref('--')
const currentRoom = ref('--')

const waitingCount = computed(() => waitingList.value.length)

const loadWaitingList = async () => {
  try {
    const res = await appointmentApi.getByStatus('已预约')
    if (res.code === 200) {
      waitingList.value = res.data || []
    }
  } catch (error) {
    ElMessage.error('加载队列失败')
  }
}

const callPatient = async (row) => {
  try {
    const res = await appointmentApi.callNumber(row.id)
    if (res.code === 200) {
      currentNumber.value = row.queueNumber
      currentDepartment.value = row.department?.deptName || '--'
      currentDoctor.value = row.doctor?.name || '--'
      currentRoom.value = '101'
      ElMessage.success('叫号成功')
      loadWaitingList()
    }
  } catch (error) {
    ElMessage.error('叫号失败')
  }
}

const callNext = () => {
  if (waitingList.value.length > 0) {
    callPatient(waitingList.value[0])
  } else {
    ElMessage.info('没有等待的患者')
  }
}

onMounted(() => {
  loadWaitingList()
})
</script>

<style scoped>
.queue-page {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.queue-page :deep(.el-card) {
  width: 100%;
}

.queue-page :deep(.el-table) {
  width: 100%;
}

.queue-page :deep(.el-col) {
  width: 50%;
}

.card-title {
  font-weight: bold;
  font-size: 16px;
}

.current-queue {
  text-align: center;
  padding: 30px 0;
}

.queue-number {
  font-size: 72px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 20px;
}

.queue-info p {
  margin: 10px 0;
  font-size: 16px;
  color: #606266;
}

.waiting-count {
  text-align: center;
  padding: 40px 0;
}

.count {
  font-size: 64px;
  font-weight: bold;
  color: #67C23A;
}

.count-label {
  font-size: 18px;
  color: #606266;
  margin-top: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
