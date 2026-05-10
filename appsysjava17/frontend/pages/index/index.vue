<template>
    <view class="container">
        <!-- 顶部banner -->
        <view class="banner">
            <view class="banner-content">
                <text class="banner-title">🌱 种子质量追溯系统</text>
                <text class="banner-subtitle">扫一扫，追溯全链条</text>
            </view>
            <view class="banner-decoration"></view>
        </view>

        <!-- 搜索框 -->
        <view class="search-section">
            <view class="search-bar" @click="goToScan">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    placeholder="输入批次号或点击扫码查询" 
                    placeholder-class="search-placeholder"
                    disabled
                />
                <text class="search-btn">查询</text>
            </view>
        </view>

        <!-- 快捷功能入口 -->
        <view class="quick-actions">
            <view class="quick-grid">
                <view class="quick-item" @click="goToScan">
                    <view class="quick-icon scan">📷</view>
                    <text class="quick-text">扫码溯源</text>
                </view>
                <view class="quick-item" @click="goToBatchList">
                    <view class="quick-icon batch">📋</view>
                    <text class="quick-text">批次管理</text>
                </view>
                <view class="quick-item" @click="goToAddBatch">
                    <view class="quick-icon add">➕</view>
                    <text class="quick-text">新增批次</text>
                </view>
                <view class="quick-item" @click="goToMine">
                    <view class="quick-icon mine">👤</view>
                    <text class="quick-text">我的</text>
                </view>
            </view>
        </view>

        <!-- 统计数据 -->
        <view class="stats-section">
            <view class="section-header">
                <text class="section-title">📊 数据概览</text>
                <text class="section-more">查看全部 →</text>
            </view>
            <view class="stats-grid">
                <view class="stats-card" @click="goToBatchList">
                    <view class="stats-icon green">🌾</view>
                    <view class="stats-info">
                        <text class="stats-value">{{ stats.totalBatches }}</text>
                        <text class="stats-label">种子批次</text>
                    </view>
                </view>
                <view class="stats-card" @click="goToBatchList">
                    <view class="stats-icon blue">✅</view>
                    <view class="stats-info">
                        <text class="stats-value">{{ stats.activeBatches }}</text>
                        <text class="stats-label">在库批次</text>
                    </view>
                </view>
                <view class="stats-card">
                    <view class="stats-icon orange">📥</view>
                    <view class="stats-info">
                        <text class="stats-value">{{ stats.totalQuantity }}</text>
                        <text class="stats-label">总数量(kg)</text>
                    </view>
                </view>
                <view class="stats-card">
                    <view class="stats-icon purple">👁</view>
                    <view class="stats-info">
                        <text class="stats-value">{{ stats.traceCount }}</text>
                        <text class="stats-label">溯源查询</text>
                    </view>
                </view>
            </view>
        </view>

        <!-- 最近批次 -->
        <view class="recent-section">
            <view class="section-header">
                <text class="section-title">🔥 最近批次</text>
                <text class="section-more" @click="goToBatchList">全部 →</text>
            </view>
            <view class="batch-list">
                <view 
                    class="batch-item" 
                    v-for="batch in recentBatches" 
                    :key="batch.id"
                    @click="goToTrace(batch.batchCode)"
                >
                    <view class="batch-icon" :class="{ inactive: batch.status !== 'ACTIVE' }">
                        🌱
                    </view>
                    <view class="batch-info">
                        <view class="batch-top">
                            <text class="batch-name">{{ batch.seedName }}</text>
                            <view class="batch-status" :class="batch.status === 'ACTIVE' ? 'active' : 'inactive'">
                                {{ batch.status === 'ACTIVE' ? '正常' : '停用' }}
                            </view>
                        </view>
                        <view class="batch-middle">
                            <text class="batch-code">批次号：{{ batch.batchCode }}</text>
                            <text class="batch-variety">{{ batch.seedVariety }}</text>
                        </view>
                        <view class="batch-bottom">
                            <text class="batch-rate">发芽率：{{ batch.germinationRate }}%</text>
                            <text class="batch-date">生产日期：{{ batch.productionDate }}</text>
                        </view>
                    </view>
                    <text class="batch-arrow">›</text>
                </view>
            </view>

            <view v-if="recentBatches.length === 0" class="empty-container">
                <text class="empty-icon">📦</text>
                <text class="empty-text">暂无批次数据</text>
                <view class="btn btn-primary btn-sm mt-20" @click="goToAddBatch">立即添加</view>
            </view>
        </view>

        <!-- 提示信息 -->
        <view class="tips-section">
            <view class="tips-card">
                <text class="tips-icon">💡</text>
                <view class="tips-content">
                    <text class="tips-title">使用提示</text>
                    <text class="tips-text">扫描种子包装上的二维码，即可查看完整的溯源信息，支持导出PDF报告和分享至微信/钉钉</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
    import { batchApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                stats: {
                    totalBatches: 0,
                    activeBatches: 0,
                    totalQuantity: 0,
                    traceCount: 0
                },
                recentBatches: [],
                loading: false
            };
        },
        
        onLoad() {
            this.loadData();
        },
        
        onShow() {
            this.loadData();
        },
        
        onPullDownRefresh() {
            this.loadData();
            setTimeout(() => {
                uni.stopPullDownRefresh();
            }, 1000);
        },
        
        methods: {
            async loadData() {
                this.loading = true;
                
                try {
                    const res = await batchApi.getAllBatches();
                    if (res.code === 200 && res.data) {
                        const batches = res.data;
                        
                        // 统计数据
                        this.stats.totalBatches = batches.length;
                        this.stats.activeBatches = batches.filter(b => b.status === 'ACTIVE').length;
                        this.stats.totalQuantity = batches.reduce((sum, b) => sum + (b.quantity || 0), 0);
                        this.stats.traceCount = batches.length * 3; // 模拟查询次数
                        
                        // 最近批次（取前5个）
                        this.recentBatches = batches.slice(0, 5);
                    }
                } catch (e) {
                    console.error('加载数据失败', e);
                    // 使用模拟数据
                    this.loadMockData();
                } finally {
                    this.loading = false;
                }
            },
            
            loadMockData() {
                this.stats = {
                    totalBatches: 5,
                    activeBatches: 4,
                    totalQuantity: 24000,
                    traceCount: 15
                };
                
                this.recentBatches = [
                    {
                        id: 1,
                        batchCode: 'SD2024A1',
                        seedName: '玉米种子',
                        seedVariety: '郑单958',
                        germinationRate: 95.5,
                        productionDate: '2024-01-15',
                        status: 'ACTIVE'
                    },
                    {
                        id: 2,
                        batchCode: 'SD2024B2',
                        seedName: '小麦种子',
                        seedVariety: '济麦22',
                        germinationRate: 92.0,
                        productionDate: '2024-02-20',
                        status: 'ACTIVE'
                    },
                    {
                        id: 3,
                        batchCode: 'SD2024C3',
                        seedName: '水稻种子',
                        seedVariety: '籼优63',
                        germinationRate: 93.8,
                        productionDate: '2024-03-10',
                        status: 'ACTIVE'
                    }
                ];
            },
            
            goToScan() {
                uni.switchTab({
                    url: '/pages/scan/scan'
                });
            },
            
            goToBatchList() {
                uni.switchTab({
                    url: '/pages/batch/list'
                });
            },
            
            goToAddBatch() {
                uni.navigateTo({
                    url: '/pages/batch/form'
                });
            },
            
            goToMine() {
                uni.switchTab({
                    url: '/pages/mine/mine'
                });
            },
            
            goToTrace(batchCode) {
                uni.navigateTo({
                    url: `/pages/trace/trace?batchCode=${batchCode}`
                });
            }
        }
    };
