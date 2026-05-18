<template>
  <view class="container">
    <view class="form-section">
      <view class="form-item">
        <text class="label">商品标题</text>
        <input v-model="form.title" placeholder="请输入商品标题" class="input" maxlength="50" />
      </view>

      <view class="form-item">
        <text class="label">商品描述</text>
        <textarea v-model="form.description" placeholder="请输入商品详细描述" class="textarea" maxlength="500" />
      </view>

      <view class="form-item">
        <text class="label">商品价格</text>
        <input v-model="form.price" type="digit" placeholder="请输入价格" class="input" />
      </view>

      <view class="form-item">
        <text class="label">商品分类</text>
        <picker :range="categories" @change="onCategoryChange">
          <view class="picker-value">{{ form.category || '请选择分类' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">商品成色</text>
        <picker :range="conditions" @change="onConditionChange">
          <view class="picker-value">{{ form.condition || '请选择成色' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">商品图片</text>
        <view class="image-upload">
          <view class="image-item" v-for="(img, index) in images" :key="index">
            <image :src="img" class="uploaded-image" mode="aspectFill" />
            <text class="delete-btn" @click="deleteImage(index)">×</text>
          </view>
          <view class="upload-btn" v-if="images.length < 6" @click="chooseImage">
            <text class="upload-icon">+</text>
            <text class="upload-text">上传图片</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <text class="label">交易地点</text>
        <view class="location-row">
          <input v-model="form.address" placeholder="请输入交易地点" class="input location-input" />
          <button class="location-btn" @click="getLocation">📍</button>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <button class="publish-btn" @click="publish" :disabled="loading">
        {{ loading ? '发布中...' : '发布商品' }}
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        title: '',
        description: '',
        price: '',
        category: '',
        condition: '',
        address: '',
        latitude: null,
        longitude: null
      },
      images: [],
      categories: ['手机数码', '电脑办公', '家用电器', '服装鞋帽', '图书文具', '运动户外', '其他'],
      conditions: ['全新', '99新', '95新', '9成新', '8成新', '7成新及以下'],
      loading: false
    }
  },
  methods: {
    onCategoryChange(e) {
      this.form.category = this.categories[e.detail.value]
    },
    onConditionChange(e) {
      this.form.condition = this.conditions[e.detail.value]
    },
    chooseImage() {
      uni.chooseImage({
        count: 6 - this.images.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.images = this.images.concat(res.tempFilePaths)
        }
      })
    },
    deleteImage(index) {
      this.images.splice(index, 1)
    },
    getLocation() {
      uni.getLocation({
        type: 'gcj02',
        success: (res) => {
          this.form.latitude = res.latitude
          this.form.longitude = res.longitude
          uni.showToast({
            title: '定位成功',
            icon: 'success'
          })
        }
      })
    },
    async publish() {
      if (!this.form.title) {
        uni.showToast({
          title: '请输入商品标题',
          icon: 'none'
        })
        return
      }
      if (!this.form.price) {
        uni.showToast({
          title: '请输入商品价格',
          icon: 'none'
        })
        return
      }
      if (this.images.length === 0) {
        uni.showToast({
          title: '请上传商品图片',
          icon: 'none'
        })
        return
      }

      this.loading = true
      try {
        const res = await this.$request({
          url: '/product/create',
          method: 'POST',
          data: {
            ...this.form,
            images: this.images.join(','),
            price: parseFloat(this.form.price)
          }
        })

        uni.showToast({
          title: '发布成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.form-section {
  background: #fff;
  padding: 20rpx 30rpx;
}

.form-item {
  padding: 30rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 20rpx;
  font-weight: 500;
}

.input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  background: #f8f8f8;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.textarea {
  width: 100%;
  min-height: 200rpx;
  padding: 20rpx;
  background: #f8f8f8;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.picker-value {
  height: 80rpx;
  line-height: 80rpx;
  padding: 0 20rpx;
  background: #f8f8f8;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #666;
}

.image-upload {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.image-item {
  position: relative;
  width: 200rpx;
  height: 200rpx;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  border-radius: 12rpx;
}

.delete-btn {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 40rpx;
  height: 40rpx;
  line-height: 40rpx;
  text-align: center;
  background: #ff4d4f;
  color: #fff;
  border-radius: 50%;
  font-size: 32rpx;
}

.upload-btn {
  width: 200rpx;
  height: 200rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8f8f8;
  border-radius: 12rpx;
  border: 2rpx dashed #ddd;
}

.upload-icon {
  font-size: 60rpx;
  color: #999;
}

.upload-text {
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

.location-row {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.location-input {
  flex: 1;
}

.location-btn {
  width: 80rpx;
  height: 80rpx;
  line-height: 80rpx;
  text-align: center;
  background: #409eff;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  border: none;
  padding: 0;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.1);
}

.publish-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  background: linear-gradient(135deg, #ff6b6b 0%, #ff4757 100%);
  color: #fff;
  border-radius: 44rpx;
  font-size: 32rpx;
  border: none;
}

.publish-btn[disabled] {
  opacity: 0.6;
}
</style>
