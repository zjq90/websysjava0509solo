<template>
	<view class="appointment-container">
		<view class="doctor-card">
			<view class="doctor-avatar">
				<text class="avatar-icon">👨‍⚕️</text>
			</view>
			<view class="doctor-info">
				<view class="doctor-header">
					<text class="doctor-name">{{ doctorName }}</text>
					<text class="doctor-title">{{ doctorTitle || '主治医师' }}</text>
				</view>
				<text class="fee">挂号费：¥{{ fee }}</text>
			</view>
		</view>
		
		<view class="form-section">
			<view class="section-title">选择就诊日期</view>
			<view class="date-picker">
				<view 
					class="date-item" 
					v-for="(date, idx) in dateList" 
					:key="idx"
					:class="{ active: selectedDate === date.full }"
					@click="selectDate(date.full)"
				>
					<text class="date-week">{{ date.week }}</text>
					<text class="date-day">{{ date.day }}</text>
				</view>
			</view>
		</view>
		
		<view class="form-section">
			<view class="section-title">选择就诊时段</view>
			<view class="time-slots">
				<view class="time-group">
					<text class="group-title">上午</text>
					<view class="slot-list">
						<view 
							class="slot-item" 
							v-for="slot in morningSlots" 
							:key="slot"
							:class="{ active: selectedTime === slot }"
							@click="selectTime(slot)"
						>
							{{ slot }}
						</view>
					</view>
				</view>
				<view class="time-group">
					<text class="group-title">下午</text>
					<view class="slot-list">
						<view 
							class="slot-item" 
							v-for="slot in afternoonSlots" 
							:key="slot"
							:class="{ active: selectedTime === slot }"
							@click="selectTime(slot)"
						>
							{{ slot }}
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="form-section">
			<view class="section-title">就诊人信息</view>
			<view class="input-group">
				<text class="input-label">患者姓名</text>
				<input v-model="patientName" type="text" placeholder="请输入患者姓名" class="input-box" />
			</view>
			<view class="input-group">
				<text class="input-label">联系电话</text>
				<input v-model="phone" type="number" placeholder="请输入联系电话" class="input-box" />
			</view>
			<view class="input-group">
				<text class="input-label">症状描述</text>
				<textarea 
					v-model="symptoms" 
					placeholder="请简要描述您的症状（选填）" 
					class="input-box textarea"
					maxlength="200"
				/>
			</view>
			
			<view class="voice-input" v-if="getApp().globalData.elderMode" @click="voiceInput">
				<text class="voice-icon">🎤</text>
				<text class="voice-text">语音输入症状</text>
			</view>
		</view>
		
		<view class="bottom-bar safe-area-bottom">
			<view class="price-info">
				<text class="price-label">挂号费</text>
				<text class="price-value">¥{{ fee }}</text>
			</view>
			<view class="submit-btn" @click="submitAppointment">
				确认预约
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			doctorId: null,
			doctorName: '',
			doctorTitle: '',
			fee: 50,
			patientName: '',
			phone: '',
			symptoms: '',
			selectedDate: '',
			selectedTime: '',
			dateList: [],
			morningSlots: ['08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30'],
			afternoonSlots: ['14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00']
		}
	},
	onLoad(options) {
		this.doctorId = options.doctorId
		this.doctorName = options.doctorName || ''
		this.doctorTitle = options.doctorTitle || ''
		this.fee = parseInt(options.fee) || 50
		
		this.generateDateList()
		this.loadUserInfo()
	},
	methods: {
		loadUserInfo() {
			const userInfo = uni.getStorageSync('userInfo')
			if (userInfo) {
				this.patientName = userInfo.realName || ''
			}
		},
		
		generateDateList() {
			const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
			const today = new Date()
			
			for (let i = 0; i < 7; i++) {
				const date = new Date(today)
				date.setDate(today.getDate() + i)
				
				const year = date.getFullYear()
				const month = (date.getMonth() + 1).toString().padStart(2, '0')
				const day = date.getDate().toString().padStart(2, '0')
				
				this.dateList.push({
					week: i === 0 ? '今天' : weeks[date.getDay()],
					day: date.getDate(),
					full: `${year}-${month}-${day}`
				})
			}
			
			if (this.dateList.length > 0) {
				this.selectedDate = this.dateList[0].full
			}
		},
		
		selectDate(date) {
			this.selectedDate = date
		},
		
		selectTime(time) {
			this.selectedTime = time
		},
		
		voiceInput() {
			uni.showToast({
				title: '语音输入功能演示',
				icon: 'none'
			})
			setTimeout(() => {
				this.symptoms = '最近感觉头疼，还有些咳嗽'
			}, 1000)
		},
		
		validateForm() {
			if (!this.patientName) {
				uni.showToast({ title: '请输入患者姓名', icon: 'none' })
				return false
			}
			if (!this.phone || !/^1[3-9]\d{9}$/.test(this.phone)) {
				uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
				return false
			}
			if (!this.selectedDate) {
				uni.showToast({ title: '请选择就诊日期', icon: 'none' })
				return false
			}
			if (!this.selectedTime) {
				uni.showToast({ title: '请选择就诊时段', icon: 'none' })
				return false
			}
			return true
		},
		
		submitAppointment() {
			if (!this.validateForm()) return
			
			uni.showModal({
				title: '确认预约',
				content: `确认预约${this.doctorName}医生${this.selectedDate} ${this.selectedTime}的号源？`,
				success: (res) => {
					if (res.confirm) {
						this.doSubmit()
					}
				}
			})
		},
		
		doSubmit() {
			this.$showLoading('预约中...')
			
			this.$request({
				url: '/patient/appointments',
				method: 'POST',
				data: {
					doctorId: this.doctorId,
					appointmentDate: this.selectedDate,
					appointmentTime: this.selectedTime,
					patientName: this.patientName,
					phone: this.phone,
					symptoms: this.symptoms,
					fee: this.fee
				}
			}).then(res => {
				this.$hideLoading()
				uni.showToast({
					title: '预约成功',
					icon: 'success'
				})
				setTimeout(() => {
					uni.redirectTo({
						url: '/pages/appointment-list/appointment-list'
					})
				}, 1500)
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		}
	}
}
</script>

<style>
.appointment-container {
	min-height: 100vh;
	background-color: #F5F5F5;
	padding-bottom: 160rpx;
}

.doctor-card {
	display: flex;
	align-items: center;
	background-color: #FFFFFF;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.doctor-avatar {
	width: 96rpx;
	height: 96rpx;
	background-color: #E3F2FF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
}

.avatar-icon {
	font-size: 48rpx;
}

.doctor-info {
	flex: 1;
}

.doctor-header {
	display: flex;
	align-items: center;
	margin-bottom: 8rpx;
}

.doctor-name {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
	margin-right: 12rpx;
}

.doctor-title {
	font-size: 24rpx;
	color: #007AFF;
	background-color: #E3F2FF;
	padding: 4rpx 12rpx;
	border-radius: 8rpx;
}

.fee {
	font-size: 28rpx;
	color: #FF3B30;
	font-weight: 500;
}

.form-section {
	background-color: #FFFFFF;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.section-title {
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 20rpx;
}

.date-picker {
	display: flex;
	justify-content: space-between;
	overflow-x: auto;
}

.date-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 16rpx 24rpx;
	border-radius: 12rpx;
	background-color: #F5F5F5;
	margin-right: 12rpx;
	flex-shrink: 0;
}

