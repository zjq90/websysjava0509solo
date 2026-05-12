<template>
  <div class="doctor-station">
    <el-card class="patient-card">
      <template #header>
        <span>当前就诊患者</span>
      </template>
      <el-empty v-if="!currentPatient" description="暂无患者就诊" />
      <div v-else class="patient-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="患者姓名">{{ currentPatient.patient?.name }}</el-descriptions-item>
          <el-descriptions-item label="排队号">{{ currentPatient.queueNumber }}</el-descriptions-item>
          <el-descriptions-item label="科室">{{ currentPatient.department?.deptName }}</el-descriptions-item>
          <el-descriptions-item label="医生">{{ currentPatient.doctor?.name }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-card class="record-card" style="margin-top: 20px">
      <template #header>
        <span>病历记录</span>
      </template>
      <el-form :model="recordForm" label-width="100px">
        <el-form-item label="主诉">
          <el-input v-model="recordForm.chiefComplaint" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="现病史">
          <el-input v-model="recordForm.presentIllness" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="诊断">
          <el-input v-model="recordForm.diagnosis" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="处方">
          <el-table :data="prescriptionItems" border style="width: 100%">
            <el-table-column prop="drugName" label="药品名称" />
            <el-table-column prop="dosage" label="剂量" />
            <el-table-column prop="usage" label="用法" />
            <el-table-column label="操作">
              <template #default="{ $index }">
                <el-button size="small" type="danger" @click="removeDrug($index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-button type="primary" size="small" style="margin-top: 10px" @click="showAddDrug">添加药品</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveRecord">保存病历</el-button>
          <el-button type="success" @click="completeVisit">完成就诊</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-dialog v-model="drugDialogVisible" title="添加药品" width="500px">
      <el-form :model="drugForm" label-width="80px">
        <el-form-item label="药品名称">
          <el-input v-model="drugForm.drugName" />
        </el-form-item>
        <el-form-item label="剂量">
          <el-input v-model="drugForm.dosage" />
        </el-form-item>
        <el-form-item label="用法">
          <el-input v-model="drugForm.usage" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="drugDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addDrug">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appointmentApi } from '../api'

const currentPatient = ref(null)
const recordForm = ref({
  chiefComplaint: '',
  presentIllness: '',
  diagnosis: ''
})
const prescriptionItems = ref([])
const drugDialogVisible = ref(false)
const drugForm = ref({
  drugName: '',
  dosage: '',
  usage: ''
})

const loadCurrentPatient = async () => {
  try {
    const res = await appointmentApi.getByStatus('已叫号')
    if (res.code === 200 && res.data && res.data.length > 0) {
      currentPatient.value = res.data[0]
    }
  } catch (error) {
    ElMessage.error('加载患者信息失败')
  }
}

const saveRecord = () => {
  ElMessage.success('病历保存成功')
}

const completeVisit = async () => {
  if (currentPatient.value) {
    try {
      const res = await appointmentApi.complete(currentPatient.value.id)
      if (res.code === 200) {
        ElMessage.success('就诊完成')
        currentPatient.value = null
        recordForm.value = { chiefComplaint: '', presentIllness: '', diagnosis: '' }
        prescriptionItems.value = []
      }
    } catch (error) {
      ElMessage.error('操作失败')
    }
  } else {
    ElMessage.warning('没有正在就诊的患者')
  }
}

const showAddDrug = () => {
  drugDialogVisible.value = true
}

const addDrug = () => {
  prescriptionItems.value.push({ ...drugForm.value })
  drugForm.value = { drugName: '', dosage: '', usage: '' }
  drugDialogVisible.value = false
  ElMessage.success('药品添加成功')
}

const removeDrug = (index) => {
  prescriptionItems.value.splice(index, 1)
}

onMounted(() => {
  loadCurrentPatient()
})
</script>

<style scoped>
.doctor-station {
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.doctor-station :deep(.el-card) {
  width: 100%;
}

.patient-info {
  padding: 20px 0;
}
</style>
