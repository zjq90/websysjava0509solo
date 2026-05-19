<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 分类筛选 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">药品分类</text>
      <view class="category-tabs">
        <text 
          class="tab-item" 
          :class="{ active: category === '' }"
          @click="category = ''"
        >全部</text>
        <text 
          class="tab-item" 
          :class="{ active: category === '抗生素' }"
          @click="category = '抗生素'"
        >抗生素</text>
        <text 
          class="tab-item" 
          :class="{ active: category === '抗病毒' }"
          @click="category = '抗病毒'"
        >抗病毒</text>
        <text 
          class="tab-item" 
          :class="{ active: category === '消化系统' }"
          @click="category = '消化系统'"
        >消化系统</text>
        <text 
          class="tab-item" 
          :class="{ active: category === '抗真菌药' }"
          @click="category = '抗真菌药'"
        >抗真菌</text>
        <text 
          class="tab-item" 
          :class="{ active: category === '驱虫药' }"
          @click="category = '驱虫药'"
        >驱虫药</text>
      </view>
    </view>

    <!-- 药品列表 -->
    <view class="card" v-for="item in medicineList" :key="item.id" @click="goToDetail(item.id)">
      <view class="flex-between">
        <text class="text-bold text-medium">{{ item.name }}</text>
        <text class="category-tag">{{ item.category }}</text>
      </view>
      <text class="text-small text-secondary mt-10">通用名：{{ item.genericName || item.name }}</text>
      <text class="text-small mt-10">{{ truncateText(item.indication, 60) }}</text>
    </view>

    <!-- 空状态 -->
    <view v-if="medicineList.length === 0" class="empty-state">
      <text class="empty-icon">💊</text>
      <text class="text-medium mt-20">暂无数据</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const category = ref('')
const medicineList = ref([])

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  fetchMedicineList()
})

watch(category, () => {
  fetchMedicineList()
})

const fetchMedicineList = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/medicine/list?category=${category.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      medicineList.value = res.data.data
    } else {
      useMockData()
    }
  } catch (e) {
    console.error('获取药品列表失败', e)
    useMockData()
  }
}

const useMockData = () => {
  const mockMedicines = [
    {
      id: 1,
      name: '猫用干扰素',
      genericName: '重组猫干扰素ω',
      category: '抗病毒',
      indication: '用于猫瘟热、猫鼻支、猫杯状病毒感染等病毒性疾病的治疗和预防。'
    },
    {
      id: 2,
      name: '犬细小单抗',
      genericName: '犬细小病毒单克隆抗体',
      category: '抗病毒',
      indication: '用于犬细小病毒病的特异性治疗，早期使用效果显著。'
    },
    {
      id: 3,
      name: '头孢噻呋钠',
      genericName: '注射用头孢噻呋钠',
      category: '抗生素',
      indication: '用于敏感菌引起的呼吸道、消化道、泌尿生殖道感染及皮肤软组织感染。'
    },
    {
      id: 4,
      name: '奥美拉唑',
      genericName: '奥美拉唑肠溶胶囊',
      category: '消化系统',
      indication: '用于胃炎、胃溃疡、十二指肠溃疡、反流性食管炎、胃酸过多等。'
    },
    {
      id: 5,
      name: '伊曲康唑',
      genericName: '伊曲康唑胶囊',
      category: '抗真菌药',
      indication: '用于皮肤真菌病、猫癣、犬小孢子菌感染、深部真菌感染等。'
    },
    {
      id: 6,
      name: '体内外驱虫药',
      genericName: '吡喹酮+非泼罗尼',
      category: '驱虫药',
      indication: '用于驱除犬猫体内蛔虫、钩虫、绦虫及体外跳蚤、蜱虫等寄生虫。'
    }
  ]
  
  medicineList.value = category.value 
    ? mockMedicines.filter(m => m.category === category.value)
    : mockMedicines
}

const truncateText = (text, maxLength) => {
  if (!text || text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

const goToDetail = (id) => {
  uni.navigateTo({
    url: `/pages/knowledge/medicine-detail?id=${id}`
  })
}
</script>

<style scoped>
.category-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.tab-item {
  padding: 12rpx 24rpx;
  background-color: #F5F7FA;
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #606266;
  transition: all 0.3s;
}

.tab-item.active {
  background-color: #409EFF;
  color: white;
}

.category-tag {
  padding: 6rpx 16rpx;
  background-color: #ECF5FF;
  color: #409EFF;
  border-radius: 4rpx;
  font-size: 22rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 40rpx;
}

.empty-icon {
  font-size: 100rpx;
  opacity: 0.5;
}
</style>
