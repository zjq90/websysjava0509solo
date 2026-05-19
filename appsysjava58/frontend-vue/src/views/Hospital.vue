<template>
  <div class="hospital-page">
    <div class="page-header">
      <el-button @click="$router.push('/home')" type="text">← 返回</el-button>
      <h2>附近医院</h2>
    </div>

    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索医院名称" 
        clearable
        @input="searchHospitals"
      >
        <template #prefix>🔍</template>
      </el-input>
    </div>

    <div class="filter-tabs">
      <div 
        class="tab" 
        :class="{ active: filterType === type.value }" 
        v-for="type in filterTypes" 
        :key="type.value"
        @click="filterType = type.value"
      >
        {{ type.label }}
      </div>
    </div>

    <div class="hospital-list">
      <div 
        class="hospital-card" 
        v-for="hospital in filteredHospitals" 
        :key="hospital.id"
        @click="viewDetail(hospital)"
      >
        <div class="hospital-badge emergency" v-if="hospital.emergency">
          🚨 24h急诊
        </div>
        
        <div class="hospital-header">
          <h3>{{ hospital.name }}</h3>
          <div class="rating">
            <span class="stars">
              <span v-for="i in 5" :key="i">{{ i <= hospital.rating ? '⭐' : '☆' }}</span>
            </span>
            <span class="score">{{ hospital.rating.toFixed(1) }}</span>
          </div>
        </div>
        
        <div class="hospital-info">
          <div class="info-item">
            <span>📍</span>
            <span>{{ hospital.address }}</span>
          </div>
          <div class="info-item">
            <span>📞</span>
            <span>{{ hospital.phone }}</span>
          </div>
          <div class="info-item" v-if="hospital.distance">
            <span>🚶</span>
            <span>距离 {{ hospital.distance }} km</span>
          </div>
        </div>
        
        <div class="hospital-tags">
          <el-tag size="small" v-for="tag in hospital.tags" :key="tag">{{ tag }}</el-tag>
        </div>
        
        <div class="hospital-actions">
          <el-button type="primary" size="small" @click.stop="callHospital(hospital.phone)">
            📞 电话咨询
          </el-button>
          <el-button size="small" @click.stop="navigateTo(hospital)">
            📍 导航
          </el-button>
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="filteredHospitals.length === 0">
      <span class="empty-icon">🏥</span>
      <span class="empty-text">暂无附近医院信息</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { hospitalApi } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const searchKeyword = ref('')
const filterType = ref('all')
const hospitals = ref([])

const filterTypes = [
  { label: '全部', value: 'all' },
  { label: '24h急诊', value: 'emergency' },
  { label: '评分最高', value: 'rating' },
  { label: '距离最近', value: 'distance' }
]

const filteredHospitals = computed(() => {
  let list = [...hospitals.value]
  
  if (searchKeyword.value) {
    list = list.filter(h => h.name.includes(searchKeyword.value))
  }
  
  if (filterType.value === 'emergency') {
    list = list.filter(h => h.emergency)
  } else if (filterType.value === 'rating') {
    list.sort((a, b) => b.rating - a.rating)
  } else if (filterType.value === 'distance') {
    list.sort((a, b) => (a.distance || 999) - (b.distance || 999))
  }
  
  return list
})

const loadHospitals = async () => {
  try {
    const res = await hospitalApi.getNearby({})
    if (res.code === 200) {
      hospitals.value = res.data
    }
  } catch (error) {
    hospitals.value = [
      {
        id: 1,
        name: '爱宠动物医院',
        address: '北京市朝阳区建国路88号',
        phone: '010-12345678',
        rating: 4.8,
        distance: 1.2,
        emergency: true,
        tags: ['疫苗接种', '宠物美容', '24h急诊', '骨科'],
        ratingCount: 256
      },
      {
        id: 2,
        name: '康贝宠物诊所',
        address: '北京市海淀区中关村大街1号',
        phone: '010-87654321',
        rating: 4.5,
        distance: 2.5,
        emergency: false,
        tags: ['疫苗接种', '内科', '皮肤科'],
        ratingCount: 128
      },
      {
        id: 3,
        name: '仁爱宠物医院',
        address: '北京市西城区金融街15号',
        phone: '010-11112222',
        rating: 4.9,
        distance: 3.8,
        emergency: true,
        tags: ['24h急诊', '手术', '住院', '体检'],
        ratingCount: 512
      }
    ]
  }
}

const searchHospitals = () => {
  // 搜索已在computed中实现
}

const viewDetail = (hospital) => {
  router.push(`/hospital/${hospital.id}`)
}

const callHospital = (phone) => {
  ElMessage.success(`正在拨打: ${phone}`)
  // 实际项目中可以调用拨打电话功能
}

const navigateTo = (hospital) => {
  ElMessage.success(`正在导航到: ${hospital.name}`)
  // 实际项目中可以调用地图导航功能
}

onMounted(() => {
  loadHospitals()
})
</script>

<style scoped>
.hospital-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 20px 80px;
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

.search-bar {
  margin-bottom: 20px;
}

.filter-tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  overflow-x: auto;
}

.tab {
  padding: 8px 20px;
  background: white;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  white-space: nowrap;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  transition: all 0.3s;
}

.tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.hospital-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.hospital-card {
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
  position: relative;
}

.hospital-card:hover {
  transform: translateY(-2px);
}

.hospital-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 5px 12px;
  border-radius: 12px;
  font-size: 12px;
}

.hospital-badge.emergency {
  background: #fff1f0;
  color: #f5222d;
}

.hospital-header {
  margin-bottom: 15px;
  padding-right: 80px;
}

.hospital-header h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #333;
}

.rating {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stars {
  font-size: 14px;
}

.score {
  font-size: 16px;
  font-weight: bold;
  color: #faad14;
}

.hospital-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.hospital-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 15px;
}

.hospital-actions {
  display: flex;
  gap: 10px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
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