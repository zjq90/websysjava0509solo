<template>
    <view class="home-page">
        <view class="header">
            <view class="header-content">
                <view class="user-info">
                    <text class="greeting">👋 {{ greetingText }}</text>
                    <text class="username">{{ currentUser.name }}</text>
                    <text class="user-role">{{ currentUser.role }}</text>
                </view>
                <view class="header-actions">
                    <text class="date-text">{{ currentDate }}</text>
                </view>
            </view>
        </view>
        
        <view class="quick-stats card">
            <view class="card-title">📊 本月概览</view>
            <view class="stats-grid">
                <view class="stat-item">
                    <text class="stat-value">¥{{ formatNumber(quickStats.sales) }}</text>
                    <text class="stat-label">销售额</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value">¥{{ formatNumber(quickStats.received) }}</text>
                    <text class="stat-label">已回款</text>
                </view>
                <view class="stat-item">
                    <text class="stat-value">{{ quickStats.orders }}单</text>
                    <text class="stat-label">订单数</text>
                </view>
            </view>
        </view>
        
        <view class="function-entries card">
            <view class="card-title">🎯 功能入口</view>
            <view class="entries-grid">
                <view class="entry-item" @click="goToPerformance">
                    <view class="entry-icon entry-blue">📈</view>
                    <text class="entry-text">个人业绩</text>
                </view>
                <view class="entry-item" @click="goToDashboard" v-if="currentUser.isManager">
                    <view class="entry-icon entry-purple">📊</view>
                    <text class="entry-text">经营看板</text>
                </view>
                <view class="entry-item" @click="goToCustomers">
                    <view class="entry-icon entry-green">👥</view>
                    <text class="entry-text">客户管理</text>
                </view>
                <view class="entry-item" @click="goToProducts">
                    <view class="entry-icon entry-orange">📦</view>
                    <text class="entry-text">产品管理</text>
                </view>
                <view class="entry-item" @click="goToOrders">
                    <view class="entry-icon entry-red">🧾</view>
                    <text class="entry-text">订单管理</text>
                </view>
                <view class="entry-item" @click="showTestFeatures">
                    <view class="entry-icon entry-gray">🔧</view>
                    <text class="entry-text">测试功能</text>
                </view>
            </view>
        </view>
        
        <view class="recent-orders card">
            <view class="card-title flex-between">
                <text>🔔 最近订单</text>
                <text class="more-link" @click="goToOrders">查看全部</text>
            </view>
            <view class="orders-list">
                <view class="order-item" v-for="(order, index) in recentOrders" :key="index">
                    <view class="order-header flex-between">
                        <text class="order-no">{{ order.orderNo }}</text>
                        <text class="status-tag" :class="'status-' + order.status.toLowerCase()">
                            {{ getStatusText(order.status) }}
                        </text>
                    </view>
                    <view class="order-info">
                        <text class="info-item">金额: ¥{{ formatNumber(order.totalAmount) }}</text>
                        <text class="info-item">回款: ¥{{ formatNumber(order.paidAmount) }}</text>
                    </view>
                </view>
                <view class="empty-state" v-if="recentOrders.length === 0">
                    <text class="empty-icon">📋</text>
                    <text class="empty-text">暂无订单数据</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import { financeApi } from '../../api/index';

