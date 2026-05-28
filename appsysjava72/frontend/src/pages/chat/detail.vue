<template>
  <view class="chat-detail-container">
    <view class="chat-header">
      <view class="club-info">
        <image 
          v-if="clubLogo" 
          :src="clubLogo" 
          class="club-logo" 
          mode="aspectFill"
        />
        <view v-else class="club-logo-placeholder">
          <text>{{ clubName ? clubName.substring(0, 1) : '社' }}</text>
        </view>
        <view class="club-detail">
          <text class="club-name">{{ clubName || '群聊' }}</text>
          <text class="member-count">{{ memberCount }} 人在线</text>
        </view>
      </view>
    </view>
    
    <scroll-view 
      class="message-list" 
      scroll-y 
      :scroll-into-view="scrollIntoView"
      @scrolltolower="loadMoreMessages"
    >
      <view class="load-more" v-if="loadingMore">
        <text>加载中...</text>
      </view>
      
      <view class="time-divider" v-if="showDateDivider">
        <text class="date-text">{{ getCurrentDateText() }}</text>
      </view>
      
      <view 
        class="message-item" 
        v-for="(msg, index) in messageList" 
        :key="msg.id"
        :id="'msg-' + msg.id"
      >
        <view class="time-divider" v-if="shouldShowTimeDivider(index)">
          <text class="date-text">{{ formatMessageDate(msg.createTime) }}</text>
        </view>
        
        <view class="message-wrapper" :class="{ 'is-mine': msg.isMine }">
          <image 
            v-if="!msg.isMine && msg.userAvatar" 
            :src="msg.userAvatar" 
            class="avatar" 
            mode="aspectFill"
          />
          <view v-if="!msg.isMine && !msg.userAvatar" class="avatar-placeholder">
            <text>{{ msg.userName ? msg.userName.substring(0, 1) : '用' }}</text>
          </view>
          
          <view class="message-content">
            <text class="user-name" v-if="!msg.isMine">{{ msg.userName || '用户' }}</text>
            <view class="bubble" :class="[msg.messageType, { 'is-mine': msg.isMine }]">
              <text v-if="msg.messageType === 'text'" class="bubble-text">{{ msg.content }}</text>
              <image 
                v-else-if="msg.messageType === 'image'" 
                :src="msg.mediaUrl" 
                class="bubble-image" 
                mode="widthFix"
                @click="previewImage(msg.mediaUrl)"
              />
              <view 
                v-else-if="msg.messageType === 'voice'" 
                class="voice-bubble"
                @click="playVoice(msg)"
              >
                <text class="voice-icon">{{ isPlaying && currentVoiceId === msg.id ? '🔊' : '🔈' }}</text>
                <text class="voice-duration">{{ msg.duration }}''</text>
                <view class="voice-wave" v-if="isPlaying && currentVoiceId === msg.id">
                  <view class="wave-bar" v-for="i in 5" :key="i" :style="{ animationDelay: (i * 0.1) + 's' }"></view>
                </view>
              </view>
            </view>
            <text class="message-time">{{ util.formatTime(msg.createTime, 'HH:mm') }}</text>
          </view>
          
          <image 
            v-if="msg.isMine && msg.userAvatar" 
            :src="msg.userAvatar" 
            class="avatar" 
            mode="aspectFill"
          />
          <view v-if="msg.isMine && !msg.userAvatar" class="avatar-placeholder mine">
            <text>{{ msg.userName ? msg.userName.substring(0, 1) : '我' }}</text>
          </view>
        </view>
      </view>
      
      <view class="loading-state" v-if="loading && messageList.length === 0">加载中...</view>
      <view class="no-more" v-if="!hasMore && messageList.length > 0">
        <text>没有更多消息了</text>
      </view>
    </scroll-view>
    
    <view class="input-bar">
      <view class="input-wrapper">
        <text class="input-icon" @click="toggleMoreMenu">➕</text>
        <input 
          v-model="inputText" 
          class="message-input" 
          placeholder="说点什么..."
          confirm-type="send"
          @confirm="sendTextMessage"
          @focus="onInputFocus"
        />
        <text 
          class="input-icon voice-icon" 
          @click="toggleVoiceMode"
        >{{ showVoiceInput ? '⌨️' : '🎤' }}</text>
      </view>
      
      <button 
        class="send-btn" 
        :disabled="!inputText.trim() && !showVoiceInput"
        @click="sendTextMessage"
        v-if="!showVoiceInput"
      >
        发送
      </button>
      
      <view 
        class="voice-btn" 
        v-else
        @touchstart="startRecord"
        @touchend="stopRecord"
        @touchcancel="cancelRecord"
      >
        <text>{{ isRecording ? '松开 结束' : '按住 说话' }}</text>
      </view>
    </view>
    
    <view class="more-menu" v-if="showMoreMenu">
      <view class="menu-mask" @click="showMoreMenu = false"></view>
      <view class="menu-content">
        <view class="menu-item" @click="chooseImage">
          <text class="menu-icon">🖼️</text>
          <text class="menu-text">图片</text>
        </view>
        <view class="menu-item" @click="chooseFile">
          <text class="menu-icon">📁</text>
          <text class="menu-text">文件</text>
        </view>
        <view class="menu-item" @click="showLocation">
          <text class="menu-icon">📍</text>
          <text class="menu-text">位置</text>
        </view>
      </view>
    </view>
    
    <view class="recording-overlay" v-if="isRecording">
      <view class="recording-modal">
        <text class="recording-icon">🎤</text>
        <text class="recording-text">{{ recordDuration }}s</text>
        <text class="recording-hint">上滑取消</text>
        <view class="recording-wave">
          <view class="wave-bar" v-for="i in 10" :key="i" :style="{ animationDelay: (i * 0.08) + 's' }"></view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import api from '../../common/api'
