<template>
    <view class="login-container">
        <view class="login-header">
            <view class="logo">
                <text class="logo-icon">📡</text>
            </view>
            <text class="title">宽带服务</text>
            <text class="subtitle">智慧生活，极速连接</text>
        </view>

        <view class="login-form">
            <view class="form-item">
                <view class="input-wrapper">
                    <text class="input-icon">📱</text>
                    <input 
                        class="input" 
                        type="number" 
                        placeholder="请输入手机号" 
                        v-model="phone"
                        maxlength="11"
                    />
                </view>
            </view>

            <view class="form-item">
                <view class="input-wrapper code-wrapper">
                    <text class="input-icon">🔐</text>
                    <input 
                        class="input" 
                        type="number" 
                        placeholder="请输入验证码" 
                        v-model="code"
                        maxlength="6"
                    />
                    <button 
                        class="code-btn" 
                        :disabled="countdown > 0"
                        @click="sendCode"
                    >
                        {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
                    </button>
                </view>
            </view>

            <button class="login-btn" @click="login">
                登录
            </button>

            <view class="other-login">
                <text class="other-title">其他登录方式</text>
                <view class="login-methods">
                    <view class="method-item" @click="loginByWechat">
                        <text class="method-icon">💬</text>
                        <text class="method-text">微信</text>
                    </view>
                    <view class="method-item" @click="loginByAlipay">
                        <text class="method-icon">💰</text>
                        <text class="method-text">支付宝</text>
                    </view>
                    <view class="method-item" @click="loginByFace">
                        <text class="method-icon">👤</text>
                        <text class="method-text">人脸识别</text>
                    </view>
                </view>
            </view>
        </view>

        <view class="login-footer">
            <text class="agreement">
                登录即表示同意 <text class="link">《用户协议》</text> 和 <text class="link">《隐私政策》</text>
            </text>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            phone: '',
            code: '',
            countdown: 0,
            timer: null
        }
    },
    onUnload() {
        if (this.timer) {
            clearInterval(this.timer)
        }
    },
    methods: {
        sendCode() {
            if (!this.phone) {
                uni.showToast({
                    title: '请输入手机号',
                    icon: 'none'
                })
                return
            }
            if (!/^1\d{10}$/.test(this.phone)) {
                uni.showToast({
                    title: '手机号格式不正确',
                    icon: 'none'
                })
                return
            }

            uni.request({
                url: '/api/auth/sendCode?phone=' + this.phone,
                method: 'POST',
                success: (res) => {
                    if (res.data.code === 200) {
                        uni.showToast({
                            title: '验证码已发送',
                            icon: 'success'
                        })
                        this.startCountdown()
                    } else {
                        uni.showToast({
                            title: res.data.message,
                            icon: 'none'
                        })
                    }
                }
            })
        },

        startCountdown() {
            this.countdown = 60
            this.timer = setInterval(() => {
                this.countdown--
                if (this.countdown <= 0) {
                    clearInterval(this.timer)
                }
            }, 1000)
        },

        login() {
            if (!this.phone) {
                uni.showToast({
                    title: '请输入手机号',
                    icon: 'none'
                })
                return
            }
            if (!this.code) {
                uni.showToast({
                    title: '请输入验证码',
                    icon: 'none'
                })
                return
            }

            uni.showLoading({
                title: '登录中...'
            })

            uni.request({
                url: '/api/auth/login/phone?phone=' + this.phone + '&code=' + this.code,
                method: 'POST',
                success: (res) => {
                    uni.hideLoading()
                    if (res.data.code === 200) {
                        uni.setStorageSync('userInfo', res.data.data.user)
                        uni.setStorageSync('token', res.data.data.token)
                        uni.setStorageSync('userId', res.data.data.userId)
                        uni.showToast({
                            title: '登录成功',
                            icon: 'success'
                        })
                        setTimeout(() => {
                            uni.switchTab({
                                url: '/pages/index/index'
                            })
                        }, 1000)
                    } else {
                        uni.showToast({
                            title: res.data.message,
                            icon: 'none'
                        })
                    }
                },
                fail: () => {
                    uni.hideLoading()
                    uni.showToast({
                        title: '登录失败，请重试',
                        icon: 'none'
                    })
                }
            })
        },

        loginByWechat() {
            uni.showModal({
                title: '提示',
                content: '模拟微信登录成功',
                showCancel: false,
                success: () => {
                    this.mockLogin()
                }
            })
        },

        loginByAlipay() {
            uni.showModal({
                title: '提示',
                content: '模拟支付宝登录成功',
                showCancel: false,
                success: () => {
                    this.mockLogin()
                }
            })
        },

        loginByFace() {
            uni.showModal({
                title: '提示',
                content: '模拟人脸识别登录成功',
                showCancel: false,
                success: () => {
                    this.mockLogin()
                }
            })
        },

        mockLogin() {
            const mockUser = {
                id: 1,
                name: '用户' + Date.now(),
                phone: '13800138000'
            }
            uni.setStorageSync('userInfo', mockUser)
            uni.setStorageSync('token', 'mock_token_' + Date.now())
            uni.setStorageSync('userId', mockUser.id)
            uni.switchTab({
                url: '/pages/index/index'
            })
        }
    }
}
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    background: linear-gradient(180deg, #E8F3FF 0%, #FFFFFF 100%);
    padding: 80rpx 40rpx;
    display: flex;
    flex-direction: column;
}

.login-header {
    text-align: center;
    margin-bottom: 80rpx;
}

.logo {
    width: 120rpx;
    height: 120rpx;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    border-radius: 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 30rpx;
}

.logo-icon {
    font-size: 60rpx;
}

.title {
    display: block;
    font-size: 48rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 16rpx;
}

.subtitle {
    display: block;
    font-size: 28rpx;
    color: #999999;
}

.login-form {
    flex: 1;
}

.form-item {
    margin-bottom: 32rpx;
}

.input-wrapper {
    display: flex;
    align-items: center;
    background: #FFFFFF;
    border-radius: 48rpx;
    padding: 24rpx 32rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 122, 255, 0.1);
}

.code-wrapper {
    justify-content: space-between;
}

.input-icon {
    font-size: 36rpx;
    margin-right: 16rpx;
}

.input {
    flex: 1;
    font-size: 30rpx;
    color: #333333;
}

.code-btn {
    padding: 12rpx 24rpx;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 32rpx;
    font-size: 24rpx;
    margin: 0;
    line-height: 1;
}

.code-btn[disabled] {
    background: #CCCCCC;
}

.login-btn {
    width: 100%;
    background: linear-gradient(135deg, #007AFF 0%, #0056CC 100%);
    color: #FFFFFF;
    border-radius: 48rpx;
    padding: 28rpx;
    font-size: 32rpx;
    font-weight: 500;
    margin-top: 20rpx;
}

.other-login {
    margin-top: 60rpx;
    text-align: center;
}

.other-title {
    display: block;
    font-size: 26rpx;
    color: #999999;
    margin-bottom: 30rpx;
}

.login-methods {
    display: flex;
    justify-content: center;
    gap: 60rpx;
}

.method-item {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.method-icon {
    width: 80rpx;
    height: 80rpx;
    background: #F5F7FA;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 40rpx;
    margin-bottom: 12rpx;
}

.method-text {
    font-size: 24rpx;
    color: #666666;
}

.login-footer {
    text-align: center;
    margin-top: auto;
    padding-top: 40rpx;
}

.agreement {
    font-size: 24rpx;
    color: #999999;
}

.link {
    color: #007AFF;
}
</style>