export default {
    data() {
        return {
            currentUser: {
                id: 2,
                name: '销售员A',
                role: '普通员工',
                isManager: false
            },
            currentDate: '',
            greetingText: '',
            quickStats: {
                sales: '0.00',
                received: '0.00',
                orders: 0
            },
            recentOrders: []
        };
    },
    onShow() {
        this.initData();
    },
    onLoad() {
        this.initDate();
    },
    methods: {
        initDate() {
            const now = new Date();
            const month = now.getMonth() + 1;
            const day = now.getDate();
            const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
            const weekDay = weekDays[now.getDay()];
            this.currentDate = `${month}月${day}日 ${weekDay}`;
            
            const hour = now.getHours();
            if (hour < 6) this.greetingText = '夜深了';
            else if (hour < 9) this.greetingText = '早上好';
            else if (hour < 12) this.greetingText = '上午好';
            else if (hour < 14) this.greetingText = '中午好';
            else if (hour < 18) this.greetingText = '下午好';
            else this.greetingText = '晚上好';
        },
        
        initData() {
            const user = uni.getStorageSync('currentUser');
            if (user) {
                this.currentUser = user;
            }
            
            this.loadPerformanceData();
            this.loadRecentOrders();
        },
        
        async loadPerformanceData() {
            try {
                const res = await financeApi.getPerformance(this.currentUser.id);
                if (res.success && res.data) {
                    this.quickStats.sales = res.data.totalSalesAmount || '0.00';
                    this.quickStats.received = res.data.totalReceivedAmount || '0.00';
                    this.quickStats.orders = res.data.orders ? res.data.orders.length : 0;
                }
            } catch (e) {
                console.error('加载业绩数据失败', e);
            }
        },
        
        async loadRecentOrders() {
            try {
                const res = await financeApi.getOrders();
                if (res.success && res.data) {
                    this.recentOrders = res.data.slice(0, 3);
                }
            } catch (e) {
                console.error('加载订单失败', e);
            }
        },
        
        formatNumber(num) {
            if (!num) return '0.00';
            const n = Number(num);
            return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        },
        
        getStatusText(status) {
            const map = {
                'COMPLETED': '已完成',
                'PARTIAL': '部分回款',
                'PENDING': '待回款'
            };
            return map[status] || status;
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
        
        showTestFeatures() {
            uni.showActionSheet({
                itemList: ['验证手机号格式', '验证批次编号', '验证保质期规则', '验证发芽率范围', '查看Swagger文档'],
                success: (res) => {
                    this.runTest(res.tapIndex);
                }
            });
        },
        
        runTest(index) {
            const tests = [
                {
                    title: '手机号格式验证',
                    content: '✓ 13812345678 (正确)\n✓ 15900001111 (正确)\n✗ 12345678901 (错误，第二位非3-9)\n✗ 1381234567 (错误，长度不足)',
                    icon: '📱'
                },
                {
                    title: '批次编号验证',
                    content: '✓ BAT001A1 (8位字母数字)\n✓ 123ABC45 (8位字母数字)\n✗ BAT001 (不足8位)\n✗ BAT001A12 (超过8位)',
                    icon: '🔢'
                },
                {
                    title: '保质期规则',
                    content: '保质期必须 ≥ 当前日期 + 6个月\n\n今天: ' + new Date().toLocaleDateString() + '\n最小保质期: ' + new Date(new Date().setMonth(new Date().getMonth() + 6)).toLocaleDateString(),
                    icon: '📅'
                },
                {
                    title: '发芽率范围',
                    content: '有效范围: 0.0% - 100.0%\n精度: 保留1位小数\n\n✓ 95.5%\n✓ 0.0%\n✓ 100.0%\n✗ 100.1%',
                    icon: '🌱'
                },
                {
                    title: 'Swagger API文档',
                    content: '访问地址:\nhttp://localhost:8080/swagger-ui.html\n\n包含所有API接口文档和测试功能',
                    icon: '📖'
                }
            ];
            
            const test = tests[index];
            uni.showModal({
                title: test.icon + ' ' + test.title,
                content: test.content,
                showCancel: false,
                confirmText: '知道了'
            });
        }
    }
};
</script>

<style scoped>
.home-page {
    padding-bottom: 40rpx;
}

.header {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    padding: 120rpx 40rpx 80rpx;
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
}

.greeting {
    display: block;
    font-size: 36rpx;
    color: rgba(255, 255, 255, 0.9);
    margin-bottom: 8rpx;
}

.username {
    display: block;
    font-size: 44rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.user-role {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.7);
    background: rgba(255, 255, 255, 0.2);
    padding: 6rpx 16rpx;
    border-radius: 20rpx;
}

.date-text {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
}

.quick-stats {
    margin-top: -50rpx;
    margin-left: 30rpx;
    margin-right: 30rpx;
}

.stats-grid {
    display: flex;
    margin-top: 20rpx;
}

.function-entries {
    margin: 24rpx 30rpx;
}

.entries-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 24rpx;
    margin-top: 20rpx;
}

.entry-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 24rpx 0;
}

.entry-icon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 48rpx;
    margin-bottom: 16rpx;
}

.entry-blue { background: #ecf5ff; }
.entry-purple { background: #f9f0ff; }
.entry-green { background: #f0f9eb; }
.entry-orange { background: #fdf6ec; }
.entry-red { background: #fef0f0; }
.entry-gray { background: #f4f4f5; }

.entry-text {
    font-size: 26rpx;
    color: #666;
}

.recent-orders {
    margin: 0 30rpx;
}

.more-link {
    font-size: 26rpx;
    color: #3c9cff;
}

.order-item {
    padding: 24rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.order-item:last-child {
    border-bottom: none;
}

.order-no {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
}

.order-info {
    display: flex;
    gap: 32rpx;
    margin-top: 16rpx;
}

.info-item {
    font-size: 26rpx;
    color: #666;
}
</style>
