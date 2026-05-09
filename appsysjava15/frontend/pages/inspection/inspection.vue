<template>
  <view class="container">
    <view class="header-card card">
      <view class="header-row">
        <text class="label">批次编号：</text>
        <text class="value">{{ batchNo || '-' }}</text>
      </view>
      <view class="header-row">
        <text class="label">质检环节：</text>
        <text class="value highlight">{{ stageName }}</text>
      </view>
    </view>

    <view class="form-card card">
      <view class="form-item">
        <text class="form-label">水分 (%) <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="digit" 
          v-model="form.moisture" 
          placeholder="0-100，保留1位小数"
        />
        <text class="form-hint">参考标准：≤13%</text>
      </view>

      <view class="form-item">
        <text class="form-label">净度 (%) <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="digit" 
          v-model="form.purity" 
          placeholder="0-100，保留1位小数"
        />
        <text class="form-hint">参考标准：≥98%</text>
      </view>

      <view class="form-item">
        <text class="form-label">发芽率 (%) <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="digit" 
          v-model="form.germinationRate" 
          placeholder="0-100，保留1位小数"
        />
        <text class="form-hint">参考标准：≥85%</text>
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
      <text>提交质检结果</text>
    </view>
  </view>
</template>

<script>
import api from '../../utils/api.js'

export default {
  data() {
    return {
      batchId: null,
      stageCode: '',
      stageName: '',
      batchNo: '',
      userInfo: null,
      form: {
        moisture: '',
        purity: '',
        germinationRate: '',
        remark: ''
      }
    }
  },
  onLoad(options) {
    this.batchId = options.batchId
    this.stageCode = options.stageCode
    this.stageName = decodeURIComponent(options.stageName || '')
    this.userInfo = uni.getStorageSync('userInfo')
    this.loadBatchInfo()
  },
  methods: {
    async loadBatchInfo() {
      try {
        const res = await api.getBatchById(this.batchId)
        if (res.data) {
          this.batchNo = res.data.batchNo
        }
      } catch (e) {
        console.error(e)
      }
    },
    validate() {
      if (!this.form.moisture) {
        uni.showToast({ title: '请输入水分值', icon: 'none' })
        return false
      }
      if (parseFloat(this.form.moisture) < 0 || parseFloat(this.form.moisture) > 100) {
        uni.showToast({ title: '水分值范围0-100', icon: 'none' })
        return false
      }
      if (!this.form.purity) {
        uni.showToast({ title: '请输入净度值', icon: 'none' })
        return false
      }
      if (parseFloat(this.form.purity) < 0 || parseFloat(this.form.purity) > 100) {
        uni.showToast({ title: '净度值范围0-100', icon: 'none' })
        return false
      }
      if (!this.form.germinationRate) {
        uni.showToast({ title: '请输入发芽率', icon: 'none' })
        return false
      }
      if (parseFloat(this.form.germinationRate) < 0 || parseFloat(this.form.germinationRate) > 100) {
        uni.showToast({ title: '发芽率范围0-100', icon: 'none' })
        return false
      }
      return true
    },
    async handleSubmit() {
      if (!this.validate()) return
      
      uni.showLoading({ title: '提交中...' })
      try {
        const data = {
          batchId: parseInt(this.batchId),
          stageCode: this.stageCode,
          operatorId: this.userInfo && this.userInfo.id ? this.userInfo.id : null,
          operatorName: this.userInfo && this.userInfo.realName ? this.userInfo.realName : null,
          moisture: parseFloat(parseFloat(this.form.moisture).toFixed(1)),
          purity: parseFloat(parseFloat(this.form.purity).toFixed(1)),
          germinationRate: parseFloat(parseFloat(this.form.germinationRate).toFixed(1)),
          remark: this.form.remark
        }
        await api.submitInspection(data)
        uni.hideLoading()
        uni.showToast({
          title: '提交成功',
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

.header-row {
  display: flex;
  padding: 12rpx 0;
}

.label {
  font-size: 28rpx;
  color: #909399;
}

.value {
  font-size: 28rpx;
  color: #303133;
  font-weight: 500;
}

.value.highlight {
  color: #409eff;
}

.form-hint {
  font-size: 22rpx;
  color: #909399;
  margin-top: 8rpx;
  display: block;
}
</style>
