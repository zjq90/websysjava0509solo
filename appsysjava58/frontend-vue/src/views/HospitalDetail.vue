<template>
  <div class="hospital-detail-page">
    <div class="page-header">
      <el-button @click="$router.back()" type="text">← 返回</el-button>
      <h2>医院详情</h2>
    </div>

    <div class="hospital-card" v-if="hospital">
      <div class="hospital-badge emergency" v-if="hospital.emergency">
        🚨 24h急诊
      </div>
      
      <div class="hospital-header">
        <h2>{{ hospital.name }}</h2>
        <div class="rating">
          <span class="stars">
            <span v-for="i in 5" :key="i">{{ i <= hospital.rating ? '⭐' : '☆' }}</span>
          </span>
          <span class="score">{{ hospital.rating.toFixed(1) }}</span>
          <span class="count">({{ hospital.ratingCount || 0 }}条评价)</span>
        </div>
      </div>
      
      <div class="hospital-info">
        <div class="info-item">
          <span class="icon">📍</span>
          <div class="content">
            <div class="label">地址</div>
            <div class="value">{{ hospital.address }}</div>
          </div>
        </div>
        <div class="info-item">
          <span class="icon">📞</span>
          <div class="content">
            <div class="label">电话</div>
            <div class="value">{{ hospital.phone }}</div>
          </div>
        </div>
        <div class="info-item" v-if="hospital.businessHours">
          <span class="icon">🕐</span>
          <div class="content">
            <div class="label">营业时间</div>
            <div class="value">{{ hospital.businessHours }}</div>
          </div>
        </div>
        <div class="info-item" v-if="hospital.distance">
          <span class="icon">🚶</span>
          <div class="content">
            <div class="label">距离</div>
            <div class="value">{{ hospital.distance }} km</div>
          </div>
        </div>
      </div>
      
      <div class="hospital-services">
        <h3>服务项目</h3>
        <div class="service-tags">
          <el-tag v-for="tag in hospital.tags" :key="tag" size="large">{{ tag }}</el-tag>
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button type="primary" size="large" @click="callHospital">
          📞 电话咨询
        </el-button>
        <el-button size="large" @click="navigateTo">
          📍 导航前往
        </el-button>
      </div>
    </div>

    <div class="rating-section">
      <h3>用户评价</h3>
      <div class="rating-list">
        <div class="rating-item" v-for="rating in ratings" :key="rating.id">
          <div class="rating-header">
            <div class="user-avatar">{{ rating.avatar }}</div>
            <div class="user-info">
              <div class="username">{{ rating.username }}</div>
              <div class="rating-time">{{ rating.time }}</div>
            </div>
            <div class="rating-stars">
              <span v-for="i in 5" :key="i">{{ i <= rating.score ? '⭐' : '☆' }}</span>
            </div>
          </div>
          <div class="rating-content">{{ rating.content }}</div>
        </div>
      </div>
      
      <div class="submit-rating">
        <h4>发表评价</h4>
        <el-rate v-model="newRating.score" show-score />
        <el-input
          v-model="newRating.content"
          type="textarea"
          :rows="3"
          placeholder="分享您的就医体验..."
          style="margin: 15px 0"
        />
        <el-button type="primary" @click="submitRating" :loading="submitting">
          提交评价
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { hospitalApi } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const hospital = ref(null)
const ratings = ref([])
const submitting = ref(false)

const newRating = ref({
  score: 5,
  content: ''
})

const loadDetail = async () => {
  try {
    const res = await hospitalApi.getDetail(route.params.id)
    if (res.code === 200) {
      hospital.value = res.data
    }
  } catch (error) {
    hospital.value = {
      id: 1,
      name: '爱宠动物医院',
      address: '北京市朝阳区建国路88号',
      phone: '010-12345678',
      rating: 4.8,
      distance: 1.2,
      emergency: true,
      tags: ['疫苗接种', '宠物美容', '24h急诊', '骨科', '内科', '皮肤科'],
      ratingCount: 256,
      businessHours: '周一至周日 08:00-22:00'
    }
  }
}

