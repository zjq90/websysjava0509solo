<template>
	<view class="page-wrapper">
		<view class="container" v-if="member">
			<view class="header">
				<view class="member-avatar" :class="'role-' + member.role">
					{{ member.userName ? member.userName.charAt(0) : '成' }}
				</view>
				<text class="member-name">{{ member.userName }}</text>
				<view class="member-tags">
					<text class="role-tag" :class="'role-' + member.role">
						{{ getRoleText(member.role) }}
					</text>
					<text class="active-tag" v-if="member.active === 1">活跃成员</text>
					<text class="dept-tag">{{ member.departmentName || '未分配' }}</text>
				</view>
			</view>

			<view class="info-card">
				<view class="info-item">
					<text class="info-label">学号</text>
					<text class="info-value">{{ member.studentNo }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">院系</text>
					<text class="info-value">{{ member.department }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">专业</text>
					<text class="info-value">{{ member.major || '-' }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">联系方式</text>
					<text class="info-value">{{ member.phone }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">邮箱</text>
					<text class="info-value">{{ member.email || '-' }}</text>
				</view>
				<view class="info-item">
					<text class="info-label">加入时间</text>
					<text class="info-value">{{ member.joinTime }}</text>
				</view>
			</view>

			<view class="section-card">
				<view class="section-title">
					<text class="iconfont icon-activity"></text>
					活动参与记录
				</view>
				<view class="stats-row">
					<view class="stat-item">
						<text class="stat-num">{{ activityStats.total }}</text>
						<text class="stat-label">参与活动</text>
					</view>
					<view class="stat-item">
						<text class="stat-num">{{ activityStats.rate }}%</text>
						<text class="stat-label">参与率</text>
					</view>
					<view class="stat-item">
						<text class="stat-num">{{ member.active === 1 ? '活跃' : '不活跃' }}</text>
						<text class="stat-label">活跃状态</text>
					</view>
				</view>
				
				<view class="activity-list" v-if="activities.length > 0">
					<view class="activity-item" v-for="activity in activities" :key="activity.id">
						<text class="activity-name">{{ activity.activityTitle }}</text>
						<text class="activity-time">{{ activity.participateTime }}</text>
					</view>
				</view>
				<view class="empty-small" v-else>
					暂无活动记录
				</view>
			</view>

			<view class="action-bar" v-if="isManager">
				<button class="btn btn-secondary" @click="toggleActive">
					<text class="iconfont" :class="member.active === 1 ? 'icon-star' : 'icon-star-outline'"></text>
					{{ member.active === 1 ? '取消活跃' : '标记活跃' }}
				</button>
				<button class="btn btn-secondary" @click="changeRole">
					<text class="iconfont icon-setting"></text>
					修改权限
				</button>
				<button class="btn btn-danger" @click="removeMember">
					<text class="iconfont icon-delete"></text>
					移除成员
				</button>
			</view>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="modal" v-if="showRoleModal" @click="showRoleModal = false">
			<view class="modal-content" @click.stop>
				<text class="modal-title">修改成员权限</text>
				<view class="role-list">
					<view 
						class="role-option" 
						:class="{ active: selectedRole === 0 }"
						@click="selectedRole = 0"
					>
						<text class="role-name">普通成员</text>
						<text class="role-desc">可参与社团活动，浏览社团内容</text>
						<text class="iconfont icon-check" v-if="selectedRole === 0"></text>
					</view>
					<view 
						class="role-option" 
						:class="{ active: selectedRole === 1 }"
						@click="selectedRole = 1"
					>
						<text class="role-name">部门负责人</text>
						<text class="role-desc">可管理本部门成员，发布部门活动</text>
						<text class="iconfont icon-check" v-if="selectedRole === 1"></text>
					</view>
					<view 
						class="role-option" 
						:class="{ active: selectedRole === 2 }"
						@click="selectedRole = 2"
					>
						<text class="role-name">社长</text>
						<text class="role-desc">拥有社团最高管理权限</text>
						<text class="iconfont icon-check" v-if="selectedRole === 2"></text>
					</view>
				</view>
				<view class="modal-actions">
					<button class="modal-btn btn-cancel" @click="showRoleModal = false">取消</button>
					<button class="modal-btn btn-confirm" @click="confirmChangeRole">确认修改</button>
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
				memberId: null,
				userId: null,
				member: null,
				activities: [],
				activityStats: { total: 0, rate: 0 },
				loading: false,
				isManager: false,
				showRoleModal: false,
				selectedRole: 0
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.memberId = options.memberId
			this.userId = options.userId
			this.checkManager()
			this.loadDetail()
		},
		methods: {
			checkManager() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id === this.clubId)
			},
			getRoleText(role) {
				const map = { 0: '普通成员', 1: '部门负责人', 2: '社长' }
				return map[role] || '成员'
			},
			async loadDetail() {
				this.loading = true
				try {
					const members = await memberApi.getList(this.clubId, { pageSize: 1000 })
					const list = members.list || members || []
					this.member = list.find(m => m.id == this.memberId) || list[0]
					
					if (this.member) {
						this.selectedRole = this.member.role
						
						try {
							const res = await memberApi.getActivityRecords(this.clubId, this.memberId)
							this.activities = res || []
							this.activityStats = {
								total: this.activities.length,
								rate: this.activities.length > 0 ? Math.min(100, Math.round((this.activities.length / 5) * 100)) : 0
							}
						} catch (e) {
							console.error(e)
						}
					}
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			async toggleActive() {
				try {
					const newActive = this.member.active === 1 ? 0 : 1
					await memberApi.markActive(this.clubId, this.memberId, { active: newActive })
					this.member.active = newActive
					uni.showToast({ 
						title: newActive === 1 ? '已标记为活跃' : '已取消活跃标记', 
						icon: 'success' 
					})
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '操作失败', icon: 'none' })
				}
			},
			changeRole() {
				this.selectedRole = this.member.role
				this.showRoleModal = true
			},
			async confirmChangeRole() {
				try {
					await memberApi.update(this.clubId, this.memberId, { role: this.selectedRole })
					this.member.role = this.selectedRole
					this.showRoleModal = false
					uni.showToast({ title: '权限修改成功', icon: 'success' })
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '修改失败', icon: 'none' })
				}
			},
			removeMember() {
				uni.showModal({
					title: '确认移除',
					content: `确定要将"${this.member.userName}"移出社团吗？`,
					success: async (res) => {
						if (res.confirm) {
							try {
								await memberApi.remove(this.clubId, this.memberId)
								uni.showToast({ title: '已移除', icon: 'success' })
								setTimeout(() => {
									uni.navigateBack()
								}, 1500)
							} catch (e) {
								console.error(e)
								uni.showToast({ title: '移除失败', icon: 'none' })
							}
						}
					}
				})
			}
		}
	}