import util from '../../common/util'
import config from '../../common/config'

export default {
  data() {
    return {
      clubId: null,
      clubName: '',
      clubLogo: '',
      memberCount: 0,
      messageList: [],
      inputText: '',
      pageNum: 1,
      pageSize: 20,
      hasMore: true,
      loading: false,
      loadingMore: false,
      scrollIntoView: '',
      showMoreMenu: false,
      showVoiceInput: false,
      isRecording: false,
      recordDuration: 0,
      recordTimer: null,
      recordFilePath: '',
      isPlaying: false,
      currentVoiceId: null,
      audioPlayer: null,
      currentUserId: null,
      lastMessageTime: null
    }
  },
  computed: {
    showDateDivider() {
      return this.messageList.length > 0 && this.shouldShowTimeDivider(0)
    }
  },
  onLoad(options) {
    this.clubId = options.clubId
    this.clubName = decodeURIComponent(options.clubName || '')
    this.currentUserId = util.getStorage(config.STORAGE_KEYS.USER_INFO)?.id
    this.loadMessages()
    this.markAsRead()
  },
  onUnload() {
    this.stopRecordingTimer()
    this.stopVoicePlayback()
  },
  methods: {
    util,
    
    async loadMessages(isLoadMore = false) {
      if (isLoadMore) {
        if (!this.hasMore || this.loadingMore) return
        this.loadingMore = true
      } else {
        if (this.loading) return
        this.loading = true
      }
      
      try {
        const res = await api.getChatMessages({
          clubId: this.clubId,
          pageNum: isLoadMore ? this.pageNum + 1 : 1,
          pageSize: this.pageSize
        })
        
        const messages = (res.data.list || []).map(msg => ({
          ...msg,
          isMine: String(msg.userId) === String(this.currentUserId)
        }))
        
        if (isLoadMore) {
          this.messageList = messages.concat(this.messageList)
          this.pageNum++
        } else {
          this.messageList = messages
          this.pageNum = 1
        }
        
        this.hasMore = this.pageNum < res.data.totalPage
        
        if (!isLoadMore && messages.length > 0) {
          this.$nextTick(() => {
            this.scrollToBottom()
          })
        }
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
        this.loadingMore = false
      }
    },
    
    async loadMoreMessages() {
      await this.loadMessages(true)
    },
    
    async markAsRead() {
      try {
        await api.markChatAsRead(this.clubId)
      } catch (e) {
        console.error(e)
      }
    },
    
    scrollToBottom() {
      if (this.messageList.length > 0) {
        const lastMsg = this.messageList[this.messageList.length - 1]
        this.scrollIntoView = 'msg-' + lastMsg.id
      }
    },
    
    shouldShowTimeDivider(index) {
      if (index === 0) return true
      const currentTime = new Date(this.messageList[index].createTime).getTime()
      const prevTime = new Date(this.messageList[index - 1].createTime).getTime()
      return currentTime - prevTime > 5 * 60 * 1000
    },
    
    formatMessageDate(timestamp) {
      const date = new Date(timestamp)
      const now = new Date()
      const diff = now.getTime() - date.getTime()
      
      if (diff < 24 * 60 * 60 * 1000) {
        if (date.getDate() === now.getDate()) {
          return '今天'
        }
        return '昨天'
      }
      if (diff < 48 * 60 * 60 * 1000) {
        return '昨天'
      }
      return util.formatDate(timestamp, 'MM月DD日 HH:mm')
    },
    
    getCurrentDateText() {
      return '今天'
    },
    
    toggleMoreMenu() {
      this.showMoreMenu = !this.showMoreMenu
    },
    
    toggleVoiceMode() {
      this.showVoiceInput = !this.showVoiceInput
    },
    
    onInputFocus() {
      this.showMoreMenu = false
      this.$nextTick(() => {
        this.scrollToBottom()
      })
    },
    
    async sendTextMessage() {
      if (!this.inputText.trim()) return
      
      const content = this.inputText.trim()
      this.inputText = ''
      
      const tempMsg = this.createTempMessage('text', content)
      this.messageList.push(tempMsg)
      this.$nextTick(() => this.scrollToBottom())
      
      try {
        const res = await api.sendMessage(this.clubId, 'text', content, '', 0)
        if (res.code === 200) {
          this.updateTempMessage(tempMsg, res.data)
        } else {
          this.markMessageFailed(tempMsg)
        }
      } catch (e) {
        this.markMessageFailed(tempMsg)
        console.error(e)
      }
    },
    
    createTempMessage(type, content, mediaUrl = '', duration = 0) {
      return {
        id: 'temp-' + Date.now(),
        clubId: this.clubId,
        userId: this.currentUserId,
        userName: util.getStorage(config.STORAGE_KEYS.USER_INFO)?.nickname || '我',
        userAvatar: util.getStorage(config.STORAGE_KEYS.USER_INFO)?.avatar || '',
        messageType: type,
        content: content,
        mediaUrl: mediaUrl,
        duration: duration,
        createTime: new Date().toISOString(),
        isMine: true,
        isTemp: true,
        sendStatus: 'sending'
      }
    },
    
    updateTempMessage(tempMsg, realMsg) {
      const index = this.messageList.findIndex(m => m.id === tempMsg.id)
      if (index !== -1) {
        this.messageList[index] = {
          ...realMsg,
          isMine: true,
          isTemp: false,
          sendStatus: 'success'
        }
      }
    },
    
    markMessageFailed(tempMsg) {
      const index = this.messageList.findIndex(m => m.id === tempMsg.id)
      if (index !== -1) {
        this.messageList[index].sendStatus = 'failed'
      }
    },
    
    chooseImage() {
      this.showMoreMenu = false
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          const tempFilePath = res.tempFilePaths[0]
          
          const tempMsg = this.createTempMessage('image', '', tempFilePath, 0)
          this.messageList.push(tempMsg)
          this.$nextTick(() => this.scrollToBottom())
          
          try {
            const uploadedUrl = await this.uploadImage(tempFilePath)
            const apiRes = await api.sendMessage(this.clubId, 'image', '', uploadedUrl, 0)
            if (apiRes.code === 200) {
              this.updateTempMessage(tempMsg, apiRes.data)
            } else {
              this.markMessageFailed(tempMsg)
            }
          } catch (e) {
            this.markMessageFailed(tempMsg)
            console.error(e)
          }
        }
      })
    },
    
    async uploadImage(filePath) {
      return new Promise((resolve, reject) => {
        uni.uploadFile({
          url: 'http://localhost:8088/api/upload',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': 'Bearer ' + util.getStorage(config.STORAGE_KEYS.TOKEN)
          },
          success: (uploadRes) => {
            const data = JSON.parse(uploadRes.data)
            if (data.code === 200) {
              resolve(data.data)
            } else {
              reject(new Error(data.message))
            }
          },
          fail: reject
        })
      })
    },
    
    chooseFile() {
      util.toast('文件功能开发中')
    },
    
    showLocation() {
      util.toast('位置功能开发中')
    },
    
    startRecord(e) {
      this.isRecording = true
      this.recordDuration = 0
      this.showMoreMenu = false
      
      const recorderManager = uni.getRecorderManager()
      recorderManager.start({
        duration: 60000,
        sampleRate: 44100,
        numberOfChannels: 1,
        encodeBitRate: 192000,
        format: 'mp3'
      })
      
      recorderManager.onStop((res) => {
        this.recordFilePath = res.tempFilePath
        if (this.recordDuration < 1) {
          util.toast('说话时间太短')
          return
        }
        this.sendVoiceMessage()
      })
      
      recorderManager.onError((err) => {
        console.error('Recording error:', err)
        this.isRecording = false
        util.toast('录音失败')
      })
      
      this.recordTimer = setInterval(() => {
        this.recordDuration++
        if (this.recordDuration >= 60) {
          this.stopRecord()
        }
      }, 1000)
    },
    
    stopRecord() {
      this.stopRecordingTimer()
      this.isRecording = false
      
      const recorderManager = uni.getRecorderManager()
      recorderManager.stop()
    },
    
    cancelRecord() {
      this.stopRecordingTimer()
      this.isRecording = false
      this.recordFilePath = ''
      
      const recorderManager = uni.getRecorderManager()
      recorderManager.stop()
    },
    
    stopRecordingTimer() {
      if (this.recordTimer) {
        clearInterval(this.recordTimer)
        this.recordTimer = null
      }
    },
    
    async sendVoiceMessage() {
      if (!this.recordFilePath) return
      
      const tempMsg = this.createTempMessage('voice', '', this.recordFilePath, this.recordDuration)
      this.messageList.push(tempMsg)
      this.$nextTick(() => this.scrollToBottom())
      
      try {
        const uploadedUrl = await this.uploadVoice(this.recordFilePath)
        const res = await api.sendMessage(this.clubId, 'voice', '', uploadedUrl, this.recordDuration)
        if (res.code === 200) {
          this.updateTempMessage(tempMsg, res.data)
        } else {
          this.markMessageFailed(tempMsg)
        }
      } catch (e) {
        this.markMessageFailed(tempMsg)
        console.error(e)
      } finally {
        this.recordFilePath = ''
        this.recordDuration = 0
      }
    },
    
    async uploadVoice(filePath) {
      return new Promise((resolve, reject) => {
        uni.uploadFile({
          url: 'http://localhost:8088/api/upload',
          filePath: filePath,
          name: 'file',
          header: {
            'Authorization': 'Bearer ' + util.getStorage(config.STORAGE_KEYS.TOKEN)
          },
          success: (uploadRes) => {
            const data = JSON.parse(uploadRes.data)
            if (data.code === 200) {
              resolve(data.data)
            } else {
              reject(new Error(data.message))
            }
          },
          fail: reject
        })
      })
    },
    
    playVoice(msg) {
      if (this.isPlaying && this.currentVoiceId === msg.id) {
        this.stopVoicePlayback()
        return
      }
      
      this.stopVoicePlayback()
      this.isPlaying = true
      this.currentVoiceId = msg.id
      
      const innerAudioContext = uni.createInnerAudioContext()
      innerAudioContext.src = msg.mediaUrl
      innerAudioContext.play()
      
      innerAudioContext.onEnded(() => {
        this.stopVoicePlayback()
      })
      
      innerAudioContext.onError(() => {
        this.stopVoicePlayback()
        util.toast('播放失败')
      })
      
      this.audioPlayer = innerAudioContext
    },
    
    stopVoicePlayback() {
      if (this.audioPlayer) {
        this.audioPlayer.stop()
        this.audioPlayer = null
      }
      this.isPlaying = false
      this.currentVoiceId = null
    },
    
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: url
      })
    }
  }
}
</script>

