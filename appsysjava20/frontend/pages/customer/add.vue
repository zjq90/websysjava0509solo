<template>
    <view class="customer-edit-page">
        <view class="form-section card">
            <view class="section-title">📋 基本信息</view>
            
            <view class="form-item">
                <text class="form-label required">客户姓名</text>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="form.name"
                    placeholder="请输入客户姓名"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label">联系电话</text>
                <view class="input-wrapper">
                    <input 
                        class="form-input" 
                        type="number" 
                        v-model="form.phone"
                        placeholder="请输入11位手机号"
                        maxlength="11"
                    />
                </view>
                <text class="form-tip" v-if="form.phone && !isValidPhone">
                    ⚠️ 请输入正确的手机号格式（1开头的11位数字）
                </text>
            </view>
            
            <view class="form-item">
                <text class="form-label">电子邮箱</text>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="form.email"
                    placeholder="请输入电子邮箱"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label">联系地址</text>
                <textarea 
                    class="form-textarea" 
                    v-model="form.address"
                    placeholder="请输入联系地址"
                    :auto-height="true"
                />
            </view>
            
            <view class="form-item">
                <text class="form-label">备注</text>
                <textarea 
                    class="form-textarea" 
                    v-model="form.remark"
                    placeholder="请输入备注信息"
                    :auto-height="true"
                />
            </view>
        </view>
        
        <view class="tips-card card">
            <view class="section-title">💡 安全提示</view>
            <view class="tips-content">
                <text class="tip-item">• 客户电话和邮箱会自动AES-256加密存储</text>
                <text class="tip-item">• 敏感信息在数据库中以加密形式保存</text>
                <text class="tip-item">• 手机号格式：1开头的11位数字</text>
            </view>
        </view>
        
        <view class="btn-wrapper">
            <button class="btn-primary" @click="handleSubmit" :disabled="submitting">
                {{ submitting ? '提交中...' : (isEdit ? '保存修改' : '创建客户') }}
            </button>
        </view>
    </view>
</template>

<script>
import { customerApi } from '../../api/index';

export default {
    data() {
        return {
            isEdit: false,
            customerId: null,
            submitting: false,
            form: {
                name: '',
                phone: '',
                email: '',
                address: '',
                remark: ''
            }
        };
    },
    computed: {
        isValidPhone() {
            if (!this.form.phone) return true;
            return /^1\d{10}$/.test(this.form.phone);
        }
    },
    onLoad(options) {
        if (options.mode === 'edit' && options.id) {
            this.isEdit = true;
            this.customerId = options.id;
            uni.setNavigationBarTitle({ title: '编辑客户' });
            this.loadDetail();
        }
    },
    methods: {
        async loadDetail() {
            try {
                const res = await customerApi.getDetail(this.customerId);
                if (res.success && res.data) {
                    this.form = {
                        name: res.data.name || '',
                        phone: res.data.phone || '',
                        email: res.data.email || '',
                        address: res.data.address || '',
                        remark: res.data.remark || ''
                    };
                }
            } catch (e) {
                console.error('加载详情失败', e);
            }
        },
        
        async handleSubmit() {
            if (!this.form.name.trim()) {
                uni.showToast({ title: '请输入客户姓名', icon: 'none' });
                return;
            }
            
            if (this.form.phone && !this.isValidPhone) {
                uni.showToast({ title: '请输入正确的手机号', icon: 'none' });
                return;
            }
            
            this.submitting = true;
            
            try {
                if (this.isEdit) {
                    await customerApi.update(this.customerId, this.form);
                } else {
                    await customerApi.create(this.form);
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
.customer-edit-page {
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
    margin-bottom: 32rpx;
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

.input-wrapper {
    position: relative;
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

.form-tip {
    display: block;
    font-size: 24rpx;
    color: #f56c6c;
    margin-top: 12rpx;
}

.tips-content {
    background: #f5f7fa;
    border-radius: 12rpx;
    padding: 24rpx;
}

.tip-item {
    display: block;
    font-size: 24rpx;
    color: #909399;
    line-height: 1.8;
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
</style>
