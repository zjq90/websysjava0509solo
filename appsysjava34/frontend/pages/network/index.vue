<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="wifi-card card">
      <view class="card-title flex-between">
        <text>WiFi设置</text>
        <switch :checked="wifiSetting.isHidden" @change="toggleWiFiVisibility" color="#667eea" />
      </view>
      <view class="wifi-info">
        <view class="wifi-name">
          <text class="wifi-icon">📶</text>
          <text class="ssid">{{ wifiSetting.ssid || 'HomeWiFi' }}</text>
        </view>
        <view class="wifi-detail">
          <text>频段：{{ wifiSetting.band || '2.4G' }}</text>
          <text>隐藏：{{ wifiSetting.isHidden ? '是' : '否' }}</text>
        </view>
      </view>
      <view class="wifi-actions">
        <button class="action-btn" @click="showPasswordModal = true">
          <text>修改密码</text>
        </button>
        <button class="action-btn" @click="restartRouter">
          <text>重启路由</text>
        </button>
      </view>
    </view>

    <view class="guest-wifi-card card" v-if="wifiSetting.guestEnabled">
      <view class="card-title flex-between">
        <text>访客网络</text>
        <switch :checked="wifiSetting.guestEnabled" color="#667eea" />
      </view>
      <view class="guest-info">
        <text class="guest-ssid">SSID：{{ wifiSetting.guestSsid || 'GuestWiFi' }}</text>
        <text class="guest-password">密码：{{ wifiSetting.guestPassword || 'guest123' }}</text>
      </view>
    </view>

    <view class="devices-card card">
      <view class="card-title">
        <text>连接设备 ({{ devices.length }})</text>
      </view>
      <view v-for="device in devices" :key="device.id" class="device-item">
        <view class="device-main">
          <view class="device-avatar">
            <text>{{ getDeviceIcon(device.deviceType) }}</text>
          </view>
          <view class="device-info">
            <view class="device-name">{{ device.deviceName }}</view>
            <view class="device-detail">
              <text>{{ device.ipAddress }}</text>
              <text>·</text>
              <text>速度: {{ device.downloadSpeed }} Mbps</text>
            </view>
          </view>
        </view>
        <view class="device-actions">
          <button class="block-btn" :class="{ blocked: device.isBlocked }" @click="toggleDeviceBlock(device)">
            {{ device.isBlocked ? '已拉黑' : '拉黑' }}
          </button>
        </view>
      </view>
    </view>

    <view class="child-guard-card card">
      <view class="card-title">儿童守护</view>
      <view v-for="guard in childGuards" :key="guard.id" class="guard-item">
        <view class="guard-header flex-between">
          <view class="guard-info">
            <text class="guard-name">{{ guard.childName }}</text>
            <text class="guard-time">{{ guard.startTime }} - {{ guard.endTime }}</text>
          </view>
          <switch :checked="guard.enabled" color="#667eea" @change="toggleGuard(guard)" />
        </view>
        <view class="guard-days">
          <text>上网时间：周{{ guard.weekDays }}</text>
        </view>
        <view class="guard-apps" v-if="guard.appWhitelist">
          <text>应用白名单：{{ guard.appWhitelist }}</text>
        </view>
      </view>
    </view>

    <uni-popup ref="passwordPopup" type="dialog">
      <uni-popup-dialog type="input" title="修改WiFi密码" placeholder="请输入新密码" :duration="2000" @confirm="changeWiFiPassword">
      </uni-popup-dialog>
    </uni-popup>
  </view>
</template>

<script>
import { networkApi } from '@/api/index.js'

export default {
  data() {
    return {
      wifiSetting: {},
      devices: [],
      childGuards: [],
      elderMode: false,
      showPasswordModal: false,
      newPassword: ''
    }
  },
  onLoad() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        this.wifiSetting = (await networkApi.getWiFiSetting(1)) || {}
        this.devices = await networkApi.getDevices(1)
        this.childGuards = await networkApi.getChildGuards(1)
      } catch (e) {
        console.error(e)
      }
    },
    async toggleWiFiVisibility(e) {
      this.wifiSetting.isHidden = e.detail.value
      try {
        await networkApi.toggleWiFiVisibility(1)
      } catch (e) {
        console.error(e)
      }
    },
    async changeWiFiPassword(value) {
      if (!value || value.length < 8) {
        uni.showToast({ title: '密码至少8位', icon: 'none' })
        return
      }
      try {
        await networkApi.updateWiFiPassword(1, value)
        uni.showToast({ title: '密码修改成功', icon: 'success' })
      } catch (e) {
        console.error(e)
      }
    },
    async toggleDeviceBlock(device) {
      try {
        await networkApi.toggleDeviceBlock(device.id)
        device.isBlocked = !device.isBlocked
        uni.showToast({ title: device.isBlocked ? '已拉黑' : '已取消拉黑', icon: 'success' })
      } catch (e) {
        console.error(e)
      }
    },
    async toggleGuard(guard) {
      guard.enabled = !guard.enabled
      try {
        await networkApi.toggleChildGuard(guard.id)
      } catch (e) {
        console.error(e)
      }
    },
    restartRouter() {
      uni.showModal({
        title: '提示',
        content: '确定要重启路由器吗？重启过程中网络会短暂断开',
        success: async (res) => {
          if (res.confirm) {
            try {
              await networkApi.restartRouter(1)
              uni.showToast({ title: '重启指令已发送', icon: 'success' })
            } catch (e) {
              console.error(e)
            }
          }
        }
      })
    },
    getDeviceIcon(type) {
      const icons = {
        '手机': '📱',
        '电脑': '💻',
        '电视': '📺',
        '平板': '📱'
      }
      return icons[type] || '📱'
    }
  }
}
</script>

<style scoped>
.wifi-info {
  padding: 20rpx 0;
}

.wifi-name {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 16rpx;
}

.wifi-icon {
  font-size: 36rpx;
}

.ssid {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.wifi-detail {
  display: flex;
  gap: 20rpx;
  font-size: 24rpx;
  color: #666;
}

.wifi-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 20rpx;
}

.action-btn {
  flex: 1;
  height: 72rpx;
  background: #f0f0f0;
  border: none;
  border-radius: 36rpx;
  font-size: 26rpx;
  color: #333;
}

.guest-info {
  padding: 20rpx 0;
}

.guest-ssid, .guest-password {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}

.device-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.device-item:last-child {
  border-bottom: none;
}

.device-main {
  display: flex;
  align-items: center;
  gap: 16rpx;
  flex: 1;
}

.device-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
}

.device-info {
  flex: 1;
}

.device-name {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 6rpx;
}

.device-detail {
  display: flex;
  gap: 8rpx;
  font-size: 22rpx;
  color: #999;
}

.block-btn {
  padding: 8rpx 20rpx;
  background: #fff3e0;
  color: #ff9800;
  border: none;
  border-radius: 20rpx;
  font-size: 22rpx;
}

.block-btn.blocked {
  background: #ffebee;
  color: #f44336;
}

.guard-item {
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.guard-item:last-child {
  border-bottom: none;
}

.guard-info {
  flex: 1;
}

.guard-name {
  display: block;
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 6rpx;
}

.guard-time {
  font-size: 22rpx;
  color: #666;
}

.guard-days, .guard-apps {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
}
</style>
