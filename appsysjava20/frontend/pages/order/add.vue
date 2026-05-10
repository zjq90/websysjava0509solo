<template>
    <view class="order-edit-page">
        <view class="form-section card">
            <view class="section-title">🧾 创建销售订单</view>
            
            <view class="form-item">
                <text class="form-label required">选择客户</text>
                <picker 
                    mode="selector" 
                    :range="customerOptions" 
                    :range-key="'label'"
                    @change="onCustomerChange"
                >
                    <view class="picker-field">
                        <text class="picker-text" :class="{ placeholder: !form.customerId }">
                            {{ selectedCustomerLabel || '请选择客户' }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
            </view>
            
            <view class="form-item">
                <text class="form-label required">选择产品</text>
                <picker 
                    mode="selector" 
                    :range="productOptions" 
                    :range-key="'label'"
                    @change="onProductChange"
                >
                    <view class="picker-field">
                        <text class="picker-text" :class="{ placeholder: !form.productId }">
                            {{ selectedProductLabel || '请选择产品' }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
                <text class="product-price" v-if="selectedProduct">
                    单价: ¥{{ selectedProduct.unitPrice }} | 库存: {{ selectedProduct.stockQuantity }}
                </text>
            </view>
            
            <view class="form-item">
                <text class="form-label required">选择销售员</text>
                <picker 
                    mode="selector" 
                    :range="employeeOptions" 
                    :range-key="'label'"
                    @change="onEmployeeChange"
                >
                    <view class="picker-field">
                        <text class="picker-text" :class="{ placeholder: !form.employeeId }">
                            {{ selectedEmployeeLabel || '请选择销售员' }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
            </view>
            
            <view class="form-item">
                <text class="form-label required">销售数量</text>
                <input 
                    class="form-input" 
                    type="number" 
                    v-model="form.quantity"
                    placeholder="请输入销售数量"
                />
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
                <text class="form-label">已回款金额 (元)</text>
                <input 
                    class="form-input" 
                    type="digit" 
                    v-model="form.paidAmount"
                    placeholder="已回款金额（可选）"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label">订单状态</text>
                <picker 
                    mode="selector" 
                    :range="statusOptions" 
                    @change="onStatusChange"
                >
                    <view class="picker-field">
                        <text class="picker-text">
                            {{ getStatusText(form.status) }}
                        </text>
                        <text class="picker-arrow">▼</text>
                    </view>
                </picker>
            </view>
            
            <view class="form-item">
                <text class="form-label">备注</text>
                <textarea 
                    class="form-textarea" 
                    v-model="form.remark"
                    placeholder="订单备注信息"
                    :auto-height="true"
                />
            </view>
        </view>
        
        <view class="summary-card card" v-if="form.quantity && form.unitPrice">
            <view class="section-title">💵 订单金额</view>
            <view class="summary-row">
                <text class="summary-label">订单总额</text>
                <text class="summary-value">¥{{ formatNumber(calculatedTotal) }}</text>
            </view>
            <view class="summary-row" v-if="form.paidAmount">
                <text class="summary-label">已回款</text>
                <text class="summary-value success">¥{{ formatNumber(form.paidAmount) }}</text>
            </view>
            <view class="summary-row" v-if="form.paidAmount">
                <text class="summary-label">待回款</text>
                <text class="summary-value warning">¥{{ formatNumber(calculatedTotal - Number(form.paidAmount)) }}</text>
            </view>
        </view>
        
        <view class="btn-wrapper">
            <button class="btn-primary" @click="handleSubmit" :disabled="submitting">
                {{ submitting ? '提交中...' : '创建订单' }}
            </button>
        </view>
    </view>
</template>

<script>
import { customerApi, productApi, employeeApi, financeApi } from '../../api/index';

export default {
    data() {
        return {
            submitting: false,
            customers: [],
            products: [],
            employees: [],
            statusOptions: ['PENDING', 'PARTIAL', 'COMPLETED'],
            form: {
                customerId: null,
                productId: null,
                employeeId: null,
                quantity: '',
                unitPrice: '',
                paidAmount: '',
                status: 'PENDING',
                remark: ''
            }
        };
    },
    computed: {
        customerOptions() {
            return this.customers.map(item => ({
                label: item.name + (item.phone ? ' (' + item.phone + ')' : ''),
                value: item.id
            }));
        },
        productOptions() {
            return this.products.map(item => ({
                label: item.name + ' (批次:' + item.batchNumber + ')',
                value: item.id
            }));
        },
        employeeOptions() {
            return this.employees.map(item => ({
                label: item.name + ' (' + item.position + ')',
                value: item.id
            }));
        },
        selectedCustomerLabel() {
            if (!this.form.customerId) return '';
            const option = this.customerOptions.find(item => item.value === this.form.customerId);
            return option ? option.label : '';
        },
        selectedProductLabel() {
            if (!this.form.productId) return '';
            const option = this.productOptions.find(item => item.value === this.form.productId);
            return option ? option.label : '';
        },
        selectedEmployeeLabel() {
            if (!this.form.employeeId) return '';
            const option = this.employeeOptions.find(item => item.value === this.form.employeeId);
            return option ? option.label : '';
        },
        selectedProduct() {
            if (!this.form.productId) return null;
            return this.products.find(item => item.id === this.form.productId);
        },
        calculatedTotal() {
            const qty = Number(this.form.quantity) || 0;
            const price = Number(this.form.unitPrice) || 0;
            return qty * price;
        }
    },
    onShow() {
        this.loadData();
        const user = uni.getStorageSync('currentUser');
        if (user) {
            this.form.employeeId = user.id;
        }
    },
    methods: {
        async loadData() {
            try {
                const [customerRes, productRes, employeeRes] = await Promise.all([
                    customerApi.getList(),
                    productApi.getList(),
                    employeeApi.getList()
                ]);
                
                if (customerRes.success) this.customers = customerRes.data;
                if (productRes.success) this.products = productRes.data;
                if (employeeRes.success) this.employees = employeeRes.data;
            } catch (e) {
                console.error('加载数据失败', e);
            }
        },
        
        onCustomerChange(e) {
            this.form.customerId = this.customerOptions[e.detail.value].value;
        },
        
        onProductChange(e) {
            this.form.productId = this.productOptions[e.detail.value].value;
            const product = this.selectedProduct;
            if (product && !this.form.unitPrice) {
                this.form.unitPrice = product.unitPrice;
            }
        },
        
        onEmployeeChange(e) {
            this.form.employeeId = this.employeeOptions[e.detail.value].value;
        },
        
        onStatusChange(e) {
            this.form.status = this.statusOptions[e.detail.value];
        },
        
        getStatusText(status) {
            const map = {
                'COMPLETED': '已完成',
                'PARTIAL': '部分回款',
                'PENDING': '待回款'
            };
            return map[status] || status;
        },
        
        formatNumber(num) {
            if (!num) return '0.00';
            const n = Number(num);
            return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        },
        
        async handleSubmit() {
            if (!this.form.customerId) {
                uni.showToast({ title: '请选择客户', icon: 'none' });
                return;
            }
            
            if (!this.form.productId) {
                uni.showToast({ title: '请选择产品', icon: 'none' });
                return;
            }
            
            if (!this.form.employeeId) {
                uni.showToast({ title: '请选择销售员', icon: 'none' });
                return;
            }
            
            if (!this.form.quantity || Number(this.form.quantity) <= 0) {
                uni.showToast({ title: '请输入有效数量', icon: 'none' });
                return;
            }
            
            if (!this.form.unitPrice || Number(this.form.unitPrice) <= 0) {
                uni.showToast({ title: '请输入有效单价', icon: 'none' });
                return;
            }
            
            this.submitting = true;
            
            const submitData = {
                customer: { id: this.form.customerId },
                product: { id: this.form.productId },
                employee: { id: this.form.employeeId },
                quantity: Number(this.form.quantity),
                unitPrice: Number(this.form.unitPrice),
                paidAmount: Number(this.form.paidAmount) || 0,
                status: this.form.status,
                remark: this.form.remark
            };
            
            try {
                await financeApi.createOrder(submitData);
                uni.showToast({ title: '创建成功', icon: 'success' });
                
                setTimeout(() => {
                    uni.navigateBack();
                }, 1000);
            } catch (e) {
                console.error('创建失败', e);
            } finally {
                this.submitting = false;
            }
        }
    }
};
</script>

<style scoped>
.order-edit-page {
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

.form-textarea {
    background: #f8f8f8;
    border-radius: 16rpx;
    padding: 28rpx;
    font-size: 30rpx;
    border: 2rpx solid #e8e8e8;
    width: 100%;
    box-sizing: border-box;
    min-height: 120rpx;
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

.product-price {
    display: block;
    font-size: 24rpx;
    color: #67c23a;
    margin-top: 12rpx;
}

.summary-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;
}

.summary-label {
    font-size: 28rpx;
    color: #666;
}

.summary-value {
    font-size: 32rpx;
    font-weight: bold;
    color: #3c9cff;
}

.summary-value.success { color: #67c23a; }
.summary-value.warning { color: #e6a23c; }

.btn-wrapper {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 24rpx 30rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}
</style>
