<template>
    <view class="trace-container">
        <!-- 加载状态 -->
        <view v-if="loading" class="loading-container">
            <view class="loading-spinner"></view>
            <text class="loading-text">正在加载溯源信息...</text>
        </view>

        <!-- 错误状态 -->
        <view v-else-if="error" class="error-container">
            <text class="error-icon">❌</text>
            <text class="error-text">{{ errorMessage }}</text>
            <view class="btn btn-primary btn-sm mt-20" @click="loadTraceInfo">重新加载</view>
        </view>

        <!-- 溯源内容 -->
        <view v-else class="trace-content">
            <!-- 批次基本信息 -->
            <view class="section">
                <view class="section-header">
                    <text class="section-icon">📦</text>
                    <text class="section-title">批次信息</text>
                </view>
                
                <view class="info-card">
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">批次编号</text>
                            <text class="info-value batch-code">{{ traceData.batchInfo ? traceData.batchInfo.batchCode : '' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">种子名称</text>
                            <text class="info-value">{{ traceData.batchInfo ? traceData.batchInfo.seedName : '' }}</text>
                        </view>
                    </view>
                    
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">种子品种</text>
                            <text class="info-value">{{ traceData.batchInfo && traceData.batchInfo.seedVariety ? traceData.batchInfo.seedVariety : '-' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">发芽率</text>
                            <text class="info-value highlight">{{ traceData.batchInfo ? traceData.batchInfo.germinationRate : 0 }}%</text>
                        </view>
                    </view>
                    
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">纯度</text>
                            <text class="info-value">{{ traceData.batchInfo && traceData.batchInfo.purity ? traceData.batchInfo.purity + '%' : '-' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">水分含量</text>
                            <text class="info-value">{{ traceData.batchInfo && traceData.batchInfo.moistureContent ? traceData.batchInfo.moistureContent + '%' : '-' }}</text>
                        </view>
                    </view>
                    
                    <view class="info-row">
                        <view class="info-item">
                            <text class="info-label">生产日期</text>
                            <text class="info-value">{{ traceData.batchInfo ? traceData.batchInfo.productionDate : '' }}</text>
                        </view>
                        <view class="info-item">
                            <text class="info-label">保质期至</text>
                            <text class="info-value">{{ traceData.batchInfo ? traceData.batchInfo.shelfLife : '' }}</text>
                        </view>
                    </view>
                    
                    <view class="info-row">
                        <view class="info-item full">
                            <text class="info-label">数量</text>
                            <text class="info-value">{{ traceData.batchInfo ? traceData.batchInfo.quantity : 0 }} kg</text>
                        </view>
                        <view class="info-item full">
                            <text class="info-label">状态</text>
                            <view class="tag" :class="traceData.batchInfo && traceData.batchInfo.status === 'ACTIVE' ? 'tag-success' : 'tag-danger'">
                                {{ traceData.batchInfo && traceData.batchInfo.status === 'ACTIVE' ? '正常' : '停用' }}
                            </view>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 亲本来源 -->
            <view class="section" v-if="traceData.parentInfo">
                <view class="section-header">
                    <text class="section-icon">🌱</text>
                    <text class="section-title">亲本来源</text>
                </view>
                
                <view class="parent-card">
                    <view class="parent-row">
                        <view class="parent-item female">
                            <view class="parent-label">母本</view>
                            <view class="parent-info">
                                <text class="parent-name">{{ traceData.parentInfo && traceData.parentInfo.femaleParentName ? traceData.parentInfo.femaleParentName : '-' }}</text>
                                <text class="parent-code">{{ traceData.parentInfo && traceData.parentInfo.femaleParentCode ? traceData.parentInfo.femaleParentCode : '-' }}</text>
                                <text class="parent-origin">{{ traceData.parentInfo && traceData.parentInfo.femaleParentOrigin ? traceData.parentInfo.femaleParentOrigin : '-' }}</text>
                            </view>
                        </view>
                        
                        <view class="parent-arrow">×</view>
                        
                        <view class="parent-item male">
                            <view class="parent-label">父本</view>
                            <view class="parent-info">
                                <text class="parent-name">{{ traceData.parentInfo && traceData.parentInfo.maleParentName ? traceData.parentInfo.maleParentName : '-' }}</text>
                                <text class="parent-code">{{ traceData.parentInfo && traceData.parentInfo.maleParentCode ? traceData.parentInfo.maleParentCode : '-' }}</text>
                                <text class="parent-origin">{{ traceData.parentInfo && traceData.parentInfo.maleParentOrigin ? traceData.parentInfo.maleParentOrigin : '-' }}</text>
                            </view>
                        </view>
                    </view>
                    
                    <view class="parent-detail" v-if="traceData.parentInfo && (traceData.parentInfo.breedingMethod || traceData.parentInfo.breedingOrganization)">
                        <view class="detail-item" v-if="traceData.parentInfo && traceData.parentInfo.breedingMethod">
                            <text class="detail-label">育种方式</text>
                            <text class="detail-value">{{ traceData.parentInfo.breedingMethod }}</text>
                        </view>
                        <view class="detail-item" v-if="traceData.parentInfo && traceData.parentInfo.breedingOrganization">
                            <text class="detail-label">育种单位</text>
                            <text class="detail-value">{{ traceData.parentInfo.breedingOrganization }}</text>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 田间管理时间轴 -->
            <view class="section" v-if="traceData.fieldRecords && traceData.fieldRecords.length > 0">
                <view class="section-header">
                    <text class="section-icon">🌾</text>
                    <text class="section-title">田间管理</text>
                    <text class="section-badge">{{ traceData.fieldRecords.length }}条记录</text>
                </view>
                
                <view class="timeline">
                    <view class="timeline-item" v-for="(record, index) in traceData.fieldRecords" :key="index">
                        <view class="timeline-dot" :class="getOperationClass(record.operationType)"></view>
                        <view class="timeline-content">
                            <view class="timeline-header">
                                <text class="timeline-date">{{ record.operationDate }}</text>
                                <view class="tag" :class="getOperationTagClass(record.operationType)">
                                    {{ getOperationTypeText(record.operationType) }}
                                </view>
                            </view>
                            <text class="timeline-title">{{ record.operationName }}</text>
                            <view class="timeline-details" v-if="record.substanceName">
                                <text class="timeline-detail">📦 {{ record.substanceName }}</text>
                                <text class="timeline-detail" v-if="record.dosage">用量：{{ record.dosage }}</text>
                            </view>
                            <view class="timeline-meta">
                                <text class="timeline-meta-item" v-if="record.location">📍 {{ record.location }}</text>
                                <text class="timeline-meta-item" v-if="record.operator">👤 {{ record.operator }}</text>
                            </view>
                            <text class="timeline-remark" v-if="record.remark">{{ record.remark }}</text>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 加工流程 -->
            <view class="section" v-if="traceData.processingRecords && traceData.processingRecords.length > 0">
                <view class="section-header">
                    <text class="section-icon">🏭</text>
                    <text class="section-title">加工流程</text>
                </view>
                
                <view class="process-list">
                    <view class="process-item" v-for="(process, index) in traceData.processingRecords" :key="index">
                        <view class="process-step">
                            <text class="step-number">{{ index + 1 }}</text>
                            <text class="step-name">{{ getProcessStepText(process.processStep) }}</text>
                        </view>
                        <view class="process-info">
                            <view class="process-row">
                                <text class="process-label">日期</text>
                                <text class="process-value">{{ process.processDate }}</text>
                            </view>
                            <view class="process-row" v-if="process.equipmentName">
                                <text class="process-label">设备</text>
                                <text class="process-value">{{ process.equipmentName }} {{ process.equipmentModel || '' }}</text>
                            </view>
                            <view class="process-row" v-if="process.processParameter">
                                <text class="process-label">参数</text>
                                <text class="process-value">{{ process.processParameter }}</text>
                            </view>
                            <view class="process-row">
                                <text class="process-label">结果</text>
                                <view class="tag" :class="process.qualityCheckResult === 'PASS' ? 'tag-success' : 'tag-danger'">
                                    {{ process.qualityCheckResult === 'PASS' ? '合格' : '不合格' }}
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 质检报告 -->
            <view class="section" v-if="traceData.qualityReports && traceData.qualityReports.length > 0">
                <view class="section-header">
                    <text class="section-icon">📋</text>
                    <text class="section-title">质检报告</text>
                </view>
                
                <view class="report-card" v-for="(report, index) in traceData.qualityReports" :key="index">
                    <view class="report-header">
                        <view class="report-title">
                            <text class="report-no">{{ report.reportNo || '质检报告' }}</text>
                            <text class="report-date">{{ report.inspectionDate }}</text>
                        </view>
                        <view class="report-result" :class="report.overallResult === 'QUALIFIED' ? 'qualified' : 'unqualified'">
                            {{ report.overallResult === 'QUALIFIED' ? '合格' : '不合格' }}
                        </view>
                    </view>
                    
                    <view class="report-stats">
                        <view class="stat-item">
                            <text class="stat-value">{{ report.germinationRateTest ? report.germinationRateTest + '%' : '-' }}</text>
                            <text class="stat-label">发芽率</text>
                        </view>
                        <view class="stat-item">
                            <text class="stat-value">{{ report.purityTest ? report.purityTest + '%' : '-' }}</text>
                            <text class="stat-label">纯度</text>
                        </view>
                        <view class="stat-item">
                            <text class="stat-value">{{ report.moistureTest ? report.moistureTest + '%' : '-' }}</text>
                            <text class="stat-label">水分</text>
                        </view>
                        <view class="stat-item">
                            <text class="stat-value">{{ report.clarityTest ? report.clarityTest + '%' : '-' }}</text>
                            <text class="stat-label">净度</text>
                        </view>
                    </view>
                    
                    <view class="report-conclusion" v-if="report.conclusion">
                        <text class="conclusion-label">检验结论</text>
                        <text class="conclusion-text">{{ report.conclusion }}</text>
                    </view>
                    
                    <view class="report-footer">
                        <text class="footer-text">检验机构：{{ report.inspectionOrganization || '-' }}</text>
                        <text class="footer-text" v-if="report.inspector">检验人员：{{ report.inspector }}</text>
                    </view>
                </view>
            </view>

            <!-- 销售记录 -->
            <view class="section" v-if="traceData.salesRecords && traceData.salesRecords.length > 0">
                <view class="section-header">
                    <text class="section-icon">💰</text>
                    <text class="section-title">销售记录</text>
                </view>
                
                <view class="sales-list">
                    <view class="sales-item" v-for="(sales, index) in traceData.salesRecords" :key="index">
                        <view class="sales-header">
                            <text class="sales-customer">{{ sales.customerName }}</text>
                            <text class="sales-date">{{ sales.salesDate }}</text>
                        </view>
                        <view class="sales-info">
                            <view class="sales-row">
                                <text class="sales-label">联系电话</text>
                                <text class="sales-value">{{ maskPhone(sales.customerPhone) }}</text>
                            </view>
                            <view class="sales-row">
                                <text class="sales-label">购买数量</text>
                                <text class="sales-value">{{ sales.purchaseQuantity }} kg</text>
                            </view>
                            <view class="sales-row">
                                <text class="sales-label">单价</text>
                                <text class="sales-value">¥{{ sales.unitPrice }}/kg</text>
                            </view>
                            <view class="sales-row highlight">
                                <text class="sales-label">总金额</text>
                                <text class="sales-value total">¥{{ sales.totalAmount }}</text>
                            </view>
                            <view class="sales-row" v-if="sales.salesChannel">
                                <text class="sales-label">销售渠道</text>
                                <text class="sales-value">{{ sales.salesChannel }}</text>
                            </view>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 查询信息 -->
            <view class="query-info">
                <text class="query-text">查询时间：{{ traceData.queryTime }}</text>
                <text class="query-text">查询ID：{{ traceData.queryId }}</text>
            </view>
        </view>

        <!-- 底部操作栏 -->
        <view class="bottom-bar" v-if="!loading && !error">
            <view class="action-item" @click="exportPdf">
                <text class="action-icon">📄</text>
                <text class="action-text">导出PDF</text>
            </view>
            <view class="action-item" @click="shareReport">
                <text class="action-icon">📤</text>
                <text class="action-text">分享报告</text>
            </view>
            <view class="action-item primary" @click="goBack">
                <text class="action-icon">🔙</text>
                <text class="action-text">返回</text>
            </view>
        </view>
    </view>
</template>

<script>
    import { traceApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                batchCode: '',
                loading: true,
                error: false,
                errorMessage: '',
                traceData: {}
            };
        },
        
        onLoad(options) {
            if (options.batchCode) {
                this.batchCode = options.batchCode;
                this.loadTraceInfo();
            }
        },
        
        onShow() {
            if (this.batchCode && this.loading) {
                this.loadTraceInfo();
            }
        },
        
        onPullDownRefresh() {
            this.loadTraceInfo();
            setTimeout(() => {
                uni.stopPullDownRefresh();
            }, 1000);
        },
        
        methods: {
            async loadTraceInfo() {
                this.loading = true;
                this.error = false;
                
                try {
                    const res = await traceApi.queryTrace(this.batchCode);
                    
                    if (res.code === 200 && res.data) {
                        this.traceData = res.data;
                    } else {
                        this.error = true;
                        this.errorMessage = res.message || '查询失败';
                    }
                } catch (e) {
                    console.error('查询溯源信息失败', e);
                    this.error = true;
                    this.errorMessage = '网络错误，请稍后重试';
                    
                    // 使用模拟数据
                    this.loadMockData();
                } finally {
                    this.loading = false;
                }
            },
            
            loadMockData() {
                this.traceData = {
                    batchInfo: {
                        batchCode: this.batchCode || 'SD2024A1',
                        seedName: '玉米种子',
                        seedVariety: '郑单958',
                        germinationRate: 95.5,
                        purity: 98.0,
                        moistureContent: 12.5,
                        productionDate: '2024-01-15',
                        shelfLife: '2025-12-31',
                        quantity: 5000,
                        status: 'ACTIVE'
                    },
                    parentInfo: {
                        femaleParentName: '郑58母本',
                        femaleParentCode: 'ZM001-F',
                        femaleParentOrigin: '河南省农科院',
                        maleParentName: '昌7-2父本',
                        maleParentCode: 'ZM001-M',
                        maleParentOrigin: '河南省农科院',
                        breedingMethod: '杂交育种',
                        breedingOrganization: '河南省农业科学院'
                    },
                    fieldRecords: [
                        {
                            operationDate: '2024-01-20',
                            operationType: 'SOWING',
                            operationName: '播种',
                            location: '河南省周口市',
                            operator: '张三'
                        },
                        {
                            operationDate: '2024-02-15',
                            operationType: 'FERTILIZER',
                            operationName: '基肥施用',
                            substanceName: '复合肥',
                            dosage: '30kg/亩',
                            location: '河南省周口市',
                            operator: '李四',
                            remark: '配合翻耕'
                        },
                        {
                            operationDate: '2024-03-10',
                            operationType: 'PESTICIDE',
                            operationName: '病虫害防治',
                            substanceName: '吡虫啉',
                            dosage: '50ml/亩',
                            concentration: '10%可湿性粉剂',
                            location: '河南省周口市',
                            operator: '王五',
                            remark: '防治蚜虫'
                        },
                        {
                            operationDate: '2024-04-20',
                            operationType: 'WATERING',
                            operationName: '灌溉',
                            applicationMethod: '滴灌',
                            location: '河南省周口市',
                            operator: '张三',
                            remark: '拔节期浇水'
                        },
                        {
                            operationDate: '2024-08-15',
                            operationType: 'HARVEST',
                            operationName: '收获',
                            location: '河南省周口市',
                            operator: '李四',
                            remark: '成熟度95%'
                        }
                    ],
                    processingRecords: [
                        {
                            processDate: '2024-08-20',
                            processStep: 'CLEANING',
                            equipmentName: '种子清选机',
                            equipmentModel: '5XZ-5.0',
                            processParameter: '风速3m/s, 振动频率1200次/分',
                            qualityCheckResult: 'PASS'
                        },
                        {
                            processDate: '2024-08-22',
                            processStep: 'GRADING',
                            equipmentName: '种子分级机',
                            equipmentModel: '5XF-10',
                            processParameter: '筛孔4.5mm, 5.0mm, 5.5mm',
                            qualityCheckResult: 'PASS'
                        },
                        {
                            processDate: '2024-08-25',
                            processStep: 'DRYING',
                            equipmentName: '种子烘干机',
                            equipmentModel: '5HG-20',
                            processParameter: '温度35℃, 湿度10%, 时间12h',
                            qualityCheckResult: 'PASS'
                        },
                        {
                            processDate: '2024-08-28',
                            processStep: 'PACKAGING',
                            equipmentName: '自动包装机',
                            equipmentModel: 'DXDK-200',
                            processParameter: '每袋5kg, 真空包装',
                            qualityCheckResult: 'PASS'
                        }
                    ],
                    qualityReports: [
                        {
                            reportNo: 'QB20240830001',
                            inspectionDate: '2024-08-30',
                            inspectionOrganization: '河南省种子质量检验站',
                            inspector: '王检验员',
                            germinationRateTest: 95.5,
                            purityTest: 98.0,
                            moistureTest: 12.5,
                            clarityTest: 99.0,
                            overallResult: 'QUALIFIED',
                            conclusion: '该批次种子各项指标均达到国家标准，建议准予销售'
                        }
                    ],
                    salesRecords: [
                        {
                            customerName: '河南农资公司',
                            customerPhone: '138****8001',
                            purchaseQuantity: 1000,
                            unitPrice: 25.00,
                            totalAmount: 25000.00,
                            salesChannel: '渠道销售',
                            salesDate: '2024-09-01'
                        },
                        {
                            customerName: '周口种养殖合作社',
                            customerPhone: '139****8002',
                            purchaseQuantity: 2000,
                            unitPrice: 24.50,
                            totalAmount: 49000.00,
                            salesChannel: '直接销售',
                            salesDate: '2024-09-10'
                        }
                    ],
                    queryTime: new Date().toLocaleString(),
                    queryId: 'MOCK-' + Date.now()
                };
                
                this.error = false;
            },
            
            getOperationClass(type) {
                switch(type) {
                    case 'SOWING': return '';
                    case 'FERTILIZER': return '';
                    case 'PESTICIDE': return 'warning';
                    case 'WATERING': return '';
                    case 'HARVEST': return '';
                    default: return '';
                }
            },
            
            getOperationTagClass(type) {
                switch(type) {
                    case 'SOWING': return 'tag-info';
                    case 'FERTILIZER': return 'tag-success';
                    case 'PESTICIDE': return 'tag-warning';
                    case 'WATERING': return 'tag-info';
                    case 'HARVEST': return 'tag-success';
                    default: return 'tag-default';
                }
            },
            
            getOperationTypeText(type) {
                switch(type) {
                    case 'SOWING': return '播种';
                    case 'FERTILIZER': return '施肥';
                    case 'PESTICIDE': return '打药';
                    case 'WATERING': return '浇水';
                    case 'HARVEST': return '收获';
                    default: return type;
                }
            },
            
            getProcessStepText(step) {
                switch(step) {
                    case 'CLEANING': return '清选';
                    case 'GRADING': return '分级';
                    case 'DRYING': return '干燥';
                    case 'PACKAGING': return '包装';
                    default: return step;
                }
            },
            
            maskPhone(phone) {
                if (!phone) return '***';
                if (phone.includes('****')) return phone;
                if (phone.length >= 11) {
                    return phone.substring(0, 3) + '****' + phone.substring(7);
                }
                return phone;
            },
            
            async exportPdf() {
                uni.showLoading({ title: '正在生成PDF...' });
                
                try {
                    const pdfUrl = traceApi.exportPdf(this.batchCode);
                    console.log('PDF URL:', pdfUrl);
                    
                    // 下载并打开PDF
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
                                    },
                                    fail: (err) => {
                                        console.error('打开PDF失败', err);
                                        // 提示用户手动打开
                                        uni.showModal({
                                            title: '提示',
                                            content: 'PDF已生成，请在文件管理器中查看',
                                            showCancel: false
                                        });
                                    }
                                });
                            }
                        },
                        fail: (err) => {
                            uni.hideLoading();
                            console.error('下载PDF失败', err);
                            uni.showToast({
                                title: '生成PDF失败',
                                icon: 'none'
                            });
                        }
                    });
                } catch (e) {
                    uni.hideLoading();
                    uni.showToast({
                        title: 'PDF功能需要后端支持',
                        icon: 'none'
                    });
                }
            },
            
            shareReport() {
                uni.showActionSheet({
                    itemList: ['分享到微信', '分享到钉钉', '复制链接', '生成图片'],
                    success: (res) => {
                        const shareUrl = `https://seedtrace.com/trace/${this.batchCode}`;
                        
                        switch(res.tapIndex) {
                            case 0:
                                // 微信分享
                                uni.showToast({
                                    title: '微信分享需要配置',
                                    icon: 'none'
                                });
                                break;
                            case 1:
                                // 钉钉分享
                                uni.showToast({
                                    title: '钉钉分享需要配置',
                                    icon: 'none'
                                });
                                break;
                            case 2:
                                // 复制链接
                                uni.setClipboardData({
                                    data: shareUrl,
                                    success: () => {
                                        uni.showToast({
                                            title: '链接已复制',
                                            icon: 'success'
                                        });
                                    }
                                });
                                break;
                            case 3:
                                // 生成图片
                                uni.showToast({
                                    title: '生成图片功能开发中',
                                    icon: 'none'
                                });
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
    .trace-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 160rpx;
    }

    .loading-container, .error-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 200rpx 0;
    }

    .loading-spinner {
        width: 80rpx;
        height: 80rpx;
        border: 6rpx solid #f0f0f0;
        border-top-color: #2b7a4b;
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }

    @keyframes spin {
        to { transform: rotate(360deg); }
    }

    .loading-text, .error-text {
        margin-top: 30rpx;
        font-size: 28rpx;
        color: #999;
    }

    .error-icon {
        font-size: 100rpx;
        margin-bottom: 30rpx;
    }

    .trace-content {
        padding: 20rpx;
    }

    .section {
        margin-bottom: 30rpx;
    }

    .section-header {
        display: flex;
        align-items: center;
        padding: 20rpx 30rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 16rpx 16rpx 0 0;
    }

    .section-icon {
        font-size: 36rpx;
        margin-right: 16rpx;
    }

    .section-title {
        flex: 1;
        font-size: 30rpx;
        font-weight: bold;
        color: #fff;
    }

    .section-badge {
        padding: 6rpx 20rpx;
        background-color: rgba(255, 255, 255, 0.3);
        border-radius: 20rpx;
        font-size: 22rpx;
        color: #fff;
    }

    .info-card, .parent-card, .process-list, .sales-list {
        background-color: #fff;
        border-radius: 0 0 16rpx 16rpx;
        padding: 30rpx;
    }

    .info-row {
        display: flex;
        margin-bottom: 24rpx;
    }

    .info-row:last-child {
        margin-bottom: 0;
    }

    .info-item {
        flex: 1;
    }

    .info-item.full {
        flex: 1;
        padding: 16rpx 20rpx;
        background-color: #f8f8f8;
        border-radius: 12rpx;
        margin-right: 16rpx;
    }

    .info-item.full:last-child {
        margin-right: 0;
    }

    .info-label {
        display: block;
        font-size: 24rpx;
        color: #999;
        margin-bottom: 8rpx;
    }

    .info-value {
        font-size: 28rpx;
        color: #333;
        font-weight: 500;
    }

    .info-value.batch-code {
        font-family: monospace;
        color: #2b7a4b;
    }

    .info-value.highlight {
        color: #ff9800;
    }

    .parent-row {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 30rpx 0;
    }

    .parent-item {
        flex: 1;
        padding: 24rpx;
        background-color: #f8f8f8;
        border-radius: 16rpx;
        text-align: center;
    }

    .parent-item.female {
        background-color: #fff0f5;
    }

    .parent-item.male {
        background-color: #e3f2fd;
    }

    .parent-arrow {
        width: 60rpx;
        height: 60rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #ff9800;
        border-radius: 50%;
        color: #fff;
        font-size: 28rpx;
        font-weight: bold;
        margin: 0 20rpx;
    }

    .parent-label {
        font-size: 24rpx;
        color: #999;
        margin-bottom: 12rpx;
    }

    .parent-name {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 8rpx;
    }

    .parent-code {
        display: block;
        font-size: 22rpx;
        color: #666;
        margin-bottom: 4rpx;
    }

    .parent-origin {
        display: block;
        font-size: 22rpx;
        color: #999;
    }

    .parent-detail {
        display: flex;
        margin-top: 20rpx;
        padding-top: 20rpx;
        border-top: 2rpx solid #f0f0f0;
    }

    .detail-item {
        flex: 1;
    }

    .detail-label {
        display: block;
        font-size: 22rpx;
        color: #999;
        margin-bottom: 6rpx;
    }

    .detail-value {
        font-size: 26rpx;
        color: #333;
    }

    .timeline {
        position: relative;
        padding: 30rpx;
        padding-left: 80rpx;
        background-color: #fff;
        border-radius: 0 0 16rpx 16rpx;
    }

    .timeline::before {
        content: '';
        position: absolute;
        left: 44rpx;
        top: 30rpx;
        bottom: 30rpx;
        width: 4rpx;
        background: linear-gradient(to bottom, #2b7a4b, #e0e0e0);
    }

    .timeline-item {
        position: relative;
        margin-bottom: 30rpx;
    }

    .timeline-item:last-child {
        margin-bottom: 0;
    }

    .timeline-dot {
        position: absolute;
        left: -62rpx;
        top: 20rpx;
        width: 32rpx;
        height: 32rpx;
        background-color: #2b7a4b;
        border: 6rpx solid #fff;
        border-radius: 50%;
        box-shadow: 0 0 0 4rpx rgba(43, 122, 75, 0.2);
    }

    .timeline-dot.warning {
        background-color: #ff9800;
        box-shadow: 0 0 0 4rpx rgba(255, 152, 0, 0.2);
    }

    .timeline-content {
        background-color: #f8f8f8;
        border-radius: 16rpx;
        padding: 24rpx;
    }

    .timeline-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 12rpx;
    }

    .timeline-date {
        font-size: 24rpx;
        color: #999;
    }

    .timeline-title {
        display: block;
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 12rpx;
    }

    .timeline-details {
        margin-bottom: 12rpx;
    }

    .timeline-detail {
        display: block;
        font-size: 24rpx;
        color: #666;
        margin-bottom: 4rpx;
    }

    .timeline-meta {
        display: flex;
        margin-bottom: 8rpx;
    }

    .timeline-meta-item {
        font-size: 22rpx;
        color: #999;
        margin-right: 20rpx;
    }

    .timeline-remark {
        font-size: 22rpx;
        color: #666;
        padding: 12rpx 16rpx;
        background-color: #fff;
        border-left: 4rpx solid #2b7a4b;
        border-radius: 8rpx;
    }

    .process-item {
        display: flex;
        margin-bottom: 24rpx;
        padding-bottom: 24rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .process-item:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
    }

    .process-step {
        width: 120rpx;
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-right: 24rpx;
    }

    .step-number {
        width: 60rpx;
        height: 60rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 50%;
        color: #fff;
        font-size: 28rpx;
        font-weight: bold;
        margin-bottom: 12rpx;
    }

    .step-name {
        font-size: 24rpx;
        color: #333;
        font-weight: 500;
    }

    .process-info {
        flex: 1;
    }

    .process-row {
        display: flex;
        margin-bottom: 12rpx;
    }

    .process-row:last-child {
        margin-bottom: 0;
    }

    .process-label {
        width: 120rpx;
        font-size: 24rpx;
        color: #999;
    }

    .process-value {
        flex: 1;
        font-size: 26rpx;
        color: #333;
    }

    .report-card {
        background-color: #fff;
        border-radius: 0 0 16rpx 16rpx;
        padding: 30rpx;
        margin-bottom: 20rpx;
    }

    .report-card:last-child {
        margin-bottom: 0;
    }

    .report-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24rpx;
        padding-bottom: 20rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .report-title {
        display: flex;
        flex-direction: column;
    }

    .report-no {
        font-size: 30rpx;
        font-weight: bold;
        color: #333;
        margin-bottom: 8rpx;
    }

    .report-date {
        font-size: 24rpx;
        color: #999;
    }

    .report-result {
        padding: 12rpx 30rpx;
        border-radius: 30rpx;
        font-size: 28rpx;
        font-weight: bold;
    }

    .report-result.qualified {
        background-color: #e8f5e9;
        color: #2b7a4b;
    }

    .report-result.unqualified {
        background-color: #ffebee;
        color: #f44336;
    }

    .report-stats {
        display: flex;
        margin-bottom: 24rpx;
    }

    .stat-item {
        flex: 1;
        text-align: center;
    }

    .stat-value {
        display: block;
        font-size: 32rpx;
        font-weight: bold;
        color: #2b7a4b;
        margin-bottom: 8rpx;
    }

    .stat-label {
        font-size: 22rpx;
        color: #999;
    }

    .report-conclusion {
        padding: 20rpx;
        background-color: #f8f8f8;
        border-radius: 12rpx;
        margin-bottom: 20rpx;
    }

    .conclusion-label {
        display: block;
        font-size: 24rpx;
        color: #999;
        margin-bottom: 10rpx;
    }

    .conclusion-text {
        font-size: 26rpx;
        color: #333;
        line-height: 1.6;
    }

    .report-footer {
        display: flex;
        justify-content: space-between;
    }

    .footer-text {
        font-size: 22rpx;
        color: #999;
    }

    .sales-item {
        padding: 24rpx;
        background-color: #f8f8f8;
        border-radius: 16rpx;
        margin-bottom: 20rpx;
    }

    .sales-item:last-child {
        margin-bottom: 0;
    }

    .sales-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16rpx;
        padding-bottom: 16rpx;
        border-bottom: 2rpx solid #e0e0e0;
    }

    .sales-customer {
        font-size: 28rpx;
        font-weight: 600;
        color: #333;
    }

    .sales-date {
        font-size: 24rpx;
        color: #999;
    }

    .sales-info {
        padding: 0 10rpx;
    }

    .sales-row {
        display: flex;
        margin-bottom: 12rpx;
    }

    .sales-row:last-child {
        margin-bottom: 0;
    }

    .sales-row.highlight {
        padding-top: 12rpx;
        margin-top: 8rpx;
        background-color: #fff9e6;
        border-radius: 8rpx;
        margin-left: -10rpx;
        margin-right: -10rpx;
        padding-left: 20rpx;
        padding-right: 20rpx;
    }

    .sales-label {
        width: 140rpx;
        font-size: 24rpx;
        color: #999;
    }

    .sales-value {
        flex: 1;
        font-size: 26rpx;
        color: #333;
    }

    .sales-value.total {
        color: #ff9800;
        font-weight: bold;
        font-size: 30rpx;
    }

    .query-info {
        padding: 30rpx;
        text-align: center;
    }

    .query-text {
        display: block;
        font-size: 22rpx;
        color: #999;
        margin-bottom: 8rpx;
    }

    .bottom-bar {
        position: fixed;
        bottom: 0;
        left: 0;
        right: 0;
        display: flex;
        background-color: #fff;
        padding: 20rpx 30rpx;
        padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
        box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    }

    .action-item {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 16rpx 0;
    }

    .action-item.primary {
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
        border-radius: 40rpx;
        margin-left: 20rpx;
    }

    .action-icon {
        font-size: 40rpx;
        margin-bottom: 6rpx;
    }

    .action-text {
        font-size: 22rpx;
        color: #333;
    }

    .action-item.primary .action-text {
        color: #fff;
    }

    .tag {
        display: inline-flex;
        align-items: center;
        padding: 4rpx 16rpx;
        border-radius: 20rpx;
        font-size: 20rpx;
    }

    .tag-success {
        background-color: #e8f5e9;
        color: #2b7a4b;
    }

    .tag-danger {
        background-color: #ffebee;
        color: #f44336;
    }

    .tag-warning {
        background-color: #fff3e0;
        color: #ff9800;
    }

    .tag-info {
        background-color: #e3f2fd;
        color: #2196f3;
    }

    .tag-default {
        background-color: #f5f5f5;
        color: #666;
    }
</style>
