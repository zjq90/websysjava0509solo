<template>
	<view class="message-container">
		<view class="tab-bar">
			<view 
				class="tab-item" 
				v-for="tab in tabs" 
				:key="tab.type"
				:class="{ active: activeTab === tab.type }"
				@click="switchTab(tab.type)"
			>
				<text>{{ tab.name }}</text>
				<view class="badge" v-if="getUnreadCount(tab.type) > 0">
					{{ getUnreadCount(tab.type) }}
				</view>
			</view>
		</view>
		
		<view class="message-list">
			<view 
				class="message-item" 
				v-for="msg in filteredMessages" 
				:key="msg.id"
				:class="{ unread: !msg.isRead }"
				@click="viewDetail(msg)"
			>
				<view class="message-icon" :class="'icon-' + msg.type.toLowerCase()">
					{{ getMessageIcon(msg.type) }}
				</view>
				<view class="message-content">
					<view class="message-header">
						<text class="message-title">{{ msg.title }}</text>
						<text class="message-time">{{ formatTime(msg.createTime) }}</text>
					</view>
					<text class="message-summary">{{ msg.content || '点击查看详情' }}</text>
				</view>
				<view class="unread-dot" v-if="!msg.isRead"></view>
			</view>
			
			<view class="empty-state" v-if="filteredMessages.length === 0">
				<text class="empty-icon">📭</text>
				<text>暂无消息</text>
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
				{ name: '全部', type: 'ALL' },
				{ name: '预约', type: 'APPOINTMENT' },
				{ name: '提醒', type: 'REMINDER' },
				{ name: '报告', type: 'REPORT' },
				{ name: '系统', type: 'SYSTEM' }
			],
			messages: []
		}
	},
	computed: {
		filteredMessages() {
			if (this.activeTab === 'ALL') {
				return this.messages
			}
			return this.messages.filter(m => m.type === this.activeTab)
		}
	},
	onShow() {
		this.loadMessages()
	},
	methods: {
		loadMessages() {
			this.$request({
				url: '/patient/messages',
				method: 'GET'
			}).then(res => {
				this.messages = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		getUnreadCount(type) {
			if (type === 'ALL') {
				return this.messages.filter(m => !m.isRead).length
			}
			return this.messages.filter(m => m.type === type && !m.isRead).length
		},
		
		switchTab(type) {
			this.activeTab = type
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
		
		formatTime(time) {
			if (!time) return ''
			const date = new Date(time)
			const now = new Date()
			const diff = now - date
			
			if (diff < 60000) return '刚刚'
			if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
			if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
			
			const month = (date.getMonth() + 1).toString().padStart(2, '0')
			const day = date.getDate().toString().padStart(2, '0')
			const hour = date.getHours().toString().padStart(2, '0')
			const minute = date.getMinutes().toString().padStart(2, '0')
			
			if (diff < 172800000) return `昨天 ${hour}:${minute}`
			return `${month}-${day} ${hour}:${minute}`
		},
		
		viewDetail(msg) {
			uni.navigateTo({
				url: `/pages/message-detail/message-detail?id=${msg.id}`
			})
		}
	}
}
</script>

<style>
.message-container {
	min-height: 100vh;
	background-color: #F5F5F5;
}

.tab-bar {
	display: flex;
	background-color: #FFFFFF;
	padding: 0 16rpx;
	position: sticky;
	top: 0;
	z-index: 100;
	overflow-x: auto;
}

.tab-item {
	flex-shrink: 0;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 24rpx 20rpx;
	font-size: 26rpx;
	color: #666666;
	position: relative;
}

.tab-item.active {
	color: #007AFF;
	font-weight: 600;
}

.tab-item.active::after {
	content: '';
	position: absolute;
	bottom: 0;
	left: 50%;
	transform: translateX(-50%);
	width: 40rpx;
	height: 4rpx;
	background-color: #007AFF;
	border-radius: 2rpx;
}

.tab-item .badge {
	position: absolute;
	top: 12rpx;
	right: 8rpx;
	min-width: 32rpx;
	height: 32rpx;
	line-height: 32rpx;
	font-size: 20rpx;
	padding: 0 8rpx;
}

.message-list {
	padding: 16rpx;
}

.message-item {
	display: flex;
	align-items: center;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
	position: relative;
}

.message-item.unread {
	background-color: #F8FBFF;
}

.message-icon {
	width: 80rpx;
	height: 80rpx;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	font-size: 36rpx;
}

.icon-appointment {
	background-color: #E3F2FF;
}

.icon-reminder {
	background-color: #FFF5E6;
}

.icon-report {
	background-color: #E8F8E8;
}

.icon-system {
	background-color: #F0F0F0;
}

.message-content {
	flex: 1;
	overflow: hidden;
}

.message-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8rpx;
}

.message-title {
	font-size: 28rpx;
	font-weight: 500;
	color: #333333;
	max-width: 400rpx;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.message-time {
	font-size: 22rpx;
	color: #999999;
	flex-shrink: 0;
}

.message-summary {
	font-size: 26rpx;
	color: #666666;
	max-width: 500rpx;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.unread-dot {
	width: 16rpx;
	height: 16rpx;
	background-color: #FF3B30;
	border-radius: 50%;
	margin-left: 16rpx;
	flex-shrink: 0;
}

.empty-state {
	text-align: center;
	padding: 120rpx 0;
}

.empty-icon {
	display: block;
	font-size: 100rpx;
	margin-bottom: 24rpx;
}

.empty-state text:last-child {
	font-size: 28rpx;
	color: #999999;
}
</style>
