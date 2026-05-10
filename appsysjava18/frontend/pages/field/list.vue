<template>
  <view class="container">
    <view class="search-bar">
      <view class="search-input-wrapper">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索地块或作物"
          placeholder-class="placeholder"
          @confirm="loadData"
        />
      </view>
      <view class="search-btn" @click="loadData">搜索</view>
    </view>

    <view class="filter-bar">
      <view class="filter-item" @click="showCropFilter">
        <text>{{ currentCrop || '全部作物' }}</text>
        <text class="filter-arrow">›</text>
      </view>
    </view>

    <view class="list-section" v-if="fieldList.length > 0">
      <view 
        class="field-card" 
        v-for="item in fieldList" 
        :key="item.id"
        @click="showDetail(item)"
      >
        <view class="card-header">
          <view class="field-info">
            <text class="field-name">{{ item.fieldName || '未知地块' }}</text>
            <text class="crop-name">{{ item.cropName || '未指定作物' }}</text>
          </view>
          <view class="record-date">{{ formatDate(item.recordDate) }}</view>
        </view>
        <view class="card-body">
          <view class="info-grid">
            <view class="info-item">
              <text class="info-icon">🌡️</text>
              <text class="info-text">{{ item.temperature || '-' }}°C</text>
            </view>
            <view class="info-item">
              <text class="info-icon">💧</text>
              <text class="info-text">{{ item.humidity || '-' }}%</text>
            </view>
            <view class="info-item">
              <text class="info-icon">🌱</text>
              <text class="info-text">{{ item.growthStage || '-' }}</text>
            </view>
            <view class="info-item">
              <text class="info-icon">💊</text>
              <text class="info-text">{{ item.pestStatus || '-' }}</text>
            </view>
          </view>
        </view>
        <view class="card-actions">
          <view class="action-btn" @click.stop="showDetail(item)">查看详情</view>
          <view class="action-btn primary" @click.stop="editItem(item)">编辑</view>
          <view class="action-btn danger" @click.stop="deleteItem(item)">删除</view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">🌾</text>
      <text class="empty-text">暂无田间记录</text>
    </view>

    <view class="add-btn" @click="addField">
      <text class="add-icon">+</text>
    </view>
  </view>
</template>

<script>
import { getFieldList, deleteField } from '@/api/field'

export default {
  data() {
    return {
      searchKeyword: '',
      currentCrop: '',
      fieldList: []
    }
  },

  onShow() {
    this.loadData()
  },

  methods: {
    // 加载数据
    async loadData() {
      try {
        const params = {
          page: 0,
          size: 100
        }
        const res = await getFieldList(params)
        this.fieldList = res.content || []
      } catch (e) {
        console.error('加载田间记录列表失败', e)
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '-'
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    // 显示作物筛选
    showCropFilter() {
      uni.showActionSheet({
        itemList: ['全部作物', '水稻', '小麦', '玉米', '大豆', '蔬菜'],
        success: (res) => {
          const crops = ['', '水稻', '小麦', '玉米', '大豆', '蔬菜']
          this.currentCrop = crops[res.tapIndex]
          this.loadData()
        }
      })
    },

    // 显示详情
    showDetail(item) {
      uni.showModal({
        title: item.fieldName || '田间记录详情',
        content: `
地块: ${item.fieldName || '-'}
作物: ${item.cropName || '-'}
记录日期: ${this.formatDate(item.recordDate)}
温度: ${item.temperature || '-'}°C
湿度: ${item.humidity || '-'}%
生长阶段: ${item.growthStage || '-'}
病虫害: ${item.pestStatus || '-'}
施肥情况: ${item.fertilizerStatus || '-'}
浇水情况: ${item.irrigationStatus || '-'}
备注: ${item.remark || '无'}
        `,
        showCancel: false
      })
    },

    // 新增记录
    addField() {
      uni.navigateTo({
        url: '/pages/field/form'
      })
    },

    // 编辑记录
    editItem(item) {
      uni.navigateTo({
        url: '/pages/field/form?id=' + item.id
      })
    },

    // 删除记录
    deleteItem(item) {
      uni.showModal({
        title: '提示',
        content: '确定要删除该田间记录吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await deleteField(item.id)
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadData()
            } catch (e) {
              console.error('删除失败', e)
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  padding: 30rpx;
  padding-bottom: 150rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.search-bar {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.search-input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 0 24rpx;
  height: 88rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
}

.placeholder {
  color: #999999;
}

.search-btn {
  background: #007AFF;
  color: #FFFFFF;
  padding: 0 30rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.filter-bar {
  display: flex;
  gap: 20rpx;
  margin-bottom: 30rpx;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: #FFFFFF;
  padding: 16rpx 24rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666666;
}

.filter-arrow {
  font-size: 28rpx;
  color: #CCCCCC;
}

.field-card {
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #F5F5F5;
}

.field-info {
  display: flex;
  flex-direction: column;
}

.field-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 6rpx;
}

.crop-name {
  font-size: 24rpx;
  color: #34C759;
}

.record-date {
  font-size: 24rpx;
  color: #999999;
}

.card-body {
  margin-bottom: 20rpx;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 16rpx;
  background: #F5F5F5;
  border-radius: 12rpx;
}

.info-icon {
  font-size: 28rpx;
}

.info-text {
  font-size: 26rpx;
  color: #333333;
}

.card-actions {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  flex: 1;
  padding: 16rpx;
  border-radius: 12rpx;
  text-align: center;
  font-size: 26rpx;
  background: #F5F5F5;
  color: #666666;
}

.action-btn.primary {
  background: #007AFF1A;
  color: #007AFF;
}

.action-btn.danger {
  background: #FF3B301A;
  color: #FF3B30;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999999;
}

.add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 60rpx;
  width: 110rpx;
  height: 110rpx;
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.4);
}

.add-icon {
  font-size: 60rpx;
  color: #FFFFFF;
}
</style>
