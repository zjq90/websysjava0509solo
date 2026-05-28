<template>
  <view class="publish-container">
    <view class="club-select" @click="showClubPicker = true">
      <text class="label">选择社团</text>
      <view class="select-value">
        <text v-if="selectedClub" class="club-name">{{ selectedClub.name }}</text>
        <text v-else class="placeholder">请选择要发布的社团</text>
        <text class="arrow">›</text>
      </view>
    </view>
    
    <view class="content-section">
      <textarea 
        v-model="content" 
        class="content-textarea" 
        placeholder="分享你的新鲜事..."
        maxlength="500"
        :auto-height="true"
      />
      <text class="char-count">{{ content.length }}/500</text>
    </view>
    
    <view class="image-section">
      <view class="section-header">
        <text class="section-title">添加图片</text>
        <text class="image-count">{{ imageList.length }}/9</text>
      </view>
      <view class="image-list">
        <view 
          class="image-item" 
          v-for="(img, index) in imageList" 
          :key="index"
        >
          <image :src="img" mode="aspectFill" class="image" />
          <view class="delete-btn" @click="removeImage(index)">
            <text>×</text>
          </view>
        </view>
        <view 
          class="add-btn" 
          v-if="imageList.length < 9"
          @click="chooseImage"
        >
          <text class="add-icon">+</text>
          <text class="add-text">添加图片</text>
        </view>
      </view>
    </view>
    
    <view class="bottom-bar">
      <view class="tip">
        <text class="tip-icon">💡</text>
        <text class="tip-text">发布内容请遵守社团规范</text>
      </view>
      <button 
        class="publish-btn" 
        :disabled="!canPublish"
        @click="publishFeed"
      >
        {{ publishing ? '发布中...' : '发布' }}
      </button>
    </view>
    
    <view class="club-picker" v-if="showClubPicker">
      <view class="picker-mask" @click="showClubPicker = false"></view>
      <view class="picker-content">
        <view class="picker-header">
          <text class="picker-title">选择社团</text>
          <text class="picker-close" @click="showClubPicker = false">取消</text>
        </view>
        <scroll-view class="club-list" scroll-y>
          <view 
            class="club-item" 
            v-for="club in myClubs" 
            :key="club.id"
            @click="selectClub(club)"
          >
            <image 
              v-if="club.logo" 
              :src="club.logo" 
              class="club-logo" 
              mode="aspectFill"
            />
            <view v-else class="club-logo-placeholder">
              <text>{{ club.name.substring(0, 1) }}</text>
            </view>
            <text class="club-item-name">{{ club.name }}</text>
            <text 
              class="check-icon" 
              v-if="selectedClub && selectedClub.id === club.id"
            >✓</text>
          </view>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      content: '',
      imageList: [],
      selectedClub: null,
      myClubs: [],
      showClubPicker: false,
      publishing: false
    }
  },
  computed: {
    canPublish() {
      return this.selectedClub && (this.content.trim() || this.imageList.length > 0) && !this.publishing
    }
  },
  onLoad() {
    this.loadMyClubs()
  },
  methods: {
    async loadMyClubs() {
      try {
        const res = await api.getMyClubs()
        this.myClubs = res.data || []
      } catch (e) {
        console.error(e)
      }
    },
    
    selectClub(club) {
      this.selectedClub = club
      this.showClubPicker = false
    },
    
    chooseImage() {
      const remain = 9 - this.imageList.length
      uni.chooseImage({
        count: remain,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.imageList = this.imageList.concat(res.tempFilePaths)
        }
      })
    },
    
    removeImage(index) {
      this.imageList.splice(index, 1)
    },
    
    async uploadImages() {
      const uploaded = []
      for (const imgPath of this.imageList) {
        try {
          const res = await new Promise((resolve, reject) => {
            uni.uploadFile({
              url: 'http://localhost:8088/api/upload',
              filePath: imgPath,
              name: 'file',
              header: {
                'Authorization': 'Bearer ' + util.getStorage('token')
              },
              success: (uploadRes) => {
                const data = JSON.parse(uploadRes.data)
                if (data.code === 200) {
                  resolve(data.data)
                } else {
                  reject(new Error(data.message))
                }
              },
              fail: reject
            })
          })
          uploaded.push(res)
        } catch (e) {
          console.error('Upload failed:', e)
          throw e
        }
      }
      return uploaded.join(',')
    },
    
    async publishFeed() {
      if (!this.canPublish) return
      
      this.publishing = true
      util.showLoading('发布中...')
      
      try {
        let images = ''
        if (this.imageList.length > 0) {
          try {
            images = await this.uploadImages()
          } catch (e) {
            util.toast('图片上传失败，请重试')
            return
          }
        }
        
        const res = await api.publishFeed(
          this.selectedClub.id,
          this.content.trim(),
          images
        )
        
        if (res.code === 200) {
          util.toast('发布成功', 'success')
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        } else {
          util.toast(res.message || '发布失败')
        }
      } catch (e) {
        util.toast('发布失败，请重试')
        console.error(e)
      } finally {
        util.hideLoading()
        this.publishing = false
      }
    }
  }
}
</script>

<style scoped>
.publish-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 120rpx;
}

.club-select {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.label {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 16rpx;
  display: block;
}

.select-value {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.club-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 600;
}

.placeholder {
  font-size: 32rpx;
  color: #ccc;
}

.arrow {
  font-size: 40rpx;
  color: #ccc;
}

.content-section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
  position: relative;
}

.content-textarea {
  width: 100%;
  min-height: 240rpx;
  font-size: 30rpx;
  color: #333;
  line-height: 1.6;
}

.char-count {
  position: absolute;
  right: 30rpx;
  bottom: 20rpx;
  font-size: 24rpx;
  color: #ccc;
}

.image-section {
  background: #fff;
  padding: 30rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 600;
}

.image-count {
  font-size: 26rpx;
  color: #999;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.image-item {
  position: relative;
  width: calc(33.33% - 12rpx);
  aspect-ratio: 1;
}

.image {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
  background: #f0f0f0;
}

.delete-btn {
  position: absolute;
  top: -12rpx;
  right: -12rpx;
  width: 44rpx;
  height: 44rpx;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.delete-btn text {
  color: #fff;
  font-size: 28rpx;
  line-height: 1;
}

.add-btn {
  width: calc(33.33% - 12rpx);
  aspect-ratio: 1;
  border: 2rpx dashed #ddd;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.add-icon {
  font-size: 48rpx;
  color: #ccc;
  line-height: 1;
  margin-bottom: 8rpx;
}

.add-text {
  font-size: 24rpx;
  color: #999;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-top: 1rpx solid #f0f0f0;
}

.tip {
  display: flex;
  align-items: center;
}

.tip-icon {
  font-size: 28rpx;
  margin-right: 8rpx;
}

.tip-text {
  font-size: 24rpx;
  color: #999;
}

.publish-btn {
  padding: 16rpx 48rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 30rpx;
  font-weight: 600;
  border: none;
}

.publish-btn[disabled] {
  background: #ccc;
}

.club-picker {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
}

.picker-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.picker-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-radius: 20rpx 20rpx 0 0;
  max-height: 70vh;
  display: flex;
  flex-direction: column;
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.picker-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.picker-close {
  font-size: 28rpx;
  color: #999;
}

.club-list {
  flex: 1;
  overflow-y: auto;
}

.club-item {
  display: flex;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 1rpx solid #f8f8f8;
}

.club-logo,
.club-logo-placeholder {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.club-logo-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}

.club-item-name {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

.check-icon {
  font-size: 36rpx;
  color: #5677fc;
  font-weight: bold;
}
</style>
