<template>
    <view class="product-list-page">
        <view class="header-banner">
            <view class="header-content">
                <text class="header-title">📦 产品管理</text>
                <text class="header-desc">共 {{ productList.length }} 个产品</text>
            </view>
            <view class="header-stats">
                <view class="stat-item">
                    <text class="stat-value">{{ totalStock }}</text>
                    <text class="stat-label">总库存</text>
                </view>
            </view>
        </view>
        
        <view class="product-list">
            <view 
                class="product-item card" 
                v-for="(product, index) in productList" 
                :key="product.id"
                @click="goToDetail(product.id)"
            >
                <view class="product-image" :style="{ background: getProductColor(index) }">
                    <text class="product-icon">🌾</text>
                </view>
                <view class="product-info">
                    <text class="product-name">{{ product.name }}</text>
                    <text class="product-batch">批次: {{ product.batchNumber }}</text>
                    <view class="product-specs">
                        <text class="spec-item">🌱 {{ product.germinationRate }}%</text>
                        <text class="spec-item">📅 {{ formatDate(product.expiryDate) }}</text>
                    </view>
                </view>
                <view class="product-right">
                    <text class="product-price">¥{{ product.unitPrice }}</text>
                    <text class="product-stock">库存: {{ product.stockQuantity || 0 }}</text>
                </view>
            </view>
        </view>
        
        <view class="empty-state" v-if="productList.length === 0">
            <text class="empty-icon">📦</text>
            <text class="empty-text">暂无产品数据</text>
        </view>
        
        <view class="fab-button" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>
    </view>
</template>

<script>
import { productApi } from '../../api/index';

export default {
    data() {
        return {
            productList: []
        };
    },
    computed: {
        totalStock() {
            return this.productList.reduce((sum, item) => sum + (item.stockQuantity || 0), 0);
        }
    },
    onShow() {
        this.loadProducts();
    },
    methods: {
        async loadProducts() {
            try {
                const res = await productApi.getList();
                if (res.success && res.data) {
                    this.productList = res.data;
                }
            } catch (e) {
                console.error('加载产品列表失败', e);
            }
        },
        
        getProductColor(index) {
            const colors = [
                'linear-gradient(135deg, #67c23a, #95d475)',
                'linear-gradient(135deg, #e6a23c, #f3d19e)',
                'linear-gradient(135deg, #3c9cff, #5ba8ff)',
                'linear-gradient(135deg, #667eea, #764ba2)',
                'linear-gradient(135deg, #f56c6c, #f78989)'
            ];
            return colors[index % colors.length];
        },
        
        formatDate(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return (date.getMonth() + 1) + '月' + date.getDate() + '日到期';
        },
        
        goToDetail(id) {
            uni.navigateTo({ url: '/pages/product/detail?id=' + id });
        },
        
        goToAdd() {
            uni.navigateTo({ url: '/pages/product/add' });
        }
    }
};
</script>

<style scoped>
.product-list-page {
    padding-bottom: 160rpx;
}

.header-banner {
    background: linear-gradient(135deg, #67c23a, #95d475);
    padding: 60rpx 40rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-title {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.header-desc {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
}

.header-stats {
    display: flex;
}

.stat-item {
    text-align: center;
}

.stat-value {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
}

.stat-label {
    font-size: 22rpx;
    color: rgba(255, 255, 255, 0.8);
}

.product-list {
    padding: 24rpx;
}

.product-item {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
}

.product-image {
    width: 120rpx;
    height: 120rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
}

.product-icon {
    font-size: 56rpx;
}

.product-info {
    flex: 1;
}

.product-name {
    display: block;
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 8rpx;
}

.product-batch {
    display: block;
    font-size: 24rpx;
    color: #667eea;
    font-weight: 500;
    margin-bottom: 12rpx;
}

.product-specs {
    display: flex;
    gap: 20rpx;
}

.spec-item {
    font-size: 22rpx;
    color: #999;
}

.product-right {
    text-align: right;
}

.product-price {
    display: block;
    font-size: 32rpx;
    font-weight: bold;
    color: #e6a23c;
    margin-bottom: 8rpx;
}

.product-stock {
    font-size: 22rpx;
    color: #999;
}

.empty-state {
    text-align: center;
    padding: 160rpx 40rpx;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-text {
    display: block;
    font-size: 28rpx;
    color: #999;
}

.fab-button {
    position: fixed;
    right: 40rpx;
    bottom: 60rpx;
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    background: linear-gradient(135deg, #67c23a, #95d475);
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(103, 194, 58, 0.4);
    z-index: 99;
}

.fab-icon {
    font-size: 60rpx;
    color: #fff;
    font-weight: lighter;
    line-height: 1;
}
</style>
