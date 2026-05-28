<template>
	<view class="container">
		<view class="header">
			<text class="title">社团大事记</text>
			<button class="btn-add" @click="goAdd" v-if="isManager">
				<text class="iconfont icon-plus"></text>
				添加
			</button>
		</view>

		<view class="timeline" v-if="milestones.length > 0">
			<view class="timeline-item" v-for="(item, idx) in milestones" :key="item.id">
				<view class="timeline-left">
					<view class="timeline-dot"></view>
					<view class="timeline-line" v-if="idx < milestones.length - 1"></view>
				</view>
				<view class="timeline-content">
					<view class="event-date">{{ item.eventDate }}</view>
					<view class="event-card">
						<view class="event-header">
							<text class="event-title">{{ item.title }}</text>
							<view class="event-actions" v-if="isManager">
								<text class="edit-btn" @click.stop="goEdit(item)">编辑</text>
								<text class="delete-btn" @click.stop="deleteItem(item)">删除</text>
							</view>
						</view>
						<text class="event-desc" v-if="item.description">{{ item.description }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="presidents-section" v-if="presidents.length > 0">
			<view class="section-header">
				<text class="section-title">历任社长</text>
			</view>
			<view class="president-list">
				<view class="president-card" v-for="president in presidents" :key="president.id">
					<view class="president-avatar">
						{{ president.name ? president.name.charAt(0) : '社' }}
					</view>
					<view class="president-info">
						<text class="president-name">{{ president.name }}</text>
						<text class="president-term">{{ president.startDate }} - {{ president.endDate }}</text>
						<text class="president-desc" v-if="president.achievement">{{ president.achievement }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-if="milestones.length === 0 && presidents.length === 0 && !loading">
			<text class="empty-text">暂无大事记记录</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { milestoneApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				milestones: [],
				presidents: [],
				loading: false,
				isManager: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkPermission()
			this.loadData()
		},
		onPullDownRefresh() {
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			checkPermission() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id == this.clubId)
			},
			async loadData() {
				this.loading = true
				try {
					const [milestoneRes, presidentRes] = await Promise.all([
						milestoneApi.getList(this.clubId, { pageSize: 100 }),
						milestoneApi.getPastPresidents(this.clubId)
					])
					this.milestones = milestoneRes.list || []
					this.presidents = presidentRes.list || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			goAdd() {
				uni.navigateTo({
					url: `/pages/milestone/edit?clubId=${this.clubId}`
				})
			},
			goEdit(item) {
				uni.navigateTo({
					url: `/pages/milestone/edit?clubId=${this.clubId}&id=${item.id}`
				})
			},
			deleteItem(item) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除这条大事记吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await milestoneApi.delete(this.clubId, item.id)
								this.milestones = this.milestones.filter(m => m.id !== item.id)
								uni.showToast({ title: '已删除', icon: 'success' })
							} catch (e) {
								console.error(e)
								uni.showToast({ title: '删除失败', icon: 'none' })
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
		padding-bottom: 40rpx;
	}

	.header {
		background: #fff;
		padding: 20rpx 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.btn-add {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		font-size: 26rpx;
		padding: 12rpx 25rpx;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		border: none;
		line-height: 1.4;
	}

	.btn-add .iconfont {
		margin-right: 8rpx;
	}

	.timeline {
		padding: 20rpx 20rpx 0;
	}

	.timeline-item {
		display: flex;
	}

	.timeline-left {
		width: 60rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.timeline-dot {
		width: 24rpx;
		height: 24rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-radius: 50%;
		margin-top: 20rpx;
		flex-shrink: 0;
	}

	.timeline-line {
		width: 4rpx;
		flex: 1;
		background: #e0e0e0;
		margin: 10rpx 0;
		min-height: 100rpx;
	}

	.timeline-content {
		flex: 1;
		padding-left: 20rpx;
		padding-bottom: 30rpx;
	}

	.event-date {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 10rpx;
		font-weight: 500;
	}

	.event-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.event-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 15rpx;
	}

	.event-title {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		flex: 1;
	}

	.event-actions {
		display: flex;
		gap: 20rpx;
		margin-left: 20rpx;
		flex-shrink: 0;
	}

	.edit-btn {
		font-size: 24rpx;
		color: #667eea;
	}

	.delete-btn {
		font-size: 24rpx;
		color: #ff4d4f;
	}

	.event-desc {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
	}

	.presidents-section {
		background: #fff;
		margin: 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.section-header {
		margin-bottom: 25rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
	}

	.president-list {
		margin-bottom: 20rpx;
	}

	.president-card {
		display: flex;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.president-card:last-child {
		border-bottom: none;
		padding-bottom: 0;
	}

	.president-card:first-child {
		padding-top: 0;
	}

	.president-avatar {
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
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.president-info {
		flex: 1;
	}

	.president-name {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.president-term {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 8rpx;
	}

	.president-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.5;
	}

	.empty,
	.loading {
		text-align: center;
		padding: 120rpx 0;
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
