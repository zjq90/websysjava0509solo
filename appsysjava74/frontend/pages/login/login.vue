<template>
    <view class="login-container">
        <view class="login-header">
            <image class="logo" src="/static/logo.png" mode="aspectFit"></image>
            <view class="title">社团管理系统</view>
            <view class="subtitle">大学生社团活动管理平台</view>
        </view>
        
        <view class="login-form">
            <u--form :model="form" label-width="0">
                <u--form-item>
                    <u--input 
                        v-model="form.username" 
                        placeholder="请输入用户名" 
                        prefix-icon="account"
                        :border="false"
                        shape="square"
                    ></u--input>
                </u--form-item>
                <u--form-item>
                    <u--input 
                        v-model="form.password" 
                        placeholder="请输入密码" 
                        prefix-icon="lock"
                        :border="false"
                        shape="square"
                        :password="true"
                        :password-icon="true"
                    ></u--input>
                </u--form-item>
            </u--form>
            
            <u-button 
                type="primary" 
                size="large"
                :loading="loading"
                loading-text="登录中..."
                @click="handleLogin"
                custom-style="margin-top: 40rpx;"
            >
                登 录
            </u-button>
            
            <view class="tips">
                <text>测试账号：</text>
                <text class="account">admin/123456</text>
                <text> 或 </text>
                <text class="account">clubadmin1/123456</text>
                <text> 或 </text>
                <text class="account">student1/123456</text>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref } from 'vue'
import { useStore } from 'vuex'

const store = useStore()
const loading = ref(false)

const form = ref({
    username: '',
    password: ''
})

const handleLogin = async () => {
    if (!form.value.username) {
        uni.showToast({ title: '请输入用户名', icon: 'none' })
        return
    }
    if (!form.value.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
    }
    
    loading.value = true
    try {
        await store.dispatch('login', form.value)
        uni.showToast({ title: '登录成功', icon: 'success' })
        setTimeout(() => {
            uni.switchTab({
                url: '/pages/index/index'
            })
        }, 1500)
    } catch (e) {
        console.error('登录失败:', e)
    } finally {
        loading.value = false
    }
}
</script>

<style lang="scss" scoped>
.login-container {
    min-height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 120rpx 60rpx 0;
}

.login-header {
    text-align: center;
    margin-bottom: 80rpx;
    
    .logo {
        width: 160rpx;
        height: 160rpx;
        border-radius: 32rpx;
        background: rgba(255, 255, 255, 0.2);
        padding: 20rpx;
    }
    
    .title {
        margin-top: 30rpx;
        font-size: 48rpx;
        font-weight: bold;
        color: #fff;
    }
    
    .subtitle {
        margin-top: 16rpx;
        font-size: 28rpx;
        color: rgba(255, 255, 255, 0.8);
    }
}

.login-form {
    background: #fff;
    border-radius: 24rpx;
    padding: 60rpx 40rpx;
    box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1);
}

.tips {
    margin-top: 40rpx;
    text-align: center;
    font-size: 24rpx;
    color: #999;
    line-height: 1.8;
    
    .account {
        color: #3c9cff;
    }
}
</style>
