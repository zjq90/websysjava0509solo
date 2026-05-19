<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 宠物类型筛选 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">宠物类型</text>
      <view class="filter-tabs">
        <text 
          class="filter-tab" 
          :class="{ active: petType === '' }"
          @click="petType = ''"
        >全部</text>
        <text 
          class="filter-tab" 
          :class="{ active: petType === '猫' }"
          @click="petType = '猫'"
        >猫</text>
        <text 
          class="filter-tab" 
          :class="{ active: petType === '狗' }"
          @click="petType = '狗'"
        >狗</text>
      </view>
    </view>

    <!-- 疾病列表 -->
    <view class="card" v-for="item in diseaseList" :key="item.id" @click="goToDetail(item.id)">
      <view class="flex-between">
        <text class="text-bold text-medium">{{ item.name }}</text>
        <view class="severity-tag" :class="'level-' + item.severity">
          {{ getSeverityLabel(item.severity) }}
        </view>
      </view>
      <text class="text-small text-secondary mt-10">宠物类型：{{ item.petType }}</text>
      <text class="text-small mt-10">{{ truncateText(item.symptoms, 80) }}</text>
    </view>

    <!-- 空状态 -->
    <view v-if="diseaseList.length === 0" class="empty-state">
      <text class="empty-icon">📋</text>
      <text class="text-medium mt-20">暂无数据</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const petType = ref('')
const diseaseList = ref([])

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  fetchDiseaseList()
})

const fetchDiseaseList = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/disease/list?petType=${petType.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      diseaseList.value = res.data.data
    } else {
      useMockData()
    }
  } catch (e) {
    console.error('获取疾病列表失败', e)
    useMockData()
  }
}

const useMockData = () => {
  const mockDiseases = [
    {
      id: 1,
      name: '猫瘟热',
      petType: '猫',
      symptoms: '发热、呕吐、腹泻、精神萎靡、白细胞减少、食欲废绝、脱水',
      severity: 3
    },
    {
      id: 2,
      name: '猫鼻支',
      petType: '猫',
      symptoms: '打喷嚏、流鼻涕、流眼泪、结膜炎、发热、精神不振、食欲下降',
      severity: 2
    },
    {
      id: 3,
      name: '猫传腹',
      petType: '猫',
      symptoms: '腹水、黄疸、发热、体重下降、精神萎靡、食欲废绝、呼吸困难',
      severity: 3
    },
    {
      id: 4,
      name: '犬细小病毒病',
      petType: '狗',
      symptoms: '剧烈呕吐、血便、发热、脱水、精神沉郁、食欲废绝、白细胞减少',
      severity: 3
    },
    {
      id: 5,
      name: '犬瘟热',
      petType: '狗',
      symptoms: '双相热、呼吸道症状、消化道症状、神经症状、脚垫增厚',
      severity: 3
    },
    {
      id: 6,
      name: '皮肤真菌病',
      petType: '通用',
      symptoms: '圆形脱毛、皮屑、瘙痒、红斑、结痂、皮肤增厚',
      severity: 1
    },
    {
      id: 7,
      name: '耳螨病',
      petType: '通用',
      symptoms: '耳朵瘙痒、摇头、抓耳、耳道有黑褐色分泌物、异味',
      severity: 1
    },
    {
      id: 8,
      name: '胃肠炎',
      petType: '通用',
      symptoms: '呕吐、腹泻、腹痛、食欲下降、精神不振、脱水',
      severity: 2
    }
  ]
  
  diseaseList.value = petType.value 
    ? mockDiseases.filter(d => d.petType === petType.value || d.petType === '通用')
    : mockDiseases
}

const getSeverityLabel = (level) => {
  const labels = {
    1: '轻度',
    2: '中度',
    3: '重度'
  }
  return labels[level] || '未知'
}

const truncateText = (text, maxLength) => {
  if (!text || text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

const goToDetail = (id) => {
  uni.navigateTo({
    url: `/pages/knowledge/disease-detail?id=${id}`
  })
}
</script>

<style scoped>
.filter-tabs {
  display: flex;
  gap: 20rpx;
}

.filter-tab {
  padding: 12rpx 32rpx;
  background-color: #F5F7FA;
  border-radius: 8rpx;
  font-size: 26rpx;
  color: #606266;
  transition: all 0.3s;
}

.filter-tab.active {
  background-color: #409EFF;
  color: white;
}

.severity-tag {
  padding: 6rpx 16rpx;
  border-radius: 4rpx;
  font-size: 22rpx;
  color: white;
}

.level-1 {
  background-color: #67C23A;
}

.level-2 {
  background-color: #E6A23C;
}

.level-3 {
  background-color: #F56C6C;
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
