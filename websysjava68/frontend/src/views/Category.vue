<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">分类管理</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增分类
      </el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="loadData">
      <el-tab-pane label="支出分类" name="EXPENSE">
        <div class="data-table">
          <el-table :data="expenseCategories" v-loading="loading" stripe>
            <el-table-column prop="icon" label="图标" width="80">
              <template #default="{ row }">
                <span style="font-size: 20px">{{ row.icon || '💰' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="分类名称" min-width="150" />
            <el-table-column prop="sortOrder" label="排序" width="80" />
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
      </el-tab-pane>
      <el-tab-pane label="收入分类" name="INCOME">
        <div class="data-table">
          <el-table :data="incomeCategories" v-loading="loading" stripe>
            <el-table-column prop="icon" label="图标" width="80">
              <template #default="{ row }">
                <span style="font-size: 20px">{{ row.icon || '💰' }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="分类名称" min-width="150" />
            <el-table-column prop="sortOrder" label="排序" width="80" />
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
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类类型" prop="categoryType">
          <el-radio-group v-model="formData.categoryType">
            <el-radio value="INCOME">收入</el-radio>
            <el-radio value="EXPENSE">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="图标">
          <el-select v-model="formData.icon" placeholder="选择图标" style="width: 100%">
            <el-option v-for="icon in icons" :key="icon" :label="icon" :value="icon">
              <span style="font-size: 20px">{{ icon }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sortOrder" :min="0" :step="1" style="width: 100%" />
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
import { listCategories, addCategory, updateCategory, deleteCategory } from '@/api/category'

const loading = ref(false)
const expenseCategories = ref([])
const incomeCategories = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增分类')
const formRef = ref(null)
const isEdit = ref(false)
const activeTab = ref('EXPENSE')

const icons = ['🍜', '🚗', '🛍️', '🎮', '🏠', '💡', '💊', '📚', '📞', '💰', '🎁', '📈', '💼', '💸']

const formData = reactive({
  id: null,
  categoryName: '',
  categoryType: 'EXPENSE',
  icon: '💰',
  sortOrder: 0,
  remark: ''
})

const formRules = {
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
  categoryType: [{ required: true, message: '请选择分类类型', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const data = await listCategories()
    expenseCategories.value = data.filter(c => c.categoryType === 'EXPENSE')
    incomeCategories.value = data.filter(c => c.categoryType === 'INCOME')
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增分类'
  Object.assign(formData, {
    id: null,
    categoryName: '',
    categoryType: activeTab.value,
    icon: '💰',
    sortOrder: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑分类'
  Object.assign(formData, {
    id: row.id,
    categoryName: row.categoryName,
    categoryType: row.categoryType,
    icon: row.icon,
    sortOrder: row.sortOrder,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除分类"${row.categoryName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategory(row.id)
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
      await updateCategory(formData)
      ElMessage.success('更新成功')
    } else {
      await addCategory(formData)
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
