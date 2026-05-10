<template>
    <view class="customer-detail-page" v-if="customer">
        <view class="header-card">
            <view class="avatar" :style="{ background: avatarBg }">
                <text class="avatar-text">{{ customer.name.charAt(0) }}</text>
            </view>
            <text class="customer-name">{{ customer.name }}</text>
            <view class="tags" v-if="customer.remark">
                <text class="tag">{{ customer.remark }}</text>
            </view>
        </view>
        
        <view class="info-card card">
            <view class="info-item" v-if="customer.phone">
                <text class="info-label">📱 联系电话</text>
                <text class="info-value">{{ customer.phone }}</text>
            </view>
            <view class="info-item" v-if="customer.email">
                <text class="info-label">📧 电子邮箱</text>
                <text class="info-value">{{ customer.email }}</text>
            </view>
            <view class="info-item" v-if="customer.address">
                <text class="info-label">📍 联系地址</text>
                <text class="info-value">{{ customer.address }}</text>
            </view>
            <view class="info-item">
                <text class="info-label">📅 创建时间</text>
                <text class="info-value">{{ formatDate(customer.createTime) }}</text>
            </view>
        </view>
        
        <view class="action-card card">
            <view class="action-grid">
                <view class="action-item" @click="editCustomer">
                    <view class="action-icon edit">✏️</view>
                    <text class="action-text">编辑</text>
                </view>
                <view class="action-item" @click="deleteCustomer">
                    <view class="action-icon delete">🗑️</view>
                    <text class="action-text">删除</text>
                </view>
            </view>
        </view>
    </view>
    
    <view class="loading" v-else>
        <text>加载中...</text>
    </view>
</template>

<script>
import { customerApi } from '../../api/index';

export default {
    data() {
        return {
            customerId: null,
            customer: null,
            avatarBg: 'linear-gradient(135deg, #3c9cff, #5ba8ff)'
        };
    },
    onLoad(options) {
        this.customerId = options.id;
        this.loadDetail();
        this.initAvatar();
    },
    methods: {
        initAvatar() {
            const colors = [
                'linear-gradient(135deg, #3c9cff, #5ba8ff)',
                'linear-gradient(135deg, #67c23a, #95d475)',
                'linear-gradient(135deg, #e6a23c, #f3d19e)',
                'linear-gradient(135deg, #f56c6c, #f78989)',
                'linear-gradient(135deg, #909399, #b6b8bb)',
                'linear-gradient(135deg, #667eea, #764ba2)'
            ];
            this.avatarBg = colors[Math.floor(Math.random() * colors.length)];
        },
        
        async loadDetail() {
            try {
                const res = await customerApi.getDetail(this.customerId);
                if (res.success && res.data) {
                    this.customer = res.data;
                }
            } catch (e) {
                console.error('加载客户详情失败', e);
                uni.showToast({ title: '加载失败', icon: 'none' });
            }
        },
        
        formatDate(dateStr) {
            if (!dateStr) return '-';
            const date = new Date(dateStr);
            return date.toLocaleDateString('zh-CN');
        },
        
        editCustomer() {
            uni.navigateTo({ 
                url: '/pages/customer/add?id=' + this.customerId + '&mode=edit' 
            });
        },
        
        deleteCustomer() {
            uni.showModal({
                title: '确认删除',
                content: '确定要删除该客户吗？此操作不可恢复。',
                confirmColor: '#f56c6c',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await customerApi.delete(this.customerId);
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
.customer-detail-page {
    padding: 0;
}

.header-card {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    padding: 80rpx 40rpx 60rpx;
    text-align: center;
}

.avatar {
    width: 140rpx;
    height: 140rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 24rpx;
    border: 6rpx solid rgba(255, 255, 255, 0.3);
}

.avatar-text {
    font-size: 56rpx;
    font-weight: bold;
    color: #fff;
}

.customer-name {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 16rpx;
}

.tags {
    display: flex;
    justify-content: center;
    gap: 16rpx;
}

.tag {
    background: rgba(255, 255, 255, 0.2);
    color: #fff;
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
}

.info-card {
    margin: -40rpx 30rpx 24rpx;
    position: relative;
    z-index: 10;
}

.info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
    border-bottom: none;
}

.info-label {
    font-size: 28rpx;
    color: #666;
}

.info-value {
    font-size: 28rpx;
    color: #333;
    max-width: 60%;
    text-align: right;
}

.action-card {
    margin: 0 30rpx;
}

.action-grid {
    display: flex;
    gap: 30rpx;
}

.action-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 24rpx 0;
}

.action-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
    margin-bottom: 12rpx;
}

.action-icon.edit { background: #ecf5ff; }
.action-icon.delete { background: #fef0f0; }

.action-text {
    font-size: 26rpx;
    color: #666;
}

.loading {
    text-align: center;
    padding: 100rpx;
    color: #999;
}
</style>
