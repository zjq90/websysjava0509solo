<template>
  <div class="club-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>社团列表</span>
          <div class="header-actions">
            <el-input
              v-model="queryParams.keyword"
              placeholder="搜索社团名称"
              style="width: 200px; margin-right: 10px"
              clearable
              @keyup.enter="loadData"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="loadData">
              <el-icon><Search /></el-icon>搜索
            </el-button>
            <el-button type="success" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增社团
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="社团名称" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属院系" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="memberCount" label="成员数" width="100" align="center" />
        <el-table-column prop="fundBalance" label="经费余额(元)" width="120" align="right" />
        <el-table-column prop="leaderName" label="负责人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="success" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; text-align: right"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { clubApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
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

const getTypeTagType = (type) => {
  const types = {
    ACADEMIC: 'primary',
    CULTURAL: 'success',
    SPORTS: 'warning',
    VOLUNTEER: 'info',
    INNOVATION: 'danger',
    HOBBY: ''
  }
  return types[type] || ''
}

const getStatusText = (status) => {
  const statuses = {
    NORMAL: '正常',
    WARNING: '警告',
    SUSPENDED: '暂停',
    CANCELLED: '已注销'
  }
  return statuses[status] || status
}

const getStatusTagType = (status) => {
  const types = {
    NORMAL: 'success',
    WARNING: 'warning',
    SUSPENDED: 'danger',
    CANCELLED: 'info'
  }
  return types[status] || ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await clubApi.getList(queryParams.value)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  ElMessage.info('新增社团功能')
}

const handleView = (row) => {
  ElMessage.info(`查看社团: ${row.name}`)
}

const handleEdit = (row) => {
  ElMessage.info(`编辑社团: ${row.name}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除社团"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await clubApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.club-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}
</style>
