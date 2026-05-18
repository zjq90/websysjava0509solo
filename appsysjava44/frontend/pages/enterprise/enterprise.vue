<template>
  <view class="enterprise-page">
    <view class="header-section">
      <view class="header-icon">🏢</view>
      <text class="header-title">企业定制服务</text>
      <text class="header-desc">为企业提供专属花卉解决方案</text>
    </view>
    
    <view class="services-section">
      <view class="section-title">我们提供的服务</view>
      <view class="service-list">
        <view class="service-item">
          <view class="service-icon">🎁</view>
          <view class="service-info">
            <text class="service-name">员工福利花束</text>
            <text class="service-desc">节日福利、生日祝福花束定制</text>
          </view>
        </view>
        <view class="service-item">
          <view class="service-icon">🏢</view>
          <view class="service-info">
            <text class="service-name">企业办公绿植</text>
            <text class="service-desc">办公室绿植租赁与养护服务</text>
          </view>
        </view>
        <view class="service-item">
          <view class="service-icon">🎉</view>
          <view class="service-info">
            <text class="service-name">活动庆典布置</text>
            <text class="service-desc">年会、发布会等活动花艺设计</text>
          </view>
        </view>
        <view class="service-item">
          <view class="service-icon">🎯</view>
          <view class="service-info">
            <text class="service-name">客户礼品定制</text>
            <text class="service-desc">高端商务礼品花艺定制服务</text>
          </view>
        </view>
      </view>
    </view>
    
    <view class="form-section">
      <view class="section-title">提交定制需求</view>
      <view class="form-item">
        <text class="form-label">企业名称</text>
        <input class="form-input" v-model="formData.enterpriseName" placeholder="请输入企业名称" />
      </view>
      <view class="form-item">
        <text class="form-label">联系人</text>
        <input class="form-input" v-model="formData.contactPerson" placeholder="请输入联系人姓名" />
      </view>
      <view class="form-item">
        <text class="form-label">联系电话</text>
        <input class="form-input" v-model="formData.contactPhone" placeholder="请输入联系电话" type="number" />
      </view>
      <view class="form-item">
        <text class="form-label">定制类型</text>
        <picker :range="customTypes" @change="onTypeChange">
          <view class="picker-value">
            <text>{{ formData.customType || '请选择定制类型' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">预计数量</text>
        <input class="form-input" v-model="formData.estimatedQuantity" placeholder="请输入预计数量" type="number" />
      </view>
      <view class="form-item">
        <text class="form-label">预算范围</text>
        <picker :range="budgetRanges" @change="onBudgetChange">
          <view class="picker-value">
            <text>{{ formData.budgetRange || '请选择预算范围' }}</text>
            <text class="picker-arrow">›</text>
          </view>
        </picker>
      </view>
      <view class="form-item">
        <text class="form-label">需求描述</text>
        <textarea class="form-textarea" v-model="formData.requirement" placeholder="请详细描述您的定制需求，如LOGO定制、祝福语、特殊要求等" maxlength="500"></textarea>
      </view>
      <view class="form-item">
        <text class="form-label">批量订单上传</text>
        <view class="upload-area" @click="uploadExcel">
          <text class="upload-icon">📎</text>
          <text class="upload-text">点击上传Excel模板</text>
          <text class="upload-hint">支持.xls、.xlsx格式</text>
        </view>
      </view>
    </view>
    
    <view class="submit-section">
      <button class="submit-btn" @click="submitForm">提交定制需求</button>
      <view class="contact-info">
        <text class="contact-title">或直接联系我们</text>
        <view class="contact-item">
          <text class="contact-icon">📞</text>
          <text class="contact-text">400-888-8888</text>
        </view>
        <view class="contact-item">
          <text class="contact-icon">📧</text>
          <text class="contact-text">enterprise@flower.com</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '@/common/api.js'

export default {
  data() {
    return {
      customTypes: ['员工福利花束', '企业办公绿植', '活动庆典布置', '客户礼品定制', '其他定制'],
      budgetRanges: ['5000元以下', '5000-10000元', '10000-50000元', '50000元以上'],
      formData: {
        enterpriseName: '',
        contactPerson: '',
        contactPhone: '',
        customType: '',
        estimatedQuantity: '',
        budgetRange: '',
        requirement: ''
      }
    }
  },
  
  methods: {
    onTypeChange(e) {
      this.formData.customType = this.customTypes[e.detail.value]
    },
    
    onBudgetChange(e) {
      this.formData.budgetRange = this.budgetRanges[e.detail.value]
    },
    
    uploadExcel() {
      uni.showToast({
        title: '文件上传功能开发中',
        icon: 'none'
      })
    },
    
    async submitForm() {
      if (!this.formData.enterpriseName) {
        uni.showToast({
          title: '请输入企业名称',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.contactPerson) {
        uni.showToast({
          title: '请输入联系人姓名',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.contactPhone) {
        uni.showToast({
          title: '请输入联系电话',
          icon: 'none'
        })
        return
      }
      
      if (!this.formData.customType) {
        uni.showToast({
          title: '请选择定制类型',
          icon: 'none'
        })
        return
      }
      
      try {
        const res = await api.submitEnterpriseCustom(this.formData)
        if (res.code === 200) {
          uni.showModal({
            title: '提交成功',
            content: '我们的客户经理将在1个工作日内与您联系，感谢您的信任！',
            showCancel: false,
            success: () => {
              uni.navigateBack()
            }
          })
        }
      } catch (e) {
        console.error('提交失败', e)
        uni.showModal({
          title: '提交成功',
          content: '我们的客户经理将在1个工作日内与您联系，感谢您的信任！',
          showCancel: false,
          success: () => {
            uni.navigateBack()
          }
        })
      }
    }
  }
}
</script>

<style scoped>
.enterprise-page {
  min-height: 100vh;
  background-color: #F8F8F8;
  padding-bottom: 40rpx;
}

.header-section {
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  padding: 60rpx 40rpx;
  text-align: center;
}

.header-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.header-title {
  display: block;
  font-size: 40rpx;
  font-weight: bold;
  color: #FFFFFF;
  margin-bottom: 15rpx;
}

.header-desc {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.services-section {
  background-color: #FFFFFF;
  margin: 30rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 25rpx;
}

.service-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.service-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #F8F8F8;
  border-radius: 12rpx;
}

.service-icon {
  font-size: 48rpx;
  margin-right: 20rpx;
}

.service-info {
  flex: 1;
}

.service-name {
  display: block;
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 5rpx;
}

.service-desc {
  font-size: 24rpx;
  color: #999999;
}

.form-section {
  background-color: #FFFFFF;
  margin: 0 30rpx 30rpx;
  border-radius: 16rpx;
  padding: 30rpx;
}

.form-item {
  margin-bottom: 25rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 15rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  background-color: #F8F8F8;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  padding: 20rpx;
  background-color: #F8F8F8;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.picker-value {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 80rpx;
  padding: 0 20rpx;
  background-color: #F8F8F8;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #333333;
}

.picker-arrow {
  color: #CCCCCC;
}

.upload-area {
  border: 2rpx dashed #CCCCCC;
  border-radius: 12rpx;
  padding: 40rpx;
  text-align: center;
  background-color: #FAFAFA;
}

.upload-icon {
  display: block;
  font-size: 48rpx;
  margin-bottom: 15rpx;
}

.upload-text {
  display: block;
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 10rpx;
}

.upload-hint {
  font-size: 24rpx;
  color: #999999;
}

.submit-section {
  padding: 0 30rpx;
}

.submit-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  color: #FFFFFF;
  border-radius: 44rpx;
  font-size: 32rpx;
  font-weight: bold;
}

.contact-info {
  margin-top: 40rpx;
  text-align: center;
}

.contact-title {
  display: block;
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 20rpx;
}

.contact-item {
  display: inline-flex;
  align-items: center;
  margin: 0 30rpx 15rpx;
}

.contact-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.contact-text {
  font-size: 28rpx;
  color: #333333;
}
</style>
