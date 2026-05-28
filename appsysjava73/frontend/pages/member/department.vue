<template>
	<view class="container">
		<view class="dept-list" v-if="departments.length > 0">
			<view 
				class="dept-card" 
				v-for="dept in departments" 
				:key="dept.id"
			>
				<view class="dept-header">
					<view class="dept-icon">
						<text class="iconfont icon-team"></text>
					</view>
					<view class="dept-info">
						<text class="dept-name">{{ dept.name }}</text>
						<text class="dept-count">{{ dept.memberCount }}名成员</text>
					</view>
					<view class="dept-actions" v-if="isManager">
						<text class="iconfont icon-edit action-icon" @click.stop="editDept(dept)"></text>
						<text class="iconfont icon-delete action-icon delete" @click.stop="deleteDept(dept)"></text>
					</view>
				</view>
				
				<view class="dept-desc" v-if="dept.description">
					{{ dept.description }}
				</view>

				<view class="dept-members" v-if="dept.members && dept.members.length > 0">
					<view class="member-avatar-small" v-for="(m, idx) in dept.members.slice(0, 5)" :key="m.id">
						{{ m.userName ? m.userName.charAt(0) : '成' }}
					</view>
					<text class="more-members" v-if="dept.members.length > 5">
						+{{ dept.members.length - 5 }}
					</text>
				</view>

				<view class="dept-footer" v-if="dept.leaderName">
					<text class="leader-label">负责人:</text>
					<text class="leader-name">{{ dept.leaderName }}</text>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无部门，请先添加部门</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="add-btn" v-if="isManager" @click="addDept">
			<text class="iconfont icon-add"></text>
			添加部门
		</view>

		<view class="modal" v-if="showModal" @click="closeModal">
			<view class="modal-content" @click.stop>
				<text class="modal-title">{{ modalTitle }}</text>
				
				<view class="form-item">
					<text class="label">部门名称 <text class="required">*</text></text>
					<input 
						type="text" 
						v-model="form.name" 
						placeholder="请输入部门名称"
						class="input"
					/>
				</view>

				<view class="form-item">
					<text class="label">部门描述</text>
					<textarea 
						v-model="form.description" 
						placeholder="请输入部门描述"
						class="textarea"
						:maxlength="200"
					/>
				</view>

				<view class="modal-actions">
					<button class="modal-btn btn-cancel" @click="closeModal">取消</button>
					<button class="modal-btn btn-confirm" @click="confirmSubmit">确认</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { memberApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				departments: [],
				loading: false,
				isManager: false,
				showModal: false,
				modalTitle: '添加部门',
				editingDept: null,
				form: {
					name: '',
					description: ''
				}
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkManager()
			this.loadData()
		},
		methods: {
			checkManager() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id === this.clubId)
			},
			async loadData() {
				this.loading = true
				try {
					const res = await memberApi.getDepartments(this.clubId)
					this.departments = res || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			addDept() {
				this.editingDept = null
				this.modalTitle = '添加部门'
				this.form = { name: '', description: '' }
				this.showModal = true
			},
			editDept(dept) {
				this.editingDept = dept
				this.modalTitle = '编辑部门'
				this.form = {
					name: dept.name,
					description: dept.description || ''
				}
				this.showModal = true
			},
			closeModal() {
				this.showModal = false
				this.editingDept = null
			},
			async confirmSubmit() {
				if (!this.form.name.trim()) {
					uni.showToast({ title: '请输入部门名称', icon: 'none' })
					return
				}

				try {
					if (this.editingDept) {
						await memberApi.updateDepartment(this.clubId, this.editingDept.id, this.form)
						uni.showToast({ title: '更新成功', icon: 'success' })
					} else {
						await memberApi.createDepartment(this.clubId, this.form)
						uni.showToast({ title: '创建成功', icon: 'success' })
					}
					this.closeModal()
					this.loadData()
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '操作失败', icon: 'none' })
				}
			},
			deleteDept(dept) {
				uni.showModal({
					title: '确认删除',
					content: `确定要删除部门"${dept.name}"吗？删除后该部门成员将变为未分配状态。`,
					success: async (res) => {
						if (res.confirm) {
							try {
								await memberApi.deleteDepartment(this.clubId, dept.id)
								uni.showToast({ title: '删除成功', icon: 'success' })
								this.loadData()
							} catch (e) {
								console.error(e)
								uni.showToast({ title: '删除失败', icon: 'none' })
							}
						}
					}
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 120rpx;
	}

	.dept-list {
		padding: 20rpx;
	}

	.dept-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.dept-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.dept-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 16rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.dept-icon .iconfont {
		color: #fff;
		font-size: 36rpx;
	}

	.dept-info {
		flex: 1;
	}

	.dept-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 5rpx;
	}

	.dept-count {
		font-size: 24rpx;
		color: #999;
	}

	.dept-actions {
		display: flex;
		gap: 20rpx;
	}

	.action-icon {
		font-size: 32rpx;
		color: #999;
		padding: 10rpx;
	}

	.action-icon.delete {
		color: #ff4d4f;
	}

	.dept-desc {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
		margin-bottom: 20rpx;
		padding: 20rpx;
		background: #f8f9ff;
		border-radius: 8rpx;
	}

	.dept-members {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.member-avatar-small {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		font-weight: bold;
		margin-right: -15rpx;
		border: 3rpx solid #fff;
	}

	.more-members {
		font-size: 24rpx;
		color: #999;
		margin-left: 25rpx;
	}

	.dept-footer {
		display: flex;
		align-items: center;
		padding-top: 15rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.leader-label {
		font-size: 24rpx;
		color: #999;
		margin-right: 10rpx;
	}

	.leader-name {
		font-size: 26rpx;
		color: #667eea;
	}

	.add-btn {
		position: fixed;
		bottom: 30rpx;
		left: 50%;
		transform: translateX(-50%);
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		padding: 20rpx 40rpx;
		border-radius: 50rpx;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 4rpx 20rpx rgba(102, 126, 234, 0.4);
	}

	.add-btn .iconfont {
		margin-right: 10rpx;
	}

	.empty,
	.loading {
		text-align: center;
		padding: 100rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	.modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
	}

	.modal-content {
		width: 600rpx;
		background: #fff;
		border-radius: 20rpx;
		padding: 40rpx;
	}

	.modal-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		text-align: center;
		margin-bottom: 30rpx;
		display: block;
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
		min-height: 150rpx;
		padding: 20rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #333;
		box-sizing: border-box;
		line-height: 1.6;
	}

	.modal-actions {
		display: flex;
		gap: 20rpx;
		margin-top: 30rpx;
	}

	.modal-btn {
		flex: 1;
		height: 70rpx;
		line-height: 70rpx;
		border-radius: 35rpx;
		font-size: 28rpx;
		border: none;
	}

	.btn-cancel {
		background: #f5f5f5;
		color: #666;
	}

	.btn-confirm {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.add-btn {
			left: calc(50% + 360px - 100rpx);
		}
	}
</style>
