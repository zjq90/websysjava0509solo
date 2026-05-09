<template>
    <view class="crop-container">
        <view class="search-section">
            <view class="search-box">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    v-model="keyword" 
                    placeholder="搜索品种名称/批次"
                    placeholder-class="placeholder"
                    @confirm="loadCrops"
                />
            </view>
        </view>

        <view class="filter-tabs">
            <view 
                class="filter-tab" 
                :class="{ active: filterType === '' }"
                @click="filterType = ''"
            >
                全部
            </view>
            <view 
                class="filter-tab" 
                v-for="type in cropTypes" 
                :key="type.value"
                :class="{ active: filterType === type.value }"
                @click="filterType = type.value"
            >
                {{ type.label }}
            </view>
        </view>

        <view class="crop-list">
            <view 
                class="crop-card" 
                v-for="crop in crops" 
                :key="crop.id"
            >
                <view class="card-header">
                    <view class="crop-info">
                        <view class="crop-name-row">
                            <text class="crop-icon">{{ getCropIcon(crop.cropType) }}</text>
                            <text class="crop-name">{{ crop.cropName }}</text>
                        </view>
                        <view class="batch-code">
                            批次: {{ crop.batchCode }}
                        </view>
                    </view>
                    <view class="crop-type-tag" :class="'type-' + crop.cropType">
                        {{ getTypeLabel(crop.cropType) }}
                    </view>
                </view>

                <view class="card-body">
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">品种名称</text>
                            <text class="info-value">{{ crop.variety || '-' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">生产商</text>
                            <text class="info-value">{{ crop.producer || '-' }}</text>
                        </view>
                    </view>

                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">发芽率</text>
                            <text class="info-value" :class="getGerminationClass(crop.germinationRate)">
                                {{ crop.germinationRate }}%
                            </text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">保质期</text>
                            <text class="info-value" :class="getShelfLifeClass(crop.shelfLife)">
                                {{ formatDate(crop.shelfLife) }}
                            </text>
                        </view>
                    </view>

                    <view class="info-row">
                        <view class="info-item full">
                            <text class="info-label">描述</text>
                            <text class="info-value">{{ crop.description || '暂无描述' }}</text>
                        </view>
                    </view>
                </view>

                <view class="card-footer">
                    <text class="created-time">添加于 {{ formatDate(crop.createdTime) }}</text>
                </view>
            </view>

            <view class="empty-tip" v-if="crops.length === 0">
                <text class="empty-icon">🌾</text>
                <text>暂无作物品种</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            keyword: '',
            filterType: '',
            crops: [],
            allCrops: [],
            cropTypes: [
                { value: 'MAIZE', label: '玉米' },
                { value: 'WHEAT', label: '小麦' },
                { value: 'RICE', label: '水稻' },
                { value: 'SOYBEAN', label: '大豆' },
                { value: 'COTTON', label: '棉花' },
                { value: 'OTHER', label: '其他' }
            ]
        }
    },

    onShow() {
        this.loadCrops()
    },

    methods: {
        async loadCrops() {
            try {
                const data = await this.$request({
                    url: '/crops',
                    method: 'GET'
                })
                this.allCrops = data || []
                this.filterCrops()
            } catch (e) {
                console.error('加载作物失败', e)
            }
        },

        filterCrops() {
            let result = [...this.allCrops]

            if (this.filterType) {
                result = result.filter(c => c.cropType === this.filterType)
            }

            if (this.keyword) {
                const kw = this.keyword.toLowerCase()
                result = result.filter(c => 
                    (c.cropName && c.cropName.toLowerCase().includes(kw)) ||
                    (c.batchCode && c.batchCode.toLowerCase().includes(kw)) ||
                    (c.variety && c.variety.toLowerCase().includes(kw))
                )
            }

            this.crops = result
        },

        getCropIcon(type) {
            const icons = {
                'MAIZE': '🌽',
                'WHEAT': '🌾',
                'RICE': '🍚',
                'SOYBEAN': '🫘',
                'COTTON': '☁️',
                'OTHER': '🌱'
            }
            return icons[type] || '🌱'
        },

        getTypeLabel(type) {
            const labels = {
                'MAIZE': '玉米',
                'WHEAT': '小麦',
                'RICE': '水稻',
                'SOYBEAN': '大豆',
                'COTTON': '棉花',
                'OTHER': '其他'
            }
            return labels[type] || type
        },

        getGerminationClass(rate) {
            if (rate >= 90) return 'text-success'
            if (rate >= 70) return 'text-primary'
            if (rate >= 50) return 'text-warning'
            return 'text-danger'
        },

        getShelfLifeClass(shelfLife) {
            if (!shelfLife) return ''
            const now = new Date()
            const expire = new Date(shelfLife)
            const daysDiff = (expire - now) / (1000 * 60 * 60 * 24)
            
            if (daysDiff < 30) return 'text-danger'
            if (daysDiff < 90) return 'text-warning'
            return 'text-success'
        },

        formatDate(dateStr) {
            if (!dateStr) return '-'
            const date = new Date(dateStr)
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
        }
    }
}
</script>

<style>
.crop-container {
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

.filter-tabs {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 20rpx;
    padding: 0 4rpx;
}

.filter-tab {
    padding: 12rpx 28rpx;
    background-color: #fff;
    border-radius: 30rpx;
    font-size: 26rpx;
    color: #666;
    margin-right: 16rpx;
    margin-bottom: 12rpx;
}

.filter-tab.active {
    background-color: #4CAF50;
    color: #fff;
}

.crop-list {
    margin-top: 10rpx;
}

.crop-card {
    background-color: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    padding: 24rpx;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    padding-bottom: 16rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.crop-info {
    flex: 1;
}

.crop-name-row {
    display: flex;
    align-items: center;
    margin-bottom: 8rpx;
}

.crop-icon {
    font-size: 36rpx;
    margin-right: 10rpx;
}

.crop-name {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.batch-code {
    font-size: 24rpx;
    color: #666;
    font-family: monospace;
}

.crop-type-tag {
    padding: 6rpx 16rpx;
    border-radius: 20rpx;
    font-size: 22rpx;
    font-weight: 500;
}

.type-MAIZE { background-color: #FFF3E0; color: #EF6C00; }
.type-WHEAT { background-color: #FFF9C4; color: #F57F17; }
.type-RICE { background-color: #E3F2FD; color: #1565C0; }
.type-SOYBEAN { background-color: #E8F5E9; color: #2E7D32; }
.type-COTTON { background-color: #F3E5F5; color: #7B1FA2; }
.type-OTHER { background-color: #ECEFF1; color: #546E7A; }

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

.text-success { color: #4CAF50; }
.text-warning { color: #FF9800; }
.text-danger { color: #f44336; }

.card-footer {
    margin-top: 16rpx;
    padding-top: 16rpx;
    border-top: 1rpx solid #f0f0f0;
}

.created-time {
    font-size: 22rpx;
    color: #aaa;
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
