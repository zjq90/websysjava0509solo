<template>
    <view class="customer-list-page">
        <view class="search-bar">
            <view class="search-input-wrapper">
                <text class="search-icon">🔍</text>
                <input 
                    class="search-input" 
                    type="text" 
                    v-model="searchKeyword"
                    placeholder="搜索客户姓名、电话"
                    @input="handleSearch"
                />
                <text class="clear-icon" v-if="searchKeyword" @click="clearSearch">✕</text>
            </view>
        </view>
        
        <view class="customer-list" v-if="filteredList.length > 0">
            <view 
                class="customer-item card" 
                v-for="(customer, index) in filteredList" 
                :key="customer.id"
                @click="goToDetail(customer.id)"
            >
                <view class="customer-avatar" :style="{ background: getAvatarColor(index) }">
                    <text class="avatar-text">{{ customer.name.charAt(0) }}</text>
                </view>
                <view class="customer-info">
                    <text class="customer-name">{{ customer.name }}</text>
                    <text class="customer-phone" v-if="customer.phone">📱 {{ customer.phone }}</text>
                    <text class="customer-address" v-if="customer.address">📍 {{ customer.address }}</text>
                </view>
                <text class="arrow">›</text>
            </view>
        </view>
        
        <view class="empty-state" v-else>
            <text class="empty-icon">👥</text>
            <text class="empty-text">暂无客户数据</text>
            <button class="btn-primary empty-btn" @click="goToAdd">添加第一个客户</button>
        </view>
        
        <view class="fab-button" @click="goToAdd">
            <text class="fab-icon">+</text>
        </view>
    </view>
</template>

<script>
import { customerApi } from '../../api/index';

export default {
    data() {
        return {
            customerList: [],
            searchKeyword: ''
        };
    },
    computed: {
        filteredList() {
            if (!this.searchKeyword) return this.customerList;
            const keyword = this.searchKeyword.toLowerCase();
            return this.customerList.filter(item => 
                (item.name && item.name.toLowerCase().includes(keyword)) ||
                (item.phone && item.phone.includes(keyword))
            );
        }
    },
    onShow() {
        this.loadCustomers();
    },
    methods: {
        async loadCustomers() {
            try {
                const res = await customerApi.getList();
                if (res.success && res.data) {
                    this.customerList = res.data;
                }
            } catch (e) {
                console.error('加载客户列表失败', e);
            }
        },
        
        handleSearch() {
        },
        
        clearSearch() {
            this.searchKeyword = '';
        },
        
        getAvatarColor(index) {
            const colors = [
                'linear-gradient(135deg, #3c9cff, #5ba8ff)',
                'linear-gradient(135deg, #67c23a, #95d475)',
                'linear-gradient(135deg, #e6a23c, #f3d19e)',
                'linear-gradient(135deg, #f56c6c, #f78989)',
                'linear-gradient(135deg, #909399, #b6b8bb)',
                'linear-gradient(135deg, #667eea, #764ba2)'
            ];
            return colors[index % colors.length];
        },
        
        goToDetail(id) {
            uni.navigateTo({ url: '/pages/customer/detail?id=' + id });
        },
        
        goToAdd() {
            uni.navigateTo({ url: '/pages/customer/add' });
        }
    }
};
</script>

<style scoped>
.customer-list-page {
    padding: 20rpx;
    min-height: 100vh;
}

.search-bar {
    background: #fff;
    border-radius: 16rpx;
    padding: 20rpx;
    margin-bottom: 24rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.search-input-wrapper {
    display: flex;
    align-items: center;
    background: #f5f5f5;
    border-radius: 12rpx;
    padding: 16rpx 24rpx;
}

.search-icon {
    font-size: 28rpx;
    margin-right: 16rpx;
}

.search-input {
    flex: 1;
    font-size: 28rpx;
    background: transparent;
}

.clear-icon {
    font-size: 28rpx;
    color: #999;
    padding: 8rpx;
}

.customer-list {
    padding-bottom: 160rpx;
}

.customer-item {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
}

.customer-avatar {
    width: 96rpx;
    height: 96rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
}

.avatar-text {
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
}

.customer-info {
    flex: 1;
}

.customer-name {
    display: block;
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 8rpx;
}

.customer-phone {
    display: block;
    font-size: 26rpx;
    color: #3c9cff;
    margin-bottom: 4rpx;
}

.customer-address {
    display: block;
    font-size: 24rpx;
    color: #999;
}

.arrow {
    font-size: 40rpx;
    color: #ccc;
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
    margin-bottom: 32rpx;
}

.empty-btn {
    width: 300rpx;
    padding: 20rpx;
}

.fab-button {
    position: fixed;
    right: 40rpx;
    bottom: 60rpx;
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 8rpx 24rpx rgba(60, 156, 255, 0.4);
    z-index: 99;
}

.fab-icon {
    font-size: 60rpx;
    color: #fff;
    font-weight: lighter;
    line-height: 1;
}
</style>
