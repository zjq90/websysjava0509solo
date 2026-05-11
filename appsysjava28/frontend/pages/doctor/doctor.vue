<template>
    <view class="doctor-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="search-section">
            <view class="search-box">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    placeholder="搜索医生姓名、专长..." 
                    v-model="searchKeyword"
                    @confirm="handleSearch"
                />
            </view>
        </view>

        <view class="content">
            <view class="filter-section">
                <view 
                    class="filter-item" 
                    :class="{ active: activeFilter === 'all' }"
                    @click="setFilter('all')"
                >
                    全部
                </view>
                <view 
                    class="filter-item" 
                    :class="{ active: activeFilter === 'rating' }"
                    @click="setFilter('rating')"
                >
                    好评优先
                </view>
                <view 
                    class="filter-item" 
                    :class="{ active: activeFilter === 'hot' }"
                    @click="setFilter('hot')"
                >
                    热门医生
                </view>
            </view>

            <view 
                class="doctor-item card" 
                v-for="doctor in doctorList" 
                :key="doctor.id"
                @click="goToDetail(doctor.id)"
            >
                <view class="doctor-avatar">
                    <text>👨‍⚕️</text>
                </view>
                <view class="doctor-info">
                    <view class="doctor-header">
                        <text class="doctor-name">{{ doctor.realName }}</text>
                        <text class="doctor-title">{{ doctor.title }}</text>
                    </view>
                    <text class="doctor-department">{{ doctor.departmentName }}</text>
                    <view class="doctor-stats">
                        <view class="stat-item">
                            <text class="star">⭐</text>
                            <text class="stat-value">{{ doctor.rating }}</text>
                        </view>
                        <view class="stat-item">
                            <text class="stat-label">评价</text>
                            <text class="stat-value">{{ doctor.reviewCount }}</text>
                        </view>
                        <view class="stat-item">
                            <text class="stat-label">就诊</text>
                            <text class="stat-value">{{ doctor.visitCount }}</text>
                        </view>
                    </view>
                    <text class="doctor-specialty">专长：{{ doctor.specialty }}</text>
                </view>
                <view class="doctor-fee">
                    <text class="fee-value">¥{{ doctor.consultationFee }}</text>
                </view>
            </view>

            <view class="empty-state" v-if="doctorList.length === 0 && !loading">
                <text class="empty-icon">👨‍⚕️</text>
                <text class="empty-text">暂无医生数据</text>
            </view>
        </view>
    </view>
</template>

<script>
import { getDoctorsByDepartment } from '@/api/department'
import { getAllDoctors, searchDoctors } from '@/api/doctor'

export default {
    data() {
        return {
            searchKeyword: '',
            doctorList: [],
            loading: false,
            activeFilter: 'all',
            deptId: null,
            deptName: '',
            elderlyMode: false
        }
    },
    onLoad(options) {
        this.deptId = options.deptId
        this.deptName = options.deptName || ''
        if (this.deptName) {
            uni.setNavigationBarTitle({ title: this.deptName })
        }
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadDoctors()
    },
    methods: {
        async loadDoctors() {
            this.loading = true
            try {
                let res
                if (this.deptId) {
                    res = await getDoctorsByDepartment(this.deptId)
                } else {
                    res = await getAllDoctors()
                }
                this.doctorList = res.data.data || []
                this.applyFilter()
            } catch (e) {
                console.error(e)
            } finally {
                this.loading = false
            }
        },
        async handleSearch() {
            if (!this.searchKeyword.trim()) {
                this.loadDoctors()
                return
            }
            try {
                const res = await searchDoctors(this.searchKeyword)
                this.doctorList = res.data.data || []
            } catch (e) {
                console.error(e)
            }
        },
        setFilter(filter) {
            this.activeFilter = filter
            this.applyFilter()
        },
        applyFilter() {
            if (this.activeFilter === 'rating') {
                this.doctorList.sort((a, b) => parseFloat(b.rating) - parseFloat(a.rating))
            } else if (this.activeFilter === 'hot') {
                this.doctorList.sort((a, b) => (b.visitCount || 0) - (a.visitCount || 0))
            }
        },
        goToDetail(id) {
            uni.navigateTo({ url: '/pages/doctor-detail/doctor-detail?id=' + id })
        }
    }
}
</script>

<style scoped>
.doctor-page {
    min-height: 100vh;
    background-color: #f5f7fa;
}

.search-section {
    background: #ffffff;
    padding: 20rpx 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.search-box {
    display: flex;
    align-items: center;
    background: #f5f7fa;
    border-radius: 40rpx;
    padding: 20rpx 30rpx;
}

.search-icon {
    font-size: 30rpx;
    margin-right: 15rpx;
}

.search-input {
    flex: 1;
    font-size: 28rpx;
}

.content {
    padding: 20rpx 30rpx;
}

.filter-section {
    display: flex;
    gap: 20rpx;
    margin-bottom: 20rpx;
}

.filter-item {
    padding: 15rpx 30rpx;
    background: #ffffff;
    border-radius: 40rpx;
    font-size: 26rpx;
    color: #666666;
}

.filter-item.active {
    background: #1677ff;
    color: #ffffff;
}

.doctor-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.doctor-avatar {
    width: 120rpx;
    height: 120rpx;
    background: #e6f4ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60rpx;
    margin-right: 25rpx;
    flex-shrink: 0;
}

.doctor-info {
    flex: 1;
    min-width: 0;
}

.doctor-header {
    display: flex;
    align-items: center;
    margin-bottom: 8rpx;
}

.doctor-name {
    font-size: 32rpx;
    font-weight: 500;
    margin-right: 15rpx;
}

.doctor-title {
    font-size: 24rpx;
    color: #1677ff;
    background: #e6f4ff;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
}

.doctor-department {
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 10rpx;
}

.doctor-stats {
    display: flex;
    gap: 25rpx;
    margin-bottom: 10rpx;
}

.stat-item {
    display: flex;
    align-items: center;
    font-size: 24rpx;
}

.star {
    margin-right: 5rpx;
}

.stat-label {
    color: #999999;
    margin-right: 5rpx;
}

.stat-value {
    color: #333333;
}

.doctor-specialty {
    font-size: 24rpx;
    color: #999999;
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.doctor-fee {
    margin-left: 20rpx;
    flex-shrink: 0;
}

.fee-value {
    font-size: 30rpx;
    color: #ff4d4f;
    font-weight: 500;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999999;
}

.elderly-mode .doctor-name {
    font-size: 38rpx;
}

.elderly-mode .doctor-title {
    font-size: 28rpx;
}

.elderly-mode .doctor-specialty {
    font-size: 28rpx;
}
</style>
