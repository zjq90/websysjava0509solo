<template>
    <view class="container">
        <view class="header">
            <view class="welcome">
                <text class="title">车辆识别系统</text>
                <text class="subtitle">智能执法 · 高效查验</text>
            </view>
            <view class="status-badge" :class="networkStatus ? 'online' : 'offline'">
                {{ networkStatus ? '在线' : '离线' }}
            </view>
        </view>

        <view class="quick-actions">
            <view class="action-card" @click="goToScan">
                <view class="action-icon scan">
                    <text class="iconfont">📷</text>
                </view>
                <text class="action-text">车牌查验</text>
            </view>
            <view class="action-card" @click="goToEnforcement">
                <view class="action-icon record">
                    <text class="iconfont">📝</text>
                </view>
                <text class="action-text">执法记录</text>
            </view>
            <view class="action-card" @click="goToTask">
                <view class="action-icon task">
                    <text class="iconfont">📋</text>
                </view>
                <text class="action-text">待办任务</text>
                <view class="badge" v-if="pendingCount > 0">{{ pendingCount }}</view>
            </view>
            <view class="action-card" @click="quickAlarm">
                <view class="action-icon alarm">
                    <text class="iconfont">🚨</text>
                </view>
                <text class="action-text">一键报警</text>
            </view>
        </view>

        <view class="card">
            <view class="card-header">
                <text class="card-title">今日统计</text>
            </view>
            <view class="stats-grid">
                <view class="stat-item">
                    <text class="stat-value primary">{{ todayCheck }}</text>
                    <text class="stat-label">查验次数</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value success">{{ legalCount }}</text>
                    <text class="stat-label">合法车辆</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value danger">{{ illegalCount }}</text>
                    <text class="stat-label">违规车辆</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value warning">{{ pendingCount }}</text>
                    <text class="stat-label">待办任务</text>
                </view>
            </view>
        </view>

        <view class="card" v-if="latestRecords.length > 0">
            <view class="card-header">
                <text class="card-title">最近记录</text>
                <text class="card-more" @click="goToEnforcement">查看全部</text>
            </view>
            <view class="record-list">
                <view class="record-item" v-for="(item, index) in latestRecords" :key="index">
                    <view class="record-plate">{{ item.plateNumber }}</view>
                    <view class="record-info">
                        <text class="record-type">{{ item.violationType || '正常查验' }}</text>
                        <text class="record-time">{{ item.time }}</text>
                    </view>
                    <view class="record-status" :class="item.status">{{ item.statusText }}</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            networkStatus: true,
            pendingCount: 0,
            todayCheck: 0,
            legalCount: 0,
            illegalCount: 0,
            latestRecords: []
        }
    },
    onShow() {
        this.checkNetwork()
        this.loadStats()
        this.loadPendingTasks()
    },
    methods: {
        checkNetwork() {
            uni.getNetworkType({
                success: (res) => {
                    this.networkStatus = res.networkType !== 'none'
                }
            })
        },
        loadStats() {
            const records = uni.getStorageSync('enforcementRecords') || []
            const today = new Date().toDateString()
            const todayRecords = records.filter(r => new Date(r.timestamp).toDateString() === today)
            
            this.todayCheck = todayRecords.length
            this.legalCount = todayRecords.filter(r => r.legal).length
            this.illegalCount = todayRecords.filter(r => !r.legal).length
            this.latestRecords = records.slice(0, 3).map(r => ({
                ...r,
                time: this.formatTime(r.timestamp),
                statusText: r.legal ? '正常' : '违规',
                status: r.legal ? 'success' : 'danger'
            }))
        },
        loadPendingTasks() {
            this.$request({
                url: '/task/pending',
                method: 'GET'
            }).then(res => {
                this.pendingCount = res.data.length
            }).catch(() => {
                this.pendingCount = 2
            })
        },
        formatTime(timestamp) {
            const date = new Date(timestamp)
            return `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`
        },
        goToScan() {
            uni.switchTab({
                url: '/pages/scan/scan'
            })
        },
        goToEnforcement() {
            uni.navigateTo({
                url: '/pages/enforcement/enforcement'
            })
        },
        goToTask() {
            uni.switchTab({
                url: '/pages/task/task'
            })
        },
        quickAlarm() {
            uni.showModal({
                title: '一键报警',
                content: '确定要拨打110报警电话吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.makePhoneCall({
                            phoneNumber: '110'
                        })
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    min-height: 100vh;
}

.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 0 40rpx;
}

.welcome .title {
    font-size: 48rpx;
    font-weight: bold;
    color: var(--text-color);
    display: block;
}

.welcome .subtitle {
    font-size: 28rpx;
    color: var(--text-secondary);
    margin-top: 8rpx;
    display: block;
}

.status-badge {
    padding: 8rpx 24rpx;
    border-radius: 30rpx;
    font-size: 24rpx;
}

.status-badge.online {
    background: #e6f4ea;
    color: #34a853;
}

.status-badge.offline {
    background: #fce8e6;
    color: #ea4335;
}

.quick-actions {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 30rpx;
}

.action-card {
    width: 25%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20rpx 0;
    position: relative;
}

.action-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48rpx;
    margin-bottom: 12rpx;
}

.action-icon.scan {
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
}

.action-icon.record {
    background: linear-gradient(135deg, #34a853, #1e7e34);
}

.action-icon.task {
    background: linear-gradient(135deg, #fbbc05, #f57c00);
}

.action-icon.alarm {
    background: linear-gradient(135deg, #ea4335, #c5221f);
}

.action-text {
    font-size: 24rpx;
    color: var(--text-color);
}

.badge {
    position: absolute;
    top: 10rpx;
    right: 20rpx;
    background: #ea4335;
    color: white;
    font-size: 20rpx;
    min-width: 36rpx;
    height: 36rpx;
    line-height: 36rpx;
    text-align: center;
    border-radius: 18rpx;
    padding: 0 8rpx;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24rpx;
}

.card-title {
    font-size: 32rpx;
    font-weight: bold;
    color: var(--text-color);
}

.card-more {
    font-size: 26rpx;
    color: var(--primary-color);
}

.stats-grid {
    display: flex;
    flex-wrap: wrap;
}

.stat-item {
    width: 50%;
    text-align: center;
    padding: 20rpx 0;
}

.stat-value {
    font-size: 48rpx;
    font-weight: bold;
    display: block;
}

.stat-value.primary {
    color: var(--primary-color);
}

.stat-value.success {
    color: var(--success-color);
}

.stat-value.danger {
    color: var(--danger-color);
}

.stat-value.warning {
    color: var(--warning-color);
}

.stat-label {
    font-size: 24rpx;
    color: var(--text-secondary);
    margin-top: 8rpx;
    display: block;
}

.record-list {
    border-top: 1rpx solid var(--border-color);
}

.record-item {
    display: flex;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid var(--border-color);
}

.record-plate {
    font-size: 32rpx;
    font-weight: bold;
    color: var(--text-color);
    width: 160rpx;
}

.record-info {
    flex: 1;
}

.record-type {
    font-size: 28rpx;
    color: var(--text-color);
    display: block;
}

.record-time {
    font-size: 24rpx;
    color: var(--text-secondary);
    margin-top: 4rpx;
    display: block;
}

.record-status {
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
}

.record-status.success {
    background: #e6f4ea;
    color: #34a853;
}

.record-status.danger {
    background: #fce8e6;
    color: #ea4335;
}
</style>
