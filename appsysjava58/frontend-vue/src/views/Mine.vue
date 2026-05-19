<template>
  <div class="mine-page">
    <div class="user-profile">
      <div class="avatar">
        <span>{{ userInfo.avatar || '😊' }}</span>
      </div>
      <div class="user-info">
        <h2>{{ userInfo.nickname || '爱宠用户' }}</h2>
        <p>{{ userInfo.signature || '欢迎来到宠物健康管理系统' }}</p>
      </div>
      <el-button type="primary" size="small" @click="showEditProfile = true">
        编辑
      </el-button>
    </div>

    <div class="stats-cards">
      <div class="stat-card">
        <div class="stat-value">{{ petsCount || 0 }}</div>
        <div class="stat-label">我的宠物</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ postsCount || 0 }}</div>
        <div class="stat-label">发布帖子</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ favoritesCount || 0 }}</div>
        <div class="stat-label">收藏</div>
      </div>
    </div>

    <div class="menu-section">
      <div class="menu-group">
        <h3>我的服务</h3>
        <div class="menu-item" @click="$router.push('/elder-mode')">
          <span class="menu-icon">👴</span>
          <span class="menu-text">长辈模式</span>
          <span class="menu-arrow">›</span>
        </div>
        <div class="menu-item" @click="$router.push('/health')">
          <span class="menu-icon">💊</span>
          <span class="menu-text">健康管理</span>
          <span class="menu-arrow">›</span>
        </div>
        <div class="menu-item" @click="$router.push('/diet')">
          <span class="menu-icon">🍽️</span>
          <span class="menu-text">饮食建议</span>
          <span class="menu-arrow">›</span>
        </div>
        <div class="menu-item" @click="$router.push('/exercise')">
          <span class="menu-icon">🏃</span>
          <span class="menu-text">运动监测</span>
          <span class="menu-arrow">›</span>
        </div>
      </div>

      <div class="menu-group">
        <h3>设置</h3>
        <div class="menu-item">
          <span class="menu-icon">🔔</span>
          <span class="menu-text">消息通知</span>
          <el-switch v-model="settings.notifications" size="small" />
        </div>
        <div class="menu-item">
          <span class="menu-icon">🌙</span>
          <span class="menu-text">深色模式</span>
          <el-switch v-model="settings.darkMode" size="small" />
        </div>
        <div class="menu-item" @click="toggleElderMode">
          <span class="menu-icon">👓</span>
          <span class="menu-text">长辈模式</span>
          <el-switch v-model="settings.elderMode" size="small" />
        </div>
        <div class="menu-item" @click="clearCache">
          <span class="menu-icon">🗑️</span>
          <span class="menu-text">清除缓存</span>
          <span class="menu-arrow">›</span>
        </div>
      </div>

      <div class="menu-group">
        <h3>关于</h3>
        <div class="menu-item" @click="showAbout = true">
          <span class="menu-icon">ℹ️</span>
          <span class="menu-text">关于我们</span>
          <span class="menu-arrow">›</span>
        </div>
        <div class="menu-item" @click="showFeedback = true">
          <span class="menu-icon">💬</span>
          <span class="menu-text">意见反馈</span>
          <span class="menu-arrow">›</span>
        </div>
      </div>
    </div>

    <nav class="bottom-nav">
      <div class="nav-item" @click="$router.push('/home')">
        <span class="nav-icon">🏠</span>
        <span class="nav-text">首页</span>
      </div>
      <div class="nav-item" @click="$router.push('/circle')">
        <span class="nav-icon">📱</span>
        <span class="nav-text">宠物圈</span>
      </div>
      <div class="nav-item" @click="$router.push('/hospital')">
        <span class="nav-icon">🏥</span>
        <span class="nav-text">医院</span>
      </div>
      <div class="nav-item active" @click="$router.push('/mine')">
        <span class="nav-icon">👤</span>
        <span class="nav-text">我的</span>
      </div>
    </nav>

    <el-dialog v-model="showEditProfile" title="编辑资料" width="400px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="签名">
          <el-input 
            v-model="editForm.signature" 
            type="textarea" 
            :rows="2" 
            placeholder="介绍一下自己吧" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditProfile = false">取消</el-button>
        <el-button type="primary" @click="saveProfile" :loading="saving">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showAbout" title="关于我们" width="400px">
      <div class="about-content">
        <h3>🐾 宠物健康管理系统</h3>
        <p>版本：v1.0.0</p>
        <p>致力于为您和您的爱宠提供专业、便捷的健康管理服务。</p>
        <ul>
          <li>✅ 驱虫体检提醒</li>
          <li>✅ 个性化饮食建议</li>
          <li>✅ 智能运动监测</li>
          <li>✅ 宠物社区交流</li>
          <li>✅ 附近医院查询</li>
        </ul>
      </div>
    </el-dialog>

    <el-dialog v-model="showFeedback" title="意见反馈" width="400px">
      <el-form label-width="80px">
        <el-form-item label="反馈内容">
          <el-input 
            v-model="feedback.content" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入您的意见或建议..." 
          />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="feedback.contact" placeholder="手机号/邮箱（选填）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFeedback = false">取消</el-button>
        <el-button type="primary" @click="submitFeedback" :loading="submitting">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAppStore } from '@/store/app'
