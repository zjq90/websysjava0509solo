<template>
    <view class="admission-apply" :class="{ 'elder-mode': elderMode }">
        <view class="form-container">
            <view class="form-section">
                <view class="section-title">选择科室</view>
                <view class="dept-list">
                    <view 
                        class="dept-item"
                        :class="{ active: form.deptId === item.id }"
                        v-for="item in departments"
                        :key="item.id"
                        @click="selectDept(item)"
                    >
                        <text class="dept-icon">{{ item.icon || '🏥' }}</text>
                        <text class="dept-name">{{ item.name }}</text>
                        <text class="dept-desc" v-if="item.description">{{ item.description }}</text>
                    </view>
                </view>
            </view>

            <view class="form-section">
                <view class="section-title">入院信息</view>
                <view class="form-item">
                    <text class="form-label">预约入院日期 <text class="required">*</text></text>
                    <picker mode="date" :value="form.admissionDate" :start="minDate" @change="onDateChange">
                        <view class="form-input form-picker">
                            <text :class="{ placeholder: !form.admissionDate }">
                                {{ form.admissionDate || '请选择入院日期' }}
                            </text>
                            <text class="picker-arrow">›</text>
                        </view>
                    </picker>
                </view>
            </view>

            <view class="form-section">
                <view class="section-title">入院原因</view>
                <textarea 
                    class="form-textarea"
                    v-model="form.reason"
                    placeholder="请简要描述入院原因，如：高血压、糖尿病等需要住院治疗..."
                    :maxlength="500"
                />
                <text class="textarea-tip">已输入 {{ form.reason.length }}/500 字</text>
            </view>

            <view class="form-section">
                <view class="section-title">备注</view>
                <textarea 
                    class="form-textarea"
                    v-model="form.remark"
                    placeholder="如有特殊需求或过敏史请在此说明（选填）"
                    :maxlength="200"
                />
            </view>
        </view>

        <view class="bottom-bar">
            <view class="agreement">
                <checkbox :checked="agreed" @change="agreed = !agreed" color="#1890FF" />
                <text class="agreement-text">我已阅读并同意</text>
                <text class="agreement-link" @click="showAgreement">《住院须知》</text>
            </view>
            <button 
                class="btn btn-primary btn-block btn-large"
                :disabled="!canSubmit"
                :class="{ disabled: !canSubmit }"
                @click="submitForm"
            >
                提交申请
            </button>
        </view>
    </view>
</template>

<script>
import api from '@/common/js/api.js'
import util from '@/common/js/util.js'

export default {
    data() {
        return {
            elderMode: false,
            departments: [],
            agreed: false,
            form: {
                deptId: '',
                deptName: '',
                admissionDate: '',
                reason: '',
                remark: ''
            }
        }
    },
    computed: {
        canSubmit() {
            return this.form.deptId && this.form.admissionDate && this.form.reason && this.agreed
        },
        minDate() {
            const today = new Date()
            return this.formatDate(today)
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        this.loadDepartments()
    },
    methods: {
        async loadDepartments() {
            try {
                const res = await api.department.list()
                if (res.code === 200) {
                    this.departments = res.data || []
                }
            } catch (e) {
                console.error('加载科室失败:', e)
            }
        },

        selectDept(item) {
            this.form.deptId = item.id
            this.form.deptName = item.name
        },

        onDateChange(e) {
            this.form.admissionDate = e.detail.value
        },

        formatDate(date) {
            const y = date.getFullYear()
            const m = String(date.getMonth() + 1).padStart(2, '0')
            const d = String(date.getDate()).padStart(2, '0')
            return `${y}-${m}-${d}`
        },

        showAgreement() {
            uni.showModal({
                title: '住院须知',
                content: '1. 入院申请提交后，医院将在1-2个工作日内审核。\n2. 审核通过后，请按时到院办理入院手续。\n3. 请携带身份证、医保卡等有效证件。\n4. 如需取消申请，请在审核通过前操作。\n5. 如有疑问，请拨打咨询电话：400-123-4567',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        async submitForm() {
            if (!this.form.deptId) {
                util.showToast('请选择科室', 'none')
                return
            }
            if (!this.form.admissionDate) {
                util.showToast('请选择入院日期', 'none')
                return
            }
            if (!this.form.reason) {
                util.showToast('请填写入院原因', 'none')
                return
            }
            if (!this.agreed) {
                util.showToast('请先阅读并同意住院须知', 'none')
                return
            }

            try {
                util.showLoading('提交中...')
                const res = await api.admission.create({
                    deptId: this.form.deptId,
                    deptName: this.form.deptName,
                    admissionDate: this.form.admissionDate,
                    reason: this.form.reason,
                    remark: this.form.remark
                })
                util.hideLoading()
                
                if (res.code === 200) {
                    util.showToast('提交成功', 'success')
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                } else {
                    util.showToast(res.message || '提交失败', 'none')
                }
            } catch (e) {
                util.hideLoading()
            }
        }
    }
}
</script>

<style scoped>
.admission-apply {
    min-height: 100vh;
    background: #F5F5F5;
    padding-bottom: 260rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.form-container {
    padding: 20rpx;
}

.form-section {
    background: #fff;
    border-radius: 20rpx;
    padding: 28rpx;
    margin-bottom: 20rpx;
}

.section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;
}

.elder-mode .section-title {
    font-size: 36rpx;
}

.dept-list {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
}

.dept-item {
    width: calc(50% - 8rpx);
    padding: 28rpx;
    background: #FAFAFA;
    border-radius: 16rpx;
    border: 2rpx solid transparent;
    box-sizing: border-box;
    transition: all 0.3s;
}

.dept-item.active {
    background: #E6F7FF;
    border-color: #1890FF;
}

.dept-icon {
    font-size: 48rpx;
    display: block;
    margin-bottom: 12rpx;
}

.dept-name {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    display: block;
}

.elder-mode .dept-name {
    font-size: 34rpx;
}

.dept-desc {
    font-size: 24rpx;
    color: #999;
    margin-top: 4rpx;
    display: block;
}

.elder-mode .dept-desc {
    font-size: 28rpx;
}

.form-item {
    margin-bottom: 0;
}

.form-label {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 12rpx;
    display: block;
}

.elder-mode .form-label {
    font-size: 32rpx;
}

.required {
    color: #FF4D4F;
}

.form-picker {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.picker-arrow {
    color: #ccc;
    font-size: 40rpx;
}

.placeholder {
    color: #999;
}

.form-textarea {
    width: 100%;
    height: 200rpx;
    padding: 20rpx 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333;
    box-sizing: border-box;
}

.elder-mode .form-textarea {
    height: 240rpx;
    font-size: 32rpx;
}

.textarea-tip {
    font-size: 24rpx;
    color: #999;
    text-align: right;
    margin-top: 8rpx;
    display: block;
}

.elder-mode .textarea-tip {
    font-size: 28rpx;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 20rpx;
    background: #fff;
    box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.08);
    padding-bottom: calc(20rpx + constant(safe-area-inset-bottom));
    padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}

.elder-mode .bottom-bar {
    padding: 28rpx;
}

.agreement {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    flex-wrap: wrap;
}

.agreement-text {
    font-size: 26rpx;
    color: #666;
    margin-left: 8rpx;
}

.elder-mode .agreement-text {
    font-size: 30rpx;
}

.agreement-link {
    font-size: 26rpx;
    color: #1890FF;
}

.elder-mode .agreement-link {
    font-size: 30rpx;
}

.btn.disabled {
    opacity: 0.5;
}
</style>
