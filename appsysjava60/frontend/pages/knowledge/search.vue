<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        v-model="keyword" 
        placeholder="输入症状描述，如：猫呕吐带血"
        @confirm="doSearch"
      />
      <view class="voice-btn" @click="voiceSearch">
        <text>🎤</text>
      </view>
      <view class="search-btn" @click="doSearch">
        <text>搜索</text>
      </view>
    </view>

    <!-- 快捷分类 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">快捷分类</text>
      <view class="category-tabs">
        <text 
          class="tab-item" 
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >全部</text>
        <text 
          class="tab-item" 
          :class="{ active: activeTab === 'disease' }"
          @click="switchTab('disease')"
        >疾病</text>
        <text 
          class="tab-item" 
          :class="{ active: activeTab === 'medicine' }"
          @click="switchTab('medicine')"
        >药品</text>
        <text 
          class="tab-item" 
          :class="{ active: activeTab === 'case' }"
          @click="switchTab('case')"
        >案例</text>
      </view>
    </view>

    <!-- 搜索结果 -->
    <view v-if="results.length > 0">
      <view class="card" v-for="item in filteredResults" :key="item.id + item.type">
        <view class="result-header flex-between">
          <view class="result-type">
            <text class="type-tag" :class="item.type">{{ getTypeLabel(item.type) }}</text>
            <text class="text-small text-secondary ml-10">{{ item.petType }}</text>
          </view>
          <text class="text-small text-secondary">相关性: {{ (item.relevanceScore / 10).toFixed(1) }}%</text>
        </view>
        <view class="result-title mt-10" @click="goToDetail(item)">
          <rich-text :nodes="item.title"></rich-text>
        </view>
        <view class="result-summary mt-10 text-small text-secondary" @click="goToDetail(item)">
          <rich-text :nodes="item.summary"></rich-text>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else-if="searched" class="empty-state">
      <text class="empty-icon">🔍</text>
      <text class="text-medium mt-20">未找到相关结果</text>
      <text class="text-small text-secondary mt-10">请尝试其他关键词或症状描述</text>
    </view>

    <!-- 初始提示 -->
    <view v-else class="card">
      <text class="text-medium text-bold mb-20">搜索提示</text>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">输入宠物症状，如"呕吐、腹泻、发烧"等</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">可以指定宠物类型，如"猫呕吐"、"狗发烧"</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">点击麦克风按钮使用语音输入搜索</text>
      </view>
      
      <text class="text-medium text-bold mt-20 mb-10">热门搜索</text>
      <view class="hot-tags">
        <text class="hot-tag" @click="quickSearch('猫呕吐带血')">猫呕吐带血</text>
        <text class="hot-tag" @click="quickSearch('狗狗发烧')">狗狗发烧</text>
        <text class="hot-tag" @click="quickSearch('猫瘟')">猫瘟</text>
        <text class="hot-tag" @click="quickSearch('犬细小')">犬细小</text>
        <text class="hot-tag" @click="quickSearch('皮肤真菌')">皮肤真菌</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const keyword = ref('')
const results = ref([])
const searched = ref(false)
const activeTab = ref('all')

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  
  // 检查是否有传入的搜索关键词
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  if (options.keyword) {
    keyword.value = decodeURIComponent(options.keyword)
    doSearch()
  }
})

const switchTab = (tab) => {
  activeTab.value = tab
}

const filteredResults = () => {
  if (activeTab.value === 'all') {
    return results.value
  }
  return results.value.filter(item => item.type === activeTab.value)
}

const getTypeLabel = (type) => {
  const labels = {
    disease: '疾病',
    medicine: '药品',
    case: '案例'
  }
  return labels[type] || type
}

