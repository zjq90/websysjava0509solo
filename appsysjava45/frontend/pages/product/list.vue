<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="search-bar">
            <input class="search-input" placeholder="搜索鲜花..." v-model="keyword" @confirm="loadProducts" />
            <button class="search-btn" @click="loadProducts">搜索</button>
        </view>
        
        <scroll-view class="category-scroll" scroll-x>
            <view class="category-item" 
                  :class="{ active: currentCategory === '' }" 
                  @click="selectCategory('')">
                全部
            </view>
            <view class="category-item" 
                  :class="{ active: currentCategory === '玫瑰' }" 
                  @click="selectCategory('玫瑰')">
                玫瑰
            </view>
            <view class="category-item" 
                  :class="{ active: currentCategory === '百合' }" 
                  @click="selectCategory('百合')">
                百合
            </view>
            <view class="category-item" 
                  :class="{ active: currentCategory === '康乃馨' }" 
                  @click="selectCategory('康乃馨')">
                康乃馨
            </view>
            <view class="category-item" 
                  :class="{ active: currentCategory === '混搭花束' }" 
                  @click="selectCategory('混搭花束')">
                混搭花束
            </view>
        </scroll-view>
        
        <view class="product-grid">
            <view class="product-card" v-for="item in products" :key="item.id" @click="goToDetail(item.id)">
                <view class="product-img" :style="{background: getRandomColor()}">
                    <text class="product-emoji">💐</text>
                </view>
                <view class="product-info">
                    <text class="product-name">{{ item.name }}</text>
                    <text class="product-desc text-sm text-muted">{{ item.description }}</text>
                    <view class="product-bottom">
                        <view class="price-box">
                            <text class="price-current text-primary font-bold">¥{{ item.price }}</text>
                            <text class="price-original text-sm line-through text-muted">¥{{ item.originalPrice }}</text>
                        </view>
                        <view class="sales text-sm text-muted">已售{{ item.sales || 0 }}件</view>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="load-more" v-if="hasMore" @click="loadMore">
            <text>加载更多</text>
        </view>
        <view class="no-more" v-else-if="products.length > 0">
            <text>没有更多了</text>
        </view>
        <view class="empty" v-if="products.length === 0 && !loading">
            <text class="empty-icon">🌸</text>
            <text class="empty-text">暂无相关商品</text>
        </view>
        
        <view class="loading" v-if="loading">
            <text>加载中...</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            keyword: '',
            currentCategory: '',
            products: [],
            page: 0,
            size: 10,
            hasMore: true,
            loading: false,
            elderMode: false
        }
    },
    onLoad(options) {
        this.elderMode = getApp().globalData.elderMode
        if (options.keyword) {
            this.keyword = options.keyword
        }
        this.loadProducts()
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    methods: {
        getRandomColor() {
            const colors = ['#FFE4E1', '#FFF0F5', '#E6E6FA', '#F0FFF0', '#FFF8DC', '#FFE4B5', '#E0FFFF']
            return colors[Math.floor(Math.random() * colors.length)]
        },
        
        selectCategory(category) {
            this.currentCategory = category
            this.page = 0
            this.products = []
            this.hasMore = true
            this.loadProducts()
        },
        
        loadProducts() {
            if (this.loading) return
            this.loading = true
            
            const params = {
                page: this.page,
                size: this.size
            }
            if (this.currentCategory) {
                params.category = this.currentCategory
            }
            if (this.keyword) {
                params.keyword = this.keyword
            }
            
            this.$request.get('/product/list', params).then(res => {
                if (this.page === 0) {
                    this.products = res.content
                } else {
                    this.products = this.products.concat(res.content)
                }
                this.hasMore = !res.last
                this.loading = false
            }).catch(err => {
                this.loading = false
                console.error(err)
            })
        },
        
        loadMore() {
            if (this.hasMore && !this.loading) {
                this.page++
                this.loadProducts()
            }
        },
        
        goToDetail(id) {
            uni.navigateTo({
                url: `/pages/product/detail?id=${id}`
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

.category-scroll {
    white-space: nowrap;
    padding: 20rpx;
    background: #fff;
    margin-top: 2rpx;
}

.category-item {
    display: inline-block;
    padding: 15rpx 30rpx;
    margin-right: 20rpx;
    border-radius: 30rpx;
    background: #f5f5f5;
    font-size: 26rpx;
    color: #666;
}

.category-item.active {
    background: #FF6B6B;
    color: #fff;
}

.product-grid {
    padding: 20rpx;
    display: flex;
    flex-wrap: wrap;
    gap: 20rpx;
}

.product-card {
    width: calc(50% - 10rpx);
    background: #fff;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.08);
}

.product-img {
    height: 280rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.product-emoji {
    font-size: 120rpx;
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

.product-bottom {
    margin-top: 15rpx;
    display: flex;
    justify-content: space-between;
    align-items: baseline;
}

.price-box {
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

.load-more, .no-more {
    padding: 30rpx;
    text-align: center;
    color: #999;
    font-size: 26rpx;
}

.empty {
    padding: 100rpx 0;
    text-align: center;
}

.empty-icon {
    font-size: 120rpx;
    display: block;
}

.empty-text {
    display: block;
    margin-top: 20rpx;
    color: #999;
    font-size: 28rpx;
}

.loading {
    padding: 30rpx;
    text-align: center;
    color: #999;
    font-size: 26rpx;
}
</style>