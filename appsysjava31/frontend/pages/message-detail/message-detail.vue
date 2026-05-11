<template>
	<view class="detail-container">
		<view class="detail-card" v-if="message">
			<view class="card-header">
				<view class="message-type" :class="'type-' + message.type.toLowerCase()">
					<text class="type-icon">{{ getMessageIcon(message.type) }}</text>
					<text class="type-name">{{ getTypeName(message.type) }}</text>
				</view>
				<text class="create-time">{{ formatTime(message.createTime) }}</text>
			</view>
			
			<view class="card-body">
				<text class="message-title">{{ message.title }}</text>
				<view class="message-content">
					<text>{{ message.content || '这是一条系统消息，用于通知您相关事项。' }}</text>
				</view>
				
				<view class="extra-info" v-if="message.relatedId">
					<view class="info-item" v-if="message.type === 'APPOINTMENT'">
						<text class="info-label">关联预约</text>
						<text class="info-value text-primary">点击查看预约详情</text>
					</view>
					<view class="info-item" v-if="message.type === 'REPORT'">
						<text class="info-label">关联报告</text>
						<text class="info-value text-primary">点击查看报告详情</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			messageId: null,
			message: null
		}
	},
	onLoad(options) {
		this.messageId = options.id
		this.loadDetail()
	},
	methods: {
		loadDetail() {
			this.$showLoading('加载中...')
			this.$request({
				url: `/patient/messages/${this.messageId}`,
				method: 'GET'
			}).then(res => {
				this.$hideLoading()
				this.message = res.data
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		},
		
		getMessageIcon(type) {
			const icons = {
				'APPOINTMENT': '📅',
				'REMINDER': '⏰',
				'REPORT': '📄',
				'SYSTEM': '🔔'
			}
			return icons[type] || '📬'
		},
		
		getTypeName(type) {
			const map = {
				'APPOINTMENT': '预约通知',
				'REMINDER': '就诊提醒',
				'REPORT': '报告通知',
				'SYSTEM': '系统消息'
			}
			return map[type] || '消息'
		},
		
		formatTime(time) {
			if (!time) return ''
			const date = new Date(time)
			const year = date.getFullYear()
			const month = (date.getMonth() + 1).toString().padStart(2, '0')
			const day = date.getDate().toString().padStart(2, '0')
			const hour = date.getHours().toString().padStart(2, '0')
			const minute = date.getMinutes().toString().padStart(2, '0')
			return `${year}-${month}-${day} ${hour}:${minute}`
		}
	}
}
</script>

<style>
.detail-container {
	min-height: 100vh;
	background-color: #F5F5F5;
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
	padding-bottom: 20rpx;
	border-bottom: 1rpx solid #F0F0F0;
	margin-bottom: 24rpx;
}

.message-type {
	display: flex;
	align-items: center;
}

.type-icon {
	font-size: 36rpx;
	margin-right: 12rpx;
}

.type-name {
	font-size: 28rpx;
	font-weight: 500;
	color: #333333;
}

.type-appointment .type-name {
	color: #007AFF;
}

.type-reminder .type-name {
	color: #FF9500;
}

.type-report .type-name {
	color: #34C759;
}

.type-system .type-name {
	color: #666666;
}

.create-time {
	font-size: 24rpx;
	color: #999999;
}

.card-body {
	padding: 0;
}

.message-title {
	display: block;
	font-size: 34rpx;
	font-weight: 700;
	color: #333333;
	margin-bottom: 24rpx;
	line-height: 1.5;
}

.message-content {
	background-color: #F9F9F9;
	padding: 24rpx;
	border-radius: 12rpx;
	margin-bottom: 24rpx;
}

.message-content text {
	font-size: 28rpx;
	color: #333333;
	line-height: 1.8;
}

.extra-info {
	border-top: 1rpx solid #F0F0F0;
	padding-top: 24rpx;
}

.info-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 16rpx 0;
}

.info-label {
	font-size: 28rpx;
	color: #666666;
}

.info-value {
	font-size: 28rpx;
}
</style>