const doSearch = async () => {
  if (!keyword.value.trim()) {
    uni.showToast({
      title: '请输入搜索关键词',
      icon: 'none'
    })
    return
  }

  uni.showLoading({
    title: '搜索中...'
  })

  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/search?keyword=${encodeURIComponent(keyword.value)}`,
      method: 'GET'
    })

    uni.hideLoading()

    if (res.data.code === 200) {
      results.value = processResults(res.data.data)
      searched.value = true
      
      if (results.value.length === 0) {
        uni.showToast({
          title: '未找到相关结果',
          icon: 'none'
        })
      }
    } else {
      uni.showToast({
        title: res.data.message || '搜索失败',
        icon: 'none'
      })
    }
  } catch (e) {
    uni.hideLoading()
    console.error('搜索失败', e)
    // 使用模拟数据
    useMockData()
  }
}

const processResults = (data) => {
  return data.map(item => {
    // 将后端返回的高亮标签转换为rich-text可识别的格式
    item.title = item.title.replace(/<em>(.*?)<\/em>/g, '<span class="highlight">$1</span>')
    item.summary = item.summary.replace(/<em>(.*?)<\/em>/g, '<span class="highlight">$1</span>')
    return item
  })
}

const useMockData = () => {
  const mockResults = [
    {
      id: 1,
      type: 'disease',
      title: `<span class="highlight">猫</span>瘟热`,
      summary: '症状：发热、<span class="highlight">呕吐</span>、<span class="highlight">腹泻</span>、精神萎靡、白细胞减少...',
      relevanceScore: 95.5,
      petType: '猫'
    },
    {
      id: 2,
      type: 'disease',
      title: '猫<span class="highlight">胃肠炎</span>',
      summary: '症状：<span class="highlight">呕吐</span>、<span class="highlight">腹泻</span>、腹痛、食欲下降、精神不振...',
      relevanceScore: 88.3,
      petType: '猫'
    },
    {
      id: 3,
      type: 'medicine',
      title: '猫<span class="highlight">用</span>干扰素',
      summary: '用于猫瘟热、猫鼻支等病毒性疾病的治疗，皮下注射，每次100-200万单位...',
      relevanceScore: 75.2,
      petType: '通用'
    },
    {
      id: 4,
      type: 'case',
      title: '2岁英短猫瘟热治疗<span class="highlight">案例</span>',
      summary: '主诉猫咪近2天持续<span class="highlight">呕吐</span>，<span class="highlight">腹泻</span>，精神很差，不吃东西...',
      relevanceScore: 70.8,
      petType: '猫'
    }
  ]
  results.value = mockResults
  searched.value = true
}

const quickSearch = (text) => {
  keyword.value = text
  doSearch()
}

const voiceSearch = () => {
  uni.showToast({
    title: '开始语音输入...',
    icon: 'none'
  })
  
  setTimeout(() => {
    const mockTexts = ['猫呕吐带血', '狗狗发烧不吃东西', '猫咪掉毛严重']
    const randomText = mockTexts[Math.floor(Math.random() * mockTexts.length)]
    
    uni.showModal({
      title: '语音识别结果',
      content: `识别到：${randomText}\n是否立即搜索？`,
      success: (res) => {
        if (res.confirm) {
          keyword.value = randomText
          doSearch()
        }
      }
    })
  }, 1500)
}

const goToDetail = (item) => {
  const urlMap = {
    disease: `/pages/knowledge/disease-detail?id=${item.id}`,
    medicine: `/pages/knowledge/medicine-detail?id=${item.id}`,
    case: `/pages/knowledge/case-detail?id=${item.id}`
  }
  uni.navigateTo({
    url: urlMap[item.type]
  })
}
</script>

<style scoped>
.search-bar {
  display: flex;
  gap: 16rpx;
  margin-bottom: 20rpx;
}

.search-input {
  flex: 1;
  background-color: #FFFFFF;
  border-radius: 8rpx;
  padding: 20rpx 24rpx;
  font-size: 28rpx;
}

.voice-btn, .search-btn {
  background-color: #409EFF;
  color: white;
  border-radius: 8rpx;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.voice-btn {
  background-color: #E6A23C;
  padding: 20rpx 24rpx;
}

.category-tabs {
  display: flex;
  gap: 20rpx;
  flex-wrap: wrap;
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

.result-header {
  margin-bottom: 10rpx;
}

.result-type {
  display: flex;
  align-items: center;
}

.type-tag {
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
  font-size: 22rpx;
  color: white;
}

.type-tag.disease {
  background-color: #F56C6C;
}

.type-tag.medicine {
  background-color: #67C23A;
}

.type-tag.case {
  background-color: #E6A23C;
}

.ml-10 {
  margin-left: 10rpx;
}

.result-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
  line-height: 1.6;
}

.result-summary {
  line-height: 1.6;
  max-height: 120rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.highlight {
  color: #F56C6C;
  font-weight: bold;
  background-color: #FEF0F0;
  padding: 0 4rpx;
  border-radius: 4rpx;
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

.tip-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 16rpx;
}

.tip-dot {
  color: #409EFF;
  margin-right: 10rpx;
  font-weight: bold;
}

.hot-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.hot-tag {
  padding: 12rpx 20rpx;
  background-color: #ECF5FF;
  color: #409EFF;
  border-radius: 20rpx;
  font-size: 26rpx;
}
</style>
