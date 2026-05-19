<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 欢迎卡片 -->
    <view class="card welcome-card">
      <view class="welcome-text">
        <text class="text-large text-bold">欢迎使用</text>
        <text class="text-primary text-large text-bold">宠物医院问诊系统</text>
      </view>
      <view class="welcome-icon">
        <text class="iconfont">&#xe600;</text>
      </view>
    </view>

    <!-- 长辈模式开关 -->
    <view class="card">
      <view class="flex-between">
        <text class="text-medium">长辈模式</text>
        <switch :checked="elderMode" @change="toggleElderMode" color="#409EFF" />
      </view>
      <text class="text-small text-secondary mt-10">开启后字体放大、界面简化，更适合长辈使用</text>
    </view>

    <!-- 快捷入口 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">快捷入口</text>
      <view class="quick-grid">
        <view class="quick-item" @click="goToSearch">
          <view class="quick-icon search-icon">🔍</view>
          <text class="text-small mt-10">智能搜索</text>
        </view>
        <view class="quick-item" @click="goToDiseaseList">
          <view class="quick-icon disease-icon">📋</view>
          <text class="text-small mt-10">疾病百科</text>
        </view>
        <view class="quick-item" @click="goToMedicineList">
          <view class="quick-icon medicine-icon">💊</view>
          <text class="text-small mt-10">药品说明</text>
        </view>
        <view class="quick-item" @click="goToCaseList">
          <view class="quick-icon case-icon">📁</view>
          <text class="text-small mt-10">案例分享</text>
        </view>
        <view class="quick-item" @click="goToStatistics">
          <view class="quick-icon stats-icon">📊</view>
          <text class="text-small mt-10">数据统计</text>
        </view>
        <view class="quick-item" @click="voiceSearch">
          <view class="quick-icon voice-icon">🎤</view>
          <text class="text-small mt-10">语音输入</text>
        </view>
      </view>
    </view>

    <!-- 使用提示 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">使用提示</text>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">在搜索框输入症状描述，系统会自动匹配相关疾病和药品</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">支持语音输入，点击麦克风按钮即可开始语音搜索</text>
      </view>
      <view class="tip-item">
        <text class="tip-dot">•</text>
        <text class="text-small">数据统计支持导出Excel，方便进行数据分析</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const app = getApp()
const elderMode = ref(false)

onMounted(() => {
  elderMode.value = app.globalData.elderMode
})

const toggleElderMode = (e) => {
  elderMode.value = e.detail.value
  app.toggleElderMode(elderMode.value)
}

const goToSearch = () => {
  uni.switchTab({
    url: '/pages/knowledge/search'
  })
}

const goToDiseaseList = () => {
  uni.navigateTo({
    url: '/pages/knowledge/disease-list'
  })
}

const goToMedicineList = () => {
  uni.navigateTo({
    url: '/pages/knowledge/medicine-list'
  })
}

const goToCaseList = () => {
  uni.navigateTo({
    url: '/pages/knowledge/case-list'
  })
}

const goToStatistics = () => {
  uni.switchTab({
    url: '/pages/statistics/index'
  })
}

const voiceSearch = () => {
  uni.showToast({
    title: '开始语音输入...',
    icon: 'none'
  })
  
  // 模拟语音识别
  setTimeout(() => {
    const mockTexts = ['猫呕吐带血', '狗狗发烧不吃东西', '猫咪掉毛严重']
    const randomText = mockTexts[Math.floor(Math.random() * mockTexts.length)]
    
    uni.showModal({
      title: '语音识别结果',
      content: `识别到：${randomText}\n是否立即搜索？`,
      success: (res) => {
        if (res.confirm) {
          uni.navigateTo({
            url: `/pages/knowledge/search?keyword=${encodeURIComponent(randomText)}`
          })
        }
      }
    })
  }, 1500)
}
</script>

<style scoped>
.welcome-card {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-text {
  display: flex;
  flex-direction: column;
}

.welcome-text text {
  color: white;
}

.welcome-icon {
  font-size: 60rpx;
  opacity: 0.8;
}

.quick-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.quick-item {
  width: calc(33.33% - 14rpx);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 10rpx;
  background-color: #F5F7FA;
  border-radius: 12rpx;
  transition: all 0.3s;
}

.quick-item:active {
  transform: scale(0.95);
  background-color: #E4E7ED;
}

.quick-icon {
  font-size: 40rpx;
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-icon {
  background-color: #ECF5FF;
}

.disease-icon {
  background-color: #F0F9EB;
}

.medicine-icon {
  background-color: #FEF0F0;
}

.case-icon {
  background-color: #FDF6EC;
}

.stats-icon {
  background-color: #F0F2EB;
}

.voice-icon {
  background-color: #E6F7FF;
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
</style>
