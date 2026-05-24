<template>
  <view class="credit-container">
    <view class="credit-header">
      <view class="score-display">
        <text class="score-value">{{ creditScore }}</text>
        <text class="score-label">信用分</text>
      </view>
      <view class="level-badge">
        <text class="level-icon">⭐</text>
        <text class="level-text">{{ creditLevel }}</text>
      </view>
    </view>

    <view class="benefit-card">
      <text class="card-title">信用权益</text>
      <view class="benefit-list">
        <view class="benefit-item" :class="{ active: creditScore >= 90 }">
          <text class="benefit-icon">🎁</text>
          <text class="benefit-name">优先调度</text>
          <text class="benefit-condition">≥90分</text>
        </view>
        <view class="benefit-item" :class="{ active: creditScore >= 85 }">
          <text class="benefit-icon">💰</text>
          <text class="benefit-name">折扣优惠</text>
          <text class="benefit-condition">≥85分</text>
        </view>
        <view class="benefit-item" :class="{ active: creditScore >= 650 }">
          <text class="benefit-icon">✅</text>
          <text class="benefit-name">免押金</text>
          <text class="benefit-condition">≥650分</text>
        </view>
        <view class="benefit-item" :class="{ active: creditScore >= 80 }">
          <text class="benefit-icon">⏰</text>
          <text class="benefit-name">延长预约</text>
          <text class="benefit-condition">≥80分</text>
        </view>
      </view>
    </view>

    <view class="rule-card">
      <text class="card-title">信用规则</text>
      <view class="rule-list">
        <view class="rule-item">
          <view class="rule-info">
            <text class="rule-name">规范还车</text>
            <text class="rule-desc">在指定区域规范还车</text>
          </view>
          <text class="rule-score positive">+2分</text>
        </view>
        <view class="rule-item">
          <view class="rule-info">
            <text class="rule-name">故障上报</text>
            <text class="rule-desc">成功上报车辆故障</text>
          </view>
          <text class="rule-score positive">+5分</text>
        </view>
        <view class="rule-item">
          <view class="rule-info">
            <text class="rule-name">乱停放</text>
            <text class="rule-desc">未在指定区域还车</text>
          </view>
          <text class="rule-score negative">-20分</text>
        </view>
        <view class="rule-item">
          <view class="rule-info">
            <text class="rule-name">破坏车辆</text>
            <text class="rule-desc">故意损坏车辆设施</text>
          </view>
          <text class="rule-score negative">-50分</text>
        </view>
      </view>
    </view>

    <view class="record-card">
      <view class="card-header">
        <text class="card-title">信用记录</text>
        <text class="more-link">查看全部 ›</text>
      </view>
      <view class="record-list">
        <view class="record-item" v-for="record in records" :key="record.id">
          <view class="record-info">
            <text class="record-name">{{ record.name }}</text>
            <text class="record-time">{{ record.time }}</text>
          </view>
          <text class="record-score" :class="record.type === 'add' ? 'positive' : 'negative'">
            {{ record.type === 'add' ? '+' : '' }}{{ record.score }}分
          </text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const creditScore = ref(95)

const creditLevel = computed(() => {
  if (creditScore.value >= 90) return '优秀'
  if (creditScore.value >= 80) return '良好'
  if (creditScore.value >= 60) return '一般'
  return '较差'
})

const records = ref([
  { id: 1, name: '规范骑行奖励', time: '2024-01-15 18:30', score: 2, type: 'add' },
  { id: 2, name: '故障上报奖励', time: '2024-01-14 09:15', score: 5, type: 'add' },
  { id: 3, name: '规范骑行奖励', time: '2024-01-13 20:45', score: 2, type: 'add' }
])
</script>

<style lang="scss" scoped>
.credit-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 20px;
}

.credit-header {
  background: linear-gradient(135deg, #00A862 0%, #00c874 100%);
  padding: 50px 20px 40px;
  text-align: center;
}

.score-display {
  margin-bottom: 16px;
}

.score-value {
  display: block;
  font-size: 64px;
  font-weight: bold;
  color: white;
  line-height: 1;
}

.score-label {
  font-size: 14px;
  color: rgba(255,255,255,0.8);
  margin-top: 4px;
}

.level-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: rgba(255,255,255,0.2);
  padding: 6px 16px;
  border-radius: 16px;
}

.level-icon {
  font-size: 16px;
}

.level-text {
  font-size: 14px;
  color: white;
}

.benefit-card, .rule-card, .record-card {
  background: white;
  margin: 12px;
  border-radius: 12px;
  padding: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.card-title {
  display: block;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
}

.card-header .card-title {
  margin-bottom: 0;
}

.more-link {
  font-size: 14px;
  color: #00A862;
}

.benefit-list {
  display: flex;
  justify-content: space-around;
}

.benefit-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  opacity: 0.4;

  &.active {
    opacity: 1;
  }
}

.benefit-icon {
  font-size: 28px;
}

.benefit-name {
  font-size: 12px;
  color: #333;
}

.benefit-condition {
  font-size: 10px;
  color: #999;
}

.rule-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.rule-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rule-info {
  flex: 1;
}

.rule-name {
  display: block;
  font-size: 14px;
  color: #333;
}

.rule-desc {
  font-size: 12px;
  color: #999;
}

.rule-score {
  font-size: 14px;
  font-weight: bold;

  &.positive {
    color: #4caf50;
  }

  &.negative {
    color: #f44336;
  }
}

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-info {
  flex: 1;
}

.record-name {
  display: block;
  font-size: 14px;
  color: #333;
}

.record-time {
  font-size: 12px;
  color: #999;
}

.record-score {
  font-size: 14px;
  font-weight: bold;

  &.positive {
    color: #4caf50;
  }

  &.negative {
    color: #f44336;
  }
}
</style>
