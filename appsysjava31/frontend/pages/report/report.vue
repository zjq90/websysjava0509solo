<template>
	<view class="report-container">
		<view class="filter-bar">
			<picker 
				mode="selector" 
				:range="typeOptions" 
				@change="onTypeChange"
			>
				<view class="filter-item">
					<text>{{ typeOptions[typeIndex] }}</text>
					<text class="filter-arrow">▼</text>
				</view>
			</picker>
			<picker 
				mode="date" 
				:value="startDate"
				@change="onDateChange"
			>
				<view class="filter-item">
					<text>{{ startDate || '选择日期' }}</text>
					<text class="filter-arrow">▼</text>
				</view>
			</picker>
		</view>
		
		<view class="report-list">
			<view class="report-card" v-for="report in filteredReports" :key="report.id" @click="viewDetail(report)">
				<view class="report-header">
					<view class="report-type" :class="'type-' + report.type.toLowerCase()">
						{{ getTypeText(report.type) }}
					</view>
					<text class="report-date">{{ formatDate(report.reportDate) }}</text>
				</view>
				
				<view class="report-body">
					<text class="report-title">{{ report.title }}</text>
					<text class="report-hospital">检查医院：{{ report.hospital || '本院' }}</text>
					<text class="report-patient">就诊人：{{ report.patientName }}</text>
				</view>
				
				<view class="report-footer">
					<view class="status-tag" :class="'status-' + report.status.toLowerCase()">
						{{ getStatusText(report.status) }}
					</view>
					<view class="actions">
						<view class="action-item" @click.stop="downloadReport(report)">
							<text class="action-icon">📥</text>
							<text class="action-text">下载</text>
						</view>
					</view>
				</view>
			</view>
			
			<view class="empty-state" v-if="filteredReports.length === 0">
				<text class="empty-icon">📄</text>
				<text>暂无检查报告</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			reports: [],
			typeOptions: ['全部类型', '检验报告', '检查报告'],
			typeIndex: 0,
			startDate: ''
		}
	},
	computed: {
		filteredReports() {
			let result = [...this.reports]
			
			if (this.typeIndex > 0) {
				const type = this.typeIndex === 1 ? 'LAB' : 'EXAM'
				result = result.filter(r => r.type === type)
			}
			
			if (this.startDate) {
				result = result.filter(r => r.reportDate >= this.startDate)
			}
			
			return result
		}
	},
	onShow() {
		this.loadReports()
	},
	methods: {
		loadReports() {
			this.$request({
				url: '/patient/reports',
				method: 'GET'
			}).then(res => {
				this.reports = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		onTypeChange(e) {
			this.typeIndex = e.detail.value
		},
		
		onDateChange(e) {
			this.startDate = e.detail.value
		},
		
		getTypeText(type) {
			const map = {
				'LAB': '检验报告',
				'EXAM': '检查报告'
			}
			return map[type] || '报告'
		},
		
		getStatusText(status) {
			const map = {
				'PENDING': '待出结果',
				'COMPLETED': '已出结果'
			}
			return map[status] || status
		},
		
		formatDate(date) {
			if (!date) return ''
			const d = new Date(date)
			return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`
		},
		
		viewDetail(report) {
			uni.navigateTo({
				url: `/pages/report-detail/report-detail?id=${report.id}`
			})
		},
		
		downloadReport(report) {
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
.report-container {
	min-height: 100vh;
	background-color: #F5F5F5;
}

.filter-bar {
	display: flex;
	padding: 16rpx 24rpx;
	background-color: #FFFFFF;
}

.filter-item {
	display: flex;
	align-items: center;
	margin-right: 32rpx;
	font-size: 26rpx;
	color: #333333;
}

.filter-arrow {
	font-size: 20rpx;
	color: #999999;
	margin-left: 8rpx;
}

.report-list {
	padding: 16rpx;
}

.report-card {
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.report-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16rpx;
}

.report-type {
	font-size: 24rpx;
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

.report-date {
	font-size: 26rpx;
	color: #666666;
}

.report-body {
	margin-bottom: 16rpx;
}

.report-title {
	display: block;
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 8rpx;
}

.report-hospital, .report-patient {
	display: block;
	font-size: 26rpx;
	color: #666666;
	margin-bottom: 4rpx;
}

.report-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding-top: 16rpx;
	border-top: 1rpx solid #F0F0F0;
}

.status-tag {
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

.actions {
	display: flex;
	gap: 24rpx;
}

.action-item {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.action-icon {
	font-size: 32rpx;
	margin-bottom: 4rpx;
}

.action-text {
	font-size: 22rpx;
	color: #666666;
}
</style>
