<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        const token = uni.getStorageSync('token')
        if (token) {
            uni.$u.http.setConfig({
                header: {
                    Authorization: 'Bearer ' + token
                }
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

<style lang="scss">
/* uview-plus 样式将在 main.js 中引入 */

page {
    background-color: #f5f5f5;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.container {
    padding: 20rpx;
}

.card {
    background: #fff;
    border-radius: 16rpx;
    padding: 24rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
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

.text-center {
    text-align: center;
}

.text-primary {
    color: #3c9cff;
}

.text-success {
    color: #5ac725;
}

.text-warning {
    color: #f9ae3d;
}

.text-danger {
    color: #f56c6c;
}

.text-muted {
    color: #909399;
}

.bg-primary {
    background-color: #3c9cff;
}

.bg-success {
    background-color: #5ac725;
}

.bg-warning {
    background-color: #f9ae3d;
}

.bg-danger {
    background-color: #f56c6c;
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

.p-10 {
    padding: 10rpx;
}

.p-20 {
    padding: 20rpx;
}

.p-30 {
    padding: 30rpx;
}

.fs-24 {
    font-size: 24rpx;
}

.fs-26 {
    font-size: 26rpx;
}

.fs-28 {
    font-size: 28rpx;
}

.fs-30 {
    font-size: 30rpx;
}

.fs-32 {
    font-size: 32rpx;
}

.fw-bold {
    font-weight: bold;
}

.ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.ellipsis-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
</style>
