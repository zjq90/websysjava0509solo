<template>
  <view class="activity-page">
    <view class="publish-btn" @click="goPublish">
      <u-icon name="plus" size="24" color="#fff"></u-icon>
      <text>发布动态</text>
    </view>

    <view class="post-list">
      <view class="post-item" v-for="item in postList" :key="item.id" @click="goDetail(item)">
        <view class="post-header">
          <image :src="item.avatar" class="avatar" mode="aspectFill"></image>
          <view class="post-user">
            <text class="user-name">{{ item.realName }}</text>
            <text class="post-time">{{ formatTime(item.createTime) }} · {{ item.location || '校园' }}</text>
          </view>
          <view class="post-tag" v-if="item.activityName">#{{ item.activityName }}</view>
        </view>
        <view class="post-content">{{ item.content }}</view>
        <view class="post-images" v-if="item.images">
          <image
            v-for="(img, idx) in item.images.split(',').slice(0, 9)"
            :key="idx"
            :src="img"
            class="post-image"
            mode="aspectFill"
            :class="{ 'single': item.images.split(',').length === 1 }"
          ></image>
        </view>
        <view class="post-footer">
          <view class="action-item" @click.stop="toggleLike(item)">
            <u-icon :name="item.isLiked ? 'heart-fill' : 'heart'" size="22" :color="item.isLiked ? '#f5222d' : '#999'"></u-icon>
            <text :class="{ 'liked': item.isLiked }">{{ item.likeCount }}</text>
          </view>
          <view class="action-item" @click.stop="goDetail(item)">
            <u-icon name="chatbox" size="22" color="#999"></u-icon>
            <text>{{ item.commentCount }}</text>
          </view>
          <view class="action-item">
            <u-icon name="share" size="22" color="#999"></u-icon>
            <text>分享</text>
          </view>
        </view>
      </view>
    </view>

    <view class="empty" v-if="postList.length === 0">
      <u-icon name="camera" size="80" color="#ddd"></u-icon>
      <view class="empty-text">还没有动态，快来发布第一条吧</view>
    </view>
  </view>
</template>

<script>
import { getActivityPostList, toggleLike, isLiked } from '@/api/square.js'

export default {
  data() {
    return {
      postList: [],
      pageNum: 1,
      pageSize: 15
    }
  },
  onLoad() {
    this.getPostList()
  },
  onPullDownRefresh() {
    this.pageNum = 1
    this.getPostList()
    uni.stopPullDownRefresh()
  },
  methods: {
    getPostList() {
      getActivityPostList({ pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.postList = res.list || []
        this.postList.forEach(item => {
          this.checkLikeStatus(item)
        })
      }).catch(err => {
        console.error('获取活动圈列表失败', err)
        this.postList = this.getMockData()
      })
    },
    checkLikeStatus(item) {
      const userId = uni.getStorageSync('userId') || 2
      isLiked({ businessType: 0, businessId: item.id, userId }).then(res => {
        item.isLiked = res
      }).catch(() => {
        item.isLiked = false
      })
    },
    toggleLike(item) {
      const userId = uni.getStorageSync('userId') || 2
      toggleLike({ businessType: 0, businessId: item.id, userId, username: 'zhangsan' }).then(() => {
        item.isLiked = !item.isLiked
        item.likeCount += item.isLiked ? 1 : -1
      }).catch(() => {
        item.isLiked = !item.isLiked
        item.likeCount += item.isLiked ? 1 : -1
      })
    },
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      if (diff < 60000) return '刚刚'
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      return time.substring(5, 16)
    },
    goDetail(item) {
      uni.navigateTo({ url: `/pages/square/activity-detail?id=${item.id}` })
    },
    goPublish() {
      uni.navigateTo({ url: '/pages/square/publish-activity' })
    },
    getMockData() {
      return [
        {
          id: 1,
          realName: '张三',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          activityName: '编程大赛培训',
          content: '今天参加了编程大赛的培训，收获满满！老师讲了很多实用的算法技巧，期待下次培训。',
          images: 'https://picsum.photos/800/600?random=1,https://picsum.photos/800/600?random=2',
          likeCount: 42,
          commentCount: 12,
          location: '计算机楼301',
          createTime: '2024-10-15 14:30:00',
          isLiked: false
        },
        {
          id: 2,
          realName: '李四',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          activityName: '科技创新大赛',
          content: '我们的项目进入决赛了！感谢团队的努力，继续加油！💪',
          images: 'https://picsum.photos/800/600?random=3',
          likeCount: 86,
          commentCount: 23,
          location: '创新创业中心',
          createTime: '2024-10-15 12:00:00',
          isLiked: true
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.activity-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20rpx;
  padding-bottom: 140rpx;
}

.publish-btn {
  position: fixed;
  right: 30rpx;
  bottom: 140rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 24rpx 32rpx;
  border-radius: 50rpx;
  display: flex;
  align-items: center;
  gap: 10rpx;
  font-size: 28rpx;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.4);
  z-index: 99;
}

.post-list {
  .post-item {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .post-header {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;

      .avatar {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        margin-right: 20rpx;
        background: #f0f0f0;
      }

      .post-user {
        flex: 1;

        .user-name {
          font-size: 28rpx;
          font-weight: 500;
          color: #333;
          display: block;
          margin-bottom: 4rpx;
        }

        .post-time {
          font-size: 22rpx;
          color: #999;
        }
      }

      .post-tag {
        font-size: 22rpx;
        color: #4A90E2;
        background: #e6f7ff;
        padding: 6rpx 16rpx;
        border-radius: 8rpx;
        max-width: 200rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .post-content {
      font-size: 28rpx;
      color: #333;
      line-height: 1.6;
      margin-bottom: 20rpx;
    }

    .post-images {
      display: flex;
      flex-wrap: wrap;
      gap: 8rpx;
      margin-bottom: 20rpx;

      .post-image {
        width: calc((100% - 16rpx) / 3);
        height: 200rpx;
        border-radius: 12rpx;
        background: #f0f0f0;

        &.single {
          width: 100%;
          height: 400rpx;
        }
      }
    }

    .post-footer {
      display: flex;
      justify-content: space-around;
      padding-top: 20rpx;
      border-top: 1rpx solid #f0f0f0;

      .action-item {
        display: flex;
        align-items: center;
        gap: 10rpx;
        font-size: 26rpx;
        color: #666;

        text.liked {
          color: #f5222d;
        }
      }
    }
  }
}

.empty {
  padding: 100rpx 40rpx;
  text-align: center;

  .empty-text {
    margin-top: 20rpx;
    font-size: 28rpx;
    color: #999;
  }
}
</style>
