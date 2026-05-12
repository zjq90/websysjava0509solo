<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <i class="el-icon-s-custom stat-icon user-icon"></i>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <i class="el-icon-s-custom stat-icon patient-icon"></i>
            <div class="stat-info">
              <div class="stat-value">{{ stats.patientCount }}</div>
              <div class="stat-label">患者总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <i class="el-icon-s-order stat-icon order-icon"></i>
            <div class="stat-info">
              <div class="stat-value">{{ stats.todayOrderCount }}</div>
              <div class="stat-label">今日挂号</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <i class="el-icon-s-finance stat-icon finance-icon"></i>
            <div class="stat-info">
              <div class="stat-value">¥{{ stats.todayIncome }}</div>
              <div class="stat-label">今日收入</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card title="系统公告">
          <ul class="notice-list">
            <li v-for="(notice, index) in notices" :key="index">
              <span class="notice-title">{{ notice.title }}</span>
              <span class="notice-time">{{ notice.time }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card title="快捷操作">
          <el-row :gutter="10">
            <el-col :span="12" style="margin-bottom: 10px">
              <el-button type="primary" icon="el-icon-plus" style="width: 100%">新增用户</el-button>
            </el-col>
            <el-col :span="12" style="margin-bottom: 10px">
              <el-button type="success" icon="el-icon-search" style="width: 100%">患者查询</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="warning" icon="el-icon-s-order" style="width: 100%">挂号登记</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="info" icon="el-icon-s-finance" style="width: 100%">收费管理</el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        userCount: 128,
        patientCount: 1568,
        todayOrderCount: 45,
        todayIncome: '12,580.00'
      },
      notices: [
        { title: '系统将于今晚22:00进行维护升级', time: '2024-01-15 09:00' },
        { title: '新增医保结算功能已上线', time: '2024-01-14 14:30' },
        { title: '医生排班信息已更新', time: '2024-01-13 10:00' },
        { title: '药房药品库存预警通知', time: '2024-01-12 16:45' }
      ]
    }
  }
}
</script>

<style scoped>
.stat-card {
  text-align: center;
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-icon {
  font-size: 48px;
  width: 80px;
  height: 80px;
  line-height: 80px;
  border-radius: 50%;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.patient-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
}

.order-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #fff;
}

.finance-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: #fff;
}

.stat-info {
  flex: 1;
  text-align: right;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 5px;
}

.notice-list {
  list-style: none;
  padding: 0;
}

.notice-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #eee;
}

.notice-list li:last-child {
  border-bottom: none;
}

.notice-title {
  color: #333;
  flex: 1;
}

.notice-time {
  color: #999;
  font-size: 12px;
}
</style>
