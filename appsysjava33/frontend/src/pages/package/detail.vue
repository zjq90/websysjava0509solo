<template>
    <view class="container">
        <view class="package-header">
            <text class="package-name">{{ packageInfo.name }}</text>
            <view class="package-bandwidth">
                <text class="bandwidth-value">{{ packageInfo.bandwidth }}</text>
                <text class="bandwidth-unit">Mbps</text>
            </view>
            <view class="package-price">
                <text class="price-symbol">¥</text>
                <text class="price-value">{{ packageInfo.monthlyFee }}</text>
                <text class="price-unit">/月</text>
            </view>
        </view>

        <view class="package-features">
            <text class="section-title">套餐特点</text>
            <view class="feature-item" v-for="(f, i) in packageInfo.features" :key="i">
                <text class="feature-icon">✓</text>
                <text class="feature-text">{{ f }}</text>
            </view>
        </view>

        <button class="btn-primary" @click="handleOrder">立即办理</button>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const packageInfo = ref({
    id: 0,
    name: '',
    bandwidth: 0,
    monthlyFee: 0,
    features: []
})

onMounted(() => {
    loadPackageDetail()
})

const loadPackageDetail = () => {
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    const id = currentPage.options.id
    
    const packages = {
        1: { id: 1, name: '100M光纤宽带', bandwidth: 100, monthlyFee: 99, features: ['光猫免费租用', '免费安装', '24小时客服支持'] },
        2: { id: 2, name: '300M光纤宽带', bandwidth: 300, monthlyFee: 159, features: ['光猫免费租用', '免费安装', '24小时客服支持', '免费提速'] },
        3: { id: 3, name: '500M光纤宽带', bandwidth: 500, monthlyFee: 219, features: ['光猫免费租用', '免费安装', '24小时客服支持', '专属客服'] },
        4: { id: 4, name: '1000M光纤宽带', bandwidth: 1000, monthlyFee: 299, features: ['光猫免费租用', '免费安装', '24小时客服支持', '专属客服', '上门维修'] }
    }
    
    packageInfo.value = packages[id] || packages[1]
}

const handleOrder = () => {
    uni.navigateTo({
        url: '/pages/order/install?packageId=' + packageInfo.value.id
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.package-header {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 20rpx;
    padding: 40rpx;
    color: #FFFFFF;
    margin-bottom: 30rpx;
}

.package-name {
    display: block;
    font-size: 36rpx;
    font-weight: 600;
    margin-bottom: 20rpx;
}

.package-bandwidth {
    display: flex;
    align-items: baseline;
    margin-bottom: 12rpx;
}

.bandwidth-value {
    font-size: 72rpx;
    font-weight: bold;
}

.bandwidth-unit {
    font-size: 28rpx;
    opacity: 0.8;
    margin-left: 8rpx;
}

.package-price {
    display: flex;
    align-items: baseline;
}

.price-symbol {
    font-size: 28rpx;
}

.price-value {
    font-size: 56rpx;
    font-weight: bold;
}

.price-unit {
    font-size: 24rpx;
    opacity: 0.8;
    margin-left: 8rpx;
}

.package-features {
    background: #FFFFFF;
    border-radius: 20rpx;
    padding: 32rpx;
    margin-bottom: 40rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.section-title {
    display: block;
    font-size: 32rpx;
    font-weight: 600;
    color: #333333;
    margin-bottom: 24rpx;
}

.feature-item {
    display: flex;
    align-items: center;
    padding: 16rpx 0;
    border-bottom: 1rpx solid #F5F5F5;
}

.feature-icon {
    color: #34C759;
    font-size: 32rpx;
    margin-right: 16rpx;
}

.feature-text {
    font-size: 28rpx;
    color: #666666;
}

.btn-primary {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}
</style>