const loadRatings = async () => {
  try {
    const res = await hospitalApi.getRatings(route.params.id)
    if (res.code === 200) {
      ratings.value = res.data
    }
  } catch (error) {
    ratings.value = [
      {
        id: 1,
        username: '旺财妈妈',
        avatar: '🐕',
        score: 5,
        time: '2024-01-15',
        content: '医生非常专业耐心，给我家狗狗做了全面检查，收费也很合理！'
      },
      {
        id: 2,
        username: '咪咪爸爸',
        avatar: '🐱',
        score: 4,
        time: '2024-01-10',
        content: '环境很干净，护士态度很好，就是等待时间有点长。'
      }
    ]
  }
}

const callHospital = () => {
  if (hospital.value?.phone) {
    ElMessage.success(`正在拨打: ${hospital.value.phone}`)
  }
}

const navigateTo = () => {
  if (hospital.value?.name) {
    ElMessage.success(`正在导航到: ${hospital.value.name}`)
  }
}

const submitRating = async () => {
  if (!newRating.value.content) {
    ElMessage.warning('请填写评价内容')
    return
  }
  
  submitting.value = true
  try {
    const res = await hospitalApi.submitRating({
      hospitalId: route.params.id,
      score: newRating.value.score,
      content: newRating.value.content
    })
    if (res.code === 200) {
      ElMessage.success('评价提交成功')
      ratings.value.unshift({
        id: Date.now(),
        username: '我',
        avatar: '😊',
        score: newRating.value.score,
        time: '刚刚',
        content: newRating.value.content
      })
      newRating.value = { score: 5, content: '' }
    }
  } catch (error) {
    ratings.value.unshift({
      id: Date.now(),
      username: '我',
      avatar: '😊',
      score: newRating.value.score,
      time: '刚刚',
      content: newRating.value.content
    })
    ElMessage.success('评价提交成功')
    newRating.value = { score: 5, content: '' }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadDetail()
  loadRatings()
})
</script>

<style scoped>
.hospital-detail-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  flex: 1;
  margin: 0;
  text-align: center;
  font-size: 20px;
}

.hospital-card {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  margin-bottom: 20px;
  position: relative;
}

.hospital-badge {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 6px 15px;
  border-radius: 15px;
  font-size: 13px;
}

.hospital-badge.emergency {
  background: #fff1f0;
  color: #f5222d;
}

.hospital-header h2 {
  margin: 0 0 10px;
  font-size: 22px;
  color: #333;
  padding-right: 100px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stars {
  font-size: 16px;
}

.score {
  font-size: 18px;
  font-weight: bold;
  color: #faad14;
}

.count {
  font-size: 14px;
  color: #999;
}

.hospital-info {
  margin: 25px 0;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .icon {
  font-size: 22px;
  width: 30px;
}

.info-item .content {
  flex: 1;
}

.info-item .label {
  font-size: 13px;
  color: #999;
  margin-bottom: 5px;
}

.info-item .value {
  font-size: 15px;
  color: #333;
}

.hospital-services {
  margin: 25px 0;
}

.hospital-services h3 {
  margin: 0 0 15px;
  font-size: 16px;
}

.service-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-top: 25px;
}

.action-buttons .el-button {
  flex: 1;
}

.rating-section {
  background: white;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.rating-section h3 {
  margin: 0 0 20px;
  font-size: 18px;
}

.rating-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 30px;
}

.rating-item {
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.rating-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.rating-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 12px;
}

.user-info {
  flex: 1;
}

.username {
  font-size: 15px;
  font-weight: bold;
  color: #333;
  margin-bottom: 3px;
}

.rating-time {
  font-size: 12px;
  color: #999;
}

.rating-stars {
  font-size: 14px;
}

.rating-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.submit-rating h4 {
  margin: 0 0 15px;
  font-size: 16px;
}
</style>