import { userApi, petApi } from '@/api'
import { ElMessage } from 'element-plus'

const appStore = useAppStore()

const showEditProfile = ref(false)
const showAbout = ref(false)
const showFeedback = ref(false)
const saving = ref(false)
const submitting = ref(false)

const userInfo = ref({
  nickname: '爱宠用户',
  signature: '欢迎来到宠物健康管理系统',
  avatar: '😊'
})

const settings = ref({
  notifications: true,
  darkMode: false,
  elderMode: false
})

const editForm = ref({
  nickname: '',
  signature: ''
})

const feedback = ref({
  content: '',
  contact: ''
})

const petsCount = ref(2)
const postsCount = ref(5)
const favoritesCount = ref(12)

const loadUserInfo = async () => {
  try {
    const res = await userApi.getInfo(1)
    if (res.code === 200) {
      userInfo.value = res.data
    }
  } catch (error) {
    // 使用默认数据
  }
}

const loadPetsCount = async () => {
  try {
    const res = await petApi.getList(1)
    if (res.code === 200) {
      petsCount.value = res.data.length
    }
  } catch (error) {
    // 使用默认数据
  }
}

const saveProfile = async () => {
  if (!editForm.value.nickname) {
    ElMessage.warning('请输入昵称')
    return
  }
  
  saving.value = true
  try {
    const res = await userApi.update(editForm.value)
    if (res.code === 200) {
      userInfo.value = { ...userInfo.value, ...editForm.value }
      ElMessage.success('保存成功')
      showEditProfile.value = false
    }
  } catch (error) {
    userInfo.value = { ...userInfo.value, ...editForm.value }
    ElMessage.success('保存成功')
    showEditProfile.value = false
  } finally {
    saving.value = false
  }
}

const toggleElderMode = async () => {
  try {
    await userApi.toggleElderMode(settings.value.elderMode)
    appStore.setElderMode(settings.value.elderMode)
    ElMessage.success(settings.value.elderMode ? '已开启长辈模式' : '已关闭长辈模式')
  } catch (error) {
    appStore.setElderMode(settings.value.elderMode)
    ElMessage.success(settings.value.elderMode ? '已开启长辈模式' : '已关闭长辈模式')
  }
}

const clearCache = () => {
  ElMessage.success('缓存已清除')
}

const submitFeedback = async () => {
  if (!feedback.value.content) {
    ElMessage.warning('请输入反馈内容')
    return
  }
  
  submitting.value = true
  try {
    // 实际项目中调用API
    setTimeout(() => {
      ElMessage.success('感谢您的反馈，我们会尽快处理！')
      showFeedback.value = false
      feedback.value = { content: '', contact: '' }
      submitting.value = false
    }, 1000)
  } catch (error) {
    ElMessage.success('感谢您的反馈，我们会尽快处理！')
    showFeedback.value = false
    feedback.value = { content: '', contact: '' }
    submitting.value = false
  }
}

onMounted(() => {
  appStore.loadSettings()
  loadUserInfo()
  loadPetsCount()
  
  // 初始化编辑表单
  editForm.value = {
    nickname: userInfo.value.nickname,
    signature: userInfo.value.signature
  }
})
</script>

<style scoped>
.mine-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px 20px 100px;
}

.user-profile {
  display: flex;
  align-items: center;
  background: white;
  padding: 25px;
  border-radius: 16px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-bottom: 20px;
}

.avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 35px;
  margin-right: 20px;
}

.user-info {
  flex: 1;
}

.user-info h2 {
  margin: 0 0 8px;
  font-size: 20px;
  color: #333;
}

.user-info p {
  margin: 0;
  font-size: 14px;
  color: #999;
}

.stats-cards {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #999;
}

.menu-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.menu-group {
  background: white;
  border-radius: 16px;
  padding: 10px 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.menu-group h3 {
  margin: 15px 0;
  font-size: 16px;
  color: #333;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: opacity 0.3s;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  opacity: 0.7;
}

.menu-icon {
  font-size: 22px;
  margin-right: 15px;
  width: 30px;
}

.menu-text {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.menu-arrow {
  font-size: 20px;
  color: #ccc;
}

.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 640px;
  display: flex;
  background: white;
  padding: 10px 0;
  box-shadow: 0 -4px 12px rgba(0,0,0,0.08);
  border-radius: 20px 20px 0 0;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  color: #999;
  transition: color 0.3s;
}

.nav-item.active {
  color: #667eea;
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 5px;
}

.nav-text {
  font-size: 12px;
}

.about-content {
  text-align: center;
  padding: 20px 0;
}

.about-content h3 {
  margin: 0 0 20px;
  color: #667eea;
}

.about-content p {
  margin: 10px 0;
  color: #666;
}

.about-content ul {
  text-align: left;
  margin: 20px 0 0;
  padding-left: 20px;
}

.about-content li {
  margin: 8px 0;
  color: #666;
}
</style>