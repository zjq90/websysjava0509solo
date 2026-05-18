<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="header">
            <text class="title">优惠券中心</text>
        </view>
        
        <view class="tabs">
            <view class="tab" :class="{ active: activeTab === 'available' }" @click="activeTab = 'available'">
                <text>可使用</text>
            </view>
            <view class="tab" :class="{ active: activeTab === 'used' }" @click="activeTab = 'used'">
                <text>已使用</text>
            </view>
            <view class="tab" :class="{ active: activeTab === 'expired' }" @click="activeTab = 'expired'">
                <text>已过期</text>
            </view>
        </view>
        
        <view class="coupon-list" v-if="activeTab === 'available'">
            <view class="coupon-card" v-for="coupon in availableCoupons" :key="coupon.id">
                <view class="coupon-left">
                    <text class="coupon-price" v-if="coupon.couponType === 'amount'">
                        ¥{{ coupon.discountValue }}
                    </text>
                    <text class="coupon-price" v-else>
                        {{ coupon.discountValue }}折
                    </text>
                    <text class="coupon-condition">满{{ coupon.minAmount }}可用</text>
                </view>
                <view class="coupon-right">
                    <text class="coupon-name">{{ coupon.name }}</text>
                    <text class="coupon-time">有效期：{{ formatDate(coupon.validStartTime) }} - {{ formatDate(coupon.validEndTime) }}</text>
                    <button class="use-btn" @click="useCoupon(coupon)">立即使用</button>
                </view>
            </view>
            
            <view class="empty" v-if="availableCoupons.length === 0">
                <text class="empty-icon">🎫</text>
                <text class="empty-text">暂无可用优惠券</text>
            </view>
        </view>
        
        <view class="coupon-list" v-if="activeTab === 'used'">
            <view class="coupon-card used" v-for="coupon in usedCoupons" :key="coupon.id">
                <view class="coupon-left">
                    <text class="coupon-price" v-if="coupon.couponType === 'amount'">
                        ¥{{ coupon.discountValue }}
                    </text>
                    <text class="coupon-price" v-else>
                        {{ coupon.discountValue }}折
                    </text>
                    <text class="coupon-condition">满{{ coupon.minAmount }}可用</text>
                </view>
                <view class="coupon-right">
                    <text class="coupon-name">{{ coupon.name }}</text>
                    <text class="coupon-time">有效期：{{ formatDate(coupon.validStartTime) }} - {{ formatDate(coupon.validEndTime) }}</text>
                    <text class="used-tag">已使用</text>
                </view>
            </view>
            
            <view class="empty" v-if="usedCoupons.length === 0">
                <text class="empty-icon">🎫</text>
                <text class="empty-text">暂无已使用优惠券</text>
            </view>
        </view>
        
        <view class="coupon-list" v-if="activeTab === 'expired'">
            <view class="coupon-card expired" v-for="coupon in expiredCoupons" :key="coupon.id">
                <view class="coupon-left">
                    <text class="coupon-price" v-if="coupon.couponType === 'amount'">
                        ¥{{ coupon.discountValue }}
                    </text>
                    <text class="coupon-price" v-else>
                        {{ coupon.discountValue }}折
                    </text>
                    <text class="coupon-condition">满{{ coupon.minAmount }}可用</text>
                </view>
                <view class="coupon-right">
                    <text class="coupon-name">{{ coupon.name }}</text>
                    <text class="coupon-time">有效期：{{ formatDate(coupon.validStartTime) }} - {{ formatDate(coupon.validEndTime) }}</text>
                    <text class="expired-tag">已过期</text>
                </view>
            </view>
            
            <view class="empty" v-if="expiredCoupons.length === 0">
                <text class="empty-icon">🎫</text>
                <text class="empty-text">暂无已过期优惠券</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            activeTab: 'available',
            availableCoupons: [],
            usedCoupons: [],
            expiredCoupons: [],
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
        this.loadCoupons()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    methods: {
        loadCoupons() {
            this.$request.get('/activity/user-coupons').then(res => {
                this.availableCoupons = res.filter(c => c.status === 'available')
                this.usedCoupons = res.filter(c => c.status === 'used')
                this.expiredCoupons = res.filter(c => c.status === 'expired')
            }).catch(err => {
                console.error(err)
            })
        },
        
        useCoupon(coupon) {
            uni.navigateTo({
                url: '/pages/product/list'
            })
        },
        
        formatDate(dateStr) {
            if (!dateStr) return ''
            const date = new Date(dateStr)
            return `${date.getMonth() + 1}/${date.getDate()}`
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background: #f5f5f5;
}

.header {
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    padding: 40rpx 30rpx;
    text-align: center;
}

.title {
    font-size: 34rpx;
    font-weight: 500;
    color: #fff;
}

.tabs {
    display: flex;
    background: #fff;
    padding: 0 30rpx;
}

.tab {
    flex: 1;
    text-align: center;
    padding: 25rpx 0;
    font-size: 28rpx;
    color: #666;
    position: relative;
}

.tab.active {
    color: #FF6B6B;
    font-weight: 500;
}

.tab.active::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 60rpx;
    height: 4rpx;
    background: #FF6B6B;
    border-radius: 2rpx;
}

.coupon-list {
    padding: 20rpx 30rpx;
}

.coupon-card {
    display: flex;
    background: #fff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
}

.coupon-card.used,
.coupon-card.expired {
    opacity: 0.7;
}

.coupon-left {
    width: 180rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    padding: 30rpx 20rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
}

.coupon-left::before,
.coupon-left::after {
    content: '';
    position: absolute;
    right: 0;
    width: 20rpx;
    height: 20rpx;
    background: #f5f5f5;
    border-radius: 50%;
    transform: translateX(50%);
}

.coupon-left::before {
    top: -10rpx;
}

.coupon-left::after {
    bottom: -10rpx;
}

.coupon-price {
    font-size: 44rpx;
    font-weight: bold;
    color: #fff;
}

.coupon-condition {
    font-size: 20rpx;
    color: rgba(255, 255, 255, 0.9);
    margin-top: 8rpx;
}

.coupon-right {
    flex: 1;
    padding: 25rpx 30rpx;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.coupon-name {
    font-size: 30rpx;
    font-weight: 500;
    color: #333;
}

.coupon-time {
    font-size: 22rpx;
    color: #999;
    margin-top: 10rpx;
}

.use-btn {
    width: 160rpx;
    height: 60rpx;
    line-height: 60rpx;
    background: #FF6B6B;
    color: #fff;
    border-radius: 30rpx;
    font-size: 24rpx;
    border: none;
    padding: 0;
    margin-top: 15rpx;
    align-self: flex-start;
}

.used-tag,
.expired-tag {
    font-size: 24rpx;
    color: #999;
    margin-top: 15rpx;
}

.empty {
    text-align: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 100rpx;
    display: block;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
    color: #999;
}
</style>