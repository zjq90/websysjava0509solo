<template>
    <view class="product-edit-page">
        <view class="form-section card">
            <view class="section-title">📦 产品信息</view>
            
            <view class="form-item">
                <text class="form-label required">产品名称</text>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="form.name"
                    placeholder="请输入产品名称"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label required">批次编号</text>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="form.batchNumber"
                    placeholder="8位数字+字母组合"
                    maxlength="8"
                />
                <text class="form-tip" :class="{ error: !rules.batch }">
                    {{ rules.batch ? '✓ 格式正确（8位）' : '⚠️ 请输入8位数字+字母组合' }}
                </text>
            </view>
            
            <view class="form-item">
                <text class="form-label required">保质期</text>
                <picker mode="date" :value="form.expiryDate" @change="onDateChange">
                    <view class="picker-field">
                        <text class="picker-text" :class="{ placeholder: !form.expiryDate }">
                            {{ form.expiryDate || '请选择保质期日期' }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
                <text class="form-tip" :class="{ error: !rules.expiry }">
                    {{ rules.expiry ? '✓ 符合要求（≥当前+6个月）' : '⚠️ 保质期不得早于当前日期+6个月' }}
                </text>
                <text class="rule-hint">提示：最小保质期为 {{ minExpiryDateStr }}</text>
            </view>
            
            <view class="form-item">
                <text class="form-label required">发芽率 (%)</text>
                <input 
                    class="form-input" 
                    type="digit" 
                    v-model="form.germinationRate"
                    placeholder="0-100，保留1位小数"
                />
                <text class="form-tip" :class="{ error: !rules.germination }">
                    {{ rules.germination ? '✓ 范围正确' : '⚠️ 发芽率必须在0-100%之间' }}
                </text>
            </view>
            
            <view class="form-item">
                <text class="form-label required">销售单价 (元)</text>
                <input 
                    class="form-input" 
                    type="digit" 
                    v-model="form.unitPrice"
                    placeholder="请输入销售单价"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label required">成本单价 (元)</text>
                <input 
                    class="form-input" 
                    type="digit" 
                    v-model="form.costPrice"
                    placeholder="请输入成本单价"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label">库存数量</text>
                <input 
                    class="form-input" 
                    type="number" 
                    v-model="form.stockQuantity"
                    placeholder="请输入库存数量"
                />
            </view>
        </view>
        
        <view class="btn-wrapper">
            <button class="btn-primary" @click="handleSubmit" :disabled="submitting || !allRulesPass">
                {{ submitting ? '提交中...' : (isEdit ? '保存修改' : '创建产品') }}
            </button>
        </view>
    </view>
</template>

<script>
import { productApi } from '../../api/index';

export default {
    data() {
        return {
            isEdit: false,
            productId: null,
            submitting: false,
            form: {
                name: '',
                batchNumber: '',
                expiryDate: '',
                germinationRate: '',
                unitPrice: '',
                costPrice: '',
                stockQuantity: ''
            }
        };
    },
    computed: {
        minExpiryDate() {
            const date = new Date();
            date.setMonth(date.getMonth() + 6);
            return date;
        },
        minExpiryDateStr() {
            const d = this.minExpiryDate;
            return d.getFullYear() + '-' + 
                   String(d.getMonth() + 1).padStart(2, '0') + '-' + 
                   String(d.getDate()).padStart(2, '0');
        },
        rules() {
            const batch = /^[A-Za-z0-9]{8}$/.test(this.form.batchNumber);
            
            let expiry = true;
            if (this.form.expiryDate) {
                expiry = new Date(this.form.expiryDate) >= this.minExpiryDate;
            }
            
            let germination = true;
            if (this.form.germinationRate !== '' && this.form.germinationRate !== null) {
                const rate = Number(this.form.germinationRate);
                germination = rate >= 0 && rate <= 100;
            }
            
            return { batch, expiry, germination };
        },
        allRulesPass() {
            return this.rules.batch && this.rules.expiry && this.rules.germination;
        }
    },
    onLoad(options) {
        if (options.mode === 'edit' && options.id) {
            this.isEdit = true;
            this.productId = options.id;
            uni.setNavigationBarTitle({ title: '编辑产品' });
            this.loadDetail();
        }
    },
    methods: {
        async loadDetail() {
            try {
                const res = await productApi.getDetail(this.productId);
                if (res.success && res.data) {
                    this.form = {
                        name: res.data.name || '',
                        batchNumber: res.data.batchNumber || '',
                        expiryDate: res.data.expiryDate || '',
                        germinationRate: res.data.germinationRate || '',
                        unitPrice: res.data.unitPrice || '',
                        costPrice: res.data.costPrice || '',
                        stockQuantity: res.data.stockQuantity || ''
                    };
                }
            } catch (e) {
                console.error('加载详情失败', e);
            }
        },
        
        onDateChange(e) {
            this.form.expiryDate = e.detail.value;
        },
        
        async handleSubmit() {
            if (!this.form.name.trim()) {
                uni.showToast({ title: '请输入产品名称', icon: 'none' });
                return;
            }
            
            if (!this.form.batchNumber) {
                uni.showToast({ title: '请输入批次编号', icon: 'none' });
                return;
            }
            
            if (!this.form.expiryDate) {
                uni.showToast({ title: '请选择保质期', icon: 'none' });
                return;
            }
            
            if (this.form.germinationRate === '' || this.form.germinationRate === null) {
                uni.showToast({ title: '请输入发芽率', icon: 'none' });
                return;
            }
            
            if (!this.form.unitPrice) {
                uni.showToast({ title: '请输入销售单价', icon: 'none' });
                return;
            }
            
            if (!this.form.costPrice) {
                uni.showToast({ title: '请输入成本单价', icon: 'none' });
                return;
            }
            
            if (!this.allRulesPass) {
                uni.showToast({ title: '请检查业务规则', icon: 'none' });
                return;
            }
            
            this.submitting = true;
            
            const submitData = {
                ...this.form,
                germinationRate: Number(this.form.germinationRate),
                unitPrice: Number(this.form.unitPrice),
                costPrice: Number(this.form.costPrice),
                stockQuantity: Number(this.form.stockQuantity) || 0
            };
            
            try {
                if (this.isEdit) {
                    await productApi.update(this.productId, submitData);
                } else {
                    await productApi.create(submitData);
                }
                
                uni.showToast({ 
                    title: this.isEdit ? '修改成功' : '创建成功', 
                    icon: 'success' 
                });
                
                setTimeout(() => {
                    uni.navigateBack();
                }, 1000);
            } catch (e) {
                console.error('提交失败', e);
            } finally {
                this.submitting = false;
            }
        }
    }
};
</script>

<style scoped>
.product-edit-page {
    padding: 30rpx;
    padding-bottom: 200rpx;
}

.section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 32rpx;
}

.form-item {
    margin-bottom: 36rpx;
}

.form-label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 16rpx;
    display: block;
}

.form-label.required::before {
    content: '*';
    color: #f56c6c;
    margin-right: 8rpx;
}

.form-input {
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 28rpx;
    font-size: 30rpx;
    border: 2rpx solid #e8e8e8;
    width: 100%;
    box-sizing: border-box;
}

.form-tip {
    display: block;
    font-size: 24rpx;
    color: #67c23a;
    margin-top: 12rpx;
}

.form-tip.error {
    color: #f56c6c;
}

.rule-hint {
    display: block;
    font-size: 22rpx;
    color: #999;
    margin-top: 8rpx;
}

.picker-field {
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 28rpx;
    border: 2rpx solid #e8e8e8;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.picker-text {
    font-size: 30rpx;
    color: #333;
}

.picker-text.placeholder {
    color: #999;
}

.picker-arrow {
    font-size: 24rpx;
    color: #ccc;
}

.btn-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 24rpx 30rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.btn-primary[disabled] {
    background: #ccc;
}
</style>
