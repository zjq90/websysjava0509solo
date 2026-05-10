<template>
  <div>
    <div class="page-header">
      <h2>智能补货</h2>
      <div>
        <el-button type="warning" icon="MagicStick" @click="handleAutoGenerate">
          自动生成补货单
        </el-button>
        <el-button type="primary" icon="Plus" @click="handleCreateManual">
          手动创建补货单
        </el-button>
      </div>
    </div>

    <el-alert
      v-if="pendingCount > 0"
      :title="`当前有 ${pendingCount} 个待处理的补货单`"
      type="warning"
      :closable="false"
      style="margin-bottom: 20px;"
    />

    <div class="table-container">
      <el-table :data="restocks" style="width: 100%">
        <el-table-column prop="restockNo" label="补货单号" width="180" />
        <el-table-column label="设备" width="180">
          <template #default="scope">
            {{ scope.row.machine?.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="创建方式" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.createType === 'AUTO' ? 'warning' : 'info'" size="small">
              {{ scope.row.createType === 'AUTO' ? '自动' : '手动' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="补货明细" min-width="250">
          <template #default="scope">
            <el-tag
              v-for="(item, idx) in scope.row.items"
              :key="idx"
              size="small"
              style="margin-right: 5px; margin-bottom: 5px;"
            >
              {{ item.productName }} +{{ item.restockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="showDetail(scope.row)">
              详情
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING' || scope.row.status === 'IN_PROGRESS'"
              type="success"
              size="small"
              link
              @click="handleComplete(scope.row)"
            >
              完成补货
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING' || scope.row.status === 'IN_PROGRESS'"
              type="danger"
              size="small"
              link
              @click="handleCancel(scope.row)"
            >
              取消
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      v-model="detailVisible"
      title="补货单详情"
      width="700px"
    >
      <el-descriptions v-if="currentRestock" border :column="2">
        <el-descriptions-item label="补货单号">
          {{ currentRestock.restockNo }}
        </el-descriptions-item>
        <el-descriptions-item label="创建方式">
          <el-tag :type="currentRestock.createType === 'AUTO' ? 'warning' : 'info'" size="small">
            {{ currentRestock.createType === 'AUTO' ? '自动' : '手动' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标设备">
          {{ currentRestock.machine?.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentRestock.status)" size="small">
            {{ getStatusText(currentRestock.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="补货员" :span="2">
          {{ currentRestock.operator || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ currentRestock.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="完成时间">
          {{ currentRestock.completeTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ currentRestock.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      
      <h4 style="margin: 20px 0 10px 0;">补货明细</h4>
      <el-table v-if="currentRestock" :data="currentRestock.items" border size="small">
        <el-table-column prop="slotNumber" label="货道" width="80" />
        <el-table-column prop="productName" label="商品" />
        <el-table-column prop="beforeStock" label="补货前库存" width="100" />
        <el-table-column prop="restockQuantity" label="补货数量" width="100">
          <template #default="scope">
            <span style="color: #67c23a; font-weight: bold;">+{{ scope.row.restockQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="afterStock" label="补货后库存" width="100" />
      </el-table>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="operatorVisible"
      title="完成补货"
      width="400px"
    >
      <el-form :model="operatorForm" label-width="60px">
        <el-form-item label="补货员">
          <el-input v-model="operatorForm.operator" placeholder="请输入补货员姓名" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="operatorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmComplete">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="cancelVisible"
      title="取消补货单"
      width="400px"
    >
      <el-form :model="cancelForm" label-width="60px">
        <el-form-item label="原因">
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入取消原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmCancel">确定取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getRestocks,
  autoGenerateRestocks,
  completeRestock,
  cancelRestock,
  getPendingCount
} from '../api/restock'

const restocks = ref([])
const pendingCount = ref(0)
const detailVisible = ref(false)
const operatorVisible = ref(false)
const cancelVisible = ref(false)
const currentRestock = ref(null)

const operatorForm = reactive({ operator: '' })
const cancelForm = reactive({ reason: '' })

const getStatusType = (status) => {
  const types = {
    PENDING: 'warning',
    IN_PROGRESS: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    PENDING: '待处理',
    IN_PROGRESS: '补货中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return texts[status] || status
}

const loadData = async () => {
  try {
    restocks.value = await getRestocks()
    pendingCount.value = await getPendingCount()
  } catch (e) {
    console.error(e)
  }
}

const showDetail = (restock) => {
  currentRestock.value = restock
  detailVisible.value = true
}

const handleAutoGenerate = async () => {
  try {
    const count = await autoGenerateRestocks()
    ElMessage.success(`自动生成了 ${count} 个补货单`)
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleCreateManual = () => {
  ElMessage.info('请在设备管理页面选择需要补货的货道')
}

const handleComplete = (restock) => {
  currentRestock.value = restock
  operatorForm.operator = ''
  operatorVisible.value = true
}

const confirmComplete = async () => {
  try {
    await completeRestock(currentRestock.value.id, operatorForm.operator || '系统管理员')
    ElMessage.success('补货完成')
    operatorVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleCancel = (restock) => {
  currentRestock.value = restock
  cancelForm.reason = ''
  cancelVisible.value = true
}

const confirmCancel = async () => {
  if (!cancelForm.reason) {
    ElMessage.warning('请输入取消原因')
    return
  }
  try {
    await cancelRestock(currentRestock.value.id, cancelForm.reason)
    ElMessage.success('已取消')
    cancelVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>
