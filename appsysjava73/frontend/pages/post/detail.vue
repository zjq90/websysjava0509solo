<template>
	<view class="page-wrapper">
		<view class="container" v-if="post">
			<view class="post-detail">
				<view class="post-header">
					<view class="user-avatar">
						{{ post.authorName ? post.authorName.charAt(0) : '用' }}
					</view>
					<view class="user-info">
						<text class="user-name">{{ post.authorName }}</text>
						<text class="post-time">{{ post.createTime }}</text>
					</view>
				</view>
				
				<text class="post-title">{{ post.title }}</text>
				<text class="post-content">{{ post.content }}</text>
				
				<view class="post-images" v-if="post.images && post.images.length > 0">
					<image 
						:src="img" 
						mode="aspectFill" 
						class="post-image"
						v-for="(img, idx) in post.images"
						:key="idx"
						@click="previewImage(idx)"
					/>
				</view>
				
				<view class="post-stats">
					<text class="stat-text">{{ post.viewCount || 0 }}次浏览</text>
				</view>
			</view>

			<view class="comment-section">
				<view class="section-header">
					<text class="section-title">评论 ({{ post.commentCount || 0 }})</text>
				</view>
				
				<view class="comment-list" v-if="comments.length > 0">
					<view class="comment-item" v-for="comment in comments" :key="comment.id">
						<view class="comment-avatar">
							{{ comment.authorName ? comment.authorName.charAt(0) : '用' }}
						</view>
						<view class="comment-content">
							<view class="comment-header">
								<text class="comment-author">{{ comment.authorName }}</text>
								<text class="comment-time">{{ comment.createTime }}</text>
							</view>
							<text class="comment-text">{{ comment.content }}</text>
							<view class="comment-actions" v-if="isManager">
								<text class="delete-btn" @click="deleteComment(comment)">删除</text>
							</view>
						</view>
					</view>
				</view>
				
				<view class="empty-comments" v-else>
					<text class="empty-text">暂无评论，快来抢沙发吧~</text>
				</view>
			</view>

			<view class="input-bar">
				<input 
					type="text" 
					v-model="commentText" 
					placeholder="发表评论..."
					class="comment-input"
				/>
				<button class="btn-send" :disabled="!commentText.trim()" @click="sendComment">
					发送
				</button>
			</view>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
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
				postId: null,
				post: null,
				comments: [],
				commentText: '',
				loading: false,
				isManager: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId
			this.postId = options.postId
			this.checkPermission()
			this.loadDetail()
		},
		methods: {
			checkPermission() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id == this.clubId)
			},
			async loadDetail() {
				this.loading = true
				try {
					this.post = await postApi.getDetail(this.clubId, this.postId)
					const res = await postApi.getComments(this.clubId, this.postId, { pageSize: 100 })
					this.comments = res.list || []
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '加载失败', icon: 'none' })
				} finally {
					this.loading = false
				}
			},
			previewImage(index) {
				uni.previewImage({
					urls: this.post.images,
					current: index
				})
			},
			async sendComment() {
				if (!this.commentText.trim()) return
				
				try {
					await postApi.createComment(this.clubId, this.postId, {
						content: this.commentText.trim()
					})
					this.commentText = ''
					uni.showToast({ title: '评论成功', icon: 'success' })
					this.loadDetail()
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '评论失败', icon: 'none' })
				}
			},
			deleteComment(comment) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除这条评论吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await postApi.deleteComment(this.clubId, comment.id)
								this.comments = this.comments.filter(c => c.id !== comment.id)
								this.post.commentCount--
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
	.page-wrapper {
		min-height: 100vh;
		background: #f5f5f5;
	}

	.container {
		min-height: 100vh;
		background: #f5f5f5;
		padding-bottom: 120rpx;
	}

	.post-detail {
		background: #fff;
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	.post-header {
		display: flex;
		align-items: center;
		margin-bottom: 25rpx;
	}

	.user-avatar {
		width: 90rpx;
		height: 90rpx;
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

	.user-info {
		flex: 1;
	}

	.user-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 8rpx;
	}

	.post-time {
		font-size: 24rpx;
		color: #999;
	}

	.post-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
		line-height: 1.4;
		margin-bottom: 20rpx;
		display: block;
	}

	.post-content {
		font-size: 28rpx;
		color: #666;
		line-height: 1.8;
		margin-bottom: 25rpx;
	}

	.post-images {
		display: flex;
		flex-wrap: wrap;
		gap: 15rpx;
		margin-bottom: 25rpx;
	}

	.post-image {
		width: 210rpx;
		height: 210rpx;
		border-radius: 12rpx;
	}

	.post-stats {
		padding-top: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.stat-text {
		font-size: 24rpx;
		color: #999;
	}

	.comment-section {
		background: #fff;
		padding: 30rpx;
	}

	.section-header {
		margin-bottom: 25rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
	}

	.comment-list {
		margin-bottom: 20rpx;
	}

	.comment-item {
		display: flex;
		margin-bottom: 30rpx;
	}

	.comment-item:last-child {
		margin-bottom: 0;
	}

	.comment-avatar {
		width: 70rpx;
		height: 70rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		font-weight: bold;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.comment-content {
		flex: 1;
		min-width: 0;
	}

	.comment-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.comment-author {
		font-size: 26rpx;
		font-weight: 500;
		color: #333;
	}

	.comment-time {
		font-size: 22rpx;
		color: #999;
	}

	.comment-text {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
		word-break: break-all;
	}

	.comment-actions {
		margin-top: 10rpx;
	}

	.delete-btn {
		font-size: 24rpx;
		color: #ff4d4f;
	}

	.empty-comments {
		text-align: center;
		padding: 60rpx 0;
	}

	.empty-text {
		font-size: 26rpx;
		color: #999;
	}

	.input-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx;
		display: flex;
		align-items: center;
		gap: 15rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.comment-input {
		flex: 1;
		height: 70rpx;
		padding: 0 25rpx;
		background: #f5f5f5;
		border-radius: 35rpx;
		font-size: 28rpx;
	}

	.btn-send {
		width: 120rpx;
		height: 70rpx;
		line-height: 70rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 35rpx;
		font-size: 28rpx;
		border: none;
	}

	.btn-send[disabled] {
		opacity: 0.5;
	}

	.loading {
		text-align: center;
		padding: 100rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.input-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
