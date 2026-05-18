<template>
  <view class="container">
    <view class="banner">
      <view class="banner-content">
        <text class="banner-title">砍价免费拿</text>
        <text class="banner-desc">邀请好友助力，砍到0元免费拿</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">热门砍价</text>
      </view>
      <view class="bargain-list">
        <view class="bargain-item" v-for="item in bargainList" :key="item.id" @click="goDetail(item)">
          <image :src="item.image" class="bargain-image" mode="aspectFill" />
          <view class="bargain-info">
            <text class="bargain-title">{{ item.title }}</text>
            <view class="price-row">
              <text class="current-price">¥{{ item.currentPrice }}</text>
              <text class="original-price">¥{{ item.originalPrice }}</text>
            </view>
            <view class="progress-wrap">
              <view class="progress-bar">
                <view class="progress" :style="{ width: item.progress + '%' }"></view>
              </view>
              <text class="progress-text">已砍{{ item.progress }}%</text>
            </view>
          </view>
          <button class="bargain-btn" @click.stop="startBargain(item)">我要砍</button>
        </view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">我的砍价</text>
      </view>
      <view class="my-bargain-list" v-if="myBargains.length > 0">
        <view class="my-bargain-item" v-for="item in myBargains" :key="item.id">
          <view class="bargain-status" :class="item.status">
            {{ item.status === 'success' ? '砍价成功' : '砍价中' }}
          </view>
          <text class="bargain-title">{{ item.title }}</text>
          <view class="bargain-price">
            <text>已砍 ¥{{ item.bargained }}</text>
            <text>剩余 ¥{{ item.remaining }}</text>
          </view>
          <button class="share-btn" @click="shareBargain(item)">邀请好友</button>
        </view>
      </view>
      <view class="empty-state" v-else>
        <text class="empty-icon">💰</text>
        <text class="empty-text">暂无砍价记录</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      bargainList: [
        {
          id: 1,
          title: 'AirPods Pro 2代 主动降噪耳机',
          image: 'https://via.placeholder.com/200x200?text=AirPods',
          currentPrice: 1299,
          originalPrice: 1899,
          progress: 32
        },
        {
          id: 2,
          title: '小米手环8 Pro 智能手表',
          image: 'https://via.placeholder.com/200x200?text=Band',
          currentPrice: 299,
          originalPrice: 399,
          progress: 75
        },
        {
          id: 3,
          title: 'SK-II神仙水护肤精华露',
          image: 'https://via.placeholder.com/200x200?text=SKII',
          currentPrice: 899,
          originalPrice: 1590,
          progress: 45
        }
      ],
      myBargains: []
    }
  },
  methods: {
    goDetail(item) {
      uni.showToast({
        title: '查看砍价详情',
        icon: 'none'
      })
    },
    startBargain(item) {
      uni.showModal({
        title: '发起砍价',
        content: `确定发起"${item.title}"砍价吗？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '砍价发起成功',
              icon: 'success'
            })
            const bargained = (item.originalPrice - item.currentPrice) * (item.progress / 100)
            const remaining = item.currentPrice - bargained
            this.myBargains.unshift({
              id: Date.now(),
              title: item.title,
              status: 'pending',
              bargained: bargained.toFixed(0),
              remaining: remaining.toFixed(0)
            })
          }
        }
      })
    },
    shareBargain(item) {
      uni.showToast({
        title: '分享功能开发中',
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
  background: linear-gradient(135deg, #a855f7 0%, #7c3aed 100%);
}

.banner-content {
  height: 240rpx;
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
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.bargain-list {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.bargain-item {
  display: flex;
  align-items: center;
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.bargain-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.bargain-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.bargain-info {
  flex: 1;
  margin: 0 20rpx;
}

.bargain-title {
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

.current-price {
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

.progress-wrap {
  position: relative;
}

.progress-bar {
  height: 12rpx;
  background: #f0f0f0;
  border-radius: 6rpx;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(90deg, #a855f7 0%, #7c3aed 100%);
  border-radius: 6rpx;
}

.progress-text {
  font-size: 20rpx;
  color: #999;
  position: absolute;
  right: 0;
  top: -30rpx;
}

.bargain-btn {
  width: 140rpx;
  height: 60rpx;
  line-height: 60rpx;
  background: linear-gradient(135deg, #a855f7 0%, #7c3aed 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 24rpx;
  border: none;
  flex-shrink: 0;
}

.my-bargain-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.my-bargain-item {
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}

.bargain-status {
  display: inline-block;
  padding: 6rpx 16rpx;
  background: #f6f0ff;
  color: #7c3aed;
  font-size: 22rpx;
  border-radius: 8rpx;
  margin-bottom: 15rpx;
}

.bargain-status.success {
  background: #f6ffed;
  color: #52c41a;
}

.bargain-title {
  display: block;
  font-size: 26rpx;
  color: #333;
  margin-bottom: 15rpx;
}

.bargain-price {
  display: flex;
  justify-content: space-between;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.share-btn {
  width: 100%;
  height: 60rpx;
  line-height: 60rpx;
  background: linear-gradient(135deg, #a855f7 0%, #7c3aed 100%);
  color: #fff;
  border-radius: 30rpx;
  font-size: 26rpx;
  border: none;
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
