<template>
  <div class="activity-approval">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <div class="stat-card stat-blue">
          <div class="stat-icon"><el-icon><Clock /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalPending || 0 }}</div>
            <div class="stat-label">待审批总数</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-orange">
          <div class="stat-icon"><el-icon><School /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.crossSchoolPending || 0 }}</div>
            <div class="stat-label">跨校活动待审</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-red">
          <div class="stat-icon"><el-icon><UserFilled /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.largeScalePending || 0 }}</div>
            <div class="stat-label">大型活动待审</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-green">
          <div class="stat-icon"><el-icon><CircleCheck /></el-icon></div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.approved || 0 }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="mt-20">
      <template #header>
        <div class="card-header">
          <span>活动审批管理</span>
          <div class="header-actions">
            <el-tabs v-model="activeTab" @tab-change="handleTabChange" class="header-tabs">
              <el-tab-pane label="全部待审" name="all" />
              <el-tab-pane label="社联审核" name="level1" />
              <el-tab-pane label="团委审核" name="level2" />
              <el-tab-pane label="校级领导审核" name="level3" />
            </el-tabs>
            <el-select v-model="queryParams.type" placeholder="活动类型" style="width: 150px; margin-left: 10px" clearable @change="loadData">
              <el-option label="校级活动" value="SCHOOL_LEVEL" />
              <el-option label="跨校活动" value="CROSS_SCHOOL" />
              <el-option label="社团内部" value="INTERNAL" />
            </el-select>
            <el-button type="primary" @click="loadData"><el-icon><Refresh /></el-icon>刷新</el-button>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="活动名称" min-width="200">
          <template #default="{ row }">
            <div class="activity-name">
              <span>{{ row.name }}</span>
              <el-tag v-if="row.type === 'CROSS_SCHOOL'" type="danger" effect="dark" style="margin-left: 8px">跨校</el-tag>
              <el-tag v-if="row.isLargeScale" type="warning" effect="dark" style="margin-left: 4px">大型</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="clubName" label="举办社团" width="150" />
        <el-table-column prop="type" label="活动类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审批级别" width="120">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.approvalLevel)">{{ getLevelText(row.approvalLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前阶段" width="120">
          <template #default="{ row }">
            <el-steps :active="row.currentApprovalStage" simple size="small" finish-status="success">
              <el-step v-for="i in row.approvalLevel" :key="i" />
            </el-steps>
          </template>
        </el-table-column>
        <el-table-column prop="expectedParticipants" label="预计人数" width="100" align="center" />
        <el-table-column prop="location" label="活动地点" width="150" />
        <el-table-column prop="createTime" label="申请时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看详情</el-button>
            <el-button type="success" link @click="handleApprove(row)">审核</el-button>
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

    <el-dialog v-model="detailDialogVisible" title="活动详情" width="800px">
      <el-descriptions :column="2" border v-if="currentActivity">
        <el-descriptions-item label="活动名称" :span="2">{{ currentActivity.name }}</el-descriptions-item>
        <el-descriptions-item label="举办社团">{{ currentActivity.clubName }}</el-descriptions-item>
        <el-descriptions-item label="活动类型">
          <el-tag :type="getTypeTagType(currentActivity.type)">{{ getTypeText(currentActivity.type) }}</el-tag>
          <el-tag v-if="currentActivity.isLargeScale" type="warning" style="margin-left: 8px">大型活动</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审批级别">
          <el-tag :type="getLevelTagType(currentActivity.approvalLevel)">{{ getLevelText(currentActivity.approvalLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="当前审批阶段">第 {{ currentActivity.currentApprovalStage }} 级 / 共 {{ currentActivity.approvalLevel }} 级</el-descriptions-item>
        <el-descriptions-item label="活动地点">{{ currentActivity.location }}</el-descriptions-item>
        <el-descriptions-item label="预计人数">{{ currentActivity.expectedParticipants }} 人</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ currentActivity.organizerName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentActivity.organizerPhone }}</el-descriptions-item>
        <el-descriptions-item label="开始时间" :span="2">{{ formatDate(currentActivity.startTime) }}</el-descriptions-item>
        <el-descriptions-item label="结束时间" :span="2">{{ formatDate(currentActivity.endTime) }}</el-descriptions-item>
        <el-descriptions-item label="活动预算" :span="2">¥ {{ currentActivity.budget }}</el-descriptions-item>
        <el-descriptions-item label="活动主题" :span="2">{{ currentActivity.theme }}</el-descriptions-item>
        <el-descriptions-item label="活动简介" :span="2">{{ currentActivity.description }}</el-descriptions-item>

        <template v-if="currentActivity.type === 'CROSS_SCHOOL'">
          <el-descriptions-item label="参与高校" :span="2">{{ currentActivity.participatingSchools }}</el-descriptions-item>
          <el-descriptions-item label="跨校联系人">{{ currentActivity.crossSchoolContact }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentActivity.crossSchoolPhone }}</el-descriptions-item>
        </template>

        <template v-if="currentActivity.isLargeScale">
          <el-descriptions-item label="安全预案" :span="2">{{ currentActivity.safetyPlan }}</el-descriptions-item>
        </template>

        <el-descriptions-item v-if="currentActivity.approvalLevel >= 1" label="社联审核" :span="2">
          <div v-if="currentActivity.associationApproverName">
            <span>审核人：{{ currentActivity.associationApproverName }}</span>
            <span style="margin-left: 20px">时间：{{ formatDate(currentActivity.associationApproveTime) }}</span>
            <div style="margin-top: 5px">意见：{{ currentActivity.associationApproveOpinion }}</div>
          </div>
          <span v-else style="color: #909399">待审核</span>
        </el-descriptions-item>

        <el-descriptions-item v-if="currentActivity.approvalLevel >= 2" label="团委审核" :span="2">
          <div v-if="currentActivity.leagueApproverName">
            <span>审核人：{{ currentActivity.leagueApproverName }}</span>
            <span style="margin-left: 20px">时间：{{ formatDate(currentActivity.leagueApproveTime) }}</span>
            <div style="margin-top: 5px">意见：{{ currentActivity.leagueApproveOpinion }}</div>
          </div>
          <span v-else style="color: #909399">待审核</span>
        </el-descriptions-item>

        <el-descriptions-item v-if="currentActivity.approvalLevel >= 3" label="校级领导审核" :span="2">
          <div v-if="currentActivity.schoolApproverName">
            <span>审核人：{{ currentActivity.schoolApproverName }}</span>
            <span style="margin-left: 20px">时间：{{ formatDate(currentActivity.schoolApproveTime) }}</span>
            <div style="margin-top: 5px">意见：{{ currentActivity.schoolApproveOpinion }}</div>
          </div>
          <span v-else style="color: #909399">待审核</span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div v-if="currentActivity?.approvalStatus === 'PENDING'">
          <el-button type="success" @click="showApproveDialog(true)">通过</el-button>
          <el-button type="danger" @click="showApproveDialog(false)">驳回</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="approveDialogVisible" :title="isApprove ? '通过申请' : '驳回申请'" width="500px">
      <el-form :model="approveForm" label-width="80px">
        <el-form-item label="审核阶段">
          <el-tag :type="getLevelTagType(currentActivity?.currentApprovalStage)">
            {{ getStageText(currentActivity?.currentApprovalStage) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="approveForm.opinion"
            type="textarea"
            :rows="4"
            :placeholder="isApprove ? '请输入审核通过意见' : '请输入驳回原因'"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button :type="isApprove ? 'success' : 'danger'" @click="submitApprove">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Clock, School, UserFilled, CircleCheck, Refresh } from '@element-plus/icons-vue'
import { activityApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const stats = ref({})
const activeTab = ref('all')
const detailDialogVisible = ref(false)
const approveDialogVisible = ref(false)
const currentActivity = ref(null)
const isApprove = ref(true)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  type: ''
})

