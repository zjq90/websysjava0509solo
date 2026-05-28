<template>
	<view class="container">
		<view class="form-card">
			<view class="form-item">
				<text class="label">帖子标题</text>
				<input 
					type="text" 
					v-model="form.title" 
					placeholder="请输入帖子标题"
					class="input"
					maxlength="100"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">帖子内容</text>
				<textarea 
					v-model="form.content" 
					placeholder="请输入帖子内容..."
					class="textarea"
					maxlength="2000"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">上传图片 (可选)</text>
				<view class="image-list">
					<view 
						class="image-item" 
						v-for="(img, idx) in images" 
						:key="idx"
					>
						<image :src="img" mode="aspectFill" class="preview-img" />
						<view class="remove-btn" @click="removeImage(idx)">
							<text class="iconfont icon-close"></text>
						</view>
					</view>
					<view class="add-btn" v-if="images.length < 9" @click="chooseImage">
						<text class="iconfont icon-plus"></text>
						<text class="add-text">添加图片</text>
					</view>
				</view>
				<text class="tip-text">最多可上传9张图片</text>
			</view>
			
			<view class="form-item" v-if="isManager">
				<view class="checkbox-item" @click="form.isTop = form.isTop === 1 ? 0 : 1">
					<view class="checkbox" :class="{ checked: form.isTop === 1 }">
						<text class="iconfont icon-check" v-if="form.isTop === 1"></text>
					</view>
					<text class="checkbox-label">置顶帖子</text>
				</view>
			</view>
		</view>

		<view class="submit-bar">
			<button 
				class="btn-submit" 
				:disabled="submitting || !canSubmit"
				@click="submit"
			>
				{{ submitting ? '发布中...' : '发布帖子' }}
			</button>
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
				form: {
					title: '',
					content: '',
					isTop: 0
				},
				images: [],
				submitting: false,
				isManager: false
			}
		},
		computed: {
			canSubmit() {
				return this.form.title.trim() && this.form.content.trim()
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.checkPermission()
		},
		methods: {
			checkPermission() {
				const userId = store.state.userInfo?.id
				const managedClubs = store.state.managedClubs || []
				this.isManager = managedClubs.some(c => c.id == this.clubId)
			},
			chooseImage() {
				const remainCount = 9 - this.images.length
				uni.chooseImage({
					count: remainCount,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						this.images = [...this.images, ...res.tempFilePaths]
					}
				})
			},
			removeImage(index) {
				this.images.splice(index, 1)
			},
			async submit() {
				if (!this.canSubmit || this.submitting) return
				
				this.submitting = true
				try {
					const submitData = {
						...this.form,
						images: this.images.join(',')
					}
					await postApi.create(this.clubId, submitData)
					uni.showToast({ title: '发布成功', icon: 'success' })
					setTimeout(() => {
						uni.navigateBack()
					}, 1500)
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '发布失败', icon: 'none' })
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
		min-height: 300rpx;
		padding: 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		line-height: 1.6;
		border: none;
		box-sizing: border-box;
	}

	.image-list {
		display: flex;
		flex-wrap: wrap;
		gap: 15rpx;
	}

	.image-item {
		width: 200rpx;
		height: 200rpx;
		position: relative;
	}

	.preview-img {
		width: 100%;
		height: 100%;
		border-radius: 12rpx;
	}

	.remove-btn {
		position: absolute;
		top: -10rpx;
		right: -10rpx;
		width: 40rpx;
		height: 40rpx;
		background: rgba(0, 0, 0, 0.6);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.remove-btn .iconfont {
		color: #fff;
		font-size: 24rpx;
	}

	.add-btn {
		width: 200rpx;
		height: 200rpx;
		background: #f8f8f8;
		border: 2rpx dashed #ddd;
		border-radius: 12rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.add-btn .iconfont {
		font-size: 60rpx;
		color: #ccc;
		margin-bottom: 10rpx;
	}

	.add-text {
		font-size: 24rpx;
		color: #999;
	}

	.tip-text {
		font-size: 22rpx;
		color: #ccc;
		margin-top: 10rpx;
		display: block;
	}

	.checkbox-item {
		display: flex;
		align-items: center;
	}

	.checkbox {
		width: 36rpx;
		height: 36rpx;
		border: 2rpx solid #ddd;
		border-radius: 6rpx;
		margin-right: 15rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.checkbox.checked {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		border-color: transparent;
	}

	.checkbox .iconfont {
		color: #fff;
		font-size: 24rpx;
	}

	.checkbox-label {
		font-size: 26rpx;
		color: #333;
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
