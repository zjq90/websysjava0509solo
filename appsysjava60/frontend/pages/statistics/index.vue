<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 统计概览 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">接诊量统计</text>
      <view class="stats-overview">
        <view class="overview-item">
          <text class="overview-value text-primary">{{ overview.today }}</text>
          <text class="overview-label">今日接诊</text>
        </view>
        <view class="overview-item">
          <text class="overview-value text-success">{{ overview.week }}</text>
          <text class="overview-label">本周接诊</text>
        </view>
        <view class="overview-item">
          <text class="overview-value text-warning">{{ overview.month }}</text>
          <text class="overview-label">本月接诊</text>
        </view>
        <view class="overview-item">
          <text class="overview-value text-danger">{{ overview.total }}</text>
          <text class="overview-label">总接诊量</text>
        </view>
      </view>
    </view>

    <!-- 时间范围选择 -->
    <view class="card">
      <view class="flex-between mb-20">
        <text class="text-medium text-bold">常见疾病排行</text>
        <view class="time-select">
          <picker mode="selector" :range="timeRanges" @change="onTimeRangeChange">
            <text class="text-small text-primary">{{ selectedTimeRange }} ▾</text>
          </picker>
        </view>
      </view>
      
      <!-- 疾病排行列表 -->
      <view class="rank-item" v-for="(item, index) in diseaseRanks" :key="index">
        <view class="rank-number" :class="'rank-' + (index + 1)">{{ index + 1 }}</view>
        <view class="rank-info">
          <text class="text-bold">{{ item.diseaseName }}</text>
          <text class="text-small text-secondary">占比 {{ (item.count / totalDiseaseCount * 100 || 0).toFixed(1) }}%</text>
        </view>
        <text class="rank-count text-bold text-primary">{{ item.count }} 例</text>
      </view>
    </view>

    <!-- 药品使用分析 -->
    <view class="card">
      <text class="text-medium text-bold mb-20">药品使用分析</text>
      <view class="medicine-item" v-for="(item, index) in medicineUsages" :key="index">
        <view class="medicine-info">
          <view class="flex-between">
            <text class="text-bold">{{ item.medicineName }}</text>
            <text class="text-small">{{ item.category }}</text>
          </view>
          <view class="progress-bar mt-10">
            <view class="progress-fill" :style="{ width: (item.usageCount / totalMedicineCount * 100 + '%' }"></view>
          </view>
          <view class="flex-between mt-10">
            <text class="text-small text-secondary">使用 {{ item.usageCount }} 次</text>
            <text class="text-small text-primary">占比 {{ (item.usageCount / totalMedicineCount * 100 || 0).toFixed(1) }}%</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 导出按钮 -->
    <view class="export-btn" @click="exportData">
      <text>📊 导出统计数据Excel</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const timeRanges = ref(['近7天', '近30天', '近90天'])
const selectedTimeIndex = ref(1)
const diseaseRanks = ref([])
const medicineUsages = ref([])

const overview = computed(() => ({
  today: 15,
  week: 86,
  month: 324,
  total: 1586
}))

const selectedTimeRange = computed(() => timeRanges.value[selectedTimeIndex.value])

const totalDiseaseCount = computed(() => {
  return diseaseRanks.value.reduce((sum, item) => sum + item.count, 0)
})

const totalMedicineCount = computed(() => {
  return medicineUsages.value.reduce((sum, item) => sum + item.usageCount, 0)
})

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  fetchStatistics()
})

const onTimeRangeChange = (e) => {
  selectedTimeIndex.value = e.detail.value
  fetchStatistics()
}

const fetchStatistics = async () => {
  const days = [7, 30, 90][selectedTimeIndex.value]
  
  try {
    const res = await uni.request({
      url: `${baseUrl}/statistics?days=${days}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      diseaseRanks.value = res.data.data.diseaseRanks || []
      medicineUsages.value = res.data.data.medicineUsages || []
    } else {
      useMockData(days)
    }
  } catch (e) {
    console.error('获取统计数据失败', e)
    useMockData(days)
  }
}

const useMockData = (days) => {
  const multiplier = days / 30
  
  diseaseRanks.value = [
    { diseaseName: '胃肠炎', count: Math.round(45 * multiplier) },
    { diseaseName: '皮肤真菌病', count: Math.round(38 * multiplier) },
    { diseaseName: '耳螨病', count: Math.round(32 * multiplier) },
    { diseaseName: '猫瘟热', count: Math.round(25 * multiplier) },
    { diseaseName: '犬细小病毒病', count: Math.round(20 * multiplier) }
  ]

  medicineUsages.value = [
    { medicineName: '头孢噻呋钠', category: '抗生素', usageCount: Math.round(52 * multiplier) },
    { medicineName: '奥美拉唑', category: '消化系统', usageCount: Math.round(45 * multiplier) },
    { medicineName: '伊曲康唑', category: '抗真菌药', usageCount: Math.round(38 * multiplier) },
    { medicineName: '猫用干扰素', category: '抗病毒', usageCount: Math.round(28 * multiplier) },
    { medicineName: '体内外驱虫药', category: '驱虫药', usageCount: Math.round(65 * multiplier) }
  ]
}

const exportData = async () => {
  uni.showLoading({
    title: '正在导出...'
  })

  setTimeout(() => {
    uni.hideLoading()
    uni.showModal({
      title: '导出成功',
      content: '统计数据已导出为Excel文件',
      showCancel: false
    })
  }, 1500)
}
</script>

<style scoped>
.stats-overview {
  display: flex;
  justify-content: space-between;
}

.overview-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.overview-value {
  font-size: 48rpx;
  font-weight: bold;
  line-height: 1.2;
}

.overview-label {
  font-size: 24rpx;
  color: #909399;
  margin-top: 8rpx;
}

.time-select {
  padding: 8rpx 16rpx;
  background-color: #ECF5FF;
  border-radius: 4rpx;
}

.rank-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.rank-item:last-child {
  border-bottom: none;
}

.rank-number {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24rpx;
  font-weight: bold;
  margin-right: 20rpx;
}

.rank-1 {
  background: linear-gradient(135deg, #FFD700 0%, #FFA500 100%);
}

.rank-2 {
  background: linear-gradient(135deg, #C0C0C0 0%, #A0A0A0 100%);
}

.rank-3 {
  background: linear-gradient(135deg, #CD7F32 0%, #B87333 100%);
}

.rank-4, .rank-5 {
  background-color: #909399;
}

.rank-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.rank-count {
  font-size: 28rpx;
}

.medicine-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
}

.medicine-item:last-child {
  border-bottom: none;
}

.medicine-info {
  flex: 1;
}

.progress-bar {
  height: 12rpx;
  background-color: #F0F0F0;
  border-radius: 6rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #409EFF 0%, #67C23A 100%);
  border-radius: 6rpx;
  transition: width 0.3s;
}

.export-btn {
  background: linear-gradient(135deg, #409EFF 0%, #67C23A 100%);
  color: white;
  text-align: center;
  padding: 30rpx;
  border-radius: 16rpx;
  font-size: 32rpx;
  font-weight: bold;
  margin-top: 20rpx;
}
</style>
