<script>
export default {
    onLaunch: function() {
        console.log('App Launch');
    },
    onShow: function() {
        console.log('App Show');
    },
    onHide: function() {
        console.log('App Hide');
    }
};
</script>

<style>
/*每个页面公共css */
page {
    background-color: #f5f5f5;
    font-size: 28rpx;
    color: #333;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.container {
    padding: 30rpx;
    box-sizing: border-box;
}

.card {
    background: #fff;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 24rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;
}

.card-title-icon {
    width: 40rpx;
    height: 40rpx;
    margin-right: 16rpx;
}

.btn-primary {
    background: linear-gradient(135deg, #3c9cff, #5ba8ff);
    color: #fff;
    border-radius: 50rpx;
    padding: 24rpx;
    font-size: 32rpx;
    text-align: center;
    border: none;
}

.btn-danger {
    background: linear-gradient(135deg, #f56c6c, #f78989);
    color: #fff;
    border-radius: 50rpx;
    padding: 24rpx;
    font-size: 32rpx;
    text-align: center;
    border: none;
}

.btn-outline {
    background: #fff;
    color: #3c9cff;
    border: 2rpx solid #3c9cff;
    border-radius: 50rpx;
    padding: 22rpx;
    font-size: 32rpx;
    text-align: center;
}

.stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
}

.stat-value {
    font-size: 48rpx;
    font-weight: bold;
    color: #3c9cff;
    margin-bottom: 8rpx;
}

.stat-label {
    font-size: 24rpx;
    color: #999;
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
    flex-direction: row;
    align-items: center;
    justify-content: center;
}

.text-primary {
    color: #3c9cff;
}

.text-success {
    color: #67c23a;
}

.text-warning {
    color: #e6a23c;
}

.text-danger {
    color: #f56c6c;
}

.text-muted {
    color: #999;
}

.text-bold {
    font-weight: bold;
}

.mt-20 {
    margin-top: 20rpx;
}

.mb-20 {
    margin-bottom: 20rpx;
}

.pt-20 {
    padding-top: 20rpx;
}

.pb-20 {
    padding-bottom: 20rpx;
}

.border-bottom {
    border-bottom: 1rpx solid #f0f0f0;
}

.form-item {
    margin-bottom: 32rpx;
}

.form-label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 12rpx;
}

.form-input {
    background: #f8f8f8;
    border-radius: 12rpx;
    padding: 24rpx;
    font-size: 28rpx;
    border: 1rpx solid #e8e8e8;
}

.form-input:focus {
    border-color: #3c9cff;
}

.list-item {
    background: #fff;
    padding: 32rpx;
    border-bottom: 1rpx solid #f0f0f0;
}

.list-item:last-child {
    border-bottom: none;
}

.status-tag {
    display: inline-block;
    padding: 8rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
}

.status-completed {
    background: #e1f3d8;
    color: #67c23a;
}

.status-pending {
    background: #fdf6ec;
    color: #e6a23c;
}

.status-partial {
    background: #ecf5ff;
    color: #409eff;
}

.progress-bar {
    height: 12rpx;
    background: #e8e8e8;
    border-radius: 6rpx;
    overflow: hidden;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #3c9cff, #67c23a);
    border-radius: 6rpx;
    transition: width 0.3s;
}

.empty-state {
    text-align: center;
    padding: 100rpx 40rpx;
    color: #999;
}

.empty-icon {
    font-size: 120rpx;
    margin-bottom: 20rpx;
}

.empty-text {
    font-size: 28rpx;
}
</style>
