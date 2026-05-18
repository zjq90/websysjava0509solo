<script>
export default {
    globalData: {
        baseUrl: '/api',
        token: '',
        userInfo: null,
        elderMode: false
    },
    onLaunch: function() {
        console.log('App Launch')
        const token = uni.getStorageSync('token')
        if (token) {
            this.globalData.token = token
        }
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
            this.globalData.userInfo = userInfo
        }
        const elderMode = uni.getStorageSync('elderMode')
        if (elderMode) {
            this.globalData.elderMode = elderMode
        }
    },
    onShow: function() {
        console.log('App Show')
    },
    onHide: function() {
        console.log('App Hide')
    },
    methods: {
        toggleElderMode() {
            this.globalData.elderMode = !this.globalData.elderMode
            uni.setStorageSync('elderMode', this.globalData.elderMode)
            uni.showToast({
                title: this.globalData.elderMode ? '已开启长辈模式' : '已关闭长辈模式',
                icon: 'success'
            })
        }
    }
}
</script>

<style>
/* 全局样式 */
page {
    background-color: #f5f5f5;
    font-size: 28rpx;
    color: #333;
}

/* 长辈模式 - 放大字体 */
.elder-mode page {
    font-size: 36rpx !important;
}

.elder-mode .text-sm {
    font-size: 32rpx !important;
}

.elder-mode .text-lg {
    font-size: 40rpx !important;
}

.elder-mode button {
    height: 100rpx !important;
    font-size: 36rpx !important;
}

.elder-mode input {
    height: 80rpx !important;
    font-size: 36rpx !important;
}

/* 常用样式类 */
.container {
    padding: 20rpx;
}

.flex {
    display: flex;
}

.flex-center {
    display: flex;
    align-items: center;
    justify-content: center;
}

.flex-between {
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.flex-col {
    flex-direction: column;
}

.text-center {
    text-align: center;
}

.text-primary {
    color: #FF6B6B;
}

.text-muted {
    color: #999;
}

.text-sm {
    font-size: 24rpx;
}

.text-lg {
    font-size: 32rpx;
}

.text-xl {
    font-size: 36rpx;
}

.font-bold {
    font-weight: bold;
}

.bg-white {
    background-color: #fff;
}

.mt-10 {
    margin-top: 10rpx;
}

.mt-20 {
    margin-top: 20rpx;
}

.mb-20 {
    margin-bottom: 20rpx;
}

.p-20 {
    padding: 20rpx;
}

.rounded {
    border-radius: 16rpx;
}

.shadow {
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
}

.btn-primary {
    background-color: #FF6B6B;
    color: #fff;
    border: none;
}

.btn-primary:active {
    background-color: #e55555;
}

.line-through {
    text-decoration: line-through;
}
</style>