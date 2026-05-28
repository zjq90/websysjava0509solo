<template>
	<view class="container">
		<view class="header">
			<view class="search-box">
				<text class="iconfont icon-search"></text>
				<input 
					type="text" 
					v-model="keyword" 
					placeholder="搜索社团..." 
					class="search-input"
					@confirm="loadData"
				/>
			</view>
		</view>

		<view class="category-tabs">
			<scroll-view scroll-x class="tabs-scroll">
				<view 
					class="tab-item" 
					:class="{ active: currentCategory === item.code }"
					v-for="item in categories" 
					:key="item.code"
					@click="selectCategory(item.code)"
				>
					{{ item.name }}
				</view>
			</scroll-view>
		</view>

		<view class="school-filter">
			<picker :range="schoolOptions" range-key="name" @change="onSchoolChange">
				<view class="picker-box">
					<text class="picker-text">{{ currentSchool ? currentSchool.name : '选择学校' }}</text>
					<text class="iconfont icon-down"></text>
				</view>
			</picker>
		</view>

		<view class="club-list" v-if="clubs.length > 0">
			<view 
				class="club-item" 
				v-for="club in clubs" 
				:key="club.id"
				@click="goDetail(club.id)"
			>
				<image :src="club.logo" mode="aspectFill" class="club-logo" />
				<view class="club-info">
					<view class="club-header">
						<text class="club-name">{{ club.name }}</text>
						<text class="tag tag-primary">{{ club.categoryName }}</text>
					</view>
					<text class="club-desc">{{ club.description }}</text>
					<view class="club-footer">
						<view class="club-stats">
							<text class="stat-item">
								<text class="iconfont icon-user"></text>
								{{ club.memberCount }}人
							</text>
							<text class="stat-item">
								<text class="iconfont icon-heart"></text>
								{{ club.followCount }}
							</text>
							<text class="stat-item">
								<text class="iconfont icon-activity"></text>
								{{ club.activityCount }}
							</text>
						</view>
						<text class="club-school">{{ club.schoolName }}</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<image src="/static/empty.png" mode="aspectFit" class="empty-icon" />
			<text class="empty-text">暂无社团</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="load-more" v-if="hasMore && !loading" @click="loadMore">
			<text>加载更多</text>
		</view>
	</view>
</template>

<script>
	import { clubApi } from '@/api/index.js'

	export default {
		data() {
			return {
				keyword: '',
				currentCategory: '',
				currentSchool: null,
				categories: [],
				schools: [],
				schoolOptions: [],
				clubs: [],
				pageNum: 1,
				pageSize: 10,
				total: 0,
				hasMore: true,
				loading: false
			}
		},
		onLoad() {
			this.loadCategories()
			this.loadData()
		},
		onPullDownRefresh() {
			this.pageNum = 1
			this.hasMore = true
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			async loadCategories() {
				try {
					this.categories = await clubApi.getCategories()
				} catch (e) {
					console.error(e)
				}
			},
			selectCategory(code) {
				this.currentCategory = this.currentCategory === code ? '' : code
				this.pageNum = 1
				this.hasMore = true
				this.loadData()
			},
			onSchoolChange(e) {
				const index = e.detail.value
				this.currentSchool = index > 0 ? this.schoolOptions[index] : null
				this.pageNum = 1
				this.hasMore = true
				this.loadData()
			},
			async loadData() {
				this.loading = true
				try {
					const params = {
						pageNum: this.pageNum,
						pageSize: this.pageSize,
						keyword: this.keyword || undefined,
						category: this.currentCategory || undefined,
						schoolId: this.currentSchool ? this.currentSchool.id : undefined
					}
					const res = await clubApi.getList(params)
					if (this.pageNum === 1) {
						this.clubs = res.list || []
					} else {
						this.clubs = [...this.clubs, ...(res.list || [])]
					}
					this.total = res.total || 0
					this.hasMore = this.clubs.length < this.total
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			loadMore() {
				if (this.hasMore && !this.loading) {
					this.pageNum++
					this.loadData()
				}
			},
			goDetail(id) {
				uni.navigateTo({
					url: `/pages/club/detail?id=${id}`
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
	}

	.header {
		padding: 20rpx;
		background: #fff;
	}

	.search-box {
		display: flex;
		align-items: center;
		background: #f5f5f5;
		border-radius: 50rpx;
		padding: 20rpx 30rpx;
	}

	.icon-search {
		color: #999;
		font-size: 28rpx;
		margin-right: 15rpx;
	}

	.search-input {
		flex: 1;
		font-size: 28rpx;
	}

	.category-tabs {
		background: #fff;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #eee;
	}

	.tabs-scroll {
		white-space: nowrap;
		padding: 0 20rpx;
	}

	.tab-item {
		display: inline-block;
		padding: 12rpx 30rpx;
		margin-right: 20rpx;
		font-size: 26rpx;
		color: #666;
		background: #f5f5f5;
		border-radius: 30rpx;
	}

	.tab-item.active {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.school-filter {
		padding: 20rpx;
		background: #fff;
		margin-bottom: 20rpx;
	}

	.picker-box {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 20rpx;
		background: #f5f5f5;
		border-radius: 12rpx;
	}

	.picker-text {
		font-size: 28rpx;
		color: #333;
	}

	.icon-down {
		font-size: 24rpx;
		color: #999;
	}

	.club-list {
		padding: 0 20rpx;
	}

	.club-item {
		display: flex;
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.club-logo {
		width: 140rpx;
		height: 140rpx;
		border-radius: 12rpx;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.club-info {
		flex: 1;
		display: flex;
		flex-direction: column;
	}

	.club-header {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.club-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-right: 15rpx;
	}

	.tag {
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-radius: 20rpx;
	}

	.tag-primary {
		background: #e6f7ff;
		color: #1890ff;
	}

	.club-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.5;
		margin-bottom: 15rpx;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}

	.club-footer {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.club-stats {
		display: flex;
	}

	.stat-item {
		font-size: 22rpx;
		color: #999;
		margin-right: 20rpx;
	}

	.stat-item .iconfont {
		margin-right: 5rpx;
	}

	.club-school {
		font-size: 22rpx;
		color: #667eea;
	}

	.empty {
		text-align: center;
		padding: 100rpx 0;
	}

	.empty-icon {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 20rpx;
	}

	.empty-text {
		font-size: 28rpx;
		color: #999;
	}

	.loading,
	.load-more {
		text-align: center;
		padding: 30rpx;
		font-size: 26rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
