<template>
    <view class="detail-container">
        <view class="detail-header">
            <view class="record-info">
                <view class="record-no-section">
                    <text class="no-label">记录编号</text>
                    <text class="no-value">{{ record.recordNo }}</text>
                </view>
                <view class="record-status" :class="'status-' + record.status">
                    {{ getStatusLabel(record.status) }}
                </view>
            </view>
            <view class="data-source-badge" v-if="record.dataSource === 'OFFLINE'">
                离线同步
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">
                <text class="title-icon">📍</text>
                <text class="title-text">地块信息</text>
            </view>
            <view class="info-grid">
                <view class="info-item">
                    <text class="info-label">地块名称</text>
                    <text class="info-value">{{ record.plotName || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">地块编码</text>
                    <text class="info-value">{{ record.plotCode || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">纬度</text>
                    <text class="info-value">{{ record.latitude || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">经度</text>
                    <text class="info-value">{{ record.longitude || '-' }}</text>
                </view>
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">
                <text class="title-icon">🌾</text>
                <text class="title-text">作物信息</text>
            </view>
            <view class="info-grid">
                <view class="info-item">
                    <text class="info-label">作物品种</text>
                    <text class="info-value">{{ record.cropName || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">批次编号</text>
                    <text class="info-value">{{ record.batchCode || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">生长阶段</text>
                    <text class="info-value">{{ getStageLabel(record.growthStage) }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">使用模板</text>
                    <text class="info-value">{{ record.templateName || '自定义' }}</text>
                </view>
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">
                <text class="title-icon">📊</text>
                <text class="title-text">观测数据</text>
            </view>
            <view class="info-grid">
                <view class="info-item">
                    <text class="info-label">株高</text>
                    <text class="info-value text-primary">{{ record.plantHeight || '-' }} cm</text>
                </view>
                <view class="info-item">
                    <text class="info-label">出苗率</text>
                    <text class="info-value text-primary">{{ record.emergenceRate || '-' }}%</text>
                </view>
                <view class="info-item">
                    <text class="info-label">病虫害等级</text>
                    <text class="info-value" :class="'pest-level-' + record.pestLevel">
                        {{ getPestLevelLabel(record.pestLevel) }}
                    </text>
                </view>
                <view class="info-item">
                    <text class="info-label">叶色</text>
                    <text class="info-value">{{ record.leafColor || '-' }}</text>
                </view>
                <view class="info-item" v-if="record.temperature">
                    <text class="info-label">气温</text>
                    <text class="info-value">{{ record.temperature }}°C</text>
                </view>
                <view class="info-item" v-if="record.humidity">
                    <text class="info-label">湿度</text>
                    <text class="info-value">{{ record.humidity }}%</text>
                </view>
            </view>
        </view>

        <view class="section-card" v-if="record.remark">
            <view class="section-title">
                <text class="title-icon">📝</text>
                <text class="title-text">备注</text>
            </view>
            <view class="remark-content">
                {{ record.remark }}
            </view>
        </view>

        <view class="section-card" v-if="images.length > 0">
            <view class="section-title">
                <text class="title-icon">📷</text>
                <text class="title-text">病虫害图片</text>
                <text class="title-count">（{{ images.length }}张）</text>
            </view>
            <view class="image-grid">
                <view 
                    class="image-item" 
                    v-for="(img, index) in images" 
                    :key="img.id || index"
                    @click="previewImage(index)"
                >
                    <image 
                        :src="img.imageUrl || '/static/placeholder.png'" 
                        mode="aspectFill" 
                        class="thumb-image"
                    />
                    <view class="image-type" v-if="img.pestType">
                        {{ img.pestType }}
                    </view>
                </view>
            </view>
        </view>

        <view class="section-card">
            <view class="section-title">
                <text class="title-icon">👤</text>
                <text class="title-text">操作信息</text>
            </view>
            <view class="info-grid">
                <view class="info-item">
                    <text class="info-label">观测人</text>
                    <text class="info-value">{{ record.observer || '-' }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">记录日期</text>
                    <text class="info-value">{{ formatDate(record.recordDate) }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">创建时间</text>
                    <text class="info-value">{{ formatDateTime(record.createdTime) }}</text>
                </view>
                <view class="info-item">
                    <text class="info-label">更新时间</text>
                    <text class="info-value">{{ formatDateTime(record.updatedTime) }}</text>
                </view>
            </view>
        </view>

        <view class="action-section">
            <view class="action-btn btn-secondary" @click="goBack">
                返回列表
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            recordId: null,
            record: {},
            images: []
        }
    },

    onLoad(options) {
        this.recordId = options.id
        this.loadDetail()
    },

    methods: {
        async loadDetail() {
            try {
                const data = await this.$request({
                    url: '/field-records/' + this.recordId,
                    method: 'GET'
                })
                this.record = data || {}
                this.loadImages()
            } catch (e) {
                console.error('加载详情失败', e)
                uni.showToast({ title: '加载失败', icon: 'none' })
            }
        },

        async loadImages() {
            try {
                const data = await this.$request({
                    url: '/pest-images/record/' + this.recordId,
                    method: 'GET'
                })
                this.images = data || []
            } catch (e) {
                console.error('加载图片失败', e)
            }
        },

        previewImage(index) {
            const urls = this.images.map(img => img.imageUrl)
            uni.previewImage({
                current: index,
                urls: urls
            })
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
            return map[stage] || stage || '-'
        },

        getPestLevelLabel(level) {
            if (level === null || level === undefined) return '-'
            const labels = ['无', '轻度', '中度', '重度', '严重']
            return labels[level] || '-'
        },

        getStatusLabel(status) {
            const map = {
                'DRAFT': '草稿',
                'SUBMITTED': '已提交',
                'REVIEWED': '已审核'
            }
            return map[status] || status || '-'
        },

        formatDate(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
        },

        formatDateTime(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
        },

        goBack() {
            uni.navigateBack()
        }
    }
}
</script>

<style>
.detail-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 20rpx;
    padding-bottom: 180rpx;
}

.detail-header {
    background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    position: relative;
}

.record-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.record-no-section {
    display: flex;
    flex-direction: column;
}

.no-label {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.8);
}

.no-value {
    font-size: 36rpx;
    color: #fff;
    font-weight: bold;
    margin-top: 6rpx;
}

.record-status {
    padding: 8rpx 24rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    background-color: rgba(255, 255, 255, 0.2);
    color: #fff;
}

.status-DRAFT {
    background-color: rgba(255, 152, 0, 0.9);
}

.status-SUBMITTED {
    background-color: rgba(33, 150, 243, 0.9);
}

.status-REVIEWED {
    background-color: rgba(76, 175, 80, 0.9);
}

.data-source-badge {
    position: absolute;
    top: 20rpx;
    right: 20rpx;
    background-color: #FFE0B2;
    color: #E65100;
    font-size: 20rpx;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
}

.section-card {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
}

.section-title {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    padding-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.title-icon {
    font-size: 32rpx;
    margin-right: 12rpx;
}

.title-text {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
}

.title-count {
    font-size: 24rpx;
    color: #999;
    margin-left: 8rpx;
}

.info-grid {
    display: flex;
    flex-wrap: wrap;
}

.info-item {
    width: 50%;
    margin-bottom: 20rpx;
}

.info-label {
    font-size: 24rpx;
    color: #999;
    display: block;
    margin-bottom: 8rpx;
}

.info-value {
    font-size: 28rpx;
    color: #333;
}

.pest-level-0 { color: #4CAF50; }
.pest-level-1 { color: #8BC34A; }
.pest-level-2 { color: #FFC107; }
.pest-level-3 { color: #FF9800; }
.pest-level-4 { color: #f44336; }

.remark-content {
    font-size: 28rpx;
    color: #666;
    line-height: 1.8;
    background-color: #f9f9f9;
    padding: 20rpx;
    border-radius: 8rpx;
}

.image-grid {
    display: flex;
    flex-wrap: wrap;
    margin: -10rpx;
}

.image-item {
    width: calc(33.33% - 20rpx);
    margin: 10rpx;
    position: relative;
    border-radius: 8rpx;
    overflow: hidden;
}

.thumb-image {
    width: 100%;
    height: 200rpx;
    border-radius: 8rpx;
}

.image-type {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.6);
    color: #fff;
    font-size: 22rpx;
    padding: 8rpx;
    text-align: center;
}

.action-section {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    padding: 20rpx;
    box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.05);
}

.action-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    text-align: center;
    font-size: 32rpx;
    border-radius: 8rpx;
}
</style>
