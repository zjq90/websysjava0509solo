<template>
  <view class="container">
    <view class="contract-header">
      <view class="contract-icon">📄</view>
      <text class="contract-title">电子销售合同</text>
    </view>

    <view class="contract-content" v-if="order">
      <view class="contract-section">
        <text class="section-title">一、合同双方</text>
        <view class="section-content">
          <view class="info-row">
            <text class="label">甲方(卖方)</text>
            <text class="value">种子销售有限公司</text>
          </view>
          <view class="info-row">
            <text class="label">乙方(买方)</text>
            <text class="value">{{ order.customerName }}</text>
          </view>
        </view>
      </view>

      <view class="contract-section">
        <text class="section-title">二、订单信息</text>
        <view class="section-content">
          <view class="info-row">
            <text class="label">订单号</text>
            <text class="value">{{ order.orderNo }}</text>
          </view>
          <view class="info-row">
            <text class="label">签订日期</text>
            <text class="value">{{ currentDate }}</text>
          </view>
        </view>
      </view>

      <view class="contract-section">
        <text class="section-title">三、商品明细</text>
        <view class="section-content">
          <view class="table-header">
            <text class="col col-name">商品名称</text>
            <text class="col col-qty">数量</text>
            <text class="col col-price">单价</text>
            <text class="col col-total">小计</text>
          </view>
          <view class="table-row" v-for="item in order.orderItems" :key="item.id">
            <text class="col col-name">{{ item.productName }}</text>
            <text class="col col-qty">{{ item.quantity }}</text>
            <text class="col col-price">¥{{ item.unitPrice }}</text>
            <text class="col col-total">¥{{ (item.unitPrice * item.quantity).toFixed(2) }}</text>
          </view>
          <view class="table-total">
            <text class="col col-name">合计</text>
            <text class="col col-qty">{{ totalItems }}</text>
            <text class="col col-price">-</text>
            <text class="col col-total">¥{{ order.totalAmount }}</text>
          </view>
        </view>
      </view>

      <view class="contract-section">
        <text class="section-title">四、付款方式</text>
        <view class="section-content">
          <text class="paragraph">1. 本合同总金额为人民币：¥{{ order.totalAmount }}</text>
          <text class="paragraph">2. 付款方式：签收后30天内结清全部货款</text>
        </view>
      </view>

      <view class="contract-section">
        <text class="section-title">五、交货方式</text>
        <view class="section-content">
          <text class="paragraph">1. 交货地点：由乙方指定</text>
          <text class="paragraph">2. 运输方式：物流配送</text>
          <text class="paragraph">3. 运费承担：甲方承担</text>
        </view>
      </view>

      <view class="contract-section">
        <text class="section-title">六、双方权利义务</text>
        <view class="section-content">
          <text class="paragraph">1. 甲方保证所供商品质量符合国家标准</text>
          <text class="paragraph">2. 乙方应按合同约定及时支付货款</text>
          <text class="paragraph">3. 如遇争议，双方协商解决；协商不成，可向甲方所在地法院起诉</text>
        </view>
      </view>

      <view class="signature-section">
        <view class="signature-box">
          <text class="signature-label">甲方签章</text>
          <text class="signature-stamp">种子销售有限公司</text>
        </view>
        <view class="signature-box" v-if="!signed">
          <text class="signature-label">乙方签章</text>
          <view class="sign-area" @click="showSignPad = true">
            <text class="sign-hint">点击签署</text>
          </view>
        </view>
        <view class="signature-box" v-else>
          <text class="signature-label">乙方签章</text>
          <text class="signature-name">{{ order.customerName }}</text>
          <text class="signature-time">已签署 {{ signedTime }}</text>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <button 
        class="sign-btn" 
        :disabled="signed"
        @click="signContract"
      >
        {{ signed ? '已签署' : '立即签署' }}
      </button>
    </view>

    <view v-if="showSignPad" class="sign-pad-mask" @click="showSignPad = false">
      <view class="sign-pad-content" @click.stop>
        <view class="sign-pad-header">
          <text class="sign-pad-title">手写签名</text>
          <text class="sign-pad-clear" @click="clearCanvas">清除</text>
        </view>
        <canvas 
          class="sign-canvas" 
          canvas-id="signCanvas"
          @touchstart="startDraw"
          @touchmove="moveDraw"
          @touchend="endDraw"
        ></canvas>
        <view class="sign-pad-actions">
          <button class="sign-pad-btn cancel" @click="showSignPad = false">取消</button>
          <button class="sign-pad-btn confirm" @click="confirmSign">确认</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import orderApi from '@/api/order.js'

