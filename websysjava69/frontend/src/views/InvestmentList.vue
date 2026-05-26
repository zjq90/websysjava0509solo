<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">投资组合</span>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        添加投资
      </el-button>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <div class="stat-card blue">
          <div class="stat-label">总市值</div>
          <div class="stat-value">¥{{ formatMoney(totalValue) }}</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card" :class="totalProfit >= 0 ? 'green' : 'orange'">
          <div class="stat-label">总盈亏</div>
          <div class="stat-value">
            {{ totalProfit >= 0 ? '+' : '' }}¥{{ formatMoney(totalProfit) }}
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card">
          <div class="stat-label">持仓数量</div>
          <div class="stat-value">{{ investments.length }}</div>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <el-table :data="investments" style="width: 100%" v-loading="loading">
        <el-table-column prop="investmentName" label="名称" />
        <el-table-column prop="code" label="代码" width="120" />
        <el-table-column prop="investmentType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ formatType(row.investmentType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="持仓数量" width="120">
          <template #default="{ row }">{{ row.quantity }}</template>
        </el-table-column>
        <el-table-column prop="costPrice" label="成本价" width="100">
          <template #default="{ row }">¥{{ formatMoney(row.costPrice) }}</template>
        </el-table-column>
        <el-table-column prop="currentPrice" label="现价" width="100">
          <template #default="{ row }">¥{{ formatMoney(row.currentPrice) }}</template>
        </el-table-column>
        <el-table-column prop="marketValue" label="市值" width="120">
          <template #default="{ row }">¥{{ formatMoney(row.marketValue) }}</template>
        </el-table-column>
        <el-table-column prop="profitLoss" label="盈亏" width="120">
          <template #default="{ row }">
            <span :class="row.profitLoss >= 0 ? 'positive' : 'negative'">
              {{ row.profitLoss >= 0 ? '+' : '' }}¥{{ formatMoney(row.profitLoss) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="profitLossRate" label="收益率" width="100">
          <template #default="{ row }">
            <span :class="row.profitLossRate >= 0 ? 'positive' : 'negative'">
              {{ row.profitLossRate >= 0 ? '+' : '' }}{{ row.profitLossRate?.toFixed(2) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑投资' : '添加投资'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="投资名称" prop="investmentName">
          <el-input v-model="form.investmentName" placeholder="请输入投资名称" />
        </el-form-item>
        <el-form-item label="代码" prop="code">
          <el-input v-model="form.code" placeholder="请输入代码" />
        </el-form-item>
        <el-form-item label="投资类型" prop="investmentType">
          <el-select v-model="form.investmentType" placeholder="请选择投资类型">
            <el-option label="股票" value="STOCK" />
            <el-option label="基金" value="FUND" />
            <el-option label="数字货币" value="CRYPTOCURRENCY" />
            <el-option label="债券" value="BOND" />
            <el-option label="定期存款" value="DEPOSIT" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="持仓数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0" :precision="4" style="width: 100%" />
        </el-form-item>
        <el-form-item label="成本价" prop="costPrice">
          <el-input-number v-model="form.costPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="现价" prop="currentPrice">
          <el-input-number v-model="form.currentPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input type="textarea" v-model="form.notes" :rows="2" />
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { investmentApi, API } from '@/api'

const loading = ref(false)
const investments = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = ref({
  id: null,
  userId: API.currentUserId,
  investmentName: '',
  code: '',
  investmentType: '',
  quantity: 0,
  costPrice: 0,
  currentPrice: 0,
  notes: ''
})

const rules = {
  investmentName: [{ required: true, message: '请输入投资名称', trigger: 'blur' }],
  investmentType: [{ required: true, message: '请选择投资类型', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入持仓数量', trigger: 'blur' }],
  costPrice: [{ required: true, message: '请输入成本价', trigger: 'blur' }],
  currentPrice: [{ required: true, message: '请输入现价', trigger: 'blur' }]
}

const totalValue = computed(() => {
  return investments.value.reduce((sum, i) => sum + Number(i.marketValue || 0), 0)
})

const totalProfit = computed(() => {
  return investments.value.reduce((sum, i) => sum + Number(i.profitLoss || 0), 0)
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatType = (type) => {
  const map = { STOCK: '股票', FUND: '基金', CRYPTOCURRENCY: '数字货币', BOND: '债券', DEPOSIT: '定期', OTHER: '其他' }
  return map[type] || type
}

const loadInvestments = async () => {
  loading.value = true
  try {
    investments.value = await investmentApi.list(API.currentUserId)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const openDialog = (row = null) => {
  if (row && row.id) {
    isEdit.value = true
    form.value = {
      id: row.id,
      userId: row.userId || API.currentUserId,
      investmentName: row.investmentName || '',
      code: row.code || '',
      investmentType: row.investmentType || '',
      quantity: row.quantity || 0,
      costPrice: row.costPrice || 0,
      currentPrice: row.currentPrice || 0,
      notes: row.notes || ''
    }
  } else {
    isEdit.value = false
    form.value = {
      id: null,
      userId: API.currentUserId,
      investmentName: '',
      code: '',
      investmentType: '',
      quantity: 0,
      costPrice: 0,
      currentPrice: 0,
      notes: ''
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (form.value.id && isEdit.value) {
      await investmentApi.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      const submitData = { ...form.value }
      delete submitData.id
      await investmentApi.create(submitData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadInvestments()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条投资记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await investmentApi.delete(row.id)
      ElMessage.success('删除成功')
      loadInvestments()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadInvestments()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
</style>
