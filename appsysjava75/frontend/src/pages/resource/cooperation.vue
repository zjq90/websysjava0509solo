<template>
  <view class="cooperation-page">
    <view class="tabs">
      <view class="tab-item" v-for="(tab, index) in tabs" :key="index" :class="{ active: activeTab === tab.value }" @click="activeTab = tab.value">
        {{ tab.label }}
        <view class="tab-badge" v-if="tab.count > 0">{{ tab.count }}</view>
      </view>
    </view>

    <view class="filter-bar">
      <view class="search-box">
        <u-icon name="search" size="18" color="#999"></u-icon>
        <input placeholder="搜索职位、企业" v-model="searchKeyword" />
      </view>
    </view>

    <view class="list-container" v-if="filteredList.length > 0">
      <view class="cooperation-item" v-for="item in filteredList" :key="item.id" @click="goDetail(item)">
        <view class="item-header">
          <image :src="item.enterpriseLogo" class="logo" mode="aspectFill"></image>
          <view class="basic-info">
            <view class="title-row">
              <text class="title">{{ item.title }}</text>
              <view class="type-tag" :style="{ background: getTypeColor(item.type) + '20', color: getTypeColor(item.type) }">
                {{ getTypeName(item.type) }}
              </view>
            </view>
            <view class="company">{{ item.enterpriseName }} · {{ item.industry }}</view>
          </view>
        </view>

        <view class="item-content">
          <view class="info-row" v-if="item.salary">
            <u-icon name="wallet" size="16" color="#faad14"></u-icon>
            <text class="salary">{{ item.salary }}</text>
          </view>
          <view class="info-row" v-if="item.sponsorAmount">
            <u-icon name="red-packet" size="16" color="#f5222d"></u-icon>
            <text class="sponsor">赞助金额：¥{{ item.sponsorAmount }}</text>
          </view>
          <view class="info-row">
            <u-icon name="map-pin" size="16" color="#4A90E2"></u-icon>
            <text>{{ item.location || '全国' }}</text>
          </view>
          <view class="info-row">
            <u-icon name="clock" size="16" color="#999"></u-icon>
            <text>截止：{{ item.deadline }}</text>
          </view>
        </view>

        <view class="item-footer">
          <view class="stats">
            <text class="stat-item"><u-icon name="eye" size="14" color="#999"></u-icon> {{ item.viewCount }}</text>
            <text class="stat-item"><u-icon name="users" size="14" color="#999"></u-icon> {{ item.applyCount }}人申请</text>
          </view>
          <button class="apply-btn" @click.stop="goApply(item)">立即申请</button>
        </view>
      </view>
    </view>

    <view class="empty" v-else>
      <u-icon name="building" size="80" color="#ddd"></u-icon>
      <view class="empty-text">暂无相关信息</view>
    </view>

    <view class="publish-btn" @click="goPublish">
      <u-icon name="plus" size="24" color="#fff"></u-icon>
      <text>发布信息</text>
    </view>
  </view>
</template>

<script>
import { getCooperationList } from '@/api/resource.js'
import { COOPERATION_TYPE_NAME, COOPERATION_TYPE_COLOR } from '@/utils/constants.js'

