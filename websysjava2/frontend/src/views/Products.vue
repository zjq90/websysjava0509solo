<template>
  <div>
    <div class="page-header">
      <h2>商品管理</h2>
      <div>
        <el-input
          v-model="searchKeyword"
          placeholder="搜索商品名称或条形码"
          style="width: 250px; margin-right: 10px;"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        <el-button type="primary" icon="Plus" @click="handleAdd">
          新增商品
        </el-button>
      </div>
    </div>

    <div class="table-container">
      <el-table :data="products" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column label="商品名称" min-width="150">
          <template #default="scope">
            <div style="display: flex; align-items: center;">
              <el-avatar
                v-if="scope.row.imageUrl"
                :src="scope.row.imageUrl"
                size="small"
                style="margin-right: 10px;"
              />
              <span>{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="barcode" label="条形码" width="140" />
        <el-table-column prop="specification" label="规格" width="120" />
        <el-table-column label="分类" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.category" size="small">
              {{ scope.row.category.name }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="成本价" width="90">
          <template #default="scope">
            ¥{{ scope.row.costPrice }}
          </template>
        </el-table-column>
        <el-table-column label="零售价" width="90">
          <template #default="scope">
            ¥{{ scope.row.retailPrice }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="scope">
            <el-switch
              v-model="scope.row.active"
              active-text="上架"
              inactive-text="下架"
              active-color="#67c23a"
              inactive-color="#c0c4cc"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" link @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" link @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="selectedIds.length > 0" style="margin-top: 15px;">
        <span style="margin-right: 15px;">已选 {{ selectedIds.length }} 项</span>
        <el-button type="success" size="small" @click="handleBatchUpdateStatus(true)">
          批量上架
        </el-button>
        <el-button type="warning" size="small" @click="handleBatchUpdateStatus(false)">
          批量下架
        </el-button>
      </div>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑商品' : '新增商品'"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="条形码">
          <el-input v-model="form.barcode" placeholder="请输入条形码" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.specification" placeholder="如：330ml/罐" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="成本价" prop="costPrice">
          <el-input-number
            v-model="form.costPrice"
            :precision="2"
            :min="0"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="零售价" prop="retailPrice">
          <el-input-number
            v-model="form.retailPrice"
            :precision="2"
            :min="0"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="库存阈值">
          <el-input-number
            v-model="form.stockThreshold"
            :min="0"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="form.active"
            active-text="上架"
            inactive-text="下架"
            active-color="#67c23a"
            inactive-color="#c0c4cc"
          />
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
import {
  getProducts,
  searchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  batchUpdateStatus,
  getCategories
} from '../api/product'

const products = ref([])
const categories = ref([])
const searchKeyword = ref('')
const selectedIds = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  barcode: '',
  specification: '',
  categoryId: null,
  costPrice: 0,
  retailPrice: 0,
  stockThreshold: 5,
  description: '',
  imageUrl: '',
  active: true
})

const rules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  costPrice: [{ required: true, message: '请输入成本价', trigger: 'blur' }],
  retailPrice: [{ required: true, message: '请输入零售价', trigger: 'blur' }]
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.barcode = ''
  form.specification = ''
  form.categoryId = null
  form.costPrice = 0
  form.retailPrice = 0
  form.stockThreshold = 5
  form.description = ''
  form.imageUrl = ''
  form.active = true
}

const loadProducts = async () => {
  try {
    products.value = await getProducts()
  } catch (e) {
    console.error(e)
  }
}

const loadCategories = async () => {
  try {
    categories.value = await getCategories()
  } catch (e) {
    console.error(e)
  }
}

const handleSearch = async () => {
  try {
    if (searchKeyword.value) {
      products.value = await searchProducts(searchKeyword.value)
    } else {
      await loadProducts()
    }
  } catch (e) {
    console.error(e)
  }
}

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const handleStatusChange = async (row) => {
  try {
    await batchUpdateStatus([row.id], row.active)
    ElMessage.success(row.active ? '已上架' : '已下架')
  } catch (e) {
    row.active = !row.active
    console.error(e)
  }
}

const handleBatchUpdateStatus = async (active) => {
  try {
    await batchUpdateStatus(selectedIds.value, active)
    ElMessage.success('批量操作成功')
    loadProducts()
  } catch (e) {
    console.error(e)
  }
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.id = row.id
  form.name = row.name
  form.barcode = row.barcode
  form.specification = row.specification
  form.categoryId = row.category?.id || null
  form.costPrice = row.costPrice
  form.retailPrice = row.retailPrice
  form.stockThreshold = row.stockThreshold
  form.description = row.description
  form.imageUrl = row.imageUrl
  form.active = row.active
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除商品"${row.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteProduct(row.id)
      ElMessage.success('删除成功')
      loadProducts()
    } catch (e) {
      console.error(e)
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    const data = {
      name: form.name,
      barcode: form.barcode,
      specification: form.specification,
      costPrice: form.costPrice,
      retailPrice: form.retailPrice,
      stockThreshold: form.stockThreshold,
      description: form.description,
      imageUrl: form.imageUrl,
      active: form.active
    }
    
    if (isEdit.value) {
      await updateProduct(form.id, data, form.categoryId)
      ElMessage.success('更新成功')
    } else {
      await createProduct(data, form.categoryId)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadProducts()
  } catch (e) {
    if (e !== false) {
      console.error(e)
    }
  }
}

onMounted(() => {
  loadProducts()
  loadCategories()
})
</script>
