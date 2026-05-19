<template>
  <view class="container">
    <view class="hospital-header">
      <view class="header-info">
        <text class="hospital-name">{{ hospital.name }}</text>
        <view class="rating-row">
          <text class="rating-score">{{ hospital.averageRating }}</text>
          <text class="rating-stars">⭐⭐⭐⭐⭐</text>
          <text class="rating-count">({{ hospital.ratingCount }}人评价)</text>
        </view>
        <view class="hospital-tags">
          <text class="tag emergency" v-if="hospital.isEmergency">急诊</text>
          <text class="tag h24" v-if="hospital.is24h">24小时</text>
        </view>
      </view>
    </view>

    <view class="info-section">
      <view class="info-item">
        <text class="info-icon">📍</text>
        <view class="info-content">
          <text class="info-label">地址</text>
          <text class="info-text">{{ hospital.address }}</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-icon">📞</text>
        <view class="info-content">
          <text class="info-label">电话</text>
          <text class="info-text phone">{{ hospital.phone }}</text>
        </view>
        <button class="call-btn" @click="makeCall">拨打</button>
      </view>
      <view class="info-item">
        <text class="info-icon">⏰</text>
        <view class="info-content">
          <text class="info-label">营业时间</text>
          <text class="info-text">{{ hospital.businessHours }}</text>
        </view>
      </view>
      <view class="info-item">
        <text class="info-icon">🛠️</text>
        <view class="info-content">
          <text class="info-label">服务项目</text>
          <text class="info-text">{{ hospital.services }}</text>
        </view>
      </view>
    </view>

    <view class="rating-section">
      <view class="section-header">
        <text class="section-title">用户评价</text>
        <text class="section-more">查看全部 →</text>
      </view>
      <view class="rating-list">
        <view class="rating-item" v-for="rating in ratings" :key="rating.id">
          <view class="rating-user">
            <text class="user-avatar">👤</text>
            <text class="user-name">{{ rating.userName }}</text>
            <view class="user-rating">
              <text class="stars" v-for="i in 5" :key="i">⭐</text>
            </view>
          </view>
          <text class="rating-comment">{{ rating.comment }}</text>
          <text class="rating-time">{{ rating.time }}</text>
        </view>
      </view>
    </view>

    <view class="bottom-actions">
      <button class="action-btn primary" @click="makeCall">
        <text class="btn-icon">📞</text>
        <text class="btn-text">电话咨询</text>
      </button>
      <button class="action-btn secondary" @click="goToNavigation">
        <text class="btn-icon">🧭</text>
        <text class="btn-text">导航前往</text>
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hospital: {
        name: '',
        address: '',
        phone: '',
        businessHours: '',
        services: '',
        averageRating: 0,
        ratingCount: 0,
        isEmergency: false,
        is24h: false
      },
      ratings: []
    }
  },
  onLoad(options) {
    this.loadHospitalDetail(options.id)
  },
  methods: {
    loadHospitalDetail(id) {
      this.$request('/hospital/' + id).then(res => {
        if (res.code === 200) {
          this.hospital = res.data
        }
      }).catch(() => {
        this.hospital = {
          id: id,
          name: '阳光宠物医院',
          address: '北京市朝阳区建国路88号',
          phone: '010-88888888',
          businessHours: '周一至周日 9:00-21:00',
          services: '常规体检,疫苗接种,外科手术,急诊,牙科',
          averageRating: 4.8,
          ratingCount: 156,
          isEmergency: true,
          is24h: false
        }

        this.ratings = [
          { id: 1, userName: '爱宠人士', score: 5, comment: '医生很专业，环境也很干净，推荐！', time: '2天前' },
          { id: 2, userName: '铲屎官小王', score: 5, comment: '24小时急诊很方便，半夜狗狗不舒服及时得到了治疗', time: '1周前' },
          { id: 3, userName: '猫咪家长', score: 4, comment: '价格合理，服务态度好', time: '2周前' }
        ]
      })
    },
    makeCall() {
      uni.makePhoneCall({
        phoneNumber: this.hospital.phone,
        fail: () => {
          this.$showToast('模拟呼叫: ' + this.hospital.phone)
        }
      })
    },
    goToNavigation() {
      this.$showToast('导航功能开发中')
    }
  }
}
</script>

<style scoped>
.hospital-header {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
  padding: 40rpx 30rpx;
  margin: -20rpx -20rpx 20rpx;
  border-radius: 0 0 30rpx 30rpx;
}

.hospital-name {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 15rpx;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 10rpx;
  margin-bottom: 15rpx;
}

.rating-score {
  font-size: 32rpx;
  font-weight: bold;
}

.rating-stars {
  font-size: 24rpx;
}

.rating-count {
  font-size: 22rpx;
  opacity: 0.8;
}

.hospital-tags {
  display: flex;
  gap: 10rpx;
}

.tag {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  background: rgba(255,255,255,0.2);
}

.info-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 10rpx 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.info-item {
  display: flex;
  align-items: center;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 50rpx;
}

.info-content {
  flex: 1;
}

.info-label {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.info-text {
  font-size: 26rpx;
  color: #333;
}

.info-text.phone {
  color: #1890ff;
  font-weight: bold;
}

.call-btn {
  padding: 12rpx 25rpx;
  background: linear-gradient(135deg, #52c41a 0%, #38ef7d 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
}

.rating-section {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 120rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 24rpx;
  color: #1890ff;
}

.rating-list {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.rating-item {
  padding-bottom: 25rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.rating-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.rating-user {
  display: flex;
  align-items: center;
  gap: 15rpx;
  margin-bottom: 15rpx;
}

.user-avatar {
  font-size: 30rpx;
}

.user-name {
  font-size: 26rpx;
  font-weight: bold;
  color: #333;
}

.user-rating {
  margin-left: auto;
}

.stars {
  font-size: 20rpx;
}

.rating-comment {
  display: block;
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 10rpx;
}

.rating-time {
  font-size: 22rpx;
  color: #999;
}

.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 20rpx;
  padding: 20rpx 30rpx;
  background: #fff;
  border-top: 1rpx solid #f0f0f0;
}

.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10rpx;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.action-btn.primary {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  color: #fff;
}

.action-btn.secondary {
  background: #f0f5ff;
  color: #1890ff;
}

.btn-icon {
  font-size: 32rpx;
}
</style>