export default {
  data() {
    return {
      orderId: '',
      order: null,
      signed: false,
      signedTime: '',
      showSignPad: false,
      ctx: null,
      isDrawing: false,
      lastX: 0,
      lastY: 0
    }
  },
  computed: {
    currentDate() {
      const now = new Date()
      return `${now.getFullYear()}年${now.getMonth() + 1}月${now.getDate()}日`
    },
    totalItems() {
      if (!this.order) return 0
      return this.order.orderItems.reduce((sum, item) => sum + item.quantity, 0)
    }
  },
  onLoad(options) {
    this.orderId = options.id
    this.loadOrder()
  },
  onReady() {
    this.ctx = uni.createCanvasContext('signCanvas', this)
    this.ctx.setStrokeStyle('#333')
    this.ctx.setLineWidth(3)
    this.ctx.setLineCap('round')
  },
  methods: {
    async loadOrder() {
      uni.showLoading({ title: '加载中...' })
      try {
        this.order = await orderApi.getOrderById(this.orderId)
        if (this.order.contractSignedTime) {
          this.signed = true
          this.signedTime = this.formatDate(this.order.contractSignedTime)
        }
      } catch (e) {
        console.error('加载订单失败', e)
      } finally {
        uni.hideLoading()
      }
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
    },
    startDraw(e) {
      const touch = e.touches[0]
      this.isDrawing = true
      this.lastX = touch.x
      this.lastY = touch.y
    },
    moveDraw(e) {
      if (!this.isDrawing) return
      const touch = e.touches[0]
      this.ctx.moveTo(this.lastX, this.lastY)
      this.ctx.lineTo(touch.x, touch.y)
      this.ctx.stroke()
      this.ctx.draw(true)
      this.lastX = touch.x
      this.lastY = touch.y
    },
    endDraw() {
      this.isDrawing = false
    },
    clearCanvas() {
      this.ctx.draw()
    },
    confirmSign() {
      this.showSignPad = false
    },
    async signContract() {
      uni.showModal({
        title: '确认签署',
        content: '请确认合同内容无误后签署',
        success: async (res) => {
          if (res.confirm) {
            uni.showLoading({ title: '签署中...' })
            try {
              await orderApi.signContract(this.orderId)
              this.signed = true
              const now = new Date()
              this.signedTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
              uni.showToast({ title: '签署成功', icon: 'success' })
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            } catch (e) {
              console.error('签署失败', e)
            } finally {
              uni.hideLoading()
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.contract-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  text-align: center;
}

.contract-icon {
  font-size: 64rpx;
  margin-bottom: 12rpx;
}

.contract-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.contract-content {
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx;
  margin-bottom: 120rpx;
}

.contract-section {
  margin-bottom: 32rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.section-content {
  background: #fafafa;
  border-radius: 12rpx;
  padding: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
}

.info-row .label {
  font-size: 28rpx;
  color: #666;
}

.info-row .value {
  font-size: 28rpx;
  color: #333;
}

.table-header,
.table-row,
.table-total {
  display: flex;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #eee;
}

.table-total {
  border-bottom: none;
  font-weight: bold;
}

.col {
  font-size: 26rpx;
  text-align: center;
}

.col-name {
  flex: 3;
  text-align: left;
}

.col-qty {
  flex: 1;
}

.col-price {
  flex: 2;
}

.col-total {
  flex: 2;
  text-align: right;
}

.table-header .col {
  color: #999;
}

.paragraph {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
  display: block;
  margin-bottom: 8rpx;
}

.signature-section {
  display: flex;
  justify-content: space-between;
  margin-top: 40rpx;
}

.signature-box {
  flex: 1;
  text-align: center;
  padding: 20rpx;
}

.signature-label {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 12rpx;
}

.signature-stamp {
  font-size: 28rpx;
  color: #667eea;
  border: 2rpx solid #667eea;
  padding: 24rpx;
  border-radius: 8rpx;
}

.sign-area {
  height: 120rpx;
  border: 2rpx dashed #ccc;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sign-hint {
  font-size: 28rpx;
  color: #999;
}

.signature-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  display: block;
}

.signature-time {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

.bottom-bar {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background: #fff;
  padding: 24rpx 32rpx;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.sign-btn {
  width: 100%;
  height: 96rpx;
  line-height: 96rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 48rpx;
  font-size: 32rpx;
  border: none;
}

.sign-btn[disabled] {
  background: #ccc;
}

.sign-pad-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  align-items: flex-end;
}

.sign-pad-content {
  width: 100%;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  padding-bottom: 40rpx;
}

.sign-pad-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  border-bottom: 2rpx solid #f5f5f5;
}

.sign-pad-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
}

.sign-pad-clear {
  font-size: 28rpx;
  color: #667eea;
}

.sign-canvas {
  width: 100%;
  height: 500rpx;
  background: #fafafa;
}

.sign-pad-actions {
  display: flex;
  gap: 24rpx;
  padding: 24rpx 32rpx 0;
}

.sign-pad-btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  border: none;
}

.sign-pad-btn.cancel {
  background: #f5f5f5;
  color: #666;
}

.sign-pad-btn.confirm {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
</style>
