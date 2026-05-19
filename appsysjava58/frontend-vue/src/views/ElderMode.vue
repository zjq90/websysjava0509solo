<template>
  <div class="elder-mode-page" :class="{ 'elder-active': elderMode }">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>👓 长辈模式</h2>
    </div>

    <div class="toggle-section">
      <div class="toggle-card">
        <div class="toggle-info">
          <div class="toggle-title">长辈模式开关</div>
          <div class="toggle-desc">开启后将放大字体，简化界面</div>
        </div>
        <el-switch v-model="elderMode" active-color="#667eea" size="large" />
      </div>
    </div>

    <div class="features-section">
      <h3 class="section-title">功能特性</h3>
      <div class="feature-list">
        <div class="feature-item">
          <span class="feature-icon">🔤</span>
          <div class="feature-text">
            <div class="feature-title">字体放大</div>
            <div class="feature-desc">所有文字放大30%，看得更清楚</div>
          </div>
        </div>
        <div class="feature-item">
          <span class="feature-icon">🎯</span>
          <div class="feature-text">
            <div class="feature-title">界面简化</div>
            <div class="feature-desc">只显示核心功能，界面更清爽</div>
          </div>
        </div>
        <div class="feature-item">
          <span class="feature-icon">🎤</span>
          <div class="feature-text">
            <div class="feature-title">语音输入</div>
            <div class="feature-desc">支持语音输入，打字更轻松</div>
          </div>
        </div>
        <div class="feature-item">
          <span class="feature-icon">📞</span>
          <div class="feature-text">
            <div class="feature-title">一键呼叫</div>
            <div class="feature-desc">快速联系宠物医院和医生</div>
          </div>
        </div>
      </div>
    </div>

    <div class="quick-actions">
      <h3 class="section-title">快捷功能</h3>
      <div class="action-grid">
        <div class="action-item" @click="quickCall">
          <span class="action-icon">📞</span>
          <span class="action-text">一键呼叫</span>
        </div>
        <div class="action-item" @click="$router.push('/health')">
          <span class="action-icon">💊</span>
          <span class="action-text">健康提醒</span>
        </div>
        <div class="action-item" @click="$router.push('/hospital')">
          <span class="action-icon">🏥</span>
          <span class="action-text">附近医院</span>
        </div>
        <div class="action-item" @click="$router.push('/diet')">
          <span class="action-icon">🍽️</span>
          <span class="action-text">饮食建议</span>
        </div>
      </div>
    </div>

    <div class="voice-demo">
      <h3 class="section-title">语音输入演示</h3>
      <div class="voice-card" @click="startVoice">
        <span class="voice-icon">🎤</span>
        <span class="voice-text">{{ voiceText || '点击开始语音输入' }}</span>
      </div>
    </div>

    <div class="preview-section">
      <h3 class="section-title">预览效果</h3>
      <div class="preview-card">
        <span class="preview-normal">这是正常字体大小</span>
        <span class="preview-elder">这是长辈模式字体大小</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()
const elderMode = ref(appStore.elderMode)

const voiceText = ref('')

watch(elderMode, (newVal) => {
  appStore.elderMode = newVal
  ElMessage.success(newVal ? '长辈模式已开启' : '长辈模式已关闭')
})

const quickCall = () => {
  ElMessage.success('正在呼叫: 010-88888888')
}

const startVoice = () => {
  voiceText.value = '正在聆听...'
  setTimeout(() => {
    voiceText.value = '我家狗狗最近食欲不好怎么办？'
    ElMessage.success('语音识别成功')
  }, 2000)
}
</script>

<style scoped>
.elder-mode-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.elder-active {
  font-size: 18px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 22px;
}

.toggle-section {
  margin-bottom: 25px;
}

.toggle-card {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.toggle-info {
  flex: 1;
}

.toggle-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.toggle-desc {
  font-size: 16px;
  color: #999;
}

.section-title {
  margin: 0 0 20px;
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.features-section {
  background: white;
  border-radius: 16px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 12px;
}

.feature-icon {
  font-size: 40px;
  margin-right: 20px;
}

.feature-text {
  flex: 1;
}

.feature-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.feature-desc {
  font-size: 14px;
  color: #666;
}

.quick-actions {
  background: white;
  border-radius: 16px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 25px;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  border-radius: 12px;
  cursor: pointer;
  transition: transform 0.3s;
}

.action-item:hover {
  transform: translateY(-2px);
}

.action-icon {
  font-size: 40px;
  margin-bottom: 12px;
}

.action-text {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.voice-demo {
  background: white;
  border-radius: 16px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.voice-card {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px;
  background: linear-gradient(135deg, #11998e15 0%, #38ef7d15 100%);
  border-radius: 12px;
  cursor: pointer;
}

.voice-icon {
  font-size: 36px;
  margin-right: 15px;
}

.voice-text {
  font-size: 16px;
  color: #333;
}

.preview-section {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.preview-card {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.preview-normal {
  font-size: 14px;
  color: #666;
  padding: 15px;
  background: #f5f5f5;
  border-radius: 8px;
}

.preview-elder {
  font-size: 18px;
  color: #333;
  font-weight: bold;
  padding: 15px;
  background: linear-gradient(135deg, #667eea15 0%, #764ba215 100%);
  border-radius: 8px;
}
</style>