<template>
  <view class="detail-page">
    <!-- 顶部操作栏 -->
    <view class="top-bar">
      <view class="back-btn" @click="goBack">
        <text>←</text>
      </view>
      <view class="top-actions">
        <view class="action-btn" :class="{ active: isFavorited }" @click="toggleFavorite">
          <text>❤</text>
        </view>
        <view class="action-btn" @click="shareHeritage">
          <text>↗</text>
        </view>
        <view class="action-btn" @click="authExpert">
          <text>🔍</text>
        </view>
      </view>
    </view>

    <!-- 3D模型加载区 -->
    <view class="model-section" v-if="heritage.model3d">
      <view class="model-container" v-if="!modelLoading && !modelError">
        <view class="model-placeholder">
          <text class="model-icon">3D</text>
          <text class="model-text">点击查看3D模型</text>
        </view>
      </view>
      <view class="loading-container" v-else-if="modelLoading">
        <view class="loading-progress">
          <view class="progress-bar" :style="{ width: progress + '%' }"></view>
        </view>
        <text class="loading-text">3D模型加载中 {{ progress }}%</text>
      </view>
      <view class="error-container" v-else>
        <text class="error-icon">!</text>
        <text class="error-text">3D模型加载失败</text>
        <text class="error-hint">已切换为高清图片模式</text>
      </view>
    </view>

    <!-- 高清图片画廊 -->
    <scroll-view class="gallery-scroll" scroll-x="true" show-scrollbar="false">
      <view class="gallery-list">
        <image 
          class="gallery-image" 
          v-for="(img, index) in galleryImages" 
          :key="index"
          :src="img || '/static/default.png'"
          mode="aspectFill"
          @click="previewImage(index)"
        ></image>
      </view>
    </scroll-view>

    <!-- 文物基本信息 -->
    <view class="card info-card">
      <view class="heritage-title">
        <text class="heritage-name">{{ heritage.name }}</text>
        <view class="heritage-category">
          <text>{{ getCategoryName(heritage.category) }}</text>
        </view>
      </view>
      <view class="heritage-meta">
        <view class="meta-item">
          <text class="meta-label">年代：</text>
          <text class="meta-value">{{ heritage.period }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">材质：</text>
          <text class="meta-value">{{ heritage.material }}</text>
        </view>
        <view class="meta-item">
          <text class="meta-label">尺寸：</text>
          <text class="meta-value">{{ heritage.dimensions }}</text>
        </view>
        <view class="meta-item" v-if="heritage.excavationSite">
          <text class="meta-label">出土地点：</text>
          <text class="meta-value">{{ heritage.excavationSite }}</text>
        </view>
      </view>
      <view class="heritage-desc">
        <text>{{ heritage.description }}</text>
      </view>
    </view>

    <!-- 3D全景展示区 -->
    <view class="card panorama-card">
      <view class="section-title">
        <text>3D全景展示</text>
        <text class="section-hint">拖动旋转查看</text>
      </view>
      <view class="panorama-container" @touchstart="onPanStart" @touchmove="onPanMove" @touchend="onPanEnd">
        <image 
          class="panorama-image" 
          :src="heritage.panoramaImage || '/static/default.png'"
          :style="{ transform: `rotateY(${panRotation}deg)` }"
        ></image>
        <view class="panorama-overlay">
          <view class="rotate-hint">
            <text>↔ 左右滑动旋转</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 高清细节放大镜 -->
    <view class="card detail-card">
      <view class="section-title">
        <text>高清细节</text>
        <text class="section-hint">长按放大查看</text>
      </view>
      <view 
        class="detail-image-container"
        @touchstart="startMagnify"
        @touchmove="moveMagnify"
        @touchend="endMagnify"
      >
        <image 
          class="detail-image" 
          :src="heritage.mainImage || '/static/default.png'"
          mode="aspectFit"
        ></image>
        <view class="magnifier" v-if="showMagnifier" :style="magnifierStyle">
          <image 
            class="magnified-image"
            :src="heritage.mainImage || '/static/default.png'"
            :style="magnifiedImageStyle"
          ></image>
        </view>
      </view>
    </view>

    <!-- 文物故事 -->
    <view class="card story-card">
      <view class="section-title">
        <text>文物故事</text>
        <view class="audio-btn" @click="toggleAudio">
          <text>{{ isPlaying ? '⏸' : '▶' }}</text>
          <text>{{ isPlaying ? '暂停' : '播放讲解' }}</text>
        </view>
      </view>
      <view class="story-section">
        <text class="story-subtitle">历史背景</text>
        <text class="story-content">{{ heritage.historyStory || '暂无历史背景介绍' }}</text>
      </view>
      <view class="story-section">
        <text class="story-subtitle">修复历程</text>
        <text class="story-content">{{ heritage.restorationStory || '暂无修复历程介绍' }}</text>
      </view>
    </view>

    <!-- 溯源入口 -->
    <view class="card trace-card" @click="goToTrace">
      <view class="trace-content">
        <view class="trace-icon">
          <text>🔗</text>
        </view>
        <view class="trace-info">
          <text class="trace-title">区块链溯源查询</text>
          <text class="trace-desc">查看文物流转记录、历史拍卖信息</text>
        </view>
      </view>
      <text class="trace-arrow">></text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      heritageId: null,
      heritage: {},
      isFavorited: false,
      modelLoading: false,
      modelError: false,
      progress: 0,
      panRotation: 0,
      touchStartX: 0,
      showMagnifier: false,
      magnifierX: 0,
      magnifierY: 0,
      isPlaying: false
    }
  },
  computed: {
    galleryImages() {
      const images = []
      if (this.heritage.mainImage) {
        images.push(this.heritage.mainImage)
      }
      if (this.heritage.detailImages) {
        const detailImages = String(this.heritage.detailImages).split(',')
        images.push(...detailImages)
      }
      return images.length > 0 ? images : ['/static/default.png']
    },
    magnifierStyle() {
      return {
        left: this.magnifierX + 'px',
        top: this.magnifierY + 'px'
      }
    },
    magnifiedImageStyle() {
      const scale = 2
      return {
        transform: `scale(${scale})`,
        transformOrigin: 'center'
      }
    }
  },
  onLoad(options) {
    this.heritageId = options.id
    this.loadHeritageDetail()
  },
  methods: {
    async loadHeritageDetail() {
      try {
        const res = await request.get(`/heritage/${this.heritageId}`)
        if (res.code === 200) {
          this.heritage = res.data
        }
      } catch (e) {
        // 使用模拟数据
        this.heritage = {
          id: this.heritageId,
          name: '示例文物',
          category: 1,
          period: '清代',
          material: '青铜',
          dimensions: '高30cm，宽20cm',
          description: '这是一件精美的古代文物，具有重要的历史和艺术价值。',
          mainImage: '',
          detailImages: '',
          model3d: '3d_model_url',
          panoramaImage: '',
          historyStory: '该文物出土于1980年，是清代宫廷御用之物，历经百年沧桑，保存完好。它见证了清代高超的工艺水平和丰富的文化内涵。',
          restorationStory: '2005年进行了首次修复，采用了最先进的文物修复技术，恢复了文物原有的光彩。2018年进行了二次保养。',
          excavationSite: '北京市海淀区'
        }
      }
      
      // 模拟3D模型加载
      this.modelLoading = true
      this.progress = 0
      const timer = setInterval(() => {
        this.progress += 10
        if (this.progress >= 80) {
          clearInterval(timer)
          // 模拟加载失败，切换为图片模式
          this.modelLoading = false
          this.modelError = true
        }
      }, 500)
    },
    getCategoryName(category) {
      const categories = {
        1: '青铜器',
        2: '陶瓷',
        3: '书画',
        4: '玉器',
        5: '杂项'
      }
      return categories[category] || '未知'
    },
    goBack() {
      uni.navigateBack()
    },
    toggleFavorite() {
      this.isFavorited = !this.isFavorited
      uni.showToast({
        title: this.isFavorited ? '已收藏' : '已取消收藏',
        icon: 'success'
      })
    },
    shareHeritage() {
      uni.showToast({
        title: '分享功能开发中',
        icon: 'none'
      })
    },
    authExpert() {
      uni.showToast({
        title: '专家鉴定功能开发中',
        icon: 'none'
      })
    },
    previewImage(index) {
      uni.previewImage({
        urls: this.galleryImages,
        current: index
      })
    },
    onPanStart(e) {
      this.touchStartX = e.touches[0].clientX
    },
    onPanMove(e) {
      const deltaX = e.touches[0].clientX - this.touchStartX
      this.panRotation += deltaX * 0.5
      this.touchStartX = e.touches[0].clientX
    },
    onPanEnd() {
      // 旋转结束
    },
    startMagnify(e) {
      this.showMagnifier = true
      this.updateMagnifierPosition(e)
    },
    moveMagnify(e) {
      if (this.showMagnifier) {
        this.updateMagnifierPosition(e)
      }
    },
    endMagnify() {
      this.showMagnifier = false
    },
    updateMagnifierPosition(e) {
      const touch = e.touches[0]
      this.magnifierX = touch.clientX - 75
      this.magnifierY = touch.clientY - 75
    },
    toggleAudio() {
      this.isPlaying = !this.isPlaying
      uni.showToast({
        title: this.isPlaying ? '开始播放语音讲解' : '已暂停',
        icon: 'none'
      })
    },
    goToTrace() {
      uni.navigateTo({
        url: `/pages/trace/index?heritageId=${this.heritageId}`
      })
    }
  }
}
</script>

