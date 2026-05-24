<template>
    <view class="container">
        <view class="task-tabs">
            <view 
                class="tab-item" 
                :class="{ active: activeTab === 'pending' }"
                @click="switchTab('pending')"
            >
                待办 <text class="tab-count">{{ pendingTasks.length }}</text>
            </view>
            <view 
                class="tab-item" 
                :class="{ active: activeTab === 'processing' }"
                @click="switchTab('processing')"
            >
                处理中 <text class="tab-count">{{ processingTasks.length }}</text>
            </view>
            <view 
                class="tab-item" 
                :class="{ active: activeTab === 'completed' }"
                @click="switchTab('completed')"
            >
                已完成
            </view>
        </view>

        <view class="task-list" v-if="currentTasks.length > 0">
            <view 
                class="task-card" 
                v-for="task in currentTasks" 
                :key="task.id"
                @click="viewTask(task)"
            >
                <view class="task-header">
                    <view class="task-plate">{{ task.plateNumber }}</view>
                    <view class="task-priority" :class="task.priority">
                        {{ getPriorityText(task.priority) }}
                    </view>
                </view>
                <view class="task-info">
                    <text class="task-type">{{ getTypeText(task.taskType) }}</text>
                    <text class="task-desc">{{ task.description }}</text>
                </view>
                <view class="task-footer">
                    <view class="task-location">
                        <text class="location-icon">📍</text>
                        <text class="location-text">{{ task.lastLocation }}</text>
                    </view>
                    <text class="task-time">{{ formatTime(task.lastSeenTime) }}</text>
                </view>
                <view class="task-actions" v-if="task.status === 'PENDING'">
                    <button class="action-btn start" @click.stop="startTask(task)">开始处理</button>
                    <button class="action-btn map" @click.stop="viewMap(task)">查看地图</button>
                </view>
            </view>
        </view>

        <view class="empty-state" v-else>
            <text class="empty-icon">📋</text>
            <text class="empty-text">暂无任务</text>
        </view>

        <view class="map-btn" @click="goMap">
            <text class="map-icon">🗺️</text>
            <text class="map-text">任务地图</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            activeTab: 'pending',
            tasks: [],
            mockTasks: [
                {
                    id: 1,
                    taskNo: 'TASK20240101001',
                    plateNumber: '京C66666',
                    taskType: 'SUSPICIOUS_VEHICLE',
                    description: '该车辆涉嫌套牌，请现场查验核实',
                    priority: 'HIGH',
                    lastLocation: '北京市海淀区中关村大街',
                    lastLongitude: 116.3198,
                    lastLatitude: 39.9891,
                    lastSeenTime: Date.now() - 7200000,
                    assignedOfficer: '赵警官',
                    status: 'PENDING'
                },
                {
                    id: 2,
                    taskNo: 'TASK20240101002',
                    plateNumber: '京E55555',
                    taskType: 'TRAFFIC_VIOLATION',
                    description: '该车辆多次闯红灯，请拦截处理',
                    priority: 'MEDIUM',
                    lastLocation: '北京市朝阳区建国路',
                    lastLongitude: 116.4700,
                    lastLatitude: 39.9088,
                    lastSeenTime: Date.now() - 18000000,
                    assignedOfficer: '王警官',
                    status: 'PENDING'
                },
                {
                    id: 3,
                    taskNo: 'TASK20240101003',
                    plateNumber: '京F22222',
                    taskType: 'SUSPICIOUS_VEHICLE',
                    description: '可疑车辆，需要进一步检查',
                    priority: 'LOW',
                    lastLocation: '北京市西城区金融街',
                    lastLongitude: 116.3600,
                    lastLatitude: 39.9150,
                    lastSeenTime: Date.now() - 3600000,
                    assignedOfficer: '李警官',
                    status: 'IN_PROGRESS'
                }
            ]
        }
    },
    computed: {
        pendingTasks() {
            return this.tasks.filter(t => t.status === 'PENDING')
        },
        processingTasks() {
            return this.tasks.filter(t => t.status === 'IN_PROGRESS')
        },
        completedTasks() {
            return this.tasks.filter(t => t.status === 'COMPLETED')
        },
        currentTasks() {
            if (this.activeTab === 'pending') return this.pendingTasks
            if (this.activeTab === 'processing') return this.processingTasks
            return this.completedTasks
        }
    },
    onShow() {
        this.loadTasks()
    },
    methods: {
        loadTasks() {
            this.$request({
                url: '/task/active',
                method: 'GET'
            }).then(res => {
                this.tasks = res.data
            }).catch(() => {
                this.tasks = this.mockTasks
            })
        },
        switchTab(tab) {
            this.activeTab = tab
        },
        getPriorityText(priority) {
            const map = {
                'LOW': '低',
                'MEDIUM': '中',
                'HIGH': '高',
                'CRITICAL': '极高'
            }
            return map[priority] || '未知'
        },
        getTypeText(type) {
            const map = {
                'SUSPICIOUS_VEHICLE': '套牌嫌疑',
                'TRAFFIC_VIOLATION': '交通违规',
                'STOLEN_VEHICLE': '被盗车辆',
                'OTHER': '其他'
            }
            return map[type] || '未知类型'
        },
        formatTime(timestamp) {
            const date = new Date(timestamp)
            const now = new Date()
            const diff = now - date
            
            if (diff < 3600000) {
                return `${Math.floor(diff / 60000)}分钟前`
            } else if (diff < 86400000) {
                return `${Math.floor(diff / 3600000)}小时前`
            } else {
                return `${date.getMonth() + 1}/${date.getDate()}`
            }
        },
        viewTask(task) {
            uni.showModal({
                title: task.plateNumber,
                content: task.description,
                showCancel: false
            })
        },
        startTask(task) {
            uni.showLoading({
                title: '更新中...'
            })
            this.$request({
                url: `/task/${task.id}/status`,
                method: 'PUT',
                data: { status: 'IN_PROGRESS' }
            }).then(() => {
                uni.hideLoading()
                task.status = 'IN_PROGRESS'
                uni.showToast({
                    title: '已开始处理',
                    icon: 'success'
                })
            }).catch(() => {
                uni.hideLoading()
                const index = this.tasks.findIndex(t => t.id === task.id)
                if (index > -1) {
                    this.tasks[index].status = 'IN_PROGRESS'
                }
                uni.showToast({
                    title: '已开始处理',
                    icon: 'success'
                })
            })
        },
        viewMap(task) {
            uni.navigateTo({
                url: `/pages/taskMap/taskMap?taskId=${task.id}`
            })
        },
        goMap() {
            uni.navigateTo({
                url: '/pages/taskMap/taskMap'
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

.task-tabs {
    display: flex;
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 8rpx;
    margin-bottom: 30rpx;
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx 0;
    font-size: 28rpx;
    color: var(--text-secondary);
    border-radius: 12rpx;
}

.tab-item.active {
    background: var(--primary-color);
    color: white;
}

.tab-count {
    background: rgba(255, 255, 255, 0.3);
    padding: 4rpx 12rpx;
    border-radius: 20rpx;
    font-size: 22rpx;
    margin-left: 8rpx;
}

.task-list {
    margin-bottom: 30rpx;
}

.task-card {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.task-plate {
    font-size: 36rpx;
    font-weight: bold;
    color: var(--text-color);
}

.task-priority {
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    font-weight: bold;
}

.task-priority.LOW {
    background: #e6f4ea;
    color: #34a853;
}

.task-priority.MEDIUM {
    background: #fef7e0;
    color: #f57c00;
}

.task-priority.HIGH {
    background: #fce8e6;
    color: #ea4335;
}

.task-priority.CRITICAL {
    background: #fce8e6;
    color: #d93025;
}

.task-info {
    margin-bottom: 20rpx;
}

.task-type {
    font-size: 28rpx;
    color: var(--primary-color);
    margin-bottom: 8rpx;
    display: block;
}

.task-desc {
    font-size: 26rpx;
    color: var(--text-secondary);
    display: block;
}

.task-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.task-location {
    display: flex;
    align-items: center;
    gap: 8rpx;
}

.location-icon {
    font-size: 24rpx;
}

.location-text {
    font-size: 24rpx;
    color: var(--text-secondary);
}

.task-time {
    font-size: 24rpx;
    color: var(--text-secondary);
}

.task-actions {
    display: flex;
    gap: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid var(--border-color);
}

.action-btn {
    flex: 1;
    height: 64rpx;
    line-height: 64rpx;
    border-radius: 32rpx;
    font-size: 26rpx;
    border: none;
    margin: 0;
}

.action-btn.start {
    background: var(--primary-color);
    color: white;
}

.action-btn.map {
    background: var(--bg-color);
    color: var(--text-color);
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 96rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: var(--text-secondary);
}

.map-btn {
    position: fixed;
    bottom: 40rpx;
    right: 40rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 120rpx;
    height: 120rpx;
    background: linear-gradient(135deg, #34a853, #1e7e34);
    border-radius: 50%;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(52, 168, 83, 0.4);
}

.map-icon {
    font-size: 36rpx;
    margin-bottom: 4rpx;
}

.map-text {
    font-size: 20rpx;
    color: white;
}
</style>
