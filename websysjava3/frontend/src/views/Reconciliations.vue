<template>
  <div class="page-container">
    <div class="page-title">对账管理</div>
    
    <el-form :inline="true" :model="searchForm" class="search-bar">
      <el-form-item label="对账日期">
        <el-date-picker
          v-model="searchForm.reconDate"
          type="date"
          placeholder="选择对账日期"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="支付渠道">
        <el-select v-model="searchForm.payChannel" placeholder="请选择支付渠道" clearable>
          <el-option label="微信支付" value="WECHAT" />
          <el-option label="支付宝" value="ALIPAY" />
          <el-option label="银联" value="UNIONPAY" />
        </el-select>
      </el-form-item>
      <el-form-item label="对账类型">
        <el-select v-model="searchForm.reconType" placeholder="请选择对账类型" clearable>
          <el-option label="日对账" value="DAILY" />
          <el-option label="月对账" value="MONTHLY" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
          <el-option label="待对账" value="PENDING" />
          <el-option label="对账一致" value="MATCHED" />
          <el-option label="对账不一致" value="UNMATCHED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>搜索
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>重置
        </el-button>
        <el-button type="success" @click="showExecuteDialog">
          <el-icon><Plus /></el-icon>执行对账
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
      <el-table-column prop="reconDate" label="对账日期" width="120" />
      <el-table-column prop="payChannel" label="支付渠道" width="100">
        <template #default="scope">
          <el-tag :type="getPayChannelTagType(scope.row.payChannel)" class="status-tag">
            {{ getPayChannelText(scope.row.payChannel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reconType" label="对账类型" width="100">
        <template #default="scope">
          {{ scope.row.reconType === 'DAILY' ? '日对账' : '月对账' }}
        </template>
      </el-table-column>
      <el-table-column prop="systemAmount" label="系统金额" width="120">
        <template #default="scope">¥{{ scope.row.systemAmount }}</template>
      </el-table-column>
      <el-table-column prop="thirdAmount" label="第三方金额" width="120">
        <template #default="scope">¥{{ scope.row.thirdAmount }}</template>
      </el-table-column>
      <el-table-column prop="diffAmount" label="差异金额" width="120">
        <template #default="scope">
          <span :style="{ color: Number(scope.row.diffAmount) !== 0 ? '#f56c6c' : '#67c23a' }">
            ¥{{ scope.row.diffAmount }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="systemCount" label="系统订单数" width="100" />
      <el-table-column prop="thirdCount" label="第三方订单数" width="110" />
      <el-table-column prop="status" label="对账状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)" class="status-tag">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reconTime" label="对账时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button 
            v-if="scope.row.diffCount > 0"
            type="primary" 
            link 
            size="small" 
            @click="viewDetails(scope.row)"
          >
            差异明细
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

    <el-dialog v-model="executeVisible" title="执行对账" width="500px">
      <el-form :model="executeForm" label-width="100px" class="dialog-form">
        <el-form-item label="对账日期" required>
          <el-date-picker
            v-model="executeForm.reconDate"
            type="date"
            placeholder="选择对账日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="支付渠道" required>
          <el-select v-model="executeForm.payChannel" placeholder="请选择支付渠道">
            <el-option label="微信支付" value="WECHAT" />
            <el-option label="支付宝" value="ALIPAY" />
            <el-option label="银联" value="UNIONPAY" />
          </el-select>
        </el-form-item>
        <el-form-item label="对账类型" required>
          <el-select v-model="executeForm.reconType" placeholder="请选择对账类型">
            <el-option label="日对账" value="DAILY" />
            <el-option label="月对账" value="MONTHLY" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="executeVisible = false">取消</el-button>
        <el-button type="primary" @click="submitExecute" :loading="executing">
          开始对账
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailsVisible" title="对账差异明细" width="700px">
      <el-table :data="detailData" border stripe>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="diffType" label="差异类型" width="150">
          <template #default="scope">
            <el-tag type="danger" class="status-tag">
              {{ getDiffTypeText(scope.row.diffType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="systemAmount" label="系统金额" width="120">
          <template #default="scope">¥{{ scope.row.systemAmount }}</template>
        </el-table-column>
        <el-table-column prop="thirdAmount" label="第三方金额" width="120">
          <template #default="scope">¥{{ scope.row.thirdAmount }}</template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { reconciliationApi } from '@/api'

const loading = ref(false)
const executing = ref(false)
const tableData = ref([])
const selectedIds = ref([])
const detailData = ref([])

const searchForm = ref({
  reconDate: '',
  payChannel: '',
  reconType: '',
  status: ''
})

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

const executeVisible = ref(false)
const executeForm = ref({
  reconDate: '',
  payChannel: '',
  reconType: 'DAILY'
})

const detailsVisible = ref(false)

const getPayChannelText = (channel) => {
  const map = {
    'WECHAT': '微信支付',
    'ALIPAY': '支付宝',
    'UNIONPAY': '银联'
  }
  return map[channel] || channel
}

const getPayChannelTagType = (channel) => {
  const map = {
    'WECHAT': 'success',
    'ALIPAY': 'primary',
    'UNIONPAY': 'warning'
  }
  return map[channel] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待对账',
    'MATCHED': '对账一致',
    'UNMATCHED': '对账不一致'
  }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = {
    'PENDING': 'info',
    'MATCHED': 'success',
    'UNMATCHED': 'danger'
  }
  return map[status] || 'info'
}

const getDiffTypeText = (type) => {
  const map = {
    'MISSING_SYSTEM': '系统缺失',
    'MISSING_THIRD': '第三方缺失',
    'AMOUNT_DIFF': '金额差异',
    'DUPLICATE_PAY': '重复扣款'
  }
  return map[type] || type
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
      ...searchForm.value
    }
    const data = await reconciliationApi.getPage(params)
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
    reconDate: '',
    payChannel: '',
    reconType: '',
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

const showExecuteDialog = () => {
  executeForm.value = {
    reconDate: '',
    payChannel: '',
    reconType: 'DAILY'
  }
  executeVisible.value = true
}

const submitExecute = async () => {
  if (!executeForm.value.reconDate) {
    ElMessage.warning('请选择对账日期')
    return
  }
  if (!executeForm.value.payChannel) {
    ElMessage.warning('请选择支付渠道')
    return
  }
  
  executing.value = true
  try {
    await reconciliationApi.execute(executeForm.value)
    ElMessage.success('对账完成')
    executeVisible.value = false
    loadData()
  } catch (error) {
    console.error('Failed to execute reconciliation:', error)
  } finally {
    executing.value = false
  }
}

const viewDetails = async (row) => {
  try {
    const data = await reconciliationApi.getDetails(row.id)
    detailData.value = data
    detailsVisible.value = true
  } catch (error) {
    console.error('Failed to load details:', error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await reconciliationApi.delete(row.id)
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
