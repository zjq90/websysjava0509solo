<template>
	<view class="container" v-if="activity">
		<image :src="activity.coverImage" mode="aspectFill" class="activity-cover" />
		
		<view class="content">
			<view class="activity-header">
				<text class="activity-title">{{ activity.title }}</text>
				<text class="activity-status" :class="'status-' + activity.status">
					{{ getStatusText(activity.status) }}
				</text>
			</view>

			<view class="club-info" @click="goClubDetail">
				<image :src="activity.clubLogo" mode="aspectFill" class="club-logo" />
				<text class="club-name">{{ activity.clubName }}</text>
				<text class="iconfont icon-right arrow"></text>
			</view>

			<view class="info-card">
				<view class="info-item">
					<text class="info-icon">
						<text class="iconfont icon-time"></text>
					</text>
					<view class="info-content">
						<text class="info-label">活动时间</text>
						<text class="info-value">{{ activity.startTime }} - {{ activity.endTime }}</text>
					</view>
				</view>
				<view class="info-item">
					<text class="info-icon">
						<text class="iconfont icon-location"></text>
					</text>
					<view class="info-content">
						<text class="info-label">活动地点</text>
						<text class="info-value">{{ activity.location }}</text>
					</view>
				</view>
				<view class="info-item">
					<text class="info-icon">
						<text class="iconfont icon-user"></text>
					</text>
					<view class="info-content">
						<text class="info-label">参与人数</text>
						<text class="info-value">{{ activity.participantCount }}/{{ activity.maxParticipants }}人</text>
					</view>
				</view>
			</view>

			<view class="section-card">
				<view class="section-title">活动介绍</view>
				<text class="section-content">{{ activity.description }}</text>
			</view>

			<view class="section-card" v-if="activity.requirements">
				<view class="section-title">参与要求</view>
				<text class="section-content">{{ activity.requirements }}</text>
			</view>

			<view class="participant-section" v-if="participants.length > 0">
				<view class="section-title">已报名成员</view>
				<scroll-view scroll-x class="participant-scroll">
					<view class="participant-item" v-for="p in participants" :key="p.id">
						<view class="participant-avatar">
							{{ p.userName ? p.userName.charAt(0) : '用' }}
						</view>
						<text class="participant-name">{{ p.userName }}</text>
					</view>
				</scroll-view>
			</view>
		</view>

		<view class="bottom-bar">
			<view class="participant-count">
				<text class="count-num">{{ activity.participantCount }}</text>
				<text class="count-label">人已报名</text>
			</view>
			<button 
				class="btn-signup" 
				:class="{ disabled: !canSignup, signed: isSignedUp }"
				:disabled="!canSignup"
				@click="handleSignup"
			>
				{{ getButtonText() }}
			</button>
		</view>
	</view>
</template>

<script>
	import { activityApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				activityId: null,
				activity: null,
				participants: [],
				isSignedUp: false,
				loading: false
			}
		},
		onLoad(options) {
			this.activityId = options.id
			this.loadDetail()
		},
		computed: {
			canSignup() {
				if (!this.activity) return false
				if (this.activity.status !== 0) return false
				if (this.isSignedUp) return false
				if (this.activity.participantCount >= this.activity.maxParticipants) return false
				return true
			}
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '未开始', 1: '进行中', 2: '已结束' }
				return map[status] || '未知'
			},
			getButtonText() {
				if (this.isSignedUp) return '已报名'
				if (this.activity?.status === 2) return '活动已结束'
				if (this.activity?.participantCount >= this.activity?.maxParticipants) return '名额已满'
				return '立即报名'
			},
			async loadDetail() {
				this.loading = true
				try {
					this.activity = await activityApi.getDetail(this.activityId)
					const res = await activityApi.getParticipants(this.activityId, { pageSize: 10 })
					this.participants = res.list || []
					this.checkSignupStatus()
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '加载失败', icon: 'none' })
				} finally {
					this.loading = false
				}
			},
			checkSignupStatus() {
				const userId = store.state.userInfo?.id
				this.isSignedUp = this.participants.some(p => p.userId === userId)
			},
			async handleSignup() {
				if (!this.canSignup) return
				try {
					await activityApi.signup(this.activityId)
					this.isSignedUp = true
					this.activity.participantCount++
					uni.showToast({ title: '报名成功', icon: 'success' })
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '报名失败', icon: 'none' })
				}
			},
			goClubDetail() {
				uni.navigateTo({
					url: `/pages/club/detail?id=${this.activity.clubId}`
				})
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

	.activity-cover {
		width: 100%;
		height: 400rpx;
	}

	.content {
		padding: 30rpx;
		margin-top: -30rpx;
		background: #f5f5f5;
		border-radius: 30rpx 30rpx 0 0;
		position: relative;
	}

	.activity-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 20rpx;
	}

	.activity-title {
		flex: 1;
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		margin-right: 15rpx;
	}

	.activity-status {
		font-size: 24rpx;
		padding: 8rpx 20rpx;
		border-radius: 24rpx;
		flex-shrink: 0;
	}

	.status-0 {
		background: #e6f7ff;
		color: #1890ff;
	}

	.status-1 {
		background: #f6ffed;
		color: #52c41a;
	}

	.status-2 {
		background: #f5f5f5;
		color: #999;
	}

	.club-info {
		display: flex;
		align-items: center;
		background: #fff;
		padding: 20rpx;
		border-radius: 12rpx;
		margin-bottom: 20rpx;
	}

	.club-logo {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		margin-right: 15rpx;
	}

	.club-name {
		flex: 1;
		font-size: 28rpx;
		color: #333;
	}

	.arrow {
		font-size: 24rpx;
		color: #999;
	}

	.info-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
	}

	.info-item {
		display: flex;
		align-items: flex-start;
		padding: 15rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.info-item:last-child {
		border-bottom: none;
	}

	.info-icon {
		width: 60rpx;
		height: 60rpx;
		background: #f0f5ff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.info-icon .iconfont {
		color: #667eea;
		font-size: 28rpx;
	}

	.info-content {
		flex: 1;
	}

	.info-label {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 6rpx;
	}

	.info-value {
		font-size: 28rpx;
		color: #333;
	}

	.section-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 20rpx;
	}

	.section-content {
		font-size: 26rpx;
		color: #666;
		line-height: 1.8;
	}

	.participant-section {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.participant-scroll {
		white-space: nowrap;
	}

	.participant-item {
		display: inline-block;
		text-align: center;
		margin-right: 30rpx;
	}

	.participant-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		font-weight: bold;
		margin: 0 auto 10rpx;
	}

	.participant-name {
		font-size: 22rpx;
		color: #666;
	}

	.bottom-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx 30rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.participant-count {
		margin-right: 30rpx;
	}

	.count-num {
		font-size: 36rpx;
		font-weight: bold;
		color: #667eea;
		display: block;
	}

	.count-label {
		font-size: 22rpx;
		color: #999;
	}

	.btn-signup {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 40rpx;
		font-size: 30rpx;
		border: none;
	}

	.btn-signup.disabled {
		background: #ccc;
	}

	.btn-signup.signed {
		background: #52c41a;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.bottom-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
