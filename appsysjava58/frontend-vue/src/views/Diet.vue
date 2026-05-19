<template>
  <div class="diet-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>饮食建议</h2>
    </div>

    <div class="pet-select">
      <span class="label">选择宠物:</span>
      <el-select v-model="selectedPet" @change="loadDiet" style="width: 150px">
        <el-option 
          v-for="pet in pets" 
          :key="pet.id" 
          :label="pet.name" 
          :value="pet.id"
        />
      </el-select>
      <el-button type="primary" @click="generateDiet" size="small" :loading="generating">
        生成建议
      </el-button>
    </div>

    <div class="diet-card" v-if="currentSuggestion">
      <div class="card-header">
      🍽️ 个性化饮食建议
      </div>
      <div class="card-body">
        <div class="pet-info">
          <strong>宠物: {{ currentPetName }}</strong>
          <span>体重: {{ currentPetWeight }} kg</span>
        </div>
        
        <div class="recommendation-section">
          <h4>📋 推荐食谱</h4>
          <p>{{ currentSuggestion.recommendation }}</p>
        </div>
        
        <div class="nutrients-section">
          <h4>🥗 每日营养配比</h4>
          <div class="nutrient-bars">
            <div class="nutrient-item">
              <span class="name">蛋白质</span>
              <div class="bar-container">
                <div class="bar protein" :style="{ width: currentSuggestion.protein + '%' }"></div>
              </div>
              <span class="value">{{ currentSuggestion.protein }}%</span>
            </div>
            <div class="nutrient-item">
              <span class="name">脂肪</span>
              <div class="bar-container">
                <div class="bar fat" :style="{ width: currentSuggestion.fat + '%' }"></div>
              </div>
              <span class="value">{{ currentSuggestion.fat }}%</span>
            </div>
            <div class="nutrient-item">
              <span class="name">碳水</span>
              <div class="bar-container">
                <div class="bar carb" :style="{ width: currentSuggestion.carb + '%' }"></div>
              </div>
              <span class="value">{{ currentSuggestion.carb }}%</span>
            </div>
          </div>
        </div>
        
        <div class="feeding-guide">
          <h4>⏰ 喂食指导</h4>
          <p>{{ currentSuggestion.feedingGuide }}</p>
        </div>
        
        <div class="reference-section" v-if="currentSuggestion.references">
          <h4>📚 权威文献参考</h4>
          <ul>
            <li v-for="(ref, index) in currentSuggestion.references" :key="index">
              {{ ref }}
            </li>
          </ul>
        </div>
        
        <div class="note-section" v-if="currentSuggestion.notes">
          <h4>⚠️ 注意事项</h4>
          <p>{{ currentSuggestion.notes }}</p>
        </div>
      </div>
    </div>

    <div class="history-section">
      <h3>历史建议</h3>
      <div class="history-list">
        <div 
        class="history-item" 
        v-for="item in history" 
        :key="item.id"
        @click="viewHistory(item)"
      >
        <div class="history-date">{{ formatDate(item.generatedDate) }}</div>
        <div class="history-preview">{{ item.recommendation?.substring(0, 50) }}...</div>
      </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { dietApi, petApi } from '@/api'
import { ElMessage } from 'element-plus'

const selectedPet = ref(1)
const pets = ref([])
const generating = ref(false)
const currentSuggestion = ref(null)
const history = ref([])

const currentPetName = computed(() => {
  const pet = pets.value.find(p => p.id === selectedPet.value)
  return pet?.name || ''
})

const currentPetWeight = computed(() => {
  const pet = pets.value.find(p => p.id === selectedPet.value)
  return pet?.weight || 0
})

const loadPets = async () => {
  try {
    const res = await petApi.getList(1)
    if (res.code === 200) {
      pets.value = res.data
    }
  } catch (error) {
    pets.value = [
      { id: 1, name: '旺财', type: 'dog', breed: '金毛', weight: 25.5 },
      { id: 2, name: '咪咪', type: 'cat', breed: '英短', weight: 4.2 }
    ]
  }
}

