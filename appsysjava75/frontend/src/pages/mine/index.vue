<template>
  <view class="mine-page">
    <view class="header">
      <view class="user-info">
        <image :src="userInfo.avatar" class="avatar" mode="aspectFill"></image>
        <view class="user-detail">
          <text class="user-name">{{ userInfo.realName }}</text>
          <text class="user-info-text">{{ userInfo.major }} · {{ userInfo.grade }}级</text>
        </view>
        <u-icon name="arrow-right" size="24" color="#fff"></u-icon>
      </view>
      <view class="stats-row">
        <view class="stat-item">
          <text class="stat-value">{{ stats.posts }}</text>
          <text class="stat-label">发布</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.followers }}</text>
          <text class="stat-label">粉丝</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.following }}</text>
          <text class="stat-label">关注</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">{{ stats.likes }}</text>
          <text class="stat-label">获赞</text>
        </view>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-group">
        <view class="menu-item" @click="goToMyPosts">
          <view class="menu-icon" style="background: #e6f7ff;">
            <u-icon name="camera" size="22" color="#4A90E2"></u-icon>
          </view>
          <text class="menu-title">我的动态</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
        <view class="menu-item" @click="goToMyResources">
          <view class="menu-icon" style="background: #f6ffed;">
            <u-icon name="folder" size="22" color="#52c41a"></u-icon>
          </view>
          <text class="menu-title">我的资源</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
        <view class="menu-item" @click="goToMyApplies">
          <view class="menu-icon" style="background: #fff7e6;">
            <u-icon name="file-text" size="22" color="#fa8c16"></u-icon>
          </view>
          <text class="menu-title">我的申请</text>
          <view class="menu-badge">3</view>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
        <view class="menu-item" @click="goToMyReimburses">
          <view class="menu-icon" style="background: #fff1f0;">
            <u-icon name="wallet" size="22" color="#f5222d"></u-icon>
          </view>
          <text class="menu-title">我的报销</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToClubManage">
          <view class="menu-icon" style="background: #f9f0ff;">
            <u-icon name="users" size="22" color="#722ed1"></u-icon>
          </view>
          <text class="menu-title">社团管理</text>
          <text class="menu-desc">计算机协会</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
        <view class="menu-item" @click="goToClubMember">
          <view class="menu-icon" style="background: #e6fffb;">
            <u-icon name="team" size="22" color="#13c2c2"></u-icon>
          </view>
          <text class="menu-title">我的社团</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
      </view>

      <view class="menu-group">
        <view class="menu-item" @click="goToSettings">
          <view class="menu-icon" style="background: #fafafa;">
            <u-icon name="gear" size="22" color="#666"></u-icon>
          </view>
          <text class="menu-title">设置</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
        <view class="menu-item" @click="goToAbout">
          <view class="menu-icon" style="background: #fafafa;">
            <u-icon name="info-circle" size="22" color="#666"></u-icon>
          </view>
          <text class="menu-title">关于我们</text>
          <u-icon name="arrow-right" size="18" color="#ccc"></u-icon>
        </view>
      </view>
    </view>

    <view class="logout-btn" @click="logout">
      <text>退出登录</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {
        avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
        realName: '张三',
        major: '软件工程',
        grade: '2021'
      },
      stats: {
        posts: 12,
        followers: 256,
        following: 128,
        likes: 520
      }
    }
  },
  onShow() {
    const userId = uni.getStorageSync('userId') || 2
    this.userInfo.avatar = `https://api.dicebear.com/7.x/avataaars/svg?seed=${userId}`
  },
  methods: {
    goToMyPosts() {
      uni.navigateTo({ url: '/pages/square/activity?userId=2' })
    },
    goToMyResources() {
      uni.navigateTo({ url: '/pages/resource/share?my=1' })
    },
    goToMyApplies() {
      uni.showToast({ title: '我的申请', icon: 'none' })
    },
    goToMyReimburses() {
      uni.navigateTo({ url: '/pages/fund/reimbursement' })
    },
    goToClubManage() {
      uni.showToast({ title: '社团管理', icon: 'none' })
    },
    goToClubMember() {
      uni.showToast({ title: '我的社团', icon: 'none' })
    },
    goToSettings() {
      uni.showToast({ title: '设置', icon: 'none' })
    },
    goToAbout() {
      uni.showToast({ title: '关于我们', icon: 'none' })
    },
    logout() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('userId')
            uni.showToast({ title: '已退出登录', icon: 'success' })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.mine-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx 40rpx;
  color: #fff;

  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 40rpx;

    .avatar {
      width: 140rpx;
      height: 140rpx;
      border-radius: 50%;
      margin-right: 24rpx;
      border: 6rpx solid rgba(255, 255, 255, 0.3);
      background: #fff;
    }

    .user-detail {
      flex: 1;

      .user-name {
        font-size: 36rpx;
        font-weight: bold;
        display: block;
        margin-bottom: 8rpx;
      }

      .user-info-text {
        font-size: 26rpx;
        opacity: 0.9;
      }
    }
  }

  .stats-row {
    display: flex;
    background: rgba(255, 255, 255, 0.15);
    border-radius: 20rpx;
    padding: 30rpx 0;

    .stat-item {
      flex: 1;
      text-align: center;

      .stat-value {
        font-size: 36rpx;
        font-weight: bold;
        display: block;
        margin-bottom: 8rpx;
      }

      .stat-label {
        font-size: 24rpx;
        opacity: 0.9;
      }
    }
  }
}

.menu-section {
  padding: 20rpx;

  .menu-group {
    background: #fff;
    border-radius: 20rpx;
    margin-bottom: 20rpx;
    overflow: hidden;

    .menu-item {
      display: flex;
      align-items: center;
      padding: 30rpx;
      border-bottom: 1rpx solid #f5f5f5;
      position: relative;

      &:last-child {
        border-bottom: none;
      }

      .menu-icon {
        width: 72rpx;
        height: 72rpx;
        border-radius: 16rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;
      }

      .menu-title {
        flex: 1;
        font-size: 28rpx;
        color: #333;
      }

      .menu-desc {
        font-size: 24rpx;
        color: #999;
        margin-right: 16rpx;
      }

      .menu-badge {
        background: #f5222d;
        color: #fff;
        font-size: 20rpx;
        min-width: 36rpx;
        height: 36rpx;
        border-radius: 18rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16rpx;
        padding: 0 10rpx;
      }
    }
  }
}

.logout-btn {
  margin: 40rpx 30rpx;
  background: #fff;
  color: #f5222d;
  text-align: center;
  padding: 30rpx;
  border-radius: 20rpx;
  font-size: 30rpx;
  font-weight: 500;
}
</style>
