<template>
  <view class="post-container">
    <view class="form-section">
      <view class="form-item">
        <text class="label">选择话题</text>
        <picker :value="topicIndex" :range="topicList" @change="onTopicChange">
          <view class="picker-value">
            <text>{{ topicList[topicIndex] }}</text>
            <text class="arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">标题</text>
        <input 
          class="input" 
          v-model="title" 
          placeholder="请输入帖子标题"
          maxlength="50"
        />
      </view>

      <view class="form-item textarea-item">
        <text class="label">内容</text>
        <textarea 
          class="textarea" 
          v-model="content" 
          placeholder="分享你的二手交易经验、心得或问题..."
          maxlength="1000"
        />
      </view>

      <view class="form-item">
        <text class="label">上传图片</text>
        <view class="image-upload">
          <view 
            class="upload-item" 
            v-for="(img, idx) in images" 
            :key="idx"
          >
            <image class="upload-img" :src="img" mode="aspectFill"></image>
            <view class="delete-btn" @click="deleteImage(idx)">×</view>
          </view>
          <view class="upload-btn" @click="chooseImage" v-if="images.length < 9">
            <text class="plus">+</text>
            <text class="upload-tip">添加图片</text>
          </view>
        </view>
      </view>
    </view>

    <view class="submit-section">
      <button class="submit-btn" :disabled="!canSubmit" @click="submitPost">
        发布帖子
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      topicIndex: 0,
      topicList: ['经验分享', '话题讨论', '求助问答', '其他'],
      title: '',
      content: '',
      images: []
    }
  },
  computed: {
    canSubmit() {
      return this.title.trim() && this.content.trim()
    }
  },
  methods: {
    onTopicChange(e) {
      this.topicIndex = e.detail.value
    },
    chooseImage() {
      uni.chooseImage({
        count: 9 - this.images.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.images = this.images.concat(res.tempFilePaths)
        }
      })
    },
    deleteImage(idx) {
      this.images.splice(idx, 1)
    },
    submitPost() {
      uni.showLoading({
        title: '发布中...'
      })
      
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '发布成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      }, 1500)
    }
  }
}
</script>

<style scoped>
.post-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 150rpx;
}

.form-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
}

.form-item {
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  display: block;
  font-size: 30rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.picker-value {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 15rpx;
  font-size: 28rpx;
  color: #666;
}

.arrow {
  color: #999;
  font-size: 36rpx;
}

.input {
  width: 100%;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 15rpx;
  font-size: 28rpx;
  color: #333;
}

.textarea-item {
  border-bottom: none;
}

.textarea {
  width: 100%;
  min-height: 300rpx;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 15rpx;
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
}

.image-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.upload-item {
  position: relative;
  width: 200rpx;
  height: 200rpx;
}

.upload-img {
  width: 100%;
  height: 100%;
  border-radius: 15rpx;
  background-color: #f5f5f5;
}

.delete-btn {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: #ff4d4f;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  line-height: 1;
}

.upload-btn {
  width: 200rpx;
  height: 200rpx;
  border: 2rpx dashed #ddd;
  border-radius: 15rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

.plus {
  font-size: 60rpx;
  color: #ccc;
  line-height: 1;
}

.upload-tip {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.submit-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 50rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.submit-btn[disabled] {
  background: #ccc;
}
</style>
