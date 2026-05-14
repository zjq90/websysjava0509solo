<template>
  <div class="schedules-page">
    <div class="page-header">
      <div class="date-selector">
        <span>选择日期:</span>
        <el-date-picker
          v-model="selectedDate"
          type="date"
          placeholder="选择日期"
          @change="loadSchedules"
        />
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        添加排班
      </el-button>
    </div>

    <div class="schedule-container">
      <div class="employees-list">
        <div
          v-for="employee in employees"
          :key="employee.id"
          class="employee-item"
          :class="{ active: selectedEmployee?.id === employee.id }"
          @click="selectEmployee(employee)"
        >
          <div class="employee-avatar">
            {{ employee.name.charAt(0) }}
          </div>
          <div class="employee-info">
            <div class="employee-name">{{ employee.name }}</div>
            <div class="employee-position">{{ getPositionDesc(employee.position) }}</div>
          </div>
        </div>
      </div>

      <div class="schedule-content">
        <div class="time-slots">
          <div
            v-for="timeSlot in timeSlots"
            :key="timeSlot.time"
            class="time-slot"
          >
            <div class="time-label">{{ timeSlot.time }}</div>
            <div
              class="slot-content"
              :class="{ hasSchedule: hasScheduleAt(timeSlot.time) }"
              @click="handleSlotClick(timeSlot)"
            >
              <div
                v-for="schedule in getSchedulesAt(timeSlot.time)"
                :key="schedule.id"
                class="schedule-block"
                :class="getScheduleClass(schedule)"
              >
                <div class="schedule-customer">{{ schedule.customerName }}</div>
                <div class="schedule-theme">{{ schedule.shootingTheme }}</div>
                <div class="schedule-actions">
                  <el-button size="mini" @click.stop="handleEditSchedule(schedule)">编辑</el-button>
                  <el-button size="mini" @click.stop="handleCompleteSchedule(schedule)" v-if="schedule.status === 'SCHEDULED'">完成</el-button>
                  <el-button size="mini" type="danger" @click.stop="handleCancelSchedule(schedule)" v-if="schedule.status === 'SCHEDULED'">取消</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog
      :title="isEdit ? '编辑排班' : '添加排班'"
      :visible.sync="showAddModal"
      width="450px"
    >
      <el-form :model="formData" label-width="100px">
        <el-form-item label="员工" prop="employeeId">
          <el-select v-model="formData.employeeId" placeholder="请选择员工">
            <el-option
              v-for="emp in employees"
              :key="emp.id"
              :label="emp.name + ' (' + getPositionDesc(emp.position) + ')'"
              :value="emp.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="scheduleDate">
          <el-date-picker v-model="formData.scheduleDate" type="date" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-time-picker v-model="formData.startTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-time-picker v-model="formData.endTime" format="HH:mm" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="拍摄主题" prop="shootingTheme">
          <el-input v-model="formData.shootingTheme" placeholder="请输入拍摄主题" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-textarea v-model="formData.remark" placeholder="请输入备注" rows="2" />
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
import { ref, onMounted, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { employeeApi } from '../api/employee'
import { scheduleApi } from '../api/schedule'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const employees = ref([])
const selectedDate = ref(new Date().toISOString().split('T')[0])
const selectedEmployee = ref(null)
const showAddModal = ref(false)
const isEdit = ref(false)

const timeSlots = [
  { time: '08:00', start: '08:00', end: '09:00' },
  { time: '09:00', start: '09:00', end: '10:00' },
  { time: '10:00', start: '10:00', end: '11:00' },
  { time: '11:00', start: '11:00', end: '12:00' },
  { time: '12:00', start: '12:00', end: '13:00' },
  { time: '13:00', start: '13:00', end: '14:00' },
  { time: '14:00', start: '14:00', end: '15:00' },
  { time: '15:00', start: '15:00', end: '16:00' },
  { time: '16:00', start: '16:00', end: '17:00' },
  { time: '17:00', start: '17:00', end: '18:00' },
  { time: '18:00', start: '18:00', end: '19:00' },
  { time: '19:00', start: '19:00', end: '20:00' }
]

const formData = ref({
  id: null,
  employeeId: null,
  scheduleDate: new Date().toISOString().split('T')[0],
  startTime: '09:00',
  endTime: '10:00',
  customerName: '',
  shootingTheme: '',
  remark: ''
})

const currentSchedules = computed(() => {
  if (!selectedEmployee.value) return []
  return schedules.value.filter(s => s.employee.id === selectedEmployee.value.id)
})

const schedules = ref([])

const loadEmployees = async () => {
  try {
    employees.value = await employeeApi.getActive()
    if (employees.value.length > 0) {
      selectEmployee(employees.value[0])
    }
  } catch (error) {
    ElMessage.error('加载员工列表失败')
  }
}

const loadSchedules = async () => {
  if (!selectedEmployee.value) return
  try {
    schedules.value = await scheduleApi.getByDate(selectedDate.value)
  } catch (error) {
    ElMessage.error('加载排班失败')
  }
}

const selectEmployee = (employee) => {
  selectedEmployee.value = employee
  loadSchedules()
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

const hasScheduleAt = (time) => {
  return currentSchedules.value.some(s => {
    return s.startTime <= time.start && s.endTime > time.start
  })
}

const getSchedulesAt = (time) => {
  return currentSchedules.value.filter(s => {
    return s.startTime <= time.start && s.endTime > time.start
  })
}

const getScheduleClass = (schedule) => {
  switch (schedule.status) {
    case 'COMPLETED':
      return 'completed'
    case 'CANCELLED':
      return 'cancelled'
    default:
      return 'scheduled'
  }
}

const handleSlotClick = (timeSlot) => {
  isEdit.value = false
  formData.value = {
    id: null,
    employeeId: selectedEmployee.value?.id,
    scheduleDate: selectedDate.value,
    startTime: timeSlot.start,
    endTime: timeSlot.end,
    customerName: '',
    shootingTheme: '',
    remark: ''
  }
  showAddModal.value = true
}

const handleEditSchedule = (schedule) => {
  isEdit.value = true
  formData.value = {
    id: schedule.id,
    employeeId: schedule.employee.id,
    scheduleDate: schedule.scheduleDate,
    startTime: schedule.startTime,
    endTime: schedule.endTime,
    customerName: schedule.customerName,
    shootingTheme: schedule.shootingTheme,
    remark: schedule.remark
  }
  showAddModal.value = true
}

const handleCompleteSchedule = async (schedule) => {
  try {
    await scheduleApi.complete(schedule.id)
    ElMessage.success('已标记完成')
    loadSchedules()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleCancelSchedule = async (schedule) => {
  try {
    await scheduleApi.cancel(schedule.id)
    ElMessage.success('已取消')
    loadSchedules()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    const data = {
      employee: { id: formData.value.employeeId },
      scheduleDate: formData.value.scheduleDate,
      startTime: formData.value.startTime,
      endTime: formData.value.endTime,
      customerName: formData.value.customerName,
      shootingTheme: formData.value.shootingTheme,
      remark: formData.value.remark
    }
    if (isEdit.value) {
      await scheduleApi.update(formData.value.id, data)
      ElMessage.success('修改成功')
    } else {
      await scheduleApi.create(data)
      ElMessage.success('添加成功')
    }
    showAddModal.value = false
    loadSchedules()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
  }
}

onMounted(() => {
  loadEmployees()
})
</script>

<style scoped>
.schedules-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.date-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}

.schedule-container {
  display: flex;
  gap: 20px;
}

.employees-list {
  width: 220px;
  background: white;
  border-radius: 8px;
  padding: 10px;
  max-height: 600px;
  overflow-y: auto;
}

.employee-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
  transition: all 0.3s;
}

.employee-item:hover {
  background: #f5f5f5;
}

.employee-item.active {
  background: #e8f4fd;
  border: 1px solid #409eff;
}

.employee-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 600;
}

.employee-name {
  font-weight: 600;
}

.employee-position {
  font-size: 12px;
  color: #999;
}

.schedule-content {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
}

.time-slots {
  display: flex;
  flex-direction: column;
}

.time-slot {
  display: flex;
  height: 60px;
  border-bottom: 1px solid #eee;
}

.time-label {
  width: 60px;
  padding: 10px;
  font-size: 12px;
  color: #999;
}

.slot-content {
  flex: 1;
  padding: 5px;
  position: relative;
}

.slot-content.hasSchedule {
  background: #fafafa;
}

.schedule-block {
  padding: 8px;
  border-radius: 6px;
  margin-bottom: 4px;
  cursor: pointer;
}

.schedule-block.scheduled {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.schedule-block.completed {
  background: #e8f5e9;
  color: #388e3c;
}

.schedule-block.cancelled {
  background: #fce4ec;
  color: #c2185b;
  opacity: 0.7;
}

.schedule-customer {
  font-weight: 600;
  font-size: 13px;
}

.schedule-theme {
  font-size: 11px;
  opacity: 0.8;
}

.schedule-actions {
  margin-top: 5px;
  display: none;
}

.schedule-block:hover .schedule-actions {
  display: flex;
  gap: 4px;
}

.schedule-actions .el-button {
  padding: 2px 6px;
  font-size: 10px;
}
</style>
