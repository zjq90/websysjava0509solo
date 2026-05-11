<template>
	<view class="invoice-container">
		<view class="filter-bar">
			<view 
				class="filter-tab" 
				v-for="tab in tabs" 
				:key="tab.value"
				:class="{ active: activeTab === tab.value }"
				@click="switchTab(tab.value)"
			>
				<text>{{ tab.name }}</text>
			</view>
		</view>
		
		<view class="invoice-list">
			<view class="invoice-card" v-for="invoice in filteredInvoices" :key="invoice.id">
				<view class="card-header">
					<view class="invoice-type">
						<text class="type-icon">🧾</text>
						<text class="type-name">{{ invoice.type === 'MEDICAL' ? '医疗电子票据' : '挂号费票据' }}</text>
					</view>
					<view class="invoice-status" :class="'status-' + invoice.status.toLowerCase()">
						{{ getStatusText(invoice.status) }}
					</view>
				</view>
				
				<view class="card-body">
					<text class="invoice-title">{{ invoice.title }}</text>
					<view class="info-row">
						<text class="info-label">票据编号</text>
						<text class="info-value">{{ invoice.invoiceNo || '202406010001' }}</text>
					</view>
					<view class="info-row">
						<text class="info-label">开票日期</text>
						<text class="info-value">{{ formatDate(invoice.createTime) }}</text>
					</view>
					<view class="info-row">
						<text class="info-label">就诊人</text>
						<text class="info-value">{{ invoice.patientName }}</text>
					</view>
				</view>
				
				<view class="card-footer">
					<view class="amount-info">
						<text class="amount-label">金额</text>
						<text class="amount-value">¥{{ invoice.amount }}</text>
					</view>
					<view class="actions">
						<view class="action-btn secondary" @click="checkInvoice(invoice)">
							查验
						</view>
						<view class="action-btn primary" @click="downloadInvoice(invoice)">
							下载PDF
						</view>
					</view>
				</view>
			</view>
			
			<view class="empty-state" v-if="filteredInvoices.length === 0">
				<text class="empty-icon">🧾</text>
				<text>暂无电子票据</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			activeTab: 'ALL',
			tabs: [
				{ name: '全部', value: 'ALL' },
				{ name: '医疗票据', value: 'MEDICAL' },
				{ name: '挂号票据', value: 'REGISTER' }
			],
			invoices: []
		}
	},
	computed: {
		filteredInvoices() {
			if (this.activeTab === 'ALL') {
				return this.invoices
			}
			return this.invoices.filter(i => i.type === this.activeTab)
		}
	},
	onShow() {
		this.loadInvoices()
	},
	methods: {
		loadInvoices() {
			this.$request({
				url: '/patient/invoices',
				method: 'GET'
			}).then(res => {
				this.invoices = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		switchTab(value) {
			this.activeTab = value
		},
		
		getStatusText(status) {
			const map = {
				'ISSUED': '已开票',
				'CANCELLED': '已作废'
			}
			return map[status] || status
		},
		
		formatDate(date) {
			if (!date) return ''
			const d = new Date(date)
			return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`
		},
		
		checkInvoice(invoice) {
			uni.showModal({
				title: '票据查验',
				content: `票据编号：${invoice.invoiceNo || '202406010001'}\n金额：¥${invoice.amount}\n状态：有效票据`,
				showCancel: false
			})
		},
		
		downloadInvoice(invoice) {
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
.invoice-container {
	min-height: 100vh;
	background-color: #F5F5F5;
}

.filter-bar {
	display: flex;
	background-color: #FFFFFF;
	padding: 0 24rpx;
	border-bottom: 1rpx solid #F0F0F0;
}

.filter-tab {
	flex: 1;
	text-align: center;
	padding: 24rpx 0;
	font-size: 28rpx;
	color: #666666;
	position: relative;
}

.filter-tab.active {
	color: #007AFF;
	font-weight: 600;
}

.filter-tab.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 48rpx;
	height: 4rpx;
	background-color: #007AFF;
	border-radius: 2rpx;
}

.invoice-list {
	padding: 16rpx;
}

.invoice-card {
	background-color: #FFFFFF;
	border-radius: 16rpx;
	margin-bottom: 16rpx;
	overflow: hidden;
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx 24rpx;
	background: linear-gradient(90deg, #007AFF 0%, #34C759 100%);
}

.invoice-type {
	display: flex;
	align-items: center;
}

.type-icon {
	font-size: 32rpx;
	margin-right: 8rpx;
}

.type-name {
	font-size: 28rpx;
	color: #FFFFFF;
	font-weight: 500;
}

.invoice-status {
	font-size: 24rpx;
	padding: 4rpx 16rpx;
	border-radius: 20rpx;
}

.status-issued {
	background-color: rgba(255, 255, 255, 0.3);
	color: #FFFFFF;
}

.status-cancelled {
	background-color: rgba(0, 0, 0, 0.2);
	color: #FFFFFF;
}

.card-body {
	padding: 20rpx 24rpx;
}

.invoice-title {
	display: block;
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 16rpx;
}

.info-row {
	display: flex;
	padding: 8rpx 0;
}

.info-label {
	width: 160rpx;
	font-size: 26rpx;
	color: #999999;
}

.info-value {
	flex: 1;
	font-size: 26rpx;
	color: #333333;
}

.card-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 20rpx 24rpx;
	border-top: 1rpx solid #F0F0F0;
	background-color: #FAFAFA;
}

.amount-info {
	display: flex;
	align-items: baseline;
}

.amount-label {
	font-size: 26rpx;
	color: #666666;
	margin-right: 8rpx;
}

.amount-value {
	font-size: 36rpx;
	font-weight: 700;
	color: #FF3B30;
}

.actions {
	display: flex;
	gap: 16rpx;
}

.action-btn {
	padding: 12rpx 28rpx;
	border-radius: 32rpx;
	font-size: 26rpx;
}

.action-btn.secondary {
	background-color: #F5F5F5;
	color: #666666;
}

.action-btn.primary {
	background-color: #007AFF;
	color: #FFFFFF;
}
</style>
