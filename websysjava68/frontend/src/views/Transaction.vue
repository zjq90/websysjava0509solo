<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">收支录入</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增记录
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="类型">
          <el-select v-model="searchForm.transactionType" placeholder="全部" clearable>
            <el-option label="收入" value="INCOME" />
            <el-option label="支出" value="EXPENSE" />
          </el-select>
        </el-form-item>
        <el-form-item label="账户">
          <el-select v-model="searchForm.accountId" placeholder="全部" clearable>
            <el-option v-for="account in accounts" :key="account.id" :label="account.accountName" :value="account.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="全部" clearable>
            <el-option v-for="category in categories" :key="category.id" :label="category.categoryName" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键字">
          <el-input v-model="searchForm.keyword" placeholder="描述/商家" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="data-table">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="transactionTime" label="时间" width="160" :formatter="formatTime" />
        <el-table-column prop="transactionType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.transactionType === 'INCOME' ? 'success' : 'danger'">
              {{ row.transactionType === 'INCOME' ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            <span :class="row.transactionType === 'INCOME' ? 'income-color' : 'expense-color'">
              {{ row.transactionType === 'INCOME' ? '+' : '-' }}¥{{ formatMoney(row.amount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="categoryIcon" label="" width="60">
          <template #default="{ row }">
            <span style="font-size: 20px">{{ row.categoryIcon || '💰' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="accountName" label="账户" width="140" />
        <el-table-column prop="description" label="描述" min-width="150" show-overflow-tooltip />
        <el-table-column prop="merchant" label="商家" width="120" show-overflow-tooltip />
        <el-table-column label="标签" width="150">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tags" :key="tag.id" size="small" :color="tag.tagColor" effect="dark" style="margin-right: 4px">
              {{ tag.tagName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="类型" prop="transactionType">
          <el-radio-group v-model="formData.transactionType">
            <el-radio value="INCOME">收入</el-radio>
            <el-radio value="EXPENSE">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="formData.amount" :min="0.01" :precision="2" :step="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="账户" prop="accountId">
          <el-select v-model="formData.accountId" placeholder="请选择账户" style="width: 100%">
            <el-option v-for="account in accounts" :key="account.id" :label="account.accountName" :value="account.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%" filterable>
            <el-option
              v-for="category in filteredCategories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id">
              <span>{{ category.icon }} {{ category.categoryName }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="transactionTime">
          <el-date-picker v-model="formData.transactionTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="formData.description" placeholder="交易描述" />
        </el-form-item>
        <el-form-item label="商家">
          <el-input v-model="formData.merchant" placeholder="商家名称" />
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="formData.tagIds" multiple placeholder="选择标签" style="width: 100%">
            <el-option v-for="tag in tags" :key="tag.id" :label="tag.tagName" :value="tag.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="备注说明" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { pageTransactions, addTransaction, updateTransaction, deleteTransaction } from '@/api/transaction'
import { listAccounts } from '@/api/account'
import { listCategories } from '@/api/category'
import { listTags } from '@/api/tag'
import dayjs from 'dayjs'

const loading = ref(false)
const tableData = ref([])
const accounts = ref([])
const categories = ref([])
const tags = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增记录')
const formRef = ref(null)
const isEdit = ref(false)

const searchForm = reactive({
  transactionType: '',
  accountId: null,
  categoryId: null,
  keyword: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formData = reactive({
  id: null,
  transactionType: 'EXPENSE',
  amount: 0.01,
  accountId: null,
  categoryId: null,
  transactionTime: new Date(),
  description: '',
  merchant: '',
  tagIds: [],
  remark: ''
})

const formRules = {
  transactionType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const filteredCategories = computed(() => {
  return categories.value.filter(c => c.categoryType === formData.transactionType)
})

const formatMoney = (value) => {
  return Number(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const formatTime = (row, column, cellValue) => {
  return dayjs(cellValue).format('YYYY-MM-DD HH:mm')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const res = await pageTransactions(params)
    tableData.value = res.records
    pagination.total = res.total
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadBaseData = async () => {
  try {
    accounts.value = await listAccounts()
    categories.value = await listCategories()
    tags.value = await listTags()
  } catch (error) {
    console.error('加载基础数据失败:', error)
  }
}

const resetSearch = () => {
  searchForm.transactionType = ''
  searchForm.accountId = null
  searchForm.categoryId = null
  searchForm.keyword = ''
  pagination.pageNum = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增记录'
  Object.assign(formData, {
    id: null,
    transactionType: 'EXPENSE',
    amount: 0.01,
    accountId: null,
    categoryId: null,
    transactionTime: new Date(),
    description: '',
    merchant: '',
    tagIds: [],
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑记录'
  Object.assign(formData, {
    id: row.id,
    transactionType: row.transactionType,
    amount: row.amount,
    accountId: row.accountId,
    categoryId: row.categoryId,
    transactionTime: row.transactionTime,
    description: row.description,
    merchant: row.merchant,
    tagIds: (row.tags || []).map(t => t.id),
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTransaction(row.id)
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
    if (isEdit.value) {
      await updateTransaction(formData)
      ElMessage.success('更新成功')
    } else {
      await addTransaction(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

onMounted(() => {
  loadBaseData()
  loadData()
})
</script>

<style lang="scss" scoped>
.income-color {
  color: #67c23a;
  font-weight: bold;
}

.expense-color {
  color: #f56c6c;
  font-weight: bold;
}
</style>
