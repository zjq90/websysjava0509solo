<template>
    <view class="patient-edit-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="form-section card">
            <view class="form-item">
                <text class="form-label">姓名</text>
                <input 
                    class="form-input" 
                    placeholder="请输入真实姓名"
                    v-model="form.name"
                />
            </view>
            <view class="form-item">
                <text class="form-label">身份证号</text>
                <input 
                    class="form-input" 
                    placeholder="请输入身份证号"
                    v-model="form.idCard"
                    maxlength="18"
                />
            </view>
            <view class="form-item">
                <text class="form-label">手机号</text>
                <input 
                    class="form-input" 
                    placeholder="请输入手机号"
                    v-model="form.phone"
                    maxlength="11"
                    type="number"
                />
            </view>
            <view class="form-item">
                <text class="form-label">关系</text>
                <picker 
                    :value="relationIndex" 
                    :range="relationOptions" 
                    @change="onRelationChange"
                >
                    <view class="picker-value">
                        {{ relationOptions[relationIndex] || '请选择' }}
                        <text class="picker-arrow">></text>
                    </view>
                </picker>
            </view>
            <view class="form-item">
                <text class="form-label">性别</text>
                <view class="gender-radio">
                    <view 
                        class="radio-item" 
                        :class="{ active: form.gender === 'MALE' }"
                        @click="form.gender = 'MALE'"
                    >
                        <text class="radio-circle"></text>
                        <text>男</text>
                    </view>
                    <view 
                        class="radio-item" 
                        :class="{ active: form.gender === 'FEMALE' }"
                        @click="form.gender = 'FEMALE'"
                    >
                        <text class="radio-circle"></text>
                        <text>女</text>
                    </view>
                </view>
            </view>
            <view class="form-item">
                <text class="form-label">出生日期</text>
                <picker 
                    mode="date" 
                    :value="form.birthDate" 
                    @change="onDateChange"
                >
                    <view class="picker-value">
                        {{ form.birthDate || '请选择' }}
                        <text class="picker-arrow">></text>
                    </view>
                </picker>
            </view>
        </view>

        <view class="verify-section card">
            <view class="verify-header">
                <text class="verify-title">实名认证</text>
                <text 
                    class="verify-status" 
                    :class="form.realNameVerified ? 'verified' : 'unverified'"
                >
                    {{ form.realNameVerified ? '已认证' : '未认证' }}
                </text>
            </view>
            <text class="verify-tip">实名认证后可预约挂号，未认证请点击下方按钮完成认证</text>
            <view class="verify-btn" @click="doVerify" v-if="!form.realNameVerified">
                <text>开始实名认证</text>
            </view>
        </view>

        <view class="default-section card">
            <view class="default-row">
                <text class="default-label">设为默认就诊人</text>
                <switch 
                    :checked="form.isDefault" 
                    @change="onDefaultChange"
                    color="#1677ff"
                />
            </view>
        </view>

        <view class="bottom-bar">
            <view class="save-btn" @click="savePatient">
                保存
            </view>
            <view class="delete-btn" v-if="patientId" @click="deletePatient">
                删除
            </view>
        </view>
    </view>
</template>

<script>
import { addPatient, updatePatient, deletePatient, getPatientList } from '@/api/patient'

