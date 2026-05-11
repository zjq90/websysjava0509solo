<template>
  <view class="page-container" :class="{ 'elder-mode-enabled': elderMode }">
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="tab-item" 
          :class="{ active: !filters.title }"
          @click="setFilter('title', '')"
        >
          全部职称
        </view>
        <view 
          class="tab-item" 
          :class="{ active: filters.title === '主任医师' }"
          @click="setFilter('title', '主任医师')"
        >
          主任医师
        </view>
        <view 
          class="tab-item" 
          :class="{ active: filters.title === '副主任医师' }"
          @click="setFilter('title', '副主任医师')"
        >
          副主任医师
        </view>
      </view>
      
      <view class="filter-tabs">
        <view 
          class="tab-item" 
          :class="{ active: !filters.isExpert }"
          @click="setFilter('isExpert', '')"
        >
          全部号源
        </view>
        <view 
          class="tab-item" 
          :class="{ active: filters.isExpert === 1 }"
          @click="setFilter('isExpert', 1)"
        >
          专家号
        </view>
        <view 
          class="tab-item" 
          :class="{ active: filters.isExpert === 0 }"
          @click="setFilter('isExpert', 0)"
        >
          普通号
        </view>
      </view>

      <view class="dept-selector">
        <view class="dept-list">
          <view 
            class="dept-tag" 
            :class="{ active: !filters.deptId }"
            @click="setFilter('deptId', '')"
          >
            全部科室
          </view>
          <view 
            class="dept-tag" 
            v-for="dept in departments" 
            :key="dept.id"
            :class="{ active: filters.deptId == dept.id }"
            @click="setFilter('deptId', dept.id)"
          >
            {{ dept.deptName }}
          </view>
        </view>
      </view>
    </view>

    <view class="doctor-list">
      <view class="doctor-card" v-for="doctor in doctors" :key="doctor.id" @click="goToDetail(doctor.id)">
        <view class="doctor-header">
          <view class="avatar">
            <text>👨‍⚕️</text>
          </view>
          <view class="info">
            <view class="name-row">
              <text class="name">{{ doctor.doctorName }}</text>
              <text class="title" v-if="doctor.title">{{ doctor.title }}</text>
              <text class="expert" v-if="doctor.isExpert === 1">专家</text>
            </view>
            <text class="dept">{{ doctor.deptName }}</text>
          </view>
        </view>
        
        <view class="specialty">
          <text class="label">擅长：</text>
          <text>{{ doctor.specialty || '暂无' }}</text>
        </view>

        <view class="footer">
          <view class="fee">
            <text class="fee-label">挂号费</text>
            <text class="fee-value">¥{{ doctor.consultationFee }}</text>
          </view>
          <view class="availability" v-if="doctor.availableSlots > 0">
            <text class="available-text">{{ doctor.availableSlots }}个号源</text>
          </view>
          <view class="availability no-slot" v-else>
            <text>暂无号源</text>
          </view>
        </view>
      </view>

      <view class="empty" v-if="doctors.length === 0">
        <text>暂无符合条件的医生</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onLoad } from 'vue'
import { useStore } from 'vuex'
import { doctorApi, departmentApi } from '@/utils/api'

const store = useStore()

const doctors = ref([])
const departments = ref([])

const filters = reactive({
  deptId: '',
  title: '',
  isExpert: ''
})

const elderMode = computed(() => store.getters.elderMode)

onLoad((options) => {
  if (options.deptId) {
    filters.deptId = options.deptId
  }
})

const loadDepartments = async () => {
  try {
    departments.value = await departmentApi.getAll()
  } catch (e) {
    console.error('加载科室失败:', e)
  }
}

const loadDoctors = async () => {
  try {
    const params = {
      ...(filters.deptId && { deptId: filters.deptId }),
      ...(filters.title && { title: filters.title }),
      ...(filters.isExpert !== '' && { isExpert: filters.isExpert })
    }
    doctors.value = await doctorApi.getAll(params)
  } catch (e) {
    console.error('加载医生失败:', e)
  }
}

const setFilter = (key, value) => {
  filters[key] = value
  loadDoctors()
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/doctor/doctor-detail?id=${id}` })
}

onMounted(() => {
  loadDepartments()
  loadDoctors()
})
</script>

<style lang="scss" scoped>
.page-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.filter-section {
  background: #fff;
  padding: 24rpx;
  position: sticky;
  top: 0;
  z-index: 10;
}

.filter-tabs {
  display: flex;
  gap: 16rpx;
  margin-bottom: 20rpx;
  flex-wrap: wrap;
}

.tab-item {
  padding: 12rpx 28rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
}

.tab-item.active {
  background: #e6f7ff;
  color: #1890ff;
}

.dept-selector {
  border-top: 2rpx solid #f0f0f0;
  padding-top: 20rpx;
}

.dept-list {
  display: flex;
  gap: 16rpx;
  flex-wrap: wrap;
}

.dept-tag {
  padding: 12rpx 24rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  font-size: 24rpx;
  color: #666;
}

.dept-tag.active {
  background: #1890ff;
  color: #fff;
}

.doctor-list {
  padding: 24rpx;
}

.doctor-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}

.doctor-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  background: #e6f7ff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 44rpx;
  margin-right: 20rpx;
}

.info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 8rpx;
}

.name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.title {
  font-size: 24rpx;
  color: #1890ff;
}

.expert {
  font-size: 20rpx;
  color: #ff4d4f;
  background: #fff1f0;
  padding: 4rpx 12rpx;
  border-radius: 4rpx;
}

.dept {
  font-size: 24rpx;
  color: #999;
}

.specialty {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.fee {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
}

.fee-label {
  font-size: 24rpx;
  color: #999;
}

.fee-value {
  font-size: 32rpx;
  font-weight: bold;
  color: #ff4d4f;
}

.availability {
  font-size: 24rpx;
  color: #52c41a;
}

.availability.no-slot {
  color: #999;
}

.empty {
  text-align: center;
  padding: 100rpx 0;
  color: #999;
  font-size: 28rpx;
}

.elder-mode-enabled {
  .tab-item, .dept-tag {
    font-size: 28rpx;
    padding: 16rpx 32rpx;
  }
  
  .name {
    font-size: 36rpx;
  }
  
  .specialty {
    font-size: 28rpx;
  }
}
</style>
