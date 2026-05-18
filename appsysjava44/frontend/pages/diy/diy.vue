<template>
  <view class="diy-page">
    <view class="preview-section">
      <view class="bouquet-preview">
        <image :src="selectedMainFlower && selectedMainFlower.image || '/static/bouquet-preview.png'" class="preview-image"></image>
        <view class="side-flowers">
          <image v-for="(flower, index) in selectedSideFlowers.slice(0, 3)" :key="index" :src="flower.image" class="side-flower"></image>
        </view>
      </view>
      <view class="estimated-price">
        <text class="price-label">预估价格</text>
        <text class="price-value">¥{{ (estimatedPrice / 100).toFixed(2) }}</text>
      </view>
    </view>
    
    <view class="options-section">
      <view class="option-group">
        <view class="group-title">
          <text class="title-text">主花材</text>
          <text class="selected-count">已选{{ selectedMainFlower ? 1 : 0 }}种</text>
        </view>
        <scroll-view class="options-scroll" scroll-x>
          <view class="options-list">
            <view class="option-item" v-for="flower in mainFlowers" :key="flower.id" @click="selectMainFlower(flower)">
              <image :src="flower.image" class="option-image"></image>
              <text class="option-name">{{ flower.name }}</text>
              <text class="option-price">¥{{ (flower.price / 100).toFixed(2) }}/枝</text>
              <view class="selected-mark" v-if="selectedMainFlower && selectedMainFlower.id === flower.id">✓</view>
            </view>
          </view>
        </scroll-view>
      </view>
      
      <view class="option-group">
        <view class="group-title">
          <text class="title-text">配花</text>
          <text class="selected-count">已选{{ selectedSideFlowers.length }}种</text>
        </view>
        <scroll-view class="options-scroll" scroll-x>
          <view class="options-list">
            <view class="option-item" v-for="flower in sideFlowers" :key="flower.id" @click="toggleSideFlower(flower)">
              <image :src="flower.image" class="option-image"></image>
              <text class="option-name">{{ flower.name }}</text>
              <text class="option-price">¥{{ (flower.price / 100).toFixed(2) }}/枝</text>
              <view class="selected-mark" v-if="isSideFlowerSelected(flower.id)">✓</view>
            </view>
          </view>
        </scroll-view>
      </view>
      
      <view class="option-group">
        <view class="group-title">
          <text class="title-text">包装颜色</text>
        </view>
        <view class="colors-list">
          <view class="color-item" v-for="color in packagingColors" :key="color.value" @click="selectedColor = color.value">
            <view class="color-circle" :style="{ backgroundColor: color.value }"></view>
            <text class="color-name">{{ color.name }}</text>
            <view class="selected-mark" v-if="selectedColor === color.value">✓</view>
          </view>
        </view>
      </view>
      
      <view class="option-group">
        <view class="group-title">
          <text class="title-text">贺卡留言</text>
        </view>
        <textarea class="greeting-input" v-model="greetingText" placeholder="写下您的祝福..." maxlength="100"></textarea>
        <text class="char-count">{{ greetingText.length }}/100</text>
      </view>
    </view>
    
    <view class="bottom-bar">
      <button class="btn btn-default save-btn" @click="saveBouquet">保存设计</button>
      <button class="btn btn-primary order-btn" @click="createOrder">立即下单</button>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      selectedMainFlower: null,
      selectedSideFlowers: [],
      selectedColor: '#FF6B9D',
      greetingText: '',
      mainFlowers: [
        { id: 1, name: '红玫瑰', price: 1500, image: 'https://picsum.photos/200/200?random=101' },
        { id: 2, name: '粉玫瑰', price: 1200, image: 'https://picsum.photos/200/200?random=102' },
        { id: 3, name: '白玫瑰', price: 1300, image: 'https://picsum.photos/200/200?random=103' },
        { id: 4, name: '百合', price: 2000, image: 'https://picsum.photos/200/200?random=104' },
        { id: 5, name: '向日葵', price: 1800, image: 'https://picsum.photos/200/200?random=105' }
      ],
      sideFlowers: [
        { id: 6, name: '满天星', price: 500, image: 'https://picsum.photos/200/200?random=106' },
        { id: 7, name: '尤加利叶', price: 300, image: 'https://picsum.photos/200/200?random=107' },
        { id: 8, name: '小雏菊', price: 400, image: 'https://picsum.photos/200/200?random=108' },
        { id: 9, name: '康乃馨', price: 600, image: 'https://picsum.photos/200/200?random=109' },
        { id: 10, name: '洋桔梗', price: 800, image: 'https://picsum.photos/200/200?random=110' }
      ],
      packagingColors: [
        { name: '玫瑰粉', value: '#FF6B9D' },
        { name: '天空蓝', value: '#87CEEB' },
        { name: '香槟金', value: '#F7E7CE' },
        { name: '珍珠白', value: '#FFFFFF' },
        { name: '典雅紫', value: '#9B59B6' },
        { name: '薄荷绿', value: '#98FB98' }
      ]
    }
  },
  
  computed: {
    estimatedPrice() {
      let total = 0
      if (this.selectedMainFlower) {
        total += this.selectedMainFlower.price * 11
      }
      total += this.selectedSideFlowers.reduce((sum, flower) => sum + flower.price * 3, 0)
      total += 5000
      return total
    }
  },
  
  methods: {
    selectMainFlower(flower) {
      this.selectedMainFlower = flower
    },
    
    toggleSideFlower(flower) {
      const index = this.selectedSideFlowers.findIndex(f => f.id === flower.id)
      if (index > -1) {
        this.selectedSideFlowers.splice(index, 1)
      } else {
        if (this.selectedSideFlowers.length < 3) {
          this.selectedSideFlowers.push(flower)
        } else {
          uni.showToast({
            title: '最多选择3种配花',
            icon: 'none'
          })
        }
      }
    },
    
    isSideFlowerSelected(flowerId) {
      return this.selectedSideFlowers.some(f => f.id === flowerId)
    },
    
    async saveBouquet() {
      if (!this.selectedMainFlower) {
        uni.showToast({
          title: '请选择主花材',
          icon: 'none'
        })
        return
      }
      
      try {
        const bouquet = {
          mainFlowers: this.selectedMainFlower,
          sideFlowers: this.selectedSideFlowers,
          packagingColor: this.selectedColor,
          greetingCard: this.greetingText,
          estimatedPrice: this.estimatedPrice
        }
        
        const res = await api.saveDIYBouquet(bouquet)
        if (res.code === 200) {
          uni.showToast({
            title: '保存成功',
            icon: 'success'
          })
        }
      } catch (e) {
        console.error('保存失败', e)
        uni.showToast({
          title: '保存成功（模拟）',
          icon: 'success'
        })
      }
    },
    
    async createOrder() {
      if (!this.selectedMainFlower) {
        uni.showToast({
          title: '请选择主花材',
          icon: 'none'
        })
        return
      }
      
      uni.showModal({
        title: '确认下单',
        content: `DIY花束价格：¥${(this.estimatedPrice / 100).toFixed(2)}，是否确认下单？`,
        success: (res) => {
          if (res.confirm) {
            uni.showToast({
              title: '下单成功',
              icon: 'success'
            })
            setTimeout(() => {
              uni.switchTab({
                url: '/pages/user/user'
              })
            }, 1500)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.diy-page {
  min-height: 100vh;
  background-color: #F8F8F8;
  padding-bottom: 140rpx;
}

.preview-section {
  background-color: #FFFFFF;
  padding: 40rpx;
  text-align: center;
}

.bouquet-preview {
  position: relative;
  width: 400rpx;
  height: 400rpx;
  margin: 0 auto 30rpx;
  background: linear-gradient(135deg, #FFF5F8 0%, #FFF0F5 100%);
  border-radius: 200rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image {
  width: 250rpx;
  height: 250rpx;
  border-radius: 125rpx;
}

.side-flowers {
  position: absolute;
  width: 100%;
  height: 100%;
}

.side-flower {
  position: absolute;
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
}

.side-flower:nth-child(1) {
  top: 20rpx;
  right: 60rpx;
}

.side-flower:nth-child(2) {
  bottom: 40rpx;
  left: 40rpx;
}

.side-flower:nth-child(3) {
  top: 100rpx;
  left: 20rpx;
}

.estimated-price {
  display: inline-flex;
  align-items: center;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  padding: 15rpx 40rpx;
  border-radius: 40rpx;
}

.price-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-right: 10rpx;
}

.price-value {
  font-size: 36rpx;
  font-weight: bold;
  color: #FFFFFF;
}

.options-section {
  padding: 30rpx;
}

.option-group {
  background-color: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.group-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25rpx;
}

.title-text {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
}

.selected-count {
  font-size: 24rpx;
  color: #FF6B9D;
}

.options-scroll {
  white-space: nowrap;
}

.options-list {
  display: inline-flex;
  gap: 20rpx;
}

.option-item {
  position: relative;
  width: 180rpx;
  text-align: center;
}

.option-image {
  width: 180rpx;
  height: 180rpx;
  border-radius: 16rpx;
  margin-bottom: 15rpx;
}

.option-name {
  display: block;
  font-size: 24rpx;
  color: #333333;
  margin-bottom: 5rpx;
}

.option-price {
  font-size: 22rpx;
  color: #FF6B9D;
}

.selected-mark {
  position: absolute;
  top: 10rpx;
  right: 10rpx;
  width: 40rpx;
  height: 40rpx;
  background: linear-gradient(135deg, #FF6B9D 0%, #FF8E53 100%);
  border-radius: 20rpx;
  color: #FFFFFF;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.colors-list {
  display: flex;
  flex-wrap: wrap;
  gap: 30rpx;
}

.color-item {
  position: relative;
  text-align: center;
  width: 100rpx;
}

.color-circle {
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
  border: 3rpx solid #F0F0F0;
  margin-bottom: 10rpx;
}

.color-name {
  display: block;
  font-size: 22rpx;
  color: #666666;
}

.greeting-input {
  width: 100%;
  height: 150rpx;
  border: 1rpx solid #E8E8E8;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.char-count {
  display: block;
  text-align: right;
  font-size: 22rpx;
  color: #999999;
  margin-top: 10rpx;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  gap: 20rpx;
  padding: 20rpx 30rpx;
  background-color: #FFFFFF;
  border-top: 1rpx solid #F0F0F0;
}

.save-btn {
  flex: 1;
  height: 80rpx;
  font-size: 28rpx;
}

.order-btn {
  flex: 2;
  height: 80rpx;
  font-size: 28rpx;
}
</style>
