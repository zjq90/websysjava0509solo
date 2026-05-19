<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">药品管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增药品
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索药品名称"
        style="width: 300px"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-button type="primary" class="ml-10" @click="fetchMedicines">查询</el-button>
    </div>

    <div class="table-container">
      <el-table :data="medicineData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="medicineName" label="药品名称" width="150" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="description" label="说明" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="petType" label="适用宠物" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑药品' : '新增药品'"
    width="600px"
  >
    <el-form :model="form" ref="formRef" label-width="100px">
      <el-form-item label="药品名称" prop="medicineName">
        <el-input v-model="form.medicineName" />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="form.categoryId" style="width: 100%" @change="onCategoryChange">
          <el-option v-for="cat in categories" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="说明" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="2" />
      </el-form-item>
      <el-form-item label="生产厂家" prop="manufacturer">
        <el-input v-model="form.manufacturer" />
      </el-form-item>
      <el-form-item label="价格" prop="price">
        <el-input-number v-model="form.price" :min="0" :precision="2" />
      </el-form-item>
      <el-form-item label="库存" prop="stock">
        <el-input-number v-model="form.stock" :min="0" />
      </el-form-item>
      <el-form-item label="适用宠物" prop="petType">
        <el-input v-model="form.petType" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" style="width: 100%">
          <el-option label="启用" value="ACTIVE" />
          <el-option label="禁用" value="INACTIVE" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const medicineData = ref([])
const categories = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  medicineName: '',
  categoryId: null,
  categoryName: '',
  description: '',
  manufacturer: '',
  price: 0,
  stock: 0,
  petType: '通用',
  status: 'ACTIVE'
})

const fetchMedicines = async () => {
  try {
    const res = await axios.get('/api/medicine/list')
    if (res.data.code === 200) {
      medicineData.value = res.data.data.filter(item =>
        !searchKeyword.value || item.medicineName.includes(searchKeyword.value)
      )
    }
  } catch (e) {
    ElMessage.error('获取药品数据失败')
  }
}

const fetchCategories = async () => {
  try {
    const res = await axios.get('/api/medicine/categories')
    if (res.data.code === 200) {
      categories.value = res.data.data
    }
  } catch (e) {
    ElMessage.error('获取分类数据失败')
  }
}

const onCategoryChange = () => {
  const cat = categories.value.find(c => c.id === form.categoryId)
  if (cat) form.categoryName = cat.categoryName
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    medicineName: '',
    categoryId: null,
    categoryName: '',
    description: '',
    manufacturer: '',
    price: 0,
    stock: 0,
    petType: '通用',
    status: 'ACTIVE'
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    const url = isEdit.value ? `/api/medicine/${form.id}` : '/api/medicine'
    const method = isEdit.value ? 'put' : 'post'
    const res = await axios[method](url, form)
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      fetchMedicines()
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该药品吗?', '提示')
    const res = await axios.delete(`/api/medicine/${id}`)
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      fetchMedicines()
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => {
  fetchCategories()
  fetchMedicines()
})
</script>
