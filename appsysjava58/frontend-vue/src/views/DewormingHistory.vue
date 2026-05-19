<template>
  <div class="deworming-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>驱虫记录</h2>
      <el-button type="primary" @click="showAddModal = true">+ 添加</el-button>
    </div>

    <div class="pet-tabs">
      <div 
        class="tab-item" 
        :class="{ active: selectedPet === pet.id }" 
        v-for="pet in pets" 
        :key="pet.id"
        @click="selectPet(pet.id)"
      >
        {{ pet.name }}
      </div>
    </div>

    <div class="history-list" v-if="records.length > 0">
      <div class="history-item" v-for="item in records" :key="item.id">
        <div class="item-header">
          <div class="date-badge">{{ formatDate(item.dewormingDate) }}</div>
          <div class="status-badge" :class="item.completed ? 'completed' : 'pending'">
            {{ item.completed ? '已完成' : '待完成' }}
          </div>
        </div>
        <div class="item-body">
          <div class="info-row">
            <span class="label">药品名称:</span>
            <span class="value">{{ item.medicineName }}</span>
          </div>
          <div class="info-row" v-if="item.dosage">
            <span class="label">用量:</span>
            <span class="value">{{ item.dosage }}</span>
          </div>
          <div class="info-row" v-if="item.nextDewormingDate">
            <span class="label">下次驱虫:</span>
            <span class="value highlight">{{ formatDate(item.nextDewormingDate) }}</span>
          </div>
          <div class="info-row" v-if="item.notes">
            <span class="label">备注:</span>
            <span class="value">{{ item.notes }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="empty-state" v-else>
      <span class="empty-icon">📭</span>
      <span class="empty-text">暂无驱虫记录</span>
    </div>

    <el-dialog v-model="showAddModal" title="添加驱虫记录" width="450px">
      <el-form :model="newRecord" label-width="100px">
        <el-form-item label="驱虫日期">
          <el-date-picker 
            v-model="newRecord.dewormingDate" 
            type="date" 
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="药品名称">
          <el-input v-model="newRecord.medicineName" placeholder="如：福来恩滴剂" />
        </el-form-item>
        <el-form-item label="用量">
          <el-input v-model="newRecord.dosage" placeholder="如：1支" />
        </el-form-item>
        <el-form-item label="下次驱虫">
          <el-date-picker 
            v-model="newRecord.nextDewormingDate" 
            type="date" 
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="newRecord.notes" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddModal = false">取消</el-button>
        <el-button type="primary" @click="addRecord">确认添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { healthApi, petApi } from '@/api'
import { ElMessage } from 'element-plus'

const showAddModal = ref(false)
const selectedPet = ref(1)
const pets = ref([])
const records = ref([])

const newRecord = ref({
  dewormingDate: '',
  medicineName: '',
  dosage: '',
  nextDewormingDate: '',
  notes: ''
})

const loadPets = async () => {
  try {
    const res = await petApi.getList(1)
    if (res.code === 200) {
      pets.value = res.data
    }
  } catch (error) {
    pets.value = [
      { id: 1, name: '旺财', type: 'dog', breed: '金毛' },
      { id: 2, name: '咪咪', type: 'cat', breed: '英短' }
    ]
  }
}

const loadRecords = async () => {
  try {
    const res = await healthApi.getDewormingHistory(selectedPet.value)
    if (res.code === 200) {
      records.value = res.data
    }
  } catch (error) {
    records.value = [
      {
        id: 1,
        dewormingDate: '2024-01-15',
        medicineName: '福来恩滴剂',
        dosage: '1支',
        nextDewormingDate: '2024-02-15',
        notes: '体外驱虫，无不良反应',
        completed: true
      },
      {
        id: 2,
        dewormingDate: '2023-10-15',
        medicineName: '拜宠清',
        dosage: '1片',
        notes: '上次体内驱虫记录',
        completed: true
      }
    ]
  }
}

const selectPet = (petId) => {
  selectedPet.value = petId
  loadRecords()
}

const formatDate = (date) => {
  return date
}

const addRecord = async () => {
  if (!newRecord.value.medicineName) {
    ElMessage.warning('请填写药品名称')
    return
  }
  try {
    const res = await healthApi.addDewormingRecord({
      ...newRecord.value,
      pet: { id: selectedPet.value },
      completed: true
    })
    if (res.code === 200) {
      ElMessage.success('添加成功')
      showAddModal.value = false
      newRecord.value = { dewormingDate: '', medicineName: '', dosage: '', nextDewormingDate: '', notes: '' }
      loadRecords()
    }
  } catch (error) {
    records.value.unshift({
      id: Date.now(),
      ...newRecord.value,
      completed: true
    })
    ElMessage.success('添加成功')
    showAddModal.value = false
    newRecord.value = { dewormingDate: '', medicineName: '', dosage: '', nextDewormingDate: '', notes: '' }
  }
}

onMounted(() => {
  loadPets()
  loadRecords()
})
</script>

<style scoped>
.deworming-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 20px;
}

.pet-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 12px;
  background: white;
  border-radius: 12px;
  font-size: 15px;
  color: #666;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.3s;
}

.tab-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-item {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.date-badge {
  background: #e6f7ff;
  color: #1890ff;
  padding: 5px 15px;
  border-radius: 15px;
  font-size: 14px;
}

.status-badge {
  padding: 5px 15px;
  border-radius: 15px;
  font-size: 14px;
}

.status-badge.completed {
  background: #f6ffed;
  color: #52c41a;
}

.status-badge.pending {
  background: #fffbe6;
  color: #faad14;
}

.item-body {
  padding: 20px;
}

.info-row {
  display: flex;
  margin-bottom: 12px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  width: 100px;
  font-size: 14px;
  color: #999;
}

.value {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.value.highlight {
  color: #667eea;
  font-weight: bold;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80px 0;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 20px;
}

.empty-text {
  font-size: 16px;
  color: #999;
}
</style>