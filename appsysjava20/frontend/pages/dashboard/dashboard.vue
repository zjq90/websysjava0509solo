<template>
    <view class="dashboard-page">
        <view class="header-card">
            <view class="header-content">
                <view class="header-left">
                    <text class="header-title">📊 经营看板</text>
                    <text class="header-desc">管理层专属视图</text>
                </view>
                <view class="header-right">
                    <picker mode="selector" :range="years" @change="onYearChange">
                        <view class="picker-box">
                            <text class="picker-text">{{ selectedYear }}年</text>
                            <text class="picker-arrow">▼</text>
                        </view>
                    </picker>
                </view>
            </view>
        </view>
        
        <view class="metrics-grid">
            <view class="metric-card blue">
                <view class="metric-header">
                    <text class="metric-label">销售额</text>
                    <text class="metric-trend up">↑ {{ dashboardData.salesGrowthRate || 0 }}%</text>
                </view>
                <text class="metric-value">¥{{ formatNumber(dashboardData.totalSalesAmount) }}</text>
                <view class="metric-chart">
                    <view class="chart-bar" v-for="i in 6" :key="i" :style="{ height: getRandomHeight() + '%' }"></view>
                </view>
            </view>
            
            <view class="metric-card green">
                <view class="metric-header">
                    <text class="metric-label">毛利率</text>
                </view>
                <text class="metric-value">{{ dashboardData.grossProfitMargin || 0 }}%</text>
                <view class="metric-progress">
                    <view class="progress-bg">
                        <view class="progress-fill green" :style="{ width: (dashboardData.grossProfitMargin || 0) + '%' }"></view>
                    </view>
                </view>
            </view>
            
            <view class="metric-card purple">
                <view class="metric-header">
                    <text class="metric-label">库存周转率</text>
                </view>
                <text class="metric-value">{{ dashboardData.inventoryTurnover || 0 }}次</text>
                <text class="metric-subtext">平均周转天数: {{ Math.round(365 / (dashboardData.inventoryTurnover || 1)) }}天</text>
            </view>
            
            <view class="metric-card orange">
                <view class="metric-header">
                    <text class="metric-label">已回款总额</text>
                </view>
                <text class="metric-value">¥{{ formatNumber(dashboardData.totalPaidAmount) }}</text>
                <text class="metric-subtext">持续增长中</text>
            </view>
        </view>
        
        <view class="section-card card">
            <view class="card-title">📈 月度销售趋势</view>
            <view class="chart-container">
                <view class="chart-bars">
                    <view class="chart-bar-item" v-for="(item, index) in salesTrend" :key="index">
                        <view class="bar-wrapper">
                            <view class="bar-fill" :style="{ height: item.height + '%' }">
                                <text class="bar-label">{{ item.label }}</text>
                            </view>
                        </view>
                        <text class="bar-month">{{ item.month }}月</text>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="section-card card">
            <view class="card-title">👥 销售排行榜</view>
            <view class="ranking-list">
                <view class="ranking-item" v-for="(item, index) in salesRanking" :key="index">
                    <view class="rank-badge" :class="'rank-' + (index + 1)">
                        {{ index + 1 }}
                    </view>
                    <view class="rank-info">
                        <text class="rank-name">{{ item.name }}</text>
                        <text class="rank-role">{{ item.role }}</text>
                    </view>
                    <view class="rank-amount">
                        <text class="amount-value">¥{{ formatNumber(item.amount) }}</text>
                        <text class="amount-rate">{{ item.rate }}%提成</text>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="section-card card">
            <view class="card-title">🏆 热销产品</view>
            <view class="product-list">
                <view class="product-item" v-for="(item, index) in hotProducts" :key="index">
                    <view class="product-rank">{{ index + 1 }}</view>
                    <view class="product-info">
                        <text class="product-name">{{ item.name }}</text>
                        <text class="product-batch">{{ item.batchNumber }}</text>
                    </view>
                    <view class="product-stats">
                        <text class="stats-price">¥{{ item.unitPrice }}</text>
                        <text class="stats-rate">发芽率 {{ item.germinationRate }}%</text>
                    </view>
                </view>
            </view>
        </view>
        
        <view class="footer-note">
            <text>数据更新时间: {{ updateTime }}</text>
        </view>
    </view>
