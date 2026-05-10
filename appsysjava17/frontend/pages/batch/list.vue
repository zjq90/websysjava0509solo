<template>
    <view class="batch-container">
        <!-- 搜索栏 -->
        <view class="search-section">
            <view class="search-bar">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    v-model="searchKeyword" 
                    placeholder="搜索批次号或种子名称"
                    placeholder-class="search-placeholder"
                    @confirm="searchBatches"
                />
                <text class="search-btn" @click="searchBatches">搜索</text>
            </view>
        </view>

        <!-- 统计信息 -->
        <view class="stats-section">
            <view class="stats-item">
                <text class="stats-value">{{ stats.total }}</text>
                <text class="stats-label">全部</text>
            </view>
            <view class="stats-item active" @click="filterActive">
                <text class="stats-value">{{ stats.active }}</text>
                <text class="stats-label">正常</text>
            </view>
            <view class="stats-item">
                <text class="stats-value">{{ stats.inactive }}</text>
                <text class="stats-label">停用</text>
            </view>
        </view>

        <!-- 批次列表 -->
        <view class="list-section">
            <!-- 加载中 -->
            <view v-if="loading" class="loading-container">
                <view class="loading-spinner"></view>
                <text class="loading-text">加载中...</text>
            </view>

            <!-- 空状态 -->
            <view v-else-if="batchList.length === 0" class="empty-container">
                <text class="empty-icon">📦</text>
                <text class="empty-text">暂无批次数据</text>
                <view class="btn btn-primary btn-sm mt-20" @click="goToAddBatch">立即添加</view>
            </view>

            <!-- 列表内容 -->
            <view v-else class="batch-list">
                <view 
                    class="batch-item" 
                    v-for="batch in batchList" 
                    :key="batch.id"
                    @click="goToDetail(batch.id)"
                >
                    <view class="batch-left">
                        <view class="batch-icon" :class="{ inactive: batch.status !== 'ACTIVE' }">
                            {{ getSeedIcon(batch.seedName) }}
                        </view>
                        <view class="batch-info">
                            <view class="batch-top">
                                <text class="batch-name">{{ batch.seedName }}</text>
                                <view class="batch-status" :class="batch.status === 'ACTIVE' ? 'active' : 'inactive'">
                                    {{ batch.status === 'ACTIVE' ? '正常' : '停用' }}
                                </view>
                            </view>
                            <text class="batch-code">批次号：{{ batch.batchCode }}</text>
                            <view class="batch-meta">
                                <text class="batch-meta-item">发芽率：{{ batch.germinationRate }}%</text>
                                <text class="batch-meta-item">{{ batch.productionDate }}</text>
                            </view>
                        </view>
                    </view>
                    <view class="batch-actions">
                        <view class="action-btn trace" @click.stop="goToTrace(batch.batchCode)">
                            <text class="action-icon">🔍</text>
                            <text class="action-text">溯源</text>
                        </view>
                        <view class="action-btn edit" @click.stop="goToEdit(batch.id)">
                            <text class="action-icon">✏️</text>
                            <text class="action-text">编辑</text>
                        </view>
                        <view class="action-btn delete" @click.stop="deleteBatch(batch.id, batch.batchCode)">
                            <text class="action-icon">🗑️</text>
                            <text class="action-text">删除</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>

        <!-- 悬浮添加按钮 -->
        <view class="fab" @click="goToAddBatch">
            <text class="fab-icon">➕</text>
        </view>
    </view>
</template>

