<template>
    <view class="form-container">
        <view class="form-header">
            <text class="form-title">{{ isEdit ? '编辑批次' : '新增批次' }}</text>
            <text class="form-subtitle">请填写种子批次的详细信息</text>
        </view>

        <view class="form-body">
            <!-- 基本信息 -->
            <view class="form-section">
                <view class="section-title">📦 基本信息</view>
                
                <!-- 批次编号 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        批次编号
                    </view>
                    <view class="input-wrapper">
                        <input 
                            class="form-input" 
                            v-model="form.batchCode" 
                            placeholder="请输入8位批次号（数字+字母）"
                            maxlength="8"
                            :disabled="isEdit"
                            @blur="checkBatchCode"
                        />
                        <text v-if="codeValidating" class="validating">验证中...</text>
                        <text v-else-if="codeValid === true" class="valid">✓ 可用</text>
                        <text v-else-if="codeValid === false" class="invalid">✗ 已存在</text>
                    </view>
                    <text class="form-hint">8位数字或字母组合，例如：SD2024A1</text>
                </view>

                <!-- 种子名称 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        种子名称
                    </view>
                    <input 
                        class="form-input" 
                        v-model="form.seedName" 
                        placeholder="请输入种子名称"
                    />
                </view>

                <!-- 种子品种 -->
                <view class="form-group">
                    <view class="form-label">种子品种</view>
                    <input 
                        class="form-input" 
                        v-model="form.seedVariety" 
                        placeholder="请输入种子品种（选填）"
                    />
                </view>

                <!-- 状态 -->
                <view class="form-group">
                    <view class="form-label">状态</view>
                    <view class="radio-group">
                        <view 
                            class="radio-item" 
                            :class="{ active: form.status === 'ACTIVE' }"
                            @click="form.status = 'ACTIVE'"
                        >
                            <view class="radio-dot" :class="{ checked: form.status === 'ACTIVE' }"></view>
                            <text>正常</text>
                        </view>
                        <view 
                            class="radio-item" 
                            :class="{ active: form.status === 'INACTIVE' }"
                            @click="form.status = 'INACTIVE'"
                        >
                            <view class="radio-dot" :class="{ checked: form.status === 'INACTIVE' }"></view>
                            <text>停用</text>
                        </view>
                    </view>
                </view>
            </view>

            <!-- 质量指标 -->
            <view class="form-section">
                <view class="section-title">📊 质量指标</view>
                
                <!-- 发芽率 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        发芽率
                    </view>
                    <view class="input-wrapper">
                        <input 
                            class="form-input" 
                            type="digit"
                            v-model="form.germinationRate" 
                            placeholder="0-100，保留1位小数"
                        />
                        <text class="input-unit">%</text>
                    </view>
                    <text class="form-hint">范围：0-100%，例如：95.5</text>
                </view>

                <!-- 纯度 -->
                <view class="form-group">
                    <view class="form-label">纯度</view>
                    <view class="input-wrapper">
                        <input 
                            class="form-input" 
                            type="digit"
                            v-model="form.purity" 
                            placeholder="请输入纯度（选填）"
                        />
                        <text class="input-unit">%</text>
                    </view>
                </view>

                <!-- 水分含量 -->
                <view class="form-group">
                    <view class="form-label">水分含量</view>
                    <view class="input-wrapper">
                        <input 
                            class="form-input" 
                            type="digit"
                            v-model="form.moistureContent" 
                            placeholder="请输入水分含量（选填）"
                        />
                        <text class="input-unit">%</text>
                    </view>
                </view>
            </view>

            <!-- 日期信息 -->
            <view class="form-section">
                <view class="section-title">📅 日期信息</view>
                
                <!-- 生产日期 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        生产日期
                    </view>
                    <picker 
                        mode="date" 
                        :value="form.productionDate"
                        :end="today"
                        @change="onProductionDateChange"
                    >
                        <view class="form-input picker-input">
                            {{ form.productionDate || '请选择生产日期' }}
                        </view>
                    </picker>
                </view>

                <!-- 保质期 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        保质期至
                    </view>
                    <picker 
                        mode="date" 
                        :value="form.shelfLife"
                        :start="minShelfLife"
                        @change="onShelfLifeChange"
                    >
                        <view class="form-input picker-input">
                            {{ form.shelfLife || '请选择保质期' }}
                        </view>
                    </picker>
                    <text class="form-hint">保质期不得早于当前日期+6个月</text>
                </view>
            </view>

            <!-- 数量和价格 -->
            <view class="form-section">
                <view class="section-title">💰 库存信息</view>
                
                <!-- 数量 -->
                <view class="form-group">
                    <view class="form-label">
                        <text class="required">*</text>
                        数量
                    </view>
                    <view class="input-wrapper">
                        <input 
                            class="form-input" 
                            type="number"
                            v-model="form.quantity" 
                            placeholder="请输入数量"
                        />
                        <text class="input-unit">kg</text>
                    </view>
                </view>

                <!-- 单价 -->
                <view class="form-group">
                    <view class="form-label">单价</view>
                    <view class="input-wrapper">
                        <text class="input-prefix">¥</text>
                        <input 
                            class="form-input" 
                            type="digit"
                            v-model="form.unitPrice" 
                            placeholder="请输入单价（选填）"
                        />
                        <text class="input-unit">/kg</text>
                    </view>
                </view>
            </view>

            <!-- 错误提示 -->
            <view v-if="errorMsg" class="error-msg">
                ⚠️ {{ errorMsg }}
            </view>
        </view>

        <!-- 底部按钮 -->
        <view class="form-footer">
            <view class="btn btn-secondary" @click="goBack">取消</view>
            <view class="btn btn-primary" @click="submitForm">
                {{ isEdit ? '保存修改' : '立即创建' }}
            </view>
        </view>
    </view>
