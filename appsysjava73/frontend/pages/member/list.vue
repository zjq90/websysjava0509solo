<template>
	<view class="container">
		<view class="header-bar">
			<view class="search-box">
				<text class="iconfont icon-search"></text>
				<input 
					type="text" 
					v-model="keyword" 
					placeholder="搜索成员姓名/学号..."
					class="search-input"
					@confirm="loadData"
				/>
			</view>
			<picker :range="departmentOptions" range-key="name" @change="onDeptChange">
				<view class="filter-btn">
					<text class="filter-text">{{ currentDept ? currentDept.name : '全部部门' }}</text>
					<text class="iconfont icon-down"></text>
				</view>
			</picker>
		</view>

		<view class="stats-bar">
			<view class="stat-item">
				<text class="stat-num">{{ total }}</text>
				<text class="stat-label">总成员</text>
			</view>
			<view class="stat-item">
				<text class="stat-num">{{ activeCount }}</text>
				<text class="stat-label">活跃成员</text>
			</view>
			<view class="stat-item">
				<text class="stat-num">{{ departmentOptions.length - 1 }}</text>
				<text class="stat-label">部门数</text>
			</view>
		</view>

		<view class="member-list" v-if="members.length > 0">
			<view 
				class="member-card" 
				v-for="member in members" 
				:key="member.id"
				@click="goDetail(member)"
			>
				<view class="member-avatar" :class="'role-' + member.role">
					{{ member.userName ? member.userName.charAt(0) : '成' }}
				</view>
				<view class="member-info">
					<view class="member-header">
						<text class="member-name">{{ member.userName }}</text>
						<text class="role-tag" :class="'role-' + member.role">
							{{ getRoleText(member.role) }}
						</text>
						<text class="active-tag" v-if="member.active === 1">活跃</text>
					</view>
					<text class="member-dept">{{ member.departmentName || '未分配部门' }}</text>
					<view class="member-meta">
						<text class="meta-text">学号: {{ member.studentNo }}</text>
						<text class="meta-text">{{ member.department || '' }}</text>
					</view>
				</view>
				<text class="iconfont icon-right arrow"></text>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无成员</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="load-more" v-if="hasMore && !loading" @click="loadMore">
			<text>加载更多</text>
		</view>

		<view class="action-bar" v-if="isManager">
			<button class="btn btn-secondary" @click="goDepartment">
				<text class="iconfont icon-team"></text>
				部门管理
			</button>
			<button class="btn btn-secondary" @click="exportMembers">
				<text class="iconfont icon-export"></text>
				导出名单
			</button>
			<button class="btn btn-primary" @click="addMember">
				<text class="iconfont icon-add"></text>
				添加成员
			</button>
		</view>
	</view>
</template>

<script>
	import { memberApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				members: [],
				departmentOptions: [{ id: null, name: '全部部门' }],
				currentDept: null,
				keyword: '',
				pageNum: 1,
				pageSize: 20,
				total: 0,
				activeCount: 0,
				hasMore: true,
				loading: false,
				isManager: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkManager()
			this.loadDepartments()
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
			checkManager() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id === this.clubId)
			},
			getRoleText(role) {
				const map = { 0: '普通成员', 1: '部门负责人', 2: '社长' }
				return map[role] || '成员'
			},
			async loadDepartments() {
				try {
					const res = await memberApi.getDepartments(this.clubId)
					const depts = res || []
					this.departmentOptions = [{ id: null, name: '全部部门' }, ...depts]
				} catch (e) {
					console.error(e)
				}
			},
			onDeptChange(e) {
				const index = e.detail.value
				this.currentDept = this.departmentOptions[index]
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
						departmentId: this.currentDept?.id || undefined
					}
					const res = await memberApi.getList(this.clubId, params)
					const list = res.list || []
					this.total = res.total || 0
					this.activeCount = list.filter(m => m.active === 1).length
					
					if (this.pageNum === 1) {
						this.members = list
					} else {
						this.members = [...this.members, ...list]
					}
					this.hasMore = this.members.length < this.total
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
			goDetail(member) {
				uni.navigateTo({
					url: `/pages/member/detail?clubId=${this.clubId}&memberId=${member.id}&userId=${member.userId}`
				})
			},
			goDepartment() {
				uni.navigateTo({
					url: `/pages/member/department?clubId=${this.clubId}`
				})
			},
			async exportMembers() {
				try {
					await memberApi.exportList(this.clubId)
					uni.showToast({ title: '导出成功', icon: 'success' })
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '导出失败', icon: 'none' })
				}
			},
			addMember() {
				uni.showToast({ title: '添加成员功能', icon: 'none' })
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 140rpx;
	}

	.header-bar {
		background: #fff;
		padding: 20rpx;
		display: flex;
		gap: 20rpx;
		margin-bottom: 20rpx;
	}

	.search-box {
		flex: 1;
		display: flex;
		align-items: center;
		background: #f5f5f5;
		border-radius: 50rpx;
		padding: 15rpx 25rpx;
	}

	.search-box .iconfont {
		color: #999;
		font-size: 28rpx;
		margin-right: 15rpx;
	}

	.search-input {
		flex: 1;
		font-size: 28rpx;
	}

	.filter-btn {
		display: flex;
		align-items: center;
		padding: 15rpx 25rpx;
		background: #f5f5f5;
		border-radius: 50rpx;
		font-size: 26rpx;
		color: #666;
	}

	.filter-text {
		margin-right: 10rpx;
	}

	.filter-btn .iconfont {
		font-size: 20rpx;
		color: #999;
	}

	.stats-bar {
		background: #fff;
		padding: 30rpx;
		display: flex;
		justify-content: space-around;
		margin-bottom: 20rpx;
	}

	.stat-item {
		text-align: center;
	}

	.stat-num {
		font-size: 36rpx;
		font-weight: bold;
		color: #667eea;
		display: block;
		margin-bottom: 8rpx;
	}

	.stat-label {
		font-size: 24rpx;
		color: #999;
	}

	.member-list {
		padding: 0 20rpx;
	}

	.member-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.member-avatar {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 36rpx;
		font-weight: bold;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.member-avatar.role-2 {
		background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
	}

	.member-avatar.role-1 {
		background: linear-gradient(135deg, #ffa751 0%, #ffe259 100%);
	}

	.member-info {
		flex: 1;
		min-width: 0;
	}

	.member-header {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 10rpx;
		margin-bottom: 10rpx;
	}

	.member-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
	}

	.role-tag {
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
		background: #f0f5ff;
		color: #667eea;
	}

	.role-tag.role-2 {
		background: #fff1f0;
		color: #ff4d4f;
	}

	.role-tag.role-1 {
		background: #fff7e6;
		color: #fa8c16;
	}

	.active-tag {
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
		background: #f6ffed;
		color: #52c41a;
	}

	.member-dept {
		font-size: 26rpx;
		color: #667eea;
		margin-bottom: 8rpx;
		display: block;
	}

	.member-meta {
		display: flex;
		gap: 20rpx;
	}

	.meta-text {
		font-size: 24rpx;
		color: #999;
	}

	.arrow {
		font-size: 28rpx;
		color: #ccc;
		margin-left: 15rpx;
	}

	.action-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx;
		display: flex;
		gap: 15rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.btn {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		font-size: 26rpx;
		border: none;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.btn .iconfont {
		margin-right: 8rpx;
	}

	.btn-secondary {
		background: #f5f5f5;
		color: #666;
	}

	.btn-primary {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.empty,
	.loading,
	.load-more {
		text-align: center;
		padding: 80rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.action-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
