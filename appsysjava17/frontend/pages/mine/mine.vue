<template>
    <view class="mine-container">
        <!-- 用户信息卡片 -->
        <view class="user-card">
            <view class="user-avatar">
                <text class="avatar-icon">👤</text>
            </view>
            <view class="user-info">
                <text class="user-name">管理员</text>
                <text class="user-role">系统管理员</text>
            </view>
            <view class="user-arrow">›</view>
        </view>

        <!-- 快捷统计 -->
        <view class="stats-card">
            <view class="stats-item" @click="goToTraceHistory">
                <text class="stats-value">{{ stats.traceCount }}</text>
                <text class="stats-label">溯源查询</text>
            </view>
            <view class="stats-item" @click="goToBatchList">
                <text class="stats-value">{{ stats.batchCount }}</text>
                <text class="stats-label">管理批次</text>
            </view>
            <view class="stats-item" @click="goToExport">
                <text class="stats-value">{{ stats.exportCount }}</text>
                <text class="stats-label">导出报告</text>
            </view>
        </view>

        <!-- 功能菜单 -->
        <view class="menu-section">
            <view class="menu-header">📋 功能菜单</view>
            
            <view class="menu-list">
                <view class="menu-item" @click="goToBatchList">
                    <view class="menu-icon green">📦</view>
                    <view class="menu-info">
                        <text class="menu-title">批次管理</text>
                        <text class="menu-desc">管理种子批次信息</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                
                <view class="menu-item" @click="goToAddBatch">
                    <view class="menu-icon blue">➕</view>
                    <view class="menu-info">
                        <text class="menu-title">新增批次</text>
                        <text class="menu-desc">录入新的种子批次</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                
                <view class="menu-item" @click="goToScan">
                    <view class="menu-icon orange">📷</view>
                    <view class="menu-info">
                        <text class="menu-title">扫码溯源</text>
                        <text class="menu-desc">扫描二维码查询溯源</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                
                <view class="menu-item" @click="goToTraceHistory">
                    <view class="menu-icon purple">🕐</view>
                    <view class="menu-info">
                        <text class="menu-title">查询历史</text>
                        <text class="menu-desc">查看溯源查询记录</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <!-- 系统设置 -->
        <view class="menu-section">
            <view class="menu-header">⚙️ 系统设置</view>
            
            <view class="menu-list">
                <view class="menu-item" @click="goToApiConfig">
                    <view class="menu-icon gray">🔧</view>
                    <view class="menu-info">
                        <text class="menu-title">API配置</text>
                        <text class="menu-desc">配置后端服务地址</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                
                <view class="menu-item" @click="clearCache">
                    <view class="menu-icon gray">🗑️</view>
                    <view class="menu-info">
                        <text class="menu-title">清除缓存</text>
                        <text class="menu-desc">清除本地缓存数据</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
                
                <view class="menu-item" @click="showAbout">
                    <view class="menu-icon gray">ℹ️</view>
                    <view class="menu-info">
                        <text class="menu-title">关于我们</text>
                        <text class="menu-desc">查看系统版本信息</text>
                    </view>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <!-- 底部信息 -->
        <view class="footer-info">
            <text class="footer-text">种子质量追溯系统 v1.0.0</text>
            <text class="footer-text">© 2024 Seed Trace System</text>
        </view>

        <!-- API配置弹窗 -->
        <view class="modal-mask" v-if="showApiModal" @click="closeApiModal">
            <view class="modal-content" @click.stop>
                <view class="modal-header">
                    <text class="modal-title">API服务配置</text>
                    <text class="modal-close" @click="closeApiModal">✕</text>
                </view>
                
                <view class="modal-body">
                    <view class="input-group">
                        <text class="input-label">后端服务地址</text>
                        <input 
                            class="form-input" 
                            v-model="apiBaseUrl" 
                            placeholder="请输入后端API地址"
                        />
                        <text class="input-hint">例如：http://localhost:8080/api</text>
                    </view>
                    
                    <view class="quick-config">
                        <text class="quick-title">快速配置：</text>
                        <view class="quick-btns">
                            <view class="quick-btn" @click="setApiUrl('http://localhost:8080/api')">本地开发</view>
                            <view class="quick-btn" @click="setApiUrl('http://192.168.1.100:8080/api')">局域网</view>
                        </view>
                    </view>
                </view>
                
                <view class="modal-footer">
                    <view class="modal-btn cancel" @click="closeApiModal">取消</view>
                    <view class="modal-btn confirm" @click="saveApiConfig">保存</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
    import { batchApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                stats: {
                    traceCount: 0,
                    batchCount: 0,
                    exportCount: 0
                },
                showApiModal: false,
                apiBaseUrl: ''
            };
        },
        
        onLoad() {
            this.loadStats();
            this.apiBaseUrl = uni.getStorageSync('apiBaseUrl') || 'http://localhost:8080/api';
        },
        
        onShow() {
            this.loadStats();
        },
        
        methods: {
            async loadStats() {
                // 统计数据
                const history = uni.getStorageSync('traceHistory') || [];
                this.stats.traceCount = history.length;
                this.stats.exportCount = uni.getStorageSync('exportCount') || 0;
                
                try {
                    const res = await batchApi.getAllBatches();
                    if (res.code === 200 && res.data) {
                        this.stats.batchCount = res.data.length;
                    }
                } catch (e) {
                    // 使用默认值
                    this.stats.batchCount = 5;
                }
            },
            
            goToBatchList() {
                uni.switchTab({
                    url: '/pages/batch/list'
                });
            },
            
            goToAddBatch() {
                uni.navigateTo({
                    url: '/pages/batch/form'
                });
            },
            
            goToScan() {
                uni.switchTab({
                    url: '/pages/scan/scan'
                });
            },
            
            goToTraceHistory() {
                uni.showToast({
                    title: '查看最近查询记录',
                    icon: 'none'
                });
            },
            
            goToExport() {
                uni.showToast({
                    title: '暂无导出记录',
                    icon: 'none'
                });
            },
            
            goToApiConfig() {
                this.apiBaseUrl = uni.getStorageSync('apiBaseUrl') || 'http://localhost:8080/api';
                this.showApiModal = true;
            },
            
            closeApiModal() {
                this.showApiModal = false;
            },
            
            setApiUrl(url) {
                this.apiBaseUrl = url;
            },
            
            saveApiConfig() {
                if (!this.apiBaseUrl) {
                    uni.showToast({
                        title: '请输入API地址',
                        icon: 'none'
                    });
                    return;
                }
                
                uni.setStorageSync('apiBaseUrl', this.apiBaseUrl);
                this.showApiModal = false;
                uni.showToast({
                    title: '配置已保存',
                    icon: 'success'
                });
            },
            
            clearCache() {
                uni.showModal({
                    title: '清除缓存',
                    content: '确定要清除本地缓存数据吗？',
                    success: (res) => {
                        if (res.confirm) {
                            uni.removeStorageSync('traceHistory');
                            uni.removeStorageSync('exportCount');
                            this.loadStats();
                            uni.showToast({
                                title: '缓存已清除',
                                icon: 'success'
                            });
                        }
                    }
                });
            },
            
            showAbout() {
                uni.showModal({
                    title: '关于',
                    content: '种子质量追溯系统\n版本：1.0.0\n\n技术栈：\n后端：Spring Boot + H2 + Swagger\n前端：UniApp\n\n功能特性：\n- 全链条溯源查询\n- PDF报告导出\n- 数据加密存储\n- 多端适配',
                    showCancel: false
                });
            }
        }
    };
