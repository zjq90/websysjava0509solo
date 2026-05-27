<template>
  <div class="club-application">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>社团成立申请</span>
          <div class="header-actions">
            <el-select v-model="queryParams.status" placeholder="状态筛选" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="待审核" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已驳回" value="REJECTED" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clubName" label="社团名称" />
        <el-table-column prop="clubType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ getTypeText(row.clubType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="initiatorName" label="发起人" width="100" />
        <el-table-column prop="initiatorPhone" label="联系电话" width="130" />
        <el-table-column prop="initiatorCount" label="发起人数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
            <el-button 
              v-if="row.status === 'PENDING'" 
              type="success" 
              link 
              @click="handleApprove(row)">审核
            </el-button>
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

    <el-dialog v-model="detailDialogVisible" title="申请详情" width="800px">
      <el-descriptions :column="2" border v-if="currentApplication">
        <el-descriptions-item label="社团名称">{{ currentApplication.clubName }}</el-descriptions-item>
        <el-descriptions-item label="社团类型">{{ getTypeText(currentApplication.clubType) }}</el-descriptions-item>
        <el-descriptions-item label="所属院系">{{ currentApplication.department }}</el-descriptions-item>
        <el-descriptions-item label="发起人">{{ currentApplication.initiatorName }}</el-descriptions-item>
        <el-descriptions-item label="发起人学号">{{ currentApplication.initiatorStudentId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentApplication.initiatorPhone }}</el-descriptions-item>
        <el-descriptions-item label="指导老师">{{ currentApplication.advisorName }}</el-descriptions-item>
        <el-descriptions-item label="发起人数">{{ currentApplication.initiatorCount }}</el-descriptions-item>
        <el-descriptions-item label="社团简介" :span="2">{{ currentApplication.description }}</el-descriptions-item>
        <el-descriptions-item label="社团章程" :span="2">
          <div style="max-height: 200px; overflow-y: auto;">
            {{ currentApplication.constitution }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentApplication.status !== 'PENDING'" label="审核人" :span="2">
          {{ currentApplication.approverName }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentApplication.status !== 'PENDING'" label="审核意见" :span="2">
          {{ currentApplication.approveOpinion }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div v-if="currentApplication?.status === 'PENDING'">
          <el-button type="success" @click="showApproveDialog(true)">通过</el-button>
          <el-button type="danger" @click="showApproveDialog(false)">驳回</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="approveDialogVisible" :title="isApprove ? '通过申请' : '驳回申请'" width="500px">
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
const currentApplication = ref(null)
const isApprove = ref(true)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  status: ''
})

const approveForm = ref({
  opinion: ''
})

const getTypeText = (type) => {
  const types = {
    ACADEMIC: '学术科技',
    CULTURAL: '文化艺术',
    SPORTS: '体育竞技',
    VOLUNTEER: '志愿服务',
    INNOVATION: '创新创业',
    HOBBY: '兴趣爱好'
  }
  return types[type] || type
}

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
    const res = await clubApi.getApplicationList(params)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleView = (row) => {
  currentApplication.value = row
  detailDialogVisible.value = true
}

const handleApprove = (row) => {
  currentApplication.value = row
  detailDialogVisible.value = true
}

const showApproveDialog = (approve) => {
  isApprove.value = approve
  approveForm.value.opinion = ''
  approveDialogVisible.value = true
}

const submitApprove = async () => {
  if (!approveForm.value.opinion) {
    ElMessage.warning('请填写审核意见')
    return
  }
  try {
    await clubApi.approveApplication(
      currentApplication.value.id,
      isApprove.value,
      approveForm.value.opinion,
      1,
      '管理员'
    )
    ElMessage.success(isApprove.value ? '已通过申请' : '已驳回申请')
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
.club-application {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
