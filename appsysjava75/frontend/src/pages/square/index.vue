<template>
  <view class="square-page">
    <view class="header">
      <view class="header-title">广场</view>
      <view class="header-subtitle">发现精彩，分享生活</view>
    </view>

    <view class="quick-nav">
      <view class="nav-item" @click="goToActivity">
        <view class="nav-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <u-icon name="camera" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">活动圈</text>
      </view>
      <view class="nav-item" @click="goToTopic">
        <view class="nav-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <u-icon name="chat-dots" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">话题</text>
      </view>
      <view class="nav-item" @click="goToSenior">
        <view class="nav-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <u-icon name="book" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">学长分享</text>
      </view>
      <view class="nav-item" @click="publish">
        <view class="nav-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
          <u-icon name="plus" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">发布</text>
      </view>
    </view>

    <view class="hot-topics" v-if="hotTopics.length > 0">
      <view class="section-header">
        <text class="section-title">🔥 热门话题</text>
        <text class="section-more" @click="goToTopic">更多</text>
      </view>
      <scroll-view class="topic-scroll" scroll-x>
        <view class="topic-item" v-for="item in hotTopics" :key="item.id" @click="goToTopicDetail(item)">
          <image :src="item.cover" class="topic-cover" mode="aspectFill"></image>
          <view class="topic-info">
            <text class="topic-name">{{ item.name }}</text>
            <text class="topic-count">{{ item.postCount }} 帖子</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="feed-section">
      <view class="section-header">
        <text class="section-title">📝 最新动态</text>
      </view>

      <view class="post-list" v-if="postList.length > 0">
        <view class="post-item" v-for="item in postList" :key="item.id" @click="goToPostDetail(item)">
          <view class="post-header">
            <image :src="item.avatar" class="avatar" mode="aspectFill"></image>
            <view class="post-user">
              <text class="user-name">{{ item.realName }}</text>
              <text class="post-time">{{ formatTime(item.createTime) }}</text>
            </view>
            <view class="post-tag" v-if="item.clubName">{{ item.clubName }}</view>
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
              <u-icon :name="item.isLiked ? 'heart-fill' : 'heart'" size="20" :color="item.isLiked ? '#f5222d' : '#999'"></u-icon>
              <text :class="{ 'liked': item.isLiked }">{{ item.likeCount }}</text>
            </view>
            <view class="action-item" @click.stop="goToPostDetail(item)">
              <u-icon name="chatbox" size="20" color="#999"></u-icon>
              <text>{{ item.commentCount }}</text>
            </view>
            <view class="action-item">
              <u-icon name="eye" size="20" color="#999"></u-icon>
              <text>{{ item.viewCount }}</text>
            </view>
          </view>
        </view>
      </view>

      <view class="empty" v-else>
        <u-icon name="image" size="80" color="#ddd"></u-icon>
        <view class="empty-text">暂无动态</view>
      </view>
    </view>
  </view>
</template>

<script>
import { getActivityPostList, getHotTopicList, toggleLike, isLiked } from '@/api/square.js'

