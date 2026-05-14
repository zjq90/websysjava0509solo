<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="report-form card">
      <view class="card-title">一键报修</view>
      
      <view class="form-item">
        <view class="label">故障类型</view>
        <view class="fault-types">
          <view v-for="type in faultTypes" :key="type.value" class="fault-type-item" :class="{ active: formData.faultType === type.value }" @click="selectFaultType(type.value)">
            <text class="fault-icon">{{ type.icon }}</text>
            <text class="fault-name">{{ type.label }}</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <view class="label">问题描述</view>
        <textarea class="textarea" :value="formData.description" @input="(e) => {formData.description = e.detail.value}" placeholder="请详细描述您遇到的问题..."></textarea>
      </view>

      <view class="form-item">
        <view class="label">联系电话</view>
        <input class="input" :value="formData.phone" @input="(e) => {formData.phone = e.detail.value}" placeholder="请输入联系电话" type="text" />
      </view>

      <view class="form-item">
        <view class="label">服务地址</view>
        <input class="input" :value="formData.address" @input="(e) => {formData.address = e.detail.value}" placeholder="请输入服务地址" />
      </view>

      <view class="form-item">
        <view class="label">上传截图/视频</view>
        <view class="upload-area">
          <view v-for="(item, index) in uploadedFiles" :key="index" class="file-item">
            <image v-if="item.type === 'image'" :src="item.url" class="preview-image" mode="aspectFill"></image>
            <view v-else class="video-preview">
              <text class="video-icon">🎬</text>
              <text class="video-name">{{ item.name }}</text>
            </view>
            <text class="delete-btn" @click="deleteFile(index)">✕</text>
          </view>
          <view class="upload-btn" @click="chooseImage" v-if="uploadedFiles.length < 6">
            <text class="plus-icon">+</text>
            <text class="upload-text">添加图片</text>
          </view>
          <view class="upload-btn" @click="chooseVideo" v-if="uploadedFiles.length < 6">
            <text class="plus-icon">🎥</text>
            <text class="upload-text">添加视频</text>
          </view>
        </view>
      </view>

      <button class="btn btn-primary mt-20" @click="submitReport" :disabled="submitting || uploading">
        {{ uploading ? '上传中...' : (submitting ? '提交中...' : '提交报修') }}
      </button>
    </view>

    <view v-if="diagnosisResult" class="diagnosis-card card">
      <view class="card-title">智能诊断结果</view>
      <view class="diagnosis-content">
        <view class="diagnosis-item">
          <text class="diagnosis-label">光猫状态：</text>
          <text class="diagnosis-value">{{ diagnosisResult.modemStatus }}</text>
        </view>
        <view class="diagnosis-item">
          <text class="diagnosis-label">信号强度：</text>
          <text class="diagnosis-value">{{ diagnosisResult.signalStrength }} dBm</text>
        </view>
        <view class="diagnosis-item">
          <text class="diagnosis-label">DNS状态：</text>
          <text class="diagnosis-value">{{ diagnosisResult.dnsStatus }}</text>
        </view>
        <view class="diagnosis-suggestion">
          <text class="suggestion-label">建议：</text>
          <text class="suggestion-text">{{ diagnosisResult.suggestion }}</text>
        </view>
      </view>
    </view>

    <view class="history-card card">
      <view class="card-title">报修记录</view>
      <view v-for="report in reports" :key="report.id" class="report-item">
        <view class="report-header flex-between">
          <text class="report-type">{{ report.faultType }}</text>
          <text class="report-status" :class="getStatusClass(report.status)">{{ report.status }}</text>
        </view>
        <view class="report-desc">{{ report.faultDesc }}</view>
        <view class="report-files" v-if="report.images">
          <text class="files-label">📷 附件：</text>
          <view class="files-list">
            <image v-for="(img, idx) in report.images.split(',').slice(0, 3)" :key="idx" :src="img" class="report-file"></image>
            <text v-if="report.images.split(',').length > 3" class="more-files">+{{ report.images.split(',').length - 3 }}</text>
          </view>
        </view>
        <view class="report-no">单号：{{ report.reportNo }}</view>
        <view v-if="report.workerName" class="report-worker">处理人：{{ report.workerName }}</view>
      </view>
      <view v-if="reports.length === 0" class="empty-state">
        <text>暂无报修记录</text>
      </view>
    </view>
  </view>
</template>

<script>
import { faultReportApi } from '@/api/index.js'

