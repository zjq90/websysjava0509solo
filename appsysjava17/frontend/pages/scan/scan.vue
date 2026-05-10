<template>
    <view class="scan-container">
        <!-- 扫描区域 -->
        <view class="scan-area">
            <view class="scan-header">
                <text class="scan-title">请扫描种子包装二维码</text>
                <text class="scan-subtitle">支持扫一扫快速查询溯源信息</text>
            </view>
            
            <view class="scan-box">
                <view class="scan-frame">
                    <view class="corner top-left"></view>
                    <view class="corner top-right"></view>
                    <view class="corner bottom-left"></view>
                    <view class="corner bottom-right"></view>
                    
                    <view class="scan-line" :class="{ active: scanning }"></view>
                </view>
                
                <view class="scan-tip">
                    <text class="tip-icon">📷</text>
                    <text class="tip-text">将二维码放入框内自动识别</text>
                </view>
            </view>
        </view>

        <!-- 操作按钮 -->
        <view class="action-section">
            <view class="action-buttons">
                <view class="action-btn" @click="startScan">
                    <view class="action-icon">🔍</view>
                    <text class="action-text">开始扫码</text>
                </view>
                
                <view class="action-btn" @click="inputBatchCode">
                    <view class="action-icon">⌨️</view>
                    <text class="action-text">手动输入</text>
                </view>
            </view>
        </view>

        <!-- 最近查询记录 -->
        <view class="history-section" v-if="historyList.length > 0">
            <view class="section-header">
                <text class="section-title">🕐 最近查询</text>
                <text class="section-clear" @click="clearHistory">清空</text>
            </view>
            
            <view class="history-list">
                <view 
                    class="history-item" 
                    v-for="(item, index) in historyList" 
                    :key="index"
                    @click="goToTrace(item.batchCode)"
                >
                    <view class="history-icon">
                        {{ getSeedIcon(item.seedName) }}
                    </view>
                    <view class="history-info">
                        <text class="history-name">{{ item.seedName }}</text>
                        <text class="history-code">{{ item.batchCode }}</text>
                    </view>
                    <view class="history-time">{{ item.time }}</view>
                </view>
            </view>
        </view>

        <!-- 热门批次 -->
        <view class="hot-section">
            <view class="section-header">
                <text class="section-title">🔥 热门批次</text>
            </view>
            
            <view class="hot-tags">
                <view class="hot-tag" v-for="batch in hotBatches" :key="batch.batchCode" @click="goToTrace(batch.batchCode)">
                    <text>{{ batch.batchCode }}</text>
                    <text class="hot-name">{{ batch.seedName }}</text>
                </view>
            </view>
        </view>

        <!-- 输入弹窗 -->
        <view class="modal-mask" v-if="showInputModal" @click="closeInputModal">
            <view class="modal-content" @click.stop>
                <view class="modal-header">
                    <text class="modal-title">输入批次号</text>
                    <text class="modal-close" @click="closeInputModal">✕</text>
                </view>
                
                <view class="modal-body">
                    <view class="input-group">
                        <text class="input-label">批次编号</text>
                        <input 
                            class="form-input" 
                            v-model="inputBatch" 
                            placeholder="请输入8位批次号（数字+字母）"
                            maxlength="8"
                        />
                        <text class="input-hint">例如：SD2024A1</text>
                    </view>
                </view>
                
                <view class="modal-footer">
                    <view class="modal-btn cancel" @click="closeInputModal">取消</view>
                    <view class="modal-btn confirm" @click="confirmInput">查询</view>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
    import { batchApi, traceApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                scanning: false,
                historyList: [],
                hotBatches: [
                    { batchCode: 'SD2024A1', seedName: '玉米' },
                    { batchCode: 'SD2024B2', seedName: '小麦' },
                    { batchCode: 'SD2024C3', seedName: '水稻' },
                    { batchCode: 'SD2024D4', seedName: '大豆' }
                ],
                showInputModal: false,
                inputBatch: ''
            };
        },
        
        onLoad() {
            this.loadHistory();
        },
        
        onShow() {
            this.loadHistory();
        },
        
        methods: {
            loadHistory() {
                const history = uni.getStorageSync('traceHistory') || [];
                this.historyList = history.slice(0, 5);
            },
            
            async startScan() {
                this.scanning = true;
                
                try {
                    const res = await new Promise((resolve, reject) => {
                        uni.scanCode({
                            onlyFromCamera: false,
                            scanType: ['qrCode'],
                            success: resolve,
                            fail: reject
                        });
                    });
                    
                    console.log('扫码结果:', res);
                    
                    // 解析结果，获取批次号
                    let batchCode = this.parseBatchCode(res.result);
                    
                    if (batchCode && batchCode.length === 8) {
                        await this.addToHistory(batchCode);
                        uni.navigateTo({
                            url: `/pages/trace/trace?batchCode=${batchCode}`
                        });
                    } else {
                        uni.showToast({
                            title: '无法识别批次号',
                            icon: 'none'
                        });
                    }
                    
                } catch (e) {
                    console.error('扫码失败', e);
                    uni.showToast({
                        title: '扫码已取消或失败',
                        icon: 'none'
                    });
                } finally {
                    this.scanning = false;
                }
            },
            
            parseBatchCode(result) {
                if (!result) return null;
                
                // 如果是URL，尝试提取批次号参数
                if (result.startsWith('http')) {
                    try {
                        const url = new URL(result);
                        const batchCode = url.searchParams.get('batchCode') || url.searchParams.get('code');
                        if (batchCode) return batchCode;
                    } catch (e) {
                        console.log('解析URL失败');
                    }
                }
                
                // 直接返回结果（假设二维码内容就是批次号）
                return result.trim();
            },
            
            async addToHistory(batchCode) {
                try {
                    const res = await batchApi.getBatchByCode(batchCode);
                    if (res.code === 200 && res.data) {
                        const history = uni.getStorageSync('traceHistory') || [];
                        
                        // 移除已存在的相同批次
                        const newHistory = history.filter(h => h.batchCode !== batchCode);
                        
                        // 添加到开头
                        newHistory.unshift({
                            batchCode: batchCode,
                            seedName: res.data.seedName,
                            time: this.formatTime(new Date())
                        });
                        
                        // 最多保留20条
                        uni.setStorageSync('traceHistory', newHistory.slice(0, 20));
                    }
                } catch (e) {
                    console.error('添加历史记录失败', e);
                }
            },
            
            formatTime(date) {
                const pad = n => n < 10 ? '0' + n : n;
                return `${date.getMonth() + 1}/${date.getDate()} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
            },
            
            inputBatchCode() {
                this.showInputModal = true;
                this.inputBatch = '';
            },
            
            closeInputModal() {
                this.showInputModal = false;
                this.inputBatch = '';
            },
            
            async confirmInput() {
                if (!this.inputBatch || this.inputBatch.length !== 8) {
                    uni.showToast({
                        title: '请输入8位批次号',
                        icon: 'none'
                    });
                    return;
                }
                
                // 验证批次号格式（数字+字母）
                if (!/^[A-Za-z0-9]+$/.test(this.inputBatch)) {
                    uni.showToast({
                        title: '批次号只能是数字和字母',
                        icon: 'none'
                    });
                    return;
                }
                
                this.closeInputModal();
                await this.addToHistory(this.inputBatch);
                
                uni.navigateTo({
                    url: `/pages/trace/trace?batchCode=${this.inputBatch}`
                });
            },
            
            clearHistory() {
                uni.showModal({
                    title: '提示',
                    content: '确定清空历史记录吗？',
                    success: (res) => {
                        if (res.confirm) {
                            uni.removeStorageSync('traceHistory');
                            this.historyList = [];
                            uni.showToast({
                                title: '已清空',
                                icon: 'success'
                            });
                        }
                    }
                });
            },
            
            goToTrace(batchCode) {
                this.addToHistory(batchCode);
                uni.navigateTo({
                    url: `/pages/trace/trace?batchCode=${batchCode}`
                });
            },
            
            getSeedIcon(seedName) {
                if (!seedName) return '🌱';
                if (seedName.includes('玉米')) return '🌽';
                if (seedName.includes('小麦')) return '🌾';
                if (seedName.includes('水稻')) return '🍚';
                if (seedName.includes('大豆')) return '🫘';
                if (seedName.includes('棉花')) return '☁️';
                return '🌱';
            }
        }
    };
</script>

<style scoped>
    .scan-container {
        min-height: 100vh;
        background: linear-gradient(to bottom, #2b7a4b 0%, #2b7a4b 300rpx, #f5f5f5 300rpx, #f5f5f5 100%);
        padding-bottom: 40rpx;
    }

    .scan-area {
        padding: 40rpx 30rpx;
    }

    .scan-header {
        text-align: center;
        margin-bottom: 40rpx;
    }

    .scan-title {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 12rpx;
    }

    .scan-subtitle {
        display: block;
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
    }

    .scan-box {
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .scan-frame {
        width: 480rpx;
        height: 480rpx;
        position: relative;
        background-color: rgba(255, 255, 255, 0.1);
        border-radius: 20rpx;
        overflow: hidden;
    }

    .corner {
        position: absolute;
        width: 60rpx;
        height: 60rpx;
        border-color: #4a9d6a;
        border-style: solid;
    }

    .corner.top-left {
        top: 0;
        left: 0;
        border-width: 6rpx 0 0 6rpx;
        border-top-left-radius: 10rpx;
    }

    .corner.top-right {
        top: 0;
        right: 0;
        border-width: 6rpx 6rpx 0 0;
        border-top-right-radius: 10rpx;
    }

    .corner.bottom-left {
        bottom: 0;
        left: 0;
        border-width: 0 0 6rpx 6rpx;
        border-bottom-left-radius: 10rpx;
    }

    .corner.bottom-right {
        bottom: 0;
        right: 0;
        border-width: 0 6rpx 6rpx 0;
        border-bottom-right-radius: 10rpx;
    }

    .scan-line {
        position: absolute;
        left: 30rpx;
        right: 30rpx;
        height: 4rpx;
        background: linear-gradient(90deg, transparent, #4a9d6a, transparent);
        top: 100rpx;
        box-shadow: 0 0 20rpx #4a9d6a;
    }

    .scan-line.active {
        animation: scanMove 2s ease-in-out infinite;
    }

    @keyframes scanMove {
        0%, 100% {
            top: 30rpx;
        }
        50% {
            top: 430rpx;
        }
    }

    .scan-tip {
        display: flex;
        align-items: center;
        margin-top: 30rpx;
        padding: 16rpx 30rpx;
        background-color: rgba(255, 255, 255, 0.2);
        border-radius: 30rpx;
    }

    .tip-icon {
        font-size: 32rpx;
        margin-right: 12rpx;
    }

    .tip-text {
        font-size: 24rpx;
        color: rgba(255, 255, 255, 0.9);
    }

    .action-section {
        padding: 0 30rpx 30rpx;
        margin-top: -20rpx;
    }

    .action-buttons {
        display: flex;
        background-color: #fff;
        border-radius: 20rpx;
        padding: 40rpx 0;
        box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
    }

    .action-btn {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    .action-icon {
        width: 100rpx;
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 30rpx;
        font-size: 48rpx;
        margin-bottom: 16rpx;
        box-shadow: 0 4rpx 16rpx rgba(43, 122, 75, 0.3);
    }

    .action-text {
        font-size: 26rpx;
        color: #333;
        font-weight: 500;
    }

    .history-section {
        padding: 20rpx 30rpx;
    }

    .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20rpx;
    }

    .section-title {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
    }

    .section-clear {
        font-size: 24rpx;
        color: #999;
    }

    .history-list {
        background-color: #fff;
        border-radius: 20rpx;
        overflow: hidden;
        box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
    }

    .history-item {
        display: flex;
        align-items: center;
        padding: 24rpx 30rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .history-item:last-child {
        border-bottom: none;
    }

    .history-icon {
        width: 64rpx;
        height: 64rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #e8f5e9;
        border-radius: 16rpx;
        font-size: 32rpx;
        margin-right: 20rpx;
    }

    .history-info {
        flex: 1;
    }

    .history-name {
        display: block;
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
        margin-bottom: 6rpx;
    }

    .history-code {
        display: block;
        font-size: 24rpx;
        color: #999;
    }

    .history-time {
        font-size: 22rpx;
        color: #ccc;
    }

    .hot-section {
        padding: 20rpx 30rpx;
    }

    .hot-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 16rpx;
    }

    .hot-tag {
        display: flex;
        align-items: center;
        padding: 16rpx 28rpx;
        background-color: #fff;
        border-radius: 40rpx;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    }

    .hot-tag text {
        font-size: 26rpx;
        color: #2b7a4b;
        font-weight: 500;
    }

    .hot-name {
        margin-left: 12rpx;
        font-size: 24rpx !important;
        color: #999 !important;
        font-weight: normal !important;
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
        font-size: 24rpx;
        color: #999;
        margin-top: 12rpx;
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