const approveForm = ref({
  opinion: ''
})

const getTypeText = (type) => {
  const types = {
    SCHOOL_LEVEL: '校级活动',
    CROSS_SCHOOL: '跨校活动',
    INTERNAL: '社团内部'
  }
  return types[type] || type
}

const getTypeTagType = (type) => {
  const types = {
    SCHOOL_LEVEL: 'danger',
    CROSS_SCHOOL: 'warning',
    INTERNAL: 'primary'
  }
  return types[type] || ''
}

const getLevelText = (level) => {
  const levels = {
    1: '一级审批',
    2: '二级审批',
    3: '三级审批'
  }
  return levels[level] || '一级审批'
}

const getLevelTagType = (level) => {
  const types = {
    1: 'primary',
    2: 'warning',
    3: 'danger'
  }
  return types[level] || 'primary'
}

const getStageText = (stage) => {
  const stages = {
    1: '社联审核',
    2: '团委审核',
    3: '校级领导审核'
  }
  return stages[stage] || '社联审核'
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN')
}

const loadStatistics = async () => {
  try {
    const res = await activityApi.getApprovalStatistics()
    stats.value = res
  } catch (e) {
    console.error(e)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    let approvalStage = null
    if (activeTab.value === 'level1') approvalStage = 1
    else if (activeTab.value === 'level2') approvalStage = 2
    else if (activeTab.value === 'level3') approvalStage = 3

    const params = { ...queryParams.value }
    if (!params.type) delete params.type

    const res = await activityApi.getPendingApprovalList(params, approvalStage)
    tableData.value = res.content
    total.value = res.totalElements
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  queryParams.value.pageNum = 1
  loadData()
}

const handleView = (row) => {
  currentActivity.value = row
  detailDialogVisible.value = true
}

const handleApprove = (row) => {
  currentActivity.value = row
  detailDialogVisible.value = true
}

const showApproveDialog = (approve) => {
  isApprove.value = approve
  approveForm.value.opinion = ''
  approveDialogVisible.value = true
}

const submitApprove = async () => {
  try {
    await activityApi.approve(
      currentActivity.value.id,
      isApprove.value,
      approveForm.value.opinion,
      1,
      '管理员'
    )
    ElMessage.success(isApprove.value ? '已通过申请' : '已驳回申请')
    approveDialogVisible.value = false
    detailDialogVisible.value = false
    loadData()
    loadStatistics()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadStatistics()
  loadData()
})
</script>

<style scoped>
.activity-approval {
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
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-blue {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.stat-orange {
  background: linear-gradient(135deg, #e6a23c, #f5dab1);
  color: #606266;
}

.stat-red {
  background: linear-gradient(135deg, #f56c6c, #f89898);
}

.stat-green {
  background: linear-gradient(135deg, #67c23a, #95d475);
}

.stat-icon {
  font-size: 40px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  line-height: 1.2;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
  margin-top: 4px;
}

.mt-20 {
  margin-top: 20px;
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

.header-tabs {
  margin-right: 20px;
}

.activity-name {
  display: flex;
  align-items: center;
}
</style>
