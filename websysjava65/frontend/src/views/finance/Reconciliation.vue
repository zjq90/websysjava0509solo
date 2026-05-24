<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">自动对账</div>
      <el-button type="primary" :icon="Plus" @click="showReconDialog = true">
        执行对账
      </el-button>
    </div>

    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">对账总数</div>
            <div style="font-size: 32px; font-weight: bold; color: #667eea">{{ reconciliations.length }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">对账成功</div>
            <div style="font-size: 32px; font-weight: bold; color: #67c23a">{{ matchedCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">对账异常</div>
            <div style="font-size: 32px; font-weight: bold; color: #f56c6c">{{ unmatchedCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="text-align: center">
            <div style="font-size: 14px; color: #909399; margin-bottom: 10px">差异总额</div>
            <div style="font-size: 32px; font-weight: bold; color: #e6a23c">¥{{ totalDiff }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <el-table :data="reconciliations" stripe style="width: 100%">
        <el-table-column prop="reconNo" label="对账编号" width="180" />
        <el-table-column prop="reconDate" label="对账日期" width="120" />
        <el-table-column prop="reconType" label="对账类型" width="120">
          <template #default="{ row }">
            <span class="badge-primary">{{ row.reconType === 'DAILY_PAYMENT' ? '日常支付' : row.reconType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="systemAmount" label="系统金额" width="120">
          <template #default="{ row }">¥{{ row.systemAmount }}</template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120">
          <template #default="{ row }">¥{{ row.actualAmount }}</template>
        </el-table-column>
        <el-table-column prop="diffAmount" label="差异金额" width="120">
          <template #default="{ row }">
            <span :style="{ color: row.diffAmount === 0 ? '#67c23a' : '#f56c6c' }">
              ¥{{ row.diffAmount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span :class="row.status === 'MATCHED' ? 'badge-success' : 'badge-danger'">
              {{ row.status === 'MATCHED' ? '已匹配' : '不匹配' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="handleTime" label="操作时间" width="180" />
      </el-table>
    </el-card>

    <el-dialog v-model="showReconDialog" title="执行对账" width="500px">
      <el-form :model="reconForm" label-width="100px">
        <el-form-item label="对账日期">
          <el-date-picker
            v-model="reconForm.reconDate"
            type="date"
            placeholder="选择对账日期"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="对账类型">
          <el-select v-model="reconForm.reconType" style="width: 100%">
            <el-option label="日常支付对账" value="DAILY_PAYMENT" />
            <el-option label="押金对账" value="DEPOSIT" />
            <el-option label="运维支出对账" value="OPERATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="实际金额">
          <el-input v-model="reconForm.actualAmount" placeholder="请输入实际金额" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showReconDialog = false">取消</el-button>
        <el-button type="primary" @click="doReconciliation">执行对账</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getReconciliations, doReconciliation as apiDoReconciliation } from '../../api'

const reconciliations = ref([])
const showReconDialog = ref(false)
const reconForm = ref({
  reconDate: '',
  reconType: 'DAILY_PAYMENT',
  actualAmount: ''
})

const matchedCount = computed(() => reconciliations.value.filter(r => r.status === 'MATCHED').length)
const unmatchedCount = computed(() => reconciliations.value.filter(r => r.status === 'UNMATCHED').length)
const totalDiff = computed(() => {
  return reconciliations.value
    .reduce((sum, r) => sum + Math.abs(parseFloat(r.diffAmount || 0)), 0)
    .toFixed(2)
})

const loadData = async () => {
  try {
    const res = await getReconciliations()
    reconciliations.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const doReconciliation = async () => {
  if (!reconForm.value.reconDate || !reconForm.value.actualAmount) {
    ElMessage.warning('请填写完整信息')
    return
  }
  try {
    await apiDoReconciliation({
      reconDate: reconForm.value.reconDate,
      reconType: reconForm.value.reconType,
      actualAmount: reconForm.value.actualAmount
    })
    ElMessage.success('对账完成')
    showReconDialog.value = false
    reconForm.value = { reconDate: '', reconType: 'DAILY_PAYMENT', actualAmount: '' }
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>
