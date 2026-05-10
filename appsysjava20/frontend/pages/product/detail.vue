<template>
    <view class="product-detail-page" v-if="product">
        <view class="header-card" :style="{ background: headerBg }">
            <view class="product-icon">🌾</view>
            <text class="product-name">{{ product.name }}</text>
            <text class="product-batch">批次号: {{ product.batchNumber }}</text>
        </view>
        
        <view class="price-section card">
            <view class="price-item">
                <text class="price-label">售价</text>
                <text class="price-value">¥{{ product.unitPrice }}</text>
            </view>
            <view class="price-divider"></view>
            <view class="price-item">
                <text class="price-label">成本</text>
                <text class="price-value cost">¥{{ product.costPrice }}</text>
            </view>
            <view class="price-divider"></view>
            <view class="price-item">
                <text class="price-label">毛利</text>
                <text class="price-value profit">¥{{ profit.toFixed(2) }}</text>
            </view>
        </view>
        
        <view class="info-section card">
            <view class="section-title">📋 产品信息</view>
            
            <view class="info-grid">
                <view class="info-item">
                    <text class="info-label">发芽率</text>
                    <text class="info-value success">{{ product.germinationRate }}%</text>
                </view>
                <view class="info-item">
                    <text class="info-label">库存数量</text>
                    <text class="info-value">{{ product.stockQuantity || 0 }}</text>
                </view>
                <view class="info-item full">
                    <text class="info-label">保质期至</text>
                    <text class="info-value" :class="{ danger: isExpiringSoon }">
                        {{ formatDate(product.expiryDate) }}
                        <text class="expire-tip" v-if="isExpiringSoon">（临期）</text>
                    </text>
                </view>
            </view>
        </view>
        
        <view class="rule-section card">
            <view class="section-title">📐 业务规则校验</view>
            <view class="rule-list">
                <view class="rule-item">
                    <view class="rule-icon" :class="rules.batch ? 'pass' : 'fail'">
                        {{ rules.batch ? '✓' : '✗' }}
                    </view>
                    <view class="rule-content">
                        <text class="rule-title">批次编号</text>
                        <text class="rule-desc">8位数字+字母组合，全局唯一</text>
                    </view>
                </view>
                <view class="rule-item">
                    <view class="rule-icon" :class="rules.expiry ? 'pass' : 'fail'">
                        {{ rules.expiry ? '✓' : '✗' }}
                    </view>
                    <view class="rule-content">
                        <text class="rule-title">保质期规则</text>
                        <text class="rule-desc">不得早于当前日期+6个月</text>
                    </view>
                </view>
                <view class="rule-item">
                    <view class="rule-icon" :class="rules.germination ? 'pass' : 'fail'">
                        {{ rules.germination ? '✓' : '✗' }}
                    </view>
                    <view class="rule-content">
                        <text class="rule-title">发芽率范围</text>
                        <text class="rule-desc">0-100%，精度保留1位小数</text>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="action-bar">
            <button class="btn-outline" @click="editProduct">编辑</button>
            <button class="btn-danger" @click="deleteProduct">删除</button>
        </view>
    </view>
</template>

<script>
import { productApi } from '../../api/index';

