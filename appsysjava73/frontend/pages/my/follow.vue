<template>
	<view class="container">
		<view class="club-list" v-if="follows.length > 0">
			<view 
				class="club-card" 
				v-for="club in follows" 
				:key="club.id"
				@click="goClubDetail(club)"
			>
				<view class="club-logo">
					{{ club.name ? club.name.charAt(0) : '社' }}
				</view>
				<view class="club-info">
					<view class="club-header">
						<text class="club-name">{{ club.name }}</text>
						<text class="school-tag" v-if="club.schoolName !== currentSchool">跨校</text>
					</view>
					<text class="club-category">{{ club.categoryName }}</text>
					<view class="club-meta">
						<text class="meta-text">{{ club.memberCount }}成员</text>
						<text class="meta-dot">·</text>
						<text class="meta-text">{{ club.schoolName }}</text>
					</view>
				</view>
				<button 
					class="btn-follow followed" 
					:loading="unfollowingId === club.id"
					@click.stop="toggleFollow(club)"
				>
					已关注
				</button>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-heart"></text>
			</text>
			<text class="empty-text">您还没有关注任何社团</text>
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
				follows: [],
				loading: false,
				unfollowingId: null,
				currentSchool: '清华大学'
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
					const res = await followApi.getMyFollows()
					this.follows = res.list || this.getMockData()
				} catch (e) {
					console.error(e)
					this.follows = this.getMockData()
				} finally {
					this.loading = false
				}
			},
			getMockData() {
				return [
					{
						id: 1,
						name: '人工智能创新协会',
						categoryName: '学术科技',
						memberCount: 128,
						schoolName: '北京大学',
						logo: null
					},
					{
						id: 2,
						name: '微电影社',
						categoryName: '文化艺术',
						memberCount: 76,
						schoolName: '清华大学',
						logo: null
					},
					{
						id: 3,
						name: '足球协会',
						categoryName: '体育竞技',
						memberCount: 156,
						schoolName: '浙江大学',
						logo: null
					},
					{
						id: 4,
						name: '创业孵化俱乐部',
						categoryName: '创新创业',
						memberCount: 89,
						schoolName: '复旦大学',
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
			},
			async toggleFollow(club) {
				uni.showModal({
					title: '确认取消关注',
					content: `确定要取消关注"${club.name}"吗？`,
					success: async (res) => {
						if (res.confirm) {
							this.unfollowingId = club.id
							try {
								await followApi.toggleFollow(club.id)
								this.follows = this.follows.filter(f => f.id !== club.id)
								uni.showToast({ title: '已取消关注', icon: 'success' })
							} catch (e) {
								console.error(e)
								uni.showToast({ title: '操作失败', icon: 'none' })
							} finally {
								this.unfollowingId = null
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
	}

	.club-list {
		padding: 20rpx;
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

	.school-tag {
		font-size: 20rpx;
		color: #fa8c16;
		background: rgba(250, 140, 22, 0.1);
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
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

	.btn-follow {
		padding: 12rpx 25rpx;
		font-size: 24rpx;
		border-radius: 30rpx;
		line-height: 1.4;
		border: none;
		flex-shrink: 0;
	}

	.btn-follow.followed {
		background: #f0f0f0;
		color: #999;
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
