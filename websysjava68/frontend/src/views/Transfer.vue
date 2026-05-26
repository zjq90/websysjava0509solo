<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">转账记录</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增转账
      </el-button>
    </div>

    <div class="data-table">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="transferTime" label="转账时间" width="160" :formatter="formatTime" />
        <el-table-column label="转出账户" min-width="150">
          <template #default="{ row }">
            <div style="text-align: right">
              <span>{{ row.fromAccountName }}</span>
              <el-icon style="margin-left: 10px; color: #f56c6c"><Minus /></el-icon>
              <span class="amount expense-color">¥{{ formatMoney(row.amount) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="转入账户" min-width="150">
          <template #default="{ row }">
            <div>
              <span class="amount income-color">¥{{ formatMoney(row.amount) }}</span>
              <el-icon style="margin-left: 10px; color: #67c23a"><Plus /></el-icon>
              <span>{{ row.toAccountName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="transferFee" label="手续费" width="120">
          <template #default="{ row }">
            <span v-if="row.transferFee > 0" class="expense-color">¥{{ formatMoney(row.transferFee) }}</span>
            <span v-else style="color: #909399">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; text-align: right">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData" />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" title="新增转账" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="转出账户" prop="fromAccountId">
          <el-select v-model="formData.fromAccountId" placeholder="请选择转出账户" style="width: 100%">
            <el-option v-for="account in accounts" :key="account.id" :label="`${account.accountName} (余额: ¥${account.balance})`" :value="account.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="转入账户" prop="toAccountId">
          <el-select v-model="formData.toAccountId" placeholder="请选择转入账户" style="width: 100%">
            <el-option v-for="account in accounts" :key="account.id" :label="account.accountName" :value="account.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="转账金额" prop="amount">
          <el-input-number v-model="formData.amount" :min="0.01" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="手续费">
          <el-input-number v-model="formData.transferFee" :min="0" :precision="2" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="转账时间" prop="transferTime">
          <el-date-picker v-model="formData.transferTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="formData.description" placeholder="转账说明" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus, Delete } from '@element-plus/icons-vue'
import { pageTransfers, addTransfer, deleteTransfer } from '@/api/transfer'
import { listAccounts } from '@/api/account'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])
const accounts = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  fromAccountId: null,
  toAccountId: null,
  amount: 100,
  transferFee: 0,
  transferTime: new Date(),
  description: ''
})

const formRules = {
  fromAccountId: [{ required: true, message: '请选择转出账户', trigger: 'change' }],
  toAccountId: [{ required: true, message: '请选择转入账户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入转账金额', trigger: 'blur' }]
}

const formatMoney = (value) => {
  return Number(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const formatTime = (row, column, cellValue) => {
  return dayjs(cellValue).format('YYYY-MM-DD HH:mm')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await pageTransfers({
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.records
    pagination.total = res.total
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadAccounts = async () => {
  try {
    accounts.value = await listAccounts()
  } catch (error) {
    console.error('加载账户失败:', error)
  }
}

const handleAdd = () => {
  Object.assign(formData, {
    fromAccountId: null,
    toAccountId: null,
    amount: 100,
    transferFee: 0,
    transferTime: new Date(),
    description: ''
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条转账记录吗？删除后将恢复账户余额。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTransfer(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    await addTransfer(formData)
    ElMessage.success('新增成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

onMounted(() => {
  loadAccounts()
  loadData()
})
</script>

<style lang="scss" scoped>
.amount {
  font-weight: bold;
  margin-left: 10px;
}

.income-color {
  color: #67c23a;
}

.expense-color {
  color: #f56c6c;
}
</style>
