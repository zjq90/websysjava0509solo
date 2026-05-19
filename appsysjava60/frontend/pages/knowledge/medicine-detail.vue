<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <text>加载中...</text>
    </view>

    <!-- 药品详情 -->
    <view v-else>
      <!-- 头部信息 -->
      <view class="card">
        <view class="flex-between mb-20">
          <text class="text-large text-bold">{{ medicine.name }}</text>
          <text class="category-tag">{{ medicine.category }}</text>
        </view>
        <text class="text-small text-secondary">通用名：{{ medicine.genericName || medicine.name }}</text>
      </view>

      <!-- 适应症 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">🏥</text>
          <text class="text-medium text-bold">适应症</text>
        </view>
        <text class="mt-10">{{ medicine.indication }}</text>
      </view>

      <!-- 用法用量 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">💉</text>
          <text class="text-medium text-bold">用法用量</text>
        </view>
        <text class="mt-10">{{ medicine.dosage }}</text>
      </view>

      <!-- 不良反应 -->
      <view v-if="medicine.adverseReaction" class="card warning-card">
        <view class="section-header">
          <text class="icon">⚠️</text>
          <text class="text-medium text-bold">不良反应</text>
        </view>
        <text class="mt-10">{{ medicine.adverseReaction }}</text>
      </view>

      <!-- 禁忌 -->
      <view v-if="medicine.contraindication" class="card danger-card">
        <view class="section-header">
          <text class="icon">🚫</text>
          <text class="text-medium text-bold">禁忌</text>
        </view>
        <text class="mt-10">{{ medicine.contraindication }}</text>
      </view>

      <!-- 注意事项 -->
      <view v-if="medicine.attention" class="card">
        <view class="section-header">
          <text class="icon">📝</text>
          <text class="text-medium text-bold">注意事项</text>
        </view>
        <text class="mt-10">{{ medicine.attention }}</text>
      </view>

      <!-- 规格与厂家 -->
      <view class="card">
        <view class="info-row">
          <text class="label">规格：</text>
          <text class="value">{{ medicine.specification || '详见说明书' }}</text>
        </view>
        <view class="info-row mt-10">
          <text class="label">生产厂家：</text>
          <text class="value">{{ medicine.manufacturer || '详见说明书' }}</text>
        </view>
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
const medicineId = ref(null)
const medicine = ref({
  name: '',
  genericName: '',
  category: '',
  indication: '',
  dosage: '',
  adverseReaction: '',
  contraindication: '',
  attention: '',
  specification: '',
  manufacturer: ''
})

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  if (options.id) {
    medicineId.value = options.id
    fetchMedicineDetail()
  } else {
    useMockData(1)
  }
})

const fetchMedicineDetail = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/medicine/${medicineId.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      medicine.value = res.data.data
      loading.value = false
    } else {
      useMockData(medicineId.value)
    }
  } catch (e) {
    console.error('获取药品详情失败', e)
    useMockData(medicineId.value)
  }
}

