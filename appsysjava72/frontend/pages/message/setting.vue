<template>
  <view class="setting-container">
    <view class="setting-section">
      <view class="section-title">消息通知</view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">🔔</text>
          <text class="item-label">活动提醒</text>
        </view>
        <switch 
          :checked="setting?.activityReminder ?? true" 
          color="#5677fc"
          @change="updateSetting('activityReminder', $event.detail.value)"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">🏫</text>
          <text class="item-label">社团公告</text>
        </view>
        <switch 
          :checked="setting?.clubAnnouncement ?? true" 
          color="#5677fc"
          @change="updateSetting('clubAnnouncement', $event.detail.value)"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">💬</text>
          <text class="item-label">聊天消息</text>
        </view>
        <switch 
          :checked="setting?.chatMessage ?? true" 
          color="#5677fc"
          @change="updateSetting('chatMessage', $event.detail.value)"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">❤️</text>
          <text class="item-label">点赞提醒</text>
        </view>
        <switch 
          :checked="setting?.likeReminder ?? true" 
          color="#5677fc"
          @change="updateSetting('likeReminder', $event.detail.value)"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">💭</text>
          <text class="item-label">评论提醒</text>
        </view>
        <switch 
          :checked="setting?.commentReminder ?? true" 
          color="#5677fc"
          @change="updateSetting('commentReminder', $event.detail.value)"
        />
      </view>
    </view>
    
    <view class="setting-section">
      <view class="section-title">提醒方式</view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">🔊</text>
          <text class="item-label">声音提醒</text>
        </view>
        <switch 
          :checked="setting?.soundEnabled ?? true" 
          color="#5677fc"
          @change="updateSetting('soundEnabled', $event.detail.value)"
        />
      </view>
      
      <view class="setting-item">
        <view class="item-info">
          <text class="item-icon">📳</text>
          <text class="item-label">震动提醒</text>
        </view>
        <switch 
          :checked="setting?.vibrationEnabled ?? true" 
          color="#5677fc"
          @change="updateSetting('vibrationEnabled', $event.detail.value)"
        />
      </view>
    </view>
    
    <view class="setting-section">
      <view class="section-title">免打扰设置</view>
      
      <view class="setting-item" @click="toggleDoNotDisturb">
        <view class="item-info">
          <text class="item-icon">🌙</text>
          <text class="item-label">免打扰模式</text>
        </view>
        <view class="item-value">
          <text class="value-text">{{ setting?.doNotDisturb ? '已开启' : '已关闭' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
      
      <view class="setting-item" v-if="setting?.doNotDisturb">
        <view class="item-info">
          <text class="item-icon">⏰</text>
          <text class="item-label">免打扰时段</text>
        </view>
        <view class="item-value">
          <text class="value-text">{{ setting?.quietStart || '22:00' }} - {{ setting?.quietEnd || '07:00' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="setting-section">
      <view class="section-title">其他设置</view>
      
      <view class="setting-item" @click="goToSystemSettings">
        <view class="item-info">
          <text class="item-icon">⚙️</text>
          <text class="item-label">系统通知设置</text>
        </view>
        <text class="arrow">›</text>
      </view>
      
      <view class="setting-item" @click="clearMessageCache">
        <view class="item-info">
          <text class="item-icon">🗑️</text>
          <text class="item-label">清除消息缓存</text>
        </view>
        <view class="item-value">
          <text class="value-text">{{ cacheSize }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>
    
    <view class="tip-section">
      <text class="tip-text">
        💡 关闭通知后，您将不会收到相应的消息推送，但消息仍会保存在消息列表中。
      </text>
    </view>
    
    <view class="loading-state" v-if="loading">加载中...</view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'

export default {
  data() {
    return {
      setting: null,
      loading: false,
      cacheSize: '0KB',
      updating: false
    }
  },
  onLoad() {
    this.loadSetting()
    this.calculateCacheSize()
  },
  methods: {
    util,
    
    async loadSetting() {
      this.loading = true
      try {
        const res = await api.getNotificationSetting()
        this.setting = res.data || {}
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    
    async updateSetting(key, value) {
      if (this.updating || !this.setting) return
      
      this.updating = true
      try {
        const newSetting = {
          ...this.setting,
          [key]: value
        }
        const res = await api.updateNotificationSetting(newSetting)
        if (res.code === 200) {
          this.setting[key] = value
          util.toast('设置已更新')
        } else {
          util.toast(res.message || '设置失败')
        }
      } catch (e) {
        util.toast('设置失败，请重试')
        console.error(e)
      } finally {
        this.updating = false
      }
    },
    
    toggleDoNotDisturb() {
      uni.showActionSheet({
        itemList: ['开启免打扰', '关闭免打扰', '设置时段'],
        success: (res) => {
          if (res.tapIndex === 0) {
            this.updateSetting('doNotDisturb', true)
          } else if (res.tapIndex === 1) {
            this.updateSetting('doNotDisturb', false)
          } else if (res.tapIndex === 2) {
            this.setQuietHours()
          }
        }
      })
    },
    
    setQuietHours() {
      util.toast('时段设置开发中')
    },
    
    goToSystemSettings() {
      if (uni.openSystemSetting) {
        uni.openSystemSetting()
      } else {
        util.toast('请在系统设置中开启通知权限')
      }
    },
    
    calculateCacheSize() {
      try {
        const info = uni.getStorageInfoSync()
        let size = info.currentSize || 0
        if (size < 1024) {
          this.cacheSize = size + 'B'
        } else if (size < 1024 * 1024) {
          this.cacheSize = (size / 1024).toFixed(2) + 'KB'
        } else {
          this.cacheSize = (size / (1024 * 1024)).toFixed(2) + 'MB'
        }
      } catch (e) {
        this.cacheSize = '0KB'
      }
    },
    
    async clearMessageCache() {
      const confirm = await util.showModal(
        '清除缓存',
        '确定要清除本地消息缓存吗？清除后本地消息记录将被删除，但服务器上的消息不会受影响。'
      )
      
      if (!confirm) return
      
      try {
        const keys = uni.getStorageInfoSync().keys
        const messageKeys = keys.filter(k => 
          k.startsWith('offline_signin') || 
          k.startsWith('message_') || 
          k.startsWith('chat_')
        )
        
        messageKeys.forEach(k => {
          uni.removeStorageSync(k)
        })
        
        this.calculateCacheSize()
        util.toast('缓存已清除', 'success')
      } catch (e) {
        util.toast('清除失败')
        console.error(e)
      }
    }
  }
}
</script>

<style scoped>
.setting-container {
  min-height: 100vh;
  background: #f5f6f8;
  padding-bottom: 40rpx;
}

.setting-section {
  background: #fff;
  margin-bottom: 20rpx;
}

.section-title {
  padding: 24rpx 30rpx 12rpx;
  font-size: 26rpx;
  color: #999;
  font-weight: 600;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f8f8f8;
}

.setting-item:last-child {
  border-bottom: none;
}

.item-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.item-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.item-label {
  font-size: 30rpx;
  color: #333;
}

.item-value {
  display: flex;
  align-items: center;
}

.value-text {
  font-size: 26rpx;
  color: #999;
  margin-right: 10rpx;
}

.arrow {
  font-size: 32rpx;
  color: #ccc;
}

.tip-section {
  padding: 30rpx;
}

.tip-text {
  font-size: 24rpx;
  color: #999;
  line-height: 1.6;
}

.loading-state {
  text-align: center;
  padding: 80rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
