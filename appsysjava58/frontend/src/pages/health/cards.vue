<template>
  <view class="container">
    <view class="health-cards">
      <view class="health-card" @click="goToDewormingHistory">
        <view class="card-header deworming">
          <text class="card-icon">💊</text>
          <text class="card-title">驱虫管理</text>
        </view>
        <view class="card-body">
          <view class="countdown">
            <text class="countdown-label">下次驱虫</text>
            <text class="countdown-days" :class="'status-' + dewormingData.status">
              {{ dewormingData.daysRemaining > 0 ? dewormingData.daysRemaining + '天' : '已到期' }}
            </text>
          </view>
          <view class="last-record" v-if="dewormingData.lastRecord">
            <text class="record-label">上次:</text>
            <text class="record-value">{{ dewormingData.lastRecord.medicineName }} - {{ dewormingData.lastRecord.date }}</text>
          </view>
          <view class="check-indicator" v-if="dewormingData.completed">
            ✅ 已完成
          </view>
        </view>
      </view>

      <view class="health-card">
        <view class="card-header checkup">
          <text class="card-icon">🏥</text>
          <text class="card-title">体检管理</text>
        </view>
        <view class="card-body">
          <view class="countdown">
            <text class="countdown-label">下次体检</text>
            <text class="countdown-days status-pending">45天</text>
          </view>
          <view class="last-record">
            <text class="record-label">上次:</text>
            <text class="record-value">常规体检 - 15天前</text>
          </view>
        </view>
      </view>
    </view>

    <view class="quick-tips">
      <text class="tips-title">💡 健康小贴士</text>
      <view class="tips-list">
        <text class="tip-item">• 幼犬每月驱虫一次，成犬每3个月一次</text>
        <text class="tip-item">• 定期体检可以早发现潜在健康问题</text>
        <text class="tip-item">• 疫苗接种后一周内注意观察宠物状态</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      dewormingData: {
        status: 'upcoming',
        daysRemaining: 20,
        completed: true,
        lastRecord: {
          date: '2024-01-15',
          medicineName: '福来恩滴剂'
        }
      }
    }
  },
  onLoad() {
    this.loadHealthData()
  },
  methods: {
    loadHealthData() {
      this.$request('/health/cards/1').then(res => {
        if (res.code === 200 && res.data.deworming) {
          const d = res.data.deworming
          this.dewormingData = {
            status: d.status,
            daysRemaining: d.daysRemaining || 0,
            completed: d.completed || false,
            lastRecord: d.lastRecord
          }
        }
      }).catch(() => {
        console.log('使用默认数据')
      })
    },
    goToDewormingHistory() {
      uni.navigateTo({ url: '/pages/health/deworming-history' })
    }
  }
}
</script>

<style scoped>
.health-cards {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
  margin-bottom: 40rpx;
}

.health-card {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  align-items: center;
  padding: 30rpx;
  color: #fff;
}

.card-header.deworming {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-header.checkup {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.card-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.card-title {
  font-size: 32rpx;
  font-weight: bold;
}

.card-body {
  padding: 30rpx;
}

.countdown {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.countdown-label {
  font-size: 28rpx;
  color: #666;
}

.countdown-days {
  font-size: 48rpx;
  font-weight: bold;
}

.last-record {
  display: flex;
  align-items: center;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
}

.record-label {
  font-size: 24rpx;
  color: #999;
  margin-right: 10rpx;
}

.record-value {
  font-size: 24rpx;
  color: #666;
  flex: 1;
}

.check-indicator {
  margin-top: 15rpx;
  padding: 10rpx 20rpx;
  background: #f6ffed;
  color: #52c41a;
  border-radius: 30rpx;
  font-size: 24rpx;
  display: inline-block;
}

.quick-tips {
  background: #fffbe6;
  border-radius: 16rpx;
  padding: 30rpx;
}

.tips-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #faad14;
  margin-bottom: 20rpx;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.tip-item {
  font-size: 24rpx;
  color: #8c6b00;
  line-height: 1.6;
}
</style>