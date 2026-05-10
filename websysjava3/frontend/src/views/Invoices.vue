<template>
  <div class="page-container">
    <div class="page-title">发票管理</div>
    
    <el-form :inline="true" :model="searchForm" class="search-bar">
      <el-form-item label="订单号">
        <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
      </el-form-item>
      <el-form-item label="用户名称">
        <el-input v-model="searchForm.userName" placeholder="请输入用户名称" clearable />
      </el-form-item>
      <el-form-item label="发票类型">
        <el-select v-model="searchForm.invoiceType" placeholder="请选择发票类型" clearable>
          <el-option label="个人" value="PERSONAL" />
          <el-option label="企业" value="ENTERPRISE" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="待审核" value="PENDING" />
          <el-option label="审核通过" value="APPROVED" />
          <el-option label="已拒绝" value="REJECTED" />
          <el-option label="已开具" value="ISSUED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>搜索
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="userName" label="用户名称" width="100" />
      <el-table-column prop="invoiceType" label="发票类型" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.invoiceType === 'ENTERPRISE' ? 'primary' : 'info'" class="status-tag">
            {{ scope.row.invoiceType === 'ENTERPRISE' ? '企业' : '个人' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="invoiceTitle" label="发票抬头" show-overflow-tooltip />
      <el-table-column prop="amount" label="开票金额" width="100">
        <template #default="scope">¥{{ scope.row.amount }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)" class="status-tag">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="申请时间" width="180" />
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="scope">
          <el-button type="primary" link size="small" @click="handleView(scope.row)">
            详情
          </el-button>
          <el-button 
            v-if="scope.row.status === 'PENDING'"
            type="success" 
            link 
            size="small" 
            @click="showAuditDialog(scope.row)"
          >
            审核
          </el-button>
          <el-button 
            v-if="scope.row.status === 'APPROVED'"
            type="warning" 
            link 
            size="small" 
            @click="showIssueDialog(scope.row)"
          >
            开具发票
          </el-button>
          <el-button type="danger" link size="small" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog v-model="detailVisible" title="发票申请详情" width="700px">
      <el-descriptions :column="2" border v-if="currentDetail">
        <el-descriptions-item label="订单号">{{ currentDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户名称">{{ currentDetail.userName }}</el-descriptions-item>
        <el-descriptions-item label="发票类型">
          <el-tag :type="currentDetail.invoiceType === 'ENTERPRISE' ? 'primary' : 'info'">
            {{ currentDetail.invoiceType === 'ENTERPRISE' ? '企业' : '个人' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开票金额">¥{{ currentDetail.amount }}</el-descriptions-item>
        <el-descriptions-item label="发票抬头">{{ currentDetail.invoiceTitle }}</el-descriptions-item>
        <el-descriptions-item label="税号">{{ currentDetail.taxNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发票内容">{{ currentDetail.invoiceContent }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentDetail.status)">
            {{ getStatusText(currentDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentDetail.email || '-' }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ currentDetail.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发票号码">{{ currentDetail.invoiceNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发票代码">{{ currentDetail.invoiceCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ currentDetail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="开票时间">{{ currentDetail.issueTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="拒绝原因" :span="2">{{ currentDetail.rejectReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核人">{{ currentDetail.reviewer || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="auditVisible" title="审核发票申请" width="500px">
      <el-form :model="auditForm" label-width="100px" class="dialog-form">
        <el-form-item label="订单号">
          <el-input v-model="auditForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="审核结果" required>
          <el-radio-group v-model="auditForm.result">
            <el-radio value="APPROVED">通过</el-radio>
            <el-radio value="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="auditForm.result === 'REJECTED'" label="拒绝原因" required>
          <el-input
            v-model="auditForm.rejectReason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
        <el-form-item label="审核人">
          <el-input v-model="auditForm.reviewer" placeholder="请输入审核人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="auditVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit" :loading="submitting">
          确认审核
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="issueVisible" title="开具发票" width="500px">
      <el-form :model="issueForm" label-width="100px" class="dialog-form">
        <el-form-item label="订单号">
          <el-input v-model="issueForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="发票号码" required>
          <el-input v-model="issueForm.invoiceNo" placeholder="请输入发票号码" />
        </el-form-item>
        <el-form-item label="发票代码">
          <el-input v-model="issueForm.invoiceCode" placeholder="请输入发票代码" />
        </el-form-item>
        <el-form-item label="开票人">
          <el-input v-model="issueForm.reviewer" placeholder="请输入开票人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="issueVisible = false">取消</el-button>
        <el-button type="primary" @click="submitIssue" :loading="submitting">
          确认开具
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { invoiceApi } from '@/api'

const loading = ref(false)
const submitting = ref(false)
const tableData = ref([])
const selectedIds = ref([])

const searchForm = ref({
  orderNo: '',
  userName: '',
  invoiceType: '',
  status: ''
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const detailVisible = ref(false)
const currentDetail = ref(null)

const auditVisible = ref(false)
const auditForm = ref({
  id: null,
  orderNo: '',
  result: 'APPROVED',
  rejectReason: '',
  reviewer: ''
})

const issueVisible = ref(false)
const issueForm = ref({
  id: null,
  orderNo: '',
  invoiceNo: '',
  invoiceCode: '',
  reviewer: ''
})

const getStatusText = (status) => {
  const map = {
    'PENDING': '待审核',
    'APPROVED': '审核通过',
    'REJECTED': '已拒绝',
    'ISSUED': '已开具'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    'PENDING': 'warning',
    'APPROVED': 'primary',
    'REJECTED': 'danger',
    'ISSUED': 'success'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
      ...searchForm.value
    }
    const data = await invoiceApi.getPage(params)
    tableData.value = data.records || []
    pagination.value.total = data.total || 0
  } catch (error) {
    console.error('Failed to load data:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.value.current = 1
  loadData()
}

const handleReset = () => {
  searchForm.value = {
    orderNo: '',
    userName: '',
    invoiceType: '',
    status: ''
  }
  pagination.value.current = 1
  loadData()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleSizeChange = (size) => {
  pagination.value.size = size
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.value.current = current
  loadData()
}

const handleView = async (row) => {
  try {
    const data = await invoiceApi.getDetail(row.id)
    currentDetail.value = data
    detailVisible.value = true
  } catch (error) {
    console.error('Failed to load detail:', error)
  }
}

const showAuditDialog = (row) => {
  auditForm.value = {
    id: row.id,
    orderNo: row.orderNo,
    result: 'APPROVED',
    rejectReason: '',
    reviewer: ''
  }
  auditVisible.value = true
}

const submitAudit = async () => {
  if (auditForm.value.result === 'REJECTED' && !auditForm.value.rejectReason) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  
  submitting.value = true
  try {
    await invoiceApi.audit(auditForm.value)
    ElMessage.success('审核成功')
    auditVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to audit:', error)
  } finally {
    submitting.value = false
  }
}

const showIssueDialog = (row) => {
  issueForm.value = {
    id: row.id,
    orderNo: row.orderNo,
    invoiceNo: '',
    invoiceCode: '',
    reviewer: ''
  }
  issueVisible.value = true
}

const submitIssue = async () => {
  if (!issueForm.value.invoiceNo) {
    ElMessage.warning('请输入发票号码')
    return
  }
  
  submitting.value = true
  try {
    await invoiceApi.issue(issueForm.value)
    ElMessage.success('开票成功')
    issueVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to issue:', error)
  } finally {
    submitting.value = false
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await invoiceApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('Failed to delete:', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>
