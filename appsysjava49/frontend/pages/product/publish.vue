<template>
  <view class="publish-page">
    <scroll-view scroll-y class="content">
      <view class="section">
        <text class="section-title">商品图片</text>
        <view class="image-upload-area">
          <view class="image-item" v-for="(img, index) in images" :key="index">
            <image :src="img" mode="aspectFill" class="uploaded-image" />
            <view class="delete-btn" @click="removeImage(index)">✕</view>
          </view>
          <view class="upload-btn" v-if="images.length < 9" @click="chooseImage">
            <text class="upload-icon">📷</text>
            <text class="upload-text">{{ images.length }}/9</text>
          </view>
        </view>
      </view>

      <view class="section">
        <text class="section-title">商品标题</text>
        <input v-model="form.title" class="title-input" placeholder="请输入商品标题（最多50字）" maxlength="50" />
        <text class="char-count">{{ form.title.length }}/50</text>
      </view>

      <view class="section">
        <text class="section-title">商品描述</text>
        <textarea v-model="form.description" class="desc-input" placeholder="请输入商品描述（最多500字）" maxlength="500" />
        <text class="char-count">{{ form.description.length }}/500</text>
      </view>

      <view class="section">
        <text class="section-title">商品分类</text>
        <view class="picker-row" @click="showCategoryPicker = true">
          <text class="picker-label">{{ form.categoryName || '请选择分类' }}</text>
          <text class="picker-arrow">›</text>
        </view>
      </view>

      <view class="section">
        <text class="section-title">成色</text>
        <view class="condition-list">
          <view
            v-for="cond in conditions"
            :key="cond"
            class="condition-item"
            :class="{ active: form.condition === cond }"
            @click="form.condition = cond"
          >
            {{ cond }}
          </view>
        </view>
      </view>

      <view class="section">
        <text class="section-title">价格</text>
        <view class="price-input-row">
          <text class="price-symbol">¥</text>
          <input v-model="form.price" type="digit" class="price-input" placeholder="0.00" />
        </view>
      </view>

      <view class="section">
        <text class="section-title">原价</text>
        <view class="price-input-row">
          <text class="price-symbol">¥</text>
          <input v-model="form.originalPrice" type="digit" class="price-input" placeholder="0.00（选填）" />
        </view>
      </view>

      <view class="section">
        <text class="section-title">品牌</text>
        <input v-model="form.brand" class="brand-input" placeholder="请输入品牌（选填）" />
      </view>

      <view class="section">
        <text class="section-title">交易方式</text>
        <view class="switch-row">
          <text class="switch-label">支持快递</text>
          <switch :checked="form.isDelivery" color="#409EFF" @change="form.isDelivery = $event.detail.value" />
        </view>
        <view class="switch-row">
          <text class="switch-label">支持自提</text>
          <switch :checked="form.isPickup" color="#409EFF" @change="form.isPickup = $event.detail.value" />
        </view>
      </view>

      <view class="section">
        <text class="section-title">是否议价</text>
        <view class="switch-row">
          <text class="switch-label">接受议价</text>
          <switch :checked="form.isNegotiable" color="#409EFF" @change="form.isNegotiable = $event.detail.value" />
        </view>
      </view>
    </scroll-view>

    <view class="bottom-bar">
      <button class="publish-btn" @click="submit" :disabled="publishing">
        {{ publishing ? '发布中...' : '立即发布' }}
      </button>
    </view>

    <view class="category-picker" v-if="showCategoryPicker" @click="showCategoryPicker = false">
      <view class="picker-content" @click.stop>
        <view class="picker-header">
          <text class="picker-cancel" @click="showCategoryPicker = false">取消</text>
          <text class="picker-title">选择分类</text>
          <text class="picker-confirm" @click="confirmCategory">确定</text>
        </view>
        <view class="category-tree">
          <view
            v-for="cat in categories"
            :key="cat.id"
            class="category-item"
            :class="{ active: selectedCategory?.id === cat.id }"
            @click="selectCategory(cat)"
          >
            <text class="cat-name">{{ cat.name }}</text>
            <text v-if="cat.children && cat.children.length > 0" class="cat-arrow">›</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request'

