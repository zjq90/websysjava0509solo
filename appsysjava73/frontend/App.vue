<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
			const token = uni.getStorageSync('token')
			if (token) {
				this.$store.commit('SET_TOKEN', token)
				const userInfo = uni.getStorageSync('userInfo')
				if (userInfo) {
					this.$store.commit('SET_USER_INFO', JSON.parse(userInfo))
				}
			}
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	@import './common/common.scss';

	page {
		background-color: #f5f5f5;
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
	}

	.container {
		padding: 20rpx;
	}

	.card {
		background: #fff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.08);
	}

	.btn-primary {
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		color: #fff;
		border: none;
		border-radius: 50rpx;
		padding: 24rpx 48rpx;
		font-size: 32rpx;
		font-weight: 500;
	}

	.btn-secondary {
		background: #f0f0f0;
		color: #333;
		border: none;
		border-radius: 50rpx;
		padding: 24rpx 48rpx;
		font-size: 32rpx;
	}

	.text-primary {
		color: #667eea;
	}

	.text-secondary {
		color: #999;
	}

	.text-success {
		color: #52c41a;
	}

	.text-warning {
		color: #faad14;
	}

	.text-danger {
		color: #ff4d4f;
	}

	.flex {
		display: flex;
	}

	.flex-center {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.flex-between {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.flex-1 {
		flex: 1;
	}

	.mt-10 {
		margin-top: 20rpx;
	}

	.mt-20 {
		margin-top: 40rpx;
	}

	.mb-10 {
		margin-bottom: 20rpx;
	}

	.mb-20 {
		margin-bottom: 40rpx;
	}

	.p-10 {
		padding: 20rpx;
	}

	.p-20 {
		padding: 40rpx;
	}

	.empty {
		text-align: center;
		padding: 100rpx 0;
		color: #999;
	}

	.empty image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 20rpx;
	}

	.tag {
		display: inline-block;
		padding: 8rpx 20rpx;
		border-radius: 30rpx;
		font-size: 24rpx;
		margin-right: 16rpx;
	}

	.tag-primary {
		background: #e6f7ff;
		color: #1890ff;
	}

	.tag-success {
		background: #f6ffed;
		color: #52c41a;
	}

	.tag-warning {
		background: #fffbe6;
		color: #faad14;
	}

	.tag-danger {
		background: #fff1f0;
		color: #ff4d4f;
	}

	.divider {
		height: 1rpx;
		background: #eee;
		margin: 30rpx 0;
	}

	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: #f0f0f0;
	}

	.avatar-lg {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
	}

	input, textarea {
		background: #f5f5f5;
		border-radius: 12rpx;
		padding: 24rpx;
		font-size: 28rpx;
	}
</style>
