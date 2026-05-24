<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">客服工单</div>
      <el-radio-group v-model="statusFilter" @change="loadData">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="PENDING">待处理</el-radio-button>
        <el-radio-button label="PROCESSING">处理中</el-radio-button>
        <el-radio-button label="RESOLVED">已解决</el-radio-button>
      </el-radio-group>
    </div>

    <el-card>
      <el-table :data="tickets" stripe style="width: 100%">
        <el-table-column prop="ticketNo" label="工单号" width="150" />
        <el-table-column prop="username" label="用户" width="100" />
        <el-table-column prop="title" label="标题" width="200" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <span :class="row.type === 'REFUND' ? 'badge-primary' : 'badge-warning'">
              {{ row.type === 'REFUND' ? '退款申诉' : '投诉反馈' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="refundAmount" label="退款金额" width="100">
          <template #default="{ row }">
            {{ row.refundAmount ? '¥' + row.refundAmount : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span :class="getStatusClass(row.status)">
              {{ getStatusText(row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" link @click="viewTicket(row)">查看</el-button>
            <el-button v-if="row.status !== 'RESOLVED'" type="primary" size="small" link @click="handleTicket(row)">
              处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showDetailDialog" title="工单详情" width="600px">
      <el-descriptions :column="2" border v-if="currentTicket">
        <el-descriptions-item label="工单号">{{ currentTicket.ticketNo }}</el-descriptions-item>
        <el-descriptions-item label="用户">{{ currentTicket.username }}</el-descriptions-item>
        <el-descriptions-item label="类型" :span="2">
          {{ currentTicket.type === 'REFUND' ? '退款申诉' : '投诉反馈' }}
        </el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ currentTicket.title }}</el-descriptions-item>
        <el-descriptions-item label="工单内容" :span="2">
          <div style="white-space: pre-wrap;">{{ currentTicket.content }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <span :class="getStatusClass(currentTicket.status)">
            {{ getStatusText(currentTicket.status) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentTicket.reply" label="回复内容" :span="2">
          <div style="white-space: pre-wrap;">{{ currentTicket.reply }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showHandleDialog" title="处理工单" width="500px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="handleForm.reply" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="handleForm.status" style="width: 100%">
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已解决" value="RESOLVED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showHandleDialog = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getTickets, handleTicket as apiHandleTicket } from '../../api'

const tickets = ref([])
const statusFilter = ref('')
const showDetailDialog = ref(false)
const showHandleDialog = ref(false)
const currentTicket = ref(null)
const currentTicketId = ref(null)
const handleForm = ref({
  reply: '',
  status: 'RESOLVED'
})

const getStatusClass = (status) => {
  const map = {
    PENDING: 'badge-warning',
    PROCESSING: 'badge-primary',
    RESOLVED: 'badge-success'
  }
  return map[status] || 'badge-info'
}

const getStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '处理中',
    RESOLVED: '已解决'
  }
  return map[status] || status
}

const loadData = async () => {
  try {
    const res = await getTickets(statusFilter.value)
    tickets.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const viewTicket = (row) => {
  currentTicket.value = row
  showDetailDialog.value = true
}

const handleTicket = (row) => {
  currentTicketId.value = row.id
  handleForm.value = { reply: row.reply || '', status: 'RESOLVED' }
  showHandleDialog.value = true
}

const submitHandle = async () => {
  if (!handleForm.value.reply) {
    ElMessage.warning('请输入回复内容')
    return
  }
  try {
    await apiHandleTicket(currentTicketId.value, {
      reply: handleForm.value.reply,
      status: handleForm.value.status
    })
    ElMessage.success('处理成功')
    showHandleDialog.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>
