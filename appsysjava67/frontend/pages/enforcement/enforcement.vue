<template>
    <view class="container">
        <view class="form-section">
            <view class="form-card">
                <view class="form-header">
                    <text class="form-title">现场执法记录</text>
                </view>

                <view class="form-item">
                    <text class="form-label">车牌号</text>
                    <input 
                        class="form-input" 
                        v-model="formData.plateNumber" 
                        placeholder="请输入车牌号"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">违规类型</text>
                    <picker 
                        mode="selector" 
                        :range="violationTypes" 
                        range-key="label"
                        @change="onViolationTypeChange"
                    >
                        <view class="picker-value">
                            {{ violationTypes[violationTypeIndex].label || '请选择违规类型' }}
                            <text class="picker-arrow">▼</text>
                        </view>
                    </picker>
                </view>

                <view class="form-item">
                    <text class="form-label">违规描述</text>
                    <textarea 
                        class="form-textarea" 
                        v-model="formData.violationDescription" 
                        placeholder="请描述违规情况"
                        maxlength="500"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">处罚金额（元）</text>
                    <input 
                        class="form-input" 
                        type="digit"
                        v-model="formData.penaltyAmount" 
                        placeholder="请输入处罚金额"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">执法地点</text>
                    <input 
                        class="form-input" 
                        v-model="formData.location" 
                        placeholder="请输入执法地点"
                    />
                    <view class="location-btn" @click="getLocation">
                        <text class="location-icon">📍</text>
                        <text>获取位置</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="photo-section">
            <view class="section-header">
                <text class="section-title">现场照片</text>
                <text class="photo-count">{{ photoList.length }}/9</text>
            </view>
            <view class="photo-grid">
                <view 
                    class="photo-item" 
                    v-for="(photo, index) in photoList" 
                    :key="index"
                >
                    <image :src="photo" mode="aspectFill" class="photo-img" />
                    <view class="photo-delete" @click="deletePhoto(index)">×</view>
                </view>
                <view class="photo-add" @click="addPhoto" v-if="photoList.length < 9">
                    <text class="add-icon">+</text>
                    <text class="add-text">拍照</text>
                </view>
            </view>
        </view>

        <view class="signature-section">
            <view class="section-header">
                <text class="section-title">签名确认</text>
            </view>
            <view class="signature-row">
                <view class="signature-box" @click="goSignature('officer')">
                    <image v-if="formData.officerSignature" :src="formData.officerSignature" class="signature-img" mode="aspectFit" />
                    <view v-else class="signature-placeholder">
                        <text class="placeholder-text">执法人签名</text>
                    </view>
                </view>
                <view class="signature-box" @click="goSignature('party')">
                    <image v-if="formData.partySignature" :src="formData.partySignature" class="signature-img" mode="aspectFit" />
                    <view v-else class="signature-placeholder">
                        <text class="placeholder-text">当事人签名</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="form-item">
            <text class="form-label">备注</text>
            <textarea 
                class="form-textarea" 
                v-model="formData.remarks" 
                placeholder="添加备注信息"
                maxlength="500"
            />
        </view>

        <view class="submit-section">
            <button class="btn-submit" @click="submitForm">提交记录</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            formData: {
                plateNumber: '',
                violationType: '',
                violationDescription: '',
                penaltyAmount: '',
                location: '',
                longitude: null,
                latitude: null,
                officerSignature: '',
                partySignature: '',
                remarks: ''
            },
            photoList: [],
            violationTypes: [
                { value: 'TRAFFIC_VIOLATION', label: '交通违规' },
                { value: 'SUSPICIOUS_PLATE', label: '套牌嫌疑' },
                { value: 'NO_LICENSE', label: '无证驾驶' },
                { value: 'DRUNK_DRIVING', label: '酒驾' },
                { value: 'OVERLOAD', label: '超载' },
                { value: 'OTHER', label: '其他' }
            ],
            violationTypeIndex: 0,
            signatureType: ''
        }
    },
    onLoad(options) {
        if (options.plateNumber) {
            this.formData.plateNumber = options.plateNumber
        }
    },
    onShow() {
        const signature = uni.getStorageSync('signatureData')
        const type = uni.getStorageSync('signatureType')
        if (signature && type) {
            if (type === 'officer') {
                this.formData.officerSignature = signature
            } else if (type === 'party') {
                this.formData.partySignature = signature
            }
            uni.removeStorageSync('signatureData')
            uni.removeStorageSync('signatureType')
        }
    },
    methods: {
        onViolationTypeChange(e) {
            this.violationTypeIndex = e.detail.value
            this.formData.violationType = this.violationTypes[e.detail.value].value
        },
        getLocation() {
            uni.getLocation({
                type: 'gcj02',
                success: (res) => {
                    this.formData.longitude = res.longitude
                    this.formData.latitude = res.latitude
                    this.formData.location = `${res.latitude.toFixed(4)}, ${res.longitude.toFixed(4)}`
                    uni.showToast({
                        title: '位置获取成功',
                        icon: 'success'
                    })
                },
                fail: () => {
                    uni.showToast({
                        title: '位置获取失败',
                        icon: 'none'
                    })
                }
            })
        },
        addPhoto() {
            uni.chooseImage({
                count: 9 - this.photoList.length,
                sourceType: ['camera', 'album'],
                success: (res) => {
                    this.photoList = [...this.photoList, ...res.tempFilePaths]
                }
            })
        },
        deletePhoto(index) {
            this.photoList.splice(index, 1)
        },
        goSignature(type) {
            this.signatureType = type
            uni.setStorageSync('signatureType', type)
            uni.navigateTo({
                url: '/pages/signature/signature'
            })
        },
        submitForm() {
            if (!this.formData.plateNumber) {
                uni.showToast({
                    title: '请输入车牌号',
                    icon: 'none'
                })
                return
            }

            uni.showLoading({
                title: '提交中...'
            })

            const submitData = {
                ...this.formData,
                photoUrls: this.photoList.join(','),
                officerName: '当前执法人'
            }

            this.$request({
                url: '/enforcement',
                method: 'POST',
                data: submitData
            }).then(() => {
                uni.hideLoading()
                this.saveLocalRecord(submitData)
                uni.showToast({
                    title: '提交成功',
                    icon: 'success'
                })
                setTimeout(() => {
                    uni.navigateBack()
                }, 1500)
            }).catch(() => {
                uni.hideLoading()
                this.saveLocalRecord(submitData)
                uni.showToast({
                    title: '已缓存，待同步',
                    icon: 'none'
                })
                setTimeout(() => {
                    uni.navigateBack()
                }, 1500)
            })
        },
        saveLocalRecord(data) {
            const records = uni.getStorageSync('enforcementRecords') || []
            records.unshift({
                ...data,
                timestamp: Date.now(),
                synced: false
            })
            uni.setStorageSync('enforcementRecords', records.slice(0, 100))
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 120rpx;
}

.form-card {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 30rpx;
}

.form-header {
    margin-bottom: 30rpx;
}

.form-title {
    font-size: 36rpx;
    font-weight: bold;
    color: var(--text-color);
}

.form-item {
    margin-bottom: 30rpx;
}

.form-label {
    font-size: 28rpx;
    color: var(--text-color);
    margin-bottom: 16rpx;
    display: block;
}

.form-input {
    width: 100%;
    height: 80rpx;
    background: var(--bg-color);
    border-radius: 12rpx;
    padding: 0 24rpx;
    font-size: 30rpx;
    box-sizing: border-box;
}

.form-textarea {
    width: 100%;
    min-height: 160rpx;
    background: var(--bg-color);
    border-radius: 12rpx;
    padding: 20rpx 24rpx;
    font-size: 30rpx;
    box-sizing: border-box;
}

.picker-value {
    height: 80rpx;
    line-height: 80rpx;
    background: var(--bg-color);
    border-radius: 12rpx;
    padding: 0 24rpx;
    font-size: 30rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: var(--text-color);
}

.picker-arrow {
    color: var(--text-secondary);
    font-size: 24rpx;
}

.location-btn {
    margin-top: 16rpx;
    padding: 16rpx 24rpx;
    background: var(--primary-color);
    color: white;
    border-radius: 8rpx;
    font-size: 26rpx;
    display: inline-flex;
    align-items: center;
    gap: 8rpx;
}

.location-icon {
    font-size: 28rpx;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: var(--text-color);
}

.photo-count {
    font-size: 26rpx;
    color: var(--text-secondary);
}

.photo-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
    margin-bottom: 30rpx;
}

