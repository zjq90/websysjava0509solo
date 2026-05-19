<template>
  <view :class="['container', elderMode ? 'elder-mode' : '']">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <text>加载中...</text>
    </view>

    <!-- 案例详情 -->
    <view v-else>
      <!-- 头部信息 -->
      <view class="card">
        <text class="text-large text-bold">{{ caseData.title }}</text>
        <view class="pet-info-card mt-15">
          <view class="info-item">
            <text class="label">品种：</text>
            <text class="value">{{ caseData.breed }}</text>
          </view>
          <view class="info-item">
            <text class="label">年龄：</text>
            <text class="value">{{ caseData.age }}</text>
          </view>
          <view class="info-item">
            <text class="label">性别：</text>
            <text class="value">{{ caseData.gender }}</text>
          </view>
          <view class="info-item">
            <text class="label">类型：</text>
            <text class="value">{{ caseData.petType }}</text>
          </view>
        </view>
        <view class="flex-between mt-15">
          <text class="text-small text-secondary">👁 {{ caseData.viewCount || 0 }} 次浏览</text>
          <text class="text-small text-secondary">医生：{{ caseData.doctorName || '匿名' }}</text>
        </view>
      </view>

      <!-- 主诉 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">📋</text>
          <text class="text-medium text-bold">主诉</text>
        </view>
        <text class="mt-10">{{ caseData.chiefComplaint }}</text>
      </view>

      <!-- 临床检查 -->
      <view v-if="caseData.clinicalExamination" class="card">
        <view class="section-header">
          <text class="icon">🔬</text>
          <text class="text-medium text-bold">临床检查</text>
        </view>
        <text class="mt-10">{{ caseData.clinicalExamination }}</text>
      </view>

      <!-- 诊断结果 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">🏥</text>
          <text class="text-medium text-bold">诊断结果</text>
        </view>
        <view class="diagnosis-box mt-10">
          <text>{{ caseData.diagnosis }}</text>
        </view>
      </view>

      <!-- 治疗方案 -->
      <view class="card">
        <view class="section-header">
          <text class="icon">💊</text>
          <text class="text-medium text-bold">治疗方案</text>
        </view>
        <text class="mt-10">{{ caseData.treatment }}</text>
      </view>

      <!-- 治疗效果 -->
      <view v-if="caseData.treatmentEffect" class="card success-card">
        <view class="section-header">
          <text class="icon">✅</text>
          <text class="text-medium text-bold">治疗效果</text>
        </view>
        <text class="mt-10">{{ caseData.treatmentEffect }}</text>
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
const caseId = ref(null)
const caseData = ref({
  title: '',
  petType: '',
  breed: '',
  age: '',
  gender: '',
  chiefComplaint: '',
  clinicalExamination: '',
  diagnosis: '',
  treatment: '',
  treatmentEffect: '',
  doctorName: '',
  viewCount: 0
})

onMounted(() => {
  elderMode.value = app.globalData.elderMode
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  if (options.id) {
    caseId.value = options.id
    fetchCaseDetail()
  } else {
    useMockData(1)
  }
})

const fetchCaseDetail = async () => {
  try {
    const res = await uni.request({
      url: `${baseUrl}/knowledge/case/${caseId.value}`,
      method: 'GET'
    })

    if (res.data.code === 200) {
      caseData.value = res.data.data
      loading.value = false
    } else {
      useMockData(caseId.value)
    }
  } catch (e) {
    console.error('获取案例详情失败', e)
    useMockData(caseId.value)
  }
}

