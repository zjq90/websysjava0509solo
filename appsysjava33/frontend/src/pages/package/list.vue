<template>
    <view class="container">
        <view class="package-list">
            <view class="package-item" v-for="pkg in packages" :key="pkg.id" @click="goDetail(pkg.id)">
                <view class="package-header">
                    <text class="package-name">{{ pkg.name }}</text>
                    <view class="package-tag">基础套餐</view>
                </view>
                <view class="package-bandwidth">
                    <text class="bandwidth-value">{{ pkg.bandwidth }}</text>
                    <text class="bandwidth-unit">Mbps</text>
                </view>
                <view class="package-price">
                    <text class="price-symbol">¥</text>
                    <text class="price-value">{{ pkg.monthlyFee }}</text>
                    <text class="price-unit">/月</text>
                </view>
                <view class="package-desc">{{ pkg.description }}</view>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const packages = ref([])

onMounted(() => {
    loadPackages()
})

const loadPackages = () => {
    packages.value = [
        { id: 1, name: '100M光纤宽带', bandwidth: 100, monthlyFee: 99, description: '适合家庭日常使用' },
        { id: 2, name: '300M光纤宽带', bandwidth: 300, monthlyFee: 159, description: '流畅观看4K视频' },
        { id: 3, name: '500M光纤宽带', bandwidth: 500, monthlyFee: 219, description: '极速上网体验' },
        { id: 4, name: '1000M光纤宽带', bandwidth: 1000, monthlyFee: 299, description: '千兆宽带，企业级体验' }
    ]
}

const goDetail = (id) => {
    uni.navigateTo({
        url: '/pages/package/detail?id=' + id
    })
}
</script>

<style scoped>
.container {
    padding: 20rpx;
}

.package-list {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.package-item {
    background: #FFFFFF;
    border-radius: 20rpx;
    padding: 32rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.package-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
}

.package-name {
    font-size: 36rpx;
    font-weight: 600;
    color: #333333;
}

.package-tag {
    background: #E8F3FF;
    color: #007AFF;
    font-size: 22rpx;
    padding: 6rpx 16rpx;
    border-radius: 20rpx;
}

.package-bandwidth {
    display: flex;
    align-items: baseline;
    margin-bottom: 8rpx;
}

.bandwidth-value {
    font-size: 56rpx;
    font-weight: bold;
    color: #007AFF;
}

.bandwidth-unit {
    font-size: 28rpx;
    color: #999999;
    margin-left: 8rpx;
}

.package-price {
    display: flex;
    align-items: baseline;
    margin-bottom: 16rpx;
}

.price-symbol {
    font-size: 28rpx;
    color: #FF3B30;
}

.price-value {
    font-size: 48rpx;
    font-weight: bold;
    color: #FF3B30;
}

.price-unit {
    font-size: 24rpx;
    color: #999999;
    margin-left: 8rpx;
}

.package-desc {
    font-size: 26rpx;
    color: #666666;
}
</style>
