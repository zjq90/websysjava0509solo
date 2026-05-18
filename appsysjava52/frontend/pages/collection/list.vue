<template>
  <view class="container">
    <!-- 搜索和筛选 -->
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索收藏..." 
        v-model="keyword"
        @confirm="searchCollection"
      />
      <view class="filter-btn" @click="showFilter = !showFilter">
        <text>筛选</text>
      </view>
    </view>

    <!-- 筛选面板 -->
    <view class="filter-panel" v-if="showFilter">
      <view class="filter-title">按类别筛选</view>
      <view class="category-list">
        <view 
          class="category-tag"
          :class="{ active: currentCategory === null }"
          @click="selectCategory(null)"
        >全部</view>
        <view 
          class="category-tag"
          :class="{ active: currentCategory === item.id }"
          v-for="item in categories" 
          :key="item.id"
          @click="selectCategory(item.id)"
        >{{ item.name }}</view>
      </view>
    </view>

    <!-- 收藏列表 -->
    <view class="collection-list">
      <view 
        class="collection-item card" 
        v-for="item in collectionList" 
        :key="item.id"
        @click="goToDetail(item)"
      >
        <image 
          class="item-image" 
          :src="item.customImages ? item.customImages.split(',')[0] : '/static/default.png'" 
          mode="aspectFill"
        ></image>
        <view class="item-info">
          <text class="item-name">{{ item.customName || '未命名文物' }}</text>
          <view class="item-meta">
            <text class="meta-tag">{{ getCategoryName(item.category) }}</text>
            <text class="meta-date">{{ formatDate(item.collectionDate) }}</text>
          </view>
          <view class="item-tags" v-if="item.tags">
            <text class="tag" v-for="tag in item.tags.split(',')" :key="tag">{{ tag }}</text>
          </view>
          <view class="item-footer">
            <text class="item-value">估值：{{ formatValue(item.estimatedValue) }}</text>
            <view class="env-badge" v-if="item.envMonitorEnabled === 1">
              <text>📡 监测中</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="collectionList.length === 0">
      <text class="empty-icon">📦</text>
      <text class="empty-text">暂无收藏文物</text>
      <button class="add-btn" @click="goToAdd">添加收藏</button>
    </view>

    <!-- 添加按钮 -->
    <view class="float-add-btn" @click="goToAdd">
      <text>+</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      keyword: '',
      currentCategory: null,
      showFilter: false,
      categories: [
        { id: 1, name: '青铜器' },
        { id: 2, name: '陶瓷' },
        { id: 3, name: '书画' },
        { id: 4, name: '玉器' },
        { id: 5, name: '杂项' }
      ],
      collectionList: []
    }
  },
  onLoad() {
    this.loadCollectionList()
  },
  onPullDownRefresh() {
    this.loadCollectionList()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    async loadCollectionList() {
      try {
        const res = await request.get('/collection/my-collection', {
          userId: 2,
          category: this.currentCategory,
          tag: this.keyword || undefined
        })
        if (res.code === 200) {
          this.collectionList = res.data || []
        }
      } catch (e) {
        // 模拟数据
        this.collectionList = [
          {
            id: 1,
            customName: '清代青花瓷盘',
            category: 2,
            material: '瓷',
            tags: '青花瓷,清代,祖传',
            collectionDate: '2023-05-15',
            estimatedValue: 1500000,
            remark: '祖传之物，倍加珍惜',
            envMonitorEnabled: 1,
            deviceId: 'SENSOR-002'
          },
          {
            id: 2,
            customName: '民国玉佩',
            category: 4,
            material: '和田玉',
            tags: '玉器,民国,配饰',
            collectionDate: '2023-03-20',
            estimatedValue: 80000,
            envMonitorEnabled: 0
          }
        ]
      }
    },
    searchCollection() {
      this.loadCollectionList()
    },
    selectCategory(id) {
      this.currentCategory = id
      this.loadCollectionList()
    },
    getCategoryName(category) {
      const cat = this.categories.find(c => c.id === category)
      return cat ? cat.name : '未知'
    },
    formatDate(date) {
      if (!date) return ''
      return date
    },
    formatValue(value) {
      if (!value) return '暂无估值'
      if (value >= 10000) {
        return (value / 10000).toFixed(2) + ' 万元'
      }
      return value + ' 元'
    },
    goToDetail(item) {
      uni.navigateTo({
        url: `/pages/collection/detail?id=${item.id}`
      })
    },
    goToAdd() {
      uni.navigateTo({
        url: '/pages/collection/add'
      })
    }
  }
}
</script>

<style scoped lang="scss">
.container {
  padding: 20rpx;
}

.search-bar {
  display: flex;
  gap: 20rpx;
  margin-bottom: 20rpx;
}

.search-input {
  flex: 1;
  background: #fff;
  border-radius: 50rpx;
  padding: 20rpx 30rpx;
  font-size: 28rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.filter-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 20rpx 30rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
  white-space: nowrap;
}

.filter-panel {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.filter-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
}

.category-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.category-tag {
  padding: 12rpx 24rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
}

.category-tag.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.collection-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.collection-item {
  display: flex;
  padding: 24rpx;
}

.item-image {
  width: 180rpx;
  height: 180rpx;
  border-radius: 12rpx;
  margin-right: 24rpx;
  background: #f0f0f0;
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.item-name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 12rpx;
}

.item-meta {
  display: flex;
  gap: 20rpx;
  margin-bottom: 12rpx;
}

.meta-tag {
  background: #f0f0ff;
  color: #667eea;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.meta-date {
  font-size: 24rpx;
  color: #999;
}

.item-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 16rpx;
}

.tag {
  background: #fff3e0;
  color: #ff9800;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
}

.item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.item-value {
  font-size: 26rpx;
  color: #ff6b6b;
  font-weight: 500;
}

.env-badge {
  background: #e8f5e9;
  color: #4caf50;
  padding: 8rpx 16rpx;
  border-radius: 30rpx;
  font-size: 22rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #999;
  margin-bottom: 40rpx;
}

.add-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: none;
  border-radius: 50rpx;
  padding: 20rpx 60rpx;
  font-size: 28rpx;
}

.float-add-btn {
  position: fixed;
  right: 40rpx;
  bottom: 120rpx;
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 48rpx;
  font-weight: bold;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
  z-index: 999;
}
</style>