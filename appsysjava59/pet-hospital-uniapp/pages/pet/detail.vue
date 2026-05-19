<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="pet-detail-card card">
      <view class="pet-header">
        <image class="pet-avatar" :src="pet?.avatar || '/static/pet-avatar.png'" mode="aspectFill"></image>
        <view class="pet-info">
          <text class="pet-name">{{ pet?.name }}</text>
          <text class="pet-specie">{{ pet?.species }} · {{ pet?.breed || '未知品种' }}</text>
          <text class="pet-meta">{{ pet?.age || 0 }}岁 · {{ pet?.gender === 'MALE' ? '公' : '母' }} · {{ pet?.weight || 0 }}kg</text>
        </view>
      </view>
      
      <view class="pet-allergy" v-if="pet?.allergyHistory">
        <text class="allergy-label">⚠️ 过敏史：</text>
        <text class="allergy-text">{{ pet?.allergyHistory }}</text>
      </view>
      
      <view class="tag-section">
        <view class="section-header flex-between">
          <text class="section-title">标签</text>
          <text class="add-tag-btn" @click="showAddTag">+ 添加标签</text>
        </view>
        <view class="tags-list">
          <view v-for="tag in tags" :key="tag.id" class="tag-item">
            <text class="tag-name" :style="{ backgroundColor: tag.tagColor + '20', color: tag.tagColor }">{{ tag.tagName }}</text>
            <text class="tag-delete" @click="deleteTag(tag.id)">×</text>
          </view>
          <view v-if="tags.length === 0" class="empty-tags">暂无标签</view>
        </view>
      </view>
    </view>
    
    <view class="section-card card">
      <view class="section-header">
        <text class="section-title">疫苗记录</text>
      </view>
      <view class="vaccine-list">
        <view v-for="record in vaccines" :key="record.id" class="vaccine-item">
          <view class="vaccine-name">{{ record.vaccineName }}</view>
          <view class="vaccine-date">{{ record.vaccineDate }}</view>
          <view class="vaccine-hospital">{{ record.hospital }}</view>
        </view>
        <view v-if="vaccines.length === 0" class="empty">暂无疫苗记录</view>
      </view>
    </view>
    
    <view class="section-card card">
      <view class="section-header">
        <text class="section-title">诊疗历史</text>
      </view>
      <view class="history-list">
        <view class="history-item">
          <view class="history-title">2025-05-10 术后复查</view>
          <view class="history-content">泰迪术后一周复查，恢复良好</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import petApi from '../../api/pet.js'

const userStore = useUserStore()

const petId = ref('')
const pet = ref(null)
const tags = ref([])
const vaccines = ref([])
const newTagName = ref('')
const elderMode = computed(() => userStore.elderMode)

const loadPetDetail = async () => {
  try {
    const res = await petApi.getPetDetail(petId.value)
    pet.value = res
  } catch (e) {
    console.error(e)
  }
}

const loadTags = async () => {
  try {
    const res = await petApi.getPetTags(petId.value)
    tags.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const loadVaccines = async () => {
  try {
    const res = await petApi.getVaccineRecords(petId.value)
    vaccines.value = res || []
  } catch (e) {
    console.error(e)
  }
}

const showAddTag = () => {
  uni.showModal({
    title: '添加标签',
    editable: true,
    placeholderText: '请输入标签名称',
    success: async (res) => {
      if (res.confirm && res.content) {
        try {
          await petApi.addPetTag(petId.value, {
            tagName: res.content,
            tagColor: '#' + Math.floor(Math.random()*16777215).toString(16).padStart(6, '0')
          })
          loadTags()
          uni.showToast({ title: '添加成功', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

const deleteTag = async (tagId) => {
  uni.showModal({
    title: '提示',
    content: '确定删除该标签吗？',
    success: async (res) => {
      if (res.confirm) {
        try {
          await petApi.deletePetTag(tagId)
          loadTags()
          uni.showToast({ title: '删除成功', icon: 'success' })
        } catch (e) {
          console.error(e)
        }
      }
    }
  })
}

onLoad((options) => {
  petId.value = options.id
  loadPetDetail()
  loadTags()
  loadVaccines()
})
</script>

<style scoped>
.pet-detail-card {
  margin-bottom: 20rpx;
}

.pet-header {
  display: flex;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 24rpx;
}

.pet-avatar {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  background-color: #f5f7fa;
}

.pet-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.pet-name {
  font-size: 36rpx;
  font-weight: bold;
  color: #303133;
}

.pet-specie {
  font-size: 28rpx;
  color: #606266;
}

.pet-meta {
  font-size: 24rpx;
  color: #909399;
}

.pet-allergy {
  background-color: #FEF0F0;
  padding: 20rpx;
  border-radius: 12rpx;
  margin-bottom: 24rpx;
}

.allergy-label {
  color: #F56C6C;
  font-weight: bold;
  font-size: 26rpx;
}

.allergy-text {
  color: #F56C6C;
  font-size: 26rpx;
}

.section-header {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
}

.add-tag-btn {
  font-size: 26rpx;
  color: #409EFF;
}

.tags-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.tag-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.tag-name {
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}

.tag-delete {
  font-size: 32rpx;
  color: #909399;
  line-height: 1;
}

.empty-tags {
  font-size: 26rpx;
  color: #909399;
}

.vaccine-item {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.vaccine-item:last-child {
  border-bottom: none;
}

.vaccine-name {
  font-size: 28rpx;
  color: #303133;
  margin-bottom: 8rpx;
}

.vaccine-date {
  font-size: 24rpx;
  color: #606266;
  margin-bottom: 4rpx;
}

.vaccine-hospital {
  font-size: 22rpx;
  color: #909399;
}

.history-item {
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.history-title {
  font-size: 28rpx;
  color: #303133;
  margin-bottom: 8rpx;
}

.history-content {
  font-size: 24rpx;
  color: #606266;
}

.elder-mode .pet-name {
  font-size: 40rpx;
}

.elder-mode .section-title {
  font-size: 34rpx;
}
</style>
