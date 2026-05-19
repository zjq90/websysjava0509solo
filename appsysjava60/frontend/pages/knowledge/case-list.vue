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

    <!-- 案例列表 -->
    <view class="card" v-for="item in caseList" :key="item.id" @click="goToDetail(item.id)">
      <view class="flex-between">
        <text class="text-bold text-medium">{{ item.title }}</text>
        <text class="type-tag">{{ item.petType }}</text>
      </view>
      <view class="pet-info mt-10">
        <text class="text-small text-secondary">{{ item.breed }} | {{ item.age }} | {{ item.gender }}</text>
      </view>
      <text class="text-small mt-10">{{ truncateText(item.chiefComplaint, 80) }}</text>
      <view class="flex-between mt-15">
        <text class="text-small text-secondary">👁 {{ item.viewCount || 0 }} 次浏览</text>
        <text class="text-small text-primary">查看详情 →</text>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="caseList.length === 0" class="empty-state">
      <text class="empty-icon">📁</text>
      <text class="text-medium mt-20">暂无案例</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const petType = ref('')
const caseList = ref([])

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  fetchCaseList()
})

watch(petType, () => {
  fetchCaseList()
})

const fetchCaseList = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/case/list?petType=${petType.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      caseList.value = res.data.data
    } else {
      useMockData()
    }
  } catch (e) {
    console.error('获取案例列表失败', e)
    useMockData()
  }
}

const useMockData = () => {
  const mockCases = [
    {
      id: 1,
      title: '2岁英短猫瘟热治疗案例',
      petType: '猫',
      breed: '英短',
      age: '2岁',
      gender: '公',
      chiefComplaint: '主诉猫咪近2天持续呕吐，腹泻，精神很差，不吃东西，体温升高。',
      viewCount: 1256
    },
    {
      id: 2,
      title: '3岁金毛犬细小病毒病治疗案例',
      petType: '狗',
      breed: '金毛',
      age: '3岁',
      gender: '公',
      chiefComplaint: '主诉狗狗昨天开始呕吐，今天拉血便，精神很差，不愿活动，食欲废绝。',
      viewCount: 987
    },
    {
      id: 3,
      title: '1岁布偶猫猫癣治疗案例',
      petType: '猫',
      breed: '布偶',
      age: '1岁',
      gender: '母',
      chiefComplaint: '主诉猫咪身上多处脱毛，有皮屑，经常抓挠，已持续约3周时间。',
      viewCount: 2341
    },
    {
      id: 4,
      title: '5岁泰迪犬耳螨治疗案例',
      petType: '狗',
      breed: '泰迪',
      age: '5岁',
      gender: '母',
      chiefComplaint: '主诉狗狗经常摇头、抓耳朵，耳道有很多黑褐色分泌物，有异味。',
      viewCount: 1567
    }
  ]
  
  caseList.value = petType.value 
    ? mockCases.filter(c => c.petType === petType.value)
    : mockCases
}

const truncateText = (text, maxLength) => {
  if (!text || text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

const goToDetail = (id) => {
  uni.navigateTo({
    url: `/pages/knowledge/case-detail?id=${id}`
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

.type-tag {
  padding: 6rpx 16rpx;
  background-color: #E6A23C;
  color: white;
  border-radius: 4rpx;
  font-size: 22rpx;
}

.pet-info {
  padding: 8rpx 16rpx;
  background-color: #F5F7FA;
  border-radius: 4rpx;
  display: inline-block;
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
