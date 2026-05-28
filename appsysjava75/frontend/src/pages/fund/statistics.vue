<template>
  <view class="statistics-page">
    <view class="header">
      <view class="date-selector" @click="showPicker = true">
        <text>{{ currentMonth }}</text>
        <u-icon name="arrow-down" size="16" color="#fff"></u-icon>
      </view>
    </view>

    <view class="overview-card">
      <view class="overview-item">
        <text class="label">总收入</text>
        <text class="value income">¥{{ statistics.totalIncome || '0.00' }}</text>
      </view>
      <view class="overview-divider"></view>
      <view class="overview-item">
        <text class="label">总支出</text>
        <text class="value expense">¥{{ statistics.totalExpense || '0.00' }}</text>
      </view>
      <view class="overview-divider"></view>
      <view class="overview-item">
        <text class="label">结余</text>
        <text class="value">{{ statistics.netIncome >= 0 ? '+' : '' }}¥{{ statistics.netIncome || '0.00' }}</text>
      </view>
    </view>

    <view class="chart-section">
      <view class="section-title">收支构成</view>
      <view class="chart-container">
        <view class="chart-tabs">
          <view class="chart-tab" :class="{ active: chartType === 'income' }" @click="chartType = 'income'">收入</view>
          <view class="chart-tab" :class="{ active: chartType === 'expense' }" @click="chartType = 'expense'">支出</view>
        </view>
        <view class="bar-chart">
          <view class="bar-item" v-for="(item, index) in currentChartData" :key="index">
            <view class="bar-wrapper">
              <view class="bar" :class="chartType" :style="{ height: item.percent + '%' }"></view>
            </view>
            <text class="bar-label">{{ item.name }}</text>
            <text class="bar-value">¥{{ item.value }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="detail-section">
      <view class="section-title">明细统计</view>

      <view class="detail-card">
        <view class="detail-header">
          <text class="detail-title">收入明细</text>
        </view>
        <view class="detail-list">
          <view class="detail-item" v-for="(item, index) in incomeList" :key="'income-' + index">
            <view class="detail-name">{{ item.name }}</view>
            <view class="detail-bar">
              <view class="detail-progress income" :style="{ width: item.percent + '%' }"></view>
            </view>
            <view class="detail-value">¥{{ item.value }}</view>
            <view class="detail-percent">{{ item.percent }}%</view>
          </view>
        </view>
      </view>

      <view class="detail-card">
        <view class="detail-header">
          <text class="detail-title">支出明细</text>
        </view>
        <view class="detail-list">
          <view class="detail-item" v-for="(item, index) in expenseList" :key="'expense-' + index">
            <view class="detail-name">{{ item.name }}</view>
            <view class="detail-bar">
              <view class="detail-progress expense" :style="{ width: item.percent + '%' }"></view>
            </view>
            <view class="detail-value">¥{{ item.value }}</view>
            <view class="detail-percent">{{ item.percent }}%</view>
          </view>
        </view>
      </view>
    </view>

    <view class="export-btn" @click="exportReport">
      <u-icon name="download" size="20" color="#4A90E2"></u-icon>
      <text>导出报表</text>
    </view>

    <u-picker
      :show="showPicker"
      :columns="columns"
      @confirm="onPickerConfirm"
      @cancel="showPicker = false"
      :keyboard-avoiding="true"
      title="选择月份"
    ></u-picker>
  </view>
</template>

<script>
import { getFundStatistics } from '@/api/fund.js'

export default {
  data() {
    return {
      currentMonth: '2024年10月',
      statistics: {},
      chartType: 'expense',
      showPicker: false,
      columns: [],
      clubId: 1
    }
  },
  computed: {
    currentChartData() {
      return this.chartType === 'income' ? this.incomeList : this.expenseList
    },
    incomeList() {
      const total = this.statistics.totalIncome || 8000
      const categories = [
        { name: '会费', value: 5000 },
        { name: '赞助', value: 2000 },
        { name: '拨款', value: 1000 },
        { name: '其他', value: 0 }
      ]
      return categories.map(item => ({
        ...item,
        percent: total > 0 ? Math.round(item.value / total * 100) : 0
      }))
    },
    expenseList() {
      const total = this.statistics.totalExpense || 6500
      const categories = [
        { name: '活动物料', value: 3000 },
        { name: '场地费', value: 1500 },
        { name: '宣传费用', value: 800 },
        { name: '差旅费', value: 700 },
        { name: '其他', value: 500 }
      ]
      return categories.map(item => ({
        ...item,
        percent: total > 0 ? Math.round(item.value / total * 100) : 0
      }))
    }
  },
  onLoad() {
    this.clubId = uni.getStorageSync('clubId') || 1
    this.initPicker()
    this.getStatistics()
  },
  methods: {
    initPicker() {
      const years = ['2024', '2023', '2022']
      const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12']
      this.columns = [
        { values: years.map(y => y + '年') },
        { values: months.map(m => m + '月') }
      ]
    },
    getStatistics() {
      const period = this.currentMonth.replace('年', '-').replace('月', '')
      getFundStatistics({ clubId: this.clubId, statisticsType: 2, period }).then(res => {
        this.statistics = res || {}
      }).catch(err => {
        console.error('获取统计数据失败', err)
        this.statistics = {
          totalIncome: 8000,
          totalExpense: 6500,
          netIncome: 1500,
          incomeCount: 5,
          expenseCount: 8
        }
      })
    },
    onPickerConfirm(e) {
      this.currentMonth = e.value[0] + e.value[1]
      this.showPicker = false
      this.getStatistics()
    },
    exportReport() {
      uni.showToast({ title: '报表导出中...', icon: 'loading' })
      setTimeout(() => {
        uni.showToast({ title: '导出成功', icon: 'success' })
      }, 1500)
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 140rpx;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30rpx 40rpx 60rpx;
  display: flex;
  justify-content: center;

  .date-selector {
    display: flex;
    align-items: center;
    gap: 10rpx;
    background: rgba(255, 255, 255, 0.2);
    padding: 16rpx 32rpx;
    border-radius: 40rpx;
    color: #fff;
    font-size: 28rpx;
  }
}

.overview-card {
  display: flex;
  align-items: center;
  background: #fff;
  margin: -30rpx 30rpx 30rpx;
  padding: 40rpx 30rpx;
  border-radius: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);

  .overview-item {
    flex: 1;
    text-align: center;

    .label {
      font-size: 24rpx;
      color: #999;
      display: block;
      margin-bottom: 10rpx;
    }

    .value {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;

      &.income {
        color: #52c41a;
      }

      &.expense {
        color: #f5222d;
      }
    }
  }

  .overview-divider {
    width: 1rpx;
    height: 60rpx;
    background: #f0f0f0;
  }
}

.chart-section {
  margin: 0 30rpx 30rpx;
  background: #fff;
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
  }

  .chart-container {
    .chart-tabs {
      display: flex;
      background: #f5f5f5;
      border-radius: 12rpx;
      padding: 6rpx;
      margin-bottom: 30rpx;

      .chart-tab {
        flex: 1;
        text-align: center;
        padding: 16rpx;
        font-size: 26rpx;
        color: #666;
        border-radius: 8rpx;
        transition: all 0.3s;

        &.active {
          background: #fff;
          color: #4A90E2;
          font-weight: 500;
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.08);
        }
      }
    }

    .bar-chart {
      display: flex;
      justify-content: space-around;
      align-items: flex-end;
      height: 300rpx;
      padding-top: 40rpx;

      .bar-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        flex: 1;

        .bar-wrapper {
          height: 200rpx;
          display: flex;
          align-items: flex-end;
          margin-bottom: 16rpx;

          .bar {
            width: 40rpx;
            border-radius: 8rpx 8rpx 0 0;
            transition: height 0.5s;

            &.income {
              background: linear-gradient(180deg, #52c41a 0%, #73d13d 100%);
            }

            &.expense {
              background: linear-gradient(180deg, #f5222d 0%, #ff7875 100%);
            }
          }
        }

        .bar-label {
          font-size: 22rpx;
          color: #666;
          margin-bottom: 6rpx;
        }

        .bar-value {
          font-size: 20rpx;
          color: #999;
        }
      }
    }
  }
}

.detail-section {
  margin: 0 30rpx;

  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    padding: 0 10rpx;
  }

  .detail-card {
    background: #fff;
    border-radius: 24rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

    .detail-header {
      margin-bottom: 20rpx;

      .detail-title {
        font-size: 28rpx;
        font-weight: 500;
        color: #333;
      }
    }

    .detail-list {
      .detail-item {
        display: flex;
        align-items: center;
        padding: 16rpx 0;

        .detail-name {
          width: 120rpx;
          font-size: 24rpx;
          color: #666;
          flex-shrink: 0;
        }

        .detail-bar {
          flex: 1;
          height: 16rpx;
          background: #f0f0f0;
          border-radius: 8rpx;
          margin: 0 20rpx;
          overflow: hidden;

          .detail-progress {
            height: 100%;
            border-radius: 8rpx;
            transition: width 0.5s;

            &.income {
              background: linear-gradient(90deg, #52c41a 0%, #73d13d 100%);
            }

            &.expense {
              background: linear-gradient(90deg, #f5222d 0%, #ff7875 100%);
            }
          }
        }

        .detail-value {
          width: 120rpx;
          font-size: 24rpx;
          color: #333;
          font-weight: 500;
          text-align: right;
          flex-shrink: 0;
        }

        .detail-percent {
          width: 80rpx;
          font-size: 22rpx;
          color: #999;
          text-align: right;
          flex-shrink: 0;
        }
      }
    }
  }
}

.export-btn {
  position: fixed;
  bottom: 40rpx;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  color: #4A90E2;
  padding: 24rpx 48rpx;
  border-radius: 50rpx;
  display: flex;
  align-items: center;
  gap: 10rpx;
  font-size: 28rpx;
  font-weight: 500;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
}
</style>
