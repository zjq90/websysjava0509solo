<script>
export default {
    onLaunch: function() {
        console.log('App Launch')
        
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
            this.globalData.userInfo = userInfo
            this.globalData.isLogin = true
        }
    },
    onShow: function() {
        console.log('App Show')
    },
    onHide: function() {
        console.log('App Hide')
    },
    globalData: {
        isLogin: false,
        userInfo: null,
        elderMode: false
    }
}
</script>

<style>
@import './static/css/common.css';
</style>
