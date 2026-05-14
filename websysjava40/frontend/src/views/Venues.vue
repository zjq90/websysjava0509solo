<template>
  <div class="venues-page">
    <div class="page-header">
      <div class="search-area">
        <el-input
          v-model="searchName"
          placeholder="搜索场地名称"
          prefix-icon="Search"
          class="search-input"
          @keyup.enter="loadVenues"
        />
        <el-select
          v-model="filterType"
          placeholder="筛选类型"
          class="filter-select"
        >
          <el-option label="全部" value="" />
          <el-option label="影棚" value="STUDIO" />
          <el-option label="外景地" value="OUTDOOR" />
        </el-select>
        <el-button type="primary" @click="loadVenues">搜索</el-button>
      </div>
      <el-button type="success" @click="showAddModal = true">
        <el-icon><component :is="icons.Plus" /></el-icon>
        添加场地
      </el-button>
    </div>

    <div class="venues-grid">
      <el-card
        v-for="venue in venues"
        :key="venue.id"
        class="venue-card"
        :class="{ maintenance: venue.status === 'MAINTENANCE' }"
      >
        <div class="venue-header">
          <div class="venue-name">{{ venue.name }}</div>
          <el-tag :type="getStatusType(venue.status)">
            {{ getStatusDesc(venue.status) }}
          </el-tag>
        </div>
        <div class="venue-info">
          <div class="venue-type">{{ getTypeDesc(venue.type) }}</div>
          <div class="venue-capacity">容量: {{ venue.capacity }}人</div>
          <div class="venue-description">{{ venue.description }}</div>
        </div>
        <div class="venue-actions">
          <el-button size="mini" @click="handleEdit(venue)">编辑</el-button>
          <el-button
            size="mini"
            :type="venue.status === 'MAINTENANCE' ? 'success' : 'warning'"
            @click="handleMaintenance(venue)"
          >
            {{ venue.status === 'MAINTENANCE' ? '恢复使用' : '维护中' }}
          </el-button>
        </div>
      </el-card>
    </div>

    <el-dialog
      :title="isEdit ? '编辑场地' : '添加场地'"
      :visible.sync="showAddModal"
      width="400px"
    >
      <el-form :model="formData" label-width="80px">
        <el-form-item label="场地名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入场地名称" />
        </el-form-item>
        <el-form-item label="场地类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择场地类型">
            <el-option label="影棚" value="STUDIO" />
            <el-option label="外景地" value="OUTDOOR" />
          </el-select>
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input v-model="formData.capacity" type="number" placeholder="请输入容量" />
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
import { venueApi } from '../api/venue'
import { ElMessage } from 'element-plus'

const icons = { Plus }

const venues = ref([])
const searchName = ref('')
const filterType = ref('')
const showAddModal = ref(false)
const isEdit = ref(false)

const formData = ref({
  id: null,
  name: '',
  type: 'STUDIO',
  capacity: 10,
  description: ''
})

const loadVenues = async () => {
  try {
    let data
    if (searchName.value) {
      data = await venueApi.search(searchName.value)
    } else if (filterType.value) {
      data = await venueApi.getByType(filterType.value)
    } else {
      data = await venueApi.getAll()
    }
    venues.value = data
  } catch (error) {
    ElMessage.error('加载场地列表失败')
  }
}

const getStatusType = (status) => {
  switch (status) {
    case 'AVAILABLE':
      return 'success'
    case 'OCCUPIED':
      return 'warning'
    case 'MAINTENANCE':
      return 'danger'
    default:
      return 'info'
  }
}

const getStatusDesc = (status) => {
  const statusMap = {
    AVAILABLE: '可预约',
    OCCUPIED: '使用中',
    MAINTENANCE: '维护中'
  }
  return statusMap[status] || status
}

const getTypeDesc = (type) => {
  return type === 'STUDIO' ? '影棚' : '外景地'
}

const handleEdit = (venue) => {
  isEdit.value = true
  formData.value = {
    id: venue.id,
    name: venue.name,
    type: venue.type,
    capacity: venue.capacity,
    description: venue.description
  }
  showAddModal.value = true
}

const handleMaintenance = async (venue) => {
  try {
    const isMaintenance = venue.status === 'MAINTENANCE'
    await venueApi.setMaintenance(venue.id, !isMaintenance)
    ElMessage.success(isMaintenance ? '已恢复使用' : '已设为维护中')
    loadVenues()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await venueApi.update(formData.value.id, formData.value)
      ElMessage.success('修改成功')
    } else {
      await venueApi.create(formData.value)
      ElMessage.success('添加成功')
    }
    showAddModal.value = false
    loadVenues()
  } catch (error) {
    ElMessage.error(isEdit.value ? '修改失败' : '添加失败')
  }
}

onMounted(() => {
  loadVenues()
})
</script>

<style scoped>
.venues-page {
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

.venues-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.venue-card {
  padding: 20px;
  transition: all 0.3s;
}

.venue-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.venue-card.maintenance {
  opacity: 0.6;
}

.venue-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.venue-name {
  font-size: 18px;
  font-weight: 600;
}

.venue-info {
  margin-bottom: 15px;
}

.venue-type {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.venue-capacity {
  font-size: 13px;
  color: #999;
  margin-bottom: 5px;
}

.venue-description {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
}

.venue-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>
