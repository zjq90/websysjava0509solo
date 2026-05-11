<template>
    <view class="patient-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="content">
            <view 
                class="patient-item card" 
                v-for="patient in patientList" 
                :key="patient.id"
                @click="handlePatientClick(patient)"
            >
                <view class="patient-info">
                    <view class="patient-header">
                        <text class="patient-name">{{ patient.name }}</text>
                        <text class="relation-badge" v-if="patient.relation">{{ patient.relation }}</text>
                        <text class="default-badge" v-if="patient.isDefault">默认</text>
                    </view>
                    <text class="patient-idcard">{{ patient.idCard }}</text>
                    <view class="patient-status">
                        <text 
                            class="status-tag" 
                            :class="patient.realNameVerified ? 'verified' : 'unverified'"
                        >
                            {{ patient.realNameVerified ? '已认证' : '未认证' }}
                        </text>
                    </view>
                </view>
                <view class="patient-actions" v-if="!isSelectMode">
                    <text class="action-btn" @click.stop="editPatient(patient.id)">编辑</text>
                    <text class="action-btn" @click.stop="setDefault(patient)" v-if="!patient.isDefault">设为默认</text>
                </view>
                <text class="select-arrow" v-if="isSelectMode">></text>
            </view>

            <view class="empty-state" v-if="patientList.length === 0">
                <text class="empty-icon">👥</text>
                <text class="empty-text">暂无就诊人</text>
                <text class="empty-tip">请先添加就诊人信息</text>
            </view>
        </view>

        <view class="add-btn" @click="addPatient" v-if="!isSelectMode">
            <text class="add-icon">+</text>
            <text class="add-text">添加就诊人</text>
        </view>
    </view>
</template>

<script>
import { getPatientList, deletePatient, setDefaultPatient } from '@/api/patient'

export default {
    data() {
        return {
            patientList: [],
            isSelectMode: false,
            bookParams: null,
            elderlyMode: false
        }
    },
    onLoad(options) {
        if (options.mode === 'select') {
            this.isSelectMode = true
            this.bookParams = JSON.parse(decodeURIComponent(options.params))
            uni.setNavigationBarTitle({ title: '选择就诊人' })
        }
    },
    onShow() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
        this.loadPatientList()
    },
    methods: {
        async loadPatientList() {
            try {
                const res = await getPatientList()
                this.patientList = res.data.data || []
            } catch (e) {
                console.error(e)
            }
        },
        handlePatientClick(patient) {
            if (this.isSelectMode) {
                this.confirmBooking(patient)
            }
        },
        async confirmBooking(patient) {
            if (!patient.realNameVerified) {
                uni.showModal({
                    title: '提示',
                    content: '该就诊人未实名认证，无法挂号，请先完成认证',
                    showCancel: false
                })
                return
            }
            
            uni.showModal({
                title: '确认预约',
                content: `确认预约${this.bookParams.doctorName}医生\n${this.bookParams.scheduleDate} ${this.bookParams.timeSlot}\n挂号费：¥${this.bookParams.fee}`,
                success: async (res) => {
                    if (res.confirm) {
                        this.navigateToConfirm(patient)
                    }
                }
            })
        },
        navigateToConfirm(patient) {
            const params = {
                ...this.bookParams,
                patientId: patient.id,
                patientName: patient.name
            }
            uni.navigateTo({
                url: '/pages/appointment/appointment?mode=confirm&params=' + encodeURIComponent(JSON.stringify(params))
            })
        },
        addPatient() {
            uni.navigateTo({ url: '/pages/patient-edit/patient-edit' })
        },
        editPatient(id) {
            uni.navigateTo({ url: '/pages/patient-edit/patient-edit?id=' + id })
        },
        async setDefault(patient) {
            try {
                await setDefaultPatient(patient.id)
                uni.showToast({ title: '设置成功', icon: 'success' })
                this.loadPatientList()
            } catch (e) {
                console.error(e)
            }
        }
    }
}
</script>

<style scoped>
.patient-page {
    min-height: 100vh;
    background-color: #f5f7fa;
    padding-bottom: 120rpx;
}

.content {
    padding: 30rpx;
}

.patient-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    margin-bottom: 20rpx;
}

.patient-info {
    flex: 1;
}

.patient-header {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;
}

.patient-name {
    font-size: 32rpx;
    font-weight: 500;
    margin-right: 15rpx;
}

.relation-badge {
    font-size: 22rpx;
    color: #1677ff;
    background: #e6f4ff;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
    margin-right: 10rpx;
}

.default-badge {
    font-size: 22rpx;
    color: #ffffff;
    background: #1677ff;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
}

.patient-idcard {
    font-size: 26rpx;
    color: #666666;
    margin-bottom: 10rpx;
}

.patient-status {
    display: flex;
}

.status-tag {
    font-size: 24rpx;
    padding: 4rpx 12rpx;
    border-radius: 4rpx;
}

.status-tag.verified {
    color: #52c41a;
    background: #f6ffed;
}

.status-tag.unverified {
    color: #faad14;
    background: #fffbe6;
}

.patient-actions {
    display: flex;
    gap: 20rpx;
}

.action-btn {
    font-size: 26rpx;
    color: #1677ff;
}

.select-arrow {
    font-size: 30rpx;
    color: #999999;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 100rpx 0;
}

.empty-icon {
    font-size: 100rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 32rpx;
    color: #333333;
    margin-bottom: 10rpx;
}

.empty-tip {
    font-size: 26rpx;
    color: #999999;
}

.add-btn {
    position: fixed;
    bottom: 30rpx;
    left: 30rpx;
    right: 30rpx;
    background: #1677ff;
    color: #ffffff;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 28rpx;
    border-radius: 48rpx;
    font-size: 32rpx;
}

.add-icon {
    font-size: 40rpx;
    margin-right: 10rpx;
}

.elderly-mode .patient-name {
    font-size: 38rpx;
}

.elderly-mode .patient-idcard {
    font-size: 32rpx;
}
</style>