export default {
    data() {
        return {
            patientId: null,
            relationOptions: ['本人', '配偶', '子女', '父母', '其他'],
            relationIndex: 0,
            form: {
                name: '',
                idCard: '',
                phone: '',
                relation: '本人',
                gender: 'MALE',
                birthDate: '',
                isDefault: false,
                realNameVerified: false
            },
            elderlyMode: false
        }
    },
    onLoad(options) {
        this.patientId = options.id
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        if (this.patientId) {
            uni.setNavigationBarTitle({ title: '编辑就诊人' })
            this.loadPatient()
        } else {
            uni.setNavigationBarTitle({ title: '添加就诊人' })
        }
    },
    methods: {
        async loadPatient() {
            try {
                const res = await getPatientList()
                const list = res.data.data || []
                const patient = list.find(p => p.id == this.patientId)
                if (patient) {
                    this.form = { ...patient }
                    const idx = this.relationOptions.indexOf(patient.relation)
                    this.relationIndex = idx > -1 ? idx : 0
                }
            } catch (e) {
                console.error(e)
            }
        },
        onRelationChange(e) {
            this.relationIndex = e.detail.value
            this.form.relation = this.relationOptions[e.detail.value]
        },
        onDateChange(e) {
            this.form.birthDate = e.detail.value
        },
        onDefaultChange(e) {
            this.form.isDefault = e.detail.value
        },
        doVerify() {
            uni.showModal({
                title: '实名认证',
                content: '将跳转至国家政务服务平台进行身份证和人脸识别认证',
                confirmText: '去认证',
                success: (res) => {
                    if (res.confirm) {
                        uni.showLoading({ title: '认证中...' })
                        setTimeout(() => {
                            uni.hideLoading()
                            uni.showModal({
                                title: '模拟认证',
                                content: '这是模拟环境，是否标记为已认证？',
                                success: (res2) => {
                                    if (res2.confirm) {
                                        this.form.realNameVerified = true
                                        uni.showToast({ title: '认证成功', icon: 'success' })
                                    }
                                }
                            })
                        }, 1500)
                    }
                }
            })
        },
        validateForm() {
            if (!this.form.name.trim()) {
                uni.showToast({ title: '请输入姓名', icon: 'none' })
                return false
            }
            if (!this.form.idCard.trim() || !/^\d{17}[\dXx]$/.test(this.form.idCard)) {
                uni.showToast({ title: '请输入有效的身份证号', icon: 'none' })
                return false
            }
            if (!this.form.phone.trim() || !/^1[3-9]\d{9}$/.test(this.form.phone)) {
                uni.showToast({ title: '请输入有效的手机号', icon: 'none' })
                return false
            }
            return true
        },
        async savePatient() {
            if (!this.validateForm()) return
            
            try {
                if (this.patientId) {
                    await updatePatient(this.patientId, this.form)
                    uni.showToast({ title: '更新成功', icon: 'success' })
                } else {
                    await addPatient(this.form)
                    uni.showToast({ title: '添加成功', icon: 'success' })
                }
                setTimeout(() => {
                    uni.navigateBack()
                }, 1500)
            } catch (e) {
                console.error(e)
            }
        },
        async deletePatient() {
            uni.showModal({
                title: '删除确认',
                content: '确定要删除该就诊人吗？',
                success: async (res) => {
                    if (res.confirm) {
                        try {
                            await deletePatient(this.patientId)
                            uni.showToast({ title: '删除成功', icon: 'success' })
                            setTimeout(() => {
                                uni.navigateBack()
                            }, 1500)
                        } catch (e) {
                            console.error(e)
                        }
                    }
                }
            })
        }
    }
}
</script>

<style scoped>
.patient-edit-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 200rpx;
}

.form-section,
.verify-section,
.default-section {
    margin: 20rpx 30rpx;
    padding: 20rpx;
}

.form-item {
    display: flex;
    align-items: center;
    padding: 25rpx 10rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.form-item:last-child {
    border-bottom: none;
}

.form-label {
    width: 160rpx;
    font-size: 28rpx;
    color: #666666;
    flex-shrink: 0;
}

.form-input {
    flex: 1;
    font-size: 28rpx;
}

.picker-value {
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 28rpx;
}

.picker-arrow {
    color: #999999;
}

.gender-radio {
    display: flex;
    gap: 40rpx;
    flex: 1;
}

.radio-item {
    display: flex;
    align-items: center;
    font-size: 28rpx;
}

.radio-circle {
    width: 36rpx;
    height: 36rpx;
    border: 2rpx solid #d9d9d9;
    border-radius: 50%;
    margin-right: 10rpx;
}

.radio-item.active .radio-circle {
    border-color: #1677ff;
    background: radial-gradient(circle, #1677ff 30%, transparent 30%);
}

.verify-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15rpx;
}

.verify-title {
    font-size: 30rpx;
    font-weight: bold;
}

.verify-status {
    font-size: 24rpx;
    padding: 4rpx 16rpx;
    border-radius: 4rpx;
}

.verify-status.verified {
    color: #52c41a;
    background: #f6ffed;
}

.verify-status.unverified {
    color: #faad14;
    background: #fffbe6;
}

.verify-tip {
    font-size: 24rpx;
    color: #999999;
    margin-bottom: 20rpx;
}

.verify-btn {
    background: #1677ff;
    color: #ffffff;
    text-align: center;
    padding: 25rpx;
    border-radius: 8rpx;
    font-size: 28rpx;
}

.default-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.default-label {
    font-size: 28rpx;
    color: #333333;
}

.bottom-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #ffffff;
    padding: 20rpx 30rpx;
    display: flex;
    gap: 20rpx;
    box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.save-btn {
    flex: 1;
    background: #1677ff;
    color: #ffffff;
    text-align: center;
    padding: 28rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
}

.delete-btn {
    flex: 1;
    background: #fff1f0;
    color: #ff4d4f;
    text-align: center;
    padding: 28rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
}

.elderly-mode .form-label,
.elderly-mode .form-input,
.elderly-mode .picker-value {
    font-size: 34rpx;
}
</style>
