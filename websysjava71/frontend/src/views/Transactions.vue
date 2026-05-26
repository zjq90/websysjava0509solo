<template>
  <!--
    账单管理页面
    支持账单的增删改查、批量操作、快捷键支持
  -->
  <div class="transactions">
    <!-- 顶部操作栏 -->
    <div class="toolbar card mb-20">
      <div class="toolbar-left">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增账单
        </el-button>
        <el-button 
          type="success" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchEditCategory"
        >
          <el-icon><Edit /></el-icon>
          批量修改分类
        </el-button>
        <el-button 
          type="danger" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </el-button>
        <el-button type="warning" @click="handleImport">
          <el-icon><Upload /></el-icon>
          导入Excel
        </el-button>
        <el-button type="info" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出Excel
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索描述、金额..."
          clearable
          style="width: 200px; margin-right: 10px"
          @keyup.enter="handleSearch"
        />
        <el-select v-model="searchForm.type" placeholder="全部类型" clearable style="width: 120px; margin-right: 10px">
          <el-option label="收入" value="INCOME" />
          <el-option label="支出" value="EXPENSE" />
        </el-select>
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="margin-right: 10px"
        />
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </div>

    <!-- 批量操作提示 -->
    <div v-if="selectedRows.length > 0" class="batch-tip mb-20">
      <span>已选择 <b>{{ selectedRows.length }}</b> 条记录</span>
      <span class="shortcut-tip">
        快捷键: Ctrl+C 复制 | Delete 删除
      </span>
    </div>

    <!-- 账单列表 -->
    <div class="card">
      <el-table
        ref="tableRef"
        :data="tableData"
        v-loading="loading"
        border
        stripe
        @selection-change="handleSelectionChange"
        @row-contextmenu="handleRowContextMenu"
        style="width: 100%"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column label="日期" width="120" align="center">
          <template #default="{ row }">
            {{ formatDate(row.transactionDate) }}
          </template>
        </el-table-column>
        <el-table-column label="类型" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 'INCOME' ? 'success' : 'danger'" size="small">
              {{ row.type === 'INCOME' ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="分类" width="120" align="center">
          <template #default="{ row }">
            <span class="category-tag" :style="{ background: row.category?.color + '20', color: row.category?.color }">
              {{ row.category?.name }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="账户" width="120" align="center">
          <template #default="{ row }">
            {{ row.account?.name }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
        <el-table-column label="金额" width="120" align="right">
          <template #default="{ row }">
            <span :class="row.type === 'INCOME' ? 'income' : 'expense'">
              {{ row.type === 'INCOME' ? '+' : '-' }}¥{{ formatMoney(row.amount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="标签" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              v-if="row.tags" 
              size="small" 
              v-for="tag in row.tags.split(',')" 
              :key="tag"
              class="mr-5"
            >
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="异常" width="70" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.abnormal" class="abnormal-icon"><Warning /></el-icon>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleCopy(row)">
              <el-icon><CopyDocument /></el-icon>
              复制
            </el-button>
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pageInfo.pageNum"
          v-model:page-size="pageInfo.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pageInfo.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑账单' : '新增账单'"
      width="600px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="formData.type">
                <el-radio value="EXPENSE">支出</el-radio>
                <el-radio value="INCOME">收入</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="日期" prop="transactionDate">
              <el-date-picker
                v-model="formData.transactionDate"
                type="date"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="cat in categories"
                  :key="cat.id"
                  :label="cat.name"
                  :value="cat.id"
                >
                  <span :style="{ color: cat.color }">●</span>
                  {{ cat.name }}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="账户" prop="accountId">
              <el-select v-model="formData.accountId" placeholder="请选择账户" style="width: 100%">
                <el-option
                  v-for="acc in accounts"
                  :key="acc.id"
                  :label="acc.name"
                  :value="acc.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="金额" prop="amount">
          <el-input-number
            v-model="formData.amount"
            :min="0"
            :precision="2"
            :step="10"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="标签">
          <el-select
            v-model="formData.tags"
            multiple
            filterable
            allow-create
            placeholder="请输入标签，回车添加"
            style="width: 100%"
          >
            <el-option label="工资" value="工资" />
            <el-option label="餐饮" value="餐饮" />
            <el-option label="交通" value="交通" />
            <el-option label="购物" value="购物" />
            <el-option label="娱乐" value="娱乐" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量修改分类弹窗 -->
    <el-dialog v-model="batchCategoryVisible" title="批量修改分类" width="400px">
      <el-form label-width="80px">
        <el-form-item label="选择分类">
          <el-select v-model="batchCategoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            >
              <span :style="{ color: cat.color }">●</span>
              {{ cat.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchCategoryVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatchCategory">确定</el-button>
      </template>
    </el-dialog>

    <!-- 右键菜单 -->
    <div
      ref="contextMenuRef"
      v-show="contextMenuVisible"
      class="context-menu"
      :style="{ left: contextMenuPosition.x + 'px', top: contextMenuPosition.y + 'px' }"
    >
      <div class="context-menu-item" @click="handleEdit(contextMenuRow)">
        <el-icon><Edit /></el-icon> 编辑
      </div>
      <div class="context-menu-item" @click="handleCopy(contextMenuRow)">
        <el-icon><CopyDocument /></el-icon> 复制 (Ctrl+C)
      </div>
      <div class="context-menu-item" @click="handleDelete(contextMenuRow)">
        <el-icon><Delete /></el-icon> 删除 (Delete)
      </div>
      <div class="context-menu-item danger" @click="handleAddFromCopy(contextMenuRow)">
        <el-icon><Plus /></el-icon> 以此为模板新建
      </div>
    </div>

    <!-- 隐藏的文件输入框 -->
    <input
      ref="fileInputRef"
      type="file"
      accept=".xlsx,.xls,.csv"
      style="display: none"
      @change="handleFileChange"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox, ElNotification } from 'element-plus'
import dayjs from 'dayjs'
import { transactionApi, categoryApi, accountApi, excelApi } from '@/api'

const tableRef = ref(null)
const formRef = ref(null)
const fileInputRef = ref(null)
const contextMenuRef = ref(null)
const loading = ref(false)
const dialogVisible = ref(false)
const batchCategoryVisible = ref(false)
const contextMenuVisible = ref(false)
const contextMenuPosition = reactive({ x: 0, y: 0 })
const contextMenuRow = ref(null)

const isEdit = ref(false)
const editId = ref(null)

const tableData = ref([])
const selectedRows = ref([])
const categories = ref([])
const accounts = ref([])

const searchForm = reactive({
  keyword: '',
  type: '',
  dateRange: []
})

const pageInfo = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})

const formData = reactive({
  type: 'EXPENSE',
  transactionDate: dayjs().format('YYYY-MM-DD'),
  categoryId: null,
  accountId: null,
  amount: 0,
  description: '',
  tags: []
})

const formRules = {
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  transactionDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  accountId: [{ required: true, message: '请选择账户', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
}

const batchCategoryId = ref(null)

const copyBuffer = ref(null)

const formatMoney = (value) => {
  if (value === null || value === undefined) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pageInfo.pageNum - 1,
      size: pageInfo.pageSize,
      keyword: searchForm.keyword,
      type: searchForm.type
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const result = await transactionApi.getTransactions(params)
    tableData.value = result.content
    pageInfo.total = result.totalElements
  } catch (error) {
    console.error('加载账单列表失败:', error)
    ElMessage.error('加载账单列表失败')
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    categories.value = await categoryApi.getAllCategories()
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadAccounts = async () => {
  try {
    accounts.value = await accountApi.getAllAccounts()
  } catch (error) {
    console.error('加载账户失败:', error)
  }
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleAdd = () => {
  isEdit.value = false
  editId.value = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  editId.value = row.id
  Object.assign(formData, {
    type: row.type,
    transactionDate: formatDate(row.transactionDate),
    categoryId: row.category?.id,
    accountId: row.account?.id,
    amount: row.amount,
    description: row.description,
    tags: row.tags ? row.tags.split(',') : []
  })
  dialogVisible.value = true
  hideContextMenu()
}

const handleAddFromCopy = (row) => {
  isEdit.value = false
  editId.value = null
  Object.assign(formData, {
    type: row.type,
    transactionDate: dayjs().format('YYYY-MM-DD'),
    categoryId: row.category?.id,
    accountId: row.account?.id,
    amount: row.amount,
    description: row.description + ' (副本)',
    tags: row.tags ? row.tags.split(',') : []
  })
  dialogVisible.value = true
  hideContextMenu()
}

const handleCopy = (row) => {
  copyBuffer.value = { ...row }
  navigator.clipboard?.writeText(JSON.stringify(row))
  ElMessage.success('已复制到剪贴板')
  hideContextMenu()
}

const handleDelete = async (row) => {
  hideContextMenu()
  await ElMessageBox.confirm('确定要删除这条账单吗？', '确认删除', {
    type: 'warning',
    confirmButtonText: '删除',
    cancelButtonText: '取消'
  })
  try {
    await transactionApi.deleteTransaction(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除失败:', error)
    ElMessage.error('删除失败')
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) return
  await ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 条账单吗？`,
    '确认批量删除',
    { type: 'warning' }
  )
  try {
    const ids = selectedRows.value.map(r => r.id)
    await transactionApi.batchDelete(ids)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    console.error('批量删除失败:', error)
    ElMessage.error('批量删除失败')
  }
}

const handleBatchEditCategory = () => {
  batchCategoryId.value = null
  batchCategoryVisible.value = true
}

const submitBatchCategory = async () => {
  if (!batchCategoryId.value) {
    ElMessage.warning('请选择分类')
    return
  }
  try {
    const ids = selectedRows.value.map(r => r.id)
    await transactionApi.batchUpdateCategory(ids, batchCategoryId.value)
    ElMessage.success('批量修改成功')
    batchCategoryVisible.value = false
    loadData()
  } catch (error) {
    console.error('批量修改失败:', error)
    ElMessage.error('批量修改失败')
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    const data = {
      ...formData,
      tags: formData.tags.join(',')
    }
    if (isEdit.value) {
      await transactionApi.updateTransaction(editId.value, data)
      ElMessage.success('更新成功')
    } else {
      await transactionApi.createTransaction(data)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(formData, {
    type: 'EXPENSE',
    transactionDate: dayjs().format('YYYY-MM-DD'),
    categoryId: null,
    accountId: null,
    amount: 0,
    description: '',
    tags: []
  })
  isEdit.value = false
  editId.value = null
}

const handleSearch = () => {
  pageInfo.pageNum = 1
  loadData()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.type = ''
  searchForm.dateRange = []
  pageInfo.pageNum = 1
  loadData()
}

const handlePageChange = (page) => {
  pageInfo.pageNum = page
  loadData()
}

const handleSizeChange = (size) => {
  pageInfo.pageSize = size
  pageInfo.pageNum = 1
  loadData()
}

const handleImport = () => {
  fileInputRef.value?.click()
}

const handleFileChange = async (e) => {
  const file = e.target.files[0]
  if (!file) return
  
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    loading.value = true
    const result = await excelApi.importExcel(formData)
    ElNotification.success({
      title: '导入成功',
      message: `成功导入 ${result.importedCount} 条记录，匹配分类 ${result.matchedCount} 个`
    })
    loadData()
  } catch (error) {
    console.error('导入失败:', error)
    ElMessage.error('导入失败，请检查文件格式')
  } finally {
    loading.value = false
    e.target.value = ''
  }
}

const handleExport = async () => {
  try {
    loading.value = true
    const params = {
      keyword: searchForm.keyword,
      type: searchForm.type
    }
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    const blob = await excelApi.exportExcel(params)
    const url = window.URL.createObjectURL(new Blob([blob]))
    const link = document.createElement('a')
    link.href = url
    link.download = `账单导出_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  } finally {
    loading.value = false
  }
}

const handleRowContextMenu = (row, column, event) => {
  event.preventDefault()
  contextMenuRow.value = row
  contextMenuPosition.x = event.clientX
  contextMenuPosition.y = event.clientY
  contextMenuVisible.value = true
}

const hideContextMenu = () => {
  contextMenuVisible.value = false
  contextMenuRow.value = null
}

const handleKeyDown = (e) => {
  if (e.target.tagName === 'INPUT' || e.target.tagName === 'TEXTAREA') return
  
  if ((e.ctrlKey || e.metaKey) && e.key === 'c' && selectedRows.value.length > 0) {
    e.preventDefault()
    handleCopy(selectedRows.value[0])
  }
  
  if (e.key === 'Delete' && selectedRows.value.length > 0) {
    e.preventDefault()
    if (selectedRows.value.length === 1) {
      handleDelete(selectedRows.value[0])
    } else {
      handleBatchDelete()
    }
  }
  
  if ((e.ctrlKey || e.metaKey) && e.key === 'v') {
    e.preventDefault()
    if (copyBuffer.value) {
      handleAddFromCopy(copyBuffer.value)
    }
  }
  
  if (e.key === 'Escape') {
    hideContextMenu()
  }
}

const handleDocumentClick = () => {
  hideContextMenu()
}

onMounted(() => {
  loadData()
  loadCategories()
  loadAccounts()
  document.addEventListener('keydown', handleKeyDown)
  document.addEventListener('click', handleDocumentClick)
})

onBeforeUnmount(() => {
  document.removeEventListener('keydown', handleKeyDown)
  document.removeEventListener('click', handleDocumentClick)
})
</script>

<style scoped lang="scss">
.transactions {
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 15px;
    
    .toolbar-left, .toolbar-right {
      display: flex;
      gap: 10px;
      flex-wrap: wrap;
    }
  }
  
  .batch-tip {
    background: #ecf5ff;
    border: 1px solid #d9ecff;
    color: #409eff;
    padding: 10px 15px;
    border-radius: 4px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .shortcut-tip {
      color: #909399;
      font-size: 13px;
    }
  }
  
  .category-tag {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
  
  .income {
    color: #67c23a;
    font-weight: 600;
  }
  
  .expense {
    color: #f56c6c;
    font-weight: 600;
  }
  
  .abnormal-icon {
    color: #e6a23c;
    font-size: 18px;
  }
  
  .pagination {
    display: flex;
    justify-content: flex-end;
    padding: 20px 0 10px;
  }
  
  .context-menu {
    position: fixed;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    padding: 8px 0;
    z-index: 9999;
    min-width: 180px;
    
    .context-menu-item {
      padding: 10px 15px;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 14px;
      transition: background 0.2s;
      
      &:hover {
        background: #f5f7fa;
      }
      
      &.danger {
        color: #f56c6c;
      }
    }
  }
}
</style>
