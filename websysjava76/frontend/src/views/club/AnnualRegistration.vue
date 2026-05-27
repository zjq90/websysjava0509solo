<template>
  <div class="annual-registration">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>社团年度注册</span>
          <div class="header-actions">
            <el-select v-model="queryParams.status" placeholder="状态筛选" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="待审核" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已驳回" value="REJECTED" />
            </el-select>
            <el-select v-model="queryParams.year" placeholder="年份" style="width: 120px; margin-right: 10px" clearable @change="loadData">
              <el-option v-for="y in years" :key="y" :label="y + '年'" :value="y" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clubName" label="社团名称" width="150" />
        <el-table-column prop="year" label="年度" width="100" align="center">
          <template #default="{ row }">
            {{ row.year }}年
          </template>
        </el-table-column>
        <el-table-column prop="summary" label="年度总结" show-overflow-tooltip />
        <el-table-column prop="plan" label="下年计划" show-overflow-tooltip />
        <el-table-column prop="memberCount" label="成员数" width="100" align="center" />
        <el-table-column prop="activityCount" label="活动数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link @click="handleApprove(row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; text-align: right"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="年度注册详情" width="800px">
      <el-descriptions :column="2" border v-if="currentRegistration">
        <el-descriptions-item label="社团名称">{{ currentRegistration.clubName }}</el-descriptions-item>
        <el-descriptions-item label="注册年度">{{ currentRegistration.year }}年</el-descriptions-item>
        <el-descriptions-item label="成员数量">{{ currentRegistration.memberCount }}</el-descriptions-item>
        <el-descriptions-item label="活动数量">{{ currentRegistration.activityCount }}</el-descriptions-item>
        <el-descriptions-item label="年度总结" :span="2">
          <div style="max-height: 150px; overflow-y: auto;">
            {{ currentRegistration.summary }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="下年计划" :span="2">
          <div style="max-height: 150px; overflow-y: auto;">
            {{ currentRegistration.plan }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentRegistration.status !== 'PENDING'" label="审核人">
          {{ currentRegistration.approverName }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentRegistration.status !== 'PENDING'" label="审核时间">
          {{ formatDate(currentRegistration.approveTime) }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentRegistration.status !== 'PENDING'" label="审核意见" :span="2">
          {{ currentRegistration.approveOpinion }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div v-if="currentRegistration?.status === 'PENDING'">
          <el-button type="success" @click="showApproveDialog(true)">通过</el-button>
          <el-button type="danger" @click="showApproveDialog(false)">驳回</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="approveDialogVisible" :title="isApprove ? '通过注册' : '驳回注册'" width="500px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审核意见">
          <el-input
            v-model="approveForm.opinion"
            type="textarea"
            :rows="4"
            :placeholder="isApprove ? '请输入审核通过意见' : '请输入驳回原因'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button :type="isApprove ? 'success' : 'danger'" @click="submitApprove">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { clubApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const detailDialogVisible = ref(false)
const approveDialogVisible = ref(false)
const currentRegistration = ref(null)
const isApprove = ref(true)

const currentYear = new Date().getFullYear()
const years = ref([currentYear, currentYear - 1, currentYear - 2])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  status: '',
  year: null
})

const approveForm = ref({
  opinion: ''
})

const getStatusText = (status) => {
  const statuses = {
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已驳回'
  }
  return statuses[status] || status
}

const getStatusTagType = (status) => {
  const types = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger'
  }
  return types[status] || ''
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...queryParams.value }
    if (!params.status) delete params.status
    if (!params.year) delete params.year
    const res = await clubApi.getAnnualRegistrationList(params)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleView = (row) => {
  currentRegistration.value = row
  detailDialogVisible.value = true
}

const handleApprove = (row) => {
  currentRegistration.value = row
  detailDialogVisible.value = true
}

const showApproveDialog = (approve) => {
  isApprove.value = approve
  approveForm.value.opinion = ''
  approveDialogVisible.value = true
}

const submitApprove = async () => {
  try {
    await clubApi.approveAnnualRegistration(
      currentRegistration.value.id,
      isApprove.value,
      approveForm.value.opinion,
      1,
      '管理员'
    )
    ElMessage.success(isApprove.value ? '已通过注册' : '已驳回注册')
    approveDialogVisible.value = false
    detailDialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.annual-registration {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
