<template>
	<view class="container">
		<view class="header">
			<text class="title">数据统计</text>
		</view>

		<view class="stats-overview">
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalMembers || 0 }}</text>
				<text class="stat-label">总成员数</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalActivities || 0 }}</text>
				<text class="stat-label">活动总数</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.totalPosts || 0 }}</text>
				<text class="stat-label">帖子总数</text>
			</view>
			<view class="stat-card">
				<text class="stat-value">{{ stats.avgParticipation || 0 }}%</text>
				<text class="stat-label">平均参与率</text>
			</view>
		</view>

		<view class="chart-section">
			<view class="section-header">
				<text class="section-title">成员增长趋势</text>
				<view class="period-tabs">
					<text 
						class="tab-item" 
						:class="{ active: memberPeriod === 'month' }"
						@click="memberPeriod = 'month'; loadMemberTrend()"
					>月度</text>
					<text 
						class="tab-item" 
						:class="{ active: memberPeriod === 'year' }"
						@click="memberPeriod = 'year'; loadMemberTrend()"
					>年度</text>
				</view>
			</view>
			<view class="chart-container">
				<view class="bar-chart">
					<view 
						class="bar-item" 
						v-for="(item, idx) in memberTrendData" 
						:key="idx"
					>
						<view class="bar-wrapper">
							<view 
								class="bar" 
								:style="{ height: getBarHeight(item.count, memberTrendData) + '%' }"
							>
								<text class="bar-value">{{ item.count }}</text>
							</view>
						</view>
						<text class="bar-label">{{ item.label }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="chart-section">
			<view class="section-header">
				<text class="section-title">活动参与率</text>
			</view>
			<view class="chart-container">
				<view class="participation-list">
					<view 
						class="participation-item" 
						v-for="(item, idx) in activityParticipationData.slice(0, 5)" 
						:key="idx"
					>
						<view class="activity-info">
							<text class="activity-name">{{ item.name }}</text>
							<text class="activity-rate">{{ item.participationRate }}%</text>
						</view>
						<view class="progress-bar">
							<view 
								class="progress-fill" 
								:style="{ width: item.participationRate + '%' }"
							></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="chart-section">
			<view class="section-header">
				<text class="section-title">部门成员分布</text>
			</view>
			<view class="chart-container">
				<view class="pie-chart">
					<view 
						class="pie-segment" 
						v-for="(item, idx) in departmentDistribution" 
						:key="idx"
						:style="getPieStyle(item, idx, departmentDistribution)"
					></view>
				</view>
				<view class="legend-list">
					<view class="legend-item" v-for="(item, idx) in departmentDistribution" :key="idx">
						<view class="legend-color" :style="{ background: getColor(idx) }"></view>
						<text class="legend-name">{{ item.name }}</text>
						<text class="legend-value">{{ item.count }}人</text>
					</view>
				</view>
			</view>
		</view>

		<view class="action-section">
			<view class="section-header">
				<text class="section-title">报告导出</text>
			</view>
			<view class="export-options">
				<button class="btn-export" @click="exportReport('month')">
					<text class="iconfont icon-file"></text>
					导出月度报告
				</button>
				<button class="btn-export" @click="exportReport('year')">
					<text class="iconfont icon-file"></text>
					导出年度报告
				</button>
			</view>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { statisticsApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				stats: {},
				memberPeriod: 'month',
				memberTrendData: [],
				activityParticipationData: [],
				departmentDistribution: [],
				loading: false,
				colors: ['#667eea', '#764ba2', '#f093fb', '#4facfe', '#43e97b', '#fa709a', '#fee140']
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.loadAllData()
		},
		methods: {
			async loadAllData() {
				this.loading = true
				try {
					await Promise.all([
						this.loadOverview(),
						this.loadMemberTrend(),
						this.loadActivityParticipation(),
						this.loadDepartmentDistribution()
					])
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			async loadOverview() {
				try {
					const res = await statisticsApi.getBase(this.clubId)
					this.stats = res || {}
				} catch (e) {
					console.error(e)
					this.stats = {
						totalMembers: 80,
						totalActivities: 12,
						totalPosts: 45,
						avgParticipation: 78
					}
				}
			},
			async loadMemberTrend() {
				try {
					const res = await statisticsApi.getMemberGrowth(this.clubId)
					if (res && res.months) {
						this.memberTrendData = res.months.map((month, idx) => ({
							label: month.substring(5),
							count: res.counts[idx] || 0
						}))
					} else {
						this.memberTrendData = this.getMockMemberTrend()
					}
				} catch (e) {
					console.error(e)
					this.memberTrendData = this.getMockMemberTrend()
				}
			},
			getMockMemberTrend() {
				if (this.memberPeriod === 'month') {
					return [
						{ label: '1月', count: 45 },
						{ label: '2月', count: 52 },
						{ label: '3月', count: 58 },
						{ label: '4月', count: 65 },
						{ label: '5月', count: 72 },
						{ label: '6月', count: 80 }
					]
				}
				return [
					{ label: '2020', count: 30 },
					{ label: '2021', count: 45 },
					{ label: '2022', count: 60 },
					{ label: '2023', count: 75 },
					{ label: '2024', count: 80 }
				]
			},
			async loadActivityParticipation() {
				try {
					const res = await statisticsApi.getActivityParticipation(this.clubId)
					if (res && res.activities) {
						this.activityParticipationData = res.activities.map(item => ({
							name: item.title,
							participationRate: item.rate || 0
						}))
					} else {
						this.activityParticipationData = this.getMockParticipation()
					}
				} catch (e) {
					console.error(e)
					this.activityParticipationData = this.getMockParticipation()
				}
			},
			getMockParticipation() {
				return [
					{ name: '春季招新活动', participationRate: 95 },
					{ name: '技术分享会', participationRate: 85 },
					{ name: '团建活动', participationRate: 78 },
					{ name: '编程大赛', participationRate: 72 },
					{ name: '换届选举', participationRate: 65 }
				]
			},
			async loadDepartmentDistribution() {
				try {
					const res = await statisticsApi.getDepartmentDistribution(this.clubId)
					if (res && res.departments) {
						this.departmentDistribution = res.departments.map((dept, idx) => ({
							name: dept,
							count: res.counts[idx] || 0
						}))
					} else {
						this.departmentDistribution = this.getMockDepartment()
					}
				} catch (e) {
					console.error(e)
					this.departmentDistribution = this.getMockDepartment()
				}
			},
			getMockDepartment() {
				return [
					{ name: '技术部', count: 25 },
					{ name: '宣传部', count: 18 },
					{ name: '组织部', count: 15 },
					{ name: '外联部', count: 12 },
					{ name: '财务部', count: 10 }
				]
			},
			getBarHeight(value, data) {
				if (!data || data.length === 0) return 0
				const max = Math.max(...data.map(d => d.count))
				return max > 0 ? (value / max) * 100 : 0
			},
			getColor(index) {
				return this.colors[index % this.colors.length]
			},
			getPieStyle(item, index, data) {
				if (!data || data.length === 0) return {}
				const total = data.reduce((sum, d) => sum + d.count, 0)
				const percentage = total > 0 ? (item.count / total) * 100 : 0
				let startAngle = 0
				for (let i = 0; i < index; i++) {
					startAngle += (data[i].count / total) * 360
				}
				return {
					background: `conic-gradient(${this.getColor(index)} 0deg, ${this.getColor(index)} ${percentage * 3.6}deg, transparent ${percentage * 3.6}deg)`,
					transform: `rotate(${startAngle}deg)`
				}
			},
			async exportReport(type) {
				try {
					uni.showLoading({ title: '正在生成...' })
					const now = new Date()
					const year = now.getFullYear()
					const month = type === 'year' ? 1 : now.getMonth() + 1
					const res = await statisticsApi.getMonthlyReport(this.clubId, { year, month })
					uni.hideLoading()
					if (res) {
						uni.showModal({
							title: res.reportTitle || '报告生成成功',
							content: `成员总数: ${res.memberStats?.totalMembers || 0}人\n新增成员: ${res.memberStats?.newMembers || 0}人\n活动总数: ${res.activityStats?.totalActivities || 0}场\n参与人次: ${res.activityStats?.totalParticipants || 0}人`,
							showCancel: false
						})
					}
				} catch (e) {
					console.error(e)
					uni.hideLoading()
					uni.showToast({ title: '导出失败', icon: 'none' })
				}
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
		margin-bottom: 20rpx;
	}

	.title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.stats-overview {
		display: flex;
		flex-wrap: wrap;
		padding: 0 20rpx;
		gap: 15rpx;
		margin-bottom: 20rpx;
	}

	.stat-card {
		flex: 1;
		min-width: calc(50% - 15rpx);
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		text-align: center;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.stat-value {
		font-size: 48rpx;
		font-weight: bold;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
		background-clip: text;
		display: block;
		margin-bottom: 10rpx;
	}

	.stat-label {
		font-size: 24rpx;
		color: #999;
	}

	.chart-section,
	.action-section {
		background: #fff;
		margin: 0 20rpx 20rpx;
		border-radius: 16rpx;
		padding: 30rpx;
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 25rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
	}

	.period-tabs {
		display: flex;
		background: #f5f5f5;
		border-radius: 30rpx;
		padding: 5rpx;
	}

	.tab-item {
		padding: 10rpx 25rpx;
		font-size: 24rpx;
		color: #666;
		border-radius: 25rpx;
		transition: all 0.3s;
	}

	.tab-item.active {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.chart-container {
		padding: 10rpx 0;
	}

	.bar-chart {
		display: flex;
		align-items: flex-end;
		justify-content: space-around;
		height: 300rpx;
		padding: 20rpx 0;
	}

	.bar-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		flex: 1;
	}

	.bar-wrapper {
		width: 40rpx;
		height: 240rpx;
		display: flex;
		align-items: flex-end;
		justify-content: center;
		margin-bottom: 15rpx;
	}

	.bar {
		width: 100%;
		background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
		border-radius: 10rpx 10rpx 0 0;
		position: relative;
		transition: height 0.5s;
		min-height: 10rpx;
	}

	.bar-value {
		position: absolute;
		top: -35rpx;
		left: 50%;
		transform: translateX(-50%);
		font-size: 20rpx;
		color: #666;
		white-space: nowrap;
	}

	.bar-label {
		font-size: 22rpx;
		color: #999;
	}

	.participation-list {
		margin-bottom: 10rpx;
	}

	.participation-item {
		margin-bottom: 25rpx;
	}

	.participation-item:last-child {
		margin-bottom: 0;
	}

	.activity-info {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.activity-name {
		font-size: 26rpx;
		color: #333;
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.activity-rate {
		font-size: 26rpx;
		font-weight: bold;
		color: #667eea;
		margin-left: 20rpx;
	}

	.progress-bar {
		width: 100%;
		height: 16rpx;
		background: #f0f0f0;
		border-radius: 8rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
		border-radius: 8rpx;
		transition: width 0.5s;
	}

	.pie-chart {
		width: 250rpx;
		height: 250rpx;
		border-radius: 50%;
		margin: 0 auto 30rpx;
		position: relative;
		overflow: hidden;
	}

	.pie-segment {
		position: absolute;
		width: 100%;
		height: 100%;
		border-radius: 50%;
	}

	.legend-list {
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		gap: 20rpx 30rpx;
	}

	.legend-item {
		display: flex;
		align-items: center;
	}

	.legend-color {
		width: 24rpx;
		height: 24rpx;
		border-radius: 6rpx;
		margin-right: 10rpx;
		flex-shrink: 0;
	}

	.legend-name {
		font-size: 24rpx;
		color: #666;
		margin-right: 15rpx;
	}

	.legend-value {
		font-size: 24rpx;
		font-weight: 500;
		color: #333;
	}

	.export-options {
		display: flex;
		gap: 20rpx;
	}

	.btn-export {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		background: #fff;
		border: 2rpx solid #667eea;
		color: #667eea;
		border-radius: 40rpx;
		font-size: 26rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.btn-export .iconfont {
		margin-right: 10rpx;
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
		.stat-card {
			min-width: calc(25% - 15rpx);
		}
	}
</style>
