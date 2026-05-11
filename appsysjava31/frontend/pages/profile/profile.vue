<template>
	<view class="profile-container">
		<view class="profile-header">
			<view class="avatar-wrap">
				<view class="avatar">
					<text class="avatar-text">{{ userInitial }}</text>
				</view>
				<view class="edit-icon">✏️</view>
			</view>
			<view class="user-info">
				<text class="user-name">{{ userInfo.realName || userInfo.username || '未登录' }}</text>
				<text class="user-role">{{ getRoleText(userInfo.role) }}</text>
			</view>
			<view class="arrow-wrap">
				<text class="arrow">></text>
			</view>
		</view>
		
		<view class="quick-entry">
			<view class="entry-item" @click="goToPage('/pages/appointment-list/appointment-list')">
				<view class="entry-icon">📋</view>
				<text class="entry-text">我的预约</text>
			</view>
			<view class="entry-item" @click="goToPage('/pages/report/report')">
				<view class="entry-icon">📄</view>
				<text class="entry-text">检验报告</text>
			</view>
			<view class="entry-item" @click="goToPage('/pages/invoice/invoice')">
				<view class="entry-icon">🧾</view>
				<text class="entry-text">电子票据</text>
			</view>
			<view class="entry-item" @click="goToPage('/pages/message/message')">
				<view class="entry-icon">🔔</view>
				<text class="entry-text">消息中心</text>
			</view>
		</view>
		
		<view class="menu-section">
			<view class="menu-item" @click="goToPage('/pages/settings/settings')">
				<view class="menu-icon">⚙️</view>
				<text class="menu-text">通知设置</text>
				<text class="arrow">></text>
			</view>
			
			<view class="menu-item" @click="showElderMode">
				<view class="menu-icon">👵</view>
				<text class="menu-text">长辈模式</text>
				<view class="menu-status">
					<text class="status-text">{{ getApp().globalData.elderMode ? '已开启' : '已关闭' }}</text>
					<text class="arrow">></text>
				</view>
			</view>
			
			<view class="menu-item" @click="showSecurity">
				<view class="menu-icon">🔐</view>
				<text class="menu-text">安全设置</text>
				<text class="arrow">></text>
			</view>
			
			<view class="menu-item" @click="showAbout">
				<view class="menu-icon">ℹ️</view>
				<text class="menu-text">关于我们</text>
				<text class="arrow">></text>
			</view>
			
			<view class="menu-item" @click="showHelp">
				<view class="menu-icon">❓</view>
				<text class="menu-text">帮助中心</text>
				<text class="arrow">></text>
			</view>
		</view>
		
		<view class="menu-section" v-if="isLogin">
			<view class="menu-item" @click="clearCache">
				<view class="menu-icon">🗑️</view>
				<text class="menu-text">清除缓存</text>
				<text class="arrow">></text>
			</view>
		</view>
		
		<view class="action-section" v-if="isLogin">
			<view class="logout-btn" @click="handleLogout">
				退出登录
			</view>
		</view>
		
		<view class="action-section" v-else>
			<view class="login-btn" @click="goToLogin">
				立即登录
			</view>
		</view>
		
		<view class="app-info">
			<text class="info-text">智慧医院 v1.0.0</text>
			<text class="info-text">您的健康，我们守护</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			userInfo: {},
			isLogin: false
		}
	},
	computed: {
		userInitial() {
			if (this.userInfo.realName) {
				return this.userInfo.realName.charAt(0)
			}
			if (this.userInfo.username) {
				return this.userInfo.username.charAt(0).toUpperCase()
			}
			return 'U'
		}
	},
	onShow() {
		this.loadUserInfo()
	},
	methods: {
		loadUserInfo() {
			const token = uni.getStorageSync('token')
			this.isLogin = !!token
			this.userInfo = uni.getStorageSync('userInfo') || {}
		},
		
		getRoleText(role) {
			const map = {
				'ADMIN': '管理员',
				'DOCTOR': '医生',
				'PATIENT': '患者'
			}
			return map[role] || '用户'
		},
		
		goToPage(url) {
			if (url.includes('appointment-list') || url.includes('message')) {
				uni.switchTab({ url })
			} else {
				uni.navigateTo({ url })
			}
		},
		
		goToLogin() {
			uni.navigateTo({
				url: '/pages/login/login'
			})
		},
		
		showElderMode() {
			const app = getApp()
			app.globalData.elderMode = !app.globalData.elderMode
			uni.showToast({
				title: app.globalData.elderMode ? '已开启长辈模式' : '已退出长辈模式',
				icon: 'none'
			})
		},
		
		showSecurity() {
			uni.showModal({
				title: '安全设置',
				content: '本系统采用国密SM4算法加密保护您的敏感数据（身份证、手机号等）。您的密码采用BCrypt加密存储，JWT令牌用于身份认证。',
				showCancel: false
			})
		},
		
		showAbout() {
			uni.showModal({
				title: '关于我们',
				content: '智慧医院预约系统\n版本：v1.0.0\n\n致力于为您提供便捷的就医服务体验。支持预约挂号、报告查询、电子票据等一站式服务。',
				showCancel: false
			})
		},
		
		showHelp() {
			uni.showModal({
				title: '帮助中心',
				content: '常见问题：\n\n1. 如何预约挂号？\n首页 -> 科室挂号 -> 选择医生 -> 预约\n\n2. 如何查看报告？\n首页 -> 检验报告\n\n3. 遇到问题？\n请联系客服：400-xxx-xxxx',
				showCancel: false
			})
		},
		
		clearCache() {
			uni.showModal({
				title: '清除缓存',
				content: '确定要清除缓存吗？这不会清除您的登录信息。',
				success: (res) => {
					if (res.confirm) {
						uni.showToast({
							title: '缓存已清除',
							icon: 'success'
						})
					}
				}
			})
		},
		
		handleLogout() {
			uni.showModal({
				title: '退出登录',
				content: '确定要退出登录吗？',
				success: (res) => {
					if (res.confirm) {
						uni.removeStorageSync('token')
						uni.removeStorageSync('userInfo')
						
						const app = getApp()
						app.globalData.userInfo = null
						app.globalData.isLogin = false
						
						uni.showToast({
							title: '已退出登录',
							icon: 'success'
						})
						
						setTimeout(() => {
							uni.reLaunch({
								url: '/pages/login/login'
							})
						}, 1000)
					}
				}
			})
		}
	}
}
</script>

