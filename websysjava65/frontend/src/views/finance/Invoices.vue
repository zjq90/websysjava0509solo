<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">发票管理</div>
      <div>
        <el-button type="primary" :icon="Plus" @click="showSingleDialog = true">
          单个开票
        </el-button>
        <el-button type="success" :icon="Tickets" @click="showBatchDialog = true" style="margin-left: 10px">
          批量开票
        </el-button>
      </div>
    </div>

    <el-card>
      <el-table :data="invoices" stripe style="width: 100%">
        <el-table-column prop="invoiceNo" label="发票编号" width="150" />
        <el-table-column prop="username" label="用户" width="100" />
        <el-table-column prop="companyName" label="企业名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="invoiceType" label="发票类型" width="100">
          <template #default="{ row }">
            <span :class="row.invoiceType === 'SPECIAL' ? 'badge-warning' : 'badge-info'">
              {{ row.invoiceType === 'SPECIAL' ? '增值税专票' : '普通发票' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="invoiceAmount" label="开票金额" width="120">
          <template #default="{ row }">¥{{ row.invoiceAmount }}</template>
        </el-table-column>
        <el-table-column prop="taxAmount" label="税额" width="100">
          <template #default="{ row }">¥{{ row.taxAmount }}</template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="价税合计" width="120">
          <template #default="{ row }">¥{{ row.totalAmount }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <span class="badge-success">已开具</span>
          </template>
        </el-table-column>
        <el-table-column prop="issueTime" label="开票时间" width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button size="small" link>详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showSingleDialog" title="单个开票" width="600px">
      <el-form :model="singleForm" label-width="100px">
        <el-form-item label="用户ID">
          <el-input v-model="singleForm.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="singleForm.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="税号">
          <el-input v-model="singleForm.taxNo" placeholder="请输入税号" />
        </el-form-item>
        <el-form-item label="企业地址">
          <el-input v-model="singleForm.companyAddress" placeholder="请输入企业地址" />
        </el-form-item>
        <el-form-item label="企业电话">
          <el-input v-model="singleForm.companyPhone" placeholder="请输入企业电话" />
        </el-form-item>
        <el-form-item label="开票金额">
          <el-input v-model="singleForm.invoiceAmount" placeholder="请输入开票金额" />
        </el-form-item>
        <el-form-item label="发票类型">
          <el-select v-model="singleForm.invoiceType" style="width: 100%">
            <el-option label="普通发票" value="NORMAL" />
            <el-option label="增值税专票" value="SPECIAL" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSingleDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateInvoice">确认开票</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showBatchDialog" title="批量开票" width="500px">
      <el-alert
        title="批量开票功能"
        type="info"
        :closable="false"
        style="margin-bottom: 20px"
      >
        <template #default>
          上传企业用户列表，系统将为符合条件的企业用户批量开具发票
        </template>
      </el-alert>
      <el-upload
        drag
        action="#"
        :auto-upload="false"
        style="text-align: center"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          拖拽文件至此处或 <em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">支持 xlsx、csv 格式的企业用户列表文件</div>
        </template>
      </el-upload>
      <template #footer>
        <el-button @click="showBatchDialog = false">取消</el-button>
        <el-button type="primary" @click="batchInvoice">开始批量开票</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Tickets, UploadFilled } from '@element-plus/icons-vue'
import { getInvoices, createInvoice as apiCreateInvoice } from '../../api'

const invoices = ref([])
const showSingleDialog = ref(false)
const showBatchDialog = ref(false)
const singleForm = ref({
  userId: '',
  companyName: '',
  taxNo: '',
  companyAddress: '',
  companyPhone: '',
  invoiceAmount: '',
  invoiceType: 'NORMAL'
})

const loadData = async () => {
  try {
    const res = await getInvoices()
    invoices.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const handleCreateInvoice = async () => {
  if (!singleForm.value.userId || !singleForm.value.companyName || !singleForm.value.invoiceAmount) {
    ElMessage.warning('请填写必填信息')
    return
  }
  try {
    await apiCreateInvoice(singleForm.value)
    ElMessage.success('开票成功')
    showSingleDialog.value = false
    singleForm.value = {
      userId: '', companyName: '', taxNo: '', companyAddress: '',
      companyPhone: '', invoiceAmount: '', invoiceType: 'NORMAL'
    }
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const batchInvoice = () => {
  ElMessage.success('批量开票任务已提交，处理中...')
  showBatchDialog.value = false
}

onMounted(() => {
  loadData()
})
</script>
