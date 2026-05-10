<template>
    <view class="mine-page">
        <view class="header-card">
            <view class="user-info">
                <view class="avatar">
                    <text class="avatar-text">{{ userInfo.name ? userInfo.name.charAt(0) : 'U' }}</text>
                </view>
                <view class="user-detail">
                    <text class="user-name">{{ userInfo.name || '未登录' }}</text>
                    <text class="user-role">{{ userInfo.role || '-' }}</text>
                    <view class="user-tags">
                        <text class="tag" :class="{ manager: userInfo.isManager }">
                            {{ userInfo.isManager ? '🏆 管理人员' : '👤 普通员工' }}
                        </text>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="quick-stats card">
            <view class="stats-title">📊 本月数据</view>
            <view class="stats-grid">
                <view class="stat-item" @click="goToPerformance">
                    <text class="stat-value">💰</text>
                    <text class="stat-label">我的业绩</text>
                </view>
                <view class="stat-item" @click="goToOrders">
                    <text class="stat-value">🧾</text>
                    <text class="stat-label">我的订单</text>
                </view>
                <view class="stat-item" @click="goToCommission">
                    <text class="stat-value">💎</text>
                    <text class="stat-label">提成计算</text>
                </view>
                <view class="stat-item" @click="goToCustomers">
                    <text class="stat-value">👥</text>
                    <text class="stat-label">我的客户</text>
                </view>
            </view>
        </view>
        
        <view class="menu-list card">
            <view class="menu-title">⚙️ 系统功能</view>
            
            <view class="menu-item" @click="goToCustomers">
                <text class="menu-icon">👥</text>
                <text class="menu-text">客户管理</text>
                <text class="menu-arrow">›</text>
            </view>
            
            <view class="menu-item" @click="goToProducts">
                <text class="menu-icon">📦</text>
                <text class="menu-text">产品管理</text>
                <text class="menu-arrow">›</text>
            </view>
            
            <view class="menu-item" @click="goToOrders">
                <text class="menu-icon">🧾</text>
                <text class="menu-text">订单管理</text>
                <text class="menu-arrow">›</text>
            </view>
            
            <view class="menu-item" v-if="userInfo.isManager" @click="goToDashboard">
                <text class="menu-icon">📊</text>
                <text class="menu-text">经营看板</text>
                <text class="menu-arrow">›</text>
            </view>
        </view>
        
        <view class="menu-list card">
            <view class="menu-title">🔧 测试与帮助</view>
            
            <view class="menu-item" @click="showTestFeatures">
                <text class="menu-icon">🧪</text>
                <text class="menu-text">业务规则测试</text>
                <text class="menu-arrow">›</text>
            </view>
            
            <view class="menu-item" @click="showApiDocs">
                <text class="menu-icon">📖</text>
                <text class="menu-text">API接口文档</text>
                <text class="menu-arrow">›</text>
            </view>
            
            <view class="menu-item" @click="showAbout">
                <text class="menu-icon">ℹ️</text>
                <text class="menu-text">关于系统</text>
                <text class="menu-arrow">›</text>
            </view>
        </view>
        
        <view class="logout-btn" @click="handleLogout" v-if="userInfo.name">
            <text>退出登录</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            userInfo: {
                name: '',
                role: '',
                isManager: false
            }
        };
    },
    onShow() {
        this.loadUserInfo();
    },
    methods: {
        loadUserInfo() {
            const user = uni.getStorageSync('currentUser');
            if (user) {
                this.userInfo = user;
            }
        },
        
        goToPerformance() {
            uni.switchTab({ url: '/pages/performance/performance' });
        },
        
        goToDashboard() {
            uni.switchTab({ url: '/pages/dashboard/dashboard' });
        },
        
        goToCustomers() {
            uni.navigateTo({ url: '/pages/customer/list' });
        },
        
        goToProducts() {
            uni.navigateTo({ url: '/pages/product/list' });
        },
        
        goToOrders() {
            uni.navigateTo({ url: '/pages/order/list' });
        },
        
        goToCommission() {
            uni.showModal({
                title: '提成计算说明',
                content: '提成金额 = 已回款金额 × 提成比例\n\n示例：\n已回款：¥10,000\n提成比例：2.5%\n提成金额：¥250.00',
                showCancel: false
            });
        },
        
        showTestFeatures() {
            uni.showActionSheet({
                itemList: [
                    '手机号格式验证',
                    '批次编号格式验证',
                    '保质期规则验证',
                    '发芽率范围验证',
                    'AES加密演示'
                ],
                success: (res) => {
                    this.runTest(res.tapIndex);
                }
            });
        },
        
        runTest(index) {
            const tests = [
                {
                    title: '📱 手机号格式验证',
                    content: '中国大陆手机号规则：\n• 11位数字\n• 以1开头\n\n正确示例：13812345678\n错误示例：12345678901 (第二位错误)\n错误示例：1381234567 (位数不足)'
                },
                {
                    title: '🔢 批次编号验证',
                    content: '批次编号规则：\n• 8位字符\n• 仅允许数字和字母\n• 全局唯一\n\n正确示例：BAT001A1\n错误示例：BAT001 (不足8位)\n错误示例：BAT@001A (含特殊字符)'
                },
                {
                    title: '📅 保质期规则',
                    content: '保质期规则：\n• 不得早于当前日期+6个月\n\n验证会在创建/编辑产品时自动执行。'
                },
                {
                    title: '🌱 发芽率范围',
                    content: '发芽率规则：\n• 数值范围：0-100%\n• 精度：保留1位小数\n\n正确示例：95.5%\n错误示例：100.1% (超出范围)'
                },
                {
                    title: '🔐 AES-256加密',
                    content: '数据加密保护：\n• 客户手机号\n• 客户邮箱\n• 员工电话\n• 财务数据\n\n敏感信息在数据库中以加密形式存储，读取时自动解密。'
                }
            ];
            
            const test = tests[index];
            uni.showModal({
                title: test.title,
                content: test.content,
                showCancel: false,
                confirmText: '知道了'
            });
        },
        
        showApiDocs() {
            uni.showModal({
                title: '📖 API接口文档',
                content: '后端Swagger接口文档地址：\n\nhttp://localhost:8080/swagger-ui.html\n\n可在浏览器中查看和测试所有API接口。',
                showCancel: false,
                confirmText: '知道了'
            });
        },
        
        showAbout() {
            uni.showModal({
                title: 'ℹ️ 关于系统',
                content: '财务与报表管理系统\n\n版本：1.0.0\n技术栈：\n后端：Spring Boot + JPA + H2\n前端：UniApp (Vue 2)\n\n功能特性：\n• 个人业绩与提成计算\n• 管理层经营看板\n• 客户/产品/订单管理\n• AES-256数据加密',
                showCancel: false,
                confirmText: '知道了'
            });
        },
        
        handleLogout() {
            uni.showModal({
                title: '确认退出',
                content: '确定要退出登录吗？',
                success: (res) => {
                    if (res.confirm) {
                        uni.removeStorageSync('currentUser');
                        uni.reLaunch({ url: '/pages/login/login' });
                    }
                }
            });
        }
    }
};
</script>