<style scoped>
.detail-page {
  padding-bottom: 40rpx;
}

.top-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  color: #333;
}

.top-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 50%;
  font-size: 30rpx;
}

.action-btn.active {
  background: #ff6b6b;
  color: #fff;
}

.model-section {
  margin-top: 100rpx;
  padding: 0 20rpx;
}

.model-container {
  height: 400rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.model-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #fff;
}

.model-icon {
  font-size: 80rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.model-text {
  font-size: 28rpx;
}

.loading-container {
  height: 400rpx;
  background: #f5f5f5;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.loading-progress {
  width: 400rpx;
  height: 12rpx;
  background: #e0e0e0;
  border-radius: 6rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  transition: width 0.3s;
}

.loading-text {
  font-size: 26rpx;
  color: #666;
}

.error-container {
  height: 400rpx;
  background: #fff5f5;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2rpx dashed #ff6b6b;
}

.error-icon {
  width: 80rpx;
  height: 80rpx;
  background: #ff6b6b;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.error-text {
  font-size: 28rpx;
  color: #ff6b6b;
  margin-bottom: 10rpx;
}

.error-hint {
  font-size: 24rpx;
  color: #999;
}

.gallery-scroll {
  margin: 20rpx 0;
}

.gallery-list {
  display: flex;
  padding: 0 20rpx;
  gap: 16rpx;
}

.gallery-image {
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  background: #f0f0f0;
}

.info-card {
  margin: 0 20rpx 20rpx;
}

.heritage-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.heritage-name {
  font-size: 40rpx;
  font-weight: bold;
  color: #333;
}

.heritage-category {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 8rpx 20rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}

.heritage-meta {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20rpx;
}

.meta-item {
  width: 50%;
  margin-bottom: 12rpx;
  display: flex;
  align-items: center;
}

.meta-label {
  font-size: 26rpx;
  color: #999;
}

.meta-value {
  font-size: 26rpx;
  color: #333;
}

.heritage-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.section-hint {
  font-size: 24rpx;
  color: #999;
  font-weight: normal;
}

.panorama-card,
.detail-card,
.story-card {
  margin: 0 20rpx 20rpx;
}

.panorama-container {
  position: relative;
  height: 400rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  overflow: hidden;
  perspective: 1000px;
}

.panorama-image {
  width: 100%;
  height: 100%;
  transition: transform 0.1s;
}

.panorama-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.5));
}

.rotate-hint {
  color: #fff;
  font-size: 24rpx;
  text-align: center;
}

.detail-image-container {
  position: relative;
  height: 400rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  overflow: hidden;
}

.detail-image {
  width: 100%;
  height: 100%;
}

.magnifier {
  position: absolute;
  width: 150rpx;
  height: 150rpx;
  border-radius: 50%;
  border: 4rpx solid #667eea;
  background: #fff;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.2);
  pointer-events: none;
}

.magnified-image {
  width: 300rpx;
  height: 300rpx;
}

.audio-btn {
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
  font-size: 24rpx;
}

.story-section {
  margin-bottom: 24rpx;
}

.story-section:last-child {
  margin-bottom: 0;
}

.story-subtitle {
  font-size: 28rpx;
  font-weight: 500;
  color: #667eea;
  margin-bottom: 12rpx;
  display: block;
}

.story-content {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
}

.trace-card {
  margin: 0 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.trace-content {
  display: flex;
  align-items: center;
  flex: 1;
}

.trace-icon {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-right: 20rpx;
}

.trace-info {
  flex: 1;
}

.trace-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.trace-desc {
  font-size: 24rpx;
  color: #999;
}

.trace-arrow {
  font-size: 32rpx;
  color: #999;
}
</style>