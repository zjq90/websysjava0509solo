<template>
  <view class="container">
    <view class="pet-selector">
      <view 
        class="pet-tab" 
        :class="{ active: selectedPet === pet.id }" 
        v-for="pet in pets" 
        :key="pet.id"
        @click="selectPet(pet.id)"
      >
        <text class="pet-emoji">{{ pet.type === 'dog' ? '🐕' : '🐱' }}</text>
        <text class="pet-name">{{ pet.name }}</text>
      </view>
    </view>

    <view class="generate-btn" @click="generateSuggestion">
      <text class="btn-icon">✨</text>
      <text class="btn-text">生成个性化饮食建议</text>
    </view>

    <view class="suggestion-card" v-if="suggestion">
      <view class="card-header">
        <text class="card-title">{{ suggestion.title }}</text>
        <text class="card-date">{{ formatDate(suggestion.generatedTime) }}</text>
      </view>
      
      <view class="summary-stats">
        <view class="stat-item">
          <text class="stat-value">{{ suggestion.calorieRecommendation }}</text>
          <text class="stat-label">kcal/天</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ suggestion.foodType }}</text>
          <text class="stat-label">推荐食物</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ suggestion.feedingFrequency }}</text>
          <text class="stat-label">喂食频率</text>
        </view>
      </view>

      <view class="content-section">
        <text class="section-title">📋 详细建议</text>
        <text class="content-text">{{ suggestion.content }}</text>
      </view>

      <view class="reference-section">
        <text class="section-title">📚 权威参考来源</text>
        <text class="reference-text">{{ suggestion.referenceSource }}</text>
      </view>
    </view>

    <view class="history-section">
      <text class="section-header">历史建议</text>
      <view class="history-list" v-if="history.length > 0">
        <view class="history-item" v-for="item in history" :key="item.id">
          <text class="history-title">{{ item.title }}</text>
          <text class="history-date">{{ formatDate(item.generatedTime) }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      pets: [
        { id: 1, name: '旺财', type: 'dog', weight: 25.5 },
        { id: 2, name: '咪咪', type: 'cat', weight: 4.2 }
      ],
      selectedPet: 1,
      suggestion: null,
      history: []
    }
  },
  onLoad() {
    this.loadSuggestion()
    this.loadHistory()
  },
  methods: {
    selectPet(id) {
      this.selectedPet = id
      this.suggestion = null
      this.loadSuggestion()
      this.loadHistory()
    },
    loadSuggestion() {
      this.$request('/diet/generate/' + this.selectedPet, 'POST').then(res => {
        if (res.code === 200) {
          this.suggestion = res.data
        }
      }).catch(() => {
        const pet = this.pets.find(p => p.id === this.selectedPet)
        this.suggestion = {
          title: pet.name + '的个性化饮食建议',
          content: pet.type === 'dog' 
            ? '根据AAFCO标准，25kg金毛每日约需820kcal热量。建议喂食高蛋白全价狗粮，每日分2-3次喂食。注意控制体重，避免肥胖。\n\n参考文献：AAFCO Dog and Cat Food Nutrient Profiles, 2023'
            : '根据WSAVA指南，4kg英短每日约需230kcal热量。建议喂食优质猫粮，确保牛磺酸含量≥0.1%。干湿搭配喂食，增加水分摄入。\n\n参考文献：WSAVA Global Nutrition Guidelines, 2022',
          calorieRecommendation: pet.type === 'dog' ? 820 : 230,
          foodType: pet.type === 'dog' ? '大型犬成犬粮' : '全价猫粮（干湿搭配）',
          feedingFrequency: pet.type === 'dog' ? '每日2-3次' : '每日3-4次',
          referenceSource: pet.type === 'dog' ? 'AAFCO, NRC' : 'WSAVA, FEDIAF',
          generatedTime: new Date().toISOString()
        }
      })
    },
    loadHistory() {
      this.$request('/diet/history/' + this.selectedPet).then(res => {
        if (res.code === 200) {
          this.history = res.data
        }
      }).catch(() => {
        this.history = [
          { id: 1, title: '旺财的饮食建议', generatedTime: new Date().toISOString() },
          { id: 2, title: '旺财的饮食建议', generatedTime: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString() }
        ]
      })
    },
    generateSuggestion() {
      this.loadSuggestion()
      this.$showToast('建议已生成', 'success')
    },
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    }
  }
}
</script>

<style scoped>
.pet-selector {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.pet-tab {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25rpx;
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
  border: 2rpx solid transparent;
}

.pet-tab.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
}

.pet-emoji {
  font-size: 50rpx;
  margin-bottom: 10rpx;
}

.pet-name {
  font-size: 26rpx;
  color: #333;
}

.generate-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 24rpx;
  border-radius: 50rpx;
  margin-bottom: 30rpx;
}

.btn-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.btn-text {
  font-size: 28rpx;
}

.suggestion-card {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
  margin-bottom: 30rpx;
}

.card-header {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: #fff;
  padding: 30rpx;
}

.card-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.card-date {
  font-size: 24rpx;
  opacity: 0.8;
}

.summary-stats {
  display: flex;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.stat-item {
  flex: 1;
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.stat-label {
  display: block;
  font-size: 22rpx;
  color: #999;
}

.content-section,
.reference-section {
  padding: 0 30rpx 30rpx;
}

.section-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 15rpx;
}

.content-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
  white-space: pre-wrap;
}

.reference-text {
  font-size: 24rpx;
  color: #1890ff;
}

.history-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.section-header {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 10rpx;
}

.history-title {
  font-size: 26rpx;
  color: #333;
}

.history-date {
  font-size: 22rpx;
  color: #999;
}
</style>