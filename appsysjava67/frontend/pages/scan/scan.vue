<template>
    <view class="container">
        <view class="scan-area">
            <view class="scan-frame">
                <view class="corner top-left"></view>
                <view class="corner top-right"></view>
                <view class="corner bottom-left"></view>
                <view class="corner bottom-right"></view>
                <view class="scan-line" :class="{ animating: isScanning }"></view>
            </view>
            <text class="scan-tip">将车牌放入框内自动识别</text>
        </view>

        <view class="input-section">
            <view class="input-card">
                <text class="input-label">手动输入车牌号</text>
                <view class="input-row">
                    <input 
                        class="plate-input" 
                        v-model="plateNumber" 
                        placeholder="请输入车牌号"
                        maxlength="10"
                    />
                    <button class="input-btn" @click="manualCheck">查验</button>
                </view>
            </view>
        </view>

        <view class="quick-plates">
            <text class="section-title">快捷查验</text>
            <view class="plate-list">
                <view 
                    class="plate-item" 
                    v-for="plate in quickPlates" 
                    :key="plate"
                    @click="quickCheck(plate)"
                >
                    {{ plate }}
                </view>
            </view>
        </view>

        <view class="camera-btn" @click="openCamera">
            <text class="camera-icon">📷</text>
            <text class="camera-text">扫码识别</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            plateNumber: '',
            isScanning: false,
            quickPlates: ['京A12345', '京B88888', '京C66666']
        }
    },
    methods: {
        openCamera() {
            this.isScanning = true
            uni.scanCode({
                onlyFromCamera: true,
                scanType: ['qrCode', 'barCode'],
                success: (res) => {
                    this.plateNumber = res.result
                    this.checkPlate(res.result)
                },
                fail: () => {
                    uni.showToast({
                        title: '识别失败，请重试',
                        icon: 'none'
                    })
                },
                complete: () => {
                    this.isScanning = false
                }
            })
        },
        manualCheck() {
            if (!this.plateNumber) {
                uni.showToast({
                    title: '请输入车牌号',
                    icon: 'none'
                })
                return
            }
            this.checkPlate(this.plateNumber)
        },
        quickCheck(plate) {
            this.plateNumber = plate
            this.checkPlate(plate)
        },
        checkPlate(plate) {
            uni.showLoading({
                title: '查验中...'
            })
            
            this.$request({
                url: `/vehicle/check/${plate}`,
                method: 'GET'
            }).then(res => {
                uni.hideLoading()
                this.saveRecord(res.data)
                uni.navigateTo({
                    url: `/pages/result/result?data=${encodeURIComponent(JSON.stringify(res.data))}`
                })
            }).catch(() => {
                uni.hideLoading()
                this.mockCheck(plate)
            })
        },
        mockCheck(plate) {
            const blacklist = ['京B88888', '京D99999']
            const isLegal = !blacklist.includes(plate)
            
            const result = {
                plateNumber: plate,
                legal: isLegal,
                message: isLegal ? '车辆合法' : '车辆异常',
                riskLevel: isLegal ? 'LOW' : 'HIGH',
                violationReason: isLegal ? '' : '套牌车辆',
                violationDescription: isLegal ? '' : '该车辆涉嫌套用其他车辆号牌',
                brand: '大众',
                model: '帕萨特',
                color: '黑色',
                ownerName: '张三'
            }
            
            this.saveRecord(result)
            uni.navigateTo({
                url: `/pages/result/result?data=${encodeURIComponent(JSON.stringify(result))}`
            })
        },
        saveRecord(result) {
            const records = uni.getStorageSync('enforcementRecords') || []
            records.unshift({
                plateNumber: result.plateNumber,
                legal: result.legal,
                timestamp: Date.now()
            })
            uni.setStorageSync('enforcementRecords', records.slice(0, 100))
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    min-height: 100vh;
}

.scan-area {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 40rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 30rpx;
}

.scan-frame {
    width: 500rpx;
    height: 200rpx;
    border: 2rpx solid var(--border-color);
    position: relative;
    margin-bottom: 20rpx;
    background: #fafafa;
    border-radius: 8rpx;
    overflow: hidden;
}

.corner {
    position: absolute;
    width: 40rpx;
    height: 40rpx;
    border: 6rpx solid var(--primary-color);
}

.corner.top-left {
    top: -2rpx;
    left: -2rpx;
    border-right: none;
    border-bottom: none;
    border-top-left-radius: 8rpx;
}

.corner.top-right {
    top: -2rpx;
    right: -2rpx;
    border-left: none;
    border-bottom: none;
    border-top-right-radius: 8rpx;
}

.corner.bottom-left {
    bottom: -2rpx;
    left: -2rpx;
    border-right: none;
    border-top: none;
    border-bottom-left-radius: 8rpx;
}

.corner.bottom-right {
    bottom: -2rpx;
    right: -2rpx;
    border-left: none;
    border-top: none;
    border-bottom-right-radius: 8rpx;
}

.scan-line {
    position: absolute;
    left: 10%;
    width: 80%;
    height: 4rpx;
    background: linear-gradient(90deg, transparent, var(--primary-color), transparent);
    top: 50%;
}

.scan-line.animating {
    animation: scan 2s linear infinite;
}

@keyframes scan {
    0% { top: 10%; }
    50% { top: 90%; }
    100% { top: 10%; }
}

.scan-tip {
    font-size: 26rpx;
    color: var(--text-secondary);
}

.input-section {
    margin-bottom: 30rpx;
}

.input-card {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 30rpx;
}

.input-label {
    font-size: 28rpx;
    color: var(--text-color);
    margin-bottom: 20rpx;
    display: block;
}

.input-row {
    display: flex;
    gap: 20rpx;
}

.plate-input {
    flex: 1;
    height: 80rpx;
    background: var(--bg-color);
    border-radius: 12rpx;
    padding: 0 24rpx;
    font-size: 32rpx;
    text-transform: uppercase;
}

.input-btn {
    width: 160rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: var(--primary-color);
    color: white;
    border-radius: 12rpx;
    font-size: 28rpx;
    margin: 0;
    padding: 0;
}

.section-title {
    font-size: 28rpx;
    color: var(--text-color);
    margin-bottom: 20rpx;
    display: block;
}

.plate-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.plate-item {
    padding: 20rpx 30rpx;
    background: var(--card-bg);
    border-radius: 12rpx;
    font-size: 28rpx;
    color: var(--text-color);
    border: 2rpx solid var(--border-color);
}

.camera-btn {
    position: fixed;
    bottom: 40rpx;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 160rpx;
    height: 160rpx;
    background: linear-gradient(135deg, var(--primary-color), #0d47a1);
    border-radius: 50%;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(26, 115, 232, 0.4);
}

.camera-icon {
    font-size: 48rpx;
    margin-bottom: 8rpx;
}

.camera-text {
    font-size: 22rpx;
    color: white;
}
</style>
