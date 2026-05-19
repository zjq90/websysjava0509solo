<template>
  <view class="container">
    <view class="filter-bar">
      <view class="filter-item" :class="{ active: filter === 'all' }" @click="setFilter('all')">全部</view>
      <view class="filter-item" :class="{ active: filter === 'emergency' }" @click="setFilter('emergency')">急诊</view>
      <view class="filter-item" :class="{ active: filter === '24h' }" @click="setFilter('24h')">24小时</view>
    </view>

    <view class="hospital-list">
      <view class="hospital-card" v-for="hospital in hospitals" :key="hospital.id" @click="goToDetail(hospital.id)">
        <view class="card-header">
          <view class="hospital-info">
            <text class="hospital-name">{{ hospital.name }}</text>
            <view class="hospital-tags">
              <text class="tag emergency" v-if="hospital.isEmergency">急诊</text>
              <text class="tag h24" v-if="hospital.is24h">24小时</text>
            </view>
          </view>
          <view class="hospital-rating">
            <text class="rating-score">{{ hospital.averageRating }}</text>
            <text class="rating-stars">⭐</text>
          </view>
        </view>
        <view class="card-body">
          <view class="info-row">
            <text class="info-icon">📍</text>
            <text class="info-text">{{ hospital.address }}</text>
          </view>
          <view class="info-row">
            <text class="info-icon">⏰</text>
            <text class="info-text">{{ hospital.businessHours }}</text>
          </view>
          <view class="info-row">
            <text class="info-icon">📞</text>
            <text class="info-text">{{ hospital.phone }}</text>
          </view>
        </view>
        <view class="card-footer">
          <text class="rating-count">{{ hospital.ratingCount }} 人评价</text>
          <text class="distance" v-if="hospital.distance">约{{ hospital.distance }}km</text>
        </view>
      </view>
    </view>

    <view class="emergency-tip">
      <text class="tip-icon">🚨</text>
      <view class="tip-content">
        <text class="tip-title">宠物紧急情况？</text>
        <text class="tip-desc">建议立即前往24小时宠物医院就诊</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      filter: 'all',
      hospitals: []
    }
  },
  onLoad() {
    this.loadHospitals()
  },
  methods: {
    setFilter(filter) {
      this.filter = filter
      this.loadHospitals()
    },
    loadHospitals() {
      let url = '/hospital/nearby?latitude=39.9042&longitude=116.4074&radius=10'
      if (this.filter === 'emergency') {
        url = '/hospital/emergency'
      }
      
      this.$request(url).then(res => {
        if (res.code === 200) {
          this.hospitals = res.data
        }
      }).catch(() => {
        this.hospitals = [
          {
            id: 1,
            name: '阳光宠物医院',
            address: '北京市朝阳区建国路88号',
            phone: '010-88888888',
            businessHours: '周一至周日 9:00-21:00',
            averageRating: 4.8,
            ratingCount: 156,
            isEmergency: true,
            is24h: false,
            distance: 1.2
          },
          {
            id: 2,
            name: '爱心宠物诊所',
            address: '北京市海淀区中关村大街1号',
            phone: '010-66666666',
            businessHours: '周一至周日 8:00-22:00',
            averageRating: 4.6,
            ratingCount: 89,
            isEmergency: true,
            is24h: true,
            distance: 2.5
          },
          {
            id: 3,
            name: '宠爱动物医院',
            address: '北京市西城区金融街2号',
            phone: '010-77777777',
            businessHours: '周一至周日 9:00-20:00',
            averageRating: 4.9,
            ratingCount: 234,
            isEmergency: false,
            is24h: false,
            distance: 3.1
          }
        ]

        if (this.filter === 'emergency') {
          this.hospitals = this.hospitals.filter(h => h.isEmergency)
        } else if (this.filter === '24h') {
          this.hospitals = this.hospitals.filter(h => h.is24h)
        }
      })
    },
    goToDetail(id) {
      uni.navigateTo({ url: '/pages/hospital/detail?id=' + id })
    }
  }
}
</script>

<style scoped>
.filter-bar {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.filter-item {
  flex: 1;
  text-align: center;
  padding: 20rpx;
  background: #fff;
  border-radius: 12rpx;
  font-size: 26rpx;
  color: #666;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.filter-item.active {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
}

.hospital-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.hospital-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 30rpx;
  background: linear-gradient(135deg, #1890ff10 0%, #096dd910 100%);
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.hospital-tags {
  display: flex;
  gap: 10rpx;
}

.tag {
  padding: 6rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
}

.tag.emergency {
  background: #fff1f0;
  color: #f5222d;
}

.tag.h24 {
  background: #f6ffed;
  color: #52c41a;
}

.hospital-rating {
  text-align: right;
}

.rating-score {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fa8c16;
}

.rating-stars {
  font-size: 24rpx;
}

.card-body {
  padding: 20rpx 30rpx;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.info-icon {
  font-size: 28rpx;
  margin-right: 15rpx;
  width: 40rpx;
}

.info-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #f0f0f0;
}

.rating-count {
  font-size: 24rpx;
  color: #999;
}

.distance {
  font-size: 24rpx;
  color: #1890ff;
  font-weight: bold;
}

.emergency-tip {
  display: flex;
  align-items: center;
  background: #fff1f0;
  border-radius: 16rpx;
  padding: 30rpx;
}

.tip-icon {
  font-size: 50rpx;
  margin-right: 20rpx;
}

.tip-content {
  flex: 1;
}

.tip-title {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #f5222d;
  margin-bottom: 8rpx;
}

.tip-desc {
  font-size: 24rpx;
  color: #cf1322;
}
</style>