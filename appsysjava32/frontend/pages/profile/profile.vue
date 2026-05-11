<template>
    <view class="profile" :class="{ 'elder-mode': elderMode }">
        <view class="profile-header">
            <view class="user-info" @click="editProfile">
                <view class="avatar">
                    <text class="avatar-text">{{ getAvatarText() }}</text>
                </view>
                <view class="user-detail">
                    <text class="user-name">{{ userInfo.nickname || userInfo.username || '未设置昵称' }}</text>
                    <text class="user-phone">{{ maskPhone(userInfo.phone) }}</text>
                </view>
                <text class="arrow">›</text>
            </view>
            <view class="elder-switch">
                <text class="switch-label">长辈模式</text>
                <switch 
                    :checked="elderMode" 
                    @change="toggleElderMode" 
                    color="#1890FF"
                />
            </view>
        </view>

        <view class="quick-actions">
            <view class="action-item" @click="goToAdmission">
                <text class="action-icon">📋</text>
                <text class="action-text">入院申请</text>
                <text class="action-badge" v-if="stats.pending > 0">{{ stats.pending }}</text>
            </view>
            <view class="action-item" @click="goToFee">
                <text class="action-icon">💰</text>
                <text class="action-text">费用查询</text>
            </view>
            <view class="action-item" @click="goToDeposit">
                <text class="action-icon">💳</text>
                <text class="action-text">押金补缴</text>
            </view>
            <view class="action-item" @click="goToDepositList">
                <text class="action-icon">📝</text>
                <text class="action-text">押金记录</text>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">我的服务</view>
            <view class="menu-list">
                <view class="menu-item" @click="goToAdmission">
                    <text class="menu-icon">📋</text>
                    <text class="menu-text">我的入院申请</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToFee">
                    <text class="menu-icon">💰</text>
                    <text class="menu-text">住院费用查询</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToDeposit">
                    <text class="menu-icon">💳</text>
                    <text class="menu-text">押金补缴</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">账户设置</view>
            <view class="menu-list">
                <view class="menu-item" @click="editProfile">
                    <text class="menu-icon">👤</text>
                    <text class="menu-text">个人资料</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="changePassword">
                    <text class="menu-icon">🔐</text>
                    <text class="menu-text">修改密码</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="goToSettings">
                    <text class="menu-icon">⚙️</text>
                    <text class="menu-text">系统设置</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="menu-section">
            <view class="menu-title">帮助与反馈</view>
            <view class="menu-list">
                <view class="menu-item" @click="showHelp">
                    <text class="menu-icon">❓</text>
                    <text class="menu-text">帮助中心</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="callService">
                    <text class="menu-icon">📞</text>
                    <text class="menu-text">联系客服</text>
                    <text class="menu-sub">400-123-4567</text>
                    <text class="menu-arrow">›</text>
                </view>
                <view class="menu-item" @click="showAbout">
                    <text class="menu-icon">ℹ️</text>
                    <text class="menu-text">关于我们</text>
                    <text class="menu-arrow">›</text>
                </view>
            </view>
        </view>

        <view class="logout-section">
            <button class="btn btn-danger btn-block" @click="logout">退出登录</button>
        </view>

        <view class="version-info">
            <text>医院住院服务 v1.0.0</text>
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
            userInfo: {},
            stats: {
                pending: 0,
                total: 0
            }
        }
    },
    onLoad() {
        this.elderMode = uni.getStorageSync('elderMode') === 1
    },
    onShow() {
        this.loadUserInfo()
        this.loadStats()
    },
    methods: {
        async loadUserInfo() {
            try {
                const res = await api.auth.getCurrentUser()
                if (res.code === 200) {
                    this.userInfo = res.data
                }
            } catch (e) {
                console.error('加载用户信息失败:', e)
            }
        },

        async loadStats() {
            try {
                const res = await api.admission.stats()
                if (res.code === 200) {
                    this.stats = res.data
                }
            } catch (e) {
                console.error('加载统计失败:', e)
            }
        },

        getAvatarText() {
            const name = this.userInfo.nickname || this.userInfo.username || '用'
            return name.charAt(0).toUpperCase()
        },

        maskPhone(phone) {
            if (!phone) return ''
            phone = phone.toString()
            if (phone.length >= 11) {
                return phone.substring(0, 3) + '****' + phone.substring(7)
            }
            return phone
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

        editProfile() {
            uni.navigateTo({ url: '/pages/profile/edit' })
        },

        changePassword() {
            uni.navigateTo({ url: '/pages/settings/settings?tab=password' })
        },

        goToSettings() {
            uni.navigateTo({ url: '/pages/settings/settings' })
        },

        goToAdmission() {
            uni.switchTab({ url: '/pages/admission/list' })
        },

        goToFee() {
            uni.switchTab({ url: '/pages/fee/list' })
        },

        goToDeposit() {
            uni.navigateTo({ url: '/pages/deposit/pay' })
        },

        goToDepositList() {
            uni.navigateTo({ url: '/pages/deposit/list' })
        },

        showHelp() {
            uni.showModal({
                title: '帮助中心',
                content: '常见问题：\n1. 如何提交入院申请？\n   首页点击"入院预约"或在"入院"页面点击"+"按钮提交申请。\n\n2. 如何查看费用？\n   点击底部"费用"标签页即可查看所有费用记录。\n\n3. 长辈模式有什么用？\n   长辈模式会放大字体、简化界面，方便老年人使用。\n\n如有其他问题，请联系客服。',
                showCancel: false,
                confirmText: '我知道了'
            })
        },

        callService() {
            uni.showModal({
                title: '联系客服',
                content: '客服热线：400-123-4567\n服务时间：8:00-22:00',
                confirmText: '拨打',
                success: (res) => {
                    if (res.confirm) {
                        uni.makePhoneCall({
                            phoneNumber: '4001234567'
                        })
                    }
                }
            })
        },

        showAbout() {
            uni.showModal({
                title: '关于我们',
                content: '医院住院服务APP\n版本：v1.0.0\n\n本应用为患者提供便捷的住院服务，包括入院申请、费用查询、押金补缴等功能，让您的住院体验更加便捷。\n\n© 2024 医院信息中心',
                showCancel: false,
                confirmText: '我知道了'
            })
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
.profile {
    min-height: 100vh;
    background: #F5F5F5;
    padding-bottom: 40rpx;
}

.elder-mode {
    font-size: 36rpx;
}

.profile-header {
    background: linear-gradient(135deg, #1890FF 0%, #096DD9 100%);
    padding: 40rpx 32rpx;
    color: #fff;
}

.user-info {
    display: flex;
    align-items: center;
    margin-bottom: 32rpx;
}

.avatar {
    width: 120rpx;
    height: 120rpx;
    background: rgba(255, 255, 255, 0.3);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 24rpx;
}

.elder-mode .avatar {
    width: 140rpx;
    height: 140rpx;
}

.avatar-text {
    font-size: 48rpx;
    font-weight: 600;
    color: #fff;
}

.elder-mode .avatar-text {
    font-size: 56rpx;
}

.user-detail {
    flex: 1;
}

.user-name {
    font-size: 36rpx;
    font-weight: 600;
    display: block;
    margin-bottom: 8rpx;
}

.elder-mode .user-name {
    font-size: 40rpx;
}

.user-phone {
    font-size: 26rpx;
    opacity: 0.9;
}

.elder-mode .user-phone {
    font-size: 30rpx;
}

.arrow {
    font-size: 40rpx;
    opacity: 0.7;
}

.elder-switch {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx 24rpx;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 16rpx;
}

.switch-label {
    font-size: 28rpx;
}

.elder-mode .switch-label {
    font-size: 32rpx;
}

.quick-actions {
    display: flex;
    background: #fff;
    margin: -20rpx 20rpx 20rpx;
    border-radius: 20rpx;
    padding: 24rpx 0;
    position: relative;
    z-index: 1;
}

.action-item {
    flex: 1;
    text-align: center;
    position: relative;
}

.action-icon {
    font-size: 48rpx;
    display: block;
    margin-bottom: 8rpx;
}

.elder-mode .action-icon {
    font-size: 56rpx;
}

.action-text {
    font-size: 24rpx;
    color: #666;
}

.elder-mode .action-text {
    font-size: 28rpx;
}

.action-badge {
    position: absolute;
    top: 0;
    right: 20%;
    min-width: 36rpx;
    height: 36rpx;
    padding: 0 10rpx;
    background: #FF4D4F;
    color: #fff;
    font-size: 20rpx;
    border-radius: 18rpx;
    line-height: 36rpx;
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

.menu-sub {
    font-size: 24rpx;
    color: #999;
    margin-right: 16rpx;
}

.elder-mode .menu-sub {
    font-size: 28rpx;
}

.menu-arrow {
    font-size: 36rpx;
    color: #ccc;
}

.logout-section {
    padding: 40rpx 20rpx 20rpx;
}

.version-info {
    text-align: center;
    padding: 40rpx 0;
    font-size: 24rpx;
    color: #ccc;
}

.elder-mode .version-info {
    font-size: 28rpx;
}
</style>
