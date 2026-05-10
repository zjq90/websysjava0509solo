<template>
  <view class="container">
    <view class="form-card card">
      <view class="section-title">
        <text class="fs-32 fw-bold">入库登记</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">批次号 <text class="text-danger">*</text></text>
        <input 
          class="form-input" 
          v-model="form.batchNo" 
          placeholder="8位数字+字母组合"
          maxlength="8"
        />
        <text class="form-tip">例：WH06JK34</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">仓库 <text class="text-danger">*</text></text>
        <picker mode="selector" :range="warehouseNames" @change="onWarehouseChange">
          <view class="picker-field">
            <text :class="selectedWarehouse ? '' : 'text-muted'">
              {{ selectedWarehouse ? selectedWarehouse.warehouseName : '请选择仓库' }}
            </text>
            <text class="text-muted">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">种子 <text class="text-danger">*</text></text>
        <picker mode="selector" :range="seedNames" @change="onSeedChange">
          <view class="picker-field">
            <text :class="selectedSeed ? '' : 'text-muted'">
              {{ selectedSeed ? selectedSeed.seedName : '请选择种子' }}
            </text>
            <text class="text-muted">›</text>
          </view>
        </picker>
      </view>
      
      <view class="form-item">
        <text class="form-label">入库数量 <text class="text-danger">*</text></text>
        <input 
          class="form-input" 
          type="digit"
          v-model="form.quantity" 
          placeholder="请输入数量"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">单位</text>
        <input 
          class="form-input" 
          v-model="form.unit" 
          :value="selectedSeed ? selectedSeed.unit : ''"
          placeholder="如：kg、袋、包"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">保质期 <text class="text-danger">*</text></text>
        <picker mode="date" :value="form.expiryDate" :start="minDate" @change="onDateChange">
          <view class="picker-field">
            <text :class="form.expiryDate ? '' : 'text-muted'">
              {{ form.expiryDate || '请选择保质期（当前+6个月以上）' }}
            </text>
            <text class="text-muted">›</text>
          </view>
        </picker>
        <text class="form-tip">保质期不得早于当前日期+6个月</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">发芽率 <text class="text-danger">*</text></text>
        <input 
          class="form-input" 
          type="digit"
          v-model="form.germinationRate" 
          placeholder="0.0-100.0，保留1位小数"
        />
        <text class="form-tip">范围：0.0-100.0%</text>
      </view>
      
      <view class="form-item">
        <text class="form-label">产地</text>
        <input 
          class="form-input" 
          v-model="form.origin" 
          placeholder="请输入产地"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">存储位置</text>
        <input 
          class="form-input" 
          v-model="form.storageLocation" 
          placeholder="如：A区-01-01"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">操作人</text>
        <input 
          class="form-input" 
          v-model="operator" 
          placeholder="请输入操作人姓名"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">备注</text>
        <textarea 
          class="form-textarea" 
          v-model="remark" 
          placeholder="请输入备注信息"
          :maxlength="500"
        />
      </view>
    </view>
    
    <view class="submit-btn" @click="submitInbound">
      <text class="fs-30 fw-bold">确认入库</text>
    </view>
  </view>
</template>

<script>
import request from '@/utils/request.js'

export default {
  data() {
    return {
      form: {
        batchNo: '',
        warehouseId: null,
        seedId: null,
        quantity: '',
        unit: '',
        expiryDate: '',
        germinationRate: '',
        origin: '',
        storageLocation: ''
      },
      operator: '',
      remark: '',
      warehouses: [],
      seeds: [],
      warehouseNames: [],
      seedNames: [],
      selectedWarehouse: null,
      selectedSeed: null,
      minDate: ''
    }
  },
  onLoad() {
    this.initMinDate()
    this.loadWarehouses()
    this.loadSeeds()
  },
  methods: {
    initMinDate() {
      const today = new Date()
      today.setMonth(today.getMonth() + 6)
      this.minDate = today.toISOString().split('T')[0]
    },
    
    async loadWarehouses() {
      try {
        const res = await request.get('/api/warehouses')
        this.warehouses = res.data || []
        this.warehouseNames = this.warehouses.map(w => w.warehouseName)
        
        const currentId = uni.getStorageSync('currentWarehouseId')
        if (currentId) {
          const idx = this.warehouses.findIndex(w => w.id === currentId)
          if (idx >= 0) {
            this.selectedWarehouse = this.warehouses[idx]
            this.form.warehouseId = this.selectedWarehouse.id
          }
        }
      } catch (e) {
        console.error('加载仓库失败', e)
      }
    },
    
    async loadSeeds() {
      try {
        const res = await request.get('/api/seeds')
        this.seeds = res.data || []
        this.seedNames = this.seeds.map(s => s.seedName)
      } catch (e) {
        console.error('加载种子失败', e)
      }
    },
    
    onWarehouseChange(e) {
      const idx = e.detail.value
      this.selectedWarehouse = this.warehouses[idx]
      this.form.warehouseId = this.selectedWarehouse.id
    },
    
    onSeedChange(e) {
      const idx = e.detail.value
      this.selectedSeed = this.seeds[idx]
      this.form.seedId = this.selectedSeed.id
      this.form.unit = this.selectedSeed.unit
    },
    
    onDateChange(e) {
      this.form.expiryDate = e.detail.value
    },
    
    validate() {
      if (!this.form.batchNo) {
        uni.showToast({ title: '请输入批次号', icon: 'none' })
        return false
      }
      const batchReg = /^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8}$/
      if (!batchReg.test(this.form.batchNo)) {
        uni.showToast({ title: '批次号必须为8位数字+字母组合', icon: 'none' })
        return false
      }
      if (!this.form.warehouseId) {
        uni.showToast({ title: '请选择仓库', icon: 'none' })
        return false
      }
      if (!this.form.seedId) {
        uni.showToast({ title: '请选择种子', icon: 'none' })
        return false
      }
      if (!this.form.quantity || parseFloat(this.form.quantity) <= 0) {
        uni.showToast({ title: '请输入有效的数量', icon: 'none' })
        return false
      }
      if (!this.form.expiryDate) {
        uni.showToast({ title: '请选择保质期', icon: 'none' })
        return false
      }
      if (!this.form.germinationRate) {
        uni.showToast({ title: '请输入发芽率', icon: 'none' })
        return false
      }
      const rate = parseFloat(this.form.germinationRate)
      if (rate < 0 || rate > 100) {
        uni.showToast({ title: '发芽率范围0-100%', icon: 'none' })
        return false
      }
      return true
    },
    
    async submitInbound() {
      if (!this.validate()) return
      
      try {
        const data = {
          batchNo: this.form.batchNo,
          warehouseId: this.form.warehouseId,
          seedId: this.form.seedId,
          quantity: parseFloat(this.form.quantity),
          unit: this.form.unit,
          expiryDate: this.form.expiryDate,
          germinationRate: parseFloat(this.form.germinationRate),
          origin: this.form.origin,
          storageLocation: this.form.storageLocation
        }
        
        const res = await request.post(
          `/api/inventory/inbound?operator=${encodeURIComponent(this.operator)}&remark=${encodeURIComponent(this.remark)}`,
          data
        )
        
        uni.showToast({ title: '入库成功', icon: 'success' })
        
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        console.error('入库失败', e)
      }
    }
  }
}
</script>

<style scoped>
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
  background: linear-gradient(135deg, #2979FF 0%, #667eea 100%);
  color: #ffffff;
  border-radius: 44rpx;
  padding: 28rpx;
  text-align: center;
  margin-top: 40rpx;
}
</style>
