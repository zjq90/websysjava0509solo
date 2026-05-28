<template>
	<view class="container">
		<view class="tabs">
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '' }"
				@click="currentStatus = ''; loadData()"
			>
				全部
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '0' }"
				@click="currentStatus = '0'; loadData()"
			>
				待审核
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '1' }"
				@click="currentStatus = '1'; loadData()"
			>
				已通过
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '2' }"
				@click="currentStatus = '2'; loadData()"
			>
				已驳回
			</view>
		</view>

		<view class="apply-list" v-if="applies.length > 0">
			<view 
				class="apply-card" 
				v-for="apply in applies" 
				:key="apply.id"
			>
				<view class="apply-header">
					<view class="club-info" @click="goClubDetail(apply.clubId)">
						<image :src="apply.clubLogo" mode="aspectFill" class="club-logo" />
						<text class="club-name">{{ apply.clubName }}</text>
					</view>
					<text class="apply-status" :class="'status-' + apply.status">
						{{ getStatusText(apply.status) }}
					</text>
				</view>

				<view class="apply-content">
					<view class="info-row">
						<text class="info-label">申请部门:</text>
						<text class="info-value">{{ apply.departmentName || '未选择' }}</text>
					</view>
					<view class="info-row">
						<text class="info-label">申请时间:</text>
						<text class="info-value">{{ apply.applyTime }}</text>
					</view>
					<view class="info-row" v-if="apply.reviewTime">
						<text class="info-label">审核时间:</text>
						<text class="info-value">{{ apply.reviewTime }}</text>
					</view>
					<view class="info-row" v-if="apply.reviewMessage">
						<text class="info-label">审核留言:</text>
						<text class="info-value message">{{ apply.reviewMessage }}</text>
					</view>
				</view>

				<view class="apply-footer">
					<text class="apply-tip" v-if="apply.status === 0">等待审核中，请耐心等待...</text>
					<text class="apply-tip success" v-else-if="apply.status === 1">恭喜！您的申请已通过，请及时联系社团负责人</text>
					<text class="apply-tip reject" v-else-if="apply.status === 2">很遗憾，您的申请未通过，再接再厉！</text>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无申请记录</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { recruitApi } from '@/api/index.js'

	export default {
		data() {
			return {
				clubId: null,
				applies: [],
				currentStatus: '',
				loading: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.loadData()
		},
		onPullDownRefresh() {
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '待审核', 1: '已通过', 2: '已驳回' }
				return map[status] || '未知'
			},
			async loadData() {
				this.loading = true
				try {
					const params = {
						pageSize: 100,
						status: this.currentStatus !== '' ? parseInt(this.currentStatus) : undefined
					}
					const res = await recruitApi.getMyApplyList(this.clubId || 0, params)
					this.applies = res.list || res || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			goClubDetail(clubId) {
				uni.navigateTo({
					url: `/pages/club/detail?id=${clubId}`
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

	.tabs {
		display: flex;
		background: #fff;
		padding: 0 20rpx;
		margin-bottom: 20rpx;
	}

	.tab-item {
		flex: 1;
		text-align: center;
		padding: 25rpx 0;
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
		width: 60rpx;
		height: 4rpx;
		background: #667eea;
		border-radius: 2rpx;
	}

	.apply-list {
		padding: 0 20rpx;
	}

	.apply-card {
		background: #fff;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.apply-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx;
		background: linear-gradient(135deg, #f8f9ff 0%, #f0f5ff 100%);
	}

	.club-info {
		display: flex;
		align-items: center;
	}

	.club-logo {
		width: 70rpx;
		height: 70rpx;
		border-radius: 12rpx;
		margin-right: 15rpx;
	}

	.club-name {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
	}

	.apply-status {
		font-size: 24rpx;
		padding: 8rpx 20rpx;
		border-radius: 24rpx;
	}

	.status-0 {
		background: #fff7e6;
		color: #fa8c16;
	}

	.status-1 {
		background: #f6ffed;
		color: #52c41a;
	}

	.status-2 {
		background: #fff1f0;
		color: #ff4d4f;
	}

	.apply-content {
		padding: 25rpx;
	}

	.info-row {
		display: flex;
		margin-bottom: 15rpx;
	}

	.info-row:last-child {
		margin-bottom: 0;
	}

	.info-label {
		font-size: 26rpx;
		color: #999;
		width: 140rpx;
		flex-shrink: 0;
	}

	.info-value {
		flex: 1;
		font-size: 26rpx;
		color: #333;
	}

	.info-value.message {
		background: #f8f9ff;
		padding: 15rpx;
		border-radius: 8rpx;
		line-height: 1.6;
	}

	.apply-footer {
		padding: 20rpx 25rpx;
		background: #fafafa;
		border-top: 1rpx solid #f0f0f0;
	}

	.apply-tip {
		font-size: 24rpx;
		color: #999;
	}

	.apply-tip.success {
		color: #52c41a;
	}

	.apply-tip.reject {
		color: #ff4d4f;
	}

	.empty,
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
