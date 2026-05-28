<template>
	<view class="container">
		<view class="header">
			<text class="title">社团讨论区</text>
			<button class="btn-publish" @click="goPublish" v-if="isMember">
				<text class="iconfont icon-edit"></text>
				发布
			</button>
		</view>

		<view class="post-list" v-if="posts.length > 0">
			<view 
				class="post-card" 
				v-for="post in posts" 
				:key="post.id"
				@click="goDetail(post)"
			>
				<view class="post-header">
					<view class="user-avatar">
						{{ post.authorName ? post.authorName.charAt(0) : '用' }}
					</view>
					<view class="user-info">
						<text class="user-name">{{ post.authorName }}</text>
						<text class="post-time">{{ post.createTime }}</text>
					</view>
					<text class="top-tag" v-if="post.isTop === 1">置顶</text>
				</view>
				
				<text class="post-title">{{ post.title }}</text>
				<text class="post-content">{{ post.content }}</text>
				
				<view class="post-images" v-if="post.images && post.images.length > 0">
					<image 
						:src="img" 
						mode="aspectFill" 
						class="post-image"
						v-for="(img, idx) in post.images.slice(0, 3)"
						:key="idx"
					/>
				</view>
				
				<view class="post-footer">
					<view class="action-item" @click.stop="toggleLike(post)">
						<text class="iconfont" :class="post.isLiked ? 'icon-like-fill' : 'icon-like'"></text>
						<text class="action-text">{{ post.likeCount || 0 }}</text>
					</view>
					<view class="action-item">
						<text class="iconfont icon-comment"></text>
						<text class="action-text">{{ post.commentCount || 0 }}</text>
					</view>
					<view class="action-item">
						<text class="iconfont icon-eye"></text>
						<text class="action-text">{{ post.viewCount || 0 }}</text>
					</view>
					<view class="action-item delete" v-if="isManager" @click.stop="deletePost(post)">
						<text class="iconfont icon-delete"></text>
						<text class="action-text">删除</text>
					</view>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无帖子，快来发布第一条吧</text>
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
	import { postApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				posts: [],
				pageNum: 1,
				pageSize: 10,
				total: 0,
				hasMore: true,
				loading: false,
				isMember: false,
				isManager: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkPermission()
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
			checkPermission() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id == this.clubId)
				this.isMember = true
			},
			async loadData() {
				this.loading = true
				try {
					const params = {
						pageNum: this.pageNum,
						pageSize: this.pageSize
					}
					const res = await postApi.getList(this.clubId, params)
					const list = res.list || []
					
					if (this.pageNum === 1) {
						this.posts = list
					} else {
						this.posts = [...this.posts, ...list]
					}
					this.total = res.total || 0
					this.hasMore = this.posts.length < this.total
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
			goPublish() {
				uni.navigateTo({
					url: `/pages/post/publish?clubId=${this.clubId}`
				})
			},
			goDetail(post) {
				uni.navigateTo({
					url: `/pages/post/detail?clubId=${this.clubId}&postId=${post.id}`
				})
			},
			async toggleLike(post) {
				try {
					await postApi.like(this.clubId, post.id)
					post.isLiked = !post.isLiked
					post.likeCount = (post.likeCount || 0) + (post.isLiked ? 1 : -1)
				} catch (e) {
					console.error(e)
				}
			},
			deletePost(post) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除这条帖子吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await postApi.delete(this.clubId, post.id)
								this.posts = this.posts.filter(p => p.id !== post.id)
								uni.showToast({ title: '已删除', icon: 'success' })
							} catch (e) {
								console.error(e)
								uni.showToast({ title: '删除失败', icon: 'none' })
							}
						}
					}
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

	.header {
		background: #fff;
		padding: 20rpx 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.btn-publish {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		font-size: 26rpx;
		padding: 12rpx 25rpx;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		border: none;
		line-height: 1.4;
	}

	.btn-publish .iconfont {
		margin-right: 8rpx;
	}

	.post-list {
		padding: 0 20rpx;
	}

	.post-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.post-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
		position: relative;
	}

	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		font-weight: bold;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.user-info {
		flex: 1;
	}

	.user-name {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 5rpx;
	}

	.post-time {
		font-size: 24rpx;
		color: #999;
	}

	.top-tag {
		position: absolute;
		right: 0;
		top: 0;
		font-size: 20rpx;
		padding: 4rpx 12rpx;
		background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
		color: #fff;
		border-radius: 8rpx;
	}

	.post-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 15rpx;
		display: block;
		line-height: 1.4;
	}

	.post-content {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
		margin-bottom: 20rpx;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}

	.post-images {
		display: flex;
		gap: 10rpx;
		margin-bottom: 20rpx;
	}

	.post-image {
		width: 200rpx;
		height: 200rpx;
		border-radius: 12rpx;
		flex-shrink: 0;
	}

	.post-footer {
		display: flex;
		align-items: center;
		padding-top: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.action-item {
		display: flex;
		align-items: center;
		margin-right: 40rpx;
		font-size: 24rpx;
		color: #999;
	}

	.action-item .iconfont {
		margin-right: 8rpx;
		font-size: 28rpx;
	}

	.action-item.liked {
		color: #ff4d4f;
	}

	.action-item.delete {
		margin-left: auto;
		margin-right: 0;
		color: #ff4d4f;
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
	}
</style>
