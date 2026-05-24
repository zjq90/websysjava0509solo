<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        this.initTheme()
        this.checkSync()
    },
    onShow: function() {
        console.log('App Show')
    },
    onHide: function() {
        console.log('App Hide')
    },
    methods: {
        initTheme: function() {
            var theme = uni.getStorageSync('theme') || 'light'
            uni.setStorageSync('theme', theme)
        },
        checkSync: function() {
            var unsyncedRecords = uni.getStorageSync('unsyncedRecords') || []
            if (unsyncedRecords.length > 0) {
                uni.showToast({
                    title: '有' + unsyncedRecords.length + '条记录待同步',
                    icon: 'none'
                })
            }
        }
    }
}
</script>

<style>
@import './static/css/theme.css';

page {
    background-color: var(--bg-color);
    color: var(--text-color);
    transition: all 0.3s ease;
}

.container {
    padding: 20rpx;
}

.card {
    background: var(--card-bg);
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx var(--shadow-color);
}

.btn-primary {
    background: linear-gradient(135deg, #1a73e8, #0d47a1);
    color: white;
    border-radius: 50rpx;
    height: 88rpx;
    line-height: 88rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.btn-success {
    background: linear-gradient(135deg, #34a853, #1e7e34);
    color: white;
    border-radius: 50rpx;
    height: 88rpx;
    line-height: 88rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.btn-danger {
    background: linear-gradient(135deg, #ea4335, #c5221f);
    color: white;
    border-radius: 50rpx;
    height: 88rpx;
    line-height: 88rpx;
    font-size: 32rpx;
    font-weight: 500;
}

.text-primary {
    color: var(--primary-color);
}

.text-success {
    color: var(--success-color);
}

.text-danger {
    color: var(--danger-color);
}

.text-warning {
    color: var(--warning-color);
}
</style>
