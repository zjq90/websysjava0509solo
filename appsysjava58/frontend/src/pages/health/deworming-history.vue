<template>
  <view class="container">
    <view class="add-btn" @click="showAddModal = true">
      <text class="add-icon">+</text>
      <text class="add-text">添加驱虫记录</text>
    </view>

    <view class="history-list" v-if="records.length > 0">
      <view class="history-item" v-for="item in records" :key="item.id">
        <view class="item-header">
          <view class="date-badge">
            <text class="date-text">{{ formatDate(item.dewormingDate) }}</text>
          </view>
          <view class="status-badge" :class="'status-' + (item.completed ? 'completed' : 'pending')">
            {{ item.completed ? '已完成' : '待完成' }}
          </view>
        </view>
        <view class="item-body">
          <view class="info-row">
            <text class="label">药品名称:</text>
            <text class="value">{{ item.medicineName }}</text>
          </view>
          <view class="info-row" v-if="item.dosage">
            <text class="label">用量:</text>
            <text class="value">{{ item.dosage }}</text>
          </view>
          <view class="info-row" v-if="item.nextDewormingDate">
            <text class="label">下次驱虫:</text>
            <text class="value highlight">{{ formatDate(item.nextDewormingDate) }}</text>
          </view>
          <view class="info-row" v-if="item.notes">
            <text class="label">备注:</text>
            <text class="value">{{ item.notes }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="empty-state" v-else>
      <text class="empty-icon">📭</text>
      <text class="empty-text">暂无驱虫记录</text>
    </view>

    <view class="modal" v-if="showAddModal" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <text class="modal-title">添加驱虫记录</text>
        <view class="form-item">
          <text class="form-label">驱虫日期</text>
          <input type="text" class="form-input" v-model="newRecord.dewormingDate" placeholder="请选择日期" />
        </view>
        <view class="form-item">
          <text class="form-label">药品名称</text>
          <input type="text" class="form-input" v-model="newRecord.medicineName" placeholder="如：福来恩滴剂" />
        </view>
        <view class="form-item">
          <text class="form-label">用量</text>
          <input type="text" class="form-input" v-model="newRecord.dosage" placeholder="如：1支" />
        </view>
        <view class="form-item">
          <text class="form-label">下次驱虫日期</text>
          <input type="text" class="form-input" v-model="newRecord.nextDewormingDate" placeholder="请选择日期" />
        </view>
        <view class="form-actions">
          <button class="btn-cancel" @click="showAddModal = false">取消</button>
          <button class="btn-confirm" @click="addRecord">确认添加</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      records: [],
      showAddModal: false,
      newRecord: {
        dewormingDate: '',
        medicineName: '',
        dosage: '',
        nextDewormingDate: '',
        notes: ''
      }
    }
  },
  onLoad() {
    this.loadRecords()
  },
  methods: {
    loadRecords() {
      this.$request('/health/deworming/history/1').then(res => {
        if (res.code === 200) {
          this.records = res.data
        }
      }).catch(() => {
        this.records = [
          {
            id: 1,
            dewormingDate: '2024-01-15',
            medicineName: '福来恩滴剂',
            dosage: '1支',
            nextDewormingDate: '2024-02-15',
            completed: true,
            notes: '体外驱虫，无不良反应'
          },
          {
            id: 2,
            dewormingDate: '2023-10-15',
            medicineName: '拜宠清',
            dosage: '1片',
            completed: true,
            notes: '上次体内驱虫记录'
          }
        ]
      })
    },
    formatDate(date) {
      return date
    },
    addRecord() {
      if (!this.newRecord.medicineName) {
        this.$showToast('请填写药品名称')
        return
      }
      this.$request('/health/deworming', 'POST', this.newRecord).then(res => {
        if (res.code === 200) {
          this.$showToast('添加成功', 'success')
          this.showAddModal = false
          this.loadRecords()
        }
      }).catch(() => {
        this.records.unshift({
          id: Date.now(),
          ...this.newRecord,
          completed: true
        })
        this.$showToast('添加成功', 'success')
        this.showAddModal = false
      })
    }
  }
}
</script>

<style scoped>
.add-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 24rpx;
  border-radius: 50rpx;
  margin-bottom: 30rpx;
}

.add-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.add-text {
  font-size: 28rpx;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.history-item {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.08);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx 30rpx;
  background: #fafafa;
  border-bottom: 1rpx solid #f0f0f0;
}

.date-badge {
  background: #e6f7ff;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.date-text {
  font-size: 24rpx;
  color: #1890ff;
}

.status-badge {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
}

.status-completed {
  background: #f6ffed;
  color: #52c41a;
}

.status-pending {
  background: #fffbe6;
  color: #faad14;
}

.item-body {
  padding: 25rpx 30rpx;
}

.info-row {
  display: flex;
  margin-bottom: 15rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  width: 140rpx;
  font-size: 26rpx;
  color: #999;
}

.value {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}

.value.highlight {
  color: #667eea;
  font-weight: bold;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-content {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
}

.modal-title {
  display: block;
  text-align: center;
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.form-actions {
  display: flex;
  gap: 20rpx;
  margin-top: 40rpx;
}

.btn-cancel,
.btn-confirm {
  flex: 1;
  height: 80rpx;
  border-radius: 40rpx;
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
</style>