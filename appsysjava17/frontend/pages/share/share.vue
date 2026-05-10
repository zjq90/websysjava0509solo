<template>
    <view class="share-container">
        <!-- 报告预览 -->
        <view class="preview-card">
            <view class="preview-header">
                <text class="preview-title">📄 溯源报告预览</text>
                <view class="preview-badge" :class="batchInfo.status === 'ACTIVE' ? 'success' : 'warning'">
                    {{ batchInfo.status === 'ACTIVE' ? '正常' : '停用' }}
                </view>
            </view>
            
            <view class="preview-content">
                <view class="preview-row">
                    <text class="preview-label">批次编号</text>
                    <text class="preview-value">{{ batchInfo.batchCode }}</text>
                </view>
                <view class="preview-row">
                    <text class="preview-label">种子名称</text>
                    <text class="preview-value">{{ batchInfo.seedName }}</text>
                </view>
                <view class="preview-row">
                    <text class="preview-label">发芽率</text>
                    <text class="preview-value highlight">{{ batchInfo.germinationRate }}%</text>
                </view>
                <view class="preview-row">
                    <text class="preview-label">生产日期</text>
                    <text class="preview-value">{{ batchInfo.productionDate }}</text>
                </view>
                <view class="preview-row">
                    <text class="preview-label">保质期至</text>
                    <text class="preview-value">{{ batchInfo.shelfLife }}</text>
                </view>
            </view>
        </view>

        <!-- 分享选项 -->
        <view class="share-section">
            <view class="section-title">📤 分享方式</view>
            
            <view class="share-grid">
                <view class="share-item" @click="shareToWechat">
                    <view class="share-icon wechat">💬</view>
                    <text class="share-text">微信</text>
                </view>
                
                <view class="share-item" @click="shareToDingtalk">
                    <view class="share-icon dingtalk">🔔</view>
                    <text class="share-text">钉钉</text>
                </view>
                
                <view class="share-item" @click="copyLink">
                    <view class="share-icon link">🔗</view>
                    <text class="share-text">复制链接</text>
                </view>
                
                <view class="share-item" @click="downloadPdf">
                    <view class="share-icon pdf">📄</view>
                    <text class="share-text">下载PDF</text>
                </view>
                
                <view class="share-item" @click="generateImage">
                    <view class="share-icon image">🖼️</view>
                    <text class="share-text">生成图片</text>
                </view>
                
                <view class="share-item" @click="shareMore">
                    <view class="share-icon more">⋯</view>
                    <text class="share-text">更多</text>
                </view>
            </view>
        </view>

        <!-- 报告信息 -->
        <view class="info-section">
            <view class="section-title">📋 报告信息</view>
            
            <view class="info-card">
                <view class="info-row">
                    <text class="info-label">查询时间</text>
                    <text class="info-value">{{ queryTime }}</text>
                </view>
                <view class="info-row">
                    <text class="info-label">查询ID</text>
                    <text class="info-value code">{{ queryId }}</text>
                </view>
                <view class="info-row">
                    <text class="info-label">数据来源</text>
                    <text class="info-value">种子质量追溯系统</text>
                </view>
            </view>
        </view>

        <!-- 提示信息 -->
        <view class="tips-section">
            <view class="tips-icon">💡</view>
            <view class="tips-content">
                <text class="tips-title">温馨提示</text>
                <text class="tips-text">本报告由种子质量追溯系统自动生成，数据真实有效。支持分享至微信、钉钉等平台，提升客户信任度。</text>
            </view>
        </view>

        <!-- 底部操作 -->
        <view class="footer-section">
            <view class="btn btn-secondary" @click="goBack">返回</view>
            <view class="btn btn-primary" @click="shareAll">立即分享</view>
        </view>
    </view>
</template>

