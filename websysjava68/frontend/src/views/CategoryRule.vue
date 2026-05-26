<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">分类规则</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增规则
      </el-button>
    </div>

    <div class="data-table">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="ruleName" label="规则名称" min-width="150" />
        <el-table-column prop="matchField" label="匹配字段" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ getMatchFieldName(row.matchField) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="matchType" label="匹配类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getMatchTypeTag(row.matchType)" size="small">{{ getMatchTypeName(row.matchType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="matchValue" label="匹配值/正则" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <code style="background: #f5f7fa; padding: 2px 6px; border-radius: 4px;">{{ row.matchValue }}</code>
          </template>
        </el-table-column>
        <el-table-column label="目标分类" width="120">
          <template #default="{ row }">
            <span>{{ getCategoryName(row.targetCategoryId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" />
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.isEnabled" :active-value="1" :inactive-value="0" @change="handleToggleEnable(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="100" show-overflow-tooltip />
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="formData.ruleName" placeholder="请输入规则名称" />
        </el-form-item>
        <el-form-item label="匹配字段" prop="matchField">
          <el-select v-model="formData.matchField" placeholder="请选择匹配字段" style="width: 100%">
            <el-option label="交易描述" value="DESCRIPTION" />
            <el-option label="商家名称" value="MERCHANT" />
            <el-option label="交易地点" value="LOCATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="匹配类型" prop="matchType">
          <el-radio-group v-model="formData.matchType">
            <el-radio value="CONTAINS">包含</el-radio>
            <el-radio value="EQUALS">等于</el-radio>
            <el-radio value="REGEX">正则表达式</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="匹配值" prop="matchValue">
          <el-input v-model="formData.matchValue" :placeholder="formData.matchType === 'REGEX' ? '请输入正则表达式' : '请输入匹配值'" />
        </el-form-item>
        <el-form-item label="目标分类" prop="targetCategoryId">
          <el-select v-model="formData.targetCategoryId" placeholder="请选择目标分类" style="width: 100%" filterable>
            <el-option v-for="category in categories" :key="category.id" :label="`${category.categoryName} (${category.categoryType === 'INCOME' ? '收入' : '支出'})`" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input-number v-model="formData.priority" :min="0" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="启用">
          <el-switch v-model="formData.isEnabled" :active-value="1" :inactive-value="0" />
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
import { listCategoryRules, addCategoryRule, updateCategoryRule, deleteCategoryRule } from '@/api/categoryRule'
import { listCategories } from '@/api/category'

const loading = ref(false)
const tableData = ref([])
const categories = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增规则')
const formRef = ref(null)
const isEdit = ref(false)

const formData = reactive({
  id: null,
  ruleName: '',
  matchField: 'MERCHANT',
  matchType: 'CONTAINS',
  matchValue: '',
  targetCategoryId: null,
  priority: 0,
  isEnabled: 1,
  remark: ''
})

const formRules = {
  ruleName: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  matchField: [{ required: true, message: '请选择匹配字段', trigger: 'change' }],
  matchType: [{ required: true, message: '请选择匹配类型', trigger: 'change' }],
  matchValue: [{ required: true, message: '请输入匹配值', trigger: 'blur' }],
  targetCategoryId: [{ required: true, message: '请选择目标分类', trigger: 'change' }]
}

const getMatchFieldName = (field) => {
  const map = {
    DESCRIPTION: '交易描述',
    MERCHANT: '商家名称',
    LOCATION: '交易地点'
  }
  return map[field] || field
}

const getMatchTypeName = (type) => {
  const map = {
    CONTAINS: '包含',
    EQUALS: '等于',
    REGEX: '正则'
  }
  return map[type] || type
}

const getMatchTypeTag = (type) => {
  const map = {
    CONTAINS: '',
    EQUALS: 'success',
    REGEX: 'warning'
  }
  return map[type] || 'info'
}

const getCategoryName = (id) => {
  const category = categories.value.find(c => c.id === id)
  return category ? category.categoryName : '-'
}

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await listCategoryRules()
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    categories.value = await listCategories()
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增规则'
  Object.assign(formData, {
    id: null,
    ruleName: '',
    matchField: 'MERCHANT',
    matchType: 'CONTAINS',
    matchValue: '',
    targetCategoryId: null,
    priority: 0,
    isEnabled: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑规则'
  Object.assign(formData, {
    id: row.id,
    ruleName: row.ruleName,
    matchField: row.matchField,
    matchType: row.matchType,
    matchValue: row.matchValue,
    targetCategoryId: row.targetCategoryId,
    priority: row.priority,
    isEnabled: row.isEnabled,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除规则"${row.ruleName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteCategoryRule(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  }).catch(() => {})
}

const handleToggleEnable = async (row) => {
  try {
    await updateCategoryRule(row)
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('更新状态失败:', error)
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateCategoryRule(formData)
      ElMessage.success('更新成功')
    } else {
      await addCategoryRule(formData)
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
  loadCategories()
  loadData()
})
</script>
