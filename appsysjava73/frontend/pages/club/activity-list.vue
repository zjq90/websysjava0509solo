<template>
	<view class="container">
		<view class="filter-bar">
			<picker :range="statusOptions" range-key="name" @change="onStatusChange">
				<view class="filter-item">
					<text class="filter-text">{{ currentStatus.name }}</text>
					<text class="iconfont icon-down"></text>
				</view>
			</picker>
			<view class="filter-item" @click="showSortOptions">
				<text class="filter-text">{{ sortText }}</text>
				<text class="iconfont icon-down"></text>
			</view>
		</view>

		<view class="activity-list" v-if="activities.length > 0">
			<view 
				class="activity-card" 
				v-for="activity in activities" 
				:key="activity.id"
				@click="goDetail(activity.id)"
			>
				<image :src="activity.coverImage" mode="aspectFill" class="activity-cover" />
				<view class="activity-content">
					<view class="activity-header">
						<text class="activity-title">{{ activity.title }}</text>
						<text class="activity-status" :class="'status-' + activity.status">
							{{ getStatusText(activity.status) }}
						</text>
					</view>
					<text class="activity-club">{{ activity.clubName }}</text>
					<view class="activity-meta">
						<view class="meta-item">
							<text class="iconfont icon-time"></text>
							<text class="meta-text">{{ activity.startTime }}</text>
						</view>
						<view class="meta-item">
							<text class="iconfont icon-location"></text>
							<text class="meta-text">{{ activity.location }}</text>
						</view>
					</view>
					<view class="activity-footer">
						<view class="participant-info">
							<text class="iconfont icon-user"></text>
							<text class="participant-text">
								{{ activity.participantCount }}/{{ activity.maxParticipants }}人
							</text>
						</view>
						<view class="progress-bar">
							<view 
								class="progress-fill" 
								:style="{ width: getProgress(activity) + '%' }"
							></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无活动</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="load-more" v-if="hasMore && !loading" @click="loadMore">
			<text>加载更多</text>
		</view>
	</view>
</template>

<script>
	import { activityApi } from '@/api/index.js'

	export default {
		data() {
			return {
				clubId: null,
				activities: [],
				pageNum: 1,
				pageSize: 10,
				total: 0,
				hasMore: true,
				loading: false,
				statusOptions: [
					{ code: '', name: '全部状态' },
					{ code: 0, name: '未开始' },
					{ code: 1, name: '进行中' },
					{ code: 2, name: '已结束' }
				],
				currentStatus: { code: '', name: '全部状态' },
				sortBy: 'startTime',
				sortText: '按时间排序'
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.loadData()
		},
		onPullDownRefresh() {
			this.pageNum = 1
			this.hasMore = true
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '未开始', 1: '进行中', 2: '已结束' }
				return map[status] || '未知'
			},
			getProgress(activity) {
				if (activity.maxParticipants === 0) return 0
				return Math.min(100, (activity.participantCount / activity.maxParticipants) * 100)
			},
			onStatusChange(e) {
				const index = e.detail.value
				this.currentStatus = this.statusOptions[index]
				this.pageNum = 1
				this.hasMore = true
				this.loadData()
			},
			showSortOptions() {
				uni.showActionSheet({
					itemList: ['按时间排序', '按热度排序', '按参与人数排序'],
					success: (res) => {
						const sortMap = ['startTime', 'viewCount', 'participantCount']
						const textMap = ['按时间排序', '按热度排序', '按参与人数排序']
						this.sortBy = sortMap[res.tapIndex]
						this.sortText = textMap[res.tapIndex]
						this.pageNum = 1
						this.hasMore = true
						this.loadData()
					}
				})
			},
			async loadData() {
				this.loading = true
				try {
					const params = {
						pageNum: this.pageNum,
						pageSize: this.pageSize,
						clubId: this.clubId || undefined,
						status: this.currentStatus.code !== '' ? this.currentStatus.code : undefined
					}
					const res = await activityApi.getList(params)
					const list = res.list || []
					if (this.pageNum === 1) {
						this.activities = list
					} else {
						this.activities = [...this.activities, ...list]
					}
					this.total = res.total || 0
					this.hasMore = this.activities.length < this.total
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			loadMore() {
				if (this.hasMore && !this.loading) {
					this.pageNum++
					this.loadData()
				}
			},
			goDetail(id) {
				uni.navigateTo({
					url: `/pages/club/activity-detail?id=${id}`
				})
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

	.filter-bar {
		display: flex;
		background: #fff;
		padding: 20rpx 30rpx;
		gap: 20rpx;
		margin-bottom: 20rpx;
	}

	.filter-item {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 15rpx;
		background: #f5f5f5;
		border-radius: 30rpx;
		font-size: 26rpx;
		color: #666;
	}

	.filter-text {
		margin-right: 10rpx;
	}

	.icon-down {
		font-size: 20rpx;
		color: #999;
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
		width: 100%;
		height: 300rpx;
	}

	.activity-content {
		padding: 25rpx;
	}

	.activity-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 10rpx;
	}

	.activity-title {
		flex: 1;
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
		margin-right: 15rpx;
	}

	.activity-status {
		font-size: 22rpx;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
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

	.activity-club {
		font-size: 24rpx;
		color: #667eea;
		margin-bottom: 15rpx;
		display: block;
	}

	.activity-meta {
		margin-bottom: 20rpx;
	}

	.meta-item {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.meta-item .iconfont {
		color: #999;
		font-size: 24rpx;
		margin-right: 10rpx;
	}

	.meta-text {
		font-size: 24rpx;
		color: #666;
	}

	.activity-footer {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}

	.participant-info {
		display: flex;
		align-items: center;
		flex-shrink: 0;
	}

	.participant-info .iconfont {
		color: #999;
		font-size: 24rpx;
		margin-right: 8rpx;
	}

	.participant-text {
		font-size: 24rpx;
		color: #666;
	}

	.progress-bar {
		flex: 1;
		height: 12rpx;
		background: #f0f0f0;
		border-radius: 6rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
		border-radius: 6rpx;
		transition: width 0.3s;
	}

	.empty,
	.loading,
	.load-more {
		text-align: center;
		padding: 80rpx 0;
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
