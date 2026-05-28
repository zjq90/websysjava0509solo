<template>
	<view class="container">
		<view class="header">
			<view class="header-top">
				<view class="greeting">
					<text class="greeting-text">你好，{{ userName }}</text>
					<text class="greeting-sub">欢迎使用社团管理系统</text>
				</view>
				<view class="user-avatar" @click="goProfile">
					<image :src="userAvatar" mode="aspectFill" class="avatar" />
				</view>
			</view>

			<view class="search-bar" @click="goSearch">
				<text class="iconfont icon-search"></text>
				<text class="search-placeholder">搜索社团、活动...</text>
			</view>
		</view>

		<view class="quick-menu">
			<view class="menu-item" @click="goClubList">
				<view class="menu-icon bg-blue">
					<text class="iconfont icon-club"></text>
				</view>
				<text class="menu-text">社团发现</text>
			</view>
			<view class="menu-item" @click="goMyClub">
				<view class="menu-icon bg-green">
					<text class="iconfont icon-user"></text>
				</view>
				<text class="menu-text">我的社团</text>
			</view>
			<view class="menu-item" @click="goActivity">
				<view class="menu-icon bg-orange">
					<text class="iconfont icon-activity"></text>
				</view>
				<text class="menu-text">活动中心</text>
			</view>
			<view class="menu-item" @click="goRecruit">
				<view class="menu-icon bg-purple">
					<text class="iconfont icon-recruit"></text>
				</view>
				<text class="menu-text">招新信息</text>
			</view>
		</view>

		<view class="section">
			<view class="section-header">
				<text class="section-title">热门社团</text>
				<text class="section-more" @click="goClubList">更多</text>
			</view>
			<view class="club-list">
				<view 
					class="club-card" 
					v-for="club in hotClubs" 
					:key="club.id"
					@click="goClubDetail(club.id)"
				>
					<image :src="club.logo" mode="aspectFill" class="club-logo" />
					<view class="club-info">
						<text class="club-name">{{ club.name }}</text>
						<view class="club-meta">
							<text class="tag tag-primary">{{ club.categoryName }}</text>
							<text class="club-count">{{ club.memberCount }}人</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="section">
			<view class="section-header">
				<text class="section-title">最新活动</text>
				<text class="section-more" @click="goActivity">更多</text>
			</view>
			<view class="activity-list">
				<view 
					class="activity-card" 
					v-for="activity in activities" 
					:key="activity.id"
					@click="goActivityDetail(activity.id)"
				>
					<image :src="activity.cover" mode="aspectFill" class="activity-cover" />
					<view class="activity-info">
						<text class="activity-title">{{ activity.title }}</text>
						<view class="activity-meta">
							<text class="iconfont icon-location"></text>
							<text class="meta-text">{{ activity.location }}</text>
						</view>
						<view class="activity-meta">
							<text class="iconfont icon-time"></text>
							<text class="meta-text">{{ activity.startTime | formatDate }} {{ activity.startTime | formatTime }}</text>
						</view>
						<view class="activity-footer">
							<text class="tag tag-success" v-if="activity.status === 1">进行中</text>
							<text class="tag tag-warning" v-else-if="activity.status === 0">即将开始</text>
							<text class="tag tag-default" v-else>已结束</text>
							<text class="participant-count">{{ activity.participantCount }}/{{ activity.maxParticipants }}人</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="section">
			<view class="section-header">
				<text class="section-title">招新中</text>
				<text class="section-more" @click="goRecruit">更多</text>
			</view>
			<view class="recruit-list">
				<view 
					class="recruit-card" 
					v-for="recruit in recruits" 
					:key="recruit.id"
					@click="goRecruitDetail(recruit.clubId, recruit.id)"
				>
					<view class="recruit-header">
						<text class="recruit-title">{{ recruit.title }}</text>
						<text class="recruit-status">招新中</text>
					</view>
					<text class="recruit-desc">{{ recruit.description }}</text>
					<view class="recruit-footer">
						<text class="recruit-quota">名额：{{ recruit.approvedCount }}/{{ recruit.quota }}</text>
						<text class="recruit-deadline">截止：{{ recruit.deadline | formatDate }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { clubApi, activityApi, recruitApi } from '@/api/index.js'

	export default {
		data() {
			return {
				hotClubs: [],
				activities: [],
				recruits: [],
				loading: false
			}
		},
		computed: {
			userName() {
				return this.$store.getters.realName || '同学'
			},
			userAvatar() {
				return this.$store.getters.avatar || '/static/default-avatar.png'
			}
		},
		onShow() {
			if (!this.$store.getters.isLoggedIn) {
				uni.reLaunch({
					url: '/pages/login/login'
				})
				return
			}
			this.loadData()
		},
		methods: {
			async loadData() {
				this.loading = true
				try {
					const [clubRes, activityRes] = await Promise.all([
						clubApi.getHot({ pageNum: 1, pageSize: 4 }),
						activityApi.getList({ pageNum: 1, pageSize: 3, status: 1 })
					])
					this.hotClubs = clubRes.list || []
					this.activities = activityRes.list || []

					for (let club of this.hotClubs) {
						try {
							const recruitRes = await recruitApi.getList(club.id, { pageNum: 1, pageSize: 1, status: 1 })
							if (recruitRes.list && recruitRes.list.length > 0) {
								this.recruits.push(...recruitRes.list)
							}
						} catch (e) {
							console.error(e)
						}
					}
					this.recruits = this.recruits.slice(0, 3)
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			goSearch() {
				uni.navigateTo({
					url: '/pages/club/list'
				})
			},
			goProfile() {
				uni.switchTab({
					url: '/pages/my/index'
				})
			},
			goClubList() {
				uni.switchTab({
					url: '/pages/club/list'
				})
			},
			goMyClub() {
				uni.navigateTo({
					url: '/pages/my/club'
				})
			},
			goActivity() {
				uni.navigateTo({
					url: '/pages/club/activity-list'
				})
			},
			goRecruit() {
				uni.navigateTo({
					url: '/pages/recruit/list'
				})
			},
			goClubDetail(id) {
				uni.navigateTo({
					url: `/pages/club/detail?id=${id}`
				})
			},
			goActivityDetail(id) {
				uni.navigateTo({
					url: `/pages/club/activity-detail?id=${id}`
				})
			},
			goRecruitDetail(clubId, recruitId) {
				uni.navigateTo({
					url: `/pages/recruit/detail?clubId=${clubId}&recruitId=${recruitId}`
				})
			}
		},
		filters: {
			formatDate(date) {
				if (!date) return ''
				const d = new Date(date)
				const month = String(d.getMonth() + 1).padStart(2, '0')
				const day = String(d.getDate()).padStart(2, '0')
				return `${month}-${day}`
			},
			formatTime(date) {
				if (!date) return ''
				const d = new Date(date)
				const hour = String(d.getHours()).padStart(2, '0')
				const minute = String(d.getMinutes()).padStart(2, '0')
				return `${hour}:${minute}`
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
	}

	.header {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx 60rpx;
		color: #fff;
	}

	.header-top {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.greeting-text {
		font-size: 36rpx;
		font-weight: bold;
		display: block;
	}

	.greeting-sub {
		font-size: 24rpx;
		opacity: 0.8;
		margin-top: 8rpx;
		display: block;
	}

	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		border: 3rpx solid rgba(255, 255, 255, 0.5);
	}

	.search-bar {
		display: flex;
		align-items: center;
		background: rgba(255, 255, 255, 0.2);
		border-radius: 50rpx;
		padding: 20rpx 30rpx;
	}

	.icon-search {
		font-size: 28rpx;
		margin-right: 15rpx;
	}

	.search-placeholder {
		font-size: 26rpx;
		opacity: 0.8;
	}

	.quick-menu {
		display: flex;
		justify-content: space-around;
		background: #fff;
		margin: -30rpx 20rpx 20rpx;
		border-radius: 20rpx;
		padding: 30rpx 0;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.menu-item {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.menu-icon {
		width: 90rpx;
		height: 90rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 15rpx;
		font-size: 40rpx;
		color: #fff;
	}

	.bg-blue {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	}

	.bg-green {
		background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
	}

	.bg-orange {
		background: linear-gradient(135deg, #faad14 0%, #ffc53d 100%);
	}

	.bg-purple {
		background: linear-gradient(135deg, #eb2f96 0%, #ff85c0 100%);
	}

	.menu-text {
		font-size: 24rpx;
		color: #333;
	}

	.section {
		padding: 20rpx;
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		padding: 0 10rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.section-more {
		font-size: 26rpx;
		color: #667eea;
	}

	.club-list {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
	}

	.club-card {
		width: 48%;
		background: #fff;
		border-radius: 16rpx;
		padding: 20rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.club-logo {
		width: 100%;
		height: 180rpx;
		border-radius: 12rpx;
		margin-bottom: 15rpx;
	}

	.club-name {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 10rpx;
	}

	.club-meta {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.tag {
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-radius: 20rpx;
	}

	.tag-primary {
		background: #e6f7ff;
		color: #1890ff;
	}

	.tag-success {
		background: #f6ffed;
		color: #52c41a;
	}

	.tag-warning {
		background: #fffbe6;
		color: #faad14;
	}

	.tag-default {
		background: #f5f5f5;
		color: #999;
	}

	.club-count {
		font-size: 22rpx;
		color: #999;
	}

	.activity-list {
		display: flex;
		flex-direction: column;
	}

	.activity-card {
		display: flex;
		background: #fff;
		border-radius: 16rpx;
		padding: 20rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.activity-cover {
		width: 160rpx;
		height: 160rpx;
		border-radius: 12rpx;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.activity-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.activity-title {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
	}

	.activity-meta {
		display: flex;
		align-items: center;
		margin-top: 8rpx;
	}

	.icon-location,
	.icon-time {
		font-size: 24rpx;
		color: #999;
		margin-right: 8rpx;
	}

	.meta-text {
		font-size: 22rpx;
		color: #999;
	}

	.activity-footer {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 10rpx;
	}

	.participant-count {
		font-size: 22rpx;
		color: #667eea;
	}

	.recruit-list {
		display: flex;
		flex-direction: column;
	}

	.recruit-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
		border-left: 6rpx solid #667eea;
	}

	.recruit-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 15rpx;
	}

	.recruit-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
	}

	.recruit-status {
		font-size: 22rpx;
		color: #52c41a;
		background: #f6ffed;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
	}

	.recruit-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.5;
		display: block;
		margin-bottom: 15rpx;
	}

	.recruit-footer {
		display: flex;
		justify-content: space-between;
	}

	.recruit-quota,
	.recruit-deadline {
		font-size: 22rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
