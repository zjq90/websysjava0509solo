<template>
	<view class="container">
		<view class="tabs">
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'joined' }"
				@click="currentTab = 'joined'; loadData()"
			>已报名</text>
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'interested' }"
				@click="currentTab = 'interested'; loadData()"
			>感兴趣</text>
		</view>

		<view class="activity-list" v-if="activities.length > 0">
			<view 
				class="activity-card" 
				v-for="activity in activities" 
				:key="activity.id"
				@click="goActivityDetail(activity)"
			>
				<view class="activity-cover">
					<image 
						:src="activity.cover || 'https://picsum.photos/200/200?random=' + activity.id" 
						mode="aspectFill"
						class="cover-img"
					/>
					<view class="status-tag" :class="activity.status">
						{{ getStatusText(activity.status) }}
					</view>
				</view>
				<view class="activity-info">
					<text class="activity-title">{{ activity.name }}</text>
					<view class="activity-meta">
						<text class="meta-item">
							<text class="iconfont icon-calendar"></text>
							{{ activity.startTime }}
						</text>
						<text class="meta-item">
							<text class="iconfont icon-location"></text>
							{{ activity.location }}
						</text>
					</view>
					<view class="activity-footer">
						<text class="club-name">{{ activity.clubName }}</text>
						<text class="participant-count">{{ activity.participantCount }}人参与</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-calendar"></text>
			</text>
			<text class="empty-text">暂无{{ currentTab === 'joined' ? '报名的' : '感兴趣的' }}活动</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentTab: 'joined',
				activities: [],
				loading: false
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
					await new Promise(resolve => setTimeout(resolve, 500))
					this.activities = this.getMockData()
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			getStatusText(status) {
				const statusMap = {
					'upcoming': '即将开始',
					'ongoing': '进行中',
					'ended': '已结束'
				}
				return statusMap[status] || status
			},
			getMockData() {
				return [
					{
						id: 1,
						name: '春季编程大赛',
						cover: null,
						startTime: '2024-06-15 14:00',
						location: '清华主楼101',
						clubName: '编程技术协会',
						participantCount: 128,
						status: 'upcoming'
					},
					{
						id: 2,
						name: '技术分享会 - AI大模型应用',
						cover: null,
						startTime: '2024-06-10 19:00',
						location: '清华主楼203',
						clubName: '编程技术协会',
						participantCount: 89,
						status: 'upcoming'
					},
					{
						id: 3,
						name: '摄影技巧讲座',
						cover: null,
						startTime: '2024-05-28 14:00',
						location: '美术学院101',
						clubName: '摄影协会',
						participantCount: 56,
						status: 'ongoing'
					},
					{
						id: 4,
						name: '春季运动会',
						cover: null,
						startTime: '2024-05-20 08:00',
						location: '东大操场',
						clubName: '足球协会',
						participantCount: 200,
						status: 'ended'
					}
				]
			},
			goActivityDetail(activity) {
				uni.navigateTo({
					url: `/pages/club/activity-detail?id=${activity.id}`
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

	.activity-list {
		padding: 0 20rpx;
	}

	.activity-card {
		background: #fff;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.activity-cover {
		position: relative;
		height: 200rpx;
	}

	.cover-img {
		width: 100%;
		height: 100%;
	}

	.status-tag {
		position: absolute;
		top: 15rpx;
		right: 15rpx;
		font-size: 22rpx;
		padding: 6rpx 15rpx;
		border-radius: 8rpx;
		color: #fff;
	}

	.status-tag.upcoming {
		background: #52c41a;
	}

	.status-tag.ongoing {
		background: #1890ff;
	}

	.status-tag.ended {
		background: #999;
	}

	.activity-info {
		padding: 25rpx;
	}

	.activity-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 15rpx;
	}

	.activity-meta {
		margin-bottom: 15rpx;
	}

	.meta-item {
		font-size: 24rpx;
		color: #999;
		display: flex;
		align-items: center;
		margin-bottom: 8rpx;
	}

	.meta-item .iconfont {
		margin-right: 8rpx;
		font-size: 24rpx;
	}

	.activity-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-top: 15rpx;
		border-top: 1rpx solid #f5f5f5;
	}

	.club-name {
		font-size: 24rpx;
		color: #667eea;
	}

	.participant-count {
		font-size: 24rpx;
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
