<template>
	<view class="container">
		<view class="header-section">
			<text class="title">历任社长</text>
			<text class="subtitle">传承社团历史，铭记每一位领导者</text>
		</view>

		<view class="president-list" v-if="presidents.length > 0">
			<view class="president-card" v-for="(president, idx) in presidents" :key="president.id">
				<view class="order-badge">{{ idx + 1 }}</view>
				<view class="president-avatar">
					{{ president.name ? president.name.charAt(0) : '社' }}
				</view>
				<view class="president-info">
					<view class="president-header">
						<text class="president-name">{{ president.name }}</text>
						<text class="president-term">{{ president.startDate }} - {{ president.endDate }}</text>
					</view>
					<text class="president-desc" v-if="president.achievement">{{ president.achievement }}</text>
					<view class="president-meta" v-if="president.major">
						<text class="meta-text">{{ president.major }}</text>
						<text class="meta-dot">·</text>
						<text class="meta-text">{{ president.grade }}级</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-user"></text>
			</text>
			<text class="empty-text">暂无历任社长信息</text>
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
				presidents: [],
				loading: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
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
					const res = await milestoneApi.getPastPresidents(this.clubId)
					this.presidents = res.list || this.getMockData()
				} catch (e) {
					console.error(e)
					this.presidents = this.getMockData()
				} finally {
					this.loading = false
				}
			},
			getMockData() {
				return [
					{
						id: 1,
						name: '张明',
						startDate: '2020-09',
						endDate: '2021-09',
						major: '计算机科学与技术',
						grade: '2018',
						achievement: '带领社团获得校级优秀社团称号，社团成员规模扩大至50人'
					},
					{
						id: 2,
						name: '李华',
						startDate: '2021-09',
						endDate: '2022-09',
						major: '软件工程',
						grade: '2019',
						achievement: '组织举办第一届校园编程大赛，参与人数突破200人'
					},
					{
						id: 3,
						name: '王芳',
						startDate: '2022-09',
						endDate: '2023-09',
						major: '人工智能',
						grade: '2020',
						achievement: '推动社团与多家企业建立合作关系，为成员提供实习机会'
					},
					{
						id: 4,
						name: '陈伟',
						startDate: '2023-09',
						endDate: '2024-09',
						major: '计算机科学与技术',
						grade: '2021',
						achievement: '创立技术分享品牌活动，累计举办30场技术讲座'
					},
					{
						id: 5,
						name: '刘洋',
						startDate: '2024-09',
						endDate: '至今',
						major: '数据科学',
						grade: '2022',
						achievement: '现任社长，致力于打造跨校交流平台'
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

	.header-section {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx;
		text-align: center;
		margin-bottom: 30rpx;
	}

	.title {
		font-size: 36rpx;
		font-weight: bold;
		color: #fff;
		display: block;
		margin-bottom: 10rpx;
	}

	.subtitle {
		font-size: 26rpx;
		color: rgba(255, 255, 255, 0.8);
	}

	.president-list {
		padding: 0 20rpx;
	}

	.president-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		display: flex;
		align-items: flex-start;
		position: relative;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.order-badge {
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 50rpx;
		height: 50rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		font-weight: bold;
	}

	.president-avatar {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
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

	.president-info {
		flex: 1;
		min-width: 0;
		padding-right: 60rpx;
	}

	.president-header {
		margin-bottom: 15rpx;
	}

	.president-name {
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.president-term {
		font-size: 24rpx;
		color: #667eea;
		background: rgba(102, 126, 234, 0.1);
		padding: 5rpx 15rpx;
		border-radius: 20rpx;
	}

	.president-desc {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
		margin-bottom: 15rpx;
		display: block;
	}

	.president-meta {
		display: flex;
		align-items: center;
		gap: 10rpx;
	}

	.meta-text {
		font-size: 24rpx;
		color: #999;
	}

	.meta-dot {
		font-size: 24rpx;
		color: #ddd;
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
