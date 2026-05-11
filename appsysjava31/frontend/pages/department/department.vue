<template>
	<view class="department-container">
		<view class="search-bar">
			<input 
				v-model="keyword" 
				type="text" 
				placeholder="搜索科室名称" 
				class="search-input"
				@confirm="searchDepartment"
			/>
			<view class="search-btn" @click="searchDepartment">
				<text>搜索</text>
			</view>
		</view>
		
		<view class="category-section">
			<view class="category-item" 
				v-for="cat in categories" 
				:key="cat.name"
				:class="{ active: activeCategory === cat.name }"
				@click="selectCategory(cat)"
			>
				<text>{{ cat.name }}</text>
			</view>
		</view>
		
		<view class="department-list">
			<view class="dept-card" v-for="dept in filteredDepartments" :key="dept.id" @click="goToDoctor(dept.id)">
				<view class="dept-icon-wrap">
					<text class="dept-icon">{{ dept.icon || '🏥' }}</text>
				</view>
				<view class="dept-info">
					<text class="dept-name">{{ dept.name }}</text>
					<text class="dept-desc">{{ dept.description || '专业医疗团队' }}</text>
				</view>
				<view class="dept-arrow">
					<text>></text>
				</view>
			</view>
			
			<view class="empty-state" v-if="filteredDepartments.length === 0">
				<text class="empty-icon">🔍</text>
				<text>未找到相关科室</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			keyword: '',
			departments: [],
			activeCategory: '全部',
			categories: [
				{ name: '全部', depts: [] },
				{ name: '内科', depts: ['内科', '心血管内科', '消化内科'] },
				{ name: '外科', depts: ['外科', '骨科', '神经外科'] },
				{ name: '专科', depts: ['儿科', '妇产科', '眼科'] }
			]
		}
	},
	computed: {
		filteredDepartments() {
			let result = [...this.departments]
			
			if (this.keyword) {
				result = result.filter(d => 
					d.name.includes(this.keyword) || 
					(d.description && d.description.includes(this.keyword))
				)
			}
			
			if (this.activeCategory !== '全部') {
				const cat = this.categories.find(c => c.name === this.activeCategory)
				if (cat) {
					result = result.filter(d => cat.depts.includes(d.name))
				}
			}
			
			return result
		}
	},
	onLoad() {
		this.loadDepartments()
	},
	methods: {
		loadDepartments() {
			this.$request({
				url: '/public/departments',
				method: 'GET'
			}).then(res => {
				this.departments = res.data || []
			}).catch(err => {
				console.log(err)
			})
		},
		
		searchDepartment() {
			uni.showToast({
				title: this.keyword ? `搜索: ${this.keyword}` : '请输入搜索关键词',
				icon: 'none'
			})
		},
		
		selectCategory(cat) {
			this.activeCategory = cat.name
		},
		
		goToDoctor(deptId) {
			uni.navigateTo({
				url: `/pages/doctor/doctor?departmentId=${deptId}`
			})
		}
	}
}
</script>

<style>
.department-container {
	min-height: 100vh;
	background-color: #F5F5F5;
}

.search-bar {
	display: flex;
	padding: 24rpx;
	background-color: #FFFFFF;
}

.search-input {
	flex: 1;
	background-color: #F5F5F5;
	border-radius: 24rpx;
	padding: 0 24rpx;
	height: 72rpx;
	font-size: 28rpx;
}

.search-btn {
	margin-left: 16rpx;
	background-color: #007AFF;
	color: #FFFFFF;
	border-radius: 24rpx;
	padding: 0 32rpx;
	height: 72rpx;
	line-height: 72rpx;
	font-size: 28rpx;
}

.category-section {
	display: flex;
	padding: 16rpx 24rpx;
	background-color: #FFFFFF;
	border-top: 1rpx solid #F0F0F0;
	overflow-x: auto;
}

.category-item {
	flex-shrink: 0;
	padding: 12rpx 24rpx;
	margin-right: 16rpx;
	border-radius: 24rpx;
	font-size: 26rpx;
	background-color: #F5F5F5;
	color: #666666;
}

.category-item.active {
	background-color: #007AFF;
	color: #FFFFFF;
}

.department-list {
	padding: 16rpx;
}

.dept-card {
	display: flex;
	align-items: center;
	background-color: #FFFFFF;
	border-radius: 16rpx;
	padding: 24rpx;
	margin-bottom: 16rpx;
}

.dept-icon-wrap {
	width: 96rpx;
	height: 96rpx;
	background-color: #E3F2FF;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin-right: 20rpx;
}

.dept-icon {
	font-size: 48rpx;
}

.dept-info {
	flex: 1;
}

.dept-name {
	display: block;
	font-size: 32rpx;
	font-weight: 600;
	color: #333333;
	margin-bottom: 8rpx;
}

.dept-desc {
	font-size: 26rpx;
	color: #999999;
}

.dept-arrow {
	font-size: 32rpx;
	color: #CCCCCC;
}
</style>
