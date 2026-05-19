<template>
  <div class="schedule-page">
    <div class="page-card">
      <div class="page-title">排班管理</div>
      
      <div class="toolbar">
        <div class="search-form">
          <el-date-picker
            v-model="searchDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
          <el-button type="primary" @click="loadData">查询</el-button>
        </div>
        <div>
          <el-upload
            :show-file-list="false"
            :before-upload="handleBeforeUpload"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            accept=".xlsx,.xls"
            style="display: inline-block; margin-right: 10px;"
          >
            <el-button type="success">
              <el-icon><Upload /></el-icon>
              批量导入
            </el-button>
          </el-upload>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增排班
          </el-button>
        </div>
      </div>

      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="doctorId" label="医生ID" width="100" />
        <el-table-column prop="departmentId" label="科室ID" width="100" />
        <el-table-column prop="scheduleDate" label="排班日期" width="120" />
        <el-table-column prop="startTime" label="开始时间" width="100" />
        <el-table-column prop="endTime" label="结束时间" width="100" />
        <el-table-column prop="maxCount" label="最大接诊数" width="120" />
        <el-table-column prop="bookedCount" label="已预约数" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑排班' : '新增排班'" width="550px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="医生ID">
          <el-input-number v-model="form.doctorId" :min="1" />
        </el-form-item>
        <el-form-item label="科室ID">
          <el-input-number v-model="form.departmentId" :min="1" />
        </el-form-item>
        <el-form-item label="排班日期">
          <el-date-picker
            v-model="form.scheduleDate"
            type="date"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间">
              <el-time-picker
                v-model="form.startTime"
                format="HH:mm"
                value-format="HH:mm"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间">
              <el-time-picker
                v-model="form.endTime"
                format="HH:mm"
                value-format="HH:mm"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="最大接诊数">
          <el-input-number v-model="form.maxCount" :min="1" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSchedules, createSchedule, updateSchedule, deleteSchedule, importSchedule } from '@/api'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const searchDate = ref(null)
const form = ref({
  doctorId: 1,
  departmentId: 1,
  scheduleDate: '',
  startTime: '08:00',
  endTime: '17:00',
  maxCount: 10,
  bookedCount: 0,
  status: 1,
  remark: ''
})

const loadData = async () => {
  const res = await getSchedules({ date: searchDate.value })
  tableData.value = res.data
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    doctorId: 1,
    departmentId: 1,
    scheduleDate: '',
    startTime: '08:00',
    endTime: '17:00',
    maxCount: 10,
    bookedCount: 0,
    status: 1,
    remark: ''
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
    await updateSchedule(form.value.id, form.value)
    ElMessage.success('编辑成功')
  } else {
    await createSchedule(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该排班吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteSchedule(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

const handleBeforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                  file.type === 'application/vnd.ms-excel'
  if (!isExcel) {
    ElMessage.error('请上传Excel文件!')
    return false
  }
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('文件大小不能超过2MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response) => {
  ElMessage.success('导入成功: ' + response.data)
  loadData()
}

const handleUploadError = () => {
  ElMessage.error('导入失败')
}

onMounted(() => {
  loadData()
})
</script>
