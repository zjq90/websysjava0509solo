<template>
  <view class="container" :class="{ 'elder-mode': elderMode }">
    <view class="search-section">
      <input class="search-input" v-model="searchKeyword" placeholder="搜索宠物名称..." />
    </view>
    
    <view class="pet-list">
      <view 
        v-for="pet in filteredPets" 
        :key="pet.id" 
        class="pet-item card"
        @click="goToDetail(pet)"
      >
        <view class="pet-header flex-between">
          <view class="pet-basic">
            <image class="pet-avatar" :src="pet.avatar || '/static/pet-avatar.png'" mode="aspectFill"></image>
            <view class="pet-info">
              <text class="pet-name">{{ pet.name }}</text>
              <text class="pet-specie">{{ pet.species }} · {{ pet.breed || '未知品种' }}</text>
            </view>
          </view>
          <view class="pet-status">
            <text class="status-tag" :class="'status-' + pet.status.toLowerCase()">{{ getStatusText(pet.status) }}</text>
          </view>
        </view>
        
        <view class="pet-allergy" v-if="pet.allergyHistory">
          <text class="allergy-label">⚠️ 过敏史：</text>
          <text class="allergy-text">{{ pet.allergyHistory }}</text>
        </view>
        
        <view class="pet-tags">
          <text v-for="tag in petTags[pet.id] || []" :key="tag.id" class="tag" :style="{ backgroundColor: tag.tagColor + '20', color: tag.tagColor }">{{ tag.tagName }}</text>
        </view>
        
        <view class="pet-footer">
          <text class="pet-age">{{ pet.age || 0 }}岁 · {{ pet.gender === 'MALE' ? '公' : '母' }}</text>
          <text class="pet-weight">{{ pet.weight || 0 }}kg</text>
        </view>
      </view>
      
      <view v-if="filteredPets.length === 0" class="empty">
        <text>暂无宠物数据</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '../../store/index.js'
import petApi from '../../api/pet.js'

const userStore = useUserStore()

const petList = ref([])
const petTags = ref({})
const searchKeyword = ref('')
const elderMode = computed(() => userStore.elderMode)

const filteredPets = computed(() => {
  if (!searchKeyword.value) return petList.value
  return petList.value.filter(pet => 
    pet.name.includes(searchKeyword.value)
  )
})

const getStatusText = (status) => {
  const map = {
    'HEALTHY': '健康',
    'CHRONIC': '慢性病',
    'POST_OPERATION': '术后'
  }
  return map[status] || status
}

const loadPets = async () => {
  try {
    const res = await petApi.getAllPets()
    petList.value = res || []
    
    for (const pet of petList.value) {
      loadPetTags(pet.id)
    }
  } catch (e) {
    console.error(e)
  }
}

const loadPetTags = async (petId) => {
  try {
    const res = await petApi.getPetTags(petId)
    petTags.value[petId] = res || []
  } catch (e) {
    console.error(e)
  }
}

const goToDetail = (pet) => {
  uni.navigateTo({
    url: '/pages/pet/detail?id=' + pet.id
  })
}

onShow(() => {
  loadPets()
})
</script>

<style scoped>
.search-section {
  margin-bottom: 20rpx;
}

.search-input {
  width: 100%;
  height: 72rpx;
  background-color: #fff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}

.pet-item {
  margin-bottom: 20rpx;
}

.pet-header {
  margin-bottom: 16rpx;
}

.pet-basic {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.pet-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background-color: #f5f7fa;
}

.pet-info {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.pet-name {
  font-size: 30rpx;
  font-weight: bold;
  color: #303133;
}

.pet-specie {
  font-size: 24rpx;
  color: #909399;
}

.status-tag {
  padding: 6rpx 12rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
}

.status-healthy {
  background-color: #F0F9EB;
  color: #67C23A;
}

.status-chronic {
  background-color: #FEF0F0;
  color: #F56C6C;
}

.status-post_operation {
  background-color: #FDF6EC;
  color: #E6A23C;
}

.pet-allergy {
  background-color: #FEF0F0;
  padding: 16rpx;
  border-radius: 8rpx;
  margin-bottom: 16rpx;
}

.allergy-label {
  color: #F56C6C;
  font-weight: bold;
}

.allergy-text {
  color: #F56C6C;
}

.pet-tags {
  margin-bottom: 16rpx;
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
}

.pet-tags .tag {
  font-size: 22rpx;
  padding: 6rpx 12rpx;
}

.pet-footer {
  display: flex;
  gap: 24rpx;
  font-size: 24rpx;
  color: #909399;
}

.elder-mode .pet-name {
  font-size: 34rpx;
}

.elder-mode .pet-specie {
  font-size: 28rpx;
}

.elder-mode .search-input {
  font-size: 32rpx;
  height: 88rpx;
}
</style>
