<template>
  <div class="card">
    <el-table :data="list" v-loading="loading" border>
      <el-table-column prop="gameName" label="游戏名称" min-width="150" />
      <el-table-column prop="sortWeight" label="排序权重" width="120" />
      <el-table-column label="生效时间" width="300">
        <template #default="{ row }">
          <div>{{ row.startTime || '不限' }} ~ {{ row.endTime || '不限' }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '生效中' : '已失效' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="!loading && list.length === 0" description="暂无推荐数据" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRecommendations, deleteRecommendation, updateRecommendation } from '@/api/recommendation'

const props = defineProps({
  type: {
    type: String,
    required: true
  },
  categoryId: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['refresh'])

const loading = ref(false)
const list = ref([])

const loadData = async () => {
  loading.value = true
  try {
    list.value = await getRecommendations(props.type, props.categoryId)
  } finally {
    loading.value = false
  }
}

const handleEdit = (row) => {
  ElMessageBox.prompt('请输入排序权重', '编辑排序权重', {
    inputValue: row.sortWeight,
    inputPattern: /^\d+$/,
    inputErrorMessage: '请输入数字'
  }).then(async ({ value }) => {
    await updateRecommendation(row.id, { sortWeight: parseInt(value) })
    ElMessage.success('更新成功')
    loadData()
  }).catch(() => {})
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定要删除推荐「${row.gameName}」吗？`, '确认', { type: 'warning' })
  await deleteRecommendation(row.id)
  ElMessage.success('删除成功')
  loadData()
}

watch(() => [props.type, props.categoryId], () => {
  loadData()
}, { deep: true })

onMounted(() => {
  loadData()
})
</script>
