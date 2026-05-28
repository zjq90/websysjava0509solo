<template>
	<view class="login-container">
		<view class="login-header">
			<view class="logo">
				<text class="logo-text">社团管理系统</text>
				<text class="logo-subtitle">Club Management System</text>
			</view>
		</view>

		<view class="login-form">
			<view class="form-title">
				<text class="title">欢迎回来</text>
				<text class="subtitle">请登录您的账号</text>
			</view>

			<view class="form-group">
				<view class="input-wrapper">
					<text class="iconfont icon-user"></text>
					<input 
						type="text" 
						v-model="form.username" 
						placeholder="请输入用户名" 
						class="input"
					/>
				</view>
			</view>

			<view class="form-group">
				<view class="input-wrapper">
					<text class="iconfont icon-lock"></text>
					<input 
						type="password" 
						v-model="form.password" 
						placeholder="请输入密码" 
						class="input"
					/>
				</view>
			</view>

			<view class="form-actions">
				<button class="btn-login" @click="handleLogin">登录</button>
			</view>

			<view class="form-footer">
				<text class="text-secondary">还没有账号？</text>
				<text class="text-primary" @click="goRegister">立即注册</text>
			</view>

			<view class="test-accounts">
				<text class="text-xs text-secondary">测试账号：</text>
				<text class="text-xs">user1/123456 或 user2/123456</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { authApi } from '@/api/index.js'

	export default {
		data() {
			return {
				form: {
					username: '',
					password: ''
				},
				loading: false
			}
		},
		methods: {
			async handleLogin() {
				if (!this.form.username) {
					uni.showToast({
						title: '请输入用户名',
						icon: 'none'
					})
					return
				}
				if (!this.form.password) {
					uni.showToast({
						title: '请输入密码',
						icon: 'none'
					})
					return
				}

				this.loading = true
				try {
					const res = await authApi.login(this.form)
					this.$store.dispatch('login', res)

					uni.showToast({
						title: '登录成功',
						icon: 'success'
					})

					setTimeout(() => {
						uni.switchTab({
							url: '/pages/index/index'
						})
					}, 1000)
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			goRegister() {
				uni.navigateTo({
					url: '/pages/register/register'
				})
			}
		}
	}
</script>

<style scoped>
	.login-container {
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		display: flex;
		flex-direction: column;
	}

	.login-header {
		padding: 100rpx 0 60rpx;
		text-align: center;
	}

	.logo-text {
		font-size: 48rpx;
		font-weight: bold;
		color: #fff;
		display: block;
	}

	.logo-subtitle {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.8);
		margin-top: 10rpx;
		display: block;
	}

	.login-form {
		flex: 1;
		background: #fff;
		border-radius: 40rpx 40rpx 0 0;
		padding: 60rpx 40rpx;
	}

	.form-title {
		margin-bottom: 60rpx;
	}

	.title {
		font-size: 40rpx;
		font-weight: bold;
		color: #333;
		display: block;
	}

	.subtitle {
		font-size: 28rpx;
		color: #999;
		margin-top: 10rpx;
		display: block;
	}

	.form-group {
		margin-bottom: 30rpx;
	}

	.input-wrapper {
		display: flex;
		align-items: center;
		background: #f5f5f5;
		border-radius: 16rpx;
		padding: 24rpx 30rpx;
	}

	.iconfont {
		color: #999;
		font-size: 32rpx;
		margin-right: 20rpx;
	}

	.input {
		flex: 1;
		font-size: 28rpx;
		color: #333;
	}

	.form-actions {
		margin-top: 50rpx;
	}

	.btn-login {
		width: 100%;
		height: 90rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 50rpx;
		border: none;
	}

	.btn-login[disabled] {
		opacity: 0.6;
	}

	.form-footer {
		margin-top: 40rpx;
		text-align: center;
	}

	.text-primary {
		color: #667eea;
		margin-left: 10rpx;
	}

	.text-secondary {
		color: #999;
	}

	.text-xs {
		font-size: 24rpx;
	}

	.test-accounts {
		margin-top: 40rpx;
		padding: 20rpx;
		background: #f9f9f9;
		border-radius: 12rpx;
		text-align: center;
	}
</style>
