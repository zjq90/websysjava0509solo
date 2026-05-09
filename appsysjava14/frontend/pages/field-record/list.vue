<template>
    <view class="list-container">
        <view class="search-section">
            <view class="search-box">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    v-model="keyword" 
                    placeholder="搜索地块/作物"
                    placeholder-class="placeholder"
                    @confirm="loadRecords"
                />
            </view>
            <view class="filter-btn" @click="showFilter = true">
                <text>筛选</text>
            </view>
        </view>

        <view class="add-btn" @click="goToAdd">
            <text class="add-icon">➕</text>
            <text class="add-text">新增记录</text>
        </view>

        <view class="record-list">
            <view 
                class="record-card" 
                v-for="record in records" 
                :key="record.id"
                @click="goToDetail(record.id)"
            >
                <view class="card-header">
                    <view class="record-no">
                        <text class="no-label">记录编号</text>
                        <text class="no-value">{{ record.recordNo }}</text>
                    </view>
                    <view class="record-status" :class="'status-' + record.status">
                        {{ getStatusLabel(record.status) }}
                    </view>
                </view>

                <view class="card-body">
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">地块</text>
                            <text class="info-value">{{ record.plotName }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">作物</text>
                            <text class="info-value">{{ record.cropName }}</text>
                        </view>
                    </view>

                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">生长阶段</text>
                            <text class="info-value">{{ getStageLabel(record.growthStage) }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">病虫害等级</text>
                            <text :class="['info-value', 'pest-level-' + record.pestLevel]">
                                {{ getPestLevelLabel(record.pestLevel) }}
                            </text>
                        </view>
                    </view>

                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">株高</text>
                            <text class="info-value">{{ record.plantHeight || '-' }} cm</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">出苗率</text>
                            <text class="info-value">{{ record.emergenceRate || '-' }}%</text>
                        </view>
                    </view>
                </view>

                <view class="card-footer">
                    <text class="observer">{{ record.observer }}</text>
                    <text class="date">{{ formatDate(record.recordDate) }}</text>
                </view>

                <view class="data-source-badge" v-if="record.dataSource === 'OFFLINE'">
                    离线同步
                </view>
            </view>

            <view class="empty-tip" v-if="records.length === 0">
                <text class="empty-icon">📝</text>
                <text>暂无田间记录</text>
                <view class="empty-btn" @click="goToAdd">去新增</view>
            </view>
        </view>

        <view class="filter-popup" v-if="showFilter">
            <view class="filter-mask" @click="showFilter = false"></view>
            <view class="filter-content">
                <view class="filter-header">
                    <text class="filter-title">筛选条件</text>
                    <text class="filter-clear" @click="clearFilter">清除</text>
                </view>

                <view class="filter-section">
                    <text class="filter-label">生长阶段</text>
                    <view class="filter-options">
                        <view 
                            class="filter-option" 
                            :class="{ active: filterStage === '' }"
                            @click="filterStage = ''"
                        >全部</view>
                        <view 
                            class="filter-option" 
                            v-for="stage in stages" 
                            :key="stage.value"
                            :class="{ active: filterStage === stage.value }"
                            @click="filterStage = stage.value"
                        >{{ stage.label }}</view>
                    </view>
                </view>

                <view class="filter-section">
                    <text class="filter-label">数据来源</text>
                    <view class="filter-options">
                        <view 
                            class="filter-option" 
                            :class="{ active: filterSource === '' }"
                            @click="filterSource = ''"
                        >全部</view>
                        <view 
                            class="filter-option" 
                            :class="{ active: filterSource === 'ONLINE' }"
                            @click="filterSource = 'ONLINE'"
                        >在线录入</view>
                        <view 
                            class="filter-option" 
                            :class="{ active: filterSource === 'OFFLINE' }"
                            @click="filterSource = 'OFFLINE'"
                        >离线同步</view>
                    </view>
                </view>

                <view class="filter-actions">
                    <view class="btn-secondary" @click="showFilter = false">取消</view>
                    <view class="btn-primary" @click="applyFilter">确定</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            keyword: '',
            records: [],
            allRecords: [],
            showFilter: false,
            filterStage: '',
            filterSource: '',
            stages: [
                { value: 'SEEDLING', label: '出苗期' },
                { value: 'TILLERING', label: '分蘖期' },
                { value: 'JOINTING', label: '拔节期' },
                { value: 'BOOTING', label: '孕穗期' },
                { value: 'HEADING', label: '抽穗期' },
                { value: 'FLOWERING', label: '开花期' },
                { value: 'FILLING', label: '灌浆期' },
                { value: 'MATURING', label: '成熟期' }
            ]
        }
    },

    onShow() {
        this.loadRecords()
    },

    methods: {
        async loadRecords() {
            try {
                const userId = this.$store.getters.userInfo && this.$store.getters.userInfo.userId
                if (!userId) return

                let data
                if (this.keyword) {
                    data = await this.$request({
                        url: '/field-records/observer/' + userId,
                        method: 'GET'
                    })
                } else {
                    data = await this.$request({
                        url: '/field-records/observer/' + userId,
                        method: 'GET'
                    })
                }
                
                this.allRecords = data || []
                this.applyFilters()
            } catch (e) {
                console.error('加载失败', e)
            }
        },

        applyFilters() {
            let result = [...this.allRecords]

            if (this.filterStage) {
                result = result.filter(r => r.growthStage === this.filterStage)
            }

            if (this.filterSource) {
                result = result.filter(r => r.dataSource === this.filterSource)
            }

            if (this.keyword) {
                const kw = this.keyword.toLowerCase()
                result = result.filter(r => 
                    (r.plotName && r.plotName.toLowerCase().includes(kw)) ||
                    (r.cropName && r.cropName.toLowerCase().includes(kw))
                )
            }

            this.records = result
        },

        clearFilter() {
            this.filterStage = ''
            this.filterSource = ''
        },

        applyFilter() {
            this.showFilter = false
            this.applyFilters()
        },

        goToAdd() {
            uni.navigateTo({ url: '/pages/field-record/add' })
        },

        goToDetail(id) {
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

        getPestLevelLabel(level) {
            const labels = ['无', '轻度', '中度', '重度', '严重']
            return labels[level] || '-'
        },

        getStatusLabel(status) {
            const map = {
                'DRAFT': '草稿',
                'SUBMITTED': '已提交',
                'REVIEWED': '已审核'
            }
            return map[status] || status
        },

        formatDate(dateStr) {
            if (!dateStr) return ''
            const date = new Date(dateStr)
            return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
        }
    }
}
</script>