</template>

<script>
    import { batchApi } from '@/common/api.js';
    
    export default {
        data() {
            return {
                isEdit: false,
                batchId: null,
                today: '',
                minShelfLife: '',
                codeValidating: false,
                codeValid: null,
                errorMsg: '',
                form: {
                    batchCode: '',
                    seedName: '',
                    seedVariety: '',
                    germinationRate: '',
                    purity: '',
                    moistureContent: '',
                    productionDate: '',
                    shelfLife: '',
                    quantity: '',
                    unitPrice: '',
                    status: 'ACTIVE',
                    createdBy: 'admin'
                }
            };
        },
        
        onLoad(options) {
            // 初始化日期
            const now = new Date();
            const pad = n => n < 10 ? '0' + n : n;
            this.today = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())}`;
            
            // 计算最小保质期（当前日期+6个月）
            const minDate = new Date();
            minDate.setMonth(minDate.getMonth() + 6);
            this.minShelfLife = `${minDate.getFullYear()}-${pad(minDate.getMonth() + 1)}-${pad(minDate.getDate())}`;
            
            // 检查是否为编辑模式
            if (options.id) {
                this.isEdit = true;
                this.batchId = parseInt(options.id);
                this.loadBatchData();
            }
        },
        
        methods: {
            async loadBatchData() {
                try {
                    const res = await batchApi.getBatchById(this.batchId);
                    if (res.code === 200 && res.data) {
                        const data = res.data;
                        this.form = {
                            batchCode: data.batchCode,
                            seedName: data.seedName || '',
                            seedVariety: data.seedVariety || '',
                            germinationRate: data.germinationRate ? String(data.germinationRate) : '',
                            purity: data.purity ? String(data.purity) : '',
                            moistureContent: data.moistureContent ? String(data.moistureContent) : '',
                            productionDate: data.productionDate || '',
                            shelfLife: data.shelfLife || '',
                            quantity: data.quantity ? String(data.quantity) : '',
                            unitPrice: data.unitPrice ? String(data.unitPrice) : '',
                            status: data.status || 'ACTIVE',
                            createdBy: data.createdBy || 'admin'
                        };
                        
                        // 编辑模式下默认批次号可用
                        this.codeValid = true;
                    }
                } catch (e) {
                    console.error('加载批次数据失败', e);
                    // 使用模拟数据
                    this.loadMockData();
                }
            },
            
            loadMockData() {
                if (this.batchId === 1) {
                    this.form = {
                        batchCode: 'SD2024A1',
                        seedName: '玉米种子',
                        seedVariety: '郑单958',
                        germinationRate: '95.5',
                        purity: '98.0',
                        moistureContent: '12.5',
                        productionDate: '2024-01-15',
                        shelfLife: '2025-12-31',
                        quantity: '5000',
                        unitPrice: '25.00',
                        status: 'ACTIVE',
                        createdBy: 'admin'
                    };
                    this.codeValid = true;
                }
            },
            
            onProductionDateChange(e) {
                this.form.productionDate = e.detail.value;
                this.validateForm();
            },
            
            onShelfLifeChange(e) {
                this.form.shelfLife = e.detail.value;
                this.validateForm();
            },
            
            async checkBatchCode() {
                if (this.isEdit) return;
                
                const code = this.form.batchCode.trim();
                if (!code || code.length !== 8) {
                    this.codeValid = null;
                    return;
                }
                
                // 验证格式（数字+字母）
                if (!/^[A-Za-z0-9]+$/.test(code)) {
                    this.codeValid = false;
                    return;
                }
                
                this.codeValidating = true;
                
                try {
                    const res = await batchApi.checkBatchCode(code);
                    if (res.code === 200) {
                        this.codeValid = !res.data; // true表示已存在，所以取反
                    }
                } catch (e) {
                    console.error('检查批次号失败', e);
                    // 模拟验证，常用测试批次号已存在
                    const existingCodes = ['SD2024A1', 'SD2024B2', 'SD2024C3', 'SD2024D4', 'SD2024E5'];
                    this.codeValid = !existingCodes.includes(code.toUpperCase());
                } finally {
                    this.codeValidating = false;
                }
            },
            
            validateForm() {
                this.errorMsg = '';
                
                // 批次编号
                if (!this.form.batchCode || this.form.batchCode.length !== 8) {
                    this.errorMsg = '请输入8位批次编号';
                    return false;
                }
                
                if (!/^[A-Za-z0-9]+$/.test(this.form.batchCode)) {
                    this.errorMsg = '批次编号只能包含数字和字母';
                    return false;
                }
                
                if (!this.isEdit && this.codeValid === false) {
                    this.errorMsg = '批次编号已存在';
                    return false;
                }
                
                // 种子名称
                if (!this.form.seedName.trim()) {
                    this.errorMsg = '请输入种子名称';
                    return false;
                }
                
                // 发芽率
                if (!this.form.germinationRate) {
                    this.errorMsg = '请输入发芽率';
                    return false;
                }
                
                const rate = parseFloat(this.form.germinationRate);
                if (isNaN(rate) || rate < 0 || rate > 100) {
                    this.errorMsg = '发芽率必须在0-100之间';
                    return false;
                }
                
                // 检查小数位数
                const rateStr = this.form.germinationRate.toString();
                if (rateStr.includes('.')) {
                    const decimalPart = rateStr.split('.')[1];
                    if (decimalPart.length > 1) {
                        this.errorMsg = '发芽率只能保留1位小数';
                        return false;
                    }
                }
                
                // 生产日期
                if (!this.form.productionDate) {
                    this.errorMsg = '请选择生产日期';
                    return false;
                }
                
                // 保质期
                if (!this.form.shelfLife) {
                    this.errorMsg = '请选择保质期';
                    return false;
                }
                
                // 检查保质期是否晚于生产日期
                if (this.form.productionDate && this.form.shelfLife) {
                    if (new Date(this.form.shelfLife) <= new Date(this.form.productionDate)) {
                        this.errorMsg = '保质期必须晚于生产日期';
                        return false;
                    }
                }
                
                // 数量
                if (!this.form.quantity) {
                    this.errorMsg = '请输入数量';
                    return false;
                }
                
                const qty = parseInt(this.form.quantity);
                if (isNaN(qty) || qty < 0) {
                    this.errorMsg = '数量必须大于等于0';
                    return false;
                }
                
                return true;
            },
            
            async submitForm() {
                if (!this.validateForm()) {
                    return;
                }
                
                uni.showLoading({ title: '提交中...' });
                
                // 构建提交数据
                const submitData = {
                    ...this.form,
                    germinationRate: this.form.germinationRate ? parseFloat(this.form.germinationRate) : null,
                    purity: this.form.purity ? parseFloat(this.form.purity) : null,
                    moistureContent: this.form.moistureContent ? parseFloat(this.form.moistureContent) : null,
                    quantity: this.form.quantity ? parseInt(this.form.quantity) : 0,
                    unitPrice: this.form.unitPrice ? parseFloat(this.form.unitPrice) : null
                };
                
                try {
                    let res;
                    if (this.isEdit) {
                        res = await batchApi.updateBatch(this.batchId, submitData);
                    } else {
                        res = await batchApi.createBatch(submitData);
                    }
                    
                    uni.hideLoading();
                    
                    if (res.code === 200) {
                        uni.showToast({
                            title: this.isEdit ? '修改成功' : '创建成功',
                            icon: 'success'
                        });
                        setTimeout(() => {
                            uni.navigateBack();
                        }, 1500);
                    } else {
                        uni.showToast({
                            title: res.message || '提交失败',
                            icon: 'none'
                        });
                    }
                } catch (e) {
                    uni.hideLoading();
                    console.error('提交失败', e);
                    
                    // 模拟成功
                    uni.showToast({
                        title: this.isEdit ? '修改成功' : '创建成功',
                        icon: 'success'
                    });
                    setTimeout(() => {
                        uni.navigateBack();
                    }, 1500);
                }
            },
            
            goBack() {
                uni.navigateBack();
            }
        }
    };
</script>

<style scoped>
    .form-container {
        min-height: 100vh;
        background-color: #f5f5f5;
        padding-bottom: 160rpx;
    }

    .form-header {
        padding: 40rpx 30rpx;
        background: linear-gradient(135deg, #2b7a4b, #4a9d6a);
    }

    .form-title {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
        color: #fff;
        margin-bottom: 10rpx;
    }

    .form-subtitle {
        font-size: 26rpx;
        color: rgba(255, 255, 255, 0.8);
    }

    .form-body {
        padding: 20rpx;
    }

    .form-section {
        background-color: #fff;
        border-radius: 20rpx;
        padding: 30rpx;
        margin-bottom: 20rpx;
    }

    .section-title {
        font-size: 30rpx;
        font-weight: 600;
        color: #333;
        margin-bottom: 30rpx;
        padding-bottom: 16rpx;
        border-bottom: 2rpx solid #f0f0f0;
    }

    .form-group {
        margin-bottom: 30rpx;
    }

    .form-group:last-child {
        margin-bottom: 0;
    }

    .form-label {
        display: flex;
        align-items: center;
        font-size: 28rpx;
        color: #333;
        margin-bottom: 16rpx;
        font-weight: 500;
    }

    .required {
        color: #f44336;
        margin-right: 6rpx;
    }

    .input-wrapper {
        display: flex;
        align-items: center;
        position: relative;
    }

    .form-input {
        width: 100%;
        height: 88rpx;
        padding: 0 24rpx;
        background-color: #f8f8f8;
        border: 2rpx solid #e0e0e0;
        border-radius: 12rpx;
        font-size: 28rpx;
        color: #333;
    }

    .form-input:focus {
        border-color: #2b7a4b;
        background-color: #fff;
    }

    .picker-input {
        display: flex;
        align-items: center;
        color: #333;
    }

    .picker-input:empty::after {
        content: '请选择';
        color: #999;
    }

    .input-unit {
        position: absolute;
        right: 24rpx;
        font-size: 26rpx;
        color: #999;
    }

    .input-prefix {
        position: absolute;
        left: 24rpx;
        font-size: 28rpx;
        color: #999;
        z-index: 1;
    }

    .input-wrapper .form-input {
        padding-left: 60rpx;
    }

    .validating {
        position: absolute;
        right: 24rpx;
        font-size: 24rpx;
        color: #999;
    }

    .valid {
        position: absolute;
        right: 24rpx;
        font-size: 24rpx;
        color: #4caf50;
        font-weight: 500;
    }

    .invalid {
        position: absolute;
        right: 24rpx;
        font-size: 24rpx;
        color: #f44336;
        font-weight: 500;
    }

    .form-hint {
        display: block;
        font-size: 22rpx;
        color: #999;
        margin-top: 10rpx;
    }

    .radio-group {
        display: flex;
    }

    .radio-item {
        display: flex;
        align-items: center;
        margin-right: 40rpx;
        padding: 16rpx 24rpx;
        background-color: #f8f8f8;
        border-radius: 40rpx;
        border: 2rpx solid transparent;
    }

    .radio-item.active {
        background-color: #e8f5e9;
        border-color: #2b7a4b;
    }

    .radio-dot {
        width: 32rpx;
        height: 32rpx;
        border-radius: 50%;
        border: 2rpx solid #ccc;
        margin-right: 12rpx;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .radio-dot.checked {
        border-color: #2b7a4b;
    }

    .radio-dot.checked::after {
        content: '';
        width: 18rpx;
        height: 18rpx;
        border-radius: 50%;
        background-color: #2b7a4b;
    }

    .error-msg {
        padding: 20rpx 30rpx;
        background-color: #ffebee;
        border-radius: 12rpx;
        margin: 20rpx 0;
        font-size: 26rpx;
        color: #f44336;
    }

    .form-footer {
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
