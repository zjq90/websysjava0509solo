<template>
	<view class="container">
		<view class="tabs">
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '' }"
				@click="currentStatus = ''; loadData()"
			>
				全部
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '0' }"
				@click="currentStatus = '0'; loadData()"
			>
				待审核
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '1' }"
				@click="currentStatus = '1'; loadData()"
			>
				已通过
			</view>
			<view 
				class="tab-item" 
				:class="{ active: currentStatus === '2' }"
				@click="currentStatus = '2'; loadData()"
			>
				已驳回
			</view>
		</view>

		<view class="apply-list" v-if="applies.length > 0">
			<view 
				class="apply-card" 
				v-for="apply in applies" 
				:key="apply.id"
			>
				<view class="apply-header">
					<view class="user-info">
						<view class="user-avatar">
							{{ apply.name ? apply.name.charAt(0) : '申' }}
						</view>
						<view class="user-detail">
							<text class="user-name">{{ apply.name }}</text>
							<text class="user-no">{{ apply.studentNo }}</text>
						</view>
					</view>
					<text class="apply-status" :class="'status-' + apply.status">
						{{ getStatusText(apply.status) }}
					</text>
				</view>

				<view class="apply-content">
					<view class="info-grid">
						<view class="info-item">
							<text class="info-label">院系</text>
							<text class="info-value">{{ apply.department }}</text>
						</view>
						<view class="info-item">
							<text class="info-label">专业</text>
							<text class="info-value">{{ apply.major }}</text>
						</view>
						<view class="info-item">
							<text class="info-label">意向部门</text>
							<text class="info-value">{{ apply.departmentName || '未选择' }}</text>
						</view>
						<view class="info-item">
							<text class="info-label">申请时间</text>
							<text class="info-value">{{ apply.applyTime }}</text>
						</view>
					</view>

					<view class="info-row">
						<text class="info-label">个人简介:</text>
						<text class="info-value">{{ apply.introduction }}</text>
					</view>

					<view class="info-row" v-if="apply.hobbies">
						<text class="info-label">兴趣爱好:</text>
						<text class="info-value">{{ apply.hobbies }}</text>
					</view>

					<view class="info-row" v-if="apply.reviewMessage">
						<text class="info-label">审核留言:</text>
						<text class="info-value message">{{ apply.reviewMessage }}</text>
					</view>
				</view>

				<view class="apply-actions" v-if="apply.status === 0">
					<button class="btn btn-reject" @click="showRejectModal(apply)">驳回</button>
					<button class="btn btn-approve" @click="showApproveModal(apply)">通过</button>
				</view>
			</view>
		</view>

		<view class="empty" v-else-if="!loading">
			<text class="empty-text">暂无申请记录</text>
		</view>

		<view class="loading" v-if="loading">
			<text>加载中...</text>
		</view>

		<view class="modal" v-if="showModal" @click="closeModal">
			<view class="modal-content" @click.stop>
				<text class="modal-title">{{ modalTitle }}</text>
				<textarea 
					v-model="reviewMessage" 
					placeholder="请输入审核留言（选填）"
					class="modal-textarea"
					:maxlength="200"
				/>
				<view class="modal-actions">
					<button class="modal-btn btn-cancel" @click="closeModal">取消</button>
					<button class="modal-btn btn-confirm" :class="modalType" @click="confirmReview">
						确认{{ modalType === 'approve' ? '通过' : '驳回' }}
					</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { recruitApi } from '@/api/index.js'
	import store from '@/store/index.js'

	export default {
		data() {
			return {
				clubId: null,
				applies: [],
				currentStatus: '',
				loading: false,
				showModal: false,
				modalType: 'approve',
				modalTitle: '审核通过',
				reviewMessage: '',
				currentApply: null
			}
		},
		onLoad(options) {
			this.clubId = options.clubId || store.state.currentClubId
			this.loadData()
		},
		onPullDownRefresh() {
			this.loadData()
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		},
		methods: {
			getStatusText(status) {
				const map = { 0: '待审核', 1: '已通过', 2: '已驳回' }
				return map[status] || '未知'
			},
			async loadData() {
				this.loading = true
				try {
					const params = {
						pageSize: 100,
						status: this.currentStatus !== '' ? parseInt(this.currentStatus) : undefined
					}
					const res = await recruitApi.getApplyList(this.clubId, params)
					this.applies = res.list || res || []
				} catch (e) {
					console.error(e)
				} finally {
					this.loading = false
				}
			},
			showApproveModal(apply) {
				this.currentApply = apply
				this.modalType = 'approve'
				this.modalTitle = '审核通过'
				this.reviewMessage = '恭喜您的申请已通过，请及时关注社团通知！'
				this.showModal = true
			},
			showRejectModal(apply) {
				this.currentApply = apply
				this.modalType = 'reject'
				this.modalTitle = '审核驳回'
				this.reviewMessage = ''
				this.showModal = true
			},
			closeModal() {
				this.showModal = false
				this.currentApply = null
				this.reviewMessage = ''
			},
			async confirmReview() {
				if (!this.currentApply) return
				
				try {
					const status = this.modalType === 'approve' ? 1 : 2
					await recruitApi.reviewApply(this.clubId, this.currentApply.id, {
						status,
						reviewMessage: this.reviewMessage
					})
					uni.showToast({ 
						title: this.modalType === 'approve' ? '已通过' : '已驳回', 
						icon: 'success' 
					})
					this.closeModal()
					this.loadData()
				} catch (e) {
					console.error(e)
					uni.showToast({ title: '操作失败', icon: 'none' })
				}
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

	.tabs {
		display: flex;
		background: #fff;
		padding: 0 20rpx;
		margin-bottom: 20rpx;
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

	.apply-list {
		padding: 0 20rpx;
	}

	.apply-card {
		background: #fff;
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.apply-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx;
		background: linear-gradient(135deg, #f8f9ff 0%, #f0f5ff 100%);
	}

	.user-info {
		display: flex;
		align-items: center;
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
	}

	.user-detail {
		display: flex;
		flex-direction: column;
	}

	.user-name {
		font-size: 30rpx;
		font-weight: 500;
		color: #333;
		margin-bottom: 5rpx;
	}

	.user-no {
		font-size: 24rpx;
		color: #999;
	}

	.apply-status {
		font-size: 24rpx;
		padding: 8rpx 20rpx;
		border-radius: 24rpx;
	}

	.status-0 {
		background: #fff7e6;
		color: #fa8c16;
	}

	.status-1 {
		background: #f6ffed;
		color: #52c41a;
	}

	.status-2 {
		background: #fff1f0;
		color: #ff4d4f;
	}

	.apply-content {
		padding: 25rpx;
	}

	.info-grid {
		display: flex;
		flex-wrap: wrap;
		margin-bottom: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.info-grid .info-item {
		width: 50%;
		margin-bottom: 15rpx;
	}

	.info-grid .info-label {
		font-size: 24rpx;
		color: #999;
		display: block;
		margin-bottom: 5rpx;
	}

	.info-grid .info-value {
		font-size: 26rpx;
		color: #333;
	}

	.info-row {
		display: flex;
		margin-bottom: 15rpx;
	}

	.info-row:last-child {
		margin-bottom: 0;
	}

	.info-label {
		font-size: 26rpx;
		color: #999;
		width: 140rpx;
		flex-shrink: 0;
	}

	.info-value {
		flex: 1;
		font-size: 26rpx;
		color: #333;
		line-height: 1.6;
	}

	.info-value.message {
		background: #f8f9ff;
		padding: 15rpx;
		border-radius: 8rpx;
	}

	.apply-actions {
		display: flex;
		gap: 20rpx;
		padding: 20rpx 25rpx;
		background: #fafafa;
		border-top: 1rpx solid #f0f0f0;
	}

	.btn {
		flex: 1;
		height: 70rpx;
		line-height: 70rpx;
		border-radius: 35rpx;
		font-size: 28rpx;
		border: none;
	}

	.btn-reject {
		background: #fff1f0;
		color: #ff4d4f;
	}

	.btn-approve {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.empty,
	.loading {
		text-align: center;
		padding: 100rpx 0;
		font-size: 28rpx;
		color: #999;
	}

	.modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
	}

	.modal-content {
		width: 600rpx;
		background: #fff;
		border-radius: 20rpx;
		padding: 40rpx;
	}

	.modal-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		text-align: center;
		margin-bottom: 30rpx;
		display: block;
	}

	.modal-textarea {
		width: 100%;
		height: 200rpx;
		padding: 20rpx;
		background: #f8f8f8;
		border-radius: 12rpx;
		font-size: 28rpx;
		color: #333;
		box-sizing: border-box;
		margin-bottom: 30rpx;
	}

	.modal-actions {
		display: flex;
		gap: 20rpx;
	}

	.modal-btn {
		flex: 1;
		height: 70rpx;
		line-height: 70rpx;
		border-radius: 35rpx;
		font-size: 28rpx;
		border: none;
	}

	.btn-cancel {
		background: #f5f5f5;
		color: #666;
	}

	.btn-confirm {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
	}

	.btn-confirm.reject {
		background: #ff4d4f;
	}

	@media screen and (min-width: 768px) {
		.container {
			max-width: 720px;
			margin: 0 auto;
		}
	}
</style>
