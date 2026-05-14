<template>
    <view class="container">
        <view class="form-card">
            <view class="form-title">选择套餐</view>
            <view class="package-selector">
                <view class="package-option" v-for="pkg in packages" :key="pkg.id" :class="{ active: selectedPackage === pkg.id }" @click="selectPackage(pkg.id)">
                    <view class="package-info">
                        <text class="package-name">{{ pkg.name }}</text>
                        <text class="package-bandwidth">{{ pkg.bandwidth }}Mbps</text>
                    </view>
                    <text class="package-price">¥{{ pkg.monthlyFee }}/月</text>
                </view>
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">选择号码</view>
            <view class="number-selector" @click="goSelectNumber">
                <text class="number-text" v-if="selectedNumber">{{ selectedNumber }}</text>
                <text class="number-placeholder" v-else>请选择宽带号码</text>
                <text class="arrow">›</text>
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">安装地址</view>
            <view class="form-item">
                <input class="input" v-model="address" placeholder="请输入详细地址" />
            </view>
            <view class="form-item">
                <input class="input" v-model="contactName" placeholder="联系人姓名" />
            </view>
            <view class="form-item">
                <input class="input" v-model="contactPhone" placeholder="联系电话" />
            </view>
        </view>

        <view class="form-card">
            <view class="form-title">预约时间</view>
            <view class="time-selector" @click="openTimePicker">
                <text class="time-text" v-if="appointmentTime">{{ appointmentTime }}</text>
                <text class="time-placeholder" v-else>请选择预约时间</text>
                <text class="arrow">›</text>
            </view>
        </view>

        <view class="modal-mask" v-if="showTimePicker" @click="showTimePicker = false">
            <view class="time-picker-modal" @click.stop>
                <view class="picker-header">
                    <text class="picker-cancel" @click="showTimePicker = false">取消</text>
                    <text class="picker-title">选择预约时间</text>
                    <text class="picker-confirm" @click="confirmTime">确定</text>
                </view>
                <picker-view class="picker-view" :value="pickerValue" @change="onPickerChange">
                    <picker-view-column>
                        <view class="picker-item" v-for="d in dates" :key="d">{{ d }}</view>
                    </picker-view-column>
                    <picker-view-column>
                        <view class="picker-item" v-for="t in times" :key="t">{{ t }}</view>
                    </picker-view-column>
                </picker-view>
            </view>
        </view>

        <view class="agreement">
            <checkbox :checked="agreed" @click="agreed = !agreed" color="#007AFF" />
            <text class="agreement-text">我已阅读并同意</text>
            <text class="agreement-link">《宽带服务协议》</text>
        </view>

        <button class="submit-btn" :disabled="!canSubmit" @click="submitOrder">提交申请</button>
    </view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const packages = ref([
    { id: 1, name: '100M光纤宽带', bandwidth: 100, monthlyFee: 99 },
    { id: 2, name: '300M光纤宽带', bandwidth: 300, monthlyFee: 159 },
    { id: 3, name: '500M光纤宽带', bandwidth: 500, monthlyFee: 219 },
    { id: 4, name: '1000M光纤宽带', bandwidth: 1000, monthlyFee: 299 }
])

const selectedPackage = ref(null)
const selectedNumber = ref('')
const address = ref('')
const contactName = ref('')
const contactPhone = ref('')
const appointmentTime = ref('')
const agreed = ref(false)
const showTimePicker = ref(false)
const pickerValue = ref([0, 0])
const selectedDate = ref('')
const selectedTimeVal = ref('')

const dates = ref([])
const times = ref([
    '09:00-10:00', '10:00-11:00', '11:00-12:00',
    '14:00-15:00', '15:00-16:00', '16:00-17:00', '17:00-18:00'
])

const canSubmit = computed(() => {
    return selectedPackage.value && selectedNumber.value && address.value && 
           contactName.value && contactPhone.value && appointmentTime.value && agreed.value
})

onMounted(() => {
    generateDates()
    uni.$on('numberSelected', handleNumberSelected)
})

