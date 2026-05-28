<template>
	<view class="container">
		<view class="form-card">
			<view class="form-item">
				<text class="label">文件名称</text>
				<input 
					type="text" 
					v-model="form.name" 
					placeholder="请输入文件名称"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">文件描述</text>
				<textarea 
					v-model="form.description" 
					placeholder="请输入文件描述（可选）"
					class="textarea"
					maxlength="200"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">选择文件</text>
				<view class="upload-area" @click="chooseFile" v-if="!selectedFile">
					<text class="iconfont icon-upload"></text>
					<text class="upload-tip">点击选择文件</text>
					<text class="upload-hint">支持 PDF、Word、Excel、PPT、图片等格式</text>
				</view>
				<view class="file-preview" v-else>
					<view class="file-icon" :class="'type-' + getFileType(selectedFile.name)">
						<text class="iconfont" :class="getFileIcon(selectedFile.name)"></text>
					</view>
					<view class="file-info">
						<text class="file-name">{{ selectedFile.name }}</text>
						<text class="file-size">{{ formatFileSize(selectedFile.size) }}</text>
					</view>
					<view class="remove-btn" @click="removeFile">
						<text class="iconfont icon-close"></text>
					</view>
				</view>
			</view>
			
			<view class="form-item">
				<text class="label">文件类型</text>
				<view class="type-tags">
					<text 
						class="tag" 
						:class="{ active: form.fileType === item.value }"
						v-for="item in fileTypes" 
						:key="item.value"
						@click="form.fileType = item.value"
					>
						{{ item.label }}
					</text>
				</view>
			</view>
		</view>

		<view class="submit-bar">
			<button 
				class="btn-submit" 
				:disabled="submitting || !canSubmit"
				@click="submit"
			>
				{{ submitting ? '上传中...' : '上传文件' }}
			</button>
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
				form: {
					name: '',
					description: '',
					fileType: 'other'
				},
				selectedFile: null,
				submitting: false,
				fileTypes: [
					{ label: '章程', value: 'constitution' },
					{ label: '策划模板', value: 'template' },
					{ label: '活动资料', value: 'activity' },
					{ label: '其他', value: 'other' }
				]
			}
		},
		computed: {
			canSubmit() {
				return this.form.name.trim() && this.selectedFile
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
		},
		methods: {
			chooseFile() {
				uni.chooseFile({
					count: 1,
					success: (res) => {
						const file = res.tempFiles[0]
						this.selectedFile = file
						if (!this.form.name.trim()) {
							this.form.name = file.name
						}
					}
				})
			},
			removeFile() {
				this.selectedFile = null
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
			async submit() {
				if (!this.canSubmit || this.submitting) return
				
				this.submitting = true
				try {
					const formData = new FormData()
					formData.append('name', this.form.name)
					formData.append('description', this.form.description)
					formData.append('fileType', this.form.fileType)
					formData.append('file', this.selectedFile)
					
					await fileApi.upload(this.clubId, formData)
					uni.showToast({ title: '上传成功', icon: 'success' })
					setTimeout(() => {
						uni.navigateBack()
					}, 1500)
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '上传失败', icon: 'none' })
				} finally {
					this.submitting = false
				}
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

	.form-card {
		background: #fff;
		padding: 30rpx;
	}

	.form-item {
		margin-bottom: 40rpx;
	}

	.form-item:last-child {
		margin-bottom: 0;
	}

	.label {
		font-size: 28rpx;
		font-weight: 500;
		color: #333;
		display: block;
		margin-bottom: 15rpx;
	}

	.input {
		width: 100%;
		height: 90rpx;
		padding: 0 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		border: none;
		box-sizing: border-box;
	}

	.textarea {
		width: 100%;
		min-height: 150rpx;
		padding: 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		line-height: 1.6;
		border: none;
		box-sizing: border-box;
	}

	.upload-area {
		border: 2rpx dashed #ddd;
		border-radius: 16rpx;
		padding: 60rpx 30rpx;
		text-align: center;
		background: #fafafa;
	}

	.upload-area .iconfont {
		font-size: 80rpx;
		color: #ccc;
		display: block;
		margin-bottom: 20rpx;
	}

	.upload-tip {
		font-size: 28rpx;
		color: #666;
		display: block;
		margin-bottom: 10rpx;
	}

	.upload-hint {
		font-size: 24rpx;
		color: #999;
	}

	.file-preview {
		display: flex;
		align-items: center;
		padding: 25rpx;
		background: #f8f8f8;
		border-radius: 16rpx;
	}

	.file-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 12rpx;
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
		font-size: 36rpx;
	}

	.file-info {
		flex: 1;
		min-width: 0;
	}

	.file-name {
		font-size: 28rpx;
		color: #333;
		display: block;
		margin-bottom: 5rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.file-size {
		font-size: 24rpx;
		color: #999;
	}

	.remove-btn {
		width: 50rpx;
		height: 50rpx;
		background: rgba(0, 0, 0, 0.5);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-left: 20rpx;
	}

	.remove-btn .iconfont {
		color: #fff;
		font-size: 24rpx;
	}

	.type-tags {
		display: flex;
		flex-wrap: wrap;
		gap: 15rpx;
	}

	.tag {
		padding: 12rpx 25rpx;
		background: #f5f5f5;
		border-radius: 30rpx;
		font-size: 26rpx;
		color: #666;
	}

	.tag.active {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.submit-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #fff;
		padding: 20rpx 30rpx;
		box-shadow: 0 -2rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.btn-submit {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border-radius: 45rpx;
		font-size: 30rpx;
		font-weight: 500;
		border: none;
	}

	.btn-submit[disabled] {
		opacity: 0.5;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
		.submit-bar {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
