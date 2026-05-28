<template>
	<view class="container">
		<view class="form-card">
			<view class="form-item">
				<text class="label">活动封面</text>
				<view class="cover-upload" @click="chooseCover">
					<image 
						:src="form.cover" 
						mode="aspectFill" 
						class="cover-preview" 
						v-if="form.cover"
					/>
					<view class="cover-placeholder" v-else>
						<text class="iconfont icon-camera"></text>
						<text class="upload-tip">点击上传封面</text>
					</view>
				</view>
			</view>
			
			<view class="form-item">
				<text class="label">活动名称</text>
				<input 
					type="text" 
					v-model="form.name" 
					placeholder="请输入活动名称"
					class="input"
					maxlength="50"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">活动类型</text>
				<picker 
					:range="activityTypes" 
					range-key="label" 
					@change="onTypeChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.type }">
							{{ form.type ? getTypeLabel() : '请选择活动类型' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<text class="label">活动开始时间</text>
				<picker 
					mode="multiSelector" 
					:range="dateTimeRange" 
					:value="dateTimeIndex"
					@change="onStartDateTimeChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.startTime }">
							{{ form.startTime || '请选择开始时间' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<text class="label">活动结束时间</text>
				<picker 
					mode="multiSelector" 
					:range="dateTimeRange" 
					:value="endDateTimeIndex"
					@change="onEndDateTimeChange"
					class="picker"
				>
					<view class="picker-input">
						<text :class="{ placeholder: !form.endTime }">
							{{ form.endTime || '请选择结束时间' }}
						</text>
						<text class="iconfont icon-arrow-down"></text>
					</view>
				</picker>
			</view>
			
			<view class="form-item">
				<text class="label">活动地点</text>
				<input 
					type="text" 
					v-model="form.location" 
					placeholder="请输入活动地点"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">参与人数上限</text>
				<input 
					type="number" 
					v-model="form.maxParticipants" 
					placeholder="请输入参与人数上限（0表示不限制）"
					class="input"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">活动描述</text>
				<textarea 
					v-model="form.description" 
					placeholder="请输入活动详细描述"
					class="textarea"
					maxlength="2000"
				/>
			</view>
			
			<view class="form-item">
				<text class="label">活动图片</text>
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
					<view class="add-btn" v-if="images.length < 9" @click="chooseImages">
						<text class="iconfont icon-plus"></text>
						<text class="add-text">添加图片</text>
					</view>
				</view>
				<text class="tip-text">最多可上传9张活动图片</text>
			</view>
			
			<view class="form-item">
				<view class="switch-item">
					<text class="switch-label">允许外部人员报名</text>
					<switch 
						:checked="form.allowExternal === 1" 
						@change="toggleExternal"
						color="#667eea"
					/>
				</view>
			</view>
		</view>

		<view class="submit-bar">
			<button 
				class="btn-submit" 
				:disabled="submitting || !canSubmit"
				@click="submit"
			>
				{{ submitting ? '发布中...' : '发布活动' }}
			</button>
		</view>
	</view>
</template>

<script>
	import { activityApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				form: {
					name: '',
					type: '',
					startTime: '',
					endTime: '',
					location: '',
					maxParticipants: 0,
					description: '',
					cover: '',
					allowExternal: 0
				},
				activityTypes: [
					{ value: 'LECTURE', label: '讲座' },
					{ value: 'CONTEST', label: '比赛' },
					{ value: 'TRAINING', label: '培训' },
					{ value: 'PARTY', label: '聚会' },
					{ value: 'VOLUNTEER', label: '志愿活动' },
					{ value: 'OTHER', label: '其他' }
				],
				images: [],
				dateTimeRange: [],
				dateTimeIndex: [0, 0, 0, 0, 0],
				endDateTimeIndex: [0, 0, 0, 0, 0],
				submitting: false
			}
		},
		computed: {
			canSubmit() {
				return this.form.name.trim() && 
					this.form.type && 
					this.form.startTime &&
					this.form.endTime &&
					this.form.location.trim() &&
					this.form.description.trim()
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.initDateTimeRange()
		},
		methods: {
			initDateTimeRange() {
				const years = []
				const months = []
				const days = []
				const hours = []
				const minutes = []
				
				const now = new Date()
				for (let i = now.getFullYear(); i <= now.getFullYear() + 2; i++) {
					years.push(i + '年')
				}
				for (let i = 1; i <= 12; i++) {
					months.push(i + '月')
				}
				for (let i = 1; i <= 31; i++) {
					days.push(i + '日')
				}
				for (let i = 0; i <= 23; i++) {
					hours.push(i + '时')
				}
				for (let i = 0; i <= 59; i += 30) {
					minutes.push(i + '分')
				}
				
				this.dateTimeRange = [years, months, days, hours, minutes]
			},
			onTypeChange(e) {
				const index = e.detail.value
				this.form.type = this.activityTypes[index].value
			},
			getTypeLabel() {
				const type = this.activityTypes.find(t => t.value === this.form.type)
				return type ? type.label : ''
			},
			onStartDateTimeChange(e) {
				this.dateTimeIndex = e.detail.value
				const [year, month, day, hour, minute] = e.detail.value
				const date = `${this.dateTimeRange[0][year].replace('年', '')}-${String(month + 1).padStart(2, '0')}-${String(day + 1).padStart(2, '0')}`
				const time = `${String(hour).padStart(2, '0')}:${String(minute * 30).padStart(2, '0')}`
				this.form.startTime = `${date} ${time}`
			},
			onEndDateTimeChange(e) {
				this.endDateTimeIndex = e.detail.value
				const [year, month, day, hour, minute] = e.detail.value
				const date = `${this.dateTimeRange[0][year].replace('年', '')}-${String(month + 1).padStart(2, '0')}-${String(day + 1).padStart(2, '0')}`
				const time = `${String(hour).padStart(2, '0')}:${String(minute * 30).padStart(2, '0')}`
				this.form.endTime = `${date} ${time}`
			},
			toggleExternal(e) {
				this.form.allowExternal = e.detail.value ? 1 : 0
			},
			chooseCover() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						this.form.cover = res.tempFilePaths[0]
					}
				})
			},
			chooseImages() {
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
					await activityApi.create(this.clubId, submitData)
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

	.cover-upload {
		width: 100%;
		height: 300rpx;
		border-radius: 16rpx;
		overflow: hidden;
	}

	.cover-preview {
		width: 100%;
		height: 100%;
	}

	.cover-placeholder {
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

	.cover-placeholder .iconfont {
		font-size: 80rpx;
		color: #ccc;
		margin-bottom: 15rpx;
	}

	.upload-tip {
		font-size: 24rpx;
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

	.switch-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.switch-label {
		font-size: 28rpx;
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
