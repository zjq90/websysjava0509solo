<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">审核管理</div>
      <el-button @click="loadRules">
        <el-icon><Setting /></el-icon>
        审核规则
      </el-button>
    </div>

    <el-tabs v-model="activeTab" class="audit-tabs">
      <el-tab-pane label="待审核队列" name="queue">
        <div class="card search-bar">
          <el-form :inline="true">
            <el-form-item label="类型">
              <el-select v-model="filterType" placeholder="全部" clearable style="width: 150px">
                <el-option label="动作" value="动作" />
                <el-option label="益智" value="益智" />
                <el-option label="休闲" value="休闲" />
                <el-option label="竞技" value="竞技" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadPendingGames">查询</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div class="card">
          <el-table :data="pendingGames" v-loading="loading" border>
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
            <el-table-column prop="submitTime" label="提交时间" width="180" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleAudit(row)">审核</el-button>
                <el-button link @click="handleAutoAudit(row)">自动审核</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            v-model:current-page="pendingPage.pageNum"
            v-model:page-size="pendingPage.pageSize"
            :total="pendingTotal"
            layout="total, prev, pager, next, jumper"
            @current-change="loadPendingGames"
            style="margin-top: 16px; justify-content: flex-end; display: flex;"
          />
        </div>
      </el-tab-pane>

      <el-tab-pane label="审核日志" name="logs">
        <div class="card">
          <el-table :data="auditLogs" v-loading="logsLoading" border>
            <el-table-column prop="gameName" label="游戏名称" width="150" />
            <el-table-column prop="auditorName" label="审核人" width="120" />
            <el-table-column prop="auditTime" label="审核时间" width="180" />
            <el-table-column prop="auditResult" label="审核结果" width="100">
              <template #default="{ row }">
                <el-tag :type="row.auditResult === 1 ? 'success' : 'danger'">
                  {{ row.auditResult === 1 ? '通过' : '拒绝' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="rejectReason" label="拒绝原因" min-width="150" />
            <el-table-column prop="remark" label="备注" min-width="200" />
          </el-table>

          <el-pagination
            v-model:current-page="logsPage.pageNum"
            v-model:page-size="logsPage.pageSize"
            :total="logsTotal"
            layout="total, prev, pager, next, jumper"
            @current-change="loadAuditLogs"
            style="margin-top: 16px; justify-content: flex-end; display: flex;"
          />
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="auditDialogVisible" title="游戏审核" width="900px" class="audit-dialog">
      <div v-if="currentGame" class="audit-content">
        <div class="game-info-section">
          <div class="game-cover">
            <el-image :src="currentGame.cover" style="width: 200px; height: 150px;" fit="cover" />
          </div>
          <div class="game-basic-info">
            <h3>{{ currentGame.name }}</h3>
            <div class="info-row">
              <span class="label">游戏ID：</span>
              <span>{{ currentGame.gameId }}</span>
            </div>
            <div class="info-row">
              <span class="label">类型：</span>
              <span>{{ currentGame.type }}</span>
            </div>
            <div class="info-row">
              <span class="label">开发者：</span>
              <span>{{ currentGame.developer }}</span>
            </div>
            <div class="info-row">
              <span class="label">标签：</span>
              <span>{{ currentGame.tags }}</span>
            </div>
            <div class="info-row">
              <span class="label">提交时间：</span>
              <span>{{ currentGame.submitTime }}</span>
            </div>
          </div>
        </div>

        <div class="game-description">
          <h4>玩法说明</h4>
          <p>{{ currentGame.description }}</p>
        </div>

        <div class="game-preview-section">
          <h4>游戏预览</h4>
          <iframe :src="currentGame.playUrl" style="width: 100%; height: 450px; border: 1px solid #e4e7ed; border-radius: 4px;"></iframe>
        </div>

        <div class="audit-form-section">
          <h4>审核操作</h4>
          <el-radio-group v-model="auditForm.auditResult">
            <el-radio :value="1" border>通过</el-radio>
            <el-radio :value="0" border>拒绝</el-radio>
          </el-radio-group>

          <el-form v-if="auditForm.auditResult === 0">
            <el-form-item label="拒绝原因" required>
              <el-select v-model="auditForm.rejectReason" placeholder="请选择或输入拒绝原因" style="width: 300px;">
                <el-option label="内容违规" value="内容违规" />
                <el-option label="质量不达标" value="质量不达标" />
                <el-option label="涉及敏感内容" value="涉及敏感内容" />
                <el-option label="重复游戏" value="重复游戏" />
              </el-select>
            </el-form-item>
          </el-form>

          <el-form-item label="备注">
            <el-input v-model="auditForm.remark" type="textarea" :rows="3" placeholder="请输入备注信息" style="width: 400px;" />
          </el-form-item>
        </div>
      </div>
      <template #footer>
        <el-button @click="auditDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAudit">提交审核</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="rulesDialogVisible" title="自动审核规则" width="700px">
      <div class="rules-toolbar">
        <el-button type="primary" @click="showAddRule">
          <el-icon><Plus /></el-icon>
          新增规则
        </el-button>
      </div>
      <el-table :data="rules" border>
        <el-table-column prop="ruleName" label="规则名称" width="200" />
        <el-table-column prop="ruleType" label="规则类型" width="150">
          <template #default="{ row }">
            {{ row.ruleType === 'developer_game_count' ? '开发者游戏数量' : '敏感词过滤' }}
          </template>
        </el-table-column>
        <el-table-column prop="ruleValue" label="规则值" />
        <el-table-column prop="action" label="处理动作" width="100">
          <template #default="{ row }">
            <el-tag :type="row.action === 1 ? 'success' : 'danger'">
              {{ row.action === 1 ? '自动通过' : '自动拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.status" @change="handleRuleStatusChange(row)" :active-value="1" :inactive-value="0" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDeleteRule(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPendingGames, getGameDetail } from '@/api/game'
import { auditGame, autoAudit, getAuditLogs, getAutoAuditRules, saveAutoAuditRule, deleteAutoAuditRule, updateAutoAuditRuleStatus } from '@/api/audit'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeTab = ref('queue')
const loading = ref(false)
const logsLoading = ref(false)
const filterType = ref('')

const pendingGames = ref([])
const pendingTotal = ref(0)
const pendingPage = reactive({ pageNum: 1, pageSize: 10 })

const auditLogs = ref([])
const logsTotal = ref(0)
const logsPage = reactive({ pageNum: 1, pageSize: 10 })

const auditDialogVisible = ref(false)
const currentGame = ref(null)
const auditForm = reactive({
  auditResult: 1,
  rejectReason: '',
  remark: ''
})

const rulesDialogVisible = ref(false)
const rules = ref([])

const loadPendingGames = async () => {
  loading.value = true
  try {
    const params = { ...pendingPage }
    if (filterType.value) {
      params.type = filterType.value
    }
    const res = await getPendingGames(params)
    pendingGames.value = res.list
    pendingTotal.value = res.total
  } finally {
    loading.value = false
  }
}

const loadAuditLogs = async () => {
  logsLoading.value = true
  try {
    const res = await getAuditLogs({ ...logsPage })
    auditLogs.value = res.list
    logsTotal.value = res.total
  } finally {
    logsLoading.value = false
  }
}

const handleAudit = async (row) => {
  currentGame.value = await getGameDetail(row.id)
  auditForm.auditResult = 1
  auditForm.rejectReason = ''
  auditForm.remark = ''
  auditDialogVisible.value = true
}

const handleAutoAudit = async (row) => {
  await ElMessageBox.confirm(`确定要对「${row.name}」执行自动审核吗？`, '确认', { type: 'warning' })
  await autoAudit(row.id)
  ElMessage.success('自动审核完成')
  loadPendingGames()
}

const submitAudit = async () => {
  if (auditForm.auditResult === 0 && !auditForm.rejectReason) {
    ElMessage.warning('请选择或输入拒绝原因')
    return
  }
  await auditGame({
    gameIds: [currentGame.value.id],
    auditResult: auditForm.auditResult,
    rejectReason: auditForm.rejectReason,
    remark: auditForm.remark,
    auditorId: 1,
    auditorName: '管理员'
  })
  ElMessage.success('审核成功')
  auditDialogVisible.value = false
  loadPendingGames()
}

const loadRules = async () => {
  rules.value = await getAutoAuditRules()
  rulesDialogVisible.value = true
}

const showAddRule = () => {
  ElMessageBox.prompt('请输入规则名称', '新增规则', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /.+/,
    inputErrorMessage: '规则名称不能为空'
  }).then(async ({ value }) => {
    await saveAutoAuditRule({
      ruleName: value,
      ruleType: 'sensitive_words',
      ruleValue: '',
      action: 0,
      status: 1
    })
    await loadRules()
    ElMessage.success('规则创建成功')
  }).catch(() => {})
}

const handleRuleStatusChange = async (row) => {
  await updateAutoAuditRuleStatus(row.id, row.status)
  ElMessage.success('状态更新成功')
}

const handleDeleteRule = async (row) => {
  await ElMessageBox.confirm(`确定要删除规则「${row.ruleName}」吗？`, '确认', { type: 'warning' })
  await deleteAutoAuditRule(row.id)
  await loadRules()
  ElMessage.success('删除成功')
}

watch(activeTab, (val) => {
  if (val === 'logs') {
    loadAuditLogs()
  }
})

onMounted(() => {
  loadPendingGames()
  if (route.query.gameId) {
    getGameDetail(route.query.gameId).then(game => {
      currentGame.value = game
      auditDialogVisible.value = true
    })
  }
})
</script>

<style scoped lang="scss">
.audit-tabs {
  :deep(.el-tabs__content) {
    padding-top: 0;
  }
}

.game-name-cell {
  display: flex;
  align-items: center;
}

.audit-content {
  .game-info-section {
    display: flex;
    gap: 24px;
    margin-bottom: 24px;
  }

  .game-basic-info {
    flex: 1;

    h3 {
      margin-bottom: 16px;
      color: #303133;
    }

    .info-row {
      margin-bottom: 8px;
      color: #606266;

      .label {
        color: #909399;
      }
    }
  }

  .game-description, .game-preview-section, .audit-form-section {
    margin-bottom: 24px;

    h4 {
      margin-bottom: 12px;
      color: #303133;
      padding-bottom: 8px;
      border-bottom: 1px solid #e4e7ed;
    }
  }

  .game-description p {
    line-height: 1.8;
    color: #606266;
  }
}

.rules-toolbar {
  margin-bottom: 16px;
}
</style>