.date-item.active {
	background-color: #007AFF;
}

.date-item.active .date-week,
.date-item.active .date-day {
	color: #FFFFFF;
}

.date-week {
	font-size: 24rpx;
	color: #666666;
	margin-bottom: 4rpx;
}

.date-day {
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
}

.time-slots {
	display: flex;
	flex-direction: column;
}

.time-group {
	margin-bottom: 24rpx;
}

.time-group:last-child {
	margin-bottom: 0;
}

.group-title {
	display: block;
	font-size: 26rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.slot-list {
	display: flex;
	flex-wrap: wrap;
	gap: 12rpx;
}

.slot-item {
	padding: 16rpx 32rpx;
	background-color: #F5F5F5;
	border-radius: 8rpx;
	font-size: 26rpx;
	color: #333333;
}

.slot-item.active {
	background-color: #007AFF;
	color: #FFFFFF;
}

.voice-input {
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 24rpx;
	background-color: #FFF5E6;
	border-radius: 12rpx;
	margin-top: 16rpx;
}

.voice-icon {
	font-size: 32rpx;
	margin-right: 8rpx;
}

.voice-text {
	font-size: 28rpx;
	color: #FF9500;
}

.textarea {
	height: 160rpx;
	resize: none;
}

.bottom-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	display: flex;
	align-items: center;
	justify-content: space-between;
	background-color: #FFFFFF;
	padding: 24rpx;
	box-shadow: 0 -4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.price-info {
	display: flex;
	align-items: baseline;
}

.price-label {
	font-size: 28rpx;
	color: #666666;
	margin-right: 8rpx;
}

.price-value {
	font-size: 40rpx;
	font-weight: 700;
	color: #FF3B30;
}

.submit-btn {
	background-color: #007AFF;
	color: #FFFFFF;
	padding: 24rpx 64rpx;
	border-radius: 48rpx;
	font-size: 30rpx;
	font-weight: 600;
}
</style>