export default {
  data() {
    return {
      activeTab: null,
      searchKeyword: '',
      tabs: [
        { label: '全部', value: null, count: 0 },
        { label: '实习信息', value: 0, count: 2 },
        { label: '赞助信息', value: 1, count: 1 },
        { label: '合作项目', value: 2, count: 1 },
        { label: '校园招聘', value: 3, count: 1 }
      ],
      list: []
    }
  },
  computed: {
    filteredList() {
      let result = this.list
      if (this.activeTab !== null) {
        result = result.filter(item => item.type === this.activeTab)
      }
      if (this.searchKeyword) {
        result = result.filter(item =>
          item.title.includes(this.searchKeyword) ||
          item.enterpriseName.includes(this.searchKeyword)
        )
      }
      return result
    }
  },
  onLoad() {
    this.getList()
  },
  onPullDownRefresh() {
    this.getList()
    uni.stopPullDownRefresh()
  },
  methods: {
    getList() {
      getCooperationList({ type: this.activeTab, pageNum: 1, pageSize: 20 }).then(res => {
        this.list = res.list || []
      }).catch(err => {
        console.error('获取校企对接列表失败', err)
        this.list = this.getMockData()
      })
    },
    getTypeName(type) {
      return COOPERATION_TYPE_NAME[type] || '其他'
    },
    getTypeColor(type) {
      return COOPERATION_TYPE_COLOR[type] || '#999'
    },
    goDetail(item) {
      uni.navigateTo({ url: `/pages/resource/cooperation-detail?id=${item.id}` })
    },
    goApply(item) {
      uni.navigateTo({ url: `/pages/resource/apply-cooperation?id=${item.id}` })
    },
    goPublish() {
      uni.showToast({ title: '请联系管理员发布', icon: 'none' })
    },
    getMockData() {
      return [
        {
          id: 1,
          type: 0,
          title: '字节跳动2025暑期实习招聘',
          enterpriseName: '字节跳动',
          enterpriseLogo: 'https://api.dicebear.com/7.x/icons/svg?seed=bytedance',
          industry: '互联网',
          salary: '200-300/天',
          location: '北京',
          deadline: '2025-06-30',
          viewCount: 568,
          applyCount: 42,
          isRecommend: 1
        },
        {
          id: 2,
          type: 1,
          title: '科技公司活动赞助',
          enterpriseName: '科技创新有限公司',
          enterpriseLogo: 'https://api.dicebear.com/7.x/icons/svg?seed=techco',
          industry: '科技',
          sponsorAmount: 10000,
          location: '全国',
          deadline: '2025-12-31',
          viewCount: 234,
          applyCount: 15,
          isRecommend: 1
        },
        {
          id: 3,
          type: 0,
          title: '腾讯云后端开发实习',
          enterpriseName: '腾讯',
          enterpriseLogo: 'https://api.dicebear.com/7.x/icons/svg?seed=tencent',
          industry: '互联网',
          salary: '250-350/天',
          location: '深圳',
          deadline: '2025-05-31',
          viewCount: 456,
          applyCount: 38,
          isRecommend: 0
        },
        {
          id: 4,
          type: 3,
          title: '阿里巴巴2025校园招聘',
          enterpriseName: '阿里巴巴',
          enterpriseLogo: 'https://api.dicebear.com/7.x/icons/svg?seed=alibaba',
          industry: '互联网',
          salary: '25-35万/年',
          location: '杭州',
          deadline: '2025-04-30',
          viewCount: 892,
          applyCount: 156,
          isRecommend: 1
        },
        {
          id: 5,
          type: 2,
          title: '企业级应用开发合作',
          enterpriseName: '智慧科技股份有限公司',
          enterpriseLogo: 'https://api.dicebear.com/7.x/icons/svg?seed=smart',
          industry: '软件服务',
          location: '北京',
          deadline: '2025-09-30',
          viewCount: 178,
          applyCount: 8,
          isRecommend: 0
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.cooperation-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.tabs {
  display: flex;
  background: #fff;
  padding: 0 10rpx;
  overflow-x: auto;
  white-space: nowrap;

  .tab-item {
    display: inline-flex;
    align-items: center;
    padding: 30rpx 24rpx;
    font-size: 28rpx;
    color: #666;
    position: relative;

    &.active {
      color: #4A90E2;
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 40rpx;
        height: 6rpx;
        background: #4A90E2;
        border-radius: 3rpx;
      }
    }

    .tab-badge {
      background: #f5222d;
      color: #fff;
      font-size: 20rpx;
      min-width: 32rpx;
      height: 32rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-left: 8rpx;
      padding: 0 8rpx;
    }
  }
}

.filter-bar {
  padding: 20rpx;
  background: #fff;

  .search-box {
    display: flex;
    align-items: center;
    background: #f5f5f5;
    border-radius: 40rpx;
    padding: 16rpx 24rpx;
    gap: 12rpx;

    input {
      flex: 1;
      font-size: 26rpx;
    }
  }
}

.list-container {
  padding: 20rpx;

  .cooperation-item {
    background: #fff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

    .item-header {
      display: flex;
      margin-bottom: 20rpx;

      .logo {
        width: 96rpx;
        height: 96rpx;
        border-radius: 16rpx;
        margin-right: 20rpx;
        background: #f0f0f0;
        flex-shrink: 0;
      }

      .basic-info {
        flex: 1;

        .title-row {
          display: flex;
          align-items: center;
          gap: 12rpx;
          margin-bottom: 8rpx;

          .title {
            font-size: 30rpx;
            font-weight: bold;
            color: #333;
            flex: 1;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .type-tag {
            font-size: 20rpx;
            padding: 4rpx 12rpx;
            border-radius: 8rpx;
            flex-shrink: 0;
          }
        }

        .company {
          font-size: 24rpx;
          color: #666;
        }
      }
    }

    .item-content {
      background: #fafafa;
      border-radius: 12rpx;
      padding: 20rpx;
      margin-bottom: 20rpx;

      .info-row {
        display: flex;
        align-items: center;
        gap: 10rpx;
        margin-bottom: 12rpx;
        font-size: 24rpx;
        color: #666;

        &:last-child {
          margin-bottom: 0;
        }

        .salary {
          color: #faad14;
          font-weight: bold;
        }

        .sponsor {
          color: #f5222d;
          font-weight: bold;
        }
      }
    }

    .item-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .stats {
        display: flex;
        gap: 24rpx;

        .stat-item {
          display: flex;
          align-items: center;
          gap: 6rpx;
          font-size: 22rpx;
          color: #999;
        }
      }

      .apply-btn {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #fff;
        font-size: 26rpx;
        padding: 14rpx 36rpx;
        border-radius: 40rpx;
        border: none;
        line-height: 1.2;
      }
    }
  }
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
