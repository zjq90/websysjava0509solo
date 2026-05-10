<template>
  <view class="container">
    <view class="form-card">
      <view class="form-item">
        <text class="label">客户名称 <text class="required">*</text></text>
        <input class="input" placeholder="请输入客户名称" v-model="form.name" />
      </view>
      <view class="form-item">
        <text class="label">联系电话 <text class="required">*</text></text>
        <input class="input" placeholder="请输入手机号" type="number" v-model="form.phone" />
      </view>
      <view class="form-item">
        <text class="label">客户等级</text>
        <picker :value="levelIndex" :range="levelOptions" range-key="label" @change="onLevelChange">
          <view class="picker-value">{{ levelOptions[levelIndex].label }}</view>
        </picker>
      </view>
      <view class="form-item">
        <text class="label">地址</text>
        <input class="input" placeholder="请输入地址" v-model="form.address" />
      </view>
      <view class="form-item">
        <text class="label">信用额度</text>
        <input class="input" placeholder="请输入信用额度" type="digit" v-model="form.creditLimit" />
      </view>
      <view class="form-item">
        <text class="label">备注</text>
        <textarea class="textarea" placeholder="请输入备注信息" v-model="form.notes"></textarea>
      </view>
    </view>
    <button class="submit-btn" @click="submit">保存</button>
  </view>
</template>

<script>
import customerApi from '@/api/customer.js'

export default {
  data() {
    return {
      form: {
        name: '',
        phone: '',
        level: 'NORMAL',
        address: '',
        creditLimit: 0,
        notes: ''
      },
      levelIndex: 0,
      levelOptions: [
        { value: 'NORMAL', label: '普通客户' },
        { value: 'VIP', label: 'VIP客户' },
        { value: 'SVIP', label: 'SVIP客户' },
        { value: 'DIAMOND', label: '钻石客户' }
      ]
    }
  },
  methods: {
    onLevelChange(e) {
      this.levelIndex = e.detail.value
      this.form.level = this.levelOptions[e.detail.value].value
    },
    validate() {
      if (!this.form.name.trim()) {
        uni.showToast({ title: '请输入客户名称', icon: 'none' })
        return false
      }
      if (!/^1[3-9]\d{9}$/.test(this.form.phone)) {
        uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
        return false
      }
      return true
    },
    async submit() {
      if (!this.validate()) return
      uni.showLoading({ title: '保存中...' })
      try {
        const data = {
          ...this.form,
          creditLimit: parseFloat(this.form.creditLimit) || 0,
          isTemporary: false
        }
        await customerApi.createCustomer(data)
        uni.showToast({ title: '保存成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack()
        }, 1000)
      } catch (e) {
        console.error('保存失败', e)
      } finally {
        uni.hideLoading()
      }
    }
  }
}
</script>

<style scoped>
.form-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
}

.form-item {
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f5f5f5;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
  display: block;
}

.required {
  color: #ff4d4f;
}

.input {
  width: 100%;
  height: 80rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.picker-value {
  height: 80rpx;
  line-height: 80rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  color: #333;
}

.textarea {
  width: 100%;
  min-height: 160rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
  padding: 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.submit-btn {
  margin-top: 40rpx;
  height: 96rpx;
  line-height: 96rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 48rpx;
  font-size: 32rpx;
  border: none;
}
</style>
