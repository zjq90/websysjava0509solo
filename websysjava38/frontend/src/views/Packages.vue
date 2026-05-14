<template>
  <div class="packages">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>套餐管理</span>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新增套餐
          </el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="套餐名称"
          style="width: 150px"
          clearable
        />
        <el-select v-model="searchForm.type" placeholder="套餐类型" style="width: 150px" clearable>
          <el-option label="婚纱" value="婚纱" />
          <el-option label="写真" value="写真" />
          <el-option label="全家福" value="全家福" />
          <el-option label="儿童" value="儿童" />
        </el-select>
        <el-button type="primary" @click="loadPackages">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>

      <el-table :data="packageList" stripe border>
        <el-table-column prop="name" label="套餐名称" width="150" />
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            <span style="color: #F56C6C; font-weight: bold">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="photoCount" label="拍摄张数" width="100" />
        <el-table-column prop="retouchCount" label="精修张数" width="100" />
        <el-table-column prop="albumCount" label="相册数量" width="100" />
        <el-table-column prop="frameCount" label="相框数量" width="100" />
        <el-table-column prop="shootingDays" label="拍摄天数" width="100" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="editPackage(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="deletePackage(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑套餐' : '新增套餐'" width="700px">
      <el-form :model="packageForm" label-width="120px">
        <el-form-item label="套餐名称">
          <el-input v-model="packageForm.name" placeholder="请输入套餐名称" />
        </el-form-item>
        <el-form-item label="套餐类型">
          <el-select v-model="packageForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="婚纱" value="婚纱" />
            <el-option label="写真" value="写真" />
            <el-option label="全家福" value="全家福" />
            <el-option label="儿童" value="儿童" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="packageForm.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="拍摄张数">
              <el-input-number v-model="packageForm.photoCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="精修张数">
              <el-input-number v-model="packageForm.retouchCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="相册数量">
              <el-input-number v-model="packageForm.albumCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="相框数量">
              <el-input-number v-model="packageForm.frameCount" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="拍摄天数">
          <el-input-number v-model="packageForm.shootingDays" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="packageForm.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="packageForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPackage">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const packageList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const searchForm = reactive({
  name: '',
  type: ''
})

const packageForm = reactive({
  id: null,
  name: '',
  type: '婚纱',
  price: 0,
  description: '',
  photoCount: 0,
  retouchCount: 0,
  albumCount: 0,
  frameCount: 0,
  shootingDays: 1,
  status: 1,
  sortOrder: 0
})

const loadPackages = async () => {
  try {
    const res = await request.get('/packages')
    packageList.value = res.data || []
  } catch (error) {
    console.error('加载套餐失败', error)
  }
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.type = ''
  loadPackages()
}

const showCreateDialog = () => {
  isEdit.value = false
  packageForm.id = null
  packageForm.name = ''
  packageForm.type = '婚纱'
  packageForm.price = 0
  packageForm.description = ''
  packageForm.photoCount = 0
  packageForm.retouchCount = 0
  packageForm.albumCount = 0
  packageForm.frameCount = 0
  packageForm.shootingDays = 1
  packageForm.status = 1
  dialogVisible.value = true
}

const editPackage = (row) => {
  isEdit.value = true
  packageForm.id = row.id
  packageForm.name = row.name
  packageForm.type = row.type || '婚纱'
  packageForm.price = row.price || 0
  packageForm.description = row.description || ''
  packageForm.photoCount = row.photoCount || 0
  packageForm.retouchCount = row.retouchCount || 0
  packageForm.albumCount = row.albumCount || 0
  packageForm.frameCount = row.frameCount || 0
  packageForm.shootingDays = row.shootingDays || 1
  packageForm.status = row.status ?? 1
  dialogVisible.value = true
}

const submitPackage = async () => {
  try {
    if (isEdit.value) {
      await request.put('/packages', packageForm)
      ElMessage.success('套餐更新成功')
    } else {
      await request.post('/packages', packageForm)
      ElMessage.success('套餐创建成功')
    }
    dialogVisible.value = false
    loadPackages()
  } catch (error) {
    console.error('提交套餐失败', error)
  }
}

const deletePackage = (row) => {
  ElMessageBox.confirm('确定要删除该套餐吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await request.delete(`/packages/${row.id}`)
      ElMessage.success('删除成功')
      loadPackages()
    } catch (error) {
      console.error('删除套餐失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadPackages()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
</style>
