<template>
  <div class="activity-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>活动列表</span>
          <div class="header-actions">
            <el-select v-model="queryParams.type" placeholder="活动类型" style="width: 150px; margin-right: 10px" clearable @change="loadData">
              <el-option label="校级活动" value="SCHOOL_LEVEL" />
              <el-option label="跨校活动" value="CROSS_CAMPUS" />
              <el-option label="社团活动" value="CLUB" />
              <el-option label="志愿活动" value="VOLUNTEER" />
              <el-option label="培训讲座" value="TRAINING" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="活动名称" />
        <el-table-column prop="clubName" label="举办社团" width="150" />
        <el-table-column prop="type" label="活动类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="活动地点" width="150" />
        <el-table-column prop="expectedParticipants" label="预计人数" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { activityApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  type: ''
})

const getTypeText = (type) => {
  const types = {
    SCHOOL_LEVEL: '校级活动',
    CROSS_CAMPUS: '跨校活动',
    CLUB: '社团活动',
    VOLUNTEER: '志愿活动',
    TRAINING: '培训讲座'
  }
  return types[type] || type
}

const getTypeTagType = (type) => {
  const types = {
    SCHOOL_LEVEL: 'danger',
    CROSS_CAMPUS: 'warning',
    CLUB: 'primary',
    VOLUNTEER: 'success',
    TRAINING: 'info'
  }
  return types[type] || ''
}

const getStatusText = (status) => {
  const statuses = {
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已驳回',
    CANCELLED: '已取消',
    COMPLETED: '已完成'
  }
  return statuses[status] || status
}

const getStatusTagType = (status) => {
  const types = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger',
    CANCELLED: 'info',
    COMPLETED: 'success'
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
    if (!params.type) delete params.type
    const res = await activityApi.getList(params)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleView = (row) => {
  ElMessage.info(`查看活动: ${row.name}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除活动"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await activityApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.activity-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
