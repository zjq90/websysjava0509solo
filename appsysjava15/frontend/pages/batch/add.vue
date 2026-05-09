<template>
  <view class="container">
    <view class="form-card card">
      <view class="form-item">
        <text class="form-label">批次编号 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.batchNo" 
          placeholder="8位数字+字母组合（如：BATCH004）"
          maxlength="8"
        />
        <text class="form-hint">必须为8位数字+字母组合，且全局唯一</text>
      </view>

      <view class="form-item">
        <text class="form-label">产品名称 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.productName" 
          placeholder="请输入产品名称"
        />
      </view>

      <view class="form-item">
        <text class="form-label">生产数量 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="digit" 
          v-model="form.quantity" 
          placeholder="请输入生产数量"
        />
      </view>

      <view class="form-item">
        <text class="form-label">单位</text>
        <picker :value="unitIndex" :range="units" @change="onUnitChange">
          <view class="form-input picker-input">
            {{ units[unitIndex] }}
            <text class="picker-arrow">▼</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">保质期 <text class="required">*</text></text>
        <picker mode="date" :value="form.shelfLife" :start="minShelfLife" @change="onDateChange">
          <view class="form-input picker-input">
            {{ form.shelfLife || '请选择保质期' }}
            <text class="picker-arrow">▼</text>
          </view>
        </picker>
        <text class="form-hint">不得早于当前日期+6个月</text>
      </view>

      <view class="form-item">
        <text class="form-label">客户名称</text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.customerName" 
          placeholder="请输入客户名称（可选）"
        />
      </view>

      <view class="form-item">
        <text class="form-label">客户手机号</text>
        <input 
          class="form-input" 
          type="number" 
          v-model="form.customerPhone" 
          placeholder="11位手机号（可选）"
          maxlength="11"
        />
        <text class="form-hint">符合中国大陆手机号格式</text>
      </view>

      <view class="form-item">
        <text class="form-label">备注</text>
        <textarea 
          class="form-textarea" 
          v-model="form.remark" 
          placeholder="请输入备注信息（可选）"
        />
      </view>
    </view>

    <view class="btn-primary" @click="handleSubmit">
      <text>创建批次</text>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      units: ['公斤', '吨', '袋', '箱'],
      unitIndex: 0,
      minShelfLife: '',
      form: {
        batchNo: '',
        productName: '',
        quantity: '',
        unit: '公斤',
        shelfLife: '',
        customerName: '',
        customerPhone: '',
        remark: ''
      }
    }
  },
  onLoad() {
    this.initMinShelfLife()
    this.generateBatchNo()
  },
  methods: {
    initMinShelfLife() {
      const now = new Date()
      now.setMonth(now.getMonth() + 6)
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      this.minShelfLife = `${year}-${month}-${day}`
    },
    generateBatchNo() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
      let result = 'BATCH'
      for (let i = 0; i < 3; i++) {
        result += chars[Math.floor(Math.random() * chars.length)]
      }
      this.form.batchNo = result
    },
    onUnitChange(e) {
      this.unitIndex = e.detail.value
      this.form.unit = this.units[e.detail.value]
    },
    onDateChange(e) {
      this.form.shelfLife = e.detail.value
    },
    validate() {
      if (!this.form.batchNo) {
        uni.showToast({ title: '请输入批次编号', icon: 'none' })
        return false
      }
      if (!/^[A-Za-z0-9]{8}$/.test(this.form.batchNo)) {
        uni.showToast({ title: '批次编号必须为8位数字+字母组合', icon: 'none' })
        return false
      }
      if (!this.form.productName) {
        uni.showToast({ title: '请输入产品名称', icon: 'none' })
        return false
      }
      if (!this.form.quantity || parseFloat(this.form.quantity) <= 0) {
        uni.showToast({ title: '请输入有效的生产数量', icon: 'none' })
        return false
      }
      if (!this.form.shelfLife) {
        uni.showToast({ title: '请选择保质期', icon: 'none' })
        return false
      }
      if (this.form.customerPhone && !/^1[3-9]\d{9}$/.test(this.form.customerPhone)) {
        uni.showToast({ title: '客户手机号格式不正确', icon: 'none' })
        return false
      }
      return true
    },
    async handleSubmit() {
      if (!this.validate()) return
      
      uni.showLoading({ title: '创建中...' })
      try {
        const data = {
          ...this.form,
          quantity: parseFloat(this.form.quantity)
        }
        if (!data.customerPhone) {
          delete data.customerPhone
        }
        await api.createBatch(data)
        uni.hideLoading()
        uni.showToast({
          title: '创建成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1000)
      } catch (e) {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.required {
  color: #f56c6c;
}

.form-hint {
  font-size: 22rpx;
  color: #909399;
  margin-top: 8rpx;
  display: block;
}

.picker-input {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.picker-arrow {
  font-size: 20rpx;
  color: #c0c4cc;
}
</style>
