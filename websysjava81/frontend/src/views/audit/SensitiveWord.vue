<template>
  <div class="page-container">
    <div class="page-header">
      <h3>敏感词管理</h3>
    </div>

    <div class="search-bar">
      <el-input
        v-model="keyword"
        placeholder="搜索敏感词"
        clearable
        style="width: 250px"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        添加敏感词
      </el-button>
      <el-button type="success" @click="handleImport">
        <el-icon><Upload /></el-icon>
        导入
      </el-button>
      <el-button type="warning" @click="handleExport">
        <el-icon><Download /></el-icon>
        导出
      </el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="word" label="敏感词" min-width="200" />
      <el-table-column prop="wordType" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="row.wordType === 1 ? 'warning' : 'info'">
            {{ row.wordType === 1 ? '正则表达式' : '普通词' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="150" />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="showEditDialog(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pagination.pageNum"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      style="margin-top: 20px; justify-content: flex-end"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑敏感词' : '添加敏感词'" width="500px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="敏感词">
          <el-input v-model="form.word" placeholder="请输入敏感词或正则表达式" />
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="form.wordType">
            <el-radio :value="0">普通词</el-radio>
            <el-radio :value="1">正则表达式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" placeholder="请输入分类" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="importDialogVisible" title="导入敏感词" width="500px">
      <el-upload
        action=""
        :auto-upload="false"
        :show-file-list="true"
        accept=".txt"
        :on-change="handleFileChange"
        :limit="1"
      >
        <el-button type="primary">选择TXT文件</el-button>
        <div style="margin-top: 10px; color: #909399; font-size: 12px">
          TXT格式：每行一条记录，格式：敏感词,类型(0/1),分类<br>
          示例：垃圾游戏,0,辱骂<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1[3-9]\d{9},1,手机号
        </div>
      </el-upload>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitImport" :disabled="!importFile">确定导入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSensitiveWordList,
  addSensitiveWord,
  updateSensitiveWord,
  deleteSensitiveWord,
  exportSensitiveWords
} from '../../api/audit'

const loading = ref(false)
const tableData = ref([])
const keyword = ref('')
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const dialogVisible = ref(false)
const importDialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = reactive({
  word: '',
  wordType: 0,
  category: ''
})
const importFile = ref(null)

const formatDate = (date) => {
  if (!date) return '-'
  return date
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      keyword: keyword.value,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getSensitiveWordList(params)
    if (res.data.code === 200) {
      tableData.value = res.data.data.records
      pagination.total = res.data.data.total
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

const handleReset = () => {
  keyword.value = ''
  pagination.pageNum = 1
  fetchData()
}

const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchData()
}

const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchData()
}

const showAddDialog = () => {
  isEdit.value = false
  editId.value = null
  form.word = ''
  form.wordType = 0
  form.category = ''
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  editId.value = row.id
  form.word = row.word
  form.wordType = row.wordType
  form.category = row.category
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.word) {
    ElMessage.warning('请输入敏感词')
    return
  }
  try {
    if (isEdit.value) {
      await updateSensitiveWord(editId.value, form)
      ElMessage.success('更新成功')
    } else {
      await addSensitiveWord(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该敏感词吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteSensitiveWord(row.id)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      fetchData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleImport = () => {
  importFile.value = null
  importDialogVisible.value = true
}

const handleFileChange = (file) => {
  importFile.value = file.raw
}

const submitImport = async () => {
  if (!importFile.value) return
  try {
    const formData = new FormData()
    formData.append('file', importFile.value)
    const res = await fetch('/api/audit/sensitive-word/import', {
      method: 'POST',
      body: formData
    })
    const data = await res.json()
    if (data.code === 200) {
      ElMessage.success('导入成功')
      importDialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(data.message || '导入失败')
    }
  } catch (error) {
    ElMessage.error('导入失败')
  }
}

const handleExport = () => {
  exportSensitiveWords()
}

onMounted(() => {
  fetchData()
})
</script>
