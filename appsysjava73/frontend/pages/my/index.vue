<template>
	<view class="container">
		<view class="header">
			<view class="user-info" v-if="userInfo">
				<view class="user-avatar">
					{{ userInfo.realName ? userInfo.realName.charAt(0) : '用' }}
				</view>
				<view class="user-detail">
					<text class="user-name">{{ userInfo.realName }}</text>
					<text class="user-school">{{ userInfo.schoolName }}</text>
					<text class="user-department">{{ userInfo.department }} · {{ userInfo.major }}</text>
				</view>
				<view class="edit-btn" @click="goEditProfile">
					<text class="iconfont icon-edit"></text>
				</view>
			</view>
			
			<view class="stats-row">
				<view class="stat-item" @click="goMyClub">
					<text class="stat-value">{{ myClubsCount }}</text>
					<text class="stat-label">我的社团</text>
				</view>
				<view class="stat-item" @click="goMyFollow">
					<text class="stat-value">{{ myFollowsCount }}</text>
					<text class="stat-label">我的关注</text>
				</view>
				<view class="stat-item" @click="goMyActivity">
					<text class="stat-value">{{ myActivitiesCount }}</text>
					<text class="stat-label">我的活动</text>
				</view>
			</view>
		</view>

		<view class="menu-section">
			<text class="section-title">常用功能</text>
			<view class="menu-list">
				<view class="menu-item" @click="goMyClub">
					<view class="menu-icon" style="background: rgba(102, 126, 234, 0.1);">
						<text class="iconfont icon-team" style="color: #667eea;"></text>
					</view>
					<text class="menu-text">我的社团</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
				<view class="menu-item" @click="goMyFollow">
					<view class="menu-icon" style="background: rgba(255, 77, 79, 0.1);">
						<text class="iconfont icon-heart" style="color: #ff4d4f;"></text>
					</view>
					<text class="menu-text">我的关注</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
				<view class="menu-item" @click="goMyActivity">
					<view class="menu-icon" style="background: rgba(82, 196, 26, 0.1);">
						<text class="iconfont icon-calendar" style="color: #52c41a;"></text>
					</view>
					<text class="menu-text">我的活动</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
				<view class="menu-item" @click="goMyApply">
					<view class="menu-icon" style="background: rgba(24, 144, 255, 0.1);">
						<text class="iconfont icon-file" style="color: #1890ff;"></text>
					</view>
					<text class="menu-text">我的申请</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
			</view>
		</view>

		<view class="menu-section">
			<text class="section-title">社团管理</text>
			<view class="menu-list" v-if="managedClubs.length > 0">
				<view class="menu-item" @click="goManageClub(club)" v-for="club in managedClubs" :key="club.id">
					<view class="club-avatar">
						{{ club.name ? club.name.charAt(0) : '社' }}
					</view>
					<view class="club-info">
						<text class="club-name">{{ club.name }}</text>
						<text class="club-role">{{ getRoleText(club.role) }}</text>
					</view>
					<text class="iconfont icon-arrow-right"></text>
				</view>
			</view>
			<view class="no-manage" v-else>
				<text class="no-manage-text">您还没有管理的社团</text>
			</view>
		</view>

		<view class="menu-section">
			<text class="section-title">设置</text>
			<view class="menu-list">
				<view class="menu-item" @click="goSetting">
					<view class="menu-icon" style="background: rgba(114, 46, 209, 0.1);">
						<text class="iconfont icon-setting" style="color: #722ed1;"></text>
					</view>
					<text class="menu-text">账号设置</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
				<view class="menu-item" @click="goAbout">
					<view class="menu-icon" style="background: rgba(250, 173, 20, 0.1);">
						<text class="iconfont icon-info" style="color: #faad14;"></text>
					</view>
					<text class="menu-text">关于我们</text>
					<text class="iconfont icon-arrow-right"></text>
				</view>
			</view>
		</view>

		<button class="btn-logout" @click="logout">退出登录</button>
	</view>
</template>