<style scoped>
.mine-page {
    min-height: 100vh;
    padding-bottom: 40rpx;
}

.header-card {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    padding: 80rpx 40rpx 60rpx;
}

.user-info {
    display: flex;
    align-items: center;
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 32rpx;
    border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.avatar-text {
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
}

.user-name {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.user-role {
    display: block;
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-bottom: 16rpx;
}

.user-tags {
    display: flex;
    gap: 16rpx;
}

.tag {
    background: rgba(255, 255, 255, 0.2);
    color: #fff;
    padding: 6rpx 20rpx;
    border-radius: 20rpx;
    font-size: 22rpx;
}

.tag.manager {
    background: rgba(255, 215, 0, 0.3);
}

.quick-stats {
    margin: -30rpx 30rpx 24rpx;
    position: relative;
    z-index: 10;
}

.stats-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20rpx;
}

.stat-item {
    text-align: center;
    padding: 16rpx 0;
}

.stat-value {
    display: block;
    font-size: 40rpx;
    margin-bottom: 8rpx;
}

.stat-label {
    font-size: 22rpx;
    color: #999;
}

.menu-list {
    margin: 0 30rpx 24rpx;
}

.menu-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 28rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-icon {
    font-size: 36rpx;
    margin-right: 20rpx;
}

.menu-text {
    flex: 1;
    font-size: 28rpx;
    color: #333;
}

.menu-arrow {
    font-size: 36rpx;
    color: #ccc;
}

.logout-btn {
    margin: 40rpx 30rpx;
    padding: 28rpx;
    background: #fff;
    border-radius: 16rpx;
    text-align: center;
    font-size: 30rpx;
    color: #f56c6c;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}
</style>