</script>

<style scoped>
	.page-wrapper {
		min-height: 100vh;
		background: #f5f5f5;
	}

	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 140rpx;
	}

	.header {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 60rpx 30rpx 40rpx;
		text-align: center;
		color: #fff;
	}

	.member-avatar {
		width: 160rpx;
		height: 160rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.3);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 56rpx;
		font-weight: bold;
		margin: 0 auto 20rpx;
		border: 4rpx solid rgba(255, 255, 255, 0.5);
	}

	.member-name {
		font-size: 36rpx;
		font-weight: bold;
		display: block;
		margin-bottom: 15rpx;
	}

	.member-tags {
		display: flex;
		justify-content: center;
		gap: 10rpx;
		flex-wrap: wrap;
	}

	.role-tag,
	.active-tag,
	.dept-tag {
		font-size: 22rpx;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 0.3);
		color: #fff;
	}

	.active-tag {
		background: rgba(82, 196, 26, 0.4);
	}

	.info-card {
		background: #fff;
		margin: -20rpx 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 10rpx 30rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.info-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.info-item:last-child {
		border-bottom: none;
	}

	.info-label {
		font-size: 26rpx;
		color: #999;
	}

	.info-value {
		font-size: 26rpx;
		color: #333;
	}

	.section-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin: 0 20rpx 20rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
	}

	.section-title .iconfont {
		color: #667eea;
		margin-right: 10rpx;
	}

	.stats-row {
		display: flex;
		justify-content: space-around;
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.stat-item {
		text-align: center;
	}

	.stat-num {
		font-size: 32rpx;
		font-weight: bold;
		color: #667eea;
		display: block;
		margin-bottom: 5rpx;
	}

	.stat-label {
		font-size: 22rpx;
		color: #999;
	}

	.activity-list {
		margin-top: 10rpx;
	}

	.activity-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 15rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
	}

	.activity-item:last-child {
		border-bottom: none;
	}

	.activity-name {
		font-size: 26rpx;
		color: #333;
	}

	.activity-time {
		font-size: 22rpx;
		color: #999;
	}

	.empty-small {
		text-align: center;
		padding: 30rpx 0;
		font-size: 26rpx;
		color: #999;
	}

	.action-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx;
		display: flex;
		gap: 15rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.btn {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		font-size: 26rpx;
		border: none;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.btn .iconfont {
		margin-right: 8rpx;
	}

	.btn-secondary {
		background: #f5f5f5;
		color: #666;
	}

	.btn-danger {
		background: #fff1f0;
		color: #ff4d4f;
	}

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
		width: 650rpx;
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

	.role-list {
		margin-bottom: 30rpx;
	}

	.role-option {
		display: flex;
		align-items: center;
		padding: 25rpx;
		border: 2rpx solid #f0f0f0;
		border-radius: 12rpx;
		margin-bottom: 15rpx;
		position: relative;
	}

	.role-option.active {
		border-color: #667eea;
		background: #f8f9ff;
	}

	.role-name {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		display: block;
		margin-bottom: 5rpx;
	}

	.role-desc {
		font-size: 24rpx;
		color: #999;
	}

	.role-option .icon-check {
		position: absolute;
		right: 25rpx;
		top: 50%;
		transform: translateY(-50%);
		color: #667eea;
		font-size: 32rpx;
	}

	.modal-actions {
		display: flex;
		gap: 20rpx;
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
		.action-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
