<template>
  <div class="costumes-page">
    <div class="page-header">
      <div class="search-area">
        <el-input
          v-model="searchName"
          placeholder="搜索服装名称"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="loadCostumes"
        />
        <el-select
          v-model="filterType"
          placeholder="筛选类型"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="婚纱" value="WEDDING_DRESS" />
          <el-option label="礼服" value="FORMAL" />
          <el-option label="主题服装" value="THEME" />
          <el-option label="配饰" value="ACCESSORY" />
        </el-select>
        <el-select
          v-model="filterStatus"
          placeholder="筛选状态"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="可借用" value="AVAILABLE" />
          <el-option label="清洗中" value="CLEANING" />
          <el-option label="已借出" value="BORROWED" />
        </el-select>
        <el-button type="primary" @click="loadCostumes">搜索</el-button>
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        添加服装
      </el-button>
    </div>

    <div class="costumes-grid">
      <el-card
        v-for="costume in costumes"
        :key="costume.id"
        class="costume-card"
        :class="{ borrowed: costume.status === 'BORROWED', cleaning: costume.cleaningStatus === 'IN_CLEANING' }"
      >
        <div class="costume-header">
          <div class="costume-name">{{ costume.name }}</div>
          <el-tag :type="getStatusType(costume.status)">
            {{ getStatusDesc(costume.status) }}
          </el-tag>
        </div>
        <div class="costume-info">
          <div class="costume-type">{{ getTypeDesc(costume.type) }}</div>
          <div class="costume-size">尺码: {{ costume.size }}</div>
          <div class="costume-color">颜色: {{ costume.color }}</div>
          <div class="costume-cleaning">
            <span>清洗状态:</span>
            <el-tag :type="getCleaningStatusType(costume.cleaningStatus)">
              {{ getCleaningStatusDesc(costume.cleaningStatus) }}
            </el-tag>
          </div>
          <div class="costume-usage">使用次数: {{ costume.usageCount }}次</div>
        </div>
        <div class="costume-description">{{ costume.description }}</div>
        <div class="costume-actions">
          <el-button size="mini" @click="handleEdit(costume)">编辑</el-button>
          <el-button
            v-if="costume.cleaningStatus === 'DIRTY'"
            size="mini"
            type="warning"
            @click="handleMarkCleaning(costume)"
          >送洗</el-button>
          <el-button
            v-if="costume.cleaningStatus === 'IN_CLEANING'"
            size="mini"
            type="success"
            @click="handleMarkCleaned(costume)"
          >已清洗</el-button>
        </div>
      </el-card>
    </div>

    <el-dialog
      :title="isEdit ? '编辑服装' : '添加服装'"
      :visible.sync="showAddModal"
      width="400px"
    >
      <el-form :model="formData" label-width="80px">
        <el-form-item label="服装名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入服装名称" />
        </el-form-item>
        <el-form-item label="服装类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择服装类型">
            <el-option label="婚纱" value="WEDDING_DRESS" />
            <el-option label="礼服" value="FORMAL" />
            <el-option label="主题服装" value="THEME" />
            <el-option label="配饰" value="ACCESSORY" />
          </el-select>
        </el-form-item>
        <el-form-item label="尺码" prop="size">
          <el-input v-model="formData.size" placeholder="请输入尺码" />
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-input v-model="formData.color" placeholder="请输入颜色" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-textarea v-model="formData.description" placeholder="请输入描述" rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { costumeApi } from '../api/costume'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const costumes = ref([])
const searchName = ref('')
const filterType = ref('')
const filterStatus = ref('')
const showAddModal = ref(false)
const isEdit = ref(false)

const formData = ref({
  id: null,
  name: '',
  type: 'WEDDING_DRESS',
  size: '',
  color: '',
  description: ''
})

const loadCostumes = async () => {
  try {
    let data
    if (searchName.value) {
      data = await costumeApi.search(searchName.value)
    } else if (filterStatus.value) {
      data = await costumeApi.getByCleaningStatus(filterStatus.value === 'AVAILABLE' ? 'CLEAN' : filterStatus.value === 'CLEANING' ? 'IN_CLEANING' : 'DIRTY')
    } else if (filterType.value) {
      data = await costumeApi.getByType(filterType.value)
    } else {
      data = await costumeApi.getAll()
    }
    costumes.value = data
  } catch (error) {
    ElMessage.error('加载服装列表失败')
  }
}

const getStatusType = (status) => {
  switch (status) {
    case 'AVAILABLE':
      return 'success'
    case 'BORROWED':
      return 'warning'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    AVAILABLE: '可借用',
    BORROWED: '已借出'
  }
  return statusMap[status] || status
}

const getTypeDesc = (type) => {
  const types = {
    WEDDING_DRESS: '婚纱',
    FORMAL: '礼服',
    THEME: '主题服装',
    ACCESSORY: '配饰'
  }
  return types[type] || type
}

const getCleaningStatusType = (status) => {
  switch (status) {
    case 'CLEAN':
      return 'success'
    case 'DIRTY':
      return 'warning'
    case 'IN_CLEANING':
      return 'info'
    default:
      return 'info'
  }
}

const getCleaningStatusDesc = (status) => {
  const statusMap = {
    CLEAN: '已清洗',
    DIRTY: '待清洗',
    IN_CLEANING: '清洗中'
  }
  return statusMap[status] || status
}

const handleEdit = (costume) => {
  isEdit.value = true
  formData.value = {
    id: costume.id,
    name: costume.name,
    type: costume.type,
    size: costume.size,
    color: costume.color,
    description: costume.description
  }
  showAddModal.value = true
}

const handleMarkCleaning = async (costume) => {
  try {
    await costumeApi.markCleaning(costume.id)
    ElMessage.success('已标记为清洗中')
    loadCostumes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleMarkCleaned = async (costume) => {
  try {
    await costumeApi.markCleaned(costume.id)
    ElMessage.success('已标记为已清洗')
    loadCostumes()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await costumeApi.update(formData.value.id, formData.value)
      ElMessage.success('修改成功')
    } else {
      await costumeApi.create(formData.value)
      ElMessage.success('添加成功')
    }
    showAddModal.value = false
    loadCostumes()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
  }
}

onMounted(() => {
  loadCostumes()
})
</script>

<style scoped>
.costumes-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-area {
  display: flex;
  gap: 10px;
  align-items: center;
}

.search-input {
  width: 200px;
}

.filter-select {
  width: 150px;
}

.costumes-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.costume-card {
  padding: 20px;
  transition: all 0.3s;
}

.costume-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.costume-card.borrowed {
  opacity: 0.7;
}

.costume-card.cleaning {
  border: 1px dashed #409eff;
}

.costume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.costume-name {
  font-size: 16px;
  font-weight: 600;
}

.costume-info {
  margin-bottom: 12px;
}

.costume-type,
.costume-size,
.costume-color,
.costume-usage {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.costume-cleaning {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.costume-description {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
  margin-bottom: 12px;
}

.costume-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>