export default {
  data() {
    return {
      formData: {
        faultType: '',
        description: '',
        phone: '',
        address: ''
      },
      uploadedFiles: [],
      submitting: false,
      uploading: false,
      diagnosisResult: null,
      reports: [],
      elderMode: false,
      faultTypes: [
        { label: '无法上网', value: '无法上网', icon: '🚫' },
        { label: '网速慢', value: '网速慢', icon: '🐢' },
        { label: '频繁掉线', value: '频繁掉线', icon: '💔' },
        { label: '其他问题', value: '其他问题', icon: '❓' }
      ]
    }
  },
  onLoad() {
    this.loadReports()
  },
  methods: {
    selectFaultType(type) {
      this.formData.faultType = type
      this.doDiagnosis(type)
    },
    async doDiagnosis(type) {
      try {
        this.diagnosisResult = await faultReportApi.diagnose(type)
      } catch (e) {
        console.error(e)
      }
    },
    chooseImage() {
      uni.chooseImage({
        count: 6 - this.uploadedFiles.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          res.tempFilePaths.forEach(path => {
            this.uploadedFiles.push({
              url: path,
              type: 'image',
              name: path.split('/').pop()
            })
          })
        }
      })
    },
    chooseVideo() {
      uni.chooseVideo({
        sourceType: ['album', 'camera'],
        maxDuration: 30,
        camera: 'back',
        success: (res) => {
          this.uploadedFiles.push({
            url: res.tempFilePath,
            type: 'video',
            name: res.tempFilePath.split('/').pop()
          })
        }
      })
    },
    deleteFile(index) {
      uni.showModal({
        title: '提示',
        content: '确定删除该文件吗？',
        success: (res) => {
          if (res.confirm) {
            this.uploadedFiles.splice(index, 1)
          }
        }
      })
    },
    async submitReport() {
      if (!this.formData.faultType) {
        uni.showToast({ title: '请选择故障类型', icon: 'none' })
        return
      }
      if (!this.formData.description) {
        uni.showToast({ title: '请填写问题描述', icon: 'none' })
        return
      }

      this.submitting = true
      try {
        const images = this.uploadedFiles
          .filter(f => f.type === 'image')
          .map(f => f.url)
          .join(',')
        
        const videos = this.uploadedFiles
          .filter(f => f.type === 'video')
          .map(f => f.url)
          .join(',')

        await faultReportApi.create({
          userId: 1,
          faultType: this.formData.faultType,
          faultDesc: this.formData.description,
          phone: this.formData.phone || '',
          address: this.formData.address || '',
          images: images,
          video: videos
        })
        uni.showToast({ title: '报修成功', icon: 'success' })
        this.formData.faultType = ''
        this.formData.description = ''
        this.formData.phone = ''
        this.formData.address = ''
        this.uploadedFiles = []
        this.diagnosisResult = null
        this.loadReports()
      } catch (e) {
        console.error(e)
        uni.showToast({ title: '报修失败，请重试', icon: 'none' })
      } finally {
        this.submitting = false
      }
    },
    async loadReports() {
      try {
        this.reports = await faultReportApi.list(1)
      } catch (e) {
        console.error(e)
      }
    },
    getStatusClass(status) {
      if (status === 'COMPLETED') return 'status-completed'
      if (status === 'PROCESSING') return 'status-processing'
      return 'status-pending'
    }
  }
}
</script>

<style scoped>
.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 16rpx;
  font-weight: 500;
}

.fault-types {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.fault-type-item {
  width: calc(50% - 8rpx);
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
}

.fault-type-item.active {
  background: #e8eaf6;
  border-color: #667eea;
}

.fault-icon {
  font-size: 40rpx;
}

.fault-name {
  font-size: 26rpx;
  color: #333;
}

.input, .textarea {
  width: 100%;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: 2rpx solid transparent;
  transition: border-color 0.3s;
}

.input:focus, .textarea:focus {
  border-color: #667eea;
}

.textarea {
  height: 160rpx;
}

.upload-area {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.file-item {
  width: calc(33.33% - 12rpx);
  position: relative;
}

.preview-image {
  width: 100%;
  height: 160rpx;
  border-radius: 12rpx;
  background: #f0f0f0;
}

.video-preview {
  width: 100%;
  height: 160rpx;
  border-radius: 12rpx;
  background: #f0f0f0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
}

.video-icon {
  font-size: 40rpx;
}

.video-name {
  font-size: 20rpx;
  color: #666;
  max-width: 90%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.delete-btn {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 40rpx;
  height: 40rpx;
  background: #f44336;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: bold;
}

.upload-btn {
  width: calc(33.33% - 12rpx);
  height: 160rpx;
  background: #f8f9fa;
  border: 2rpx dashed #ddd;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
}

.plus-icon {
  font-size: 40rpx;
  color: #999;
}

.upload-text {
  font-size: 22rpx;
  color: #999;
}

.diagnosis-content {
  padding: 20rpx 0;
}

.diagnosis-item {
  display: flex;
  margin-bottom: 16rpx;
}

.diagnosis-label {
  font-size: 26rpx;
  color: #666;
  width: 140rpx;
}

.diagnosis-value {
  font-size: 26rpx;
  color: #333;
  font-weight: 500;
}

.diagnosis-suggestion {
  margin-top: 20rpx;
  padding: 20rpx;
  background: #fff3e0;
  border-radius: 12rpx;
}

.suggestion-label {
  font-size: 26rpx;
  color: #f57c00;
  font-weight: 500;
}

.suggestion-text {
  font-size: 24rpx;
  color: #e65100;
  line-height: 1.6;
}

.report-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.report-item:last-child {
  border-bottom: none;
}

.report-header {
  margin-bottom: 12rpx;
}

.report-type {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
}

.report-status {
  font-size: 22rpx;
  padding: 6rpx 14rpx;
  border-radius: 16rpx;
}

.status-completed {
  background: #e8f5e9;
  color: #4caf50;
}

.status-processing {
  background: #e3f2fd;
  color: #2196f3;
}

.status-pending {
  background: #fff3e0;
  color: #ff9800;
}

.report-desc {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  line-height: 1.5;
}

.report-files {
  margin-bottom: 12rpx;
}

.files-label {
  font-size: 22rpx;
  color: #999;
  display: block;
  margin-bottom: 8rpx;
}

.files-list {
  display: flex;
  gap: 8rpx;
  align-items: center;
}

.report-file {
  width: 80rpx;
  height: 80rpx;
  border-radius: 8rpx;
  background: #f0f0f0;
}

.more-files {
  font-size: 22rpx;
  color: #667eea;
  font-weight: 500;
}

.report-no, .report-worker {
  font-size: 22rpx;
  color: #999;
}

.empty-state {
  text-align: center;
  padding: 60rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