export default {
  data() {
    return {
      hotTopics: [],
      postList: [],
      pageNum: 1,
      pageSize: 10
    }
  },
  onShow() {
    this.getHotTopics()
    this.getPostList()
  },
  onPullDownRefresh() {
    this.pageNum = 1
    this.getHotTopics()
    this.getPostList()
    uni.stopPullDownRefresh()
  },
  methods: {
    getHotTopics() {
      getHotTopicList().then(res => {
        this.hotTopics = res || []
      }).catch(err => {
        console.error('获取热门话题失败', err)
        this.hotTopics = this.getMockTopics()
      })
    },
    getPostList() {
      getActivityPostList({ pageNum: this.pageNum, pageSize: this.pageSize }).then(res => {
        this.postList = res.list || []
        this.postList.forEach(item => {
          this.checkLikeStatus(item)
        })
      }).catch(err => {
        console.error('获取动态列表失败', err)
        this.postList = this.getMockPosts()
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
      const user = { userId, username: 'zhangsan' }
      toggleLike({ businessType: 0, businessId: item.id, ...user }).then(() => {
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
    goToActivity() {
      uni.navigateTo({ url: '/pages/square/activity' })
    },
    goToTopic() {
      uni.navigateTo({ url: '/pages/square/topic' })
    },
    goToSenior() {
      uni.navigateTo({ url: '/pages/square/senior' })
    },
    goToTopicDetail(item) {
      uni.navigateTo({ url: `/pages/square/topic-detail?id=${item.id}&name=${item.name}` })
    },
    goToPostDetail(item) {
      uni.navigateTo({ url: `/pages/square/activity-detail?id=${item.id}` })
    },
    publish() {
      uni.navigateTo({ url: '/pages/square/publish-activity' })
    },
    getMockTopics() {
      return [
        { id: 1, name: '#社团生活日常', cover: 'https://picsum.photos/200/200?topic=1', postCount: 128 },
        { id: 2, name: '#编程学习交流', cover: 'https://picsum.photos/200/200?topic=2', postCount: 256 },
        { id: 3, name: '#就业经验分享', cover: 'https://picsum.photos/200/200?topic=3', postCount: 189 },
        { id: 4, name: '#考研交流', cover: 'https://picsum.photos/200/200?topic=4', postCount: 95 }
      ]
    },
    getMockPosts() {
      return [
        {
          id: 1,
          userId: 2,
          realName: '张三',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          clubName: '计算机协会',
          content: '今天参加了编程大赛的培训，收获满满！老师讲了很多实用的算法技巧，期待下次培训。',
          images: 'https://picsum.photos/800/600?random=1,https://picsum.photos/800/600?random=2',
          likeCount: 42,
          commentCount: 12,
          viewCount: 156,
          createTime: '2024-10-15 14:30:00',
          isLiked: false
        },
        {
          id: 2,
          userId: 3,
          realName: '李四',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          clubName: '科技创新社',
          content: '我们的项目进入决赛了！感谢团队的努力，继续加油！💪',
          images: 'https://picsum.photos/800/600?random=3',
          likeCount: 86,
          commentCount: 23,
          viewCount: 324,
          createTime: '2024-10-15 12:00:00',
          isLiked: true
        },
        {
          id: 3,
          userId: 4,
          realName: '王五',
          avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4',
          clubName: '计算机协会',
          content: '周末的户外拓展活动太好玩了，大家玩得很开心，增进了友谊！',
          images: 'https://picsum.photos/800/600?random=4,https://picsum.photos/800/600?random=5,https://picsum.photos/800/600?random=6',
          likeCount: 58,
          commentCount: 15,
          viewCount: 234,
          createTime: '2024-10-14 20:00:00',
          isLiked: false
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.square-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 60rpx 40rpx 80rpx;
  color: #fff;

  .header-title {
    font-size: 48rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
  }

  .header-subtitle {
    font-size: 28rpx;
    opacity: 0.9;
  }
}

.quick-nav {
  display: flex;
  justify-content: space-around;
  background: #fff;
  margin: -40rpx 30rpx 30rpx;
  border-radius: 24rpx;
  padding: 40rpx 20rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .nav-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    .nav-icon {
      width: 100rpx;
      height: 100rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
    }

    .nav-text {
      font-size: 26rpx;
      color: #333;
    }
  }
}

.hot-topics {
  padding: 0 30rpx 30rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 26rpx;
      color: #4A90E2;
    }
  }

  .topic-scroll {
    white-space: nowrap;
    margin: 0 -30rpx;
    padding: 0 30rpx;

    .topic-item {
      display: inline-block;
      width: 240rpx;
      margin-right: 20rpx;
      background: #fff;
      border-radius: 16rpx;
      overflow: hidden;
      vertical-align: top;

      .topic-cover {
        width: 100%;
        height: 160rpx;
        background: #f0f0f0;
      }

      .topic-info {
        padding: 16rpx;

        .topic-name {
          font-size: 26rpx;
          color: #333;
          font-weight: 500;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          margin-bottom: 6rpx;
        }

        .topic-count {
          font-size: 22rpx;
          color: #999;
        }
      }
    }
  }
}

.feed-section {
  padding: 0 30rpx;

  .section-header {
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
    }
  }

  .post-list {
    .post-item {
      background: #fff;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 20rpx;
      box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

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
          gap: 8rpx;
          font-size: 24rpx;
          color: #666;

          text.liked {
            color: #f5222d;
          }
        }
      }
    }
  }
}

.empty {
  background: #fff;
  border-radius: 20rpx;
  padding: 80rpx 40rpx;
  text-align: center;

  .empty-text {
    margin-top: 20rpx;
    font-size: 28rpx;
    color: #999;
  }
}
</style>
