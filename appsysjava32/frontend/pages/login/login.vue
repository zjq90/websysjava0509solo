<template>
    <view class="login-container" :class="{ 'elder-mode': elderMode }">
        <view class="login-header">
            <view class="logo">
                <text class="logo-icon">🏥</text>
            </view>
            <text class="app-title">医院住院服务</text>
            <text class="app-subtitle">住院服务延伸功能系统</text>
        </view>
        
        <view class="login-form">
            <view class="form-item">
                <view class="input-wrapper">
                    <text class="input-icon">👤</text>
                    <input 
                        class="form-input" 
                        type="text" 
                        v-model="form.username" 
                        placeholder="请输入用户名"
                        :placeholder-style="elderMode ? 'font-size: 32rpx;' : ''"
                    />
                </view>
            </view>
            
            <view class="form-item">
                <view class="input-wrapper">
                    <text class="input-icon">🔒</text>
                    <input 
                        class="form-input" 
                        type="password" 
                        v-model="form.password" 
                        placeholder="请输入密码"
                        :placeholder-style="elderMode ? 'font-size: 32rpx;' : ''"
                    />
                </view>
            </view>
            
            <button 
                class="btn btn-primary btn-block btn-large mt-30" 
                :class="{ 'is-loading': loading }"
                @click="handleLogin"
                :disabled="loading"
            >
                {{ loading ? '登录中...' : '登 录' }}
            </button>
            
            <view class="login-actions">
                <text class="link-text" @click="goToRegister">注册账号</text>
                <text class="link-text" @click="toggleElderMode">
                    {{ elderMode ? '👓 关闭长辈模式' : '👵 长辈模式' }}
                </text>
            </view>
        </view>
        
        <view class="demo-accounts">
            <text class="demo-title">测试账号：</text>
            <view class="demo-list">
                <view class="demo-item" @click="fillForm('patient1', '123456')">
                    <text class="demo-role">患者</text>
                    <text class="demo-account">patient1 / 123456</text>
                </view>
                <view class="demo-item" @click="fillForm('doctor1', '123456')">
                    <text class="demo-role">医生</text>
                    <text class="demo-account">doctor1 / 123456</text>
                </view>
                <view class="demo-item" @click="fillForm('admin', '123456')">
                    <text class="demo-role">管理员</text>
                    <text class="demo-account">admin / 123456</text>
                </view>
            </view>
        </view>
    </view>
</template>

<script>
import api from '@/common/js/api.js'
import util from '@/common/js/util.js'

export default {
    data() {
        return {
            form: {
                username: '',
                password: ''
            },
            loading: false,
            elderMode: false
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
    },
    methods: {
        async handleLogin() {
            if (!this.form.username || !this.form.password) {
                util.showToast('请输入用户名和密码')
                return
            }
            
            this.loading = true
            try {
                const res = await api.auth.login(this.form)
                if (res.code === 200) {
                    util.showToast('登录成功', 'success')
                    setTimeout(() => {
                        uni.switchTab({ url: '/pages/index/index' })
                    }, 1000)
                }
            } catch (e) {
                console.error('登录失败:', e)
            } finally {
                this.loading = false
            }
        },
        
        fillForm(username, password) {
            this.form.username = username
            this.form.password = password
        },
        
        goToRegister() {
            uni.navigateTo({ url: '/pages/register/register' })
        },
        
        toggleElderMode() {
            this.elderMode = !this.elderMode
            uni.setStorageSync('elderMode', this.elderMode ? 1 : 0)
            util.showToast(this.elderMode ? '已开启长辈模式' : '已关闭长辈模式')
        }
    }
}
</script>

<style scoped>
.login-container {
    min-height: 100vh;
    background: linear-gradient(180deg, #1890FF 0%, #1890FF 35%, #F5F5F5 35%);
    padding: 0 40rpx;
    box-sizing: border-box;
}

.elder-mode {
    font-size: 36rpx;
}

.login-header {
    padding-top: 100rpx;
    text-align: center;
}

.logo {
    width: 160rpx;
    height: 160rpx;
    background: #fff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 30rpx;
    box-shadow: 0 8rpx 30rpx rgba(24, 144, 255, 0.3);
}

.logo-icon {
    font-size: 80rpx;
}

.app-title {
    font-size: 48rpx;
    font-weight: bold;
    color: #fff;
    display: block;
}

.app-subtitle {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.8);
    margin-top: 12rpx;
    display: block;
}

.elder-mode .app-title {
    font-size: 56rpx;
}

.elder-mode .app-subtitle {
    font-size: 32rpx;
}

.login-form {
    margin-top: 60rpx;
    padding: 40rpx;
    background: #fff;
    border-radius: 24rpx;
    box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
}

.input-wrapper {
    position: relative;
    display: flex;
    align-items: center;
}

.input-icon {
    position: absolute;
    left: 24rpx;
    font-size: 40rpx;
    z-index: 1;
}

.form-input {
    width: 100%;
    height: 96rpx;
    padding: 0 24rpx 0 80rpx;
    background: #F8F9FA;
    border-radius: 16rpx;
    font-size: 28rpx;
}

.elder-mode .form-input {
    height: 110rpx;
    font-size: 36rpx;
}

.login-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 30rpx;
}

.link-text {
    color: #1890FF;
    font-size: 26rpx;
}

.elder-mode .link-text {
    font-size: 30rpx;
}

.demo-accounts {
    margin-top: 40rpx;
    padding: 30rpx;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16rpx;
}

.demo-title {
    font-size: 24rpx;
    color: #666;
    margin-bottom: 20rpx;
    display: block;
}

.demo-list {
    display: flex;
    flex-wrap: wrap;
    gap: 16rpx;
}

.demo-item {
    flex: 1;
    min-width: 200rpx;
    padding: 16rpx;
    background: #F0F5FF;
    border-radius: 12rpx;
    text-align: center;
}

.demo-role {
    display: block;
    font-size: 22rpx;
    color: #1890FF;
    margin-bottom: 8rpx;
}

.demo-account {
    font-size: 22rpx;
    color: #666;
}

.is-loading {
    opacity: 0.7;
}
</style>
