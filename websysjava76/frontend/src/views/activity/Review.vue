<template>
  <div class="activity-review">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-blue">
          <div class="stat-icon">
            <el-icon><Clock /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.pending || 0 }}</div>
            <div class="stat-label">待审核</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-green">
          <div class="stat-icon">
            <el-icon><CircleCheck /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.approved || 0 }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-red">
          <div class="stat-icon">
            <el-icon><CircleClose /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.blocked || 0 }}</div>
            <div class="stat-label">已屏蔽</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-purple">
          <div class="stat-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.total || 0 }}</div>
            <div class="stat-label">总审查数</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="mt-20">
      <template #header>
        <div class="card-header">
          <span>内容审查列表</span>
          <div class="header-actions">
            <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 120px; margin-right: 10px" clearable @change="loadData">
              <el-option label="待审核" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已屏蔽" value="BLOCKED" />
            </el-select>
            <el-button type="primary" @click="loadData">
              <el-icon><Refresh /></el-icon>刷新
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="activityName" label="活动名称" width="200" />
        <el-table-column prop="clubName" label="社团名称" width="150" />
        <el-table-column prop="reviewType" label="审查类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getReviewTypeTagType(row.reviewType)">{{ getReviewTypeText(row.reviewType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fieldName" label="检测字段" width="120">
          <template #default="{ row }">
            <el-tag>{{ getFieldNameText(row.fieldName) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sensitiveWords" label="敏感词" width="200">
          <template #default="{ row }">
            <el-tag v-for="word in parseSensitiveWords(row.sensitiveWords)" :key="word" type="danger" style="margin: 2px">
              {{ word }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="originalContent" label="内容预览" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewerName" label="审核人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link @click="handleApprove(row)">通过</el-button>
            <el-button v-if="row.status === 'PENDING'" type="danger" link @click="handleBlock(row)">屏蔽</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="detailDialogVisible" title="内容详情" width="800px">
      <el-descriptions :column="2" border v-if="currentReview">
        <el-descriptions-item label="活动名称">{{ currentReview.activityName }}</el-descriptions-item>
        <el-descriptions-item label="社团名称">{{ currentReview.clubName }}</el-descriptions-item>
        <el-descriptions-item label="审查类型">{{ getReviewTypeText(currentReview.reviewType) }}</el-descriptions-item>
        <el-descriptions-item label="检测字段">{{ getFieldNameText(currentReview.fieldName) }}</el-descriptions-item>
        <el-descriptions-item label="敏感词" :span="2">
          <el-tag v-for="word in parseSensitiveWords(currentReview.sensitiveWords)" :key="word" type="danger" style="margin: 2px">
            {{ word }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="原始内容" :span="2">
          <div style="max-height: 200px; overflow-y: auto; white-space: pre-wrap;">
            {{ currentReview.originalContent }}
          </div>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentReview.status !== 'PENDING'" label="审核人">
          {{ currentReview.reviewerName }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentReview.status !== 'PENDING'" label="审核时间">
          {{ formatDate(currentReview.reviewTime) }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentReview.status !== 'PENDING'" label="审核意见" :span="2">
          {{ currentReview.reviewOpinion }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div v-if="currentReview?.status === 'PENDING'">
          <el-button type="success" @click="handleApprove(currentReview)">审核通过</el-button>
          <el-button type="danger" @click="handleBlock(currentReview)">屏蔽内容</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="reviewDialogVisible" :title="isApprove ? '审核通过' : '屏蔽内容'" width="500px">
      <el-form :model="reviewForm" label-width="80px">
        <el-form-item label="审核意见">
          <el-input
            v-model="reviewForm.opinion"
            type="textarea"
            :rows="4"
            :placeholder="isApprove ? '请输入审核通过意见' : '请输入屏蔽原因'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button :type="isApprove ? 'success' : 'danger'" @click="submitReview">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { activityApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const stats = ref({ pending: 0, approved: 0, blocked: 0, total: 0 })
const statusFilter = ref('')
const detailDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentReview = ref(null)
const isApprove = ref(true)

const reviewForm = ref({
  opinion: ''
})

const getReviewTypeText = (type) => {
  const types = {
    DESCRIPTION: '活动描述',
    POSTER: '海报内容',
    THEME: '活动主题',
    NAME: '活动名称'
  }
  return types[type] || type
}

const getReviewTypeTagType = (type) => {
  const types = {
    DESCRIPTION: 'primary',
    POSTER: 'warning',
    THEME: 'success',
    NAME: 'danger'
  }
  return types[type] || ''
}

const getFieldNameText = (field) => {
  const fields = {
    name: '活动名称',
    theme: '活动主题',
    description: '活动描述',
    poster: '海报内容'
  }
  return fields[field] || field
}

const getStatusText = (status) => {
  const statuses = {
    PENDING: '待审核',
    APPROVED: '已通过',
    BLOCKED: '已屏蔽',
    MODIFIED: '已修改'
  }
  return statuses[status] || status
}

const getStatusTagType = (status) => {
  const types = {
    PENDING: 'warning',
    APPROVED: 'success',
    BLOCKED: 'danger',
    MODIFIED: 'info'
  }
  return types[status] || ''
}

const parseSensitiveWords = (words) => {
  if (!words) return []
  try {
    return JSON.parse(words)
  } catch (e) {
    return words.replace(/[\[\]"]/g, '').split(',').map(w => w.trim())
  }
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await activityApi.getReviewList()
    tableData.value = Array.isArray(res) ? res : []
    
    const statsRes = await activityApi.getReviewStatistics()
    stats.value = statsRes
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleView = (row) => {
  currentReview.value = row
  detailDialogVisible.value = true
}

const handleApprove = (row) => {
  currentReview.value = row
  isApprove.value = true
  reviewForm.value.opinion = ''
  reviewDialogVisible.value = true
}

const handleBlock = (row) => {
  currentReview.value = row
  isApprove.value = false
  reviewForm.value.opinion = ''
  reviewDialogVisible.value = true
}

const submitReview = async () => {
  try {
    if (isApprove.value) {
      await activityApi.approveReview(
        currentReview.value.id,
        1,
        '管理员',
        reviewForm.value.opinion
      )
      ElMessage.success('审核通过')
    } else {
      await activityApi.blockReview(
        currentReview.value.id,
        1,
        '管理员',
        reviewForm.value.opinion
      )
      ElMessage.success('已屏蔽内容')
    }
    reviewDialogVisible.value = false
    detailDialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.activity-review {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  color: #fff;
}

.stat-blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.stat-green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-red { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }
.stat-purple { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
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

.mt-20 {
  margin-top: 20px;
}
</style>
