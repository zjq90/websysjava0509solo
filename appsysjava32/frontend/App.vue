<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        const token = uni.getStorageSync('token')
        if (token) {
            this.$store.commit('SET_TOKEN', token)
            const userInfo = uni.getStorageSync('userInfo')
            if (userInfo) {
                this.$store.commit('SET_USER_INFO', userInfo)
            }
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
@import './common/css/uni.css';

page {
    background-color: #f5f5f5;
    font-size: 28rpx;
    color: #333;
}

.elder-mode {
    font-size: 36rpx;
}

.elder-mode .text-normal {
    font-size: 40rpx;
}

.elder-mode .text-small {
    font-size: 32rpx;
}

.elder-mode .text-large {
    font-size: 44rpx;
}
</style>