const generateDiet = async () => {
  generating.value = true
  try {
    const res = await dietApi.generateSuggestion(selectedPet.value)
    if (res.code === 200) {
      currentSuggestion.value = res.data
      ElMessage.success('饮食建议已生成')
    }
  } catch (error) {
    const pet = pets.value.find(p => p.id === selectedPet.value)
    const isDog = pet?.type === 'dog'
    currentSuggestion.value = {
      recommendation: isDog
        ? '推荐每日喂食2次，每次200-250g高品质狗粮，搭配适量的鸡肉、胡萝卜等新鲜蔬菜。'
        : '推荐每日喂食3-4次，每次50-60g高品质猫粮，搭配适量的鱼肉、鸡胸肉。',
      protein: isDog ? 25 : 30,
      fat: isDog ? 15 : 20,
      carb: isDog ? 50 : 40,
      feedingGuide: isDog
        ? '早上7:00-8:00，晚上18:00-19:00喂食，每日更换饮用水24小时供应。'
        : '早中晚分3次喂食，夜间可适当留一些猫粮供应，饮用水24小时供应。',
      references: [
        '《犬猫营养需要（NRC，2006）',
        '《宠物营养学》（AAFCO官方标准）',
        '《小动物临床营养学第5版'
      ],
      notes: isDog
        ? '建议选择无谷蛋白来源为主，避免喂食洋葱、巧克力、葡萄等有毒食物。'
        : '猫咪需要更多的牛磺酸，避免喂食洋葱、巧克力、葡萄等有毒食物。'
    }
    ElMessage.success('饮食建议已生成')
  } finally {
    generating.value = false
  }
}

const loadDiet = async () => {
  try {
    const res = await dietApi.getHistory(selectedPet.value)
    if (res.code === 200) {
      history.value = res.data
      if (res.data.length > 0) {
        currentSuggestion.value = res.data[0]
      }
    }
  } catch (error) {
    history.value = []
  }
}

const viewHistory = (item) => {
  currentSuggestion.value = item
}

const formatDate = (date) => {
  return date
}

onMounted(() => {
  loadPets()
  loadDiet()
  if (!currentSuggestion.value) {
    generateDiet()
  }
})
</script>

<style scoped>
.diet-page {
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

.pet-select {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.pet-select .label {
  font-size: 15px;
  color: #333;
}

.diet-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-bottom: 20px 0;
}

.card-header {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.card-body {
  padding: 25px;
}

.pet-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #fafafa;
  border-radius: 12px;
  margin-bottom: 20px;
}

.recommendation-section,
.nutrients-section,
.feeding-guide,
.reference-section,
.note-section {
  margin-bottom: 25px;
}

.recommendation-section h4,
.nutrients-section h4,
.feeding-guide h4,
.reference-section h4,
.note-section h4 {
  margin: 0 0 12px;
  font-size: 16px;
  color: #333;
}

.recommendation-section p,
.feeding-guide p,
.note-section p {
  margin: 0;
  line-height: 1.8;
  color: #666;
}

.nutrient-bars {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.nutrient-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.nutrient-item .name {
  width: 60px;
  font-size: 14px;
  color: #666;
}

.bar-container {
  flex: 1;
  height: 20px;
  background: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
}

.bar {
  height: 100%;
  border-radius: 10px;
  transition: width 0.5s;
}

.bar.protein {
  background: linear-gradient(90deg, #52c41a, #73d13d);
}

.bar.fat {
  background: linear-gradient(90deg, #faad14, #ffc53d);
}

.bar.carb {
  background: linear-gradient(90deg, #1890ff, #40a9ff);
}

.nutrient-item .value {
  width: 50px;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.reference-section ul {
  margin: 0;
  padding-left: 20px;
}

.reference-section li {
  margin-bottom: 8px;
  font-size: 13px;
  color: #999;
}

.history-section {
  margin-top: 30px;
}

.history-section h3 {
  margin: 0 0 20px;
  font-size: 18px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  padding: 15px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: transform 0.3s;
}

.history-item:hover {
  transform: translateY(-2px);
}

.history-date {
  font-size: 14px;
  color: #667eea;
  font-weight: bold;
}

.history-preview {
  font-size: 14px;
  color: #999;
}
</style>