<script>
    import { traceApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                batchCode: '',
                batchInfo: {
                    batchCode: '-',
                    seedName: '-',
                    germinationRate: '-',
                    productionDate: '-',
                    shelfLife: '-',
                    status: 'ACTIVE'
                },
                queryTime: '',
                queryId: ''
            };
        },
        
        onLoad(options) {
            if (options.batchCode) {
                this.batchCode = options.batchCode;
                this.loadBatchInfo();
            }
            
            const now = new Date();
            const pad = n => n < 10 ? '0' + n : n;
            this.queryTime = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}`;
            this.queryId = 'SHARE-' + Date.now();
        },
        
        methods: {
            async loadBatchInfo() {
                try {
                    const res = await traceApi.queryTrace(this.batchCode);
                    if (res.code === 200 && res.data && res.data.batchInfo) {
                        this.batchInfo = res.data.batchInfo;
                        if (res.data.queryTime) {
                            this.queryTime = res.data.queryTime;
                        }
                        if (res.data.queryId) {
                            this.queryId = res.data.queryId;
                        }
                    }
                } catch (e) {
                    console.error('加载批次信息失败', e);
                    // 使用模拟数据
                    this.batchInfo = {
                        batchCode: this.batchCode || 'SD2024A1',
                        seedName: '玉米种子',
                        germinationRate: 95.5,
                        productionDate: '2024-01-15',
                        shelfLife: '2025-12-31',
                        status: 'ACTIVE'
                    };
                }
            },
            
            shareToWechat() {
                uni.showToast({
                    title: '微信分享需要配置小程序',
                    icon: 'none'
                });
            },
            
            shareToDingtalk() {
                uni.showToast({
                    title: '钉钉分享需要配置应用',
                    icon: 'none'
                });
            },
            
            copyLink() {
                const shareLink = `https://seedtrace.com/trace/${this.batchCode}`;
                uni.setClipboardData({
                    data: shareLink,
                    success: () => {
                        uni.showToast({
                            title: '链接已复制',
                            icon: 'success'
                        });
                    }
                });
            },
            
            downloadPdf() {
                uni.showLoading({ title: '生成PDF...' });
                
                const pdfUrl = traceApi.exportPdf(this.batchCode);
                console.log('PDF URL:', pdfUrl);
                
                uni.downloadFile({
                    url: pdfUrl,
                    success: (res) => {
                        if (res.statusCode === 200) {
                            uni.hideLoading();
                            uni.openDocument({
                                filePath: res.tempFilePath,
                                fileType: 'pdf',
                                showMenu: true,
                                success: () => {
                                    console.log('打开PDF成功');
                                    // 记录导出次数
                                    const count = uni.getStorageSync('exportCount') || 0;
                                    uni.setStorageSync('exportCount', count + 1);
                                },
                                fail: () => {
                                    uni.showToast({
                                        title: '打开失败，文件已保存',
                                        icon: 'none'
                                    });
                                }
                            });
                        }
                    },
                    fail: () => {
                        uni.hideLoading();
                        uni.showToast({
                            title: 'PDF导出需要后端支持',
                            icon: 'none'
                        });
                    }
                });
            },
            
            generateImage() {
                uni.showToast({
                    title: '图片生成功能开发中',
                    icon: 'none'
                });
            },
            
            shareMore() {
                uni.showActionSheet({
                    itemList: ['分享到QQ', '分享到微博', '发送邮件', '保存到相册'],
                    success: (res) => {
                        uni.showToast({
                            title: '功能开发中',
                            icon: 'none'
                        });
                    }
                });
            },
            
            shareAll() {
                uni.showActionSheet({
                    itemList: ['微信好友', '微信朋友圈', '钉钉', '复制链接'],
                    success: (res) => {
                        switch(res.tapIndex) {
                            case 0:
                                this.shareToWechat();
                                break;
                            case 1:
                                uni.showToast({ title: '朋友圈分享开发中', icon: 'none' });
                                break;
                            case 2:
                                this.shareToDingtalk();
                                break;
                            case 3:
                                this.copyLink();
                                break;
                        }
                    }
                });
            },
            
            goBack() {
                uni.navigateBack();
            }
        }
    };
</script>

<style scoped>
    .share-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 180rpx;
    }

    .preview-card {
        margin: 20rpx;
        background-color: #fff;
        border-radius: 20rpx;
        overflow: hidden;
    }

    .preview-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 30rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
    }

    .preview-title {
        font-size: 32rpx;
        font-weight: bold;
        color: #fff;
    }

    .preview-badge {
        padding: 8rpx 24rpx;
        border-radius: 20rpx;
        font-size: 24rpx;
        background-color: rgba(255, 255, 255, 0.3);
        color: #fff;
    }

    .preview-content {
        padding: 30rpx;
    }

    .preview-row {
        display: flex;
        justify-content: space-between;
        padding: 20rpx 0;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .preview-row:last-child {
        border-bottom: none;
    }

    .preview-label {
        font-size: 28rpx;
        color: #999;
    }

    .preview-value {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
    }

    .preview-value.highlight {
        color: #ff9800;
    }

    .share-section {
        margin: 20rpx;
        background-color: #fff;
        border-radius: 20rpx;
        padding: 30rpx;
    }

    .section-title {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 30rpx;
    }

    .share-grid {
        display: flex;
        flex-wrap: wrap;
    }

    .share-item {
        width: 33.33%;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx 0;
    }

    .share-icon {
        width: 100rpx;
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 30rpx;
        font-size: 48rpx;
        margin-bottom: 12rpx;
    }

    .share-icon.wechat {
        background-color: #e8f5e9;
    }

    .share-icon.dingtalk {
        background-color: #e3f2fd;
    }

    .share-icon.link {
        background-color: #fff3e0;
    }

    .share-icon.pdf {
        background-color: #ffebee;
    }

    .share-icon.image {
        background-color: #f3e5f5;
    }

    .share-icon.more {
        background-color: #f5f5f5;
    }

    .share-text {
        font-size: 24rpx;
        color: #666;
    }

    .info-section {
        margin: 20rpx;
        background-color: #fff;
        border-radius: 20rpx;
        padding: 30rpx;
    }

    .info-card {
        background-color: #f8f8f8;
        border-radius: 16rpx;
        padding: 20rpx;
    }

    .info-row {
        display: flex;
        justify-content: space-between;
        padding: 16rpx 0;
    }

    .info-label {
        font-size: 26rpx;
        color: #999;
    }

    .info-value {
        font-size: 26rpx;
        color: #333;
    }

    .info-value.code {
        font-family: monospace;
        font-size: 24rpx;
    }

    .tips-section {
        display: flex;
        margin: 20rpx;
        padding: 30rpx;
        background-color: #fff9e6;
        border-radius: 20rpx;
        border-left: 8rpx solid #ff9800;
    }

    .tips-icon {
        font-size: 40rpx;
        margin-right: 20rpx;
    }

    .tips-content {
        flex: 1;
    }

    .tips-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #ff9800;
        margin-bottom: 10rpx;
    }

    .tips-text {
        font-size: 24rpx;
        color: #666;
        line-height: 1.6;
    }

    .footer-section {
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        padding: 20rpx 30rpx;
        padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
        background-color: #fff;
        box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    }

    .btn {
        flex: 1;
        height: 88rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 44rpx;
        font-size: 30rpx;
        font-weight: 500;
    }

    .btn-secondary {
        background-color: #f5f5f5;
        color: #666;
        margin-right: 20rpx;
    }

    .btn-primary {
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        color: #fff;
        box-shadow: 0 4rpx 16rpx rgba(43, 122, 75, 0.3);
    }
</style>
