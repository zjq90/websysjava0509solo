<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">分类标签管理</div>
    </div>

    <el-tabs v-model="activeTab" class="category-tag-tabs">
      <el-tab-pane label="游戏分类" name="category">
        <div class="card">
          <div class="table-toolbar">
            <el-button type="primary" @click="showCategoryDialog">
              <el-icon><Plus /></el-icon>
              新增分类
            </el-button>
          </div>

          <el-table :data="categories" v-loading="categoryLoading" border>
            <el-table-column prop="name" label="分类名称" width="150" />
            <el-table-column prop="sort" label="排序" width="100" />
            <el-table-column prop="gameCount" label="游戏数量" width="100" />
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditCategory(row)">编辑</el-button>
                <el-button type="danger" link @click="handleDeleteCategory(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>

      <el-tab-pane label="标签管理" name="tag">
        <div class="card">
          <div class="table-toolbar">
            <div>
              <el-button type="primary" @click="showTagDialog">
                <el-icon><Plus /></el-icon>
                新增标签
              </el-button>
              <el-button type="warning" @click="handleMergeTags" :disabled="selectedTags.length < 2">
                <el-icon><Promotion /></el-icon>
                合并标签 ({{ selectedTags.length }})
              </el-button>
              <el-button type="danger" @click="handleCleanTags">
                <el-icon><Delete /></el-icon>
                清理低使用率标签
              </el-button>
            </div>
          </div>

          <el-table
            :data="tags"
            v-loading="tagLoading"
            border
            @selection-change="handleTagSelectionChange"
          >
            <el-table-column type="selection" width="50" />
            <el-table-column prop="name" label="标签名称" width="150" />
            <el-table-column prop="alias" label="别名" width="150" />
            <el-table-column prop="gameCount" label="关联游戏数" width="120" />
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link @click="handleEditTag(row)">编辑</el-button>
                <el-button type="danger" link @click="handleDeleteTag(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="categoryDialogVisible" title="分类" width="400px">
      <el-form :model="categoryForm" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sort" :min="0" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="categoryForm.icon" placeholder="请输入图标URL" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveCategory">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="tagDialogVisible" title="标签" width="400px">
      <el-form :model="tagForm" label-width="80px">
        <el-form-item label="标签名称" required>
          <el-input v-model="tagForm.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="别名">
          <el-input v-model="tagForm.alias" placeholder="请输入别名，如：.io游戏" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="tagDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveTag">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="mergeDialogVisible" title="合并标签" width="400px">
      <el-form label-width="100px">
        <el-form-item label="目标标签" required>
          <el-select v-model="targetTagId" placeholder="请选择目标标签" style="width: 100%;">
            <el-option
              v-for="tag in mergeCandidates"
              :key="tag.id"
              :label="`${tag.name} (${tag.gameCount}个游戏)`"
              :value="tag.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="将被合并">
          <el-tag
            v-for="tag in sourceTags"
            :key="tag.id"
            style="margin-right: 8px; margin-bottom: 8px;"
          >
            {{ tag.name }}
          </el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="mergeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmMerge">确认合并</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getAllCategories, saveCategory as apiSaveCategory, updateCategory, deleteCategory,
  getAllTags, saveTag as apiSaveTag, updateTag, deleteTag, mergeTags, cleanLowUsageTags
} from '@/api/categoryTag'

const activeTab = ref('category')
const categoryLoading = ref(false)
const tagLoading = ref(false)
const categories = ref([])
const tags = ref([])
const selectedTags = ref([])

const categoryDialogVisible = ref(false)
const tagDialogVisible = ref(false)
const mergeDialogVisible = ref(false)
const targetTagId = ref(null)
const editingId = ref(null)

const categoryForm = reactive({
  name: '',
  sort: 0,
  icon: ''
})

const tagForm = reactive({
  name: '',
  alias: ''
})

const sourceTags = computed(() => {
  return selectedTags.value.filter(t => t.id !== targetTagId.value)
})

const mergeCandidates = computed(() => {
  return selectedTags.value
})

const loadCategories = async () => {
  categoryLoading.value = true
  try {
    categories.value = await getAllCategories()
  } finally {
    categoryLoading.value = false
  }
}

const loadTags = async () => {
  tagLoading.value = true
  try {
    tags.value = await getAllTags()
  } finally {
    tagLoading.value = false
  }
}

const showCategoryDialog = (row = null) => {
  editingId.value = row?.id || null
  categoryForm.name = row?.name || ''
  categoryForm.sort = row?.sort || 0
  categoryForm.icon = row?.icon || ''
  categoryDialogVisible.value = true
}

const handleSaveCategory = async () => {
  if (!categoryForm.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  if (editingId.value) {
    await updateCategory(editingId.value, categoryForm)
    ElMessage.success('更新成功')
  } else {
    await apiSaveCategory(categoryForm)
    ElMessage.success('创建成功')
  }
  categoryDialogVisible.value = false
  loadCategories()
}

const handleEditCategory = (row) => {
  showCategoryDialog(row)
}

const handleDeleteCategory = async (row) => {
  await ElMessageBox.confirm(`确定要删除分类「${row.name}」吗？`, '确认', { type: 'warning' })
  try {
    await deleteCategory(row.id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

const showTagDialog = (row = null) => {
  editingId.value = row?.id || null
  tagForm.name = row?.name || ''
  tagForm.alias = row?.alias || ''
  tagDialogVisible.value = true
}

const handleSaveTag = async () => {
  if (!tagForm.name.trim()) {
    ElMessage.warning('请输入标签名称')
    return
  }
  if (editingId.value) {
    await updateTag(editingId.value, tagForm)
    ElMessage.success('更新成功')
  } else {
    await apiSaveTag(tagForm)
    ElMessage.success('创建成功')
  }
  tagDialogVisible.value = false
  loadTags()
}

const handleEditTag = (row) => {
  showTagDialog(row)
}

const handleDeleteTag = async (row) => {
  await ElMessageBox.confirm(`确定要删除标签「${row.name}」吗？`, '确认', { type: 'warning' })
  try {
    await deleteTag(row.id)
    ElMessage.success('删除成功')
    loadTags()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

const handleTagSelectionChange = (selection) => {
  selectedTags.value = selection
}

const handleMergeTags = () => {
  if (selectedTags.value.length < 2) {
    ElMessage.warning('请至少选择2个标签进行合并')
    return
  }
  targetTagId.value = selectedTags.value[0].id
  mergeDialogVisible.value = true
}

const confirmMerge = async () => {
  if (!targetTagId.value) {
    ElMessage.warning('请选择目标标签')
    return
  }
  const sourceIds = sourceTags.value.map(t => t.id)
  if (sourceIds.length === 0) {
    ElMessage.warning('请选择要合并的源标签')
    return
  }
  await ElMessageBox.confirm(`将把 ${sourceIds.length} 个标签合并到目标标签，确定吗？`, '确认', { type: 'warning' })
  await mergeTags(targetTagId.value, sourceIds)
  ElMessage.success('合并成功')
  mergeDialogVisible.value = false
  loadTags()
}

const handleCleanTags = async () => {
  await ElMessageBox.confirm('确定要删除所有关联游戏数小于5的标签吗？此操作不可恢复！', '确认', { type: 'warning' })
  await cleanLowUsageTags(5)
  ElMessage.success('清理成功')
  loadTags()
}

onMounted(() => {
  loadCategories()
  loadTags()
})
</script>

<style scoped lang="scss">
.category-tag-tabs {
  :deep(.el-tabs__content) {
    padding-top: 0;
  }
}
</style>
