<template>
	<view class="container">
		<view class="header">
			<view class="logo">
				<text class="logo-text">社团管理</text>
			</view>
			<text class="subtitle">欢迎加入我们</text>
		</view>

		<view class="form-card">
			<view class="form-item">
				<text class="label">学号</text>
				<input 
					type="text" 
					v-model="form.studentNo" 
					placeholder="请输入学号"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">用户名</text>
				<input 
					type="text" 
					v-model="form.username" 
					placeholder="请输入用户名"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">密码</text>
				<input 
					type="password" 
					v-model="form.password" 
					placeholder="请输入密码（至少6位）"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">确认密码</text>
				<input 
					type="password" 
					v-model="form.confirmPassword" 
					placeholder="请再次输入密码"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">真实姓名</text>
				<input 
					type="text" 
					v-model="form.realName" 
					placeholder="请输入真实姓名"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">手机号</text>
				<input 
					type="tel" 
					v-model="form.phone" 
					placeholder="请输入手机号"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">所在学校</text>
				<picker 
					:range="schools" 
					range-key="name" 
					@change="onSchoolChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.schoolId }">
							{{ form.schoolId ? getSchoolName() : '请选择学校' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<text class="label">院系</text>
				<input 
					type="text" 
					v-model="form.department" 
					placeholder="请输入院系"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">专业</text>
				<input 
					type="text" 
					v-model="form.major" 
					placeholder="请输入专业"
					class="input"
				/>
			</view>
		</view>

		<button class="btn-register" :disabled="submitting || !canSubmit" @click="register">
			{{ submitting ? '注册中...' : '注册' }}
		</button>

		<view class="footer">
			<text class="tip-text">已有账号？</text>
			<text class="login-link" @click="goLogin">立即登录</text>
		</view>
	</view>
</template>

<script>
	import { authApi } from '@/api/index.js'

	export default {
		data() {
			return {
				form: {
					studentNo: '',
					username: '',
					password: '',
					confirmPassword: '',
					realName: '',
					phone: '',
					schoolId: null,
					department: '',
					major: ''
				},
				schools: [
					{ id: 1, name: '清华大学' },
					{ id: 2, name: '北京大学' },
					{ id: 3, name: '浙江大学' },
					{ id: 4, name: '复旦大学' },
					{ id: 5, name: '上海交通大学' }
				],
				submitting: false
			}
		},
		computed: {
			canSubmit() {
				return this.form.studentNo.trim() &&
					this.form.username.trim() &&
					this.form.password.trim() &&
					this.form.confirmPassword.trim() &&
					this.form.realName.trim() &&
					this.form.phone.trim() &&
					this.form.schoolId &&
					this.form.department.trim() &&
					this.form.major.trim() &&
					this.form.password === this.form.confirmPassword &&
					this.form.password.length >= 6
			}
		},
		methods: {
			onSchoolChange(e) {
				const index = e.detail.value
				this.form.schoolId = this.schools[index].id
			},
			getSchoolName() {
				const school = this.schools.find(s => s.id === this.form.schoolId)
				return school ? school.name : ''
			},
			async register() {
				if (!this.canSubmit || this.submitting) return
				
				if (this.form.password !== this.form.confirmPassword) {
					uni.showToast({ title: '两次密码不一致', icon: 'none' })
					return
				}
				
				this.submitting = true
				try {
					await authApi.register({
						...this.form
					})
					uni.showToast({ title: '注册成功', icon: 'success' })
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/login/login'
						})
					}, 1500)
				} catch (e) {
					console.error(e)
					uni.showToast({ 
						title: e.message || '注册失败', 
						icon: 'none' 
					})
				} finally {
					this.submitting = false
				}
			},
			goLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 60rpx 40rpx;
	}

	.header {
		text-align: center;
		margin-bottom: 60rpx;
	}

	.logo {
		width: 120rpx;
		height: 120rpx;
		background: rgba(255, 255, 255, 0.2);
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 auto 20rpx;
	}

	.logo-text {
		font-size: 36rpx;
		font-weight: bold;
		color: #fff;
	}

	.subtitle {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.form-card {
		background: #fff;
		border-radius: 24rpx;
		padding: 40rpx;
		margin-bottom: 40rpx;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.1);
	}

	.form-item {
		margin-bottom: 30rpx;
	}

	.form-item:last-child {
		margin-bottom: 0;
	}

	.label {
		font-size: 28rpx;
		color: #666;
		display: block;
		margin-bottom: 15rpx;
	}

	.input {
		width: 100%;
		height: 80rpx;
		padding: 0 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		border: none;
		box-sizing: border-box;
	}

	.picker {
		width: 100%;
	}

	.picker-input {
		height: 80rpx;
		padding: 0 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.picker-input .placeholder {
		color: #999;
	}

	.picker-input .iconfont {
		color: #999;
		font-size: 24rpx;
	}

	.btn-register {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		background: #fff;
		color: #667eea;
		border-radius: 45rpx;
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 30rpx;
		border: none;
	}

	.btn-register[disabled] {
		opacity: 0.6;
	}

	.footer {
		text-align: center;
	}

	.tip-text {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.login-link {
		font-size: 28rpx;
		color: #fff;
		font-weight: 500;
		margin-left: 10rpx;
	}

	@media screen and (min-width: 768px) {
		.container {
			padding: 60rpx 20%;
		}
	}
</style>