const useMockData = (id) => {
  const mockCases = {
    1: {
      title: '2岁英短猫瘟热治疗案例',
      petType: '猫',
      breed: '英短',
      age: '2岁',
      gender: '公',
      chiefComplaint: '主诉猫咪近2天持续呕吐，腹泻，精神很差，不吃东西，体温升高，昨天开始拉水样便。已接种疫苗，但接种时间超过1年。',
      clinicalExamination: '体温40.2℃，心率160次/分，呼吸35次/分。精神沉郁，脱水约8%。血常规检查：白细胞总数2.8×10^9/L，中性粒细胞减少。猫瘟抗原检测阳性。',
      diagnosis: '猫瘟热（猫泛白细胞减少症）',
      treatment: '1. 抗病毒治疗：猫用干扰素200万单位皮下注射，每日1次，连用5天\n2. 抗感染：头孢噻呋钠每公斤体重5mg肌肉注射，每日1次，连用5天\n3. 补液治疗：复方氯化钠+5%葡萄糖+维生素C+辅酶A静脉输液，每日2次，纠正脱水\n4. 止吐：奥美拉唑每公斤体重0.5mg皮下注射，每日1次\n5. 止泻：蒙脱石散口服，每日2次\n6. 营养支持：高蛋白肠道处方罐头，强制喂食，配合营养膏',
      treatmentEffect: '治疗第2天呕吐次数减少，第3天体温恢复正常，精神好转，开始主动进食。第5天白细胞回升至6.5×10^9/L。连续治疗7天后痊愈出院。恢复期继续服用益生菌调理肠道，建议少食多餐，2周后完全恢复正常。',
      doctorName: '张医生',
      viewCount: 1256
    },
    2: {
      title: '3岁金毛犬细小病毒病治疗案例',
      petType: '狗',
      breed: '金毛',
      age: '3岁',
      gender: '公',
      chiefComplaint: '主诉狗狗昨天开始呕吐，今天拉血便，精神很差，不愿活动，食欲废绝。昨天在外面可能捡了东西吃，未接种疫苗。',
      clinicalExamination: '体温39.8℃，心率140次/分，精神极度沉郁，脱水约10%，皮肤弹性差，眼窝凹陷。血常规白细胞总数3.2×10^9/L。犬细小病毒抗原检测阳性。',
      diagnosis: '犬细小病毒病',
      treatment: '1. 抗病毒治疗：犬细小单抗按每公斤体重2ml静脉滴注，每日1次，连用5天\n2. 干扰素：每公斤体重50万单位皮下注射，每日1次，连用5天\n3. 抗菌消炎：头孢噻呋钠每公斤体重5mg肌肉注射，每日2次\n4. 止血：酚磺乙胺肌肉注射，每日2次\n5. 补液：大量静脉输液，纠正脱水和电解质紊乱，每日2-3次\n6. 止吐：止吐宁皮下注射，每日2次\n7. 禁食禁水至少48小时，待呕吐停止后逐渐恢复饮食',
      treatmentEffect: '治疗第3天呕吐停止，第4天血便消失，开始有食欲。第6天白细胞恢复至正常范围。连续治疗10天后痊愈出院。出院后给予易消化肠道处方粮，少食多餐，2周后完全恢复正常。',
      doctorName: '张医生',
      viewCount: 987
    },
    3: {
      title: '1岁布偶猫猫癣治疗案例',
      petType: '猫',
      breed: '布偶',
      age: '1岁',
      gender: '母',
      chiefComplaint: '主诉猫咪身上多处脱毛，有皮屑，经常抓挠，已持续约3周时间。家里还有另一只猫也开始出现类似症状。',
      clinicalExamination: '头部、背部、四肢多处圆形脱毛区，边缘有红色丘疹，表面有灰色鳞屑。伍德氏灯检查呈阳性荧光。皮肤刮片镜检发现大量真菌孢子。',
      diagnosis: '皮肤真菌病（猫癣，犬小孢子菌感染）',
      treatment: '1. 口服抗真菌药物：伊曲康唑每公斤体重5mg，每日1次，连用6周\n2. 药浴：抗真菌香波，每周2次，每次药浴15分钟\n3. 外用药：酮康唑软膏涂抹患处，每日2次\n4. 补充维生素B族，增强皮肤抵抗力\n5. 环境消毒：使用含氯消毒剂喷洒环境，猫咪用品暴晒消毒\n6. 同时治疗家中另一只感染的猫',
      treatmentEffect: '治疗2周后瘙痒减轻，脱毛区开始长出新毛。治疗4周后伍德氏灯检查阴性，继续巩固治疗2周后痊愈。随访1个月无复发。建议保持环境干燥通风，定期消毒。',
      doctorName: '王医生',
      viewCount: 2341
    },
    4: {
      title: '5岁泰迪犬耳螨治疗案例',
      petType: '狗',
      breed: '泰迪',
      age: '5岁',
      gender: '母',
      chiefComplaint: '主诉狗狗经常摇头、抓耳朵，耳道有很多黑褐色分泌物，有异味。该症状已持续约2周，最近加重。',
      clinicalExamination: '双侧耳道有大量黑褐色蜡样分泌物，异味明显。耳道分泌物涂片镜检发现大量耳螨成虫和虫卵。耳道皮肤红肿增厚。',
      diagnosis: '耳螨病（犬耳痒螨感染）',
      treatment: '1. 清洁耳道：使用宠物专用洗耳液，每日1-2次，彻底清除耳道内分泌物\n2. 杀螨滴耳液：每日2次，每次2-3滴，连用2周\n3. 体外驱虫：全身体外驱虫，每2周1次，连用2-3次\n4. 环境消毒：使用杀螨喷剂喷洒狗狗活动区域，特别是窝垫\n5. 瘙痒严重时可配合使用止痒药物，防止继发感染',
      treatmentEffect: '治疗1周后摇头抓耳明显减少，2周后复查耳道分泌物基本消失，镜检未发现耳螨。继续巩固治疗1周，完全康复。建议定期进行体外驱虫，保持耳道清洁干燥。',
      doctorName: '王医生',
      viewCount: 1567
    }
  }
  
  caseData.value = mockCases[id] || mockCases[1]
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

.pet-info-card {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx 40rpx;
  padding: 20rpx;
  background-color: #F5F7FA;
  border-radius: 8rpx;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.info-item .label {
  color: #909399;
  font-size: 24rpx;
}

.info-item .value {
  color: #303133;
  font-size: 24rpx;
  font-weight: 500;
}

.diagnosis-box {
  padding: 20rpx;
  background-color: #ECF5FF;
  border: 2rpx solid #409EFF;
  border-radius: 8rpx;
  color: #409EFF;
  font-weight: 500;
}

.success-card {
  border-left: 8rpx solid #67C23A;
}
</style>