<style>
.profile-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

.profile-header {
	display: flex;
	align-items: center;
	padding: 48rpx 32rpx;
	background: linear-gradient(135deg, #007AFF 0%, #0066CC 100%);
}

.avatar-wrap {
	position: relative;
	margin-right: 24rpx;
}

.avatar {
	width: 120rpx;
	height: 120rpx;
	background-color: #FFFFFF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}

.avatar-text {
	font-size: 56rpx;
	font-weight: 700;
	color: #007AFF;
}

.edit-icon {
	position: absolute;
	right: 0;
	bottom: 0;
	font-size: 24rpx;
	background-color: #FFFFFF;
	border-radius: 50%;
	width: 40rpx;
	height: 40rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.user-info {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.user-name {
	font-size: 36rpx;
	font-weight: 700;
	color: #FFFFFF;
	margin-bottom: 8rpx;
}

.user-role {
	font-size: 26rpx;
	color: rgba(255, 255, 255, 0.8);
}

.arrow-wrap {
	margin-left: 16rpx;
}

.arrow {
	font-size: 32rpx;
	color: rgba(255, 255, 255, 0.6);
}

.quick-entry {
	display: flex;
	background-color: #FFFFFF;
	padding: 32rpx 0;
	margin-top: -24rpx;
	margin-left: 32rpx;
	margin-right: 32rpx;
	border-radius: 16rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.entry-item {
	flex: 1;
	display: flex;
	flex-direction: column;
	align-items: center;
}

.entry-icon {
	font-size: 48rpx;
	margin-bottom: 12rpx;
}

.entry-text {
	font-size: 24rpx;
	color: #333333;
}

.menu-section {
	background-color: #FFFFFF;
	margin-top: 24rpx;
}

.menu-item {
	display: flex;
	align-items: center;
	padding: 28rpx 32rpx;
	border-top: 1rpx solid #F0F0F0;
}

.menu-item:first-child {
	border-top: none;
}

.menu-icon {
	font-size: 36rpx;
	margin-right: 20rpx;
}

.menu-text {
	flex: 1;
	font-size: 30rpx;
	color: #333333;
}

.menu-status {
	display: flex;
	align-items: center;
}

.status-text {
	font-size: 26rpx;
	color: #999999;
	margin-right: 8rpx;
}

.action-section {
	padding: 40rpx 32rpx;
}

.logout-btn {
	background-color: #FFFFFF;
	color: #FF3B30;
	text-align: center;
	padding: 28rpx;
	border-radius: 16rpx;
	font-size: 32rpx;
	font-weight: 500;
}

.login-btn {
	background-color: #007AFF;
	color: #FFFFFF;
	text-align: center;
	padding: 28rpx;
	border-radius: 16rpx;
	font-size: 32rpx;
	font-weight: 500;
}

.app-info {
	text-align: center;
	padding: 40rpx 0;
}

.info-text {
	display: block;
	font-size: 24rpx;
	color: #999999;
	margin-bottom: 8rpx;
}
</style>
