<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        const token = uni.getStorageSync('token')
        if (token) {
            uni.reLaunch({
                url: '/pages/index/index'
            })
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
page {
    background-color: #f5f7fa;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    font-size: 28rpx;
    color: #333333;
}

.container {
    padding: 30rpx;
}

.card {
    background-color: #ffffff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
    background-color: #1677ff;
    color: #ffffff;
    border-radius: 48rpx;
    font-size: 32rpx;
    padding: 20rpx 0;
    text-align: center;
}

.btn-primary:active {
    opacity: 0.8;
}

.btn-outline {
    background-color: #ffffff;
    color: #1677ff;
    border: 2rpx solid #1677ff;
    border-radius: 48rpx;
    font-size: 32rpx;
    padding: 18rpx 0;
    text-align: center;
}

.text-primary {
    color: #1677ff;
}

.text-secondary {
    color: #666666;
}

.text-muted {
    color: #999999;
}

.text-success {
    color: #52c41a;
}

.text-warning {
    color: #faad14;
}

.text-danger {
    color: #ff4d4f;
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

.flex-1 {
    flex: 1;
}

.mt-10 {
    margin-top: 10rpx;
}

.mt-20 {
    margin-top: 20rpx;
}

.mt-30 {
    margin-top: 30rpx;
}

.mb-10 {
    margin-bottom: 10rpx;
}

.mb-20 {
    margin-bottom: 20rpx;
}

.mb-30 {
    margin-bottom: 30rpx;
}

.elderly-mode {
    font-size: 36rpx !important;
}

.elderly-mode .card {
    padding: 40rpx;
    margin-bottom: 30rpx;
}

.elderly-mode .btn-primary,
.elderly-mode .btn-outline {
    font-size: 40rpx;
    padding: 30rpx 0;
}
</style>
