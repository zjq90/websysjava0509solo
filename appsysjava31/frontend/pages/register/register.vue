<template>
	<view class="register-container">
		<view class="register-header">
			<text class="title">用户注册</text>
			<text class="subtitle">创建您的智慧医院账号</text>
		</view>
		
		<view class="register-form">
			<view class="input-group">
				<text class="input-label">用户名</text>
				<input 
					v-model="formData.username" 
					type="text" 
					placeholder="请输入用户名" 
					class="input-box"
				/>
			</view>
			
			<view class="input-group">
				<text class="input-label">真实姓名</text>
				<input 
					v-model="formData.realName" 
					type="text" 
					placeholder="请输入真实姓名" 
					class="input-box"
				/>
			</view>
			
			<view class="input-group">
				<text class="input-label">密码</text>
				<input 
					v-model="formData.password" 
					type="password" 
					placeholder="请输入密码（6-20位）" 
					class="input-box"
				/>
			</view>
			
			<view class="input-group">
				<text class="input-label">确认密码</text>
				<input 
					v-model="formData.confirmPassword" 
					type="password" 
					placeholder="请再次输入密码" 
					class="input-box"
				/>
			</view>
			
			<view class="input-group">
				<text class="input-label">手机号</text>
				<input 
					v-model="formData.phone" 
					type="number" 
					placeholder="请输入手机号" 
					class="input-box"
				/>
			</view>
			
			<view class="input-group">
				<text class="input-label">身份证号</text>
				<input 
					v-model="formData.idCard" 
					type="text" 
					placeholder="请输入身份证号" 
					class="input-box"
				/>
			</view>
			
			<view class="btn-group">
				<view class="btn-primary register-btn" @click="handleRegister">
					注册
				</view>
			</view>
			
			<view class="login-link">
				<text class="text-secondary">已有账号？</text>
				<text class="text-primary" @click="goToLogin">立即登录</text>
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
				realName: '',
				password: '',
				confirmPassword: '',
				phone: '',
				idCard: ''
			}
		}
	},
	methods: {
		validateForm() {
			if (!this.formData.username) {
				uni.showToast({ title: '请输入用户名', icon: 'none' })
				return false
			}
			if (!this.formData.realName) {
				uni.showToast({ title: '请输入真实姓名', icon: 'none' })
				return false
			}
			if (!this.formData.password || this.formData.password.length < 6) {
				uni.showToast({ title: '密码至少6位', icon: 'none' })
				return false
			}
			if (this.formData.password !== this.formData.confirmPassword) {
				uni.showToast({ title: '两次密码不一致', icon: 'none' })
				return false
			}
			if (!this.formData.phone || !/^1[3-9]\d{9}$/.test(this.formData.phone)) {
				uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
				return false
			}
			if (!this.formData.idCard || this.formData.idCard.length < 15) {
				uni.showToast({ title: '请输入正确的身份证号', icon: 'none' })
				return false
			}
			return true
		},
		
		handleRegister() {
			if (!this.validateForm()) return
			
			this.$showLoading('注册中...')
			
			this.$request({
				url: '/auth/register',
				method: 'POST',
				data: {
					username: this.formData.username,
					password: this.formData.password,
					realName: this.formData.realName,
					phone: this.formData.phone,
					idCard: this.formData.idCard,
					role: 'PATIENT'
				}
			}).then(res => {
				this.$hideLoading()
				uni.showToast({
					title: '注册成功，请登录',
					icon: 'success'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1500)
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		},
		
		goToLogin() {
			uni.navigateBack()
		}
	}
}
</script>

<style>
.register-container {
	min-height: 100vh;
	padding: 40rpx;
	background-color: #F5F5F5;
}

.register-header {
	text-align: center;
	margin-bottom: 48rpx;
	padding-top: 40rpx;
}

.title {
	display: block;
	font-size: 40rpx;
	font-weight: 700;
	color: #333333;
	margin-bottom: 8rpx;
}

.subtitle {
	font-size: 28rpx;
	color: #666666;
}

.register-form {
	background-color: #FFFFFF;
	border-radius: 24rpx;
	padding: 48rpx 32rpx;
}

.register-btn {
	font-size: 32rpx;
	font-weight: 600;
	height: 96rpx;
	line-height: 96rpx;
}

.login-link {
	text-align: center;
	margin-top: 32rpx;
}

.login-link .text-primary {
	margin-left: 8rpx;
	font-weight: 500;
}
</style>
