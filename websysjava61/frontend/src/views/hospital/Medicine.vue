<template>
  <div class="medicine-page">
    <div class="page-card">
      <div class="page-title">药品管理</div>
      
      <div class="toolbar">
        <el-button type="warning" @click="showWarningOnly = !showWarningOnly">
          <el-icon><Warning /></el-icon>
          {{ showWarningOnly ? '显示全部' : '仅显示预警' }}
        </el-button>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增药品
        </el-button>
      </div>

      <el-table :data="filteredData" border stripe style="width: 100%" :row-class-name="tableRowClassName">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="药品名称" />
        <el-table-column prop="code" label="药品编码" />
        <el-table-column prop="category" label="分类" width="100" />
        <el-table-column prop="specification" label="规格" />
        <el-table-column prop="manufacturer" label="生产厂家" />
        <el-table-column prop="stockQuantity" label="库存数量" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.stockQuantity <= row.warningQuantity ? '#F56C6C' : '' }">
              {{ row.stockQuantity }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="warningQuantity" label="预警数量" width="100" />
        <el-table-column prop="price" label="单价(元)" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑药品' : '新增药品'" width="600px">
      <el-form :model="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="药品名称">
              <el-input v-model="form.name" placeholder="请输入药品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="药品编码">
              <el-input v-model="form.code" placeholder="请输入药品编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类">
              <el-input v-model="form.category" placeholder="请输入分类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格">
              <el-input v-model="form.specification" placeholder="请输入规格" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="生产厂家">
          <el-input v-model="form.manufacturer" placeholder="请输入生产厂家" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="库存数量">
              <el-input-number v-model="form.stockQuantity" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预警数量">
              <el-input-number v-model="form.warningQuantity" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单价">
              <el-input-number v-model="form.price" :min="0" :precision="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位">
              <el-input v-model="form.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { getMedicines, createMedicine, updateMedicine, deleteMedicine } from '@/api'

const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const showWarningOnly = ref(false)
const form = ref({
  name: '',
  code: '',
  category: '',
  specification: '',
  manufacturer: '',
  stockQuantity: 0,
  warningQuantity: 10,
  price: 0,
  unit: '',
  description: '',
  status: 1
})

const filteredData = computed(() => {
  if (showWarningOnly.value) {
    return tableData.value.filter(item => item.stockQuantity <= item.warningQuantity)
  }
  return tableData.value
})

const tableRowClassName = ({ row }) => {
  if (row.stockQuantity <= row.warningQuantity) {
    return 'warning-row'
  }
  return ''
}

const loadData = async () => {
  const res = await getMedicines()
  tableData.value = res.data
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    name: '',
    code: '',
    category: '',
    specification: '',
    manufacturer: '',
    stockQuantity: 0,
    warningQuantity: 10,
    price: 0,
    unit: '',
    description: '',
    status: 1
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (isEdit.value) {
    await updateMedicine(form.value.id, form.value)
    ElMessage.success('编辑成功')
  } else {
    await createMedicine(form.value)
    ElMessage.success('新增成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该药品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteMedicine(row.id)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => {
  loadData()
})
</script>
