<template>
  <view class="container">
    <view class="banner">
      <swiper :indicator-dots="true" :autoplay="true">
        <swiper-item>
          <view class="banner-item">
            <text class="banner-title">拼团特惠</text>
            <text class="banner-desc">3人成团，价格更优惠</text>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">正在拼团</text>
        <text class="section-more" @click="loadMore">更多</text>
      </view>
      <view class="activity-list">
        <view class="activity-item" v-for="item in activityList" :key="item.id" @click="goDetail(item)">
          <image :src="item.image" class="activity-image" mode="aspectFill" />
          <view class="activity-info">
            <text class="activity-title">{{ item.title }}</text>
            <view class="price-row">
              <text class="group-price">¥{{ item.groupPrice }}</text>
              <text class="original-price">¥{{ item.originalPrice }}</text>
            </view>
            <view class="progress-bar">
              <view class="progress" :style="{ width: (item.joined / item.total * 100) + '%' }"></view>
              <text class="progress-text">已拼{{ item.joined }}/{{ item.total }}人</text>
            </view>
          </view>
          <button class="join-btn" @click.stop="joinGroup(item)">去拼团</button>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">我的拼团</text>
      </view>
      <view class="my-group-list" v-if="myGroups.length > 0">
        <view class="my-group-item" v-for="item in myGroups" :key="item.id">
          <view class="group-status" :class="item.status">{{ item.status === 'success' ? '拼团成功' : '拼团中' }}</view>
          <text class="group-title">{{ item.title }}</text>
          <text class="group-time">创建时间：{{ item.createTime }}</text>
        </view>
      </view>
      <view class="empty-state" v-else>
        <text class="empty-icon">👥</text>
        <text class="empty-text">暂无拼团记录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activityList: [
        {
          id: 1,
          title: 'iPhone 15 Pro 256G 深空黑',
          image: 'https://via.placeholder.com/200x200?text=iPhone',
          groupPrice: 7999,
          originalPrice: 8999,
          joined: 2,
          total: 3
        },
        {
          id: 2,
          title: 'AirPods Pro 2代 无线耳机',
          image: 'https://via.placeholder.com/200x200?text=AirPods',
          groupPrice: 1599,
          originalPrice: 1899,
          joined: 1,
          total: 3
        },
        {
          id: 3,
          title: '小米14 Ultra 摄影旗舰',
          image: 'https://via.placeholder.com/200x200?text=Xiaomi',
          groupPrice: 5999,
          originalPrice: 6499,
          joined: 3,
          total: 3
        }
      ],
      myGroups: []
    }
  },
  methods: {
    goDetail(item) {
      uni.showToast({
        title: '查看拼团详情',
        icon: 'none'
      })
    },
    joinGroup(item) {
      uni.showModal({
        title: '参与拼团',
        content: `确定参与"${item.title}"拼团吗？成团价¥${item.groupPrice}`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '参与成功',
              icon: 'success'
            })
            this.myGroups.unshift({
              id: Date.now(),
              title: item.title,
              status: 'pending',
              createTime: new Date().toLocaleString()
            })
          }
        }
      })
    },
    loadMore() {
      uni.showToast({
        title: '加载更多',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 30rpx;
}

.banner {
  margin: 20rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.banner-item {
  height: 240rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.banner-title {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.banner-desc {
  font-size: 26rpx;
  color: rgba(255,255,255,0.8);
}

.section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 24rpx;
  color: #999;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.activity-item {
  display: flex;
  align-items: center;
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.activity-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.activity-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.activity-info {
  flex: 1;
  margin: 0 20rpx;
}

.activity-title {
  font-size: 28rpx;
  color: #333;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  line-height: 1.4;
  margin-bottom: 10rpx;
}

.price-row {
  display: flex;
  align-items: baseline;
  margin-bottom: 15rpx;
}

.group-price {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.original-price {
  font-size: 22rpx;
  color: #999;
  text-decoration: line-through;
  margin-left: 15rpx;
}

.progress-bar {
  position: relative;
  height: 12rpx;
  background: #f0f0f0;
  border-radius: 6rpx;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(90deg, #ff6b6b 0%, #ff4757 100%);
  border-radius: 6rpx;
}

.progress-text {
  position: absolute;
  right: 0;
  top: -30rpx;
  font-size: 20rpx;
  color: #999;
}

.join-btn {
  width: 140rpx;
  height: 60rpx;
  line-height: 60rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
  flex-shrink: 0;
}

.my-group-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.my-group-item {
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.group-status {
  display: inline-block;
  padding: 6rpx 16rpx;
  background: #e6f7ff;
  color: #1890ff;
  font-size: 22rpx;
  border-radius: 8rpx;
  margin-bottom: 15rpx;
}

.group-status.success {
  background: #f6ffed;
  color: #52c41a;
}

.group-title {
  display: block;
  font-size: 26rpx;
  color: #333;
  margin-bottom: 10rpx;
}

.group-time {
  font-size: 22rpx;
  color: #999;
}

.empty-state {
  padding: 80rpx 0;
  text-align: center;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
