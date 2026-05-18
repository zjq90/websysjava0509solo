<template>
  <div>
    <h1 class="page-title">📦 文物管理</h1>
    
    <div class="card-wrapper">
      <div class="table-header">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索文物名称..."
          class="search-box"
          clearable
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增文物
        </el-button>
      </div>

      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column prop="relicNo" label="文物编号" width="120" />
        <el-table-column prop="name" label="文物名称" min-width="180" />
        <el-table-column prop="category" label="类别" width="100" />
        <el-table-column prop="dynasty" label="朝代" width="100" />
        <el-table-column prop="material" label="材质" width="100" />
        <el-table-column prop="weight" label="重量(g)" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="location" label="存放位置" width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑文物' : '新增文物'"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="文物编号" prop="relicNo">
          <el-input v-model="form.relicNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="文物名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="青铜器" value="青铜器" />
            <el-option label="瓷器" value="瓷器" />
            <el-option label="书画" value="书画" />
            <el-option label="玉器" value="玉器" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="朝代" prop="dynasty">
          <el-select v-model="form.dynasty" style="width: 100%">
            <el-option label="商代" value="商代" />
            <el-option label="周代" value="周代" />
            <el-option label="汉代" value="汉代" />
            <el-option label="唐代" value="唐代" />
            <el-option label="宋代" value="宋代" />
            <el-option label="元代" value="元代" />
            <el-option label="明代" value="明代" />
            <el-option label="清代" value="清代" />
          </el-select>
        </el-form-item>
        <el-form-item label="材质" prop="material">
          <el-input v-model="form.material" />
        </el-form-item>
        <el-form-item label="重量(g)" prop="weight">
          <el-input-number v-model="form.weight" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="正常" :value="0" />
            <el-option label="修复中" :value="1" />
            <el-option label="展览中" :value="2" />
            <el-option label="封存" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="存放位置" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/api'

const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  id: null,
  relicNo: '',
  name: '',
  category: '',
  dynasty: '',
  material: '',
  weight: 0,
  status: 0,
  location: '',
  description: ''
})

const getStatusType = (status) => {
  const types = ['success', 'warning', 'primary', 'info']
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = ['正常', '修复中', '展览中', '封存']
  return texts[status] || '未知'
}

const loadData = async () => {
  try {
    const res = await request.get('/relic')
    tableData.value = res.data
    total.value = res.data.length
  } catch (e) {
    ElMessage.error('加载数据失败')
  }
}

const handleSearch = () => {
  ElMessage.info('搜索功能')
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: null,
    relicNo: 'RL' + Date.now().toString().slice(-6),
    name: '',
    category: '',
    dynasty: '',
    material: '',
    weight: 0,
    status: 0,
    location: '',
    description: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleView = (row) => {
  ElMessage.info('查看文物: ' + row.name)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该文物吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete('/relic/' + row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (e) {
      ElMessage.error('删除失败')
    }
  })
}

const handleSave = async () => {
  try {
    if (isEdit.value) {
      await request.put('/relic', form.value)
      ElMessage.success('更新成功')
    } else {
      await request.post('/relic', form.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

onMounted(() => {
  loadData()
})
</script>
