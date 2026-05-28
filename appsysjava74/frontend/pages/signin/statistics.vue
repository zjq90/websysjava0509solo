<template>
    <view class="container">
        <view class="header" v-if="activity">
            <view class="activity-name">{{ activity.name }}</view>
            <view class="activity-time">
                {{ formatDateTime(activity.startTime) }} - {{ formatTime(activity.endTime) }}
            </view>
        </view>
        
        <view class="stats-overview" v-if="statistics">
            <view class="stats-row">
                <view class="stat-card">
                    <view class="stat-value primary">{{ statistics.registeredCount || 0 }}</view>
                    <view class="stat-label">报名人数</view>
                </view>
                <view class="stat-card">
                    <view class="stat-value success">{{ statistics.signedInCount || 0 }}</view>
                    <view class="stat-label">实到人数</view>
                </view>
                <view class="stat-card">
                    <view class="stat-value warning">{{ statistics.absentCount || 0 }}</view>
                    <view class="stat-label">未签到</view>
                </view>
            </view>
            
            <view class="stats-row single">
                <view class="stat-card large">
                    <view class="stat-value big" :class="getRateClass()">
                        {{ statistics.signInRate || '0.0' }}%
                    </view>
                    <view class="stat-label">签到率</view>
                    <view class="rate-bar">
                        <view class="rate-fill" :style="{ width: (statistics.signInRate || 0) + '%' }"></view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="signin-record">
            <view class="section-title">
                <text>签到记录</text>
                <view class="action-btns">
                    <u-button type="primary" size="mini" @click="exportSignIn">导出签到表</u-button>
                </view>
            </view>
            
            <view class="record-list">
                <view 
                    class="record-item" 
                    v-for="item in list" 
                    :key="item.id"
                >
                    <view class="user-info">
                        <u-avatar 
                            :text="item.studentName?.charAt(0)" 
                            size="80"
                            :bgColor="getAvatarColor(item.id)"
                        ></u-avatar>
                        <view class="user-detail">
                            <view class="user-name">{{ item.studentName }}</view>
                            <view class="user-meta">
                                <text>{{ item.department }}</text>
                                <text class="divider">|</text>
                                <text>{{ item.studentNo }}</text>
                            </view>
                        </view>
                    </view>
                    
                    <view class="signin-info">
                        <u-tag 
                            :type="item.signedIn ? 'success' : 'warning'" 
                            :text="item.signedIn ? '已签到' : '未签到'"
                            size="mini"
                        ></u-tag>
                        <view class="signin-time" v-if="item.signInTime">
                            {{ formatTime(item.signInTime) }}
                        </view>
                        <view class="signin-type" v-if="item.signInType">
                            {{ item.signInType === 'QRCODE' ? '扫码签到' : '手动补签' }}
                        </view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="loading" v-if="loading">
            <u-loading mode="circle"></u-loading>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { activityApi, signInApi } from '@/utils/api'
import dayjs from 'dayjs'

const route = useRoute()
const activityId = computed(() => route.query.activityId || route.params.activityId)

const loading = ref(false)
const activity = ref(null)
const statistics = ref(null)
const list = ref([])

const formatDateTime = (date) => dayjs(date).format('YYYY-MM-DD HH:mm')
const formatTime = (date) => dayjs(date).format('HH:mm')

const getAvatarColor = (id) => {
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#722ed1', '#13c2c2']
    return colors[id % colors.length]
}

const getRateClass = () => {
    const rate = statistics.value?.signInRate || 0
    if (rate >= 90) return 'success'
    if (rate >= 70) return 'warning'
    return 'error'
}

const loadData = async () => {
    loading.value = true
    try {
        const activityRes = await activityApi.getById(activityId.value)
        activity.value = activityRes.data || activityRes
        
        const statsRes = await signInApi.getStatistics(activityId.value)
        statistics.value = statsRes.data || statsRes
        
        const listRes = await signInApi.getSignInList(activityId.value, { page: 1, size: 100 })
        list.value = listRes.content || listRes.data?.content || []
    } catch (e) {
        console.error('加载数据失败:', e)
    } finally {
        loading.value = false
    }
}

const exportSignIn = async () => {
    try {
        await signInApi.exportSignInSheet(activityId.value)
        uni.showToast({ title: '导出成功', icon: 'success' })
    } catch (e) {
        console.error('导出失败:', e)
        uni.showToast({ title: '导出失败', icon: 'none' })
    }
}

onMounted(() => {
    loadData()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 40rpx 30rpx;
    color: #fff;
    
    .activity-name {
        font-size: 36rpx;
        font-weight: bold;
        margin-bottom: 12rpx;
    }
    
    .activity-time {
        font-size: 26rpx;
        opacity: 0.9;
    }
}

.stats-overview {
    padding: 20rpx;
    
    .stats-row {
        display: flex;
        gap: 16rpx;
        margin-bottom: 16rpx;
        
        &.single {
            .stat-card {
                flex: 1;
            }
        }
        
        .stat-card {
            flex: 1;
            background: #fff;
            border-radius: 16rpx;
            padding: 30rpx 20rpx;
            text-align: center;
            box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
            
            &.large {
                padding: 40rpx 30rpx;
            }
            
            .stat-value {
                font-size: 48rpx;
                font-weight: bold;
                margin-bottom: 8rpx;
                
                &.big {
                    font-size: 72rpx;
                }
                
                &.primary { color: #3c9cff; }
                &.success { color: #5ac725; }
                &.warning { color: #f9ae3d; }
                &.error { color: #f56c6c; }
            }
            
            .stat-label {
                font-size: 24rpx;
                color: #999;
                margin-bottom: 16rpx;
            }
            
            .rate-bar {
                width: 100%;
                height: 12rpx;
                background: #f0f0f0;
                border-radius: 6rpx;
                overflow: hidden;
                
                .rate-fill {
                    height: 100%;
                    background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
                    border-radius: 6rpx;
                    transition: width 0.3s ease;
                }
            }
        }
    }
}

.signin-record {
    background: #fff;
    margin: 0 20rpx;
    border-radius: 16rpx;
    padding: 30rpx;
    
    .section-title {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 24rpx;
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
    }
    
    .record-list {
        .record-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 20rpx 0;
            border-bottom: 1rpx solid #f5f5f5;
            
            &:last-child {
                border-bottom: none;
            }
            
            .user-info {
                display: flex;
                align-items: center;
                gap: 16rpx;
                
                .user-detail {
                    .user-name {
                        font-size: 28rpx;
                        color: #333;
                        font-weight: 500;
                        margin-bottom: 4rpx;
                    }
                    
                    .user-meta {
                        font-size: 22rpx;
                        color: #999;
                        display: flex;
                        align-items: center;
                        gap: 8rpx;
                        
                        .divider {
                            color: #e0e0e0;
                        }
                    }
                }
            }
            
            .signin-info {
                display: flex;
                flex-direction: column;
                align-items: flex-end;
                gap: 4rpx;
                
                .signin-time {
                    font-size: 22rpx;
                    color: #5ac725;
                }
                
                .signin-type {
                    font-size: 20rpx;
                    color: #999;
                }
            }
        }
    }
}

.loading {
    display: flex;
    justify-content: center;
    padding: 100rpx 0;
}
</style>
