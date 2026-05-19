<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="search-bar">
      <input 
        class="search-input" 
        v-model="searchKeyword" 
        placeholder="搜索药品名称"
        @confirm="searchMedicine"
      />
      <text class="search-icon">🔍</text>
    </view>

    <view class="category-section">
      <text class="section-title">药品分类</text>
      <scroll-view class="category-scroll" scroll-x="true">
        <view 
          class="category-item" 
          :class="{ active: selectedCategory === category.id }"
          v-for="category in categories" 
          :key="category.id"
          @click="selectCategory(category)"
        >
          <text class="category-icon">{{ category.icon }}</text>
          <text class="category-name">{{ category.name }}</text>
        </view>
      </scroll-view>
    </view>

    <view class="prescription-section" v-if="selectedCategory === 'prescription'">
      <view class="prescription-header">
        <text class="section-title">我的处方</text>
        <text class="add-prescription" @click="uploadPrescription">+ 添加处方</text>
      </view>
      <view class="prescription-list">
        <view class="prescription-item" v-for="prescription in prescriptions" :key="prescription.id">
          <view class="prescription-info">
            <text class="prescription-no">处方号：{{ prescription.no }}</text>
            <text class="prescription-doctor">医生：{{ prescription.doctor }}</text>
            <text class="prescription-date">日期：{{ prescription.date }}</text>
          </view>
          <view class="prescription-status" :class="{ used: prescription.used }">
            <text>{{ prescription.used ? '已使用' : '有效' }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="medicine-section">
      <text class="section-title">{{ selectedCategory === 'prescription' ? '处方药' : '推荐药品' }}</text>
      <view class="medicine-grid">
        <view class="medicine-card" v-for="medicine in medicines" :key="medicine.id" @click="viewMedicine(medicine)">
          <view class="medicine-image">
            <text class="medicine-icon">💊</text>
          </view>
          <view class="medicine-info">
            <text class="medicine-name">{{ medicine.name }}</text>
            <text class="medicine-spec">{{ medicine.spec }}</text>
            <text class="medicine-price">¥{{ medicine.price }}</text>
          </view>
          <view class="medicine-action">
            <button class="buy-btn" @click.stop="buyMedicine(medicine)">购买</button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { medicineApi } from '@/utils/api.js'

export default {
  data() {
    return {
      isElderMode: false,
      searchKeyword: '',
      selectedCategory: null,
      categories: [
        { id: 'deworming', name: '驱虫药', icon: '🐛' },
        { id: 'nutrition', name: '营养膏', icon: '🥗' },
        { id: 'cold', name: '感冒药', icon: '🤒' },
        { id: 'stomach', name: '肠胃药', icon: '🩹' },
        { id: 'skin', name: '皮肤药', icon: '🟡' },
        { id: 'vaccine', name: '疫苗', icon: '💉' },
        { id: 'health', name: '保健品', icon: '❤️' },
        { id: 'prescription', name: '处方药', icon: '📋' }
      ],
      prescriptions: [
        { id: 1, no: 'P202401001', doctor: '王医生', date: '2024-01-15', used: false },
        { id: 2, no: 'P202401002', doctor: '李医生', date: '2024-01-10', used: true }
      ],
      medicines: []
    }
  },
  onLoad() {
    this.checkElderMode()
    this.loadMedicines()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
    },
    selectCategory(category) {
      this.selectedCategory = category.id
      this.loadMedicines()
    },
    async loadMedicines() {
      try {
        const res = await medicineApi.getMedicineList({ category: this.selectedCategory })
        if (res.code === 200 || res.code === 0) {
          this.medicines = res.data || []
        }
      } catch (e) {
        console.log('加载药品失败', e)
        // 使用模拟数据
        this.medicines = [
          { id: 1, name: '体内驱虫片', spec: '12片/盒', price: 68 },
          { id: 2, name: '体外驱虫滴剂', spec: '3支/盒', price: 128 },
          { id: 3, name: '综合营养膏', spec: '120g/支', price: 88 },
          { id: 4, name: '宠物感冒颗粒', spec: '10袋/盒', price: 45 },
          { id: 5, name: '皮肤消炎膏', spec: '15g/支', price: 55 },
          { id: 6, name: '狂犬疫苗', spec: '1针/盒', price: 80 },
          { id: 7, name: '复合维生素片', spec: '60片/瓶', price: 65 },
          { id: 8, name: '益生菌粉', spec: '20袋/盒', price: 58 }
        ]
      }
    },
    searchMedicine() {
      uni.showToast({ title: '搜索中...', icon: 'loading' })
      setTimeout(() => {
        uni.hideToast()
      }, 500)
    },
    uploadPrescription() {
      uni.showActionSheet({
        itemList: ['拍照上传', '从相册选择'],
        success: (res) => {
          uni.showToast({ title: '上传成功', icon: 'success' })
        }
      })
    },
    viewMedicine(medicine) {
      uni.showModal({
        title: medicine.name,
        content: `规格：${medicine.spec}\n价格：¥${medicine.price}\n\n请按医嘱或说明书使用。`,
        showCancel: false
      })
    },
    buyMedicine(medicine) {
      uni.showModal({
        title: '确认购买',
        content: `确认购买 ${medicine.name}？\n价格：¥${medicine.price}`,
        success: (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '提交订单...' })
            setTimeout(() => {
              uni.hideLoading()
              uni.showToast({
                title: '下单成功！',
                icon: 'success'
              })
            }, 1000)
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 20rpx;
}

.search-bar {
  position: relative;
  margin-bottom: 30rpx;
}

.search-input {
  width: 100%;
  height: 80rpx;
  background: #fff;
  border-radius: 40rpx;
  padding: 0 80rpx 0 30rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.search-icon {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
  font-size: 36rpx;
}

.section-title {
  display: block;
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.category-section {
  margin-bottom: 30rpx;
}

.category-scroll {
  white-space: nowrap;
}

.category-item {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 24rpx;
  background: #fff;
  border-radius: 12rpx;
  margin-right: 16rpx;
  border: 2rpx solid transparent;
}

.category-item.active {
  background: #E8F5E9;
  border-color: #4CAF50;
}

.category-icon {
  font-size: 40rpx;
  margin-bottom: 8rpx;
}

.category-name {
  font-size: 24rpx;
  color: #333;
}

.prescription-section {
  margin-bottom: 30rpx;
}

.prescription-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.add-prescription {
  font-size: 26rpx;
  color: #4CAF50;
}

.prescription-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.prescription-item {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.prescription-info {
  flex: 1;
}

.prescription-no,
.prescription-doctor,
.prescription-date {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 6rpx;
}

.prescription-status {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  background: #E8F5E9;
  color: #4CAF50;
  font-size: 24rpx;
}

.prescription-status.used {
  background: #F5F5F5;
  color: #999;
}

.medicine-section {
  margin-bottom: 30rpx;
}

.medicine-grid {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.medicine-card {
  background: #fff;
  border-radius: 12rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
}

.medicine-image {
  width: 120rpx;
  height: 120rpx;
  background: #F5F5F5;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.medicine-icon {
  font-size: 60rpx;
}

.medicine-info {
  flex: 1;
}

.medicine-name {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.medicine-spec {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.medicine-price {
  display: block;
  font-size: 30rpx;
  font-weight: bold;
  color: #FF5722;
}

.medicine-action {
  margin-left: 20rpx;
}

.buy-btn {
  background: linear-gradient(135deg, #4CAF50, #45a049);
  color: #fff;
  border: none;
  border-radius: 30rpx;
  padding: 16rpx 32rpx;
  font-size: 26rpx;
}

.elder-mode .search-input {
  height: 100rpx;
  font-size: 32rpx;
}

.elder-mode .section-title {
  font-size: 36rpx;
}

.elder-mode .category-name {
  font-size: 28rpx;
}

.elder-mode .medicine-name,
.elder-mode .medicine-price {
  font-size: 34rpx;
}

.elder-mode .buy-btn {
  font-size: 30rpx;
  padding: 20rpx 40rpx;
}
</style>
