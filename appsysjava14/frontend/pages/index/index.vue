<template>
    <view class="index-container">
        <view class="header-section">
            <view class="user-info">
                <view class="avatar">
                    <text>{{ userInitial }}</text>
                </view>
                <view class="user-detail">
                    <text class="user-name">{{ userInfo.realName || userInfo.username }}</text>
                    <text class="user-dept">{{ userInfo.department }}</text>
                </view>
            </view>
        </view>

        <view class="stats-section">
            <view class="stat-card" v-for="stat in stats" :key="stat.key" @click="onStatClick(stat)">
                <view class="stat-icon" :style="{ background: stat.bgColor }">
                    <text>{{ stat.icon }}</text>
                </view>
                <view class="stat-content">
                    <text class="stat-value">{{ stat.value }}</text>
                    <text class="stat-label">{{ stat.label }}</text>
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="section-header">
                <text class="section-title">功能菜单</text>
            </view>
            <view class="menu-grid">
                <view class="menu-item" v-for="menu in menus" :key="menu.path" @click="goTo(menu.path)">
                    <view class="menu-icon" :style="{ background: menu.bgColor }">
                        <text>{{ menu.icon }}</text>
                    </view>
                    <text class="menu-label">{{ menu.label }}</text>
                </view>
            </view>
        </view>

        <view class="recent-section">
            <view class="section-header">
                <text class="section-title">最近记录</text>
                <text class="section-more" @click="goTo('/pages/field-record/list')">查看全部</text>
            </view>
            <view class="record-list">
                <view 
                    class="record-item" 
                    v-for="record in recentRecords" 
                    :key="record.id"
                    @click="viewDetail(record.id)"
                >
                    <view class="record-left">
                        <view class="record-stage">
                            <text>{{ getStageLabel(record.growthStage) }}</text>
                        </view>
                    </view>
                    <view class="record-right">
                        <view class="record-title">{{ record.plotName }}</view>
                        <view class="record-sub">
                            <text>{{ record.cropName }}</text>
                            <text class="separator">|</text>
                            <text>{{ formatDate(record.recordDate) }}</text>
                        </view>
                    </view>
                </view>
                <view class="empty-tip" v-if="recentRecords.length === 0">
                    <text>暂无记录</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            stats: [
                { key: 'today', icon: '📝', value: 0, label: '今日记录', bgColor: '#E8F5E9' },
                { key: 'total', icon: '📊', value: 0, label: '总记录数', bgColor: '#E3F2FD' },
                { key: 'plots', icon: '🌾', value: 0, label: '试验地块', bgColor: '#FFF3E0' },
                { key: 'crops', icon: '🌽', value: 0, label: '作物品种', bgColor: '#FCE4EC' }
            ],
            menus: [
                { path: '/pages/field-record/add', icon: '➕', label: '新增记录', bgColor: '#E8F5E9' },
                { path: '/pages/plot/list', icon: '🗺️', label: '地块管理', bgColor: '#E3F2FD' },
                { path: '/pages/crop/list', icon: '🌾', label: '作物品种', bgColor: '#FFF3E0' },
                { path: '/pages/weather/weather', icon: '☀️', label: '气象数据', bgColor: '#FCE4EC' }
            ],
            recentRecords: []
        }
    },

    computed: {
        userInfo() {
            return this.$store.getters.userInfo || {}
        },
        userInitial() {
            const name = this.userInfo.realName || this.userInfo.username || ''
            return name.charAt(0)
        }
    },

    onShow() {
        this.checkLogin()
        this.loadData()
    },

    methods: {
        checkLogin() {
            if (!this.$store.getters.isLoggedIn) {
                uni.redirectTo({ url: '/pages/login/login' })
            }
        },

        async loadData() {
            try {
                const userId = this.userInfo.userId
                
                const recordCount = await this.$request({
                    url: `/field-records/count/${userId}`,
                    method: 'GET'
                })
                this.stats[1].value = recordCount || 0

                const plots = await this.$request({
                    url: '/plots/active',
                    method: 'GET'
                })
                this.stats[2].value = plots ? plots.length : 0

                const crops = await this.$request({
                    url: '/crops/active',
                    method: 'GET'
                })
                this.stats[3].value = crops ? crops.length : 0

                const records = await this.$request({
                    url: '/field-records/observer/' + userId,
                    method: 'GET'
                })
                this.recentRecords = records ? records.slice(0, 5) : []

                this.stats[0].value = this.getTodayCount(records || [])
            } catch (e) {
                console.error('加载数据失败', e)
            }
        },

        getTodayCount(records) {
            const today = new Date().toDateString()
            return records.filter(r => {
                const date = new Date(r.recordDate).toDateString()
                return date === today
            }).length
        },

        onStatClick(stat) {
            switch (stat.key) {
                case 'today':
                case 'total':
                    uni.switchTab({ url: '/pages/field-record/list' })
                    break
                case 'plots':
                    uni.navigateTo({ url: '/pages/plot/list' })
                    break
                case 'crops':
                    uni.navigateTo({ url: '/pages/crop/list' })
                    break
            }
        },

        goTo(path) {
            uni.navigateTo({ url: path })
        },

        viewDetail(id) {
            uni.navigateTo({ url: '/pages/field-record/detail?id=' + id })
        },

        getStageLabel(stage) {
            const map = {
                'SEEDLING': '出苗期',
                'TILLERING': '分蘖期',
                'JOINTING': '拔节期',
                'BOOTING': '孕穗期',
                'HEADING': '抽穗期',
                'FLOWERING': '开花期',
                'FILLING': '灌浆期',
                'MATURING': '成熟期'
            }
            return map[stage] || stage
        },

        formatDate(dateStr) {
            if (!dateStr) return ''
            const date = new Date(dateStr)
            return `${date.getMonth() + 1}/${date.getDate()}`
        }
    }
}
</script>

