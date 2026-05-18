<template>
  <div class="page-container">
    <div class="warning-header">
      <span class="warning-icon">⚠️</span>
      <span class="warning-title">紧急心理危机干预</span>
    </div>

    <div class="hotline-section">
      <div class="hotline-card" v-for="hotline in emergencyHotlines" :key="hotline.id" @click="callPhone(hotline.phone)">
        <span class="hotline-icon">📞</span>
        <div class="hotline-info">
          <span class="hotline-name">{{ hotline.name }}</span>
          <span class="hotline-desc">{{ hotline.description }}</span>
        </div>
        <div class="hotline-call">
          <span class="call-text">拨打</span>
          <span class="call-icon">→</span>
        </div>
      </div>
    </div>

    <div class="self-help-section">
      <div class="section-title">
        <span class="title-icon">💡</span>
        <span class="title-text">自我急救方法</span>
      </div>
      
      <div class="self-help-list">
        <div class="self-help-item" v-for="item in selfHelpMethods" :key="item.id">
          <span class="method-icon">{{ item.icon }}</span>
          <div class="method-content">
            <span class="method-title">{{ item.title }}</span>
            <span class="method-desc">{{ item.description }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="breathing-section">
      <div class="section-title">
        <span class="title-icon">🧘</span>
        <span class="title-text">呼吸练习</span>
      </div>
      
      <div class="breathing-exercise" @click="toggleBreathing">
        <div class="breathing-circle" :class="{ active: isBreathing }">
          <span class="breathing-text">{{ breathingPhase }}</span>
        </div>
        <span class="breathing-hint">{{ isBreathing ? '点击停止' : '点击开始练习' }}</span>
      </div>
    </div>

    <div class="safety-section">
      <div class="section-title">
        <span class="title-icon">💚</span>
        <span class="title-text">安全计划</span>
      </div>
      
      <div class="safety-list">
        <div class="safety-item" v-for="(item, index) in safetyPlan" :key="index">
          <span class="safety-number">{{ index + 1 }}</span>
          <span class="safety-text">{{ item }}</span>
        </div>
      </div>
    </div>

    <div class="disclaimer">
      <span class="disclaimer-text">
        本服务仅供心理援助使用，不能替代专业医疗服务。
        若情况紧急，请立即前往附近医院急诊科或拨打120急救电话。
      </span>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  setup() {
    const isBreathing = ref(false)
    const breathingPhase = ref('准备开始')
    let breathingTimer = null
    
    const emergencyHotlines = ref([
      { id: 1, name: '全国心理援助热线', phone: '400-161-9995', description: '24小时免费心理咨询服务' },
      { id: 2, name: '北京心理危机研究中心', phone: '010-82951332', description: '专业危机干预服务' },
      { id: 3, name: '希望24热线', phone: '400-161-9995', description: '生命教育与危机干预' },
      { id: 4, name: '全国妇女维权热线', phone: '12338', description: '心理健康与法律咨询' }
    ])
    
    const selfHelpMethods = ref([
      { id: 1, icon: '🌿', title: '5-4-3-2-1接地法', description: '说出5个看到的东西、4个感觉到的、3个听到的、2个闻到的、1个尝到的' },
      { id: 2, icon: '💧', title: '冷水刺激', description: '用冷水洗脸或握住冰块，转移注意力' },
      { id: 3, icon: '🚶', title: '身体活动', description: '散步、慢跑或做简单的伸展运动' },
      { id: 4, icon: '📝', title: '情绪日记', description: '写下当下的感受，释放情绪压力' }
    ])
    
    const safetyPlan = ref([
      '保证环境安全：移走身边可能造成伤害的物品',
      '找到支持系统：联系至少一位信任的人陪伴',
      '寻求专业帮助：联系心理咨询师或医生',
      '等待情绪平复：做放松练习，等待情绪好转',
      '制定后续计划：规划接下来的行动方案'
    ])
    
    const callPhone = (phone) => {
      alert(`即将拨打: ${phone}`)
    }
    
    const toggleBreathing = () => {
      if (isBreathing.value) {
        isBreathing.value = false
        breathingPhase.value = '准备开始'
        if (breathingTimer) {
          clearInterval(breathingTimer)
          breathingTimer = null
        }
      } else {
        isBreathing.value = true
        startBreathingCycle()
      }
    }
    
    const startBreathingCycle = () => {
      const phases = [
        { text: '吸气', duration: 4000 },
        { text: '屏息', duration: 4000 },
        { text: '呼气', duration: 6000 }
      ]
      
      let currentPhase = 0
      
      const runPhase = () => {
        if (!isBreathing.value) return
        
        breathingPhase.value = phases[currentPhase].text
        
        breathingTimer = setTimeout(() => {
          currentPhase = (currentPhase + 1) % phases.length
          runPhase()
        }, phases[currentPhase].duration)
      }
      
      runPhase()
    }
    
    return {
      isBreathing,
      breathingPhase,
      emergencyHotlines,
      selfHelpMethods,
      safetyPlan,
      callPhone,
      toggleBreathing
    }
  }
}
</script>

<style scoped>
.warning-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  padding: 40rpx;
  background: linear-gradient(135deg, #F56C6C 0%, #FF8787 100%);
  border-radius: 20rpx;
  margin-bottom: 30rpx;
}

.warning-icon {
  font-size: 48rpx;
}

.warning-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #ffffff;
}

.hotline-section {
  margin-bottom: 40rpx;
}

.hotline-card {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 30rpx;
  background: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.hotline-icon {
  font-size: 40rpx;
}

.hotline-info {
  flex: 1;
}

.hotline-name {
  display: block;
  font-size: 28rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8rpx;
}

.hotline-desc {
  font-size: 24rpx;
  color: #909399;
}

.hotline-call {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.call-text {
  font-size: 26rpx;
  color: #409EFF;
  font-weight: 500;
}

.call-icon {
  font-size: 24rpx;
  color: #409EFF;
}

.self-help-section {
  margin-bottom: 40rpx;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 24rpx;
  padding: 0 10rpx;
}

.title-icon {
  font-size: 32rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #303133;
}

.self-help-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.self-help-item {
  display: flex;
  align-items: flex-start;
  gap: 20rpx;
  padding: 24rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.method-icon {
  font-size: 36rpx;
}

.method-content {
  flex: 1;
}

.method-title {
  display: block;
  font-size: 26rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8rpx;
}

.method-desc {
  font-size: 24rpx;
  color: #909399;
  line-height: 1.6;
}

.breathing-section {
  margin-bottom: 40rpx;
}

.breathing-exercise {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.breathing-circle {
  width: 200rpx;
  height: 200rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #67C23A 0%, #85CE61 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 24rpx;
  transition: all 2s ease-in-out;
}

.breathing-circle.active {
  transform: scale(1.3);
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
}

.breathing-text {
  font-size: 28rpx;
  font-weight: 600;
  color: #ffffff;
}

.breathing-hint {
  font-size: 24rpx;
  color: #909399;
}

.safety-section {
  margin-bottom: 40rpx;
}

.safety-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.safety-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 20rpx 24rpx;
  background: #ffffff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.safety-number {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #67C23A 0%, #85CE61 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 600;
  color: #ffffff;
  flex-shrink: 0;
}

.safety-text {
  flex: 1;
  font-size: 26rpx;
  color: #606266;
  line-height: 1.5;
}

.disclaimer {
  padding: 30rpx;
  background: #FEF0F0;
  border-radius: 12rpx;
}

.disclaimer-text {
  font-size: 24rpx;
  color: #F56C6C;
  line-height: 1.8;
}
</style>
