<template>
  <div class="page-container">
    <div class="search-section">
      <div class="search-bar">
        <span class="search-icon">🔍</span>
        <input class="search-input" v-model="searchKeyword" placeholder="搜索咨询师..." />
      </div>
    </div>

    <div class="filter-section">
      <div class="filter-scroll">
        <span class="filter-item" :class="{ active: activeFilter === 'all' }" @click="setFilter('all')">全部</span>
        <span class="filter-item" :class="{ active: activeFilter === 'anxiety' }" @click="setFilter('anxiety')">焦虑</span>
        <span class="filter-item" :class="{ active: activeFilter === 'depression' }" @click="setFilter('depression')">抑郁</span>
        <span class="filter-item" :class="{ active: activeFilter === 'marriage' }" @click="setFilter('marriage')">婚姻家庭</span>
        <span class="filter-item" :class="{ active: activeFilter === 'work' }" @click="setFilter('work')">职场压力</span>
      </div>
    </div>

    <div class="sort-section">
      <span class="sort-item" :class="{ active: sortBy === 'rating' }" @click="setSort('rating')">
        <span>评分</span>
        <span class="sort-icon" v-if="sortBy === 'rating'">↓</span>
      </span>
      <span class="sort-item" :class="{ active: sortBy === 'price' }" @click="setSort('price')">
        <span>价格</span>
        <span class="sort-icon" v-if="sortBy === 'price'">↑</span>
      </span>
      <span class="sort-item" :class="{ active: sortBy === 'experience' }" @click="setSort('experience')">
        <span>经验</span>
        <span class="sort-icon" v-if="sortBy === 'experience'">↓</span>
      </span>
    </div>

    <div class="counselor-list">
      <div class="counselor-card" v-for="counselor in filteredCounselors" :key="counselor.id" @click="goToDetail(counselor.id)">
        <div class="counselor-avatar">
          <span>{{ counselor.name.charAt(0) }}</span>
        </div>
        <div class="counselor-content">
          <div class="counselor-header">
            <span class="counselor-name">{{ counselor.name }}</span>
            <div class="counselor-rating">
              <span class="rating-star">⭐</span>
              <span class="rating-score">{{ counselor.rating }}</span>
            </div>
          </div>
          <span class="counselor-qualification">{{ counselor.qualification }}</span>
          <div class="counselor-tags">
            <span class="tag tag-primary" v-for="(specialty, index) in counselor.specialties.slice(0, 3)" :key="index">{{ specialty }}</span>
          </div>
          <div class="counselor-footer">
            <span class="counselor-price">¥{{ counselor.pricePerHour }}/小时</span>
            <span class="counselor-experience">{{ counselor.experienceYears }}年经验</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const router = useRouter()
    const { proxy } = getCurrentInstance()
    
    const searchKeyword = ref('')
    const activeFilter = ref('all')
    const sortBy = ref('rating')
    const counselors = ref([])
    
    const filteredCounselors = computed(() => {
      let result = [...counselors.value]
      
      if (searchKeyword.value) {
        result = result.filter(c => 
          c.name.includes(searchKeyword.value) || 
          c.specialties.some(s => s.includes(searchKeyword.value))
        )
      }
      
      if (activeFilter.value !== 'all') {
        const specialtyMap = {
          anxiety: '焦虑',
          depression: '抑郁',
          marriage: '婚姻家庭',
          work: '职场压力'
        }
        result = result.filter(c => c.specialties.includes(specialtyMap[activeFilter.value]))
      }
      
      if (sortBy.value === 'rating') {
        result.sort((a, b) => b.rating - a.rating)
      } else if (sortBy.value === 'price') {
        result.sort((a, b) => a.pricePerHour - b.pricePerHour)
      } else if (sortBy.value === 'experience') {
        result.sort((a, b) => b.experienceYears - a.experienceYears)
      }
      
      return result
    })
    
    const setFilter = (filter) => {
      activeFilter.value = filter
    }
    
    const setSort = (sort) => {
      sortBy.value = sort
    }
    
    const goToDetail = (id) => {
      alert('咨询师详情页开发中...')
    }
    
    const loadCounselors = () => {
      proxy.$request({
        url: '/counselors'
      }).then(res => {
        counselors.value = res
      }).catch(() => {
        counselors.value = [
          { id: 1, name: '张医生', qualification: '国家二级心理咨询师，心理学博士', rating: 4.8, pricePerHour: 300, experienceYears: 15, specialties: ['抑郁', '焦虑', '情绪管理', '婚姻家庭'] },
          { id: 2, name: '李咨询师', qualification: '注册心理师，临床心理学硕士', rating: 4.6, pricePerHour: 250, experienceYears: 8, specialties: ['职场压力', '人际关系', '个人成长', '职业规划'] },
          { id: 3, name: '王教授', qualification: '心理学教授，博士生导师', rating: 4.9, pricePerHour: 500, experienceYears: 22, specialties: ['亲子关系', '青少年心理', '学业压力', '青春期'] }
        ]
      })
    }
    
    onMounted(() => {
      loadCounselors()
    })
    
    return {
      searchKeyword,
      activeFilter,
      sortBy,
      counselors,
      filteredCounselors,
      setFilter,
      setSort,
      goToDetail
    }
  }
}
</script>

<style scoped>
.search-section {
  margin-bottom: 30rpx;
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background: #ffffff;
  border-radius: 50rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.search-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  border: none;
  font-size: 28rpx;
  outline: none;
}

.filter-section {
  margin-bottom: 30rpx;
}

.filter-scroll {
  display: flex;
  gap: 16rpx;
  padding-bottom: 10rpx;
  overflow-x: auto;
}

.filter-item {
  flex-shrink: 0;
  padding: 16rpx 32rpx;
  background: #ffffff;
  border-radius: 40rpx;
  font-size: 26rpx;
  color: #606266;
  cursor: pointer;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.filter-item.active {
  background: linear-gradient(135deg, #409EFF 0%, #66B1FF 100%);
  color: #ffffff;
}

.sort-section {
  display: flex;
  gap: 40rpx;
  margin-bottom: 30rpx;
  padding: 0 10rpx;
}

.sort-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  font-size: 26rpx;
  color: #909399;
  cursor: pointer;
}

.sort-item.active {
  color: #409EFF;
  font-weight: 500;
}

.sort-icon {
  font-size: 20rpx;
}

.counselor-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.counselor-card {
  display: flex;
  gap: 24rpx;
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
  flex-shrink: 0;
}

.counselor-content {
  flex: 1;
}

.counselor-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12rpx;
}

.counselor-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #303133;
}

.counselor-rating {
  display: flex;
  align-items: center;
  gap: 6rpx;
}

.rating-star {
  font-size: 24rpx;
}

.rating-score {
  font-size: 28rpx;
  font-weight: 600;
  color: #E6A23C;
}

.counselor-qualification {
  display: block;
  font-size: 24rpx;
  color: #909399;
  margin-bottom: 16rpx;
}

.counselor-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 16rpx;
}

.counselor-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.counselor-price {
  font-size: 28rpx;
  font-weight: 600;
  color: #F56C6C;
}

.counselor-experience {
  font-size: 24rpx;
  color: #909399;
}
</style>