</template>

<script>
import { dashboardApi } from '../../api/index';

export default {
    data() {
        return {
            years: [],
            selectedYear: new Date().getFullYear(),
            updateTime: '',
            dashboardData: {
                totalSalesAmount: '0.00',
                grossProfitMargin: 0,
                inventoryTurnover: 0,
                totalPaidAmount: '0.00',
                salesGrowthRate: 0
            },
            salesTrend: [],
            salesRanking: [
                { name: '销售员A', role: '销售员', amount: '37000.00', rate: 2.0 },
                { name: '销售员B', role: '销售员', amount: '17100.00', rate: 2.5 },
                { name: '王经理', role: '销售经理', amount: '0.00', rate: 3.5 }
            ],
            hotProducts: [
                { name: '有机蔬菜种子', batchNumber: 'BAT003C3', unitPrice: '50.00', germinationRate: 88.8 },
                { name: '优质小麦种子', batchNumber: 'BAT001A1', unitPrice: '120.00', germinationRate: 95.5 },
                { name: '高产玉米种子', batchNumber: 'BAT002B2', unitPrice: '85.50', germinationRate: 92.3 },
                { name: '大豆种子', batchNumber: 'BAT004D4', unitPrice: '68.00', germinationRate: 90.0 }
            ]
        };
    },
    onShow() {
        this.initYears();
        this.loadData();
        this.initSalesTrend();
    },
    methods: {
        initYears() {
            const currentYear = new Date().getFullYear();
            this.years = [currentYear - 2, currentYear - 1, currentYear, currentYear + 1];
        },
        
        initSalesTrend() {
            const base = 50000;
            this.salesTrend = [];
            for (let i = 1; i <= 6; i++) {
                const value = base + Math.random() * 30000;
                this.salesTrend.push({
                    month: i,
                    value: value,
                    height: Math.round(30 + Math.random() * 70),
                    label: '¥' + Math.round(value / 1000) + 'k'
                });
            }
        },
        
        onYearChange(e) {
            this.selectedYear = this.years[e.detail.value];
            this.loadData();
        },
        
        async loadData() {
            const now = new Date();
            this.updateTime = now.toLocaleString('zh-CN');
            
            try {
                const res = await dashboardApi.getData({
                    year: this.selectedYear,
                    month: new Date().getMonth() + 1
                });
                if (res.success && res.data) {
                    this.dashboardData = res.data;
                }
            } catch (e) {
                console.error('加载看板数据失败', e);
            }
        },
        
        getRandomHeight() {
            return 30 + Math.round(Math.random() * 70);
        },
        
        formatNumber(num) {
            if (!num) return '0.00';
            const n = Number(num);
            return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        }
    }
};
</script>

<style scoped>
.dashboard-page {
    padding-bottom: 40rpx;
}

.header-card {
    background: linear-gradient(135deg, #667eea, #764ba2);
    padding: 60rpx 40rpx 40rpx;
}

.header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.header-title {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 8rpx;
}

.header-desc {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.7);
}

.picker-box {
    background: rgba(255, 255, 255, 0.2);
    border-radius: 24rpx;
    padding: 16rpx 28rpx;
    display: flex;
    align-items: center;
    gap: 8rpx;
}

.picker-text {
    font-size: 28rpx;
    color: #fff;
    font-weight: bold;
}

.picker-arrow {
    font-size: 20rpx;
    color: rgba(255, 255, 255, 0.8);
}

.metrics-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24rpx;
    padding: 24rpx;
    margin-top: -20rpx;
}

.metric-card {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.metric-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12rpx;
}

.metric-label {
    font-size: 26rpx;
    color: #999;
}

.metric-trend {
    font-size: 22rpx;
    padding: 4rpx 12rpx;
    border-radius: 12rpx;
}

