<template>
  <view class="reviews-container">
    <view class="stats-section">
      <view class="stat-item">
        <text class="stat-value">{{ stats.total }}</text>
        <text class="stat-label">全部评价</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.good }}</text>
        <text class="stat-label">好评</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.medium }}</text>
        <text class="stat-label">中评</text>
      </view>
      <view class="stat-item">
        <text class="stat-value">{{ stats.bad }}</text>
        <text class="stat-label">差评</text>
      </view>
    </view>

    <view class="tabs">
      <view 
        class="tab" 
        :class="{ active: activeTab === item.value }"
        v-for="item in tabList" 
        :key="item.value"
        @click="activeTab = item.value"
      >
        {{ item.label }}
      </view>
    </view>

    <view class="review-list">
      <view 
        class="review-item" 
        v-for="review in filteredReviews" 
        :key="review.id"
      >
        <view class="review-header">
          <image class="product-img" :src="review.productImage" mode="aspectFill"></image>
          <view class="product-info">
            <text class="product-name">{{ review.productName }}</text>
            <view class="rating">
              <text 
                class="star" 
                :class="{ filled: idx < review.rating }"
                v-for="idx in 5" 
                :key="idx"
              >★</text>
            </view>
          </view>
          <view class="review-time">{{ review.createTime }}</view>
        </view>
        
        <view class="review-content">
          <text class="review-text">{{ review.content }}</text>
        </view>

        <view class="review-images" v-if="review.images && review.images.length > 0">
          <image 
            class="review-img" 
            :src="img" 
            mode="aspectFill"
            v-for="(img, idx) in review.images" 
            :key="idx"
          ></image>
        </view>

        <view class="reply-section" v-if="review.reply">
          <view class="reply-label">卖家回复</view>
          <text class="reply-text">{{ review.reply }}</text>
        </view>

        <view class="review-actions">
          <button class="action-btn" @click="appendReview(review)">追加评价</button>
        </view>
      </view>
    </view>

    <view class="empty-state" v-if="filteredReviews.length === 0">
      <text class="empty-icon">📝</text>
      <text class="empty-text">暂无评价</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'all',
      tabList: [
        { label: '全部', value: 'all' },
        { label: '好评', value: 'good' },
        { label: '中评', value: 'medium' },
        { label: '差评', value: 'bad' }
      ],
      stats: {
        total: 15,
        good: 12,
        medium: 2,
        bad: 1
      },
      reviews: [
        {
          id: 1,
          productName: 'iPhone 13 128G 蓝色',
          productImage: 'https://picsum.photos/100/100?random=30',
          rating: 5,
          content: '商品成色非常好，和描述一致，卖家发货也很快，包装很严实，非常满意的一次购物！',
          images: ['https://picsum.photos/150/150?random=31', 'https://picsum.photos/150/150?random=32'],
          createTime: '2024-01-12',
          type: 'good',
          reply: '感谢您的好评！欢迎再次光临~'
        },
        {
          id: 2,
          productName: 'MacBook Pro 14寸 M2',
          productImage: 'https://picsum.photos/100/100?random=33',
          rating: 4,
          content: '电脑整体不错，就是电池循环次数比描述的稍高一些，不过不影响使用。',
          images: [],
          createTime: '2024-01-10',
          type: 'medium'
        },
        {
          id: 3,
          productName: 'AirPods Pro 2代',
          productImage: 'https://picsum.photos/100/100?random=34',
          rating: 5,
          content: '全新未拆封，正品无疑，价格比官网便宜很多，性价比很高！',
          images: ['https://picsum.photos/150/150?random=35'],
          createTime: '2024-01-08',
          type: 'good'
        }
      ]
    }
  },
  computed: {
    filteredReviews() {
      if (this.activeTab === 'all') {
        return this.reviews
      }
      return this.reviews.filter(r => r.type === this.activeTab)
    }
  },
  methods: {
    appendReview(review) {
      uni.showModal({
        title: '追加评价',
        editable: true,
        placeholderText: '请输入追加评价内容...',
        success: (res) => {
          if (res.confirm && res.content) {
            uni.showToast({
              title: '评价成功',
              icon: 'success'
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.reviews-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.stats-section {
  display: flex;
  background-color: #fff;
  padding: 30rpx 0;
  margin-bottom: 20rpx;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 36rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

.tabs {
  display: flex;
  background-color: #fff;
  padding: 0 20rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.tab {
  flex: 1;
  text-align: center;
  padding: 25rpx 0;
  font-size: 28rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab.active {
  color: #409EFF;
  border-bottom-color: #409EFF;
  font-weight: bold;
}

.review-list {
  padding: 20rpx;
}

.review-item {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.review-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.product-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 10rpx;
  background-color: #f5f5f5;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  margin-left: 20rpx;
}

.product-name {
  display: block;
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 10rpx;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.rating {
  display: flex;
  gap: 5rpx;
}

.star {
  font-size: 28rpx;
  color: #ddd;
}

.star.filled {
  color: #faad14;
}

.review-time {
  font-size: 24rpx;
  color: #999;
  flex-shrink: 0;
}

.review-content {
  margin-bottom: 20rpx;
}

.review-text {
  display: block;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.review-images {
  display: flex;
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.review-img {
  width: 150rpx;
  height: 150rpx;
  border-radius: 10rpx;
  background-color: #f5f5f5;
}

.reply-section {
  background-color: #f9f9f9;
  border-radius: 15rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.reply-label {
  font-size: 26rpx;
  color: #409EFF;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.reply-text {
  display: block;
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.review-actions {
  display: flex;
  justify-content: flex-end;
  padding-top: 20rpx;
  border-top: 1rpx solid #f5f5f5;
}

.action-btn {
  padding: 15rpx 40rpx;
  background-color: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 40rpx;
  font-size: 26rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #999;
}
</style>
