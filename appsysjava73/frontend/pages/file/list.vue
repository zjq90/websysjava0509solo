<template>
	<view class="container">
		<view class="header">
			<text class="title">社团网盘</text>
			<button class="btn-upload" @click="goUpload" v-if="isManager">
				<text class="iconfont icon-upload"></text>
				上传
			</button>
		</view>

		<view class="file-list" v-if="files.length > 0">
			<view 
				class="file-card" 
				v-for="file in files" 
				:key="file.id"
				@click="handleFile(file)"
			>
				<view class="file-icon" :class="'type-' + getFileType(file.name)">
					<text class="iconfont" :class="getFileIcon(file.name)"></text>
				</view>
				<view class="file-info">
					<text class="file-name">{{ file.name }}</text>
					<view class="file-meta">
						<text class="meta-text">{{ formatFileSize(file.size) }}</text>
						<text class="meta-dot">·</text>
						<text class="meta-text">{{ file.uploadTime }}</text>
						<text class="meta-dot">·</text>
						<text class="meta-text">{{ file.uploaderName }}</text>
					</view>
				</view>
				<view class="file-actions" v-if="isManager" @click.stop>
					<text class="iconfont icon-delete delete" @click="deleteFile(file)"></text>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-icon">
				<text class="iconfont icon-folder"></text>
			</text>
			<text class="empty-text">暂无文件</text>
			<text class="empty-tip" v-if="isManager">点击右上角上传文件</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>
	</view>
</template>

<script>
	import { fileApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				files: [],
				loading: false,
				isManager: false
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkPermission()
			this.loadData()
		},
		onPullDownRefresh() {
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
			},
			async loadData() {
				this.loading = true
				try {
					const res = await fileApi.getList(this.clubId, { pageSize: 100 })
					this.files = res.list || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			getFileType(name) {
				const ext = name.split('.').pop().toLowerCase()
				if (['pdf'].includes(ext)) return 'pdf'
				if (['doc', 'docx'].includes(ext)) return 'doc'
				if (['xls', 'xlsx'].includes(ext)) return 'xls'
				if (['ppt', 'pptx'].includes(ext)) return 'ppt'
				if (['jpg', 'jpeg', 'png', 'gif', 'webp'].includes(ext)) return 'img'
				if (['zip', 'rar', '7z'].includes(ext)) return 'zip'
				return 'other'
			},
			getFileIcon(name) {
				const type = this.getFileType(name)
				const icons = {
					pdf: 'icon-pdf',
					doc: 'icon-word',
					xls: 'icon-excel',
					ppt: 'icon-ppt',
					img: 'icon-image',
					zip: 'icon-zip',
					other: 'icon-file'
				}
				return icons[type] || 'icon-file'
			},
			formatFileSize(bytes) {
				if (!bytes) return '0B'
				const k = 1024
				const sizes = ['B', 'KB', 'MB', 'GB']
				const i = Math.floor(Math.log(bytes) / Math.log(k))
				return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + sizes[i]
			},
			goUpload() {
				uni.navigateTo({
					url: `/pages/file/upload?clubId=${this.clubId}`
				})
			},
			handleFile(file) {
				uni.showActionSheet({
					itemList: ['下载文件', '预览文件'],
					success: async (res) => {
						if (res.tapIndex === 0) {
							this.downloadFile(file)
						} else if (res.tapIndex === 1) {
							this.previewFile(file)
						}
					}
				})
			},
			async downloadFile(file) {
				try {
					await fileApi.download(this.clubId, file.id)
					uni.showToast({ title: '下载成功', icon: 'success' })
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '下载失败', icon: 'none' })
				}
			},
			async previewFile(file) {
				uni.showToast({ title: '预览功能开发中', icon: 'none' })
			},
			deleteFile(file) {
				uni.showModal({
					title: '确认删除',
					content: `确定要删除文件"${file.name}"吗？`,
					success: async (res) => {
						if (res.confirm) {
							try {
								await fileApi.delete(this.clubId, file.id)
								this.files = this.files.filter(f => f.id !== file.id)
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

	.btn-upload {
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

	.btn-upload .iconfont {
		margin-right: 8rpx;
	}

	.file-list {
		padding: 0 20rpx;
	}

	.file-card {
		background: #fff;
		border-radius: 16rpx;
		padding: 25rpx;
		margin-bottom: 15rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.file-icon {
		width: 90rpx;
		height: 90rpx;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		flex-shrink: 0;
	}

	.file-icon.type-pdf {
		background: #fff1f0;
	}
	.file-icon.type-pdf .iconfont {
		color: #ff4d4f;
	}

	.file-icon.type-doc {
		background: #e6f7ff;
	}
	.file-icon.type-doc .iconfont {
		color: #1890ff;
	}

	.file-icon.type-xls {
		background: #f6ffed;
	}
	.file-icon.type-xls .iconfont {
		color: #52c41a;
	}

	.file-icon.type-ppt {
		background: #fff7e6;
	}
	.file-icon.type-ppt .iconfont {
		color: #fa8c16;
	}

	.file-icon.type-img {
		background: #f9f0ff;
	}
	.file-icon.type-img .iconfont {
		color: #722ed1;
	}

	.file-icon.type-zip {
		background: #fffbe6;
	}
	.file-icon.type-zip .iconfont {
		color: #faad14;
	}

	.file-icon.type-other {
		background: #f5f5f5;
	}
	.file-icon.type-other .iconfont {
		color: #666;
	}

	.file-icon .iconfont {
		font-size: 40rpx;
	}

	.file-info {
		flex: 1;
		min-width: 0;
	}

	.file-name {
		font-size: 28rpx;
		color: #333;
		font-weight: 500;
		display: block;
		margin-bottom: 10rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.file-meta {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 8rpx;
	}

	.meta-text {
		font-size: 22rpx;
		color: #999;
	}

	.meta-dot {
		font-size: 22rpx;
		color: #ddd;
	}

	.file-actions {
		margin-left: 20rpx;
	}

	.file-actions .delete {
		font-size: 32rpx;
		color: #ff4d4f;
		padding: 10rpx;
	}

	.empty {
		text-align: center;
		padding: 120rpx 0;
	}

	.empty-icon {
		font-size: 120rpx;
		color: #ddd;
		display: block;
		margin-bottom: 20rpx;
	}

	.empty-text {
		font-size: 28rpx;
		color: #999;
		display: block;
		margin-bottom: 10rpx;
	}

	.empty-tip {
		font-size: 24rpx;
		color: #ccc;
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
	}
</style>
