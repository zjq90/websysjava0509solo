<template>
  <div class="page-container">
    <div class="welcome-section">
      <div class="welcome-card">
        <div class="welcome-title">
          <span class="emoji">🌻</span>
          <span>你好，{{ nickname || '朋友' }}</span>
        </div>
        <span class="welcome-subtitle">今天心情怎么样？让我们一起守护心理健康</span>
      </div>
    </div>

    <div class="quick-actions">
      <div class="action-grid">
        <div class="action-item" @click="goToAssessment('phq9')">
          <div class="action-icon bg-blue">📋</div>
          <span class="action-text">抑郁测评</span>
        </div>
        <div class="action-item" @click="goToAssessment('gad7')">
          <div class="action-icon bg-green">📝</div>
          <span class="action-text">焦虑测评</span>
        </div>
        <div class="action-item" @click="goToCounselors">
          <div class="action-icon bg-purple">👨‍⚕️</div>
          <span class="action-text">找咨询师</span>
        </div>
        <div class="action-item" @click="goToEmergency">
          <div class="action-icon bg-red">🆘</div>
          <span class="action-text">紧急求助</span>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <span class="section-title">💡 每日建议</span>
      </div>
      <div class="card">
        <div class="daily-tip">
          <span class="tip-text">{{ dailyTip }}</span>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <span class="section-title">⭐ 推荐咨询师</span>
        <span class="section-more" @click="goToCounselors">查看更多 ></span>
      </div>
      <div class="counselor-list">
        <div class="counselor-card" v-for="counselor in recommendedCounselors" :key="counselor.id" @click="goToDetail(counselor.id)">
          <div class="counselor-avatar">
            <span>{{ counselor.name.charAt(0) }}</span>
          </div>
          <div class="counselor-info">
            <div class="counselor-name">{{ counselor.name }}</div>
            <div class="counselor-qualification">{{ counselor.qualification }}</div>
            <div class="counselor-tags">
              <span class="tag tag-primary" v-for="(specialty, index) in counselor.specialties.slice(0, 2)" :key="index">{{ specialty }}</span>
            </div>
          </div>
          <div class="counselor-rating">
            <span class="rating-star">⭐</span>
            <span class="rating-score">{{ counselor.rating }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="section-header">
        <span class="section-title">🎧 冥想音频</span>
      </div>
      <div class="resource-list">
        <div class="resource-card" v-for="resource in audioResources" :key="resource.id">
          <div class="resource-icon bg-purple">🎵</div>
          <div class="resource-info">
            <div class="resource-title">{{ resource.title }}</div>
            <div class="resource-meta">
              <span>{{ formatDuration(resource.durationSeconds) }}</span>
              <span> · {{ resource.viewCount }} 次收听</span>
            </div>
          </div>
          <div class="play-btn">▶</div>
        </div>
      </div>
    </div>

    <div class="section" style="margin-bottom: 120rpx;">
      <div class="section-header">
        <span class="section-title">📚 科普文章</span>
      </div>
      <div class="article-list">
        <div class="article-card" v-for="resource in articleResources" :key="resource.id">
          <div class="article-content">
            <div class="article-title">{{ resource.title }}</div>
            <div class="article-desc">{{ resource.content.substring(0, 50) }}...</div>
            <div class="article-meta">
              <span class="view-count">{{ resource.viewCount }} 阅读</span>
              <span class="tag tag-success">{{ resource.category }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter()
    const { proxy } = getCurrentInstance()
    
    const nickname = ref('')
    const dailyTip = ref('深呼吸是最简单的放松方式。试着慢慢吸气4秒，屏住呼吸4秒，再慢慢呼气6秒。重复几次，你会感到更加平静。')
    const recommendedCounselors = ref([])
    const audioResources = ref([])
    const articleResources = ref([])
    
    const loadUserInfo = () => {
      const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
      if (userInfo) {
        nickname.value = userInfo.nickname || userInfo.username
      }
    }
    
    const loadRecommendedCounselors = () => {
      proxy.$request({
        url: '/counselors'
      }).then(res => {
        recommendedCounselors.value = res.slice(0, 3)
      }).catch(() => {
        recommendedCounselors.value = [
          { id: 1, name: '张医生', qualification: '国家二级心理咨询师', rating: 4.8, specialties: ['抑郁', '焦虑'] },
          { id: 2, name: '李咨询师', qualification: '注册心理师', rating: 4.6, specialties: ['职场压力', '人际关系'] },
          { id: 3, name: '王教授', qualification: '心理学教授', rating: 4.9, specialties: ['亲子关系', '青少年心理'] }
        ]
      })
    }
    
    const loadResources = () => {
      proxy.$request({
        url: '/resources'
      }).then(res => {
        audioResources.value = res.filter(r => r.type === 'AUDIO').slice(0, 2)
        articleResources.value = res.filter(r => r.type === 'ARTICLE').slice(0, 3)
      }).catch(() => {
        audioResources.value = [
          { id: 1, title: '正念冥想：缓解焦虑', durationSeconds: 900, viewCount: 1256 },
          { id: 2, title: '深呼吸放松训练', durationSeconds: 480, viewCount: 2341 }
        ]
        articleResources.value = [
          { id: 3, title: '如何识别抑郁信号', content: '本文详细介绍抑郁症的早期症状和识别方法', viewCount: 5678, category: '科普' },
          { id: 4, title: '职场压力管理10招', content: '实用的职场压力管理技巧，帮助您在工作中保持良好的心理状态', viewCount: 3456, category: '职场' }
        ]
      })
    }
    
    const goToAssessment = (type) => {
      if (!localStorage.getItem('token')) {
        router.push('/login')
        return
      }
      alert('测评功能开发中...')
    }
    
    const goToCounselors = () => {
      router.push('/counselors')
    }
    
    const goToDetail = (id) => {
      alert('咨询师详情页开发中...')
    }
    
    const goToEmergency = () => {
      router.push('/emergency')
    }
    
    const formatDuration = (seconds) => {
      const mins = Math.floor(seconds / 60)
      const secs = seconds % 60
      return `${mins}:${secs.toString().padStart(2, '0')}`
    }
    
    onMounted(() => {
      loadUserInfo()
      loadRecommendedCounselors()
      loadResources()
    })
    
    return {
      nickname,
      dailyTip,
      recommendedCounselors,
      audioResources,
      articleResources,
      goToAssessment,
      goToCounselors,
      goToDetail,
      goToEmergency,
      formatDuration
    }
  }
}
</script>