<style scoped>
.chat-detail-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f6f8;
}

.chat-header {
  background: #fff;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.club-info {
  display: flex;
  align-items: center;
}

.club-logo,
.club-logo-placeholder {
  width: 72rpx;
  height: 72rpx;
  border-radius: 14rpx;
  margin-right: 16rpx;
  flex-shrink: 0;
}

.club-logo-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}

.club-detail {
  flex: 1;
}

.club-name {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 4rpx;
}

.member-count {
  font-size: 24rpx;
  color: #999;
}

.message-list {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;
}

.load-more,
.loading-state,
.no-more {
  text-align: center;
  padding: 20rpx;
  font-size: 24rpx;
  color: #999;
}

.time-divider {
  text-align: center;
  margin: 20rpx 0;
}

.date-text {
  padding: 8rpx 20rpx;
  background: rgba(0, 0, 0, 0.1);
  color: #fff;
  border-radius: 20rpx;
  font-size: 22rpx;
}

.message-item {
  margin-bottom: 24rpx;
}

.message-wrapper {
  display: flex;
  align-items: flex-start;
}

.message-wrapper.is-mine {
  flex-direction: row-reverse;
}

.avatar,
.avatar-placeholder {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.avatar-placeholder {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}

.avatar-placeholder.mine {
  background: linear-gradient(135deg, #4caf50 0%, #45a049 100%);
}

.message-content {
  max-width: 70%;
  margin: 0 16rpx;
}

.message-wrapper.is-mine .message-content {
  align-items: flex-end;
}

.user-name {
  display: block;
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
  margin-left: 8rpx;
}

.message-wrapper.is-mine .user-name {
  text-align: right;
  margin-right: 8rpx;
}

.bubble {
  padding: 20rpx 24rpx;
  border-radius: 20rpx;
  background: #fff;
  position: relative;
  word-break: break-all;
}

.bubble.is-mine {
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
}

.bubble-text {
  font-size: 28rpx;
  line-height: 1.5;
}

.bubble-image {
  max-width: 400rpx;
  border-radius: 12rpx;
}

.voice-bubble {
  display: flex;
  align-items: center;
  min-width: 160rpx;
  padding: 20rpx 24rpx;
}

.voice-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
}

.voice-duration {
  font-size: 26rpx;
  margin-right: 16rpx;
}

.voice-wave {
  display: flex;
  align-items: center;
  gap: 4rpx;
}

.wave-bar {
  width: 6rpx;
  height: 24rpx;
  background: currentColor;
  border-radius: 3rpx;
  animation: wave 0.6s ease-in-out infinite;
}

@keyframes wave {
  0%, 100% { height: 8rpx; }
  50% { height: 24rpx; }
}

.message-time {
  display: block;
  font-size: 22rpx;
  color: #ccc;
  margin-top: 8rpx;
  margin-left: 8rpx;
}

.message-wrapper.is-mine .message-time {
  text-align: right;
  margin-right: 8rpx;
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 16rpx 20rpx;
  background: #fff;
  border-top: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: #f5f6f8;
  border-radius: 36rpx;
  padding: 0 16rpx;
  margin-right: 16rpx;
}

.input-icon {
  font-size: 36rpx;
  padding: 12rpx;
}

.voice-icon {
  font-size: 32rpx;
}

.message-input {
  flex: 1;
  height: 72rpx;
  font-size: 28rpx;
}

.send-btn {
  padding: 14rpx 32rpx;
  background: linear-gradient(135deg, #667eea 0%, #5677fc 100%);
  color: #fff;
  border-radius: 36rpx;
  font-size: 28rpx;
  border: none;
  flex-shrink: 0;
}

.send-btn[disabled] {
  background: #ccc;
}

.voice-btn {
  flex: 1;
  height: 72rpx;
  background: #f5f6f8;
  border-radius: 36rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #666;
  margin-right: 16rpx;
}

.more-menu {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.menu-mask {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.menu-content {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 30rpx;
  display: flex;
  justify-content: space-around;
  border-radius: 20rpx 20rpx 0 0;
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
}

.menu-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.menu-text {
  font-size: 24rpx;
  color: #666;
}

.recording-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.recording-modal {
  width: 400rpx;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 20rpx;
  padding: 60rpx 40rpx;
  text-align: center;
  color: #fff;
}

.recording-icon {
  display: block;
  font-size: 80rpx;
  margin-bottom: 20rpx;
}

.recording-text {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  margin-bottom: 16rpx;
}

.recording-hint {
  display: block;
  font-size: 24rpx;
  color: #ccc;
  margin-bottom: 30rpx;
}

.recording-wave {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6rpx;
  height: 40rpx;
}

.recording-wave .wave-bar {
  width: 8rpx;
  background: #fff;
  border-radius: 4rpx;
  animation: recordWave 0.5s ease-in-out infinite;
}

@keyframes recordWave {
  0%, 100% { height: 12rpx; }
  50% { height: 40rpx; }
}
</style>
