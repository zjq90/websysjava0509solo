<template>
  <div class="page-container">
    <div class="page-header">
      <span class="page-title">资产管理</span>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>
        添加资产
      </el-button>
    </div>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <div class="stat-card green">
          <div class="stat-label">资产总额</div>
          <div class="stat-value">¥{{ formatMoney(totalAssets) }}</div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="stat-card blue">
          <div class="stat-label">资产数量</div>
          <div class="stat-value">{{ assets.length }}</div>
        </div>
      </el-col>
    </el-row>

    <div class="card-shadow p-20">
      <el-table :data="assets" style="width: 100%" v-loading="loading">
        <el-table-column prop="assetName" label="资产名称" />
        <el-table-column prop="assetType" label="类型" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ formatAssetType(row.assetType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="originalValue" label="原值" width="120">
          <template #default="{ row }">¥{{ formatMoney(row.originalValue) }}</template>
        </el-table-column>
        <el-table-column prop="currentValue" label="现值" width="120">
          <template #default="{ row }">¥{{ formatMoney(row.currentValue) }}</template>
        </el-table-column>
        <el-table-column label="增值" width="120">
          <template #default="{ row }">
            <span :class="(row.currentValue - row.originalValue) >= 0 ? 'positive' : 'negative'">
              {{ (row.currentValue - row.originalValue) >= 0 ? '+' : '' }}¥{{ formatMoney(row.currentValue - row.originalValue) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="位置/备注" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑资产' : '添加资产'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="资产名称" prop="assetName">
          <el-input v-model="form.assetName" placeholder="请输入资产名称" />
        </el-form-item>
        <el-form-item label="资产类型" prop="assetType">
          <el-select v-model="form.assetType" placeholder="请选择资产类型">
            <el-option label="现金" value="CASH" />
            <el-option label="银行存款" value="BANK_DEPOSIT" />
            <el-option label="房产" value="REAL_ESTATE" />
            <el-option label="车辆" value="VEHICLE" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="原值" prop="originalValue">
          <el-input-number v-model="form.originalValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="现值" prop="currentValue">
          <el-input-number v-model="form.currentValue" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="位置/备注" prop="location">
          <el-input v-model="form.location" placeholder="请输入位置或备注" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input type="textarea" v-model="form.description" :rows="2" />
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
import { assetApi, API } from '@/api'

const loading = ref(false)
const assets = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = ref({
  id: null,
  userId: API.currentUserId,
  assetName: '',
  assetType: '',
  originalValue: 0,
  currentValue: 0,
  location: '',
  description: ''
})

const rules = {
  assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
  assetType: [{ required: true, message: '请选择资产类型', trigger: 'change' }],
  originalValue: [{ required: true, message: '请输入原值', trigger: 'blur' }],
  currentValue: [{ required: true, message: '请输入现值', trigger: 'blur' }]
}

const totalAssets = computed(() => {
  return assets.value.reduce((sum, a) => sum + Number(a.currentValue || 0), 0)
})

const formatMoney = (value) => {
  if (!value && value !== 0) return '0.00'
  return Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const formatAssetType = (type) => {
  const map = { CASH: '现金', BANK_DEPOSIT: '银行存款', REAL_ESTATE: '房产', VEHICLE: '车辆', OTHER: '其他' }
  return map[type] || type
}

const loadAssets = async () => {
  loading.value = true
  try {
    assets.value = await assetApi.list(API.currentUserId)
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
      assetName: row.assetName || '',
      assetType: row.assetType || '',
      originalValue: row.originalValue || 0,
      currentValue: row.currentValue || 0,
      location: row.location || '',
      description: row.description || ''
    }
  } else {
    isEdit.value = false
    form.value = {
      id: null,
      userId: API.currentUserId,
      assetName: '',
      assetType: '',
      originalValue: 0,
      currentValue: 0,
      location: '',
      description: ''
    }
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (form.value.id && isEdit.value) {
      await assetApi.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      const submitData = { ...form.value }
      delete submitData.id
      await assetApi.create(submitData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadAssets()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条资产记录吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await assetApi.delete(row.id)
      ElMessage.success('删除成功')
      loadAssets()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadAssets()
})
</script>

<style scoped>
.p-20 { padding: 20px; }
</style>
