<template>
	<view class="container">
		<view class="form-card">
			<view class="form-item">
				<text class="label">招新标题</text>
				<input 
					type="text" 
					v-model="form.title" 
					placeholder="请输入招新标题"
					class="input"
					maxlength="50"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">招新名额</text>
				<input 
					type="number" 
					v-model="form.quota" 
					placeholder="请输入招新名额"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">招新要求</text>
				<textarea 
					v-model="form.requirements" 
					placeholder="请输入招新要求，如专业、年级、技能等"
					class="textarea"
					maxlength="500"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">招新描述</text>
				<textarea 
					v-model="form.description" 
					placeholder="请输入招新详细描述"
					class="textarea"
					maxlength="1000"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">招新部门</text>
				<view class="department-list">
					<view 
						class="dept-item" 
						:class="{ active: selectedDepartments.includes(dept.id) }"
						v-for="dept in departments" 
						:key="dept.id"
						@click="toggleDepartment(dept.id)"
					>
						<text class="dept-name">{{ dept.name }}</text>
						<text class="iconfont icon-check" v-if="selectedDepartments.includes(dept.id)"></text>
					</view>
				</view>
			</view>
			
			<view class="form-item">
				<text class="label">截止时间</text>
				<picker 
					mode="date" 
					:value="form.endDate" 
					@change="onDateChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.endDate }">
							{{ form.endDate || '请选择截止日期' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<view class="switch-item">
					<text class="switch-label">立即开启招新</text>
					<switch 
						:checked="form.isActive === 1" 
						@change="toggleActive"
						color="#667eea"
					/>
				</view>
			</view>
		</view>

		<view class="submit-bar">
			<button 
				class="btn-submit" 
				:disabled="submitting || !canSubmit"
				@click="submit"
			>
				{{ submitting ? '发布中...' : '发布招新' }}
			</button>
		</view>
	</view>
</template>

<script>
	import { recruitApi, departmentApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				form: {
					title: '',
					quota: '',
					requirements: '',
					description: '',
					endDate: '',
					isActive: 1
				},
				departments: [],
				selectedDepartments: [],
				submitting: false
			}
		},
		computed: {
			canSubmit() {
				return this.form.title.trim() && 
					this.form.quota > 0 && 
					this.form.requirements.trim() &&
					this.form.description.trim() &&
					this.form.endDate &&
					this.selectedDepartments.length > 0
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.loadDepartments()
		},
		methods: {
			async loadDepartments() {
				try {
					const res = await departmentApi.getList(this.clubId)
					this.departments = res.list || []
				} catch (e) {
					console.error(e)
					this.departments = [
						{ id: 1, name: '技术部' },
						{ id: 2, name: '宣传部' },
						{ id: 3, name: '组织部' },
						{ id: 4, name: '外联部' },
						{ id: 5, name: '财务部' }
					]
				}
			},
			toggleDepartment(deptId) {
				const index = this.selectedDepartments.indexOf(deptId)
				if (index > -1) {
					this.selectedDepartments.splice(index, 1)
				} else {
					this.selectedDepartments.push(deptId)
				}
			},
			onDateChange(e) {
				this.form.endDate = e.detail.value
			},
			toggleActive(e) {
				this.form.isActive = e.detail.value ? 1 : 0
			},
			async submit() {
				if (!this.canSubmit || this.submitting) return
				
				this.submitting = true
				try {
					const submitData = {
						...this.form,
						departmentIds: this.selectedDepartments.join(',')
					}
					await recruitApi.create(this.clubId, submitData)
					uni.showToast({ title: '发布成功', icon: 'success' })
					setTimeout(() => {
						uni.navigateBack()
					}, 1500)
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '发布失败', icon: 'none' })
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
		padding: 30rpx;
	}

	.form-item {
		margin-bottom: 40rpx;
	}

	.form-item:last-child {
		margin-bottom: 0;
	}

	.label {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 15rpx;
	}

	.input {
		width: 100%;
		height: 90rpx;
		padding: 0 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		border: none;
		box-sizing: border-box;
	}

	.textarea {
		width: 100%;
		min-height: 150rpx;
		padding: 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		line-height: 1.6;
		border: none;
		box-sizing: border-box;
	}

	.department-list {
		display: flex;
		flex-wrap: wrap;
		gap: 15rpx;
	}

	.dept-item {
		padding: 15rpx 25rpx;
		background: #f5f5f5;
		border-radius: 30rpx;
		font-size: 26rpx;
		color: #666;
		display: flex;
		align-items: center;
		gap: 10rpx;
		border: 2rpx solid transparent;
	}

	.dept-item.active {
		background: rgba(102, 126, 234, 0.1);
		color: #667eea;
		border-color: #667eea;
	}

	.dept-item .icon-check {
		font-size: 24rpx;
	}

	.picker {
		width: 100%;
	}

	.picker-input {
		height: 90rpx;
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

	.switch-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.switch-label {
		font-size: 28rpx;
		color: #333;
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
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 45rpx;
		font-size: 30rpx;
		font-weight: 500;
		border: none;
	}

	.btn-submit[disabled] {
		opacity: 0.5;
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
