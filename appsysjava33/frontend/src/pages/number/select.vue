<template>
    <view class="container">
        <view class="search-bar">
            <input class="search-input" v-model="searchKeyword" placeholder="搜索号码" />
        </view>

        <view class="tab-bar">
            <view class="tab-item" :class="{ active: activeTab === 'normal' }" @click="activeTab = 'normal'">普通号码</view>
            <view class="tab-item" :class="{ active: activeTab === 'fancy' }" @click="activeTab = 'fancy'">靓号</view>
        </view>

        <view class="number-list">
            <view class="number-item" v-for="num in filteredNumbers" :key="num.id" :class="{ selected: selectedNumber === num.number }" @click="selectNumber(num.number)">
                <text class="number-text">{{ num.number }}</text>
                <text class="number-tag" v-if="num.type === 2">靓号</text>
                <text class="number-price" v-if="num.type === 2">¥{{ num.extraFee }}</text>
            </view>
        </view>

        <button class="confirm-btn" v-if="selectedNumber" @click="confirmSelect">确定选择</button>
    </view>
</template>

<script setup>
import { ref, computed } from 'vue'

const activeTab = ref('normal')
const searchKeyword = ref('')
const selectedNumber = ref('')

const numbers = ref([
    { id: 1, number: '01012345678', type: 1, extraFee: 0 },
    { id: 2, number: '01012345679', type: 1, extraFee: 0 },
    { id: 3, number: '01012345680', type: 1, extraFee: 0 },
    { id: 4, number: '01088888888', type: 2, extraFee: 200 },
    { id: 5, number: '01066666666', type: 2, extraFee: 200 },
    { id: 6, number: '01012345677', type: 1, extraFee: 0 },
    { id: 7, number: '01012345676', type: 1, extraFee: 0 },
    { id: 8, number: '01016881688', type: 2, extraFee: 150 },
    { id: 9, number: '01018888888', type: 2, extraFee: 300 },
    { id: 10, number: '01012345675', type: 1, extraFee: 0 }
])

const filteredNumbers = computed(() => {
    const type = activeTab.value === 'normal' ? 1 : 2
    let list = numbers.value.filter(n => n.type === type)
    if (searchKeyword.value) {
        list = list.filter(n => n.number.includes(searchKeyword.value))
    }
    return list
})

const selectNumber = (number) => {
    selectedNumber.value = number
}

const confirmSelect = () => {
    uni.showToast({
        title: '已选择：' + selectedNumber.value,
        icon: 'success'
    })
    setTimeout(() => {
        uni.$emit('numberSelected', selectedNumber.value)
        uni.navigateBack()
    }, 500)
}
</script>

<style scoped>
.container {
    padding: 20rpx;
    padding-bottom: 120rpx;
}

.search-bar {
    margin-bottom: 20rpx;
}

.search-input {
    width: 100%;
    padding: 20rpx 24rpx;
    background: #FFFFFF;
    border-radius: 48rpx;
    font-size: 28rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.tab-bar {
    display: flex;
    background: #FFFFFF;
    border-radius: 12rpx;
    padding: 8rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.tab-item {
    flex: 1;
    text-align: center;
    padding: 16rpx;
    font-size: 28rpx;
    color: #666666;
    border-radius: 8rpx;
}

.tab-item.active {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    font-weight: 500;
}

.number-list {
    display: flex;
    flex-direction: column;
    gap: 16rpx;
}

.number-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx;
    background: #FFFFFF;
    border-radius: 12rpx;
    border: 2rpx solid transparent;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.number-item.selected {
    border-color: #007AFF;
    background: #E8F3FF;
}

.number-text {
    font-size: 32rpx;
    font-weight: 500;
    color: #333333;
}

.number-tag {
    font-size: 22rpx;
    color: #FF9500;
    background: #FFF5E6;
    padding: 4rpx 12rpx;
    border-radius: 8rpx;
}

.number-price {
    font-size: 28rpx;
    color: #FF3B30;
    font-weight: 500;
}

.confirm-btn {
    position: fixed;
    bottom: 40rpx;
    left: 20rpx;
    right: 20rpx;
    width: calc(100% - 40rpx);
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
}
</style>
