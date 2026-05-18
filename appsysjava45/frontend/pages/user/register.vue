<template>
    <view class="container" :class="{ 'elder-mode': elderMode }">
        <view class="logo-section">
            <text class="logo-icon">💐</text>
            <text class="app-name">花店APP</text>
            <text class="app-slogan">注册账号，开启美好之旅</text>
        </view>
        
        <view class="form-section">
            <view class="form-item">
                <text class="form-label">用户名</text>
                <input class="form-input" v-model="username" placeholder="请输入用户名" />
            </view>
            <view class="form-item">
                <text class="form-label">昵称</text>
                <input class="form-input" v-model="nickname" placeholder="请输入昵称" />
            </view>
            <view class="form-item">
                <text class="form-label">手机号</text>
                <input class="form-input" v-model="phone" type="number" placeholder="请输入手机号" />
            </view>
            <view class="form-item">
                <text class="form-label">密码</text>
                <input class="form-input" v-model="password" type="password" placeholder="请输入密码" />
            </view>
            <view class="form-item">
                <text class="form-label">确认密码</text>
                <input class="form-input" v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
            </view>
            
            <button class="register-btn" @click="handleRegister">注册</button>
            
            <view class="login-tip">
                <text class="tip-text">已有账号？</text>
                <text class="login-link" @click="goToLogin">立即登录</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            username: '',
            nickname: '',
            phone: '',
            password: '',
            confirmPassword: '',
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
        handleRegister() {
            if (!this.username.trim()) {
                uni.showToast({
                    title: '请输入用户名',
                    icon: 'none'
                })
                return
            }
            if (!this.nickname.trim()) {
                uni.showToast({
                    title: '请输入昵称',
                    icon: 'none'
                })
                return
            }
            if (!this.phone.trim()) {
                uni.showToast({
                    title: '请输入手机号',
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
            if (this.password !== this.confirmPassword) {
                uni.showToast({
                    title: '两次密码输入不一致',
                    icon: 'none'
                })
                return
            }
            
            this.$request.post('/user/register', {
                username: this.username,
                nickname: this.nickname,
                phone: this.phone,
                password: this.password
            }).then(res => {
                uni.showToast({
                    title: '注册成功',
                    icon: 'success'
                })
                
                setTimeout(() => {
                    uni.navigateBack()
                }, 1500)
            }).catch(err => {
                console.error(err)
            })
        },
        
        goToLogin() {
            uni.navigateBack()
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
    padding: 60rpx 0 40rpx;
}

.logo-icon {
    font-size: 100rpx;
    display: block;
    margin-bottom: 20rpx;
}

.app-name {
    font-size: 40rpx;
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
    padding: 40rpx;
}

.form-item {
    margin-bottom: 25rpx;
}

.form-label {
    font-size: 26rpx;
    color: #333;
    display: block;
    margin-bottom: 12rpx;
}

.form-input {
    width: 100%;
    height: 80rpx;
    line-height: 80rpx;
    padding: 0 25rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 26rpx;
    box-sizing: border-box;
}

.register-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
    color: #fff;
    border-radius: 44rpx;
    font-size: 30rpx;
    font-weight: 500;
    border: none;
    padding: 0;
    margin-top: 10rpx;
}

.login-tip {
    text-align: center;
    margin-top: 25rpx;
}

.tip-text {
    font-size: 24rpx;
    color: #999;
}

.login-link {
    font-size: 24rpx;
    color: #FF6B6B;
    margin-left: 10rpx;
}
</style>