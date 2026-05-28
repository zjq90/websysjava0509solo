<template>
	<view class="container">
		<view class="tabs">
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'joined' }"
				@click="currentTab = 'joined'"
			>已加入</text>
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'managed' }"
				@click="currentTab = 'managed'"
			>我管理的</text>
		</view>

		<view class="club-list" v-if="filteredClubs.length > 0">
			<view 
				class="club-card" 
				v-for="club in filteredClubs" 
				:key="club.id"
				@click="goClubDetail(club)"
			>
				<view class="club-logo">
					{{ club.name ? club.name.charAt(0) : '社' }}
				</view>
				<view class="club-info">
					<view class="club-header">
						<text class="club-name">{{ club.name }}</text>
						<text class="club-role" v-if="club.role">{{ getRoleText(club.role) }}</text>
					</view>
					<text class="club-category">{{ club.categoryName }}</text>
					<view class="club-meta">
						<text class="meta-text">{{ club.memberCount }}成员</text>
						<text class="meta-dot">·</text>
						<text class="meta-text">{{ club.schoolName }}</text>
					</view>
				</view>
				<text class="iconfont icon-arrow-right"></text>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-team"></text>
			</text>
			<text class="empty-text">{{ currentTab === 'joined' ? '您还没有加入任何社团' : '您还没有管理的社团' }}</text>
			<button class="btn-find" @click="goFindClub">去发现社团</button>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { followApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				currentTab: 'joined',
				clubs: [],
				loading: false
			}
		},
		computed: {
			filteredClubs() {
				if (this.currentTab === 'managed') {
					return store.state.managedClubs || []
				}
				return this.clubs
			}
		},
		onLoad() {
			this.loadData()
		},
		onPullDownRefresh() {
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			async loadData() {
				this.loading = true
				try {
					const res = await followApi.getMyClubs()
					this.clubs = res.list || this.getMockData()
				} catch (e) {
					console.error(e)
					this.clubs = this.getMockData()
				} finally {
					this.loading = false
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
			getMockData() {
				return [
					{
						id: 1,
						name: '编程技术协会',
						categoryName: '学术科技',
						memberCount: 156,
						schoolName: '清华大学',
						role: 'PRESIDENT',
						logo: null
					},
					{
						id: 2,
						name: '摄影协会',
						categoryName: '文化艺术',
						memberCount: 89,
						schoolName: '清华大学',
						role: 'NORMAL',
						logo: null
					},
					{
						id: 3,
						name: '志愿者协会',
						categoryName: '公益实践',
						memberCount: 234,
						schoolName: '清华大学',
						role: 'NORMAL',
						logo: null
					}
				]
			},
			goClubDetail(club) {
				uni.navigateTo({
					url: `/pages/club/detail?id=${club.id}`
				})
			},
			goFindClub() {
				uni.switchTab({
					url: '/pages/club/list'
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
	}

	.tabs {
		display: flex;
		background: #fff;
		padding: 0 60rpx;
		margin-bottom: 20rpx;
	}

	.tab-item {
		flex: 1;
		text-align: center;
		padding: 30rpx 0;
		font-size: 30rpx;
		color: #666;
		position: relative;
	}

	.tab-item.active {
		color: #667eea;
		font-weight: 500;
	}

	.tab-item.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 60rpx;
		height: 6rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 3rpx;
	}

	.club-list {
		padding: 0 20rpx;
	}

	.club-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.club-logo {
		width: 100rpx;
		height: 100rpx;
		border-radius: 20rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 40rpx;
		font-weight: bold;
		margin-right: 25rpx;
		flex-shrink: 0;
	}

	.club-info {
		flex: 1;
		min-width: 0;
	}

	.club-header {
		display: flex;
		align-items: center;
		margin-bottom: 8rpx;
	}

	.club-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-right: 15rpx;
	}

	.club-role {
		font-size: 22rpx;
		color: #667eea;
		background: rgba(102, 126, 234, 0.1);
		padding: 4rpx 12rpx;
		border-radius: 10rpx;
		flex-shrink: 0;
	}

	.club-category {
		font-size: 24rpx;
		color: #667eea;
		display: block;
		margin-bottom: 8rpx;
	}

	.club-meta {
		display: flex;
		align-items: center;
		gap: 10rpx;
	}

	.meta-text {
		font-size: 22rpx;
		color: #999;
	}

	.meta-dot {
		font-size: 22rpx;
		color: #ddd;
	}

	.club-card .icon-arrow-right {
		font-size: 28rpx;
		color: #ccc;
		flex-shrink: 0;
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
		display: block;
		margin-bottom: 40rpx;
	}

	.btn-find {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		font-size: 28rpx;
		padding: 20rpx 50rpx;
		border-radius: 40rpx;
		border: none;
		display: inline-block;
	}

	.loading {
		text-align: center;
		padding: 100rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