export default {
    data() {
        return {
            productId: null,
            product: null,
            headerBg: 'linear-gradient(135deg, #67c23a, #95d475)'
        };
    },
    computed: {
        profit() {
            if (!this.product) return 0;
            return Number(this.product.unitPrice) - Number(this.product.costPrice);
        },
        isExpiringSoon() {
            if (!this.product || !this.product.expiryDate) return false;
            const expiry = new Date(this.product.expiryDate);
            const now = new Date();
            const diffMonths = (expiry.getFullYear() - now.getFullYear()) * 12 + 
                              (expiry.getMonth() - now.getMonth());
            return diffMonths < 9;
        },
        rules() {
            if (!this.product) return { batch: true, expiry: true, germination: true };
            
            const batchValid = /^[A-Za-z0-9]{8}$/.test(this.product.batchNumber);
            
            const minExpiry = new Date();
            minExpiry.setMonth(minExpiry.getMonth() + 6);
            const expiryValid = new Date(this.product.expiryDate) >= minExpiry;
            
            const rate = Number(this.product.germinationRate);
            const germinationValid = rate >= 0 && rate <= 100;
            
            return {
                batch: batchValid,
                expiry: expiryValid,
                germination: germinationValid
            };
        }
    },
    onLoad(options) {
        this.productId = options.id;
        this.loadDetail();
        this.initHeader();
    },
    methods: {
        initHeader() {
            const colors = [
                'linear-gradient(135deg, #67c23a, #95d475)',
                'linear-gradient(135deg, #e6a23c, #f3d19e)',
                'linear-gradient(135deg, #3c9cff, #5ba8ff)',
                'linear-gradient(135deg, #667eea, #764ba2)'
            ];
            this.headerBg = colors[Math.floor(Math.random() * colors.length)];
        },
        
        async loadDetail() {
            try {
                const res = await productApi.getDetail(this.productId);
                if (res.success && res.data) {
                    this.product = res.data;
                }
            } catch (e) {
                console.error('加载产品详情失败', e);
            }
        },
        
        formatDate(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日';
        },
        
        editProduct() {
            uni.navigateTo({ 
                url: '/pages/product/add?id=' + this.productId + '&mode=edit' 
            });
        },
        
        deleteProduct() {
            uni.showModal({
                title: '确认删除',
                content: '确定要删除该产品吗？',
                confirmColor: '#f56c6c',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await productApi.delete(this.productId);
                            uni.showToast({ title: '删除成功', icon: 'success' });
                            setTimeout(() => {
                                uni.navigateBack();
                            }, 1000);
                        } catch (e) {
                            console.error('删除失败', e);
                        }
                    }
                }
            });
        }
    }
};
</script>

<style scoped>
.product-detail-page {
    padding-bottom: 160rpx;
}

.header-card {
    padding: 80rpx 40rpx;
    text-align: center;
}

.product-icon {
    font-size: 100rpx;
    display: block;
    margin-bottom: 20rpx;
}

.product-name {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 12rpx;
}

.product-batch {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
    background: rgba(255, 255, 255, 0.2);
    padding: 8rpx 24rpx;
    border-radius: 20rpx;
}

.price-section {
    margin: -40rpx 30rpx 24rpx;
    position: relative;
    z-index: 10;
    display: flex;
    align-items: center;
}

.price-item {
    flex: 1;
    text-align: center;
    padding: 16rpx 0;
}

.price-label {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 8rpx;
}

.price-value {
    font-size: 32rpx;
    font-weight: bold;
    color: #e6a23c;
}

.price-value.cost { color: #909399; }
.price-value.profit { color: #67c23a; }

.price-divider {
    width: 1rpx;
    height: 60rpx;
    background: #f0f0f0;
}

.info-section {
    margin: 0 30rpx 24rpx;
}

.section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24rpx;
}

.info-item {
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 24rpx;
}

.info-item.full {
    grid-column: span 2;
}

.info-label {
    display: block;
    font-size: 24rpx;
    color: #999;
    margin-bottom: 12rpx;
}

.info-value {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
}

.info-value.success { color: #67c23a; }
.info-value.danger { color: #f56c6c; }

.expire-tip {
    font-size: 22rpx;
    color: #f56c6c;
    margin-left: 8rpx;
}

.rule-section {
    margin: 0 30rpx 24rpx;
}

.rule-list {
    margin-top: 16rpx;
}

.rule-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.rule-item:last-child {
    border-bottom: none;
}

.rule-icon {
    width: 48rpx;
    height: 48rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24rpx;
    font-weight: bold;
    margin-right: 20rpx;
}

.rule-icon.pass {
    background: #f0f9eb;
    color: #67c23a;
}

.rule-icon.fail {
    background: #fef0f0;
    color: #f56c6c;
}

.rule-title {
    display: block;
    font-size: 28rpx;
    color: #333;
    margin-bottom: 4rpx;
}

.rule-desc {
    font-size: 22rpx;
    color: #999;
}

.action-bar {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 24rpx 30rpx;
    background: #fff;
    display: flex;
    gap: 24rpx;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.action-bar button {
    flex: 1;
    margin: 0;
}
</style>
