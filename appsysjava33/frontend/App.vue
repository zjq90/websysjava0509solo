<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        const elderMode = uni.getStorageSync('elderMode')
        if (elderMode) {
            this.$store.commit('SET_ELDER_MODE', elderMode)
        }
    },
    onShow: function() {
        console.log('App Show')
    },
    onHide: function() {
        console.log('App Hide')
    }
}
</script>

<style>
/* 全局样式 */
page {
    background-color: #F5F5F5;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
}

/* 长辈模式 */
.elder-mode page {
    font-size: 32rpx;
}

.elder-mode .text-small {
    font-size: 28rpx !important;
}

.elder-mode .text-medium {
    font-size: 32rpx !important;
}

.elder-mode .text-large {
    font-size: 40rpx !important;
}

/* 通用样式类 */
.container {
    padding: 20rpx;
}

.card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 24rpx;
    text-align: center;
    font-size: 30rpx;
    font-weight: 500;
}

.btn-primary:active {
    opacity: 0.8;
}

.btn-secondary {
    background: #F5F7FA;
    color: #333333;
    border-radius: 48rpx;
    padding: 24rpx;
    text-align: center;
    font-size: 30rpx;
    font-weight: 500;
}

.text-primary {
    color: #007AFF;
}

.text-success {
    color: #34C759;
}

.text-warning {
    color: #FF9500;
}

.text-danger {
    color: #FF3B30;
}

.text-gray {
    color: #999999;
}

.text-dark {
    color: #333333;
}

.flex {
    display: flex;
}

.flex-between {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.flex-center {
    display: flex;
    justify-content: center;
    align-items: center;
}

.flex-column {
    display: flex;
    flex-direction: column;
}

.mt-10 {
    margin-top: 10rpx;
}

.mt-20 {
    margin-top: 20rpx;
}

.mb-10 {
    margin-bottom: 10rpx;
}

.mb-20 {
    margin-bottom: 20rpx;
}
</style>