<script>
	import store from '@/store/index.js'
	import { authApi } from '@/api/index.js'

	export default {
		data() {
			return {
				userInfo: null,
				managedClubs: [],
				myClubsCount: 0,
				myFollowsCount: 5,
				myActivitiesCount: 12
			}
		},
		onShow() {
			this.loadUserInfo()
		},
		methods: {
			loadUserInfo() {
				this.userInfo = store.state.userInfo
				this.managedClubs = store.state.managedClubs || []
				this.myClubsCount = this.managedClubs.length
			},
			getRoleText(role) {
				const roles = {
					'PRESIDENT': '社长',
					'DEPARTMENT_HEAD': '部门负责人',
					'NORMAL': '普通成员'
				}
				return roles[role] || '成员'
			},
			goEditProfile() {
				uni.showToast({ title: '功能开发中', icon: 'none' })
			},
			goMyClub() {
				uni.navigateTo({
					url: '/pages/my/club'
				})
			},
			goMyFollow() {
				uni.navigateTo({
					url: '/pages/my/follow'
				})
			},
			goMyActivity() {
				uni.navigateTo({
					url: '/pages/my/activity'
				})
			},
			goMyApply() {
				uni.navigateTo({
					url: '/pages/recruit/my-apply'
				})
			},
			goManageClub(club) {
				store.commit('setCurrentClubId', club.id)
				uni.switchTab({
					url: '/pages/manage/index'
				})
			},
			goSetting() {
				uni.showToast({ title: '功能开发中', icon: 'none' })
			},
			goAbout() {
				uni.showModal({
					title: '关于我们',
					content: '大学生社团管理系统 v1.0\n\n致力于为高校社团提供便捷的数字化管理服务，提升社团运营效率。',
					showCancel: false
				})
			},
			logout() {
				uni.showModal({
					title: '确认退出',
					content: '确定要退出登录吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await authApi.logout()
							} catch (e) {
								console.error(e)
							}
							store.commit('logout')
							uni.reLaunch({
								url: '/pages/login/login'
							})
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
		padding-bottom: 60rpx;
	}

	.header {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx 30rpx;
		margin-bottom: 20rpx;
	}

	.user-info {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		position: relative;
	}

	.user-avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.2);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 48rpx;
		font-weight: bold;
		margin-right: 25rpx;
		border: 4rpx solid rgba(255, 255, 255, 0.3);
		flex-shrink: 0;
	}

	.user-detail {
		flex: 1;
		min-width: 0;
	}

	.user-name {
		font-size: 36rpx;
		font-weight: bold;
		color: #fff;
		display: block;
		margin-bottom: 8rpx;
	}

	.user-school {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.9);
		display: block;
		margin-bottom: 5rpx;
	}

	.user-department {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.7);
	}

	.edit-btn {
		width: 60rpx;
		height: 60rpx;
		background: rgba(255, 255, 255, 0.2);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.edit-btn .iconfont {
		color: #fff;
		font-size: 28rpx;
	}

	.stats-row {
		display: flex;
		background: rgba(255, 255, 255, 0.15);
		border-radius: 20rpx;
		padding: 25rpx 0;
	}

	.stat-item {
		flex: 1;
		text-align: center;
	}

	.stat-value {
		font-size: 40rpx;
		font-weight: bold;
		color: #fff;
		display: block;
		margin-bottom: 5rpx;
	}

	.stat-label {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.menu-section {
		background: #fff;
		margin: 0 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 20rpx 0;
	}

	.section-title {
		font-size: 26rpx;
		color: #999;
		padding: 10rpx 30rpx 20rpx;
		display: block;
	}

	.menu-list {
		padding: 0 30rpx;
	}

	.menu-item {
		display: flex;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
	}

	.menu-item:last-child {
		border-bottom: none;
	}

	.menu-icon {
		width: 70rpx;
		height: 70rpx;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.menu-icon .iconfont {
		font-size: 32rpx;
	}

	.menu-text {
		flex: 1;
		font-size: 28rpx;
		color: #333;
	}

	.menu-item .icon-arrow-right {
		font-size: 24rpx;
		color: #ccc;
	}

	.club-avatar {
		width: 70rpx;
		height: 70rpx;
		border-radius: 16rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		font-weight: bold;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.club-info {
		flex: 1;
	}

	.club-name {
		font-size: 28rpx;
		color: #333;
		display: block;
		margin-bottom: 5rpx;
	}

	.club-role {
		font-size: 24rpx;
		color: #667eea;
		background: rgba(102, 126, 234, 0.1);
		padding: 3rpx 12rpx;
		border-radius: 10rpx;
	}

	.no-manage {
		text-align: center;
		padding: 40rpx 0;
	}

	.no-manage-text {
		font-size: 26rpx;
		color: #999;
	}

	.btn-logout {
		width: calc(100% - 60rpx);
		height: 90rpx;
		line-height: 90rpx;
		background: #fff;
		color: #ff4d4f;
		border: 2rpx solid #ff4d4f;
		border-radius: 45rpx;
		font-size: 30rpx;
		margin: 40rpx 30rpx 0;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.header {
			border-radius: 0 0 20rpx 20rpx;
		}
	}
</style>
