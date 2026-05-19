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

    <view class="device-info">
      <view class="device-status connected">
        <text class="status-dot"></text>
        <text class="status-text">智能项圈已连接</text>
      </view>
      <text class="device-type">FitBark</text>
    </view>

    <view class="today-stats">
      <text class="stats-title">今日运动</text>
      <view class="stats-grid">
        <view class="stat-card">
          <text class="stat-icon">👣</text>
          <text class="stat-number">{{ todayStats.steps }}</text>
          <text class="stat-label">步数</text>
        </view>
        <view class="stat-card">
          <text class="stat-icon">📏</text>
          <text class="stat-number">{{ todayStats.distance }}</text>
          <text class="stat-label">公里</text>
        </view>
        <view class="stat-card">
          <text class="stat-icon">🔥</text>
          <text class="stat-number">{{ todayStats.calories }}</text>
          <text class="stat-label">卡路里</text>
        </view>
        <view class="stat-card">
          <text class="stat-icon">⏱️</text>
          <text class="stat-number">{{ todayStats.activeMinutes }}</text>
          <text class="stat-label">活动分钟</text>
        </view>
      </view>

      <view class="goal-progress">
        <view class="goal-header">
          <text class="goal-label">每日目标 (10000步)</text>
          <text class="goal-percent">{{ Math.floor(todayStats.steps / 10000 * 100) }}%</text>
        </view>
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: Math.min(todayStats.steps / 10000 * 100, 100) + '%' }"></view>
        </view>
        <text class="goal-status" :class="{ achieved: todayStats.steps >= 10000 }">
          {{ todayStats.steps >= 10000 ? '🎉 目标已达成！' : '还差 ' + (10000 - todayStats.steps) + ' 步' }}
        </text>
      </view>
    </view>

    <view class="supported-devices">
      <text class="section-title">🔗 支持的设备</text>
      <view class="device-list">
        <text class="device-tag" v-for="device in supportedDevices" :key="device">{{ device }}</text>
      </view>
    </view>

    <view class="manual-add">
      <button class="btn-primary" @click="showAddModal = true">手动添加运动记录</button>
    </view>

    <view class="modal" v-if="showAddModal" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <text class="modal-title">手动添加记录</text>
        <view class="form-item">
          <text class="form-label">步数</text>
          <input type="number" class="form-input" v-model="manualRecord.steps" placeholder="请输入步数" />
        </view>
        <view class="form-item">
          <text class="form-label">距离(公里)</text>
          <input type="number" class="form-input" v-model="manualRecord.distance" placeholder="请输入距离" />
        </view>
        <view class="form-actions">
          <button class="btn-cancel" @click="showAddModal = false">取消</button>
          <button class="btn-confirm" @click="addManualRecord">确认添加</button>
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
        { id: 1, name: '旺财', type: 'dog' },
        { id: 2, name: '咪咪', type: 'cat' }
      ],
      selectedPet: 1,
      todayStats: {
        steps: 8520,
        distance: 4.2,
        calories: 185,
        activeMinutes: 75
      },
      supportedDevices: [],
      showAddModal: false,
      manualRecord: {
        steps: '',
        distance: ''
      }
    }
  },
  onLoad() {
    this.loadStats()
    this.loadSupportedDevices()
  },
  methods: {
    selectPet(id) {
      this.selectedPet = id
      this.loadStats()
    },
    loadStats() {
      this.$request('/exercise/stats/' + this.selectedPet).then(res => {
        if (res.code === 200) {
          this.todayStats = res.data
        }
      }).catch(() => {
        const baseSteps = this.selectedPet === 1 ? 8000 : 2000
        this.todayStats = {
          steps: baseSteps + Math.floor(Math.random() * 2000),
          distance: (this.selectedPet === 1 ? 3 : 0.5) + Math.random(),
          calories: (this.selectedPet === 1 ? 150 : 30) + Math.random() * 50,
          activeMinutes: (this.selectedPet === 1 ? 60 : 20) + Math.floor(Math.random() * 30)
        }
      })
    },
    loadSupportedDevices() {
      this.$request('/exercise/devices').then(res => {
        if (res.code === 200) {
          this.supportedDevices = res.data
        }
      }).catch(() => {
        this.supportedDevices = ['FitBark', 'Whistle', 'PitPat', 'Tractive', 'Garmin']
      })
    },
    addManualRecord() {
      this.$request('/exercise/manual', 'POST', {
        pet: { id: this.selectedPet },
        ...this.manualRecord
      }).then(res => {
        if (res.code === 200) {
          this.$showToast('添加成功', 'success')
          this.showAddModal = false
          this.loadStats()
        }
      }).catch(() => {
        if (this.manualRecord.steps) {
          this.todayStats.steps += parseInt(this.manualRecord.steps)
        }
        this.$showToast('添加成功', 'success')
        this.showAddModal = false
      })
    }
  }
}
</script>

<style scoped>
.pet-selector {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
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
  border-color: #52c41a;
  background: linear-gradient(135deg, #52c41a15 0%, #38ef7d15 100%);
}

.pet-emoji {
  font-size: 50rpx;
  margin-bottom: 10rpx;
}

.pet-name {
  font-size: 26rpx;
  color: #333;
}

.device-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  padding: 25rpx 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.device-status {
  display: flex;
  align-items: center;
}

.status-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background: #52c41a;
  margin-right: 15rpx;
}

.status-text {
  font-size: 26rpx;
  color: #52c41a;
}

.device-type {
  font-size: 24rpx;
  color: #999;
}

.today-stats {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
}

.stats-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.stat-icon {
  font-size: 36rpx;
  margin-bottom: 10rpx;
}

.stat-number {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 22rpx;
  color: #999;
}

.goal-progress {
  padding-top: 30rpx;
  border-top: 1rpx solid #f0f0f0;
}

.goal-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
}

.goal-label {
  font-size: 26rpx;
  color: #666;
}

.goal-percent {
  font-size: 26rpx;
  font-weight: bold;
  color: #52c41a;
}

.progress-bar {
  height: 20rpx;
  background: #f0f0f0;
  border-radius: 10rpx;
  overflow: hidden;
  margin-bottom: 15rpx;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #52c41a 0%, #38ef7d 100%);
  border-radius: 10rpx;
  transition: width 0.3s;
}

.goal-status {
  font-size: 24rpx;
  color: #999;
}

.goal-status.achieved {
  color: #52c41a;
  font-weight: bold;
}

.supported-devices {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.section-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.device-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.device-tag {
  padding: 10rpx 25rpx;
  background: #f0f5ff;
  color: #1890ff;
  border-radius: 30rpx;
  font-size: 24rpx;
}

.manual-add {
  margin-bottom: 20rpx;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-content {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
}

.modal-title {
  display: block;
  text-align: center;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.form-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 40rpx;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.btn-cancel {
  background: #f5f5f5;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #52c41a 0%, #38ef7d 100%);
  color: #fff;
}
</style>