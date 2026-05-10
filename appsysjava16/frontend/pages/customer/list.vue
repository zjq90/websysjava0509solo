<template>
  <view class="container">
    <view class="search-bar">
      <input 
        class="search-input" 
        placeholder="搜索客户姓名..." 
        v-model="searchKeyword"
        @confirm="searchCustomers"
      />
      <button class="search-btn" @click="searchCustomers">搜索</button>
    </view>

    <view class="filter-row">
      <view 
        :class="['filter-tab', filterType === 'all' ? 'active' : '']" 
        @click="setFilter('all')"
      >全部</view>
      <view 
        :class="['filter-tab', filterType === 'regular' ? 'active' : '']" 
        @click="setFilter('regular')"
      >正式客户</view>
      <view 
        :class="['filter-tab', filterType === 'temporary' ? 'active' : '']" 
        @click="setFilter('temporary')"
      >临时客户</view>
    </view>

    <view class="quick-add" @click="goToAdd">
      <text class="add-icon">+</text>
      <text class="add-text">添加客户</text>
    </view>

    <view class="quick-add temp" @click="addTemporary">
      <text class="add-icon">🏷️</text>
      <text class="add-text">快速注册临时客户</text>
    </view>

    <view v-if="customerList.length > 0">
      <view 
        class="customer-card" 
        v-for="customer in customerList" 
        :key="customer.id"
        @click="goToDetail(customer.id)"
      >
        <view class="customer-avatar">
          <text>{{ customer.name.charAt(0) }}</text>
        </view>
        <view class="customer-info">
          <view class="customer-header">
            <text class="customer-name">{{ customer.name }}</text>
            <view :class="['level-tag', getLevelClass(customer.level)]">
              {{ getLevelText(customer.level) }}
            </view>
          </view>
          <view class="customer-meta">
            <text class="text-muted">累计消费：</text>
            <text class="text-primary">¥{{ customer.totalPurchaseAmount || 0 }}</text>
          </view>
          <view class="customer-meta" v-if="customer.isTemporary">
            <text class="temp-badge">临时客户</text>
          </view>
        </view>
        <view class="customer-arrow">›</view>
      </view>
    </view>
    <view v-else class="empty">暂无客户数据</view>

    <view v-if="showTempDialog" class="dialog-mask" @click="showTempDialog = false">
      <view class="dialog-content" @click.stop>
        <view class="dialog-title">快速注册临时客户</view>
        <input 
          class="dialog-input" 
          placeholder="请输入客户姓名" 
          v-model="tempName"
        />
        <input 
          class="dialog-input" 
          placeholder="请输入手机号" 
          type="number"
          v-model="tempPhone"
        />
        <view class="dialog-actions">
          <button class="dialog-btn cancel" @click="showTempDialog = false">取消</button>
          <button class="dialog-btn confirm" @click="submitTemporary">确定</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import customerApi from '@/api/customer.js'

export default {
  data() {
    return {
      searchKeyword: '',
      filterType: 'all',
      customerList: [],
      allCustomers: [],
      showTempDialog: false,
      tempName: '',
      tempPhone: ''
    }
  },
  onShow() {
    this.loadCustomers()
  },
  methods: {
    async loadCustomers() {
      uni.showLoading({ title: '加载中...' })
      try {
        let res
        if (this.filterType === 'regular') {
          res = await customerApi.getRegularCustomers()
        } else if (this.filterType === 'temporary') {
          res = await customerApi.getTemporaryCustomers()
        } else {
          res = await customerApi.getAllCustomers()
        }
        this.allCustomers = res
        this.customerList = res
      } catch (e) {
        console.error('加载客户失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    async searchCustomers() {
      if (!this.searchKeyword.trim()) {
        this.customerList = this.allCustomers
        return
      }
      uni.showLoading({ title: '搜索中...' })
      try {
        const res = await customerApi.searchCustomers(this.searchKeyword)
        this.customerList = res
      } catch (e) {
        console.error('搜索失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    setFilter(type) {
      this.filterType = type
      this.loadCustomers()
    },
    getLevelClass(level) {
      const map = {
        'TEMPORARY': 'level-temp',
        'NORMAL': 'level-normal',
        'VIP': 'level-vip',
        'SVIP': 'level-svip',
        'DIAMOND': 'level-diamond'
      }
      return map[level] || 'level-normal'
    },
    getLevelText(level) {
      const map = {
        'TEMPORARY': '临时',
        'NORMAL': '普通',
        'VIP': 'VIP',
        'SVIP': 'SVIP',
        'DIAMOND': '钻石'
      }
      return map[level] || level
    },
    goToAdd() {
      uni.navigateTo({ url: '/pages/customer/add' })
    },
    goToDetail(id) {
      uni.navigateTo({ url: `/pages/customer/detail?id=${id}` })
    },
    addTemporary() {
      this.tempName = ''
      this.tempPhone = ''
      this.showTempDialog = true
    },
    async submitTemporary() {
      if (!this.tempName.trim()) {
        uni.showToast({ title: '请输入姓名', icon: 'none' })
        return
      }
      if (!/^1[3-9]\d{9}$/.test(this.tempPhone)) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return
      }
      uni.showLoading({ title: '注册中...' })
      try {
        await customerApi.createTemporaryCustomer(this.tempName, this.tempPhone)
        uni.showToast({ title: '注册成功', icon: 'success' })
        this.showTempDialog = false
        this.loadCustomers()
      } catch (e) {
        console.error('注册失败', e)
      } finally {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.search-bar {
  display: flex;
  margin-bottom: 20rpx;
  gap: 16rpx;
}

.search-input {
  flex: 1;
  height: 72rpx;
  background: #fff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  border: 2rpx solid #eee;
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 36rpx;
  height: 72rpx;
  line-height: 72rpx;
  padding: 0 32rpx;
  font-size: 28rpx;
  border: none;
}

.filter-row {
  display: flex;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.filter-tab {
  padding: 12rpx 24rpx;
  background: #fff;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
  border: 2rpx solid #eee;
}

.filter-tab.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-color: transparent;
}

.quick-add {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.quick-add.temp {
  background: linear-gradient(135deg, #fff8e1 0%, #fff3e0 100%);
}

.add-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 36rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}

.quick-add.temp .add-icon {
  background: #fff;
  font-size: 32rpx;
}

.add-text {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.customer-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.customer-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.customer-info {
  flex: 1;
}

.customer-header {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.customer-name {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-right: 12rpx;
}

.level-tag {
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 16rpx;
}

.level-temp { background: #f5f5f5; color: #999; }
.level-normal { background: #e3f2fd; color: #1565c0; }
.level-vip { background: #fff8e1; color: #ff8f00; }
.level-svip { background: #fce4ec; color: #c2185b; }
.level-diamond { background: #e8eaf6; color: #283593; }

.customer-meta {
  font-size: 26rpx;
  margin-top: 6rpx;
}

.temp-badge {
  background: #fff3e0;
  color: #e65100;
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.customer-arrow {
  font-size: 40rpx;
  color: #ccc;
}

.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.dialog-content {
  width: 600rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 40rpx;
}

.dialog-title {
  font-size: 32rpx;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30rpx;
  color: #333;
}

.dialog-input {
  height: 80rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  margin-bottom: 20rpx;
  font-size: 28rpx;
}

.dialog-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.dialog-btn {
  flex: 1;
  height: 80rpx;
  line-height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  border: none;
}

.dialog-btn.cancel {
  background: #f5f5f5;
  color: #666;
}

.dialog-btn.confirm {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
</style>
