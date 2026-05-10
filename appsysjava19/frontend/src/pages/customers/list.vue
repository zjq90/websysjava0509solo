<template>
  <view class="container">
    <view class="header-bar">
      <text class="fs-32 fw-bold">客户管理</text>
    </view>
    
    <view class="search-bar">
      <input 
        class="search-input" 
        v-model="searchKeyword" 
        placeholder="搜索客户名称、编号"
        @confirm="searchCustomer"
      />
    </view>
    
    <view class="customer-list">
      <view 
        class="customer-card card" 
        v-for="customer in filteredCustomers" 
        :key="customer.id"
      >
        <view class="card-header">
          <view class="customer-info">
            <view class="customer-avatar">
              <text class="avatar-text">{{ getInitial(customer.customerName) }}</text>
            </view>
            <view class="customer-basic">
              <text class="fs-30 fw-bold">{{ customer.customerName }}</text>
              <text class="fs-24 text-muted mt-5">{{ customer.customerCode }}</text>
            </view>
          </view>
          <view :class="customer.status === 'ACTIVE' ? 'badge-success' : 'badge-normal'">
            <text>{{ customer.status === 'ACTIVE' ? '活跃' : '停用' }}</text>
          </view>
        </view>
        
        <view class="card-body">
          <view class="info-row" v-if="customer.contactName">
            <text class="info-label">联系人</text>
            <text class="info-value">{{ customer.contactName }}</text>
          </view>
          <view class="info-row" v-if="customer.phone">
            <text class="info-label">电话</text>
            <text class="info-value">{{ maskPhone(customer.phone) }}</text>
          </view>
          <view class="info-row" v-if="customer.customerType">
            <text class="info-label">类型</text>
            <text class="info-value">{{ customer.customerType === 'ENTERPRISE' ? '企业' : '个人' }}</text>
          </view>
        </view>
        
        <view class="card-actions">
          <view class="action-btn action-call" @click="callCustomer(customer.phone)" v-if="customer.phone">
            <text class="fs-26">📞 联系</text>
          </view>
        </view>
      </view>
      
      <view class="empty-state" v-if="filteredCustomers.length === 0">
        <text class="text-muted fs-28">暂无客户数据</text>
      </view>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      customers: [],
      searchKeyword: ''
    }
  },
  computed: {
    filteredCustomers() {
      if (!this.searchKeyword) {
        return this.customers
      }
      const keyword = this.searchKeyword.toLowerCase()
      return this.customers.filter(c => 
        c.customerName.toLowerCase().includes(keyword) || 
        c.customerCode.toLowerCase().includes(keyword)
      )
    }
  },
  onShow() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await request.get('/api/customers')
        this.customers = res.data || []
      } catch (e) {
        console.error('加载客户失败', e)
      }
    },
    
    searchCustomer() {
      // 已在computed中处理
    },
    
    getInitial(name) {
      if (!name) return '?'
      return name.charAt(0)
    },
    
    maskPhone(phone) {
      if (!phone) return ''
      if (phone.length === 11) {
        return phone.substring(0, 3) + '****' + phone.substring(7)
      }
      return phone
    },
    
    callCustomer(phone) {
      uni.makePhoneCall({
        phoneNumber: phone,
        fail: () => {
          uni.showToast({ title: '拨号失败', icon: 'none' })
        }
      })
    }
  }
}
</script>

<style scoped>
.header-bar {
  padding: 20rpx;
  background-color: #ffffff;
  margin-bottom: 20rpx;
}

.search-bar {
  padding: 0 20rpx 20rpx;
}

.search-input {
  width: 100%;
  height: 80rpx;
  background-color: #ffffff;
  border-radius: 40rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
}

.customer-card {
  margin-bottom: 20rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.customer-info {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.customer-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  color: #ffffff;
  font-size: 32rpx;
  font-weight: 600;
}

.customer-basic {
  display: flex;
  flex-direction: column;
}

.mt-5 {
  margin-top: 5rpx;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 26rpx;
  color: #999;
}

.info-value {
  font-size: 26rpx;
  color: #333;
}

.card-actions {
  margin-top: 20rpx;
  padding-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
}

.action-btn {
  padding: 10rpx 24rpx;
  border-radius: 20rpx;
}

.action-call {
  background-color: #E3F2FD;
  color: #1976D2;
}

.empty-state {
  text-align: center;
  padding: 100rpx 0;
}
</style>
