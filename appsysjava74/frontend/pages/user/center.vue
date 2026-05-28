<template>
    <view class="container">
        <view class="user-header">
            <view class="user-avatar">
                <u-avatar 
                    :text="userInfo?.realName || '用户'" 
                    size="120"
                    :bgColor="avatarColor"
                ></u-avatar>
            </view>
            <view class="user-info">
                <view class="user-name">{{ userInfo?.realName || '未登录' }}</view>
                <view class="user-dept">{{ userInfo?.department }} {{ userInfo?.major }}</view>
                <view class="user-role">
                    <u-tag 
                        :type="roleType" 
                        :text="roleText"
                        size="mini"
                    ></u-tag>
                </view>
            </view>
        </view>
        
        <view class="stats-section">
            <view class="stat-item" @click="goToMyRegistrations">
                <text class="stat-num">{{ stats.registered }}</text>
                <text class="stat-label">已报名</text>
            </view>
            <view class="stat-item">
                <text class="stat-num">{{ stats.signedIn }}</text>
                <text class="stat-label">已签到</text>
            </view>
            <view class="stat-item">
                <text class="stat-num">{{ stats.rated }}</text>
                <text class="stat-label">已评价</text>
            </view>
        </view>
        
        <view class="menu-section">
            <u-cell-group :border="false">
                <u-cell-item 
                    title="我的报名" 
                    :arrow="true"
                    @click="goToMyRegistrations"
                >
                    <template #icon>
                        <u-icon name="calendar" size="36" color="#3c9cff"></u-icon>
                    </template>
                </u-cell-item>
                
                <u-cell-item 
                    title="扫码签到" 
                    :arrow="true"
                    @click="goToScan"
                    v-if="store.getters.isStudent"
                >
                    <template #icon>
                        <u-icon name="scan" size="36" color="#5ac725"></u-icon>
                    </template>
                </u-cell-item>
                
                <u-cell-item 
                    title="发布活动" 
                    :arrow="true"
                    @click="goToPublish"
                    v-if="store.getters.isClubAdmin || store.getters.isAdmin"
                >
                    <template #icon>
                        <u-icon name="plus-circle" size="36" color="#3c9cff"></u-icon>
                    </template>
                </u-cell-item>
                
                <u-cell-item 
                    title="活动总结" 
                    :arrow="true"
                    @click="goToPublishSummary"
                    v-if="store.getters.isClubAdmin || store.getters.isAdmin"
                >
                    <template #icon>
                        <u-icon name="edit-pen" size="36" color="#f9ae3d"></u-icon>
                    </template>
                </u-cell-item>
                
                <u-cell-item 
                    title="历史活动" 
                    :arrow="true"
                    @click="goToArchived"
                >
                    <template #icon>
                        <u-icon name="clock" size="36" color="#909399"></u-icon>
                    </template>
                </u-cell-item>
            </u-cell-group>
        </view>
        
        <view class="menu-section">
            <u-cell-group :border="false">
                <u-cell-item 
                    title="设置" 
                    :arrow="true"
                >
                    <template #icon>
                        <u-icon name="setting" size="36" color="#722ed1"></u-icon>
                    </template>
                </u-cell-item>
                
                <u-cell-item 
                    title="关于我们" 
                    :arrow="true"
                >
                    <template #icon>
                        <u-icon name="info-circle" size="36" color="#13c2c2"></u-icon>
                    </template>
                </u-cell-item>
            </u-cell-group>
        </view>
        
        <view class="logout-section">
            <u-button type="error" size="large" @click="handleLogout">退出登录</u-button>
        </view>
    </view>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { registrationApi } from '@/utils/api'

const store = useStore()
const userInfo = computed(() => store.state.userInfo)

const stats = reactive({
    registered: 0,
    signedIn: 0,
    rated: 0
})

const avatarColor = computed(() => {
    const name = userInfo.value?.realName || 'U'
    const colors = ['#3c9cff', '#5ac725', '#f9ae3d', '#f56c6c', '#722ed1', '#13c2c2']
    const index = name.charCodeAt(0) % colors.length
    return colors[index]
})

const roleMap = {
    ADMIN: { text: '系统管理员', type: 'primary' },
    CLUB_ADMIN: { text: '社团管理员', type: 'success' },
    STUDENT: { text: '学生', type: 'warning' }
}

const roleText = computed(() => roleMap[userInfo.value?.role]?.text || '未知')
const roleType = computed(() => roleMap[userInfo.value?.role]?.type || 'info')

const loadStats = async () => {
    try {
        const res = await registrationApi.getMyList({ page: 1, size: 100 })
        const list = res.content || res.data?.content || []
        stats.registered = list.length
        stats.signedIn = list.filter(r => r.signedIn).length
        stats.rated = list.filter(r => r.status === 'APPROVED').length / 2
    } catch (e) {
        console.error('加载统计数据失败:', e)
    }
}

const goToMyRegistrations = () => {
    uni.navigateTo({
        url: '/pages/user/my-registrations'
    })
}

const goToScan = () => {
    uni.navigateTo({
        url: '/pages/scan/scan'
    })
}

const goToPublish = () => {
    uni.navigateTo({
        url: '/pages/activity/publish'
    })
}

const goToPublishSummary = () => {
    uni.navigateTo({
        url: '/pages/summary/publish'
    })
}

const goToArchived = () => {
    uni.switchTab({
        url: '/pages/activity/archived'
    })
}

const handleLogout = () => {
    uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        success: (res) => {
            if (res.confirm) {
                store.dispatch('logout')
            }
        }
    })
}

onMounted(() => {
    loadStats()
})
</script>

<style lang="scss" scoped>
.container {
    min-height: 100vh;
    background: #f5f5f5;
    padding-bottom: 40rpx;
}

.user-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 60rpx 40rpx;
    display: flex;
    align-items: center;
    
    .user-avatar {
        margin-right: 24rpx;
    }
    
    .user-info {
        flex: 1;
        
        .user-name {
            font-size: 36rpx;
            font-weight: bold;
            color: #fff;
            margin-bottom: 8rpx;
        }
        
        .user-dept {
            font-size: 24rpx;
            color: rgba(255, 255, 255, 0.8);
            margin-bottom: 12rpx;
        }
    }
}

.stats-section {
    display: flex;
    background: #fff;
    margin: -30rpx 20rpx 20rpx;
    border-radius: 16rpx;
    padding: 30rpx 0;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
    
    .stat-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        
        .stat-num {
            font-size: 36rpx;
            font-weight: bold;
            color: #333;
        }
        
        .stat-label {
            font-size: 24rpx;
            color: #999;
            margin-top: 4rpx;
        }
    }
}

.menu-section {
    background: #fff;
    margin: 0 20rpx 20rpx;
    border-radius: 16rpx;
    overflow: hidden;
}

.logout-section {
    padding: 40rpx;
}
</style>
