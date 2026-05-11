<template>
	<view class="index-container">
		<view class="header">
			<view class="user-info">
				<view class="avatar">
					<text class="avatar-text">{{ userInitial }}</text>
				</view>
				<view class="user-detail">
					<text class="greeting">您好，</text>
					<text class="username">{{ userInfo.realName || userInfo.username }}</text>
				</view>
			</view>
			<view class="elder-badge" v-if="getApp().globalData.elderMode">
				<text>👵 长辈模式</text>
			</view>
		</view>
		
		<view class="banner">
			<view class="banner-content">
				<text class="banner-title">智慧医院预约系统</text>
				<text class="banner-subtitle">便捷预约，健康随行</text>
			</view>
		</view>
		
		<view class="quick-actions">
			<view class="action-card" @click="goToDepartment">
				<view class="action-icon">🏥</view>
				<text class="action-text">科室挂号</text>
			</view>
			<view class="action-card" @click="goToAppointmentList">
				<view class="action-icon">📋</view>
				<text class="action-text">我的预约</text>
			</view>
			<view class="action-card" @click="goToReport">
				<view class="action-icon">📄</view>
				<text class="action-text">检验报告</text>
			</view>
			<view class="action-card" @click="goToInvoice">
				<view class="action-icon">🧾</view>
				<text class="action-text">电子票据</text>
			</view>
		</view>
		
		<view class="section">
			<view class="section-header">
				<text class="section-title">热门科室</text>
				<text class="section-more" @click="goToDepartment">查看全部 ></text>
			</view>
			<view class="department-list">
				<view 
					class="department-item" 
					v-for="dept in hotDepartments" 
					:key="dept.id"
					@click="goToDoctor(dept.id)"
				>
					<view class="dept-icon">{{ dept.icon || '🏥' }}</view>
					<text class="dept-name">{{ dept.name }}</text>
				</view>
			</view>
		</view>
		
		<view class="section">
			<view class="section-header">
				<text class="section-title">最新消息</text>
				<text class="section-more" @click="goToMessage">查看全部 ></text>
			</view>
			<view class="message-list">
				<view class="message-item" v-for="msg in latestMessages" :key="msg.id">
					<view class="msg-type" :class="'msg-' + msg.type.toLowerCase()">
						{{ getMessageIcon(msg.type) }}
					</view>
					<view class="msg-content">
						<text class="msg-title">{{ msg.title }}</text>
						<text class="msg-time">{{ formatTime(msg.createTime) }}</text>
					</view>
				</view>
				<view class="empty-state" v-if="latestMessages.length === 0">
					<text class="empty-icon">📭</text>
					<text>暂无新消息</text>
				</view>
			</view>
		</view>
		
		<view class="voice-input-section" v-if="getApp().globalData.elderMode">
			<view class="voice-card">
				<text class="voice-title">语音描述症状</text>
				<view class="voice-btn" @click="startVoiceInput">
					<text class="voice-icon">🎤</text>
					<text class="voice-text">{{ isRecording ? '正在听...' : '点击说话' }}</text>
				</view>
				<view class="voice-result" v-if="voiceResult">
					<text class="result-label">识别结果：</text>
					<text class="result-text">{{ voiceResult }}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			userInfo: {},
			hotDepartments: [],
			latestMessages: [],
			isRecording: false,
			voiceResult: ''
		}
	},
	computed: {
		userInitial() {
			if (this.userInfo.realName) {
				return this.userInfo.realName.charAt(0)
			}
			if (this.userInfo.username) {
				return this.userInfo.username.charAt(0).toUpperCase()
			}
			return 'U'
		}
	},
	onLoad() {
		this.loadUserInfo()
	},
	onShow() {
		this.loadHotDepartments()
		this.loadLatestMessages()
	},
	methods: {
		loadUserInfo() {
			this.userInfo = uni.getStorageSync('userInfo') || {}
		},
		
		loadHotDepartments() {
			this.$request({
				url: '/public/departments',
				method: 'GET'
			}).then(res => {
				this.hotDepartments = res.data.slice(0, 8)
			}).catch(err => {
				console.log(err)
			})
		},
		
		loadLatestMessages() {
			const token = uni.getStorageSync('token')
			if (!token) return
			
			this.$request({
				url: '/patient/messages',
				method: 'GET'
			}).then(res => {
				this.latestMessages = res.data.slice(0, 3)
			}).catch(err => {
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
		
		formatTime(time) {
			if (!time) return ''
			const date = new Date(time)
			const month = date.getMonth() + 1
			const day = date.getDate()
			const hour = date.getHours().toString().padStart(2, '0')
			const minute = date.getMinutes().toString().padStart(2, '0')
			return `${month}-${day} ${hour}:${minute}`
		},
		
		startVoiceInput() {
			this.isRecording = true
			setTimeout(() => {
				this.isRecording = false
				this.voiceResult = '我最近感觉头晕，还有些咳嗽'
				uni.showToast({
					title: '语音识别成功',
					icon: 'success'
				})
			}, 2000)
		},
		
		goToDepartment() {
			uni.navigateTo({
				url: '/pages/department/department'
			})
		},
		
		goToDoctor(deptId) {
			uni.navigateTo({
				url: `/pages/doctor/doctor?departmentId=${deptId}`
			})
		},
		
		goToAppointmentList() {
			uni.switchTab({
				url: '/pages/appointment-list/appointment-list'
			})
		},
		
		goToReport() {
			uni.navigateTo({
				url: '/pages/report/report'
			})
		},
		
		goToInvoice() {
			uni.navigateTo({
				url: '/pages/invoice/invoice'
			})
		},
		
		goToMessage() {
			uni.switchTab({
				url: '/pages/message/message'
			})
		}
	}
}
</script>

<style>
.index-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 32rpx 32rpx 16rpx;
	background: linear-gradient(180deg, #007AFF 0%, #0066CC 100%);
}

.user-info {
	display: flex;
	align-items: center;
}

.avatar {
	width: 80rpx;
	height: 80rpx;
	background-color: #FFFFFF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 16rpx;
}

.avatar-text {
	font-size: 36rpx;
	font-weight: 700;
	color: #007AFF;
}

.user-detail {
	display: flex;
	flex-direction: column;
}

.greeting {
	font-size: 28rpx;
	color: rgba(255, 255, 255, 0.8);
}

.username {
	font-size: 32rpx;
	font-weight: 600;
	color: #FFFFFF;
}

.elder-badge {
	background-color: rgba(255, 255, 255, 0.2);
	padding: 8rpx 16rpx;
	border-radius: 20rpx;
	font-size: 24rpx;
	color: #FFFFFF;
}

.banner {
	margin: -8rpx 32rpx 24rpx;
	padding: 40rpx 32rpx;
	background: linear-gradient(135deg, #007AFF 0%, #34C759 100%);
	border-radius: 24rpx;
}

.banner-content {
	display: flex;
	flex-direction: column;
}

.banner-title {
	font-size: 36rpx;
	font-weight: 700;
	color: #FFFFFF;
	margin-bottom: 8rpx;
}

.banner-subtitle {
	font-size: 26rpx;
	color: rgba(255, 255, 255, 0.9);
}

.quick-actions {
	display: flex;
	justify-content: space-between;
	padding: 0 32rpx;
	margin-bottom: 24rpx;
}

.action-card {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 24rpx 16rpx;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	width: 22%;
}

.action-icon {
	font-size: 48rpx;
	margin-bottom: 8rpx;
}

.action-text {
	font-size: 24rpx;
	color: #333333;
}

.section {
	margin: 0 32rpx 24rpx;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20rpx;
}

.section-title {
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
}

.section-more {
	font-size: 26rpx;
	color: #007AFF;
}

.department-list {
	display: flex;
	flex-wrap: wrap;
	gap: 16rpx;
}

.department-item {
	width: calc(25% - 12rpx);
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 16rpx 8rpx;
	background-color: #F5F5F5;
	border-radius: 12rpx;
}

.dept-icon {
	font-size: 40rpx;
	margin-bottom: 8rpx;
}

.dept-name {
	font-size: 24rpx;
	color: #333333;
	text-align: center;
}

.message-list {
	display: flex;
	flex-direction: column;
}

.message-item {
	display: flex;
	align-items: center;
	padding: 16rpx 0;
	border-bottom: 1rpx solid #F0F0F0;
}

.message-item:last-child {
	border-bottom: none;
}

.msg-type {
	width: 64rpx;
	height: 64rpx;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 16rpx;
	font-size: 28rpx;
}

.msg-appointment {
	background-color: #E3F2FF;
}

.msg-reminder {
	background-color: #FFF5E6;
}

.msg-report {
	background-color: #E8F8E8;
}

.msg-system {
	background-color: #F0F0F0;
}

.msg-content {
	flex: 1;
	display: flex;
	flex-direction: column;
}

.msg-title {
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 4rpx;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.msg-time {
	font-size: 24rpx;
	color: #999999;
}

.voice-input-section {
	margin: 0 32rpx 24rpx;
}

.voice-card {
	background: linear-gradient(135deg, #FF9500 0%, #FF6B00 100%);
	border-radius: 24rpx;
	padding: 32rpx;
}

.voice-title {
	display: block;
	font-size: 32rpx;
	font-weight: 600;
	color: #FFFFFF;
	text-align: center;
	margin-bottom: 24rpx;
}

.voice-btn {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 160rpx;
	height: 160rpx;
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 50%;
	margin: 0 auto 24rpx;
}

.voice-icon {
	font-size: 48rpx;
	margin-bottom: 8rpx;
}

.voice-text {
	font-size: 28rpx;
	color: #FFFFFF;
}

.voice-result {
	background-color: rgba(255, 255, 255, 0.2);
	border-radius: 12rpx;
	padding: 16rpx;
}

.result-label {
	font-size: 26rpx;
	color: rgba(255, 255, 255, 0.8);
}

.result-text {
	font-size: 28rpx;
	color: #FFFFFF;
}
</style>
