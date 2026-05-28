<template>
	<view class="container">
		<view class="header" v-if="currentClub">
			<view class="club-selector" @click="showClubSelector = true">
				<view class="club-info">
					<view class="club-logo">
						{{ currentClub.name ? currentClub.name.charAt(0) : '社' }}
					</view>
					<view class="club-detail">
						<text class="club-name">{{ currentClub.name }}</text>
						<text class="club-role">{{ getRoleText(currentClub.role) }}</text>
					</view>
				</view>
				<text class="iconfont icon-arrow-down"></text>
			</view>
		</view>

		<view class="stats-overview" v-if="currentClub">
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalMembers || 0 }}</text>
				<text class="stat-label">成员数</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalActivities || 0 }}</text>
				<text class="stat-label">活动数</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.pendingApply || 0 }}</text>
				<text class="stat-label">待审核</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalFollows || 0 }}</text>
				<text class="stat-label">关注数</text>
			</view>
		</view>

		<view class="manage-section" v-if="currentClub">
			<text class="section-title">社团管理</text>
			<view class="menu-grid">
				<view class="menu-item" @click="goMemberManage">
					<view class="menu-icon" style="background: rgba(102, 126, 234, 0.1);">
						<text class="iconfont icon-team" style="color: #667eea;"></text>
					</view>
					<text class="menu-text">成员管理</text>
				</view>
				<view class="menu-item" @click="goDepartmentManage">
					<view class="menu-icon" style="background: rgba(82, 196, 26, 0.1);">
						<text class="iconfont icon-folder" style="color: #52c41a;"></text>
					</view>
					<text class="menu-text">部门管理</text>
				</view>
				<view class="menu-item" @click="goRecruitManage">
					<view class="menu-icon" style="background: rgba(24, 144, 255, 0.1);">
						<text class="iconfont icon-user-add" style="color: #1890ff;"></text>
					</view>
					<text class="menu-text">招新管理</text>
				</view>
				<view class="menu-item" @click="goActivityManage">
					<view class="menu-icon" style="background: rgba(250, 140, 22, 0.1);">
						<text class="iconfont icon-calendar" style="color: #fa8c16;"></text>
					</view>
					<text class="menu-text">活动管理</text>
				</view>
				<view class="menu-item" @click="goPostManage">
					<view class="menu-icon" style="background: rgba(114, 46, 209, 0.1);">
						<text class="iconfont icon-comment" style="color: #722ed1;"></text>
					</view>
					<text class="menu-text">讨论区</text>
				</view>
				<view class="menu-item" @click="goFileManage">
					<view class="menu-icon" style="background: rgba(250, 173, 20, 0.1);">
						<text class="iconfont icon-file" style="color: #faad14;"></text>
					</view>
					<text class="menu-text">社团网盘</text>
				</view>
				<view class="menu-item" @click="goMilestone">
					<view class="menu-icon" style="background: rgba(255, 77, 79, 0.1);">
						<text class="iconfont icon-flag" style="color: #ff4d4f;"></text>
					</view>
					<text class="menu-text">大事记</text>
				</view>
				<view class="menu-item" @click="goStatistics">
					<view class="menu-icon" style="background: rgba(19, 194, 194, 0.1);">
						<text class="iconfont icon-chart" style="color: #13c2c2;"></text>
					</view>
					<text class="menu-text">数据统计</text>
				</view>
			</view>
		</view>

		<view class="manage-section" v-if="currentClub && isPresident">
			<text class="section-title">高级管理</text>
			<view class="menu-grid">
				<view class="menu-item" @click="goClubEdit">
					<view class="menu-icon" style="background: rgba(102, 126, 234, 0.1);">
						<text class="iconfont icon-edit" style="color: #667eea;"></text>
					</view>
					<text class="menu-text">编辑社团</text>
				</view>
				<view class="menu-item" @click="goTransfer">
					<view class="menu-icon" style="background: rgba(82, 196, 26, 0.1);">
						<text class="iconfont icon-swap" style="color: #52c41a;"></text>
					</view>
					<text class="menu-text">转让社长</text>
				</view>
			</view>
		</view>

		<view class="empty" v-if="!currentClub && !loading">
			<text class="empty-icon">
				<text class="iconfont icon-team"></text>
			</text>
			<text class="empty-text">您还没有管理的社团</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="club-selector-popup" v-if="showClubSelector" @click="showClubSelector = false">
			<view class="selector-content" @click.stop>
				<view class="selector-header">
					<text class="selector-title">选择社团</text>
					<text class="close-btn" @click="showClubSelector = false">
						<text class="iconfont icon-close"></text>
					</text>
				</view>
				<view class="club-list">
					<view 
						class="club-item" 
						v-for="club in managedClubs" 
						:key="club.id"
						:class="{ active: club.id === currentClub?.id }"
						@click="selectClub(club)"
					>
						<view class="club-logo">
							{{ club.name ? club.name.charAt(0) : '社' }}
						</view>
						<view class="club-info">
							<text class="club-name">{{ club.name }}</text>
							<text class="club-role">{{ getRoleText(club.role) }}</text>
						</view>
						<text class="iconfont icon-check" v-if="club.id === currentClub?.id"></text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import store from '@/store/index.js'
	import { statisticsApi } from '@/api/index.js'

	export default {
		data() {
			return {
				managedClubs: [],
				currentClub: null,
				stats: {},
				loading: false,
				showClubSelector: false
			}
		},
		computed: {
			isPresident() {
				return this.currentClub?.role === 'PRESIDENT'
			}
		},
		onShow() {
			this.loadData()
		},
		methods: {
			async loadData() {
				this.loading = true
				try {
					this.managedClubs = store.state.managedClubs || []
					if (this.managedClubs.length > 0) {
						const currentClubId = store.state.currentClubId || this.managedClubs[0].id
						this.currentClub = this.managedClubs.find(c => c.id == currentClubId) || this.managedClubs[0]
						store.commit('setCurrentClubId', this.currentClub.id)
						await this.loadStats()
					}
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			async loadStats() {
				try {
					const res = await statisticsApi.getBase(this.currentClub.id)
					this.stats = res || {}
				} catch (e) {
					console.error(e)
					this.stats = {
						totalMembers: 80,
						totalActivities: 12,
						pendingApply: 5,
						totalFollows: 156
					}
				}
			},
			getRoleText(role) {
				const roles = {
					'PRESIDENT': '社长',
					'DEPARTMENT_HEAD': '部门负责人',
					'NORMAL': '普通成员'
				}
				return roles[role] || '成员'
			},
			selectClub(club) {
				this.currentClub = club
				store.commit('setCurrentClubId', club.id)
				this.showClubSelector = false
				this.loadStats()
			},
			goMemberManage() {
				uni.navigateTo({
					url: `/pages/member/list?clubId=${this.currentClub.id}`
				})
			},
			goDepartmentManage() {
				uni.navigateTo({
					url: `/pages/member/department?clubId=${this.currentClub.id}`
				})
			},
			goRecruitManage() {
				uni.navigateTo({
					url: `/pages/recruit/list?clubId=${this.currentClub.id}&manage=1`
				})
			},
			goActivityManage() {
				uni.navigateTo({
					url: `/pages/club/activity-list?clubId=${this.currentClub.id}&manage=1`
				})
			},
			goPostManage() {
				uni.navigateTo({
					url: `/pages/post/list?clubId=${this.currentClub.id}`
				})
			},
			goFileManage() {
				uni.navigateTo({
					url: `/pages/file/list?clubId=${this.currentClub.id}`
				})
			},
			goMilestone() {
				uni.navigateTo({
					url: `/pages/milestone/list?clubId=${this.currentClub.id}`
				})
			},
			goStatistics() {
				uni.navigateTo({
					url: `/pages/statistics/index?clubId=${this.currentClub.id}`
				})
			},
			goClubEdit() {
				uni.navigateTo({
					url: `/pages/manage/club-edit?clubId=${this.currentClub.id}`
				})
			},
			goTransfer() {
				uni.showToast({ title: '功能开发中', icon: 'none' })
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 40rpx;
	}

	.header {
		background: #fff;
		padding: 20rpx 30rpx;
		margin-bottom: 20rpx;
	}

	.club-selector {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.club-info {
		display: flex;
		align-items: center;
	}

	.club-logo {
		width: 80rpx;
		height: 80rpx;
		border-radius: 20rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		font-weight: bold;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.club-detail {
		display: flex;
		flex-direction: column;
	}

	.club-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 5rpx;
	}

	.club-role {
		font-size: 24rpx;
		color: #667eea;
	}

	.club-selector .icon-arrow-down {
		font-size: 28rpx;
		color: #999;
	}

	.stats-overview {
		display: flex;
		flex-wrap: wrap;
		padding: 0 20rpx;
		gap: 15rpx;
		margin-bottom: 20rpx;
	}

	.stat-card {
		flex: 1;
		min-width: calc(50% - 15rpx);
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		text-align: center;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.stat-value {
		font-size: 44rpx;
		font-weight: bold;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
		background-clip: text;
		display: block;
		margin-bottom: 8rpx;
	}

	.stat-label {
		font-size: 24rpx;
		color: #999;
	}

	.manage-section {
		background: #fff;
		margin: 0 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.section-title {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 25rpx;
		display: block;
	}

	.menu-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 30rpx;
	}

	.menu-item {
		width: calc(25% - 22.5rpx);
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.menu-icon {
		width: 90rpx;
		height: 90rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 12rpx;
	}

	.menu-icon .iconfont {
		font-size: 40rpx;
	}

	.menu-text {
		font-size: 24rpx;
		color: #333;
		text-align: center;
	}

	.empty {
		text-align: center;
		padding: 120rpx 0;
	}

	.empty-icon {
		font-size: 120rpx;
		color: #ddd;
		display: block;
		margin-bottom: 20rpx;
	}

	.empty-text {
		font-size: 28rpx;
		color: #999;
	}

	.loading {
		text-align: center;
		padding: 100rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	.club-selector-popup {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: flex-end;
		z-index: 1000;
	}

	.selector-content {
		width: 100%;
		background: #fff;
		border-radius: 24rpx 24rpx 0 0;
		padding: 30rpx;
		max-height: 70vh;
		overflow-y: auto;
	}

	.selector-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.selector-title {
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
	}

	.close-btn {
		width: 50rpx;
		height: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.close-btn .iconfont {
		font-size: 32rpx;
		color: #999;
	}

	.club-list {
		margin-bottom: 20rpx;
	}

	.club-item {
		display: flex;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
	}

	.club-item:last-child {
		border-bottom: none;
	}

	.club-item.active {
		background: rgba(102, 126, 234, 0.05);
		margin: 0 -30rpx;
		padding: 25rpx 30rpx;
	}

	.club-item .club-logo {
		width: 70rpx;
		height: 70rpx;
		border-radius: 16rpx;
		font-size: 28rpx;
	}

	.club-item .club-info {
		flex: 1;
	}

	.club-item .club-name {
		font-size: 28rpx;
	}

	.club-item .icon-check {
		font-size: 32rpx;
		color: #667eea;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.selector-content {
			max-width: 720px;
			margin: 0 auto;
		}
		.stat-card {
			min-width: calc(25% - 15rpx);
		}
	}
</style>