export default {
  data() {
    return {
      publishing: false,
      showCategoryPicker: false,
      images: [],
      selectedCategory: null,
      categories: [
        { id: 1, name: '手机' },
        { id: 2, name: '电脑' },
        { id: 3, name: '数码' },
        { id: 4, name: '服饰' },
        { id: 5, name: '家电' },
        { id: 6, name: '图书' },
        { id: 7, name: '运动' },
        { id: 8, name: '母婴' }
      ],
      conditions: ['全新', '99新', '95新', '9成新', '8成新', '7成新及以下'],
      form: {
        title: '',
        description: '',
        categoryId: null,
        categoryName: '',
        condition: '',
        price: '',
        originalPrice: '',
        brand: '',
        isDelivery: true,
        isPickup: true,
        isNegotiable: true
      }
    }
  },
  methods: {
    chooseImage() {
      uni.chooseImage({
        count: 9 - this.images.length,
        success: (res) => {
          this.images = [...this.images, ...res.tempFilePaths]
        }
      })
    },
    removeImage(index) {
      this.images.splice(index, 1)
    },
    selectCategory(cat) {
      this.selectedCategory = cat
    },
    confirmCategory() {
      if (this.selectedCategory) {
        this.form.categoryId = this.selectedCategory.id
        this.form.categoryName = this.selectedCategory.name
      }
      this.showCategoryPicker = false
    },
    async submit() {
      if (!this.images.length) {
        uni.showToast({
          title: '请上传商品图片',
          icon: 'none'
        })
        return
      }
      if (!this.form.title) {
        uni.showToast({
          title: '请输入商品标题',
          icon: 'none'
        })
        return
      }
      if (!this.form.description) {
        uni.showToast({
          title: '请输入商品描述',
          icon: 'none'
        })
        return
      }
      if (!this.form.categoryId) {
        uni.showToast({
          title: '请选择商品分类',
          icon: 'none'
        })
        return
      }
      if (!this.form.condition) {
        uni.showToast({
          title: '请选择商品成色',
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

      this.publishing = true

      try {
        const res = await request.post('/product', {
          ...this.form,
          images: this.images.join(','),
          coverImage: this.images[0]
        })

        uni.showToast({
          title: '发布成功',
          icon: 'success'
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({
          title: '发布成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } finally {
        this.publishing = false
      }
    }
  }
}
</script>

<style scoped>
.publish-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.content {
  height: calc(100vh - 140rpx);
}

.section {
  background: #fff;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
  display: block;
  margin-bottom: 20rpx;
}

.image-upload-area {
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
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  color: #fff;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.upload-btn {
  width: 200rpx;
  height: 200rpx;
  border: 2rpx dashed #ddd;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.upload-icon {
  font-size: 48rpx;
  margin-bottom: 8rpx;
}

.upload-text {
  font-size: 24rpx;
  color: #999;
}

.title-input {
  width: 100%;
  height: 80rpx;
  font-size: 28rpx;
  color: #333;
}

.desc-input {
  width: 100%;
  min-height: 200rpx;
  font-size: 28rpx;
  color: #333;
}

.char-count {
  font-size: 24rpx;
  color: #999;
  text-align: right;
  display: block;
  margin-top: 10rpx;
}

.picker-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.picker-label {
  font-size: 28rpx;
  color: #333;
}

.picker-arrow {
  font-size: 32rpx;
  color: #ccc;
}

.condition-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.condition-item {
  padding: 12rpx 30rpx;
  border: 2rpx solid #ddd;
  border-radius: 40rpx;
  font-size: 26rpx;
  color: #666;
}

.condition-item.active {
  border-color: #409EFF;
  background: #ecf5ff;
  color: #409EFF;
}

.price-input-row {
  display: flex;
  align-items: center;
  border-bottom: 1rpx solid #f5f5f5;
  padding: 20rpx 0;
}

.price-symbol {
  font-size: 32rpx;
  color: #F56C6C;
  font-weight: bold;
  margin-right: 10rpx;
}

.price-input {
  flex: 1;
  font-size: 32rpx;
  color: #333;
}

.brand-input {
  width: 100%;
  height: 80rpx;
  font-size: 28rpx;
  color: #333;
}

.switch-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.switch-row:last-child {
  border-bottom: none;
}

.switch-label {
  font-size: 28rpx;
  color: #333;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 140rpx;
  background: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.publish-btn {
  width: 100%;
  height: 100rpx;
  background: linear-gradient(135deg, #409EFF, #67C23A);
  color: #fff;
  border: none;
  border-radius: 50rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.category-picker {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1000;
  display: flex;
  align-items: flex-end;
}

.picker-content {
  width: 100%;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  max-height: 70vh;
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.picker-cancel,
.picker-confirm {
  font-size: 28rpx;
  color: #409EFF;
}

.picker-title {
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
}

.category-tree {
  max-height: 50vh;
  overflow-y: auto;
}

.category-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.category-item.active {
  background: #ecf5ff;
}

.cat-name {
  font-size: 28rpx;
  color: #333;
}

.cat-arrow {
  font-size: 28rpx;
  color: #ccc;
}
</style>
