<template>
	<view class="appointment-list-container">
		<view class="tab-bar">
			<view 
				class="tab-item" 
				v-for="tab in tabs" 
				:key="tab.status"
				:class="{ active: activeTab === tab.status }"
				@click="switchTab(tab.status)"
			>
				<text>{{ tab.name }}</text>
				<view class="badge" v-if="getCount(tab.status) > 0">{{ getCount(tab.status) }}</view>
			</view>
		</view>
		
		<view class="appointment-list">
			<view class="appointment-card" v-for="appt in filteredAppointments" :key="appt.id">
				<view class="card-header">
					<view class="status-tag" :class="'status-' + appt.status.toLowerCase()">
						{{ getStatusText(appt.status) }}
					</view>
					<text class="appt-date">{{ appt.appointmentDate }} {{ appt.appointmentTime }}</text>
				</view>
				
				<view class="card-body">
					<view class="doctor-info">
						<view class="doctor-avatar">
							<text class="avatar-icon">👨‍⚕️</text>
						</view>
						<view class="doctor-detail">
							<view class="doctor-row">
								<text class="doctor-name">{{ appt.doctorName }}</text>
								<text class="doctor-title">{{ appt.doctorTitle || '主治医师' }}</text>
							</view>
							<text class="department">{{ appt.departmentName }}</text>
							<text class="patient">就诊人：{{ appt.patientName }}</text>
						</view>
					</view>
					
					<view class="fee-info">
						<text class="fee-label">挂号费</text>
						<text class="fee-value">¥{{ appt.fee }}</text>
					</view>
				</view>
				
				<view class="card-footer">
					<view class="action-btns" v-if="appt.status === 'PENDING'">
						<view class="btn-secondary action-btn" @click="cancelAppointment(appt)">
							取消预约
						</view>
						<view class="btn-primary action-btn" @click="payAppointment(appt)">
							立即支付
						</view>
					</view>
					<view class="action-btns" v-else-if="appt.status === 'PAID'">
						<view class="btn-secondary action-btn" @click="cancelAppointment(appt)">
							取消预约
						</view>
						<view class="btn-primary action-btn" @click="viewDetail(appt)">
							查看详情
						</view>
					</view>
					<view class="action-btns" v-else-if="appt.status === 'COMPLETED'">
						<view class="btn-secondary action-btn" @click="viewDetail(appt)">
							查看详情
						</view>
						<view class="btn-primary action-btn" @click="rateAppointment(appt)">
							评价
						</view>
					</view>
				</view>
			</view>
			
			<view class="empty-state" v-if="filteredAppointments.length === 0">
				<text class="empty-icon">📋</text>
				<text>暂无预约记录</text>
				<view class="empty-btn" @click="goToDepartment">
					去预约
				</view>
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
				{ name: '全部', status: 'ALL' },
				{ name: '待支付', status: 'PENDING' },
				{ name: '待就诊', status: 'PAID' },
				{ name: '已完成', status: 'COMPLETED' },
				{ name: '已取消', status: 'CANCELLED' }
			],
			appointments: []
		}
	},
	computed: {
		filteredAppointments() {
			if (this.activeTab === 'ALL') {
				return this.appointments
			}
			return this.appointments.filter(a => a.status === this.activeTab)
		}
	},
	onShow() {
		this.loadAppointments()
	},
	methods: {
		loadAppointments() {
			this.$request({
				url: '/patient/appointments',
				method: 'GET'
			}).then(res => {
				this.appointments = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		getCount(status) {
			if (status === 'ALL') return 0
			return this.appointments.filter(a => a.status === status).length
		},
		
		switchTab(status) {
			this.activeTab = status
		},
		
		getStatusText(status) {
			const map = {
				'PENDING': '待支付',
				'PAID': '待就诊',
				'COMPLETED': '已完成',
				'CANCELLED': '已取消'
			}
			return map[status] || status
		},
		
		cancelAppointment(appt) {
			uni.showModal({
				title: '确认取消',
				content: '确定要取消该预约吗？',
				success: (res) => {
					if (res.confirm) {
						this.doCancel(appt.id)
					}
				}
			})
		},
		
		doCancel(id) {
			this.$showLoading('取消中...')
			this.$request({
				url: `/patient/appointments/${id}/cancel`,
				method: 'PUT'
			}).then(res => {
				this.$hideLoading()
				uni.showToast({ title: '取消成功', icon: 'success' })
				this.loadAppointments()
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		},
		
		payAppointment(appt) {
			uni.showModal({
				title: '模拟支付',
				content: `需支付¥${appt.fee}，确认支付？`,
				success: (res) => {
					if (res.confirm) {
						this.doPay(appt.id)
					}
				}
			})
		},
		
		doPay(id) {
			this.$showLoading('支付中...')
			this.$request({
				url: `/patient/appointments/${id}/pay`,
				method: 'PUT'
			}).then(res => {
				this.$hideLoading()
				uni.showToast({ title: '支付成功', icon: 'success' })
				this.loadAppointments()
			}).catch(err => {
				this.$hideLoading()
				console.log(err)
			})
		},
		
		viewDetail(appt) {
			uni.showToast({
				title: '查看详情功能',
				icon: 'none'
			})
		},
		
		rateAppointment(appt) {
			uni.showToast({
				title: '评价功能',
				icon: 'none'
			})
		},
		
		goToDepartment() {
			uni.navigateTo({
				url: '/pages/department/department'
			})
		}
	}
}
</script>

<style>
.appointment-list-container {
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
}

.tab-item {
	flex: 1;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 24rpx 0;
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
	width: 48rpx;
	height: 4rpx;
	background-color: #007AFF;
	border-radius: 2rpx;
}

.tab-item .badge {
	position: absolute;
	top: 16rpx;
	right: 20rpx;
	font-size: 20rpx;
}

.appointment-list {
	padding: 16rpx;
}

.appointment-card {
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.card-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16rpx;
}

.status-tag {
	font-size: 24rpx;
	padding: 6rpx 16rpx;
	border-radius: 20rpx;
}

.status-pending {
	background-color: #FFF5E6;
	color: #FF9500;
}

.status-paid {
	background-color: #E3F2FF;
	color: #007AFF;
}

.status-completed {
	background-color: #E8F8E8;
	color: #34C759;
}

.status-cancelled {
	background-color: #F0F0F0;
	color: #999999;
}

.appt-date {
	font-size: 26rpx;
	color: #666666;
}

.card-body {
	padding: 16rpx 0;
	border-top: 1rpx solid #F0F0F0;
	border-bottom: 1rpx solid #F0F0F0;
}

.doctor-info {
	display: flex;
	align-items: center;
	margin-bottom: 16rpx;
}

.doctor-avatar {
	width: 80rpx;
	height: 80rpx;
	background-color: #E3F2FF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 16rpx;
}

.avatar-icon {
	font-size: 40rpx;
}

.doctor-detail {
	flex: 1;
}

.doctor-row {
	display: flex;
	align-items: center;
	margin-bottom: 4rpx;
}

.doctor-name {
	font-size: 30rpx;
	font-weight: 600;
	color: #333333;
	margin-right: 12rpx;
}

.doctor-title {
	font-size: 24rpx;
	color: #007AFF;
	background-color: #E3F2FF;
	padding: 2rpx 8rpx;
	border-radius: 4rpx;
}

.department, .patient {
	display: block;
	font-size: 26rpx;
	color: #666666;
}

.fee-info {
	display: flex;
	justify-content: flex-end;
	align-items: baseline;
}

.fee-label {
	font-size: 26rpx;
	color: #666666;
	margin-right: 8rpx;
}

.fee-value {
	font-size: 32rpx;
	font-weight: 700;
	color: #FF3B30;
}

.card-footer {
	padding-top: 16rpx;
}

.action-btns {
	display: flex;
	justify-content: flex-end;
	gap: 16rpx;
}

.action-btn {
	font-size: 26rpx;
	padding: 12rpx 32rpx;
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

.empty-state text {
	display: block;
	font-size: 28rpx;
	color: #999999;
	margin-bottom: 16rpx;
}

.empty-btn {
	display: inline-block;
	background-color: #007AFF;
	color: #FFFFFF;
	padding: 16rpx 48rpx;
	border-radius: 48rpx;
	font-size: 28rpx;
	margin-top: 24rpx;
}
</style>