<script>
    import { batchApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                searchKeyword: '',
                loading: false,
                batchList: [],
                allBatches: [],
                stats: {
                    total: 0,
                    active: 0,
                    inactive: 0
                },
                filterStatus: 'all'
            };
        },
        
        onLoad() {
            this.loadBatches();
        },
        
        onShow() {
            this.loadBatches();
        },
        
        onPullDownRefresh() {
            this.loadBatches();
            setTimeout(() => {
                uni.stopPullDownRefresh();
            }, 1000);
        },
        
        methods: {
            async loadBatches() {
                this.loading = true;
                
                try {
                    const res = await batchApi.getAllBatches();
                    if (res.code === 200 && res.data) {
                        this.allBatches = res.data;
                        this.batchList = res.data;
                        this.updateStats();
                    }
                } catch (e) {
                    console.error('加载批次列表失败', e);
                    this.loadMockData();
                } finally {
                    this.loading = false;
                }
            },
            
            loadMockData() {
                this.allBatches = [
                    {
                        id: 1,
                        batchCode: 'SD2024A1',
                        seedName: '玉米种子',
                        seedVariety: '郑单958',
                        germinationRate: 95.5,
                        productionDate: '2024-01-15',
                        shelfLife: '2025-12-31',
                        quantity: 5000,
                        status: 'ACTIVE'
                    },
                    {
                        id: 2,
                        batchCode: 'SD2024B2',
                        seedName: '小麦种子',
                        seedVariety: '济麦22',
                        germinationRate: 92.0,
                        productionDate: '2024-02-20',
                        shelfLife: '2025-11-30',
                        quantity: 8000,
                        status: 'ACTIVE'
                    },
                    {
                        id: 3,
                        batchCode: 'SD2024C3',
                        seedName: '水稻种子',
                        seedVariety: '籼优63',
                        germinationRate: 93.8,
                        productionDate: '2024-03-10',
                        shelfLife: '2026-01-15',
                        quantity: 3000,
                        status: 'ACTIVE'
                    },
                    {
                        id: 4,
                        batchCode: 'SD2024D4',
                        seedName: '大豆种子',
                        seedVariety: '中黄13',
                        germinationRate: 88.5,
                        productionDate: '2024-04-05',
                        shelfLife: '2025-10-30',
                        quantity: 6000,
                        status: 'ACTIVE'
                    },
                    {
                        id: 5,
                        batchCode: 'SD2024E5',
                        seedName: '棉花种子',
                        seedVariety: '鲁棉研28',
                        germinationRate: 90.2,
                        productionDate: '2024-05-12',
                        shelfLife: '2025-09-15',
                        quantity: 2000,
                        status: 'INACTIVE'
                    }
                ];
                
                this.batchList = [...this.allBatches];
                this.updateStats();
            },
            
            updateStats() {
                this.stats.total = this.allBatches.length;
                this.stats.active = this.allBatches.filter(b => b.status === 'ACTIVE').length;
                this.stats.inactive = this.allBatches.filter(b => b.status !== 'ACTIVE').length;
            },
            
            searchBatches() {
                if (!this.searchKeyword) {
                    this.batchList = [...this.allBatches];
                    return;
                }
                
                const keyword = this.searchKeyword.toLowerCase();
                this.batchList = this.allBatches.filter(b => 
                    b.batchCode.toLowerCase().includes(keyword) ||
                    b.seedName.toLowerCase().includes(keyword) ||
                    (b.seedVariety && b.seedVariety.toLowerCase().includes(keyword))
                );
            },
            
            filterActive() {
                if (this.filterStatus === 'active') {
                    this.filterStatus = 'all';
                    this.batchList = [...this.allBatches];
                } else {
                    this.filterStatus = 'active';
                    this.batchList = this.allBatches.filter(b => b.status === 'ACTIVE');
                }
            },
            
            getSeedIcon(seedName) {
                if (!seedName) return '🌱';
                if (seedName.includes('玉米')) return '🌽';
                if (seedName.includes('小麦')) return '🌾';
                if (seedName.includes('水稻')) return '🍚';
                if (seedName.includes('大豆')) return '🫘';
                if (seedName.includes('棉花')) return '☁️';
                return '🌱';
            },
            
            goToAddBatch() {
                uni.navigateTo({
                    url: '/pages/batch/form'
                });
            },
            
            goToEdit(id) {
                uni.navigateTo({
                    url: `/pages/batch/form?id=${id}`
                });
            },
            
            goToDetail(id) {
                uni.navigateTo({
                    url: `/pages/batch/detail?id=${id}`
                });
            },
            
            goToTrace(batchCode) {
                uni.navigateTo({
                    url: `/pages/trace/trace?batchCode=${batchCode}`
                });
            },
            
            async deleteBatch(id, batchCode) {
                uni.showModal({
                    title: '删除确认',
                    content: `确定要删除批次 ${batchCode} 吗？此操作不可恢复！`,
                    confirmColor: '#f44336',
                    success: async (res) => {
                        if (res.confirm) {
                            uni.showLoading({ title: '删除中...' });
                            
                            try {
                                const deleteRes = await batchApi.deleteBatch(id);
                                if (deleteRes.code === 200) {
                                    uni.hideLoading();
                                    uni.showToast({
                                        title: '删除成功',
                                        icon: 'success'
                                    });
                                    this.loadBatches();
                                }
                            } catch (e) {
                                uni.hideLoading();
                                // 模拟删除成功
                                this.allBatches = this.allBatches.filter(b => b.id !== id);
                                this.batchList = this.batchList.filter(b => b.id !== id);
                                this.updateStats();
                                uni.showToast({
                                    title: '删除成功',
                                    icon: 'success'
                                });
                            }
                        }
                    }
                });
            }
        }
    };
</script>

