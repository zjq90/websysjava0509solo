<template>
  <view class="container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索文物..." 
        v-model="keyword"
        @confirm="searchHeritage"
      />
      <view class="search-btn" @click="searchHeritage">搜索</view>
    </view>

    <!-- 分类导航 -->
    <scroll-view class="category-scroll" scroll-x="true" show-scrollbar="false">
      <view class="category-list">
        <view 
          class="category-item" 
          :class="{ active: currentCategory === null }"
          @click="selectCategory(null)"
        >
          <text>全部</text>
        </view>
        <view 
          class="category-item" 
          :class="{ active: currentCategory === item.id }"
          v-for="item in categories" 
          :key="item.id"
          @click="selectCategory(item.id)"
        >
          <text>{{ item.name }}</text>
        </view>
      </view>
    </scroll-view>

    <!-- 轮播图 -->
    <swiper class="banner" indicator-dots="true" autoplay="true" circular="true" interval="3000">
      <swiper-item v-for="(item, index) in banners" :key="index">
        <image class="banner-image" :src="item.image" mode="aspectFill"></image>
      </swiper-item>
    </swiper>

    <!-- 热门文物 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">热门文物</text>
        <text class="section-more" @click="goToMore">查看更多 ></text>
      </view>
      <scroll-view class="hot-scroll" scroll-x="true" show-scrollbar="false">
        <view class="hot-list">
          <view 
            class="hot-item" 
            v-for="item in hotList" 
            :key="item.id"
            @click="goToDetail(item.id)"
          >
            <image class="hot-image" :src="item.mainImage || '/static/default.png'" mode="aspectFill"></image>
            <text class="hot-name">{{ item.name }}</text>
            <text class="hot-period">{{ item.period }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 文物列表 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">全部文物</text>
      </view>
      <view class="heritage-list">
        <view 
          class="heritage-item card" 
          v-for="item in heritageList" 
          :key="item.id"
          @click="goToDetail(item.id)"
        >
          <image class="heritage-image" :src="item.mainImage || '/static/default.png'" mode="aspectFill"></image>
          <view class="heritage-info">
            <text class="heritage-name">{{ item.name }}</text>
            <text class="heritage-period">{{ item.period }}</text>
            <text class="heritage-desc">{{ item.description }}</text>
            <view class="heritage-stats">
              <text class="stat-item">👁 {{ item.viewCount }}</text>
              <text class="stat-item">❤ {{ item.favoriteCount }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 悬浮AR入口按钮 -->
    <view class="ar-float-btn" @click="openAR">
      <text class="ar-icon">AR</text>
      <text class="ar-text">AR体验</text>
    </view>

    <!-- 加载更多 -->
    <view class="load-more" v-if="hasMore">
      <text @click="loadMore">加载更多</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      keyword: '',
      currentCategory: null,
      categories: [
        { id: 1, name: '青铜器' },
        { id: 2, name: '陶瓷' },
        { id: 3, name: '书画' },
        { id: 4, name: '玉器' },
        { id: 5, name: '杂项' }
      ],
      banners: [
        { image: '/static/banner1.jpg' },
        { image: '/static/banner2.jpg' },
        { image: '/static/banner3.jpg' }
      ],
      hotList: [],
      heritageList: [],
      page: 0,
      size: 10,
      hasMore: true
    }
  },
  onLoad() {
    this.loadHotHeritage()
    this.loadHeritageList()
  },
  onPullDownRefresh() {
    this.refresh()
  },
  onReachBottom() {
    this.loadMore()
  },
  methods: {
    async loadHotHeritage() {
      try {
        const res = await request.get('/heritage/hot')
        if (res.code === 200) {
          this.hotList = res.data || []
        }
      } catch (e) {
        // 使用模拟数据
        this.hotList = this.getMockData(5)
      }
    },
    async loadHeritageList() {
      try {
        const params = {
          page: this.page,
          size: this.size,
          category: this.currentCategory,
          keyword: this.keyword || undefined
        }
        const res = await request.get('/heritage/list', params)
        if (res.code === 200) {
          const content = res.data.content || []
          if (this.page === 0) {
            this.heritageList = content
          } else {
            this.heritageList = [...this.heritageList, ...content]
          }
          this.hasMore = !res.data.last
        }
      } catch (e) {
        // 使用模拟数据
        if (this.page === 0) {
          this.heritageList = this.getMockData(10)
        }
        this.hasMore = this.page < 2
      }
    },
    searchHeritage() {
      this.page = 0
      this.loadHeritageList()
    },
    selectCategory(id) {
      this.currentCategory = id
      this.page = 0
      this.loadHeritageList()
    },
    loadMore() {
      if (this.hasMore) {
        this.page++
        this.loadHeritageList()
      }
    },
    refresh() {
      this.page = 0
      this.keyword = ''
      this.currentCategory = null
      this.loadHotHeritage()
      this.loadHeritageList()
      setTimeout(() => {
        uni.stopPullDownRefresh()
      }, 1000)
    },
    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/heritage/detail?id=${id}`
      })
    },
    goToMore() {
      // 跳转至更多页面
    },
    openAR() {
      uni.showToast({
        title: 'AR功能开发中',
        icon: 'none'
      })
    },
    getMockData(count) {
      const data = []
      for (let i = 1; i <= count; i++) {
        data.push({
          id: i,
          name: `示例文物${i}`,
          period: '清代',
          description: '这是一件精美的古代文物，具有重要的历史和艺术价值。',
          mainImage: '',
          viewCount: Math.floor(Math.random() * 1000),
          favoriteCount: Math.floor(Math.random() * 500)
        })
      }
      return data
    }
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  background: #fff;
  border-radius: 50rpx;
  padding: 8rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.search-input {
  flex: 1;
  padding: 16rpx 24rpx;
  font-size: 28rpx;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 16rpx 32rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
}

.category-scroll {
  white-space: nowrap;
  margin-bottom: 20rpx;
}

.category-list {
  display: flex;
  padding: 0 10rpx;
}

.category-item {
  padding: 16rpx 32rpx;
  margin: 0 10rpx;
  background: #fff;
  border-radius: 50rpx;
  font-size: 28rpx;
  color: #666;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.category-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.banner {
  height: 320rpx;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 30rpx;
}

.banner-image {
  width: 100%;
  height: 100%;
}

.section {
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding: 0 10rpx;
}

.section-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.section-more {
  font-size: 26rpx;
  color: #999;
}

.hot-scroll {
  white-space: nowrap;
}

.hot-list {
  display: flex;
  padding: 0 10rpx;
}

.hot-item {
  width: 240rpx;
  margin-right: 20rpx;
  display: flex;
  flex-direction: column;
}

.hot-image {
  width: 240rpx;
  height: 180rpx;
  border-radius: 12rpx;
  margin-bottom: 12rpx;
  background: #f0f0f0;
}

.hot-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 6rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-period {
  font-size: 24rpx;
  color: #999;
}

.heritage-list {
  padding: 0 10rpx;
}

.heritage-item {
  display: flex;
  margin-bottom: 20rpx;
}

.heritage-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  background: #f0f0f0;
  flex-shrink: 0;
}

.heritage-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.heritage-name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 8rpx;
}

.heritage-period {
  font-size: 26rpx;
  color: #667eea;
  margin-bottom: 8rpx;
}

.heritage-desc {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 12rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.heritage-stats {
  display: flex;
  margin-top: auto;
}

.stat-item {
  font-size: 24rpx;
  color: #999;
  margin-right: 20rpx;
}

.ar-float-btn {
  position: fixed;
  right: 30rpx;
  bottom: 200rpx;
  width: 120rpx;
  height: 120rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
  z-index: 999;
}

.ar-icon {
  font-size: 32rpx;
  font-weight: bold;
  color: #fff;
}

.ar-text {
  font-size: 20rpx;
  color: #fff;
  margin-top: 4rpx;
}

.load-more {
  text-align: center;
  padding: 30rpx;
  color: #999;
  font-size: 26rpx;
}
</style>