</script>

<style scoped>
    .mine-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 40rpx;
    }

    .user-card {
        display: flex;
        align-items: center;
        padding: 40rpx 30rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
    }

    .user-avatar {
        width: 120rpx;
        height: 120rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(255, 255, 255, 0.2);
        border-radius: 50%;
        margin-right: 30rpx;
    }

    .avatar-icon {
        font-size: 60rpx;
    }

    .user-info {
        flex: 1;
    }

    .user-name {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 10rpx;
    }

    .user-role {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
    }

    .user-arrow {
        font-size: 40rpx;
        color: rgba(255, 255, 255, 0.6);
    }

    .stats-card {
        display: flex;
        margin: -30rpx 20rpx 0;
        padding: 30rpx 0;
        background-color: #fff;
        border-radius: 20rpx;
        box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
        position: relative;
        z-index: 10;
    }

    .stats-item {
        flex: 1;
        text-align: center;
    }

    .stats-value {
        display: block;
        font-size: 40rpx;
        font-weight: bold;
        color: #2b7a4b;
        margin-bottom: 8rpx;
    }

    .stats-label {
        font-size: 24rpx;
        color: #999;
    }

    .menu-section {
        margin: 30rpx 20rpx;
    }

    .menu-header {
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
        padding: 20rpx 10rpx;
    }

    .menu-list {
        background-color: #fff;
        border-radius: 20rpx;
        overflow: hidden;
    }

    .menu-item {
        display: flex;
        align-items: center;
        padding: 30rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .menu-item:last-child {
        border-bottom: none;
    }

    .menu-icon {
        width: 70rpx;
        height: 70rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 18rpx;
        font-size: 32rpx;
        margin-right: 24rpx;
    }

    .menu-icon.green {
        background-color: #e8f5e9;
    }

    .menu-icon.blue {
        background-color: #e3f2fd;
    }

    .menu-icon.orange {
        background-color: #fff3e0;
    }

    .menu-icon.purple {
        background-color: #f3e5f5;
    }

    .menu-icon.gray {
        background-color: #f5f5f5;
    }

    .menu-info {
        flex: 1;
    }

    .menu-title {
        display: block;
        font-size: 30rpx;
        color: #333;
        font-weight: 500;
        margin-bottom: 6rpx;
    }

    .menu-desc {
        font-size: 24rpx;
        color: #999;
    }

    .menu-arrow {
        font-size: 36rpx;
        color: #ccc;
    }

    .footer-info {
        text-align: center;
        padding: 40rpx;
    }

    .footer-text {
        display: block;
        font-size: 24rpx;
        color: #ccc;
        margin-bottom: 10rpx;
    }

    .modal-mask {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
    }

    .modal-content {
        width: 620rpx;
        background-color: #fff;
        border-radius: 24rpx;
        overflow: hidden;
    }

    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 30rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .modal-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #333;
    }

    .modal-close {
        font-size: 40rpx;
        color: #999;
    }

    .modal-body {
        padding: 30rpx;
    }

    .input-label {
        display: block;
        font-size: 28rpx;
        color: #333;
        margin-bottom: 16rpx;
    }

    .form-input {
        width: 100%;
        height: 88rpx;
        padding: 0 24rpx;
        background-color: #f5f5f5;
        border: 2rpx solid #e0e0e0;
        border-radius: 12rpx;
        font-size: 28rpx;
        color: #333;
    }

    .input-hint {
        display: block;
        font-size: 22rpx;
        color: #999;
        margin-top: 12rpx;
    }

    .quick-config {
        margin-top: 30rpx;
        padding-top: 30rpx;
        border-top: 2rpx solid #f0f0f0;
    }

    .quick-title {
        display: block;
        font-size: 26rpx;
        color: #666;
        margin-bottom: 16rpx;
    }

    .quick-btns {
        display: flex;
        gap: 16rpx;
    }

    .quick-btn {
        padding: 16rpx 28rpx;
        background-color: #e8f5e9;
        border-radius: 30rpx;
        font-size: 24rpx;
        color: #2b7a4b;
    }

    .modal-footer {
        display: flex;
        border-top: 2rpx solid #f0f0f0;
    }

    .modal-btn {
        flex: 1;
        padding: 28rpx;
        text-align: center;
        font-size: 30rpx;
    }

    .modal-btn.cancel {
        color: #666;
        border-right: 2rpx solid #f0f0f0;
    }

    .modal-btn.confirm {
        color: #2b7a4b;
        font-weight: 500;
    }
</style>
