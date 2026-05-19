<template>
  <div class="home-page">
    <div class="header">
      <h1>🐾 宠物健康管理</h1>
      <p>守护毛孩子的每一天</p>
    </div>

    <div class="pet-selector">
      <h3>我的宠物</h3>
      <div class="pet-list">
        <div 
          class="pet-card" 
          v-for="pet in pets" 
          :key="pet.id"
          :class="{ active: currentPet?.id === pet.id }"
          @click="selectPet(pet)"
        >
          <div class="pet-avatar">{{ pet.type === 'dog' ? '🐕' : '🐱' }}</div>
          <div class="pet-info">
            <div class="pet-name">{{ pet.name }}</div>
            <div class="pet-breed">{{ pet.breed }}</div>
          </div>
          <div class="pet-stats" v-if="pet.weight">
            <span>体重: {{ pet.weight }}kg</span>
          </div>
        </div>
      </div>
      <el-button type="primary" class="add-pet-btn" @click="showAddPet = true" size="small">
        + 添加宠物
      </el-button>
    </div>

    <div class="health-section" v-if="currentPet">
      <h3>健康提醒</h3>
      <div class="health-cards">
        <div class="health-card deworming" @click="$router.push('/health/deworming')">
          <div class="card-icon">💊</div>
          <div class="card-content">
            <div class="card-title">驱虫提醒</div>
            <div class="card-status" :class="healthData.deworming.status">
              {{ healthData.deworming.days > 0 ? `还有 ${healthData.deworming.days} 天` : '已到期' }}
            </div>
          </div>
          <div class="check-mark" v-if="healthData.deworming.status === 'completed'">✅</div>
        </div>

        <div class="health-card checkup" @click="$router.push('/health')">
          <div class="card-icon">🏥</div>
          <div class="card-content">
            <div class="card-title">体检提醒</div>
            <div class="card-status">下次体检: {{ healthData.checkup.days }}天后</div>
          </div>
        </div>
      </div>
    </div>

    <div class="quick-actions">
      <h3>快捷功能</h3>
      <div class="action-grid">
        <div class="action-item" @click="$router.push('/diet')">
          <div class="action-icon">🍽️</div>
          <div class="action-text">饮食建议</div>
        </div>
        <div class="action-item" @click="$router.push('/exercise')">
          <div class="action-icon">🏃</div>
          <div class="action-text">运动监测</div>
        </div>
        <div class="action-item" @click="$router.push('/circle')">
          <div class="action-icon">📱</div>
          <div class="action-text">宠物圈</div>
        </div>
        <div class="action-item" @click="$router.push('/hospital')">
          <div class="action-icon">🏥</div>
          <div class="action-text">附近医院</div>
        </div>
      </div>
    </div>

    <nav class="bottom-nav">
      <div class="nav-item active" @click="$router.push('/home')">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </div>
      <div class="nav-item" @click="$router.push('/circle')">
        <span class="nav-icon">📱</span>
        <span class="nav-text">宠物圈</span>
      </div>
      <div class="nav-item" @click="$router.push('/hospital')">
        <span class="nav-icon">🏥</span>
        <span class="nav-text">医院</span>
      </div>
      <div class="nav-item" @click="$router.push('/mine')">
        <span class="nav-icon">👤</span>
        <span class="nav-text">我的</span>
      </div>
    </nav>

    <el-dialog v-model="showAddPet" title="添加宠物" width="400px">
      <el-form :model="newPet" label-width="80px">
        <el-form-item label="宠物名称">
          <el-input v-model="newPet.name" placeholder="请输入宠物名称" />
        </el-form-item>
        <el-form-item label="宠物类型">
          <el-select v-model="newPet.type" placeholder="请选择类型">
            <el-option label="狗狗" value="dog" />
            <el-option label="猫咪" value="cat" />
          </el-select>
        </el-form-item>
        <el-form-item label="品种">
          <el-input v-model="newPet.breed" placeholder="如：金毛、英短" />
        </el-form-item>
        <el-form-item label="体重(kg)">
          <el-input-number v-model="newPet.weight" :min="0" :precision="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddPet = false">取消</el-button>
        <el-button type="primary" @click="addPet">确认添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAppStore } from '@/store/app'
