<template>
    <view class="container">
        <view class="map-container">
            <map 
                id="taskMap"
                class="task-map"
                :latitude="centerLat"
                :longitude="centerLng"
                :scale="15"
                :markers="markers"
                @markertap="onMarkerTap"
            ></map>
        </view>

        <view class="task-panel" v-if="selectedTask">
            <view class="panel-header">
                <view class="panel-plate">{{ selectedTask.plateNumber }}</view>
                <view class="panel-priority" :class="selectedTask.priority">
                    {{ getPriorityText(selectedTask.priority) }}
                </view>
            </view>
            <view class="panel-info">
                <text class="info-label">任务类型</text>
                <text class="info-value">{{ getTypeText(selectedTask.taskType) }}</text>
            </view>
            <view class="panel-info">
                <text class="info-label">出现位置</text>
                <text class="info-value">{{ selectedTask.lastLocation }}</text>
            </view>
            <view class="panel-info">
                <text class="info-label">出现时间</text>
                <text class="info-value">{{ formatTime(selectedTask.lastSeenTime) }}</text>
            </view>
            <view class="panel-actions">
                <button class="action-btn primary" @click="startCheck">立即查验</button>
                <button class="action-btn secondary" @click="closePanel">关闭</button>
            </view>
        </view>

        <view class="legend">
            <view class="legend-item">
                <view class="legend-dot high"></view>
                <text>高优先级</text>
            </view>
            <view class="legend-item">
                <view class="legend-dot medium"></view>
                <text>中优先级</text>
            </view>
            <view class="legend-item">
                <view class="legend-dot low"></view>
                <text>低优先级</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            centerLat: 39.9042,
            centerLng: 116.4074,
            markers: [],
            selectedTask: null,
            tasks: [
                {
                    id: 1,
                    plateNumber: '京C66666',
                    taskType: 'SUSPICIOUS_VEHICLE',
                    description: '该车辆涉嫌套牌',
                    priority: 'HIGH',
                    lastLocation: '北京市海淀区中关村大街',
                    lastLongitude: 116.3198,
                    lastLatitude: 39.9891,
                    lastSeenTime: Date.now() - 7200000
                },
                {
                    id: 2,
                    plateNumber: '京E55555',
                    taskType: 'TRAFFIC_VIOLATION',
                    description: '多次闯红灯',
                    priority: 'MEDIUM',
                    lastLocation: '北京市朝阳区建国路',
                    lastLongitude: 116.4700,
                    lastLatitude: 39.9088,
                    lastSeenTime: Date.now() - 18000000
                },
                {
                    id: 3,
                    plateNumber: '京F22222',
                    taskType: 'SUSPICIOUS_VEHICLE',
                    description: '可疑车辆',
                    priority: 'LOW',
                    lastLocation: '北京市西城区金融街',
                    lastLongitude: 116.3600,
                    lastLatitude: 39.9150,
                    lastSeenTime: Date.now() - 3600000
                }
            ]
        }
    },
    onLoad() {
        this.initMarkers()
    },
    methods: {
        initMarkers() {
            this.markers = this.tasks.map(task => ({
                id: task.id,
                latitude: task.lastLatitude,
                longitude: task.lastLongitude,
                width: 40,
                height: 40,
                callout: {
                    content: task.plateNumber,
                    color: '#ffffff',
                    fontSize: 12,
                    borderRadius: 4,
                    bgColor: this.getMarkerColor(task.priority),
                    padding: 6,
                    display: 'BYCLICK'
                }
            }))
            
            if (this.markers.length > 0) {
                this.centerLat = this.markers[0].latitude
                this.centerLng = this.markers[0].longitude
            }
        },
        getMarkerColor(priority) {
            const colors = {
                'LOW': '#34a853',
                'MEDIUM': '#fbbc05',
                'HIGH': '#ea4335',
                'CRITICAL': '#d93025'
            }
            return colors[priority] || '#1a73e8'
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
            return `${date.getFullYear()}-${(date.getMonth()+1).toString().padStart(2,'0')}-${date.getDate().toString().padStart(2,'0')} ${date.getHours().toString().padStart(2,'0')}:${date.getMinutes().toString().padStart(2,'0')}`
        },
        onMarkerTap(e) {
            const taskId = e.markerId
            this.selectedTask = this.tasks.find(t => t.id === taskId)
        },
        startCheck() {
            if (this.selectedTask) {
                uni.switchTab({
                    url: '/pages/scan/scan'
                })
            }
        },
        closePanel() {
            this.selectedTask = null
        }
    }
}
</script>

<style scoped>
.container {
    position: relative;
    height: 100vh;
}

.map-container {
    width: 100%;
    height: 100vh;
}

.task-map {
    width: 100%;
    height: 100%;
}

.task-panel {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: var(--card-bg);
    border-radius: 24rpx 24rpx 0 0;
    padding: 30rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.panel-plate {
    font-size: 36rpx;
    font-weight: bold;
    color: var(--text-color);
}

.panel-priority {
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    font-weight: bold;
}

.panel-priority.LOW {
    background: #e6f4ea;
    color: #34a853;
}

.panel-priority.MEDIUM {
    background: #fef7e0;
    color: #f57c00;
}

.panel-priority.HIGH {
    background: #fce8e6;
    color: #ea4335;
}

.panel-priority.CRITICAL {
    background: #fce8e6;
    color: #d93025;
}

.panel-info {
    display: flex;
    justify-content: space-between;
    padding: 16rpx 0;
    border-bottom: 1rpx solid var(--border-color);
}

.info-label {
    font-size: 28rpx;
    color: var(--text-secondary);
}

.info-value {
    font-size: 28rpx;
    color: var(--text-color);
}

.panel-actions {
    display: flex;
    gap: 20rpx;
    margin-top: 30rpx;
}

.action-btn {
    flex: 1;
    height: 72rpx;
    line-height: 72rpx;
    border-radius: 36rpx;
    font-size: 28rpx;
    border: none;
}

.action-btn.primary {
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
    color: white;
}

.action-btn.secondary {
    background: var(--bg-color);
    color: var(--text-color);
}

.legend {
    position: fixed;
    top: 20rpx;
    right: 20rpx;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 12rpx;
    padding: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 12rpx;
    margin-bottom: 12rpx;
    font-size: 24rpx;
    color: #333;
}

.legend-item:last-child {
    margin-bottom: 0;
}

.legend-dot {
    width: 16rpx;
    height: 16rpx;
    border-radius: 50%;
}

.legend-dot.high {
    background: #ea4335;
}

.legend-dot.medium {
    background: #fbbc05;
}

.legend-dot.low {
    background: #34a853;
}
</style>