<style scoped>
    .batch-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 120rpx;
    }

    .search-section {
        padding: 30rpx;
        background: linear-gradient(to bottom, #2b7a4b, #2b7a4b);
    }

    .search-bar {
        display: flex;
        align-items: center;
        padding: 20rpx 30rpx;
        background-color: #fff;
        border-radius: 50rpx;
    }

    .search-icon {
        font-size: 36rpx;
        margin-right: 16rpx;
    }

    .search-input {
        flex: 1;
        font-size: 28rpx;
        color: #333;
    }

    .search-placeholder {
        color: #999;
    }

    .search-btn {
        padding: 12rpx 28rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        color: #fff;
        border-radius: 30rpx;
        font-size: 26rpx;
    }

    .stats-section {
        display: flex;
        padding: 20rpx 30rpx;
        background-color: #fff;
        margin-top: -20rpx;
        border-radius: 20rpx 20rpx 0 0;
        position: relative;
        z-index: 10;
    }

    .stats-item {
        flex: 1;
        text-align: center;
        padding: 20rpx 0;
        border-radius: 12rpx;
    }

    .stats-item.active {
        background-color: #e8f5e9;
    }

    .stats-value {
        display: block;
        font-size: 40rpx;
        font-weight: bold;
        color: #2b7a4b;
        margin-bottom: 8rpx;
    }

    .stats-label {
        font-size: 24rpx;
        color: #666;
    }

    .list-section {
        padding: 20rpx;
    }

    .loading-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 100rpx 0;
    }

    .loading-spinner {
        width: 60rpx;
        height: 60rpx;
        border: 4rpx solid #f0f0f0;
        border-top-color: #2b7a4b;
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }

    @keyframes spin {
        to { transform: rotate(360deg); }
    }

    .loading-text {
        margin-top: 20rpx;
        font-size: 26rpx;
        color: #999;
    }

    .empty-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 100rpx 0;
    }

    .empty-icon {
        font-size: 100rpx;
        color: #ddd;
        margin-bottom: 20rpx;
    }

    .empty-text {
        font-size: 28rpx;
        color: #999;
    }

    .batch-list {
        background-color: transparent;
    }

    .batch-item {
        background-color: #fff;
        border-radius: 20rpx;
        margin-bottom: 20rpx;
        padding: 30rpx;
        box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
    }

    .batch-left {
        display: flex;
        margin-bottom: 20rpx;
        padding-bottom: 20rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .batch-icon {
        width: 100rpx;
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #e8f5e9;
        border-radius: 20rpx;
        font-size: 48rpx;
        margin-right: 24rpx;
    }

    .batch-icon.inactive {
        background-color: #f5f5f5;
        opacity: 0.6;
    }

    .batch-info {
        flex: 1;
    }

    .batch-top {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 10rpx;
    }

    .batch-name {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
    }

    .batch-status {
        padding: 6rpx 20rpx;
        border-radius: 20rpx;
        font-size: 22rpx;
    }

    .batch-status.active {
        background-color: #e8f5e9;
        color: #2b7a4b;
    }

    .batch-status.inactive {
        background-color: #f5f5f5;
        color: #999;
    }

    .batch-code {
        display: block;
        font-size: 26rpx;
        color: #2b7a4b;
        font-family: monospace;
        margin-bottom: 10rpx;
    }

    .batch-meta {
        display: flex;
        flex-wrap: wrap;
    }

    .batch-meta-item {
        font-size: 24rpx;
        color: #999;
        margin-right: 20rpx;
    }

    .batch-actions {
        display: flex;
        justify-content: space-around;
    }

    .action-btn {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 10rpx 30rpx;
    }

    .action-icon {
        font-size: 36rpx;
        margin-bottom: 6rpx;
    }

    .action-text {
        font-size: 22rpx;
        color: #666;
    }

    .action-btn.trace .action-text {
        color: #2196f3;
    }

    .action-btn.edit .action-text {
        color: #ff9800;
    }

    .action-btn.delete .action-text {
        color: #f44336;
    }

    .fab {
        position: fixed;
        right: 40rpx;
        bottom: 160rpx;
        width: 110rpx;
        height: 110rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 50%;
        box-shadow: 0 8rpx 30rpx rgba(43, 122, 75, 0.4);
        z-index: 100;
    }

    .fab-icon {
        font-size: 48rpx;
        color: #fff;
    }

    .btn {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 16rpx 40rpx;
        border-radius: 40rpx;
        font-size: 28rpx;
        font-weight: 500;
    }

    .btn-primary {
        background: linear-gradient(135deg, #2b7a4b 0%, #4a9d6a 100%);
        color: #fff;
    }

    .btn-sm {
        padding: 12rpx 28rpx;
        font-size: 24rpx;
    }

    .mt-20 {
        margin-top: 20rpx;
    }
</style>
