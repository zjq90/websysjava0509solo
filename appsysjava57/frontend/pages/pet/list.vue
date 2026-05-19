<template>
  <view class="container" :class="{ 'elder-mode': isElderMode }">
    <view class="add-pet-btn" @click="addPet">
      <text class="add-icon">+</text>
      <text class="add-text">添加宠物</text>
    </view>

    <view class="pet-list" v-if="pets.length > 0">
      <view class="pet-card" v-for="pet in pets" :key="pet.id" @click="viewPet(pet)">
        <view class="pet-avatar">
          <text class="avatar-emoji">{{ getPetEmoji(pet.species) }}</text>
        </view>
        <view class="pet-info">
          <text class="pet-name">{{ pet.name }}</text>
          <text class="pet-detail">{{ pet.breed }} · {{ pet.gender }}</text>
          <text class="pet-age">{{ getAgeText(pet.birthDate) }}</text>
        </view>
        <view class="pet-arrow">›</view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">🐕</text>
      <text class="empty-text">还没有添加宠物</text>
      <text class="empty-desc">点击上方按钮添加您的爱宠</text>
    </view>
  </view>
</template>

<script>
import { petApi } from '@/utils/api.js'

export default {
  data() {
    return {
      isElderMode: false,
      pets: []
    }
  },
  onShow() {
    this.checkElderMode()
    this.loadPets()
  },
  methods: {
    checkElderMode() {
      this.isElderMode = uni.getStorageSync('elderMode') || false
    },
    async loadPets() {
      try {
        // 使用模拟的用户ID 1，实际项目中应该从用户信息获取
        const res = await petApi.getUserPets(1)
        if (res.code === 200 || res.code === 0) {
          this.pets = res.data || []
        }
      } catch (e) {
        console.log('加载宠物失败', e)
        this.pets = [
          { id: 1, name: '豆豆', species: '狗', breed: '金毛', gender: '公', birthDate: '2022-05-15', weight: 25.5 },
          { id: 2, name: '咪咪', species: '猫', breed: '英短', gender: '母', birthDate: '2023-03-20', weight: 4.5 }
        ]
      }
    },
    getPetEmoji(species) {
      if (species && species.includes('狗')) return '🐕'
      if (species && species.includes('猫')) return '🐱'
      return '🐾'
    },
    getAgeText(birthDate) {
      if (!birthDate) return '未知年龄'
      const birth = new Date(birthDate)
      const now = new Date()
      const diff = now - birth
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      if (days < 30) return `${days}天`
      if (days < 365) return `${Math.floor(days / 30)}个月`
      return `${Math.floor(days / 365)}岁`
    },
    addPet() {
      uni.showToast({ title: '添加宠物功能开发中', icon: 'none' })
    },
    viewPet(pet) {
      uni.navigateTo({ url: `/pages/pet/detail?id=${pet.id}` })
    }
  }
}
</script>

<style scoped>
.add-pet-btn {
  background: linear-gradient(135deg, #4CAF50, #45a049);
  border-radius: 16rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
}

.add-icon {
  font-size: 40rpx;
  color: #fff;
  margin-right: 12rpx;
}

.add-text {
  font-size: 32rpx;
  color: #fff;
  font-weight: 500;
}

.pet-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.pet-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.pet-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #FFF3E0, #FFE0B2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}

.avatar-emoji {
  font-size: 60rpx;
}

.pet-info {
  flex: 1;
}

.pet-name {
  display: block;
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 8rpx;
}

.pet-detail {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 6rpx;
}

.pet-age {
  display: block;
  font-size: 24rpx;
  color: #999;
}

.pet-arrow {
  font-size: 48rpx;
  color: #ccc;
}

.empty-state {
  text-align: center;
  padding: 100rpx 40rpx;
}

.empty-icon {
  display: block;
  font-size: 120rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  display: block;
  font-size: 32rpx;
  color: #333;
  font-weight: bold;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 26rpx;
  color: #999;
}
</style>
