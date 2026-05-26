<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">账单标签</div>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增标签
      </el-button>
    </div>

    <div class="data-table">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">
            <span style="font-size: 20px">{{ row.icon || '🏷️' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="tagName" label="标签名称" min-width="150" />
        <el-table-column prop="tagColor" label="标签颜色" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.tagColor" :color="row.tagColor" effect="dark">{{ row.tagColor }}</el-tag>
            <span v-else style="color: #909399">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="tagType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.tagType === 'SYSTEM' ? 'info' : ''">
              {{ row.tagType === 'SYSTEM' ? '系统' : '自定义' }}
            </el-tag>
          </template>
        </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="formData.tagName" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="标签颜色">
          <el-color-picker v-model="formData.tagColor" />
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
import { listTags, addTag, updateTag, deleteTag } from '@/api/tag'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增标签')
const formRef = ref(null)
const isEdit = ref(false)

const icons = ['✈️', '🎂', '💊', '💼', '👨‍👩‍👧', '📖', '💪', '🎉', '🏷️', '⭐', '❤️', '🔥']

const formData = reactive({
  id: null,
  tagName: '',
  tagColor: '',
  icon: '🏷️',
  sortOrder: 0,
  remark: ''
})

const formRules = {
  tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    tableData.value = await listTags()
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增标签'
  Object.assign(formData, {
    id: null,
    tagName: '',
    tagColor: '',
    icon: '🏷️',
    sortOrder: 0,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑标签'
  Object.assign(formData, {
    id: row.id,
    tagName: row.tagName,
    tagColor: row.tagColor,
    icon: row.icon,
    sortOrder: row.sortOrder,
    remark: row.remark
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除标签"${row.tagName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTag(row.id)
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
      await updateTag(formData)
      ElMessage.success('更新成功')
    } else {
      await addTag(formData)
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
