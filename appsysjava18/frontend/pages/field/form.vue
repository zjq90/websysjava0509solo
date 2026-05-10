<template>
  <view class="container">
    <view class="form-card">
      <view class="form-title">{{ isEdit ? '编辑田间记录' : '新增田间记录' }}</view>

      <view class="form-item">
        <text class="form-label">地块名称 <text class="required">*</text></text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.fieldName" 
          placeholder="请输入地块名称"
        />
      </view>

      <view class="form-item">
        <text class="form-label">作物名称 <text class="required">*</text></text>
        <picker :value="cropIndex" :range="cropOptions" @change="onCropChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.cropName }">
              {{ cropOptions[cropIndex] || '请选择作物' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">记录日期 <text class="required">*</text></text>
        <picker mode="date" :value="form.recordDate" @change="onDateChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.recordDate }">
              {{ form.recordDate || '请选择日期' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-row">
        <view class="form-item half">
          <text class="form-label">温度 (°C)</text>
          <input 
            class="form-input" 
            type="digit" 
            v-model="form.temperature" 
            placeholder="如: 25.5"
          />
        </view>
        <view class="form-item half">
          <text class="form-label">湿度 (%)</text>
          <input 
            class="form-input" 
            type="digit" 
            v-model="form.humidity" 
            placeholder="如: 65"
          />
        </view>
      </view>

      <view class="form-item">
        <text class="form-label">生长阶段</text>
        <picker :value="growthStageIndex" :range="growthStageOptions" @change="onGrowthStageChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.growthStage }">
              {{ growthStageOptions[growthStageIndex] || '请选择生长阶段' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">病虫害情况</text>
        <picker :value="pestIndex" :range="pestOptions" @change="onPestChange">
          <view class="picker-wrapper">
            <text class="picker-value" :class="{ 'placeholder': !form.pestStatus }">
              {{ pestOptions[pestIndex] || '请选择病虫害情况' }}
            </text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">施肥情况</text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.fertilizerStatus" 
          placeholder="请描述施肥情况"
        />
      </view>

      <view class="form-item">
        <text class="form-label">浇水情况</text>
        <input 
          class="form-input" 
          type="text" 
          v-model="form.irrigationStatus" 
          placeholder="请描述浇水情况"
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
import { createField, updateField, getFieldDetail } from '@/api/field'

export default {
  data() {
    return {
      id: null,
      isEdit: false,
      cropOptions: ['请选择作物', '水稻', '小麦', '玉米', '大豆', '蔬菜', '水果', '其他'],
      cropIndex: 0,
      growthStageOptions: ['请选择生长阶段', '播种期', '幼苗期', '生长期', '开花期', '结果期', '成熟期'],
      growthStageIndex: 0,
      pestOptions: ['请选择病虫害情况', '无病虫害', '轻微', '中等', '严重'],
      pestIndex: 0,
      form: {
        fieldName: '',
        cropName: '',
        recordDate: '',
        temperature: '',
        humidity: '',
        growthStage: '',
        pestStatus: '',
        fertilizerStatus: '',
        irrigationStatus: '',
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
        const res = await getFieldDetail(this.id)
        this.form.fieldName = res.fieldName || ''
        this.form.cropName = res.cropName || ''
        this.cropIndex = Math.max(0, this.cropOptions.indexOf(res.cropName))
        this.form.recordDate = res.recordDate || ''
        this.form.temperature = res.temperature || ''
        this.form.humidity = res.humidity || ''
        this.form.growthStage = res.growthStage || ''
        this.growthStageIndex = Math.max(0, this.growthStageOptions.indexOf(res.growthStage))
        this.form.pestStatus = res.pestStatus || ''
        this.pestIndex = Math.max(0, this.pestOptions.indexOf(res.pestStatus))
        this.form.fertilizerStatus = res.fertilizerStatus || ''
        this.form.irrigationStatus = res.irrigationStatus || ''
        this.form.remark = res.remark || ''
      } catch (e) {
        console.error('加载详情失败', e)
      }
    },

    // 作物选择
    onCropChange(e) {
      this.cropIndex = e.detail.value
      if (this.cropIndex > 0) {
        this.form.cropName = this.cropOptions[this.cropIndex]
      } else {
        this.form.cropName = ''
      }
    },

    // 日期选择
    onDateChange(e) {
      this.form.recordDate = e.detail.value
    },

    // 生长阶段选择
    onGrowthStageChange(e) {
      this.growthStageIndex = e.detail.value
      if (this.growthStageIndex > 0) {
        this.form.growthStage = this.growthStageOptions[this.growthStageIndex]
      } else {
        this.form.growthStage = ''
      }
    },

    // 病虫害选择
    onPestChange(e) {
      this.pestIndex = e.detail.value
      if (this.pestIndex > 0) {
        this.form.pestStatus = this.pestOptions[this.pestIndex]
      } else {
        this.form.pestStatus = ''
      }
    },

    // 提交表单
    async handleSubmit() {
      if (!this.form.fieldName.trim()) {
        uni.showToast({ title: '请输入地块名称', icon: 'none' })
        return
      }

      if (!this.form.cropName) {
        uni.showToast({ title: '请选择作物', icon: 'none' })
        return
      }

      if (!this.form.recordDate) {
        uni.showToast({ title: '请选择记录日期', icon: 'none' })
        return
      }

      uni.showLoading({ title: '提交中...', mask: true })

      try {
        const data = {
          ...this.form,
          temperature: this.form.temperature ? Number(this.form.temperature) : null,
          humidity: this.form.humidity ? Number(this.form.humidity) : null
        }

        if (this.isEdit) {
          await updateField(this.id, data)
        } else {
          await createField(data)
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

.form-row {
  display: flex;
  gap: 20rpx;
}

.form-item {
  margin-bottom: 36rpx;
}

.form-item.half {
  flex: 1;
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
  box-sizing: border-box;
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
