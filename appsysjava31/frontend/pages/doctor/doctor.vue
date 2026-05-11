<template>
	<view class="doctor-container">
		<view class="filter-bar">
			<picker 
				mode="selector" 
				:range="sortOptions" 
				@change="onSortChange"
			>
				<view class="filter-item">
					<text>{{ sortOptions[sortIndex] }}</text>
					<text class="filter-arrow">▼</text>
				</view>
			</picker>
			<picker 
				mode="selector" 
				:range="titleOptions" 
				@change="onTitleChange"
			>
				<view class="filter-item">
					<text>{{ titleOptions[titleIndex] }}</text>
					<text class="filter-arrow">▼</text>
				</view>
			</picker>
		</view>
		
		<view class="doctor-list">
			<view class="doctor-card" v-for="doc in doctors" :key="doc.id" @click="goToAppointment(doc)">
				<view class="doctor-avatar">
					<text class="avatar-icon">👨‍⚕️</text>
				</view>
				<view class="doctor-info">
					<view class="doctor-header">
						<text class="doctor-name">{{ doc.name }}</text>
						<text class="doctor-title">{{ doc.title || '主治医师' }}</text>
					</view>
					<text class="doctor-dept">{{ doc.departmentName || '内科' }}</text>
					<view class="doctor-tags">
						<text class="tag" v-for="(tag, idx) in (doc.specialties || '心血管疾病,高血压').split(',').slice(0, 3)" :key="idx">
							{{ tag }}
						</text>
					</view>
					<view class="doctor-footer">
						<text class="rating">⭐ 好评率 98%</text>
						<text class="price">挂号费 ¥{{ doc.fee || 50 }}</text>
					</view>
				</view>
			</view>
			
			<view class="empty-state" v-if="doctors.length === 0">
				<text class="empty-icon">👨‍⚕️</text>
				<text>暂无医生信息</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			departmentId: null,
			doctors: [],
			sortOptions: ['综合排序', '好评优先', '价格最低', '价格最高'],
			sortIndex: 0,
			titleOptions: ['全部职称', '主任医师', '副主任医师', '主治医师'],
			titleIndex: 0
		}
	},
	onLoad(options) {
		if (options.departmentId) {
			this.departmentId = options.departmentId
		}
		this.loadDoctors()
	},
	methods: {
		loadDoctors() {
			let url = '/public/doctors'
			if (this.departmentId) {
				url += `?departmentId=${this.departmentId}`
			}
			
			this.$request({
				url: url,
				method: 'GET'
			}).then(res => {
				this.doctors = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		onSortChange(e) {
			this.sortIndex = e.detail.value
		},
		
		onTitleChange(e) {
			this.titleIndex = e.detail.value
		},
		
		goToAppointment(doctor) {
			uni.navigateTo({
				url: `/pages/appointment/appointment?doctorId=${doctor.id}&doctorName=${doctor.name}&doctorTitle=${doctor.title}&fee=${doctor.fee || 50}`
			})
		}
	}
}
</script>

<style>
.doctor-container {
	min-height: 100vh;
	background-color: #F5F5F5;
}

.filter-bar {
	display: flex;
	padding: 16rpx 24rpx;
	background-color: #FFFFFF;
	border-bottom: 1rpx solid #F0F0F0;
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

.doctor-list {
	padding: 16rpx;
}

.doctor-card {
	display: flex;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.doctor-avatar {
	width: 120rpx;
	height: 120rpx;
	background-color: #E3F2FF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
	flex-shrink: 0;
}

.avatar-icon {
	font-size: 64rpx;
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

.doctor-dept {
	display: block;
	font-size: 26rpx;
	color: #666666;
	margin-bottom: 12rpx;
}

.doctor-tags {
	display: flex;
	flex-wrap: wrap;
	margin-bottom: 12rpx;
}

.doctor-tags .tag {
	font-size: 22rpx;
	margin-bottom: 8rpx;
}

.doctor-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.rating {
	font-size: 24rpx;
	color: #FF9500;
}

.price {
	font-size: 26rpx;
	font-weight: 600;
	color: #FF3B30;
}
</style>
