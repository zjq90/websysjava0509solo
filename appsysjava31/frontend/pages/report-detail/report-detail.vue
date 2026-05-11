<template>
	<view class="detail-container">
		<view class="detail-card" v-if="report">
			<view class="card-header">
				<text class="title">{{ report.title }}</text>
				<view class="type-tag" :class="'type-' + report.type.toLowerCase()">
					{{ getTypeText(report.type) }}
				</view>
			</view>
			
			<view class="info-section">
				<view class="info-row">
					<text class="info-label">就诊人</text>
					<text class="info-value">{{ report.patientName }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">报告日期</text>
					<text class="info-value">{{ formatDate(report.reportDate) }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">检查医院</text>
					<text class="info-value">{{ report.hospital || '本院' }}</text>
				</view>
				<view class="info-row">
					<text class="info-label">报告状态</text>
					<text class="info-value">
						<text class="status-tag" :class="'status-' + report.status.toLowerCase()">
							{{ getStatusText(report.status) }}
						</text>
					</text>
				</view>
			</view>
			
			<view class="content-section">
				<text class="section-title">检查项目</text>
				<text class="section-content">{{ report.items || '血常规、肝功能、肾功能' }}</text>
			</view>
			
			<view class="content-section">
				<text class="section-title">检查结论</text>
				<text class="section-content">{{ report.conclusion || '各项指标基本正常，建议定期复查。如有不适请及时就医。' }}</text>
			</view>
		</view>
		
		<view class="bottom-bar safe-area-bottom">
			<view class="btn-secondary bottom-btn" @click="printReport">
				打印报告
			</view>
			<view class="btn-primary bottom-btn" @click="downloadReport">
				下载PDF
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			reportId: null,
			report: null
		}
	},
	onLoad(options) {
		this.reportId = options.id
		this.loadDetail()
	},
	methods: {
		loadDetail() {
			this.$showLoading('加载中...')
			this.$request({
				url: `/patient/reports/${this.reportId}`,
				method: 'GET'
			}).then(res => {
				this.$hideLoading()
				this.report = res.data
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		},
		
		getTypeText(type) {
			const map = { 'LAB': '检验报告', 'EXAM': '检查报告' }
			return map[type] || '报告'
		},
		
		getStatusText(status) {
			const map = { 'PENDING': '待出结果', 'COMPLETED': '已出结果' }
			return map[status] || status
		},
		
		formatDate(date) {
			if (!date) return ''
			const d = new Date(date)
			return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`
		},
		
		printReport() {
			uni.showToast({
				title: '打印功能演示',
				icon: 'none'
			})
		},
		
		downloadReport() {
			uni.showLoading({ title: '下载中...' })
			setTimeout(() => {
				uni.hideLoading()
				uni.showToast({
					title: 'PDF已保存',
					icon: 'success'
				})
			}, 1500)
		}
	}
}
</script>

<style>
.detail-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 140rpx;
}

.detail-card {
	margin: 16rpx;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 24rpx;
	padding-bottom: 24rpx;
	border-bottom: 1rpx solid #F0F0F0;
}

.title {
	font-size: 34rpx;
	font-weight: 700;
	color: #333333;
}

.type-tag {
	font-size: 26rpx;
	padding: 6rpx 16rpx;
	border-radius: 20rpx;
}

.type-lab {
	background-color: #E3F2FF;
	color: #007AFF;
}

.type-exam {
	background-color: #FFF5E6;
	color: #FF9500;
}

.info-section {
	margin-bottom: 24rpx;
}

.info-row {
	display: flex;
	padding: 16rpx 0;
	border-bottom: 1rpx solid #F5F5F5;
}

.info-row:last-child {
	border-bottom: none;
}

.info-label {
	width: 160rpx;
	font-size: 28rpx;
	color: #666666;
}

.info-value {
	flex: 1;
	font-size: 28rpx;
	color: #333333;
}

.status-tag {
	display: inline-block;
	font-size: 24rpx;
	padding: 4rpx 12rpx;
	border-radius: 16rpx;
}

.status-pending {
	background-color: #FFF5E6;
	color: #FF9500;
}

.status-completed {
	background-color: #E8F8E8;
	color: #34C759;
}

.content-section {
	margin-bottom: 24rpx;
}

.section-title {
	display: block;
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 12rpx;
}

.section-content {
	display: block;
	font-size: 28rpx;
	color: #666666;
	line-height: 1.8;
	background-color: #F9F9F9;
	padding: 20rpx;
	border-radius: 12rpx;
}

.bottom-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	display: flex;
	gap: 16rpx;
	background-color: #FFFFFF;
	padding: 24rpx;
}

.bottom-btn {
	flex: 1;
	font-size: 30rpx;
}
</style>
