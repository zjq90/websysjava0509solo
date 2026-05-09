<template>
    <view class="login-container">
        <view class="header">
            <view class="logo">
                <text class="logo-icon">🌱</text>
            </view>
            <text class="app-name">田间数据采集</text>
            <text class="app-desc">农业育种试验数据采集系统</text>
        </view>

        <view class="form-container">
            <view class="form-item">
                <text class="form-label">用户名</text>
                <input 
                    class="form-input" 
                    type="text" 
                    v-model="formData.username" 
                    placeholder="请输入用户名"
                    placeholder-class="placeholder"
                />
            </view>

            <view class="form-item">
                <text class="form-label">密码</text>
                <input 
                    class="form-input" 
                    type="password" 
                    v-model="formData.password" 
                    placeholder="请输入密码"
                    placeholder-class="placeholder"
                />
            </view>

            <view class="form-item checkbox-item">
                <checkbox 
                    :checked="rememberPassword" 
                    @change="onRememberPassword"
                    color="#4CAF50"
                />
                <text class="checkbox-label">记住密码</text>
            </view>

            <view class="btn-primary login-btn" @click="handleLogin">
                登 录
            </view>

            <view class="tips">
                <text>默认账号：admin / 123456</text>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            formData: {
                username: '',
                password: ''
            },
            rememberPassword: false
        }
    },

    onLoad() {
        const savedUsername = uni.getStorageSync('savedUsername')
        const savedPassword = uni.getStorageSync('savedPassword')
        if (savedUsername && savedPassword) {
            this.formData.username = savedUsername
            this.formData.password = savedPassword
            this.rememberPassword = true
        }
    },

    methods: {
        onRememberPassword(e) {
            this.rememberPassword = e.detail.value.length > 0
        },

        async handleLogin() {
            if (!this.formData.username || !this.formData.password) {
                uni.showToast({
                    title: '请输入用户名和密码',
                    icon: 'none'
                })
                return
            }

            try {
                uni.showLoading({ title: '登录中...' })
                
                const res = await this.$request({
                    url: '/auth/login',
                    method: 'POST',
                    data: {
                        username: this.formData.username,
                        password: this.formData.password
                    }
                })

                uni.hideLoading()

                if (res) {
                    if (this.rememberPassword) {
                        uni.setStorageSync('savedUsername', this.formData.username)
                        uni.setStorageSync('savedPassword', this.formData.password)
                    } else {
                        uni.removeStorageSync('savedUsername')
                        uni.removeStorageSync('savedPassword')
                    }

                    this.$store.dispatch('login', {
                        userInfo: res,
                        token: 'token_' + res.userId
                    })

                    uni.showToast({
                        title: '登录成功',
                        icon: 'success'
                    })

                    setTimeout(() => {
                        uni.switchTab({ url: '/pages/index/index' })
                    }, 1000)
                }
            } catch (e) {
                uni.hideLoading()
            }
        }
    }
}
</script>

<style>
.login-container {
    min-height: 100vh;
    background: linear-gradient(180deg, #4CAF50 0%, #2E7D32 100%);
    padding: 0;
}

.header {
    padding: 120rpx 0 80rpx 0;
    text-align: center;
}

.logo {
    width: 160rpx;
    height: 160rpx;
    background: rgba(255, 255, 255, 0.2);
    border-radius: 50%;
    margin: 0 auto 30rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.logo-icon {
    font-size: 80rpx;
}

.app-name {
    font-size: 44rpx;
    font-weight: bold;
    color: #fff;
    display: block;
    margin-bottom: 16rpx;
}

.app-desc {
    font-size: 28rpx;
    color: rgba(255, 255, 255, 0.8);
}

.form-container {
    background-color: #fff;
    border-radius: 24rpx 24rpx 0 0;
    padding: 60rpx 40rpx;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    min-height: 50vh;
}

.form-item {
    margin-bottom: 40rpx;
}

.form-label {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 16rpx;
    display: block;
}

.form-input {
    background-color: #f5f5f5;
    border-radius: 12rpx;
    height: 96rpx;
    padding: 0 24rpx;
    font-size: 30rpx;
}

.placeholder {
    color: #999;
}

.checkbox-item {
    display: flex;
    align-items: center;
}

.checkbox-label {
    font-size: 26rpx;
    color: #666;
    margin-left: 12rpx;
}

.login-btn {
    margin-top: 40rpx;
    height: 96rpx;
    line-height: 96rpx;
    font-size: 32rpx;
    font-weight: bold;
}

.tips {
    text-align: center;
    margin-top: 30rpx;
    font-size: 24rpx;
    color: #999;
}
</style>
