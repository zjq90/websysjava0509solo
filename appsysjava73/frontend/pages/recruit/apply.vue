<template>
	<view class="container">
		<view class="form-card">
			<view class="form-title">申请加入社团</view>
			
			<view class="form-item">
				<text class="label">姓名 <text class="required">*</text></text>
				<input 
					type="text" 
					v-model="form.name" 
					placeholder="请输入您的姓名"
					class="input"
				/>
			</view>

			<view class="form-item">
				<text class="label">学号 <text class="required">*</text></text>
				<input 
					type="text" 
					v-model="form.studentNo" 
					placeholder="请输入您的学号"
					class="input"
				/>
			</view>

			<view class="form-item">
				<text class="label">院系 <text class="required">*</text></text>
				<input 
					type="text" 
					v-model="form.department" 
					placeholder="请输入您的院系"
					class="input"
				/>
			</view>

			<view class="form-item">
				<text class="label">专业 <text class="required">*</text></text>
				<input 
					type="text" 
					v-model="form.major" 
					placeholder="请输入您的专业"
					class="input"
				/>
			</view>

			<view class="form-item">
				<text class="label">联系电话 <text class="required">*</text></text>
				<input 
					type="number" 
					v-model="form.phone" 
					placeholder="请输入您的联系电话"
					class="input"
				/>
			</view>

			<view class="form-item">
				<text class="label">邮箱</text>
				<input 
					type="text" 
					v-model="form.email" 
					placeholder="请输入您的邮箱"
					class="input"
				/>
			</view>

			<view class="form-item" v-if="departments.length > 0">
				<text class="label">意向部门 <text class="required">*</text></text>
				<picker 
					:range="departments" 
					range-key="name" 
					@change="onDeptChange"
				>
					<view class="picker-box">
						<text class="picker-text" :class="{ placeholder: !form.departmentId }">
							{{ selectedDept ? selectedDept.name : '请选择意向部门' }}
						</text>
						<text class="iconfont icon-down"></text>
					</view>
				</picker>
			</view>

			<view class="form-item">
				<text class="label">个人简介 <text class="required">*</text></text>
				<textarea 
					v-model="form.introduction" 
					placeholder="请介绍一下您自己，包括相关经历、技能特长、加入动机等"
					class="textarea"
					:maxlength="500"
				/>
				<text class="count">{{ form.introduction.length }}/500</text>
			</view>

			<view class="form-item">
				<text class="label">兴趣爱好</text>
				<textarea 
					v-model="form.hobbies" 
					placeholder="请描述您的兴趣爱好"
					class="textarea"
					:maxlength="200"
				/>
			</view>

			<view class="form-item">
				<text class="label">期望获得</text>
				<textarea 
					v-model="form.expectation" 
					placeholder="您希望在社团中获得什么？"
					class="textarea"
					:maxlength="200"
				/>
			</view>
		</view>

		<view class="submit-bar">
			<button class="btn-submit" :disabled="submitting" @click="submitApply">
				<text v-if="submitting">提交中...</text>
				<text v-else>提交申请</text>
			</button>
		</view>
	</view>
</template>

<script>
	import { recruitApi, memberApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				recruitId: null,
				departments: [],
				selectedDept: null,
				submitting: false,
				form: {
					name: '',
					studentNo: '',
					department: '',
					major: '',
					phone: '',
					email: '',
					departmentId: null,
					introduction: '',
					hobbies: '',
					expectation: ''
				}
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.recruitId = options.recruitId
			this.loadDepartments()
			this.loadUserInfo()
		},
		methods: {
			loadUserInfo() {
				const userInfo = store.state.userInfo
				if (userInfo) {
					this.form.name = userInfo.name || ''
					this.form.studentNo = userInfo.studentNo || ''
					this.form.department = userInfo.department || ''
					this.form.phone = userInfo.phone || ''
					this.form.email = userInfo.email || ''
				}
			},
			async loadDepartments() {
				try {
					const res = await memberApi.getDepartments(this.clubId)
					this.departments = res || []
				} catch (e) {
					console.error(e)
				}
			},
			onDeptChange(e) {
				const index = e.detail.value
				this.selectedDept = this.departments[index]
				this.form.departmentId = this.selectedDept.id
			},
			validateForm() {
				if (!this.form.name.trim()) {
					uni.showToast({ title: '请输入姓名', icon: 'none' })
					return false
				}
				if (!this.form.studentNo.trim()) {
					uni.showToast({ title: '请输入学号', icon: 'none' })
					return false
				}
				if (!this.form.department.trim()) {
					uni.showToast({ title: '请输入院系', icon: 'none' })
					return false
				}
				if (!this.form.major.trim()) {
					uni.showToast({ title: '请输入专业', icon: 'none' })
					return false
				}
				if (!this.form.phone.trim()) {
					uni.showToast({ title: '请输入联系电话', icon: 'none' })
					return false
				}
				if (!this.form.departmentId) {
					uni.showToast({ title: '请选择意向部门', icon: 'none' })
					return false
				}
				if (!this.form.introduction.trim()) {
					uni.showToast({ title: '请输入个人简介', icon: 'none' })
					return false
				}
				return true
			},
			async submitApply() {
				if (!this.validateForm()) return
				
				this.submitting = true
				try {
					await recruitApi.submitApply(this.clubId, this.recruitId, this.form)
					uni.showToast({ 
						title: '申请提交成功', 
						icon: 'success',
						duration: 2000
					})
					setTimeout(() => {
						uni.redirectTo({
							url: `/pages/recruit/my-apply?clubId=${this.clubId}`
						})
					}, 2000)
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '提交失败，请重试', icon: 'none' })
				} finally {
					this.submitting = false
				}
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 140rpx;
	}

	.form-card {
		background: #fff;
		margin: 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.form-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		text-align: center;
		margin-bottom: 30rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.form-item {
		margin-bottom: 30rpx;
	}

	.label {
		font-size: 28rpx;
		color: #333;
		display: block;
		margin-bottom: 15rpx;
	}

	.required {
		color: #ff4d4f;
		margin-left: 5rpx;
	}

	.input {
		width: 100%;
		height: 80rpx;
		padding: 0 20rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #333;
		box-sizing: border-box;
	}

	.textarea {
		width: 100%;
		min-height: 200rpx;
		padding: 20rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #333;
		box-sizing: border-box;
		line-height: 1.6;
	}

	.count {
		font-size: 22rpx;
		color: #999;
		text-align: right;
		margin-top: 10rpx;
		display: block;
	}

	.picker-box {
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 80rpx;
		padding: 0 20rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
	}

	.picker-text {
		font-size: 28rpx;
		color: #333;
	}

	.picker-text.placeholder {
		color: #999;
	}

	.icon-down {
		font-size: 24rpx;
		color: #999;
	}

	.submit-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx 30rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.btn-submit {
		width: 100%;
		height: 80rpx;
		line-height: 80rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 40rpx;
		font-size: 30rpx;
		border: none;
	}

	.btn-submit[disabled] {
		opacity: 0.6;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.submit-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
