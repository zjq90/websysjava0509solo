<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">保险理赔管理</h2>
      <el-button type="primary" @click="handleAddClaim">
        <el-icon><Plus /></el-icon>
        申请理赔
      </el-button>
    </div>

    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索宠物名称" style="width: 250px" @keyup.enter="fetchData">
        <template #append>
          <el-button @click="fetchData">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
      <el-select v-model="statusFilter" placeholder="状态筛选" clearable @change="fetchData" style="width: 150px; margin-left: 10px">
        <el-option label="待审核" value="PENDING" />
        <el-option label="已批准" value="APPROVED" />
        <el-option label="已拒绝" value="REJECTED" />
        <el-option label="已完成" value="COMPLETED" />
        <el-option label="已取消" value="CANCELLED" />
      </el-select>
      <el-button @click="fetchData" style="margin-left: 10px">刷新</el-button>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="claimNo" label="理赔编号" width="180" />
        <el-table-column prop="consultationNo" label="问诊编号" width="150" />
        <el-table-column prop="petName" label="宠物名称" width="100" />
        <el-table-column prop="ownerName" label="申请人" width="100" />
        <el-table-column prop="insuranceCompanyName" label="保险公司" width="120" />
        <el-table-column prop="totalFee" label="总费用" width="100">
          <template #default="scope">¥{{ scope.row.totalFee }}</template>
        </el-table-column>
        <el-table-column prop="claimAmount" label="理赔金额" width="100">
          <template #default="scope">¥{{ scope.row.claimAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="160" />
        <el-table-column label="操作" width="350" fixed="right">
          <template #default="scope">
            <el-button size="small" type="success" v-if="scope.row.status === 'PENDING'" @click="handleReview(scope.row, 'APPROVED')">
              通过
            </el-button>
            <el-button size="small" type="warning" v-if="scope.row.status === 'PENDING'" @click="handleReview(scope.row, 'REJECTED')">
              拒绝
            </el-button>
            <el-button size="small" type="primary" v-if="scope.row.status === 'APPROVED'" @click="handleComplete(scope.row.id)">
              完成支付
            </el-button>
            <el-button size="small" v-if="scope.row.status === 'PENDING' || scope.row.status === 'APPROVED'" @click="handleCancel(scope.row.id)">
              取消
            </el-button>
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="claimDialogVisible" title="申请理赔" width="600px" @close="resetClaimForm">
      <el-form :model="claimForm" :rules="claimRules" ref="claimFormRef" label-width="100px">
        <el-form-item label="选择问诊" prop="consultationId">
          <el-select v-model="claimForm.consultationId" placeholder="请选择问诊记录" style="width: 100%" @change="onConsultationChange">
            <el-option v-for="item in consultationList" :key="item.id" :label="`${item.petName} - ${item.consultationNo} - ¥${item.consultationFee}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择保险公司" prop="companyId">
          <el-select v-model="claimForm.companyId" placeholder="请选择保险公司" style="width: 100%" @change="calculateClaimAmount">
            <el-option v-for="item in companyList" :key="item.id" :label="`${item.companyName} (${item.defaultClaimRate}%)`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="保单号" prop="policyNo">
          <el-input v-model="claimForm.policyNo" placeholder="请输入保单号" />
        </el-form-item>
        <el-form-item label="理赔说明" prop="claimDescription">
          <el-input v-model="claimForm.claimDescription" type="textarea" :rows="3" placeholder="请输入理赔说明（可选）" />
        </el-form-item>

        <el-divider content-position="left">理赔金额计算</el-divider>
        <el-descriptions :column="2" border v-if="calculateResult">
          <el-descriptions-item label="问诊总费用">¥{{ calculateResult.totalFee }}</el-descriptions-item>
          <el-descriptions-item label="免赔额">¥{{ calculateResult.deductible }}</el-descriptions-item>
          <el-descriptions-item label="理赔比例">{{ calculateResult.claimRate }}%</el-descriptions-item>
          <el-descriptions-item label="最高理赔限额">¥{{ calculateResult.maxClaimAmount }}</el-descriptions-item>
          <el-descriptions-item label="可理赔金额" :span="2">¥{{ calculateResult.claimableAmount }}</el-descriptions-item>
          <el-descriptions-item label="预计理赔金额" :span="2" type="success">
            <span style="color: #67c23a; font-weight: bold; font-size: 16px">¥{{ calculateResult.claimAmount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="自付金额" :span="2" type="danger">
            <span style="color: #f56c6c; font-weight: bold">¥{{ calculateResult.selfPayAmount }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <el-empty v-else description="请先选择问诊记录和保险公司" :image-size="80" />
      </el-form>
      <template #footer>
        <el-button @click="claimDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitClaim" :loading="submitLoading">提交申请</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="理赔详情" width="650px">
      <el-descriptions :column="2" border v-if="currentClaim">
        <el-descriptions-item label="理赔编号" :span="2">{{ currentClaim.claimNo }}</el-descriptions-item>
        <el-descriptions-item label="问诊编号">{{ currentClaim.consultationNo }}</el-descriptions-item>
        <el-descriptions-item label="保险公司">{{ currentClaim.insuranceCompanyName }}</el-descriptions-item>
        <el-descriptions-item label="宠物名称">{{ currentClaim.petName }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ currentClaim.ownerName }}</el-descriptions-item>
        <el-descriptions-item label="保单号">{{ currentClaim.policyNo }}</el-descriptions-item>
        <el-descriptions-item label="总费用">¥{{ currentClaim.totalFee }}</el-descriptions-item>
        <el-descriptions-item label="免赔额">¥{{ currentClaim.deductible }}</el-descriptions-item>
        <el-descriptions-item label="理赔比例">{{ currentClaim.claimRate }}%</el-descriptions-item>
        <el-descriptions-item label="理赔金额">¥{{ currentClaim.claimAmount }}</el-descriptions-item>
        <el-descriptions-item label="自付金额">¥{{ currentClaim.selfPayAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentClaim.status)">{{ getStatusText(currentClaim.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交易编号">{{ currentClaim.transactionId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="理赔说明" :span="2">{{ currentClaim.claimDescription || '-' }}</el-descriptions-item>
        <el-descriptions-item label="拒绝原因" :span="2" v-if="currentClaim.rejectReason">{{ currentClaim.rejectReason }}</el-descriptions-item>
        <el-descriptions-item label="审核人" v-if="currentClaim.reviewedBy">{{ currentClaim.reviewedBy }}</el-descriptions-item>
        <el-descriptions-item label="审核时间" v-if="currentClaim.reviewedAt">{{ currentClaim.reviewedAt }}</el-descriptions-item>
        <el-descriptions-item label="申请时间" :span="2">{{ currentClaim.createdAt }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rejectDialogVisible" title="拒绝理赔" width="500px">
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="拒绝原因">
          <el-input v-model="rejectForm.rejectReason" type="textarea" :rows="3" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmReject">确认拒绝</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import axios from 'axios'

const tableData = ref([])
const loading = ref(false)
const searchKeyword = ref('')
const statusFilter = ref('')

const claimDialogVisible = ref(false)
const claimFormRef = ref(null)
const claimForm = reactive({
  consultationId: null,
  companyId: null,
  policyNo: '',
  claimDescription: ''
})
const claimRules = {
  consultationId: [{ required: true, message: '请选择问诊记录', trigger: 'change' }],
  companyId: [{ required: true, message: '请选择保险公司', trigger: 'change' }],
  policyNo: [{ required: true, message: '请输入保单号', trigger: 'blur' }]
}
const calculateResult = ref(null)
const submitLoading = ref(false)

const detailDialogVisible = ref(false)
const currentClaim = ref(null)

const rejectDialogVisible = ref(false)
const rejectForm = reactive({
  id: null,
  rejectReason: ''
})

const consultationList = ref([])
const companyList = ref([])

const getStatusType = (status) => {
  const map = { PENDING: 'warning', APPROVED: 'primary', REJECTED: 'danger', COMPLETED: 'success', CANCELLED: 'info' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { PENDING: '待审核', APPROVED: '已批准', REJECTED: '已拒绝', COMPLETED: '已完成', CANCELLED: '已取消' }
  return map[status] || status
}

const fetchData = async () => {
  loading.value = true
  try {
    let url = '/api/insurance/claims'
    const params = {}
    if (statusFilter.value) {
      url = `/api/insurance/claims/status/${statusFilter.value}`
    } else if (searchKeyword.value) {
      url = '/api/insurance/claims/search'
      params.petName = searchKeyword.value
    }
    const res = await axios.get(url, { params })
    if (res.data.code === 200) {
      tableData.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const fetchConsultationList = async () => {
  try {
    const res = await axios.get('/api/consultation/records')
    if (res.data.code === 200) {
      consultationList.value = res.data.data.filter(item => item.status === 'COMPLETED' && !item.claimed)
    }
  } catch (e) {
    ElMessage.error('获取问诊列表失败')
  }
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

const handleAddClaim = async () => {
  await fetchConsultationList()
  await fetchCompanyList()
  claimDialogVisible.value = true
}

const onConsultationChange = () => {
  if (claimForm.consultationId && claimForm.companyId) {
    calculateClaimAmount()
  }
}

const calculateClaimAmount = async () => {
  if (!claimForm.consultationId || !claimForm.companyId) return
  
  const consultation = consultationList.value.find(c => c.id === claimForm.consultationId)
  if (!consultation) return

  try {
    const res = await axios.post('/api/insurance/calculate', null, {
      params: {
        companyId: claimForm.companyId,
        totalFee: consultation.consultationFee
      }
    })
    if (res.data.code === 200) {
      calculateResult.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('计算理赔金额失败')
  }
}

const submitClaim = async () => {
  if (!claimFormRef.value) return
  
  await claimFormRef.value.validate(async (valid) => {
    if (!valid) return

    const consultation = consultationList.value.find(c => c.id === claimForm.consultationId)
    if (!consultation) {
      ElMessage.error('请选择有效的问诊记录')
      return
    }

    submitLoading.value = true
    try {
      const requestData = {
        consultationId: claimForm.consultationId,
        insuranceCompanyId: claimForm.companyId,
        policyNo: claimForm.policyNo,
        claimDescription: claimForm.claimDescription,
        totalFee: consultation.consultationFee
      }

      const res = await axios.post('/api/insurance/claims', requestData)
      if (res.data.code === 200) {
        ElMessage.success('理赔申请提交成功！')
        claimDialogVisible.value = false
        fetchData()
      } else {
        ElMessage.error(res.data.message || '提交失败')
      }
    } catch (e) {
      ElMessage.error(e.response?.data?.message || '提交失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const resetClaimForm = () => {
  claimForm.consultationId = null
  claimForm.companyId = null
  claimForm.policyNo = ''
  claimForm.claimDescription = ''
  calculateResult.value = null
  if (claimFormRef.value) {
    claimFormRef.value.resetFields()
  }
}

const handleReview = async (row, status) => {
  if (status === 'REJECTED') {
    rejectForm.id = row.id
    rejectForm.rejectReason = ''
    rejectDialogVisible.value = true
    return
  }

  try {
    await ElMessageBox.confirm('确定通过该理赔申请吗？', '提示')
    const res = await axios.put(`/api/insurance/claims/${row.id}/review`, null, {
      params: { status: 'APPROVED', reviewedBy: '管理员' }
    })
    if (res.data.code === 200) {
      ElMessage.success('已通过')
      fetchData()
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

const confirmReject = async () => {
  if (!rejectForm.rejectReason) {
    ElMessage.warning('请输入拒绝原因')
    return
  }

  try {
    const res = await axios.put(`/api/insurance/claims/${rejectForm.id}/review`, null, {
      params: { status: 'REJECTED', reviewedBy: '管理员', rejectReason: rejectForm.rejectReason }
    })
    if (res.data.code === 200) {
      ElMessage.success('已拒绝')
      rejectDialogVisible.value = false
      fetchData()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleComplete = async (id) => {
  try {
    await ElMessageBox.confirm('确定完成该理赔支付吗？', '提示')
    const res = await axios.put(`/api/insurance/claims/${id}/complete`)
    if (res.data.code === 200) {
      ElMessage.success('已完成支付')
      fetchData()
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

const handleCancel = async (id) => {
  try {
    await ElMessageBox.confirm('确定取消该理赔申请吗？', '提示')
    const res = await axios.put(`/api/insurance/claims/${id}/cancel`)
    if (res.data.code === 200) {
      ElMessage.success('已取消')
      fetchData()
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('操作失败')
  }
}

const handleView = async (row) => {
  try {
    const res = await axios.get(`/api/insurance/claims/${row.id}`)
    if (res.data.code === 200) {
      currentClaim.value = res.data.data
      detailDialogVisible.value = true
    }
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该理赔记录吗？', '提示')
    const res = await axios.delete(`/api/insurance/claims/${id}`)
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

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.search-bar {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}
</style>
