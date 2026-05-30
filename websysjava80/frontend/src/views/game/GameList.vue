<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">游戏管理</div>
      <div>
        <el-button type="primary" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出Excel
        </el-button>
      </div>
    </div>

    <div class="card search-bar">
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="游戏名称/开发者" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待审核" :value="0" />
            <el-option label="已上线" :value="1" />
            <el-option label="已下架" :value="2" />
            <el-option label="审核拒绝" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="queryForm.type" placeholder="游戏类型" clearable style="width: 120px" />
        </el-form-item>
        <el-form-item label="开发者">
          <el-input v-model="queryForm.developer" placeholder="开发者" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <div class="batch-actions">
          <el-button type="success" :disabled="selectedIds.length === 0" @click="handleBatchOnline">
            批量上线
          </el-button>
          <el-button type="warning" :disabled="selectedIds.length === 0" @click="handleBatchOffline">
            批量下架
          </el-button>
          <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchRecommend(1)">
            批量推荐
          </el-button>
          <el-button :disabled="selectedIds.length === 0" @click="handleBatchRecommend(0)">
            取消推荐
          </el-button>
        </div>
        <span>已选择 {{ selectedIds.length }} 项</span>
      </div>

      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="gameId" label="游戏ID" width="100" />
        <el-table-column prop="name" label="游戏名称" min-width="150">
          <template #default="{ row }">
            <div class="game-name-cell">
              <el-image :src="row.cover" style="width: 40px; height: 30px; margin-right: 8px;" fit="cover" />
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="developer" label="开发者" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="hotValue" label="热度值" width="100" />
        <el-table-column prop="recommend" label="推荐" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.recommend" type="success" size="small">是</el-tag>
            <el-tag v-else type="info" size="small">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">详情</el-button>
            <el-button type="success" link @click="handleAudit(row)" v-if="row.status === 0">审核</el-button>
            <el-button type="warning" link @click="handleOffline(row)" v-if="row.status === 1">下架</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 16px; justify-content: flex-end; display: flex;"
      />
    </div>

    <el-dialog v-model="detailVisible" title="游戏详情" width="800px">
      <el-descriptions :column="2" border v-if="currentGame">
        <el-descriptions-item label="游戏ID">{{ currentGame.gameId }}</el-descriptions-item>
        <el-descriptions-item label="游戏名称">{{ currentGame.name }}</el-descriptions-item>
        <el-descriptions-item label="封面">
          <el-image :src="currentGame.cover" style="width: 120px; height: 90px;" fit="cover" />
        </el-descriptions-item>
        <el-descriptions-item label="类型">{{ currentGame.type }}</el-descriptions-item>
        <el-descriptions-item label="开发者">{{ currentGame.developer }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentGame.status)">{{ getStatusText(currentGame.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="热度值">{{ currentGame.hotValue }}</el-descriptions-item>
        <el-descriptions-item label="游戏次数">{{ currentGame.playCount }}</el-descriptions-item>
        <el-descriptions-item label="标签">{{ currentGame.tags }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentGame.submitTime }}</el-descriptions-item>
        <el-descriptions-item label="上线时间">{{ currentGame.onlineTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="下架时间">{{ currentGame.offlineTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="玩法说明" :span="2">{{ currentGame.description }}</el-descriptions-item>
      </el-descriptions>
      <div class="game-preview" v-if="currentGame?.playUrl">
        <h4>游戏预览</h4>
        <iframe :src="currentGame.playUrl" style="width: 100%; height: 400px; border: 1px solid #e4e7ed; border-radius: 4px;"></iframe>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getGameList, getGameDetail, batchUpdateStatus, batchUpdateRecommend, exportGames } from '@/api/game'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedIds = ref([])
const detailVisible = ref(false)
const currentGame = ref(null)
const dateRange = ref([])

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null,
  type: '',
  developer: '',
  startTime: null,
  endTime: null
})

const statusMap = {
  0: { text: '待审核', type: 'warning' },
  1: { text: '已上线', type: 'success' },
  2: { text: '已下架', type: 'info' },
  3: { text: '审核拒绝', type: 'danger' }
}

const getStatusText = (status) => statusMap[status]?.text || '未知'
const getStatusType = (status) => statusMap[status]?.type || 'info'

const loadData = async () => {
  if (dateRange.value && dateRange.value.length === 2) {
    queryForm.startTime = dateRange.value[0]
    queryForm.endTime = dateRange.value[1]
  } else {
    queryForm.startTime = null
    queryForm.endTime = null
  }

  loading.value = true
  try {
    const res = await getGameList(queryForm)
    tableData.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  queryForm.keyword = ''
  queryForm.status = null
  queryForm.type = ''
  queryForm.developer = ''
  dateRange.value = []
  loadData()
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleBatchOnline = async () => {
  await ElMessageBox.confirm('确定要批量上线选中的游戏吗？', '确认', { type: 'warning' })
  await batchUpdateStatus(selectedIds.value, 1)
  ElMessage.success('批量上线成功')
  loadData()
}

const handleBatchOffline = async () => {
  await ElMessageBox.confirm('确定要批量下架选中的游戏吗？', '确认', { type: 'warning' })
  await batchUpdateStatus(selectedIds.value, 2)
  ElMessage.success('批量下架成功')
  loadData()
}

const handleBatchRecommend = async (recommend) => {
  const action = recommend ? '推荐' : '取消推荐'
  await ElMessageBox.confirm(`确定要${action}选中的游戏吗？`, '确认', { type: 'warning' })
  await batchUpdateRecommend(selectedIds.value, recommend)
  ElMessage.success(`${action}成功`)
  loadData()
}

const handleView = async (row) => {
  currentGame.value = await getGameDetail(row.id)
  detailVisible.value = true
}

const handleAudit = (row) => {
  router.push({ path: '/audit', query: { gameId: row.id } })
}

const handleOffline = async (row) => {
  await ElMessageBox.confirm(`确定要下架游戏「${row.name}」吗？`, '确认', { type: 'warning' })
  await batchUpdateStatus([row.id], 2)
  ElMessage.success('下架成功')
  loadData()
}

const handleExport = async () => {
  const blob = await exportGames(queryForm)
  const url = window.URL.createObjectURL(new Blob([blob]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', `游戏列表_${new Date().getTime()}.xlsx`)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.game-name-cell {
  display: flex;
  align-items: center;
}

.game-preview {
  margin-top: 20px;

  h4 {
    margin-bottom: 12px;
    color: #303133;
  }
}
</style>
