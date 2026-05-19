<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <text>加载中...</text>
    </view>

    <!-- 疾病详情 -->
    <view v-else>
      <!-- 头部信息 -->
      <view class="card">
        <view class="flex-between mb-20">
          <text class="text-large text-bold">{{ disease.name }}</text>
          <view class="severity-tag" :class="'level-' + disease.severity">
            {{ getSeverityLabel(disease.severity) }}
          </view>
        </view>
        <text class="text-small text-secondary">适用宠物：{{ disease.petType }}</text>
      </view>

      <!-- 症状 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">💡</text>
          <text class="text-medium text-bold">主要症状</text>
        </view>
        <text class="mt-10">{{ disease.symptoms }}</text>
      </view>

      <!-- 治疗方案 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">💊</text>
          <text class="text-medium text-bold">治疗方案</text>
        </view>
        <text class="mt-10">{{ disease.treatment }}</text>
      </view>

      <!-- 病因分析 -->
      <view v-if="disease.cause" class="card">
        <view class="section-header">
          <text class="icon">🔍</text>
          <text class="text-medium text-bold">病因分析</text>
        </view>
        <text class="mt-10">{{ disease.cause }}</text>
      </view>

      <!-- 预防措施 -->
      <view v-if="disease.prevention" class="card">
        <view class="section-header">
          <text class="icon">🛡️</text>
          <text class="text-medium text-bold">预防措施</text>
        </view>
        <text class="mt-10">{{ disease.prevention }}</text>
      </view>

      <!-- 版本信息 -->
      <view class="card version-info">
        <text class="text-small text-secondary">
          📋 知识库版本 v{{ disease.version || 1 }} | 最后更新：{{ disease.updateTime || '2024-01-01' }}
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const app = getApp()
const baseUrl = app.globalData.baseUrl

const elderMode = ref(false)
const loading = ref(true)
const diseaseId = ref(null)
const disease = ref({
  name: '',
  petType: '',
  symptoms: '',
  treatment: '',
  cause: '',
  prevention: '',
  severity: 2,
  version: 1
})

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  if (options.id) {
    diseaseId.value = options.id
    fetchDiseaseDetail()
  } else {
    useMockData(1)
  }
})

const fetchDiseaseDetail = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/disease/${diseaseId.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      disease.value = res.data.data
      loading.value = false
    } else {
      useMockData(diseaseId.value)
    }
  } catch (e) {
    console.error('获取疾病详情失败', e)
    useMockData(diseaseId.value)
  }
}

const useMockData = (id) => {
  const mockDiseases = {
    1: {
      name: '猫瘟热',
      petType: '猫',
      symptoms: '1. 发热：体温可达40-41℃，呈双相热型\n2. 消化系统症状：频繁呕吐、腹泻，严重时出现血便\n3. 精神状态：精神萎靡、嗜睡、反应迟钝\n4. 食欲：食欲废绝，饮水后也会呕吐\n5. 脱水：眼球凹陷、皮肤弹性下降\n6. 血常规：白细胞数量显著减少',
      treatment: '1. 抗病毒治疗：猫用干扰素，皮下注射，每日1-2次\n2. 止吐止泻：使用止吐药和止泻药物控制症状\n3. 抗生素：防止继发细菌感染\n4. 补液治疗：静脉输液纠正脱水和电解质紊乱\n5. 营养支持：高蛋白易消化食物，必要时强制喂食\n6. 护理：保暖、清洁、隔离，避免交叉感染',
      cause: '由猫细小病毒（FPV）引起的高度接触性传染病，病毒可通过直接接触、污染物、跳蚤等传播，未接种疫苗的猫易感，幼猫死亡率极高。',
      prevention: '1. 按时接种疫苗：幼猫8周龄开始接种，每3周1次，共3次，成年后每年加强1次\n2. 定期消毒：使用含氯消毒剂消毒环境\n3. 新猫隔离：新购入猫咪需隔离观察至少2周\n4. 避免接触病猫：不接触患病动物及其分泌物\n5. 增强免疫力：合理饮食，适度运动',
      severity: 3,
      version: 2
    },
    2: {
      name: '猫鼻支',
      petType: '猫',
      symptoms: '1. 呼吸道症状：频繁打喷嚏、流鼻涕，初期为浆液性，后期为脓性\n2. 眼部症状：结膜炎、流泪、眼部分泌物增多，严重时角膜溃疡\n3. 发热：体温升高，精神不振\n4. 食欲：食欲下降或废绝\n5. 口腔：可能出现口腔溃疡',
      treatment: '1. 抗病毒治疗：猫用干扰素，每日1次\n2. 抗生素：防止继发细菌感染\n3. 眼部护理：使用抗病毒眼药水和抗生素眼药水交替滴眼\n4. 支持治疗：补充维生素A，增强免疫力\n5. 补液：食欲废绝时静脉或皮下补液',
      cause: '主要由猫疱疹病毒1型（FHV-1）引起，也可由猫杯状病毒等引起，通过呼吸道分泌物传播，传染性强，恢复后可带毒排毒。',
      prevention: '1. 接种疫苗：猫三联疫苗可预防猫鼻支\n2. 环境通风：保持猫舍清洁通风\n3. 减少应激：避免过度拥挤和应激\n4. 新猫检疫：新猫需隔离观察并完成疫苗接种',
      severity: 2,
      version: 1
    },
    4: {
      name: '犬细小病毒病',
      petType: '狗',
      symptoms: '1. 剧烈呕吐：频繁呕吐，呕吐物初为食物，后为黄绿色液体或带血\n2. 血便：典型的番茄汁样血便，有特殊腥臭味\n3. 发热：体温升高至40℃以上\n4. 脱水：迅速脱水，眼球凹陷，皮肤弹性下降\n5. 精神沉郁：极度萎靡，不愿活动\n6. 食欲废绝：完全不吃不喝',
      treatment: '1. 单抗治疗：犬细小病毒单克隆抗体，早期使用效果好\n2. 干扰素：抗病毒治疗\n3. 止吐止泻：使用止吐药和肠道黏膜保护剂\n4. 抗生素：控制继发感染\n5. 补液：大量静脉输液，纠正脱水和电解质紊乱\n6. 止血：使用止血药物\n7. 禁食禁水：至少24-48小时，症状缓解后逐渐恢复饮食',
      cause: '由犬细小病毒（CPV）引起的高度接触性传染病，通过消化道传播，病毒抵抗力强，可在环境中存活数月，幼犬死亡率极高。',
      prevention: '1. 按时接种疫苗：幼犬6周龄开始接种，每3周1次，共3次\n2. 避免接触：未完成疫苗接种前不外出，不接触病犬\n3. 严格消毒：使用84消毒液等含氯消毒剂\n4. 隔离病犬：及时隔离患病动物，避免扩散',
      severity: 3,
      version: 1
    }
  }
  
  disease.value = mockDiseases[id] || mockDiseases[1]
  loading.value = false
}

const getSeverityLabel = (level) => {
  const labels = {
    1: '轻度',
    2: '中度',
    3: '重度'
  }
  return labels[level] || '未知'
}
</script>

<style scoped>
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 100rpx;
  font-size: 28rpx;
  color: #909399;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.icon {
  font-size: 32rpx;
}

.severity-tag {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
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

.version-info {
  background-color: #F5F7FA;
  border: 1rpx solid #E4E7ED;
  text-align: center;
}
</style>
