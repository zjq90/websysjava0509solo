<template>
  <view class="fund-page">
    <view class="header">
      <view class="header-title">经费管理</view>
      <view class="header-subtitle">透明公开，规范管理</view>
    </view>

    <view class="summary-card">
      <view class="summary-row">
        <view class="summary-item">
          <text class="label">当前余额</text>
          <text class="value balance">¥{{ summary.balance || '0.00' }}</text>
        </view>
      </view>
      <view class="summary-row">
        <view class="summary-item">
          <text class="label">总收入</text>
          <text class="value income">¥{{ summary.totalIncome || '0.00' }}</text>
        </view>
        <view class="divider"></view>
        <view class="summary-item">
          <text class="label">总支出</text>
          <text class="value expense">¥{{ summary.totalExpense || '0.00' }}</text>
        </view>
      </view>
    </view>

    <view class="quick-access">
      <view class="quick-item" @click="goToRecord">
        <view class="quick-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
          <u-icon name="list" size="28" color="#fff"></u-icon>
        </view>
        <text class="quick-text">经费流水</text>
      </view>
      <view class="quick-item" @click="goToAddRecord">
        <view class="quick-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
          <u-icon name="plus-circle" size="28" color="#fff"></u-icon>
        </view>
        <text class="quick-text">记一笔</text>
      </view>
      <view class="quick-item" @click="goToReimbursement">
        <view class="quick-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
          <u-icon name="file-text" size="28" color="#fff"></u-icon>
        </view>
        <text class="quick-text">报销申请</text>
      </view>
      <view class="quick-item" @click="goToStatistics">
        <view class="quick-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
          <u-icon name="chart" size="28" color="#fff"></u-icon>
        </view>
        <text class="quick-text">统计报表</text>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">最近流水</text>
        <text class="section-more" @click="goToRecord">查看全部</text>
      </view>

      <view class="record-list" v-if="recentRecords.length > 0">
        <view class="record-item" v-for="item in recentRecords" :key="item.id" @click="goToRecordDetail(item)">
          <view class="record-icon" :class="item.type === 1 ? 'income' : 'expense'">
            <u-icon :name="item.type === 1 ? 'arrow-up' : 'arrow-down'" size="24" color="#fff"></u-icon>
          </view>
          <view class="record-info">
            <view class="record-top">
              <text class="record-category">{{ item.categoryName }}</text>
              <text class="record-amount" :class="item.type === 1 ? 'income' : 'expense'">
                {{ item.type === 1 ? '+' : '-' }}¥{{ item.amount?.toFixed(2) }}
              </text>
            </view>
            <view class="record-bottom">
              <text class="record-desc">{{ item.summary }}</text>
              <text class="record-time">{{ formatDate(item.occurDate) }}</text>
            </view>
          </view>
        </view>
      </view>

      <view class="empty" v-else>
        <u-icon name="list" size="60" color="#ddd"></u-icon>
        <view class="empty-text">暂无流水记录</view>
      </view>
    </view>

    <view class="section">
      <view class="section-header">
        <text class="section-title">报销进度</text>
        <text class="section-more" @click="goToReimbursement">查看全部</text>
      </view>

      <view class="reimburse-list" v-if="recentReimburses.length > 0">
        <view class="reimburse-item" v-for="item in recentReimburses" :key="item.id" @click="goToReimburseDetail(item)">
          <view class="reimburse-info">
            <text class="reimburse-title">{{ item.categoryName }}</text>
            <text class="reimburse-amount">¥{{ item.amount?.toFixed(2) }}</text>
          </view>
          <view class="reimburse-status" :class="'status-' + item.status">
            {{ getStatusName(item.status) }}
          </view>
        </view>
      </view>

      <view class="empty" v-else>
        <u-icon name="file-text" size="60" color="#ddd"></u-icon>
        <view class="empty-text">暂无报销申请</view>
      </view>
    </view>
  </view>
</template>

<script>
import { getFundSummary, getFundRecordList, getMyReimbursementList } from '@/api/fund.js'
import { REIMBURSEMENT_STATUS_NAME } from '@/utils/constants.js'

