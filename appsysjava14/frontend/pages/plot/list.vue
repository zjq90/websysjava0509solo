<template>
    <view class="plot-container">
        <view class="search-section">
            <view class="search-box">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    v-model="keyword" 
                    placeholder="搜索地块名称/编码"
                    placeholder-class="placeholder"
                    @confirm="loadPlots"
                />
            </view>
        </view>

        <view class="plot-list">
            <view 
                class="plot-card" 
                v-for="plot in plots" 
                :key="plot.id"
                @click="goToRecords(plot.id)"
            >
                <view class="card-header">
                    <view class="plot-name">
                        <text class="name-icon">📍</text>
                        <text class="name-text">{{ plot.plotName }}</text>
                    </view>
                    <view class="plot-area">
                        {{ plot.area || '-' }} 亩
                    </view>
                </view>

                <view class="card-body">
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">地块编码</text>
                            <text class="info-value">{{ plot.plotCode }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">土壤类型</text>
                            <text class="info-value">{{ plot.soilType || '-' }}</text>
                        </view>
                    </view>

                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">纬度</text>
                            <text class="info-value">{{ plot.latitude || '-' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">经度</text>
                            <text class="info-value">{{ plot.longitude || '-' }}</text>
                        </view>
                    </view>

                    <view class="info-row" v-if="plot.location">
                        <view class="info-item full">
                            <text class="info-label">详细地址</text>
                            <text class="info-value">{{ plot.location }}</text>
                        </view>
                    </view>
                </view>

                <view class="card-footer">
                    <view class="footer-left">
                        <text class="status-dot" :class="'status-' + plot.status"></text>
                        <text class="status-text">{{ getStatusLabel(plot.status) }}</text>
                    </view>
                    <text class="record-count">
                        记录: {{ plot.recordCount || 0 }}条
                    </text>
                </view>

                <view class="view-records" @click.stop="goToRecords(plot.id)">
                    <text>查看记录</text>
                    <text class="arrow">›</text>
                </view>
            </view>

            <view class="empty-tip" v-if="plots.length === 0">
                <text class="empty-icon">🗺️</text>
                <text>暂无地块数据</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            keyword: '',
            plots: [],
            allPlots: []
        }
    },

    onShow() {
        this.loadPlots()
    },

    methods: {
        async loadPlots() {
            try {
                const data = await this.$request({
                    url: '/plots',
                    method: 'GET'
                })
                this.allPlots = data || []
                this.filterPlots()
            } catch (e) {
                console.error('加载地块失败', e)
            }
        },

        filterPlots() {
            if (!this.keyword) {
                this.plots = this.allPlots
                return
            }
            const kw = this.keyword.toLowerCase()
            this.plots = this.allPlots.filter(p => 
                (p.plotName && p.plotName.toLowerCase().includes(kw)) ||
                (p.plotCode && p.plotCode.toLowerCase().includes(kw))
            )
        },

        getStatusLabel(status) {
            const map = {
                'ACTIVE': '正常使用',
                'IDLE': '空闲',
                'MAINTENANCE': '维护中',
                'INACTIVE': '已停用'
            }
            return map[status] || status || '-'
        },

        goToRecords(plotId) {
            uni.navigateTo({
                url: '/pages/field-record/list?plotId=' + plotId
            })
        }
    }
}
</script>

<style>
.plot-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 20rpx;
    padding-bottom: 40rpx;
}

.search-section {
    margin-bottom: 20rpx;
}

.search-box {
    background-color: #fff;
    border-radius: 40rpx;
    padding: 0 24rpx;
    display: flex;
    align-items: center;
    height: 80rpx;
}

.search-icon {
    font-size: 32rpx;
    margin-right: 12rpx;
}

.search-input {
    flex: 1;
    font-size: 28rpx;
    height: 100%;
}

.placeholder {
    color: #999;
}

.plot-list {
    margin-top: 10rpx;
}

.plot-card {
    background-color: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    padding: 24rpx;
    position: relative;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    padding-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.plot-name {
    display: flex;
    align-items: center;
}

.name-icon {
    font-size: 32rpx;
    margin-right: 10rpx;
}

.name-text {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.plot-area {
    background-color: #E8F5E9;
    color: #4CAF50;
    padding: 6rpx 16rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    font-weight: 500;
}

.card-body {
    padding: 0 10rpx;
}

.info-row {
    display: flex;
    margin-bottom: 16rpx;
}

.info-item {
    width: 50%;
}

.info-item.full {
    width: 100%;
}

.info-label {
    font-size: 24rpx;
    color: #999;
    display: block;
    margin-bottom: 6rpx;
}

.info-value {
    font-size: 28rpx;
    color: #333;
}

.card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid #f0f0f0;
}

.footer-left {
    display: flex;
    align-items: center;
}

.status-dot {
    width: 16rpx;
    height: 16rpx;
    border-radius: 50%;
    margin-right: 8rpx;
}

.status-ACTIVE { background-color: #4CAF50; }
.status-IDLE { background-color: #FFC107; }
.status-MAINTENANCE { background-color: #FF9800; }
.status-INACTIVE { background-color: #999; }

.status-text {
    font-size: 24rpx;
    color: #666;
}

.record-count {
    font-size: 24rpx;
    color: #999;
}

.view-records {
    position: absolute;
    right: 24rpx;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    align-items: center;
    background-color: #f5f5f5;
    padding: 10rpx 20rpx;
    border-radius: 30rpx;
}

.view-records text {
    font-size: 26rpx;
    color: #4CAF50;
}

.arrow {
    margin-left: 4rpx;
}

.empty-tip {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 120rpx 0;
}

.empty-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
}

.empty-tip text {
    font-size: 28rpx;
    color: #999;
}
</style>
