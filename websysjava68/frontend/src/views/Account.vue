<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">账户管理</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增账户
      </el-button>
    </div>

    <div class="data-table">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">
            <span style="font-size: 24px">{{ row.icon || '💰' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="accountName" label="账户名称" min-width="150" />
        <el-table-column prop="accountType" label="账户类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getAccountTypeTag(row.accountType)">{{ getAccountTypeName(row.accountType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accountNumber" label="账户号码" width="180">
          <template #default="{ row }">
            <span v-if="row.accountNumber">{{ row.accountNumber }}</span>
            <span v-else style="color: #909399">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="150">
          <template #default="{ row }">
            <span :class="row.balance >= 0 ? 'positive' : 'negative'">
              ¥{{ formatMoney(row.balance) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="currency" label="币种" width="80" />
        <el-table-column prop="isDefault" label="默认" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.isDefault === 1" type="success" size="small">是</el-tag>
            <span v-else style="color: #909399">否</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button link type="danger" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="账户名称" prop="accountName">
          <el-input v-model="formData.accountName" placeholder="请输入账户名称" />
        </el-form-item>
        <el-form-item label="账户类型" prop="accountType">
          <el-select v-model="formData.accountType" placeholder="请选择账户类型" style="width: 100%">
            <el-option label="现金" value="CASH" />
            <el-option label="银行卡" value="BANK_CARD" />
            <el-option label="信用卡" value="CREDIT_CARD" />
            <el-option label="支付宝" value="ALIPAY" />
            <el-option label="微信钱包" value="WECHAT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="账户号码">
          <el-input v-model="formData.accountNumber" placeholder="银行卡号/账号" />
        </el-form-item>
        <el-form-item label="余额" prop="balance">
          <el-input-number v-model="formData.balance" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="币种">
          <el-select v-model="formData.currency" style="width: 100%">
            <el-option label="人民币 (CNY)" value="CNY" />
            <el-option label="美元 (USD)" value="USD" />
            <el-option label="欧元 (EUR)" value="EUR" />
          </el-select>
        </el-form-item>
        <el-form-item label="图标">
          <el-select v-model="formData.icon" placeholder="选择图标" style="width: 100%">
            <el-option v-for="icon in icons" :key="icon" :label="icon" :value="icon">
              <span style="font-size: 20px">{{ icon }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="formData.isDefault" :active-value="1" :inactive-value="0" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="备注说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listAccounts, addAccount, updateAccount, deleteAccount } from '@/api/account'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增账户')
const formRef = ref(null)
const isEdit = ref(false)

const icons = ['💵', '🏦', '💳', '📱', '💬', '💰', '💎', '📊']

const formData = reactive({
  id: null,
  accountName: '',
  accountType: 'BANK_CARD',
  accountNumber: '',
  balance: 0,
  currency: 'CNY',
  icon: '🏦',
  isDefault: 0,
  remark: ''
})

const formRules = {
  accountName: [{ required: true, message: '请输入账户名称', trigger: 'blur' }],
  accountType: [{ required: true, message: '请选择账户类型', trigger: 'change' }],
  balance: [{ required: true, message: '请输入余额', trigger: 'blur' }]
}

const formatMoney = (value) => {
  return Number(value).toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

const getAccountTypeName = (type) => {
  const map = {
    CASH: '现金',
    BANK_CARD: '银行卡',
    CREDIT_CARD: '信用卡',
    ALIPAY: '支付宝',
    WECHAT: '微信钱包',
    OTHER: '其他'
  }
  return map[type] || type
}

const getAccountTypeTag = (type) => {
  const map = {
    CASH: 'warning',
    BANK_CARD: '',
    CREDIT_CARD: 'danger',
    ALIPAY: 'success',
    WECHAT: 'success',
    OTHER: 'info'
  }
  return map[type] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await listAccounts()
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增账户'
  Object.assign(formData, {
    id: null,
    accountName: '',
    accountType: 'BANK_CARD',
    accountNumber: '',
    balance: 0,
    currency: 'CNY',
    icon: '🏦',
    isDefault: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑账户'
  Object.assign(formData, {
    id: row.id,
    accountName: row.accountName,
    accountType: row.accountType,
    accountNumber: row.accountNumber,
    balance: row.balance,
    currency: row.currency,
    icon: row.icon,
    isDefault: row.isDefault,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除账户"${row.accountName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAccount(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateAccount(formData)
      ElMessage.success('更新成功')
    } else {
      await addAccount(formData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error !== false) {
      console.error('提交失败:', error)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.positive {
  color: #67c23a;
  font-weight: bold;
}

.negative {
  color: #f56c6c;
  font-weight: bold;
}
</style>
