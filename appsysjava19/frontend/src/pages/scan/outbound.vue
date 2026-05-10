<template>
  <view class="container">
    <view class="form-card card">
      <view class="section-title">
        <text class="fs-32 fw-bold">出库登记</text>
      </view>
      
      <view class="batch-section">
        <view class="batch-input-row">
          <input 
            class="batch-input" 
            v-model="batchNo" 
            placeholder="请输入或扫描批次号"
            maxlength="8"
          />
          <view class="scan-btn" @click="startScan">
            <text class="fs-24">📷 扫描</text>
          </view>
        </view>
        <view class="test-batches">
          <text class="fs-24 text-muted">测试批次：</text>
          <text 
            class="batch-tag" 
            v-for="batch in testBatches" 
            :key="batch" 
            @click="selectBatch(batch)"
          >
            {{ batch }}
          </text>
        </view>
      </view>
      
      <view class="inventory-info card" v-if="inventory">
        <view class="info-header">
          <text class="fs-28 fw-bold">库存信息</text>
          <view :class="getStatusBadgeClass(inventory.status)">
            <text>{{ getStatusText(inventory.status) }}</text>
          </view>
        </view>
        <view class="info-row">
          <text class="info-label">批次号</text>
          <text class="info-value">{{ inventory.batchNo }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">当前库存</text>
          <text class="info-value text-primary fw-bold">{{ inventory.quantity }} {{ inventory.unit }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">保质期</text>
          <text class="info-value">{{ inventory.expiryDate }}</text>
        </view>
        <view class="info-row">
          <text class="info-label">发芽率</text>
          <text class="info-value">{{ inventory.germinationRate }}%</text>
        </view>
      </view>
      
      <view class="form-item" v-if="inventory">
        <text class="form-label">出库数量 <text class="text-danger">*</text></text>
        <input 
          class="form-input" 
          type="digit"
          v-model="quantity" 
          placeholder="请输入出库数量"
        />
        <text class="form-tip">可用库存：{{ inventory.quantity }} {{ inventory.unit }}</text>
      </view>
      
      <view class="form-item" v-if="inventory">
        <text class="form-label">客户</text>
        <picker mode="selector" :range="customerNames" @change="onCustomerChange">
          <view class="picker-field">
            <text :class="selectedCustomer ? '' : 'text-muted'">
              {{ selectedCustomer ? selectedCustomer.customerName : '请选择客户（可选）' }}
            </text>
            <text class="text-muted">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item" v-if="inventory">
        <text class="form-label">关联单号</text>
        <input 
          class="form-input" 
          v-model="relatedNo" 
          placeholder="如：销售单号、采购单号"
        />
      </view>
      
      <view class="form-item" v-if="inventory">
        <text class="form-label">操作人</text>
        <input 
          class="form-input" 
          v-model="operator" 
          placeholder="请输入操作人姓名"
        />
      </view>
      
      <view class="form-item" v-if="inventory">
        <text class="form-label">备注</text>
        <textarea 
          class="form-textarea" 
          v-model="remark" 
          placeholder="请输入备注信息"
          :maxlength="500"
        />
      </view>
    </view>
    
    <view class="submit-btn" v-if="inventory" @click="submitOutbound">
      <text class="fs-30 fw-bold">确认出库</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      batchNo: '',
      quantity: '',
      relatedNo: '',
      operator: '',
      remark: '',
      inventory: null,
      customers: [],
      customerNames: [],
      selectedCustomer: null,
      testBatches: ['WH01AB1234', 'WH02CD5678', 'WH03EF9012']
    }
  },
  onLoad() {
    const savedBatch = uni.getStorageSync('currentBatchNo')
    if (savedBatch) {
      this.batchNo = savedBatch
      this.queryInventory()
    }
    this.loadCustomers()
  },
  methods: {
    startScan() {
      uni.scanCode({
        success: (res) => {
          this.batchNo = res.result
          this.queryInventory()
        },
        fail: (err) => {
          uni.showToast({
            title: '扫码失败',
            icon: 'none'
          })
        }
      })
    },
    
    selectBatch(batch) {
      this.batchNo = batch
      this.queryInventory()
    },
    
    async queryInventory() {
      if (!this.batchNo) return
      
      try {
        const res = await request.get(`/api/inventory/scan/${this.batchNo}`)
        this.inventory = res.data.inventory
        if (!this.inventory) {
          uni.showToast({ title: '未找到该批次', icon: 'none' })
        }
      } catch (e) {
        this.inventory = null
      }
    },
    
    async loadCustomers() {
      try {
        const res = await request.get('/api/customers')
        this.customers = res.data || []
        this.customerNames = this.customers.map(c => c.customerName)
      } catch (e) {
        console.error('加载客户失败', e)
      }
    },
    
    onCustomerChange(e) {
      const idx = e.detail.value
      this.selectedCustomer = this.customers[idx]
    },
    
    validate() {
      if (!this.batchNo) {
        uni.showToast({ title: '请输入批次号', icon: 'none' })
        return false
      }
      if (!this.inventory) {
        uni.showToast({ title: '无效的批次号', icon: 'none' })
        return false
      }
      if (!this.quantity || parseFloat(this.quantity) <= 0) {
        uni.showToast({ title: '请输入有效的数量', icon: 'none' })
        return false
      }
      if (parseFloat(this.quantity) > parseFloat(this.inventory.quantity)) {
        uni.showToast({ title: '库存不足', icon: 'none' })
        return false
      }
      if (this.inventory.status === 'EXPIRED') {
        uni.showToast({ title: '该批次已过期，无法出库', icon: 'none' })
        return false
      }
      return true
    },
    
    async submitOutbound() {
      if (!this.validate()) return
      
      try {
        const params = new URLSearchParams()
        params.append('batchNo', this.batchNo)
        params.append('quantity', this.quantity)
        if (this.operator) params.append('operator', this.operator)
        if (this.selectedCustomer) params.append('customerId', this.selectedCustomer.id)
        if (this.relatedNo) params.append('relatedNo', this.relatedNo)
        if (this.remark) params.append('remark', this.remark)
        
        const res = await request.post(
          `/api/inventory/outbound?${params.toString()}`,
          null,
          'application/x-www-form-urlencoded'
        )
        
        uni.showToast({ title: '出库成功', icon: 'success' })
        
        uni.removeStorageSync('currentBatchNo')
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        console.error('出库失败', e)
      }
    },
    
    getStatusBadgeClass(status) {
      const map = {
        'NORMAL': 'badge-success',
        'NEAR_EXPIRY': 'badge-warning',
        'EXPIRED': 'badge-danger',
        'LOW_STOCK': 'badge-warning'
      }
      return map[status] || 'badge-normal'
    },
    
    getStatusText(status) {
      const map = {
        'NORMAL': '正常',
        'NEAR_EXPIRY': '近效期',
        'EXPIRED': '已过期',
        'LOW_STOCK': '缺货'
      }
      return map[status] || '未知'
    }
  }
}
</script>

<style scoped>
.batch-section {
  margin-bottom: 30rpx;
}

.batch-input-row {
  display: flex;
  gap: 15rpx;
  margin-bottom: 15rpx;
}

.batch-input {
  flex: 1;
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.scan-btn {
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  color: #ffffff;
  border-radius: 12rpx;
  padding: 0 30rpx;
  display: flex;
  align-items: center;
}

.test-batches {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10rpx;
}

.batch-tag {
  background-color: #E3F2FD;
  color: #1976D2;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.inventory-info {
  margin-bottom: 30rpx;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12rpx 0;
}

.info-label {
  font-size: 26rpx;
  color: #999;
}

.info-value {
  font-size: 26rpx;
  color: #333;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 12rpx;
  display: block;
}

.form-input {
  width: 100%;
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.form-textarea {
  width: 100%;
  height: 160rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  font-size: 28rpx;
}

.form-tip {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

.picker-field {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 88rpx;
  background-color: #f5f7fa;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.submit-btn {
  background: linear-gradient(135deg, #FF5252 0%, #ff1744 100%);
  color: #ffffff;
  border-radius: 44rpx;
  padding: 28rpx;
  text-align: center;
  margin-top: 40rpx;
}
</style>
