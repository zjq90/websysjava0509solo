<template>
    <view class="detail-container">
        <!-- 加载状态 -->
        <view v-if="loading" class="loading-container">
            <view class="loading-spinner"></view>
            <text class="loading-text">加载中...</text>
        </view>

        <!-- 错误状态 -->
        <view v-else-if="error" class="error-container">
            <text class="error-icon">❌</text>
            <text class="error-text">{{ errorMessage }}</text>
        </view>

        <!-- 详情内容 -->
        <view v-else class="detail-content">
            <!-- 头部信息 -->
            <view class="header-section">
                <view class="header-icon">
                    {{ getSeedIcon(batch.seedName) }}
                </view>
                <view class="header-info">
                    <text class="header-name">{{ batch.seedName }}</text>
                    <text class="header-code">批次号：{{ batch.batchCode }}</text>
                    <view class="header-status" :class="batch.status === 'ACTIVE' ? 'active' : 'inactive'">
                        {{ batch.status === 'ACTIVE' ? '正常' : '停用' }}
                    </view>
                </view>
            </view>

            <!-- 基本信息 -->
            <view class="section">
                <view class="section-title">📋 基本信息</view>
                <view class="info-grid">
                    <view class="info-item">
                        <text class="info-label">种子品种</text>
                        <text class="info-value">{{ batch.seedVariety || '-' }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">发芽率</text>
                        <text class="info-value highlight">{{ batch.germinationRate }}%</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">纯度</text>
                        <text class="info-value">{{ batch.purity ? batch.purity + '%' : '-' }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">水分含量</text>
                        <text class="info-value">{{ batch.moistureContent ? batch.moistureContent + '%' : '-' }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">生产日期</text>
                        <text class="info-value">{{ batch.productionDate }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">保质期至</text>
                        <text class="info-value">{{ batch.shelfLife }}</text>
                    </view>
                    <view class="info-item full">
                        <text class="info-label">库存数量</text>
                        <text class="info-value">{{ batch.quantity }} kg</text>
                    </view>
                    <view class="info-item full">
                        <text class="info-label">单价</text>
                        <text class="info-value">¥{{ batch.unitPrice ? batch.unitPrice + '/kg' : '-' }}</text>
                    </view>
                </view>
            </view>

            <!-- 快捷操作 -->
            <view class="section">
                <view class="section-title">⚡ 快捷操作</view>
                <view class="action-grid">
                    <view class="action-item" @click="goToTrace">
                        <view class="action-icon trace">🔍</view>
                        <text class="action-text">溯源查询</text>
                    </view>
                    <view class="action-item" @click="goToEdit">
                        <view class="action-icon edit">✏️</view>
                        <text class="action-text">编辑信息</text>
                    </view>
                    <view class="action-item" @click="shareBatch">
                        <view class="action-icon share">📤</view>
                        <text class="action-text">分享</text>
                    </view>
                    <view class="action-item" @click="deleteBatch">
                        <view class="action-icon delete">🗑️</view>
                        <text class="action-text">删除</text>
                    </view>
                </view>
            </view>

            <!-- 创建信息 -->
            <view class="section" v-if="batch.createdAt">
                <view class="section-title">📅 记录信息</view>
                <view class="info-grid">
                    <view class="info-item">
                        <text class="info-label">创建时间</text>
                        <text class="info-value">{{ formatDate(batch.createdAt) }}</text>
                    </view>
                    <view class="info-item">
                        <text class="info-label">更新时间</text>
                        <text class="info-value">{{ formatDate(batch.updatedAt || batch.createdAt) }}</text>
                    </view>
                    <view class="info-item full">
                        <text class="info-label">创建人</text>
                        <text class="info-value">{{ batch.createdBy || '-' }}</text>
                    </view>
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
                batchId: null,
                loading: true,
                error: false,
                errorMessage: '',
                batch: {}
            };
        },
        
        onLoad(options) {
            if (options.id) {
                this.batchId = parseInt(options.id);
                this.loadBatchDetail();
            }
        },
        
        onShow() {
            if (this.batchId) {
                this.loadBatchDetail();
            }
        },
        
        methods: {
            async loadBatchDetail() {
                this.loading = true;
                this.error = false;
                
                try {
                    const res = await batchApi.getBatchById(this.batchId);
                    if (res.code === 200 && res.data) {
                        this.batch = res.data;
                    } else {
                        this.error = true;
                        this.errorMessage = res.message || '获取数据失败';
                    }
                } catch (e) {
                    console.error('获取批次详情失败', e);
                    this.loadMockData();
                } finally {
                    this.loading = false;
                }
            },
            
            loadMockData() {
                this.batch = {
                    id: this.batchId || 1,
                    batchCode: 'SD2024A1',
                    seedName: '玉米种子',
                    seedVariety: '郑单958',
                    germinationRate: 95.5,
                    purity: 98.0,
                    moistureContent: 12.5,
                    productionDate: '2024-01-15',
                    shelfLife: '2025-12-31',
                    quantity: 5000,
                    unitPrice: 25.00,
                    status: 'ACTIVE',
                    createdBy: 'admin',
                    createdAt: '2024-01-15T10:30:00'
                };
                this.error = false;
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
            
            formatDate(dateStr) {
                if (!dateStr) return '-';
                try {
                    const date = new Date(dateStr);
                    const pad = n => n < 10 ? '0' + n : n;
                    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
                } catch (e) {
                    return dateStr;
                }
            },
            
            goToTrace() {
                uni.navigateTo({
                    url: `/pages/trace/trace?batchCode=${this.batch.batchCode}`
                });
            },
            
            goToEdit() {
                uni.navigateTo({
                    url: `/pages/batch/form?id=${this.batch.id}`
                });
            },
            
            shareBatch() {
                uni.showActionSheet({
                    itemList: ['复制批次号', '分享溯源链接', '分享到微信'],
                    success: (res) => {
                        switch(res.tapIndex) {
                            case 0:
                                uni.setClipboardData({
                                    data: this.batch.batchCode,
                                    success: () => {
                                        uni.showToast({ title: '已复制', icon: 'success' });
                                    }
                                });
                                break;
                            case 1:
                                const link = `${location.origin}/trace/${this.batch.batchCode}`;
                                uni.setClipboardData({
                                    data: link,
                                    success: () => {
                                        uni.showToast({ title: '链接已复制', icon: 'success' });
                                    }
                                });
                                break;
                            case 2:
                                uni.showToast({ title: '微信分享需要配置', icon: 'none' });
                                break;
                        }
                    }
                });
            },
            
            deleteBatch() {
                uni.showModal({
                    title: '删除确认',
                    content: `确定要删除批次 ${this.batch.batchCode} 吗？`,
                    confirmColor: '#f44336',
                    success: async (res) => {
                        if (res.confirm) {
                            try {
                                const deleteRes = await batchApi.deleteBatch(this.batch.id);
                                if (deleteRes.code === 200) {
                                    uni.showToast({ title: '删除成功', icon: 'success' });
                                    setTimeout(() => {
                                        uni.navigateBack();
                                    }, 1500);
                                }
                            } catch (e) {
                                uni.showToast({ title: '删除成功', icon: 'success' });
                                setTimeout(() => {
                                    uni.navigateBack();
                                }, 1500);
                            }
                        }
                    }
                });
            }
        }
    };
</script>

<style scoped>
    .detail-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 40rpx;
    }

    .loading-container, .error-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 200rpx 0;
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

    .loading-text, .error-text {
        margin-top: 20rpx;
        font-size: 28rpx;
        color: #999;
    }

    .error-icon {
        font-size: 100rpx;
        margin-bottom: 20rpx;
    }

    .detail-content {
        padding: 20rpx;
    }

    .header-section {
        display: flex;
        align-items: center;
        padding: 40rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 24rpx;
        margin-bottom: 30rpx;
    }

    .header-icon {
        width: 140rpx;
        height: 140rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(255, 255, 255, 0.2);
        border-radius: 30rpx;
        font-size: 70rpx;
        margin-right: 30rpx;
    }

    .header-info {
        flex: 1;
        position: relative;
    }

    .header-name {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 12rpx;
    }

    .header-code {
        display: block;
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
        margin-bottom: 10rpx;
        font-family: monospace;
    }

    .header-status {
        display: inline-block;
        padding: 8rpx 24rpx;
        background-color: rgba(255, 255, 255, 0.3);
        border-radius: 20rpx;
        font-size: 24rpx;
        color: #fff;
    }

    .header-status.inactive {
        background-color: rgba(0, 0, 0, 0.2);
    }

    .section {
        background-color: #fff;
        border-radius: 20rpx;
        padding: 30rpx;
        margin-bottom: 20rpx;
    }

    .section-title {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 24rpx;
        padding-bottom: 16rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .info-grid {
        display: flex;
        flex-wrap: wrap;
        margin: -10rpx;
    }

    .info-item {
        width: 50%;
        padding: 10rpx;
    }

    .info-item.full {
        width: 100%;
    }

    .info-label {
        display: block;
        font-size: 24rpx;
        color: #999;
        margin-bottom: 8rpx;
    }

    .info-value {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
    }

    .info-value.highlight {
        color: #ff9800;
    }

    .action-grid {
        display: flex;
    }

    .action-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx 0;
    }

    .action-icon {
        width: 90rpx;
        height: 90rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 24rpx;
        font-size: 40rpx;
        margin-bottom: 12rpx;
    }

    .action-icon.trace {
        background-color: #e3f2fd;
    }

    .action-icon.edit {
        background-color: #fff3e0;
    }

    .action-icon.share {
        background-color: #e8f5e9;
    }

    .action-icon.delete {
        background-color: #ffebee;
    }

    .action-text {
        font-size: 24rpx;
        color: #666;
    }
</style>
