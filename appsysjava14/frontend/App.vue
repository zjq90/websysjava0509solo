<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        const token = uni.getStorageSync('token')
        const userInfo = uni.getStorageSync('userInfo')
        if (token && userInfo) {
            this.$store.commit('SET_USER_INFO', userInfo)
            this.$store.commit('SET_TOKEN', token)
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
    background-color: #f5f5f5;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.container {
    padding: 20rpx;
}

.card {
    background-color: #fff;
    border-radius: 12rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.btn-primary {
    background-color: #4CAF50;
    color: #fff;
    border-radius: 8rpx;
    height: 88rpx;
    line-height: 88rpx;
    text-align: center;
    font-size: 32rpx;
}

.btn-primary:active {
    background-color: #45a049;
}

.btn-secondary {
    background-color: #fff;
    color: #4CAF50;
    border: 2rpx solid #4CAF50;
    border-radius: 8rpx;
    height: 88rpx;
    line-height: 84rpx;
    text-align: center;
    font-size: 32rpx;
}

.form-item {
    margin-bottom: 24rpx;
}

.form-label {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 12rpx;
}

.form-input {
    background-color: #f8f8f8;
    border-radius: 8rpx;
    height: 80rpx;
    padding: 0 20rpx;
    font-size: 28rpx;
}

.form-textarea {
    background-color: #f8f8f8;
    border-radius: 8rpx;
    padding: 20rpx;
    font-size: 28rpx;
    min-height: 160rpx;
}

.divider {
    height: 1rpx;
    background-color: #eee;
    margin: 20rpx 0;
}

.flex-row {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.flex-between {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}

.flex-center {
    display: flex;
    align-items: center;
    justify-content: center;
}

.text-primary {
    color: #4CAF50;
}

.text-danger {
    color: #f44336;
}

.text-gray {
    color: #999;
}

.text-sm {
    font-size: 24rpx;
}

.text-base {
    font-size: 28rpx;
}

.text-lg {
    font-size: 32rpx;
}

.text-xl {
    font-size: 36rpx;
}

.mt-10 { margin-top: 10rpx; }
.mt-20 { margin-top: 20rpx; }
.mt-30 { margin-top: 30rpx; }
.mb-10 { margin-bottom: 10rpx; }
.mb-20 { margin-bottom: 20rpx; }
.mb-30 { margin-bottom: 30rpx; }
</style>
