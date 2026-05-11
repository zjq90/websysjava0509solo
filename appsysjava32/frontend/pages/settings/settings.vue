<template>
    <view class="settings" :class="{ 'elder-mode': elderMode }">
        <view class="menu-section">
            <view class="menu-list">
                <view class="menu-item">
                    <text class="menu-icon">👴</text>
                    <text class="menu-text">长辈模式</text>
                    <switch 
                        :checked="elderMode" 
                        @change="toggleElderMode" 
                        color="#1890FF"
                    />
                </view>
                <view class="menu-item" @click="showElderHelp">
                    <text class="menu-icon">❓</text>
                    <text class="menu-text">长辈模式说明</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="menu-section" v-if="showPasswordTab">
            <view class="menu-title">账户安全</view>
            <view class="menu-list">
                <view class="menu-item" @click="showPasswordModal = true">
                    <text class="menu-icon">🔐</text>
                    <text class="menu-text">修改密码</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">隐私设置</view>
            <view class="menu-list">
                <view class="menu-item">
                    <text class="menu-icon">🔒</text>
                    <text class="menu-text">数据加密说明</text>
                    <text class="menu-arrow">›</text>
                    @click="showPrivacyInfo"
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">关于</view>
            <view class="menu-list">
                <view class="menu-item" @click="showAbout">
                    <text class="menu-icon">ℹ️</text>
                    <text class="menu-text">关于我们</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item">
                    <text class="menu-icon">📝</text>
                    <text class="menu-text">用户协议</text>
                    <text class="menu-arrow">›</text>
                    @click="showAgreement"
                </view>
                <view class="menu-item">
                    <text class="menu-icon">🔒</text>
                    <text class="menu-text">隐私政策</text>
                    <text class="menu-arrow">›</text>
                    @click="showPrivacy"
                </view>
                <view class="menu-item">
                    <text class="menu-icon">📋</text>
                    <text class="menu-text">版本信息</text>
                    <text class="version-text">v1.0.0</text>
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">其他</view>
            <view class="menu-list">
                <view class="menu-item" @click="clearCache">
                    <text class="menu-icon">🗑️</text>
                    <text class="menu-text">清除缓存</text>
                    <text class="cache-size">{{ cacheSize }}</text>
                </view>
            </view>
        </view>

        <view class="logout-section">
            <button class="btn btn-danger btn-block" @click="logout">退出登录</button>
        </view>

        <view class="password-modal" v-if="showPasswordModal">
            <view class="modal-mask" @click="showPasswordModal = false"></view>
            <view class="modal-content">
                <view class="modal-header">
                    <text class="modal-title">修改密码</text>
                    <text class="modal-close" @click="showPasswordModal = false">×</text>
                </view>
                <view class="modal-body">
                    <view class="form-item">
                        <text class="form-label">原密码</text>
                        <input 
                            class="form-input"
                            v-model="passwordForm.oldPassword"
                            type="password"
                            placeholder="请输入原密码"
                        />
                    </view>
                    <view class="form-item">
                        <text class="form-label">新密码</text>
                        <input 
                            class="form-input"
                            v-model="passwordForm.newPassword"
                            type="password"
                            placeholder="请输入新密码（6-20位）"
                        />
                    </view>
                    <view class="form-item">
                        <text class="form-label">确认新密码</text>
                        <input 
                            class="form-input"
                            v-model="passwordForm.confirmPassword"
                            type="password"
                            placeholder="请再次输入新密码"
                        />
                    </view>
                </view>
                <view class="modal-footer">
                    <button class="btn btn-default" @click="showPasswordModal = false">取消</button>
                    <button class="btn btn-primary" @click="submitPassword">确认修改</button>
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
            cacheSize: '0KB',
            showPasswordModal: false,
            showPasswordTab: true,
            passwordForm: {
                oldPassword: '',
                newPassword: '',
                confirmPassword: ''
            }
        }
    },
    onLoad(options) {
        this.elderMode = uni.getStorageSync('elderMode') === 1
        if (options.tab === 'password') {
            this.showPasswordModal = true
        }
        this.getCacheSize()
    },
    methods: {
        async getCacheSize() {
            try {
                const info = uni.getStorageInfoSync()
                let size = info.currentSize || 0
                if (size < 1024) {
                    this.cacheSize = size + 'KB'
                } else {
                    this.cacheSize = (size / 1024).toFixed(2) + 'MB'
                }
            } catch (e) {
                this.cacheSize = '0KB'
            }
        },

        async toggleElderMode(e) {
            const newValue = e.detail.value
            try {
                util.showLoading('设置中...')
                const res = await api.user.toggleElderMode(newValue ? 1 : 0)
                util.hideLoading()
                if (res.code === 200) {
                    this.elderMode = newValue
                    uni.setStorageSync('elderMode', newValue ? 1 : 0)
                    util.showToast(newValue ? '已开启长辈模式' : '已关闭长辈模式', 'success')
                }
            } catch (e) {
                util.hideLoading()
            }
        },

        showElderHelp() {
            uni.showModal({
                title: '长辈模式说明',
                content: '长辈模式为老年人提供更友好的使用体验：\n\n1. 字体放大：所有文字字号加大，便于阅读\n2. 界面简化：去除非必要元素，重点信息更突出\n3. 操作简化：按钮更大，点击区域更宽\n4. 减少动画：降低视觉负担\n\n开启后，整个应用会自动适配长辈模式。',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        showPrivacyInfo() {
            uni.showModal({
                title: '数据加密说明',
                content: '为保护您的隐私安全，本应用采用国密算法进行数据加密：\n\n• SM2非对称加密：用于身份认证和数字签名\n• SM3哈希算法：用于密码存储和数据完整性校验\n• SM4对称加密：用于手机号、身份证号等敏感数据加密\n\n所有敏感数据在传输和存储过程中均进行加密处理，确保您的个人信息安全。',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        showAbout() {
            uni.showModal({
                title: '关于我们',
                content: '医院住院服务APP\n版本：v1.0.0\n\n本应用为患者提供便捷的住院服务，包括入院申请、费用查询、押金补缴等功能。\n\n技术支持：医院信息中心\n客服热线：400-123-4567\n\n© 2024 医院信息中心 版权所有',
                showCancel: false,
                confirmText: '我知道了'
            })
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
                content: '我们深知个人信息对您的重要性，将按法律法规要求采取相应安全保护措施，保护您的个人信息安全可控。\n\n一、信息收集\n我们仅收集必要的个人信息用于提供服务，包括手机号、身份证号等。\n\n二、信息使用\n收集的信息仅用于提供服务、改善用户体验。\n\n三、信息存储\n所有敏感数据采用国密算法加密存储，保存期限不超过法律法规要求的期限。\n\n四、信息保护\n我们采用业界领先的安全技术保护您的信息安全。\n\n如有疑问，请联系客服：400-123-4567',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        async clearCache() {
            const confirmed = await util.confirm('确定要清除缓存吗？清除后需要重新登录。')
            if (!confirmed) return

            try {
                uni.clearStorageSync()
                util.showToast('清除成功', 'success')
                this.cacheSize = '0KB'
                
                setTimeout(() => {
                    uni.reLaunch({ url: '/pages/login/login' })
                }, 1500)
            } catch (e) {
                util.showToast('清除失败', 'none')
            }
        },

        async submitPassword() {
            if (!this.passwordForm.oldPassword) {
                util.showToast('请输入原密码', 'none')
                return
            }
            if (!this.passwordForm.newPassword) {
                util.showToast('请输入新密码', 'none')
                return
            }
            if (this.passwordForm.newPassword.length < 6 || this.passwordForm.newPassword.length > 20) {
                util.showToast('密码长度应为6-20位', 'none')
                return
            }
            if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
                util.showToast('两次输入的密码不一致', 'none')
                return
            }

            try {
                util.showLoading('修改中...')
                const res = await api.user.changePassword({
                    oldPassword: this.passwordForm.oldPassword,
                    newPassword: this.passwordForm.newPassword
                })
                util.hideLoading()
                if (res.code === 200) {
                    util.showToast('修改成功，请重新登录', 'success')
                    this.showPasswordModal = false
                    setTimeout(() => {
                        uni.removeStorageSync('token')
                        uni.reLaunch({ url: '/pages/login/login' })
                    }, 1500)
                } else {
                    util.showToast(res.message || '修改失败', 'none')
                }
            } catch (e) {
                util.hideLoading()
            }
        },

        async logout() {
            const confirmed = await util.confirm('确定要退出登录吗？')
            if (!confirmed) return

            try {
                util.showLoading('退出中...')
                await api.auth.logout()
                util.hideLoading()
                
                uni.removeStorageSync('token')
                uni.removeStorageSync('userInfo')
                uni.removeStorageSync('elderMode')
                
                uni.reLaunch({ url: '/pages/login/login' })
            } catch (e) {
                util.hideLoading()
                uni.removeStorageSync('token')
                uni.reLaunch({ url: '/pages/login/login' })
            }
        }
    }
}
</script>

<style scoped>
.settings {
    min-height: 100vh;
    background: #F5F5F5;
    padding-bottom: 40rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.menu-section {
    background: #fff;
    margin-bottom: 20rpx;
}

.menu-title {
    font-size: 26rpx;
    color: #999;
    padding: 20rpx 28rpx 12rpx;
}

.elder-mode .menu-title {
    font-size: 30rpx;
}

.menu-list {
    
}

.menu-item {
    display: flex;
    align-items: center;
    padding: 28rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.menu-item:last-child {
    border-bottom: none;
}

.menu-icon {
    font-size: 40rpx;
    margin-right: 20rpx;
}

.elder-mode .menu-icon {
    font-size: 48rpx;
}

.menu-text {
    flex: 1;
    font-size: 30rpx;
    color: #333;
}

.elder-mode .menu-text {
    font-size: 34rpx;
}

.menu-arrow {
    font-size: 36rpx;
    color: #ccc;
}

.version-text {
    font-size: 26rpx;
    color: #999;
}

.elder-mode .version-text {
    font-size: 30rpx;
}

.cache-size {
    font-size: 26rpx;
    color: #999;
}

.elder-mode .cache-size {
    font-size: 30rpx;
}

.logout-section {
    padding: 40rpx 20rpx 20rpx;
}

.password-modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
}

.modal-mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
}

.modal-content {
    width: 600rpx;
    background: #fff;
    border-radius: 20rpx;
    position: relative;
    z-index: 1;
    overflow: hidden;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx;
    border-bottom: 1rpx solid #F5F5F5;
}

.modal-title {
    font-size: 34rpx;
    font-weight: 600;
    color: #333;
}

.elder-mode .modal-title {
    font-size: 38rpx;
}

.modal-close {
    font-size: 48rpx;
    color: #999;
    line-height: 1;
}

.modal-body {
    padding: 32rpx;
}

.form-item {
    margin-bottom: 28rpx;
}

.form-item:last-child {
    margin-bottom: 0;
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

.form-input {
    width: 100%;
    height: 88rpx;
    padding: 0 24rpx;
    background: #f5f5f5;
    border-radius: 12rpx;
    font-size: 30rpx;
    color: #333;
    box-sizing: border-box;
}

.elder-mode .form-input {
    height: 100rpx;
    font-size: 34rpx;
}

.modal-footer {
    display: flex;
    border-top: 1rpx solid #F5F5F5;
}

.modal-footer .btn {
    flex: 1;
    height: 100rpx;
    border-radius: 0;
    font-size: 32rpx;
}

.elder-mode .modal-footer .btn {
    height: 120rpx;
    font-size: 36rpx;
}

.modal-footer .btn:first-child {
    border-right: 1rpx solid #F5F5F5;
}
</style>
