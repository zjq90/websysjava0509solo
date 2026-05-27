<template>
  <div class="finance-list">
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <div class="stat-card stat-green">
          <div class="stat-icon">
            <el-icon><Wallet /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.totalIncome || 0 }}</div>
            <div class="stat-label">总收入</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-red">
          <div class="stat-icon">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ stats.totalExpense || 0 }}</div>
            <div class="stat-label">总支出</div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card stat-blue">
          <div class="stat-icon">
            <el-icon><Coin /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ (stats.totalIncome || 0) - (stats.totalExpense || 0) }}</div>
            <div class="stat-label">当前结余</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card class="mt-20">
      <template #header>
        <div class="card-header">
          <span>经费流水记录</span>
          <div class="header-actions">
            <el-select v-model="queryParams.type" placeholder="类型筛选" style="width: 120px; margin-right: 10px" clearable @change="loadData">
              <el-option label="收入" value="INCOME" />
              <el-option label="支出" value="EXPENSE" />
              <el-option label="报销" value="REIMBURSEMENT" />
            </el-select>
            <el-select v-model="queryParams.abnormal" placeholder="异常标记" style="width: 120px; margin-right: 10px" clearable @change="loadData">
              <el-option label="是" :value="true" />
              <el-option label="否" :value="false" />
            </el-select>
          </div>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="clubName" label="社团名称" width="150" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="amount" label="金额(元)" width="120" align="right">
          <template #default="{ row }">
            <span :class="row.type === 'INCOME' ? 'text-success' : 'text-danger'">
              {{ row.type === 'INCOME' ? '+' : '-' }}{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
        <el-table-column prop="abnormal" label="异常" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.abnormal" type="danger">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="经办人" width="100" />
        <el-table-column prop="createTime" label="时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.abnormal" type="danger" link @click="handleInvestigate(row)">调查</el-button>
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
import { ElMessage } from 'element-plus'
import { financeApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const stats = ref({})

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  type: '',
  abnormal: null
})

const getTypeText = (type) => {
  const types = {
    INCOME: '收入',
    EXPENSE: '支出',
    REIMBURSEMENT: '报销'
  }
  return types[type] || type
}

const getTypeTagType = (type) => {
  const types = {
    INCOME: 'success',
    EXPENSE: 'danger',
    REIMBURSEMENT: 'warning'
  }
  return types[type] || ''
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
    if (params.abnormal === null || params.abnormal === '') delete params.abnormal
    
    const res = await financeApi.getList(params)
    tableData.value = res.content
    total.value = res.totalElements

    const statsRes = await financeApi.getStatistics()
    stats.value = statsRes
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleInvestigate = (row) => {
  ElMessage.info(`开始调查 ${row.clubName} 的异常经费记录`)
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.finance-list {
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

.stat-green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
.stat-red { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }
.stat-blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }

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

.text-success {
  color: #67c23a;
  font-weight: bold;
}

.text-danger {
  color: #f56c6c;
  font-weight: bold;
}

.mt-20 {
  margin-top: 20px;
}
</style>