const useMockData = (id) => {
  const mockMedicines = {
    1: {
      name: '猫用干扰素',
      genericName: '重组猫干扰素ω',
      category: '抗病毒',
      indication: '用于猫瘟热、猫鼻支、猫杯状病毒感染等病毒性疾病的治疗和预防。可增强机体免疫力，帮助患病猫快速恢复。',
      dosage: '皮下注射：\n1. 体重2kg以下：每次100万单位\n2. 体重2-4kg：每次200万单位\n3. 体重4kg以上：每次300-400万单位\n每日1次，连用3-5天，重症可加倍剂量。',
      adverseReaction: '1. 注射部位可能出现轻微红肿、疼痛，一般可自行消退\n2. 少数猫可能出现短暂发热、精神稍差\n3. 过敏反应罕见，如出现应立即停药并给予抗过敏治疗',
      contraindication: '1. 对干扰素或本品任何成分过敏者禁用\n2. 严重肝肾功能不全者慎用\n3. 孕猫慎用，需权衡利弊',
      attention: '1. 本品需2-8℃冷藏保存，避免冷冻\n2. 开启后立即使用，剩余药液应废弃\n3. 与其他药物同时使用时请咨询兽医\n4. 仅限兽用，请置于儿童不易接触处',
      specification: '2ml:100万单位/支',
      manufacturer: '某生物制药有限公司'
    },
    3: {
      name: '头孢噻呋钠',
      genericName: '注射用头孢噻呋钠',
      category: '抗生素',
      indication: '用于敏感菌引起的呼吸道感染、消化道感染、泌尿生殖道感染、皮肤软组织感染、手术预防感染等。对大肠杆菌、沙门氏菌、链球菌、葡萄球菌等有良好抗菌作用。',
      dosage: '肌肉注射：\n1. 犬猫：每公斤体重5mg，每日1次\n2. 重症：剂量可加倍\n3. 疗程：一般3-5天，严重感染可延长至7天',
      adverseReaction: '1. 可能出现胃肠道反应：恶心、呕吐、腹泻\n2. 偶见过敏反应：皮疹、瘙痒\n3. 长期使用可能导致二重感染\n4. 肌肉注射部位可能有轻微疼痛',
      contraindication: '1. 对头孢类抗生素过敏者禁用\n2. 对青霉素类过敏者慎用\n3. 严重肾功能不全者需调整剂量\n4. 新生幼畜慎用',
      attention: '1. 用药前需详细询问过敏史\n2. 肾功能不全者需减量或延长给药间隔\n3. 长期使用需定期检查肾功能\n4. 出现过敏反应立即停药并给予肾上腺素等抢救措施\n5. 注意与其他药物的相互作用',
      specification: '0.5g/瓶',
      manufacturer: '某制药有限公司'
    },
    5: {
      name: '伊曲康唑',
      genericName: '伊曲康唑胶囊',
      category: '抗真菌药',
      indication: '用于皮肤真菌病、猫癣、犬小孢子菌感染、马拉色菌性皮炎、深部真菌感染（如念珠菌病、曲霉菌病）等。对多种皮肤癣菌和酵母菌有良好抗菌作用。',
      dosage: '口服：\n1. 皮肤真菌病：每公斤体重5-10mg，每日1次，连用4-8周\n2. 猫癣：每公斤体重5mg，每日1次，连用6-8周\n3. 深部真菌感染：每公斤体重10mg，每日1-2次，疗程视病情而定\n建议与食物同服以增加吸收',
      adverseReaction: '1. 胃肠道反应：食欲下降、呕吐、腹泻\n2. 长期使用可能引起肝功能异常\n3. 偶见皮肤反应：红斑、瘙痒\n4. 罕见神经系统反应：头晕、嗜睡',
      contraindication: '1. 对伊曲康唑或其他唑类抗真菌药过敏者禁用\n2. 严重肝功能不全者禁用\n3. 孕猫禁用，可能致畸\n4. 哺乳期禁用',
      attention: '1. 用药前和用药期间需定期检查肝功能\n2. 肝功能不全者慎用\n3. 避免与其他肝毒性药物合用\n4. 需坚持足够疗程，过早停药易复发\n5. 注意环境卫生，防止重复感染',
      specification: '100mg/粒',
      manufacturer: '某制药有限公司'
    }
  }
  
  medicine.value = mockMedicines[id] || mockMedicines[1]
  loading.value = false
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

.category-tag {
  padding: 8rpx 20rpx;
  background-color: #67C23A;
  color: white;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.warning-card {
  border-left: 8rpx solid #E6A23C;
}

.danger-card {
  border-left: 8rpx solid #F56C6C;
}

.info-row {
  display: flex;
  align-items: baseline;
}

.label {
  color: #909399;
  font-size: 26rpx;
  min-width: 120rpx;
}

.value {
  color: #303133;
  font-size: 26rpx;
  flex: 1;
}
</style>
