<template>
  <view class="container">
    <view class="points-header card">
      <view class="points-info">
        <text class="points-title">我的积分</text>
        <text class="points-value">{{ userInfo.points || 0 }}</text>
      </view>
      <view class="points-actions">
        <button class="btn btn-outline" @click="showRecords = !showRecords">
          积分记录
        </button>
      </view>
    </view>

    <view class="records-panel card" v-if="showRecords">
      <text class="card-title">积分记录</text>
      <view class="record-list">
        <view class="record-item" v-for="record in pointRecords" :key="record.id">
          <view class="record-left">
            <text class="record-desc">{{ record.description }}</text>
            <text class="record-time">{{ record.createTime }}</text>
          </view>
          <text class="record-points" :class="record.points > 0 ? 'text-success' : 'text-danger'">
            {{ record.points > 0 ? '+' : '' }}{{ record.points }}
          </text>
        </view>
      </view>
    </view>

    <view class="goods-tabs card">
      <view class="tabs-header">
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeTab === 'all' }"
          @click="activeTab = 'all'"
        >全部</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeTab === 1 }"
          @click="activeTab = 1"
        >话费</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeTab === 2 }"
          @click="activeTab = 2"
        >会员</text>
        <text 
          class="tab-item" 
          :class="{ 'tab-active': activeTab === 3 }"
          @click="activeTab = 3"
        >实物</text>
      </view>
    </view>

    <view class="goods-list">
      <view class="goods-card card" v-for="goods in filteredGoods" :key="goods.id">
        <view class="goods-img" :style="getGoodsBgColor(goods.type)">
          <text>{{ getGoodsIcon(goods.type) }}</text>
        </view>
        <view class="goods-info">
          <text class="goods-name">{{ goods.name }}</text>
          <text class="goods-desc">{{ goods.description }}</text>
          <view class="goods-bottom">
            <text class="goods-points">{{ goods.points }} 积分</text>
            <button 
              class="btn btn-primary exchange-btn"
              :disabled="(userInfo.points || 0) < goods.points"
              @click="handleExchange(goods)"
            >
              兑换
            </button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api.js'

export default {
  data() {
    return {
      userInfo: {},
      showRecords: false,
      pointRecords: [],
      allGoods: [],
      activeTab: 'all'
    }
  },
  computed: {
    filteredGoods() {
      if (this.activeTab === 'all') {
        return this.allGoods
      }
      return this.allGoods.filter(g => g.type === this.activeTab)
    }
  },
  onShow() {
    this.loadUserInfo()
    this.loadPointGoods()
    this.loadPointRecords()
  },
  methods: {
    async loadUserInfo() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          const user = await api.getUserMemberInfo(userId)
          this.userInfo = user
        } catch (e) {
          console.error(e)
        }
      }
    },

    async loadPointGoods() {
      try {
        this.allGoods = await api.getPointGoods()
      } catch (e) {
        console.error(e)
      }
    },

    async loadPointRecords() {
      const userId = uni.getStorageSync('userId')
      if (userId) {
        try {
          this.pointRecords = await api.getPointRecords(userId)
        } catch (e) {
          console.error(e)
        }
      }
    },

    async handleExchange(goods) {
      const userId = uni.getStorageSync('userId')
      if (!userId) return

      uni.showModal({
        title: '确认兑换',
        content: `确认消耗 ${goods.points} 积分兑换 ${goods.name}？`,
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '兑换中...' })
            try {
              await api.exchangeGoods(userId, goods.id)
              uni.hideLoading()
              uni.showToast({
                title: '兑换成功',
                icon: 'success'
              })
              this.loadUserInfo()
              this.loadPointRecords()
            } catch (e) {
              uni.hideLoading()
            }
          }
        }
      })
    },

    getGoodsIcon(type) {
      const icons = {
        1: '📱',
        2: '🎬',
        3: '🎁'
      }
      return icons[type] || '🎁'
    },

    getGoodsBgColor(type) {
      const colors = {
        1: 'background-color: #e8f3ff;',
        2: 'background-color: #e8f9f1;',
        3: 'background-color: #fff3e8;'
      }
      return colors[type] || colors[3]
    }
  }
}
</script>

<style scoped>
.points-header {
  background: linear-gradient(135deg, #1989fa 0%, #4facfe 100%);
  color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.points-title {
  font-size: 26rpx;
  opacity: 0.9;
  display: block;
  margin-bottom: 8rpx;
}

.points-value {
  font-size: 48rpx;
  font-weight: bold;
}

.tabs-header {
  display: flex;
  justify-content: space-around;
}

.tab-item {
  padding: 16rpx 32rpx;
  font-size: 28rpx;
  color: #666;
  border-radius: 8rpx;
}

.tab-item.tab-active {
  color: #1989fa;
  background: #e8f3ff;
  font-weight: 500;
}

.record-list {
  margin-top: 20rpx;
  max-height: 500rpx;
  overflow-y: auto;
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.record-item:last-child {
  border-bottom: none;
}

.record-left {
  flex: 1;
}

.record-desc {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.record-time {
  font-size: 22rpx;
  color: #999;
}

.record-points {
  font-size: 28rpx;
  font-weight: 500;
}

.goods-list {
  margin-top: 20rpx;
}

.goods-card {
  display: flex;
  margin-bottom: 20rpx;
}

.goods-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  margin-right: 24rpx;
}

.goods-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.goods-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 8rpx;
}

.goods-desc {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 16rpx;
}

.goods-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goods-points {
  font-size: 28rpx;
  color: #ff6b35;
  font-weight: 500;
}

.exchange-btn {
  width: 120rpx;
  height: 56rpx;
  line-height: 56rpx;
  padding: 0;
  font-size: 24rpx;
}
</style>
