<template>
	<view class="container">
		<view class="header-card" v-if="club">
			<image :src="club.logo" mode="aspectFill" class="club-logo" />
			<view class="club-info">
				<text class="club-name">{{ club.name }}</text>
				<text class="club-desc">{{ club.description }}</text>
			</view>
		</view>

		<view class="recruit-list" v-if="recruits.length > 0">
			<view 
				class="recruit-card" 
				v-for="recruit in recruits" 
				:key="recruit.id"
				@click="goDetail(recruit.id)"
			>
				<view class="recruit-header">
					<text class="recruit-title">{{ recruit.title }}</text>
					<text class="recruit-status" :class="'status-' + recruit.status">
						{{ getStatusText(recruit.status) }}
					</text>
				</view>
				
				<view class="recruit-meta">
					<view class="meta-item">
						<text class="iconfont icon-user"></text>
						<text class="meta-text">招新名额: {{ recruit.quota }}人</text>
					</view>
					<view class="meta-item">
						<text class="iconfont icon-time"></text>
						<text class="meta-text">截止: {{ recruit.deadline }}</text>
					</view>
				</view>

				<view class="progress-section">
					<view class="progress-info">
						<text class="progress-text">已申请 {{ recruit.applyCount }}/{{ recruit.quota }}人</text>
						<text class="progress-percent">{{ getProgress(recruit) }}%</text>
					</view>
					<view class="progress-bar">
						<view 
							class="progress-fill" 
							:style="{ width: getProgress(recruit) + '%' }"
						></view>
					</view>
				</view>

				<view class="recruit-footer">
					<text class="remaining">剩余 {{ recruit.quota - recruit.applyCount }} 个名额</text>
					<text class="view-detail">查看详情 <text class="iconfont icon-right"></text></text>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无招新信息</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { recruitApi, clubApi } from '@/api/index.js'

	export default {
		data() {
			return {
				clubId: null,
				club: null,
				recruits: [],
				loading: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.loadData()
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '未开始', 1: '进行中', 2: '已结束' }
				return map[status] || '未知'
			},
			getProgress(recruit) {
				if (recruit.quota === 0) return 0
				return Math.min(100, Math.round((recruit.applyCount / recruit.quota) * 100))
			},
			async loadData() {
				this.loading = true
				try {
					if (this.clubId) {
						this.club = await clubApi.getDetail(this.clubId)
						if (this.club.club) this.club = this.club.club
					}
					const res = await recruitApi.getList(this.clubId, { pageSize: 100 })
					this.recruits = res.list || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			goDetail(id) {
				uni.navigateTo({
					url: `/pages/recruit/detail?clubId=${this.clubId}&recruitId=${id}`
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

	.header-card {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx;
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.club-logo {
		width: 120rpx;
		height: 120rpx;
		border-radius: 16rpx;
		margin-right: 20rpx;
		flex-shrink: 0;
		border: 4rpx solid rgba(255, 255, 255, 0.3);
	}

	.club-info {
		flex: 1;
		color: #fff;
	}

	.club-name {
		font-size: 32rpx;
		font-weight: bold;
		display: block;
		margin-bottom: 10rpx;
	}

	.club-desc {
		font-size: 24rpx;
		opacity: 0.9;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}

	.recruit-list {
		padding: 0 20rpx;
	}

	.recruit-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.recruit-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.recruit-title {
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
		flex: 1;
		margin-right: 15rpx;
	}

	.recruit-status {
		font-size: 24rpx;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
		flex-shrink: 0;
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
		background: #f5f5f5;
		color: #999;
	}

	.recruit-meta {
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
		font-size: 26rpx;
		color: #666;
	}

	.progress-section {
		margin-bottom: 20rpx;
	}

	.progress-info {
		display: flex;
		justify-content: space-between;
		margin-bottom: 10rpx;
	}

	.progress-text {
		font-size: 24rpx;
		color: #666;
	}

	.progress-percent {
		font-size: 24rpx;
		color: #667eea;
		font-weight: 500;
	}

	.progress-bar {
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

	.recruit-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-top: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.remaining {
		font-size: 24rpx;
		color: #ff4d4f;
	}

	.view-detail {
		font-size: 24rpx;
		color: #667eea;
	}

	.view-detail .iconfont {
		font-size: 20rpx;
		margin-left: 5rpx;
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
