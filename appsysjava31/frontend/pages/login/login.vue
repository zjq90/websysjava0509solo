<template>
	<view class="login-container">
		<view class="login-header">
			<view class="logo">
				<text class="logo-icon">🏥</text>
				<text class="logo-text">智慧医院</text>
			</view>
			<text class="slogan">便捷就医，健康相伴</text>
		</view>
		
		<view class="login-form">
			<view class="input-group">
				<text class="input-label">用户名</text>
				<view class="input-wrapper">
					<text class="input-icon">👤</text>
					<input v-model="username" type="text" placeholder="请输入用户名" class="input-field" />
				</view>
			</view>
			
			<view class="input-group">
				<text class="input-label">密码</text>
				<view class="input-wrapper">
					<text class="input-icon">🔒</text>
					<input v-model="password" type="password" placeholder="请输入密码" class="input-field" />
				</view>
			</view>
			
			<view class="btn-group">
				<view class="btn-primary login-btn" @click="handleLogin">登录</view>
			</view>
			
			<view class="register-link">
				<text class="text-secondary">还没有账号？</text>
				<text class="text-primary register-text" @click="goToRegister">立即注册</text>
			</view>
		</view>
		
		<view class="test-accounts">
			<view class="test-accounts-title">测试账号（体验功能）</view>
			<view class="account-list">
				<view class="account-item" @click="quickLogin('admin', 'admin123')">
					<view class="account-role">管理员</view>
					<view class="account-info">admin / admin123</view>
				</view>
				<view class="account-item" @click="quickLogin('patient', '123456')">
					<view class="account-role">患者</view>
					<view class="account-info">patient / 123456</view>
				</view>
				<view class="account-item" @click="quickLogin('doctor', '123456')">
					<view class="account-role">医生</view>
					<view class="account-info">doctor / 123456</view>
				</view>
			</view>
		</view>
		
		<view class="elder-mode-toggle" @click="toggleElderMode">
			<text class="toggle-icon">{{ elderIcon }}</text>
			<text class="toggle-text">{{ elderModeText }}</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			username: '',
			password: '',
			elderIcon: '👴',
			elderModeText: '长辈模式'
		}
	},
	onShow() {
		const app = getApp()
		if (app && app.globalData && app.globalData.elderMode) {
			this.elderIcon = '👵'
			this.elderModeText = '退出长辈模式'
		} else {
			this.elderIcon = '👴'
			this.elderModeText = '长辈模式'
		}
	},
	methods: {
		handleLogin() {
			if (!this.username || !this.password) {
				uni.showToast({
					title: '请输入用户名和密码',
					icon: 'none'
				})
				return
			}
			
			uni.showLoading({
				title: '登录中...',
				mask: true
			})
			
			const baseUrl = 'http://localhost:8080/api'
			const token = uni.getStorageSync('token')
			
			uni.request({
				url: baseUrl + '/auth/login',
				method: 'POST',
				data: {
					username: this.username,
					password: this.password
				},
				header: {
					'Content-Type': 'application/json',
					'Authorization': token ? 'Bearer ' + token : ''
				},
				success: (res) => {
					uni.hideLoading()
					if (res.statusCode === 200 && res.data.code === 200) {
						uni.setStorageSync('token', res.data.data.token)
						uni.setStorageSync('userInfo', res.data.data.user)
						
						const app = getApp()
						app.globalData.userInfo = res.data.data.user
						app.globalData.isLogin = true
						
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
							title: res.data.message || '登录失败',
							icon: 'none'
						})
					}
				},
				fail: (err) => {
					uni.hideLoading()
					uni.showToast({
						title: '网络异常，请检查网络',
						icon: 'none'
					})
				}
			})
		},
		
		quickLogin(username, password) {
			this.username = username
			this.password = password
			this.handleLogin()
		},
		
		goToRegister() {
			uni.navigateTo({
				url: '/pages/register/register'
			})
		},
		
		toggleElderMode() {
			const app = getApp()
			if (app && app.globalData) {
				app.globalData.elderMode = !app.globalData.elderMode
				if (app.globalData.elderMode) {
					this.elderIcon = '👵'
					this.elderModeText = '退出长辈模式'
					uni.showToast({
						title: '已开启长辈模式',
						icon: 'none'
					})
				} else {
					this.elderIcon = '👴'
					this.elderModeText = '长辈模式'
					uni.showToast({
						title: '已退出长辈模式',
						icon: 'none'
					})
				}
			}
		}
	}
}
</script>

<style>
.login-container {
	min-height: 100vh;
	padding: 80rpx 40rpx;
	display: flex;
	flex-direction: column;
	background: linear-gradient(180deg, #E3F2FF 0%, #FFFFFF 100%);
}

.login-header {
	text-align: center;
	margin-bottom: 80rpx;
}

.logo {
	display: flex;
	align-items: center;
	justify-content: center;
	margin-bottom: 16rpx;
}

.logo-icon {
	font-size: 64rpx;
	margin-right: 16rpx;
}

.logo-text {
	font-size: 48rpx;
	font-weight: 700;
	color: #007AFF;
}

.slogan {
	font-size: 28rpx;
	color: #666666;
}

.login-form {
	background-color: #FFFFFF;
	border-radius: 24rpx;
	padding: 48rpx 32rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 122, 255, 0.1);
	margin-bottom: 40rpx;
}

.input-group {
	margin-bottom: 32rpx;
}

.input-label {
	display: block;
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 12rpx;
	font-weight: 500;
}

.input-wrapper {
	display: flex;
	align-items: center;
	background-color: #F5F5F5;
	border-radius: 12rpx;
	padding: 0 24rpx;
	height: 96rpx;
}

.input-icon {
	font-size: 32rpx;
	margin-right: 16rpx;
}

.input-field {
	flex: 1;
	font-size: 30rpx;
}

.btn-group {
	margin-top: 48rpx;
}

.login-btn {
	font-size: 32rpx;
	font-weight: 600;
	height: 96rpx;
	line-height: 96rpx;
}

.register-link {
	text-align: center;
	margin-top: 32rpx;
}

.register-text {
	margin-left: 8rpx;
	font-weight: 500;
}

.test-accounts {
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 32rpx;
}

.test-accounts-title {
	font-size: 26rpx;
	color: #666666;
	text-align: center;
	margin-bottom: 16rpx;
}

.account-list {
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
}

.account-item {
	width: 31%;
	background-color: #F5F5F5;
	border-radius: 12rpx;
	padding: 16rpx 8rpx;
	text-align: center;
}

.account-role {
	font-size: 24rpx;
	color: #007AFF;
	font-weight: 500;
	margin-bottom: 4rpx;
}

.account-info {
	font-size: 20rpx;
	color: #999999;
}

.elder-mode-toggle {
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 24rpx;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	border: 2rpx dashed #007AFF;
}

.toggle-icon {
	font-size: 32rpx;
	margin-right: 8rpx;
}

.toggle-text {
	font-size: 28rpx;
	color: #007AFF;
}

.btn-primary {
	background-color: #007AFF;
	color: #FFFFFF;
	border-radius: 48rpx;
	padding: 20rpx 48rpx;
	font-size: 30rpx;
	text-align: center;
}

.text-primary {
	color: #007AFF;
}

.text-secondary {
	color: #666666;
}
</style>
