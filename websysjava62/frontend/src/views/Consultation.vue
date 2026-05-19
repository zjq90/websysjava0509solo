<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">问诊记录管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增问诊
      </el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="petName" label="宠物名称" width="120" />
        <el-table-column prop="petType" label="宠物类型" width="100" />
        <el-table-column prop="ownerName" label="主人姓名" width="100" />
        <el-table-column prop="chiefComplaint" label="主诉" show-overflow-tooltip />
        <el-table-column prop="doctorName" label="接诊医生" width="100" />
        <el-table-column prop="consultationFee" label="费用" width="100">
          <template #default="scope">¥{{ scope.row.consultationFee }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="success" @click="generateAIDiagnosis(scope.row.id)">
              AI诊断
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="quickClaim(scope.row)"
              :disabled="scope.row.status !== 'COMPLETED' || scope.row.claimed"
            >
              {{ scope.row.claimed ? '已理赔' : '申请理赔' }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog v-model="viewDialogVisible" title="问诊详情" width="700px">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="宠物名称">{{ currentRecord.petName }}</el-descriptions-item>
      <el-descriptions-item label="宠物类型">{{ currentRecord.petType }}</el-descriptions-item>
      <el-descriptions-item label="主人姓名">{{ currentRecord.ownerName }}</el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ currentRecord.ownerPhone }}</el-descriptions-item>
      <el-descriptions-item label="主诉" :span="2">{{ currentRecord.chiefComplaint }}</el-descriptions-item>
      <el-descriptions-item label="症状详情" :span="2">{{ currentRecord.symptomDetail }}</el-descriptions-item>
      <el-descriptions-item label="诊断结果" :span="2">{{ currentRecord.diagnosisResult }}</el-descriptions-item>
      <el-descriptions-item label="治疗方案" :span="2">{{ currentRecord.treatmentPlan }}</el-descriptions-item>
      <el-descriptions-item label="接诊医生">{{ currentRecord.doctorName }}</el-descriptions-item>
      <el-descriptions-item label="费用">¥{{ currentRecord.consultationFee }}</el-descriptions-item>
      <el-descriptions-item label="是否已理赔">
        <el-tag :type="currentRecord.claimed ? 'success' : 'info'">
          {{ currentRecord.claimed ? '已理赔' : '未理赔' }}
        </el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </el-dialog>

  <el-dialog v-model="quickClaimDialogVisible" title="一键理赔申请" width="500px">
    <el-form :model="quickClaimForm" :rules="quickClaimRules" ref="quickClaimFormRef" label-width="100px">
      <el-form-item label="选择保险公司" prop="companyId">
        <el-select v-model="quickClaimForm.companyId" placeholder="请选择保险公司" style="width: 100%">
          <el-option v-for="item in companyList" :key="item.id" :label="`${item.companyName} (${item.defaultClaimRate}%)`" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="保单号" prop="policyNo">
        <el-input v-model="quickClaimForm.policyNo" placeholder="请输入保单号" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="quickClaimDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitQuickClaim" :loading="quickClaimLoading">提交理赔</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const tableData = ref([])
const viewDialogVisible = ref(false)
const currentRecord = reactive({})

const getStatusType = (status) => {
  const map = { PENDING: 'warning', DIAGNOSED: 'primary', COMPLETED: 'success' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待诊断', DIAGNOSED: '已诊断', COMPLETED: '已完成' }
  return map[status] || status
}

const fetchData = async () => {
  try {
    const res = await axios.get('/api/consultation/records')
    if (res.data.code === 200) {
      tableData.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('获取数据失败')
  }
}

const handleAdd = () => {
  ElMessage.info('新增功能开发中')
}

const handleView = (row) => {
  Object.assign(currentRecord, row)
  viewDialogVisible.value = true
}

const generateAIDiagnosis = async (id) => {
  try {
    const res = await axios.post(`/api/consultation/suggestions/generate/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('AI诊断建议已生成，请前往AI诊断建议页面查看')
    }
  } catch (e) {
    ElMessage.error('生成失败')
  }
}

const quickClaimDialogVisible = ref(false)
const quickClaimForm = reactive({
  consultationId: null,
  companyId: null,
  policyNo: ''
})
const quickClaimFormRef = ref(null)
const companyList = ref([])
const quickClaimLoading = ref(false)

const quickClaimRules = {
  companyId: [{ required: true, message: '请选择保险公司', trigger: 'change' }],
  policyNo: [{ required: true, message: '请输入保单号', trigger: 'blur' }]
}

const fetchCompanyList = async () => {
  try {
    const res = await axios.get('/api/insurance/companies/active')
    if (res.data.code === 200) {
      companyList.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('获取保险公司列表失败')
  }
}

const quickClaim = async (row) => {
  quickClaimForm.consultationId = row.id
  quickClaimForm.companyId = null
  quickClaimForm.policyNo = ''
  await fetchCompanyList()
  quickClaimDialogVisible.value = true
}

const submitQuickClaim = async () => {
  if (!quickClaimFormRef.value) return
  
  await quickClaimFormRef.value.validate(async (valid) => {
    if (!valid) return

    quickClaimLoading.value = true
    try {
      const res = await axios.post(`/api/insurance/claims/quick/${quickClaimForm.consultationId}`, null, {
        params: { policyNo: quickClaimForm.policyNo, companyId: quickClaimForm.companyId }
      })
      if (res.data.code === 200) {
        ElMessage.success('一键理赔申请成功！')
        quickClaimDialogVisible.value = false
        fetchData()
      } else {
        ElMessage.error(res.data.message || '申请失败')
      }
    } catch (e) {
      ElMessage.error(e.response?.data?.message || '申请失败')
    } finally {
      quickClaimLoading.value = false
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该问诊记录吗?', '提示')
    const res = await axios.delete(`/api/consultation/records/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchData()
})
</script>
