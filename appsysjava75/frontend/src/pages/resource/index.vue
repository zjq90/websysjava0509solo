<template>
  <view class="resource-page">
    <view class="header">
      <view class="header-title">资源共享</view>
      <view class="header-subtitle">知识共享，共同成长</view>
    </view>

    <view class="quick-nav">
      <view class="nav-item" @click="goToShare">
        <view class="nav-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <u-icon name="folder" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">资料分享</text>
      </view>
      <view class="nav-item" @click="goToCooperation">
        <view class="nav-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <u-icon name="building" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">校企对接</text>
      </view>
      <view class="nav-item" @click="goToUpload">
        <view class="nav-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <u-icon name="cloud-upload" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">上传资源</text>
      </view>
      <view class="nav-item" @click="goToMyResources">
        <view class="nav-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
          <u-icon name="heart" size="32" color="#fff"></u-icon>
        </view>
        <text class="nav-text">我的收藏</text>
      </view>
    </view>

    <view class="category-section">
      <view class="section-header">
        <text class="section-title">资源分类</text>
      </view>
      <view class="category-grid">
        <view class="category-item" v-for="(item, index) in categories" :key="index" @click="filterByCategory(item.value)">
          <view class="category-icon" :style="{ background: item.gradient }">
            <u-icon :name="item.icon" size="28" color="#fff"></u-icon>
          </view>
          <text class="category-name">{{ item.name }}</text>
          <text class="category-count">{{ item.count }} 份</text>
        </view>
      </view>
    </view>

    <view class="hot-resources">
      <view class="section-header">
        <text class="section-title">🔥 热门资源</text>
        <text class="section-more" @click="goToShare">更多</text>
      </view>

      <view class="resource-list">
        <view class="resource-item" v-for="item in hotResources" :key="item.id" @click="goToDetail(item)">
          <image :src="item.cover" class="resource-cover" mode="aspectFill"></image>
          <view class="resource-info">
            <view class="resource-title">{{ item.title }}</view>
            <view class="resource-meta">
              <text class="tag">{{ item.categoryName }}</text>
              <text class="type-tag">{{ item.fileFormat?.toUpperCase() }}</text>
            </view>
            <view class="resource-footer">
              <view class="uploader">
                <image :src="item.uploaderAvatar" class="avatar-small" mode="aspectFill"></image>
                <text class="uploader-name">{{ item.uploaderName }}</text>
              </view>
              <view class="stats">
                <u-icon name="download" size="14" color="#999"></u-icon>
                <text>{{ item.downloadCount }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="cooperation-banner" @click="goToCooperation">
      <view class="banner-content">
        <view class="banner-title">校企对接</view>
        <view class="banner-desc">实习机会、企业赞助、合作项目</view>
        <view class="banner-btn">立即查看</view>
      </view>
      <view class="banner-icon">
        <u-icon name="arrow-right" size="32" color="#fff"></u-icon>
      </view>
    </view>
  </view>
</template>

<script>
import { getResourceList } from '@/api/resource.js'
import { RESOURCE_CATEGORY_NAME } from '@/utils/constants.js'

export default {
  data() {
    return {
      categories: [
        { name: '学习资料', value: 0, icon: 'book', count: 128, gradient: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { name: '比赛经验', value: 1, icon: 'trophy', count: 56, gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { name: '技能教程', value: 2, icon: 'video', count: 89, gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { name: '工具软件', value: 3, icon: 'gear', count: 45, gradient: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
        { name: '其他', value: 4, icon: 'grid', count: 34, gradient: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' }
      ],
      hotResources: []
    }
  },
  onShow() {
    this.getHotResources()
  },
  methods: {
    getHotResources() {
      getResourceList({ pageNum: 1, pageSize: 5 }).then(res => {
        this.hotResources = res.list || []
      }).catch(err => {
        console.error('获取热门资源失败', err)
        this.hotResources = this.getMockData()
      })
    },
    filterByCategory(category) {
      uni.navigateTo({
        url: `/pages/resource/share?category=${category}`
      })
    },
    goToShare() {
      uni.navigateTo({ url: '/pages/resource/share' })
    },
    goToCooperation() {
      uni.navigateTo({ url: '/pages/resource/cooperation' })
    },
    goToUpload() {
      uni.navigateTo({ url: '/pages/resource/upload' })
    },
    goToMyResources() {
      uni.showToast({ title: '功能开发中', icon: 'none' })
    },
    goToDetail(item) {
      uni.navigateTo({ url: `/pages/resource/share-detail?id=${item.id}` })
    },
    getMockData() {
      return [
        {
          id: 1,
          title: 'Java面试宝典2024版',
          cover: 'https://picsum.photos/200/200?res=1',
          categoryName: '学习资料',
          fileFormat: 'pdf',
          uploaderName: '张三',
          uploaderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=2',
          downloadCount: 328
        },
        {
          id: 2,
          title: 'Python数据分析实战教程',
          cover: 'https://picsum.photos/200/200?res=2',
          categoryName: '技能教程',
          fileFormat: 'mp4',
          uploaderName: '李四',
          uploaderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=3',
          downloadCount: 256
        },
        {
          id: 3,
          title: '算法竞赛入门指南',
          cover: 'https://picsum.photos/200/200?res=3',
          categoryName: '比赛经验',
          fileFormat: 'pdf',
          uploaderName: '王五',
          uploaderAvatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=4',
          downloadCount: 189
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-page {
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

.category-section {
  margin: 0 30rpx 30rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .section-header {
    margin-bottom: 24rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
    }
  }

  .category-grid {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .category-item {
      width: 31%;
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-bottom: 20rpx;

      .category-icon {
        width: 88rpx;
        height: 88rpx;
        border-radius: 20rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12rpx;
      }

      .category-name {
        font-size: 26rpx;
        color: #333;
        margin-bottom: 4rpx;
      }

      .category-count {
        font-size: 22rpx;
        color: #999;
      }
    }
  }
}

.hot-resources {
  margin: 0 30rpx 30rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;
    padding: 0 10rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
    }

    .section-more {
      font-size: 26rpx;
      color: #4A90E2;
    }
  }

  .resource-list {
    .resource-item {
      display: flex;
      background: #fff;
      border-radius: 16rpx;
      padding: 20rpx;
      margin-bottom: 16rpx;
      box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

      .resource-cover {
        width: 160rpx;
        height: 160rpx;
        border-radius: 12rpx;
        margin-right: 20rpx;
        background: #f0f0f0;
        flex-shrink: 0;
      }

      .resource-info {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .resource-title {
          font-size: 28rpx;
          color: #333;
          font-weight: 500;
          margin-bottom: 12rpx;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .resource-meta {
          display: flex;
          gap: 10rpx;
          margin-bottom: 12rpx;

          .tag {
            font-size: 20rpx;
            padding: 4rpx 12rpx;
            border-radius: 8rpx;
            background: #e6f7ff;
            color: #4A90E2;
          }

          .type-tag {
            font-size: 20rpx;
            padding: 4rpx 12rpx;
            border-radius: 8rpx;
            background: #f6ffed;
            color: #52c41a;
            font-weight: bold;
          }
        }

        .resource-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .uploader {
            display: flex;
            align-items: center;
            gap: 8rpx;

            .avatar-small {
              width: 40rpx;
              height: 40rpx;
              border-radius: 50%;
              background: #eee;
            }

            .uploader-name {
              font-size: 22rpx;
              color: #666;
            }
          }

          .stats {
            display: flex;
            align-items: center;
            gap: 6rpx;
            font-size: 22rpx;
            color: #999;
          }
        }
      }
    }
  }
}

.cooperation-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin: 0 30rpx;
  padding: 40rpx;
  border-radius: 20rpx;
  color: #fff;

  .banner-content {
    .banner-title {
      font-size: 32rpx;
      font-weight: bold;
      margin-bottom: 8rpx;
    }

    .banner-desc {
      font-size: 24rpx;
      opacity: 0.9;
      margin-bottom: 16rpx;
    }

    .banner-btn {
      display: inline-block;
      background: rgba(255, 255, 255, 0.2);
      padding: 10rpx 24rpx;
      border-radius: 30rpx;
      font-size: 24rpx;
    }
  }

  .banner-icon {
    width: 64rpx;
    height: 64rpx;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