<style scoped>
.welcome-section {
  margin-bottom: 30rpx;
}

.welcome-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  padding: 40rpx;
  color: #ffffff;
}

.welcome-title {
  display: flex;
  align-items: center;
  font-size: 40rpx;
  font-weight: 600;
  margin-bottom: 16rpx;
}

.emoji {
  margin-right: 12rpx;
  font-size: 48rpx;
}

.welcome-subtitle {
  font-size: 26rpx;
  opacity: 0.9;
  line-height: 1.6;
}

.quick-actions {
  margin-bottom: 30rpx;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 20rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.action-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-bottom: 16rpx;
}

.bg-blue { background: #ECF5FF; }
.bg-green { background: #F0F9EB; }
.bg-purple { background: #F5F0FF; }
.bg-red { background: #FEF0F0; }

.action-text {
  font-size: 24rpx;
  color: #606266;
}

.section {
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
  padding: 0 10rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #303133;
}

.section-more {
  font-size: 24rpx;
  color: #409EFF;
  cursor: pointer;
}

.daily-tip {
  display: flex;
  align-items: flex-start;
}

.tip-text {
  font-size: 28rpx;
  color: #606266;
  line-height: 1.8;
}

.counselor-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.counselor-card {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
  cursor: pointer;
}

.counselor-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 36rpx;
  font-weight: 600;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.counselor-info {
  flex: 1;
}

.counselor-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8rpx;
}

.counselor-qualification {
  font-size: 24rpx;
  color: #909399;
  margin-bottom: 12rpx;
}

.counselor-rating {
  display: flex;
  align-items: center;
  margin-left: 20rpx;
}

.rating-star {
  font-size: 24rpx;
  margin-right: 6rpx;
}

.rating-score {
  font-size: 28rpx;
  font-weight: 600;
  color: #E6A23C;
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.resource-card {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.resource-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.resource-info {
  flex: 1;
}

.resource-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8rpx;
}

.resource-meta {
  font-size: 24rpx;
  color: #909399;
}

.play-btn {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  font-size: 24rpx;
  cursor: pointer;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.article-card {
  padding: 30rpx;
  background: #ffffff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.article-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #303133;
  margin-bottom: 12rpx;
}

.article-desc {
  font-size: 24rpx;
  color: #909399;
  margin-bottom: 16rpx;
  line-height: 1.6;
}

.article-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.view-count {
  font-size: 22rpx;
  color: #C0C4CC;
}
</style>