.photo-item {
    width: 200rpx;
    height: 200rpx;
    position: relative;
    border-radius: 12rpx;
    overflow: hidden;
}

.photo-img {
    width: 100%;
    height: 100%;
}

.photo-delete {
    position: absolute;
    top: 8rpx;
    right: 8rpx;
    width: 40rpx;
    height: 40rpx;
    background: rgba(0, 0, 0, 0.6);
    color: white;
    border-radius: 50%;
    text-align: center;
    line-height: 36rpx;
    font-size: 32rpx;
}

.photo-add {
    width: 200rpx;
    height: 200rpx;
    background: var(--bg-color);
    border: 2rpx dashed var(--border-color);
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.add-icon {
    font-size: 48rpx;
    color: var(--text-secondary);
}

.add-text {
    font-size: 24rpx;
    color: var(--text-secondary);
    margin-top: 8rpx;
}

.signature-row {
    display: flex;
    gap: 20rpx;
    margin-bottom: 30rpx;
}

.signature-box {
    flex: 1;
    height: 200rpx;
    background: var(--bg-color);
    border: 2rpx solid var(--border-color);
    border-radius: 12rpx;
    overflow: hidden;
}

.signature-img {
    width: 100%;
    height: 100%;
}

.signature-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.placeholder-text {
    font-size: 26rpx;
    color: var(--text-secondary);
}

.submit-section {
    margin-top: 40rpx;
}

.btn-submit {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
    color: white;
    border-radius: 50rpx;
    font-size: 32rpx;
    font-weight: 500;
    border: none;
}
</style>