.metric-trend.up {
    background: #f0f9eb;
    color: #67c23a;
}

.metric-trend.down {
    background: #fef0f0;
    color: #f56c6c;
}

.metric-value {
    display: block;
    font-size: 40rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
}

.metric-card.blue .metric-value { color: #3c9cff; }
.metric-card.green .metric-value { color: #67c23a; }
.metric-card.purple .metric-value { color: #909399; }
.metric-card.orange .metric-value { color: #e6a23c; }

.metric-subtext {
    font-size: 22rpx;
    color: #999;
}

.metric-chart {
    display: flex;
    align-items: flex-end;
    gap: 8rpx;
    height: 60rpx;
}

.chart-bar {
    flex: 1;
    background: linear-gradient(180deg, #3c9cff, #5ba8ff);
    border-radius: 4rpx;
    transition: height 0.3s;
}

.metric-progress {
    margin-top: 8rpx;
}

.progress-bg {
    height: 12rpx;
    background: #f0f0f0;
    border-radius: 6rpx;
    overflow: hidden;
}

.progress-fill {
    height: 100%;
    border-radius: 6rpx;
    transition: width 0.5s;
}

.progress-fill.green { background: linear-gradient(90deg, #67c23a, #95d475); }

.section-card {
    margin: 24rpx;
}

.chart-container {
    padding: 20rpx 0;
}

.chart-bars {
    display: flex;
    align-items: flex-end;
    gap: 16rpx;
    height: 300rpx;
}

.chart-bar-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.bar-wrapper {
    width: 100%;
    height: 240rpx;
    display: flex;
    align-items: flex-end;
}

.bar-fill {
    width: 100%;
    background: linear-gradient(180deg, #3c9cff, #67c23a);
    border-radius: 12rpx 12rpx 4rpx 4rpx;
    display: flex;
    align-items: flex-start;
    justify-content: center;
    padding-top: 10rpx;
    transition: height 0.5s;
    position: relative;
}

.bar-label {
    font-size: 18rpx;
    color: #fff;
    transform: rotate(-90deg);
    white-space: nowrap;
}

.bar-month {
    margin-top: 12rpx;
    font-size: 22rpx;
    color: #999;
}

.ranking-list {
    margin-top: 16rpx;
}

.ranking-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.ranking-item:last-child {
    border-bottom: none;
}

.rank-badge {
    width: 56rpx;
    height: 56rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 26rpx;
    font-weight: bold;
    color: #fff;
    margin-right: 20rpx;
}

.rank-1 { background: linear-gradient(135deg, #f093fb, #f5576c); }
.rank-2 { background: linear-gradient(135deg, #4facfe, #00f2fe); }
.rank-3 { background: linear-gradient(135deg, #43e97b, #38f9d7); }

.rank-info {
    flex: 1;
}

.rank-name {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
}

.rank-role {
    font-size: 22rpx;
    color: #999;
}

.rank-amount {
    text-align: right;
}

.amount-value {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    color: #3c9cff;
}

.amount-rate {
    font-size: 22rpx;
    color: #e6a23c;
}

.product-list {
    margin-top: 16rpx;
}

.product-item {
    display: flex;
    align-items: center;
    padding: 20rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
}

.product-item:last-child {
    border-bottom: none;
}

.product-rank {
    width: 40rpx;
    font-size: 28rpx;
    font-weight: bold;
    color: #999;
    text-align: center;
}

.product-info {
    flex: 1;
    margin-left: 16rpx;
}

.product-name {
    display: block;
    font-size: 28rpx;
    color: #333;
}

.product-batch {
    font-size: 22rpx;
    color: #999;
}

.product-stats {
    text-align: right;
}

.stats-price {
    display: block;
    font-size: 26rpx;
    font-weight: bold;
    color: #e6a23c;
}

.stats-rate {
    font-size: 22rpx;
    color: #67c23a;
}

.footer-note {
    text-align: center;
    padding: 30rpx;
    color: #bbb;
    font-size: 24rpx;
}
</style>
