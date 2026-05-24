<template>
  <view class="scan-container">
    <view class="scan-area">
      <view class="scan-frame">
        <view class="corner corner-tl"></view>
        <view class="corner corner-tr"></view>
        <view class="corner corner-bl"></view>
        <view class="corner corner-br"></view>
        <view class="scan-line" :class="{ scanning: isScanning }"></view>
      </view>
      <text class="scan-tip">将二维码放入框内，自动扫描</text>
    </view>

    <view class="bottom-actions">
      <view class="action-item" @click="inputBikeNo">
        <text class="action-icon">⌨️</text>
        <text class="action-text">输入编号</text>
      </view>
      <view class="action-item" @click="bluetoothUnlock">
        <text class="action-icon">📶</text>
        <text class="action-text">蓝牙开锁</text>
      </view>
      <view class="action-item" @click="openLight">
        <text class="action-icon">💡</text>
        <text class="action-text">开灯</text>
      </view>
    </view>

    <view class="input-modal" v-if="showInputModal" @click="showInputModal = false">
      <view class="input-content" @click.stop>
        <text class="modal-title">输入车辆编号</text>
        <input 
          class="bike-input" 
          v-model="bikeNoInput" 
          placeholder="请输入车辆编号"
          maxlength="10"
        />
        <view class="modal-actions">
          <button class="cancel-btn" @click="showInputModal = false">取消</button>
          <button class="confirm-btn" @click="confirmUnlock">确定开锁</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const isScanning = ref(true)
const showInputModal = ref(false)
const bikeNoInput = ref('')

let scanTimer = null

const startScanning = () => {
  isScanning.value = true
  scanTimer = setTimeout(() => {
    uni.showLoading({ title: '开锁中...' })
    setTimeout(() => {
      uni.hideLoading()
      uni.showToast({ title: '开锁成功', icon: 'success' })
      setTimeout(() => {
        uni.redirectTo({ url: '/pages/ride/ride' })
      }, 1000)
    }, 2000)
  }, 3000)
}

const inputBikeNo = () => {
  showInputModal.value = true
}

const bluetoothUnlock = () => {
  uni.showLoading({ title: '蓝牙连接中...' })
  setTimeout(() => {
    uni.hideLoading()
    uni.showToast({ title: '蓝牙开锁成功', icon: 'success' })
    setTimeout(() => {
      uni.redirectTo({ url: '/pages/ride/ride' })
    }, 1000)
  }, 2000)
}

const openLight = () => {
  uni.showToast({ title: '手电筒已开启', icon: 'none' })
}

const confirmUnlock = () => {
  if (!bikeNoInput.value) {
    uni.showToast({ title: '请输入车辆编号', icon: 'none' })
    return
  }
  
  showInputModal.value = false
  uni.showLoading({ title: '开锁中...' })
  setTimeout(() => {
    uni.hideLoading()
    uni.showToast({ title: '开锁成功', icon: 'success' })
    setTimeout(() => {
      uni.redirectTo({ url: '/pages/ride/ride' })
    }, 1000)
  }, 2000)
}

onMounted(() => {
  startScanning()
})

onUnmounted(() => {
  if (scanTimer) {
    clearTimeout(scanTimer)
  }
})
</script>

<style lang="scss" scoped>
.scan-container {
  height: 100vh;
  background: #000;
  display: flex;
  flex-direction: column;
}

.scan-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.scan-frame {
  width: 260px;
  height: 260px;
  position: relative;
  margin-bottom: 32px;
}

.corner {
  position: absolute;
  width: 24px;
  height: 24px;
  border: 3px solid #00A862;
}

.corner-tl {
  top: 0;
  left: 0;
  border-right: none;
  border-bottom: none;
}

.corner-tr {
  top: 0;
  right: 0;
  border-left: none;
  border-bottom: none;
}

.corner-bl {
  bottom: 0;
  left: 0;
  border-right: none;
  border-top: none;
}

.corner-br {
  bottom: 0;
  right: 0;
  border-left: none;
  border-top: none;
}

.scan-line {
  position: absolute;
  left: 10%;
  width: 80%;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00A862, transparent);
  top: 0;
}

.scan-line.scanning {
  animation: scan 2s linear infinite;
}

@keyframes scan {
  0% { top: 0; opacity: 1; }
  50% { opacity: 1; }
  100% { top: 100%; opacity: 0; }
}

.scan-tip {
  font-size: 14px;
  color: rgba(255,255,255,0.7);
}

.bottom-actions {
  display: flex;
  justify-content: space-around;
  padding: 40px 20px 60px;
  background: rgba(255,255,255,0.1);
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.action-icon {
  width: 56px;
  height: 56px;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.action-text {
  font-size: 12px;
  color: white;
}

.input-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.input-content {
  width: 80%;
  background: white;
  border-radius: 16px;
  padding: 24px;
}

.modal-title {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.bike-input {
  width: 100%;
  height: 48px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 0 16px;
  font-size: 16px;
  box-sizing: border-box;
  margin-bottom: 20px;
}

.modal-actions {
  display: flex;
  gap: 12px;
}

.cancel-btn {
  flex: 1;
  height: 44px;
  background: #f5f5f5;
  color: #666;
  border: none;
  border-radius: 10px;
  font-size: 15px;
}

.confirm-btn {
  flex: 1;
  height: 44px;
  background: #00A862;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 15px;
}
</style>
