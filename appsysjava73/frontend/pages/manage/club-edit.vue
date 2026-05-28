<template>
	<view class="container">
		<view class="form-card">
			<view class="form-item">
				<text class="label">社团Logo</text>
				<view class="logo-upload" @click="chooseLogo">
					<image 
						:src="form.logo || 'https://picsum.photos/200/200'" 
						mode="aspectFill" 
						class="logo-preview" 
						v-if="form.logo"
					/>
					<view class="logo-placeholder" v-else>
						<text class="iconfont icon-camera"></text>
						<text class="upload-tip">点击上传Logo</text>
					</view>
				</view>
			</view>
			
			<view class="form-item">
				<text class="label">社团名称</text>
				<input 
					type="text" 
					v-model="form.name" 
					placeholder="请输入社团名称"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">社团分类</text>
				<picker 
					:range="categories" 
					range-key="label" 
					@change="onCategoryChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.category }">
							{{ form.category ? getCategoryLabel() : '请选择分类' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<text class="label">社团简介</text>
				<textarea 
					v-model="form.description" 
					placeholder="请输入社团简介"
					class="textarea"
					maxlength="500"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">联系方式</text>
				<input 
					type="text" 
					v-model="form.contactInfo" 
					placeholder="请输入联系方式（微信/QQ/电话等）"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">社团口号</text>
				<input 
					type="text" 
					v-model="form.slogan" 
					placeholder="请输入社团口号（可选）"
					class="input"
				/>
			</view>
		</view>

		<view class="submit-bar">
			<button 
				class="btn-submit" 
				:disabled="submitting || !canSubmit"
				@click="submit"
			>
				{{ submitting ? '保存中...' : '保存修改' }}
			</button>
		</view>
	</view>
</template>

<script>
	import { clubApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				form: {
					name: '',
					category: '',
					description: '',
					contactInfo: '',
					slogan: '',
					logo: ''
				},
				categories: [
					{ value: 'ACADEMIC_TECHNOLOGY', label: '学术科技' },
					{ value: 'CULTURE_ART', label: '文化艺术' },
					{ value: 'SPORTS', label: '体育竞技' },
					{ value: 'PUBLIC_WELFARE', label: '公益实践' },
					{ value: 'INNOVATION', label: '创新创业' }
				],
				submitting: false
			}
		},
		computed: {
			canSubmit() {
				return this.form.name.trim() && 
					this.form.category && 
					this.form.description.trim() &&
					this.form.contactInfo.trim()
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.loadClubInfo()
		},
		methods: {
			async loadClubInfo() {
				try {
					const club = await clubApi.getDetail(this.clubId)
					this.form = {
						name: club.name || '',
						category: club.category || '',
						description: club.description || '',
						contactInfo: club.contactInfo || '',
						slogan: club.slogan || '',
						logo: club.logo || ''
					}
				} catch (e) {
					console.error(e)
				}
			},
			onCategoryChange(e) {
				const index = e.detail.value
				this.form.category = this.categories[index].value
			},
			getCategoryLabel() {
				const cat = this.categories.find(c => c.value === this.form.category)
				return cat ? cat.label : ''
			},
			chooseLogo() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						this.form.logo = res.tempFilePaths[0]
					}
				})
			},
			async submit() {
				if (!this.canSubmit || this.submitting) return
				
				this.submitting = true
				try {
					await clubApi.update(this.clubId, this.form)
					uni.showToast({ title: '保存成功', icon: 'success' })
					setTimeout(() => {
						uni.navigateBack()
					}, 1500)
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '保存失败', icon: 'none' })
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

	.logo-upload {
		width: 200rpx;
		height: 200rpx;
		border-radius: 16rpx;
		overflow: hidden;
	}

	.logo-preview {
		width: 100%;
		height: 100%;
	}

	.logo-placeholder {
		width: 100%;
		height: 100%;
		background: #f8f8f8;
		border: 2rpx dashed #ddd;
		border-radius: 16rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	.logo-placeholder .iconfont {
		font-size: 60rpx;
		color: #ccc;
		margin-bottom: 10rpx;
	}

	.upload-tip {
		font-size: 22rpx;
		color: #999;
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

	.picker {
		width: 100%;
	}

	.picker-input {
		height: 90rpx;
		padding: 0 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.picker-input .placeholder {
		color: #999;
	}

	.picker-input .iconfont {
		color: #999;
		font-size: 24rpx;
	}

	.textarea {
		width: 100%;
		min-height: 200rpx;
		padding: 25rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		line-height: 1.6;
		border: none;
		box-sizing: border-box;
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
