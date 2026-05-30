<template>
  <div class="page-container">
    <div class="page-header">
      <h3>评论审核</h3>
    </div>

    <div class="search-bar">
      <el-select v-model="searchForm.auditStatus" placeholder="审核状态" clearable style="width: 150px">
        <el-option label="待审核" :value="0" />
        <el-option label="已通过" :value="1" />
        <el-option label="已删除" :value="2" />
        <el-option label="已屏蔽" :value="3" />
      </el-select>
      <el-input
        v-model="searchForm.keyword"
        placeholder="搜索评论内容"
        clearable
        style="width: 250px"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="commentId" label="评论ID" width="120" />
      <el-table-column prop="gameName" label="关联游戏" width="150" />
      <el-table-column prop="nickname" label="用户昵称" width="120" />
      <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
      <el-table-column prop="hasSensitiveWord" label="敏感词" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.hasSensitiveWord === 1" type="danger">是</el-tag>
          <el-tag v-else type="success">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="auditStatus" label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :class="getAuditStatusClass(row.auditStatus)">
            {{ getAuditStatusText(row.auditStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.auditStatus === 0"
            type="success"
            link
            @click="handleAudit(row, 1)"
          >
            通过
          </el-button>
          <el-button
            v-if="row.auditStatus === 0"
            type="danger"
            link
            @click="handleAudit(row, 2)"
          >
            删除
          </el-button>
          <el-button
            v-if="row.auditStatus === 0"
            type="warning"
            link
            @click="handleAudit(row, 3)"
          >
            屏蔽
          </el-button>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCommentList, auditComment } from '../../api/audit'

const loading = ref(false)
const tableData = ref([])
const searchForm = reactive({
  auditStatus: 0,
  keyword: ''
})
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const formatDate = (date) => {
  if (!date) return '-'
  return date
}

const getAuditStatusClass = (status) => {
  const classes = ['status-tag-pending', 'status-tag-passed', 'status-tag-deleted', 'status-tag-pending']
  return classes[status] || ''
}

const getAuditStatusText = (status) => {
  const texts = ['待审核', '已通过', '已删除', '已屏蔽']
  return texts[status] || '未知'
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getCommentList(params)
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
  searchForm.auditStatus = 0
  searchForm.keyword = ''
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

const handleAudit = async (row, status) => {
  const actionTexts = { 1: '通过', 2: '删除', 3: '屏蔽' }
  try {
    await ElMessageBox.confirm(`确定要${actionTexts[status]}该评论吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await auditComment({
      commentId: row.commentId,
      auditStatus: status,
      auditorId: 'ADMIN001'
    })
    if (res.data.code === 200) {
      ElMessage.success(`${actionTexts[status]}成功`)
      fetchData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

onMounted(() => {
  fetchData()
})
</script>