onUnmounted(() => {
    uni.$off('numberSelected', handleNumberSelected)
})

const generateDates = () => {
    const result = []
    const today = new Date()
    for (let i = 1; i <= 7; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + i)
        const month = date.getMonth() + 1
        const day = date.getDate()
        const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
        const weekDay = weekDays[date.getDay()]
        result.push(`${month}月${day}日 ${weekDay}`)
    }
    dates.value = result
    selectedDate.value = result[0]
    selectedTimeVal.value = times.value[0]
}

const onPickerChange = (e) => {
    pickerValue.value = e.detail.value
    selectedDate.value = dates.value[e.detail.value[0]]
    selectedTimeVal.value = times.value[e.detail.value[1]]
}

const openTimePicker = () => {
    showTimePicker.value = true
}

const confirmTime = () => {
    appointmentTime.value = `${selectedDate.value} ${selectedTimeVal.value}`
    showTimePicker.value = false
}

const selectPackage = (id) => {
    selectedPackage.value = id
}

const goSelectNumber = () => {
    uni.navigateTo({
        url: '/pages/number/select'
    })
}

const handleNumberSelected = (number) => {
    selectedNumber.value = number
}

const submitOrder = () => {
    uni.showLoading({ title: '提交中...' })
    
    setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
            title: '申请提交成功',
            icon: 'success'
        })
        setTimeout(() => {
            uni.navigateBack()
        }, 1500)
    }, 1000)
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 100rpx;
}

.form-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-title {
    display: block;
    font-size: 30rpx;
    font-weight: 500;
    color: #333333;
    margin-bottom: 20rpx;
}

.package-selector {
    display: flex;
    flex-direction: column;
    gap: 16rpx;
}

.package-option {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx;
    border: 2rpx solid #E5E5E5;
    border-radius: 12rpx;
}

.package-option.active {
    border-color: #007AFF;
    background: #E8F3FF;
}

.package-info {
    display: flex;
    flex-direction: column;
    gap: 4rpx;
}

.package-name {
    font-size: 28rpx;
    color: #333333;
    font-weight: 500;
}

.package-bandwidth {
    font-size: 24rpx;
    color: #999999;
}

.package-price {
    font-size: 28rpx;
    color: #FF3B30;
    font-weight: 500;
}

.number-selector, .time-selector {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
}

.number-text, .time-text {
    font-size: 28rpx;
    color: #333333;
}

.number-placeholder, .time-placeholder {
    font-size: 28rpx;
    color: #999999;
}

.arrow {
    font-size: 32rpx;
    color: #CCCCCC;
}

.form-item {
    margin-bottom: 16rpx;
}

.form-item:last-child {
    margin-bottom: 0;
}

.input {
    width: 100%;
    padding: 20rpx;
    background: #F5F7FA;
    border-radius: 12rpx;
    font-size: 28rpx;
    color: #333333;
}

.agreement {
    display: flex;
    align-items: center;
    padding: 20rpx;
    margin-bottom: 30rpx;
}

.agreement-text {
    font-size: 24rpx;
    color: #666666;
    margin-left: 12rpx;
}

.agreement-link {
    font-size: 24rpx;
    color: #007AFF;
}

.submit-btn {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.submit-btn[disabled] {
    background: #CCCCCC;
}

.modal-mask {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 999;
    display: flex;
    align-items: flex-end;
}

.time-picker-modal {
    width: 100%;
    background: #FFFFFF;
    border-radius: 24rpx 24rpx 0 0;
    padding-bottom: 40rpx;
}

.picker-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.picker-cancel {
    font-size: 28rpx;
    color: #999999;
}

.picker-title {
    font-size: 32rpx;
    font-weight: 500;
    color: #333333;
}

.picker-confirm {
    font-size: 28rpx;
    color: #007AFF;
    font-weight: 500;
}

.picker-view {
    height: 400rpx;
}

.picker-item {
    line-height: 80rpx;
    text-align: center;
    font-size: 28rpx;
    color: #333333;
}
</style>
