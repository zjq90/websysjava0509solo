<template>
    <view class="add-container">
        <view class="step-indicator">
            <view 
                class="step-item" 
                :class="{ active: currentStep >= 1, done: currentStep > 1 }"
                v-for="(step, index) in steps" 
                :key="index"
            >
                <view class="step-circle">
                    <text v-if="currentStep > index + 1">✓</text>
                    <text v-else>{{ index + 1 }}</text>
                </view>
                <text class="step-label">{{ step }}</text>
            </view>
        </view>

        <view class="form-content">
            <view class="step-panel" v-if="currentStep === 1">
                <view class="section-title">选择地块</view>
                
                <view class="location-btn" @click="getLocation">
                    <text class="location-icon">📍</text>
                    <text class="location-text">{{ locationInfo || '点击定位当前位置' }}</text>
                </view>

                <view class="form-item">
                    <text class="form-label">选择地块 *</text>
                    <picker 
                        :range="plotOptions" 
                        range-key="plotName" 
                        :value="selectedPlotIndex"
                        @change="onPlotChange"
                    >
                        <view class="picker-input">
                            {{ (plotOptions[selectedPlotIndex] && plotOptions[selectedPlotIndex].plotName) || '请选择地块' }}
                            <text class="picker-arrow">▼</text>
                        </view>
                    </picker>
                </view>
            </view>

            <view class="step-panel" v-if="currentStep === 2">
                <view class="section-title">选择作物</view>

                <view class="form-item">
                    <text class="form-label">作物类型</text>
                    <view class="type-tabs">
                        <view 
                            class="type-tab" 
                            :class="{ active: selectedCropType === type.value }"
                            v-for="type in cropTypes" 
                            :key="type.value"
                            @click="onCropTypeChange(type.value)"
                        >
                            <text class="type-icon">{{ type.icon }}</text>
                            <text class="type-label">{{ type.label }}</text>
                        </view>
                    </view>
                </view>

                <view class="form-item">
                    <text class="form-label">选择品种 *</text>
                    <picker 
                        :range="filteredCrops" 
                        range-key="cropName" 
                        :value="selectedCropIndex"
                        @change="onCropChange"
                    >
                        <view class="picker-input">
                            {{ (filteredCrops[selectedCropIndex] && filteredCrops[selectedCropIndex].cropName) || '请选择品种' }}
                            <text class="picker-arrow">▼</text>
                        </view>
                    </picker>
                </view>

                <view class="template-section" v-if="availableTemplates.length > 0">
                    <view class="section-title">选择观测模板</view>
                    <view 
                        class="template-item" 
                        :class="{ active: selectedTemplateId === item.id }"
                        v-for="item in availableTemplates" 
                        :key="item.id"
                        @click="selectedTemplateId = item.id"
                    >
                        <view class="template-info">
                            <text class="template-name">{{ item.templateName }}</text>
                            <text class="template-desc">{{ item.description }}</text>
                        </view>
                        <radio :checked="selectedTemplateId === item.id" color="#4CAF50" />
                    </view>
                </view>
            </view>

            <view class="step-panel" v-if="currentStep === 3">
                <view class="section-title">录入数据</view>

                <view class="form-item">
                    <text class="form-label">生长阶段 *</text>
                    <view class="stage-grid">
                        <view 
                            class="stage-item" 
                            :class="{ active: formData.growthStage === item.value }"
                            v-for="item in growthStages" 
                            :key="item.value"
                            @click="formData.growthStage = item.value"
                        >
                            <text>{{ item.label }}</text>
                        </view>
                    </view>
                </view>

                <view class="form-item">
                    <text class="form-label">记录日期</text>
                    <picker mode="date" :value="recordDate" @change="onDateChange">
                        <view class="picker-input">
                            {{ recordDate || '选择日期' }}
                            <text class="picker-arrow">📅</text>
                        </view>
                    </picker>
                </view>

                <view class="form-item">
                    <text class="form-label">株高 (cm)</text>
                    <input 
                        class="form-input" 
                        type="digit" 
                        v-model="formData.plantHeight" 
                        placeholder="请输入株高"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">出苗率 (%)</text>
                    <input 
                        class="form-input" 
                        type="digit" 
                        v-model="formData.emergenceRate" 
                        placeholder="请输入出苗率 (0-100)"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">病虫害等级</text>
                    <view class="pest-levels">
                        <view 
                            class="pest-item" 
                            :class="{ active: formData.pestLevel === index }"
                            v-for="(item, index) in pestLevels" 
                            :key="index"
                            @click="formData.pestLevel = index"
                        >
                            <text class="pest-level">{{ index }}</text>
                            <text class="pest-label">{{ item }}</text>
                        </view>
                    </view>
                </view>

                <view class="form-item">
                    <text class="form-label">病虫害种类</text>
                    <input 
                        class="form-input" 
                        type="text" 
                        v-model="formData.pestType" 
                        placeholder="如：蚜虫、红蜘蛛等"
                    />
                </view>

                <view class="form-item">
                    <text class="form-label">叶色</text>
                    <view class="color-options">
                        <view 
                            class="color-item" 
                            :class="{ active: formData.leafColor === item.value }"
                            v-for="item in leafColors" 
                            :key="item.value"
                            @click="formData.leafColor = item.value"
                        >
                            <view class="color-dot" :style="{ background: item.color }"></view>
                            <text>{{ item.label }}</text>
                        </view>
                    </view>
                </view>

                <view class="form-item">
                    <text class="form-label">生育期描述</text>
                    <textarea 
                        class="form-textarea" 
                        v-model="formData.growthDescription" 
                        placeholder="请输入生育期描述"
                    />
                </view>
            </view>

            <view class="step-panel" v-if="currentStep === 4">
                <view class="section-title">上传照片</view>

                <view class="image-grid">
                    <view 
                        class="image-item" 
                        v-for="(img, index) in images" 
                        :key="index"
                    >
                        <image :src="img.path" mode="aspectFill" class="preview-image" />
                        <view class="delete-btn" @click="removeImage(index)">✕</view>
                    </view>
                    <view class="add-image" @click="chooseImage" v-if="images.length < 9">
                        <text class="add-icon">📷</text>
                        <text class="add-text">添加照片</text>
                    </view>
                </view>

                <view class="form-item mt-30">
                    <text class="form-label">照片描述</text>
                    <textarea 
                        class="form-textarea" 
                        v-model="formData.imageDescription" 
                        placeholder="请输入照片描述（选填）"
                    />
                </view>
            </view>
        </view>

        <view class="footer-actions">
            <view class="btn-secondary" v-if="currentStep > 1" @click="prevStep">
                上一步
            </view>
            <view class="btn-primary" v-if="currentStep < 4" @click="nextStep">
                下一步
            </view>
            <view class="btn-primary submit-btn" v-if="currentStep === 4" @click="submitRecord">
                提交记录
            </view>
            <view class="btn-secondary offline-btn" v-if="currentStep === 4" @click="saveOffline">
                保存离线
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            currentStep: 1,
            steps: ['选择地块', '选择作物', '录入数据', '上传照片'],
            
            locationInfo: '',
            plots: [],
            plotOptions: [],
            selectedPlotIndex: 0,
            
            crops: [],
            cropTypes: [
                { value: 'CORN', label: '玉米', icon: '🌽' },
                { value: 'WHEAT', label: '小麦', icon: '🌾' },
                { value: 'RICE', label: '水稻', icon: '🍚' },
                { value: 'SOYBEAN', label: '大豆', icon: '🫘' }
            ],
            selectedCropType: 'CORN',
            selectedCropIndex: 0,
            
            templates: [],
            availableTemplates: [],
            selectedTemplateId: null,
            
            growthStages: [
                { value: 'SEEDLING', label: '出苗期' },
                { value: 'TILLERING', label: '分蘖期' },
                { value: 'JOINTING', label: '拔节期' },
                { value: 'BOOTING', label: '孕穗期' },
                { value: 'HEADING', label: '抽穗期' },
                { value: 'FLOWERING', label: '开花期' },
                { value: 'FILLING', label: '灌浆期' },
                { value: 'MATURING', label: '成熟期' }
            ],
            
            pestLevels: ['无', '轻度', '中度', '重度', '严重'],
            
            leafColors: [
                { value: 'DARK_GREEN', label: '深绿', color: '#2E7D32' },
                { value: 'GREEN', label: '绿色', color: '#4CAF50' },
                { value: 'LIGHT_GREEN', label: '浅绿', color: '#8BC34A' },
                { value: 'YELLOW', label: '黄色', color: '#FFEB3B' }
            ],
            
            recordDate: '',
            
            formData: {
                growthStage: '',
                plantHeight: '',
                emergenceRate: '',
                pestLevel: 0,
                pestType: '',
                leafColor: '',
                growthDescription: '',
                imageDescription: ''
            },
            
            images: []
        }
    },

    computed: {
        filteredCrops() {
            return this.crops.filter(c => c.cropType === this.selectedCropType)
        }
    },

    onLoad() {
        this.recordDate = this.getToday()
        this.loadPlots()
        this.loadCrops()
        this.loadTemplates()
    },

    methods: {
        getToday() {
            const today = new Date()
            return `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
        },

        async loadPlots() {
            try {
                const data = await this.$request({
                    url: '/plots/active',
                    method: 'GET'
                })
                this.plots = data || []
                this.plotOptions = data || []
            } catch (e) {
                console.error('加载地块失败', e)
            }
        },

        async loadCrops() {
            try {
                const data = await this.$request({
                    url: '/crops/active',
                    method: 'GET'
                })
                this.crops = data || []
            } catch (e) {
                console.error('加载作物失败', e)
            }
        },

        async loadTemplates() {
            try {
                const data = await this.$request({
                    url: '/templates/system',
                    method: 'GET'
                })
                this.templates = data || []
            } catch (e) {
                console.error('加载模板失败', e)
            }
        },

        onCropTypeChange(type) {
            this.selectedCropType = type
            this.selectedCropIndex = 0
            this.availableTemplates = this.templates.filter(t => t.cropType === type)
        },

        onPlotChange(e) {
            this.selectedPlotIndex = e.detail.value
        },

        onCropChange(e) {
            this.selectedCropIndex = e.detail.value
        },

        onDateChange(e) {
            this.recordDate = e.detail.value
        },

        getLocation() {
            uni.showLoading({ title: '定位中...' })
            uni.getLocation({
                type: 'gcj02',
                success: (res) => {
                    uni.hideLoading()
                    this.locationInfo = `纬度: ${res.latitude.toFixed(4)}, 经度: ${res.longitude.toFixed(4)}`
                    uni.showToast({
                        title: '定位成功',
                        icon: 'success'
                    })
                },
                fail: () => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '定位失败，请手动选择',
                        icon: 'none'
                    })
                }
            })
        },

        chooseImage() {
            uni.chooseImage({
                count: 9 - this.images.length,
                sizeType: ['compressed'],
                sourceType: ['album', 'camera'],
                success: (res) => {
                    res.tempFilePaths.forEach(path => {
                        this.images.push({
                            path,
                            type: 'PEST'
                        })
                    })
                }
            })
        },

        removeImage(index) {
            this.images.splice(index, 1)
        },

        validateStep() {
            switch (this.currentStep) {
                case 1:
                    if (!this.plotOptions[this.selectedPlotIndex]) {
                        uni.showToast({ title: '请选择地块', icon: 'none' })
                        return false
                    }
                    return true
                case 2:
                    if (!this.filteredCrops[this.selectedCropIndex]) {
                        uni.showToast({ title: '请选择作物品种', icon: 'none' })
                        return false
                    }
                    return true
                case 3:
                    if (!this.formData.growthStage) {
                        uni.showToast({ title: '请选择生长阶段', icon: 'none' })
                        return false
                    }
                    return true
                case 4:
                    return true
                default:
                    return true
            }
        },

        prevStep() {
            if (this.currentStep > 1) {
                this.currentStep--
            }
        },

        nextStep() {
            if (this.validateStep() && this.currentStep < 4) {
                this.currentStep++
            }
        },

        async submitRecord() {
            if (!this.validateStep()) return

            const selectedPlot = this.plotOptions[this.selectedPlotIndex]
            const selectedCrop = this.filteredCrops[this.selectedCropIndex]
            const userInfo = this.$store.getters.userInfo

            const recordData = {
                plotId: selectedPlot.id,
                plotName: selectedPlot.plotName,
                cropId: selectedCrop.id,
                cropName: selectedCrop.cropName,
                growthStage: this.formData.growthStage,
                recordDate: this.recordDate + 'T00:00:00',
                plantHeight: this.formData.plantHeight ? parseFloat(this.formData.plantHeight) : null,
                emergenceRate: this.formData.emergenceRate ? parseFloat(this.formData.emergenceRate) : null,
                pestLevel: this.formData.pestLevel,
                pestType: this.formData.pestType,
                leafColor: this.formData.leafColor,
                growthDescription: this.formData.growthDescription,
                observer: userInfo.realName,
                observerId: userInfo.userId,
                dataSource: 'ONLINE',
                status: 'SUBMITTED',
                remarks: this.formData.imageDescription
            }

            try {
                uni.showLoading({ title: '提交中...' })
                
                const result = await this.$request({
                    url: '/field-records',
                    method: 'POST',
                    data: recordData
                })

                if (this.images.length > 0 && result.id) {
                    await this.saveImages(result.id)
                }

                uni.hideLoading()
                uni.showToast({
                    title: '提交成功',
                    icon: 'success'
                })

                setTimeout(() => {
                    uni.switchTab({ url: '/pages/field-record/list' })
                }, 1500)
            } catch (e) {
                uni.hideLoading()
            }
        },

        async saveImages(recordId) {
            const imageData = this.images.map(img => ({
                fieldRecordId: recordId,
                imagePath: img.path,
                imageType: img.type,
                description: this.formData.imageDescription,
                status: 'ACTIVE',
                uploadedBy: this.$store.getters.userInfo && this.$store.getters.userInfo.userId
            }))

            try {
                await this.$request({
                    url: '/pest-images/batch',
                    method: 'POST',
                    data: imageData
                })
            } catch (e) {
                console.error('保存图片失败', e)
            }
        },

        saveOffline() {
            const selectedPlot = this.plotOptions[this.selectedPlotIndex]
            const selectedCrop = this.filteredCrops[this.selectedCropIndex]

            const offlineRecord = {
                plotId: selectedPlot && selectedPlot.id,
                plotName: selectedPlot && selectedPlot.plotName,
                cropId: selectedCrop && selectedCrop.id,
                cropName: selectedCrop && selectedCrop.cropName,
                ...this.formData,
                recordDate: this.recordDate,
                images: this.images
            }

            this.$store.dispatch('saveOfflineRecord', offlineRecord)
            
            setTimeout(() => {
                uni.switchTab({ url: '/pages/field-record/list' })
            }, 1000)
        }
    }
}
</script>

<style>
.add-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 180rpx;
}

.step-indicator {
    display: flex;
    justify-content: space-between;
    padding: 40rpx 30rpx;
    background-color: #fff;
    position: relative;
}

.step-indicator::before {
    content: '';
    position: absolute;
    top: 72rpx;
    left: 80rpx;
    right: 80rpx;
    height: 2rpx;
    background-color: #e0e0e0;
    z-index: 1;
}

.step-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: relative;
    z-index: 2;
}

.step-circle {
    width: 56rpx;
    height: 56rpx;
    border-radius: 50%;
    background-color: #f0f0f0;
    color: #999;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24rpx;
    margin-bottom: 10rpx;
    border: 2rpx solid #e0e0e0;
}

.step-item.active .step-circle,
.step-item.done .step-circle {
    background-color: #4CAF50;
    border-color: #4CAF50;
    color: #fff;
}

.step-label {
    font-size: 22rpx;
    color: #999;
}

.step-item.active .step-label,
.step-item.done .step-label {
    color: #4CAF50;
}

.form-content {
    padding: 20rpx;
    margin-top: 20rpx;
}

.step-panel {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 30rpx;
    padding-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.location-btn {
    display: flex;
    align-items: center;
    background-color: #E8F5E9;
    padding: 24rpx;
    border-radius: 12rpx;
    margin-bottom: 30rpx;
}

.location-icon {
    font-size: 40rpx;
    margin-right: 16rpx;
}

.location-text {
    flex: 1;
    font-size: 28rpx;
    color: #4CAF50;
}

.form-item {
    margin-bottom: 30rpx;
}

.form-label {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 16rpx;
    display: block;
}

.form-input {
    background-color: #f8f8f8;
    border-radius: 12rpx;
    height: 88rpx;
    padding: 0 24rpx;
    font-size: 28rpx;
}

.form-textarea {
    background-color: #f8f8f8;
    border-radius: 12rpx;
    padding: 24rpx;
    font-size: 28rpx;
    min-height: 160rpx;
    width: 100%;
}

.picker-input {
    background-color: #f8f8f8;
    border-radius: 12rpx;
    height: 88rpx;
    padding: 0 24rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 28rpx;
    color: #333;
}

.picker-arrow {
    font-size: 24rpx;
    color: #999;
}

.type-tabs {
    display: flex;
    flex-wrap: wrap;
}

.type-tab {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
    border-radius: 12rpx;
}

.type-tab.active {
    background-color: #E8F5E9;
}

.type-icon {
    font-size: 44rpx;
    margin-bottom: 8rpx;
}

.type-label {
    font-size: 24rpx;
    color: #666;
}

.type-tab.active .type-label {
    color: #4CAF50;
}

.template-section {
    margin-top: 30rpx;
}

.template-item {
    display: flex;
    align-items: center;
    padding: 20rpx;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    margin-bottom: 16rpx;
}

.template-item.active {
    background-color: #E8F5E9;
    border: 2rpx solid #4CAF50;
}

.template-info {
    flex: 1;
}

.template-name {
    font-size: 28rpx;
    color: #333;
    display: block;
}

.template-desc {
    font-size: 24rpx;
    color: #999;
    margin-top: 6rpx;
}

.stage-grid {
    display: flex;
    flex-wrap: wrap;
}

.stage-item {
    width: 33.33%;
    padding: 10rpx;
    box-sizing: border-box;
}

.stage-item text {
    display: block;
    text-align: center;
    padding: 16rpx 8rpx;
    background-color: #f8f8f8;
    border-radius: 8rpx;
    font-size: 24rpx;
    color: #666;
}

.stage-item.active text {
    background-color: #4CAF50;
    color: #fff;
}

.pest-levels {
    display: flex;
}

.pest-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 16rpx 0;
    margin: 0 6rpx;
    background-color: #f8f8f8;
    border-radius: 8rpx;
}

.pest-item.active {
    background-color: #4CAF50;
}

.pest-item.active .pest-level,
.pest-item.active .pest-label {
    color: #fff;
}

.pest-level {
    font-size: 32rpx;
    font-weight: bold;
    color: #666;
}

.pest-label {
    font-size: 22rpx;
    color: #999;
    margin-top: 4rpx;
}

.color-options {
    display: flex;
    align-items: center;
}

.color-item {
    display: flex;
    align-items: center;
    margin-right: 30rpx;
    padding: 10rpx 20rpx;
    border-radius: 8rpx;
}

.color-item.active {
    background-color: #f0f0f0;
}

.color-dot {
    width: 28rpx;
    height: 28rpx;
    border-radius: 50%;
    margin-right: 10rpx;
}

.color-item text {
    font-size: 26rpx;
    color: #333;
}

.image-grid {
    display: flex;
    flex-wrap: wrap;
}

.image-item, .add-image {
    width: 200rpx;
    height: 200rpx;
    margin-right: 20rpx;
    margin-bottom: 20rpx;
    position: relative;
}

.preview-image {
    width: 100%;
    height: 100%;
    border-radius: 12rpx;
}

.delete-btn {
    position: absolute;
    top: -10rpx;
    right: -10rpx;
    width: 40rpx;
    height: 40rpx;
    background-color: rgba(0, 0, 0, 0.6);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 24rpx;
}

.add-image {
    background-color: #f8f8f8;
    border: 2rpx dashed #ddd;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.add-icon {
    font-size: 60rpx;
}

.add-text {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
}

.footer-actions {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    padding: 20rpx;
    display: flex;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.footer-actions .btn-secondary,
.footer-actions .btn-primary {
    flex: 1;
    margin: 0 10rpx;
    height: 88rpx;
    line-height: 88rpx;
}

.offline-btn {
    background-color: #FFF3E0;
    color: #FF9800;
    border-color: #FFE082;
}

.submit-btn {
    background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
}
</style>
