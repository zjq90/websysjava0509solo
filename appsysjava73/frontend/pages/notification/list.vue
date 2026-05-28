<template>
	<view class="container">
		<view class="tabs">
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'all' }"
				@click="currentTab = 'all'; loadData()"
			>全部</text>
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'activity' }"
				@click="currentTab = 'activity'; loadData()"
			>活动通知</text>
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'recruit' }"
				@click="currentTab = 'recruit'; loadData()"
			>招新通知</text>
			<text 
				class="tab-item" 
				:class="{ active: currentTab === 'system' }"
				@click="currentTab = 'system'; loadData()"
			>系统通知</text>
		</view>

		<view class="notification-list" v-if="notifications.length > 0">
			<view 
				class="notification-card" 
				v-for="notification in notifications" 
				:key="notification.id"
				:class="{ unread: notification.isRead === 0 }"
				@click="handleRead(notification)"
			>
				<view class="notification-icon" :class="'type-' + notification.type">
					<text class="iconfont" :class="getIcon(notification.type)"></text>
					<view class="unread-dot" v-if="notification.isRead === 0"></view>
				</view>
				<view class="notification-content">
					<view class="notification-header">
						<text class="notification-title">{{ notification.title }}</text>
						<text class="notification-time">{{ notification.createTime }}</text>
					</view>
					<text class="notification-desc">{{ notification.content }}</text>
					<text class="notification-club" v-if="notification.clubName">来自：{{ notification.clubName }}</text>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-notification"></text>
			</text>
			<text class="empty-text">暂无通知</text>
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
	import { notificationApi } from '@/api/index.js'

	export default {
		data() {
			return {
				currentTab: 'all',
				notifications: [],
				pageNum: 1,
				pageSize: 20,
				total: 0,
				hasMore: true,
				loading: false
			}
		},
		onShow() {
			this.pageNum = 1
			this.hasMore = true
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
			getIcon(type) {
				const icons = {
					activity: 'icon-calendar',
					recruit: 'icon-user-add',
					system: 'icon-info',
					all: 'icon-bell'
				}
				return icons[type] || 'icon-bell'
			},
			async loadData() {
				this.loading = true
				try {
					const type = this.currentTab === 'all' ? null : this.currentTab
					const params = {
						type,
						pageNum: this.pageNum,
						pageSize: this.pageSize
					}
					const res = await notificationApi.getList(params)
					const list = res.list || []
					
					if (this.pageNum === 1) {
						this.notifications = list
					} else {
						this.notifications = [...this.notifications, ...list]
					}
					this.total = res.total || 0
					this.hasMore = this.notifications.length < this.total
				} catch (e) {
					console.error(e)
					if (this.pageNum === 1) {
						this.notifications = this.getMockData()
					}
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
			async handleRead(notification) {
				if (notification.isRead === 0) {
					try {
						await notificationApi.markAsRead(notification.id)
						notification.isRead = 1
					} catch (e) {
						console.error(e)
					}
				}
				
				if (notification.type === 'activity' && notification.relatedId) {
					uni.navigateTo({
						url: `/pages/club/activity-detail?id=${notification.relatedId}`
					})
				}
			},
			getMockData() {
				return [
					{
						id: 1,
						type: 'activity',
						title: '活动报名提醒',
						content: '您关注的"编程技术协会"将举办"春季编程大赛"，点击查看详情',
						clubName: '编程技术协会',
						relatedId: 1,
						isRead: 0,
						createTime: '2024-05-28 10:30'
					},
					{
						id: 2,
						type: 'recruit',
						title: '招新申请审核通过',
						content: '恭喜您，您加入"摄影协会"的申请已通过审核！',
						clubName: '摄影协会',
						relatedId: 2,
						isRead: 0,
						createTime: '2024-05-27 14:20'
					},
					{
						id: 3,
						type: 'activity',
						title: '活动开始提醒',
						content: '您报名的"技术分享会"将于今天下午14:00开始，请准时参加',
						clubName: '编程技术协会',
						relatedId: 3,
						isRead: 1,
						createTime: '2024-05-27 09:00'
					},
					{
						id: 4,
						type: 'system',
						title: '系统通知',
						content: '欢迎使用大学生社团管理系统，祝您使用愉快！',
						clubName: null,
						relatedId: null,
						isRead: 1,
						createTime: '2024-05-25 08:00'
					}
				]
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

	.tabs {
		display: flex;
		background: #fff;
		padding: 0 20rpx;
		margin-bottom: 20rpx;
		position: sticky;
		top: 0;
		z-index: 100;
	}

	.tab-item {
		flex: 1;
		text-align: center;
		padding: 30rpx 0;
		font-size: 28rpx;
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
		width: 40rpx;
		height: 6rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 3rpx;
	}

	.notification-list {
		padding: 0 20rpx;
	}

	.notification-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		display: flex;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.notification-card.unread {
		background: #fff;
		border-left: 6rpx solid #667eea;
	}

	.notification-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		position: relative;
		flex-shrink: 0;
	}

	.notification-icon.type-activity {
		background: rgba(102, 126, 234, 0.1);
	}
	.notification-icon.type-activity .iconfont {
		color: #667eea;
	}

	.notification-icon.type-recruit {
		background: rgba(82, 196, 26, 0.1);
	}
	.notification-icon.type-recruit .iconfont {
		color: #52c41a;
	}

	.notification-icon.type-system {
		background: rgba(24, 144, 255, 0.1);
	}
	.notification-icon.type-system .iconfont {
		color: #1890ff;
	}

	.notification-icon .iconfont {
		font-size: 36rpx;
	}

	.unread-dot {
		position: absolute;
		top: -5rpx;
		right: -5rpx;
		width: 16rpx;
		height: 16rpx;
		background: #ff4d4f;
		border-radius: 50%;
	}

	.notification-content {
		flex: 1;
		min-width: 0;
	}

	.notification-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 10rpx;
	}

	.notification-title {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		flex: 1;
		margin-right: 20rpx;
	}

	.notification-time {
		font-size: 22rpx;
		color: #999;
		flex-shrink: 0;
	}

	.notification-desc {
		font-size: 26rpx;
		color: #666;
		line-height: 1.5;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
		margin-bottom: 8rpx;
	}

	.notification-club {
		font-size: 22rpx;
		color: #999;
	}

	.empty,
	.loading,
	.load-more {
		text-align: center;
		padding: 120rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	.empty-icon {
		font-size: 100rpx;
		color: #ddd;
		display: block;
		margin-bottom: 20rpx;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.tabs {
			max-width: 720px;
			margin: 0 auto 20rpx;
		}
	}
</style>