import { petApi, healthApi } from '@/api'
import { ElMessage } from 'element-plus'

const appStore = useAppStore()

const pets = ref([])
const currentPet = ref(null)
const showAddPet = ref(false)
const newPet = ref({
  name: '',
  type: 'dog',
  breed: '',
  weight: null
})

const healthData = ref({
  deworming: { status: 'upcoming', days: 20, lastRecord: null },
  checkup: { days: 45 }
})

const loadPets = async () => {
  try {
    const res = await petApi.getList(1)
    if (res.code === 200) {
      pets.value = res.data
      if (res.data.length > 0) {
        currentPet.value = res.data[0]
        loadHealthData()
      }
    }
  } catch (error) {
    console.log('使用默认宠物数据')
    pets.value = [
      { id: 1, name: '旺财', type: 'dog', breed: '金毛', weight: 25.5 },
      { id: 2, name: '咪咪', type: 'cat', breed: '英短', weight: 4.2 }
    ]
    currentPet.value = pets.value[0]
  }
}

const loadHealthData = async () => {
  if (!currentPet.value) return
  try {
    const res = await healthApi.getHealthCards(currentPet.value.id)
    if (res.code === 200) {
      healthData.value = res.data
    }
  } catch (error) {
    console.log('使用默认健康数据')
  }
}

const selectPet = (pet) => {
  currentPet.value = pet
  loadHealthData()
}

const addPet = async () => {
  if (!newPet.value.name) {
    ElMessage.warning('请输入宠物名称')
    return
  }
  try {
    const res = await petApi.add({ ...newPet.value, user: { id: 1 } })
    if (res.code === 200) {
      ElMessage.success('添加成功')
      showAddPet.value = false
      newPet.value = { name: '', type: 'dog', breed: '', weight: null }
      loadPets()
    }
  } catch (error) {
    pets.value.push({
      id: Date.now(),
      ...newPet.value
    })
    ElMessage.success('添加成功')
    showAddPet.value = false
    newPet.value = { name: '', type: 'dog', breed: '', weight: null }
  }
}

onMounted(() => {
  appStore.loadSettings()
  loadPets()
})
</script>

<style scoped>
.home-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 20px 100px;
}

.header {
  text-align: center;
  padding: 30px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 20px;
  margin-bottom: 30px;
}

.header h1 {
  margin: 0 0 10px;
  font-size: 28px;
}

.header p {
  margin: 0;
  opacity: 0.9;
}

.pet-selector,
.health-section,
.quick-actions {
  margin-bottom: 30px;
}

.pet-selector h3,
.health-section h3,
.quick-actions h3 {
  margin: 0 0 20px;
  color: #333;
  font-size: 20px;
}

.pet-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 15px;
}

.pet-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.pet-card.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
}

.pet-avatar {
  font-size: 40px;
  margin-right: 15px;
}

.pet-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.pet-breed {
  font-size: 14px;
  color: #999;
}

.pet-stats {
  margin-left: auto;
  font-size: 13px;
  color: #666;
}

.add-pet-btn {
  width: 100%;
}

.health-cards {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.health-card {
  display: flex;
  align-items: center;
  padding: 25px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.health-card:hover {
  transform: translateY(-2px);
}

.card-icon {
  font-size: 40px;
  margin-right: 20px;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.card-status {
  font-size: 14px;
  color: #faad14;
}

.card-status.completed {
  color: #52c41a;
}

.card-status.overdue {
  color: #f5222d;
}

.check-mark {
  font-size: 30px;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25px 15px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.action-item:hover {
  transform: translateY(-2px);
}

.action-icon {
  font-size: 36px;
  margin-bottom: 10px;
}

.action-text {
  font-size: 14px;
  color: #666;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 640px;
  display: flex;
  background: white;
  padding: 10px 0;
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  border-radius: 20px 20px 0 0;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  color: #999;
  transition: color 0.3s;
}

.nav-item.active {
  color: #667eea;
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.nav-text {
  font-size: 12px;
}
</style>