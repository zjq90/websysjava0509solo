<template>
    <view class="register" :class="{ 'elder-mode': elderMode }">
        <view class="header">
            <view class="back-btn" @click="goBack">
                <text class="back-icon">‹</text>
            </view>
            <text class="title">用户注册</text>
            <view class="placeholder"></view>
        </view>

        <view class="logo-section">
            <view class="logo">🏥</view>
            <text class="app-name">医院住院服务</text>
            <text class="app-desc">便捷的住院服务平台</text>
        </view>

        <view class="form-section">
            <view class="form-item">
                <text class="form-label">用户名</text>
                <view class="form-input-wrapper">
                    <text class="input-icon">👤</text>
                    <input 
                        class="form-input"
                        v-model="form.username"
                        placeholder="请输入用户名（4-20位）"
                        maxlength="20"
                    />
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">手机号</text>
                <view class="form-input-wrapper">
                    <text class="input-icon">📱</text>
                    <input 
                        class="form-input"
                        v-model="form.phone"
                        type="number"
                        placeholder="请输入手机号"
                        maxlength="11"
                    />
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">验证码</text>
                <view class="form-input-wrapper has-suffix">
                    <text class="input-icon">🔐</text>
                    <input 
                        class="form-input"
                        v-model="form.code"
                        type="number"
                        placeholder="请输入验证码"
                        maxlength="6"
                    />
                    <button 
                        class="code-btn"
                        :disabled="countdown > 0"
                        :class="{ disabled: countdown > 0 }"
                        @click="sendCode"
                    >
                        {{ countdown > 0 ? countdown + 's' : '获取验证码' }}
                    </button>
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">密码</text>
                <view class="form-input-wrapper">
                    <text class="input-icon">🔒</text>
                    <input 
                        class="form-input"
                        v-model="form.password"
                        type="password"
                        placeholder="请输入密码（6-20位）"
                        maxlength="20"
                    />
                    <text class="toggle-pwd" @click="showPwd1 = !showPwd1">
                        {{ showPwd1 ? '👁️' : '🙈' }}
                    </text>
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">确认密码</text>
                <view class="form-input-wrapper">
                    <text class="input-icon">🔒</text>
                    <input 
                        class="form-input"
                        v-model="form.confirmPassword"
                        type="password"
                        placeholder="请再次输入密码"
                        maxlength="20"
                    />
                    <text class="toggle-pwd" @click="showPwd2 = !showPwd2">
                        {{ showPwd2 ? '👁️' : '🙈' }}
                    </text>
                </view>
            </view>

            <view class="form-item">
                <text class="form-label">身份证号</text>
                <view class="form-input-wrapper">
                    <text class="input-icon">🆔</text>
                    <input 
                        class="form-input"
                        v-model="form.idCard"
                        placeholder="请输入身份证号（选填）"
                        maxlength="18"
                    />
                </view>
            </view>

            <view class="agreement">
                <checkbox :checked="agreed" @change="agreed = !agreed" color="#1890FF" />
                <text class="agreement-text">我已阅读并同意</text>
                <text class="agreement-link" @click="showAgreement">《用户协议》</text>
                <text class="agreement-text">和</text>
                <text class="agreement-link" @click="showPrivacy">《隐私政策》</text>
            </view>

            <button 
                class="btn btn-primary btn-block btn-large submit-btn"
                :disabled="!canSubmit"
                :class="{ disabled: !canSubmit }"
                @click="submitRegister"
            >
                注册
            </button>

            <view class="login-link">
                <text class="link-text">已有账号？</text>
                <text class="link-btn" @click="goToLogin">立即登录</text>
            </view>
        </view>

        <view class="test-accounts" v-if="showTest">
            <text class="test-title">测试账号（开发环境）</text>
            <view class="test-list">
                <view class="test-item" @click="fillTest('patient')">
                    <text class="test-role">患者账号</text>
                    <text class="test-account">patient1 / 123456</text>
                </view>
                <view class="test-item" @click="fillTest('doctor')">
                    <text class="test-role">医生账号</text>
                    <text class="test-account">doctor1 / 123456</text>
                </view>
                <view class="test-item" @click="fillTest('admin')">
                    <text class="test-role">管理员账号</text>
                    <text class="test-account">admin / 123456</text>
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
            elderMode: false,
            showTest: true,
            agreed: false,
            countdown: 0,
            showPwd1: false,
            showPwd2: false,
            form: {
                username: '',
                phone: '',
                code: '',
                password: '',
                confirmPassword: '',
                idCard: ''
            }
        }
    },
    computed: {
        canSubmit() {
            return this.form.username && 
                   this.form.phone && 
                   this.form.code && 
                   this.form.password && 
                   this.form.confirmPassword &&
                   this.agreed
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
    },
    methods: {
        goBack() {
            uni.navigateBack()
        },

        goToLogin() {
            uni.navigateBack()
        },

        sendCode() {
            if (!this.form.phone) {
                util.showToast('请输入手机号', 'none')
                return
            }
            if (!/^1[3-9]\d{9}$/.test(this.form.phone)) {
                util.showToast('手机号格式不正确', 'none')
                return
            }

            this.countdown = 60
            const timer = setInterval(() => {
                this.countdown--
                if (this.countdown <= 0) {
                    clearInterval(timer)
                }
            }, 1000)

            util.showToast('验证码已发送（测试：123456）', 'none')
        },

        showAgreement() {
            uni.showModal({
                title: '用户协议',
                content: '一、服务说明\n本应用为患者提供住院相关服务，包括入院申请、费用查询、押金补缴等功能。\n\n二、用户责任\n用户应妥善保管账号密码，因账号泄露造成的损失由用户承担。\n\n三、隐私保护\n我们重视用户隐私，所有敏感数据均采用国密算法加密存储。\n\n四、服务变更\n我们保留随时修改或中断服务的权利。\n\n五、法律适用\n本协议受中华人民共和国法律管辖。',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        showPrivacy() {
            uni.showModal({
                title: '隐私政策',
                content: '我们深知个人信息对您的重要性，将按法律法规要求采取相应安全保护措施，保护您的个人信息安全可控。\n\n一、信息收集\n我们仅收集必要的个人信息用于提供服务，包括手机号、身份证号等。\n\n二、信息使用\n收集的信息仅用于提供服务、改善用户体验。\n\n三、信息存储\n所有敏感数据采用国密算法加密存储。\n\n四、信息保护\n我们采用业界领先的安全技术保护您的信息安全。',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        async submitRegister() {
            if (!this.form.username) {
                util.showToast('请输入用户名', 'none')
                return
            }
            if (this.form.username.length < 4) {
                util.showToast('用户名至少4位', 'none')
                return
            }
            if (!this.form.phone) {
                util.showToast('请输入手机号', 'none')
                return
            }
            if (!/^1[3-9]\d{9}$/.test(this.form.phone)) {
                util.showToast('手机号格式不正确', 'none')
                return
            }
            if (!this.form.code) {
                util.showToast('请输入验证码', 'none')
                return
            }
            if (!this.form.password) {
                util.showToast('请输入密码', 'none')
                return
            }
            if (this.form.password.length < 6 || this.form.password.length > 20) {
                util.showToast('密码长度应为6-20位', 'none')
                return
            }
            if (this.form.password !== this.form.confirmPassword) {
                util.showToast('两次输入的密码不一致', 'none')
                return
            }
            if (!this.agreed) {
                util.showToast('请先阅读并同意用户协议', 'none')
                return
            }

            try {
                util.showLoading('注册中...')
                const res = await api.auth.register({
                    username: this.form.username,
                    phone: this.form.phone,
                    code: this.form.code,
                    password: this.form.password,
                    idCard: this.form.idCard
                })
                util.hideLoading()

                if (res.code === 200) {
                    util.showToast('注册成功，请登录', 'success')
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1500)
                } else {
                    util.showToast(res.message || '注册失败', 'none')
                }
            } catch (e) {
                util.hideLoading()
            }
        },

        fillTest(type) {
            const users = {
                patient: { username: 'patient1', password: '123456' },
                doctor: { username: 'doctor1', password: '123456' },
                admin: { username: 'admin', password: '123456' }
            }
            const user = users[type]
            this.form.username = user.username
            this.form.phone = '13800138000'
            this.form.code = '123456'
            this.form.password = user.password
            this.form.confirmPassword = user.password
            util.showToast('已填充测试账号', 'none')
        }
    }
}
</script>

<style scoped>
.register {
    min-height: 100vh;
    background: linear-gradient(180deg, #E6F7FF 0%, #F5F5F5 100%);
}

.elder-mode {
    font-size: 36rpx;
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 80rpx 32rpx 20rpx;
}

.back-btn {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.back-icon {
    font-size: 60rpx;
    color: #333;
}

.title {
    font-size: 36rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .title {
    font-size: 40rpx;
}

.placeholder {
    width: 60rpx;
}

.logo-section {
    text-align: center;
    padding: 40rpx 0 60rpx;
}

.logo {
    font-size: 120rpx;
    display: block;
}

.elder-mode .logo {
    font-size: 140rpx;
}

.app-name {
    font-size: 40rpx;
    font-weight: 700;
    color: #1890FF;
    margin-top: 20rpx;
    display: block;
}

.elder-mode .app-name {
    font-size: 48rpx;
}

.app-desc {
    font-size: 26rpx;
    color: #999;
    margin-top: 12rpx;
    display: block;
}

.elder-mode .app-desc {
    font-size: 30rpx;
}

.form-section {
    padding: 0 40rpx;
}

.form-item {
    margin-bottom: 32rpx;
}

.form-label {
    display: block;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 12rpx;
}

.elder-mode .form-label {
    font-size: 32rpx;
}

.form-input-wrapper {
    display: flex;
    align-items: center;
    background: #fff;
    border-radius: 16rpx;
    padding: 0 24rpx;
    height: 100rpx;
    border: 2rpx solid #E8E8E8;
}

.form-input-wrapper.has-suffix {
    padding-right: 0;
}

.input-icon {
    font-size: 36rpx;
    margin-right: 16rpx;
}

.elder-mode .input-icon {
    font-size: 40rpx;
}

.form-input {
    flex: 1;
    height: 100rpx;
    font-size: 30rpx;
    color: #333;
}

.elder-mode .form-input {
    height: 110rpx;
    font-size: 34rpx;
}

.toggle-pwd {
    font-size: 36rpx;
    padding: 0 12rpx;
}

.code-btn {
    height: 100rpx;
    padding: 0 28rpx;
    background: #1890FF;
    color: #fff;
    font-size: 26rpx;
    border-radius: 0 16rpx 16rpx 0;
    margin-left: 16rpx;
}

.elder-mode .code-btn {
    font-size: 30rpx;
    padding: 0 32rpx;
}

.code-btn.disabled {
    background: #ccc;
}

.agreement {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    margin-bottom: 40rpx;
}

.agreement-text {
    font-size: 24rpx;
    color: #666;
    margin-left: 8rpx;
}

.elder-mode .agreement-text {
    font-size: 28rpx;
}

.agreement-link {
    font-size: 24rpx;
    color: #1890FF;
}

.elder-mode .agreement-link {
    font-size: 28rpx;
}

.submit-btn {
    margin-bottom: 32rpx;
}

.submit-btn.disabled {
    opacity: 0.5;
}

.login-link {
    text-align: center;
}

.link-text {
    font-size: 28rpx;
    color: #666;
}

.elder-mode .link-text {
    font-size: 32rpx;
}

.link-btn {
    font-size: 28rpx;
    color: #1890FF;
    font-weight: 600;
}

.elder-mode .link-btn {
    font-size: 32rpx;
}

.test-accounts {
    margin: 60rpx 40rpx 40rpx;
    background: rgba(24, 144, 255, 0.1);
    border-radius: 16rpx;
    padding: 24rpx;
}

.test-title {
    font-size: 24rpx;
    color: #1890FF;
    margin-bottom: 16rpx;
    display: block;
}

.elder-mode .test-title {
    font-size: 28rpx;
}

.test-list {
    
}

.test-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 0;
    border-bottom: 1rpx solid rgba(24, 144, 255, 0.2);
}

.test-item:last-child {
    border-bottom: none;
}

.test-role {
    font-size: 26rpx;
    color: #333;
}

.elder-mode .test-role {
    font-size: 30rpx;
}

.test-account {
    font-size: 24rpx;
    color: #1890FF;
}

.elder-mode .test-account {
    font-size: 28rpx;
}
</style>
