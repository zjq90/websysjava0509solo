<template>
	<view class="settings-container">
		<view class="settings-section">
			<view class="section-title">消息通知设置</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">📅</view>
					<view class="setting-detail">
						<text class="setting-name">预约成功通知</text>
						<text class="setting-desc">预约挂号成功后发送通知</text>
					</view>
				</view>
				<switch 
					:checked="settings.appointmentSuccess" 
					@change="toggleSetting('appointmentSuccess')"
					color="#007AFF"
				/>
			</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">⏰</view>
					<view class="setting-detail">
						<text class="setting-name">就诊前提醒</text>
						<text class="setting-desc">就诊前1小时发送提醒消息</text>
					</view>
				</view>
				<switch 
					:checked="settings.visitReminder" 
					@change="toggleSetting('visitReminder')"
					color="#007AFF"
				/>
			</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">🚫</view>
					<view class="setting-detail">
						<text class="setting-name">医生停诊通知</text>
						<text class="setting-desc">医生临时停诊时及时通知</text>
					</view>
				</view>
				<switch 
					:checked="settings.doctorCancel" 
					@change="toggleSetting('doctorCancel')"
					color="#007AFF"
				/>
			</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">📄</view>
					<view class="setting-detail">
						<text class="setting-name">报告结果通知</text>
						<text class="setting-desc">检验检查报告出具后通知</text>
					</view>
				</view>
				<switch 
					:checked="settings.reportReady" 
					@change="toggleSetting('reportReady')"
					color="#007AFF"
				/>
			</view>
		</view>
		
		<view class="settings-section">
			<view class="section-title">其他设置</view>
			
			<view class="setting-item" @click="toggleElderMode">
				<view class="setting-info">
					<view class="setting-icon">👵</view>
					<view class="setting-detail">
						<text class="setting-name">长辈模式</text>
						<text class="setting-desc">字体放大、界面简化</text>
					</view>
				</view>
				<view class="setting-status">
					<text class="status-text">{{ getApp().globalData.elderMode ? '已开启' : '已关闭' }}</text>
					<text class="arrow">></text>
				</view>
			</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">🔔</view>
					<view class="setting-detail">
						<text class="setting-name">消息免打扰</text>
						<text class="setting-desc">夜间时段不推送消息</text>
					</view>
				</view>
				<switch 
					:checked="settings.doNotDisturb" 
					@change="toggleSetting('doNotDisturb')"
					color="#007AFF"
				/>
			</view>
			
			<view class="setting-item" v-if="settings.doNotDisturb">
				<view class="setting-info">
					<view class="setting-icon">🌙</view>
					<view class="setting-detail">
						<text class="setting-name">免打扰时段</text>
						<text class="setting-desc">{{ settings.dndStart || '22:00' }} - {{ settings.dndEnd || '07:00' }}</text>
					</view>
				</view>
				<text class="arrow">></text>
			</view>
		</view>
		
		<view class="settings-section">
			<view class="section-title">关于</view>
			
			<view class="setting-item">
				<view class="setting-info">
					<view class="setting-icon">📱</view>
					<view class="setting-detail">
						<text class="setting-name">版本信息</text>
					</view>
				</view>
				<view class="setting-status">
					<text class="status-text">v1.0.0</text>
				</view>
			</view>
			
			<view class="setting-item" @click="showPrivacy">
				<view class="setting-info">
					<view class="setting-icon">🔒</view>
					<view class="setting-detail">
						<text class="setting-name">隐私政策</text>
					</view>
				</view>
				<text class="arrow">></text>
			</view>
			
			<view class="setting-item" @click="showTerms">
				<view class="setting-info">
					<view class="setting-icon">📋</view>
					<view class="setting-detail">
						<text class="setting-name">用户协议</text>
					</view>
				</view>
				<text class="arrow">></text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			settings: {
				appointmentSuccess: true,
				visitReminder: true,
				doctorCancel: true,
				reportReady: true,
				doNotDisturb: false,
				dndStart: '22:00',
				dndEnd: '07:00'
			}
		}
	},
	onLoad() {
		this.loadSettings()
	},
	methods: {
		loadSettings() {
			const savedSettings = uni.getStorageSync('notificationSettings')
			if (savedSettings) {
				this.settings = { ...this.settings, ...savedSettings }
			}
		},
		
		saveSettings() {
			uni.setStorageSync('notificationSettings', this.settings)
		},
		
		toggleSetting(key) {
			this.settings[key] = !this.settings[key]
			this.saveSettings()
			uni.showToast({
				title: this.settings[key] ? '已开启' : '已关闭',
				icon: 'none'
			})
		},
		
		toggleElderMode() {
			const app = getApp()
			app.globalData.elderMode = !app.globalData.elderMode
			uni.showToast({
				title: app.globalData.elderMode ? '已开启长辈模式' : '已退出长辈模式',
				icon: 'none'
			})
		},
		
		showPrivacy() {
			uni.showModal({
				title: '隐私政策',
				content: '我们重视您的隐私保护。本应用严格按照相关法律法规处理您的个人信息。敏感数据（身份证、手机号等）采用国密SM4算法加密存储和传输。',
				showCancel: false
			})
		},
		
		showTerms() {
			uni.showModal({
				title: '用户协议',
				content: '欢迎使用智慧医院预约系统。使用本应用即表示您同意遵守相关服务条款。请合理使用预约资源，按时就诊。',
				showCancel: false
			})
		}
	}
}
</script>

<style>
.settings-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 40rpx;
}

.settings-section {
	background-color: #FFFFFF;
	margin-top: 24rpx;
}

.section-title {
	font-size: 26rpx;
	color: #666666;
	padding: 24rpx 32rpx 16rpx;
}

.setting-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 28rpx 32rpx;
	border-top: 1rpx solid #F0F0F0;
}

.setting-info {
	display: flex;
	align-items: center;
	flex: 1;
}

.setting-icon {
	font-size: 40rpx;
	margin-right: 20rpx;
}

.setting-detail {
	display: flex;
	flex-direction: column;
}

.setting-name {
	font-size: 30rpx;
	color: #333333;
	margin-bottom: 4rpx;
}

.setting-desc {
	font-size: 24rpx;
	color: #999999;
}

.setting-status {
	display: flex;
	align-items: center;
}

.status-text {
	font-size: 26rpx;
	color: #999999;
	margin-right: 8rpx;
}

.arrow {
	font-size: 32rpx;
	color: #CCCCCC;
}
</style>
