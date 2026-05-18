<template>
  <view class="community-container">
    <view class="header">
      <view class="tabs">
        <view 
          class="tab" 
          :class="{ active: activeTab === 'hot' }"
          @click="activeTab = 'hot'"
        >
          热门话题
        </view>
        <view 
          class="tab" 
          :class="{ active: activeTab === 'latest' }"
          @click="activeTab = 'latest'"
        >
          最新发布
        </view>
      </view>
      <view class="post-btn" @click="goToPost">
        <text class="icon">+</text>
        发布帖子
      </view>
    </view>

    <view class="topic-list">
      <view 
        class="topic-card" 
        v-for="topic in topics" 
        :key="topic.id"
        @click="goToDetail(topic.id)"
      >
        <view class="topic-header">
          <view class="user-info">
            <image class="avatar" :src="topic.avatar" mode="aspectFill"></image>
            <view class="user-detail">
              <text class="nickname">{{ topic.nickname }}</text>
              <text class="time">{{ topic.createTime }}</text>
            </view>
          </view>
          <view class="topic-tag" v-if="topic.tag">{{ topic.tag }}</view>
        </view>
        <view class="topic-content">
          <text class="title">{{ topic.title }}</text>
          <text class="desc">{{ topic.content }}</text>
        </view>
        <view class="topic-images" v-if="topic.images && topic.images.length > 0">
          <image 
            class="topic-img" 
            :src="img" 
            mode="aspectFill"
            v-for="(img, idx) in topic.images.slice(0, 3)" 
            :key="idx"
          ></image>
        </view>
        <view class="topic-stats">
          <view class="stat">
            <text class="stat-icon">👍</text>
            <text class="stat-num">{{ topic.likeCount }}</text>
          </view>
          <view class="stat">
            <text class="stat-icon">💬</text>
            <text class="stat-num">{{ topic.commentCount }}</text>
          </view>
          <view class="stat">
            <text class="stat-icon">👁</text>
            <text class="stat-num">{{ topic.viewCount }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeTab: 'hot',
      topics: [
        {
          id: 1,
          nickname: '二手达人',
          avatar: 'https://picsum.photos/100/100?random=1',
          createTime: '2小时前',
          title: '如何判断二手手机的真实成色？',
          content: '分享一下我多年买卖二手手机的经验，教大家如何快速判断手机的真实成色，避免被坑...',
          tag: '经验分享',
          images: ['https://picsum.photos/200/200?random=10'],
          likeCount: 128,
          commentCount: 36,
          viewCount: 1520
        },
        {
          id: 2,
          nickname: '环保小卫士',
          avatar: 'https://picsum.photos/100/100?random=2',
          createTime: '5小时前',
          title: '闲置物品循环利用，从我做起！',
          content: '最近整理家里，发现好多闲置物品都可以二手转让，既环保又能回血，大家一起行动起来...',
          tag: '话题讨论',
          likeCount: 89,
          commentCount: 24,
          viewCount: 890
        },
        {
          id: 3,
          nickname: '数码发烧友',
          avatar: 'https://picsum.photos/100/100?random=3',
          createTime: '1天前',
          title: '笔记本电脑二手交易注意事项',
          content: '出了几台笔记本，总结了一些二手笔记本交易的注意事项，包括验机、物流、售后等方面...',
          tag: '经验分享',
          images: ['https://picsum.photos/200/200?random=11', 'https://picsum.photos/200/200?random=12'],
          likeCount: 256,
          commentCount: 78,
          viewCount: 3200
        }
      ]
    }
  },
  methods: {
    goToPost() {
      uni.navigateTo({
        url: '/pages/community/post'
      })
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/community/detail?id=${id}`
      })
    }
  }
}
</script>

<style scoped>
.community-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.header {
  background-color: #fff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.tabs {
  display: flex;
  margin-bottom: 20rpx;
}

.tab {
  padding: 15rpx 40rpx;
  font-size: 32rpx;
  color: #666;
  border-bottom: 4rpx solid transparent;
}

.tab.active {
  color: #409EFF;
  border-bottom-color: #409EFF;
  font-weight: bold;
}

.post-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  padding: 20rpx;
  border-radius: 50rpx;
  font-size: 30rpx;
  font-weight: bold;
}

.post-btn .icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.topic-list {
  padding: 20rpx;
}

.topic-card {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.topic-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-detail {
  display: flex;
  flex-direction: column;
}

.nickname {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 8rpx;
}

.time {
  font-size: 24rpx;
  color: #999;
}

.topic-tag {
  background-color: #ECF5FF;
  color: #409EFF;
  padding: 8rpx 20rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}

.topic-content {
  margin-bottom: 20rpx;
}

.title {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 15rpx;
  line-height: 1.4;
}

.desc {
  display: block;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.topic-images {
  display: flex;
  gap: 15rpx;
  margin-bottom: 20rpx;
}

.topic-img {
  width: 200rpx;
  height: 200rpx;
  border-radius: 15rpx;
  background-color: #f5f5f5;
}

.topic-stats {
  display: flex;
  gap: 50rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #eee;
}

.stat {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.stat-icon {
  font-size: 28rpx;
}

.stat-num {
  font-size: 26rpx;
  color: #999;
}
</style>