</script>

<style scoped>
    .container {
        padding-bottom: 40rpx;
        background: linear-gradient(to bottom, #2b7a4b 0%, #2b7a4b 400rpx, #f5f5f5 400rpx, #f5f5f5 100%);
        min-height: 100vh;
    }

    .banner {
        padding: 60rpx 40rpx 80rpx;
        position: relative;
    }

    .banner-content {
        position: relative;
        z-index: 2;
    }

    .banner-title {
        display: block;
        font-size: 44rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 16rpx;
    }

    .banner-subtitle {
        display: block;
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.8);
    }

    .search-section {
        padding: 0 30rpx;
        margin-top: -60rpx;
        position: relative;
        z-index: 10;
    }

    .search-bar {
        display: flex;
        align-items: center;
        padding: 24rpx 30rpx;
        background-color: #fff;
        border-radius: 50rpx;
        box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.12);
    }

    .search-icon {
        font-size: 40rpx;
        margin-right: 20rpx;
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
        padding: 16rpx 36rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        color: #fff;
        border-radius: 30rpx;
        font-size: 26rpx;
        font-weight: 500;
    }

    .quick-actions {
        padding: 40rpx 30rpx;
    }

    .quick-grid {
        display: flex;
        background-color: #fff;
        border-radius: 20rpx;
        padding: 40rpx 0;
        box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .quick-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .quick-icon {
        width: 100rpx;
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 30rpx;
        font-size: 48rpx;
        margin-bottom: 16rpx;
    }

    .quick-icon.scan {
        background-color: #e8f5e9;
    }

    .quick-icon.batch {
        background-color: #e3f2fd;
    }

    .quick-icon.add {
        background-color: #fff3e0;
    }

    .quick-icon.mine {
        background-color: #f3e5f5;
    }

    .quick-text {
        font-size: 26rpx;
        color: #333;
    }

    .stats-section {
        padding: 0 30rpx 20rpx;
    }

    .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
    }

    .section-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
    }

    .section-more {
        font-size: 24rpx;
        color: #2b7a4b;
    }

    .stats-grid {
        display: flex;
        flex-wrap: wrap;
        margin: -10rpx;
    }

    .stats-card {
        width: 50%;
        padding: 10rpx;
    }

    .stats-card .stats-card-inner {
        display: flex;
        align-items: center;
        padding: 30rpx;
        background-color: #fff;
        border-radius: 20rpx;
        box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
    }

    .recent-section {
        padding: 20rpx 30rpx;
    }

    .batch-list {
        background-color: #fff;
        border-radius: 20rpx;
        overflow: hidden;
        box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
    }

    .batch-item {
        display: flex;
        align-items: center;
        padding: 30rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .batch-item:last-child {
        border-bottom: none;
    }

    .batch-icon {
        width: 80rpx;
        height: 80rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #e8f5e9;
        border-radius: 20rpx;
        font-size: 40rpx;
        margin-right: 24rpx;
    }

    .batch-icon.inactive {
        background-color: #f5f5f5;
        opacity: 0.5;
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
        font-size: 30rpx;
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

    .batch-middle {
        display: flex;
        align-items: center;
        margin-bottom: 8rpx;
    }

    .batch-code {
        font-size: 24rpx;
        color: #666;
        margin-right: 20rpx;
    }

    .batch-variety {
        font-size: 24rpx;
        color: #999;
    }

    .batch-bottom {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .batch-rate {
        font-size: 24rpx;
        color: #2b7a4b;
    }

    .batch-date {
        font-size: 22rpx;
        color: #999;
    }

    .batch-arrow {
        font-size: 40rpx;
        color: #ccc;
        margin-left: 20rpx;
    }

    .tips-section {
        padding: 20rpx 30rpx;
    }

    .tips-card {
        display: flex;
        align-items: flex-start;
        padding: 30rpx;
        background-color: #fff9e6;
        border-radius: 20rpx;
        border-left: 8rpx solid #ff9800;
    }

    .tips-icon {
        font-size: 36rpx;
        margin-right: 20rpx;
    }

    .tips-content {
        flex: 1;
    }

    .tips-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #ff9800;
        margin-bottom: 8rpx;
    }

    .tips-text {
        display: block;
        font-size: 24rpx;
        color: #666;
        line-height: 1.6;
    }

    .empty-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 80rpx 0;
    }

    .empty-icon {
        font-size: 100rpx;
        color: #ddd;
        margin-bottom: 20rpx;
    }

    .empty-text {
        font-size: 28rpx;
        color: #999;
        margin-bottom: 20rpx;
    }
</style>
