<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="logo-section">
            <text class="logo-icon">💐</text>
            <text class="app-name">花店APP</text>
            <text class="app-slogan">用心传递每一份美好</text>
        </view>
        
        <view class="form-section">
            <view class="form-item">
                <text class="form-label">用户名</text>
                <input class="form-input" v-model="username" placeholder="请输入用户名" />
            </view>
            <view class="form-item">
                <text class="form-label">密码</text>
                <input class="form-input" v-model="password" type="password" placeholder="请输入密码" />
            </view>
            
            <button class="login-btn" @click="handleLogin">登录</button>
            
            <view class="register-tip">
                <text class="tip-text">还没有账号？</text>
                <text class="register-link" @click="goToRegister">立即注册</text>
            </view>
        </view>
        
        <view class="test-account">
            <text class="test-title">测试账号：</text>
            <text class="test-info">用户名：admin / 密码：admin123</text>
            <text class="test-info">用户名：user / 密码：user123</text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            username: '',
            password: '',
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = getApp().globalData.elderMode
    },
    onShow() {
        this.elderMode = getApp().globalData.elderMode
    },
    methods: {
        handleLogin() {
            if (!this.username.trim()) {
                uni.showToast({
                    title: '请输入用户名',
                    icon: 'none'
                })
                return
            }
            if (!this.password.trim()) {
                uni.showToast({
                    title: '请输入密码',
                    icon: 'none'
                })
                return
            }
            
            this.$request.post('/user/login', {
                username: this.username,
                password: this.password
            }).then(res => {
                uni.setStorageSync('token', res.token)
                uni.setStorageSync('userInfo', res.user)
                getApp().globalData.token = res.token
                getApp().globalData.userInfo = res.user
                
                uni.showToast({
                    title: '登录成功',
                    icon: 'success'
                })
                
                setTimeout(() => {
                    uni.switchTab({
                        url: '/pages/index/index'
                    })
                }, 1500)
            }).catch(err => {
                console.error(err)
            })
        },
        
        goToRegister() {
            uni.navigateTo({
                url: '/pages/user/register'
            })
        }
    }
}
</script>

<style scoped>
.container {
    min-height: 100vh;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    padding: 40rpx 30rpx;
}

.logo-section {
    text-align: center;
    padding: 80rpx 0 60rpx;
}

.logo-icon {
    font-size: 120rpx;
    display: block;
    margin-bottom: 20rpx;
}

.app-name {
    font-size: 44rpx;
    font-weight: bold;
    color: #fff;
    display: block;
    margin-bottom: 10rpx;
}

.app-slogan {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.9);
}

.form-section {
    background: #fff;
    border-radius: 24rpx;
    padding: 50rpx 40rpx;
}

.form-item {
    margin-bottom: 30rpx;
}

.form-label {
    font-size: 28rpx;
    color: #333;
    display: block;
    margin-bottom: 15rpx;
}

.form-input {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    padding: 0 30rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 28rpx;
    box-sizing: border-box;
}

.login-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    font-weight: 500;
    border: none;
    padding: 0;
    margin-top: 20rpx;
}

.register-tip {
    text-align: center;
    margin-top: 30rpx;
}

.tip-text {
    font-size: 26rpx;
    color: #999;
}

.register-link {
    font-size: 26rpx;
    color: #FF6B6B;
    margin-left: 10rpx;
}

.test-account {
    margin-top: 40rpx;
    padding: 30rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 16rpx;
}

.test-title {
    font-size: 26rpx;
    color: #fff;
    font-weight: 500;
    display: block;
    margin-bottom: 15rpx;
}

.test-info {
    font-size: 24rpx;
    color: rgba(255, 255, 255, 0.9);
    display: block;
    margin-bottom: 8rpx;
}
</style>