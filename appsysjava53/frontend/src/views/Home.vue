<template>
  <div class="home-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索文物、帖子..."
        prefix-icon="Search"
        clearable
        class="search-input"
      />
    </div>

    <!-- 轮播图 -->
    <el-carousel height="200px" class="banner-carousel" indicator-position="none">
      <el-carousel-item v-for="(banner, index) in banners" :key="index">
        <img :src="banner.image" class="banner-image" />
      </el-carousel-item>
    </el-carousel>

    <!-- 功能入口 -->
    <div class="menu-grid">
      <div class="menu-item" @click="goToExpert">
        <div class="menu-icon expert">
          <el-icon :size="28"><User /></el-icon>
        </div>
        <span>专家问答</span>
      </div>
      <div class="menu-item" @click="goToAuction">
        <div class="menu-icon auction">
          <el-icon :size="28"><Present /></el-icon>
        </div>
        <span>拍卖专区</span>
      </div>
      <div class="menu-item" @click="goToDonation">
        <div class="menu-icon donation">
          <el-icon :size="28"><Heart /></el-icon>
        </div>
        <span>捐赠通道</span>
      </div>
      <div class="menu-item" @click="goToActivity">
        <div class="menu-icon activity">
          <el-icon :size="28"><Calendar /></el-icon>
        </div>
        <span>线下活动</span>
      </div>
    </div>

    <!-- 热门文物 -->
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">热门文物</h3>
        <span class="section-more" @click="goToMarket">更多 ></span>
      </div>
      <div class="heritage-scroll">
        <div
          class="heritage-card"
          v-for="item in heritageList"
          :key="item.id"
          @click="goToHeritageDetail(item.id)"
        >
          <img :src="item.image" class="heritage-image" />
          <div class="heritage-name">{{ item.name }}</div>
          <div class="heritage-price">¥{{ item.price }}</div>
        </div>
      </div>
    </div>

    <!-- 热门帖子 -->
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">热门帖子</h3>
        <span class="section-more" @click="goToCommunity">更多 ></span>
      </div>
      <div class="post-list">
        <div
          class="post-card"
          v-for="post in postList"
          :key="post.id"
          @click="goToPostDetail(post.id)"
        >
          <div class="post-header">
            <div class="post-avatar">
              <el-icon :size="20"><User /></el-icon>
            </div>
            <div class="post-user">
              <div class="post-username">{{ post.username }}</div>
              <div class="post-time text-light">{{ post.time }}</div>
            </div>
          </div>
          <div class="post-content">{{ post.content }}</div>
          <div class="post-footer">
            <span class="post-action">
              <el-icon :size="16"><Star /></el-icon>
              {{ post.likes }}
            </span>
            <span class="post-action">
              <el-icon :size="16"><ChatDotRound /></el-icon>
              {{ post.comments }}
            </span>
            <span class="post-action">
              <el-icon :size="16"><View /></el-icon>
              {{ post.views }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search,
  User,
  Present,
  Heart,
  Calendar,
  Star,
  ChatDotRound,
  View
} from '@element-plus/icons-vue'

const router = useRouter()
const searchKeyword = ref('')

const banners = [
  { image: 'https://picsum.photos/800/400?random=1' },
  { image: 'https://picsum.photos/800/400?random=2' },
  { image: 'https://picsum.photos/800/400?random=3' }
]

const heritageList = [
  { id: 1, name: '清代青花瓷瓶', price: '28,000', image: 'https://picsum.photos/300/300?random=10' },
  { id: 2, name: '明代青铜器', price: '55,000', image: 'https://picsum.photos/300/300?random=11' },
  { id: 3, name: '齐白石虾图', price: '120,000', image: 'https://picsum.photos/300/300?random=12' },
  { id: 4, name: '和田玉籽料', price: '35,000', image: 'https://picsum.photos/300/300?random=13' }
]

const postList = [
  {
    id: 1,
    username: '收藏爱好者小王',
    content: '分享一下我收藏这件清代青花瓷瓶的经历，从偶然发现到最终入手，过程非常有趣。',
    time: '2小时前',
    likes: 128,
    comments: 32,
    views: 2560
  },
  {
    id: 2,
    username: '古董藏家老李',
    content: '作为一个有十几年收藏经验的藏家，今天给大家分享一些青铜器辨别的小技巧。',
    time: '5小时前',
    likes: 256,
    comments: 48,
    views: 5120
  }
]

const goToExpert = () => {
  ElMessage.info('专家问答功能开发中')
}

const goToAuction = () => {
  ElMessage.info('拍卖专区功能开发中')
}

const goToDonation = () => {
  ElMessage.info('捐赠通道功能开发中')
}

const goToActivity = () => {
  router.push('/activity')
}

const goToMarket = () => {
  router.push('/market')
}

const goToCommunity = () => {
  router.push('/community')
}

const goToHeritageDetail = (id) => {
  router.push(`/heritage/${id}`)
}

const goToPostDetail = (id) => {
  router.push(`/post/${id}`)
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.banner-carousel {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.menu-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.menu-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 8px;
}

.menu-icon.expert { background: linear-gradient(135deg, #667eea, #764ba2); }
.menu-icon.auction { background: linear-gradient(135deg, #f093fb, #f5576c); }
.menu-icon.donation { background: linear-gradient(135deg, #4facfe, #00f2fe); }
.menu-icon.activity { background: linear-gradient(135deg, #43e97b, #38f9d7); }

.menu-item span {
  font-size: 14px;
  color: #333;
}

.section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.section-more {
  color: #8B4513;
  font-size: 14px;
  cursor: pointer;
}

.heritage-scroll {
  display: flex;
  gap: 16px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.heritage-card {
  flex-shrink: 0;
  width: 160px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.heritage-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.heritage-name {
  padding: 12px 12px 8px;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.heritage-price {
  padding: 0 12px 12px;
  font-size: 18px;
  font-weight: bold;
  color: #ff4d4f;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  cursor: pointer;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.post-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f5f0eb;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #8B4513;
}

.post-user {
  display: flex;
  flex-direction: column;
}

.post-username {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-content {
  font-size: 15px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 16px;
}

.post-footer {
  display: flex;
  gap: 24px;
}

.post-action {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #999;
}

.elder-mode .menu-item span {
  font-size: 18px;
}

.elder-mode .section-title {
  font-size: 22px;
}

.elder-mode .heritage-name {
  font-size: 18px;
}

.elder-mode .heritage-price {
  font-size: 20px;
}

.elder-mode .post-content {
  font-size: 18px;
}
</style>
