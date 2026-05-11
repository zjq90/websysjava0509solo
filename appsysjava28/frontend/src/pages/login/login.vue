<template>
    <view class="login-page" :class="{ 'elderly-mode': elderlyMode }">
        <view class="header">
            <view class="logo">
                <text class="logo-icon">🏥</text>
            </view>
            <text class="title">智慧医疗</text>
            <text class="subtitle">便捷预约，健康相伴</text>
        </view>

        <view class="login-form">
            <view class="form-item">
                <input 
                    type="number" 
                    class="input" 
                    placeholder="请输入手机号" 
                    v-model="phone"
                    maxlength="11"
                />
            </view>

            <view class="form-item code-item">
                <input 
                    type="number" 
                    class="input code-input" 
                    placeholder="请输入验证码" 
                    v-model="code"
                    maxlength="6"
                />
                <view class="code-btn" :class="{ disabled: countdown > 0 }" @click="sendCode">
                    {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
                </view>
            </view>

            <view class="btn-primary" @click="handleLogin">
                登录 / 注册
            </view>

            <view class="divider">
                <view class="line"></view>
                <text class="divider-text">其他登录方式</text>
                <view class="line"></view>
            </view>

            <view class="wechat-login" @click="handleWechatLogin">
                <text class="wechat-icon">💚</text>
                <text class="wechat-text">微信一键登录</text>
            </view>
        </view>

        <view class="footer">
            <text class="agreement">登录即表示同意</text>
            <text class="link">《用户协议》</text>
            <text class="agreement">和</text>
            <text class="link">《隐私政策》</text>
        </view>

        <view class="elderly-toggle" @click="toggleElderlyMode">
            <text>{{ elderlyMode ? '👵 长辈模式' : '👴 切换长辈模式' }}</text>
        </view>
    </view>
</template>

<script>
import { sendSmsCode, loginWithSms, loginWithWechat } from '@/api/auth'

export default {
    data() {
        return {
            phone: '',
            code: '',
            countdown: 0,
            timer: null,
            elderlyMode: false
        }
    },
    onLoad() {
        this.elderlyMode = uni.getStorageSync('elderlyMode') || false
    },
    onUnload() {
        if (this.timer) {
            clearInterval(this.timer)
        }
    },
    methods: {
        async sendCode() {
            if (this.countdown > 0) return
            if (!this.phone || this.phone.length !== 11) {
                uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
                return
            }

            try {
                const res = await sendSmsCode(this.phone, 'LOGIN')
                uni.showToast({ title: res.data.message || '验证码已发送', icon: 'success' })
                
                this.countdown = 60
                this.timer = setInterval(() => {
                    this.countdown--
                    if (this.countdown <= 0) {
                        clearInterval(this.timer)
                    }
                }, 1000)
            } catch (e) {
                console.error(e)
            }
        },

        async handleLogin() {
            if (!this.phone || this.phone.length !== 11) {
                uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
                return
            }
            if (!this.code || this.code.length !== 6) {
                uni.showToast({ title: '请输入验证码', icon: 'none' })
                return
            }

            uni.showLoading({ title: '登录中...' })
            try {
                const res = await loginWithSms(this.phone, this.code)
                uni.hideLoading()
                
                uni.setStorageSync('token', res.data.data.token)
                uni.setStorageSync('userInfo', res.data.data.user)
                
                uni.showToast({ title: '登录成功', icon: 'success' })
                setTimeout(() => {
                    uni.switchTab({ url: '/pages/index/index' })
                }, 1000)
            } catch (e) {
                uni.hideLoading()
                console.error(e)
            }
        },

        async handleWechatLogin() {
            uni.showLoading({ title: '微信登录中...' })
            try {
                const res = await loginWithWechat('mock_code_' + Date.now(), '微信用户', '')
                uni.hideLoading()
                
                uni.setStorageSync('token', res.data.data.token)
                uni.setStorageSync('userInfo', res.data.data.user)
                
                uni.showToast({ title: '登录成功', icon: 'success' })
                setTimeout(() => {
                    uni.switchTab({ url: '/pages/index/index' })
                }, 1000)
            } catch (e) {
                uni.hideLoading()
                console.error(e)
            }
        },

        toggleElderlyMode() {
            this.elderlyMode = !this.elderlyMode
            uni.setStorageSync('elderlyMode', this.elderlyMode)
        }
    }
}
</script>

<style scoped>
.login-page {
    min-height: 100vh;
    background: linear-gradient(180deg, #1677ff 0%, #4096ff 50%, #f5f7fa 50%);
    padding: 0 40rpx;
    box-sizing: border-box;
}

.header {
    text-align: center;
    padding: 120rpx 0 80rpx;
}

.logo {
    width: 160rpx;
    height: 160rpx;
    background: #ffffff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 30rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.logo-icon {
    font-size: 80rpx;
}

.title {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #ffffff;
    margin-bottom: 10rpx;
}

.subtitle {
    display: block;
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
}

.login-form {
    background: #ffffff;
    border-radius: 24rpx;
    padding: 50rpx 40rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
}

.form-item {
    margin-bottom: 30rpx;
    border-bottom: 2rpx solid #f0f0f0;
    padding-bottom: 20rpx;
}

.input {
    width: 100%;
    height: 80rpx;
    font-size: 32rpx;
}

.code-item {
    display: flex;
    align-items: center;
}

.code-input {
    flex: 1;
}

.code-btn {
    padding: 15rpx 30rpx;
    background: #e6f4ff;
    color: #1677ff;
    border-radius: 40rpx;
    font-size: 28rpx;
    white-space: nowrap;
}

.code-btn.disabled {
    color: #999999;
    background: #f5f5f5;
}

.divider {
    display: flex;
    align-items: center;
    margin: 50rpx 0;
}

.line {
    flex: 1;
    height: 1rpx;
    background: #e0e0e0;
}

.divider-text {
    padding: 0 30rpx;
    font-size: 26rpx;
    color: #999999;
}

.wechat-login {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 25rpx 0;
    border: 2rpx solid #07c160;
    border-radius: 48rpx;
}

.wechat-icon {
    font-size: 36rpx;
    margin-right: 15rpx;
}

.wechat-text {
    font-size: 30rpx;
    color: #07c160;
}

.footer {
    text-align: center;
    margin-top: 60rpx;
    font-size: 24rpx;
    color: #999999;
}

.link {
    color: #1677ff;
}

.elderly-toggle {
    position: fixed;
    bottom: 60rpx;
    right: 40rpx;
    padding: 20rpx 30rpx;
    background: #ffffff;
    border-radius: 40rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
    font-size: 26rpx;
    color: #1677ff;
}

.elderly-mode .title {
    font-size: 56rpx;
}

.elderly-mode .subtitle {
    font-size: 34rpx;
}

.elderly-mode .input {
    font-size: 40rpx;
}

.elderly-mode .btn-primary {
    font-size: 40rpx;
    padding: 30rpx 0;
}
</style>
