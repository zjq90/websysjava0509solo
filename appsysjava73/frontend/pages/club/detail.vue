<template>
	<view class="container">
		<view class="club-header" v-if="club">
			<image :src="club.logo" mode="aspectFill" class="club-logo" />
			<view class="club-info">
				<view class="club-title">
					<text class="club-name">{{ club.name }}</text>
					<text class="tag tag-primary">{{ club.categoryName }}</text>
				</view>
				<text class="club-school">{{ club.schoolName }}</text>
				<view class="club-stats">
					<view class="stat">
						<text class="stat-num">{{ club.memberCount }}</text>
						<text class="stat-label">成员</text>
					</view>
					<view class="stat">
						<text class="stat-num">{{ club.followCount }}</text>
						<text class="stat-label">关注</text>
					</view>
					<view class="stat">
						<text class="stat-num">{{ club.activityCount || 0 }}</text>
						<text class="stat-label">活动</text>
					</view>
				</view>
			</view>
		</view>

		<view class="action-bar" v-if="club">
			<button 
				class="btn-follow" 
				:class="{ followed: isFollowed }"
				@click="toggleFollow"
			>
				<text class="iconfont" :class="isFollowed ? 'icon-heart-fill' : 'icon-heart'"></text>
				{{ isFollowed ? '已关注' : '关注' }}
			</button>
			<button class="btn-primary" @click="goRecruit" v-if="club.recruiting === 1">
				<text class="iconfont icon-join"></text>
				申请加入
			</button>
			<button class="btn-primary" @click="goManage" v-if="isManager">
				<text class="iconfont icon-setting"></text>
				管理社团
			</button>
		</view>

		<view class="tabs">
			<view 
				class="tab-item" 
				:class="{ active: currentTab === 'intro' }"
				@click="currentTab = 'intro'"
			>
				社团介绍
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentTab === 'activity' }"
				@click="currentTab = 'activity'"
			>
				历史活动
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentTab === 'honor' }"
				@click="currentTab = 'honor'"
			>
				获奖荣誉
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentTab === 'contact' }"
				@click="currentTab = 'contact'"
			>
				联系方式
			</view>
		</view>

		<view class="tab-content" v-if="club">
			<view class="intro-section" v-show="currentTab === 'intro'">
				<view class="section-card">
					<view class="section-title">
						<text class="iconfont icon-info"></text>
						社团简介
					</view>
					<text class="section-content">{{ club.description }}</text>
				</view>
				
				<view class="section-card" v-if="club.purpose">
					<view class="section-title">
						<text class="iconfont icon-target"></text>
						社团宗旨
					</view>
					<text class="section-content">{{ club.purpose }}</text>
				</view>

				<view class="section-card">
					<view class="section-title">
						<text class="iconfont icon-user"></text>
						现任负责人
					</view>
					<view class="leader-info">
						<view class="leader-avatar">
							{{ club.leaderName ? club.leaderName.charAt(0) : '负' }}
						</view>
						<view class="leader-detail">
							<text class="leader-name">{{ club.leaderName || '暂无' }}</text>
							<text class="leader-role">社长</text>
						</view>
					</view>
				</view>
			</view>

			<view class="activity-section" v-show="currentTab === 'activity'">
				<view class="section-card" v-if="activities && activities.length > 0">
					<view 
						class="activity-item" 
						v-for="activity in activities" 
						:key="activity.id"
						@click="goActivityDetail(activity.id)"
					>
						<image :src="activity.coverImage" mode="aspectFill" class="activity-cover" />
						<view class="activity-info">
							<text class="activity-title">{{ activity.title }}</text>
							<text class="activity-time">{{ activity.startTime }}</text>
							<view class="activity-stats">
								<text class="stat-text">
									<text class="iconfont icon-user"></text>
									{{ activity.participantCount }}/{{ activity.maxParticipants }}
								</text>
								<text class="activity-status" :class="'status-' + activity.status">
									{{ activity.status === 0 ? '未开始' : activity.status === 1 ? '进行中' : '已结束' }}
								</text>
							</view>
						</view>
					</view>
				</view>
				<view class="empty" v-else>
					<text class="empty-text">暂无活动记录</text>
				</view>
			</view>

			<view class="honor-section" v-show="currentTab === 'honor'">
				<view class="section-card" v-if="honors && honors.length > 0">
					<view class="honor-item" v-for="honor in honors" :key="honor.id">
						<view class="honor-icon">
							<text class="iconfont icon-trophy"></text>
						</view>
						<view class="honor-info">
							<text class="honor-title">{{ honor.title }}</text>
							<text class="honor-desc">{{ honor.description }}</text>
							<text class="honor-date">{{ honor.awardDate }}</text>
						</view>
					</view>
				</view>
				<view class="empty" v-else>
					<text class="empty-text">暂无荣誉记录</text>
				</view>
			</view>

			<view class="contact-section" v-show="currentTab === 'contact'">
				<view class="section-card">
					<view class="contact-item" v-if="club.contactPhone">
						<text class="contact-icon">
							<text class="iconfont icon-phone"></text>
						</text>
						<view class="contact-info">
							<text class="contact-label">联系电话</text>
							<text class="contact-value">{{ club.contactPhone }}</text>
						</view>
						<button class="contact-btn" @click="callPhone(club.contactPhone)">拨打</button>
					</view>
					
					<view class="contact-item" v-if="club.contactEmail">
						<text class="contact-icon">
							<text class="iconfont icon-email"></text>
						</text>
						<view class="contact-info">
							<text class="contact-label">联系邮箱</text>
							<text class="contact-value">{{ club.contactEmail }}</text>
						</view>
					</view>
					
					<view class="contact-item" v-if="club.qqGroup">
						<text class="contact-icon">
							<text class="iconfont icon-qq"></text>
						</text>
						<view class="contact-info">
							<text class="contact-label">QQ群</text>
							<text class="contact-value">{{ club.qqGroup }}</text>
						</view>
						<button class="contact-btn" @click="copyText(club.qqGroup)">复制</button>
					</view>
					
					<view class="contact-item" v-if="club.wechat">
						<text class="contact-icon">
							<text class="iconfont icon-wechat"></text>
						</text>
						<view class="contact-info">
							<text class="contact-label">微信号</text>
							<text class="contact-value">{{ club.wechat }}</text>
						</view>
						<button class="contact-btn" @click="copyText(club.wechat)">复制</button>
					</view>
				</view>
			</view>
		</view>

		<view class="album-section" v-if="photos && photos.length > 0">
			<view class="section-title album-title">
				<text class="iconfont icon-image"></text>
				活动相册
			</view>
			<scroll-view scroll-x class="album-scroll">
				<image 
					:src="photo.url" 
					mode="aspectFill" 
					class="album-photo"
					v-for="photo in photos"
					:key="photo.id"
				/>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import { clubApi, followApi, activityApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				club: null,
				activities: [],
				honors: [],
				photos: [],
				isFollowed: false,
				isManager: false,
				currentTab: 'intro',
				loading: false
			}
		},
		onLoad(options) {
			this.clubId = options.id
			this.loadDetail()
		},
		methods: {
			async loadDetail() {
				this.loading = true
				try {
					const res = await clubApi.getDetail(this.clubId)
					this.club = res.club || res
					this.activities = res.activities || []
					this.honors = res.honors || []
					this.photos = res.photos || []
					
					const userId = store.state.userInfo?.id
					if (userId && this.club.leaderId === userId) {
						this.isManager = true
					}
					
					this.checkFollowStatus()
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '加载失败', icon: 'none' })
				} finally {
					this.loading = false
				}
			},
			async checkFollowStatus() {
				try {
					const res = await followApi.checkStatus(this.clubId)
					this.isFollowed = res === true || res.followed === true
				} catch (e) {
					console.error(e)
				}
			},
			async toggleFollow() {
				try {
					if (this.isFollowed) {
						await followApi.unfollow(this.clubId)
						this.isFollowed = false
						this.club.followCount--
						uni.showToast({ title: '已取消关注', icon: 'success' })
					} else {
						await followApi.follow(this.clubId)
						this.isFollowed = true
						this.club.followCount++
						uni.showToast({ title: '关注成功', icon: 'success' })
					}
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '操作失败', icon: 'none' })
				}
			},
			goRecruit() {
				uni.navigateTo({
					url: `/pages/recruit/list?clubId=${this.clubId}`
				})
			},
			goManage() {
				uni.switchTab({
					url: '/pages/manage/index'
				})
			},
			goActivityDetail(id) {
				uni.navigateTo({
					url: `/pages/club/activity-detail?id=${id}`
				})
			},
			callPhone(phone) {
				uni.makePhoneCall({ phoneNumber: phone })
			},
			copyText(text) {
				uni.setClipboardData({
					data: text,
					success: () => uni.showToast({ title: '已复制', icon: 'success' })
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 40rpx;
	}

	.club-header {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		padding: 40rpx 30rpx;
		display: flex;
		align-items: center;
	}

	.club-logo {
		width: 160rpx;
		height: 160rpx;
		border-radius: 20rpx;
		border: 4rpx solid #fff;
		margin-right: 30rpx;
		flex-shrink: 0;
	}

	.club-info {
		flex: 1;
		color: #fff;
	}

	.club-title {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.club-name {
		font-size: 36rpx;
		font-weight: bold;
		margin-right: 15rpx;
	}

	.tag {
		font-size: 22rpx;
		padding: 6rpx 16rpx;
		border-radius: 20rpx;
		background: rgba(255, 255, 255, 0.3);
		color: #fff;
	}

	.club-school {
		font-size: 26rpx;
		opacity: 0.9;
		margin-bottom: 20rpx;
		display: block;
	}

	.club-stats {
		display: flex;
	}

	.stat {
		text-align: center;
		margin-right: 40rpx;
	}

	.stat-num {
		font-size: 32rpx;
		font-weight: bold;
		display: block;
	}

	.stat-label {
		font-size: 22rpx;
		opacity: 0.8;
	}

	.action-bar {
		background: #fff;
		padding: 20rpx 30rpx;
		display: flex;
		gap: 20rpx;
	}

	.btn-follow,
	.btn-primary {
		flex: 1;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border: none;
	}

	.btn-follow {
		background: #f5f5f5;
		color: #666;
	}

	.btn-follow.followed {
		background: #fff0f0;
		color: #ff4d4f;
	}

	.btn-primary {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.btn-follow .iconfont,
	.btn-primary .iconfont {
		margin-right: 8rpx;
	}

	.tabs {
		display: flex;
		background: #fff;
		margin-top: 20rpx;
		padding: 0 20rpx;
	}

	.tab-item {
		flex: 1;
		text-align: center;
		padding: 25rpx 0;
		font-size: 28rpx;
		color: #666;
		position: relative;
	}

	.tab-item.active {
		color: #667eea;
		font-weight: 500;
	}

	.tab-item.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 60rpx;
		height: 4rpx;
		background: #667eea;
		border-radius: 2rpx;
	}

	.tab-content {
		padding: 20rpx;
	}

	.section-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
	}

	.section-title .iconfont {
		color: #667eea;
		margin-right: 10rpx;
	}

	.section-content {
		font-size: 26rpx;
		color: #666;
		line-height: 1.8;
	}

	.leader-info {
		display: flex;
		align-items: center;
	}

	.leader-avatar {
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
	}

	.leader-name {
		font-size: 30rpx;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.leader-role {
		font-size: 24rpx;
		color: #667eea;
		background: #e6f7ff;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
	}

	.activity-item {
		display: flex;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.activity-item:last-child {
		border-bottom: none;
	}

	.activity-cover {
		width: 180rpx;
		height: 120rpx;
		border-radius: 12rpx;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.activity-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
	}

	.activity-title {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		margin-bottom: 8rpx;
	}

	.activity-time {
		font-size: 24rpx;
		color: #999;
		margin-bottom: 8rpx;
	}

	.activity-stats {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.stat-text {
		font-size: 22rpx;
		color: #999;
	}

	.stat-text .iconfont {
		margin-right: 5rpx;
	}

	.activity-status {
		font-size: 22rpx;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
	}

	.status-0 {
		background: #e6f7ff;
		color: #1890ff;
	}

	.status-1 {
		background: #f6ffed;
		color: #52c41a;
	}

	.status-2 {
		background: #f5f5f5;
		color: #999;
	}

	.honor-item {
		display: flex;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.honor-item:last-child {
		border-bottom: none;
	}

	.honor-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #ffa751 0%, #ffe259 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.honor-icon .iconfont {
		color: #fff;
		font-size: 36rpx;
	}

	.honor-info {
		flex: 1;
	}

	.honor-title {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		display: block;
		margin-bottom: 8rpx;
	}

	.honor-desc {
		font-size: 24rpx;
		color: #666;
		display: block;
		margin-bottom: 8rpx;
	}

	.honor-date {
		font-size: 22rpx;
		color: #999;
	}

	.contact-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.contact-item:last-child {
		border-bottom: none;
	}

	.contact-icon {
		width: 70rpx;
		height: 70rpx;
		border-radius: 50%;
		background: #f0f5ff;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.contact-icon .iconfont {
		color: #667eea;
		font-size: 32rpx;
	}

	.contact-info {
		flex: 1;
	}

	.contact-label {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 4rpx;
	}

	.contact-value {
		font-size: 28rpx;
		color: #333;
	}

	.contact-btn {
		font-size: 24rpx;
		color: #667eea;
		background: #f0f5ff;
		border: none;
		padding: 10rpx 20rpx;
		border-radius: 20rpx;
		line-height: 1.4;
	}

	.album-section {
		padding: 0 20rpx;
		margin-top: 20rpx;
	}

	.album-title {
		margin-bottom: 20rpx;
	}

	.album-scroll {
		white-space: nowrap;
	}

	.album-photo {
		width: 240rpx;
		height: 180rpx;
		border-radius: 12rpx;
		margin-right: 15rpx;
		display: inline-block;
	}

	.empty {
		text-align: center;
		padding: 80rpx 0;
	}

	.empty-text {
		font-size: 28rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
