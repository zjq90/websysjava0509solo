<template>
	<view class="container" v-if="recruit">
		<view class="recruit-header">
			<text class="recruit-title">{{ recruit.title }}</text>
			<text class="recruit-status" :class="'status-' + recruit.status">
				{{ getStatusText(recruit.status) }}
			</text>
		</view>

		<view class="info-card">
			<view class="info-item">
				<text class="info-label">招新名额</text>
				<text class="info-value">{{ recruit.quota }}人</text>
			</view>
			<view class="info-item">
				<text class="info-label">已申请</text>
				<text class="info-value">{{ recruit.applyCount }}人</text>
			</view>
			<view class="info-item">
				<text class="info-label">剩余名额</text>
				<text class="info-value highlight">{{ recruit.quota - recruit.applyCount }}人</text>
			</view>
			<view class="info-item">
				<text class="info-label">截止时间</text>
				<text class="info-value">{{ recruit.deadline }}</text>
			</view>
		</view>

		<view class="section-card">
			<view class="section-title">
				<text class="iconfont icon-file"></text>
				招新要求
			</view>
			<text class="section-content">{{ recruit.requirements }}</text>
		</view>

		<view class="section-card" v-if="recruit.description">
			<view class="section-title">
				<text class="iconfont icon-info"></text>
				招新说明
			</view>
			<text class="section-content">{{ recruit.description }}</text>
		</view>

		<view class="section-card" v-if="departments && departments.length > 0">
			<view class="section-title">
				<text class="iconfont icon-team"></text>
				招新部门
			</view>
			<view class="dept-list">
				<view class="dept-item" v-for="dept in departments" :key="dept.id">
					<view class="dept-header">
						<text class="dept-name">{{ dept.name }}</text>
						<text class="dept-count">{{ dept.memberCount }}人</text>
					</view>
					<text class="dept-desc">{{ dept.description }}</text>
				</view>
			</view>
		</view>

		<view class="bottom-bar">
			<view class="progress-info">
				<text class="progress-text">申请进度</text>
				<view class="progress-bar">
					<view 
						class="progress-fill" 
						:style="{ width: getProgress() + '%' }"
					></view>
				</view>
				<text class="progress-percent">{{ getProgress() }}%</text>
			</view>
			<button 
				class="btn-apply" 
				:disabled="!canApply"
				:class="{ disabled: !canApply }"
				@click="goApply"
			>
				立即申请
			</button>
		</view>
	</view>
</template>

<script>
	import { recruitApi, memberApi } from '@/api/index.js'

	export default {
		data() {
			return {
				clubId: null,
				recruitId: null,
				recruit: null,
				departments: [],
				loading: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.recruitId = options.recruitId
			this.loadDetail()
		},
		computed: {
			canApply() {
				if (!this.recruit) return false
				if (this.recruit.status !== 1) return false
				if (this.recruit.applyCount >= this.recruit.quota) return false
				return true
			}
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '未开始', 1: '进行中', 2: '已结束' }
				return map[status] || '未知'
			},
			getProgress() {
				if (!this.recruit || this.recruit.quota === 0) return 0
				return Math.min(100, Math.round((this.recruit.applyCount / this.recruit.quota) * 100))
			},
			async loadDetail() {
				this.loading = true
				try {
					this.recruit = await recruitApi.getDetail(this.clubId, this.recruitId)
					const deptRes = await memberApi.getDepartments(this.clubId)
					this.departments = deptRes || []
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '加载失败', icon: 'none' })
				} finally {
					this.loading = false
				}
			},
			goApply() {
				if (!this.canApply) {
					uni.showToast({ title: '当前无法申请', icon: 'none' })
					return
				}
				uni.navigateTo({
					url: `/pages/recruit/apply?clubId=${this.clubId}&recruitId=${this.recruitId}`
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 180rpx;
	}

	.recruit-header {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 50rpx 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
	}

	.recruit-title {
		flex: 1;
		font-size: 36rpx;
		font-weight: bold;
		color: #fff;
		margin-right: 20rpx;
	}

	.recruit-status {
		font-size: 24rpx;
		padding: 8rpx 20rpx;
		border-radius: 24rpx;
		background: rgba(255, 255, 255, 0.3);
		color: #fff;
		flex-shrink: 0;
	}

	.status-0 {
		background: rgba(250, 140, 22, 0.3);
	}

	.status-1 {
		background: rgba(82, 196, 26, 0.3);
	}

	.status-2 {
		background: rgba(153, 153, 153, 0.3);
	}

	.info-card {
		background: #fff;
		margin: -30rpx 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
		display: flex;
		flex-wrap: wrap;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.info-item {
		width: 50%;
		margin-bottom: 20rpx;
	}

	.info-item:nth-child(odd) {
		padding-right: 15rpx;
	}

	.info-item:nth-child(even) {
		padding-left: 15rpx;
	}

	.info-label {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 8rpx;
	}

	.info-value {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.info-value.highlight {
		color: #ff4d4f;
	}

	.section-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin: 0 20rpx 20rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
	}

	.section-title .iconfont {
		color: #667eea;
		margin-right: 10rpx;
	}

	.section-content {
		font-size: 26rpx;
		color: #666;
		line-height: 1.8;
	}

	.dept-list {
		margin-top: 10rpx;
	}

	.dept-item {
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.dept-item:last-child {
		border-bottom: none;
	}

	.dept-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.dept-name {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
	}

	.dept-count {
		font-size: 24rpx;
		color: #999;
	}

	.dept-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.6;
	}

	.bottom-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx 30rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.progress-info {
		display: flex;
		align-items: center;
		margin-bottom: 15rpx;
		gap: 15rpx;
	}

	.progress-text {
		font-size: 24rpx;
		color: #666;
		flex-shrink: 0;
	}

	.progress-bar {
		flex: 1;
		height: 10rpx;
		background: #f0f0f0;
		border-radius: 5rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
		border-radius: 5rpx;
	}

	.progress-percent {
		font-size: 24rpx;
		color: #667eea;
		font-weight: 500;
		flex-shrink: 0;
	}

	.btn-apply {
		width: 100%;
		height: 80rpx;
		line-height: 80rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 40rpx;
		font-size: 30rpx;
		border: none;
	}

	.btn-apply.disabled {
		background: #ccc;
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
