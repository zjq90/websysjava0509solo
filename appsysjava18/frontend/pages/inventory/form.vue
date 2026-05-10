<template>
  <view class="container">
    <view class="form-card">
      <view class="form-title">{{ isEdit ? '编辑库存' : '新增库存' }}</view>

      <view class="form-item">
        <text class="form-label">种子名称 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.seedName" 
          placeholder="请输入种子名称"
        />
      </view>

      <view class="form-item">
        <text class="form-label">批次编号 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.batchNumber" 
          placeholder="8位数字+字母组合，如：ABC00001"
          :disabled="isEdit"
        />
        <text class="form-tip">批次号格式：8位数字+字母组合</text>
      </view>

      <view class="form-item">
        <text class="form-label">入库数量 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="number" 
          v-model="form.quantity" 
          placeholder="请输入入库数量（公斤）"
        />
      </view>

      <view class="form-item">
        <text class="form-label">单位</text>
        <picker :value="unitIndex" :range="unitOptions" @change="onUnitChange">
          <view class="picker-wrapper">
            <text class="picker-value">{{ unitOptions[unitIndex] }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">保质期 <text class="required">*</text></text>
        <picker mode="date" :value="form.shelfLife" @change="onDateChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.shelfLife }">
              {{ form.shelfLife || '请选择保质期' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
        <text class="form-tip">保质期不得早于当前日期+6个月</text>
      </view>

      <view class="form-item">
        <text class="form-label">发芽率 (%)</text>
        <input 
          class="form-input" 
          type="digit" 
          v-model="form.germinationRate" 
          placeholder="0-100，保留1位小数"
        />
      </view>

      <view class="form-item">
        <text class="form-label">备注</text>
        <textarea 
          class="form-textarea" 
          v-model="form.remark" 
          placeholder="请输入备注信息"
          :maxlength="500"
        />
      </view>
    </view>

    <view class="submit-section">
      <view class="submit-btn" @click="handleSubmit">
        <text class="submit-text">{{ isEdit ? '保存修改' : '确认提交' }}</text>
      </view>
    </view>
  </view>
</template>

<script>
import { createInventory, updateInventory, getInventoryDetail } from '@/api/inventory'

export default {
  data() {
    return {
      id: null,
      isEdit: false,
      unitOptions: ['公斤', '袋', '箱', '吨'],
      unitIndex: 0,
      form: {
        seedName: '',
        batchNumber: '',
        quantity: '',
        unit: '公斤',
        shelfLife: '',
        germinationRate: '',
        remark: ''
      }
    }
  },

  onLoad(options) {
    if (options.id) {
      this.id = options.id
      this.isEdit = true
      this.loadDetail()
    }
  },

  methods: {
    // 加载详情
    async loadDetail() {
      try {
        const res = await getInventoryDetail(this.id)
        this.form.seedName = res.seedName || ''
        this.form.batchNumber = res.batchNumber || ''
        this.form.quantity = res.quantity || ''
        this.form.unit = res.unit || '公斤'
        this.unitIndex = this.unitOptions.indexOf(this.form.unit)
        this.form.shelfLife = res.shelfLife || ''
        this.form.germinationRate = res.germinationRate || ''
        this.form.remark = res.remark || ''
      } catch (e) {
        console.error('加载详情失败', e)
      }
    },

    // 单位选择
    onUnitChange(e) {
      this.unitIndex = e.detail.value
      this.form.unit = this.unitOptions[this.unitIndex]
    },

    // 日期选择
    onDateChange(e) {
      this.form.shelfLife = e.detail.value
    },

    // 生成批次号
    generateBatchNumber() {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
      let result = ''
      for (let i = 0; i < 8; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length))
      }
      return result
    },

    // 提交表单
    async handleSubmit() {
      if (!this.form.seedName.trim()) {
        uni.showToast({ title: '请输入种子名称', icon: 'none' })
        return
      }

      if (!this.form.batchNumber.trim()) {
        this.form.batchNumber = this.generateBatchNumber()
      }

      if (!/^[A-Z0-9]{8}$/.test(this.form.batchNumber)) {
        uni.showToast({ title: '批次号格式不正确', icon: 'none' })
        return
      }

      if (!this.form.quantity || Number(this.form.quantity) <= 0) {
        uni.showToast({ title: '请输入正确的数量', icon: 'none' })
        return
      }

      if (!this.form.shelfLife) {
        uni.showToast({ title: '请选择保质期', icon: 'none' })
        return
      }

      uni.showLoading({ title: '提交中...', mask: true })

      try {
        const data = {
          ...this.form,
          quantity: Number(this.form.quantity),
          germinationRate: this.form.germinationRate ? Number(this.form.germinationRate) : null
        }

        if (this.isEdit) {
          await updateInventory(this.id, data)
        } else {
          await createInventory(data)
        }

        uni.hideLoading()
        uni.showToast({ title: this.isEdit ? '修改成功' : '提交成功', icon: 'success' })

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
.container {
  padding: 30rpx;
  padding-bottom: 200rpx;
  min-height: 100vh;
  background: #F5F5F5;
}

.form-card {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 40rpx;
  text-align: center;
}

.form-item {
  margin-bottom: 36rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 16rpx;
}

.required {
  color: #FF3B30;
}

.form-input {
  width: 100%;
  height: 88rpx;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  color: #333333;
}

.form-tip {
  display: block;
  font-size: 22rpx;
  color: #999999;
  margin-top: 8rpx;
}

.picker-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 88rpx;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 0 24rpx;
}

.picker-value {
  font-size: 28rpx;
  color: #333333;
}

.picker-value.placeholder {
  color: #999999;
}

.picker-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  background: #F5F5F5;
  border-radius: 16rpx;
  padding: 24rpx;
  font-size: 28rpx;
  color: #333333;
  box-sizing: border-box;
}

.submit-section {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 30rpx;
  background: #FFFFFF;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
  border-radius: 16rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.submit-text {
  font-size: 32rpx;
  color: #FFFFFF;
  font-weight: 500;
}
</style>
