<template>
    <view class="container">
        <view class="signature-header">
            <text class="signature-title">请在下方签名</text>
            <text class="signature-subtitle">签名区域</text>
        </view>
        
        <view class="signature-canvas-wrapper">
            <canvas 
                canvas-id="signatureCanvas" 
                class="signature-canvas"
                @touchstart="touchStart"
                @touchmove="touchMove"
                @touchend="touchEnd"
            ></canvas>
        </view>

        <view class="signature-actions">
            <button class="action-btn clear" @click="clearCanvas">清除</button>
            <button class="action-btn save" @click="saveSignature">确认签名</button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            ctx: null,
            isDrawing: false,
            lastX: 0,
            lastY: 0,
            hasSignature: false
        }
    },
    onReady() {
        this.ctx = uni.createCanvasContext('signatureCanvas', this)
        this.ctx.setStrokeStyle('#333333')
        this.ctx.setLineWidth(4)
        this.ctx.setLineCap('round')
        this.ctx.setLineJoin('round')
    },
    methods: {
        touchStart(e) {
            this.isDrawing = true
            this.lastX = e.touches[0].x
            this.lastY = e.touches[0].y
            this.hasSignature = true
        },
        touchMove(e) {
            if (!this.isDrawing) return
            
            const currentX = e.touches[0].x
            const currentY = e.touches[0].y
            
            this.ctx.beginPath()
            this.ctx.moveTo(this.lastX, this.lastY)
            this.ctx.lineTo(currentX, currentY)
            this.ctx.stroke()
            this.ctx.draw(true)
            
            this.lastX = currentX
            this.lastY = currentY
        },
        touchEnd() {
            this.isDrawing = false
        },
        clearCanvas() {
            this.ctx.clearRect(0, 0, 700, 400)
            this.ctx.draw()
            this.hasSignature = false
        },
        saveSignature() {
            if (!this.hasSignature) {
                uni.showToast({
                    title: '请先签名',
                    icon: 'none'
                })
                return
            }
            
            uni.canvasToTempFilePath({
                canvasId: 'signatureCanvas',
                success: (res) => {
                    uni.setStorageSync('signatureData', res.tempFilePath)
                    uni.showToast({
                        title: '签名成功',
                        icon: 'success'
                    })
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1000)
                },
                fail: () => {
                    uni.showToast({
                        title: '签名保存失败',
                        icon: 'none'
                    })
                }
            }, this)
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.signature-header {
    text-align: center;
    margin-bottom: 30rpx;
}

.signature-title {
    font-size: 36rpx;
    font-weight: bold;
    color: var(--text-color);
    display: block;
    margin-bottom: 12rpx;
}

.signature-subtitle {
    font-size: 26rpx;
    color: var(--text-secondary);
}

.signature-canvas-wrapper {
    flex: 1;
    background: white;
    border: 2rpx solid var(--border-color);
    border-radius: 16rpx;
    overflow: hidden;
    margin-bottom: 30rpx;
}

.signature-canvas {
    width: 100%;
    height: 500rpx;
}

.signature-actions {
    display: flex;
    gap: 20rpx;
    margin-bottom: 40rpx;
}

.action-btn {
    flex: 1;
    height: 88rpx;
    line-height: 88rpx;
    border-radius: 50rpx;
    font-size: 32rpx;
    border: none;
}

.action-btn.clear {
    background: var(--card-bg);
    color: var(--text-color);
    border: 2rpx solid var(--border-color);
}

.action-btn.save {
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
    color: white;
}
</style>
