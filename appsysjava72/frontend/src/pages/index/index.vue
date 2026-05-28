<template>
  <view class="index-container">
    <view class="search-bar">
      <view class="search-input">
        <text class="search-icon">🔍</text>
        <input 
          class="input-field" 
          placeholder="搜索社团、活动..." 
          v-model="keyword"
          @confirm="searchClubs"
        />
      </view>
    </view>
    
    <view class="category-section">
      <scroll-view class="category-scroll" scroll-x="true" show-scrollbar="false">
        <view 
          class="category-item" 
          :class="{ active: selectedCategory === null }"
          @click="selectCategory(null)"
        >
          <text class="cat-icon">🏠</text>
          <text class="cat-name">全部</text>
        </view>
        <view 
          class="category-item" 
          v-for="cat in categories" 
          :key="cat.id"
          :class="{ active: selectedCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          <text class="cat-icon">{{ cat.icon || '📚' }}</text>
          <text class="cat-name">{{ cat.name }}</text>
        </view>
      </scroll-view>
    </view>
    
    <view class="banner-section">
      <swiper class="banner-swiper" autoplay circular indicator-dots indicator-active-color="#5677fc">
        <swiper-item v-for="(banner, index) in banners" :key="index">
          <view class="banner-item" :style="{ background: banner.bg }">
            <view class="banner-content">
              <text class="banner-title">{{ banner.title }}</text>
              <text class="banner-desc">{{ banner.desc }}</text>
            </view>
            <text class="banner-emoji">{{ banner.emoji }}</text>
          </view>
        </swiper-item>
      </swiper>
    </view>
    
    <view class="section-header">
      <text class="section-title">热门社团</text>
      <text class="more-link" @click="viewAll">查看更多 →</text>
    </view>
    
    <view class="club-list" v-if="clubList.length > 0">
      <view 
        class="club-card" 
        v-for="club in clubList" 
        :key="club.id"
        @click="goClubDetail(club.id)"
      >
        <view class="club-avatar">
          <image v-if="club.logo" :src="club.logo" mode="aspectFill" class="logo-img" />
          <view v-else class="avatar-placeholder" :style="{ background: getRandomColor() }">
            <text class="avatar-text">{{ club.name.substring(0, 1) }}</text>
          </view>
        </view>
        <view class="club-info">
          <view class="club-header">
            <text class="club-name">{{ club.name }}</text>
            <view class="club-tag">{{ getCategoryName(club.categoryId) }}</view>
          </view>
          <text class="club-desc">{{ club.description || '暂无简介' }}</text>
          <view class="club-meta">
            <text class="meta-item">👥 {{ club.memberCount || 0 }} 人</text>
            <text class="meta-item">📅 {{ util.formatDate(club.createTime) }}</text>
          </view>
        </view>
        <view class="arrow">›</view>
      </view>
    </view>
    
    <view class="empty-state" v-else-if="!loading">
      <text class="empty-icon">📭</text>
      <text class="empty-text">暂无社团数据</text>
    </view>
    
    <view class="loading-state" v-if="loading">
      <text>加载中...</text>
    </view>
    
    <view class="load-more" v-if="hasMore && !loading" @click="loadMore">
      <text>加载更多</text>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      keyword: '',
      selectedCategory: null,
      categories: [],
      clubList: [],
      banners: [
        { title: '新学期招新季', desc: '加入你喜欢的社团', bg: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)', emoji: '🎉' },
        { title: '精彩活动', desc: '丰富的校园活动等你来', bg: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', emoji: '🎪' },
        { title: '社团管理', desc: '轻松管理你的社团', bg: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', emoji: '🏫' }
      ],
      pageNum: 1,
      pageSize: 10,
      hasMore: true,
      loading: false
    }
  },
  onShow() {
    if (!this.categories.length) {
      this.loadCategories()
    }
    this.refreshList()
    this.checkAuth()
  },
  onPullDownRefresh() {
    this.refreshList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    util,
    
    checkAuth() {
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.reLaunch({ url: '/pages/login/login' })
      }
    },
    
    getRandomColor() {
      const colors = ['#5677fc', '#4cd964', '#f0ad4e', '#dd524d', '#9c27b0', '#00bcd4']
      return colors[Math.floor(Math.random() * colors.length)]
    },
    
    getCategoryName(categoryId) {
      const cat = this.categories.find(c => c.id === categoryId)
      return cat ? cat.name : '其他'
    },
    
    async loadCategories() {
      try {
        const res = await api.getCategories()
        this.categories = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    
    selectCategory(categoryId) {
      this.selectedCategory = categoryId
      this.refreshList()
    },
    
    searchClubs() {
      this.refreshList()
    },
    
    async refreshList() {
      this.pageNum = 1
      this.hasMore = true
      this.loading = true
      try {
        const res = await api.getClubPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          categoryId: this.selectedCategory,
          keyword: this.keyword
        })
        this.clubList = res.data.list || []
        this.hasMore = this.pageNum < res.data.totalPage
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async loadMore() {
      this.pageNum++
      this.loading = true
      try {
        const res = await api.getClubPage({
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          categoryId: this.selectedCategory,
          keyword: this.keyword
        })
        this.clubList = this.clubList.concat(res.data.list || [])
        this.hasMore = this.pageNum < res.data.totalPage
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    goClubDetail(id) {
      uni.navigateTo({ url: '/pages/club/detail?id=' + id })
    },
    
    viewAll() {
      uni.showToast({ title: '已显示全部', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.index-container {
  padding-bottom: 120rpx;
  background: #f5f6f8;
  min-height: 100vh;
}

.search-bar {
  padding: 20rpx 30rpx;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-input {
  display: flex;
  align-items: center;
  height: 80rpx;
  background: #f0f2f5;
  border-radius: 40rpx;
  padding: 0 30rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 15rpx;
}

.input-field {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.category-section {
  background: #fff;
  padding: 20rpx 0;
  margin-bottom: 20rpx;
}

.category-scroll {
  white-space: nowrap;
  padding: 0 20rpx;
}

.category-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  padding: 15rpx 30rpx;
  margin-right: 10rpx;
  border-radius: 16rpx;
  transition: all 0.3s;
}

.category-item.active {
  background: #e8ecff;
}

.cat-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.cat-name {
  font-size: 24rpx;
  color: #666;
}

.category-item.active .cat-name {
  color: #5677fc;
  font-weight: 600;
}

.banner-section {
  padding: 0 30rpx;
  margin-bottom: 30rpx;
}

.banner-swiper {
  height: 240rpx;
  border-radius: 20rpx;
  overflow: hidden;
}

.banner-item {
  height: 100%;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40rpx;
}

.banner-content {
  flex: 1;
}

.banner-title {
  display: block;
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
  margin-bottom: 10rpx;
}

.banner-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.banner-emoji {
  font-size: 100rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.more-link {
  font-size: 26rpx;
  color: #5677fc;
}

.club-list {
  padding: 0 30rpx;
}

.club-card {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.club-avatar {
  width: 120rpx;
  height: 120rpx;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.logo-img {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #fff;
}

.club-info {
  flex: 1;
  overflow: hidden;
}

.club-header {
  display: flex;
  align-items: center;
  margin-bottom: 10rpx;
}

.club-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-right: 15rpx;
}

.club-tag {
  padding: 4rpx 16rpx;
  background: #e8ecff;
  color: #5677fc;
  border-radius: 20rpx;
  font-size: 22rpx;
  flex-shrink: 0;
}

.club-desc {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.club-meta {
  display: flex;
  gap: 25rpx;
}

.meta-item {
  font-size: 24rpx;
  color: #999;
}

.arrow {
  font-size: 40rpx;
  color: #ccc;
  margin-left: 10rpx;
}

.empty-state,
.loading-state,
.load-more {
  text-align: center;
  padding: 60rpx 0;
  color: #999;
  font-size: 28rpx;
}

.empty-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.load-more {
  color: #5677fc;
}
</style>
