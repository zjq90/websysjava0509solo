<template>
  <div class="page-container">
    <div class="page-title">异常订单管理</div>
    
    <el-form :inline="true" :model="searchForm" class="search-bar">
      <el-form-item label="订单号">
        <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
      </el-form-item>
      <el-form-item label="异常类型">
        <el-select v-model="searchForm.exceptionType" placeholder="请选择异常类型" clearable>
          <el-option label="支付成功未出货" value="PAY_SUCCESS_NO_SHIP" />
          <el-option label="出货失败" value="SHIP_FAILED" />
          <el-option label="重复扣款" value="DUPLICATE_PAY" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态">
        <el-select v-model="searchForm.handleStatus" placeholder="请选择处理状态" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已解决" value="RESOLVED" />
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
      <el-table-column prop="exceptionType" label="异常类型" width="150">
        <template #default="scope">
          <el-tag :type="getExceptionTypeTagType(scope.row.exceptionType)" class="status-tag">
            {{ getExceptionTypeText(scope.row.exceptionType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="exceptionDesc" label="异常描述" show-overflow-tooltip />
      <el-table-column prop="handleStatus" label="处理状态" width="100">
        <template #default="scope">
          <el-tag :type="getHandleStatusTagType(scope.row.handleStatus)" class="status-tag">
            {{ getHandleStatusText(scope.row.handleStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" link size="small" @click="handleView(scope.row)">
            详情
          </el-button>
          <el-button 
            v-if="scope.row.handleStatus !== 'RESOLVED'"
            type="success" 
            link 
            size="small" 
            @click="handleProcess(scope.row)"
          >
            处理
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

    <el-dialog v-model="detailVisible" title="异常订单详情" width="800px">
      <el-descriptions :column="2" border v-if="currentDetail">
        <el-descriptions-item label="订单号">{{ currentDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="异常类型">
          <el-tag :type="getExceptionTypeTagType(currentDetail.exceptionType)">
            {{ getExceptionTypeText(currentDetail.exceptionType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="异常描述" :span="2">{{ currentDetail.exceptionDesc }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getHandleStatusTagType(currentDetail.handleStatus)">
            {{ getHandleStatusText(currentDetail.handleStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理方式">
          {{ getHandleTypeText(currentDetail.handleType) }}
        </el-descriptions-item>
        <el-descriptions-item label="处理备注" :span="2">{{ currentDetail.handleRemark }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentDetail.handler }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ currentDetail.handleTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentDetail.createTime }}</el-descriptions-item>
      </el-descriptions>
      <el-divider>订单信息</el-divider>
      <el-descriptions :column="2" border v-if="currentDetail && currentDetail.order">
        <el-descriptions-item label="用户">{{ currentDetail.order.userName }}</el-descriptions-item>
        <el-descriptions-item label="商品">{{ currentDetail.order.productName }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ currentDetail.order.amount }}</el-descriptions-item>
        <el-descriptions-item label="支付渠道">{{ currentDetail.order.payChannel }}</el-descriptions-item>
        <el-descriptions-item label="支付状态">{{ currentDetail.order.payStatus }}</el-descriptions-item>
        <el-descriptions-item label="出货状态">{{ currentDetail.order.shipStatus }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="handleVisible" title="处理异常订单" width="500px">
      <el-form :model="handleForm" label-width="100px" class="dialog-form">
        <el-form-item label="订单号">
          <el-input v-model="handleForm.orderNo" disabled />
        </el-form-item>
        <el-form-item label="处理方式" required>
          <el-select v-model="handleForm.handleType" placeholder="请选择处理方式">
            <el-option label="退款" value="REFUND" />
            <el-option label="补发" value="RESHIP" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理备注">
          <el-input
            v-model="handleForm.handleRemark"
            type="textarea"
            :rows="3"
            placeholder="请输入处理备注"
          />
        </el-form-item>
        <el-form-item label="处理人">
          <el-input v-model="handleForm.handler" placeholder="请输入处理人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle" :loading="submitting">
          确认处理
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { exceptionOrderApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])
const submitting = ref(false)

const searchForm = ref({
  orderNo: '',
  exceptionType: '',
  handleStatus: ''
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const detailVisible = ref(false)
const currentDetail = ref(null)

const handleVisible = ref(false)
const handleForm = ref({
  id: null,
  orderNo: '',
  handleType: '',
  handleRemark: '',
  handler: ''
})

const getExceptionTypeText = (type) => {
  const map = {
    'PAY_SUCCESS_NO_SHIP': '支付成功未出货',
    'SHIP_FAILED': '出货失败',
    'DUPLICATE_PAY': '重复扣款',
    'OTHER': '其他'
  }
  return map[type] || type
}

const getExceptionTypeTagType = (type) => {
  const map = {
    'PAY_SUCCESS_NO_SHIP': 'warning',
    'SHIP_FAILED': 'danger',
    'DUPLICATE_PAY': 'danger',
    'OTHER': 'info'
  }
  return map[type] || 'info'
}

const getHandleStatusText = (status) => {
  const map = {
    'PENDING': '待处理',
    'PROCESSING': '处理中',
    'RESOLVED': '已解决'
  }
  return map[status] || status
}

const getHandleStatusTagType = (status) => {
  const map = {
    'PENDING': 'warning',
    'PROCESSING': 'primary',
    'RESOLVED': 'success'
  }
  return map[status] || 'info'
}

const getHandleTypeText = (type) => {
  const map = {
    'REFUND': '退款',
    'RESHIP': '补发',
    'OTHER': '其他'
  }
  return map[type] || '-'
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
      ...searchForm.value
    }
    const data = await exceptionOrderApi.getPage(params)
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
    exceptionType: '',
    handleStatus: ''
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
    const data = await exceptionOrderApi.getDetail(row.id)
    currentDetail.value = data
    detailVisible.value = true
  } catch (error) {
    console.error('Failed to load detail:', error)
  }
}

const handleProcess = (row) => {
  handleForm.value = {
    id: row.id,
    orderNo: row.orderNo,
    handleType: '',
    handleRemark: '',
    handler: ''
  }
  handleVisible.value = true
}

const submitHandle = async () => {
  if (!handleForm.value.handleType) {
    ElMessage.warning('请选择处理方式')
    return
  }
  
  submitting.value = true
  try {
    await exceptionOrderApi.handle(handleForm.value)
    ElMessage.success('处理成功')
    handleVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to handle:', error)
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
      await exceptionOrderApi.delete(row.id)
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
