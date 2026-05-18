<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="search-bar">
            <input class="search-input" placeholder="搜索鲜花..." v-model="keyword" @confirm="searchProduct" />
            <button class="search-btn" @click="searchProduct">搜索</button>
        </view>
        
        <scroll-view class="banner" scroll-x>
            <view class="banner-item" v-for="i in 3" :key="i">
                <view class="banner-img" :style="{background: bannerColors[i-1]}">
                    <text class="banner-text">{{ bannerTexts[i-1] }}</text>
                </view>
            </view>
        </scroll-view>
        
        <view class="section">
            <view class="section-header">
                <text class="section-title">🔥 热门推荐</text>
                <text class="section-more" @click="goToList('hot')">查看更多 ></text>
            </view>
            <view class="product-list">
                <view class="product-item" v-for="item in hotProducts" :key="item.id" @click="goToDetail(item.id)">
                    <view class="product-img" :style="{background: getRandomColor()}">
                        <text class="product-emoji">💐</text>
                    </view>
                    <view class="product-info">
                        <text class="product-name">{{ item.name }}</text>
                        <text class="product-desc text-sm text-muted">{{ item.description }}</text>
                        <view class="product-price">
                            <text class="price-current text-primary font-bold">¥{{ item.price }}</text>
                            <text class="price-original text-sm line-through text-muted">¥{{ item.originalPrice }}</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="section">
            <view class="section-header">
                <text class="section-title">✨ 新品上架</text>
                <text class="section-more" @click="goToList('new')">查看更多 ></text>
            </view>
            <view class="product-list">
                <view class="product-item" v-for="item in newProducts" :key="item.id" @click="goToDetail(item.id)">
                    <view class="product-img" :style="{background: getRandomColor()}">
                        <text class="product-emoji">🌸</text>
                    </view>
                    <view class="product-info">
                        <text class="product-name">{{ item.name }}</text>
                        <text class="product-desc text-sm text-muted">{{ item.description }}</text>
                        <view class="product-price">
                            <text class="price-current text-primary font-bold">¥{{ item.price }}</text>
                            <text class="price-original text-sm line-through text-muted">¥{{ item.originalPrice }}</text>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="section">
            <view class="section-header">
                <text class="section-title">🎫 优惠券</text>
                <text class="section-more" @click="goToCoupon">查看更多 ></text>
            </view>
            <view class="coupon-list">
                <view class="coupon-item" v-for="item in coupons" :key="item.id">
                    <view class="coupon-left">
                        <text class="coupon-value text-primary font-bold">¥{{ item.value }}</text>
                        <text class="coupon-condition text-sm text-muted">满{{ item.minAmount }}可用</text>
                    </view>
                    <view class="coupon-right">
                        <text class="coupon-name">{{ item.name }}</text>
                        <text class="coupon-desc text-sm text-muted">{{ item.description }}</text>
                    </view>
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
            hotProducts: [],
            newProducts: [],
            coupons: [],
            bannerColors: ['linear-gradient(135deg, #FF6B6B, #FFE66D)', 'linear-gradient(135deg, #4ECDC4, #44A08D)', 'linear-gradient(135deg, #667eea, #764ba2)'],
            bannerTexts: ['情人节特惠 浪漫鲜花5折起', '母亲节专场 给妈妈最好的爱', '新用户注册立享20元优惠券'],
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
        this.loadHotProducts()
        this.loadNewProducts()
        this.loadCoupons()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        searchProduct() {
            if (this.keyword.trim()) {
                uni.navigateTo({
                    url: `/pages/product/list?keyword=${this.keyword}`
                })
            }
        },
        
        goToList(type) {
            uni.navigateTo({
                url: `/pages/product/list?type=${type}`
            })
        },
        
        goToDetail(id) {
            uni.navigateTo({
                url: `/pages/product/detail?id=${id}`
            })
        },
        
        goToCoupon() {
            uni.navigateTo({
                url: '/pages/coupon/list'
            })
        },
        
        loadHotProducts() {
            this.$request.get('/product/hot').then(res => {
                this.hotProducts = res
            }).catch(err => {
                console.error(err)
            })
        },
        
        loadNewProducts() {
            this.$request.get('/product/new').then(res => {
                this.newProducts = res
            }).catch(err => {
                console.error(err)
            })
        },
        
        loadCoupons() {
            this.$request.get('/coupon/list', { page: 0, size: 3 }).then(res => {
                this.coupons = res.content || []
            }).catch(err => {
                console.error(err)
            })
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
}

.search-bar {
    display: flex;
    padding: 20rpx;
    background: #fff;
    gap: 20rpx;
}

.search-input {
    flex: 1;
    height: 70rpx;
    background: #f5f5f5;
    border-radius: 35rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
}

.search-btn {
    width: 120rpx;
    height: 70rpx;
    line-height: 70rpx;
    background: #FF6B6B;
    color: #fff;
    border-radius: 35rpx;
    font-size: 28rpx;
    padding: 0;
    border: none;
}

.banner {
    white-space: nowrap;
    padding: 20rpx 0;
    background: #fff;
}

.banner-item {
    display: inline-block;
    width: 90%;
    margin: 0 20rpx;
}

.banner-img {
    height: 280rpx;
    border-radius: 20rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.banner-text {
    color: #fff;
    font-size: 36rpx;
    font-weight: bold;
}

.section {
    background: #fff;
    margin-top: 20rpx;
    padding: 20rpx;
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
}

.section-more {
    font-size: 24rpx;
    color: #999;
}

.product-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.product-item {
    width: calc(50% - 10rpx);
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.08);
}

.product-img {
    height: 240rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-emoji {
    font-size: 100rpx;
}

.product-info {
    padding: 20rpx;
}

.product-name {
    font-size: 28rpx;
    font-weight: 500;
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.product-desc {
    display: block;
    margin-top: 10rpx;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.product-price {
    margin-top: 15rpx;
    display: flex;
    align-items: baseline;
    gap: 10rpx;
}

.price-current {
    font-size: 32rpx;
}

.price-original {
    font-size: 24rpx;
}

.coupon-list {
    display: flex;
    flex-direction: column;
    gap: 20rpx;
}

.coupon-item {
    display: flex;
    background: linear-gradient(135deg, #fff5f5, #fff);
    border-radius: 16rpx;
    overflow: hidden;
    border: 2rpx dashed #FF6B6B;
}

.coupon-left {
    width: 180rpx;
    padding: 30rpx 20rpx;
    text-align: center;
    border-right: 2rpx dashed #FF6B6B;
}

.coupon-value {
    font-size: 40rpx;
}

.coupon-condition {
    display: block;
    margin-top: 10rpx;
}

.coupon-right {
    flex: 1;
    padding: 20rpx 30rpx;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.coupon-name {
    font-size: 28rpx;
    font-weight: 500;
}

.coupon-desc {
    margin-top: 10rpx;
}
</style>