export default {
  data() {
    return {
      summary: {},
      recentRecords: [],
      recentReimburses: [],
      clubId: 1
    }
  },
  onShow() {
    this.clubId = uni.getStorageSync('clubId') || 1
    this.getSummary()
    this.getRecentRecords()
    this.getRecentReimburses()
  },
  methods: {
    getSummary() {
      getFundSummary(this.clubId).then(res => {
        this.summary = res || {}
      }).catch(err => {
        console.error('获取经费汇总失败', err)
        this.summary = { balance: 5200, totalIncome: 18000, totalExpense: 12800 }
      })
    },
    getRecentRecords() {
      getFundRecordList({ clubId: this.clubId, pageNum: 1, pageSize: 5 }).then(res => {
        this.recentRecords = res.list || []
      }).catch(err => {
        console.error('获取经费记录失败', err)
        this.recentRecords = this.getMockRecords()
      })
    },
    getRecentReimburses() {
      const userId = uni.getStorageSync('userId') || 2
      getMyReimbursementList({ applicantId: userId, pageNum: 1, pageSize: 3 }).then(res => {
        this.recentReimburses = res.list || []
      }).catch(err => {
        console.error('获取报销申请失败', err)
        this.recentReimburses = this.getMockReimburses()
      })
    },
    getStatusName(status) {
      return REIMBURSEMENT_STATUS_NAME[status] || '未知'
    },
    formatDate(date) {
      if (!date) return ''
      return date.substring(5)
    },
    goToRecord() {
      uni.navigateTo({ url: '/pages/fund/record' })
    },
    goToAddRecord() {
      uni.navigateTo({ url: '/pages/fund/add-record' })
    },
    goToReimbursement() {
      uni.navigateTo({ url: '/pages/fund/reimbursement' })
    },
    goToStatistics() {
      uni.navigateTo({ url: '/pages/fund/statistics' })
    },
    goToRecordDetail(item) {
      uni.navigateTo({ url: `/pages/fund/record-detail?id=${item.id}` })
    },
    goToReimburseDetail(item) {
      uni.navigateTo({ url: `/pages/fund/reimbursement-detail?id=${item.id}` })
    },
    getMockRecords() {
      return [
        { id: 1, type: 1, categoryName: '会费', amount: 5000, summary: '2024年度会员会费收入', occurDate: '2024-09-01' },
        { id: 2, type: 1, categoryName: '赞助', amount: 3000, summary: '科技公司赞助', occurDate: '2024-09-10' },
        { id: 3, type: 0, categoryName: '活动物料', amount: 1500, summary: '编程大赛物资采购', occurDate: '2024-09-15' },
        { id: 4, type: 0, categoryName: '场地费', amount: 800, summary: '活动场地租赁费用', occurDate: '2024-09-20' }
      ]
    },
    getMockReimburses() {
      return [
        { id: 1, categoryName: '活动物料', amount: 500, status: 0 },
        { id: 2, categoryName: '宣传费用', amount: 300, status: 1 },
        { id: 3, categoryName: '差旅费用', amount: 200, status: 2 }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.fund-page {
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

.summary-card {
  background: #fff;
  margin: -40rpx 30rpx 30rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .summary-row {
    display: flex;
    align-items: center;

    &:first-child {
      border-bottom: 1rpx solid #f0f0f0;
      padding-bottom: 30rpx;
      margin-bottom: 30rpx;
    }

    .summary-item {
      flex: 1;
      text-align: center;

      .label {
        font-size: 26rpx;
        color: #999;
        display: block;
        margin-bottom: 10rpx;
      }

      .value {
        font-size: 36rpx;
        font-weight: bold;

        &.balance {
          color: #333;
          font-size: 48rpx;
        }

        &.income {
          color: #52c41a;
        }

        &.expense {
          color: #f5222d;
        }
      }
    }

    .divider {
      width: 1rpx;
      height: 60rpx;
      background: #f0f0f0;
    }
  }
}

.quick-access {
  display: flex;
  justify-content: space-around;
  background: #fff;
  margin: 0 30rpx 30rpx;
  border-radius: 24rpx;
  padding: 40rpx 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .quick-item {
    display: flex;
    flex-direction: column;
    align-items: center;

    .quick-icon {
      width: 96rpx;
      height: 96rpx;
      border-radius: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
    }

    .quick-text {
      font-size: 26rpx;
      color: #333;
    }
  }
}

.section {
  margin: 0 30rpx 30rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

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

  .record-list {
    .record-item {
      display: flex;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .record-icon {
        width: 72rpx;
        height: 72rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20rpx;

        &.income {
          background: linear-gradient(135deg, #52c41a 0%, #73d13d 100%);
        }

        &.expense {
          background: linear-gradient(135deg, #f5222d 0%, #ff7875 100%);
        }
      }

      .record-info {
        flex: 1;

        .record-top {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8rpx;

          .record-category {
            font-size: 28rpx;
            color: #333;
            font-weight: 500;
          }

          .record-amount {
            font-size: 28rpx;
            font-weight: bold;

            &.income {
              color: #52c41a;
            }

            &.expense {
              color: #f5222d;
            }
          }
        }

        .record-bottom {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .record-desc {
            font-size: 24rpx;
            color: #999;
            flex: 1;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-right: 16rpx;
          }

          .record-time {
            font-size: 22rpx;
            color: #ccc;
            flex-shrink: 0;
          }
        }
      }
    }
  }

  .reimburse-list {
    .reimburse-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .reimburse-info {
        .reimburse-title {
          font-size: 28rpx;
          color: #333;
          display: block;
          margin-bottom: 6rpx;
        }

        .reimburse-amount {
          font-size: 26rpx;
          color: #666;
        }
      }

      .reimburse-status {
        font-size: 24rpx;
        padding: 8rpx 20rpx;
        border-radius: 20rpx;

        &.status-0 {
          background: #fffbe6;
          color: #faad14;
        }

        &.status-1 {
          background: #f6ffed;
          color: #52c41a;
        }

        &.status-2 {
          background: #fff1f0;
          color: #f5222d;
        }

        &.status-3 {
          background: #e6f7ff;
          color: #1890ff;
        }
      }
    }
  }
}

.empty {
  padding: 60rpx 40rpx;
  text-align: center;

  .empty-text {
    margin-top: 16rpx;
    font-size: 26rpx;
    color: #999;
  }
}
</style>
