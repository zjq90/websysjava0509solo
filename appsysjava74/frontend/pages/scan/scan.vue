<template>
    <view class="container">
        <view class="scan-header">
            <view class="scan-tip">将二维码放入框内，即可完成签到</view>
        </view>
        
        <view class="scan-area">
            <view class="scan-frame">
                <view class="scan-corner corner-tl"></view>
                <view class="scan-corner corner-tr"></view>
                <view class="scan-corner corner-bl"></view>
                <view class="scan-corner corner-br"></view>
                <view class="scan-line" :style="{ top: lineTop + '%' }"></view>
            </view>
        </view>
        
        <view class="scan-result" v-if="scanResult">
            <view class="result-success" v-if="scanResult.success">
                <u-icon name="checkmark-circle" size="80" color="#5ac725"></u-icon>
                <text class="result-title">签到成功</text>
                <text class="result-info">{{ scanResult.message }}</text>
                <view class="result-detail">
                    <view class="detail-item">
                        <text class="label">活动名称</text>
                        <text class="value">{{ scanResult.activityName }}</text>
                    </view>
                    <view class="detail-item">
                        <text class="label">签到时间</text>
                        <text class="value">{{ scanResult.signInTime }}</text>
                    </view>
                </view>
            </view>
            <view class="result-error" v-else>
                <u-icon name="info-circle" size="80" color="#f56c6c"></u-icon>
                <text class="result-title">签到失败</text>
                <text class="result-info">{{ scanResult.message }}</text>
            </view>
        </view>
        
        <view class="scan-actions">
            <u-button type="primary" size="large" @click="startScan">
                <u-icon name="scan" size="32" style="margin-right: 8rpx;"></u-icon>
                {{ scanning ? '识别中...' : '开始扫码' }}
            </u-button>
        </view>
        
        <view class="scan-manual">
            <text class="manual-tip">或输入签到码手动签到</text>
            <view class="manual-input">
                <u--input v-model="manualCode" placeholder="请输入签到码"></u--input>
                <u-button type="success" size="normal" @click="manualSignIn" :loading="submitting">
                    签到
                </u-button>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { signInApi } from '@/utils/api'
import dayjs from 'dayjs'

const scanning = ref(false)
const submitting = ref(false)
const manualCode = ref('')
const scanResult = ref(null)
const lineTop = ref(0)
let animationTimer = null

const startAnimation = () => {
    animationTimer = setInterval(() => {
        lineTop.value = (lineTop.value + 2) % 100
    }, 50)
}

const stopAnimation = () => {
    if (animationTimer) {
        clearInterval(animationTimer)
        animationTimer = null
    }
}

const startScan = () => {
    scanResult.value = null
    scanning.value = true
    startAnimation()
    
    uni.scanCode({
        onlyFromCamera: false,
        scanType: ['qrCode', 'barCode'],
        success: async (res) => {
            try {
                const qrData = JSON.parse(res.result)
                await handleSignIn(qrData)
            } catch (e) {
                scanResult.value = {
                    success: false,
                    message: '二维码格式错误'
                }
            }
        },
        fail: () => {
            scanResult.value = {
                success: false,
                message: '扫码取消或失败'
            }
        },
        complete: () => {
            scanning.value = false
            stopAnimation()
        }
    })
}

const handleSignIn = async (data) => {
    try {
        const res = await signInApi.signIn({
            activityId: data.activityId,
            qrCodeToken: data.token
        })
        
        scanResult.value = {
            success: true,
            message: '欢迎参加活动！',
            activityName: data.activityName || '活动签到',
            signInTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
        }
        
        uni.vibrateShort({ type: 'medium' })
    } catch (e) {
        scanResult.value = {
            success: false,
            message: e.message || '签到失败，请重试'
        }
    }
}

const manualSignIn = async () => {
    if (!manualCode.value) {
        uni.showToast({ title: '请输入签到码', icon: 'none' })
        return
    }
    
    submitting.value = true
    try {
        const res = await signInApi.signIn({
            activityId: manualCode.value,
            qrCodeToken: 'manual'
        })
        
        scanResult.value = {
            success: true,
            message: '欢迎参加活动！',
            activityName: '活动签到',
            signInTime: dayjs().format('YYYY-MM-DD HH:mm:ss')
        }
        
        uni.vibrateShort({ type: 'medium' })
    } catch (e) {
        scanResult.value = {
            success: false,
            message: e.message || '签到失败，请重试'
        }
    } finally {
        submitting.value = false
    }
}

onMounted(() => {
    startAnimation()
})

onUnmounted(() => {
    stopAnimation()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    background: #000;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 60rpx 40rpx;
}

.scan-header {
    text-align: center;
    margin-bottom: 60rpx;
    
    .scan-tip {
        font-size: 28rpx;
        color: #fff;
        opacity: 0.8;
    }
}

.scan-area {
    width: 500rpx;
    height: 500rpx;
    position: relative;
    margin-bottom: 60rpx;
    
    .scan-frame {
        width: 100%;
        height: 100%;
        position: relative;
        border: 2rpx solid rgba(255, 255, 255, 0.3);
        
        .scan-corner {
            position: absolute;
            width: 40rpx;
            height: 40rpx;
            border: 6rpx solid #3c9cff;
            
            &.corner-tl {
                top: -2rpx;
                left: -2rpx;
                border-right: none;
                border-bottom: none;
            }
            
            &.corner-tr {
                top: -2rpx;
                right: -2rpx;
                border-left: none;
                border-bottom: none;
            }
            
            &.corner-bl {
                bottom: -2rpx;
                left: -2rpx;
                border-right: none;
                border-top: none;
            }
            
            &.corner-br {
                bottom: -2rpx;
                right: -2rpx;
                border-left: none;
                border-top: none;
            }
        }
        
        .scan-line {
            position: absolute;
            left: 0;
            right: 0;
            height: 4rpx;
            background: linear-gradient(90deg, transparent, #3c9cff, transparent);
            transition: top 0.05s linear;
        }
    }
}

.scan-result {
    width: 100%;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 16rpx;
    padding: 40rpx;
    margin-bottom: 40rpx;
    
    .result-success, .result-error {
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .result-title {
            font-size: 36rpx;
            font-weight: bold;
            color: #fff;
            margin: 20rpx 0;
        }
        
        .result-info {
            font-size: 26rpx;
            color: rgba(255, 255, 255, 0.8);
            margin-bottom: 30rpx;
        }
        
        .result-detail {
            width: 100%;
            
            .detail-item {
                display: flex;
                justify-content: space-between;
                padding: 16rpx 0;
                border-bottom: 1rpx solid rgba(255, 255, 255, 0.1);
                
                .label {
                    font-size: 26rpx;
                    color: rgba(255, 255, 255, 0.6);
                }
                
                .value {
                    font-size: 26rpx;
                    color: #fff;
                }
            }
        }
    }
}

.scan-actions {
    width: 100%;
    margin-bottom: 40rpx;
}

.scan-manual {
    width: 100%;
    
    .manual-tip {
        display: block;
        text-align: center;
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.6);
        margin-bottom: 20rpx;
    }
    
    .manual-input {
        display: flex;
        gap: 16rpx;
        
        :deep(.u-input) {
            flex: 1;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 8rpx;
        }
    }
}
</style>