<style>
.list-container {
    min-height: 100vh;
    background-color: #f5f5f5;
    padding: 20rpx;
    padding-bottom: 180rpx;
}

.search-section {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
}

.search-box {
    flex: 1;
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

.filter-btn {
    margin-left: 20rpx;
    background-color: #fff;
    padding: 0 24rpx;
    height: 80rpx;
    border-radius: 40rpx;
    display: flex;
    align-items: center;
}

.filter-btn text {
    font-size: 28rpx;
    color: #666;
}

.add-btn {
    position: fixed;
    right: 30rpx;
    bottom: 180rpx;
    background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
    padding: 24rpx 40rpx;
    border-radius: 50rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 8rpx 24rpx rgba(76, 175, 80, 0.3);
}

.add-icon {
    font-size: 32rpx;
    margin-right: 12rpx;
}

.add-text {
    font-size: 28rpx;
    color: #fff;
    font-weight: 500;
}

.record-list {
    margin-top: 20rpx;
}

.record-card {
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
    padding-bottom: 20rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.record-no {
    display: flex;
    flex-direction: column;
}

.no-label {
    font-size: 22rpx;
    color: #999;
}

.no-value {
    font-size: 26rpx;
    color: #333;
    font-weight: 500;
    margin-top: 4rpx;
}

.record-status {
    padding: 6rpx 20rpx;
    border-radius: 20rpx;
    font-size: 22rpx;
}

.status-DRAFT {
    background-color: #FFF3E0;
    color: #FF9800;
}

.status-SUBMITTED {
    background-color: #E8F5E9;
    color: #4CAF50;
}

.status-REVIEWED {
    background-color: #E3F2FD;
    color: #2196F3;
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

.pest-level-0 { color: #4CAF50; }
.pest-level-1 { color: #8BC34A; }
.pest-level-2 { color: #FFC107; }
.pest-level-3 { color: #FF9800; }
.pest-level-4 { color: #f44336; }

.card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20rpx;
    padding-top: 20rpx;
    border-top: 1rpx solid #f0f0f0;
}

.observer {
    font-size: 24rpx;
    color: #666;
}

.date {
    font-size: 24rpx;
    color: #999;
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

.empty-btn {
    margin-top: 30rpx;
    background-color: #4CAF50;
    color: #fff;
    padding: 16rpx 40rpx;
    border-radius: 40rpx;
    font-size: 28rpx;
}

.filter-popup {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 999;
}

.filter-mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
}

.filter-content {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: #fff;
    border-radius: 24rpx 24rpx 0 0;
    padding: 30rpx;
}

.filter-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
}

.filter-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.filter-clear {
    font-size: 28rpx;
    color: #4CAF50;
}

.filter-section {
    margin-bottom: 30rpx;
}

.filter-label {
    font-size: 28rpx;
    color: #333;
    display: block;
    margin-bottom: 16rpx;
}

.filter-options {
    display: flex;
    flex-wrap: wrap;
}

.filter-option {
    padding: 12rpx 28rpx;
    background-color: #f5f5f5;
    border-radius: 30rpx;
    font-size: 26rpx;
    color: #666;
    margin-right: 16rpx;
    margin-bottom: 16rpx;
}

.filter-option.active {
    background-color: #E8F5E9;
    color: #4CAF50;
}

.filter-actions {
    display: flex;
    margin-top: 40rpx;
}

.filter-actions .btn-secondary,
.filter-actions .btn-primary {
    flex: 1;
    margin: 0 10rpx;
    height: 88rpx;
    line-height: 88rpx;
}
</style>
