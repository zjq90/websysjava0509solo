<script>
    export default {
        onLaunch: function() {
            console.log('种子质量追溯系统启动');
            // 初始化全局配置
            uni.setStorageSync('apiBaseUrl', 'http://localhost:8080/api');
        },
        onShow: function() {
            console.log('App Show');
        },
        onHide: function() {
            console.log('App Hide');
        }
    }
</script>

<style>
    /*每个页面公共css */
    @import './common/uni.css';
</style>