<style>
.index-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding-bottom: 40rpx;
}

.header-section {
    background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
    padding: 60rpx 30rpx 80rpx;
    border-radius: 0 0 32rpx 32rpx;
}

.user-info {
    display: flex;
    align-items: center;
}

.avatar {
    width: 100rpx;
    height: 100rpx;
    background: rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
}

.avatar text {
    font-size: 40rpx;
    color: #fff;
    font-weight: bold;
}

.user-detail {
    display: flex;
    flex-direction: column;
}

.user-name {
    font-size: 34rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.user-dept {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
}

.stats-section {
    display: flex;
    padding: 0 20rpx;
    margin-top: -50rpx;
    flex-wrap: wrap;
}

.stat-card {
    width: 50%;
    padding: 0 10rpx;
    margin-bottom: 20rpx;
}

.stat-card:nth-child(2n+1) {
    padding-left: 20rpx;
}

.stat-card:nth-child(2n) {
    padding-right: 20rpx;
}

.stat-card .card {
    margin-bottom: 0;
    display: flex;
    align-items: center;
    padding: 30rpx 24rpx;
}

.stat-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 16rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 20rpx;
}

.stat-icon text {
    font-size: 40rpx;
}

.stat-content {
    display: flex;
    flex-direction: column;
}

.stat-value {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
}

.stat-label {
    font-size: 24rpx;
    color: #999;
    margin-top: 4rpx;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx 30rpx 20rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.section-more {
    font-size: 26rpx;
    color: #4CAF50;
}

.menu-section {
    padding: 0 20rpx;
}

.menu-grid {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 20rpx;
    display: flex;
    flex-wrap: wrap;
}

.menu-item {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 30rpx 0;
}

.menu-icon {
    width: 90rpx;
    height: 90rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16rpx;
}

.menu-icon text {
    font-size: 44rpx;
}

.menu-label {
    font-size: 26rpx;
    color: #333;
}

.recent-section {
    padding: 0 20rpx;
    margin-top: 20rpx;
}

.record-list {
    background-color: #fff;
    border-radius: 16rpx;
    overflow: hidden;
}

.record-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.record-item:last-child {
    border-bottom: none;
}

.record-left {
    margin-right: 24rpx;
}

.record-stage {
    background-color: #E8F5E9;
    padding: 8rpx 20rpx;
    border-radius: 8rpx;
}

.record-stage text {
    font-size: 24rpx;
    color: #4CAF50;
}

.record-right {
    flex: 1;
}

.record-title {
    font-size: 30rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 8rpx;
}

.record-sub {
    display: flex;
    align-items: center;
    font-size: 24rpx;
    color: #999;
}

.separator {
    margin: 0 12rpx;
    color: #ddd;
}

.empty-tip {
    padding: 60rpx 0;
    text-align: center;
    color: #999;
    font-size: 28rpx;
}
</style>
